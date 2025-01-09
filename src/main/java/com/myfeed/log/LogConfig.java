package com.myfeed.log;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myfeed.log.model.CustomLog;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class LogConfig {

	private static final String format = "yyyy-MM-dd HH:mm:ss.SSS";

	@Pointcut("@annotation(com.myfeed.log.annotation.LogUserBehavior)")
	public void logging() {}

	@Around("logging()")
	public Object logUserBehavior(ProceedingJoinPoint joinPoint) throws Throwable {
		CustomLog customLog = new CustomLog();

		// 1. 사용자 식별자
			// 사용자 식별자는 userId로 할 것
			// userId는 jwt 구현되면 가져오는 로직 추가할 예정

		// 2. api 호출 시각
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(format);
		customLog.setCreatedAt(LocalDateTime.now().format(timeFormatter));

		// 3. 호출된 api의 엔드포인트 및 HTTP Method
		HttpServletRequest request =
			((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		customLog.setHttpMethod(request.getMethod());
		customLog.setUri(request.getRequestURI());

		// 4. boardId -> nullable
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		String boardId = null;
		if (method.getName().equals("testVarB")) { // 게시글 상세 조회 메서드 (컨트롤러)
			String[] uriParts = request.getRequestURI().split("/");
			boardId = uriParts[4];
		}
		customLog.setBoardId(boardId);

		// 서비스 로직 실행
		Object response = joinPoint.proceed();

		// log.info(getMessage(customLog));
		log.info("{}", getMessage(customLog));

		return response;
	}

	private String getMessage(CustomLog customLog) throws JsonProcessingException {
		Map<String, String> map = new LinkedHashMap<>();

		map.put("createdAt", customLog.getCreatedAt());
		map.put("httpMethod", customLog.getHttpMethod());
		map.put("uri", customLog.getUri());
		map.put("boardId", customLog.getBoardId());

		return new ObjectMapper().writeValueAsString(map);
	}
}
