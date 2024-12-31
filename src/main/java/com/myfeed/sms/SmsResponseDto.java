package com.myfeed.sms;

import co.elastic.clients.elasticsearch.watcher.ScheduleTriggerEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SmsResponseDto {
    private String authCode;
}
