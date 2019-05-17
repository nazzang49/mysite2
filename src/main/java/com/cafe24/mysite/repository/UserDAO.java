package com.cafe24.mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.omg.CORBA.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cafe24.mysite.exception.UserDAOException;
import com.cafe24.mysite.vo.GuestBookVO;
import com.cafe24.mysite.vo.UserVO;

@Repository
public class UserDAO {

	@Autowired
	private DataSource datasource;
	
	@Autowired
	private SqlSession sqlSession;
	
	static Connection conn = null;
	static PreparedStatement pstmt = null;
	static ResultSet rs = null;
	static String sql = "";
	
	public UserDAO() {
		System.out.println("UserDAO 생성자");
	}
	
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
	
	//회원추가(세션 팩토리)
	public boolean insert(UserVO vo) {
		return sqlSession.insert("user.insert", vo)==1;
	}
	
	//회원정보 변경
	public boolean update(UserVO vo) {
		return sqlSession.update("user.update", vo)==1;
	}
	
	public UserVO get(Long no) {
		UserVO vo = sqlSession.selectOne("user.getByNo",no);
		return vo;
	}
	
	//로그인하는 사용자의 세션값 저장을 위한 정보 추출
	public UserVO get(String email, String pw) throws UserDAOException{
		Map<String, String> map = new HashMap<>();
		map.put("email", email);
		map.put("pw", pw);
		UserVO vo = sqlSession.selectOne("user.getByEmailAndPw",map);
		return vo;
	}
}
