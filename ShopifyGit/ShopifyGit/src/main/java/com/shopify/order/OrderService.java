package com.shopify.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.shopify.api.ShopifyApiController;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.mapper.OrderMapper;
import com.shopify.mapper.ShopMapper;
import com.shopify.shop.ShopData;

/**
 * 주문 서비스
 *
 */
@Service
@Transactional
public class OrderService {
	private Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
	@Autowired private OrderMapper orderMapper;
	@Autowired private ShopMapper shopMapper;
	@Autowired private ShopifyApiController sApiController;
	@Autowired private UtilFn util;

	/**
     * api > 주문조회 
     * @return
     */
	public Map<String,Object> shopifyGetOrder(Model model, HttpServletRequest req, HttpSession sess) {
		return sApiController.shopifyGetOrder(model, req, sess);
	}

//	/**
//     * 주문 > 주문생성 
//     * @return
//     */
//	public int insertOrder(OrderData order){
//		
//		try {
//			LOGGER.debug(">>>>> 주문저장 Service [insertOrder] order : " + order);
//			return orderMapper.insertOrder(order);
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOGGER.debug(">>>>> 주문저장 Service [insertOrder] ERR : " + order.toString());
//			return 0;
//		}
//	}
    
	public int insertOrderOne(OrderData order){
		return orderMapper.insertOrderOne(order);
	}
	
//	/**
//     * 주문 > 주문수정 
//     * @return
//     */
//	public int updateOrder(OrderData order){
//		try {
//			LOGGER.debug(">>>>> 주문저장 Service [updateOrder] order : " + order);
//			return orderMapper.updateOrder(order);
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOGGER.debug(">>>>> 주문저장 Service [updateOrder] ERR : " + order.toString());
//			return 0;
//		}
//	}
	
	public int updateOrderOne(OrderData order){
		return orderMapper.updateOrderOne(order);
	}
    
	/**
     * 주문 > 주문삭제 
     * @return
     */
	public int deleteOrder(OrderData order,HttpSession sess){
		
		ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		
		
		String[] arrOrderIdxChk =  order.getOrderIdxChk().split(",");//삭제할 오더번호
//		String[] arrShopIdxChk =  order.getShopIdxChk().split(",");
		int arrShopIdxChk= sessionData.getShopIdx();//삭제할 샵 번호
		
    	int chk = 0;
    	int i = 0;
    	
    	for (String item : arrOrderIdxChk) {
    		OrderData orderData = new OrderData();
    		orderData.setOrderCode(item);
    		orderData.setShopIdx(arrShopIdxChk);

			chk = orderMapper.deleteOrder(orderData);
			
			i++;
		}
		
    	return chk;
    	
		//return orderMapper.deleteOrder(order);
	}
    
	/**
     * 주문 > 주문 존재 확인
     * @return
     */
	public int selectOneOrderCount(OrderData order){
		return orderMapper.selectOneOrderCount(order);
	}
	
	/**
     * 주문 > 주문상세 
     * @return
     */
	public OrderData selectOneOrder(OrderData order){
		return orderMapper.selectOneOrder(order);
	}
	
	public List<OrderData> selectOneOrderList(OrderData order){
		return orderMapper.selectOneOrderList(order);
	}
	
	/**
     * 주문 > 주문상세 > 입력 
     * @return
     */
    public int insertOrderDetail(OrderData order){
    	try {
    		return orderMapper.insertOrderDetail(order);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.debug(">>>>> 주문상세저장 Service [insertOrderDetail] ERR : " + order.toString());
			return 0;
		} 
    }
    
    /**
     * 주문 > 주문상세 > 수정 
     * @return
     */
    public int updateOrderDetail(OrderData order){
        try {
    		return orderMapper.updateOrderDetail(order);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.debug(">>>>> 주문상세저장 Service [updateOrderDetail] ERR : " + order.toString());
			return 0;
		}
    }
    
    public int mergeOrderDetail(OrderData order){
    	return orderMapper.mergeOrderDetail(order);
    }
    
    /**
     * 주문 > 주문상세 > 삭제 
     * @return
     */
    public int deleteOrderDetail(OrderData order){
        return orderMapper.deleteOrderDetail(order);
    }
    
    /**
     * 주문 > 주문상세 > 개수 
     * @return
     */
    public int selectOneOrderDetailCount(OrderData order){
        return orderMapper.selectOneOrderDetailCount(order);
    }
    
