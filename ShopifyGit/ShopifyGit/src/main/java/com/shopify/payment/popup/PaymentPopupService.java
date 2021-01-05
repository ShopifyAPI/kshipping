package com.shopify.payment.popup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.api.ShopifyApiController;
import com.shopify.common.EmailData;
import com.shopify.common.MailService;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.mapper.EmailMapper;
import com.shopify.mapper.PaymentMapper;
import com.shopify.payinfo.PayinfoData;
import com.shopify.payinfo.PayinfoService;
import com.shopify.payment.PaymentService;
import com.shopify.shipment.ShipmentData;
import com.shopify.shipment.popup.LocalDeliveryPaymentData;
import com.shopify.shipment.popup.ShipmentPopupData;
import com.shopify.shipment.popup.ShipmentPopupDataLocalData;
import com.shopify.shop.ShopData;

/**
 * 배송 팝업 서비스
 *
 */
@Service
@Transactional
public class PaymentPopupService {
	private Logger LOGGER = LoggerFactory.getLogger(PaymentPopupService.class);
	@Autowired
	private PaymentMapper paymentMapper;
	@Autowired
	private EmailMapper emailMapper;
	@Autowired
	private ShopifyApiController sApiController;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private UtilFn util;
 
	@Autowired
	private PayinfoService payinfoService;
 

	@Value("${mail.send}")		String mailSend;
	@Value("${mail.real}")		String mailReal;
	public List<ShipmentPopupData> selectPopPaymentSkuList(ShipmentPopupData shipPopData, HttpSession sess) {
		return paymentMapper.selectPopPaymentSkuList(shipPopData);
	}

	public int selectPopPaymentSkuCount(ShipmentPopupData shipPopData) {
		return paymentMapper.selectPopPaymentSkuCount(shipPopData);
	}

	public List<ShipmentPopupData> selectPopPaymentList(ShipmentPopupData shipPopData, HttpSession sess) {
		return paymentMapper.selectPopPaymentList(shipPopData);
	}

	public List<ShipmentPopupData> selectPopPaymentCombineList(ShipmentPopupData shipPopData, HttpSession sess) {
		return paymentMapper.selectPopPaymentCombineList(shipPopData);
	}

	public int selectPopPaymentCount(ShipmentPopupData shipPopData) {
		return paymentMapper.selectPopPaymentCount(shipPopData);
	}

	public int selectPopPaymentCombineCount(ShipmentPopupData shipPopData) {
		return paymentMapper.selectPopPaymentCombineCount(shipPopData);
	}

	public List<ShipmentPopupData> selectPopPaymentDeliveryList(ShipmentPopupData shipPopData, HttpSession sess) {
		return paymentMapper.selectPopPaymentDeliveryList(shipPopData);
	}

	public List<ShipmentPopupData> selectPopPaymentDeliveryArrayList(ShipmentPopupData shipPopData, HttpSession sess) {
		return paymentMapper.selectPopPaymentDeliveryArrayList(shipPopData);
	}
	/**
	 * 관리자 세팅 배송비 결제 방법 (조한두)
	 * @param email
	 * @return
	 */
	public String selectAdminPayMethod(String email) {
		return paymentMapper.selectAdminPayMethod(email);
	}
	/**
	 * 배송 > 로컬정보
	 * 
	 * @return List<LocalDeliveryShipmentData>
	 */
	public List<LocalDeliveryPaymentData> selectLocalDeliveryPaymentList(LocalDeliveryPaymentData payment, HttpSession sess) {
		return paymentMapper.selectLocalDeliveryPayment(payment);
	}

	/**
	 * 배송 > 결제처리 > 배송정보
	 * 
	 * @return int
	 */
	public int updatePopPaymentPaymentDelivery(ShipmentPopupData ship) {
		return paymentMapper.updatePopPaymentPaymentDelivery(ship);
	}

	/**
	 * 배송 > 결제처리 > 결제정보
	 * 
	 * @return int
	 */
	public int updatePopPaymentPaymentDeliveryPayment(ShipmentPopupData ship) {
		return paymentMapper.updatePopPaymentPaymentDeliveryPayment(ship);
	}

	/**
	 * 배송 > 결제처리 > 로컬 결제정보
	 * 
	 * @return int
	 */
	public int insertPopPaymentPaymentDeliveryPayment(ShipmentPopupData ship) {
		return paymentMapper.insertPopPaymentPaymentDeliveryPayment(ship);
	}

	/**
	 * 배송 > 결제처리 > 로컬처리
	 * 
	 * @return int
	 */
	public int insertPopPaymentPaymentDeliveryLocal(ShipmentPopupData ship) {
		return paymentMapper.insertPopPaymentPaymentDeliveryLocal(ship);
	}

	public int insertPopPaymentPaymentDeliveryLocalData(ShipmentPopupData ship) {
		return paymentMapper.insertPopPaymentPaymentDeliveryLocalData(ship);
	}

