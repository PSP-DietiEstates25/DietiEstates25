package com.dietiestates25.backend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping("/")
	public void home() {
		System.out.println("User is sending request");
	}
}
