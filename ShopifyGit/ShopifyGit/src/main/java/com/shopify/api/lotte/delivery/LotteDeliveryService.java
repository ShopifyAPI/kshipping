package com.shopify.api.lotte.delivery;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.shopify.api.LotteLogisApiService;
import com.shopify.common.RestService;
import com.shopify.mapper.LotteMapper;
import com.shopify.mapper.ShipmentMapper;
import com.shopify.payment.popup.PaymentPopupRestService;
import com.shopify.pdf.LotteWaybill3PService;
import com.shopify.schedule.CronJobData;
import com.shopify.shipment.ShipmentData;
import com.shopify.shipment.TrackingData;
import com.shopify.shipment.popup.ShipmentPopupData;

@Service
@Transactional
public class LotteDeliveryService {
	
	private Logger LOGGER = LoggerFactory.getLogger(LotteDeliveryService.class);
	
	@Value("${lotte.zipcode.api.url}")
	private String lotteZipcodeUrl; // 롯데 택배 주소 URL
	
	@Value("${lotte.delivery.api.url}")
	private String lotteHomeUrl; // 롯데 택배 주문 URL
	
	@Value("${lotte.delivery.generate.invno}")
	private boolean generateInvno; // 운송장번호 생성 여부
	
	@Value("${lotteLogis.delivery.jobcustcd}")
	private String lotteJobcustcd; // 거래처코드
	
	@Value("${lotteHome.tracking.url}")
	private String lotteHomeTrackingUrl; //tracking url
	
	@Value("${lotteHome.vendor.tracking.url}")
	private String lotteHomeVendorTrackingUrl; // 롯데 택배 tracking  URL
	
	@Value("${lotteLogis.code.success}") 	
	private String successCode;
	
	@Value("${lotteLogis.code.company}") 	
	private String lotteCompanyCode;

	@Value("${lotteLogis.home.courier}") 	
	private String lotteHomeCourier;
	
	@Autowired private PaymentPopupRestService paymentPopupRestService;
	@Autowired private LotteLogisApiService lotteLogisApiService;
	@Autowired private ShipmentMapper shipmentMapper;
	
	@Autowired private RestService restService;
	
	@Autowired private LotteMapper lotteMapper;
	@Autowired private LotteWaybill3PService lotteWaybill3PService;
	
	private Map<String, String> lotteTrackingCodeMap;

	
	@PostConstruct
	private void postConstruct() {
		
		String[][] codeSet = {
				{ "10", "A020040" },	// 10	집하,   		 -> A020040		집하(국내)
				{ "12", "A020040" },	// 12	운송장등록,    -> A020040		집하(국내)
				
				{ "20", "A020045" },	// 20	발송(구간/셔틀),    -> A020045  배송중(국내)
				{ "21", "A020045" },	// 21	도착(구간/셔틀),    -> A020045  배송중(국내)
				{ "40", "A020045" },	// 40	배달전,    		-> A020045  배송중(국내)

				{ "41", "A020049" },	// 41	배달완료,    -> A020049	  배송완료(국내)
				{ "45", "A020049" }		// 45	인수자등록,    -> A020049	  배송완료(국내)
		};

		lotteTrackingCodeMap = Arrays.stream(codeSet).collect(Collectors.toMap(e -> e[0], e -> e[1]));
	}
	

	public String popPaymentShipmentProc(HttpHeaders headers, ShipmentPopupData shipPopData) {
		String result = null;
		
		String masterCode = shipPopData.getMasterCode();
		ShipmentData shipData = lotteMapper.selectLotteDelivery(masterCode);
		
		// 1. 주소 정제 api 호출
		result = callLotteHomeAddrApi(headers, shipData);
		if ( result !=  null ) {
			return result;
		}
		
		// 2. 주문 api 호출
		Map<String, String> resultMap = callLotteHomeDeliveryApi(headers, shipData);
		if ( resultMap.get("result") !=  null ) {
			result = resultMap.get("result");
			return result;
		}
		Map<String, Object> trackingMap = composeTrackingUrl(resultMap.get("invNo"));
		
		// 3. shopify api 호출
		String[] masterCodeList = new String[1];
		masterCodeList[0] = masterCode;
		boolean flag = paymentPopupRestService.shopifyPostOrderIdFullist(masterCodeList, trackingMap);
		
		if  ( flag == false ) {
			result = masterCode + " : error from calling shopify api call ";
			return result;
		}
		
		// 4. delivery state update
		List<ShipmentData> list = new ArrayList<>();
		list.add(shipData);
		lotteLogisApiService.updateDeliveryState(list, successCode);
		

		return result;
	}
	
