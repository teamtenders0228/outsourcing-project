package com.example.outsourcingproject.domain.refreshToken;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    private Long userId;  // 유저 ID를 PK로 사용

    @Column(nullable = false, unique = true)
    private String token;

    public void updateToken(String newToken) {
        this.token = newToken;
    }
}
