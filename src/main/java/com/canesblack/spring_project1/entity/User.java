package com.canesblack.spring_project1.entity;

//실제 데이터를 담을 그릇 ->User Entity , 이 클래스는 MVC(Model-View-Controller) 패턴의 'Model' 영역
//DB의 user 테이블과 1:1로 매핑되는 데이터 객체(Entity)
public class User {
		
	private int idx;
	private String username;
	private String password;
	private String writer;
	private Role role;
	private String postcode;
	private String address1;
	private String address2;
	private String phone; 
	
	
	public User() {}
	
	
	public User(int idx, String username, String password, String writer, Role role, String postcode, String address1, String address2, String phone) {	
		this.idx = idx;
		this.username = username;
		this.password = password;
		this.writer = writer;
		this.role = role;
		this.postcode = postcode;
		this.address1 = address1;
		this.address2 = address2;
		this.phone = phone; 
	}

// 모든 private 필드에 외부에서 접근(값을 읽거나 설정) 할 수 있도록 접근 제한자의 getter와 setter 메서드를 모두 정의하였다. 
	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password; 
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}