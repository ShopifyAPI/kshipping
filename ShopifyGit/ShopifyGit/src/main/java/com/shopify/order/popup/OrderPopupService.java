package com.shopify.order.popup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.common.util.UtilFunc;
import com.shopify.mapper.OrderMapper;
import com.shopify.mapper.SettingMapper;
import com.shopify.order.OrderData;
import com.shopify.order.OrderDetailSkuData;
import com.shopify.setting.SettingBoxData;
import com.shopify.setting.SettingSenderData;
import com.shopify.setting.SettingService;
import com.shopify.shipment.ShipmentData;
import com.shopify.shop.ShopData;


/**
* 주문 팝업 Service
* @author : 심윤
* @since  : 2019-12-26
* @desc   : 주문 팝업 서비스 
*/

@Service
@Transactional
public class OrderPopupService {
	private Logger LOGGER = LoggerFactory.getLogger(OrderPopupService.class);
	
	@Autowired private UtilFn util;
	@Autowired private OrderMapper orderMapper;
	@Autowired private SettingMapper settingMapper;
	@Autowired private SettingService settingService;
    @Autowired private OrderPopupRestService orderPopupRestService;
    @Autowired private ObjectMapper mapper;
    
    private Pattern escapePattern = Pattern.compile("&[^;]+;");
	
	public List<OrderData> selectOrderCode(List<OrderData> orderList){
        return orderMapper.selectOrderCode(orderList);
    }
	
	public int updateOrderCodeMerge(List<OrderData> orderList){
		
		int result = orderMapper.updateOrderDetailCodeMerge(orderList);
		
        return orderMapper.updateOrderCodeMerge(orderList);
    }
	
	public int selectOrderIdx(String orderCode){
		
        return orderMapper.selectOrderIdx(orderCode);
    }
	

