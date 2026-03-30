package com.debateseason.extractor.media.raw.repository.impl;

import org.springframework.stereotype.Repository;

import com.debateseason.extractor.media.raw.entity.ErrorEntity;
import com.debateseason.extractor.media.raw.domain.repository.ErrorRepository;
import com.debateseason.extractor.media.raw.repository.jpa.ErrorJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ErrorRepositoryImpl implements ErrorRepository {

	private final ErrorJpaRepository errorJpaRepository;

	@Override
	public void save(ErrorEntity errorEntity) {
		errorJpaRepository.save(errorEntity);

	}
}
