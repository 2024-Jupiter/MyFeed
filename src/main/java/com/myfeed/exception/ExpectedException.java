package com.myfeed.exception;

import com.myfeed.response.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExpectedException extends RuntimeException {

	private final ErrorCode errorCode;
}
