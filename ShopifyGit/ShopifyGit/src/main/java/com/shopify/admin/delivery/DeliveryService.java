package com.shopify.admin.delivery;

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

import com.shopify.admin.AdminData;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.mapper.AdminDeliveryMapper;
 
@Service
@Transactional

/**
 * 배송사 서비스
 *
 */

public class DeliveryService {
 
    @Autowired private AdminDeliveryMapper deliveryMapper;
    @Autowired private UtilFn util;
    
    private Logger LOGGER = LoggerFactory.getLogger(DeliveryService.class);
        
    //배송사조회
    public Map<String, Object> listShipCompany(DeliveryData deliveryData, HttpSession sess){
    	
    	AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	
    	int dataCount = deliveryMapper.selectCompanyCount(deliveryData); // 전체 건수 조회
		int currentPage = deliveryData.getCurrentPage(); //현제 페이지
		int pageSize = deliveryData.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		List<DeliveryData> list = new ArrayList<DeliveryData>(); //데이터 리스트 변수 선언 
		
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			
			deliveryData.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			deliveryData.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			
			list = deliveryMapper.listShipCompany(deliveryData);
		}
		

		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("paging", paging);
		
		return map;
    }
    
    /**
	 * 관리자 > 배송사 관리 > 상세보기
	 * @param DeliveryData
	 * @return List<DeliveryData>
	 */
    public DeliveryData selectDeliveryCompanyServiceView(DeliveryData deliveryData) {
    	DeliveryData service = deliveryMapper.selectShipCompanyView(deliveryData);
    	return service;
    }
    
    /**
	 * 관리자 > 배송사 관리 >배송 서비스 리스트
	 * @param DeliveryData
	 * @return List<DeliveryData>
	 */
    public List<DeliveryData> selectDeliveryCompanyService(DeliveryData deliveryData) {
    	List<DeliveryData> service = deliveryMapper.selectDeliveryCompanyService(deliveryData);
    	return service;
    }
    
    
    /**
	 * 관리자 > 배송사 관리 >배송사 리스트
	 * @param DeliveryData
	 * @return List<DeliveryData>
	 */
    public List<DeliveryData> selectShipCompany(DeliveryData deliveryData) {
    	List<DeliveryData> company = deliveryMapper.selectShipCompany(deliveryData);
    	return company;
    }
    
    /**
	 * 관리자 > 배송사 관리 > 배송비 리스트
	 * @param DeliveryData
	 * @return List<DeliveryData>
	 */
    public List<DeliveryData> selectShipService(DeliveryData deliveryData) {
    	List<DeliveryData> service = deliveryMapper.selectShipService(deliveryData);
    	return service;
    }
    
    /**
	 * 관리자 > 배송사 관리 >배송비 등록
	 * @param DeliveryData
	 * @return List<DeliveryData>
	 */
    public int insertShipCompany(DeliveryData deliveryData ,HttpSession sess) {
    	AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	
    	deliveryData.setCodeId(deliveryMapper.selectCodeId(deliveryData));
    	
    	int chk = deliveryMapper.chkShipCompany(deliveryData);
    	
    	if(chk > 0) {
    		return -1;
    	}
    	
    	return deliveryMapper.insertShipCompany(deliveryData);

    }
    
    /**
	 * 관리자 > 배송사 관리 > 배송비 사용 여부 
	 * @param DeliveryData
	 * @return int
	 */
	public int updateShipServiceUseYn(DeliveryData deliveryData) {
		return deliveryMapper.updateShipCompany(deliveryData);
	}
	
	/**
	 * 관리자 > 배송사 관리 > 배송비 수정
	 * @param DeliveryData
	 * @return int
	 */
	public int updateShipCompany(DeliveryData deliveryData) {
		return deliveryMapper.updateShipCompany(deliveryData);
	}
	
    /**
	 * 관리자 > 배송사 관리 >배송비 삭제
	 * @param DeliveryData
	 * @return List<DeliveryData>
	 */
	public int deleteShipCompany(DeliveryData deliveryData) {
		String[] delItem = deliveryData.getCkBox().split(",");
		int result = 0;
		
		for (String item : delItem){
			
			LOGGER.debug("item : " + item.toString());
			
			if(!item.equals("")) {
				String[] shipData = item.split("@");
				
				//LOGGER.debug("setShipId : " + shipData[0]);
				//LOGGER.debug("setCode : " + shipData[1]);
				
				deliveryData.setShipId(shipData[0]);
				deliveryData.setCode(shipData[1]);
				
				deliveryMapper.deleteShipCompany(deliveryData);
				result ++;
			}
		}
		
		return result;
	}
}
