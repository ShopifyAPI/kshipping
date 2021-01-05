package com.shopify.api;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.common.RestData;
import com.shopify.common.RestService;
import com.shopify.common.SpConstants;
import com.shopify.common.util.MessageDigestUtils;
import com.shopify.common.util.UtilFn;
import com.shopify.common.util.UtilFunc;
import com.shopify.mapper.ShipmentMapper;
import com.shopify.mapper.ShopMapper;
import com.shopify.shipment.ShipmentData;
import com.shopify.shipment.TrackingData;
import com.shopify.shipment.popup.ShipmentPopupData;
import com.shopify.shop.ShopData;

/**
* 롯데 로지스 연동 서비스
* @author : jwh
* @since  : 2020-02-11
* @desc   : 롯데 로지스 연동(특송/택배, 화물추적)
*/

@Service
public class LotteLogisApiService {
	private Logger LOGGER = LoggerFactory.getLogger(LotteLogisApiService.class);
	
	@Autowired private RestTemplate restTemplate;
	@Autowired private UtilFn util;
	
	@Autowired private ShipmentMapper shipmentMapper;
	@Autowired	private RestService restService;
	@Autowired	private ShopMapper shopMapper;
	@Autowired private ExchangeApiService exchangeApi;
	private ObjectMapper objectMapper = new ObjectMapper();
	
	//properties/lotteLogis.properties 설정
	@Value("${lotteLogis.api.uri}") 		String requestUri;
	@Value("${lottte.logis.jobcustcd}") 	String jobCustCd;
	@Value("${lotteLogis.sign.key}") 		String signKey;
	@Value("${lotteLogis.sing.type}") 		String signType;
	@Value("${lotteLogis.sing.charset}") 	String charSet;
	@Value("${lotteLogis.code.order}") 		String orderCode;
	@Value("${lotteLogis.code.tracking}") 	String trackingCode;
	@Value("${lotteLogis.code.success}") 	String successCode;
	@Value("${lotteLogis.code.error}") 		String errorCode;
	@Value("${lotteLogis.code.company}") 	String companyCode;
	
	@Value("${lotteLogis.pick.time}") 		int pickCheckTime;
	@Value("${lotteLogis.acper.name}") 		String acperNm;
	@Value("${lotteLogis.acper.tel}") 		String acperTel;
	@Value("${lotteLogis.acper.cpno}") 		String acperCpno;
	@Value("${lotteLogis.acper.zipcode}") 	String acperZipcd;
	@Value("${lotteLogis.acper.addr}") 		String acperAdr;
	@Value("${lotteLogis.currency}") 		String currency;
	@Value("${lotte.api.test}")    			String isTestMode;     
	@Value("${lotteLogis.code.tracking}")	String tracking;
	String custCode="SLGATE";
	
	@Value("${Fulfillment.Get.Event.Create.Step1.full}")  private String getFulfillmentCreateStep1Url;
	@Value("${Fulfillment.Put.Event.Create.Step1.full}") private String puttFulfillmentUpdateStep1Url;
	
	@Value("${lotte.tracking.url.lglUs}") private String usTrackingkUrl;
	@Value("${lotte.tracking.url.lglRu}") private String ruTrackingUrl;
	@Value("${lotte.tracking.url.ems}") private String emsTrackingUrl;
	@Value("${lotte.tracking.url.ups}") private String upsTrackingUrl;
	@Value("${lotte.tracking.url.delivery}") private String deliveryTrackingUrl;
	
	
	
	/**
	 * 롯데 로지스 API 연동
	 * @param PaymentPopupData
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> requestLotteLogisDelivery(ShipmentPopupData shipPopData, String email) {	
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			shipPopData.setEmail(email);
			//String[] masterCodeList = shipPopData.getMasterCodeList();
			
			// localCode select
			List<ShipmentPopupData> localCodeList = shipmentMapper.selectLocalCodeList(shipPopData);
			
			LOGGER.debug("localCodeList : " + localCodeList.toString());
			LOGGER.debug("localCodeList[size] :" + localCodeList.size());
			
			if(localCodeList.size() > 0) { //국내 택배 설정
				
				LOGGER.debug("collectCargoYn : Y");
				
				for (ShipmentPopupData deliveryList : localCodeList) {
					String collectCargoYn = "Y";
					
					JSONObject jsonObject = new JSONObject(); // 전체 json파라메터
					
					shipPopData.setLocalCode(deliveryList.getLocalCode());
					 
					// 특송 리스트 생성
					List<ShipmentData> LocalCodeDelivery = shipmentMapper.selectLocalCodeDelivery(shipPopData);
					
					// json 생성
					Map<String, Object> jsonOrder = makeJsonOrder(LocalCodeDelivery, collectCargoYn, shipPopData);
					
					jsonObject.put("collectCargoYn", collectCargoYn);
					jsonObject.put("isTestMode", isTestMode);  
					jsonObject.put("collectCargo", jsonOrder.get("collectCargo"));
					jsonObject.put("orders", jsonOrder.get("orders"));
					
					LOGGER.debug("jsonObject : " + jsonObject.toString());
					
					Map<String, Object> send = sendLotteLogis(jsonObject, orderCode);
					if("true".equalsIgnoreCase((String) send.get("success"))) { //성공
						updateDeliveryState(LocalCodeDelivery, successCode);
						map.put("jsonData", send.get("jsonData"));
						map.put("result", 1);
						
						
					} else { // 실패
						updateDeliveryState(LocalCodeDelivery, errorCode);

					}
					String jsonString = jsonObject.toString();
					String sendString = send.toString();
					ShipmentPopupData spd = new ShipmentPopupData();
					spd.setApiParamJson(jsonString);
					spd.setApiReturnJson(sendString);
					//spd.setMasterCode(deliveryList.getMasterCode()); /// null 국내 픽업 선택시 오류 : 화면에서 값이 넘어옴: paymentList.jsp : /popPaymentShipmentProc (배송->팝업->결제처리)
					LOGGER.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					LOGGER.debug("deliveryList>>"+ deliveryList.toString());
					spd.setMasterCode(shipPopData.getMasterCode()); // 조한두 (2020.04.24)
					shipmentMapper.insertShipmentPaymentLotteApi(spd);
				}
			} else {
				String collectCargoYn = "N";
				JSONObject jsonObject = new JSONObject(); // 전체 json파라메터
				
				LOGGER.debug("collectCargoYn : " + collectCargoYn);
				LOGGER.debug("collectCargoYn [getMasterCodeList] : " + shipPopData.getMasterCodeList().length);
				
				// 특송 리스트 생성
				List<ShipmentData> deliveryList = shipmentMapper.selectDeliveryList(shipPopData);
				
				LOGGER.debug("collectCargoYn [deliveryList] : " + deliveryList.toString());
				
				// json 생성
				Map<String, Object> jsonOrder = makeJsonOrder(deliveryList, collectCargoYn, shipPopData);
				
				jsonObject.put("collectCargoYn", collectCargoYn);
				jsonObject.put("isTestMode", isTestMode);
				jsonObject.put("collectCargo", jsonOrder.get("collectCargo"));
				jsonObject.put("orders", jsonOrder.get("orders"));
				
				LOGGER.debug("jsonObject : " + jsonObject.toString());
/*				orderIdx = oData.get(0).getOrderIdx();*/	

