<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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
<body>
<script type="text/javascript">
	//ajax 적용
	$(function(){
		//댓글 입력
		$('#comment-submit').on('click', function(){
			var contents = $('#contents').val();
			var boardNo = ${bvo.no}
			$.ajax({
				url:"${pageContext.servletContext.contextPath }/comment/api/insert?contents="+contents+"&boardNo="+boardNo,
				//요청 방식
				type:"get",
				//리턴 데이터 타입(JSONResult.java 클래스 활용)
				dataType:"json",
				//통신 성공 시 브라우저에 표시할 콜백 함수
				success:function(response){
					alert(response.data+","+response.result);
				}
			});
		});
		
		//댓글 수정
		$('#delete').on('click', function(){
			var confirm = alert("삭제하시겠습니까");
			//삭제하고자 하는 댓글 번호
			var no = $('#no').val();
			if(confirm){
				$.ajax({
					url:"${pageContext.servletContext.contextPath }/board/comment/delete?no="+no,
					//요청 방식
					type:"get",
					//리턴 데이터 타입
					dataType:"json",
					//통신 성공 시 브라우저에 표시할 콜백 함수
					success:function(response){
						alert("[삭제 완료]");
					}
				});	
			}
		});
		
		//댓글 삭제
		$('#delete').on('click', function(){
			var confirm = alert("삭제하시겠습니까");
			//삭제하고자 하는 댓글 번호
			var no = $('#no').val();
			if(confirm){
				$.ajax({
					url:"${pageContext.servletContext.contextPath }/board/comment/delete?no="+no,
					//요청 방식
					type:"get",
					//리턴 데이터 타입
					dataType:"json",
					//통신 성공 시 브라우저에 표시할 콜백 함수
					success:function(response){
						alert("[삭제 완료]");
					}
				});	
			}
		});
	});

</script>
	<div id="container">
		<c:import url='/WEB-INF/views/includes/header.jsp'/>
		<div id="content">
			<div id="board" class="board-form">
				<table class="tbl-ex">
					<tr>
						<th colspan="6">글보기</th>
					</tr>
					<tr>
						<td class="label">제목</td>
						<td>${bvo.title }</td>
						<td class="label">작성일</td>
						<td>${bvo.regDate }</td>
						<td class="label">조회수</td>
						<td>${bvo.hit }</td>
					</tr>
					<tr>
						<td class="label">내용</td>
						<td colspan="5">
							<div class="view-content">
								${bvo.contents }
							</div>
						</td>
					</tr>
				</table>
				<div class="bottom">
					<a href="${pageContext.servletContext.contextPath }/board">글목록</a>
					<!-- 수정은 본인이면 가능 -->
					<c:if test='${authUser.name==bvo.userName }'>
					<a href="${pageContext.servletContext.contextPath }/board/modify?no=${bvo.no}">글수정</a>
					</c:if>
					<!-- 답글은 회원이면 가능 -->
					<c:if test='${authUser!=null }'>
					<a href="${pageContext.servletContext.contextPath }/board/rewrite?groupNo=${bvo.groupNo }&orderNo=${bvo.orderNo }&depth=${bvo.depth }&pageNum=${pageNum}">답글</a>
					</c:if>
				</div>
				
				<!-- 댓글 -->
				<div id="comment">
				<!-- 회원에 한해 댓글 작성 가능 -->
				<c:if test='${authUser!=null }'>
				
				<!-- validation 적용 -->
				<table class="tbl-ex">
						<tr>
							<th colspan="3">댓글 입력</th>
						</tr>
						<tr><!-- 현재 로그인 중인 사용자 = 작성자 -->
							<td class="label" style="width:20%;">${authUser.name }</td>
							<td class="label" style="width:80%;">
							<textarea name="contents" id="contents" style="width:400px; height:100px;">[입력 대기]</textarea>
							<button id="comment-submit">입력</button>
							</td>
						</tr>
				</table>
				</c:if>
				<!-- 댓글 목록 -->
				<table class="tbl-ex">
					<tr>
						<th colspan="4">댓글 목록</th>
					</tr>
					<tr>
						<td class="label">작성자</td>
						<td class="label">내용</td>
						<td class="label">작성일</td>
						<td class="label">수정 및 삭제</td>
					</tr>
					<c:if test="${commentList!=null }">
					<c:forEach items='${commentList }' var='cvo'>
					<tr>
						<td class="new">${cvo.userName }</td>
						<td class="new">${cvo.contents }</td>
						<td class="new">${cvo.regDate }</td>
						<td>
						<input type="hidden" value="${cvo.no }" id="no">
							<!-- 본인 것만 변경 가능 -->
							<c:if test="${authUser.name==cvo.userName }">
							<button id="delete">삭제</button>
							<button id="update">수정</button>
							</c:if>
							<c:if test="${authUser.name!=cvo.userName || authUser==null}">
							<p style="text-align: center;">변경불가</p>
							</c:if>
						</td>
					</tr>
					</c:forEach>
					</c:if>
				</table>
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