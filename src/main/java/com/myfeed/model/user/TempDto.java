package com.myfeed.model.user;

import lombok.Getter;

@Getter
public class TempDto {
	private String temp;

	public TempDto(String temp) {
		this.temp = temp;
	}
}
