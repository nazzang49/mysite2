package com.cafe24.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.cafe24.mysite.vo.UserVO;

public class AuthUserHandlerArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return	parameter.getParameterAnnotation(AuthUser.class)!=null&&
				parameter.getParameterType().equals(UserVO.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		if(!supportsParameter(parameter)) {
			//not my authority
			return WebArgumentResolver.UNRESOLVED;
		}
		HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		HttpSession session = httpServletRequest.getSession();
		UserVO vo = (UserVO) session.getAttribute("vo");
		return vo;
	}

}
