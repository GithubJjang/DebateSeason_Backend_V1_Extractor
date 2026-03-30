package com.debateseason.extractor.media.raw.domain.repository;

import com.debateseason.extractor.media.raw.entity.ErrorEntity;

public interface ErrorRepository {

	void save(ErrorEntity errorEntity);
}
