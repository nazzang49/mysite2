<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.cafe24.mysite.vo.GuestBookVO"%>
<%@page import="java.util.List"%>
<%
	//개행처리
	pageContext.setAttribute("newline", "\n");
%>
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
				<form action="${pageContext.servletContext.contextPath }/guestbook" method="post">
					<table>
						<tr>
							<td>이름</td><td><input type="text" name="name"></td>
							<td>비밀번호</td><td><input type="password" name="password"></td>
						</tr>
						<tr>
							<td colspan=4><textarea name="contents" id="content"></textarea></td>
						</tr>
						<tr>
							<td colspan=4 align=right><input type="submit" VALUE=" 확인 "></td>
						</tr>
					</table>
				</form>
				<ul>
				<c:set var='count' value='${fn:length(list) }'/>
					<c:forEach items='${list }' var='vo' varStatus='status'>
						<table id="list" width=510 border=1>
							<tr>
								<td>[${status.count }:${status.index }]</td>
								<td>${vo.name }</td>
								<td>${vo.reg_date }</td>
								<!-- get 방식 파라미터 전송 -->
								<td><a href="${pageContext.servletContext.contextPath}/guestbook/delete?no=${vo.no}">삭제</a>
									<a href="${pageContext.servletContext.contextPath}/guestbook/update?no=${vo.no}">수정</a></td>
							</tr>
							<tr>
								<!-- 개행처리(el 태그 안에서는 개행처리가 안되므로 별도의 page 단위 변수를 생성한 후 삽입해준다 newline -->
								<td colspan=4>${fn:replace(vo.contents,newline,"<br>") }</td>
							</tr>
						</table>
					</c:forEach>
				</ul>
			</div>
		</div>
		<c:import url='/WEB-INF/views/includes/navigation.jsp'>
			<c:param name="menu" value="guestbook"/>
		</c:import>
		<c:import url='/WEB-INF/views/includes/footer.jsp'/>
	</div>
</body>
</html>