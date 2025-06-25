package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "test_user")
@EqualsAndHashCode(callSuper = false)
@Data
public class TestUserEntity extends BaseTimeEntity {
    @Id
    private String id;

    @NotNull
    @Column
    private String name;

    @Column
    private Integer age;

    @Column(columnDefinition = "TEXT")
    private String email;
}
