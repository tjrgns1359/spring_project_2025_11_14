package com.canesblack.spring_project1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.canesblack.spring_project1.entity.CustomUser;
import com.canesblack.spring_project1.entity.User;
import com.canesblack.spring_project1.mapper.UserMapper;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	UserMapper userMapper;
	
	
	@Override  // 스프링 시큐리티가 로그인 요청을 가로챘을 때 자동으로 구현하도록 강제
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		User user=userMapper.findByUsername(username);	//Autowired로 UserMapper를 불러 왔기때문에 findByUsername(username)를 호출한다. 즉 DB 유저 테이블에서 해당 아이디의 회원 정보를 조회
		if(user==null) {
			//데이터가없을시
			throw new UsernameNotFoundException(username+"존재하지않습니다.");
		}
		//로그인했을때 디비에 로그인데이터즉 유저정보가 존재할시에는
		
		return new CustomUser(user);	//다음과 같은 코드가 실행된다. 여기서 우리가 만든 user 엔티티를 그대로 반환하는 것이 아니라 CustomUser로 한번 감싸서 반환한다는 것이다.
										//스프링 시큐리티는 우리가 만든 User 엔티티의 구조를 모른다.스프링 시큐리티가 인증에 사용하는 표준 규격은 UserDetails라는 인터페이스이다.
										//따라서 CustomUser 클래스는 우리가 UserMapper를 통해 DB에서 조회한 User 객체의 정보를 UserDetails 규격에 맞게 변환해주는 '어댑터(Adapter)' 역할
	}
	
}
