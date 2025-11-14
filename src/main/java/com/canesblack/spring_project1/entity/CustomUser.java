package com.canesblack.spring_project1.entity;

import org.springframework.security.core.authority.AuthorityUtils;

public class CustomUser extends org.springframework.security.core.userdetails.User { //스프링 시큐리티가 기본으로 제공하는 User 클래스를 상속한다.
																					//이 부모 클래스는 UserDetails 인터페이스를 이미 구현하고 있다.
	private User user;																//따라서 우리는 UserDetails의 모든 추상 메서드(like getAuthorities(), getUsername() 등)를 직접 구현할 필요 없이, 
																					//부모의 생성자만 잘 호출해 주면 된다.
	
	public CustomUser(User user) {	//DB 정보가 담긴 우리의 User 엔티티를 파라미터로 받는다.
		super(user.getUsername(),user.getPassword(),AuthorityUtils.createAuthorityList(user.getRole().toString()));	//부모 생성자 호출, username,password,authorities(권한목록)을 UserDetails에 전달한다.

	}
	
}
