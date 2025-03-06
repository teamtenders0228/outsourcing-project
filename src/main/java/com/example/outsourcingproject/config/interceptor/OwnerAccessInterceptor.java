package com.example.outsourcingproject.config.interceptor;

import com.example.outsourcingproject.domain.user.entity.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class OwnerAccessInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Interceptor 실행 - 요청 경로: {}", request.getRequestURI());

        UserRole userRole = UserRole.of((String) request.getAttribute("userRole"));
        if(!UserRole.OWNER.equals(userRole)) {
            log.warn("권한: ",userRole);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "해당 기능에 대한 권한이 없습니다.");
            return false;
        }
        return true;
    }
}
