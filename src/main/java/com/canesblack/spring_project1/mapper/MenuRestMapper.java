package com.canesblack.spring_project1.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; 
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.canesblack.spring_project1.entity.Menu;


@Mapper
public interface MenuRestMapper {
	
	// 🔽 [수정] getLists 메서드 수정
	// 1. @Param("search") String search 파라미터 추가
	// 2. @Select 쿼리를 <script> 태그로 감싸 동적 SQL로 변경
	// 3. <where> 태그와 <if> 태그를 사용해 검색 조건(title, content) 추가
	@Select("<script>"
			+ "SELECT idx,memID,title,content,writer,indate,count FROM backend_spring_project.menu" //DB에서 읽어오기 
			+ " <where>"
			+ "  <if test='search != null and search != \"\"'>" // 👈 search 파라미터가 비어있지 않다면 실행 
			+ "    (title LIKE CONCAT('%', #{search}, '%') OR content LIKE CONCAT('%', #{search}, '%'))"
			+ "  </if>" //제목이나 내용에 search 칸에 들어간 값이 넘어오면 조건이 발동된다. 만약 검색어가 없으면 생략되고 전체 목록 조회
			+ " </where>"
			+ " ORDER BY idx DESC LIMIT #{limit} OFFSET #{offset}" //게시글 번호(idx)를 기준으로 최신순 정렬, 한 번에 가져올 글 수 만큼 제한, 시작위치 지정 
			+ "</script>")
	public List<Menu> getLists(@Param("offset") int offset, @Param("limit") int limit, @Param("search") String search);
	//실제 동작 예시 search 가 '공지' 일때 제목이나 내용에서 '공지' 가 들어간 게시글만 반환 
	// search가 빈값일때 모든 게시글 반환 
	
	
	// 🔽 [수정] getTotalCount 메서드 수정
	// 1. @Param("search") String search 파라미터 추가
	// 2. @Select 쿼리를 <script> 태그로 감싸 동적 SQL로 변경
	// 3. getLists와 "동일한" <where> 조건을 추가 (이게 틀리면 페이지 계산이 망가짐)
	@Select("<script>"
			+ "SELECT COUNT(*) FROM backend_spring_project.menu" //테이블에 들어있는 레코드(게시글)의 총 개수를 세어 반환한다.
			+ " <where>"
			+ "  <if test='search != null and search != \"\"'>" //search라는 파라미터가 null이 아니고, 빈 문자열이 아니라면(if문 동작)
			+ "    (title LIKE CONCAT('%', #{search}, '%') OR content LIKE CONCAT('%', #{search}, '%'))" // 제목 또는 내용에 search가 포함된 레코드만 대상으로 카운트함(즉, 검색 결과의 개수만 셈).
			+ "  </if>"
			+ " </where>"
			+ "</script>")
	public int getTotalCount(@Param("search") String search);
	
	
	// --- (이하 기존 코드 동일) ---
	
	@Insert("INSERT INTO backend_spring_project.menu(memID,title,content,writer,indate)VALUES(#{memID},#{title},#{content},#{writer},#{indate})")
	public void boardInsert(Menu menu);
	
	@Select("SELECT idx,memID,title,content,writer,indate,count FROM backend_spring_project.menu WHERE idx=#{idx}")
	public Menu boardContent(int idx);
	
	@Delete("DELETE FROM backend_spring_project.menu WHERE idx =#{idx}")
	public void boardDelete (int idx);
	
	@Update("UPDATE backend_spring_project.menu SET title=#{title},content=#{content},writer=#{writer} WHERE idx=#{idx}")
	public void boardUpdate(Menu menu);
	
	@Update("UPDATE backend_spring_project.menu SET count=count+1 WHERE idx=#{idx}")
	public void boardCount(int idx);
	
}
