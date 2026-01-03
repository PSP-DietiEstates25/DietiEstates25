package com.dietiestate.bff.controller;

import com.dietiestate.bff.config.BackendForFrontendServerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final BackendForFrontendServerProperties properties;

    @GetMapping("/")
    public String root() {
        return "redirect:" + properties.baseUri();
    }

    @GetMapping("/authorized")
    public String authorized() {
        return "redirect:" + properties.baseUri();
    }

}

