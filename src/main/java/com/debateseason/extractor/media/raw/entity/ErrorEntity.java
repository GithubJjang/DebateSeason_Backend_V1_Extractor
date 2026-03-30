package com.debateseason.extractor.media.raw.entity;

import com.debateseason.extractor.media.common.ProcessingStage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "error")
@Builder
public class ErrorEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "crawl_id")
	private Long crawlId;

	@Column(name = "message")
	private String message;

	@Enumerated(EnumType.STRING)
	private ProcessingStage processingStage;

}
