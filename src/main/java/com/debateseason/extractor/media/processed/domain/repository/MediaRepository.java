package com.debateseason.extractor.media.processed.domain.repository;

import com.debateseason.extractor.media.processed.entity.MediaEntity;

public interface MediaRepository {

	void save(MediaEntity media);
	boolean existsByTitle(String title); // title이 있나요?

}
