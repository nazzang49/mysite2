package com.cafe24.mysite.controller;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cafe24.fileupload.service.FileuploadService;
import com.cafe24.mysite.service.BoardService;
import com.cafe24.mysite.service.CommentService;
import com.cafe24.mysite.vo.BoardVO;
import com.cafe24.mysite.vo.CommentVO;
import com.cafe24.mysite.vo.UserVO;
import com.cafe24.security.Auth;
import com.cafe24.security.AuthUser;
import com.cafe24.web.util.WebUtil;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private CommentService commentService;
	
	//게시물 목록
	//Long 타입은 받을 때 오토 파싱
	@RequestMapping(value={"/list",""})
	public String list(@RequestParam (value="pageNum", required=true, defaultValue="1") Long pageNum,
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

		List<BoardVO> list = null;
		//전체 게시물 수
		Long count = null;
		
		if(!type.equals("empty")&&!keyword.equals("empty")) {
			
			list = boardService.search(type, keyword, startRow-1, pageSize);
			count = boardService.getCount(type, keyword);
			
			model.addAttribute("type", type);
			model.addAttribute("keyword", WebUtil.encodeURL(keyword, "utf-8"));
		}else {
			list = boardService.getList(startRow-1, pageSize);
			count = boardService.getCount();
		}
				
		model.addAttribute("count", count);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("pageBlock", 3L);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("list", list);

		return "board/list";
	}
	
	//게시물 보기(groupNo으로 orderNo, depth 추출)
	@RequestMapping("/view")
	public String view(@RequestParam (value="no", required=true, defaultValue="0") Long no,
					   Model model) {
		//조회수 up
		boardService.hitUp(no);
		
		//현재 게시물 제목, 내용 호출(모델 객체로 넘길 때 다른 객체명과 겹치지 않도록 주의 like vo)
		BoardVO bvo = boardService.getOne(no);
		model.addAttribute("bvo", bvo);
		
		List<CommentVO> commentList = commentService.getList(no);
		model.addAttribute("commentList", commentList);
		
		return "board/view";
	}
	
	//게시물 작성
	@Auth
	@RequestMapping("/write")
	public String write(@AuthUser UserVO vo) {
		return "board/write";
	}
	
	@Auth
	//게시물 작성(신규)
	@RequestMapping(value="/write", method=RequestMethod.POST)
	public String write(@RequestParam (value="title", required=true, defaultValue="") String title,
						@RequestParam (value="contents", required=true, defaultValue="") String contents,
						@RequestParam (value="file") MultipartFile file,
						@AuthUser UserVO authUser,
						@ModelAttribute BoardVO vo) {
		
		Long lastGroupNo = 0L;
		String url = boardService.restore(file);
		
		//db저장
		if(boardService.getLastGroupNo()!=null) {
			lastGroupNo = boardService.getLastGroupNo();
		}
		lastGroupNo++;
		boolean result = boardService.insert(title,contents,lastGroupNo,authUser.getNo(),url);
		
		//성공
		if(result) {
			return "redirect:/board/list";
		}
		return "board/write";
	}
	
	//답글 이동
	@RequestMapping("/rewrite")
	public String write(@RequestParam (value="groupNo", required=true, defaultValue="0") Long groupNo,
						@RequestParam (value="orderNo", required=true, defaultValue="0") Long orderNo,
						@RequestParam (value="depth", required=true, defaultValue="0") Long depth,
						Model model) {
		
		model.addAttribute("groupNo", groupNo);
		model.addAttribute("orderNo", orderNo);
		model.addAttribute("depth", depth);
		
		return "board/rewrite";
	}
	
	//답글 작성
	@RequestMapping(value="/rewrite", method=RequestMethod.POST)
	public String write(@RequestParam (value="groupNo", required=true, defaultValue="0") Long groupNo,
						@RequestParam (value="orderNo", required=true, defaultValue="0") Long orderNo,
						@RequestParam (value="depth", required=true, defaultValue="0") Long depth,
						@RequestParam (value="title", required=true, defaultValue="") String title,
						@RequestParam (value="contents", required=true, defaultValue="") String contents,
						@AuthUser UserVO authUser,
						Model model) {
		
		//기존 orderNo update 작업
		orderNo++;
		boardService.updateOrderNo(groupNo,orderNo);
		depth++;
		
		BoardVO vo = new BoardVO();
		vo.setTitle(title);
		vo.setContents(contents);
		vo.setGroupNo(groupNo);
		vo.setOrderNo(orderNo);
		vo.setDepth(depth);
		vo.setUserNo(authUser.getNo());
		
		boolean result = boardService.rewrite(vo);
		
		//성공
		if(result) {
			return "redirect:/board/list";
		}
		return "board/rewrite";
	}
	
	//게시물 삭제(그룹 내 모든 게시물 동시 삭제)
	@RequestMapping("/delete")
	public String delete(@RequestParam (value="no", required=true, defaultValue="0") Long no,
						 @RequestParam (value="groupNo", required=true, defaultValue="0") Long groupNo,
						 @RequestParam (value="orderNo", required=true, defaultValue="0") Long orderNo,
						 @RequestParam (value="depth", required=true, defaultValue="0") Long depth) {
		
		//1) 파라미터로 전달된 orderNo, depth보다 큰 모든 게시물 조회
		List<BoardVO> list = boardService.getRelatedList(groupNo, depth, orderNo);
		
		//2) 가장 큰 orderNo 구하기 - 1
		Long maxOrderNo = 1L;
		
		//가장 최상위 글이 아닌 경우에만
		if(depth!=0&&orderNo!=1) {
			//자기 자신만 삭제하는 경우
			if(list.size()==1) {
				maxOrderNo = ((BoardVO)list.get(0)).getOrderNo();
			}
			
			boolean flag = false;
			
			//여러개의 하위 게시물이 있는 경우
			for(int i=0;i<list.size();i++) {
				//자기 자신 패스
				if(i==0) continue;
				BoardVO vo = (BoardVO)list.get(i);
				//입력된 레벨과 같아지는 게시물 확인
				Long childDepth = vo.getDepth();
				Long childOrderNo = vo.getOrderNo();
				
				if(depth==childDepth) {
					flag = true;
					maxOrderNo = childOrderNo-1;
					break;
				}
			}
			//삭제하고자 하는 게시물과 같은 depth가 없는 경우(=현재 게시물이 모든 하위 게시물의 부모인 경우)
			if(!flag) {
				maxOrderNo = list.get(list.size()-1).getOrderNo();
			}
		}else {
			maxOrderNo = list.get(list.size()-1).getOrderNo();
		}
				
		//3) 하위 게시물 모두 삭제
		for(int i=0;i<list.size();i++){
			BoardVO vo=(BoardVO)list.get(i);
			Long childNo = vo.getNo();
			Long childOrderNo = vo.getOrderNo();
			Long childDepth = vo.getDepth();
			
			if(childOrderNo<=maxOrderNo){
				boardService.delete(childNo);
			}
		}
		return "redirect:/board/list";
	}
	
	//게시물 수정(select)
	@RequestMapping("/modify")
	public String modify(@RequestParam (value="no", required=true, defaultValue="0") Long no,
						 Model model) {
		BoardVO bvo = boardService.getOne(no);
		model.addAttribute("bvo", bvo);
		return "board/modify";
	}
	
	//게시물 수정(update)
	@RequestMapping(value="/modify", method=RequestMethod.POST)
	public String modify(@ModelAttribute BoardVO vo) {
		boolean flag = boardService.update(vo);
		//성공
		if(flag) {
			return "redirect:/board";
		}
		return "board/modify";
	}
	
	//댓글 삭제
//	@RequestMapping("/comment/delete")
//	public String delete(@RequestParam (value="no", required=true, defaultValue="0") Long no) {
//		
//		
//		
//	}
		
}
