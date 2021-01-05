package com.shopify.shipment.popup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.api.LotteLogisApiService;
import com.shopify.api.ShopifyApiController;
import com.shopify.api.ShopifyApiService;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.mapper.OrderMapper;
import com.shopify.mapper.ShipmentMapper;
import com.shopify.mapper.ShopMapper;
import com.shopify.order.OrderData;
import com.shopify.payinfo.PayinfoData;
import com.shopify.payinfo.PayinfoService;
import com.shopify.payment.PaymentService;
import com.shopify.payment.popup.PaymentPopupService;
import com.shopify.setting.SettingShopData;
import com.shopify.shipment.ShipmentData;
import com.shopify.shipment.ShipmentService;
import com.shopify.shipment.popup.ShipmentPopupData;
import com.shopify.shipment.popup.ShipmentPopupDataLocalData;
import com.shopify.shop.ShopData;
import com.shopify.order.popup.OrderPopupService;

/**
 * 배송 팝업 서비스
 *
 */
@Service
@Transactional
public class ShipmentPopupService {
	private Logger LOGGER = LoggerFactory.getLogger(ShipmentPopupService.class);
	@Autowired private ShipmentMapper shipmentMapper;
	@Autowired private ShopifyApiController sApiController;
	@Autowired private ShopifyApiService sApiService;
	@Autowired private ShipmentService shipmentService;
	@Autowired private PaymentPopupService paymentPopupService;
    @Autowired private UtilFn util;
    @Autowired private LotteLogisApiService lotteLogisApiService;
    @Autowired private PayinfoService payinfoService;
    @Autowired private OrderPopupService orderPopupService;

	public List<ShipmentPopupData> selectPopShipmentSkuList(ShipmentPopupData shipPopData, HttpSession sess) {
	    return shipmentMapper.selectPopShipmentSkuList(shipPopData);
	}

    public int selectPopShipmentSkuCount(ShipmentPopupData shipPopData) {
        return shipmentMapper.selectPopShipmentSkuCount(shipPopData);
    }

	public List<ShipmentPopupData> selectPopShipmentList(ShipmentPopupData shipPopData, HttpSession sess) {
	    return shipmentMapper.selectPopShipmentList(shipPopData);
	}
	
	public List<ShipmentPopupData> selectPopShipmentCombineList(ShipmentPopupData shipPopData, HttpSession sess) {
	    return shipmentMapper.selectPopShipmentCombineList(shipPopData);
	}

    public int selectPopShipmentCount(ShipmentPopupData shipPopData) {
        return shipmentMapper.selectPopShipmentCount(shipPopData);
    }
    
    public int selectPopShipmentCombineCount(ShipmentPopupData shipPopData) {
        return shipmentMapper.selectPopShipmentCombineCount(shipPopData);
    }
    
    
    public List<ShipmentPopupData> selectPopShipmentDeliveryList(ShipmentPopupData shipPopData, HttpSession sess) {
        return shipmentMapper.selectPopShipmentDeliveryList(shipPopData);
    }

    public List<ShipmentPopupData> selectPopShipmentDeliveryArrayList(ShipmentPopupData shipPopData, HttpSession sess) {
        return shipmentMapper.selectPopShipmentDeliveryArrayList(shipPopData);
    }
    
    /**
     * 배송 > 로컬정보
     * @return List<LocalDeliveryPaymentData>
     */
    public List<LocalDeliveryPaymentData> selectLocalDeliveryPaymentList(LocalDeliveryPaymentData payment, HttpSession sess) {
        return shipmentMapper.selectLocalDeliveryPayment(payment);
    }
    
    /**
     * 배송 > 결제처리 > 배송정보
     * @return int
     */
    public int updatePopShipmentPaymentDelivery(ShipmentPopupData ship){
        return shipmentMapper.updatePopShipmentPaymentDelivery(ship);
    }
    /**
     * 배송 > 결제처리 > 결제정보
     * @return int
     */
    public int updatePopShipmentPaymentDeliveryPayment(ShipmentPopupData ship){
        return shipmentMapper.updatePopShipmentPaymentDeliveryPayment(ship);
    }
    
    /**
     * 배송 > 결제처리 > 로컬 결제정보
     * @return int
     */
    public int insertPopShipmentPaymentDeliveryPayment(ShipmentPopupData ship){
        return shipmentMapper.insertPopShipmentPaymentDeliveryPayment(ship);
    }
    /**
     * 배송 > 결제처리 > 로컬처리 
     * @return int
     */
    public int insertPopShipmentPaymentDeliveryLocal(ShipmentPopupData ship){
        return shipmentMapper.insertPopShipmentPaymentDeliveryLocal(ship);
    }
    public int insertPopShipmentPaymentDeliveryLocalData(ShipmentPopupData ship){
        return shipmentMapper.insertPopShipmentPaymentDeliveryLocalData(ship);
    }
    
