package com.shopify.order.popup;

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
import com.shopify.mapper.OrderMapper;
import com.shopify.mapper.SettingMapper;
import com.shopify.order.OrderData;
import com.shopify.order.OrderDetailSkuData;
import com.shopify.setting.SettingBoxData;
import com.shopify.setting.SettingSenderData;
import com.shopify.shipment.ShipmentData;
import com.shopify.shop.ShopData;


/**
* 주문 팝업 Service
* @author : 심윤
* @since  : 2020-02-27
* @desc   : 주문 합배송 서비스 
*/

@Service
@Transactional
public class OrderCombineService extends OrderPopupService {
	private Logger LOGGER = LoggerFactory.getLogger(OrderCombineService.class);
	
	@Autowired private UtilFn util;
	@Autowired private OrderMapper orderMapper;
	@Autowired private SettingMapper settingMapper;
	

    /**
     * 주문 > 합배송 체크 
     * @param OrderPopupData
	 * @param HttpSession
     * @return Map<String, Object>
     */
	public Map<String, Object> orderCombineCheck(OrderPopupData orderPopupData, HttpSession sess){
		
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		
		String arrOrderCode = orderPopupData.getArrOrderCode();
		String ckOrderCode = arrOrderCode.replaceAll(",", "").trim();
		String arrShopIdx = orderPopupData.getIdx();
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if("".equals(ckOrderCode)) {
			map.put("list", "");
			map.put("checkCode", "error");
		} else {
			String[] arrOrder = arrOrderCode.split(",");
			String[] arrShop = arrShopIdx.split(",");
			
			List<OrderData> orderIdxList = new ArrayList<OrderData>();
			
			for (int i = 0; i < arrOrder.length; i++) {
				String code = arrOrder[i].trim();
				String idx = arrShop[i].trim();
				
				if(!"".equals(code) && !"".equals(idx)) {
					OrderData od = new OrderData();
					od.setEmail(email);
					od.setOrderCode(code);
                    od.setShopIdx(Integer.parseInt(idx));
                    
                    orderIdxList.add(od);
				}
			}
			
			// 검증 데이터 받아 오기 
			List<OrderData> orderCodeList = orderMapper.selectCombineCheckList(orderIdxList);
			
			String zipcode = "";
			String country = "";
			int index = 0;
			int weight = 0;
			boolean check = true;
			
			for (OrderData orderData : orderCodeList) {
				if(index == 0) {
					zipcode = orderData.getBuyerZipCode(); 
					country = orderData.getBuyerCountryCode();
				}
				
				if(!zipcode.equals(orderData.getBuyerZipCode())) { //우편번호 체크 
					check = false;
				}
				
				if(!country.equals(orderData.getBuyerCountryCode())) {//국가 체크
					check = false;
				}
				
				weight += orderData.getWeight();
				
				index++;
			}
			
			if(check == false) { // 주소 체크 
				map.put("checkCode", "addr");
			} else if (weight > 30000) { // 중량 체크 
				map.put("checkCode", "weight");
			} else {
				map.put("checkCode", "ok");
			}
			
			map.put("list", orderCodeList);
			map.put("arrOrderCode", arrOrderCode);
			map.put("arrShopIdx", arrShopIdx);
		}

		return map;
	}
	
