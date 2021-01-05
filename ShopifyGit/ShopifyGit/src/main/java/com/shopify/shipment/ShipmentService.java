package com.shopify.shipment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopify.api.ShopifyApiService;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.mapper.PaymentMapper;
import com.shopify.mapper.ShipmentMapper;
import com.shopify.shipment.popup.ShipmentPopupData;
import com.shopify.shop.ShopData;

@Service
@Transactional
public class ShipmentService {
	private Logger LOGGER = LoggerFactory.getLogger(ShipmentService.class);
	@Autowired 
	private ShipmentMapper shipmentMapper;
	@Autowired 
	private PaymentMapper payMapper;
	@Autowired 
	private ShopifyApiService apiService;
	
	@Autowired	
	private UtilFn util;
	
	@Value("${lotteLogis.acper.name}") 		String lotteAcperNm;
	@Value("${lotteLogis.acper.tel}") 		String lotteAcperTel;
	@Value("${lotteLogis.acper.cpno}") 		String lotteAcperCpno;
	@Value("${lotteLogis.acper.zipcode}") 	String lotteAcperZipcd;
	@Value("${lotteLogis.acper.addr}") 		String lotteAcperAddr;
	
	/**
     * 배송 > 배송생성 
     * @return
     */
	public int insertShipment(ShipmentData shipmentData,HttpSession sess) {
        ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
        //System.out.println("###############1aaaaaaaaaaaa1=util.sessionData=>/"+sessionData);
        //배송코드 임의생성(D+오늘날짜+임의수4자리)
        //LOGGER.debug("###############1aaaaaaaaaaaa=util.getDateElement(\"full\")=>/"+util.getDateElement("full"));
        //System.out.println("###############1aaaaaaaaaaaa1=util.getDateElement(\\\"full\\\")=>/"+util.getDateElement("full"));
        String keyFull = util.getDateElement("full");
        String masterCode = keyFull.substring(2,keyFull.length()-1)+util.getRandomString(4).toUpperCase();
        
        //System.out.println("###############1aaaaaaaaaaaa2=util.sessionData=>/"+sessionData);
        String phone = shipmentData.getSellerPhone();
        if(phone == null) phone = " ";
        String sellerName = sessionData.getShopName();
        if(sellerName == null) sellerName = sessionData.getEmail();
        String buyerFirstName = shipmentData.getBuyerFirstname();
        if(buyerFirstName == null) buyerFirstName = " ";
        String buyerLastName = shipmentData.getBuyerLastname();
        if(buyerLastName == null) buyerLastName = " ";
        String buyerPhone = shipmentData.getBuyerPhone();
        if(buyerPhone == null) buyerPhone = " ";
        //System.out.println("###############1aaaaaaaaaaaa3=util.sessionData=>/"+sessionData);
        //System.out.println("###############1aaaaaaaaaaaa4=phone/"+phone);
        shipmentData.setMasterCode(masterCode);
//        shipmentData.setOrderIdx(shipmentData.getOrderIdx());
//        shipmentData.setMasterCode(shipmentData.getMasterCode());
//        shipmentData.setOrderCode(shipmentData.getOrderCode());
//        shipmentData.setOrderDate(shipmentData.getOrderDate());
//        shipmentData.setShopIdx(shipmentData.getShopIdx());
        shipmentData.setSellerPhone(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, phone));
        shipmentData.setSellerName(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, sellerName));
        //System.out.println("###############1aaaaaaaaaaaa5=util.sessionData=>/"+sessionData);
//        shipmentData.setSellerCountryCode(shipmentData.getSellerCountryCode());
//        shipmentData.setSellerCity(shipmentData.getSellerCity());
//        shipmentData.setSellerZipCode(shipmentData.getSellerZipCode());
//        shipmentData.setSellerAddr1(shipmentData.getSellerAddr1());
//        shipmentData.setSellerAddr2(shipmentData.getSellerAddr2());
        shipmentData.setBuyerFirstname(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, buyerFirstName));
        //System.out.println("###############1aaaaaaaaaaaa6=util.sessionData=>/"+sessionData);
        shipmentData.setBuyerLastname(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, buyerLastName));
        shipmentData.setBuyerPhone(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, buyerPhone));
        shipmentData.setBuyerEmail(sessionData.getEmail());
        //System.out.println("###############1aaaaaaaaaaaa7=util.sessionData=>/"+sessionData);
