package com.cafe24.mysite.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cafe24.mysite.dto.JSONResult;
import com.cafe24.mysite.service.BoardService;
import com.cafe24.mysite.service.CommentService;
import com.cafe24.mysite.service.UserService;
import com.cafe24.mysite.vo.CommentVO;
import com.cafe24.mysite.vo.UserVO;

@RequestMapping("/comment/api")
//다른 패키지에 있는 commnetcontroller와 헷갈리지 않도록
@Controller("commentAPIController")
public class CommentController {

	@Autowired
	private CommentService commentService;
	
	@ResponseBody
	@RequestMapping("/insert")
	public JSONResult insert(@RequestParam (value="contents", required=true, defaultValue="") String contents,
								 @RequestParam (value="boardNo", required=true, defaultValue="0") Long boardNo,
								 HttpSession session) {
	
		UserVO vo = (UserVO)session.getAttribute("vo");
		
		CommentVO cvo = new CommentVO();
		cvo.setBoardNo(boardNo);
		cvo.setUserNo(vo.getNo());
		cvo.setContents(contents);
		
		boolean flag = commentService.insert(cvo);
		
		//JSON 형태로 변형하여 전송
		JSONResult result = JSONResult.success(flag);
		return result;
	}
	
}