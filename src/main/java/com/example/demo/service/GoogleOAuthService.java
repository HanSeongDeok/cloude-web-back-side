package com.example.demo.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.dto.GoogleTokenResponse;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthService {
    private final UserRepository userRepository;
    private final WebClient webClient;

    @Value("${oauth2.google.client-id}")
    private String clientId;

    @Value("${oauth2.google.client-secret}")
    private String clientSecret;

    @Value("${oauth2.google.redirect-uri}")
    private String redirectUri;

    public GoogleTokenResponse getIdToken(String code) {
        String tokenUrl = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("code", code);
        body.add("grant_type", "authorization_code");
        body.add("redirect_uri", redirectUri);

        try {
            GoogleTokenResponse response = webClient.post()
                    .uri(tokenUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("client_id", clientId)
                            .with("client_secret", clientSecret)
                            .with("code", code)
                            .with("grant_type", "authorization_code")
                            .with("redirect_uri", redirectUri))
                    .retrieve()
                    .bodyToMono(GoogleTokenResponse.class)
                    .block(); // 동기적으로 실행

            if (response != null && response.getId_token() != null) {
                return response;
            }
        } catch (Exception e) {
            log.error("Google ID token 요청 실패: {}", e.getMessage());
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getUserInfo(String accessToken) {
        String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
        try {
            Map<String, Object> userInfo = webClient.get()
                    .uri(userInfoUrl)
                    .headers(headers -> headers.setBearerAuth(accessToken))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return userInfo;
        } catch (Exception e) {
            log.error("Google user info 요청 실패: {}", e.getMessage());
            return null;
        }
    }

    public UserEntity processUserLogin(Map<String, Object> userInfo) {
        String email = (String) userInfo.get("email");
        String id = (String) userInfo.get("id");
        String name = (String) userInfo.get("name");
        String picture = (String) userInfo.get("picture");

        UserEntity user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            user = UserEntity.fromGoogleInfo(id, email, name, picture);
            saveUser(user);
            log.info("새 사용자 생성: {}", email);
        } else {
            user.setName(name);
            user.setPicture(picture);
            saveUser(user);
            log.info("기존 사용자 정보 업데이트: {}", email);
        }

        return user;
    }

    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    public Optional<UserEntity> getUser(String email) {
        return userRepository.findByEmail(email);
    }
}