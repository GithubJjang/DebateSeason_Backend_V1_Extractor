package com.debateseason.extractor.media.raw.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.debateseason.extractor.media.raw.domain.repository.RawMediaRepository;
import com.debateseason.extractor.media.raw.entity.RawMediaEntity;
import com.debateseason.extractor.media.raw.repository.jpa.RawMediaJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class RawMediaRepositoryImpl implements RawMediaRepository {

	private final RawMediaJpaRepository rawMediaJpaRepository;

	@Override
	public void save(RawMediaEntity rawMediaEntity) {
		rawMediaJpaRepository.save(rawMediaEntity);
	}
	@Override
	public Long findLastIdOrderByIdDesc(){
		return rawMediaJpaRepository.findLastIdOrderByIdDesc();
	}
	@Override
	public List<RawMediaEntity> findOldestUnCheckedMediaOrderByIdAsc(int limit){
		return rawMediaJpaRepository.findOldestUnCheckedMediaOrderByIdAsc(limit);
	}

	@Override
	public void updateChecked(Long id) {
		rawMediaJpaRepository.updateChecked(id);
	}
}
