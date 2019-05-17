<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="com.cafe24.mysite.vo.UserVO"%>
<%@page import="com.cafe24.mysite.vo.GuestBookVO"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.servletContext.contextPath }/assets/css/guestbook.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url='/WEB-INF/views/includes/header.jsp'/>
		<div id="content">
			<div id="guestbook">
				
				<form action="${pageContext.servletContext.contextPath }/guestbook/update" method="post">
					<input type="hidden" name="no" value="${vo.no }">
					<table>
						<tr>
							<td>이름</td><td><input type="text" name="name" value="${vo.name }" readonly></td>
							<td>비밀번호</td><td><input type="password" name="pw"></td>
						</tr>
						<tr>
							<td colspan=4><textarea name="contents" id="content">${vo.contents }</textarea></td>
						</tr>
						<tr>
							<td colspan=4 align=right><input type="submit" VALUE="수정"></td>
						</tr>
					</table>
				</form>
				
			</div>
		</div>
		<c:import url='/WEB-INF/views/includes/navigation.jsp'>
			<c:param name="menu" value="guestbook"/>
		</c:import>
		<c:import url='/WEB-INF/views/includes/footer.jsp'/>
	</div>
</body>
</html>