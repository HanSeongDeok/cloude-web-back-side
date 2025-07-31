package com.example.demo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "oauth2.google.client-id=test-client-id",
    "oauth2.google.client-secret=test-client-secret",
    "oauth2.google.redirect-uri=http://localhost:8080/auth/google/callback",
    "oauth2.google.frontend-redirect-uri=http://localhost:5173/save-token",
    "jwt.secret=test-jwt-secret-key-for-testing-purposes-only",
    "jwt.expiration=86400000"
})
@Disabled("OAuth 서비스 구현 완료 후 활성화 예정")
class MainApplicationTests {

	@Test
	void contextLoads() {
	}

}
