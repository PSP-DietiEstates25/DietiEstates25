package com.dietiestates.clientserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicController {

	@GetMapping("/public/messages")
	public String publicMessages(
			Model model
			) {
		model.addAttribute("body", "nobody");
		return "response";
	}
}
