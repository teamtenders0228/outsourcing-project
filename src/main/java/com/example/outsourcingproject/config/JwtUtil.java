package com.example.outsourcingproject.config;

import com.example.outsourcingproject.domain.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKNE_TIME = 120 * 1000L; // 1 hour
    private static final long REFRESH_TOKNE_TIME = 24 * 3600 * 1000L;

    @Value("${jwt.secret.accessKey}")
    private String accessKey;
    @Value("${jwt.secret.refreshKey}")
    private String refreshKey;

    private Key access_key, refresh_key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {

        byte[] bytes1 = Base64.getDecoder().decode(accessKey);
        access_key = Keys.hmacShaKeyFor(bytes1);

        byte[] bytes2 = Base64.getDecoder().decode(refreshKey);
        refresh_key = Keys.hmacShaKeyFor(bytes2);
    }


    public String createAccessToken(Long userId, String email, UserRole userRole) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder().
                        setSubject(String.valueOf(userId))
                        .claim("email", email)
                        .claim("userRole", userRole)
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKNE_TIME))
                        .setIssuedAt(date)
                        .signWith(access_key, signatureAlgorithm)
                        .compact();
    }

    public String createRefreshToken(Long userId) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setExpiration(new Date(date.getTime() + REFRESH_TOKNE_TIME))
                .setIssuedAt(date)
                .signWith(refresh_key, signatureAlgorithm)
                .compact();
    }

    public String substringToken(String token){
        if(StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(7);
        }
        throw new RuntimeException("토큰을 찾지 못했습니다.");
    }

    public Claims extractClaims(String token, boolean isRefreshToken) {
        Key key = isRefreshToken ? refresh_key : access_key;
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
