package com.debateseason.extractor.media.processed.service;

import org.springframework.stereotype.Service;

import com.debateseason.extractor.media.processed.domain.repository.MediaRepository;
import com.debateseason.extractor.media.processed.entity.MediaEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MediaService {

	private final MediaRepository mediaRepository;

	public void save(MediaEntity mediaEntity){
		mediaRepository.save(mediaEntity);
	}
	public boolean existsByTitle(String title){
		return mediaRepository.existsByTitle(title);
	}

}
