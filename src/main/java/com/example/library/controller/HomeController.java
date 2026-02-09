package com.example.library.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String loginPage() {
		return "login.xhtml";
	}

	@GetMapping("/register")
	public String registerPage() {
		return "register.xhtml";
	}


}
