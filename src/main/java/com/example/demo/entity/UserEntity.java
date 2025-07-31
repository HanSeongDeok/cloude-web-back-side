package com.example.demo.entity;

import com.example.demo.entity.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @Column(unique = true)
    private String providerId;

    @Builder.Default
    @Column
    private boolean hasPermission = false;

    @Builder.Default
    @Column
    private boolean isRequesting = false;

    public enum Role {
        USER, ADMIN, SUPER_ADMIN
    }

    public enum Provider {
        GOOGLE, NAVER, KAKAO
    }

    // Google OAuth 전용 생성자
    public static UserEntity fromGoogleInfo(String id, String email, String name, String picture) {
        return UserEntity.builder()
                .email(email)
                .name(name)
                .picture(picture)
                .role(Role.USER)
                .provider(Provider.GOOGLE)
                .providerId(id)
                .hasPermission(false)
                .isRequesting(false)
                .build();
    }
}