    /**
     * 배송 > 결제처리  > 배송비계산
     * @return ShipmentPopupData
     */
    public ShipmentPopupData selectShipmentDelivery(ShipmentPopupData ship){
        return shipmentMapper.selectShipmentDelivery(ship);
    }
    
    /**
     * 배송 > 결제처리  > 결제후 저장
     * @return ShipmentPopupData
     */
    public int insertShipmentPayInfo(ShipmentPopupData ship){
        return shipmentMapper.insertShipmentPayInfo(ship);
    }
    
    /**
     * 배송 > 결제처리  > API > 이행이벤트처리
     * @return
     */
    public Map<String,Object> shopifyPostEventOrderId(ShipmentPopupData ship, HttpServletRequest req, HttpSession sess){
        return sApiController.shopifyPostEventOrderId(ship, req, sess);
    }
    /**
     * 배송 > 결제처리  > API > 이행목록 조회
     * @return
     */
    public ShipmentPopupData shopifyGetOrderIdFullist(ShipmentPopupData ship, HttpServletRequest req, HttpSession sess){
        return sApiController.shopifyGetOrderIdFullist(ship, req, sess);
    }
    /**
     * 배송 > 결제처리  > API > 이행등록
     * @return
     */
    public Map<String,Object> shopifyPostOrderIdFullist(ShipmentPopupData ship, HttpServletRequest req, HttpSession sess){
        return sApiController.shopifyPostOrderIdFullist(ship, req, sess);
    }
    
    /**
     * 배송 > 결제처리  > API > 빌링
     * @return
     */
    public ShipmentPopupData shopifyProcBilling(ShipmentPopupData ship, HttpServletRequest req, HttpSession sess){
        return sApiController.shopifyProcBilling(ship, req, sess);
    }

    public int selectShipmentPrice(ShipmentPopupData shipPopData) {
        return shipmentMapper.selectShipmentPrice(shipPopData);
    }
    
    public int updatePopShipmentStat(ShipmentPopupData shipPopData, HttpSession sess) {
    	
    	ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
    	
    	ShipmentPopupData ship = new ShipmentPopupData();
    	
    	String stateGroup = shipPopData.getStateGroup();
		String state = shipPopData.getStateGroup().substring(0, 5) + "10"; // 초기값 세팅
		String changeReason = shipPopData.getChangeReason();
		
		ship.setStateCode(state);
		ship.setStateGroupCode(stateGroup);
		ship.setEmail(email);
    	ship.setChangeReason(changeReason);
		/*
    	}
    	*/
 
    	
    	LOGGER.debug("updatePopShipmentStat[stateGroup] : " + ship.getStateGroupCode());
		LOGGER.debug("updatePopShipmentStat[state] : " + ship.getStateCode());
    	
    	String[] masterCodeList = shipPopData.getMasterCode().split(",");
    	ship.setMasterCodeList(masterCodeList);
    	
    	int result = shipmentMapper.insertChangeReason(ship);
    	LOGGER.debug("result of insertChangeReason : " + result);
    	
        return shipmentMapper.updatePopShipmentStat(ship);
    }
    

	/**
     * 배송 > 배송비 결제 api 저장 
     * @return
     */
	public int insertShipmentPaymentApi(ShipmentPopupData shipmentPopupData) {
	   return shipmentMapper.insertShipmentPaymentApi(shipmentPopupData);
	}
    
