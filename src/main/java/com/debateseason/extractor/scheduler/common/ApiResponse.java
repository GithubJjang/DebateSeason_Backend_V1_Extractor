package com.debateseason.extractor.scheduler.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse<T> {
	private Status status;
	private int code;
	private String message;
	private T data;

	public ApiResponse(Status status, int code, String message, T data) {
		this.status = status;
		this.code = code;
		this.message = message;
		this.data = data;
	}
}
