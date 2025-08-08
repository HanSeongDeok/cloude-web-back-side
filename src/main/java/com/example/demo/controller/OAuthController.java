package com.example.demo.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.GoogleTokenResponse;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.GoogleOAuthService;
import com.example.demo.service.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
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
            GoogleTokenResponse idToken = Optional.ofNullable(googleOAuthService.getIdToken(code))
                    .orElseThrow(() -> new RuntimeException("Google ID token 획득 실패"));

            Map<String, Object> userInfo = googleOAuthService.getUserInfo(idToken.getAccess_token());
            UserEntity user = googleOAuthService.processUserLogin(userInfo);

            String newAccessToken = jwtService.createToken(user.getEmail());
            idToken.setAccess_token(newAccessToken);

            // accessToken과 refreshToken을 쿠키로 설정
            Cookie accessTokenCookie = new Cookie("accessToken", idToken.getAccess_token());
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setPath("/");
            // 필요에 따라 Secure 옵션 추가
            // accessTokenCookie.setSecure(true);

            Cookie refreshTokenCookie = new Cookie("refreshToken", idToken.getRefresh_token());
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath("/");
            // refreshTokenCookie.setSecure(true);

            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);
            response.sendRedirect(frontendRedirectUri + "?success=true");
        } catch (Exception e) {
            log.error("Google OAuth 처리 중 오류 발생: {}", e.getMessage(), e);
            response.sendRedirect(frontendRedirectUri + "?error=server_error");
        }
    }

    @GetMapping("/permission/V2")
    public ResponseEntity<?> getPermission(Authentication auth, HttpServletRequest request) {
        try {
            Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]);
            String token = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("accessToken"))
                    .findFirst().map(Cookie::getValue)
                    .orElseThrow(() -> new RuntimeException("Token not found"));

            String email = jwtService.getEmailFromToken(token);
            UserEntity user = googleOAuthService.getUser(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return ResponseEntity.ok(Map.of(
                    "hasPermission", user.isHasPermission(),
                    "isRequesting", user.isRequesting(),
                    "role", user.getRole().name(),
                    "name", user.getName(),
                    "picture", user.getPicture(),
                    "email", user.getEmail()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                    "code", "AUTH_ERROR",
                    "message", "인증 오류가 발생했습니다."));
        }
    }

    @PostMapping("/permission")
    public ResponseEntity<?> getPermission(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");

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

    @PutMapping("/permission/request/V2")
    public ResponseEntity<Map<String, Object>> requestPermission(Authentication auth) {
        String email = auth.getName();
        UserEntity user = googleOAuthService.getUser(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRequesting(true);
        googleOAuthService.saveUser(user);
        return ResponseEntity.ok().body(Map.of(
                "success", true,
                "email", email,
                "isRequesting", user.isRequesting(),
                "hasPermission", user.isHasPermission(),
                "role", user.getRole().name()));
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