package com.cafe24.mysite.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cafe24.mysite.repository.GuestBookDAO;
import com.cafe24.mysite.vo.GuestBookVO;

@Service
public class GuestbookService {

	@Autowired
	private GuestBookDAO guestbookDao;
	
	//방명록 추가
	public boolean insert(GuestBookVO vo) {
		return guestbookDao.insert(vo);
	}
	
	//방명록 목록
	public List<GuestBookVO> getList(){
		return guestbookDao.getList();
	}
	
	//기존 정보 호출
	public GuestBookVO get(Long no) {
		return guestbookDao.get(no);
	}
	
	//방명록 수정
	public boolean update(Long no, String contents) {
		return guestbookDao.update(no, contents);
	}
	
	//방명록 삭제
	public boolean delete(Long no, String password) {
		return guestbookDao.delete(no, password);
	}
	
}
