package com.cafe24.mysite.controller.api;

import java.sql.SQLException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cafe24.mysite.dto.JSONResult;
import com.cafe24.mysite.service.BoardService;
import com.cafe24.mysite.vo.BoardVO;

@RequestMapping("/board/api")
//다른 패키지에 있는 commnetcontroller와 헷갈리지 않도록
@Controller("boardAPIController")
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	@ResponseBody
	@RequestMapping("")
	public JSONResult search(@RequestParam (value="pageNum", required=true, defaultValue="1") Long pageNum,
							 @RequestParam (value="type", required=true, defaultValue="empty") String type,
			   				 @RequestParam (value="keyword", required=true, defaultValue="empty") String keyword,
							 Model model) throws SQLException {
	
		//현재 페이지
		Long currentPage = pageNum;
		
		//한 페이지에 10개의 게시물 표시
		Long pageSize = 5L;
		
		//한 페이지의 시작행, 끝행
		Long startRow = (currentPage-1)*pageSize+1;
		Long endRow = currentPage*pageSize;
		
		List<BoardVO> list = boardService.search(type, keyword, startRow-1, pageSize);
		
		//검색 게시물 수
		Long count = boardService.getCount(type, keyword);
		
		model.addAttribute("count", count);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("pageBlock", 3L);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("list", list);
		
		//JSON 형태로 변형하여 전송
		JSONResult result = JSONResult.success(model);
		return result;
	}
	
}
