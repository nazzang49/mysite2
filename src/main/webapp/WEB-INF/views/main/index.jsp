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
<link href="${pageContext.servletContext.contextPath }/assets/css/main.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url='/WEB-INF/views/includes/header.jsp'/>
			<div id="wrapper">
				<div id="content">
					<div id="site-introduction">
						<h2>- CAFE24 교육생 박진영입니다 -</h2>
						<p>
							[웹 프로그램밍 실습과제 예제 사이트]<br>
							메뉴는  사이트 소개/방명록/게시판 및 Python/데이터베이스/웹프로그래밍 수업<br><br>
							<a href="${pageContext.servletContext.contextPath }/guestbook">방명록</a>write now<br>
						</p>
					</div>
				</div>
			</div>
		<c:import url='/WEB-INF/views/includes/navigation.jsp'>
			<c:param name="menu" value="main"/>
		</c:import>
		<c:import url='/WEB-INF/views/includes/footer.jsp'/>
	</div>
</body>
</html>