	/**
	 * 배송 > 결제처리 > 배송비계산
	 * 
	 * @return ShipmentPopupData
	 */
	public ShipmentPopupData selectPaymentDelivery(ShipmentPopupData ship) {
		return paymentMapper.selectPaymentDelivery(ship);
	}
	
	
	/**
	 * 배송 > 결제승인 팝업
	 * 
	 * @return ShipmentPopupData
	 */
	public ShipmentPopupData selectPaymentPopAccept(ShipmentPopupData ship) {
		return paymentMapper.selectPaymentPopAccept(ship);
	}

	
	/**
	 * 배송 > 결제처리 > 결제후 저장
	 * 
	 * @return ShipmentPopupData
	 */
	public int insertPaymentPayInfo(ShipmentPopupData ship) {
		return paymentMapper.insertPaymentPayInfo(ship);
	}

	/**
	 * 배송 > 결제처리 > API > 이행이벤트처리
	 * 
	 * @return
	 */
	public Map<String, Object> shopifyPostEventOrderId(ShipmentPopupData ship, HttpServletRequest req, HttpSession sess) {
		return sApiController.shopifyPostEventOrderId(ship, req, sess);
	}

	/**
	 * 배송 > 결제처리 > API > 이행목록 조회
	 * 
	 * @return
	 */
	public ShipmentPopupData shopifyGetOrderIdFullist(ShipmentPopupData ship, HttpServletRequest req, HttpSession sess) {
		return sApiController.shopifyGetOrderIdFullist(ship, req, sess);
	}

	/**
	 * 배송 > 결제처리 > API > 이행등록
	 * 
	 * @return
	 */
	public Map<String, Object> shopifyPostOrderIdFullist(ShipmentPopupData ship, HttpServletRequest req, HttpSession sess) {
		return sApiController.shopifyPostOrderIdFullist(ship, req, sess);
	}

	/**
	 * 배송 > 결제처리 > API > 빌링
	 * 
	 * @return
	 */
	public ShipmentPopupData shopifyProcBilling(ShipmentPopupData ship, HttpServletRequest req, HttpSession sess) {
		return sApiController.shopifyProcBilling(ship, req, sess);
	}

	/**
	 * 배송 > 결제처리 > API > 빌링 활성화
	 * 
	 * @return
	 */
	public ShipmentPopupData shopifyProcBillingActivate(ShipmentPopupData ship, HttpServletRequest req, HttpSession sess) {
		return sApiController.shopifyProcBillingActivate(ship, req, sess);
	}

	public int selectPaymentPrice(ShipmentPopupData shipPopData) {
		return paymentMapper.selectPaymentPrice(shipPopData);
	}

	public int updatePopPaymentStat(ShipmentPopupData shipPopData) {

		ShipmentPopupData ship = new ShipmentPopupData();

		String stateGroup = shipPopData.getStateGroup();

		/* 반송사유 없음
		if("A030000".equals(shipPopData.getStateGroup())) {
			String reason = shipPopData.getReason();
			ship.setReason(reason); //반송사유로 세팅
			ship.setStateCode(reason);
			ship.setStateGroupCode(stateGroup);
		} else {
		*/
		String state = shipPopData.getStateGroup().substring(0, 5) + "10"; // 초기값 세팅

		ship.setStateCode(state);
		ship.setStateGroupCode(stateGroup);
		/*
		}
		*/

		LOGGER.debug("updatePopPaymentStat[stateGroup] : " + ship.getStateGroupCode());
		LOGGER.debug("updatePopPaymentStat[state] : " + ship.getStateCode());

		String[] masterCodeList = shipPopData.getMasterCode().split(",");
		ship.setMasterCodeList(masterCodeList);

		return paymentMapper.updatePopPaymentStat(ship);
	}

	/**
	 * 배송 > 배송비 결제 api 저장
	 * 
	 * @return
	 */
	public int insertPaymentPaymentApi(ShipmentPopupData paymentPopupData) {
		return paymentMapper.insertPaymentPaymentApi(paymentPopupData);
	}
	/**
	 * 배송 > 배송비 결제(펌뱅킹) api 저장
	 * 
	 * @return
	 */
	public int insertPaymentCashApi(ShipmentPopupData paymentPopupData) {
		return paymentMapper.insertPaymentCashApi(paymentPopupData);
	}
	
	

	/**
	 * 배송 > 배송비 결제 api 수정
	 * 
	 * @return
	 */
	public int updatePaymentPaymentApi(ShipmentPopupData paymentPopupData) {
		return paymentMapper.updatePaymentPaymentApi(paymentPopupData);
	}
	
	public int updatePaymentBoxSize(ShipmentPopupData shipPopPayment ) {
		return paymentMapper.updatePaymentBoxSize(shipPopPayment);
	}
	/*
	 *  주문 모니터링 - 이메일 
	 * 
	 */
	private void sendEmailMonitoring(boolean isReal, String company, String contents ) {
		String to_email = "shopigate@solugate.com";
		StringBuffer temp = new StringBuffer();
		if(isReal)
			temp.append("[운영 모니터링]");
		else
			temp.append("[개발 모니터링]");
		temp.append(" - "+company);
	    String subject = temp.toString();
	    EmailData email = new EmailData();
	    email.setToEmail(to_email);
	    email.setSubject(subject);
	    email.setContent(contents);
	    emailMapper.insertEmail(email);
	}

