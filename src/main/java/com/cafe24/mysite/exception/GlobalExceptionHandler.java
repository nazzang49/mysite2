package com.cafe24.mysite.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	//모든 예외를 하나의 메소드로 정리
	@ExceptionHandler(Exception.class)
	public String handleException(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception{
		
		//1) 로깅
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		System.out.println(errors.toString());
		
		//2) 안내 페이지 이동 + 정상 종료
		request.setAttribute("uri", request.getRequestURI());
		request.setAttribute("exception", errors.toString());
		
		return "error/exception";
	}

}