	/**
     * 주문 > 합배송 주소 정보 받아 오기 
     * @param OrderPopupData
	 * @param HttpSession
     * @return Map<String, Object>
     */
	public Map<String, Object> orderCombineAddr(OrderPopupData orderPopupData, HttpSession sess){
		
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		
		String arrOrderCode = orderPopupData.getArrOrderCode();
		String ckOrderCode = arrOrderCode.replaceAll(",", "").trim();
		String arrShopIdx = orderPopupData.getArrShopIdx();
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if("".equals(ckOrderCode)) {
			map.put("arrOrderCode", arrOrderCode);
			map.put("arrShopIdx", arrShopIdx);
			map.put("address", "");
			map.put("seller", "");
			map.put("status", "no");
		} else {
			///////////////////////////////////////////////////////////////////////////////////////////////
			// 셀러 기본 주소 받아 오기
			///////////////////////////////////////////////////////////////////////////////////////////////
			SettingSenderData settingSeller = new SettingSenderData();
			settingSeller.setEmail(email);
			
			String[] arrOrder = arrOrderCode.split(",");
			String[] arrShop = arrShopIdx.split(",");
			
			LOGGER.debug("arrOrderCode : " + arrOrderCode);
			LOGGER.debug("arrShop : " + arrShopIdx);
			
			ShipmentData address = new ShipmentData();
			
			OrderData settingOrder = new OrderData();
			settingOrder.setShopIdx(Integer.parseInt(arrShop[0]));
			settingOrder.setOrderCode(arrOrder[0]);
			
			OrderData orderAddr = orderMapper.selectOneOrder(settingOrder);
			if (orderAddr != null) {
				try {
					try {
						address.setBuyerFirstname(util.nullToEmpty(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, orderAddr.getBuyerFirstname())));
					} catch (Exception e) {
						e.getStackTrace();
					}
					
					try {
						address.setBuyerLastname(util.nullToEmpty(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, orderAddr.getBuyerLastname())));
					} catch (Exception e) {
						e.getStackTrace();
					}
					
					try {
						address.setBuyerPhone(util.nullToEmpty(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, orderAddr.getBuyerPhone())));
						String[] arrPhoneNumber = address.getBuyerPhone().split("-");
						if(arrPhoneNumber.length == 4) {
							address.setBuyerPhone01(arrPhoneNumber[0]);
							address.setBuyerPhone02(arrPhoneNumber[1]);
							address.setBuyerPhone03(arrPhoneNumber[2]);
							address.setBuyerPhone04(arrPhoneNumber[3]);
						}
					} catch (Exception e) {
						e.getStackTrace();
					}

					address.setBuyerEmail(util.nullToEmpty(orderAddr.getBuyerEmail()));
					address.setBuyerCountryCode(util.nullToEmpty(orderAddr.getBuyerCountryCode()));
					address.setBuyerCountry(util.nullToEmpty(orderAddr.getBuyerCountry()));
					address.setBuyerCity(util.nullToEmpty(orderAddr.getBuyerCity()));
					address.setBuyerProvince(util.nullToEmpty(orderAddr.getBuyerProvince()));
					address.setBuyerZipCode(util.nullToEmpty(orderAddr.getBuyerZipCode()));
					address.setBuyerAddr1(util.nullToEmpty(orderAddr.getBuyerAddr1()));
					address.setBuyerAddr2(util.nullToEmpty(orderAddr.getBuyerAddr2()));
					
					address.setShopIdx(orderAddr.getShopIdx());
					address.setOrderIdx(orderAddr.getOrderIdx());
					address.setOrderCode(orderAddr.getOrderCode());
					address.setOrderDate(orderAddr.getOrderDate());
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
			
			// 기본 출고지 정보 받아 오기 
			SettingSenderData sellerDefaultAddr = settingMapper.selectSenderDefault(settingSeller);
			
			if (sellerDefaultAddr != null) {
				try {
					address.setSellerName(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sellerDefaultAddr.getName()));
				} catch (Exception e) {
					e.getStackTrace();
				}
				try {
					address.setSellerPhone(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sellerDefaultAddr.getPhoneNumber()));
					String[] arrPhoneNumber = address.getSellerPhone().split("-");
					if(arrPhoneNumber.length == 4) {
						address.setSellerPhone01(arrPhoneNumber[0]);
						address.setSellerPhone02(arrPhoneNumber[1]);
						address.setSellerPhone03(arrPhoneNumber[2]);
						address.setSellerPhone04(arrPhoneNumber[3]);
					}
				} catch (Exception e) {
					e.getStackTrace();
				}
				
