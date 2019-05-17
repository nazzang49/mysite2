package com.cafe24.mysite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@RequestMapping({"/","/main"})
	public String main() {
		//main폴더 안 index.jsp를 찾아간다
		return "main/index";
	}
	
}
