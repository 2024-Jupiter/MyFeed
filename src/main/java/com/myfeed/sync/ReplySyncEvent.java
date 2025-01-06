package com.myfeed.sync;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReplySyncEvent {
    private final Long replyId;
    private final String operation; // CREATE, UPDATE, DELETE
}
