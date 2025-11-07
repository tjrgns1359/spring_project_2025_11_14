<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>카네스블랙카페</title>
<meta name="_csrf"content="${_csrf.token}">
<meta name="_csrf_header"content="${_csrf.headerName}">

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/noticeAdd/style.css">
</head>
<body>

	<%@include file="/WEB-INF/views/common/header.jsp" %>

<!--javascript코드로 form태그의 acton기능을 대신하는 기능을 만들겁니다.
일종의 restapi개발방식의 일부분이다. -->
		<form id="menuForm">
			<div id=container>
				<div id="menuAdmin">
					<h2 id="menuAdminH2">공지사항 작성</h2>
					<br>
					<label for="memID">회원ID</label>
					<input type="text" id="memID" name="memID" placeholder="회원아이디" maxlength="20" value=" ${username}" readonly>
					<br>
					<label for="title">제목</label>
					<input type="text" id="title" name="title" placeholder="제목" maxlength="20" >
					<br>
					<label for="title">내용</label>
					<input type="text" id="content" name="content" placeholder="내용" maxlength="50" >
					<br>
					<label for="title">작성자</label>
					<input type="text" id="writer" name="writer" placeholder="작성자" maxlength="10" value="${writer}" readonly>
					<br>
					<input type="hidden" id="indate" name="indate" >
					
					
					<button type="button" id="buttonSubmit">확인</button>
				</div>
			</div>
		</form>



	<%@include file="/WEB-INF/views/common/footer.jsp" %>
	
	
<script src="${pageContext.request.contextPath}/resources/js/noticeAdd/script.js"></script>
</body>
</html>