package com.canesblack.spring_project1.config;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Configuration
@EnableWebSecurity
//SpringSecurity기능을 사용하려면 이 어노테이션을 써줘야한다.
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
		//스프링시큐리티 기능을 사용하고자 할때 이 메소드 안에서 작성한다.
		http
		.csrf(csrf->csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))	
		//csrf해킹기법으로 보호조치를 하는 코드방법, => 나중에 따로 HTML,jsp자바스크립트에다가 csrf기능도 넣어놓을 것
		.cors(cors->cors.configurationSource(corsCorsfiConfigurationSource()))
		//cors는 특정서버로만 데이터를 넘길수 있도록 설정할 수 잇씁니다. 
		.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
		//세션설정
		.authorizeHttpRequests(authz->authz.requestMatchers("/","/loginPage","/logout","/noticeCheckPage","/registerPage","/menu/all")
				.permitAll()
				.requestMatchers(HttpMethod.POST,"/login","/register").permitAll()
				.requestMatchers("/resources/**","/WEB-INF/**").permitAll()
				.requestMatchers("/noticeAddPage","/noticeModifyPage").hasAnyAuthority("ADMIN","MANAGER")
				.requestMatchers(HttpMethod.POST,"/menu/add").hasAnyAuthority("ADMIN","MANAGER")
				.requestMatchers(HttpMethod.POST,"/menu/update").hasAnyAuthority("ADMIN","MANAGER")
				.requestMatchers(HttpMethod.DELETE,"/menu/delete").hasAnyAuthority("ADMIN","MANAGER")
				.anyRequest().authenticated()//로그인을 해야지만 접근이 가능하게끔! 그렇기 떄문에 로그인페이지로 자동이동됩니다.
				)
		
		
		
		.formLogin(
			//url을 작성해서 로그인페이지로 이동할때 
			login->login.loginPage("/loginPage")
			.loginProcessingUrl("/login")
			.failureUrl("/loginPage?error=true")
			.usernameParameter("username")
			.passwordParameter("password")
			.successHandler(authenticationSuccessHandler())
			.permitAll()
			)
		
		
		.logout(logout->logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))//logout URL을 통해서 로그아웃이 이뤄짐
			.logoutSuccessUrl("/")//로그아웃 성공후 이 url로 리다이렉팅
			.invalidateHttpSession(true)//세션무효화 => 세션공간안에 있던 데이터가 사라짐
			.deleteCookies("JSESSIONID")//쿠키삭제
			.permitAll()//위 기능을 수행시키려면 이 메서드 실행
			);
		
		
		
		
		return http.build();
		//최종 http에 적용시킬때 사용하는 메서드 입니다. 
		
	}
	
	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new SimpleUrlAuthenticationSuccessHandler(){
			
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				// 로그인이 성공했을때 우리가 특별한 기능을 넣고 싶을떄 (세션,권한 기능)
				HttpSession session=request.getSession();//세션 기능을 가지고 온것
				boolean isManager =authentication.getAuthorities().stream()
						.anyMatch(grantedAuthoirity->
						grantedAuthoirity.getAuthority().equals("ADMIN")||
						grantedAuthoirity.getAuthority().equals("MANAGER"));
					if(isManager) {
						session.setAttribute("MANAGER",true);
					}
					session.setAttribute("username", authentication.getName());
					//세션에다가 로그인한 아이디를 저장한다.
					session.setAttribute("isAuthenticated",true);
					//세션에다가 로그인됬냐 여부를 저장
					//request.getContextPath()=>localhost:8080
					response.sendRedirect(request.getContextPath()+"/");
					super.onAuthenticationSuccess(request, response, authentication);
			}
		};
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	
	
	
	@Bean
	public org.springframework.web.cors.CorsConfigurationSource corsCorsfiConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080","https://localhost:8080"));
		//localhost:8080서버에서는 프론트에서 백엔드단 혹은 벡엔드단에서 프론트단인데 데이터를 주고받을수있게 만든것
		configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));
		org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
