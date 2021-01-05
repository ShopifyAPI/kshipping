package com.shopify.admin.cs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopify.common.MailService;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.cs.CsData;
import com.shopify.admin.AdminData;
import com.shopify.admin.board.AdminBoardData;
import com.shopify.admin.cs.AdminCsData;
import com.shopify.admin.cs.popup.AdminCsPopupData;
import com.shopify.mapper.AdminCsMapper;
import com.shopify.shop.ShopData;
 
@Service
@Transactional

/**
 * CS 서비스
 *
 */

public class AdminCsService {
 
    @Autowired private AdminCsMapper csMapper;
    @Autowired private UtilFn util;
    @Autowired private MailService mailService;
    
    private Logger LOGGER = LoggerFactory.getLogger(AdminCsService.class);
    
    
    /**
	 * CS관리 > 배송리스트 NEW
	 * @param Data
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectAdminCsDeliveryList(AdminCsData csData, HttpSession sess) {
		
		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		String local = adminData.getLocale();
		csData.setLocale(local);
		String searchWord = csData.getSearchWord();
		String searchType = csData.getSearchType();
		
		int dataCount = csMapper.selectAdminCsDeliveryListCount(csData); // 전체 건수 조회
		int currentPage = csData.getCurrentPage(); //현제 페이지
		int pageSize = csData.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		//검색조건 이름일 경우 암호화 처리
		if(searchWord != null && !"".equals(searchWord)) {
			csData.setSearchWordAese(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, searchWord));
		}
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		List<AdminCsData> list = new ArrayList<AdminCsData>(); //데이터 리스트 변수 선언 
		
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			
			csData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			csData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			
			list = csMapper.selectAdminCsDeliveryList(csData);
		}
		
		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("paging", paging);
		
		return map;
	}

	/**
	 * CS관리 > 배송 상세보기 NEW
	 * @param AdminCsData
	 * @return Map<String, Object>
	 */
    public Map<String, Object> selectAdminCsDeliveryView(AdminCsData adminCsData) {

    	AdminCsData detail = csMapper.selectAdminCsDeliveryView(adminCsData);
    	
    	if(detail != null) {
    		String customerName = detail.getCustomerName();
    		String sellerName = detail.getSellerName();
    		String sellerPhone = detail.getSellerPhone();
    		String buyerFirstname = detail.getBuyerFirstname();
    		String buyerLastname = detail.getBuyerLastname();
    		String buyerPhone = detail.getBuyerPhone();
    		
    		try {
    			customerName = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, customerName);
    			if("null".equals(customerName) || " null".equals(customerName))
    				customerName = "GUEST";
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
    		try {
    			sellerName = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sellerName);
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
    		try {
    			sellerPhone = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sellerPhone);
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
    		try {
    			buyerFirstname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, buyerFirstname);
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
    		try {
    			buyerLastname = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, buyerLastname);
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
    		try {
    			buyerPhone = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, buyerPhone);
			} catch (Exception e) {
				e.printStackTrace();
			}
    		 
    		detail.setCustomerName(customerName);
    		detail.setSellerName(sellerName);
    		detail.setSellerPhone(sellerPhone);
    		detail.setBuyerFirstname(buyerFirstname);
    		detail.setBuyerLastname(buyerLastname);
    		detail.setBuyerPhone(buyerPhone);
    	}

    	List<AdminCsPopupData> list = new ArrayList<AdminCsPopupData>();
    	String combineCode = adminCsData.getCombineCode();
    	
    	if(combineCode != null && "Y".equals(combineCode)) {
    		list = csMapper.selectAdminCsDeliverySku(adminCsData); // 합배송
    	} else {
    		list = csMapper.selectAdminCsDeliverySku(adminCsData); // 일반배송
    	}

    	// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("detail", detail);
		map.put("list", list);
		
