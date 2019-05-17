<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="com.cafe24.mysite.vo.UserVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.servletContext.contextPath }/assets/css/user.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url='/WEB-INF/views/includes/header.jsp'/>
		<div id="content">
			<div id="user">

				<form id="update-form" name="updateForm" method="post" action="${pageContext.servletContext.contextPath }/user/update">
				<input type="hidden" name="no" value="${no }">
					<label class="block-label" for="name">이름</label>
					<input id="name" name="name" type="text" value="${vo.name }">
					<label class="block-label" for="email">이메일</label>
					<input id="email" name="email" type="text" value="${vo.email }">
					<label class="block-label">패스워드</label>
					<input name="pw" type="password" value="${vo.pw }">
					
					<fieldset>
						<legend>약관동의</legend>
						<input id="agree-prov" type="checkbox" name="agreeProv" value="y">
						<label>서비스 약관 동의</label>
					</fieldset>
					<input type="submit" value="수정">
				</form>
			</div>
		</div>
		<c:import url='/WEB-INF/views/includes/navigation.jsp'>
			<c:param name="menu" value="main"/>
		</c:import>
		<c:import url='/WEB-INF/views/includes/footer.jsp'/>
	</div>
</body>
</html>