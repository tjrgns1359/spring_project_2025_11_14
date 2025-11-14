package com.canesblack.spring_project1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.canesblack.spring_project1.entity.User;
import com.canesblack.spring_project1.mapper.UserMapper;

//컨트롤러가 매퍼를 직접 호출하면 역할 분담에 모호해진다. 
//그래서 서비스(Service)계층은 두는 것
@Service	//서비스 계층임을 알려 Autowired로 사용 가능하게 
public class UserService {
//의존성 주입
	@Autowired
	private UserMapper userMapper; 	//이전에 만든 UserMapper를 불러온다.
	
	public void insertUser(User user) { //User 컨트롤러로 부터 user 객체를 받아 insertUser 매서드로 전달한다.
	userMapper.insertUser(user);
	}
	
	public String findWriter(String username) {	//결과를 다시 반환
		return userMapper.findWriter(username);	
	}
	
	
}
