package com.debateseason.extractor.scheduler.extractor;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.debateseason.extractor.media.dto.ExtractedWords;
import com.debateseason.extractor.media.manager.MediaManager;
import com.debateseason.extractor.media.processed.entity.MediaEntity;
import com.debateseason.extractor.media.processed.service.MediaApplicationService;

import com.debateseason.extractor.media.common.ProcessingStage;
import com.debateseason.extractor.media.processed.service.MediaService;
import com.debateseason.extractor.media.raw.entity.ErrorEntity;
import com.debateseason.extractor.media.raw.service.ErrorService;
import com.debateseason.extractor.prompt.PromptBuilder;
import com.debateseason.extractor.prompt.PromptInput;
import com.debateseason.extractor.media.raw.entity.RawMediaEntity;
import com.debateseason.extractor.media.raw.service.RawMediaService;
import com.debateseason.extractor.llm.LlmRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
@Component
public class ExtractScheduler {

	private final MediaApplicationService mediaApplicationService;

	private final MediaService mediaService;
	private final RawMediaService rawMediaService;
	private final ErrorService errorService;

	private final MediaManager mediaManager;

	private final ObjectMapper objectMapper;

	private final int size = 50;

	// 병렬 처리
	private final ExecutorService executorService = Executors.newFixedThreadPool(4); // 6 ->3 코어

	private final BlockingQueue<RawMediaEntity> queue = new ArrayBlockingQueue<>(size);

	private final AtomicInteger taskCount = new AtomicInteger(0);
	private int target = 0;
	private boolean lock = false;


	@Scheduled(fixedDelay = 30000)
	public void extractKeyword(){

		// 원시 데이터 불러오기.
		List<RawMediaEntity> rawMediaEntityList = rawMediaService.findOldestUnCheckedMediaOrderByIdAsc(size);

		if(target==0){ // target=0만 수행 이후 종료가 될 경우, 하위 로직을 실행할 수 없다. 따라서 이를 해결 하기 위한 보상 로직.
			taskCount.set(0);
			lock = false;
		}

		if(!lock){ // 작업 초기화

			// 전체 할당량
			target = rawMediaEntityList.size();

			// Queue를 사용하지 않을 경우, 어디까지 작업 수행을 완료했는지 매번 인덱스를 기록해야 하는 번거로움이 있다.
			queue.addAll(rawMediaEntityList);
			
			lock=true;
		}

		// queue에 있는 것들 consume
		if(taskCount.get()!=target){ // 현재 완료한 작업 건수가 전체 할당량에 미치지 못할 경우, 남은 작업을 계속 수행한다.

			while(!queue.isEmpty()){

				try {

					RawMediaEntity item = queue.take();

					executorService.execute(() -> {

						Long crawlId = item.getId();

						try {

							String title = item.getTitle();

							PromptInput promptInput = PromptInput.builder()
								.title(title)
								.build();

							if(!mediaService.existsByTitle(title)){// 새로 시작을 한다.

								ExtractedWords extractedWords = sendRawMediaToLLM(crawlId,promptInput); // 추출된 고유명사 + 일반명사
								MediaEntity mediaEntity = mediaManager.convertRawMediaToMedia(item); //

								// MediaEntity, MediaKeywordEntity, KeywordEntity 1개의 트랜잭션으로 관리를 한다.
								mediaApplicationService.saveMediaAndKeywords(mediaEntity, extractedWords);

								// rawMedia의 checked를 업데이트를 한다.
								rawMediaService.updateRawMediaToChecked(crawlId);
								log.info(crawlId+" is first created");

							}
							else{ // 이미 있다. -> rawMedia의 checked만 업데이트를 한다.
								rawMediaService.updateRawMediaToChecked(crawlId);
								log.info(crawlId+" is secondary updated");
							}


						}
						catch (Exception e) {

							ErrorEntity errorEntity = ErrorEntity.builder()
								.crawlId(crawlId)
								.message(e.getMessage())
								.processingStage(ProcessingStage.KEYWORDS_EXTRACTED)
								.build();

							errorService.save(errorEntity);
						}
						finally {
							// +1
							taskCount.incrementAndGet();
						}
					});
				} catch (InterruptedException e) {

					// 더이상 가져올 것이 없는 경우
					throw new RuntimeException(e);
				}

			}
		}
		else{ //target = count이면, count를 갱신하자.
			target = 0;
			taskCount.set(0);
			lock = false;
		}

	}

	// 2. LLM으로 요청보내기
	public ExtractedWords sendRawMediaToLLM(Long crawlId,PromptInput input){

		List<String> nouns = new ArrayList<>();// 고유명사 + 일반명사

		try{
			PromptBuilder promptBuilder = PromptBuilder.builder()
				.promptInput(input)
				.build();

			String prompt = promptBuilder.getPrompt();

			LlmRequest request = new LlmRequest(prompt);
			String jsonRequest = objectMapper.writeValueAsString(request);

			HttpClient client = HttpClient.newHttpClient();

			// LLM에 API 요청 보내기
			HttpRequest llmRequest = HttpRequest.newBuilder()
				.uri(new URI("http://localhost:11434/api/generate")) // LLM 포트
				.header("Content-Type","application/json")
				.POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
				.build();

			HttpResponse<String> llmResponse = client.send(llmRequest,HttpResponse.BodyHandlers.ofString());

			String result = llmResponse.body();

			JsonNode responseNode = objectMapper.readTree(result);
			String jsonResponse = responseNode.get("response").asText();

			// 1. ```json 블록 추출
			Pattern pattern = Pattern.compile("```json\\s*(\\{.*?})\\s*```", Pattern.DOTALL);
			Matcher matcher = pattern.matcher(jsonResponse);

			if (matcher.find()) {
				String jsonResult = matcher.group(1);

				// 주석 제거
				jsonResult = jsonResult
					.replaceAll("//.*", "")         // // 주석 제거
					.replaceAll("<!--.*?-->", "");  // HTML 주석 제거

				JsonNode rootNode = objectMapper.readTree(jsonResult);

				// 최종결과 노드
				JsonNode finalResult = rootNode.path("최종결과");

				// 고유 명사는 찾았는데, 일반 명사를 못찾아서 에러를 터트릴 수 있다. 그래서 일부만 저장이 된다.
				// 정상 저장이 된 것처럼 보인다.

				// 고유명사
				for (JsonNode node : finalResult.path("고유명사")) {
					nouns.add(node.asText());
				}

				// 일반명사
				for (JsonNode node : finalResult.path("일반명사")) {
					nouns.add(node.asText());
				}
			}
		}
		catch (Exception e){ // 문제가 주로 발생하는 부분.

			ErrorEntity errorEntity = ErrorEntity.builder()
				.crawlId(crawlId)
				.message(e.getMessage())
				.processingStage(ProcessingStage.LLM_REQUESTED)
				.build();

			errorService.save(errorEntity);

		}
		// nouns는 empty or not
		return ExtractedWords.builder()
			.nouns(nouns) // 만약 아무런 명사가 없다면, 빈 배열을 전송한다.
			.build();
	}
}