	/**
	 * 주문 > 팝업 > 주소 설정
	 * @param TrackingData
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectAddress(ShipmentData setting, HttpSession sess){
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		// 기본 배송 정보 받아 오기 
		ShipmentData address = orderMapper.selectDeliveryAddressOrderList(setting);
		String masterCode = "";
		
		if(address != null) {
			masterCode = address.getMasterCode();
		}
		
		// 셀러 기본 주소 받아 오기
		SettingSenderData settingSeller = new SettingSenderData();
		settingSeller.setEmail(email);
		
		// 정보가 없을 경우
		//if(address == null) {
		if(address == null || address.getBuyerZipCode() == null || "".equals(address.getBuyerZipCode())) {
			address = new ShipmentData();
			
			// 주문 리스트의 배송 정보 가져오기
			OrderData settingOrder = new OrderData();
			settingOrder.setShopIdx(setting.getShopIdx());
			settingOrder.setOrderCode(setting.getOrderCode());
			
			OrderData orderAddr = orderMapper.selectOneOrder(settingOrder);
			if (orderAddr != null) {
				try {
					try {
						address.setBuyerFirstname(UtilFunc.getJsonString(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, orderAddr.getBuyerFirstname())));
					} catch (Exception e) {
						e.getStackTrace();
					}
					
					try {
						address.setBuyerLastname(UtilFunc.getJsonString(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, orderAddr.getBuyerLastname())));
					} catch (Exception e) {
						e.getStackTrace();
					}
					
					try {
						address.setBuyerPhone(UtilFunc.getJsonString(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, orderAddr.getBuyerPhone())));
					} catch (Exception e) {
						e.getStackTrace();
					}

					address.setBuyerEmail(UtilFunc.getJsonString(orderAddr.getBuyerEmail()));
					address.setBuyerCountryCode(UtilFunc.getJsonString(orderAddr.getBuyerCountryCode()));
					address.setBuyerCountry(UtilFunc.getJsonString(orderAddr.getBuyerCountry()));
					address.setBuyerCity(UtilFunc.getJsonString(orderAddr.getBuyerCity()));
					address.setBuyerProvince(UtilFunc.getJsonString(orderAddr.getBuyerProvince()));
					address.setBuyerZipCode(UtilFunc.getJsonString(orderAddr.getBuyerZipCode()));
					address.setBuyerAddr1(UtilFunc.getJsonString(orderAddr.getBuyerAddr1()));
					address.setBuyerAddr2(UtilFunc.getJsonString(orderAddr.getBuyerAddr2()));
					
					address.setOrderCourier(orderAddr.getOrderCourier());
					address.setShippingLineName(orderAddr.getShippingLineName());
					address.setShippingLineCode(orderAddr.getShippingLineCode());
					
					
					LOGGER.debug("setting.getOrderDate() : " + setting.getOrderDate());
					
					address.setShopIdx(setting.getShopIdx());
					address.setOrderIdx(orderAddr.getOrderIdx());
					address.setOrderCode(setting.getOrderCode());
					address.setOrderDate(orderAddr.getOrderDate());
					
					address.setMasterCode(masterCode);
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
				address.setSellerAddr1Ename(sellerDefaultAddr.getAddr1Ename());
				address.setSellerAddr2Ename(sellerDefaultAddr.getAddr2Ename());
				address.setSellerProvince(sellerDefaultAddr.getProvince());
				address.setSellerCity(sellerDefaultAddr.getCity());
				address.setSellerCountryCode("");
				address.setSellerCountry("");
			}
			
			
			// box 정보
			
		}
		else { // 정보가 있을경우
			try {
				address.setBuyerFirstname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, address.getBuyerFirstname()));
			} catch (Exception e) {
				e.getStackTrace();
			}
			
			try {
				address.setBuyerLastname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, address.getBuyerLastname()));
			} catch (Exception e) {
				e.getStackTrace();
			}
			
			try {
				address.setBuyerPhone(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, address.getBuyerPhone()));
//				String[] arrPhoneNumber = address.getBuyerPhone().split("-");
//				if(arrPhoneNumber.length == 4) {
//					address.setBuyerPhone01(arrPhoneNumber[0]);
//					address.setBuyerPhone02(arrPhoneNumber[1]);
//					address.setBuyerPhone03(arrPhoneNumber[2]);
//					address.setBuyerPhone04(arrPhoneNumber[3]);
//				}
			} catch (Exception e) {
				e.getStackTrace();
			}
			
			try {
				address.setSellerName(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, address.getSellerName()));
			} catch (Exception e) {
				e.getStackTrace();
			}
			
			try {
				address.setSellerPhone(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, address.getSellerPhone()));
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
		
		// return setting
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("address", address);
		map.put("seller", seller);
		
		return map;
    }
	
	public Map<String, Object> selectDomesticAddress(int shopIdx, ShipmentData setting, HttpSession sess){
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		//합배송 데이터 체크하기
		if(setting.getArrShopIdx().indexOf(",")>0) {
			
			String[] arrOrder = setting.getOrderCode().split(",");
			String[] arrShop = setting.getArrShopIdx().split(",");
			
			setting.setShopIdx(Integer.parseInt(arrShop[0]));
			setting.setOrderCode(arrOrder[0]);

		}
		else {
			setting.setShopIdx(shopIdx);
		}
		// 기본 배송 정보 받아 오기 
		ShipmentData address = orderMapper.selectDeliveryAddress(setting);
		String masterCode = "";
		
		if(address != null) {
			masterCode = address.getMasterCode();
		}
		
		// 셀러 기본 주소 받아 오기
		SettingSenderData settingSeller = new SettingSenderData();
		settingSeller.setEmail(email);
		
		// 정보가 없을 경우
		//if(address == null) {
		if(address == null || address.getBuyerZipCode() == null || "".equals(address.getBuyerZipCode())) {
			address = new ShipmentData();
			
			// 주문 리스트의 배송 정보 가져오기
			OrderData settingOrder = new OrderData();
			settingOrder.setShopIdx(setting.getShopIdx());
			settingOrder.setOrderCode(setting.getOrderCode());
			
			OrderData orderAddr = orderMapper.selectOneOrder(settingOrder);
			if (orderAddr != null) {
				try {
					try {
						address.setBuyerFirstname(UtilFunc.getJsonString(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, orderAddr.getBuyerFirstname())));
					} catch (Exception e) {
						e.getStackTrace();
					}
					
					try {
						address.setBuyerLastname(UtilFunc.getJsonString(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, orderAddr.getBuyerLastname())));
					} catch (Exception e) {
						e.getStackTrace();
					}
					
					try {
						address.setBuyerPhone(UtilFunc.getJsonString(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, orderAddr.getBuyerPhone())));
					} catch (Exception e) {
						e.getStackTrace();
					}
					
					address.setBuyerEmail(UtilFunc.getJsonString(orderAddr.getBuyerEmail()));
					address.setBuyerCountryCode(UtilFunc.getJsonString(orderAddr.getBuyerCountryCode()));
					address.setBuyerCountry(UtilFunc.getJsonString(orderAddr.getBuyerCountry()));
					address.setBuyerCity(UtilFunc.getJsonString(orderAddr.getBuyerCity()));
					address.setBuyerProvince(UtilFunc.getJsonString(orderAddr.getBuyerProvince()));
					address.setBuyerZipCode(UtilFunc.getJsonString(orderAddr.getBuyerZipCode()));
					address.setBuyerAddr1(UtilFunc.getJsonString(orderAddr.getBuyerAddr1()));
					address.setBuyerAddr2(UtilFunc.getJsonString(orderAddr.getBuyerAddr2()));
					
					LOGGER.debug("setting.getOrderDate() : " + setting.getOrderDate());
					
					address.setShopIdx(setting.getShopIdx());
					address.setOrderIdx(orderAddr.getOrderIdx());
					address.setOrderCode(setting.getOrderCode());
					address.setOrderDate(orderAddr.getOrderDate());
					
					address.setMasterCode(masterCode);
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
				address.setSellerAddr1Ename(sellerDefaultAddr.getAddr1Ename());
				address.setSellerAddr2Ename(sellerDefaultAddr.getAddr2Ename());
				address.setSellerProvince(sellerDefaultAddr.getProvince());
				address.setSellerCity(sellerDefaultAddr.getCity());
				address.setSellerCountryCode("");
				address.setSellerCountry("");
			}
		}
		else { // 정보가 있을경우
			try {
				address.setBuyerFirstname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, address.getBuyerFirstname()));
			} catch (Exception e) {
				e.getStackTrace();
			}
			
			try {
				address.setBuyerLastname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, address.getBuyerLastname()));
			} catch (Exception e) {
				e.getStackTrace();
			}
			
			try {
				address.setBuyerPhone(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, address.getBuyerPhone()));
			} catch (Exception e) {
				e.getStackTrace();
			}
			
			try {
				address.setSellerName(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, address.getSellerName()));
			} catch (Exception e) {
				e.getStackTrace();
			}
			
			try {
				address.setSellerPhone(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, address.getSellerPhone()));
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
		}
		
		String buyerName;
		if ( "KR".contentEquals(address.getBuyerCountryCode()) ) {
			buyerName = String.join(" ", address.getBuyerLastname(), address.getBuyerFirstname());
		} else {
			buyerName = String.join(" ", address.getBuyerFirstname(), address.getBuyerLastname());
		}
		address.setBuyerName(buyerName);
		
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
				if (arrPhoneNumber.length == 4) {
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
		
		// return setting
		setting.setTotalWeight(address.getTotalWeight());
		setting.setBuyerCountryCode(address.getBuyerCountryCode());
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<OrderCourierData> shopList = settingService.selectHomeDefaultCourier(email, shopIdx);
		OrderCourierData defaultCourier;
		if ( shopList.size() > 0 ) {
			defaultCourier = shopList.get(0);
		} else {
			defaultCourier = new OrderCourierData();
		}
		
//		shopList.stream().forEach( d -> UtilFunc.printValue(d) );
		
		map.put("defaultCourier", defaultCourier) ;
		map.put("address", address);
		map.put("seller", seller);
		
		return map;
	}
	
	
	/**
	 * 주문 > 팝업 > 주소와 box 의 validity check
	 * @param TrackingData
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> checkAddressAndBox(ShipmentData setting, HttpSession sess){
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		// 기본 배송 정보 받아 오기 
		ShipmentData address = orderMapper.selectDeliveryAddress(setting);
		
		// return setting
		Map<String, Object> map = new HashMap<String, Object>();
		// 정보가 없을 경우
		//if(address == null) {
		if(address == null ) {
			map.put("msgCode", "order.popup.customerAlert.link.buyerZipCode");
//		} else if(  ! "KR".equals(address.getBuyerCountryCode()) && StringUtils.isBlank(address.getBoxType() )) { // 정보가 있을경우
//			map.put("msgCode", "order.popup.customerAlert.link.boxType");
		} else {
			map.put("msgCode", "SUCCESS");
		}

		return map;
	}
	
	/**
	 * 주문 > 팝업 > 관세 정보 설정
	 * @param TrackingData
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	
	public Map<String, Object> selectOrderExpress(ShipmentData setting, HttpSession sess){
		Map<String, Object> map = new HashMap<String, Object>();
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		// 기본 배송 정보 받아 오기 
		ShipmentData address = orderMapper.selectDeliveryAddress(setting);
		
		int shopIdx = setting.getShopIdx();
		/* String orderCode = setting.getOrderCode(); */
		/*String[] arrOrderCode = {};
		String[] arrShopIdx = {};*/
		
