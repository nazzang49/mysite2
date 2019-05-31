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
<link href="${pageContext.servletContext.contextPath }/assets/css/board.css" rel="stylesheet" type="text/css">
<!-- 제이쿼리 등록 -->
<script src="${pageContext.servletContext.contextPath }/assets/js/jquery/jquery-1.9.0.js"></script>
</head>
<%
	Long count = (Long)request.getAttribute("count");
	Long pageSize = (Long)request.getAttribute("pageSize");
	pageContext.setAttribute("countDivision", count/pageSize);
%>
<body>
<!-- 검색어 유효성 검사 -->
<script type="text/javascript">
	$(function(){
		$('#search-submit').on('click',function(){
			
			var type=$('#search').val();
			var keyword=$('#kwd').val();
			
			//입력된 타입이나 검색어가 없으면
			if(type==''||keyword==''){
				alert("[타입 및 검색어 입력 필수]");
				$('#kwd').focus();
				return false;
			}
			$('#type').val(type);
			document.getElementById('search-form').submit();
		});
	});
</script>
	<div id="container">
		<c:import url='/WEB-INF/views/includes/header.jsp'/>
		<div id="content">
			<div id="board">
			
			--${pageNum}
			--${type}
			--${keyword}
				<!-- 검색 -->
				<form id="search_form" action="${pageContext.servletContext.contextPath }/board" method="post">
					<select id="search">
						<option value="">검색타입</option>
						<option value="name">작성자</option>
						<option value="title">제목</option>
						<option value="contents">내용</option>
					</select>
					<input type="hidden" id="type" name="type" value="">
					<input type="text" id="kwd" name="keyword" value="">
					<input type="submit" id="search-submit" value="검색">
				</form>
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th><!-- 휴지통 모양의 삭제 아이콘 들어갈 자리 --></th>
					</tr>
					<!-- 게시물 목록 -->
					<c:forEach items='${list }' var='bvo'>
					<tr>
						<td>${bvo.no }</td>
						<td style="text-align: left; padding-left: ${15*bvo.depth}px">
						<c:if test="${bvo.depth!=0 }">
						<img src="${pageContext.servletContext.contextPath }/assets/images/reply.png">
						</c:if>
						<!-- 게시물 보기 이동 시 페이지 번호 전송 -->
						<a href="${pageContext.servletContext.contextPath }/board/view?no=${bvo.no}">${bvo.title }</a></td>
						<td>${bvo.userName }</td>
						<td>${bvo.hit }</td>
						<td>${bvo.regDate }</td>
						
						<!-- 현재 로그인 중인 사용자 = 게시물 작성자 -->
						<!-- 계층형 게시물 삭제 -->
						<td>
						<c:if test="${authUser.no==bvo.userNo }">
						<a href="${pageContext.servletContext.contextPath }/board/delete?no=${bvo.no}&groupNo=${bvo.groupNo}
						&orderNo=${bvo.orderNo}&depth=${bvo.depth}" class="del">삭제</a>
						</c:if>
						</td>
						
					</tr>
					</c:forEach>
				</table>
				
				
				<!-- 게시물이 존재하는 경우에만 페이징 처리 -->
				<c:if test='${count!=0 }'>
				
				<!-- 페이징 처리에 필요한 변수 생성 -->
				<!-- 총 필요한 페이지 수(나누어 떨어지지 않는 경우 페이지 하나 추가 -->
				<c:set var="pageCount" value="${countDivision+(count%pageSize==0? 0:1) }" />
				<!-- 현재 페이지 기준 시작 페이지  -->
				<%
					Long currentPage = (Long)request.getAttribute("currentPage");
					Long pageBlock = (Long)request.getAttribute("pageBlock");
					pageContext.setAttribute("pageDivision", (currentPage-1)/pageBlock);
				%>
				<c:set var="startPage" value="${pageDivision*pageBlock+1 }" />
				
				<!-- 현재 페이지 기준 끝 페이지  -->
				<c:set var="endPage" value="${startPage+pageBlock-1 }" />
				
				<c:if test="${endPage > pageCount }">
				<c:set var="endPage" value="${pageCount }" />
				</c:if>
				
				<!-- pager 추가 -->
				<div class="pager">
					<ul>
						<li>
						<!-- 현재 페이지 기준, 시작 페이지 > 페이지 블럭 -->
						<c:if test='${startPage>pageBlock }'>
						<a href="${pageContext.servletContext.contextPath }/board?pageNum=${startPage-pageBlock}&type=${type}&keyword=${keyword}"> ◀ </a>
						</c:if>
						</li>
						<!-- begin = startPage / end = endPage -->
						<c:forEach var="i" begin="${startPage }" end="${endPage }">
						<li class="selected"><a href="${pageContext.servletContext.contextPath }/board?pageNum=${i}&type=${type}&keyword=${keyword}">${i}</a></li>
						</c:forEach>
						<li>
						<c:if test='${endPage<pageCount }'>
						<a href="${pageContext.servletContext.contextPath }/board?pageNum=${startPage+pageBlock}&type=${type}&keyword=${keyword}"> ▶ </a>
						</c:if>
						</li>
					</ul>
				</div>
				<!-- pager 추가 -->
				</c:if>
				<div class="bottom">
					<!-- 로그인 정보가 있을 경우에만 글쓰기 가능 -->
					<c:if test='${authUser!=null }'>
					<a href="${pageContext.servletContext.contextPath }/board/write" id="new-book">글쓰기</a>
					</c:if>
				</div>				
			</div>
		</div>
		<c:import url='/WEB-INF/views/includes/navigation.jsp'>
			<c:param name="menu" value="board"/>
		</c:import>
		<c:import url='/WEB-INF/views/includes/footer.jsp'/>
	</div>
</body>
</html>