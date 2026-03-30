package com.debateseason.extractor.media.processed.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "media")
public class MediaEntity { // DebateSeason의 백엔드 서버 DB에 추가를 하기 위한, 엔티티

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "title",columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
	private String title;

	@Column(name = "url")
	private String url;

	@Column(name = "src")
	private String src;

	@Column(name = "category")
	private String category;

	@Column(name = "media")
	private String media;

	@Column(name = "type")
	private String type;// news, community, youtube

	@Column(name = "count")
	private int count;// 조회수

	// @Column(name = "checked")
	// private int checked;// LLM으로 필터링을 한 여부, 초기값은 0으로 둔다.

	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

}
