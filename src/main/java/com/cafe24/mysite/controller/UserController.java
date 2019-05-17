package com.cafe24.mysite.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.cafe24.mysite.service.UserService;
import com.cafe24.mysite.vo.UserVO;

@RequestMapping("/user")
@Controller
public class UserController {

	//webapplicationcontext에서 부모측에 해당하는 rootapplicationcontext에 UserDAO 클래스 존재 유무 탐색 및 주입
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/join") 
	public String join() {
		//view 폴더까지는 이미 path 설정이 되어 있음
		return "user/join";
	}
	
	//컨트롤러 + 서비스 구분 완료
	@RequestMapping(value="/join", method=RequestMethod.POST)
	public String join(@ModelAttribute UserVO vo) {
		//DB 저장 작업(별도 객체 생성 필요 없음 / bean 객체 자동 주입)
		userService.join(vo);
		return "redirect:/user/joinsuccess";
	}
	
	@RequestMapping(value="/joinsuccess") 
	public String joinsuccess() {
		//view 폴더까지는 이미 path 설정이 되어 있음
		return "user/joinsuccess";
	}
	
	@RequestMapping(value="/login") 
	public String login() {
		//view 폴더까지는 이미 path 설정이 되어 있음
		return "user/login";
	}
	
	//컨트롤러 + 서비스 구분 완료
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String login(@RequestParam(value="email", required=true, defaultValue="") String email,
						@RequestParam(value="pw", required=true, defaultValue="") String pw,
						HttpSession session,
						Model model) {
		//DB 저장 작업(별도 객체 생성 필요 없음 / bean 객체 자동 주입)
		UserVO vo = new UserVO();
		vo.setEmail(email);
		vo.setPw(pw);
		vo = userService.getUser(vo);
		
		if(vo==null) {
			model.addAttribute("result", "fail");
			//forward(form 문서)
			return "user/login";
		}
		//회원가입 성공 시 세션 처리
		session.setAttribute("vo", vo);
		//redirect(입력값 처리 후)
		return "redirect:/";
	}
	
	@RequestMapping(value="/logout")
	public String logout(HttpSession session) {
		//현재 로그인 중인 세션 삭제 및 공간 제거
		session.removeAttribute("vo");
		session.invalidate();
		return "redirect:/";
	}
	
	//회원 수정 이동(완료)
	@RequestMapping(value= "/update")
	public String update(HttpSession session, Model model) {
		//로그인 세션 정보가 존재하는 경우에만 수정 가능
		if(session!=null&&session.getAttribute("vo")!=null) {
			UserVO vo = (UserVO)session.getAttribute("vo");
			UserVO user = userService.getUser(vo.getNo());
			
			model.addAttribute("vo",user);
			model.addAttribute("no",vo.getNo());
			return "user/update";
		}
		//로그인 세션 정보가 없으면 로그인 페이지로 이동
		return "redirect:/user/login";
	}
	
	//회원 수정 실시
	@RequestMapping(value= "/update", method=RequestMethod.POST)
	public String update(@RequestParam(value="no",required=true, defaultValue="") String no,
						 @RequestParam(value="name",required=true, defaultValue="") String name,
						 @RequestParam(value="email",required=true, defaultValue="") String email,
						 @RequestParam(value="pw",required=true, defaultValue="") String pw,
						 Model model,
						 HttpSession session) {
		
		UserVO vo = new UserVO();
		vo.setNo(Long.parseLong(no));
		vo.setName(name);
		vo.setEmail(email);
		vo.setPw(pw);
		
		boolean flag = userService.update(vo);
		
		//기존 세션 삭제
		session.removeAttribute("vo");
		
		vo = new UserVO();
		//세션에는 사용자의 번호, 이름만 저장됨
		vo.setNo(Long.parseLong(no));
		vo.setName(name);

		//변경된 사용자 정보로 새 세션 등록
		session.setAttribute("vo", vo);
		
		//수정 실패
		if(!flag) {
			model.addAttribute("no", Long.parseLong(no));
			return "user/update";
		}
		//수정 성공(메인으로 이동)
		return "redirect:/";
	}
	
	//컨트롤러를 활용한 예외 처리(클래스를 만드는 것이 효율적)
	//@ExceptionHandler(UserDAOException.class)
	//public String handleDaoException(Exception e) {
	//	return "error/exception";
	//}
	
}
