package com.debateseason.extractor.media.processed.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.debateseason.extractor.media.processed.entity.MediaEntity;

@Repository
public interface MediaJpaRepository extends JpaRepository<MediaEntity,Long> {

	boolean existsByTitle(String title); // title이 있나요?

}