	/**
	 * 조한두 : 결제방법 추가 (20.05.12)
	 * payMethod : paypal, bank 
	 * @param shipPopData
	 * @param model
	 * @param req
	 * @param res
	 * @param sess
	 * @return
	 */
	public ModelAndView popPaymentPaymentProc(final ShipmentPopupData viewShipPopData, Model model, HttpServletRequest req, HttpServletResponse res, HttpSession sess) {
		ModelAndView mav = new ModelAndView("jsonView");
		int paymentTotal = 0;
		int weightTotal = 0;
		int deliveryAmount = 0;
		// 모니터링 변수 시작 ------
		boolean  isReal = false;
		boolean isMonitor = false;
		boolean isMailSend = false;
		String company="";
		String contents="";
		if("true".equals(mailSend))
			isMailSend = true;
		if("true".equals(mailReal))
			isReal = true;
		// 모니터링 변수 끝 ------ 
		LOGGER.debug("############### popPaymentPaymentProc=shipPopData=>/" + viewShipPopData);
		String payMethod = viewShipPopData.getPayMethod();			// 1: 페이팔 , 2: 계좌 이체   조한두(20.05.12)
		LOGGER.debug("### [USER SELECT PAY METHOD] " + payMethod + " ##############################");
		ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
	 	String billingYn=sessionData.getBillingYn();																				// 결제 테스트 모드를 tb_pay_info 에 추가: 데이터 삭제시 billing_yn 값이 N 인 데이터만 클린징 할것 : 2020.06.01 조한두 ***************************** 
		ShipmentData ship = new ShipmentData();																			// selectPaymentDetail 계산용 변수 
		ShipmentPopupData returnspData = new ShipmentPopupData();									// paypal 결제 결과 저장 변수 
		List<ShipmentPopupData> shipPopData = new ArrayList<ShipmentPopupData>();		// 1~N 건 계산용 변수 
		ShipmentPopupData shipPopPayment = new ShipmentPopupData();								// 배송비 계산 1건용 임시 변수 
		shipPopPayment.setEmail(sessionData.getEmail());
		shipPopPayment.setLocale(sessionData.getLocale());
		ship.setLocale(sessionData.getLocale());
		ship.setEmail(sessionData.getEmail());
		String[] masterCodeList = null;
		String[] boxSizeList = null;
		List<ShipmentPopupDataLocalData> localDataList = viewShipPopData.getLocalData();		// 국내 픽업 계산시 변수 (픽업 선택하는 종류만큼 추가됨 , 픽업 체크 박스->픽업 상자
		if(localDataList != null)
		{
			LOGGER.debug(">>>>Select Pickup  localDataList  SIZE:>>>>" + localDataList.size() );
			for (ShipmentPopupDataLocalData lData : localDataList) {
				LOGGER.debug("company:" +lData.getCompany());
				LOGGER.debug("Box size:" + lData.getBoxSize());
			}
		}
		//########### 결제처리 연동 ##########
		// 결제 처리 프로세스
		int payChk = 1;
		LOGGER.debug("[BILLING_YN] " + billingYn + " ###### Y= REAL , N = TEST ####");
		masterCodeList = viewShipPopData.getMasterCodeList();
		if(masterCodeList == null || masterCodeList.length ==0 ) {
			LOGGER.error(">>>> masterCodeList  SIZE>>>>" + masterCodeList.length );
			mav.addObject("result", 0);
			mav.addObject("errCode", false);
			mav.addObject("errMsg", "실패");
			return mav;
		}
        if(masterCodeList != null)
        {
        	LOGGER.debug(">>>> masterCodeList  SIZE>>>>" + masterCodeList.length );
        	int count=0;
        	for (String mCode : masterCodeList) {
        		LOGGER.debug(">>>> masterCode[" +count+"]" + mCode );
        		count++;
        	}
        }
        LOGGER.debug(">>>>>>>>CALCULATE Process >>>>>>>>>>>>>>>>>>");    
//        ShipmentData sData = null; // 국내 계산용 변수 
		//총결제 금액 처리
		int combineCnt = 0;
		int total_price_sum = 0; 	// 합계 금액 
		int total_price = 0;
		int total_delivery_price = 0;
		int total_delivery_pickupprice = 0;
		int total_delivery_vatprice = 0;
		int total_delivery_rankprice = 0;
		shipPopPayment.setMasterCodeList(masterCodeList);
		if (masterCodeList != null) {
			//국내택배 픽업시 총결제 금액 계산
			if (localDataList != null) { 
				LOGGER.debug("#################[ND]###############");
					
				for (ShipmentPopupDataLocalData lData : localDataList) {
					LOGGER.debug("--------------------------------------------------------------------");
					LOGGER.debug("lData>" + lData.toString());
					if("B010030".equals(lData.getCompany()))
					{
						isMonitor = true;
						company = "판토스";
						contents  = "픽업주문 ";
					}
					else if("B010020".equals(lData.getCompany()))
					{
						isMonitor = true;
						company = "롯데글로벌로지스";
						contents  = "픽업주문 ";
					}
					
					int cnt = 0;
					String[] localMasterCodeList = lData.getMcode();
					for (String mCode : localMasterCodeList) {
						int onePrice=0 ;		//1주문 최종금액
						int oneRankPrice=0;		//1주문 할인금액
						int oneDeliveryPrice=0; //1주문 공시요금
						int onePickupPrice = 0;
						int onePickupVat = 0;
						total_price = 0;
						total_delivery_price = 0;
						total_delivery_pickupprice = 0;
						total_delivery_vatprice = 0;
						total_delivery_rankprice = 0; // 할인율 초기화  (조한두 20.11.02 N개 결재시 이전 할인율이 다른건에도 적용됨)
							LOGGER.debug("--------------------------------------------------------------------");
							LOGGER.debug("mcode:" + mCode);
							contents += " 마스터코드:" + mCode ;
							ShipmentPopupData shipPopDataTemp = new ShipmentPopupData() ;				     
							// 로컬 택배 값을 저장 한다. 
							shipPopPayment.setLocalCompany(lData.getCompany());
							shipPopPayment.setLocalBoxSize(lData.getBoxSize());
							shipPopPayment.setLocalPrice(lData.getPrice());
							shipPopPayment.setLocalMcode(lData.getMcode());
							shipPopPayment.setMasterCode(mCode);
							shipPopPayment.setBoxSize(lData.getBoxSize());
							shipPopDataTemp.setMasterCode(mCode);
							LOGGER.debug("BOX SIZE:" + lData.getBoxSize());
							LOGGER.debug("############### popPaymentPaymentProc=shipPopPayment=>/ " + shipPopPayment);
							updatePaymentBoxSize(shipPopPayment);
							// 배송비 요금계산 
							if("B010030".equals(lData.getCompany()))
								shipPopPayment.setPickupCode("T_PICKUP");
							else if("B010020".equals(lData.getCompany()))
								shipPopPayment.setPickupCode("L_PICKUP");
							else
								shipPopPayment.setPickupCode("P_PICKUP");
							ShipmentPopupData spData = selectPaymentDelivery(shipPopPayment);			 
							LOGGER.debug("selectPaymentDelivery: " + spData);
							oneDeliveryPrice = spData.getPayment();
							total_delivery_price += oneDeliveryPrice;  //공시요금
							
							if(cnt == 0) {
								onePickupPrice = spData.getDeliveryAmount(); //픽업요금
								
								
							}
							else				onePickupPrice = 0;
							
							total_delivery_pickupprice += onePickupPrice;
							onePickupVat = (int) Math.round(onePickupPrice * 0.1);
							LOGGER.debug(">>>vat:" + onePickupVat );
							total_delivery_vatprice += onePickupVat;
							
							if(spData.getRankPrice() > 0) {
								LOGGER.debug("rankPrice:" + spData.getRankPrice());
								oneRankPrice = spData.getRankPrice();
								total_delivery_rankprice += spData.getRankPrice();
							}
							// 쇼피파이 빌링시 오더 정보 저장 후 전송을 위한 정보 
							ship.setMasterCode(mCode);
							
							ShipmentData sData = paymentService.selectPaymentDetail(ship);			// (orderCode,goods,)
					        if ( cnt == 0 ) {
					        	shipPopDataTemp.setDeliveryCompany(lData.getCompany());
					        }
							LOGGER.debug("getDeliveryCompany>>"+ lData.getCompany());
							shipPopDataTemp.setInvoice(sData.getInvoice());
							if (sData.getCourier() != null) {
								shipPopDataTemp.setCourier(sData.getCourier());
							}
							shipPopDataTemp.setOrderCode(sData.getOrderCode());
							shipPopDataTemp.setGoods(sData.getGoods());
							shipPopDataTemp.setEmail(sessionData.getEmail());
							shipPopDataTemp.setMasterCodeList(masterCodeList);
							shipPopDataTemp.setBoxSize(lData.getBoxSize());

							onePrice = oneDeliveryPrice + onePickupPrice + onePickupVat - oneRankPrice;
							
							total_price = total_price + total_delivery_price + total_delivery_pickupprice + total_delivery_vatprice ;
							shipPopDataTemp.setPickupPrice(onePickupPrice); 
							shipPopDataTemp.setPaymentVat(onePickupVat); 

							total_price = total_price - total_delivery_rankprice;
							total_price_sum += total_price;
							shipPopDataTemp.setPayment(oneDeliveryPrice);
							shipPopDataTemp.setRankPrice(oneRankPrice);
							shipPopDataTemp.setPaymentTotal(onePrice);
							//shipPopDataTemp.setPaymentTotal(onePickupPrice + onePickupVat);			// BUG -> tb_shopify_api_applicationchg 에 픽업 금액이 셋팅됨  (20.11.11 이후 실결제금액이 셋팅됨)
							shipPopDataTemp.setTotalAmount(onePrice);
							shipPopDataTemp.setBillingYn(billingYn);
							shipPopDataTemp.setTotalAmountSum(total_price_sum);
							LOGGER.debug(">total_delivery_price:" + total_delivery_price);
							LOGGER.debug(">total_delivery_pickupprice:" + total_delivery_pickupprice);
							LOGGER.debug(">total_delivery_vatprice:" + total_delivery_vatprice);
							LOGGER.debug(">total_delivery_rankprice:" + total_delivery_rankprice);
							LOGGER.debug(">DeliveryCompany:" + lData.getCompany());
							LOGGER.debug(">total_price:" + total_price);
							LOGGER.debug(">total_price_sum:" + total_price_sum);
							
							cnt ++;  //픽업요금 계산 한번만 되도록 
							shipPopData.add(shipPopDataTemp);			// **********************************************
						} // end masterCodeList
				}// end Local
			} 
			//해외특송시 총결제 금액 계산  + 국내택배
			else { 
				String combineChk = "";
				LOGGER.debug("#################[NA]###############");
				contents  = "일반주문 마스터코드: ";
				for (String mCode : masterCodeList) {
					LOGGER.debug("--------------------------------------------------------------------");
					LOGGER.debug("mcode:" + mCode);
					total_delivery_rankprice = 0; // 할인율 초기화  (조한두 20.11.02 N개 결재시 이전 할인율이 다른건에도 적용됨)
					total_delivery_price = 0; 		 // 초기화 
					total_price = 0;						// 초기화 
					contents  += mCode +",";
					shipPopPayment.setMasterCode(mCode);
					shipPopPayment.setBoxSize("ZZ"); //0처리를위한 임의의 값
					LOGGER.debug("###############popPaymentPaymentProc=pay_id===total_price=>/" + total_price);
					ShipmentPopupData spData = selectPaymentDelivery(shipPopPayment);		 
					LOGGER.debug("selectPaymentDelivery: " + spData);
					if("B010030".equals(spData.getDeliveryCompany()))
					{
						isMonitor = true;
						company = "판토스";
					}
					else if("B010020".equals(spData.getDeliveryCompany()))
					{
						isMonitor = true;
						company = "롯데글로벌로지스";
					}
					else if("B010040".equals(spData.getDeliveryCompany()))
					{
						isMonitor = true;
						company = "ACI Express";
					}
					combineChk = spData.getCombineCode();
					LOGGER.debug("###############popPaymentPaymentProc=pay_id===combineChk=>/" + combineChk);
					if (combineCnt == 0) {
						total_delivery_price = spData.getPayment();
						LOGGER.debug("total_delivery_price:" + total_delivery_price );
						if(spData.getRankPrice() > 0) {
							LOGGER.debug("rankPrice(%):" + spData.getRankPrice());
							total_delivery_rankprice = spData.getRankPrice();
						}
						total_price =  total_delivery_price;
					}
					if ("Y".equals(combineChk)) {
						combineCnt++;
					}
					total_price = total_price - total_delivery_rankprice;
					total_price_sum += total_price;
					LOGGER.debug(">total_delivery_price:" + total_delivery_price);
					LOGGER.debug(">total_delivery_rankprice:" + total_delivery_rankprice);
					LOGGER.debug(">total_price:" + total_price);
					LOGGER.debug(">total_price_sum:" + total_price_sum);
			        // 쇼피파이 빌링시 오더 정보 저장 후 전송을 위한 정보 
					ship.setMasterCode(mCode);
					
					ShipmentData sData = paymentService.selectPaymentDetail(ship);
					
					ShipmentPopupData shipPopDataTemp = new ShipmentPopupData() ;
					shipPopDataTemp.setInvoice(sData.getInvoice());
					if (sData.getCourier() != null) {
						shipPopDataTemp.setCourier(sData.getCourier());
					}
					
					shipPopDataTemp.setPayment(total_delivery_price);
					if ( "DO".equals(viewShipPopData.getPaymentCode()) ) {
						isMonitor = true;
						company = "롯데국내택배";
						shipPopDataTemp.setPaymentVat( (int)(sData.getPayment() * 0.1));
						shipPopDataTemp.setPaymentCode(viewShipPopData.getPaymentCode());
					}
					shipPopDataTemp.setMasterCode(mCode);
					shipPopDataTemp.setOrderCode(sData.getOrderCode());
					shipPopDataTemp.setGoods(sData.getGoods());
					shipPopDataTemp.setEmail(sessionData.getEmail());
					shipPopDataTemp.setMasterCodeList(masterCodeList);
					//shipPopDataTemp.setGoodsCode(sData.getGoodsCode());
					//shipPopDataTemp.setFulId(sData.getFulId());
					shipPopDataTemp.setBillingYn(billingYn);
					shipPopDataTemp.setPaymentTotal(total_price);
					shipPopDataTemp.setTotalAmount(total_price);
					
					shipPopData.add(shipPopDataTemp);			// **********************************************
				}
				
			}
		} else {
			LOGGER.debug("###############[ERROR]################ popPaymentPaymentProc=session.Email=>/" + sessionData.getEmail());
		}
		LOGGER.debug("ARRAY SIZE:"  + shipPopData.size());
		
		
		LOGGER.debug(">>>>>>>> PAYPAL/BANK Process >>>>>>>>>>>>>>>>>>");
		payChk = 1;
		int retChk = 0;
		int chk = 1;
		boolean ret = true;
		
		if("paypal".equals(payMethod))
		{	
			LOGGER.debug(">>>>>>>> Paypal Process >>>>>>>>>>>>>>>>>>");
			LOGGER.debug(">total_price_sum:" + total_price_sum);
			for(ShipmentPopupData data :shipPopData )
				data.setTotalAmountSum(total_price_sum);
			ShipmentPopupData temp = shipPopData.get(0);// 첫번째의 sum 금액으로 결제 요청 한다. 
			LOGGER.debug("paypal TotalAmountSum:"  + temp.getTotalAmountSum());
			returnspData = shopifyProcBilling(temp, req, sess);		// ******************************************************************
			if(returnspData == null || returnspData.getApiReturnChk() == false )
			{
				mav.addObject("result", 0);
				mav.addObject("errCode", false);
				mav.addObject("errMsg", "실패");
				return mav;
			}
			else 
			{
				// N 개 결제시 컨펌 URL 은 합계금액으로 1번만 타고 각각의 금액을 price_unit 에 기록 한다. !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				String apiConfirmationUrl = returnspData.getApiConfirmationUrl();
				if (apiConfirmationUrl != null) {
					apiConfirmationUrl = apiConfirmationUrl.replaceAll("\\\\", "");
					mav.addObject("confirmUrl", apiConfirmationUrl);
				}
		    }
		}
//		cnt = 0;
		//국내택배 픽업시업데이트
		if(localDataList != null) {
			
//			for(ShipmentPopupDataLocalData lData : localDataList) {
				for(ShipmentPopupData data :shipPopData )
				{
					LOGGER.debug("###############popPaymentPaymentProc["+payMethod+"]=ND/"+data.getMasterCode());
//				    if(cnt == 0) {
//					data.setLocalCompany(lData.getCompany());
//					data.setLocalBoxSize(lData.getBoxSize());
//					data.setLocalPrice(lData.getPrice());
//					
//					
//				    }
//				    data.setLocalMcode(lData.getMcode());
//				    cnt++;
	 				if("bank".equals(payMethod)) 
						chk = popPaymentCashProcess(data, chk);
					else if("paypal".equals(payMethod))
						chk = popPaymentPaymentProcess(data, chk, returnspData);	
					if(chk == 0) {
						ret = false;
					}
//					sData.setDeliveryCompany(null);
					
				}// end for	
//			}
			
			if(!ret) {			//실패처리
				chk = 0;
				}
			}
		//해외특송시 업데이트
		else {
			for(ShipmentPopupData data :shipPopData )
			{
				LOGGER.debug("###############popPaymentPaymentProc["+payMethod+"]=NA/"+data.getMasterCode());
		 		if("bank".equals(payMethod))
					chk = popPaymentCashProcess(data, chk);
				else if("paypal".equals(payMethod))
					chk = popPaymentPaymentProcess(data, chk, returnspData);	
				if(chk == 0) {
					ret = false;
				}
			}
			if(!ret) {			//실패처리
				chk = 0;
			}
		}
		if(ret) 
		{
			//	shopify tracking call
			/*
			 * boolean apiResult = true; if("bank".equals(payMethod)) apiResult =
			 * paymentPopupRestService.shopifyPostOrderIdFullist(masterCodeList);
			 */
			if (ret == true) {
				mav.addObject("result", chk);
				mav.addObject("errCode", true);
				mav.addObject("errMsg", "성공");	
			} else {
				mav.addObject("result", chk);
				mav.addObject("errCode", false);
				mav.addObject("errMsg", "실패");
			}
		}
		else
		{
			mav.addObject("result", chk);
			mav.addObject("errCode", false);
			mav.addObject("errMsg", "실패");
		}
		// 모니터링 이메일 
  	   if(isMonitor == true && isMailSend == true)
		   sendEmailMonitoring(isReal, company, contents);
 
  	   // 로컬에서 롤백하여 계산 금액을 LOG 로 보면서 테스트시 TRUE 로 놓고 사용할것 
  	   /*
  	   boolean flag = true;
  	   if ( flag == true ) {
  		   throw new RuntimeException("redo");
  	   }
  	   */
		return mav;
	}
	
