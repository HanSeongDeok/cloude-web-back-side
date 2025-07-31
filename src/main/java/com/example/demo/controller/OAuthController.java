package com.example.demo.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.UserEntity;
import com.example.demo.service.GoogleOAuthService;
import com.example.demo.service.JwtService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class OAuthController {
    private final GoogleOAuthService googleOAuthService;
    private final JwtService jwtService;

    @Value("${oauth2.google.client-id}")
    private String clientId;

    @Value("${oauth2.google.redirect-uri}")
    private String redirectUri;

    @Value("${oauth2.google.frontend-redirect-uri}")
    private String frontendRedirectUri;

    @GetMapping("/google")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        String oauthUrl = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&scope=openid%20email%20profile";

        log.info("Google OAuth 리다이렉트: {}", oauthUrl);
        response.sendRedirect(oauthUrl);
    }

    @GetMapping("/google/callback")
    public void handleGoogleCallback(@RequestParam("code") String code,
            HttpServletResponse response) throws IOException {
        try {
            log.info("Google OAuth 콜백 수신: code={}", code);

            // 1. access_token 요청
            String accessToken = googleOAuthService.getAccessToken(code);
            if (accessToken == null) {
                log.error("Google access token 획득 실패");
                response.sendRedirect(frontendRedirectUri + "?error=auth_failed");
                return;
            }

            // 2. 유저 정보 가져오기
            Map<String, Object> userInfo = googleOAuthService.getUserInfo(accessToken);
            if (userInfo == null) {
                log.error("Google user info 획득 실패");
                response.sendRedirect(frontendRedirectUri + "?error=user_info_failed");
                return;
            }

            // 3. 사용자 처리 (생성 또는 업데이트)
            googleOAuthService.processUserLogin(userInfo);

            // 4. JWT 생성
            String email = (String) userInfo.get("email");
            String jwt = jwtService.createToken(email);

            // 5. 프론트엔드로 리다이렉트
            String redirectUrl = frontendRedirectUri + "?jwt=" + jwt;
            log.info("JWT 토큰과 함께 프론트엔드로 리다이렉트: {}", redirectUrl);
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            log.error("Google OAuth 처리 중 오류 발생: {}", e.getMessage(), e);
            response.sendRedirect(frontendRedirectUri + "?error=server_error");
        }
    }

    @PostMapping("/permission")
    public ResponseEntity<?> getPermission(@RequestBody Map<String, String> request) {
        try {            String email = request.get("email");

            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "hasPermission", false,
                        "error", "Email is required"));
            }

            Optional<UserEntity> user = googleOAuthService.getUser(email);

            if (user.isPresent()) {
                return ResponseEntity.ok(Map.of(
                        "hasPermission", user.get().isHasPermission(),
                        "isRequesting", user.get().isRequesting(),
                        "role", user.get().getRole().name(),
                        "name", user.get().getName(),
                        "picture", user.get().getPicture(),
                        "email", user.get().getEmail()));
            }
            return ResponseEntity.ok(Map.of(
                    "hasPermission", false,
                    "isRequesting", false));
        } catch (Exception e) {
            log.error("권한 확인 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "hasPermission", false,
                    "isRequesting", false,
                    "error", "Internal server error"));
        }
    }

    @PutMapping("/permission/request")
    public ResponseEntity<?> requestPermission(@RequestBody Map<String, Object> request) {
        try {
            String email = request.get("email").toString();
            Boolean isRequest = (Boolean) request.get("isRequest");

            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "error", "Email is required"));
            }

            log.info("권한 요청 업데이트: email={}, isRequest={}", email, isRequest);
            Optional<UserEntity> userEntityOpt = googleOAuthService.getUser(email);

            if (userEntityOpt.isPresent()) {
                UserEntity user = userEntityOpt.get();
                user.setRequesting(isRequest);
                googleOAuthService.saveUser(user);

                log.info("권한 요청 상태 업데이트 성공: email={}, isRequesting={}", email, isRequest);

                return ResponseEntity.ok().body(Map.of(
                        "success", true,
                        "email", email,
                        "isRequesting", user.isRequesting(),
                        "hasPermission", user.isHasPermission(),
                        "role", user.getRole().name()));
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            log.error("권한 요청 업데이트 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "error", "Internal server error"));
        }
    }
}