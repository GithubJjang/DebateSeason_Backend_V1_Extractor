package com.debateseason.extractor.scheduler.collector;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.debateseason.extractor.scheduler.common.ApiResponse;
import com.debateseason.extractor.media.manager.MediaManager;
import com.debateseason.extractor.media.raw.response.RawMediaResponse;
import com.debateseason.extractor.media.raw.service.RawMediaService;
import com.debateseason.extractor.media.raw.entity.RawMediaEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
@Component
public class CollectorScheduler { // 가져오는 것은 하나의 작업이니 비동기는 아니고, 후처리만 비동기

	private final RawMediaService rawMediaService;
	private final MediaManager mediaManager;

	private final ObjectMapper objectMapper;

	@Scheduled(fixedDelay = 3000)
	public void harvestRawCrawledData(){ // Raw 크롤링 데이터를 긁어온다.

		// PK 내림차순으로 정렬(최신 날짜 순으로 정렬) -> unmarked로 필터링 -> 10개 가져오기
		try {

			// DB에 저장된 마지막 포인터 가져오기. (항상 초기에는 없기 때문에 빈 배열 []을 전송한다.)
			Long lastId = rawMediaService.findLastIdOrderByIdDesc();

			String url = (lastId != null) ? "http://localhost:8081/media?lastId="+lastId : "http://localhost:8081/media";

			// 1. 크롤러로 Get 요청 보내기.
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.GET()
				.build()
				;
			HttpResponse<String> resCrawlerData = client.send(request,HttpResponse.BodyHandlers.ofString());

			// 2. JSON 파싱
			String body = resCrawlerData.body();

			//
			if(!body.isEmpty() || !body.isBlank()){ // 가져올 데이터(body)가 있는 경우

				// 응답 데이터를 역직렬화를 한다.
				ApiResponse<List<RawMediaResponse>> response =
					objectMapper.readValue(
						body,
						new TypeReference<>() {
						}
					);

				List<RawMediaResponse> responseList = response.getData();

				if(!responseList.isEmpty()){
					// 빈 배열이 아니면, checked를 해서 DB에 저장을 한다.
					for(RawMediaResponse m : responseList){
						RawMediaEntity rawMediaEntity = mediaManager.createRawMediaEntity(m);
						rawMediaService.save(rawMediaEntity);
					}
				}
			}
			else{
				log.info("크롤러로부터 받은 데이터가 누락되었습니다.");
			}
		}
		catch (URISyntaxException | IOException | InterruptedException e) {
			log.error("크롤러와 연결이 끊어졌습니다.");
		}

	}

}
