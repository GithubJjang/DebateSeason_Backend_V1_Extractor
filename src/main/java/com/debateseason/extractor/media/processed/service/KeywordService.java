package com.debateseason.extractor.media.processed.service;

import org.springframework.stereotype.Service;

import com.debateseason.extractor.media.processed.domain.repository.KeywordRepository;
import com.debateseason.extractor.media.processed.entity.KeywordEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class KeywordService {

	private final KeywordRepository keywordRepository;

	public void save(KeywordEntity keywordEntity){
		keywordRepository.save(keywordEntity);
	}
}