	private Map<String, Object> composeTrackingUrl(String invNo) {
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("tracking_number", invNo); 
		map.put("tracking_number", invNo); 
		map.put("tracking_company", "Lotte Global Logistics"); 
		map.put("tracking_urls", Arrays.asList(lotteHomeTrackingUrl + invNo));
		map.put("notify_customer", true);
		
		return map;
	}

	
	private String callLotteHomeAddrApi(HttpHeaders headers, ShipmentData shipData) {
		String result = null;
		
		LotteZipData zipData = new LotteZipData();
		zipData.setId(lotteJobcustcd);
		zipData.setNetwork("00");
		zipData.setAreaNo(shipData.getBuyerZipCode());
		zipData.setZipNo("");
		zipData.setAddress( shipData.composeFulladdress() );
		
		// 1. rest call
		JsonNode node = restService.postForEntity(headers, this.lotteZipcodeUrl, zipData, LotteZipData.class);
		
		if ( node == null ) {
			result = shipData.getMasterCode() + " : unknown error LG logistics address api call ";
			return result;
		}
		
		// 2 response 분석
		LotteAddressResultData zipResult = restService.treeToValue(node, LotteAddressResultData.class);
		 
		if ( "error".equals(zipResult.getResult())) {
			result = shipData.getMasterCode() + " : " + zipResult.getMessage();
			return result;
		}
		
		// 3. table insert
		zipResult.setMasterCode(shipData.getMasterCode());
		int count = lotteMapper.insertLotteAddress(zipResult);
		
		LOGGER.debug("insertLotteAddress count = " + count);
		
		return result;
	}
	
	private Map<String, String> callLotteHomeDeliveryApi(HttpHeaders headers, ShipmentData shipData) {
		String result = null;
		Map<String, String> resultMap = new HashMap<>();
		
		String masterCode = shipData.getMasterCode();
	
		List<String> list = lotteMapper.selectLotteDeliverySku(masterCode);
		
		String gdsNames = restService.writeValueAsString(list);
		
		
		/**
		 * 롯데의 개발 API 가 오류가 발생하여 개발환경에서도 운영 API 를 호출해야 하므로
		 * 운영이외의 환경에서는 운송장번호 생성기를 실행하지 않도록 하고,
		 * 운영에서만 새로운 번호를 생성하도록 프로그램에서 분기한다.
		 */
		// 1. 운송장 번호 생성
		LotteInvnoData seq;
		if ( generateInvno == true ) {
			seq = new LotteInvnoData();
			seq.setMasterCode(masterCode);
			lotteMapper.insertLotteInvnoGenerator(seq);
		} else {
			seq = lotteMapper.selectLotteInvnoGenerator();
		}
		
		ResultPair pair = LotteUtil.buildInvno(seq.getInvNo());
		 
		if ( pair.getMsg() != null ) {
			result = shipData.getMasterCode() + " : " + pair.getMsg();
			resultMap.put("result", result);
			return resultMap;
		}
		String invNo = pair.getData();
		
		// 2. 운송장 data 생성
		LotteDeliveryData ldd = LotteUtil.toLotteDevlieryData(invNo, lotteJobcustcd, shipData, list, gdsNames);
		ldd.setMasterCode(masterCode);
		
		LotteDeliveryDataList deliveryList = new LotteDeliveryDataList();
		deliveryList.addList(ldd);
		
		////////////////////////////////////////////////////////
		if ( generateInvno == false ) {
			// 3. table insert
			lotteMapper.insertLotteWaybill(ldd);
			
			resultMap.put("result", result);
			resultMap.put("invNo", invNo);
			return resultMap;
		}
		//////////////////////////////////////////////////////////
		
		// 3. 롯데 주문 API 호출
		JsonNode node = restService.postForEntity(headers, this.lotteHomeUrl, deliveryList, LotteDeliveryDataList.class);
		
		if (node == null ) {
			result = shipData.getMasterCode() + " : unknown error LG logistics delivery api call ";
			resultMap.put("result", result);
			return resultMap;
		}
		
		
		// 2 response 분석
		LotteDeliveryResultList resultList = restService.treeToValue(node, LotteDeliveryResultList.class);
		
		if ( generateInvno == true ) {
			if ( "success".equals(resultList.getStatus()) ) {
				LotteDeliveryResult resultData = resultList.get(0);
				if ( resultData != null ) {
					if ( "S".equals(resultData.getRtnCd()) ) {
						// 3. table insert
						lotteMapper.insertLotteWaybill(ldd);
					} else {
						result = shipData.getMasterCode() + " : " + resultData.getRtnMsg();
					}
				} else {
					result = shipData.getMasterCode() + " : " + resultList.getMessage();
				}
			} else {
				result = shipData.getMasterCode() + " : " + resultList.getMessage() + " (" + resultList.getCode() + ")";
			}
		} else {
			LOGGER.debug( "운영환경 아니라서 에러나도 진행함");
			lotteMapper.insertLotteWaybill(ldd);
		}
		
		resultMap.put("result", result);
		resultMap.put("invNo", invNo);
		return resultMap;
	}
	

