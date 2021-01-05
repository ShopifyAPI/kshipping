package com.shopify.admin.code;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.shopify.admin.AdminData;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.mapper.AdminCodeMapper;
import com.shopify.shop.ShopData;

/**
 * Admin 코드관리 서비스
 *
 */

@Service
@Transactional
public class AdminCodeService {
	private Logger LOGGER = LoggerFactory.getLogger(AdminCodeService.class);
	
	@Autowired 
	private AdminCodeMapper adminCodeMapper;
    @Autowired	
	private UtilFn util;
	
	/**
	 * 운영 관리 > 코드 관리 > 조회
	 * @return
	 */
    public Map<String,Object> selectAdminCode(AdminCodeData codeData, HttpSession sess){
        
//    	AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	
    	int dataCount = adminCodeMapper.selectAdminCodeCount(codeData); // 전체 건수 조회
		int currentPage = codeData.getCurrentPage(); //현제 페이지
		int pageSize = codeData.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수    	
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		Map<String, Object> returnMap = new HashMap<String, Object>();  // 리턴 변수 선언
		
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			
			codeData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			codeData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			List<AdminCodeData> list = adminCodeMapper.selectAdminCode(codeData); //데이터 리스트
			
			returnMap.put("list", list);
			returnMap.put("paging", paging);
			
		}
		
		return returnMap;
		
    }
    
    /**
	 * 운영 관리 > 코드 관리 > 코드등록(대분류)
	 * @return
	 */
    public int insertAdminCodeGroup(AdminCodeData codeData ,HttpSession sess) {
    	
//    	AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	
    	AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	
    	//대분류 중복체크
		int chk = adminCodeMapper.chkAdminCodeGroup(codeData);
		
		if(chk > 0) {
			return -1;
		}else {
			return adminCodeMapper.insertAdminCodeGroup(codeData);
		}

    }
    
    /**
	 * 운영 관리 > 코드 관리 > 코드등록(중분류)
	 * @return
	 */
    public int insertAdminCodeId(AdminCodeData codeData ,HttpSession sess) {
//    	AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);		
    	AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	//중분류 중복체크
		int chk = adminCodeMapper.chkAdminCodeId(codeData);
		
		codeData.setCodeRef(adminCodeMapper.selectCodeRef(codeData));
		
		if(chk > 0) {
			return -1;
		}else {
			return adminCodeMapper.insertAdminCodeId(codeData);
		}

    }
    
    /**
	 * 운영 관리 > 코드 관리 > 코드그룹 셀렉트박스 DATA 세팅
	 * @return
	 */
	public List<Map<String,String>> selectAdminCodeGroup(){
//    	AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
//		ShopData sd = (ShopData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		
	    List list = new ArrayList();
	    
	    list = adminCodeMapper.selectAdminCodeGroup();
	    
	    
		return list;
	}
    
	/**
	 * 운영 관리 > 코드 관리 > 코드그룹 수정
	 * @return
	 */
	public int updateAdminCode(AdminCodeData codeData ,HttpSession sess) {
//    	AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		
		return adminCodeMapper.updateAdminCode(codeData);
	}
    
	
	 /**
	 * 운영 관리 > 코드 관리  > 코드그룹 삭제
	 * @return
	 */
	public int deleteAdminCode(AdminCodeData codeData ,HttpSession sess) {
//    	AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		int result = 0;
		
		String key = codeData.getCkBox();
		
		String[] array = key.split(",");

		ArrayList list = new ArrayList(Arrays.asList(array));
		
		Iterator rootItr = list.iterator();
		
		while (rootItr.hasNext()) {
    		String codeId = (String) rootItr.next();

    		adminCodeMapper.deleteAdminCode(codeId);
    		
    		result ++;
    		
    	}
		
		return result;
		
	}
	
}