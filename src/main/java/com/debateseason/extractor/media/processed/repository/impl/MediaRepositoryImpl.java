package com.debateseason.extractor.media.processed.repository.impl;

import org.springframework.stereotype.Repository;

import com.debateseason.extractor.media.processed.domain.repository.MediaRepository;
import com.debateseason.extractor.media.processed.entity.MediaEntity;
import com.debateseason.extractor.media.processed.repository.jpa.MediaJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class MediaRepositoryImpl implements MediaRepository {

	private final MediaJpaRepository mediaJpaRepository;

	@Override
	public void save(MediaEntity mediaEntity) {
		mediaJpaRepository.save(mediaEntity);
	}

	@Override
	public boolean existsByTitle(String title) {
		return mediaJpaRepository.existsByTitle(title);
	}
}
