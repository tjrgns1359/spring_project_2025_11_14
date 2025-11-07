package com.canesblack.spring_project1.service;

import java.util.HashMap; 
import java.util.List;
import java.util.Map; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canesblack.spring_project1.entity.Menu;
import com.canesblack.spring_project1.mapper.MenuRestMapper;

@Service
public class MenuRestService {

	@Autowired
	private MenuRestMapper menuRestMapper;
	
	// 🔽 [수정] getList 메서드 수정
	// 1. String search 파라미터 추가
	public Map<String, Object> getList(int pageNum, String search) { // 사용자가 요청한 페이지 번호(pageNum)와 검색어(search)를 동시에 전달받음
		
		int pageSize = 10;  //10개의 게시물이 1페이지에 쌓이게 
		int offset = (pageNum - 1) * pageSize; // 예를 들어 2페이지면 offset = 10, 3페이지면 offset = 20
		
		// 1. [수정] Mapper에 search 파라미터 전달
		List<Menu> menus = menuRestMapper.getLists(offset, pageSize, search); //Mapper에 offset, pageSize, 검색어를 전달하여 해당 페이지의, 조건에 맞는 게시글 리스트 반환

		
		// 2. [수정] Mapper에 search 파라미터 전달
		int totalCount = menuRestMapper.getTotalCount(search);//Mapper에 검색어 파라미터 보내서, 조건에 맞는 총게시글 개수를 반환
		
		// 3. 총 페이지 수 계산
		int totalPages = (int) Math.ceil((double) totalCount / pageSize); //전체 개수를 페이지당 게시글 수로 나누고 올림 처리
		
		// 4. Map에 담아 컨트롤러로 반환
		Map<String, Object> response = new HashMap<>();
		response.put("menus", menus); 
		response.put("totalPages", totalPages); 
		response.put("currentPage", pageNum); 
		
		return response;
	}
	
	// (boardContent 메서드는 수정 없음)
	public Menu boardContent(int idx) {
		return menuRestMapper.boardContent(idx);
	}
	
	
	// --- (이하 기존 코드 동일) ---
	// 게시글 생성: Menu 객체를 받아 새 게시글을 DB에 등록합니다.
	public void boardInsert(Menu menu) {
		menuRestMapper.boardInsert(menu);
	}
	// 게시글 삭제: 게시글의 고유번호(idx)를 받아 해당 글을 DB에서 삭제합니다.
	public void boardDelete(int idx) {
		menuRestMapper.boardDelete(idx);
	}
	// 게시글 수정: Menu 객체를 받아 해당 글의 내용을 DB에서 수정합니다.
	public void boardUpdate(Menu menu) {
		menuRestMapper.boardUpdate(menu);
	}
	// 조회수 증가: 게시글의 고유번호(idx)를 받아 해당 글의 조회수를 1 증가시킵니다.
	public void boardCount(int idx) {
		menuRestMapper.boardCount(idx);
	}
	
}
