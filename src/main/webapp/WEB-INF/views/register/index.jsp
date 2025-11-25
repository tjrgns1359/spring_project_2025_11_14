<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title> 


<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/common/header.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/common/footer.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/register/style.css">

<!-- Daum 주소 찾기 API 스크립트 -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<!-- 구글 reCAPTCHA API 스크립트 -->
<script src="https://www.google.com/recaptcha/api.js" async defer></script>

</head>
<body>

<%@include file="/WEB-INF/views/common/header.jsp" %>

<div id="register-container-wrapper">
	<div class="register-container">
		<h2>회원가입</h2>
		
		<%-- 에러 메시지 표시 (캡챠 실패 시) --%>
		<c:if test="${param.error == 'captcha'}">
			<p style="color:red; font-size:14px; margin-bottom:10px;">캡챠 인증에 실패했습니다. 다시 시도해주세요.</p>
		</c:if>
		
		<form id="register-form" action="${pageContext.request.contextPath}/register" method="post">
			
			<input type="hidden" name="_csrf" value="${_csrf.token}">
		
			<div class="input-group">
				<label for="username">아이디</label>
				<input type="text" id="username" name="username" required>
			</div>
			<div class="input-group">
				<label for="password">비밀번호</label>
				<input type="password" id="password" name="password" required>
			</div>
			
		
			<div class="input-group">
				<label for="password-confirm">비밀번호 확인</label>
				<input type="password" id="password-confirm" name="password-confirm" required>
			</div>
			
			<%-- 비밀번호 불일치 시 에러 메시지를 표시할 영역 --%>
			<div id="password-error" class="error-message"></div>
			
			
			<div class="input-group">
				<label for="writer">작성자 (닉네임)</label> 
				<input type="text" id="writer" name="writer" required>
			</div>
			
			
			<div class="input-group">
				<label for="phone">전화번호</label> 
				<input type="tel" id="phone" name="phone" placeholder="010-1234-5678">
			</div>
			
			
			<hr class="divider">
			
			<%-- 주소 입력 그룹 --%>
			<div class="input-group postcode-group">
				<label for="postcode">우편번호</label>
				<input type="text" id="postcode" name="postcode" placeholder="우편번호" readonly>
				<button type="button" id="btn-postcode">주소 검색</button>
			</div>
			<div class="input-group">
				<label for="address1">주소</label>
				<input type="text" id="address1" name="address1" placeholder="주소" readonly>
			</div>
			<div class="input-group">
				<label for="address2">상세주소</label>
				<input type="text" id="address2" name="address2" placeholder="상세주소를 입력하세요">
			</div>
			
			<hr class="divider">
			
			<%-- 구글 캡챠 위젯 (사이트 키 사용) --%>
			<div class="input-group" style="display:flex; justify-content:center; margin-top:10px;">
				<div class="g-recaptcha" data-sitekey="6Lc1PxEsAAAAAImnzCGy1axKbCBlPFMf_4vLKapr"></div>
			</div>
			
			
			<div class="input-group">
				<button type="submit" class="register-button">회원가입</button>
			</div>
			
			<div class="login-link">
				<a href= "${pageContext.request.contextPath}/loginPage">이미 계정이 있으신가요?</a>
			</div>
			
		</form>
	</div>
</div>

<%@include file="/WEB-INF/views/common/footer.jsp" %>

<script src="${pageContext.request.contextPath}/resources/js/register/script.js"></script>

</body>
</html>