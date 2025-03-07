package com.example.outsourcingproject.config;

import com.example.outsourcingproject.domain.user.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.domain.refreshToken.RefreshToken;
import com.example.outsourcingproject.domain.refreshToken.RefreshTokenRepository;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static com.example.outsourcingproject.common.exception.ErrorCode.*;

@Slf4j
@Component
public class JwtUtil {
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKNE_TIME = 3600 * 1000L; // 1 hour
    private static final long REFRESH_TOKNE_TIME = 24 * 3600 * 1000L;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final HttpServletResponse httpServletResponse;

    @Value("${jwt.secret.accessKey}")
    private String accessKey;
    @Value("${jwt.secret.refreshKey}")
    private String refreshKey;

    private Key access_key, refresh_key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public JwtUtil(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, HttpServletResponse httpServletResponse) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.httpServletResponse = httpServletResponse;
    }

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

        String token = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setExpiration(new Date(date.getTime() + REFRESH_TOKNE_TIME))
                .setIssuedAt(date)
                .signWith(refresh_key, signatureAlgorithm)
                .compact();

        refreshTokenRepository.findByUserId(userId)
                .ifPresentOrElse(
                        existingToken -> existingToken.updateToken(token),
                        () -> refreshTokenRepository.save(new RefreshToken(userId, token))
                );

        return token;
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
    public String refreshAccessToken(Long userId) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new BaseException(REFRESH_TOKEN_NOT_FOUND, null));

        try {
            Claims claims = extractClaims(refreshToken.getToken(), true);

            if(claims == null) {
                throw new BaseException(REFRESH_TOKEN_NOT_FOUND, null);
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new BaseException(USER_NOT_FOUND, null));

            return createAccessToken(userId, user.getEmail(), user.getUserRole());
        } catch (ExpiredJwtException e) {
            refreshTokenRepository.delete(refreshToken); // 만료된 토큰 삭제
            throw new BaseException(INVALID_REFRESH_TOKEN, "Refresh Token이 만료되었습니다. 다시 로그인하세요.");
        }
    }
}
