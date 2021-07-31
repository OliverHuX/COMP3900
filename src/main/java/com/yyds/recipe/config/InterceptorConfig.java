package com.yyds.recipe.config;

import com.yyds.recipe.filter.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(List.of(
                        "/home",
                        "/login",
                        "/visitor/recipe_list",
                        "/register",
                        "/emailVerify/**",
                        "swagger/**",
                        "/swagger-ui.html",
                        "swagger-ui.html",
                        "/webjars/**",
                        "/swagger-ui.html/*",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/test"
                ));
    }

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }
}
