package com.debateseason.extractor.media.processed.repository.impl;

import org.springframework.stereotype.Repository;

import com.debateseason.extractor.media.processed.domain.repository.MediaKeywordRepository;
import com.debateseason.extractor.media.processed.entity.MediaKeyWordEntity;
import com.debateseason.extractor.media.processed.repository.jpa.MediaKeywordJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class MediaKeywordRepositoryImpl implements MediaKeywordRepository {
	private final MediaKeywordJpaRepository mediaKeywordJpaRepository;

	@Override
	public void save(MediaKeyWordEntity mediaKeywordEntity) {
		mediaKeywordJpaRepository.save(mediaKeywordEntity);
	}
}
