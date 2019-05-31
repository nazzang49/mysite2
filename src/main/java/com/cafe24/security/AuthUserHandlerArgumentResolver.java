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
   public Object resolveArgument(
         MethodParameter parameter,
         ModelAndViewContainer mavContainer,
         NativeWebRequest webRequest,
         WebDataBinderFactory binderFactory) throws Exception {
      
      if(supportsParameter(parameter)==false) {
         return WebArgumentResolver.UNRESOLVED;
      }
      
      // 이미 알고 있는 파라미터라면
      HttpServletRequest request = 
            webRequest.getNativeRequest(HttpServletRequest.class);
      HttpSession session = request.getSession();
      if(session == null) {
         return null;
      }
      
      return session.getAttribute("authUser");

   }
   
   @Override
   public boolean supportsParameter(MethodParameter parameter) {
      AuthUser authUser = parameter.getParameterAnnotation(AuthUser.class);
      
      // @AuthUser가 안 붙어 있다면
      if(authUser==null) {
         return false; 
      }
      // @AuthUser가 붙어있는데 UserVO가 다른 타입일 때
      if(parameter.getParameterType().equals(UserVO.class)==false) {
         return false;
      }
      return true;
   }

}