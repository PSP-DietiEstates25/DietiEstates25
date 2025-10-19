package com.dietiestates.clientserver.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	/* vecchio(versione thymeleaf)
	@GetMapping("/")
	public String home(Model model, @AuthenticationPrincipal OAuth2User user) {
		model.addAttribute("userName", user.getAttribute("name"));
		return "index.html";
	}
	*/
	
	@GetMapping("/")
	public String home() {
		return "index.html";
	}
}
