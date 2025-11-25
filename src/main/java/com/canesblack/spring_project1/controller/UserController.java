package com.canesblack.spring_project1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam; 

import com.canesblack.spring_project1.entity.Role;
import com.canesblack.spring_project1.entity.User;
import com.canesblack.spring_project1.service.UserService;
import com.canesblack.spring_project1.service.RecaptchaService;


@Controller				//PageController는 단순 페이지 이동을 담당하기 때문에 실제 데이터 처리를 담당하는 건 UserController
public class UserController {

	@Autowired
	private UserService userService;	//userService 불러온다.

	@Autowired
	private PasswordEncoder passwordEncoder;	//비밀번호 암호와 불러온다.
	
	@Autowired
	private RecaptchaService recaptchaService; //  캡챠 서비스 불러온다.
	
	
	@PostMapping("/register")	//register.jsp 파일의 <form action="/register" method="post"> 요청을 이 메서드가 처리한다
	public String register(@ModelAttribute User user,
							@RequestParam("g-recaptcha-response") String recaptchaToken) {	 //  캡챠 토큰 받기
	//register.jsp 파일에서 전송된 username,password,writer를 User 객체를 생성해 필드에 매핑한다.
		
		//  캡챠 검증
		boolean isHuman = recaptchaService.verify(recaptchaToken);
		if (!isHuman) {
		// 캡챠 검증 실패 시 로그인 페이지로 튕겨내거나 에러 페이지로 보냄
		// (더 좋은 방법은 다시 회원가입 페이지로 에러 메시지와 함께 보내는 것이지만, 일단 리다이렉트 처리)
		return "redirect:/registerPage?error=captcha"; 
		}
		
		
		
		
		//  회원가입 로직
		String userPassword=user.getPassword();
		System.out.println("userPassword:"+userPassword);
		user.setRole(Role.MEMBER);	//기본 권한을 member로 DB에 저장한다.
		String passwordEncoded=passwordEncoder.encode(userPassword);	//비번 암호화
		user.setPassword(passwordEncoded);
		userService.insertUser(user);	//User 객체를 UserService를 통해 UserMapper로 전달해 DB에 insert 한다
		return"redirect:/loginPage"; 	
	}
	
}
