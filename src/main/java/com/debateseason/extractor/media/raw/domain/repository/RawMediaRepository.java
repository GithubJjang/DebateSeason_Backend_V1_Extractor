package com.debateseason.extractor.media.raw.domain.repository;

import java.util.List;

import com.debateseason.extractor.media.raw.entity.RawMediaEntity;

public interface RawMediaRepository {

	void save(RawMediaEntity rawMediaEntity);
	Long findLastIdOrderByIdDesc();
	List<RawMediaEntity> findOldestUnCheckedMediaOrderByIdAsc(int limit);
	void updateChecked(Long id);
}