    public ByteArrayInputStream generateLotteWaybill(String masterCodeList) {
    	
    	  String[] codeAry = masterCodeList.split(",");
          List<LotteAddressResultData> addressList = lotteMapper.selectLotteAddress(codeAry);
          List<LotteDeliveryData> deliveryList = lotteMapper.selectLotteWaybill(codeAry);
          
          Map<String, LotteAddressResultData> addressMap = addressList.parallelStream().collect(Collectors.toMap(LotteAddressResultData::getMasterCode, Function.identity()));
          Map<String, LotteDeliveryData> deliveryMap = deliveryList.parallelStream().collect(Collectors.toMap(LotteDeliveryData::getMasterCode, Function.identity()));
          
          return lotteWaybill3PService.generate(codeAry, addressMap, deliveryMap);
    }
    
    
    // read only  : transaction 이 없다.
    @Transactional(readOnly = true)  
    public List<ShipmentData> listLotteHomeTracking() {
		
		ShipmentData ship = new ShipmentData();
		ship.setCourierCompany(lotteCompanyCode); // 롯데 로지스
		ship.setCourier(lotteHomeCourier);        // 롯데 국내택배
		
//		List<ShipmentData> list = new ArrayList<>();
//		ShipmentData data = new ShipmentData();
////		data.setMasterCode("200805141511B2X5");
//		data.setMasterCode("200805193248IRF3");
//		data.setInvNo("402463600210");
//		list.add(data);
//		return list;
//		
		return shipmentMapper.listLotteHomeTracking(ship); 		
	}
	
    public void scheduledLotteHomeTracking(CronJobData cronData, HttpHeaders headers, ShipmentData item, int seq) {

    		
    	// 1. lotte 에서 trackinig 정보 가져 오기
    	Map<String, String> resultMap = callLotteHomeTrackingApi(headers, item);
    	if ( ! resultMap.get("flag").equals("true")) {
    		cronData.setData("F", resultMap.get("result"));
    		return;
    	}
    	
    	// 2. update 배송 상태 table
    	String  code = (String) resultMap.get("result");
		String result = updateTrackingDelivery(item, code, seq);
		if ( result != null ) {
			cronData.setData("F", result);
		} else {
			cronData.setData("S", item.getMasterCode());
		}
    }
    

	private String updateTrackingDelivery(ShipmentData item, String code, int seq) {
		
		String result = null;
		
		String state = lotteTrackingCodeMap.get(code);
		if ( state == null ) {
			result = item.getMasterCode() + " : 롯데택배 state code 가 정의되지 않았습니다 = " + code;
			return result;
		}
		
		TrackingData data = new TrackingData();
		data.setMasterCode(item.getMasterCode());
		data.setState(state);
		
		int ret = shipmentMapper.updateTrackingDelivery(data);
		
		return result;
	}

	private Map<String, String> callLotteHomeTrackingApi(HttpHeaders headers, ShipmentData shipData) {
		
		String result = null;
		String flag = "false";
		Map<String, String> resultMap = new HashMap<>();
		
		String url = this.lotteHomeVendorTrackingUrl + shipData.getInvNo();
		
		// 1. 롯데 tracking API 호출
		JsonNode node = restService.getRestTemplate(headers, url);
		
		if (node == null ) {
			result = shipData.getMasterCode() + " : Lotte tracking url 호출 에러 발생  = " + url;
		} else {
			// 2 response 분석
			JsonNode tracking = node.findPath("tracking");
			if (tracking.isArray()) {
				int size = tracking.size();
				if ( size > 0 ) {
					result = tracking.get(size - 1).findPath("GODS_STAT_CD").asText();
					flag = "true";
				}
			} else {
				result = node.toString();
			}
			
		}
		
		resultMap.put("flag", flag);
		resultMap.put("result", result);
		
		return resultMap;
	}
	
}
