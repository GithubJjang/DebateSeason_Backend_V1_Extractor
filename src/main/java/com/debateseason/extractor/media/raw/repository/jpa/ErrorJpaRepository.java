package com.debateseason.extractor.media.raw.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.debateseason.extractor.media.raw.entity.ErrorEntity;

@Repository
public interface ErrorJpaRepository extends JpaRepository<ErrorEntity,Long> {
}
