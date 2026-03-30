package com.debateseason.extractor.media.processed.repository.impl;

import org.springframework.stereotype.Repository;

import com.debateseason.extractor.media.processed.domain.repository.KeywordRepository;
import com.debateseason.extractor.media.processed.entity.KeywordEntity;
import com.debateseason.extractor.media.processed.repository.jpa.KeywordJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class KeywordRepositoryImpl implements KeywordRepository {

	private final KeywordJpaRepository keywordJpaRepository;

	@Override
	public void save(KeywordEntity keywordEntity) {
		keywordJpaRepository.save(keywordEntity);
	}
}
