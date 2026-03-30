package com.debateseason.extractor.media.manager;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.debateseason.extractor.media.processed.entity.MediaEntity;
import com.debateseason.extractor.media.raw.entity.RawMediaEntity;
import com.debateseason.extractor.media.raw.response.RawMediaResponse;

@Component
public class MediaManager { // 클래스도 얼마 없으니까, 일단 God 클래스로 하고나서 나중에 분리하든가 하자.

	public RawMediaEntity createRawMediaEntity(RawMediaResponse rawMediaResponse){

		String date = rawMediaResponse.getCreatedAt();
		LocalDateTime createdAt = LocalDateTime.parse(date);

		return RawMediaEntity.builder()
			.id(rawMediaResponse.getId())
			.title(rawMediaResponse.getTitle())
			.url(rawMediaResponse.getUrl())
			.src(rawMediaResponse.getSrc())
			.category(rawMediaResponse.getCategory())
			.media(rawMediaResponse.getMedia())
			.type(rawMediaResponse.getType())
			.count(rawMediaResponse.getCount())
			.checked(0)// Raw
			.createdAt(createdAt)
			.build();
	}

	public MediaEntity convertRawMediaToMedia(RawMediaEntity rawMediaEntity){

		return MediaEntity.builder()
			//.id(rawMediaEntity.getId())
			.title(rawMediaEntity.getTitle())
			.url(rawMediaEntity.getUrl())
			.src(rawMediaEntity.getSrc())
			.category(rawMediaEntity.getCategory())
			.media(rawMediaEntity.getMedia())
			.type(rawMediaEntity.getType())
			.count(rawMediaEntity.getCount())
			.createdAt(rawMediaEntity.getCreatedAt())
			.build();
	}


}
