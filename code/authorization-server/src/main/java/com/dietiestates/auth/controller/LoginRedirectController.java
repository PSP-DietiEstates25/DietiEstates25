package com.dietiestates.auth.controller;

import com.dietiestates.auth.config.AuthorizationServerProperties;
import com.dietiestates.auth.model.Authorization;
import com.dietiestates.auth.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Controller
@RequiredArgsConstructor
public class LoginRedirectController implements WebMvcConfigurer {

    private final AuthorizationServerProperties properties;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:" + properties.loginUrl());
        registry.addViewController(properties.loginUrl()).setViewName("forward:/auth/index.html");
    }
}