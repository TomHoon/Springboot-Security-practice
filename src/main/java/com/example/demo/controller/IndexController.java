package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
	
	@GetMapping({"", "/"})
	public String index() {
		// src/main/resources 기본 폴더
		// 뷰리졸버 설정 : templates (prefix), mustache(suffix)
		// yml에다가 뷰리졸버 셋팅 안해도 됨
		// maven으로 라이브러리 가져오면
		// framework에 자동으로 셋팅됨
		
		// 기본경로가 다음과 같다.
		// src/main/resources/template/index.mustache
		// 우리는 html로 해야되기 떄문에 config 파일에서 수정해주자
		
		return "index"; 
	}
	
	@GetMapping("/user")
	public String user() {
		return "user";
	}
	
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public String manager() {
		return "manager";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/join")
	public String join() {
		return "join";
	}
	
	@GetMapping("/joinProc")
	public @ResponseBody String joinProc() {
		return "회원가입 완료됨";
	}
	
	
}
