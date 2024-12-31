package com.myfeed.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApiResponse<T> {

	private final String status;

	private final T data;

	private final String errorCode;

	// private String errorMessage;

	public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
		return new ResponseEntity<>(new ApiResponse<>("success", data, null), HttpStatus.OK);
	}

	public static <T> ResponseEntity<ApiResponse<?>> error(String errorCode) {
		return new ResponseEntity<>(new ApiResponse<>("error", null, errorCode), HttpStatus.BAD_REQUEST);
	}
}