		return map;
    }
    
	/**
	 * CS관리 > 추가요금 리스트
	 * @param Data
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectAdminPaymentList(AdminCsData csData, HttpSession sess) {
		// 세션 이메일 받아 오기
		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		String local = adminData.getLocale();

		csData.setLocale(local);
		String searchWord = csData.getSearchWord();
		String searchType = csData.getSearchType();
		
		//검색조건 이름일 경우 암호화 처리
		if(searchWord != null && !"".equals(searchWord)) {
			csData.setSearchWordAese(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, searchWord));
		}
        
		
		int dataCount = csMapper.selectAdminPaymentListCount(csData); // 전체 건수 조회
		int currentPage = csData.getCurrentPage(); //현제 페이지
		int pageSize = csData.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		List<AdminCsData> list = new ArrayList<AdminCsData>(); //데이터 리스트 변수 선언 
				
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			
			csData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			csData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			
			list = csMapper.selectAdminPaymentList(csData);
			
			//암호화풀고 리스트 재생성
			int intA = 0;
			for(AdminCsData item : list) {
			    String fName = item.getBuyerFirstname();
			    String lName = item.getBuyerLastname();
			    String phone = item.getBuyerPhone(); 
			    if(fName != null) item.setBuyerFirstname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, fName));
			    if(lName != null) item.setBuyerLastname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, lName));
			    if(phone != null) item.setBuyerPhone(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, phone));
			    list.set(intA, item);
			    intA++;
			}
			
		}

		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("paging", paging);
		
		return map;
	}
	
	/**
	 * CS 관리 > 추가요금 상태변경
	 * @param CsData
	 * @param HttpSession
	 * @return int
	 */
	public int updateAdminCsPaymentStatus(AdminCsData csData, HttpSession sess) {	
		
		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		
		String[] arrCkBox = csData.getCkBox().split(",");
		int result = 0;
		
		// 결재완료 'Y'
		csData.setPayState(SpConstants.PAYMENT_STATE_END);
		
		//상태체크
		for (String masterCode : arrCkBox){
			if(!masterCode.equals("")) {
				csData.setMasterCode(masterCode);
				result = csMapper.updateAdminCsPaymentStatus(csData);
				result ++;
			}
		}
		
		return result;
	}
    
	
	
	
	
	
	
	
	
	
	
	
	
	
    
    
    
    
    /**
	 * CS관리 > 배송리스트
	 * @param Data
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectAdminDeliveryList(AdminCsData csData, HttpSession sess) {
		// 세션 이메일 받아 오기

		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		String local = adminData.getLocale();
		csData.setLocale(local);
		String searchWord = csData.getSearchWord();
		String searchType = csData.getSearchType();
		
		//검색조건 이름일 경우 암호화 처리
		if(searchWord != null && !"".equals(searchWord)) {
			csData.setSearchWordAese(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, searchWord));
		}
        

		int dataCount = csMapper.selectAdminDeliveryListCount(csData); // 전체 건수 조회
		int currentPage = csData.getCurrentPage(); //현제 페이지
		int pageSize = csData.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		List<AdminCsData> list = new ArrayList<AdminCsData>(); //데이터 리스트 변수 선언 
				
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			
			csData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			csData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			
			list = csMapper.selectAdminDeliveryList(csData);
			
			//암호화풀고 리스트 재생성
			int intA = 0;
			for(AdminCsData item : list) {
			    String fName = item.getBuyerFirstname();
			    String lName = item.getBuyerLastname();
			    String phone = item.getBuyerPhone(); 
			    if(fName != null) item.setBuyerFirstname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, fName));
			    if(lName != null) item.setBuyerLastname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, lName));
			    if(phone != null) item.setBuyerPhone(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, phone));
			    list.set(intA, item);
			    intA++;
			}
			
		}

		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("paging", paging);
		
		return map;
	}
    
	/**
	 * CS관리 > 반송리스트
	 * @param Data
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectAdminBackList(AdminCsData csData, HttpSession sess) {
		// 세션 이메일 받아 오기
		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		String local = adminData.getLocale();

		csData.setLocale(local);
		String searchWord = csData.getSearchWord();
		String searchType = csData.getSearchType();

		//검색조건 이름일 경우 암호화 처리
		if(searchWord != null && !"".equals(searchWord)) {
			csData.setSearchWordAese(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, searchWord));
		}
        
		
		int dataCount = csMapper.selectAdminBackListCount(csData); // 전체 건수 조회
		int currentPage = csData.getCurrentPage(); //현제 페이지
		int pageSize = csData.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		List<AdminCsData> list = new ArrayList<AdminCsData>(); //데이터 리스트 변수 선언 
				
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			
			csData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			csData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			
			list = csMapper.selectAdminBackList(csData);
			
			//암호화풀고 리스트 재생성
			int intA = 0;
			for(AdminCsData item : list) {
			    String fName = item.getBuyerFirstname();
			    String lName = item.getBuyerLastname();
			    String phone = item.getBuyerPhone(); 
			    if(fName != null) item.setBuyerFirstname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, fName));
			    if(lName != null) item.setBuyerLastname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, lName));
			    if(phone != null) item.setBuyerPhone(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, phone));
			    list.set(intA, item);
			    intA++;
			}
			
		}

		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("paging", paging);
		
		return map;
	}
	
	/**
	 * CS관리 > 교환리스트
	 * @param Data
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectAdminExchangeList(AdminCsData csData, HttpSession sess) {
		// 세션 이메일 받아 오기
		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		String local = adminData.getLocale();

		csData.setLocale(local);
		String searchWord = csData.getSearchWord();
		String searchType = csData.getSearchType();
		
		//검색조건 이름일 경우 암호화 처리
		if(searchWord != null && !"".equals(searchWord)) {
			csData.setSearchWordAese(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, searchWord));
		}
        
		
		int dataCount = csMapper.selectAdminExchangeListCount(csData); // 전체 건수 조회
		int currentPage = csData.getCurrentPage(); //현제 페이지
		int pageSize = csData.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		List<AdminCsData> list = new ArrayList<AdminCsData>(); //데이터 리스트 변수 선언 
				
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			
			csData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			csData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			
			list = csMapper.selectAdminExchangeList(csData);
			
			//암호화풀고 리스트 재생성
			int intA = 0;
			for(AdminCsData item : list) {
			    String fName = item.getBuyerFirstname();
			    String lName = item.getBuyerLastname();
			    String phone = item.getBuyerPhone(); 
			    if(fName != null) item.setBuyerFirstname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, fName));
			    if(lName != null) item.setBuyerLastname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, lName));
			    if(phone != null) item.setBuyerPhone(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, phone));
			    list.set(intA, item);
			    intA++;
			}
			
		}

		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("paging", paging);
		
		return map;
	}
	
	/**
	 * CS관리 > 반품리스트
	 * @param Data
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectAdminReturnList(AdminCsData csData, HttpSession sess) {
		// 세션 이메일 받아 오기
		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		String local = adminData.getLocale();
		
		csData.setLocale(local);
		String searchWord = csData.getSearchWord();
		String searchType = csData.getSearchType();
		
		//검색조건 이름일 경우 암호화 처리
		if(searchWord != null && !"".equals(searchWord)) {
			csData.setSearchWordAese(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, searchWord));
		}
        
		
		int dataCount = csMapper.selectAdminReturnListCount(csData); // 전체 건수 조회
		int currentPage = csData.getCurrentPage(); //현제 페이지
		int pageSize = csData.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		List<AdminCsData> list = new ArrayList<AdminCsData>(); //데이터 리스트 변수 선언 
				
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			
			csData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			csData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			
			list = csMapper.selectAdminReturnList(csData);
			
			//암호화풀고 리스트 재생성
			int intA = 0;
			for(AdminCsData item : list) {
			    String fName = item.getBuyerFirstname();
			    String lName = item.getBuyerLastname();
			    String phone = item.getBuyerPhone(); 
			    if(fName != null) item.setBuyerFirstname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, fName));
			    if(lName != null) item.setBuyerLastname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, lName));
			    if(phone != null) item.setBuyerPhone(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, phone));
			    list.set(intA, item);
			    intA++;
			}
			
		}

		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("paging", paging);
		
		return map;
	}
	
	
	
	/**
	 * CS 관리 > 상태변경
	 * @param CsData
	 * @param HttpSession
	 * @return int
	 */
	public int updateAdminCsStatus(AdminCsData csData, HttpSession sess) {	
		
		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		
		String[] delIdx = csData.getCkBox().split(",");
		int result = 0;
		
		//상태체크
		for (String idx : delIdx){
			if(!idx.equals("")) {
				csData.setMasterCode(idx);
				
				String status = csMapper.csStatusChk(csData);
				
				// A040040:교환 완료 , A050030:반품 완료
				if(status.equals("A040040") || status.equals("A050030")) {
					return -1;
				}
				
				result ++;
			}
		}
		
		//상태변경
		for (String idx : delIdx){
			if(!idx.equals("")) {
				csData.setMasterCode(idx);
				csMapper.updateAdminCsStatus(csData);
				result ++;
			}
		}
		
		return result;
	}
	
	/**
	 * CS 관리 > 메일발송
	 * @param CsData
	 * @param HttpSession
	 * @return int
	 */
	public int sendMailAdminCs(AdminCsData csData, HttpSession sess) {	
		
		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		int result = 0;
		String mCode = csData.getCkBox();
		String title = "테스트 메일";
		String contents = "테스트 내용";
		
		List<AdminCsData> cdList = new ArrayList<AdminCsData>();
		//다중선택 메일발송
		if(mCode != null && mCode.indexOf(",") > 0) {
			String[] mCodeList = mCode.split(",");
			csData.setMasterCodeList(mCodeList);
			cdList = csMapper.selectAdminCsPaymentList(csData);
			//메일발송
			for (AdminCsData csItem : cdList){
				String email = csItem.getEmail();
				boolean chk = false;
				if(email != null) {
					email = "shim.yun@uniwiz.co.kr";		//임시테스트용
					chk = mailService.sendMail(email, title, contents);
				}
				if(chk) {
					result ++;
				}
			}
		}else {
			//단일선택 메일발송
			csData.setMasterCode(mCode);
			cdList = csMapper.selectAdminCsPaymentList(csData);
			String email = cdList.get(0).getEmail();
			boolean chk = false;
			if(email != null) {
				email = "shim.yun@uniwiz.co.kr";		//임시테스트용
				chk = mailService.sendMail(email, title, contents);
			}
			if(chk) {
				result ++;
			}
			
		}
		
		return result;
	}
	
	/**
	 * CS관리 > 배송리스트 디테일(팝업)
	 * @param Data
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectAdminDeliveryDetailList(AdminCsData csData, HttpSession sess) {
		// 세션 이메일 받아 오기
		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		String local = adminData.getLocale();

		csData.setLocale(local);

		List<AdminCsData> detail = new ArrayList<AdminCsData>(); //데이터 리스트 변수 선언 
				
		detail = csMapper.selectAdminDeliveryDetailList(csData);
			
		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("detail", detail);
		
		return map;
	}
	
	/**
	 * CS관리 > 반송리스트 디테일(팝업)
	 * @param Data
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectAdminBackDetailList(AdminCsData csData, HttpSession sess) {
		// 세션 이메일 받아 오기
		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		String local = adminData.getLocale();

		csData.setLocale(local);
		
		List<AdminCsData> detail = new ArrayList<AdminCsData>(); //데이터 리스트 변수 선언 
				
		detail = csMapper.selectAdminBackDetailList(csData);
			
		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("detail", detail);
		
		return map;
	}
	
	/**
	 * CS관리 > 교환리스트 디테일(팝업)
	 * @param Data
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectAdminExchangeDetailList(AdminCsData csData, HttpSession sess) {
		// 세션 이메일 받아 오기
		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		String local = adminData.getLocale();

		csData.setLocale(local);
		
		List<AdminCsData> detail = new ArrayList<AdminCsData>(); //데이터 리스트 변수 선언 
				
		detail = csMapper.selectAdminExchangeDetailList(csData);
			
		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("detail", detail);
		
		return map;
	}
	
	/**
	 * CS관리 > 반품리스트 디테일(팝업)
	 * @param Data
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectAdminReturnDetailList(AdminCsData csData, HttpSession sess) {
		// 세션 이메일 받아 오기
		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		String local = adminData.getLocale();

		csData.setLocale(local);
		
		List<AdminCsData> detail = new ArrayList<AdminCsData>(); //데이터 리스트 변수 선언 
				
		detail = csMapper.selectAdminReturnDetailList(csData);
			
		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("detail", detail);
		
		return map;
	}
	
	/**
	 * CS관리 > 추가요금 리스트 디테일(팝업)
	 * @param Data
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectAdminPaymentDetailList(AdminCsData csData, HttpSession sess) {
		// 세션 이메일 받아 오기
		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		String local = adminData.getLocale();

		csData.setLocale(local);
		
		List<AdminCsData> detail = new ArrayList<AdminCsData>(); //데이터 리스트 변수 선언 
				
		detail = csMapper.selectAdminPaymentDetailList(csData);
			
		//리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("detail", detail);
		
		return map;
	}
	//Admin CS > 상세보기 count (전체 배송조회)
    public int selectAdminCsDeliveryCount(AdminCsData adminCsData, HttpSession sess) {
        return csMapper.selectAdminCsDeliveryCount(adminCsData);
    }
	
	//Admin CS > 상세보기 count (상태 반송만 조회)
    public int selectAdminCsBackCount(AdminCsData adminCsData, HttpSession sess) {
        return csMapper.selectAdminCsBackCount(adminCsData);
    }
    
    //Admin CS > 상세보기 count (상태 교환만 조회)
    public int selectAdminCsExchangeCount(AdminCsData adminCsData, HttpSession sess) {
        return csMapper.selectAdminCsExchangeCount(adminCsData);
    }
    
    //Admin CS > 상세보기 count (상태 반품만 조회)
    public int selectAdminCsReturnCount(AdminCsData adminCsData, HttpSession sess) {
        return csMapper.selectAdminCsReturnCount(adminCsData);
    }
    //Admin CS > 상세보기 count (상태 추가요금만 조회)
    public int selectAdminCsPaymentCount(AdminCsData adminCsData, HttpSession sess) {
        return csMapper.selectAdminCsPaymentCount(adminCsData);
    }
    
    //Admin CS > CS상세(전체 디테일 data 조회)
    public AdminCsData selectAdminCsDeliveryDetail(AdminCsData adminCsData) {
        return csMapper.selectAdminCsDeliveryDetail(adminCsData);
    }
	
    //Admin CS > CS상세(반송 디테일 data 조회)
    public AdminCsData selectAdminCsBackDetail(AdminCsData adminCsData) {
        return csMapper.selectAdminCsBackDetail(adminCsData);
    }
    
    //Admin CS > CS상세(교환 디테일 data 조회)
    public AdminCsData selectAdminCsExchangeDetail(AdminCsData adminCsData) {
        return csMapper.selectAdminCsExchangeDetail(adminCsData);
    }
    
    //Admin CS > CS상세(반품 디테일 data 조회)
    public AdminCsData selectAdminCsReturnDetail(AdminCsData adminCsData) {
        return csMapper.selectAdminCsReturnDetail(adminCsData);
    }
    
    //Admin CS > CS상세(추가요금 디테일 data 조회)
    public AdminCsData selectAdminCsPaymentDetail(AdminCsData adminCsData) {
        return csMapper.selectAdminCsPaymentDetail(adminCsData);
    }
	
    /**
	 * CS관리 > 리스트 엑셀다운
	 * @param Data
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectAdminExcelList(AdminCsData adminCsData, HttpSession sess) {
		// 세션 이메일 받아 오기
		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		String email = adminData.getEmail();
		String local = adminData.getLocale();
		String searchWord = adminCsData.getSearchWord();
		String searchType = adminCsData.getSearchType();
		adminCsData.setEmail(email);
		adminCsData.setLocale(local);

		//검색조건 이름일 경우 암호화 처리
		if(searchWord != null && !"".equals(searchWord)) {
			adminCsData.setSearchWordAese(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, searchWord));
		}
		
		int dataCount = csMapper.selectExcelListCount(adminCsData); // 전체 건수 조회
		int currentPage = adminCsData.getCurrentPage(); //현제 페이지
		int pageSize = adminCsData.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		List<AdminCsData> list = new ArrayList<AdminCsData>(); //데이터 리스트 변수 선언 
				
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			
			adminCsData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			adminCsData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			
			list = csMapper.selectExcelList(adminCsData);
			
			//암호화풀고 리스트 재생성
			int intA = 0;
			for(AdminCsData item : list) {
			    String fName = item.getBuyerFirstname();
			    String lName = item.getBuyerLastname();
			    String phone = item.getBuyerPhone(); 
			    if(fName != null) item.setBuyerFirstname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, fName));
			    if(lName != null) item.setBuyerLastname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, lName));
			    if(phone != null) item.setBuyerPhone(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, phone));
			    list.set(intA, item);
			    intA++;
			}
			
		}

		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("paging", paging);
		
		return map;
	}
    
    
}
