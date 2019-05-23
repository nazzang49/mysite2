package com.cafe24.mysite.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cafe24.mysite.vo.BoardVO;

@Repository
public class BoardDAO {

	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
	private DataSource dataSource;
	private Connection conn;
	
	//목록
	public List<BoardVO> getList(Long startRow, Long pageSize){
		Map<String, Long> map = new HashMap<>();
		map.put("startRow", startRow);
		map.put("pageSize", pageSize);
		return sqlSession.selectList("board.getList",map);
	}
	
	//연관 목록 추출(삭제)
	public List<BoardVO> getRelatedList(Long groupNo, Long depth, Long orderNo){
		Map<String, Long> map = new HashMap<>();
		map.put("depth", depth);
		map.put("groupNo", groupNo);
		map.put("orderNo", orderNo);
		return sqlSession.selectList("board.getRelatedList",map);
	}
	
	public BoardVO getOne(Long no){
		return sqlSession.selectOne("board.getOne", no);
	}
	
	public Long getCount(){
		return sqlSession.selectOne("board.getCount");
	}
	
	//검색 게시물 수
	public Long getCount(String type, String keyword) throws SQLException{
		
		Long count = 0L;
		conn = dataSource.getConnection();
		StringBuilder sb = new StringBuilder();
		
		sb.append("select count(*) from board where");
		
		//작성자 검색 = 조인 필요
		if("name".equals(type)) {
			sb = new StringBuilder();
			sb.append("select count(*)" + 
					" from board b, user u" + 
					" where b.user_no=u.no" + 
					" and u.name like '%"+keyword+"%'");	
		}else if("title".equals(type)) {
			sb.append(" title like '%"+keyword+"%'");
		}else {
			sb.append(" contents like '%"+keyword+"%'");
		}
		
		PreparedStatement pstmt = conn.prepareStatement(sb.toString());
		ResultSet rs = pstmt.executeQuery();
		
		if(rs.next()) {
			count = rs.getLong(1);
		}
		
		return count;
	}
	
	//검색 게시물 리스트
	public List<BoardVO> search(String type, String keyword, Long startRow, Long pageSize) throws SQLException{
		
		List<BoardVO> list = new ArrayList<>();
		conn = dataSource.getConnection();
		StringBuilder sb = new StringBuilder();
		
		sb.append("select b.no, b.title, b.reg_date as regDate, b.hit, b.order_no as orderNo, b.group_no as groupNo, b.depth, u.name as userName, u.no as userNo" + 
				" from board b, user u" + 
				" where b.user_no=u.no");
		
		//작성자 검색 = 조인 필요
		if("name".equals(type)) {
			sb.append(" and u.name like '%"+keyword+"%'");
		}else if("title".equals(type)) {
			sb.append(" and title like '%"+keyword+"%'");
		}else {
			sb.append(" and contents like '%"+keyword+"%'");
		}
		
		sb.append(" order by groupNo desc, orderNo asc limit ?,?");
		
		PreparedStatement pstmt = conn.prepareStatement(sb.toString());
		pstmt.setLong(1, startRow);
		pstmt.setLong(2, pageSize);
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()) {
			BoardVO vo = new BoardVO();
			
			vo.setNo(rs.getLong(1));
			vo.setTitle(rs.getString(2));
			vo.setRegDate(rs.getDate(3));
			vo.setHit(rs.getLong(4));
			vo.setOrderNo(rs.getLong(5));
			vo.setGroupNo(rs.getLong(6));
			vo.setDepth(rs.getLong(7));
			vo.setUserName(rs.getString(8));
			vo.setUserNo(rs.getLong(9));
			
			list.add(vo);
		}
		
		return list;
	}
	
	//max(그룹번호)
	public Long getLastGroupNo(){
		return sqlSession.selectOne("board.getLastGroupNo");
	}
	
	public void updateHit(Long no) {
		sqlSession.update("board.updateHit", no);
	}
	
	//게시물 수정
	public boolean update(BoardVO vo) {
		return sqlSession.update("board.update", vo)==1;
	}
	
	public void updateOrderNo(Long groupNo, Long orderNo) {
		Map<String, Long> map = new HashMap<>();
		map.put("groupNo", groupNo);
		map.put("orderNo", orderNo);
		sqlSession.update("board.updateOrderNo", map);
	}
	
	//신규등록
	public boolean insert(String title, String contents, Long lastGroupNo, Long userNo, String url) {
		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("contents", contents);
		map.put("groupNo", lastGroupNo);
		map.put("userNo", userNo);
		map.put("url", url);
		return sqlSession.insert("board.insert",map)==1;
	}
	
	public boolean insert(BoardVO vo) {
		return sqlSession.insert("board.rewrite",vo)==1;
	}
	
	public boolean delete(Long no) {
		return sqlSession.delete("board.delete",no)==1;
	}
	
}