	/**
	 * 배송 > 팝업 > 결제승인 팝업
	 * 
	 * @return ModelAndView
	 */
	public ModelAndView popPaymentPopOpen(ShipmentPopupData shipPopData, Model model, HttpServletRequest req, HttpServletResponse res, HttpSession sess) {
		ModelAndView mav = new ModelAndView("jsonView");
		ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		ShipmentData ship = new ShipmentData();
		ShipmentPopupData shipPopPayment = new ShipmentPopupData();
		shipPopData.setLocale(sessionData.getLocale());
		shipPopData.setEmail(sessionData.getEmail());
		shipPopPayment.setEmail(sessionData.getEmail());
		shipPopPayment.setLocale(sessionData.getLocale());
		ship.setLocale(sessionData.getLocale());
		ship.setEmail(sessionData.getEmail());

		String[] masterCodeList = null;
		String masterCode = shipPopData.getMasterCode();
		ship.setMasterCode(masterCode);

		
		if (masterCode != null) {
			if (masterCode.indexOf(",") > 0) {
				masterCodeList = masterCode.split(",");
				ship.setMasterCodeList(masterCodeList);
				ship.setMasterCode("");

				masterCode = "";

			} else {
				masterCode = shipPopData.getMasterCode();
				ship.setMasterCodeList(masterCodeList);
				ship.setMasterCode(masterCode);
			}
		} else {
			masterCodeList = shipPopData.getMasterCodeList();
			masterCode = "";
			ship.setMasterCodeList(masterCodeList);
			ship.setMasterCode(masterCode);
		}
		
		String confirmUrl = "";
		if (masterCodeList != null) {
			for (String mCode : masterCodeList) {
				shipPopPayment.setMasterCode(mCode);
				ShipmentPopupData spData = selectPaymentPopAccept(shipPopPayment);
				if("".equals(confirmUrl)) {
					confirmUrl = spData.getApiConfirmationUrl();
				}else {
					confirmUrl = confirmUrl+"|"+spData.getApiConfirmationUrl();
				}
			}
		} else {
			shipPopPayment.setMasterCode(masterCode);
			ShipmentPopupData spData = selectPaymentPopAccept(shipPopPayment);
			confirmUrl = spData.getApiConfirmationUrl();
		}
		
		
		mav.addObject("confirmUrl", confirmUrl);
		mav.addObject("errCode", true);
		mav.addObject("errMsg", "성공");
		return mav;
	}
	
