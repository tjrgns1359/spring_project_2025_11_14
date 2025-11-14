<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet"type="text/css"href="${pageContext.request.contextPath}/resources/css/common/header.css">
</head>
<body>

<div id="header">
	<div style="color:white;text-align:left;float:left;cursor:pointer"onclick="location.href=`${pageContext.request.contextPath}/`">
		CanesblackCafe
	</div>
	
	<c:choose>		<%-- c:choose태그를 써서 isAuthenticated라는 변수에 true값을 실어주면 로그아웃 버튼을 그렇지 않으면 로그인 버튼을  --%>
		<c:when test="${isAuthenticated!=null&& isAuthenticated==true}">
			<div style="float:right;">
			<!-- localhost:8080/logout -->
				<a href="${pageContext.request.contextPath}/logout"style="color:white;margin-right:15px;text-decoration:none;font-size:15px;">로그아웃</a>
			</div>
		</c:when>
		<c:otherwise>
			<div style="float:right;">
			<!-- localhost:8080/logout -->
				<a href="${pageContext.request.contextPath}/loginPage"style="color:white;margin-right:15px;text-decoration:none;font-size:15px;">로그인</a>
			</div>
		</c:otherwise>
	</c:choose>
	
	
</div>

</body>
</html>