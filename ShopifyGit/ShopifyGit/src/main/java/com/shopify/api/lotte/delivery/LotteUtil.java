package com.shopify.api.lotte.delivery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shopify.common.util.UtilFunc;
import com.shopify.order.OrderData;
import com.shopify.shipment.ShipmentData;

public class LotteUtil {
	
	private static Logger LOGGER = LoggerFactory.getLogger(LotteUtil.class);
	
	private static final Map<String, String> provinceMap = new HashMap<>();
	
	static {
			provinceMap.put("gangwon", "강원도");
			provinceMap.put("gyeonggi", "경기도");
			provinceMap.put("gyeongnam", "경상남도");
			provinceMap.put("gyeongbuk", "경상북도");
			provinceMap.put("gwangju", "광주광역시");
			provinceMap.put("daegu", "대구광역시");
			provinceMap.put("daejeon", "대전광역시");
			provinceMap.put("busan", "부산광역시");
			provinceMap.put("seoul", "서울특별시");
			provinceMap.put("sejong", "세종특별자치시");
			provinceMap.put("ulsan", "울산광역시");
			provinceMap.put("incheon", "인천광역시");
			provinceMap.put("jeonnam", "전라남도");
			provinceMap.put("jeonbuk", "전라북도");
			provinceMap.put("jeju", "제주특별자치도");
			provinceMap.put("chungnam", "충청남도");
			provinceMap.put("chungbuk", "충청북도");
	}
	
	
	public static LotteDeliveryData toLotteDevlieryData(String invNo, String lotteJobcustcd, ShipmentData shipData, List<String> list, String gdsNames) {
		LotteDeliveryData ldd = new LotteDeliveryData();
		
		String sellerPhone = UtilFunc.getAESDecrypt(shipData.getSellerPhone());
		
		ldd.setMasterCode(shipData.getMasterCode());  // 마스터코드
		ldd.setOrderCode(shipData.getOrderCode());  // 주문번호
		ldd.setOrderName(shipData.getOrderName());  // 주문명
		ldd.setMasterCode(shipData.getMasterCode());  // 주문번호
		ldd.setInvNo(invNo);  // 운송장 번호
		ldd.setOrglInvNo("");                   // 원송장 번호
		
		ldd.setJobCustCd(lotteJobcustcd);
		ldd.setUstRtgSctCd("02");
		ldd.setOrdSct("1");
		ldd.setFareSctCd("03");
		
		ldd.setSnperNm( UtilFunc.getAESDecrypt(shipData.getSellerName()) );
		ldd.setSnperTel(UtilFunc.DeliverySellerPhone(sellerPhone));
		ldd.setSnperCpno("");
		ldd.setSnperZipcd( shipData.getSellerZipCode() );
		ldd.setSnperAdr( shipData.getSellerAddr1() + " " + shipData.getSellerAddr2() );
		
		ldd.setAcperNm( UtilFunc.getAESDecrypt(shipData.getBuyerLastname()) + " " + UtilFunc.getAESDecrypt(shipData.getBuyerFirstname()) );
		ldd.setAcperTel( UtilFunc.getAESDecrypt(shipData.getBuyerPhone()) );
		ldd.setAcperCpno( "" );
		ldd.setAcperZipcd( shipData.getBuyerZipCode());
		ldd.setAcperAdr(  shipData.composeFulladdress() );
		
		ldd.setBoxTypCd( shipData.getBoxType());
		
		ldd.setGdsNm(String.join(" / ", list));
		ldd.setGdsNames(gdsNames);
		
		
		ldd.setDlvMsgCont("");         // 배달메세지내용
		ldd.setCusMsgCont("");			//고객메세지내용
		 
		ldd.setPickReqYmd(UtilFunc.plusDays(0, "yyyyMMdd"));			//집하요청일
		ldd.setBdpkSctCd("N");			//합포장여부(Y/N)
		ldd.setBdpkKey("");				//합포장 KEY;
		ldd.setBdpkRpnSeq("");	

		
		
		return ldd;
	}

	public static void convertProvince(OrderData od) {
		if ( "KR".equals(od.getBuyerCountryCode())) {
			String province = od.getBuyerProvince().toLowerCase();
			if ( provinceMap.containsKey(province)) {
				LOGGER.error("주소의 '시' 영문 이름 found : " + od.getBuyerProvince());
				od.setBuyerProvince( provinceMap.get(province) );
			} else {
				LOGGER.error("ACTION: 주소의 '시' 영문 이름이 등록되지 않았습니다. : " + od.getBuyerProvince());
			}
		}
	}
	


	public static ResultPair buildInvno(long invNo) {
		
		ResultPair result = new ResultPair();
		
		if ( invNo < 40246360001L || invNo > 40246460000L ) {
			result.setMsg("생성되는 운송장 번호가 지정된 범위가 아닙니다.(" + invNo + ")");
		} else {
			long mod = invNo % 7;
			result.setData( Long.toString(invNo) + Long.toString(mod) );
		}
		
		return result;
	}

}