				address.setSelectSender(sellerDefaultAddr.getSenderIdx());
				address.setSellerZipCode(sellerDefaultAddr.getZipCode());
				address.setSellerAddr1(sellerDefaultAddr.getAddr1());
				address.setSellerAddr2(sellerDefaultAddr.getAddr2());
				address.setSellerAddr1Ename(sellerDefaultAddr.getAddr1Ename());			//영문 주소 추가 [2020.05.25 YR]
				address.setSellerAddr2Ename(sellerDefaultAddr.getAddr2Ename());			//영문 주소 추가 [2020.05.25 YR] 
				address.setSellerProvince(sellerDefaultAddr.getProvince());				//영문 주소 추가 [2020.05.25 YR]
				address.setSellerCity(sellerDefaultAddr.getCity());						//영문 주소 추가 [2020.05.25 YR]
				//address.setSellerCity("");
				address.setSellerCountryCode("");
				address.setSellerCountry("");
			}
			
			// 셀러 주소 리스트
			List<SettingSenderData> list = settingMapper.selectSender(settingSeller);
			List<SettingSenderData> seller = new ArrayList<SettingSenderData>();
			for(SettingSenderData item : list){
				try {
					item.setName(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, item.getName()));
				} catch (Exception e) {
					e.getStackTrace();
				}
				
				try {
					item.setPhoneNumber(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, item.getPhoneNumber()));
					String[] arrPhoneNumber = item.getPhoneNumber().split("-");
					if(arrPhoneNumber.length == 4) {
						item.setPhoneNumber01(arrPhoneNumber[0]);
						item.setPhoneNumber02(arrPhoneNumber[1]);
						item.setPhoneNumber03(arrPhoneNumber[2]);
						item.setPhoneNumber04(arrPhoneNumber[3]);
					}
				} catch (Exception e) {
					e.getStackTrace();
				}
				
				seller.add(item);
			}
			

			///////////////////////////////////////////////////////////////////////////////////////////////
			// 포장재정보 받아 오기
			///////////////////////////////////////////////////////////////////////////////////////////////
			// 기본 박스 정보 받아 오기 
			SettingBoxData settingBox = new SettingBoxData();
			settingBox.setEmail(email);
			
			// 박스 정보 
			List<SettingBoxData> boxList = settingMapper.selectBox(settingBox);
			SettingBoxData sellerDefaultBox = settingMapper.selectBoxDefault(settingBox);
			
			if (sellerDefaultBox != null) {
				address.setSelectBox(sellerDefaultBox.getBoxIdx());
				address.setBoxType(sellerDefaultBox.getBoxType());
				
				address.setBoxHeight(sellerDefaultBox.getBoxHeight());
				address.setBoxLength(sellerDefaultBox.getBoxLength());
				address.setBoxWidth(sellerDefaultBox.getBoxWidth());
				address.setBoxUnit(sellerDefaultBox.getBoxUnit());
				
				address.setBoxWeight(sellerDefaultBox.getBoxWeight());
				address.setWeightUnit(sellerDefaultBox.getWeightUnit());
			}

			// 주문 관세 정보 받아 오기
			OrderDetailSkuData orderDetail = new OrderDetailSkuData();
			orderDetail.setShopIdx(Integer.parseInt(arrShop[0]));
			orderDetail.setOrderCode(arrOrder[0]);
			orderDetail.setArrOrderCode(arrOrder);
			orderDetail.setArrShopIdx(arrShop);
			
			List<OrderDetailSkuData> skuList = orderMapper.selectOrderDetailList(orderDetail);
			
			map.put("arrOrderCode", arrOrderCode);
			map.put("arrShopIdx", arrShopIdx);
			map.put("address", address);
			map.put("seller", seller);
			map.put("boxList", boxList);
			map.put("skuList", skuList);
			map.put("status", "error");
		}

		return map;
	}
	
	/**
     * 주문 > 합배송 배송 서비스  
     * @param OrderPopupData
	 * @param HttpSession
     * @return Map<String, Object>
     */
	public Map<String, Object> orderCombineDeliveryService(OrderPopupData orderPopupData, HttpSession sess){
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		String locale = sd.getLocale();
		
		String nowDate = util.getDateElement("today");
		String nationCode = orderPopupData.getNationCode();
		int weight = orderPopupData.getWeight();
		/** 고객이 선택한 캐리어 적용  [yr 2020.05.26]  **/
		String[] arrOrderCode = orderPopupData.getArrOrderCode().split(",");
		String[] arrShopIdx = orderPopupData.getArrShopIdx().split(",");
		int shopIdx = Integer.parseInt(arrShopIdx[0]);
			
		ShipmentData setting = new ShipmentData(); 
		setting.setOrderCode(arrOrderCode[0]);
		setting.setEmail(email); setting.setShopIdx(shopIdx);
		 
		ShipmentData address = orderMapper.selectCombineCarrier(setting);

		OrderCourierData courier;
		
		String status = "no";
		if(address != null) {
			OrderCourierData input = new OrderCourierData();
			input.setShippingLineCode(address.getShippingLineCode());
			input.setShippingLineName(address.getShippingLineName());
			input.setMasterCode(address.getMasterCode());
			//courier = orderMapper.selectPayment(input);
			courier = new OrderCourierData();
			courier.setEmail(email);
			courier.setLocale(locale);
			courier.setNationCode(nationCode);
			courier.setWeight(weight);
			courier.setNowDate(nowDate);
			
			status = "yes";
		}else {
			courier = new OrderCourierData();
		}
	    
		/** ---끝-----  [yr 2020.05.26]  **/
	    
	    List<OrderCourierData> list = new ArrayList<OrderCourierData>();
	    
	    Map<String, Object> map = new HashMap<String, Object>();
	    
	    if(nationCode == null || "".equals(nationCode)) {
	    	map.put("status", "no");
		} else {
			list = orderMapper.selectCourierList(courier); //특송업체 리스트
			map.put("list", list);
			map.put("status", "ok");
			map.put("address", address);
			map.put("courier", courier);
		}

		map.put("list", list);
		map.put("courier", courier);
		map.put("address", address);
		
		return map;
	}
	
	/**
     * 주문 > 합배송 배송 서비스  
     * @param OrderPopupData
	 * @param HttpSession
     * @return Map<String, Object>
     */
