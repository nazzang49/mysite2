package com.cafe24.mysite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.cafe24.mysite.service.GuestbookService;
import com.cafe24.mysite.vo.GuestBookVO;

@RequestMapping("/guestbook")
@Controller
public class GuestbookController {

	@Autowired
	private GuestbookService guestbookService;
	
	//방명록 목록 이동
	@RequestMapping(value= {"/",""})
	public String list(Model model) {
		model.addAttribute("list",guestbookService.getList());
		return "guestbook/list";
	}
	
	//방명록 추가 및 목록 이동
	@RequestMapping(value= {"/",""}, method=RequestMethod.POST)
	public String list(@ModelAttribute GuestBookVO vo, Model model) {
		guestbookService.insert(vo);
		model.addAttribute("list",guestbookService.getList());
		//입력 후 방명록 폴더 내 방명록 목록 페이지로 이동
		//redirect는 명시되는 주소로 실제 이동
		return "redirect:/guestbook";
	}
	
	//방명록 수정 이동
	@RequestMapping(value= "/update")
	public String update(@RequestParam String no, Model model) {
		//기존 사용자 정보
		model.addAttribute("vo", guestbookService.get(Long.parseLong(no)));
		return "guestbook/update";
	}
	
	//방명록 수정 실시
	@RequestMapping(value= "/update", method=RequestMethod.POST)
	public String update(@ModelAttribute GuestBookVO vo, Model model) {
		boolean flag = guestbookService.update(vo.getNo(),vo.getContents());
		//수정 실패
		if(!flag) {
			model.addAttribute("no", vo.getNo());
			return "guestbook/update";
		}
		//기존 사용자 정보
		model.addAttribute("list", guestbookService.getList());
		return "redirect:/guestbook";
	}
	
	//방명록 삭제 이동
	@RequestMapping(value= "/delete")
	public String delete(@RequestParam(value="no",required=true, defaultValue="") String no, Model model) {
		model.addAttribute("no", no);
		return "guestbook/delete";
	}
	
	//방명록 삭제 실시
	@RequestMapping(value= "/delete", method=RequestMethod.POST)
	public String delete(@RequestParam(value="no",required=true, defaultValue="") String no,
						 @RequestParam(value="password",required=true, defaultValue="") String password) {
		guestbookService.delete(Long.parseLong(no),password);
		return "redirect:/guestbook";
	}
	
}
