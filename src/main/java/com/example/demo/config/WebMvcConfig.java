package com.example.demo.config;

import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// ioc 등록을 위해 annotation 설정을 한다
@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	// WebMvcConfigurer를 구현함으로써
	// 내 입맛에 맞게 config를 조정한다.
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		MustacheViewResolver resolver = new MustacheViewResolver();
		resolver.setCharset("UTF-8");
		resolver.setContentType("text/html; charset=UTF-8");
		// classpath는 vue로 예를 들었을 때 
		// @/ 와 같다.
		resolver.setPrefix("classpath:/templates/");
		resolver.setSuffix(".html");
		
		registry.viewResolver(resolver);
	}
}
