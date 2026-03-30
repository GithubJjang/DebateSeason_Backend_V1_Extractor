package com.debateseason.extractor.media.processed.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.debateseason.extractor.media.dto.ExtractedWords;
import com.debateseason.extractor.media.processed.entity.KeywordEntity;
import com.debateseason.extractor.media.processed.entity.MediaEntity;
import com.debateseason.extractor.media.processed.entity.MediaKeyWordEntity;
import com.debateseason.extractor.media.raw.service.RawMediaService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MediaApplicationService { // Facade패턴

	private final MediaService mediaService;
	private final MediaKeywordService mediaKeywordService;
	private final KeywordService keywordService;

	// 1. Media, MediaKeyword, Keyword를 하나의 트랜잭션으로 관리를 한다.
	@Transactional
	public void saveMediaAndKeywords(MediaEntity mediaEntity, ExtractedWords extractedWords) { // 예외를 던짐

		// 1. MediaEntity 저장하기
		mediaService.save(mediaEntity);

		// 2. 추출한 명사에서 KeywordEntity 생성하기
		List<String> nouns = extractedWords.getNouns();// 키워드가 있다면,

		if (!nouns.isEmpty()) { // 내부 요소가 있다면,

			List<KeywordEntity> keywordEntityList = new ArrayList<>();

			for (String s : nouns) {

				KeywordEntity keywordEntity = KeywordEntity.builder()
					.name(s)
					.build();

				keywordService.save(keywordEntity);
				keywordEntityList.add(keywordEntity);
			}

			// 3. MediaKeywordEntity 생성하기
			for (KeywordEntity keywordEntity : keywordEntityList) {

				MediaKeyWordEntity mediaKeyWordEntity = MediaKeyWordEntity.builder()
					.media(mediaEntity)
					.keywordEntity(keywordEntity)
					.build();
				mediaKeywordService.save(mediaKeyWordEntity);
			}

		}
	}

}
