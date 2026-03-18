package com.hezheng.authapi.config;

import com.hezheng.authapi.common.JWTInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//添加白名单
@Configuration
public class JWTConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login","/register","/upload","/download/**","/api/**"
                );
    }

    @Bean
    public JWTInterceptor jwtInterceptor(){
        return new JWTInterceptor();
    }
}
