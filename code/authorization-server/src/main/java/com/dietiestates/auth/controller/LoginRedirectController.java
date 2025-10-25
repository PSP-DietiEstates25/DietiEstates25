package com.dietiestates.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Controller
public class LoginRedirectController implements WebMvcConfigurer {

    @Value("${loginUrl}")
    private String loginUrl;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(loginUrl).setViewName("forward:/auth/index.html");
    }
}