    /**
     * 주문 > 주문목록 
     * @return
     */
    public Map<String, Object> orderList(OrderData order, Model model, HttpSession sess) {

    	////////////////////////////////////////////////////////////////////////
    	////
    	////	검색조건 확인 및 가공
    	////
    	////////////////////////////////////////////////////////////////////////
    	
		boolean bSearchWordEncrypt = false ;
		String searchType = "" ;
		String searchWord = "" ;
		if (true) { // 검색조건별 파라미터 가공
			searchType = order.getSearchOrder() ; // 검색조건
			if (!(searchType != null && searchType.trim().length() > 0)) { searchType = "" ; }
			searchWord = order.getSearchWord() ; // 검색어
			if (!(searchWord != null && searchWord.trim().length() > 0)) { searchWord = "" ; }
			// 검색조건 및 검색어가 모두 유효한 값을 가졌는지 확인한다.
			if (searchType.trim().length() > 0 && searchType.trim().length() > 0) {
				// 검색조건이 암호화 대상 필드인 경우, 검색어를 일시적으로 암호화한다.
				// 암호화된 검색조건은 질의가 끝난 후 다시 입력 문자로 치환된다.
				if (searchType.equals("customerName")) {
					// TODO : '주문자 이름' 필드의 경우 쿼리에 보면 성 또는 이름, 전체가 같은지를 확인하도록 되어 있는데 대소문자가 틀린 경우는 어찌 할건지 고민해봐야 한다. 
					order.setSearchWord(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, searchWord)) ;
					bSearchWordEncrypt = true ;
				}
			}
		}
    	
    	////////////////////////////////////////////////////////////////////////
    	////
    	////	리스트 카운트 조회 및 페이징
    	////
    	////////////////////////////////////////////////////////////////////////

    	int dataCount = orderMapper.selectAllOrderCount(order); // 전체 건수 조회
    	int currentPage = order.getCurrentPage(); //현제 페이지
        int pageSize = order.getPageSize(); // 페이지 당 데이터 수
        int pageBlockSize = 10; // 페이지 블럭 수

        Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
        if (dataCount > 0) {
            paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
            order.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
            order.setRowPerPage(pageSize); 
            order.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
        }
    	
    	////////////////////////////////////////////////////////////////////////
    	////
    	////	리스트 목록 조회
    	////
    	////////////////////////////////////////////////////////////////////////

        List<OrderData> list = new ArrayList<OrderData>();
        if (dataCount > 0) {
            list = orderMapper.selectAllOrder(order);
            for (OrderData item : list) {
            	if (item.getBuyerFirstname() != null) {
            		try {
             		   	String buyerFirstname = util.nullToEmpty(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, item.getBuyerFirstname()));
        				item.setBuyerFirstname(buyerFirstname);
             	   	} catch (Exception e) {
     	   				e.getStackTrace();
     	   			}
            	}
            	if(item.getBuyerLastname() != null) {
            		try {
             		   	String buyerLastname = util.nullToEmpty(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, item.getBuyerLastname()));
        				item.setBuyerLastname(buyerLastname);
             	   	} catch (Exception e) {
     	   				e.getStackTrace();
     	   			}
            	}
        	}
        }

    	////////////////////////////////////////////////////////////////////////
        
		if (bSearchWordEncrypt) {
			// 암호화된 검색조건은 질의가 끝난 후 다시 입력 문자로 치환된다.
			order.setSearchWord(searchWord) ;
		}

    	////////////////////////////////////////////////////////////////////////

		Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        map.put("paging", paging);
        return map;
    }
    
    /**
     * 주문 > 배송 저장 
     * @param OrderData
     * @return OrderData order
     */
    public int updateShimentList(OrderData order){
    	
    	String[] arrOrderIdxChk =  order.getOrderIdxChk().split(",");
    	int chk = 0;
    	
    	for (String item : arrOrderIdxChk) {
    		// TODO : 배송데이터 정합성 처리
    		
    		OrderData orderData = new OrderData();
    		
    		LOGGER.debug("OrderCode : " + item);
    		
    		orderData.setOrderCode(item);
    		orderData.setHideYn("Y");
    		
    		LOGGER.debug("OrderCode : " + orderData.toString());
    		
			chk = orderMapper.updateOrderhide(orderData);
		}
    	
        return chk;
    }
    
    
	//paging