		// 배송 디테일 정보 받아 오기
		OrderDetailSkuData orderDetail = new OrderDetailSkuData();
		orderDetail.setShopIdx(shopIdx);
		/* orderDetail.setOrderCode(orderCode); */
		/* orderDetail.setOrderCode(orderCode); */
		if("".equals(setting.getArrShopIdx()) || setting.getArrShopIdx() == null) {
			String orderCode = setting.getOrderCode();
			orderDetail.setOrderCode(orderCode);
		}
		else {
			String[] arrOrder = setting.getOrderCode().split(",");
			String[] arrShop = setting.getArrShopIdx().split(",");
			orderDetail.setOrderCode(arrOrder[0]);
			orderDetail.setArrOrderCode(arrOrder);
			orderDetail.setArrShopIdx(arrShop);
		}				
		
		List<OrderDetailSkuData> skuList = new ArrayList<OrderDetailSkuData>();
		
		// 기본 박스 정보 받아 오기 
		SettingBoxData settingBox = new SettingBoxData();
		settingBox.setEmail(email);
		
		List<SettingBoxData> boxList = null;
		List<OrderCourierData> courierList = null;
		String masterCode = "";
		
		if ( "KR".equals( setting.getBuyerCountryCode() ) ) {
//			map.putAll(m);
			Map<String, Object> setMap = settingService.selectOrderCustomsOrder(sess, false);
			
			courierList = (List<OrderCourierData>) setMap.get("companyList");
			map.put("courierList", courierList);
//			map.put("boxTypeList", setMap.get("boxList"));
			
//			///////////////////////
//			List<OrderCourierData>  boxTypeList = new ArrayList<>();
//			List<OrderCourierData>  tempList = (List<OrderCourierData>) setMap.get("boxList");
//			
//			for(  OrderCourierData box : tempList ) {
//				boxTypeList.add(box);
//			}
//			for(  OrderCourierData box : tempList ) {
//				OrderCourierData clone = new OrderCourierData();
//				BeanUtils.copyProperties(box, clone);
//				clone.setComCode("B010030");
//				clone.setCode("P_HOME");
//				clone.setPrice(clone.getPrice() + 10000);
//				clone.setCourierId("B140010");
//				boxTypeList.add(clone);
//			}
//			//////////////////////////
			
			List<OrderCourierData>  boxTypeList = (List<OrderCourierData>) setMap.get("boxList");
			map.put("boxTypeList", boxTypeList);
			
			
//	    	model.addAttribute("shopList", map.get("shopList"));
//	    	model.addAttribute("companyList", map.get("companyList"));
//	    	model.addAttribute("boxList", map.get("boxList"));
		} else {
			
			// 박스 정보 
			boxList = settingMapper.selectBox(settingBox);
			map.put("boxList", boxList);
			
//			// 특송업체 리스트
//			double totlaWeight = 0.0 ;
//			String buyerCountryCode = "" ;
//			String nowDate = util.getDateElement("today");
//			String locale = sd.getLocale() ;
//			if (address != null) {
//				totlaWeight = Double.valueOf(address.getTotalWeight());
//				buyerCountryCode = address.getBuyerCountryCode(); 
//				masterCode = address.getMasterCode();
//			} else {
//				totlaWeight = Double.valueOf(setting.getTotalWeight());
//				buyerCountryCode = setting.getBuyerCountryCode(); 
//			}
//			int weight = util.wigthConversion(totlaWeight, "kg");
//			OrderCourierData courier;
//			courier = new OrderCourierData();
//			courier.setEmail(email);
//			courier.setLocale(locale);
//			courier.setNationCode(buyerCountryCode);
//			courier.setWeight(weight);
//			courier.setNowDate(nowDate);
//			courierList = orderMapper.selectCourierList(courier); 
//			map.put("courierList", courierList);
//			
//			List<LocalDeliveryData> boxTypeList = new ArrayList<LocalDeliveryData>();
//			LocalDeliveryData pikData = new LocalDeliveryData();
//			for (OrderCourierData item:courierList)
//			{
//				pikData.setId(item.getComCode());
//				pikData.setCode(item.getCode());
//				pikData.setCourierName(item.getCodeName());
//				boxTypeList.addAll( orderMapper.selectLocalDelivery(pikData) );		
//			}
//			map.put("boxTypeList", boxTypeList);
		}
		
		
		// 정보가 없을 경우
		if(address == null || address.getBoxType() == null || "".equals(address.getBoxType())) {
			// 주문 리스트의 배송 정보 가져오기
			OrderData settingOrder = new OrderData();
			settingOrder.setShopIdx(shopIdx);
			settingOrder.setOrderCode(orderDetail.getOrderCode());
			
			OrderData orderAddr = orderMapper.selectOneOrder(settingOrder);
			
			address = new ShipmentData();
			
			address.setShopIdx(orderAddr.getShopIdx());
			address.setOrderIdx(orderAddr.getOrderIdx());
			address.setOrderCode(orderAddr.getOrderCode());
			address.setOrderDate(orderAddr.getOrderDate());
			address.setBuyerCountryCode(orderAddr.getBuyerCountryCode());
			address.setMasterCode(masterCode);
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
			skuList = orderMapper.selectOrderDetailList(orderDetail);
		}
		else {
			// 배송 관세 정보 받아 오기
			orderDetail.setMasterCode(address.getMasterCode());
			skuList = orderMapper.selectDeliverySkuList(orderDetail);
		}
		map.put("skuList", skuList);
		map.put("address", address);
		
