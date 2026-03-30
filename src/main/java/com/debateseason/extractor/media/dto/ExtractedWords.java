package com.debateseason.extractor.media.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ExtractedWords {

	private final List<String> nouns;

}
