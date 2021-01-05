package com.shopify.admin.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.LocaleResolver;

import com.shopify.admin.AdminData;
import com.shopify.board.BoardData;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.mapper.AdminBoardMapper;
import com.shopify.shop.ShopData;

/**
 * Admin 서비스
 *
 */

@Service
@Transactional
public class AdminBoardService {
	private Logger LOGGER = LoggerFactory.getLogger(AdminBoardService.class);
	
	@Autowired private AdminBoardMapper adminBoardMapper;
    @Autowired private UtilFn util;
	
	/**
	 * 고객센터 > 관리자 Q&A 조회
	 * @return
	 */
    public Map<String,Object> selectQna(AdminBoardData boardData, HttpSession sess){
        
    	AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	
    	boardData.setDivision("D010002");
    	boardData.setLocale(adminData.getLocale());
		
		int dataCount = adminBoardMapper.selectAllQnaCount(boardData);
		int currentPage = boardData.getCurrentPage(); //현제 페이지
		int pageSize = boardData.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		
		List<AdminBoardData> qnaList = new ArrayList<AdminBoardData>(); //데이터 리스트
		
		List<AdminBoardData> returnList = new ArrayList<AdminBoardData>();//복호화처리한 데이터 리스트
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			boardData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			boardData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			qnaList = adminBoardMapper.selectAllQna(boardData);//데이터 리스트
			
			for(AdminBoardData item : qnaList){
				
				String firstName = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, item.getFirstName());
				String lastName = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, item.getLastName());
			    String fullName = firstName.concat(lastName);
				item.setWriter(fullName);
				
				//답변완료되었을 경우 답변작성자 set
				if(item.getAdmName() != null ) {
					String admName = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, item.getAdmName());
					item.setUpWriter(admName);
				}			
				returnList.add(item);
			}
		}
		
		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", returnList);
		map.put("paging", paging);
		
		return map;

    }
    	
    /**
	 * Q&A 상세 조회(관리자 답변등록시)
	 * @return
	 */
    public AdminBoardData selectAdmQnaAnswer(AdminBoardData boardData, HttpSession sess){
    	
    	AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	boardData.setLocale(adminData.getLocale());
    	
    	AdminBoardData data = adminBoardMapper.selectAdmQnaAnswer(boardData);
    	
    	String adminName = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, data.getUpWriter());
    	data.setUpWriter(adminName);
    	
    	return data;
    }
    
    /**
	 * Q&A 수정(관리자 답변 등록)
	 * @return
	 */
	public int updateBoardAnswer(AdminBoardData boardData, HttpSession sess) {
		
		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		boardData.setLocale(adminData.getLocale());
		boardData.setUpWriter(adminData.getEmail());	
		
		return adminBoardMapper.updateBoardAnswer(boardData);
		
	}
	
	/**
	 * 관리자 FAQ 조회
	 * @return
	 */
    public Map<String, Object> selectFaq(AdminBoardData boardData, HttpSession sess){

    	AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	
    	boardData.setDivision("D010001");
    	boardData.setLocale(adminData.getLocale());
		
		int dataCount = adminBoardMapper.selectAllFaqCount(boardData);
		int currentPage = boardData.getCurrentPage(); //현제 페이지
		int pageSize = boardData.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		
		List<AdminBoardData> returnList = new ArrayList<AdminBoardData>();//복호화처리한 데이터 리스트
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			boardData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			boardData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			returnList = adminBoardMapper.selectAllFaq(boardData);//데이터 리스트
		}
		
		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", returnList);
		map.put("paging", paging);
		
		return map;
    }
    	
    /**
	 * 관리자 FAQ 상세조회
	 * @return
	 */
    public AdminBoardData selectFaqShow(AdminBoardData boardData, HttpSession sess){
    	
    	AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	
        return adminBoardMapper.selectFaqShow(boardData);
    }
    
    /**
	 * FAQ 등록
	 * @return
	 */
    public int insertBoard(AdminBoardData boardData,HttpSession sess) {
    	
//    	AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
//    	String eMail = adminData.getEmail();
    	AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	String eMail = sd.getEmail();
    	
		boardData.setWriter(eMail);
    	
		return adminBoardMapper.insertBoard(boardData);

    }
    
    /**
	 * FAQ 수정
	 * @return
	 */
	public int updateFaq(AdminBoardData boardData, HttpSession sess) {
		
//		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
//		String eMail = adminData.getEmail();
		
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	String eMail = sd.getEmail();
		
		boardData.setWriter(eMail);
		
		return adminBoardMapper.updateFaq(boardData);
		
	}
	
	/**
	 * 관리자 게시물 삭제(Q&A , FAQ)
	 * @return
	 * 여러개의 row를 삭제처리 하기 위해 Map으로 처리하였음
	 */
	public int deleteBoard(AdminBoardData boardData, HttpSession sess) {
		
//		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		
		int result = 0;
		
		String key = boardData.getCkBox();
		
		String[] array = key.split(",");

		ArrayList list = new ArrayList(Arrays.asList(array));
		
		Iterator rootItr = list.iterator();
		
		while (rootItr.hasNext()) {
    		String idx = (String) rootItr.next();

    		adminBoardMapper.deleteBoard(idx);
    		
    		result ++;
    		
    	}
		
		return result;
		
	}
    
}