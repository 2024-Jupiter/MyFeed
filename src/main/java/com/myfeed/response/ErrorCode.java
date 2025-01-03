package com.myfeed.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	/**
	 * 사용자
	 */
	USER_NOT_FOUND("USER_NOT_FOUND","사용자를 찾을 수 없습니다.");


	private final String errorCode;
	private final String errorMessage;
}