				String LocalShipmentIndex = deliveryList.get(0).getBuyerCountryCode();
				/* if(deliveryList.get(index)) */
				Map<String, Object> send = sendLotteLogis(jsonObject, orderCode);
				if("true".equalsIgnoreCase((String) send.get("success"))) { //성공
					updateDeliveryState(deliveryList, successCode);
					map.put("jsonData", send.get("jsonData"));
					map.put("result", 1);

				} else { // 실패
					updateDeliveryState(deliveryList, errorCode);
				}
				String jsonString = jsonObject.toString();
				String sendString = send.toString();
				ShipmentPopupData spd = new ShipmentPopupData();
				spd.setApiParamJson(jsonString);
				spd.setApiReturnJson(sendString);
				spd.setMasterCode(shipPopData.getMasterCode());
				shipmentMapper.insertShipmentPaymentLotteApi(spd);
			}			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("### Lotte delivery API 연동중 오류 ###"+e.getMessage());
		}
		
		
		
		
		return map;
	}
	
	/**
	 * 롯데 로지스 API 연동 > 스케줄링(CronTable.java) > 배송상태 확인 
	 * @return void
	 * @throws JSONException 
	 */
	public void scheduledLotteTracking() throws JSONException {
		LOGGER.debug("## LotteLogisApiService >>> scheduledLotteTracking >>> Start");
		
		ShipmentData ship = new ShipmentData();
		ship.setCourierCompany(companyCode); // 롯데 로지스
		
		JSONObject jsonObject = new JSONObject(); // 전체 json파라메터
		
		//국내택배
		List<ShipmentData> listND = shipmentMapper.selectShipmentTrackingLocalList(ship); 		
		ShipmentData test = new ShipmentData();
		//test.setMasterCode("200226150644s741");
		//listND.set(0, test);
		for (ShipmentData item : listND) {
			jsonObject = new JSONObject();
			jsonObject.put("custCode", custCode);
			jsonObject.put("tagBarcode", item.getMasterCode());
			
			try {
				Map<String, Object> send = sendLotteLogis(jsonObject, trackingCode);
				if("true".equalsIgnoreCase((String) send.get("success"))) { //성공
					//tranType(A:택배/ C:센터/ L:해외특송사), trackingNo, trackingCode, trackingName, location, trackingYmd, trackingTime
					//10:집하 , 30:배송중, 41:배송완료, 50:해외발송준비(C), 60:접수, 70:현지도착, 80:현지통관중,90:현지배송중,99:배송완료
					//send.tracking
					JSONObject info = (JSONObject) send.get("jsonData");
					LOGGER.debug(">info>" + info.toString());
					JSONArray trackingList = info.getJSONArray("trackingList");
					if(trackingList != null)
					{
						LOGGER.debug(">trackingList>" + trackingList.toString());
						// 디비 처리 
						lotteTrackingUpdate(item.getMasterCode(), trackingList);
						// Tracking 정보 Shopify 업데이트 처리
						shopifyPostOrderIdFullist(item.getMasterCode(), trackingList);
					}
				}
			} catch (Exception e) {
				LOGGER.debug("scheduledLotteTracking [Exception] : " + e.toString());
			}
			
		}
		
		//국외특송
		List<ShipmentData> listNA = shipmentMapper.selectShipmentTrackingList(ship); 
		
		for (ShipmentData item : listNA) {
			jsonObject = new JSONObject();
			jsonObject.put("custCode", custCode);
			jsonObject.put("tagBarcode", item.getMasterCode());
			
			try {
				Map<String, Object> send = sendLotteLogis(jsonObject, trackingCode);
				if("true".equalsIgnoreCase((String) send.get("success"))) { //성공
					//tranType(A:택배/ C:센터/ L:해외특송사), trackingNo, trackingCode, trackingName, location, trackingYmd, trackingTime
					//10:집하 , 30:배송중, 41:배송완료, 50:해외발송준비(C), 60:접수, 70:현지도착, 80:현지통관중,90:현지배송중,99:배송완료
					//send.tracking
					JSONObject info = (JSONObject) send.get("jsonData");
					LOGGER.debug(">info>" + info.toString());
					JSONArray trackingList = info.getJSONArray("trackingList");
					if(trackingList != null)
					{
						LOGGER.debug(">trackingList>" + trackingList.toString());
						// 디비 처리 
						lotteTrackingUpdate(item.getMasterCode(), trackingList);
						// Tracking 정보 Shopify 업데이트 처리
						shopifyPostOrderIdFullist(item.getMasterCode(), trackingList);
					}
				}
			} catch (Exception e) {
				LOGGER.debug("scheduledLotteTracking [Exception] : " + e.toString());
			}
			
		}
	}
	
	/**
	 * 롯데 로지스 API 연동 > 스케줄링(CronTable.java) > 배송상태 확인 
	 * @return Map<String, Object>
	 * @throws JSONException 
	 */
	public Map<String, Object> requestLotteTracking(ShipmentPopupData ship) {
		Map<String, Object> map = new HashMap<String, Object>();
		LOGGER.debug("## LotteLogisApiService >>> requestLotteTracking >>> Start");
		String trackingNoList = "";
		String trackingCodeList = "";
		ship.setCourierCompany(companyCode); // 롯데 로지스
		LOGGER.debug("## LotteLogisApiService >>> requestLotteTracking >>> ship =>"+ship);
		
		JSONObject jsonObject = new JSONObject(); // 전체 json파라메터
		 
		//국내택배
		List<ShipmentPopupData> listND = shipmentMapper.selectShipmentTrackingMasterCodeNDList(ship); 		
		ShipmentData test = new ShipmentData();
		try {
//			listND.set(0, test);
			for (ShipmentPopupData item : listND) {
				jsonObject = new JSONObject();
				//jsonObject.put("custCode", jobCustCd);
				jsonObject.put("custCode", custCode);
				jsonObject.put("tagBarcode", item.getMasterCode());
				Map<String, Object> send = sendLotteLogis(jsonObject, trackingCode);
				 
				LOGGER.debug("requestLotteTracking [send] : " + send.toString());
				if("true".equalsIgnoreCase((String) send.get("success"))) { //성공
					//tranType(A:택배/ C:센터/ L:해외특송사), trackingNo, trackingCode, trackingName, location, trackingYmd, trackingTime
					//10:집하 , 30:배송중, 41:배송완료, 50:해외발송준비(C), 60:접수, 70:현지도착, 80:현지통관중,90:현지배송중,99:배송완료
					
					//send.tracking
					JSONObject info = (JSONObject) send.get("jsonData");
					
					//if("1000".equalsIgnoreCase((String) send.get("errorMsg"))) { //1000 배송 정보 
						JSONArray trackingList = info.getJSONArray("trackingList");
						
						LOGGER.debug("requestLotteTracking [trackingList] : " + trackingList.toString());
						map.put("trackingList", trackingList);
						if(trackingList != null && trackingList.length() > 0) {
							for(int i=0; i<trackingList.length(); i++) {
								JSONObject tList = new JSONObject();
								tList = (JSONObject) trackingList.get(i);
								if(trackingNoList.equals("")) {
									trackingNoList = tList.getString("trackingNo");
								} else {
									trackingNoList = trackingNoList + "," + tList.getString("trackingNo");
								}
								if(trackingCodeList.equals("")) {
									trackingCodeList = tList.getString("trackingCode");
								} else {
									trackingCodeList = trackingCodeList + "," + tList.getString("trackingCode");
								}
							}
							LOGGER.debug("## LotteLogisApiService >>> requestLotteTracking >>> trackingNoList =>"+trackingNoList);
							map.put("trackingNoList", trackingNoList);
							map.put("trackingCodeList", trackingCodeList);
						}
						// 디비 처리 
						lotteTrackingUpdate(item.getMasterCode(), trackingList);
						// Tracking 정보 Shopify 업데이트 처리
						shopifyPostOrderIdFullist(item.getMasterCode(), trackingList);
					//}
				}

				
			}
			
			//국외특송
			List<ShipmentPopupData> listNA = shipmentMapper.selectShipmentTrackingMasterCodeNAList(ship); 
			
			for (ShipmentPopupData item : listNA) {
				jsonObject = new JSONObject();
				jsonObject.put("custCode", custCode);
				jsonObject.put("tagBarcode", item.getMasterCode());
				

				Map<String, Object> send = sendLotteLogis(jsonObject, trackingCode);
				LOGGER.debug("requestLotteTracking [send2] : " + send.toString());
				if("true".equalsIgnoreCase((String) send.get("success"))) { //성공
					//tranType(A:택배/ C:센터/ L:해외특송사), trackingNo, trackingCode, trackingName, location, trackingYmd, trackingTime
					//10:집하 , 30:배송중, 41:배송완료, 50:해외발송준비(C), 60:접수, 70:현지도착, 80:현지통관중,90:현지배송중,99:배송완료
					
					//send.tracking
					JSONObject info = (JSONObject) send.get("jsonData");
					
//					if("1000".equalsIgnoreCase((String) send.get("errorMsg"))) { //1000 배송 정보 
						JSONArray trackingList = info.getJSONArray("trackingList");
						
						LOGGER.debug("requestLotteTracking [trackingList] : " + trackingList.toString());
						map.put("trackingList", trackingList);
						if(trackingList != null && trackingList.length() > 0) {
							for(int i=0; i<trackingList.length(); i++) {
								JSONObject tList = new JSONObject();
								tList = (JSONObject) trackingList.get(i);
								if(trackingNoList.equals("")) {
									trackingNoList = tList.getString("trackingNo");
								} else {
									trackingNoList = trackingNoList + "," + tList.getString("trackingNo");
								}
								if(trackingCodeList.equals("")) {
									trackingCodeList = tList.getString("trackingCode");
								} else {
									trackingCodeList = trackingCodeList + "," + tList.getString("trackingCode");
								}
							}
							LOGGER.debug("## LotteLogisApiService >>> requestLotteTracking >>> trackingNoList 2=>"+trackingNoList);
							map.put("trackingNoList", trackingNoList);
							map.put("trackingCodeList", trackingCodeList);
						}

						// 디비 처리 
						lotteTrackingUpdate(item.getMasterCode(), trackingList);
						// Tracking 정보 Shopify 업데이트 처리
						shopifyPostOrderIdFullist(item.getMasterCode(), trackingList);
//					}
				}
				
			}
		} catch (Exception e) {
			LOGGER.debug("requestLotteTracking [Exception] : " + e.toString());
		}
		return map;
	}
	
	public void shopifyPostOrderIdFullist(String masterCode, JSONArray tracking) throws JSONException {
 		ShopData shopData = shopMapper.selectLotteTrackingFromMaster(masterCode);
		String url = null;

		TrackingData td = shipmentMapper.selectTranType(masterCode);
		td.setMasterCode(masterCode);
		
		JSONObject tList = new JSONObject();
		tList = (JSONObject) tracking.get(tracking.length()-1);
		String tranType2 = tList.getString("tranType");
		LOGGER.debug("tranType2 : " + tranType2) ;
		
		//트래킹 마지막 tranType이 이전 상태와 같을때 return
		if(tranType2.equals(td.getTranType())) {
			LOGGER.debug("tranType no Change!!!") ;
			return;
		}
		
		td.setTranType(tranType2);
		int ret = shipmentMapper.updateTranType(td);
		LOGGER.debug("tranType Change!!!") ;
		
		HttpHeaders httpHeaders = composeHeaders(shopData);

		//1.fulfillment ID get
		url = String.format(getFulfillmentCreateStep1Url, shopData.getDomain(), shopData.getOrderCode());
		JsonNode node = restService.getRestTemplate(httpHeaders, url);
		if ( node == null )
			return;
		List<JsonNode> items =  node.findValue("order").findValues("fulfillments");
		String fulfillmentId = items.get(0).findValue("id").asText();	
		
		//2. Tracking update
		//body={trackingNo, trackingURL, fulfillmentsId} 
		String trackingNo =  tList.getString("trackingNo");
		String URL ="";
		if(shopData.getCourier().equals("B040060") && shopData.getBuyerCountryCode().equals("US"))
			URL=usTrackingkUrl+trackingNo;  //LGL US
		else if(shopData.getCourier().equals("B040060") && shopData.getBuyerCountryCode().equals("RU"))
			URL=ruTrackingUrl+trackingNo; 		//LGL RU
		else if(shopData.getCourier().equals("B040010") || (shopData.getCourier().equals("B040020")))
			URL=emsTrackingUrl;   //EMS or EMS-p
		else if(shopData.getCourier().equals("B040040"))
			URL=upsTrackingUrl+trackingNo; //UPS
		else
			URL=deliveryTrackingUrl; //PICKUP
		
		String input = composeFullfillment(trackingNo, fulfillmentId, URL);
		url = String.format(puttFulfillmentUpdateStep1Url, shopData.getDomain(), shopData.getOrderCode(),fulfillmentId);
		node = restService.putRestTemplate(httpHeaders, url, input);
		}							
		
	private String composeFullfillment(String trackingNo, String Id, String URL) {
		String json = null;

		Map<String, Object> payload = new LinkedHashMap<>();
		payload.put("tracking_number", trackingNo);
		payload.put("tracking_url", URL);
		payload.put("location_id", Id);
		payload.put("notify_customer", "false");
		
		Map<String, Object> fullfillMap = new HashMap<>();
		fullfillMap.put("fulfillment", payload);

		try {
			json = this.objectMapper.writeValueAsString(fullfillMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return json;
		
	}
	
	private HttpHeaders composeHeaders(ShopData sd) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("X-Shopify-Access-Token", UtilFunc.getAESDecrypt(sd.getAccessToken()));
//			httpHeaders.add("Authorization", accessToken);
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return httpHeaders;
	}	
	/**
	 * 롯데 로지스 API 연동 > 스케줄링 > 배송상태 디비 처리 (json 형태 그대로 저장)  
	 * @return void
	 * @throws JSONException 
	 */
	private void lotteTrackingUpdate(String masterCode, JSONArray tracking) throws JSONException {
		//tranType(A:택배/ C:센터/ L:해외특송사), trackingNo, trackingCode, trackingName, location, trackingYmd, trackingTime
		//10:집하 , 30:배송중, 41:배송완료, 50:해외발송준비(C), 60:접수, 70:현지도착, 80:현지통관중,90:현지배송중,99:배송완료
		// test
//		tracking = lotteTestData();
		LOGGER.debug(">>>>>>> TRACKING  DB UPDATE >>>>>>>");
		LOGGER.debug(">> masterCode:" + masterCode);
		LOGGER.debug(">> tracking:" + tracking.toString());
		LOGGER.debug(">>>>>>> TRACKING >>>>>>>");
		int len = tracking.length();
		JSONObject lastData = (JSONObject) tracking.get(len-1);
		String trackingYmd = lastData.getString("trackingYmd"); // 트래킹 생성일
		String trackingCode = lastData.getString("trackingCode"); // 트래킹 코드 
		
		// 디비입력 : tb_delivery_tracking
		TrackingData data = new TrackingData();
		data.setMasterCode(masterCode);
		data.setTracking(tracking.toString());
		int ret = shipmentMapper.insertTrackingLotteApi(data);
		LOGGER.debug(">>>>>>> TRACKING INSERT ROW>>>>>>>" + ret);
		lotteTrackingDeliveryUpdate(masterCode, tracking); // 딜리버리 1 row 업데이트 
	}
	
	// 트래킹 업데이트 (조한두)
	private void lotteTrackingDeliveryUpdate(String masterCode, JSONArray tracking) throws JSONException {
		
		JSONArray trackingList = tracking;
		LOGGER.debug("lotteTrackingDeliveryUpdate>>>>>>>>>>>> [masterCode]" + masterCode);
		String lastCode = "00";
		if(trackingList != null && trackingList.length() > 0) {
			for(int i=0; i<trackingList.length(); i++) {
				JSONObject tList = new JSONObject();
				tList = (JSONObject) trackingList.get(i);
				String trackingNo = tList.getString("trackingNo");
				String tranType    = "운송구분";
				if(tList.getString("tranType") != null)
				 tranType = tList.getString("tranType");
				String trackingCode =  tList.getString("trackingCode");
				String trackingName = tList.getString("trackingName");
				String location      = tList.getString("location");
				lastCode = trackingCode ;				// 항상 마지막 상태만 디비에 한번 업데이트 한다. (A->B->C->D 에서 D의 상태만 업데이트함)
				LOGGER.debug("<trackingNo>" +trackingNo);
				LOGGER.debug("<tranType>" +tranType);
				LOGGER.debug("<trackingCode>" +trackingCode);
				LOGGER.debug("<trackingName>" +trackingName);
				LOGGER.debug("<location>" +location);
			}
		}
		LOGGER.debug("lotteTrackingDeliveryUpdate>>>>>>>>>>>> [trackingCode]" + lastCode);
		// 디비수정 : tb_delivery
		TrackingData data = new TrackingData();
		data.setMasterCode(masterCode);
		if("00".equals(lastCode)) // 롯데 접수
			data.setState("A020021");
		else if("10".equals(lastCode)) // 집하 (국내)
			data.setState("A020040");
		else if("30".equals(lastCode)) // 배송중(국내)
			data.setState("A020045");
		else if("41".equals(lastCode)) // 배송 완료(국내)
			data.setState("A020049");
		else if("50".equals(lastCode)) // 해외 발송 준비 
			data.setState("A020050");
		else if("60".equals(lastCode)) // 해외 접수 
			data.setState("A020060");
		else if("70".equals(lastCode)) // 현지 도착 
			data.setState("A020070");
		else if("80".equals(lastCode)) // 현지 통관중 
			data.setState("A020080");
		else if("90".equals(lastCode)) // 현지 배송중 
			data.setState("A020090");
		else if("99".equals(lastCode)) // 배송 완료  
			data.setState("A020099");
		int ret = shipmentMapper.updateTrackingDelivery(data);
		LOGGER.debug(">>>>>>> TRACKING UPDATE ROW>>>>>>>" + ret);
	}
	
	// 테스트용 데이터 
	private JSONArray lotteTestData() throws JSONException {
		JSONArray tracking = new JSONArray();
		
		JSONObject test = new JSONObject();
		
		test.put("tranType ", "A");
		test.put("trackingNo ", "TEST200310123412345");
		test.put("trackingCode ", "10");
		test.put("trackingName ", "집하");
		test.put("location ", "123");
		test.put("trackingYmd ", "2020-03-05");
		test.put("trackingTime ", "15:12:11");
		tracking.put(test);
		
		JSONObject test2 = new JSONObject();
		test.put("tranType ", "A");
		test.put("trackingNo ", "TEST200310123412345");
		test.put("trackingCode ", "30");
		test.put("trackingName ", "배송중");
		test.put("location ", "123");
		test.put("trackingYmd ", "2020-03-06");
		test.put("trackingTime ", "15:12:11");
		tracking.put(test2);
		
		JSONObject test3 = new JSONObject();
		test.put("tranType ", "A");
		test.put("trackingNo ", "TEST200310123412345");
		test.put("trackingCode ", "41");
		test.put("trackingName ", "배송완료");
		test.put("location ", "123");
		test.put("trackingYmd ", "2020-03-07");
		test.put("trackingTime ", "15:12:11");
		tracking.put(test3); 

		JSONObject test6 = new JSONObject();
		test.put("tranType ", "L");
		test.put("trackingNo ", "TEST_L200310123412345");
		test.put("trackingCode ", "60");
		test.put("trackingName ", "접수");
		test.put("location ", "123");
		test.put("trackingYmd ", "2020-03-08");
		test.put("trackingTime ", "15:12:11");
		tracking.put(test6); 
		
		JSONObject test4 = new JSONObject();
		test.put("tranType ", "L");
		test.put("trackingNo ", "TEST_L200310123412345");
		test.put("trackingCode ", "70");
		test.put("trackingName ", "현지도착");
		test.put("location ", "123");
		test.put("trackingYmd ", "2020-03-08");
		test.put("trackingTime ", "15:12:11");		
		tracking.put(test4); 
		
		JSONObject test5 = new JSONObject();
		test.put("tranType ", "L");
		test.put("trackingNo ", "TEST_L200310123412345");
		test.put("trackingCode ", "80");
		test.put("trackingName ", "현지 통관 중");
		test.put("location ", "123");
		test.put("trackingYmd ", "2020-03-09");
		test.put("trackingTime ", "15:12:11");
		tracking.put(test5); 
		
		
		return tracking;
	}
	
	/**
	 * 롯데 로지스 API 연동 > 스케줄링(CronTable.java) > 배송 오류 재발송 
	 * @return void
	 */ 
	public void scheduledLotteReOrder() {
		LOGGER.debug("## LotteLogisApiService >>> scheduledLotteReOrder >>>> Start");
		
		ShipmentData ship = new ShipmentData();
		ship.setCourierCompany(companyCode); // 롯데 로지스
		
		List<ShipmentData> deliveryList = shipmentMapper.selectShipmentErrorList(ship);

		if(deliveryList.size() > 0) {
			String email = "";
			List<String> arrayList = new ArrayList<>();
			
			int i = 0;
			for (ShipmentData item : deliveryList) {
				if(i == 0) { // 이메일 초기화
					email = item.getEmail();
				}
				
				arrayList.add(item.getMasterCode());
				
				if(!email.equals(item.getEmail())) {
					String[] arrMasterCode = arrayList.toArray(new String[arrayList.size()]);
					
					ShipmentPopupData shipPopData = new ShipmentPopupData();
					shipPopData.setMasterCodeList(arrMasterCode);
					
					requestLotteLogisDelivery(shipPopData, email);
					
					arrayList = new ArrayList<>();
				}
				
				email = item.getEmail();
				i++;
			}
			
			if(arrayList.size() > 0) { // 최종 발송 처리 
				String[] arrMasterCode = arrayList.toArray(new String[arrayList.size()]);
				
				ShipmentPopupData shipPopData = new ShipmentPopupData();
				shipPopData.setMasterCodeList(arrMasterCode);
				
				requestLotteLogisDelivery(shipPopData, email);
				
				arrayList = new ArrayList<>();
			}
		}
	}
	
	
	/**
	 * 롯데 로지스 API 연동 > json 기본 정보 생성
	 * @param List<ShipmentData>
	 * @param String LocalCode
	 * @return Map<String, Object>
	 * @throws Exception 
	 */
	private Map<String, Object> makeJsonOrder(List<ShipmentData> shipmentList, String LocalCode, ShipmentPopupData shipPopData) throws Exception {
		
		String email = shipPopData.getEmail();
		JSONObject collectCargo = new JSONObject();
		JSONArray orders = new JSONArray();
		
		// TODO : 박스 타입 매핑
		//String boxTypCd = "A";
		
		int ii = 0;
		for (ShipmentData item : shipmentList) {
			LOGGER.debug("### shipmentList.size(): " +shipmentList.size() );
			JSONObject jsonOrder = new JSONObject();
			JSONObject jsonShipper = new JSONObject();
			JSONObject jsonConsignee = new JSONObject();
			JSONArray jsonItems = new JSONArray();

			// 데이터 복호화
			String sellerName =  util.nullToEmpty(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, item.getSellerName()));
//			String sellerEname =  util.nullToEmpty(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, item.getSellerEname()));
			
			String sellerPhone =  util.nullToEmpty(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, item.getSellerPhone()));
			
			String buyerFirstname =  util.nullToEmpty(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, item.getBuyerFirstname()));
			String buyerLastname =  util.nullToEmpty(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, item.getBuyerLastname()));
			String buyerPhone =  util.nullToEmpty(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, item.getBuyerPhone()));
			String buyerName = buyerFirstname + " " + buyerLastname;
			
			// 연락처 설정
			String[] sellerMobile = new String[4];
			
			if(item.getSellerPhone() != null) {
				String[] arrSellerMobile = sellerPhone.split("-");
				
				if(arrSellerMobile.length < 4) {
					sellerMobile[0] = sellerPhone;
					sellerMobile[1] = " ";
					sellerMobile[2] = " ";
					sellerMobile[3] = " ";
				} else {
					int i = 0;
					for (String phone : arrSellerMobile) {
						if(i < 4) {
							sellerMobile[i] = phone;
							i++;
						}
					}
				}
			}
			
			String[] buyerMobile = new String[4];
			
			if(item.getSellerPhone() != null) {
				String[] arrBuyerMobile = buyerPhone.split("-");
				
				if(arrBuyerMobile.length < 4) {
					buyerMobile[0] = buyerPhone;
					buyerMobile[1] = " ";
					buyerMobile[2] = " ";
					buyerMobile[3] = " ";
				} else {
					int i = 0;
					for (String phone : arrBuyerMobile) {
						if(i < 4) {
							buyerMobile[i] = phone;
							i++;
						}
					}
				}
			}
			
			// 주문 상품 가져 오기 
			ShipmentPopupData ShipmentPopup = new ShipmentPopupData();
			ShipmentPopup.setEmail(email);
			ShipmentPopup.setMasterCode(item.getMasterCode());
			ShipmentPopup.setLocale("ko");
			List<ShipmentPopupData> skuKist = shipmentMapper.selectPopShipmentSkuList(ShipmentPopup);

			LOGGER.debug("### skuKist email : " + email);
			LOGGER.debug("### skuKist MasterCode : " + item.getMasterCode());
			LOGGER.debug("### skuKist skuKist : " + skuKist.toString());
			