	/**
	 * 배송 > 팝업 > 결제처리 > 내부 메소드 : paypal
	 * 
	 * @return int
	 */
	private int popPaymentPaymentProcess(ShipmentPopupData shipPopData, int chk, ShipmentPopupData payData) {
		// ******************************************************************************************************
		String pay_id = "pay" + util.getDateElement("full").substring(2, 12) + util.randomNumber(4, 4);
		// ******************************************************************************************************
//		if (sData.getCourier() != null) {
//			shipPopData.setCourier(sData.getCourier());
//		}
//		shipPopData.setInvoice(sData.getInvoice());
		shipPopData.setState("A010020");		// 결제 완료 
		shipPopData.setPayState("Y");
		shipPopData.setPayId(pay_id);
		if (shipPopData.getPaymentCode() == null) {
			shipPopData.setPaymentCode("NA");
		}
//		String deliveryCompany = sData.getDeliveryCompany();
//
//		shipPopData.setDeliveryCompany(deliveryCompany);
		chk = updatePopPaymentPaymentDelivery(shipPopData);		// tb_delivery ******************************************(state,state_group,box_size---master_code)
		//결제상태 반영
		chk = updatePopPaymentPaymentDeliveryPayment(shipPopData);		// tb_delivery_payment **************************** (pay_state, payment_code,payment, payment_date,courier,pay_id,payment_vat --- master_code)

		//로컬픽업 반영 부분..
		// 조한두 (2020.04.23) 롯데 로지스 집화 예약 번호 테스트 용 
		if (shipPopData.getDeliveryCompany() != null) {
			//국내택배 반영
			shipPopData.setPaymentCode("ND");
			shipPopData.setPayment(shipPopData.getTotalAmount()); // 조한두 (20.04.28)
			chk = insertPopPaymentPaymentDeliveryPayment(shipPopData);				// tb_delivery_payment (ND) *************************(payment_code='ND',---master_code)
			String keyFull = util.getDateElement("full");
			String localCode = "local" + keyFull + util.getRandomString(5); //국내택배 api 코드 -> api를 통하여 받도록 변경 예정 임시로 임의수
			shipPopData.setLocalCode(localCode);
			shipPopData.setStateCode("00"); //국내택배시 접수 -> api를 통하여 상태변경처리 예정
//			shipPopData.setDeliveryCode(deliveryCompany);

			if (chk == 1) {
				chk = insertPopPaymentPaymentDeliveryLocal(shipPopData);			// tb_delivery_local(local_code,invoice,delivery_company,state_code)
				chk = insertPopPaymentPaymentDeliveryLocalData(shipPopData); // tb_delivery_local_data (local_code,master_code)
				}
		}
		LOGGER.debug("-----------insertPaymentPaymentApi<paypal>");
		LOGGER.debug("masterCode:" + shipPopData.getMasterCode());
		LOGGER.debug("totalAmount:" + shipPopData.getTotalAmount());
		payData.setApiPayId(pay_id);
 		payData.setMasterCode(shipPopData.getMasterCode());			// 조한두 : 히스토리 추적용 
		payData.setPriceUnit(shipPopData.getTotalAmount());				// 조한두 : 히스토리 추적용 
		insertPaymentPaymentApi(payData);		//결제요청 json 데이터 저장   tb_shopify_api_applicationchg  ****************************(id,pay_id,name,client_id,status,price,test,return_url,confirmation_url,create_date,return_json,price_usd)
		shipPopData.setPayCompany("Shopify");
		shipPopData.setPayType("PayPal");
		shipPopData.setPayId(pay_id);
		shipPopData.setPayMethod("paypal");
		shipPopData.setPriceUsd(payData.getPriceUsd()); // 조한두 
		chk = insertPaymentPayInfo(shipPopData);	// tb_pay_info *************************************************************(pay_id,master_code,pay_company,pay_type,total_amount,remain_amount,cancel_amount,pay_method,financename,cardinstallmonth,cardointyn,pay_yn,price_usd,billing_yn)

		return chk;
	}
	/**
	 * 배송 > 팝업 > 결제처리 > 내부 메소드 : cash
	 * 
	 * @return int
	 */
	private int popPaymentCashProcess(ShipmentPopupData shipPopData, int chk) {
		// ******************************************************************************************************
		String pay_id = "pay" + util.getDateElement("full").substring(2, 12) + util.randomNumber(4, 4);
		// ******************************************************************************************************
		/*
		 * if (sData.getCourier() != null) { shipPopData.setCourier(sData.getCourier());
		 * }
		 */
//		shipPopData.setInvoice(sData.getInvoice());
		shipPopData.setState("A010020");		// 결제 완료 
		shipPopData.setPayState("Y");
		shipPopData.setPayId(pay_id);
		if (shipPopData.getPaymentCode() == null) {
			shipPopData.setPaymentCode("NA");
		}
//		String deliveryCompany = sData.getDeliveryCompany();
//
//		shipPopData.setDeliveryCompany(deliveryCompany);
		chk = updatePopPaymentPaymentDelivery(shipPopData);		// tb_delivery
		//결제상태 반영
		chk = updatePopPaymentPaymentDeliveryPayment(shipPopData);		// tb_delivery_payment ****************************
		
		//로컬픽업 반영 부분..
		// 조한두 (2020.04.23) 롯데 로지스 집화 예약 번호 테스트 용 
		if (shipPopData.getDeliveryCompany() != null) {
			LOGGER.debug(">>>>>>LOCAL>>>>>>>>");
			//국내택배 반영
			shipPopData.setPaymentCode("ND");
			shipPopData.setPayment(shipPopData.getTotalAmount()); // 조한두 (20.04.28)
			chk = insertPopPaymentPaymentDeliveryPayment(shipPopData);				// tb_delivery_payment (ND) *************************
			String keyFull = util.getDateElement("full");
			String localCode = "local" + keyFull + util.getRandomString(5); //국내택배 api 코드 -> api를 통하여 받도록 변경 예정 임시로 임의수
			shipPopData.setLocalCode(localCode);
			shipPopData.setStateCode("00"); //국내택배시 접수 -> api를 통하여 상태변경처리 예정
//			shipPopData.setDeliveryCode(deliveryCompany);

			if (chk == 1) {
				chk = insertPopPaymentPaymentDeliveryLocal(shipPopData);
				chk = insertPopPaymentPaymentDeliveryLocalData(shipPopData);
			}
		}
		LOGGER.debug("-----------insertPaymentPaymentApi<cash>");
		LOGGER.debug("masterCode:" + shipPopData.getMasterCode());
		LOGGER.debug("totalAmount:" + shipPopData.getTotalAmount());
	
		
		shipPopData.setPayCompany("SOLUGATE");
		shipPopData.setPayType("Cash");
		shipPopData.setPayId(pay_id);
		shipPopData.setPayMethod("bank");
		insertPaymentCashApi(shipPopData);		//결제요청 json 데이터 저장   tb_shopify_api_applicationchg  ****************************
		chk = insertPaymentPayInfo(shipPopData);	// tb_pay_info *************************************************************

		// 배송 상태로 최종 변경 
 		shipPopData.setState("A020020");
		shipPopData.setStateGroup("A020000");
     	chk = paymentMapper.updatePopPaymentPaymentDelivery(shipPopData);
	 
		return chk;
	}
	


	/**
	 * 결제저장 > 내부 메소드
	 * 
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

}
