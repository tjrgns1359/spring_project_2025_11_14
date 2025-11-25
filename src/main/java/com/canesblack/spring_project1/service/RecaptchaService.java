package com.canesblack.spring_project1.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class RecaptchaService {	//사용자의 reCAPTCHA 응답 토큰을 받아 Google 서버에서 검증 요청을 보내고, 그 결과로 사용자가 '사람'인지 확인

    // 구글에서 발급받은 비밀 키 (Secret Key)
    private static final String SECRET_KEY = "6Lc1PxEsAAAAANtHBOqDmJuM2HWzzTyg6v8RZyb5";
    // 구글 검증 API URL
    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean verify(String token) {	//파라미터로 받은 token이 null 또는 빈 문자열이면, 즉시 실패 반환 (false)
        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            RestTemplate restTemplate = new RestTemplate();	//외부 서버로 HTTP 요청을 보내는 도구(객체)

            // 구글 서버로 보낼 파라미터 설정
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("secret", SECRET_KEY);	//자신의 비밀키
            params.add("response", token);		//클라이언트가 보낸 사용자 토큰

            // API 호출 (POST)
            ResponseEntity<Map> response = restTemplate.postForEntity(VERIFY_URL, params, Map.class);	//restTemplate를 이용해 POST 요청 보냄

            // 응답 확인
            if (response.getBody() != null && (Boolean) response.getBody().get("success")) { //Google이 반환하는 JSON
                return true; // 검증 성공 (사람임)
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 에러 발생 시 검증 실패 처리
        }

        return false; // 검증 실패 (로봇이거나 토큰 만료)
    }
}