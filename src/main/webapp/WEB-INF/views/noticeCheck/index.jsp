<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항조회</title>
<meta name="_csrf"content="${_csrf.token}">
<meta name="_csrf_header"content="${_csrf.headerName}">


<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/common/header.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/common/footer.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/noticeCheck/style.css">
</head>
<body>

 <%@include file="/WEB-INF/views/common/header.jsp" %>

  <!--공지사항 조회폼 -->
		<form id="menuForm">
			<div id="container">
				<div id="menuAdmin">
					<h2 id="menuAdminH2">공지사항 조회</h2>
					
					<input type="hidden" id="idx" name="idx" value="${menu.idx}">
					<input type="hidden" id="indate" name="indate" value="${menu.indate}" >
					
					
					
					<%-- 회원ID와 작성자를 묶는 상단 메타 그룹 --%>
					<div class="form-meta-group">
						<div class="meta-item">
							<label for="memID">회원ID</label>
							<input type="text" id="memID" name="memID" value="${menu.memID}" readonly>
						</div>
						<div class="meta-item">
							<label for="writer">작성자</label>
							<input type="text" id="writer" name="writer" value="${menu.writer}" readonly>
						</div>
					</div>
					<%-- 메타 그룹 종료 --%>

					<label for="title">제목</label>
					<input type="text" id="title" name="title" value="${menu.title}" readonly>
					<br>
					
					<label for="content">내용</label>
					<textarea id="content" name="content" rows="15" readonly>${menu.content}</textarea>
					<br>
					
					<div id="buttonContainer">
						<c:if test="${MANAGER == true}">
							<button type="button" id="buttonUpdate">수정</button>
							<button type="button" id="buttonDelete">삭제</button>
						</c:if>
					</div>
					
				</div>
			</div>
		</form>

	<%@include file="/WEB-INF/views/common/footer.jsp" %>
	
<script src="${pageContext.request.contextPath}/resources/js/noticeCheck/script.js"></script>

</body>
</html>