		return map;
	}
	
	
	public Map<String, Object> selectExpress(ShipmentData setting, HttpSession sess, ShipmentData address){
		Map<String, Object> map = new HashMap<String, Object>();
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		// 기본 배송 정보 받아 오기 
//		ShipmentData address = orderMapper.selectDeliveryAddress(setting);
		
		int shopIdx = setting.getShopIdx();
		/* String orderCode = setting.getOrderCode(); */
		/*String[] arrOrderCode = {};
		String[] arrShopIdx = {};*/
		
		// 배송 디테일 정보 받아 오기
		OrderDetailSkuData orderDetail = new OrderDetailSkuData();
		orderDetail.setShopIdx(shopIdx);
		/* orderDetail.setOrderCode(orderCode); */
		/* orderDetail.setOrderCode(orderCode); */
		if("".equals(setting.getArrShopIdx()) || setting.getArrShopIdx() == null) {
			String orderCode = setting.getOrderCode();
			orderDetail.setOrderCode(orderCode);
		}
		else {
			String[] arrOrder = setting.getOrderCode().split(",");
			String[] arrShop = setting.getArrShopIdx().split(",");
			orderDetail.setOrderCode(arrOrder[0]);
			orderDetail.setArrOrderCode(arrOrder);
			orderDetail.setArrShopIdx(arrShop);
		}				
		
		List<OrderDetailSkuData> skuList;
		
		// 기본 박스 정보 받아 오기 
		SettingBoxData settingBox = new SettingBoxData();
		settingBox.setEmail(email);
		
		List<SettingBoxData> boxList = null;
		List<OrderCourierData> courierList = null;
		String masterCode = "";
		
		if ( "KR".equals( setting.getBuyerCountryCode() ) ) {
//			map.putAll(m);
			Map<String, Object> setMap = settingService.selectOrderCustomsOrder(sess, false);
			
			courierList = (List<OrderCourierData>) setMap.get("companyList");
			map.put("courierList", courierList);
//			map.put("boxTypeList", setMap.get("boxList"));
			
//			///////////////////////
//			List<OrderCourierData>  boxTypeList = new ArrayList<>();
//			List<OrderCourierData>  tempList = (List<OrderCourierData>) setMap.get("boxList");
//			
//			for(  OrderCourierData box : tempList ) {
//				boxTypeList.add(box);
//			}
//			for(  OrderCourierData box : tempList ) {
//				OrderCourierData clone = new OrderCourierData();
//				BeanUtils.copyProperties(box, clone);
//				clone.setComCode("B010030");
//				clone.setCode("P_HOME");
//				clone.setPrice(clone.getPrice() + 10000);
//				clone.setCourierId("B140010");
//				boxTypeList.add(clone);
//			}
//			//////////////////////////
			
			List<OrderCourierData>  boxTypeList = (List<OrderCourierData>) setMap.get("boxList");
			map.put("boxTypeList", boxTypeList);
			
			
//	    	model.addAttribute("shopList", map.get("shopList"));
//	    	model.addAttribute("companyList", map.get("companyList"));
//	    	model.addAttribute("boxList", map.get("boxList"));
		} else {
			
			// 박스 정보 
			boxList = settingMapper.selectBox(settingBox);
			map.put("boxList", boxList);
			
		}
		
		if( address == null ) {
			// case-1  KR 에서 최초 진입했을 때
			// 주문 리스트의 배송 정보 가져오기
			OrderData settingOrder = new OrderData();
			settingOrder.setShopIdx(shopIdx);
			settingOrder.setOrderCode(orderDetail.getOrderCode());
			
			OrderData orderAddr = orderMapper.selectOneOrder(settingOrder);
			
			address = new ShipmentData();
			address.setShopIdx(orderAddr.getShopIdx());
			address.setOrderIdx(orderAddr.getOrderIdx());
			address.setOrderCode(orderAddr.getOrderCode());
			address.setOrderDate(orderAddr.getOrderDate());
			address.setBuyerCountryCode(orderAddr.getBuyerCountryCode());
			address.setMasterCode(masterCode);
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
			skuList = orderMapper.selectOrderDetailList(orderDetail);
		} else if ( address.getBoxType() == null || "".equals(address.getBoxType()) ) {
			// case-2  Foreign 에서 최초진입인 경우
			skuList = orderMapper.selectOrderDetailList(orderDetail);
		} else {
			// 배송 관세 정보 받아 오기
			orderDetail.setMasterCode(address.getMasterCode());
			skuList = orderMapper.selectDeliverySkuList(orderDetail);
//			throw new RuntimeException("TODO");
		}
		
		map.put("skuList", skuList);
		map.put("address", address);
		
		// skuList 에서 selectBox 이 있는 box size 가 가장 큰 것 하나만 가져온다.
		if ( address.getSelectBox() < 1 ) {
			OrderDetailSkuData skuData = skuList.stream().max(
					Comparator.comparingInt(b -> Integer.parseInt(b.getBoxLength()) * Integer.parseInt(b.getBoxWidth()) * Integer.parseInt(b.getBoxHeight()) )
					).orElseGet(OrderDetailSkuData::new);
			
			address.setSelectBox(skuData.getSelectBox());
			address.setBoxWeight("init");
			address.setWeightUnit("g");
		}
		
		map.put("courier", selectPayment(address));
		
		return map;
	}
	
	/**
	 * 주문 > 팝업 > 해외특송 선택
	 * @param TrackingData
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectOrderCustoms(ShipmentData setting, HttpSession sess){
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = "";
		String locale = "";
		
		if(sd != null) {
			email = sd.getEmail();
			locale = sd.getLocale();
		}
		setting.setEmail(email);
		
		// 기본 배송 정보 받아 오기 
		ShipmentData address = orderMapper.selectDeliveryAddress(setting);
		
		OrderCourierData courier;
		
		String status = "no";
		if(address != null) {
			
			OrderCourierData input = new OrderCourierData();
			input.setShippingLineName(address.getShippingLineName());
			input.setShippingLineCode(address.getShippingLineCode());
			input.setMasterCode(address.getMasterCode());
			
			courier = orderMapper.selectPayment(input); //결제 정보 받아 오기 
			if( courier == null) {
				courier = new OrderCourierData();
			}
			
			//결제 정보 기타 정보 세팅
			double totlaWeight = Double.valueOf(address.getTotalWeight());
//		    String weightUnit =  address.getWeightUnit();
		    
		    int weight =  util.wigthConversion(totlaWeight, "kg");
		    String buyerCountryCode = address.getBuyerCountryCode();
		    String nowDate = util.getDateElement("today");
		    
		    
		    courier.setEmail(email);
		    courier.setLocale(locale);
		    courier.setNationCode(buyerCountryCode);
			courier.setWeight(weight);
		    courier.setNowDate(nowDate);
			
			status = "yes";
		} else {
			courier = new OrderCourierData();
		}
		
		int shopIdx = setting.getShopIdx();
		String orderCode = setting.getOrderCode();
		
		address.setShopIdx(shopIdx);
		address.setOrderCode(orderCode);
		
		List<OrderCourierData> list = orderMapper.selectCourierList(courier); //특송업체 리스트
		
		// return setting
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("courier", courier);
		map.put("address", address);
		map.put("status", status);
		return map;
	}
	
	public OrderCourierData selectPayment(ShipmentData address){
	
		OrderCourierData courier;
		
		if(address != null) {
			
			OrderCourierData input = new OrderCourierData();
			input.setShippingLineName(address.getShippingLineName());
			input.setShippingLineCode(address.getShippingLineCode());
			input.setMasterCode(address.getMasterCode());
			
			courier = orderMapper.selectPayment(input); //결제 정보 받아 오기 
			if( courier == null) {
				courier = new OrderCourierData();
			}
			
		} else {
			courier = new OrderCourierData();
		}
		
//		int shopIdx = setting.getShopIdx();
//		String orderCode = setting.getOrderCode();
//		
//		address.setShopIdx(shopIdx);
//		address.setOrderCode(orderCode);
		
//		List<OrderCourierData> list = orderMapper.selectCourierList(courier); //특송업체 리스트
//		List<OrderCourierData> list = orderPopupRestService.selectVolumeticCourierList(email, buyerCountryCode, locale, weight, length, width, height); //특송업체 리스트
		
		// return setting
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("list", list);
//		map.put("courier", courier);
//		map.put("address", address);
//		map.put("status", status);
//		return map;
		
		return courier;
	}
	

	
	
	/**
	 * 주문 > 팝업 > 합배송 개수
	 * @param TrackingData
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public int selectDeliveryCombineCount(OrderData orderCode){
		OrderData chk = orderMapper.selectDeliveryCombineCount(orderCode);
		LOGGER.debug("###############selectDeliveryCombineCount=chk=>/"+chk);
		LOGGER.debug("###############selectDeliveryCombineCount=chk=>/"+chk.getParentCodeCnt());
		return chk.getParentCodeCnt();
	}

	/**
	 * 주문 > 팝업 > 합배송 저장
	 * @param OrderData
	 * @param HttpSession
	 * @return int
	 */
	public int insertDeliveryCombine(OrderData orderData) {
		return orderMapper.insertDeliveryCombine(orderData);
	}
		
	public List<OrderData> selectDeliveryCombine(String mastercode) {
		return orderMapper.selectDeliveryCombine(mastercode);
	}
	
	/**
	 * 주문 > 팝업 > 합배송 배송 정보 등록(insert와 동일하지만 mastercode를 반환받아야함)
	 * @param OrderDeliveryData
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
    public Map<String, Object> createDelivery(OrderDeliveryData deliveryData,HttpSession sess) {
        ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);

        String sellerName = sessionData.getShopName();
        if(sellerName == null) sellerName = sessionData.getEmail();
        
        String sellerPhone = deliveryData.getSellerPhone01() + "-" + deliveryData.getSellerPhone02() + "-" + deliveryData.getSellerPhone03() + "-" + deliveryData.getSellerPhone04();
        //String sellerPhone = deliveryData.getSellerPhone();
        if(sellerPhone == null) sellerPhone = "";

        String buyerFirstName = deliveryData.getBuyerFirstname();
        if(buyerFirstName == null) buyerFirstName = "";
        
        String buyerLastName = deliveryData.getBuyerLastname();
        if(buyerLastName == null) buyerLastName = "";

        String buyerPhone = deliveryData.getBuyerPhone01() + "-" + deliveryData.getBuyerPhone02() + "-" + deliveryData.getBuyerPhone03() + "-" + deliveryData.getBuyerPhone04(); 
        //String buyerPhone = deliveryData.getBuyerPhone();
        if(buyerPhone == null) buyerPhone = "";
        
        try {
        	deliveryData.setSellerName(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, sellerName));  
		} catch (Exception e) {
			e.getStackTrace();
		}
        
        try {
        	deliveryData.setSellerPhone(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, sellerPhone));
		} catch (Exception e) {
			e.getStackTrace();
		}
        
        try {
        	deliveryData.setBuyerFirstname(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, buyerFirstName));
		} catch (Exception e) {
			e.getStackTrace();
		}
        
        try {
        	deliveryData.setBuyerLastname(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, buyerLastName));
		} catch (Exception e) {
			e.getStackTrace();
		}
        
        try {
        	deliveryData.setBuyerPhone(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, buyerPhone));
		} catch (Exception e) {
			e.getStackTrace();
		}
        
        deliveryData.setCombineChk("Y");

        //배송 생성/업데이트
        Map<String, Object> chk = createDeliveryData(deliveryData);

        return chk; 
    }
	/**
	 * 주문 > 팝업 > 배송 정보 등록
	 * @param OrderDeliveryData
	 * @param HttpSession
	 * @return int
	 */
    public int insertDelivery(OrderDeliveryData deliveryData,HttpSession sess) {
        ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);

        String sellerName = sessionData.getShopName();
        if(sellerName == null) sellerName = sessionData.getEmail();
        
        String sellerPhone = deliveryData.getSellerPhone01() + "-" + deliveryData.getSellerPhone02() + "-" + deliveryData.getSellerPhone03() + "-" + deliveryData.getSellerPhone04();
        //String sellerPhone = deliveryData.getSellerPhone();
        if(sellerPhone == null) sellerPhone = "";

        String buyerFirstName = deliveryData.getBuyerFirstname();
        if(buyerFirstName == null) buyerFirstName = "";
        
        String buyerLastName = deliveryData.getBuyerLastname();
        if(buyerLastName == null) buyerLastName = "";

