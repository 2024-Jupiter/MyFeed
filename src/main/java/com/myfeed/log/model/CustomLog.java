package com.myfeed.log.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomLog {
	private String createdAt;
	private String httpMethod;
	private String uri;
	private String boardId;
}
