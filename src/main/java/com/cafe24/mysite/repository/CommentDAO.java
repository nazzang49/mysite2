package com.cafe24.mysite.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.cafe24.mysite.vo.CommentVO;

@Repository
public class CommentDAO {

	@Autowired
	private SqlSession sqlSession;
	
	//목록
	public List<CommentVO> getList(Long boardNo){
		return sqlSession.selectList("comment.getList",boardNo);
	}
	
	//작성
	public boolean insert(CommentVO cvo){
		return sqlSession.insert("comment.insert",cvo)==1;
	}
	
}