    /**
     * 배송 > 배송비 결제 api 수정 
     * @return
     */
    public int updateShipmentPaymentApi(ShipmentPopupData shipmentPopupData){
        return shipmentMapper.updateShipmentPaymentApi(shipmentPopupData);
    }
    
    
    public ModelAndView popPaymentShipmentProc(List<ShipmentPopupData> shipPopDataList, Model model, HttpServletRequest req, HttpSession sess) {
    	 ModelAndView mav = new ModelAndView("jsonView");
    	
    	ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
   	 
        int chk = 1;
        Map<String,Object> apiMapEvent;
        
   	 
        for(ShipmentPopupData shipPopData : shipPopDataList) {
        	
        	if(shipPopData.getMasterCode() == null || "".equals(shipPopData.getMasterCode()) )  continue;
        	
        	String masterCode = shipPopData.getMasterCode();
        	
        	ShipmentData ship = new ShipmentData();
        	ship.setLocale(sessionData.getLocale());
        	ship.setEmail(sessionData.getEmail());
        	
        	ship.setMasterCode(masterCode);
        	
            ShipmentData sData = shipmentService.selectShipmentDetail(ship);
            shipPopData.setOrderCode(sData.getOrderCode());
            //shipPopData.setPayment(sData.getPayment());
            //shipPopData.setPaymentTotal(sData.getPaymentTotal());
            shipPopData.setGoods(sData.getGoods());
            shipPopData.setGoodsCode(sData.getGoodsCode());
            shipPopData.setFulId(sData.getFulId());

            String[] masterCodeList = {masterCode};
            shipPopData.setMasterCodeList(masterCodeList); 		//확인 사항

            Map<String, Object> shipMap = lotteLogisApiService.requestLotteLogisDelivery(shipPopData, sessionData.getEmail());
//        	int date = shipmentMapper.updateShipmentData(shipPopData);
            
        	if(shipMap.get("result") == null ) {
        		  model.addAttribute("result", "chk" );
        	        model.addAttribute("errCode", false);
        	        model.addAttribute("errMsg", "API Error");
        	        mav.addObject("errCode",false);
        	}        	
//        	Map<String, Object> trackMap = lotteLogisApiService.requestLotteTracking(shipPopData);
        }
      ///orderCode 하나 당 post 처리
        Set<String> set = shipPopDataList.stream().map( d -> d.getMasterCode()).collect(Collectors.toSet());
        System.out.println(set); 
        /*Set<String> set1 = new HashSet<>(); for(
		 * ShipmentPopupData d : shipPopDataList ) { set1.add( d.getMasterCode()); }
		 * System.out.println(set1);
		 */
              for(String masterCode: set) {    	  
	      OrderData ordercode = new OrderData();
	      ordercode.setParentCode(masterCode);
	      int ChildOrder= orderPopupService.selectDeliveryCombineCount(ordercode);
	      if(ChildOrder > 0) {
	    	  List<OrderData> ChildOrderCode = orderPopupService.selectDeliveryCombine(masterCode);
	    	  for(int i=0; i<ChildOrder; i++) {
	    		  ShipmentPopupData shipPopData = shipPopDataList.get(i);
	    		  shipPopData.setOrderCode(ChildOrderCode.get(i).getChildCode());
	    		  apiMapEvent = shopifyPostOrderIdFullist(shipPopData,req, sess);   		  
	    	  }
	      }
	      else {
	    	  for(ShipmentPopupData shipPopData: shipPopDataList)
	    	  {
	    		  if(shipPopData.getMasterCode()==masterCode)
	    			  apiMapEvent = shopifyPostOrderIdFullist(shipPopData,req, sess);
	    	  }	    	  
	      	}
      }
       
        
        model.addAttribute("result", chk);
	    model.addAttribute("errCode", true);
	    model.addAttribute("errMsg", "성공");
	    
	    mav.addObject("errCode",true);
	    

        return mav;
    }
    
    /*
     *  관리자 페이지 : 배송시작 (조한두)
     */
    
