<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="_csrf"content="${_csrf.token}">
<meta name="_csrf_header"content="${_csrf.headerName}">
<title>카네스블랙 카페</title>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/common/header.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/common/footer.css">
<link rel="stylesheet"type="text/css"href="${pageContext.request.contextPath}/resources/css/style.css">

</head>
<body>

<%@include file="/WEB-INF/views/common/header.jsp" %>    <%-- include 라는 jsp 태그로 헤더 조립 --%>

<div id="container">
	<div id="menuAdmin">
		<h2 id="menuAdminH2">공지사항</h2>
		
		<%-- c:if 써서 manger 권한이 있을때만 작성 버튼이 활성화--%>
		<c:if test="${MANAGER==true}">     
			<button type="button" onclick="location.href=`${pageContext.request.contextPath}/noticeAddPage`">작성</button>
		</c:if>
 
		<table class="notice-table">
			<thead>		<%--테이블 헤더--%>
				<tr>
					<th style="width: 50%;">제목</th>
					<th style="width: 15%;">작성자</th>
					<th style="width: 20%;">작성일</th>
					<th style="width: 15%;">조회수</th>
				</tr>
			</thead>
			
			<%-- script.js가 테이블 본문(tbody)을 타겟팅할 수 있도록, tbody에 id="menuList"를 부여한다. --%>
			<tbody id="menuList">
				<%-- (script.js가 채울 것) --%>
			</tbody>
		</table>
	
		
		
	
		<%-- 페이지네이션 컨테이너 구조 --%>
		<div id="pagination-container">
			
			<!-- 1. 검색창 -->
			<div id="search-box">
				<input type="text" id="search-input" placeholder="제목 + 내용 검색">
				<button type="button" id="search-button">검색</button>
			</div>
			
			<!-- 2. 페이지 버튼 -->
			<div id="pagination-buttons">
				<%-- (script.js가 채울 것) --%>
			</div>
			
		</div>
	</div>
</div>

<%@include file="/WEB-INF/views/common/footer.jsp" %>      <%-- include 라는 jsp 태그로 푸터 조립 --%>

<script type="text/javascript"src="${pageContext.request.contextPath}/resources/js/script.js"></script>
</body>
</html>