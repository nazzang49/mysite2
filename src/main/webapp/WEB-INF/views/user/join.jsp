<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page import="com.cafe24.mysite.vo.UserVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.servletContext.contextPath }/assets/css/user.css" rel="stylesheet" type="text/css">
<!-- 제이쿼리 등록 -->
<script src="${pageContext.servletContext.contextPath }/assets/js/jquery/jquery-1.9.0.js"></script>

<!-- 자바스크립트에서는 함수도 객체 역할 / 자바는 불가능 -->
<script type="text/javascript">
	$(function(){
		$('#email').change(function(){
			$('#check-button').show();
			$('#check-image').hide();
		});
		
		$('#check-button').on('click',function(){
			var email = $('#email').val();
			//이메일 입력되지 않았다면 전송 X
			if(email==''){
				return;
			}
			
			$.ajax({
				//요청 보낼 url(get 방식은 파라미터 기입)
				url:"${pageContext.servletContext.contextPath }/user/api/checkemail?email="+email,
				//요청 방식
				type:"get",
				//리턴 데이터 타입
				dataType:"json",
				//실제 데이터 내용(post 방식일 때만 기입)
				data:"",
				//통신 성공 시 브라우저에 표시할 콜백 함수
				success:function(response){
					if(response.result!="success"){
						//응답 받는 메시지
						console.error(response.message);
						return;
					}
					if(response.data==true){
						alert("[이미 존재하는 이메일]\n 다른 이메일 주소를 입력해주세요!");
						$('#email').focus();
						$('#email').val("");
						return;
					}
					
					$('#check-button').hide();
					$('#check-image').show();
					
				},
				error:function(xhr, error){
					console.log("error : "+error);
				}
			});
		});
	});
</script>
</head>
<body>
	<div id="container">
		<c:import url='/WEB-INF/views/includes/header.jsp'/>
		<div id="content">
			<div id="user">
			
				<form:form
				modelAttribute="userVO"
				id="join-form"
				name="joinForm"
				method="post"
				action="${pageContext.servletContext.contextPath }/user/join">
					<label class="block-label" for="name">이름</label>
					<input id="name" name="name" type="text" value="">
					
					<spring:hasBindErrors name="userVO">
					    <c:if test="${errors.hasFieldErrors('name') }">
					    	<p style="font-weight:bold; color:red; text-align:left; padding:0">
					            <spring:message
						     	code="${errors.getFieldError('name').codes[0] }"
						     	text="${errors.getFieldError('name').defaultMessage }" />
						    </p>
					   </c:if>
					</spring:hasBindErrors>
					
					<label class="block-label" for="email">이메일</label>
					
					<!-- 인풋 태그 자동 생성 -->
					<form:input path="email"/>
					
<%-- 					<spring:hasBindErrors name="userVO"> --%>
<%-- 					    <c:if test="${errors.hasFieldErrors('email') }"> --%>
<!-- 					    	<p style="font-weight:bold; color:red; text-align:left; padding:0"> -->
<%-- 					            <spring:message --%>
<%-- 						     	code="${errors.getFieldError('email').codes[0] }" --%>
<%-- 						     	text="${errors.getFieldError('email').defaultMessage }" /> --%>
<!-- 						    </p> -->
<%-- 					   </c:if> --%>
<%-- 					</spring:hasBindErrors> --%>
					
					<input type="button" id="check-button" value="체크">
					<!-- 처음엔 보이면 안됨 / 통과하면 표시 -->
					<img src="${pageContext.servletContext.contextPath }/assets/images/check.png"
						 style="display:none;" id="check-image">
					
					<!-- 에러 메시지 단축 구문 -->
					<p style="font-weight:bold; color:red; text-align:left; padding:0; margin:0;">
						<form:errors path="email"/>
					</p>
						 
					<label class="block-label">패스워드</label>
					<input name="pw" type="password" value="">
					
					<fieldset>
						<legend>성별</legend>
						<label>여</label> <form:radiobutton path="gender" value="여성" checked="checked"/>
						<label>남</label> <form:radiobutton path="gender" value="남성"/>
					</fieldset>
					
					<fieldset>
						<legend>약관동의</legend>
						<input id="agree-prov" type="checkbox" name="agreeProv" value="y">
						<label>서비스 약관 동의</label>
					</fieldset>
					<input type="submit" value="가입">
				</form:form>
			</div>
		</div>
		<c:import url='/WEB-INF/views/includes/navigation.jsp'>
			<c:param name="menu" value="main"/>
		</c:import>
		<c:import url='/WEB-INF/views/includes/footer.jsp'/>
	</div>
</body>
</html>