public int orderCombineDeliveryProc(Map<String, String> request, HttpSession sess){
		
		ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String sellerName = sessionData.getShopName();
		if(sellerName == null) sellerName = sessionData.getEmail();
		
		String[] arrIdx = request.get("arrShopIdx").split(",");
		String[] arrCode = request.get("arrOrderCode").split(",");
		
		int shopIdx = Integer.parseInt(arrIdx[0]);
		// 새로운 코드 생성
		//String orderCode = util.getDateElement("full").substring(2,12)+util.randomNumber(4,4);
		String orderCode = arrCode[0];

		// 암호화 시작
		String buyerFirstname = request.get("buyerFirstname");
		String buyerLastname = request.get("buyerLastname");
		System.out.println("####orderCombineDeliveryProc###############request.get(\"arrGoodsCode\")==>"+request.get("arrGoodsCode"));
		System.out.println("####orderCombineDeliveryProc###############buyerLastname==>"+buyerLastname);
		
		
		String sellerPhone = request.get("sellerPhone01") + "-" + request.get("sellerPhone02") + "-" + request.get("sellerPhone03") + "-" + request.get("sellerPhone04");
		try {
			sellerPhone = util.getAESEncrypt(SpConstants.ENCRYPT_KEY, sellerPhone);  
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		String buyerPhone = request.get("buyerPhone");
		try {
			buyerPhone = util.getAESEncrypt(SpConstants.ENCRYPT_KEY, buyerPhone);  
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		sellerName = util.getAESEncrypt(SpConstants.ENCRYPT_KEY, sellerName);  
		buyerFirstname = util.getAESEncrypt(SpConstants.ENCRYPT_KEY, buyerFirstname); 
		buyerLastname = util.getAESEncrypt(SpConstants.ENCRYPT_KEY, buyerLastname); 
		
		/* ######################################
		 * 기본 배송 정보 생성
		 * ######################################
		 */
		OrderDeliveryData deliveryData = new OrderDeliveryData();
		deliveryData.setShopIdx(shopIdx);
		deliveryData.setOrderCode(orderCode);
		deliveryData.setSelectSender(Integer.parseInt(request.get("selectSender")));
		deliveryData.setSellerName(sellerName);
		deliveryData.setSellerPhone(sellerPhone);
		deliveryData.setSellerCountryCode(request.get("sellerCountryCode"));
		deliveryData.setSellerCountry(request.get("sellerCountry"));
		deliveryData.setSellerCity(request.get("sellerCity"));
		deliveryData.setSellerZipCode(request.get("sellerZipCode"));
		deliveryData.setSellerCountryCode(request.get("sellerCountryCode"));
		deliveryData.setSellerAddr1(request.get("sellerAddr1"));
		deliveryData.setSellerAddr2(request.get("sellerAddr2"));
		
		deliveryData.setBuyerPhone(buyerPhone);
		deliveryData.setBuyerFirstname(buyerFirstname);
		deliveryData.setBuyerLastname(buyerLastname);
		deliveryData.setBuyerEmail(request.get("buyerEmail"));
		deliveryData.setBuyerCountryCode(request.get("buyerCountryCode"));
		deliveryData.setBuyerCity(request.get("buyerCity"));
		deliveryData.setBuyerProvince(request.get("buyerProvince"));
		deliveryData.setBuyerProvinceCode(request.get("buyerProvinceCode"));
		deliveryData.setBuyerZipCode(request.get("buyerZipCode"));
		deliveryData.setBuyerAddr1(request.get("buyerAddr1"));
		deliveryData.setBuyerAddr2(request.get("buyerAddr2"));
		deliveryData.setHscode(request.get("hscode"));
		
		String selectBox = request.get("selectBox");
		if(selectBox != null && !"".equals(selectBox)){
			deliveryData.setSelectBox(Integer.parseInt(selectBox));
		}
		deliveryData.setBoxType(request.get("boxType"));
		deliveryData.setBoxLength(Integer.parseInt(request.get("boxLength")));
		deliveryData.setBoxWidth(Integer.parseInt(request.get("boxWidth")));
		deliveryData.setBoxHeight(Integer.parseInt(request.get("boxHeight")));
		deliveryData.setBoxUnit(request.get("boxHeight"));
		deliveryData.setWeight(Double.parseDouble(request.get("weight")));
		deliveryData.setWeightUnit(request.get("weightUnit"));
		deliveryData.setTotalWeight(request.get("totalWeight"));
		
		deliveryData.setArrShopIdx(request.get("arrShopIdx"));
		deliveryData.setArrOrderCode(request.get("arrOrderCode"));
		deliveryData.setOrderDate(request.get("orderDate"));
		deliveryData.setCombineCode("Y");
		deliveryData.setCombineChk("Y");
		
		// 기본 배송 정보 생성  
		Map<String, Object> delivery =  createDeliveryData(deliveryData);
		String masterCode = (String) delivery.get("masterCode");
		int orderIdx = (int) delivery.get("orderIdx");
		int chk = (int) delivery.get("chk");
		
		
		/* ######################################
		 * 관세 정보 생성
		 * ######################################
		 */
		
		String[] arrGoodsItemId = request.get("arrGoodsItemId").split(",");
		String[] arrSkuIdx = request.get("arrSkuIdx").split(",");
		String[] arrGoodsCode = request.get("arrGoodsCode").split(",");
		String[] arrGoods = request.get("arrGoods").split(",");
		String[] arrGoodsSku = request.get("arrGoodsSku").split(",");
		String[] arrOrigin = request.get("arrOrigin").split(",");
		String[] arrHscode = request.get("arrHscode").split(",");
		String[] arrGoodsType = request.get("arrGoodsType").split(",");
		String[] arrWeightUnit = request.get("arrWeightUnit").split(",");
		String[] arrWeight = request.get("arrWeight").split(",");
		String[] arrUnitCost = request.get("arrUnitCost").split(",");
		String[] arrQuantity = request.get("arrQuantity").split(",");
		
		
		OrderDeliverySkuData[] ods = new OrderDeliverySkuData[arrGoodsCode.length];
		String[] brand = new String[ods.length];
	    String[] itemLink = new String[ods.length];
	    
	     ods = orderMapper.selectCombineOrderItemDetail(arrCode);
		
		for(int i=0 ; i < ods.length; i++) {
			brand[i] = ods[i].getBrand() ;
			itemLink[i] = ods[i].getItemLink();
		}

        // 저장용 포장재 정보
        int boxLength = Integer.parseInt(request.get("boxLength"));
        int boxWidth = Integer.parseInt(request.get("boxWidth"));
        int boxHeight = Integer.parseInt(request.get("boxHeight"));
        String boxUnit = request.get("boxUnit");
        String boxType = request.get("boxType");
        int payWeight = 0;
        for (int i = 0; i < arrGoodsCode.length; i++) {
        	OrderDeliverySkuData skuData = new OrderDeliverySkuData();
        	
        	if(arrGoodsItemId != null) {
        		skuData.setGoodsItemId(arrGoodsItemId[i]);
        	}
        	skuData.setMasterCode(masterCode); 
        	skuData.setOrderIdx(orderIdx);
        	skuData.setOrderCode(orderCode);
        	skuData.setGoodsCode(arrGoodsCode[i]);
        	skuData.setGoods(arrGoods[i]);
        	skuData.setGoodsSku(arrGoodsSku[i]);
        	skuData.setOrigin(arrOrigin[i]);
        	skuData.setHscode(arrHscode[i]);
        	skuData.setGoodsType(arrGoodsType[i]);
        	skuData.setWeight(arrWeight[i]);
        	skuData.setWeightUnit(arrWeightUnit[i]);
        	skuData.setBoxLength(boxLength);
        	skuData.setBoxWidth(boxWidth);
        	skuData.setBoxHeight(boxHeight);
        	skuData.setBoxUnit(boxUnit);
        	skuData.setBoxType(boxType);

        	skuData.setUnitCost(Double.parseDouble(arrUnitCost[i]));
        	skuData.setQuantity(Integer.parseInt(arrQuantity[i]));
        	
        	
        	//sku 저장용
        	skuData.setSkuIdx(Integer.parseInt(arrSkuIdx[i]));
        	skuData.setShopIdx(shopIdx);
        	skuData.setItemCode(arrGoodsCode[i]);
        	skuData.setItemName(arrGoods[i]);
        	skuData.setItemType(arrGoodsType[i]);
        	skuData.setItemSku(arrGoodsSku[i]);
        	skuData.setItemPrice(Double.parseDouble(arrUnitCost[i]));
        	int qty = Integer.parseInt(arrQuantity[i]);
        	if(qty == 0) {
        		qty = 1; //기본 1로 설정
        	}
        	skuData.setItemQty(qty);
        	skuData.setItemOrigin(arrOrigin[i]);
        	skuData.setItemWeight(arrWeight[i]);
        	skuData.setRegId("order");
        	
        	skuData.setItemLink(itemLink[i]);
        	skuData.setBrand(brand[i]);
        	
        	payWeight += Integer.parseInt(arrWeight[i]);
        	
        	//관세정보 업데이트
            chk = (orderMapper.selectSettingSkuCount(skuData) <= 0) ? orderMapper.insertSettingSku(skuData):orderMapper.updateSettingSku(skuData);
            chk = (orderMapper.selectDeliverySkuCount(skuData) <= 0) ? orderMapper.insertDeliverySku(skuData):orderMapper.updateDeliverySku(skuData);  
        }

		/* ######################################
		 * 배송서비스 정보 생성
		 * ######################################
		 */
        OrderCourierData courier = new OrderCourierData();

        System.out.println("######!!yr!!request###############)==>"+request);
		//yr추가//	
        OrderCourierData input = new OrderCourierData();
		input.setShippingLineName(request.get("shippingLineName"));
		input.setShippingLineCode(request.get("shippingLineCode"));
		input.setMasterCode(masterCode);
		
		OrderCourierData courierData = orderMapper.selectPayment(input);
		if(courierData == null) {
			//courier.setInvoice(createInvoice());
			/*courier.setPayWeight(address.getBoxWeight());
			courier.setPayWeightUnit(address.getWeightUnit());*/
			courier.setInvoice(""); // 송장번호 업데이트는 LGL연동 작업
			courier.setMasterCode(masterCode);
			//courier.setPaymentIdx(courierData.getPaymentIdx());
	        courier.setPaymentCode(request.get("paymentCode"));
	        courier.setInvoice(createInvoice());
	        courier.setCourierCompany(request.get("courierCompany"));
	        courier.setCourier(request.get("courier"));
	        courier.setCourierId(request.get("courierId"));
	        courier.setPayment(Integer.parseInt(request.get("price")));
	        courier.setRankPrice(Integer.parseInt(request.get("rankPrice")));
	        courier.setPayState(request.get("payState"));   
			courier.setPayWeight(payWeight+"");
			 
			courier.setPayWeightUnit("g");
			chk = orderMapper.insertDeliveryPayment(courier);
		}
		else {
	        courier.setMasterCode(masterCode);
	        courier.setPaymentIdx(courierData.getPaymentIdx());
	        courier.setPaymentCode(request.get("paymentCode"));
	        courier.setInvoice(createInvoice());
	        courier.setCourierCompany(request.get("courierCompany"));
	        courier.setCourier(request.get("courier"));
	        courier.setCourierId(request.get("courierId"));
	        courier.setPayment(Integer.parseInt(request.get("price")));
	        courier.setRankPrice(Integer.parseInt(request.get("rankPrice")));
	        //courier.setPayWeight(request.get("payWeight"));
	        courier.setPayWeight(payWeight+"");
	        courier.setPayState(request.get("payState"));
	        if(request.get("payWeightUnit") == null) {
	        	courier.setPayWeightUnit("g");
	        }else {
	        	courier.setPayWeightUnit(request.get("payWeightUnit"));
	        }
	        System.out.println("yryr33####paymentCourier###############==>"+courier);
	        chk = orderMapper.insertDeliveryPayment(courier);
			
		}
       
	
        //테스트용 order_code
        //2185397469289
        //2185395437673
		
		/* ######################################
		 * 햅배송 정보 생성 (createDeliveryData) method 에서 처리
		 * ######################################
		 */
//        for (int y = 0; y < arrCode.length; y++) {
//			String code = arrCode[y].trim();
//			String idx = arrIdx[y].trim();
//			
//			if(!"".equals(code) && !"".equals(idx)) {
//				OrderData od = new OrderData();
//				od.setParentCode(masterCode);
//				od.setChildCode(code);
//				od.setOrderCode(code);
//                od.setShopIdx(Integer.parseInt(idx));
//                
//                chk = orderMapper.insertDeliveryCombine(od);
//			}
//		}

		return chk;
	}
}
