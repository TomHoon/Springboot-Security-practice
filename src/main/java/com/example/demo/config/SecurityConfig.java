package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.config.oauth.PrincipalOauth2UserService;

@Configuration
@EnableWebSecurity
// secure 어노테이션 활성화 ex) /info
// preAuthorize 활성화 ex) /data 
// 둘의 차이는 role 갯수인 듯 하다.
// info는 하나만, data는 매니저, 어드민 둘 다 일때 가능하게 만들 수 있음
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled = true) 
// 기본 스프링 프레임워크에 필터가 존재한다.
// 기존 스프링 프레임워크 필터 + 시큐리티 필터를 더해주는 것이 바로
// EnableWebSecurity 이다
// 내 입맛에 맞게 security filter를 고치는 곳이다.
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserSvc;
	
	// 해당 메서드의 리턴되는 오브젝트를 IOC로 넣는다.
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
		.antMatchers("/user/**").authenticated()
		.antMatchers("/manager/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
		.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
		.anyRequest().permitAll()
		.and()
		.formLogin()
		.loginPage("/loginForm")
//		.usernameParameter("username2")
		.loginProcessingUrl("/login") //login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해줍니다
		.defaultSuccessUrl("/")
		
		.and()
		.oauth2Login()
		.loginPage("/loginForm") // 구글 로그인 이후 getRole을 통해 권한 체크를 해야하는 후처리가 필요함
		/**
		 * 
		 * (카카오에서는)
		 * 1. 코드 받기
		 * 2. 액세스 토큰 받기 (권한이 생겼다는 것)
		 * ---------------------------------------------------------
		 * 1,2. (구글에서는) 코드는 안받고, (액세스토큰 + 사용자프로필) 정보를 한꺼번에 가져옴. 굉장히 편리!
		 * 3. 사용자 프로필을 가져옴
		 * 4. 그 정보를 토대로 회원가입을 자동으로 진행시킴
		 * 4-1. 쇼핑몰이라면 주소, 이메일 등 다양한 정보가 필요함.
		 * 4-2. 추가적인 정보를 기입할 폼을 보여줘야함
		 * 4-3. 만약 필요 없고 그냥 기본 정보로만 가입시켜도 된다면 바로 회원가입 처리 완료됨!
		 */
		.userInfoEndpoint()
		.userService(principalOauth2UserSvc);
		
	}
}
