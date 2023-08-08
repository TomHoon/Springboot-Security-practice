package com.example.demo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

// 시큐리티 설정에서 loginProcessingUrl("/login");
// login 요청이 오면 자동으로 UserDetailsService 타입으로 IOC 되어 있는
// loadUserByUsername 함수가 실행. 
// 이것은 그냥 규칙이다.

// 그러니까 PrincipalDetails 에서 UserDetails를 우리가 입맛에 맞게 정의했다.
// 로그인을 한 뒤 security Config에 설정한 내용 대로 
// 실행이 되면 해당 값들을 PrincipalDetails에서 정의한다.

// 그리고
// 정의한 유저 데이터 한 덩어리가
// loadUserByUsername의 메소드에서 사용된다.

@Service
public class PrincipalDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	// 시큐리티 session => Authentication => UserDetails
	// => session(내부 Authentication(내부 UserDetails))
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userEntity = userRepository.findByUsername(username);
		
		if (userEntity != null) {
			return new PrincipalDetails(userEntity);
		}
		return null;
	}

}
