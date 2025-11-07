package com.canesblack.spring_project1.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam; 
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.StringUtils; 
import com.canesblack.spring_project1.entity.Menu;
import com.canesblack.spring_project1.service.MenuRestService;

@RestController
public class MenuRestController {
	
	@Autowired
	private MenuRestService menuRestService;
	
	// 🔽 [수정] getAllMenus 메서드 수정..
	// 1. @RequestParam(value = "search", required = false) String search 파라미터 추가
	@GetMapping("/menu/all")
	public ResponseEntity<Map<String, Object>> getAllMenus(
			@RequestParam(value = "page", defaultValue = "1") int page, // 페이지 번호 값을 받고 없으면 1페이지로 처리한다(기본페이지) 
			@RequestParam(value = "search", required = false) String search) { // search 칸에 검색어를 넣으면 그 값을 받고 없으면 null 처리 
		
		// 2. [수정] 서비스에 page와 search 파라미터 전달
		Map<String, Object> response = menuRestService.getList(page, search); //서비스가 해당 페이지와 검색어에 맞는 게시글 목록과 페이징 정보를 Map에 담아 내려줌
		
		// (이하 로직은 기존과 동일)
		List<Menu> menus = (List<Menu>) response.get("menus"); //응답 맵에서 실제 게시글 데이터만 추출
		
		if(menus != null && !menus.isEmpty()){
			return ResponseEntity.ok(response); 
		}else {
			// 검색 결과가 없거나, 해당 페이지에 글이 없는 경우
			// 204 No Content를 반환하면 프론트 script.js에서 에러로 처리될 수 있습니다.
			// [개선] 빈 목록이라도 정상 응답(200 OK)을 보내주는 것이 더 좋습니다.
			return ResponseEntity.ok(response); // 👈 [수정] noContent() 대신 ok(response)를 반환
		}
	}
	
	
	// --- (이하 기존 코드 동일) ---

	//2.메뉴(한개의 게시판 생성)생성
		@PostMapping("/menu/add")
		public ResponseEntity<String>addMenu(@RequestBody Menu menu){
			
			if(!StringUtils.hasText(menu.getIndate())) {  //null, 빈 문자열, 공백만 있는 경우 모두 false로 처리
				menu.setIndate(LocalDate.now().toString());
			}
			
			menu.setCount(0);
			menuRestService.boardInsert(menu);
			return ResponseEntity.ok("게시글 잘 작성됨");
		}
	
	//메뉴(한개의 게시판 수정)수정 
	@PutMapping("/menu/update/{idx}")
	public void updateMenu(@RequestBody Menu menu,@PathVariable("idx")int idx) {
		menu.setIdx(idx);
		menuRestService.boardUpdate(menu);
	}
	
	//4.메뉴(한개의 게시판 삭제)삭제
	@DeleteMapping("/menu/delete/{idx}")
	public void deleteMenu(@PathVariable("idx")int idx) {
		menuRestService.boardDelete(idx);
	}
	
	//5.특정메뉴(한개의 게시판의 내용을 조회)조회
	@GetMapping("/menu/{idx}")
	public ResponseEntity<Menu>getMenuById(@PathVariable("idx")int idx){
		Menu menu=menuRestService.boardContent(idx); 
		if(menu!=null) {
			return ResponseEntity.ok(menu);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//6.특정메뉴(게시판조회수 증가)조회수 증가
	@PutMapping("/menu/count/{idx}")
	public void incrementMenuCount(@PathVariable("idx")int idx) {
		menuRestService.boardCount(idx);
	}
}

