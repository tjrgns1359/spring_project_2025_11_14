package com.canesblack.spring_project1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.canesblack.spring_project1.entity.Menu;
import com.canesblack.spring_project1.entity.User;
import com.canesblack.spring_project1.service.MenuRestService;
import com.canesblack.spring_project1.service.UserService;

import jakarta.servlet.http.HttpServletRequest;



// 사용자가 요청하는 URL을 듣고 필요하면 일꾼들에게 데이터를 들고 오라고 시킨뒤, 모델에 실어서 jsp 파일로 연결해주는 역할을 한다. ->PageController
@Controller        //이 클래스는 컨트롤러 역할을 할거야 라는 라벨이라고 생각 
public class PageController {
	
	@Autowired					//Autowired를 쓰는 이유는 PageController가 페이지를 보여주기 전에 DB에서 게시글 정보나 사용자 정보를 조회해야할 때 컨트롤러가 직접 접근하면 복잡
	private MenuRestService menuRestService;	//그래서 MenuRestService와 UserService라는 객체를 이곳에 불러와 맡기는 것, 일꾼 불러오기 
	
	@Autowired
	private UserService userService;
	
	
	
	@GetMapping("/")   //GetMapping 은 사용자가 웹브라우저 주소창에 특정 URL 입력했을때 어떤 메서드 실행할지, 연결해주는 역할
	public String home() {  //localhost:8080/ 기본 주소로 접속하면 home() 메서드 실행
		return"index";   	//application.properties에서 설정흘 해두었기에 바로 /WEB-INF/views/inde.jsp 파일을 찾아서 보여줌
	}

//페이지를 조회및 이동할때 위와 같이 @GetMapping을 써서 이동합니다.
//  => localhost:8080/register
@GetMapping("/registerPage")	//회원가입 페이지 
public String registerPage(HttpServletRequest request,org.springframework.ui.Model model){
	
	CsrfToken csrfToken = (CsrfToken)request.getAttribute(CsrfToken.class.getName());	//스프링 시큐리티가 발급한 CsrfToken을 request에서 꺼내 model에 _csrf 라는 이름으로 담는 것 
	model.addAttribute("_csrf",csrfToken);				//model은 JSP에게 데이터를 전달할때 사용하는 바구니 개념
	
	return "register/index";
}

@GetMapping("/loginPage")	//로그인 페이지 
public String loginPage(HttpServletRequest request,org.springframework.ui.Model model){
	
	CsrfToken csrfToken = (CsrfToken)request.getAttribute(CsrfToken.class.getName());
	model.addAttribute("_csrf",csrfToken);
	
	return "login/index";
}

	@GetMapping("/noticeAddPage")	//글쓰기 페이지
	public String noticeAddPage(Model model,Authentication authentication) { //authentication 객체는 현재 로그인한 사용자의 정보라고 생각하자 
		String writer=userService.findWriter(authentication.getName()); //authentication.getName()으로 로그인 ID를 가져오고 이걸 userService에 넘겨서 작성자명을 조회하게 된다. 
		model.addAttribute("writer",writer);	//조회한 작성자명을 model에 담아서 jsp로 넘겨주기 
		return"noticeAdd/index";
	}
	
	@GetMapping("/noticeCheckPage") //조회 페이지
	public String showNoticeCheckPage(@RequestParam("idx") int idx,Model model ) { //@RequestParam("idx") int idx 코드는 URL 주소에 ?idx=5처럼 숫자 5를 idx라는 정수형 변수에 자동으로 넣는다.
		Menu menu= menuRestService.boardContent(idx);	//이때 이 idx값을 menuRestService로 넘겨서 해당번호의 게시글 데이터를 통째로 DB에서 들고온다.
		model.addAttribute("menu",menu);	//model로 담아 jsp로 보낸다
		return "noticeCheck/index";
	}
	@GetMapping("/noticeModifyPage")	//수정 페이지
	public String showNoticeModifyPage(@RequestParam("idx") int idx,Model model ) {
		Menu menu= menuRestService.boardContent(idx);
		model.addAttribute("menu",menu);
		return "noticeModify/index";
	}

}