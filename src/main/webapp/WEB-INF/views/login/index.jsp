<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인페이지</title>
<link rel="stylesheet"type="text/css"href="${pageContext.request.contextPath}/resources/css/login/style.css">
</head>
<body>

<%@include file="/WEB-INF/views/common/header.jsp" %>
	<div id="login-container-wrapper">
		<div id="login-container">
			<h2>로그인</h2>
			
			<!-- 로그인 실패시 오류 메시지 표시 -->
			<c:if test="${not empty param.error}" > 
				<p style="color:red;">아이디 또는 비밀번호가 잘못되었습니다.</p>
			
			</c:if>
			
			<!-- localhost:8080/login으로 username=---&password=--- -->
			<form action="${pageContext.request.contextPath}/login" method="post"> 
			
			<!-- CSRF 토큰추가 -->
				<input type="hidden"name="_csrf" value="${_csrf.token}">
			
			<div class="input-group">
				<label for="username">아이디</label>
				<input type="text"id="username"name="username" required/>
			</div>
			<div class="input-group">
				<label for="password">비밀번호</label>
				<input type="password"id="password"name="password" required/>
			</div>
			<button type="submit"id="login-button">로그인</button>
			</form>
			<div id="register-link">
			<!-- localhost:8080/ -->
				<a href="${pageContext.request.contextPath}/registerPage">회원가입</a>
			
			</div>
		</div>
	</div>
	

<%@include file="/WEB-INF/views/common/footer.jsp" %>
</body>
</html>