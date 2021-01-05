package com.shopify.cs;

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

import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.mapper.CsMapper;
import com.shopify.shop.ShopData;
 
@Service
@Transactional

/**
 * CS 서비스
 *
 */

public class CsService {
 
    @Autowired private CsMapper csMapper;
    @Autowired private UtilFn util;
    
    private Logger LOGGER = LoggerFactory.getLogger(CsService.class);

    /**
	 * CS관리 > 배송리스트 NEW
	 * @param Data
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectCsDeliveryList(CsData csData, HttpSession sess) {
		
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String local = sd.getLocale();
		String email = sd.getEmail();
		
		csData.setLocale(local);
		csData.setEmail(email);
		
		String searchWord = csData.getSearchWord();
		String searchType = csData.getSearchType();
		
		int dataCount = csMapper.selectCsDeliveryListCount(csData); // 전체 건수 조회
		int currentPage = csData.getCurrentPage(); //현제 페이지
		int pageSize = csData.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		//검색조건 이름일 경우 암호화 처리
		if(searchWord != null && !"".equals(searchWord)) {
			csData.setSearchWordAese(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, searchWord));
		}
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		List<CsData> list = new ArrayList<CsData>(); //데이터 리스트 변수 선언 
		
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			
			csData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			csData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			
			list = csMapper.selectCsDeliveryList(csData);
		}
		
		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("paging", paging);
		
		return map;
	}

	/**
	 * CS관리 > 배송 상세보기 NEW
	 * @param CsData
	 * @return Map<String, Object>
	 */
    public Map<String, Object> selectCsDeliveryView(CsData CsData, HttpSession sess) {
    	
    	ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String local = sd.getLocale();
		String email = sd.getEmail();
		CsData.setLocale(local);
		CsData.setEmail(email);
				
    	CsData detail = csMapper.selectCsDeliveryView(CsData);
    	
    	if(detail != null) {
    		String customerName = detail.getCustomerName();
    		String sellerName = detail.getSellerName();
    		String sellerPhone = detail.getSellerPhone();
    		String buyerFirstname = detail.getBuyerFirstname();
    		String buyerLastname = detail.getBuyerLastname();
    		String buyerPhone = detail.getBuyerPhone();
    		String hblNo = detail.getHblNo();
    		
    		try {
    			customerName = util.getAESDecrypt(SpConstants.ENCRYPT_KEY, customerName);
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

    	List<CsData> list = new ArrayList<CsData>();
    	String combineCode = CsData.getCombineCode();
    	
    	if(combineCode != null && "Y".equals(combineCode)) {
    		list = csMapper.selectCsDeliverySku(CsData); // 합배송
    	} else {
    		list = csMapper.selectCsDeliverySku(CsData); // 일반배송
    	}

    	// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("detail", detail);
		map.put("list", list);
		
		return map;
    }
    
    
    
    
    
    
    
    
    
	/**
	 * CS관리 > 반송리스트
	 * @param Data
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectBackList(CsData csData, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		String local = sd.getLocale();
		String searchWord = csData.getSearchWord();
		String searchType = csData.getSearchType();
		csData.setEmail(email);
		csData.setLocale(local);

		//검색조건 이름일 경우 암호화 처리
		if(searchWord != null && !"".equals(searchWord)) {
			csData.setSearchWordAese(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, searchWord));
		}
		
		int dataCount = csMapper.selectBackListCount(csData); // 전체 건수 조회
		int currentPage = csData.getCurrentPage(); //현제 페이지
		int pageSize = csData.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		List<CsData> list = new ArrayList<CsData>(); //데이터 리스트 변수 선언 
				
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			
			csData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			csData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			
			list = csMapper.selectBackList(csData);
			
			//암호화풀고 리스트 재생성
			int intA = 0;
			for(CsData item : list) {
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
	 * CS관리 > 반송리스트 엑셀다운
	 * @param Data
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectExcelList(CsData csData, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		String local = sd.getLocale();
		String searchWord = csData.getSearchWord();
		String searchType = csData.getSearchType();
		csData.setEmail(email);
		csData.setLocale(local);

		//검색조건 이름일 경우 암호화 처리
		if(searchWord != null && !"".equals(searchWord)) {
			csData.setSearchWordAese(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, searchWord));
		}
		
		int dataCount = csMapper.selectExcelListCount(csData); // 전체 건수 조회
		int currentPage = csData.getCurrentPage(); //현제 페이지
		int pageSize = csData.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		List<CsData> list = new ArrayList<CsData>(); //데이터 리스트 변수 선언 
				
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			
			csData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			csData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			
			list = csMapper.selectExcelList(csData);
			
			//암호화풀고 리스트 재생성
			int intA = 0;
			for(CsData item : list) {
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
	public Map<String, Object> selectExchangeList(CsData csData, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		String local = sd.getLocale();
		String searchWord = csData.getSearchWord();
		String searchType = csData.getSearchType();
		csData.setEmail(email);
		csData.setLocale(local);
		
		//검색조건 이름일 경우 암호화 처리
		if(searchWord != null && !"".equals(searchWord)) {
			csData.setSearchWordAese(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, searchWord));
		}
        
		int dataCount = csMapper.selectExchangeListCount(csData); // 전체 건수 조회
		int currentPage = csData.getCurrentPage(); //현제 페이지
		int pageSize = csData.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		List<CsData> list = new ArrayList<CsData>(); //데이터 리스트 변수 선언 
				
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			
			csData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			csData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			
			list = csMapper.selectExchangeList(csData);
			
			//암호화풀고 리스트 재생성
			int intA = 0;
			for(CsData item : list) {
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
	public Map<String, Object> selectReturnList(CsData csData, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		String local = sd.getLocale();
		String searchWord = csData.getSearchWord();
		String searchType = csData.getSearchType();
		csData.setEmail(email);
		csData.setLocale(local);
		
		//검색조건 이름일 경우 암호화 처리
		if(searchWord != null && !"".equals(searchWord)) {
			csData.setSearchWordAese(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, searchWord));
		}
        
		
		int dataCount = csMapper.selectReturnListCount(csData); // 전체 건수 조회
		int currentPage = csData.getCurrentPage(); //현제 페이지
		int pageSize = csData.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		List<CsData> list = new ArrayList<CsData>(); //데이터 리스트 변수 선언 
				
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			
			csData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			csData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			
			list = csMapper.selectReturnList(csData);
			
			//암호화풀고 리스트 재생성
			int intA = 0;
			for(CsData item : list) {
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
	 * CS관리 > 추가요금 리스트
	 * @param Data
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectPaymentList(CsData csData, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		String local = sd.getLocale();
		String searchWord = csData.getSearchWord();
		String searchType = csData.getSearchType();
		csData.setEmail(email);
		csData.setLocale(local);
		
		//검색조건 이름일 경우 암호화 처리
		if(searchWord != null && !"".equals(searchWord)) {
			csData.setSearchWordAese(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, searchWord));
		}
        
		
		int dataCount = csMapper.selectPaymentListCount(csData); // 전체 건수 조회
		int currentPage = csData.getCurrentPage(); //현제 페이지
		int pageSize = csData.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		List<CsData> list = new ArrayList<CsData>(); //데이터 리스트 변수 선언 
				
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			
			csData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			csData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			
			list = csMapper.selectPaymentList(csData);
			
			//암호화풀고 리스트 재생성
			int intA = 0;
			for(CsData item : list) {
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
	public int updateCsStatus(CsData csData, HttpSession sess) {	
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		csData.setEmail(email);
		
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
				csMapper.updateCsStatus(csData);
				result ++;
			}
		}
		
		return result;
	}
	
	/**
	 * CS관리 > 반송리스트 디테일(팝업)
	 * @param Data
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectBackDetailList(CsData csData, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		String local = sd.getLocale();
		csData.setEmail(email);
		csData.setLocale(local);
		
		List<CsData> detail = new ArrayList<CsData>(); //데이터 리스트 변수 선언 
				
//		detail = csMapper.selectBackDetailList(csData);
			
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
	public Map<String, Object> selectExchangeDetailList(CsData csData, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		String local = sd.getLocale();
		csData.setEmail(email);
		csData.setLocale(local);
		
		List<CsData> detail = new ArrayList<CsData>(); //데이터 리스트 변수 선언 
				
		detail = csMapper.selectExchangeDetailList(csData);
			
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
	public Map<String, Object> selectReturnDetailList(CsData csData, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		String local = sd.getLocale();
		csData.setEmail(email);
		csData.setLocale(local);
		
		List<CsData> detail = new ArrayList<CsData>(); //데이터 리스트 변수 선언 
				
		detail = csMapper.selectReturnDetailList(csData);
			
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
	public Map<String, Object> selectPaymentDetailList(CsData csData, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		String local = sd.getLocale();
		csData.setEmail(email);
		csData.setLocale(local);
		
		List<CsData> detail = new ArrayList<CsData>(); //데이터 리스트 변수 선언 
				
		detail = csMapper.selectPaymentDetailList(csData);
			
		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("detail", detail);
		
		return map;
	}
	
	//CS > 상세보기 count (상태 반송만 조회)
    public int selectCsBackCount(CsData csData, HttpSession sess) {
        return csMapper.selectCsBackCount(csData);
    }
    
    //CS > 상세보기 count (상태 교환만 조회)
    public int selectCsExchangeCount(CsData csData, HttpSession sess) {
        return csMapper.selectCsExchangeCount(csData);
    }
    
    //CS > 상세보기 count (상태 반품만 조회)
    public int selectCsReturnCount(CsData csData, HttpSession sess) {
        return csMapper.selectCsReturnCount(csData);
    }
    //CS > 상세보기 count (상태 추가요금만 조회)
    public int selectCsPaymentCount(CsData csData, HttpSession sess) {
        return csMapper.selectCsPaymentCount(csData);
    }
    
	
    //CS > CS상세(반송 디테일 data 조회)
    public CsData selectCsBackDetail(CsData csData) {
        return csMapper.selectCsBackDetail(csData);
    }
    
    //CS > CS상세(교환 디테일 data 조회)
    public CsData selectCsExchangeDetail(CsData csData) {
        return csMapper.selectCsExchangeDetail(csData);
    }
    
    //CS > CS상세(반품 디테일 data 조회)
    public CsData selectCsReturnDetail(CsData csData) {
        return csMapper.selectCsReturnDetail(csData);
    }
    
    //CS > CS상세(추가요금 디테일 data 조회)
    public CsData selectCsPaymentDetail(CsData csData) {
        return csMapper.selectCsPaymentDetail(csData);
    }
	
}