//        shipmentData.setBuyerCountryCode(shipmentData.getBuyerCountryCode());
//        shipmentData.setBuyerCity(shipmentData.getBuyerCity());
//        shipmentData.setBuyerProvince(shipmentData.getBuyerProvince());
//        shipmentData.setBuyerZipCode(shipmentData.getBuyerZipCode());
//        shipmentData.setBuyerAddr1(shipmentData.getBuyerAddr1());
//        shipmentData.setBuyerAddr2(shipmentData.getBuyerAddr2());
        //System.out.println("###############1aaaaaaaaaaaa8=shipmentData/"+shipmentData);
        int cnt = 0;
        int orderShipmentCnt = shipmentMapper.selectShipmentOrderCount(shipmentData);
        //System.out.println("###############1aaaaaaaaaaaa9=orderShipmentCnt/"+orderShipmentCnt);
        if(orderShipmentCnt == 0) {
            cnt = shipmentMapper.insertShipment(shipmentData);
            if(cnt == 1) {
                int orderShipmentOrderCnt = shipmentMapper.selectShipmentOrderCountOrder(shipmentData);
                //System.out.println("###############1aaaaaaaaaaaa10=orderShipmentOrderCnt/"+orderShipmentOrderCnt);
                if(orderShipmentOrderCnt == 0)
                    cnt = shipmentMapper.insertShipmentOrder(shipmentData);
            }
        }else {
            cnt = shipmentMapper.updateShipment(shipmentData);
        }
        
        return cnt;
    }
	
	/**
     * 배송 > 배송주문저장 
     * @return
     */
	public int insertShipmentOrder(ShipmentData shipmentData,HttpSession sess) {
	   return shipmentMapper.insertShipmentOrder(shipmentData);
	}
    
    /**
     * 배송 > 배송수정 
     * @return
     */
    public int updateShipment(ShipmentData ship){
        return shipmentMapper.updateShipment(ship);
    }
    
    //배송 > 배송상세
    public ShipmentData selectShipmentDetail(ShipmentData ship) {
        return shipmentMapper.selectShipmentDetail(ship);
    }
    
	
    //배송 > 배송조회(개수)_delivery만 조회
    public int selectShipmentCount(ShipmentData ship, HttpSession sess) {
        return shipmentMapper.selectShipmentCount(ship);
    }
    
    //배송 > 배송주문조회(개수) delivery와 delivery_order join하여 조회
    public int selectShipmentOrderCount(ShipmentData ship, HttpSession sess) {
        return shipmentMapper.selectShipmentOrderCount(ship);
    }
    
    //배송 > 삭제
    public int deleteShipment(ShipmentData ship, HttpSession sess) {
        int cnt = 0;
        String email = util.getUserEmail(sess); // 세션 이메일 받아 오기
        String mcode = ship.getMasterCode();
        ship.setEmail(email);
        Map<String, Object> map = new HashMap<String, Object>();
        if(ship != null) {
            if(mcode != null) {
                if(mcode.indexOf(",") > 0) {
                    String[] codeAry = mcode.split(",");
                    //map.put("masterCodeAry", codeAry);
                    ship.setMasterCodeList(codeAry);
                    //System.out.println("##########111ship==>"+ship);
                    
                    cnt = shipmentMapper.selectShipmentDeleteCounts(ship);
                    if(cnt > 0) {
                        shipmentMapper.deleteShipmentsOrder(ship);       //주문 활성처리
                        cnt = shipmentMapper.deleteShipments(ship);     //배송 비활성처리
                    }else {
                        cnt = -1;
                    }
                }else {
                    //System.out.println("##########222222222222asdasdasd==>"+ship);
                    cnt = shipmentMapper.selectShipmentDeleteCount(ship);
                    //System.out.println("##########222222222222asdasdasd11==>"+cnt);
                    if(cnt > 0) {
                        shipmentMapper.deleteShipmentOrder(ship);
                        cnt = shipmentMapper.deleteShipment(ship);
                    }else {
                        cnt = -1;
                    }
                }
            }else {
                System.out.println("##########23333333333asdasdasd==>"+ship);
                cnt = shipmentMapper.selectShipmentDeleteCount(ship);
                if(cnt > 0) {
                    shipmentMapper.deleteShipmentsOrder(ship);
                    cnt = shipmentMapper.deleteShipment(ship);
                }else {
                    cnt = -1;
                }
            }
            
        }

        
        return cnt;
    }
    
	
	/**
	 * 배송 > 배송목록
	 * @return
	 */
	public Map<String, Object> selectShimentList(ShipmentData ship, HttpSession sess) {
		String email = util.getUserEmail(sess);   // 세션 이메일 받아 오기
		String locale = util.getUserLocale(sess); // 세션 다국어 받아 오기 
		ShipmentData sData = new ShipmentData();
		if(ship == null) {
		    sData.setCurrentPage(1);
		    sData.setPageSize(10);
		    ship = sData;
		}
		ship.setEmail(email);
		ship.setLocale(locale);
		int dataCount = shipmentMapper.selectShipmentCount(ship); // 전체 건수 조회
		int currentPage = ship.getCurrentPage(); //현제 페이지
		int pageSize = ship.getPageSize(); // 페이지 당 데이터 수
		if (pageSize == 0) {
			currentPage = 1 ;
			ship.setCurrentPage(currentPage) ;
			pageSize = dataCount ;
			ship.setPageSize(pageSize) ;
		}
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		List<ShipmentData> list = new ArrayList<ShipmentData>(); //데이터 리스트 변수 선언 
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			ship.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			ship.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			System.out.println("################ship=>"+ship);
			list = shipmentMapper.selectShipment(ship);
			//암호화풀기어 리스트 재생성
			int intA = 0;
			for(ShipmentData item : list) {
			    String fName = item.getBuyerFirstname();
			    String lName = item.getBuyerLastname();
			    String phone = item.getBuyerPhone();
			    String sellerName = item.getSellerName();
			    String customerName = item.getCustomerName();
			    if(fName != null) item.setBuyerFirstname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, fName));
			    if(lName != null) item.setBuyerLastname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, lName));
			    if(phone != null) item.setBuyerPhone(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, phone));
			    if(sellerName != null) item.setSellerName(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sellerName));
			    if(customerName != null) item.setCustomerName(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, customerName));
			    String hblNo = item.getHblNo();
			    // 엑셀 저장 처리 
			    if(currentPage == 0) {
			    	String courierCompany = item.getCourierCompany();
			    	if(courierCompany != null) {
			    		if(courierCompany.equals("B010020")) { // 롯데 정보
			    			item.setConsigneeName(lotteAcperNm);
			    			item.setConsigneeTel(lotteAcperTel);
			    			item.setConsigneeCpno(lotteAcperCpno);
			    			item.setConsigneeZipcode(lotteAcperZipcd);
			    			item.setConsigneeAddr1(lotteAcperAddr);
			    			item.setConsigneeAddr2("");
			    		} else if(courierCompany.equals("B010010")) { // 우체국 정보
			    			
			    		}
			    	}
			    }
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
	 * 배송 > 배송목록 (펌뱅킹)
	 * @return
	 */
	public Map<String, Object> selectShimentListBank(ShipmentData ship, HttpSession sess) {
		String email = util.getUserEmail(sess);   // 세션 이메일 받아 오기
		String locale = util.getUserLocale(sess); // 세션 다국어 받아 오기 
		ShipmentData sData = new ShipmentData();
		if(ship == null) {
			sData.setCurrentPage(1);
			sData.setPageSize(10);
			ship = sData;
		}
		ship.setEmail(email);
		ship.setLocale(locale);
		int dataCount = shipmentMapper.selectShipmentCountBank(ship); // 전체 건수 조회
		int currentPage = ship.getCurrentPage(); //현제 페이지
		int pageSize = ship.getPageSize(); // 페이지 당 데이터 수
		if (pageSize == 0) {
			currentPage = 1 ;
			ship.setCurrentPage(currentPage) ;
			pageSize = dataCount ;
			ship.setPageSize(pageSize) ;
		}
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		List<ShipmentData> list = new ArrayList<ShipmentData>(); //데이터 리스트 변수 선언 
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			ship.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			ship.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			System.out.println("################ship=>"+ship);
			list = shipmentMapper.selectShipmentBank(ship);
			//암호화풀기어 리스트 재생성
			int intA = 0;
			for(ShipmentData item : list) {
				String fName = item.getBuyerFirstname();
				String lName = item.getBuyerLastname();
				String phone = item.getBuyerPhone();
				String sellerName = item.getSellerName();
				String customerName = item.getCustomerName();
				if(fName != null) item.setBuyerFirstname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, fName));
				if(lName != null) item.setBuyerLastname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, lName));
				if(phone != null) item.setBuyerPhone(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, phone));
				if(sellerName != null) item.setSellerName(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sellerName));
				if(customerName != null) item.setCustomerName(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, customerName));
				// 엑셀 저장 처리 
				if(currentPage == 0) {
					String courierCompany = item.getCourierCompany();
					if(courierCompany != null) {
						if(courierCompany.equals("B010020")) { // 롯데 정보
							item.setConsigneeName(lotteAcperNm);
							item.setConsigneeTel(lotteAcperTel);
							item.setConsigneeCpno(lotteAcperCpno);
							item.setConsigneeZipcode(lotteAcperZipcd);
							item.setConsigneeAddr1(lotteAcperAddr);
							item.setConsigneeAddr2("");
						} else if(courierCompany.equals("B010010")) { // 우체국 정보

						}
					}
				}
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
	
	//결제 이후 처리
    public ShipmentPopupData selectPaymentChangeInfo(ShipmentPopupData ship, HttpServletRequest req, HttpSession sess) {
    	ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
    	ShipmentPopupData returnData = new ShipmentPopupData();
    	returnData.setApiReturnChk(true);
    	System.out.println("###############selectPaymentChangeInfo=>/0000000"+ship);
    	List<ShipmentPopupData> lspData = apiService.shopifyGetBilling(ship, req, sess);
    	System.out.println("yryr11###############selectPaymentChangeInfo=>/0000000"+lspData);
    	for(ShipmentPopupData data : lspData) {
    		//String apiStatus = data.getApiStatus();
    		String apiStatus = "accepted"; //yr추가
    		
    		System.out.println("###############selectPaymentChangeInfo=>/11111111"+apiStatus);
    		if("pending".equals(apiStatus)) {		//pending
        		
    		}else if("accepted".equals(apiStatus) || "active".equals(apiStatus)) {		//accepted or active (조한두 2020.05.07.목)
        		LOGGER.debug("<<<<<<<<<<<<<<<apiStatus>>>>>>>>>>>>>>>>>>" + apiStatus);     
        		// N개의 PAYPAL 처리 로직으로 수정 
        		int ret = 0;
        		List<ShipmentPopupData> payInListData = payMapper.selectPaymentChangeInfo(ship);		//pay_id 검색
        		LOGGER.debug(">>>>> apiId:" + ship.getApiId());
        		for(ShipmentPopupData payInData: payInListData ) 
        		{
        			LOGGER.debug(">>>>> pay_id:" + payInData.getPayId());   // tb_shopify_api_applicationchg
        			LOGGER.debug(">>>>> masterCode:" + payInData.getMasterCode());
        			payInData.setEmail(ship.getEmail());
            		payInData.setShopId(ship.getShopId());
            		payInData.setApiId(ship.getApiId());
            		payInData.setMasterCode(payInData.getMasterCode());		// 조한두 20.06.01
            		ShipmentPopupData actInData = payMapper.selectPaymentPopAccept(payInData); // master_code
            		ShipmentPopupData spData = new ShipmentPopupData();
        			spData.setEmail(ship.getEmail());
                 	spData.setApiId(actInData.getApiId());
                 	spData.setApiName(actInData.getApiName());
                 	spData.setApiClientId(actInData.getApiClientId());
                 	spData.setApiPrice(actInData.getApiPrice());
                 	LOGGER.debug(">>>>>>>>>>> ACTIVEATE [ START ] >>>>>>>>>>>>>>>>>>");
                 	ShipmentPopupData retData = apiService.shopifyProcBillingActivate(spData, req, sess);						//pay_id로 activate 처리 
                 	if(retData.getApiReturnChk()) {
                 		LOGGER.debug(">>>>>>>>>>> ACTIVEATE [ END ] >>>>>>>>>>>>>>>>>>");
                 		spData.setShopName(actInData.getShopName());
                     	spData.setOrderCode(actInData.getOrderCode());
                     	Map<String,Object> apiMapEvent = apiService.shopifyPostOrderIdFullist(spData,req, sess);
                     	spData.setMasterCode(actInData.getMasterCode());
                     	spData.setState("A020020");
                     	spData.setStateGroup("A020000");
                     	ret = payMapper.updatePopPaymentPaymentDelivery(spData); // tb_delivery 
                     	LOGGER.debug("###############updatePopPaymentPaymentDelivery=>"+ ret);
                 	}else {
                 		returnData.setApiReturnChk(false);
                 	}
        		}
             	ship.setApiStatus(apiStatus);
        	   	ret = payMapper.updatePaymentChangeInfo(ship);			// tb_shopify_api_applicationchg
             	LOGGER.debug("###############updatePaymentChangeInfo=>"+ ret);
             	
        		// 1개의 PAYPAL 처리 로직 (원본)
        		/*
        		ShipmentPopupData payInData = payMapper.selectPaymentChangeInfo(ship);		//pay_id 검색
        		payInData.setEmail(ship.getEmail());
        		payInData.setShopId(ship.getShopId());
        		payInData.setApiId(ship.getApiId());
        		 ShipmentPopupData actInData = payMapper.selectPaymentPopAccept(payInData); 

        			System.out.println("###############selectPaymentChangeInfo=>ship.getEmail()/3333333333"+ship.getEmail());
        			System.out.println("###############selectPaymentChangeInfo=>ship.getApiId()/3333333333"+ship.getApiId());
        			ShipmentPopupData spData = new ShipmentPopupData();
        			spData.setEmail(ship.getEmail());
                 	spData.setApiId(actInData.getApiId());
                 	spData.setApiName(actInData.getApiName());
                 	spData.setApiClientId(actInData.getApiClientId());
                 	spData.setApiPrice(actInData.getApiPrice());
                 	
                 	ShipmentPopupData retData = apiService.shopifyProcBillingActivate(spData, req, sess);						//pay_id로 activate 처리 
                 	System.out.println("###############selectPaymentChangeInfo=>retData.getApiReturnChk()/4444444444"+retData.getApiReturnChk());
                 	if(retData.getApiReturnChk()) {
                 		//System.out.println("###############selectPaymentChangeInfo=>/4444444444");
                 		spData.setShopName(actInData.getShopName());
                     	spData.setOrderCode(actInData.getOrderCode());
                     	Map<String,Object> apiMapEvent = apiService.shopifyPostOrderIdFullist(spData,req, sess);
                     	spData.setMasterCode(actInData.getMasterCode());
                     	spData.setState("A020020");
                     	spData.setStateGroup("A020000");
                     	int ret = payMapper.updatePopPaymentPaymentDelivery(spData); // tb_delivery 
                     	LOGGER.debug("###############updatePopPaymentPaymentDelivery=>"+ ret);
                     	ship.setApiStatus(apiStatus);
                     	ret = payMapper.updatePaymentChangeInfo(ship);			// tb_shopify_api_applicationchg
                     	LOGGER.debug("###############updatePaymentChangeInfo=>"+ ret);
                 	}else {
                 		returnData.setApiReturnChk(false);
                 	}
        		*/
        		
        		 
        	}else if("declined".equals(apiStatus)) {
        		
        	}else if("expired".equals(apiStatus)) {
        		
        	}else {
        		
        	}
    	}
    	
    	
        return returnData;
    }
}
