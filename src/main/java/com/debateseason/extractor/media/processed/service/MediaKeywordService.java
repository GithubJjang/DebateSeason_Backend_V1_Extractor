package com.debateseason.extractor.media.processed.service;

import org.springframework.stereotype.Service;

import com.debateseason.extractor.media.processed.domain.repository.MediaKeywordRepository;
import com.debateseason.extractor.media.processed.entity.MediaKeyWordEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MediaKeywordService {

	private final MediaKeywordRepository mediaKeywordRepository;

	public void save(MediaKeyWordEntity mediaKeywordEntity){
		mediaKeywordRepository.save(mediaKeywordEntity);
	}

}
