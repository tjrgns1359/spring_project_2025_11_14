<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet"type="text/css"href="${pageContext.request.contextPath}/resources/css/register/style.css">
</head>
<body>

<%@include file="/WEB-INF/views/common/header.jsp" %>

<div id="register-container-wrapper">
	<div class="register-container">
		<h2>회원가입</h2>
		<form action="${pageContext.request.contextPath}/register" method="post">
			<!-- CSRF 토큰추가 -->
				<input type="hidden"name="_csrf" value="${_csrf.token}">
		
			<div class="input-group">
				<label for="username">아이디</label>
				<input type="text" id="username" name="username" required>
			</div>
			<div class="input-group">
				<label for="password">비밀번호</label>
				<input type="password" id="password" name="password" required>
			</div>
			<div class="input-group">
				<label for="writer">작성자</label>
				<input type="text" id="writer" name="writer" required>
			</div>
			<div class="input-group">
				<button type="submit" class="register-button">회원가입</button>
			</div>
		</form>
	</div>
</div>

<%@include file="/WEB-INF/views/common/footer.jsp" %>

</body>
</html>