//        String buyerPhone = deliveryData.getBuyerPhone01() + "-" + deliveryData.getBuyerPhone02() + "-" + deliveryData.getBuyerPhone03() + "-" + deliveryData.getBuyerPhone04(); 
        String buyerPhone = deliveryData.getBuyerPhone();
        if(buyerPhone == null) buyerPhone = "";
        
        try {
        	deliveryData.setSellerName(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, sellerName));  
		} catch (Exception e) {
			e.getStackTrace();
		}
        
        try {
        	deliveryData.setSellerPhone(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, sellerPhone));
		} catch (Exception e) {
			e.getStackTrace();
		}
        
        try {
        	deliveryData.setBuyerFirstname(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, buyerFirstName));
		} catch (Exception e) {
			e.getStackTrace();
		}
        
        try {
        	deliveryData.setBuyerLastname(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, buyerLastName));
		} catch (Exception e) {
			e.getStackTrace();
		}
        
        try {
        	deliveryData.setBuyerPhone(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, buyerPhone));
		} catch (Exception e) {
			e.getStackTrace();
		}

        //배송 생성/업데이트
        Map<String, Object> chk = createDeliveryData(deliveryData);

        return (int) chk.get("chk"); 
    }
    
    
    // call from foreign
    public int insertDeliveryForeign(OrderDeliveryData deliveryData) {
    	
    	
    	//배송 생성/업데이트
    	Map<String, Object> chk = createDeliveryData(deliveryData);
    	
    	return (int) chk.get("chk"); 
    }

	private void encryptAddress(OrderDeliveryData deliveryData, HttpSession sess) {
		ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		
		String sellerName = sessionData.getShopName();
    	if(sellerName == null) sellerName = sessionData.getEmail();
    	
//    	String sellerPhone = deliveryData.getSellerPhone01() + "-" + deliveryData.getSellerPhone02() + "-" + deliveryData.getSellerPhone03() + "-" + deliveryData.getSellerPhone04();
    	String sellerPhone = deliveryData.getSellerPhone();
    	if(sellerPhone == null) sellerPhone = "";
    	
    	String buyerFirstName = deliveryData.getBuyerFirstname();
    	if(buyerFirstName == null) buyerFirstName = "";
    	
    	String buyerLastName = deliveryData.getBuyerLastname();
    	if(buyerLastName == null) buyerLastName = "";
    	
//        String buyerPhone = deliveryData.getBuyerPhone01() + "-" + deliveryData.getBuyerPhone02() + "-" + deliveryData.getBuyerPhone03() + "-" + deliveryData.getBuyerPhone04(); 
    	String buyerPhone = deliveryData.getBuyerPhone();
    	if(buyerPhone == null) buyerPhone = "";
    	
    	try {
    		deliveryData.setSellerName(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, sellerName));  
    	} catch (Exception e) {
    		e.getStackTrace();
    	}
    	
    	try {
    		deliveryData.setSellerPhone(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, sellerPhone));
    	} catch (Exception e) {
    		e.getStackTrace();
    	}
    	
    	try {
    		deliveryData.setBuyerFirstname(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, buyerFirstName));
    	} catch (Exception e) {
    		e.getStackTrace();
    	}
    	
    	try {
    		deliveryData.setBuyerLastname(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, buyerLastName));
    	} catch (Exception e) {
    		e.getStackTrace();
    	}
    	
    	try {
    		deliveryData.setBuyerPhone(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, buyerPhone));
    	} catch (Exception e) {
    		e.getStackTrace();
    	}
	}
    
	public Map<String, Object> insertFacadeExpressCustoms(OrderDeliveryData delivery, OrderCourierData courier, HttpSession sess) {
		
		Map<String, Object> map = new HashMap<>();
		
		// insertDeliveryForeign() 에서 delivery.orderIdx 를 autoIncrement 값으로 변경하기 때문에, 여기서 복사해 놓는다.
    	encryptAddress(delivery, sess);
		OrderDeliveryData deliveryClone = new OrderDeliveryData();
		BeanUtils.copyProperties(delivery, deliveryClone);
		
		int chk = insertDeliveryForeign(delivery);
  
		if (chk == 0) {
			map.put("errCode", "order.popup.expressAlert.courier.fail");
		} else {
			map.putAll( insertDeliveryExpress(deliveryClone, sess) );
			String result = (String) map.get("errCode");

			if ( result.equals("SUCCESS")) {
				chk = insertDeliveryCustoms(courier, sess);
				if(chk == 0) {
					map.put("errCode", "order.popup.expressAlert.courier.fail");
				}
			}
		}
   
		return map;
	}
    
    /**
	 * 주문 > 팝업 > 관세 정보 등록
	 * @param OrderDeliveryData
	 * @param HttpSession
	 * @return int
	 */
    @SuppressWarnings("null")
	public Map<String, Object> insertDeliveryExpress(OrderDeliveryData deliveryData, HttpSession sess) {
        ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
        Map<String, Object> result = new HashMap<String,Object>();

        String sellerName = sessionData.getShopName();
        if(sellerName == null) sellerName = sessionData.getEmail();
        
//        deliveryData.setBuyerCountryCode(orderMapper.selectBuyerCountryCode(deliveryData)); 
        
        //배송 생성/업데이트
        //LOGGER.debug("deliveryData : " + deliveryData.toString());
        
        
        Map<String, Object> map = createDeliveryData(deliveryData);
        int chk = (int) map.get("chk");
        
        int count = deliveryData.getArrGoodsCode().split(",").length;
//        int count = split == null ? 1 : split.length;
//        String[] emptyArray = new String[count];
//        for( int i = 0; i < count; i++ ) {
//        	emptyArray[i] = "";
//        }
               
        String[] arrSkuIdx = UtilFunc.splitComma(deliveryData.getArrSkuIdx(), count);
        String[] arrGoodsCode = UtilFunc.splitComma(deliveryData.getArrGoodsCode(), count);
        String[] arrGoods = UtilFunc.splitComma(deliveryData.getArrGoods(), count);
        String[] arrGoodsSku = UtilFunc.splitComma(deliveryData.getArrGoodsSku(), count);
        String[] arrOrigin = UtilFunc.splitComma(deliveryData.getArrOrigin(), count);
        String[] arrHscode = UtilFunc.splitComma(deliveryData.getArrHscode(), count);
        String[] arrGoodsType = UtilFunc.splitComma(deliveryData.getArrGoodsType(), count);
	    String[] arrRepreNmRu = UtilFunc.splitComma(deliveryData.getArrRepreItemNmRu(), count);
	    String[] arrRepreNm = UtilFunc.splitComma(deliveryData.getArrRepreItemNm(), count);
        String[] arrWeightUnit = UtilFunc.splitComma(deliveryData.getArrWeightUnit(), count);
        
        String[] arrWeight = UtilFunc.splitComma(deliveryData.getArrWeight(), count);
        String[] arrUnitCost = UtilFunc.splitComma(deliveryData.getArrUnitCost(), count);
        String[] arrPriceCurrency = UtilFunc.splitComma(deliveryData.getArrPriceCurrency(), count);
        String[] arrQuantity = UtilFunc.splitComma(deliveryData.getArrQuantity(), count);
        
        String[] arrGoodsItemId = UtilFunc.splitComma(deliveryData.getArrGoodsItemId(), count);
        
        List<OrderDeliverySkuData> skuList = new ArrayList<OrderDeliverySkuData>();
         
        int shopIdx = deliveryData.getShopIdx();
        int orderIdx = deliveryData.getOrderIdx();
        String orderCode = deliveryData.getOrderCode();
        String masterCode = (String) map.get("masterCode");
        
        // 저장용 포장재 정보
        int boxLength = deliveryData.getBoxLength();
        int boxWidth = deliveryData.getBoxWidth();
        int boxHeight = deliveryData.getBoxHeight();
        String boxUnit = deliveryData.getBoxUnit();
        String boxType = deliveryData.getBoxType();
        List<OrderDeliverySkuData> list = new ArrayList<>();
        if(deliveryData.getArrOrderCode()!=null)
        {
        	String[] arrOrder = deliveryData.getArrOrderCode().split(",");
        	for(String ordercode : arrOrder) 
        	{
        		 List<OrderDeliverySkuData> ods = orderMapper.selectOrderItemDetail(ordercode);
        		 list.addAll(ods);
        	}
        }
        
        else {
        	List<OrderDeliverySkuData> ods = orderMapper.selectOrderItemDetail(orderCode);
        	list.addAll(ods);
        }
        
        String[] brand = new String[list.size()];
        String[] itemLink = new String[list.size()];
        
        for(int i=0 ; i < list.size(); i++) {
	       brand[i] = list.get(i).getBrand() ;
	       itemLink[i] =list.get(i).getItemLink();
        }
        
        for (int i = 0; i < arrGoodsCode.length; i++) {
        	OrderDeliverySkuData skuData = new OrderDeliverySkuData();

        	// 특수 문자 치환
        	String goods = util.strReplace(arrGoods[i]);
			String goodsSku = util.strReplace(arrGoodsSku[i]);
        	
        	skuData.setMasterCode(masterCode); 
        	skuData.setOrderIdx(orderIdx);
        	skuData.setOrderCode(orderCode);
        	skuData.setGoodsCode(arrGoodsCode[i]);
        	skuData.setGoods(goods);
        	skuData.setGoodsSku(goodsSku);
        	skuData.setOrigin(arrOrigin[i]);
        	skuData.setRepreItemNm(arrRepreNm[i]);
        	skuData.setHscode(arrHscode[i]);
        	skuData.setGoodsType(arrGoodsType[i]);
        	skuData.setRepreItemNmRu(arrRepreNmRu[i]);
        	skuData.setWeight(arrWeight[i]);
        	skuData.setWeightUnit(arrWeightUnit[i]);
        	skuData.setBoxLength(boxLength);
        	skuData.setBoxWidth(boxWidth);
        	skuData.setBoxHeight(boxHeight);
        	skuData.setBoxUnit(boxUnit);
        	skuData.setBoxType(boxType);
        	skuData.setUnitCost(Double.parseDouble(arrUnitCost[i]));
        	skuData.setQuantity(Integer.parseInt(arrQuantity[i]));
        	skuData.setPriceCurrency(arrPriceCurrency[i]);
        	skuData.setGoodsItemId(arrGoodsItemId[i]);
        	skuData.setBrand(brand[i]);
        	skuData.setItemLink(itemLink[i]);
        	
        	
        	
        	//sku 저장용
        	skuData.setSkuIdx(Integer.parseInt(arrSkuIdx[i]));
        	skuData.setShopIdx(shopIdx);
        	skuData.setItemCode(arrGoodsCode[i]);
        	skuData.setItemName(goods);
        	skuData.setItemType(arrGoodsType[i]);
        	skuData.setItemSku(goodsSku);
        	skuData.setItemPrice(Double.parseDouble(arrUnitCost[i]));
        	skuData.setItemQty(1); //기본 1로 설정
        	skuData.setItemOrigin(arrOrigin[i]);
        	skuData.setItemWeight(arrWeight[i]);
        	skuData.setRegId("order");
        		
        	
        	
        	skuData.setRepreItemNmRu(arrRepreNmRu[i]);
            skuData.setRepreItemNm(arrRepreNm[i]);
             
            if(deliveryData.getBuyerCountryCode().equals("RU") && StringUtils.isBlank(skuData.getRepreItemNmRu()) && StringUtils.isBlank(skuData.getRepreItemNmRu()) ) {
            	result.put("errCode", "order.popup.expressAlert.link.rusia");
            	return result; 
            }
        	skuList.add(skuData);
		}

        //관세정보 업데이트
        if(chk > 0) {
        	for (OrderDeliverySkuData item : skuList) {
        		chk = (orderMapper.selectSettingSkuCount(item) <= 0) ? orderMapper.insertSettingSku(item):orderMapper.updateSettingSku(item);
        		chk = (orderMapper.selectDeliverySkuCount(item) <= 0) ? orderMapper.insertDeliverySku(item):orderMapper.updateDeliverySku(item);
        		// tb_order_detail 의 goods 를 수정함, 에러는 무시함
        		String dbGoods = orderMapper.selectOrderDetailGoods(item);
        		if ( ! item.getGoods().equals(dbGoods) ) {
        			orderMapper.updateOrderDetailGoods(item);  
        		}
			}
        	result.put("errCode", "SUCCESS");
        }
        else {
        	result.put("errCode", "order.popup.expressAlert.link.fail");
        }
        
        return result; 
    }
    
    /**
	 * 주문 > 팝업 > 배송사 정보 정보 등록
	 * @param OrderCourierData
	 * @param HttpSession
	 * @return int
	 */
    public int insertDeliveryCustoms(OrderCourierData courier, HttpSession sess) {
    	ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		LOGGER.debug("insertDeliveryCustoms [courier] : " + courier.toString());
    	int chk = 0;
    	
    	// 기본 배송 정보 체크
    	ShipmentData setting = new ShipmentData();
    	setting.setOrderCode(courier.getOrderCode());
    	setting.setEmail(email);
    	
    	ShipmentData address = orderMapper.selectDeliveryAddress(setting);
    	
    	if(address != null) {
    		// payment 체크
    		OrderCourierData courierData = orderMapper.selectPayment(courier);
    		
    		if(courierData == null) {
    			//courier.setInvoice(createInvoice());
				/*courier.setPayWeight(address.getBoxWeight());
				courier.setPayWeightUnit(address.getWeightUnit());*/
    			courier.setMasterCode(address.getMasterCode());
    			courier.setInvoice(""); // 송장번호 업데이트는 LGL연동 작업
    			courier.setPayWeight((address.getTotalWeight())*1000  +"");
    			LOGGER.debug("*****************address.getTotalWeight : "  + address.getTotalWeight());  
				courier.setPayWeightUnit("g");
    			chk = orderMapper.insertDeliveryPayment(courier);
    			if("create".equals(courier.getProc())){
    				OrderData order = new OrderData();
    				order.setOrderCode(courier.getOrderCode());
    				order.setHideYn("Y");
    				orderMapper.updateOrderhide(order);
    			}
    			
    		}
    		else {
    			courier.setPaymentIdx(courierData.getPaymentIdx());
    			courier.setPayWeight((address.getTotalWeight())*1000 +"");
    			LOGGER.debug("*****************address.getTotalWeight : "  + address.getTotalWeight());  
				courier.setPayWeightUnit("g");
    			chk = orderMapper.updateDeliveryPayment(courier);
    			if("create".equals(courier.getProc())){
    				OrderData order = new OrderData();
    				order.setOrderCode(courier.getOrderCode());
    				order.setHideYn("Y");
    				orderMapper.updateOrderhide(order);
    			}
    		}
    	}
    	
    	return chk; 
    }
    
    
    /**
	 * 주문 > 기본정보 생성
	 * @param OrderDeliveryData
	 * @return Map<String, Object>
	 */
    protected Map<String, Object> createDeliveryData(OrderDeliveryData deliveryData) {
    	
    	// 기본 정보 저장 체크 
    	LOGGER.debug(">>>>>>>>>>>>>>>>>>>>>>>> [createDeliveryData] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    	LOGGER.debug("ORDER_CODE:" + deliveryData.getOrderCode() + "/ORDER_IDX:" + deliveryData.getOrderIdx() + "/SHOP_IDX:" + deliveryData.getShopIdx()+"/MASTER_CODE:" + deliveryData.getMasterCode());
    	//int chk1 = orderMapper.selectDeliveryCount(deliveryData);	// tb_devery 
    	List<OrderDeliveryData> order = orderMapper.selectDeliveryOrderCount1(deliveryData);	// tb_devery_order 
    	String masterCode = "";
    	int chkOrder = order.size();
    	LOGGER.debug("ORDER_SIZE:" + chkOrder);
    	//LOGGER.debug("createDeliveryData ==== chk1 : " + chk1);
    	 int chk1 = 0;
    	 if((deliveryData.getMasterCode() != null && !"".equals(deliveryData.getMasterCode())) || chkOrder > 0) // 같은 창에서 계속 저장시 오류 (조한두 20.05.06)
    		 chk1 = 1;
    	// Delivery 기본 정보 생성
        if(chk1==0) { //입력
        	masterCode = util.getDateElement("full").substring(2,14)+util.getRandomString(4).toUpperCase();
        	deliveryData.setMasterCode(masterCode);
        	
        	orderMapper.insertDeliveryOrder(deliveryData); // tb_delivery_order
//        	splitAddress(deliveryData);
        	LOGGER.debug("**sellercity =" + deliveryData);
        	chk1 = orderMapper.insertDelivery(deliveryData);	// tb_delivery
        	
        } else { 
        	// 조한두 : 한 화면에서 최초 연속 저장 버튼 클릭시 오류 수정 
        	if(deliveryData.getMasterCode() != null && !"".equals(deliveryData.getMasterCode()))
        		masterCode = deliveryData.getMasterCode();
        	else if(chkOrder> 0)
        	{
        		OrderDeliveryData temp = (OrderDeliveryData)order.get(0);
        		masterCode = temp.getMasterCode();
        		deliveryData.setMasterCode(masterCode);
        		LOGGER.debug("getMasterCode:" + masterCode);
        	}
//        	splitAddress(deliveryData);
        	LOGGER.debug("**sellercity =" + deliveryData);
        	chk1 = orderMapper.updateDeliveryOrder(deliveryData); // tb_delivery_order
        	
        }
        
        if("Y".equals(deliveryData.getCombineChk())) { // 합배송 처리
        	String[] arrOrder = deliveryData.getArrOrderCode().split(",");
			String[] arrShop = deliveryData.getArrShopIdx().split(",");
        	
			for (int i = 0; i < arrOrder.length; i++) {
				String code = arrOrder[i].trim();
				String idx = arrShop[i].trim();
				
				if(!"".equals(code) && !"".equals(idx)) {
					OrderData od = new OrderData();
					od.setParentCode(masterCode);
					od.setChildCode(code);
					od.setHideYn("Y");
                    od.setShopIdx(Integer.parseInt(idx));
                    
                    chk1 = insertDeliveryCombine(od); // 합배송 저장
                    
                    od.setOrderCode(code);
                    chk1 = orderMapper.updateOrderhide(od); //베송 처리
				}
			}
        	//orderData.setParentCode(masterCode);
        	//orderData.setChildCode(deliveryData.getChildCode());
        	//chk1 = insertDeliveryCombine(orderData);
        }
    	
        LOGGER.debug("createDeliveryData [createDeliveryData] : " + deliveryData.toString());
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("chk", chk1);
        map.put("orderIdx", deliveryData.getOrderIdx());
        map.put("masterCode", masterCode);
    	return map; 
    }

//    private void splitAddress(OrderDeliveryData deliveryData) {
//		LOGGER.debug("dd=" + deliveryData );
//		
//		if(deliveryData.getSellerAddr1() !="" && deliveryData.getSellerAddr1() != null) {
//			String[] array = deliveryData.getSellerAddr1().split("\\s+");
//			deliveryData.setSellerCity(array[0]);
//		}else {
//			LOGGER.debug("***관세정보 입력*********");
//			return;
//		}
//		
//		
//		return ;
//	}

	/**
	 * 주문 > 특송 주문 번호 생성
	 * @param 
	 * @return String
	 */
    public String createInvoice(){
    	return "DE" + util.getDateElement("full").substring(2,12) + util.randomNumber(4,4);
    }
    
    /**
	 * 주문 > 배송 목록
	 * @param OrderDeliveryData
	 * @return List<OrderDeliveryData>
	 */
    public List<OrderDeliveryData> selectDelivery(OrderDeliveryData deliveryData){
        
        return orderMapper.selectDelivery(deliveryData);
    }
    
    /**
     * 국내 배송을 한번에 받아 3가지 service 를 순차적으로 호춣함
     * @param inputData
     * @param sess
     * @return
     */
	public int orderDeliveryFacade(OrderInputData inputData, HttpSession sess) {

		OrderDeliveryData delivery = new OrderDeliveryData();
		BeanUtils.copyProperties(inputData, delivery);
		
		int chk = insertDelivery(delivery, sess);
		
		if ( chk != 0 ) {
			Map<String, Object> map = insertDeliveryExpress(delivery, sess);
			String errCode = (String) map.get("errCode");
			
			if ( "SUCCESS".equals(errCode) ) {
				OrderCourierData courier = new OrderCourierData();
				BeanUtils.copyProperties(inputData, courier);
				
		        chk = insertDeliveryCustoms(courier, sess);
			} else {
				chk = 0;
			}
		}
		  
		return chk;
	}
	
	public List<LocalDeliveryData> selectLocalDeliverytList(LocalDeliveryData localdata, HttpSession sess) {
		return orderMapper.selectLocalDelivery(localdata);
	}
	
	
}
