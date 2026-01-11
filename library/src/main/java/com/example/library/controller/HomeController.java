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

	@GetMapping("/member")
	public String memberHome(HttpSession session) {
		if (session.getAttribute("user") == null) {
			System.out.println("USER UNAUTHORIZED");
			return "unauthorized.xhtml";
		}
		System.out.println("USER AUTHORIZED");
		return "member/index.xhtml";
	}

	@GetMapping("/admin")
	public String adminHome(HttpSession session) {
		if (session.getAttribute("user") == null) {
			return "unauthorized.xhtml";
		}
		return "admin/index.xhtml";
	}
}
