package com.canesblack.spring_project1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.canesblack.spring_project1.entity.Role;
import com.canesblack.spring_project1.entity.User;
import com.canesblack.spring_project1.service.UserService;
@Controller				//PageController는 단순 페이지 이동을 담당하기 때문에 실제 데이터 처리를 담당하는 건 UserController
public class UserController {

	@Autowired
	private UserService userService;	//userService 불러온다.

	@Autowired
	private PasswordEncoder passwordEncoder;	//비밀번호 암호와 불러온다.
	
	@PostMapping("/register")	//register.jsp 파일의 <form action="/register" method="post"> 요청을 이 메서드가 처리한다
	public String register(@ModelAttribute User user) {	//register.jsp 파일에서 전송된 username,password,writer를 User 객체를 생성해 필드에 매핑한다.
		String userPassword=user.getPassword();
		System.out.println("userPassword:"+userPassword);
		user.setRole(Role.MEMBER);	//기본 권한을 member로 DB에 저장한다.
		String passwordEncoded=passwordEncoder.encode(userPassword);	//비번 암호화
		user.setPassword(passwordEncoded);
		userService.insertUser(user);	//User 객체를 UserService를 통해 UserMapper로 전달해 DB에 insert 한다
		return"redirect:/loginPage"; 	
	}
	
	
	
	
}