//			int boxCount = skuKist.size();
			int boxCount = 1;   //     5/25 일단 1주문 1박스로 처리
			
			// order 기본 정보 
			jsonOrder.put("tagBarcode", item.getMasterCode());
			jsonOrder.put("orderNo", item.getMasterCode());
			jsonOrder.put("trnType", item.getCourier());
			jsonOrder.put("boxCount", boxCount);
			jsonOrder.put("grossWeight", item.getTotalWeight());   //boxWeight -> totalWeight
			
			// shiper(송하인) 기본 정보
			jsonShipper.put("nameKor", sellerName);
			jsonShipper.put("nameEng", sellerName); //sellerEname 으로 변경예정
			jsonShipper.put("orderNameEng", sellerName);
			if(item.getSellerCountryCode().equals("CA")) {
				jsonShipper.put("postalCode", item.getSellerZipCode());
			}
			else {
				jsonShipper.put("postalCode", UtilFunc.trimZipCode(item.getSellerZipCode(),"KR"));
			}
//			jsonShipper.put("provinceName", item.getSellerProvince() + "");
			UtilFunc.jsonNullCk(jsonShipper, "provinceName", item.getSellerProvince()); 
//			LOGGER.debug("******************item.getSellerProvince() : [" + item.getSellerProvince()+ "]S");
			jsonShipper.put("cityName", item.getSellerCity() + "");
			jsonShipper.put("masterAddrKor", item.getSellerAddr1());
			jsonShipper.put("detailAddrKor", item.getSellerAddr2());
			jsonShipper.put("masterAddrEng", item.getSellerAddr1Ename());
			jsonShipper.put("detailAddrEng", item.getSellerAddr2Ename());
			jsonShipper.put("email", "");
			jsonShipper.put("mobile1", sellerMobile[0]);
			jsonShipper.put("mobile2", sellerMobile[1]);
			jsonShipper.put("mobile3", sellerMobile[2]);
			jsonShipper.put("mobile4", sellerMobile[3]);
			jsonShipper.put("telNo1", sellerMobile[0]);
			jsonShipper.put("telNo2", sellerMobile[1]);
			jsonShipper.put("telNo3", sellerMobile[2]);
			jsonShipper.put("telNo4", sellerMobile[3]);

			jsonOrder.put("shipper", jsonShipper);
			
			// consignee(수하인) 기본 정보
			jsonConsignee.put("companyName", " ");
			jsonConsignee.put("name", buyerName);
			jsonConsignee.put("countyCode", item.getBuyerCountryCode());
			jsonConsignee.put("state", " ");
			UtilFunc.jsonNullCk(jsonConsignee, "province", item.getBuyerProvince());   //psh
			LOGGER.debug("******************item.getBuyerProvince : " + item.getBuyerProvince());
