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
import org.springframework.util.StopWatch;

import com.cafe24.mysite.exception.UserDAOException;
import com.cafe24.mysite.vo.GuestBookVO;
import com.cafe24.mysite.vo.UserVO;

@Repository
public class UserDAO {

	@Autowired
	private DataSource datasource;
	
	@Autowired
	private SqlSession sqlSession;
	
	public UserDAO() {
		System.out.println("UserDAO 생성자");
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
	
	public UserVO get(String email) {
		UserVO vo = sqlSession.selectOne("user.getByEmail",email);
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
