package com.example.demo.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{

//	@Autowired
//	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	// 구글 로그인 이후에 후처리 되는 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// registration으로 어떤 Oauth로 로그인했는지 확인 가능함
		System.out.println("userRequest getClientId(): " + userRequest.getClientRegistration().getClientId());
		System.out.println("userRequest getClientSecret(): " + userRequest.getClientRegistration().getClientSecret());
		System.out.println("userRequest getClientName(): " + userRequest.getClientRegistration().getClientName());
		System.out.println("userRequest getAccessToken(): " + userRequest.getAccessToken().getTokenValue());
		
		// 우리가 유일하게 필요한 정보는 getAttributes
		/**
		 * sub=110630609917470721949, 
		 * name=이동훈, 
		 * given_name=동훈, 
		 * family_name=이, 
		 * picture=https://lh3.googleusercontent.com/a/AAcHTtehZiZ6iH9JUn12nn3wUjvzCjorKpw3fegt0R-O4I73=s96-c, 
		 * email=gnsdl9079@gmail.com, 
		 * email_verified=true, 
		 * locale=ko
		 * 
		 * username = google_110630609917470721949(중복방지)
		 * password = 암호화(겟인데어) ?? 아... 그냥 구글 로그인할 때 넣어라는 말이구나
		 * email = gnsdl9079@gmail.com
		 * role = ROLE_USER
		 * provider = google
		 * provideId = 110630609917470721949
		 * 
		 */
		
		
		// 구글 로그인 버튼 -> 구글 로그인창 -> 로그인 완료 -> code를 리턴 ( OAuth-Client 라이브러리) -> AccessToken 요청
		// userRequest정보를 얻으려면 loadUser함수 -> 회원프로필
		System.out.println("userRequest getAccessToken(): " + super.loadUser(userRequest).getAttributes());
		
		
		OAuth2User oauth2User = super.loadUser(userRequest);
		
		String provider = userRequest.getClientRegistration().getClientId();
		String providerId = oauth2User.getAttribute("sub");
		String username = provider + "_" + providerId;
//		String password = bCryptPasswordEncoder.encode("겟인데어");
		String email = oauth2User.getAttribute("email");
		String role = "ROLE_USER";

		User userEntity = userRepository.findByUsername(username);
		
		if (userEntity == null) {
			userEntity = User.builder()
					.username(username)
//					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		} else {
			
		}
		
		// 회원가입을 강제로 해볼 예정
		return super.loadUser(userRequest);
	}
}
