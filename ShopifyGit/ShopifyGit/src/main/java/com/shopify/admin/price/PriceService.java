package com.shopify.admin.price;

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
import com.shopify.admin.delivery.DeliveryData;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.mapper.PriceMapper;
import com.shopify.shop.ShopData;

/**
 * Price 서비스(할인율 맵핑관리)
 *
 */

@Service
@Transactional
public class PriceService {
	private Logger LOGGER = LoggerFactory.getLogger(PriceService.class);
	
	@Autowired private UtilFn util;
	@Autowired private PriceMapper priceMapper;
	
	/**
	 * 특송 요금 관리 > 요금맵핑관리 VIew 페이지
	 * (공시요금)
	 * @return
	 */
    public Map<String, Object> feesPriceMappingList(PriceData priceData, HttpSession sess){
    	
//    	AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	String zoneCodeGroup = priceData.getZoneCodeGroup();
    	String zoneCodeId = priceData.getZoneCodeId();
    	String nowDate = priceData.getNowDate();
    	priceData.setLocale(sd.getLocale());
    	
    	// 검색일자 생성 
    	if(nowDate == null || "".equals(nowDate)) {
    		nowDate = util.getDateElement("today"); 
    		priceData.setNowDate(nowDate);
    	}
    	
    	//최초 화면 접근시 배송서비스 EMS로 세팅
    	if(zoneCodeGroup == null) {
    		zoneCodeGroup = "";
    	}
    	if("".equals(zoneCodeGroup)) {
    		priceData.setZoneCodeGroup("EMS");
    		zoneCodeGroup = "EMS";
    	}
    	//최초 화면  접근시 배송사 롯데로 세팅
    	if(zoneCodeId == null) {
    		zoneCodeId = "";
    	}
    	if("".equals(zoneCodeId)) {
    		priceData.setZoneCodeId("B010020");
    		zoneCodeId = "B010020";
    	}
    	
    	Map<String,Object> returnMap = new HashMap<String, Object>();
    	
    	/*
    	 * 1.특송사별 지역 select TB_USE_CODE */
    	
    	List<PriceData> headerList = new ArrayList<PriceData>();
    	
    	headerList = priceMapper.headerList(priceData);
    	
    	LOGGER.debug("headerList>>>>>>>>>>>>>>>>"+headerList);
    	
    	/*
    	 * 2.특송사별 무게 select
    	 * */
    	
    	List<PriceData> weightList = new ArrayList<PriceData>();

    	weightList = priceMapper.feesWeightList(priceData);
    	
    	LOGGER.debug("weightList>>>>>>>>>>>>>>>>>>>>>>>"+weightList);
    	
    	Map<String,Object> dataList = new HashMap<String, Object>();
    	
    	Map<String,Object> tmpMap = new HashMap<String, Object>();
    	
    	/*
    	 * 3.무게별 지역 data select (무게.지역.금액)
    	 * for1에서 
    	 * */
    	for (PriceData item : weightList) {
    		Map<String,Object> subDataList = new HashMap<String, Object>();
    		String key = item.getWeightCode();
    		String CodeGroup = item.getZoneCodeGroup();
    		tmpMap.put("weightCode", key);
    		tmpMap.put("zoneCodeGroup", CodeGroup);
    		tmpMap.put("service",zoneCodeGroup);
    		tmpMap.put("nowDate",nowDate);
    		
    		
    		List<PriceViewData> tempList = priceMapper.feesPriceMappingList(tmpMap);
    		
    		for (PriceViewData subItem : tempList) {
    			
    			String zone = subItem.getZone();
    			String price = subItem.getPrice();
    			subDataList.put(zone, price);
    		}

    		dataList.put(key, subDataList);
    		
    	}
    	
    	returnMap.put("headerList", headerList);
    	returnMap.put("dataList", dataList);
    	returnMap.put("weightList", weightList); 
    	returnMap.put("partShipCompany", zoneCodeId);
    	returnMap.put("partDeliveryService", zoneCodeGroup);
    	returnMap.put("nowDate", nowDate);
    	
    	LOGGER.debug("returnMap>>>>>>>>>>>>>>>>>>>>>>>>"+returnMap);
    	return returnMap;
    }
    
