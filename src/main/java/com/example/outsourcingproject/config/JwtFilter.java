package com.example.outsourcingproject.config;

import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.refreshToken.RefreshToken;
import com.example.outsourcingproject.domain.refreshToken.RefreshTokenRepository;
import com.example.outsourcingproject.domain.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

import static com.example.outsourcingproject.common.exception.ErrorCode.INVALID_REFRESH_TOKEN;
import static com.example.outsourcingproject.common.exception.ErrorCode.REFRESH_TOKEN_NOT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter implements Filter {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String url = httpRequest.getRequestURI();

        if(url.startsWith("/api/v1/auth")) {
            chain.doFilter(request, response);
            return;
        }

        String bearerJwt = httpRequest.getHeader("Authorization");

        if(bearerJwt == null) {
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "로그인 상태가 아닙니다.");
            return;
        }

        String jwt = jwtUtil.substringToken(bearerJwt);

        try {
            Claims claims = jwtUtil.extractClaims(jwt, false);

            if (claims == null) {
                httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다.");
                return;
            }

            UserRole userRole = UserRole.of(claims.get("userRole", String.class));

            httpRequest.setAttribute("userId", Long.parseLong(claims.getSubject()));
            httpRequest.setAttribute("email", claims.get("email"));
            httpRequest.setAttribute("userRole", claims.get("userRole"));
//
//            if (url.startsWith("/admin")) {
//                if (!UserRole.ADMIN.equals(userRole)) {
//                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자 권한이 없습니다.");
//                    return;
//                }
//                chain.doFilter(request, response);
//                return;
//            }

            chain.doFilter(request, response);
        } catch (ExpiredJwtException e){
            log.info("Access Token이 만료됨. Refresh Token을 이용해 새로운 Access Token 발급");

            Long userId = Long.parseLong(e.getClaims().getSubject());

            try {
                String newAccessToken = jwtUtil.refreshAccessToken(userId);
                httpResponse.setHeader("Authorization",  newAccessToken);
                chain.doFilter(request, response);
            } catch (BaseException ex) {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
            }
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
