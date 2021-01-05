package com.shopify.payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.mapper.PaymentMapper;
import com.shopify.shipment.ShipmentData;
import com.shopify.shop.ShopData;

@Service
@Transactional
public class PaymentService {
	
	@Autowired 
	private PaymentMapper payMapper;
	
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
	public int insertPaymentPayment(ShipmentData Data,HttpSession sess) {
        ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
        //System.out.println("###############1aaaaaaaaaaaa1=util.sessionData=>/"+sessionData);
        //배송코드 임의생성(D+오늘날짜+임의수4자리)
        //LOGGER.debug("###############1aaaaaaaaaaaa=util.getDateElement(\"full\")=>/"+util.getDateElement("full"));
        //System.out.println("###############1aaaaaaaaaaaa1=util.getDateElement(\\\"full\\\")=>/"+util.getDateElement("full"));
        String keyFull = util.getDateElement("full");
        String masterCode = keyFull.substring(2,keyFull.length()-1)+util.getRandomString(4).toUpperCase();
        
        //System.out.println("###############1aaaaaaaaaaaa2=util.sessionData=>/"+sessionData);
        String phone = Data.getSellerPhone();
        if(phone == null) phone = " ";
        String sellerName = sessionData.getShopName();
        if(sellerName == null) sellerName = sessionData.getEmail();
        String buyerFirstName = Data.getBuyerFirstname();
        if(buyerFirstName == null) buyerFirstName = " ";
        String buyerLastName = Data.getBuyerLastname();
        if(buyerLastName == null) buyerLastName = " ";
        String buyerPhone = Data.getBuyerPhone();
        if(buyerPhone == null) buyerPhone = " ";
        //System.out.println("###############1aaaaaaaaaaaa3=util.sessionData=>/"+sessionData);
        //System.out.println("###############1aaaaaaaaaaaa4=phone/"+phone);
        Data.setMasterCode(masterCode);
//        Data.setOrderIdx(Data.getOrderIdx());
//        Data.setMasterCode(Data.getMasterCode());
//        Data.setOrderCode(Data.getOrderCode());
//        Data.setOrderDate(Data.getOrderDate());
//        Data.setShopIdx(Data.getShopIdx());
        Data.setSellerPhone(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, phone));
        Data.setSellerName(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, sellerName));
        //System.out.println("###############1aaaaaaaaaaaa5=util.sessionData=>/"+sessionData);
//        Data.setSellerCountryCode(Data.getSellerCountryCode());
//        Data.setSellerCity(Data.getSellerCity());
//        Data.setSellerZipCode(Data.getSellerZipCode());
//        Data.setSellerAddr1(Data.getSellerAddr1());
//        Data.setSellerAddr2(Data.getSellerAddr2());
        Data.setBuyerFirstname(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, buyerFirstName));
        //System.out.println("###############1aaaaaaaaaaaa6=util.sessionData=>/"+sessionData);
        Data.setBuyerLastname(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, buyerLastName));
        Data.setBuyerPhone(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, buyerPhone));
        Data.setBuyerEmail(sessionData.getEmail());
        //System.out.println("###############1aaaaaaaaaaaa7=util.sessionData=>/"+sessionData);
//        Data.setBuyerCountryCode(Data.getBuyerCountryCode());
//        Data.setBuyerCity(Data.getBuyerCity());
//        Data.setBuyerProvince(Data.getBuyerProvince());
//        Data.setBuyerZipCode(Data.getBuyerZipCode());
//        Data.setBuyerAddr1(Data.getBuyerAddr1());
//        Data.setBuyerAddr2(Data.getBuyerAddr2());
        //System.out.println("###############1aaaaaaaaaaaa8=Data/"+Data);
        int cnt = 0;
        int orderPaymentCnt = payMapper.selectPaymentOrderCount(Data);
        //System.out.println("###############1aaaaaaaaaaaa9=orderPaymentCnt/"+orderPaymentCnt);
        if(orderPaymentCnt == 0) {
            cnt = payMapper.insertPayment(Data);
            if(cnt == 1) {
                int orderPaymentOrderCnt = payMapper.selectPaymentOrderCountOrder(Data);
                //System.out.println("###############1aaaaaaaaaaaa10=orderPaymentOrderCnt/"+orderPaymentOrderCnt);
                if(orderPaymentOrderCnt == 0)
                    cnt = payMapper.insertPaymentOrder(Data);
            }
        }else {
            cnt = payMapper.updatePayment(Data);
        }
        
        return cnt;
    }
	
	/**
     * 배송 > 배송주문저장 
     * @return
     */
	public int insertPaymentOrder(ShipmentData Data,HttpSession sess) {
	   return payMapper.insertPaymentOrder(Data);
	}
    
    /**
     * 배송 > 배송수정 
     * @return
     */
    public int updatePayment(ShipmentData ship){
        return payMapper.updatePayment(ship);
    }
    
    //배송 > 배송상세
    public ShipmentData selectPaymentDetail(ShipmentData ship) {
        return payMapper.selectPaymentDetail(ship);
    }
    
	
    //배송 > 배송조회(개수)_delivery만 조회
    public int selectPaymentCount(ShipmentData ship, HttpSession sess) {
        return payMapper.selectPaymentCount(ship);
    }
    
    //배송 > 배송주문조회(개수) delivery와 delivery_order join하여 조회
    public int selectPaymentOrderCount(ShipmentData ship, HttpSession sess) {
        return payMapper.selectPaymentOrderCount(ship);
    }
    
    //배송 > 삭제
    public int deletePayment(ShipmentData ship, HttpSession sess) {
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
                    
                    cnt = payMapper.selectPaymentDeleteCounts(ship);
                    if(cnt > 0) {
                        payMapper.deletePaymentsOrder(ship);       //주문 활성처리
                        cnt = payMapper.deletePayments(ship);     //배송 비활성처리
                    }else {
                        cnt = -1;
                    }
                }else {
                    //System.out.println("##########222222222222asdasdasd==>"+ship);
                    cnt = payMapper.selectPaymentDeleteCount(ship);
                    //System.out.println("##########222222222222asdasdasd11==>"+cnt);
                    if(cnt > 0) {
                        payMapper.deletePaymentOrder(ship);
                        cnt = payMapper.deletePayment(ship);
                    }else {
                        cnt = -1;
                    }
                }
            }else {
                System.out.println("##########23333333333asdasdasd==>"+ship);
                cnt = payMapper.selectPaymentDeleteCount(ship);
                if(cnt > 0) {
                    payMapper.deletePaymentsOrder(ship);
                    cnt = payMapper.deletePayment(ship);
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
		//System.out.println("################ship.locale=>"+locale);
		ship.setEmail(email);
		ship.setLocale(locale);
		
		int dataCount = payMapper.selectPaymentCount(ship); // 전체 건수 조회
		int currentPage = ship.getCurrentPage(); //현제 페이지
		int pageSize = ship.getPageSize(); // 페이지 당 데이터 수
		if (pageSize == 0) {
			currentPage = 1 ;
			ship.setCurrentPage(currentPage) ;
			pageSize = dataCount ;
			ship.setPageSize(pageSize) ;
		}
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		//System.out.println("################ship=>"+dataCount);
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		List<ShipmentData> list = new ArrayList<ShipmentData>(); //데이터 리스트 변수 선언 
		
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			
			ship.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			ship.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			System.out.println("################ship=>"+ship);
			list = payMapper.selectPayment(ship);
			
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
}
