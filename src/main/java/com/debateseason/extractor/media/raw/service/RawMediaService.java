package com.debateseason.extractor.media.raw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.debateseason.extractor.media.raw.domain.repository.RawMediaRepository;
import com.debateseason.extractor.media.raw.entity.RawMediaEntity;
import com.debateseason.extractor.media.raw.repository.jpa.RawMediaJpaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RawMediaService {
	

	private final RawMediaRepository rawMediaRepository;
	
	// 1. Media 저장
	public void save(RawMediaEntity rawMediaEntity){
		rawMediaRepository.save(rawMediaEntity);
	}

	// 2.
	public Long findLastIdOrderByIdDesc(){
		return rawMediaRepository.findLastIdOrderByIdDesc();

	}

	// 3. Unchecked된 것 중에서 오래된 media들을 가져온다.
	public List<RawMediaEntity> findOldestUnCheckedMediaOrderByIdAsc(int limit){
		return rawMediaRepository.findOldestUnCheckedMediaOrderByIdAsc(limit);
	}
	@Transactional
	public void updateRawMediaToChecked(Long id){
		rawMediaRepository.updateChecked(id);
	}


	
	
}
