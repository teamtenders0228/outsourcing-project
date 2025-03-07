package com.example.outsourcingproject.config;

import com.example.outsourcingproject.config.interceptor.OwnerAccessInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private final OwnerAccessInterceptor ownerAccessInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthUserArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ownerAccessInterceptor)
                .addPathPatterns(
                        "/api/v1/menus/**",             // MenuController
                        "/api/v1/owner/stores/**",      // StoreOwnerController
                        "/api/v1/orders/accept/**",     // OrderController
                        "/api/v1/orders/status/progress/**",
                        "/api/v1/orders/status/reject/**"
                );
    }
}
