package com.debateseason.extractor.media.raw.service;

import org.springframework.stereotype.Service;

import com.debateseason.extractor.media.raw.domain.repository.ErrorRepository;
import com.debateseason.extractor.media.raw.entity.ErrorEntity;
import com.debateseason.extractor.media.raw.repository.jpa.ErrorJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ErrorService {

	private final ErrorRepository errorRepository;

	public void save(ErrorEntity errorEntity){
		errorRepository.save(errorEntity);
	}
}
