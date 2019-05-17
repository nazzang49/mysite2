package com.cafe24.mysite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cafe24.mysite.repository.UserDAO;
import com.cafe24.mysite.vo.UserVO;

@Service
public class UserService {

	@Autowired
	private UserDAO userDao;
	
	public boolean join(UserVO vo) {
		return userDao.insert(vo);
	}
	
	//로그인 세션
	public UserVO getUser(UserVO vo) {
		UserVO user = userDao.get(vo.getEmail(), vo.getPw());
		return user;
	}
	
	//회원정보 수정
	public UserVO getUser(Long no) {
		UserVO user = userDao.get(no);
		return user;
	}
	
	//회원정보 수정
	public boolean update(UserVO vo) {
		return userDao.update(vo);
	}
	
}
