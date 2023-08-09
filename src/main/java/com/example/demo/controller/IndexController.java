package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.auth.PrincipalDetails;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Controller
public class IndexController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bcPwdEncoder;
	
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
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@PostMapping("/join")
	public String join(User user) {
		user.setRole("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = bcPwdEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		userRepository.save(user);
		return "redirect:/loginForm";
	}
	
	@GetMapping("/joinProc")
	public @ResponseBody String joinProc() {
		return "회원가입 완료됨";
	}
	
	@GetMapping("/info")
	@Secured("ROLE_ADMIN")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	@GetMapping("/data")
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
	public @ResponseBody String data() {
		return "데이터정보";
	}
	
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(
			Authentication authentication,
//			@AuthenticationPrincipal UserDetails userDetails
			@AuthenticationPrincipal PrincipalDetails principalUserDetails
			) {
		// Google로 로그인했을 때 로그인이 안됨.. PrincipalDetails<- 이걸로 받을 수가 없다?
		// 그래서 /test/oauth/login을 새로 만듦
		PrincipalDetails pDetails = (PrincipalDetails)authentication.getPrincipal(); 
		System.out.println("authentication : " + pDetails.getUser());
		System.out.println(principalUserDetails.getUser());
		return "세션정보 확인하기";
	}
	
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOauthLogin(
			Authentication authentication,
			@AuthenticationPrincipal OAuth2User oauth
			) {
		System.out.println("/test/oauth/login");
		
		
		OAuth2User oAuth2User= (OAuth2User)authentication.getPrincipal();
		System.out.println("oauth2User : " + oauth.getAttributes());
		System.out.println("authentication : " + oAuth2User.getAttributes());
		return "OAuth 세션정보 확인하기";
	}	
	
}
