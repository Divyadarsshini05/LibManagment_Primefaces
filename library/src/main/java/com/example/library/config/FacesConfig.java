package com.example.library.config;

import javax.faces.webapp.FacesServlet;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FacesConfig {

	@Bean
	public ServletRegistrationBean<FacesServlet> facesServlet() {
		ServletRegistrationBean<FacesServlet> bean = new ServletRegistrationBean<>(new FacesServlet(), "*.xhtml");
		bean.setLoadOnStartup(1);
		return bean;
	}
}