//	public Map<String, Object> orderList(Model model, HttpSession sess, Map<String,Object> mapParam){
//		OrderData order = new OrderData();
//		ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
//		// 페이지에 보여줄 행의 개수 ROW_PER_PAGE = 10으로 고정
//		int ROW_PER_PAGE = 10; 
//		int pageSize = Integer.parseInt(mapParam.get("currentPage").toString());
//		ROW_PER_PAGE = pageSize;
//		
//		// 페이지에 보여줄 첫번째 페이지 번호는 1로 초기화
//		int startPageNum = 1;
//		
//		// 처음 보여줄 마지막 페이지 번호는 10
//		int lastPageNum = ROW_PER_PAGE;
//		
//		int currentPage = Integer.parseInt(mapParam.get("currentPage").toString());
//		
//		// 현재 페이지가 ROW_PER_PAGE/2 보다 클 경우
//		if(currentPage > (ROW_PER_PAGE/2)) {
//			// 보여지는 페이지 첫번째 페이지 번호는 현재페이지 - ((마지막 페이지 번호/2) -1 )
//			// ex 현재 페이지가 6이라면 첫번째 페이지번호는 2
//			startPageNum = currentPage - ((lastPageNum/2)-1);
//			// 보여지는 마지막 페이지 번호는 현재 페이지 번호 + 현재 페이지 번호 - 1 
//			lastPageNum += (startPageNum-1);
//		}
//		
//		// Map Data Type 객체 참조 변수 map 선언
//		// HashMap() 생성자 메서드로 새로운 객체를 생성, 생성된 객체의 주소값을 객체 참조 변수에 할당
//		Map<String, Object> map = new HashMap<String, Object>();
//		// 한 페이지에 보여지는 첫번째 행은 (현재페이지 - 1) * 10
//		int startRow = (currentPage - 1)*ROW_PER_PAGE;
//		// 값을 map에 던져줌
//		order.setStartRow(startRow);
//		order.setRowPerPage(ROW_PER_PAGE);
//		order.setEncryptKey(SpConstants.ENCRYPT_KEY);
//		order.setShopIdx(sessionData.getShopIdx());
//		order.setSearchDateStart(mapParam.get("searchDateStart").toString());
//		order.setSearchDateEnd(mapParam.get("searchDateEnd").toString());
//		order.setCurrentPage(Integer.parseInt(mapParam.get("currentPage").toString()));
//		order.setSearchType(mapParam.get("searchType").toString());
//		order.setSearchWord(mapParam.get("searchWord").toString());
//		// DB 행의 총 개수를 구하는 getBoardAllCount() 메서드를 호출하여 double Date Type의 boardCount 변수에 대입
//		double orderCount = orderMapper.selectAllOrderCount(order);
//
//		// 마지막 페이지번호를 구하기 위해 총 개수 / 페이지당 보여지는 행의 개수 -> 올림 처리 -> lastPage 변수에 대입
//		int lastPage = (int)(Math.ceil(orderCount/ROW_PER_PAGE));
//		// 현재 페이지가 (마지막 페이지-4) 보다 같거나 클 경우
//		if(currentPage >= (lastPage-4)) {
//			// 마지막 페이지 번호는 lastPage
//			lastPageNum = lastPage;
//		}
//		//LOGGER.debug("###############1aaaaaaaaaaaa444=order=>/"+order);
//		// 구성한 값들을 Map Date Type의 resultMap 객체 참조 변수에 던져주고 return
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		List<OrderData> orderList = orderMapper.selectAllOrder(order);
//		//LOGGER.debug("###############1aaaaaaaaaaaa=0orderList=>/"+orderList);
//		List<ShopData> shopList = shopMapper.selectAllShop(sessionData);
//		//LOGGER.debug("###############1aaaaaaaaaaaa=orderList=>/"+shopList);
//		
//		//페이징처리
//		List<Map<String,Object>> pageList  = new ArrayList<Map<String,Object>>();
//		
//		for(int i=startPageNum; i<lastPageNum;i++) {
//		    Map<String, Object> pageMap = new HashMap<String, Object>();
//			pageMap.put("page", i);
//			pageList.add(pageMap);
//		}
//		
//		LOGGER.debug("###############1aaaaaaaaaaaa=orderList=>/"+pageList);
//		resultMap.put("list", orderList);
//		resultMap.put("slist", shopList);
//		resultMap.put("pageList", pageList);
//		resultMap.put("pageSize", orderCount);
//		resultMap.put("currentPage", currentPage);
//		resultMap.put("lastPage", lastPage);
//		resultMap.put("startPageNum", startPageNum);
//		resultMap.put("lastPageNum", lastPageNum);
//		return resultMap;
//	}

}
