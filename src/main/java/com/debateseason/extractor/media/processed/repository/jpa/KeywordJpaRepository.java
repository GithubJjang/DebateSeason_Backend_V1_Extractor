package com.debateseason.extractor.media.processed.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.debateseason.extractor.media.processed.entity.KeywordEntity;

@Repository
public interface KeywordJpaRepository extends JpaRepository<KeywordEntity,Long> {
}