//			jsonConsignee.put("province", item.getBuyerProvince());
			jsonConsignee.put("city", item.getBuyerCity());
			jsonConsignee.put("address", item.getBuyerAddr2() + " " + item.getBuyerAddr1());
			UtilFunc.jsonNullCk(jsonConsignee, "postalCode", UtilFunc.trimZipCode(item.getBuyerZipCode(),item.getBuyerCountryCode()));
//			jsonConsignee.put("postalCode", item.getBuyerZipCode());
			jsonConsignee.put("mobile1", buyerMobile[0]);
			jsonConsignee.put("mobile2", buyerMobile[1]);
			jsonConsignee.put("mobile3", buyerMobile[2]);
			jsonConsignee.put("mobile4", buyerMobile[3]);
			jsonConsignee.put("telNo1", buyerMobile[0]);
			jsonConsignee.put("telNo2", buyerMobile[1]);
			jsonConsignee.put("telNo3", buyerMobile[2]);
			jsonConsignee.put("telNo4", buyerMobile[3]);
			jsonConsignee.put("email", item.getBuyerEmail());
			jsonConsignee.put("berryDlvType", "2");     //운송타입 : LGL(러시아행)일 경우 필수. 2로 픽스
			jsonConsignee.put("pudoCode","");      //운송타입 : LGL(러시아행), berryDivType : 1인 경우 필수. 러시아행 없음

			jsonOrder.put("consignee", jsonConsignee);
			
			// 주문 상품 정보 
			String gdsNm = "";
			for (ShipmentPopupData shipmentItem : skuKist) {
				JSONObject jsonSku = new JSONObject();
				jsonSku.put("itemCode", shipmentItem.getGoodsCode());
				jsonSku.put("name", shipmentItem.getGoods());
				jsonSku.put("quantity", shipmentItem.getQuantity());
				//루블화 -> usd
//				jsonSku.put("unitPrice", shipmentItem.getUnitCost());
				jsonSku.put("unitPrice", changeCurrency(shipmentItem.getPriceCurrency(),shipmentItem.getUnitCost()));
				jsonSku.put("currency", currency);
				if(shipmentItem.getHscode().length() == 6)
					jsonSku.put("hsCode", shipmentItem.getHscode()+"1000");		
				else jsonSku.put("hsCode", shipmentItem.getHscode());
				jsonSku.put("itemLink",shipmentItem.getItemLink());	//상품구매 사이트
				
				jsonSku.put("brand", shipmentItem.getBrand());		//상품 브랜드
				UtilFunc.jsonNullCk(jsonSku, "repreItemNm",  shipmentItem.getRepreItemNm());
				UtilFunc.jsonNullCk(jsonSku, "repreItemNmRu",  shipmentItem.getRepreItemNmRu());
				jsonSku.put("notiNo", ""); 		//FSS신고번호
				jsonSku.put("notiExpDate", "");  //FSS신고 유효기간
				jsonSku.put("notiCode", "");	//FSS신고코드
				
				gdsNm = shipmentItem.getGoods();
				jsonItems.put(jsonSku);
			}
			
			jsonOrder.put("items", jsonItems);
			
			// 전체 json 생성
			orders.put(jsonOrder);
			
			LOGGER.debug("### LocalCode : " + LocalCode);
			
			// 국내 집화 seller정보와 롯데 집하 정보
			if(ii == 0 && "Y".equals(LocalCode)) { // 국내 배송 정보 처리 
				// TODO : 요청일자 규칙 설정
				String pickReqYmd = "";
				Calendar cal = new GregorianCalendar();
				SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
				
				int dd = 0;
				if(Integer.parseInt(util.getDateElement("hh")) < pickCheckTime) { // 오늘 픽업 요청
					dd = 0;
				} else { // 내일 픽업 요청
					dd = 1;
				}
				
				LOGGER.debug("### collectCargo : Start");
				
				cal.add(Calendar.DATE, dd);
				pickReqYmd = sdf.format(cal.getTime());
				
				collectCargo.put("jobCustCd", jobCustCd); 	//택배 거래처 코드 
				
				
				collectCargo.put("ustRtgSctCd", "02"); 		//출고반품구분 (01: 출고 / 02: 반품) 
				collectCargo.put("ordSct", "1"); 			//오더구분 (1:일반/2:교환/3:AS)
				collectCargo.put("fareSctCd", "03");			//운임구분 (03:신용/04:복합) 
				
				collectCargo.put("invNo", item.getLocalCode());				//집화 예약 번호
				//collectCargo.put("invNo", "mAC2Rj78rq");				//집화 예약 번호
				collectCargo.put("snperNm", sellerName);			//송하인명
				collectCargo.put("snperTel", UtilFunc.DeliverySellerPhone(sellerPhone));			//송하인전화번호	
				collectCargo.put("snperCpno", UtilFunc.DeliverySellerPhone(sellerPhone));		//송하인휴대전화번호	
				collectCargo.put("snperZipcd", item.getSellerZipCode());		//송하인우편번호
				collectCargo.put("snperAdr", item.getSellerAddr1() + " " + item.getSellerAddr2());			//송하인주소 (기본주소 + 상세주소)	

				collectCargo.put("acperNm", acperNm);			//수하인명 	
				collectCargo.put("acperTel", acperTel);			//수하인전화번호 
				collectCargo.put("acperCpno", acperCpno);			//수하인휴대전화번호 	
				collectCargo.put("acperZipcd", acperZipcd);		//수하인우편번호 	
				collectCargo.put("acperAdr", acperAdr);			//수하인주소 (기본주소 + 상세주소)
				
				collectCargo.put("boxTypCd", item.getBoxType());			//박스크기 (A, B, C, D, E, F) 
				collectCargo.put("gdsNm", gdsNm);				//상품명 	
				collectCargo.put("dlvMsgCont", "");		//배달메세지내용
				collectCargo.put("cusMsgCont", "");		//고객메세지내용 	
				collectCargo.put("pickReqYmd", pickReqYmd);		//집하요청일
			}
			else {
				LOGGER.debug("***************국내 픽업 신청 X**********"); 
			}
		}
		
		LOGGER.debug("### collectCargo : " + collectCargo.toString());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orders", orders);
		map.put("collectCargo", collectCargo);
		return map;
	}
	
	
	private double changeCurrency(String priceCurrency, double unitCost) throws Exception {
		if(priceCurrency.equals("RUB")) {
			double price = exchangeApi.getExchangePriceRUBTOUSD(unitCost);
			return price;
		}
		else if(priceCurrency.equals("CAD")) {
			double price = exchangeApi.getExchangePriceCADTOUSD(unitCost);
			return price;
		}
			
		else 
			return unitCost;
	}


	/**
	 * 롯데 로지스 API 연동 > 롯데 API전달
	 * @param List<ShipmentData>
	 * @param String LocalCode
	 * @return Map<String, Object>
	 * @throws JSONException 
	 */
	private Map<String, Object> sendLotteLogis(JSONObject json, String notifyType) throws NoSuchAlgorithmException, ClientProtocolException, IOException {
		
		int THREE_MINUTES = 3 * 60 * 1000;
		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(THREE_MINUTES).setConnectTimeout(
		        THREE_MINUTES).setConnectionRequestTimeout(THREE_MINUTES).setStaleConnectionCheckEnabled(true).build();

		
		// json
		// 1단계 Json data => String UTF-8
		Charset chrutf = Charset.forName("UTF-8");
		String dataDigest = new String(json.toString().getBytes(),chrutf);
		dataDigest = new String(MessageDigestUtils.encodeBase64(MessageDigestUtils.getMessageDigest(
				(json.toString() + signKey), signType, charSet).getBytes()));
		
		LOGGER.debug("#### sendLotteLogis [dataDigest] : " + dataDigest);
		
		CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        HttpPost httppost = new HttpPost(requestUri);
		
        ArrayList<NameValuePair> httpParameters = new ArrayList<NameValuePair>();
        httpParameters.add(new BasicNameValuePair("sign_type", signType)); //메시지 유형
        httpParameters.add(new BasicNameValuePair("notify_type", notifyType)); //발신자 코드
        httpParameters.add(new BasicNameValuePair("input_charset", charSet)); //파트너 코드 
        httpParameters.add(new BasicNameValuePair("sign", dataDigest)); //정보사인값
        httpParameters.add(new BasicNameValuePair("content", json.toString())); //메시지 유형별 XML 전문 내용
		
        LOGGER.debug("#### sendLotteLogis [notify_type] : " + notifyType);
		LOGGER.debug("#### sendLotteLogis [signKey] : " + signKey);
		LOGGER.debug("#### sendLotteLogis [json] : " + json.toString());
		LOGGER.debug("#### sendLotteLogis [HttpEntity] : " + httpParameters.toString());
        
        httppost.setEntity(new UrlEncodedFormEntity(httpParameters, charSet));
        LOGGER.debug("#### sendLotteLogis [getURI] : " + httppost.getURI().toString());
        
        CloseableHttpResponse response = client.execute(httppost);

        String resultContent = EntityUtils.toString(response.getEntity(), charSet);

        LOGGER.debug("#### sendLotteLogis [resultContent] : " + resultContent);
        
        String success = "";
		String errorCode = "";
		String errorMsg = "";
		String trackingNoList = "";
		JSONObject jsonData = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        
		try {
			jsonData = new JSONObject(resultContent);
			
			success = jsonData.getString("success");
			errorCode = jsonData.getString("errorCode");
			errorMsg = jsonData.getString("errorMsg");
			
			if(resultContent.indexOf("trackingList") > 0) {
				jsonArray = jsonData.getJSONArray("trackingList");
				if(jsonArray != null && jsonArray.length() > 0) {
					for(int i=0; i<jsonArray.length(); i++) {
						JSONObject tList = new JSONObject();
						tList = (JSONObject) jsonArray.get(i);
						if(trackingNoList.equals("")) {
							trackingNoList = tList.getString("trackingNo");
						} else {
							trackingNoList = trackingNoList + "," + tList.getString("trackingNo");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.debug("#### sendLotteLogis [ParseException] : " + EntityUtils.toString(response.getEntity(), charSet));
			
			success = "false";
			errorMsg = e.toString();
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", success);
		map.put("errorCode", errorCode);
		map.put("errorMsg", errorMsg);
		map.put("jsonData", jsonData);
		map.put("trackingNoList",trackingNoList);
		
		return map;
	}
	
	/**
	 * 롯데 로지스 API 연동 > 배송 상태 변경
	 * @param List<ShipmentData>
	 * @param String LocalCode
	 * @param String stateCode
	 * @return int
	 */
	public int updateDeliveryState(List<ShipmentData> shipmentList, String stateCode) {		
		ShipmentPopupData ship = new ShipmentPopupData();
		String[] arrMasterCode = new String[shipmentList.size()]; 
		
		int i = 0;
		for (ShipmentData item : shipmentList) {
			arrMasterCode[i] = item.getMasterCode();
			i++;
		}
		
		ship.setMasterCodeList(arrMasterCode);
		ship.setStateCode(stateCode);
		
		int chk = shipmentMapper.updateDeliveryState(ship);
		
		return chk;
	}
}
