package com.cafe24.mysite.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cafe24.mysite.repository.BoardDAO;
import com.cafe24.mysite.vo.BoardVO;

@Service
public class BoardService {

	private static final String SAVE_PATH = "/mysite-uploads";
	private static final String URL = "/images";
	
	@Autowired
	private BoardDAO boardDao;
	
	public List<BoardVO> getList(Long startRow, Long pageSize) {
		return boardDao.getList(startRow, pageSize);
	}
	
	public Long getLastGroupNo() {
		return boardDao.getLastGroupNo();
	}
	
	//상세보기
	public BoardVO getOne(Long no) {
		return boardDao.getOne(no);
	}
	
	//전체 게시물 수
	public Long getCount() {
		return boardDao.getCount();
	}
	
	//검색 게시물 수
	public Long getCount(String type, String keyword) throws SQLException {
		return boardDao.getCount(type, keyword);
	}
	
	//검색 게시물 리스트
	public List<BoardVO> search(String type, String keyword, Long startRow, Long pageSize) throws SQLException {
		return boardDao.search(type, keyword, startRow, pageSize);
	}
	
	//게시물 삭제(조회)
	public List<BoardVO> getRelatedList(Long groupNo, Long depth, Long orderNo) {
		return boardDao.getRelatedList(groupNo, depth, orderNo);
	}
	
	public void hitUp(Long no) {
		boardDao.updateHit(no);
		return;
	}
	
	//신규 글쓰기
	public boolean insert(String title, String contents, Long lastGroupNo, Long userNo, String url) {
		return boardDao.insert(title, contents, lastGroupNo, userNo, url);
	}
	
	//orderNo+1
	public void updateOrderNo(Long groupNo, Long orderNo) {
		boardDao.updateOrderNo(groupNo, orderNo);
	}
	
	//답글 글쓰기
	public boolean rewrite(BoardVO vo) {
		return boardDao.insert(vo);
	}
	
	//게시물 삭제
	public boolean delete(Long no) {
		return boardDao.delete(no);
	}
	
	//게시물 수정
	public boolean update(BoardVO vo) {
		return boardDao.update(vo);
	}
	
	public String restore(MultipartFile multipartFile) {
		String url = "";

		try {
		
			if(multipartFile.isEmpty()) {
				return url;
			}
			
			String originalFilename = 
					multipartFile.getOriginalFilename();
			String extName = originalFilename.substring(originalFilename.lastIndexOf('.')+1);
			String saveFileName = generateSaveFileName(extName);
			long fileSize = multipartFile.getSize();
			
			System.out.println("##########" + originalFilename);
			System.out.println("##########" + extName);
			System.out.println("##########" + saveFileName);
			System.out.println("##########" + fileSize);
			
			byte[] fileData = multipartFile.getBytes();
			OutputStream os = new FileOutputStream(SAVE_PATH + "/" + saveFileName);
			os.write(fileData);
			os.close();
			
			url = URL + "/" + saveFileName;
			
		} catch (IOException e) {
			throw new RuntimeException("Fileupload error:" + e);
		}
		
		return url;
	}
	
	private String generateSaveFileName(String extName) {
		String filename = "";
		Calendar calendar = Calendar.getInstance();
		
		filename += calendar.get(Calendar.YEAR);
		filename += calendar.get(Calendar.MONTH);
		filename += calendar.get(Calendar.DATE);
		filename += calendar.get(Calendar.HOUR);
		filename += calendar.get(Calendar.MINUTE);
		filename += calendar.get(Calendar.SECOND);
		filename += calendar.get(Calendar.MILLISECOND);
		filename += ("." + extName);
		
		return filename;
	}
	
}
