package com.cafe24.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cafe24.mysite.vo.UserVO;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	//필터 후 and 컨트롤러 전
	@Override
	public boolean preHandle(HttpServletRequest request,
						     HttpServletResponse response,
						     Object handler)
						     throws Exception {
		
		//1) 핸들러 종류 확인
		if(handler instanceof HandlerMethod == false) {
			return true;
		}
		
		//2) 캐스팅
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		
		//3) 메소드의 @Auth 받기
		Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
		
		//4) @Auth 없을 시 Class(Type)에 @Auth 받기
//		if(auth==null) {
//			auth = 
//		}
		
		//5) Class에도 없는 경우
		if(auth==null) {
			return true;
		}
		
		//6) @Auth가 탐색된 경우(클래스 or 메소드) = 인증 여부 확인
		HttpSession session = request.getSession();
		
		//세션 자체가 없거나 찾고자 하는 세션이 없는 경우
		if(session==null) {
			response.sendRedirect(request.getContextPath()+"/user/login");
			return false;
		}
		UserVO vo = (UserVO)session.getAttribute("vo");
		if(vo==null) {
			response.sendRedirect(request.getContextPath()+"/user/login");
			return false;
		}
		
		//7) role 가져오기
		Auth.Role role = auth.role();
		
		//8) role이 USER라면, 인증된 모든 사용자 접근 허용
		if(role==Auth.Role.USER) {
			return true;
		}
		
		//9) Admin Role 권한 체크(DB와 연계된 작업)
		//vo.getRole().equals("ADMIN")
		
		return true;
	}
	
}
