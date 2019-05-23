package com.cafe24.mysite.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cafe24.mysite.vo.GuestBookVO;

//applicationcontext.xml에서 스캔하기 위해
@Repository
public class GuestBookDAO {

	@Autowired
	private DataSource datasource;
	@Autowired
	private SqlSession sqlSession;
	
	static Connection conn = null;
	static PreparedStatement pstmt = null;
	static ResultSet rs = null;
	static String sql = "";
	
	//일괄 종료
	public static void close() {
		try {
			if(rs!=null&&!rs.isClosed()) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}try {
			if(pstmt!=null&&!pstmt.isClosed()) {
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}try {
			if(conn!=null&&!conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<GuestBookVO> getList(){
		return sqlSession.selectList("guestbook.getList");
	}
	
	public GuestBookVO get(Long no) {
		return sqlSession.selectOne("guestbook.getByNo", no);
	}
	
	public boolean insert(GuestBookVO vo) {
		return sqlSession.insert("guestbook.insert",vo)==1;
	}
	
	public boolean delete(Long no, String password) {
		//두개 매개변수 타입이 다른 경우 object로 저장
		Map<String, Object> map = new HashMap<>();
		map.put("no", no);
		map.put("password", password);
		return sqlSession.delete("guestbook.delete", map)==1;
	}
	
	public boolean update(Long no, String contents) {
		Map<String, Object> map = new HashMap<>();
		map.put("no", no);
		map.put("contents", contents);
		return sqlSession.update("guestbook.update",map)==1;
	}
}