    public ModelAndView popPaymentShipmentProcAdmin(List<ShipmentPopupData> shipPopDataList, Model model, HttpServletRequest req, HttpSession sess) {
   	 ModelAndView mav = new ModelAndView("jsonView");
   	 
     ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
     
     int chk = 1;
     Map<String,Object> apiMapEvent;
   	 
       for(ShipmentPopupData shipPopData : shipPopDataList) 
       {
    	   LOGGER.debug(">>>>>>>>>>>>>>>>>> ADMIN >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");	
		       LOGGER.debug("###############popPaymentShipmentProc=req=>/"+req.getParameter("masterCode"));
		       //LOGGER.debug("###############popPaymentShipmentProc=shipPopData=>/"+shipPopData);
		       LOGGER.debug("###############1111popPaymentShipmentProc=shipPopData=>/"+shipPopData);
		       
		       if(shipPopData.getMasterCode() == null || "".equals(shipPopData.getMasterCode()) )  continue;
		       
		       String masterCode = shipPopData.getMasterCode();
		       
		       ShipmentData ship = new ShipmentData();
		       ship.setLocale("ko");
		       ship.setEmail(shipPopData.getEmail());
		       ship.setMasterCode(masterCode);
		       
		       ShipmentData sData = shipmentService.selectShipmentDetail(ship);
		       shipPopData.setOrderCode(sData.getOrderCode());
		       shipPopData.setGoods(sData.getGoods());
		       shipPopData.setGoodsCode(sData.getGoodsCode());
		       shipPopData.setFulId(sData.getFulId());
		       
		       String[] masterCodeList = {masterCode};
		       shipPopData.setMasterCodeList(masterCodeList);
		       
		       Map<String, Object> shipMap = lotteLogisApiService.requestLotteLogisDelivery(shipPopData, sessionData.getEmail());
//		       int date = shipmentMapper.updateShipmentData(shipPopData);

		       if(shipMap.get("result") == null) {
		    	   model.addAttribute("result", "chk" );
	      	        model.addAttribute("errCode", false);
	      	        model.addAttribute("errMsg", "API Error");
	      	        mav.addObject("errCode",false);
		       }
       }
		
       
     ///orderCode 하나 당 post 처리
       Set<String> set = shipPopDataList.stream().map( d -> d.getMasterCode()).collect(Collectors.toSet());
       System.out.println(set); 
       /*Set<String> set1 = new HashSet<>(); for(
		 * ShipmentPopupData d : shipPopDataList ) { set1.add( d.getMasterCode()); }
		 * System.out.println(set1);
		 */
       
       for(String masterCode: set) {    	  
 	      OrderData ordercode = new OrderData();
 	      ordercode.setParentCode(masterCode);
 	      int ChildOrder= orderPopupService.selectDeliveryCombineCount(ordercode);
 	      if(ChildOrder > 0) {
 	    	  List<OrderData> ChildOrderCode = orderPopupService.selectDeliveryCombine(masterCode);
 	    	  for(int i=0; i<ChildOrder; i++) {
 	    		  ShipmentPopupData shipPopData = shipPopDataList.get(i);
 	    		  shipPopData.setOrderCode(ChildOrderCode.get(i).getChildCode());
 	    		 apiMapEvent = shopifyPostOrderIdFullist(shipPopData,req, sess);   		  
 	    	  }
 	      }
 	      else {
 	    	  for(ShipmentPopupData shipPopData: shipPopDataList)
 	    	  {
 	    		  if(shipPopData.getMasterCode()==masterCode)
 	    			  apiMapEvent = shopifyPostOrderIdFullist(shipPopData,req, sess);
 	    	  }
 	      	}
       }
       
		
       model.addAttribute("result", chk);
       model.addAttribute("errCode", true);
       model.addAttribute("errMsg", "성공");
       

       return mav;
   }
    
    /**
     * 배송 > 팝업 > 결제 상태만 변경
     * @return int
     */
    private int popPaymentShipmentProcess(ShipmentPopupData shipPopData, ShipmentData sData, int chk, ShipmentPopupData payData) {
	    shipPopData.setState("A020020");
	    chk = updatePopShipmentPaymentDelivery(shipPopData);
	    return chk;
    }
    
    /**
     * 결제저장 > 내부 메소드
     * @return int
     */
    private int popPaymentPayinfotProcess(PayinfoData payinfoData, ShipmentData sData, int chk) {
	    String deliveryCompany = sData.getDeliveryCompany();
		/*if(deliveryCompany != null) {
			shipPopData.setPaymentVat(300);	//국내택배시 vat 10%...	
		}*/
	    //결제 반영
	    chk = payinfoService.insertPayinfo(payinfoData);
	    return chk;
    }
    
    public Map getDeliveryByCompanys(List<ShipmentPopupData> shipPopDataList) {
		// TODO Auto-generated method stub
		
		List<Map> masterCodes = shipmentMapper.selectDeliveryByCompanys(shipPopDataList);
		if(masterCodes == null ) return null;

		Map masterCodeMap =new HashMap();
		
		for (Map map : masterCodes) {
			
			if(map.isEmpty()) continue;
			
			String courierCompany = (String) map.get("courierCompany");
		    if(masterCodeMap.get(courierCompany) == null) {
		    	
		    	List list =new ArrayList();
		    	ShipmentPopupData deliveryData = new ShipmentPopupData();
		    	deliveryData.setMasterCode((String)map.get("masterCode"));
		    	deliveryData.setShopId((String)map.get("shopId"));
		    	deliveryData.setEmail((String)map.get("email"));
		    	deliveryData.setBuyerCountryCode((String)map.get("buyerCountryCode"));
		    	list.add(deliveryData);
		    	masterCodeMap.put(courierCompany, list);
		    	
		    } else {
		    	List list = (List) masterCodeMap.get(courierCompany);
		    	ShipmentPopupData deliveryData = new ShipmentPopupData();
		    	deliveryData.setMasterCode((String)map.get("masterCode"));
		    	deliveryData.setShopId((String)map.get("shopId"));
		    	deliveryData.setEmail((String)map.get("email"));
		    	deliveryData.setBuyerCountryCode((String)map.get("buyerCountryCode"));
		    	list.add(deliveryData);
		    }
		}
		
		return masterCodeMap;
	}
    
    
    
}
