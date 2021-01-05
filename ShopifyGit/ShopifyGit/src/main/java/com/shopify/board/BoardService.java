package com.shopify.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.mapper.BoardMapper;
import com.shopify.shop.ShopData;
 
@Service
@Transactional

/**
 * 게시판 서비스
 *
 */

public class BoardService {
 
    @Autowired private BoardMapper boardMapper;
    
    @Autowired private UtilFn util;
    
    private Logger LOGGER = LoggerFactory.getLogger(BoardService.class);
    
    /**
	 * Q&A 조회
	 * @return
	 */
    public Map<String,Object> selectQna(BoardData boardData, HttpSession sess){
        
    	ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);

    	String eMail = sd.getEmail();
    	
    	boardData.setDivision("D010002");
    	boardData.setWriter(eMail);
    	boardData.setLocale(sd.getLocale());
		
		int dataCount = boardMapper.selectAllQnaCount(boardData);
		int currentPage = boardData.getCurrentPage(); //현제 페이지
		int pageSize = boardData.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		
		List<BoardData> qnaList = new ArrayList<BoardData>(); //데이터 리스트
		
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			boardData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			boardData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			qnaList = boardMapper.selectAllQna(boardData);//데이터 리스트
		}
		
		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", qnaList);
		map.put("paging", paging);
		
		return map;
		
    }
    
    
    /**
	 * Q&A 상세 조회
	 * @return
	 */
    public BoardData showQna(BoardData boardData, HttpSession sess){
        
    	ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
    	boardData.setLocale(sd.getLocale());
    	
		return boardMapper.showQna(boardData);
    }
    
    /**
	 * Q&A 등록
	 * @return
	 */
    public int insertQna(BoardData boardData, HttpSession sess) {
		
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
    	
		String msg = "";
    	String eMail = sd.getEmail();
		boardData.setWriter(eMail);
    	
    	return boardMapper.insertQna(boardData);

    }
    
    /**
	 * 셀러 FAQ 조회
	 * @return
	 */
    public Map<String,Object> selectFaq(BoardData boardData, HttpSession sess){
    	
    	ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);

    	if(sd == null) {
    		boardData.setLocale("KO");
    	} else {
    		boardData.setLocale(sd.getLocale());
    	}
    	
    	
    	boardData.setDivision("D010001");
    	
		
		int dataCount = boardMapper.selectAllFaqCount(boardData);
		int currentPage = boardData.getCurrentPage(); //현제 페이지
		int pageSize = boardData.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		
		List<BoardData> faqList = new ArrayList<BoardData>(); //데이터 리스트
		
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			boardData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			boardData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			faqList = boardMapper.selectAllFaq(boardData);//데이터 리스트
		}
		
		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", faqList);
		map.put("paging", paging);
		
		return map;
		
		
    }
    
	/**
	 * 게시물 삭제
	 * @return
	 * 여러개의 row를 삭제처리 하기 위해 Map으로 처리하였음
	 */
	public int deleteBoard(Map<String, Object> params,HttpSession sess) {
		
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		int result = 0;
		
		List list = (List) params.get("idx");
		
		Iterator rootItr = list.iterator();
		
		while (rootItr.hasNext()) {
    		String idx = (String) rootItr.next();

    		map.put("idx",idx);
    		
    		boardMapper.deleteBoard(map);
    		
    		result ++;
    		
    	}
		
		return result;
		
	}

}
