package com.debateseason.extractor.media.processed.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.debateseason.extractor.media.processed.entity.MediaKeyWordEntity;

@Repository
public interface MediaKeywordJpaRepository extends JpaRepository<MediaKeyWordEntity,Long> {
}