    /**
	 * 특송 요금 관리 > 요금맵핑관리 VIew 페이지
	 * (할인요금)
	 * @return
	 */
    public Map<String, Object> salePriceMappingList(PriceData priceData, HttpSession sess){
    	
//    	AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	String zoneCodeGroup = priceData.getZoneCodeGroup();
    	String zoneCodeId = priceData.getZoneCodeId();
    	String nowDate = priceData.getNowDate();
    	priceData.setLocale(sd.getLocale());
    	
//    	String codeGroup = priceMapper.selectCodeGroup(zoneCodeGroup);
    	
//    	priceData.setCodeGroup(codeGroup);
    	
    	
    	
    	//최초 화면 접근시 배송서비스 EMS로 세팅
    	if(zoneCodeGroup == null) {
    		zoneCodeGroup = "";
    	}
    	if("".equals(zoneCodeGroup)) {
    		priceData.setZoneCodeGroup("EMS");
    		zoneCodeGroup = "EMS";
    	}
    	//최초 화면  접근시 배송사 롯데로 세팅
    	if(zoneCodeId == null) {
    		zoneCodeId = "";
    	}
    	if("".equals(zoneCodeId)) {
    		priceData.setZoneCodeId("B010020");
    		zoneCodeId = "B010020";
    	}
    	
    	// 검색일자 생성 
    	if(nowDate == null || "".equals(nowDate)) {
    		nowDate = util.getDateElement("today"); 
    		priceData.setNowDate(nowDate);
    	}
    	
    	Map<String,Object> returnMap = new HashMap<String, Object>();
    	
    	/*
    	 * 1.특송사별 지역 select TB_USE_CODE */
    	
    	List<PriceData> headerList = new ArrayList<PriceData>();
    	
    	headerList = priceMapper.headerList(priceData);
    	
    	LOGGER.debug("headerList>>>>>>>>>>>>>>>>"+headerList);
    	
    	/*
    	 * 2.특송사별 무게 select
    	 * */
    	
    	List<PriceData> weightList = new ArrayList<PriceData>();

    	weightList = priceMapper.saleWeightList(priceData);
    	
    	LOGGER.debug("weightList>>>>>>>>>>>>>>>>>>>>>>>"+weightList);
    	
    	Map<String,Object> dataList = new HashMap<String, Object>();
    	
    	Map<String,Object> tmpMap = new HashMap<String, Object>();
    	
    	/*
    	 * 3.무게별 지역 data select (무게.지역.금액)
    	 *  
    	 * */
    	for (PriceData item : weightList) {
    		Map<String,Object> subDataList = new HashMap<String, Object>();
    		String key = item.getWeightCode();
    		String CodeGroup = item.getZoneCodeGroup();
    		tmpMap.put("weightCode", key);
    		tmpMap.put("zoneCodeGroup", CodeGroup);
    		tmpMap.put("service",zoneCodeGroup);  
    		tmpMap.put("nowDate",nowDate);
    		
    		List<PriceViewData> tempList = priceMapper.salePriceMappingList(tmpMap);
    		
    		for (PriceViewData subItem : tempList) {
    			
    			String zone = subItem.getZone();
    			String price = subItem.getPrice();
    			subDataList.put(zone, price);
    		}

    		dataList.put(key, subDataList);
    		
    	}
    	
    	returnMap.put("headerList", headerList);
    	returnMap.put("dataList", dataList);
    	returnMap.put("weightList", weightList);
    	returnMap.put("partShipCompany", zoneCodeId);
    	returnMap.put("partDeliveryService", zoneCodeGroup);
    	returnMap.put("nowDate", nowDate);
    	
    	return returnMap;
    }
    
    /**
	 * 관리자 > 요금매핑 > 배송서비스 리스트 
	 * @param DeliveryData
	 * @return List<DeliveryData>
	 */
    public List<PriceData> selectShipServiceList(PriceData priceData) {
    	return priceMapper.selectShipServiceList(priceData);
    }
    /**
     *  관리자>요금매핑>요금 조회 : 조한두
     */
    public PriceData selectPrice(PriceData priceData) {
    	return priceMapper.selectPrice(priceData);
    }
    
    
    
}