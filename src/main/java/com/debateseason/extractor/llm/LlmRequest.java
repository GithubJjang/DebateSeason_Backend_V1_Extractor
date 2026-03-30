package com.debateseason.extractor.llm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class LlmRequest {

	private final String model = "exaone3.5:latest";
	private final boolean stream = false;
	private final double temperature = 0.2;
	private final double topP = 0.6;
	private final String prompt;

	public LlmRequest(String prompt){
		this.prompt = prompt;
	}
}
