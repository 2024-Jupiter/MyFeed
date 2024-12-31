package com.myfeed.sms;

import co.elastic.clients.elasticsearch.watcher.ScheduleTriggerEvent;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Balance;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.StorageType;
import net.nurigo.sdk.message.request.MessageListRequest;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.MessageListResponse;
import net.nurigo.sdk.message.response.MultipleDetailMessageSentResponse;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@RequestMapping("/api/users")
@Controller
public class SmsController {

    @Autowired SmsService smsService;

    @PostMapping("/find-password") //todo get??
    @ResponseBody
    public ResponseEntity<Map<String, Object>> findPassword(@RequestBody SmsDto smsDto) {
        Map<String, Object> messagemap = new HashMap<>();
        SingleMessageSentResponse response = smsService.sendMessage(smsDto);
        messagemap.put("status", "success");
        messagemap.put("response", response);
        messagemap.put("redirectUrl", "/api/users/password/"); // 비번 변경 페이지로 넘어가기
        return ResponseEntity.ok().body(messagemap);
    }

}
