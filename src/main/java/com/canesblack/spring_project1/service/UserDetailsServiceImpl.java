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
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		User user=userMapper.findByUsername(username);
		if(user==null) {
			//데이터가없을시
			throw new UsernameNotFoundException(username+"존재하지않습니다.");
		}
		//로그인했을때 디비에 로그인데이터즉 유저정보가 존재할시에는
		
		return new CustomUser(user);
	}
	
}
