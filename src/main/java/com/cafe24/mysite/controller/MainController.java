package com.cafe24.mysite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cafe24.mysite.vo.UserVO;

@Controller
public class MainController {

	@RequestMapping({"/","/main"})
	public String main() {
		//main폴더 안 index.jsp를 찾아간다
		return "main/index";
	}
	
	//메시지 컨버터 실습
	@ResponseBody
	@RequestMapping("/hello")
	public String hello() {
		return "<h1>안녕하세요</h1>";
	}
	
	//메시지 컨버터 실습
	@ResponseBody
	@RequestMapping("/hello2")
	public UserVO hello2() {
		UserVO vo = new UserVO();
		vo.setNo(10L);
		vo.setName("박진영");
		vo.setPw("pw");
		vo.setGender("남성");
		return vo;
	}
	
}
