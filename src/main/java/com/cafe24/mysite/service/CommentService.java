package com.cafe24.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cafe24.mysite.repository.CommentDAO;
import com.cafe24.mysite.vo.BoardVO;
import com.cafe24.mysite.vo.CommentVO;

@Service
public class CommentService {

	@Autowired
	private CommentDAO commentDao;
	
	//댓글은 페이징 처리 X
	public List<CommentVO> getList(Long boardNo) {
		return commentDao.getList(boardNo);
	}
	
	public boolean insert(CommentVO cvo) {
		return commentDao.insert(cvo);
	}
	
}
