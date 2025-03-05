package com.example.outsourcingproject.common.aop;

import com.example.outsourcingproject.domain.order.dto.response.OrderStatusResponseDto;
import com.example.outsourcingproject.domain.order.entity.Order;
import com.example.outsourcingproject.domain.order.repository.OrderRepository;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.repository.StoreRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

@Aspect
@Slf4j
@Component
@AllArgsConstructor
public class TraceAspect {

    // @Trace 어노테이션이 붙은 메서드를 위한 Pointcut 설정
    @Pointcut("@annotation(com.example.outsourcingproject.common.annotation.Trace)")
    public void traceAnnotatedMethod() {}

    // @Trace 어노테이션이 붙은 메서드를 감싸는 Around 어드바이스
    @Around("traceAnnotatedMethod()")
    public Object handleTrace(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메서드 실행 전 로직 (예: 로그 찍기, 파라미터 확인 등)
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();

        log.info("Trace - Before executing method: " + methodName);

        // 메서드 실행
        Object result = joinPoint.proceed();

        // 메서드 실행 후 로직 (예: 결과 값 확인, 후처리 등)
        if (result instanceof OrderStatusResponseDto responseDto) {
            log.info("Trace - orderId: " + responseDto.getOrderId());
            log.info("Trace - storeId: " + responseDto.getStoreId());
        }

        return result;
    }
}
