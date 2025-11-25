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
// JSP 파일,즉 문은 얼추 만들었는데 이 문을 누가 열 수 있는가,언제 열어야 하는가, 권한을 줘야하나 말아야하나 결정하는 기능 -> SecurityConfig
//SpringSecurity기능을 사용하려면 이 어노테이션을 써줘야한다. 스프링시큐리티 기능을 활성화 할게! 라는 뜻 
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
		.authorizeHttpRequests(authz->authz.requestMatchers("/","/loginPage","/logout","/noticeCheckPage","/registerPage","/menu/all") //누가 어디에 들어 갈 수 있는가? 권한 설정
				.permitAll()
				.requestMatchers(HttpMethod.POST,"/login","/register").permitAll()
				.requestMatchers("/resources/**","/WEB-INF/**").permitAll() //로그인 안 한 사용자도 당연히 들어와야하는 페이지들은 permitAll로 다 열어줬다.
				.requestMatchers("/noticeAddPage","/noticeModifyPage").hasAnyAuthority("ADMIN","MANAGER") //글쓰기, 메뉴 수정 같은 기능은 ADMIN,MANAGER로 권한 있는 사용자만 접근
				.requestMatchers(HttpMethod.POST,"/menu/add").hasAnyAuthority("ADMIN","MANAGER")
				.requestMatchers(HttpMethod.POST,"/menu/update").hasAnyAuthority("ADMIN","MANAGER")
				.requestMatchers(HttpMethod.DELETE,"/menu/delete").hasAnyAuthority("ADMIN","MANAGER")
				.anyRequest().authenticated()//로그인을 해야지만 접근이 가능하게끔! 그렇기 떄문에 로그인페이지로 자동이동됩니다.
				)
		
		
		
		.formLogin(					//접근이 제한된 사용자들을 로그인 페이지로 보내라
			//url을 작성해서 로그인페이지로 이동할때 
			login->login.loginPage("/loginPage")
			.loginProcessingUrl("/login")
			.failureUrl("/loginPage?error=true")	//만약 로그인이 실패하면 ?error=ture 라는 꼬리표를 붙여서 보낸다. login.jsp에서 c:if 를 사용해 아이디 비번틀림 메시지를 보여준게 이것이다.
			.usernameParameter("username")
			.passwordParameter("password")
			.successHandler(authenticationSuccessHandler())	//로그인이 성공하면 그냥 메인페이지로 바로가는게 x , 일단 우리가만든 authenticationSuccessHandler()를 실행시켜라
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
	public AuthenticationSuccessHandler authenticationSuccessHandler() {	//로그인이 성공하면 단순히 페이지를 이동시키는게 아니라 세션에 중요한 정보들을 저장하는 작업을 추가했다.
		return new SimpleUrlAuthenticationSuccessHandler(){
			
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				// 로그인이 성공했을때 우리가 특별한 기능을 넣고 싶을떄 (세션,권한 기능)
				HttpSession session=request.getSession();//세션 기능을 가지고 온것
				boolean isManager =authentication.getAuthorities().stream()
						.anyMatch(grantedAuthoirity->
						grantedAuthoirity.getAuthority().equals("ADMIN")||		//권한체크
						grantedAuthoirity.getAuthority().equals("MANAGER"));
					if(isManager) {
						session.setAttribute("MANAGER",true);		//index.jsp에서 c:if로 true 일때 작성버튼 보여줄지 말지 결정하는 것임
					}
					session.setAttribute("username", authentication.getName());
					//세션에다가 로그인한 아이디를 저장한다.
					session.setAttribute("isAuthenticated",true);	//마찬가지로 header.jsp에서 c:if로 로그인/로그아웃 버튼 보여줄지 말지 결정할때 
					//세션에다가 로그인됬냐 여부를 저장
					//request.getContextPath()=>localhost:8080
					response.sendRedirect(request.getContextPath()+"/");		//모든 세션작업을 마친 후에 사용자를 메인페이지로 보낸다.
					super.onAuthenticationSuccess(request, response, authentication);
			}
		};
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {	//비밀번호를 암호화, Bean으로 등록해 프로젝트 전역에서  BCryptPasswordEncoder()암호화 방식을 쓸 수 있게
		return new BCryptPasswordEncoder();
	}
	
	
	
	
	
	@Bean
	public org.springframework.web.cors.CorsConfigurationSource corsCorsfiConfigurationSource() {	//누구랑 대화할지 정하는 규칙. 주소가 다르더라도 이 주소에서 오는 요청은 믿을 수 있으니 허락해줘
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080","https://localhost:8080"));
		//localhost:8080서버에서는 프론트에서 백엔드단 혹은 벡엔드단에서 프론트단인데 데이터를 주고받을수있게 만든것
		configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));
		org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration); //모든 URL에 대해 설정한 CORS 귲칙을 적용하겠다.
		return source;
	}
}
