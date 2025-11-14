package com.canesblack.spring_project1.entity;
// user에서 private Role role을 정의 했는데 이 Role이 뭔지 자바 코드내에서 명확하게 정의하기위해 Role.java 파일을 추가 
public enum Role {
	//enum=>미리 정의되어있는 상수들의 집합, 클래스가 아닌 열거형 타입 , 권한을 string 타입으로 관리하면 
	//오타 admin 이 발생할 수 있기때문에 Role.ADMIN 처럼 정해진 값만 사용하도록 강제
	//권한 명세서라고 생각하자
	ADMIN,MANAGER,MEMBER;
}
