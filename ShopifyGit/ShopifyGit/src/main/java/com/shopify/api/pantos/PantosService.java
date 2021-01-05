package com.shopify.api.pantos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.api.ExchangeApiService;
import com.shopify.common.RestData;
import com.shopify.common.RestService;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.mapper.PantosMapper;
import com.shopify.payment.popup.PaymentPopupRestService;
import com.shopify.shipment.popup.ShipmentPopupData;

import gsi.cm.app.extif.http.seed.SeedUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SOLUGATE
 *
 */
@Service
@Slf4j
public class PantosService {
	public static String TOKEN_PANTOS = null; // 토큰 값
	private final static int length_token = 14; // 토큰 값 체크 시 날짜 yyyyMMddHHmmss 값의 체크를 위해 토킅 길이 14자 이상 체크

	@Autowired
	PantosMapper pantosMapper;
	
	@Autowired private UtilFn util;

	@Autowired PaymentPopupRestService paymentPopupRestService;
	@Autowired private ExchangeApiService exchangeApi;	
	@Autowired	private RestService restService;
	// 설정 파일
	@Value("${pantos.api.uri}")
	String CALL_API_URI; // 판토스 연계 URL
	@Value("${pantos.api.version}")
	String CALL_API_VERSION; // 판토스 연동 API Version
	@Value("${pantos.api.enc}")
	String CALL_API_ENC; // 판토스 연동시 암호화 여부(Y: 암호화, N: 미암호화)
	@Value("${pantos.sign.key}")
	String API_SIGN_KEY; // 판토스 연동 파라미터(JSON) 암호화 key 값
	@Value("${pantos.charset}")
	String CHARSET_STRING; // 판토스 연동시 파라미터 charterSet 값(UTF-8)

	@Value("${pantos.method.order.insert}")
	String ORDER_INSERT; // 판토스 연동 프로세스 key (주문 등록) 1건 이상
	@Value("${pantos.method.order.list}")
	String ORDER_LIST; // 판토스 연동 프로세스 key (주문리스트) 1건 이상
	@Value("${pantos.method.order.confirm}")
	String ORDER_CONFIRM; // 판토스 연동 프로세스 key (주문확정) 1건 이상
	@Value("${pantos.method.order.detail}")
	String ORDER_DETAIL; // 판토스 연동 프로세스 key (주문상세정보) 1건 이상

	@Value("${Order.ProductLinks}") private String productUrl;

	// 음답 코드 정뵤
	@Value("${pantos.code.success}")
	String CODE_SUCCESS; // 판토스 연동 응담 코드 값 (성공)
	@Value("${pantos.code.error.code}")
	String ERROR_CODE; // 판토스 연동 응담 코드 값 (전송파라미터정보가 비 정상)
	@Value("${pantos.code.error.server}")
	String ERROR_SERVER; // 판토스 연동 응담 코드 값 (등록되지 않은 서버 정보)
	@Value("${pantos.code.error.token.published}")
	String TOKEN_PUBLISHED; // 판토스 연동 응담 코드 값 (토큰 값 생성)
	@Value("${pantos.code.error.token}")
	String ERROR_TOKEN; // 판토스 연동 응담 코드 값 (유효하지 않은 토큰 값)
	@Value("${pantos.code.error.exception}")
	String ERROR_EXCEPTION; // 판토스 연동 응담 코드 값 (처리시 오류 발생)

	// 음답 메시지
//	@Value("${pantos.message.success}") 					String CODE_SUCCESS_MESSAGE;  
//	@Value("${pantos.message.error.code}") 					String ERROR_CODE_MESSAGE;  
//	@Value("${pantos.message.error.server}")				String ERROR_SERVER_MESSAGE;  
//	@Value("${pantos.message.error.token.published}") 		String TOKEN_PUBLISHED_MESSAGE;  
//	@Value("${pantos.message.error.token}") 				String ERROR_TOKEN_MESSAGE;  
//	@Value("${pantos.message.error.exception}") 			String ERROR_EXCEPTION_MESSAGE;  

	@Value("${pantos.result.success}")
	String RESULT_SUCCESS; // 리턴한 값이(정상처리된 경우)
	@Value("${pantos.result.fail}")
	String RESULT_FAIL; // 리턴한 값이(실패한 경우)

	@Value("${pantos.log.is.save}")
	String IS_API_LOG_SAVE_DB; // 전송 정보를 DB에 저장 여부 (Y:저장, N : 저장하지 않음)

	@Value("${pantos.header.string}")
	String headerString; // 전송 파라미터(JSON) 중 header JSON String(일부 추가 setting 필요)

	@Value("${pantos.tracking.ftp.folder}")
	String TRACKING_FOLDER; // 트랙킹 처리시 트래킹 파일의 위치 정보

	@Value("${pontos.api.id}")
	String PANTOS_BIZ_ID; // 트랙킹 처리시 트래킹 파일의 위치 정보
	
	@Value("${pontos.api.pwd}")
	String PANTOS_BIZ_PWD; // 트랙킹 처리시 트래킹 파일의 위치 정보
	
	final String JSON_HEAD_NAME = "header"; // 전송 파라미터(JSONObject)의 Header object key 값
	final String JSON_BODY_NAME = "body"; // 전송 파라미터(JSONObject)의 Body object key 값

	String PANTOS_COM_CODE = "solugate"; // 판토스 연동시 필요한 코드값 (회사연계 코드) 기본 setting 값

	String rowSpliter = "\\^", columSpliter = "\\|"; // 판토스 Tracking 처리 결과 String 값을 분리할 때 필요 rowSpliter(배송정보 건당 처리),
														// columSpliter(1건에 대한 분리 값)

	/**
	 * 주문 정보를 처리한 방법 중 하나로 한건, 여러건 처리할 수 있음 그 중에서 여러건 배송정보 전송 처리
	 * @param deliveryList<PantosDeliveryData> 판토스 연동 API에 맞춘 배송 정보 Bean
	 * @return 연동 결과 받은 JSONObject 값
	 * @throws JSONException
	 */
	public JSONObject sendOrderInsert(List<PantosDeliveryData> deliveryList) throws JSONException {

		if (deliveryList == null || deliveryList.isEmpty())
			return null;

		String ifCd = "hdcho"; // 개인사용자 식별코드
		JSONObject sendJson = setHeader(ifCd, ORDER_INSERT);
		if (sendJson == null) {
			log.error("set Header ERROR");
			return null;
		}

		JSONArray bodyList = new JSONArray();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		String masterCode = null;

		// 배송정보 여러건 처리 하기 위한 함수
//		int deliverySize = deliveryList.size();
//		setNULL2Empty(mapper);
		for (PantosDeliveryData deliveryData : deliveryList) {
			if(!setOrderBody(deliveryData, mapper, bodyList)) { // 배성정보 단 건 셋팅
				log.error("data : "+deliveryData.toString()); 
				return null;
			}
//			masterCode = deliveryData.getCoNo();
		}

		sendJson.put(JSON_BODY_NAME, bodyList);

		log.debug("sendData : " + sendJson.toString());

		JSONObject resultJson = callPantosAPI(sendJson);

		// 연동 정보 DB 저장 처리
		if ("Y".equals(IS_API_LOG_SAVE_DB)) {
			masterCode = System.currentTimeMillis() + "";
			setAPILog2DB(sendJson, resultJson, masterCode);
		}
		
		resultJson.put("apiCode", masterCode);

		return resultJson;

	}

	/**
	 * 주문 정보를 처리한 방법 중 하나로 한건, 여러건 처리할 수 있음 그 중에서 단건 배송정보 전송 처리
	 * 
	 * @param PantosDeliveryData 판토스 연동 API에 맞춘 배송 정보 Bean
	 * @return 연동 결과 받은 JSONObject 값
	 * @throws JSONException
	 */
	public JSONObject sendOrderInsert(PantosDeliveryData deliveryData) throws JSONException {

		String ifCd = "hdcho"; // 개인사용자 식별코드

		JSONObject sendJson = setHeader(ifCd, ORDER_INSERT);

		ObjectMapper mapper = new ObjectMapper();
		JSONArray bodyList = new JSONArray();

		// 배성정보 단 건 셋팅
		if(!setOrderBody(deliveryData, mapper, bodyList) ) {
			log.error("data : "+deliveryData.toString()); 
			return null;
		}

		sendJson.put(JSON_BODY_NAME, bodyList);

		log.debug("sendData : " + sendJson.toString());

		JSONObject resultJson = callPantosAPI(sendJson);

		// 연동 정보 DB 저장 처리
		if ("Y".equals(IS_API_LOG_SAVE_DB))
			setAPILog2DB(sendJson, resultJson, deliveryData.getCoNo());

		return resultJson;
	}

	private JSONObject setHeader(String ifCd, String processId) throws JSONException {

		if (!isToken()) {
			// token 값 setting
			if (!setToken(ifCd)) {
//				System.out.println("isToken: false");
				log.info(" setOrderHeader isToken: false");
				return null;
			}
		}

		JSONObject sendJson = new JSONObject();

		JSONObject mObj = new JSONObject(headerString);
		mObj.put("callId", processId);
		mObj.put("ifCd", ifCd);
		mObj.put("token", TOKEN_PANTOS);

		// 판토스 Header JSONObject setting
		sendJson.put(JSON_HEAD_NAME, mObj);
		log.debug(JSON_HEAD_NAME + " : " + sendJson.toString());
//		System.out.println(sendJson.toString());
		return sendJson;
	}

	/**
	 * @param deliveryData PantosDeliveryData(배송정보) :단건
	 * @param mapper       ObjectMapper(항성 사용)
	 * @param bodyList     JSONArray (판토스 연동시 필요한 body setting 값)
	 * @throws JSONException
	 */
	public boolean setOrderBody(PantosDeliveryData deliveryData, ObjectMapper mapper, JSONArray bodyList) {
		// Object to JSONObject 처리
		JSONObject bodyJson = null;
		try {
			String jsonStr = mapper.writeValueAsString(deliveryData);
			if (jsonStr == null || jsonStr.isEmpty()) {
				log.error("setOrderBody: delveryData to jsonString ERROR");
				return false;
			}
			bodyJson = new JSONObject(jsonStr);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("setOrderBody: deliveryData to JsonProcessingException error");
			return false;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("setOrderBody: deliveryData to JSONException error");
			return false;
		}

		bodyList.put(bodyJson);
		return true;
	}
	

	public String updateTracking(String ifData, String userId, String userPwd) throws JSONException {
		
		
		JSONObject json = new JSONObject();
//		if(!PANTOS_BIZ_ID.equals(userId) || !PANTOS_BIZ_PWD.equals(userPwd)) {
//			json.put("result", RESULT_FAIL);
//			json.put("code", "ERR_001");
//			json.put("message", "User information is invalid.");
//			return json.toString();
//		}
		
		
		
		int pCnt = 0;
		
		List<PantosTrackingData> dataList = getTrackingDatas(ifData, rowSpliter, columSpliter, PANTOS_TRACKING_PARAMETER_LENGTH);
		if(dataList == null || dataList.isEmpty()) {
			log.info("Tracking Data Empty ");
			json.put("result", RESULT_SUCCESS);
			json.put("code", "ERR_002");
			json.put("updCnt", pCnt);
			json.put("message", "There is no tracking information.");
			
			return json.toString();
		} 
		
		JSONArray aryJson = new JSONArray();
		for (PantosTrackingData data : dataList) {
			int rsInt = 0 ;
			try {
				rsInt = pantosMapper.funTrackingProcess(data);
				data.setUpCnt(rsInt);
				log.debug(rsInt+" "+ data.toString());
				if(rsInt ==1)
					pCnt += rsInt;
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
//				log.error(data.toString());
				log.error(data.toString() + "\n"+ e.toString());
				data.setUpCnt(-99);
			}

			JSONObject objJson = new JSONObject();
			objJson.put("hblNo", data.getHblNo());
			objJson.put("statusCd", data.getStatusCd());
			objJson.put("upCnt", data.getUpCnt());
			aryJson.put(objJson);
			
		}
		
		if(dataList.size() == pCnt) {
			json.put("result", RESULT_SUCCESS);
			json.put("code", "0");
			json.put("message", "");
		} else  {
			json.put("result", RESULT_FAIL);
			json.put("code", "ERR_003");
			json.put("message", "There is an error in the tracking information.");
		}
		
		if ("Y".equals(IS_API_LOG_SAVE_DB)) {
			String masterCode = System.currentTimeMillis() + "";
			setTrackingLog2DB(ifData, masterCode, json.getString("result"));
		}
		
		json.put("dataSize", dataList.size());
		json.put("updCnt", pCnt);
		json.put("resultList", aryJson);
		return json.toString();
	}

	/**
	 * 판토스에 보내준 파일 하나에 대하여 모든 Tracking 정보를 리시트 형태로 변환 하는 함수
	 * @param list	: 파일의  String 리스트
	 * @param rowSpliter : String의 split (row 단위 spliter) ['^'] 을 사용
	 * @param columSpliter : String의 split (column 단위 spliter) ['|'] 을 사용
	 * @param dataSplitCount : column의 개수로 현재는 3개임
	 * @return List<PantosTrackingData> 
	 */
	private List<PantosTrackingData> getTrackingDatas(String receveData, String rowSpliter, String columSpliter, int dataSplitCount) {
		
		List<PantosTrackingData> trackingDatas = new ArrayList<>();

			if (receveData == null || "".equals(receveData)) {
				log.debug("NO Tracking Process Data");
				return null;
			}
			for (String trackingData : receveData.split(rowSpliter)) {
				String[] tempStr = trackingData.split(columSpliter);

				if (tempStr == null || tempStr.length != dataSplitCount) {
					log.error("Tracking DATA ERROR :" + trackingData);
					continue;
				}

				trackingDatas.add(new PantosTrackingData(tempStr));
			}

		return trackingDatas;
	}
	

	/**
	 * 판토스 Tracking 없데이트 처리 판토스에서 보내준 파일을 임시 디렉토리([폴더명]_날짜)로 이동하여 처리한다. 보내준 파일중
	 * 정상적이지 않은 파일은 임시 디렉토리에 남기고 처리하지 않은 파일이 존재하여도 처리하지 않음 정상 동작 완료 되면 임시 디렉토리를 지우고
	 * 종료한다. Tracking 정보는 한 Line 단으로 처리한다. 정상 처리 되었을 경우 updateTracking(String
	 * receveData)에서 처리한 갯수가 1 이상
	 */
	public void updateTracking() {

		String targetFolder = new UtilFn().getDateElement("full");
		File[] files = getTrackingFiles(TRACKING_FOLDER, targetFolder);
		if (files == null || files.length == 0) {
			log.info(TRACKING_FOLDER + "		Not  exists tracking Files");
			return ;
		}

		for (File file : files) {
//			log.info("Tracking File NAME : " + file.getName());
			List<String> list = readFile(file, CHARSET_STRING);
			
			List<PantosTrackingData> dataList = getTrackingDatas(list, rowSpliter, columSpliter, PANTOS_TRACKING_PARAMETER_LENGTH);
			if(dataList == null || dataList.isEmpty()) {
				log.info("Tracking Data Empty "+file.getName());
				continue;
			} 
			
			int pCnt = 0;
			for (PantosTrackingData data : dataList) {
				int rsInt = 0 ;
				try {
					rsInt = pantosMapper.funTrackingProcess(data);
					log.debug(rsInt+" "+ data.toString());
					if(rsInt ==1)
						pCnt += rsInt;
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
//					log.error(data.toString());
					log.error(data.toString() + "\n"+ e.toString());
				}

			}
			
			String resultStr = null;
			if(dataList.size() == pCnt) {
				resultStr = RESULT_SUCCESS;
				if (file.exists())
					file.delete();
			} else  {
				resultStr = RESULT_FAIL;
			}
			
			log.info(resultStr + "  : " +file.getName());

			for (String receveData : list) {
				// DB에 저장하는 Tracking Log Line 단위 처리
				if ("Y".equals(IS_API_LOG_SAVE_DB)) {
					String masterCode = System.currentTimeMillis() + "";
					setTrackingLog2DB(receveData, masterCode, resultStr);
				}
			}

		}

		// 처리 완료되면 디렉토리 삭제처리
		File fileToMove =new File(TRACKING_FOLDER+targetFolder);
		if (fileToMove.exists())
			fileToMove.delete();

	}

	/**
	 * 판토스에 보내준 파일 하나에 대하여 모든 Tracking 정보를 리시트 형태로 변환 하는 함수
	 * @param list	: 파일의  String 리스트
	 * @param rowSpliter : String의 split (row 단위 spliter) ['^'] 을 사용
	 * @param columSpliter : String의 split (column 단위 spliter) ['|'] 을 사용
	 * @param dataSplitCount : column의 개수로 현재는 3개임
	 * @return List<PantosTrackingData> 
	 */
	private List<PantosTrackingData> getTrackingDatas(List<String> list, String rowSpliter, String columSpliter, int dataSplitCount) {
		
		List<PantosTrackingData> trackingDatas = new ArrayList<>();
		
		for (String receveData : list) {
			if (receveData == null || "".equals(receveData)) {
				log.debug("NO Tracking Process Data");
				continue;
			}
			for (String trackingData : receveData.split(rowSpliter)) {
				String[] tempStr = trackingData.split(columSpliter);

				if (tempStr == null || tempStr.length != dataSplitCount) {
					log.error("Tracking DATA ERROR :" + trackingData);
					continue;
				}

				trackingDatas.add(new PantosTrackingData(tempStr));
			}
		}
		return trackingDatas;
	}

	/**
	 * 판토스 Tracking 정보 파일 리시트
	 * @param srcFolder [판토스에서 보내주는 Tracking 파일을 받는 폴더이다.]
	 * @param targetFolderName [Tracking 폴더를  새로운 폴더로 이동하여 처리한다.]
	 * @return File[]  리스트 형태로 Tracking 폴더의 모든 파일 리스트
	 */
	private File[] getTrackingFiles(String srcFolder, String targetFolderName) {

		File[] files = null;
		try {
			File srcFile = FileUtils.getFile(srcFolder);
			String[] fileNames = srcFile.list();
			if (fileNames == null || fileNames.length == 0) {
				return null;
			}

			String targetFolder = srcFolder + targetFolderName;

			File fileToMove = FileUtils.getFile(targetFolder);
			FileUtils.moveDirectory(srcFile, fileToMove);

			if (!srcFile.exists())
				srcFile.mkdir();

//			files = fileToMove.listFiles(); // 경로에 있는 파일목록구함
			files = sortFileList(fileToMove.listFiles()); // Date로 Sort실행

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("getTrackingFiles ERROR IOException");
			return null;
		}

		return files;
	}

	private int PANTOS_TRACKING_PARAMETER_LENGTH = 3; // 판토스 결과 파라미터 개수]


	/**
	 * 판토스 연동 처리 함수로 JSONObject 값(Header, Body) 을 암호화 후 판토스 시스템으로 REST 통신을 함 토큰 값을
	 * 가져 올 때 함수를 호출 하며(주문 확정 프로세스 사용) 주문 입력, 주문 확정 , 주문 상세, 주문 리스트 조회 프로세스도 같이 사용함
	 * @param sendJson(PANTOS 연동 JSON) :Header, Body 값 setting 각 프로세스별 전송 값 다름
	 * @return JSONObject : 결과 JSON
	 */
	private JSONObject callPantosAPI(JSONObject sendJson) {

		// 송신 전문이 없는 경우 미처리 하기
		if (sendJson == null)
			return null;

		JSONObject jsonObj = null;
		String sendMessage = sendJson.toString();

		try {
			sendMessage = new SeedUtil().getSeedEncrypt(sendMessage, API_SIGN_KEY);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error(" callPantosAPI Exception error : SeedUtil().getSeedEncrypt : " + sendMessage);
			return jsonObj;
		}

		int THREE_MINUTES = 3 * 60 * 1000;

		@SuppressWarnings("deprecation")
		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(THREE_MINUTES)
				.setConnectTimeout(THREE_MINUTES).setConnectionRequestTimeout(THREE_MINUTES)
				.setStaleConnectionCheckEnabled(true).build();

		CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
		HttpPost httppost = new HttpPost(CALL_API_URI);

		ArrayList<NameValuePair> httpParameters = new ArrayList<NameValuePair>();
		httpParameters.add(new BasicNameValuePair("comCd", PANTOS_COM_CODE)); // 회사연계 코드
		httpParameters.add(new BasicNameValuePair("encYn", CALL_API_ENC)); // 메세지가 암호화 되어 있는 경우 Y로 처리(데이터값을 Y 처리)
		httpParameters.add(new BasicNameValuePair("encver", CALL_API_VERSION)); // 인코팅 모듈 버전 (2.0으로 설정)
		httpParameters.add(new BasicNameValuePair("msg", sendMessage)); // 송신전문(암호화 전)

		log.debug("#### callPantosAPI [comCd] : " + PANTOS_COM_CODE + "	[encYn] : " + CALL_API_ENC + "	[encver] : "
				+ CALL_API_VERSION + "	[msg] : " + sendMessage);

		try {
			httppost.setEntity(new UrlEncodedFormEntity(httpParameters, CHARSET_STRING));
//			log.debug("#### sendPantos [getURI] : " + httppost.getURI().toString());

			CloseableHttpResponse response = client.execute(httppost);

			String resultContent = EntityUtils.toString(response.getEntity(), CHARSET_STRING);
//		    log.debug("#### callPantosAPI Result: " +resultContent);
			try {
				if (resultContent != null && !"".equals(resultContent)) {
					resultContent = new SeedUtil().getSeedDecrypt(resultContent, API_SIGN_KEY);
					jsonObj = new JSONObject(resultContent);
					checkInvalidToken(jsonObj);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				log.error("callPantosAPI JSONException error");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("callPantosAPI Exception error : SeedUtil().getSeedDecrypt : " + resultContent);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("callPantosAPI ERROR UnsupportedEncodingException");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			log.error("callPantosAPI ERROR ClientProtocolException");
		} catch (IOException e) {
			e.printStackTrace();
			log.error("callPantosAPI ERROR IOException");
		}

		return jsonObj;
	}

	private void checkInvalidToken(JSONObject jsonObj) throws JSONException {
		if(!jsonObj.has("header")) return;
		
		JSONObject headerObj = jsonObj.getJSONObject("header") ;
		
		if(!headerObj.has("result")  || !headerObj.has("code")  )  return;
		
		String result = headerObj.getString("result");
		String code = headerObj.getString("code");
		
		if(RESULT_FAIL.equals(result)  && ERROR_TOKEN.equals(code)) {
			this.TOKEN_PANTOS = "";
			return;
		}
		
	}
	
	private String checkResultHeaderValidate(JSONObject headerObj) throws JSONException {
		
		if(!headerObj.has("result")  || !headerObj.has("code")  )  return  null;
		
		String result = headerObj.getString("result");
		String code = headerObj.getString("code");
		
		if(RESULT_FAIL.equals(result)  && ERROR_TOKEN.equals(code)) {
			this.TOKEN_PANTOS = "";
			return code;
		} else if(RESULT_SUCCESS.equals(result) ) {
			return code;
		} else if(RESULT_FAIL.equals(result) ) {
			return code;
		}
		
		return null;
	}
	
	/**
	 * 판토스 연동하기 위한 토큰 값을 가져오는 함수 한번 가져온 토큰 값은 1일 사용한다.
	 * @param ifCd 개인 식별 번호 로 현재는 고정값 'hdcho' 을 사용한다.
	 * @return 토큰 값을 TOKEN_PANTOS라는 Global 변수에 setting 하며 정성 처리 할 경우 True, 토큰 값이 없을
	 *         경우 false
	 */
	private boolean setToken(String ifCd) {

		JSONObject resultJson = null;

		JSONObject sendJson = new JSONObject();

		try {

			JSONObject mObj = new JSONObject(headerString);
			mObj.put("callId", ORDER_CONFIRM);
			mObj.put("ifCd", ifCd);
			mObj.put("token", "");

			sendJson.put(JSON_HEAD_NAME, mObj);
			JSONObject bodyObj = new JSONObject();
			bodyObj.put("soNo", "");
			
			sendJson.put(JSON_BODY_NAME, bodyObj);

			log.debug("setToken sendJson : " + sendJson.toString());
//			System.out.println(sendJson.toString());

			resultJson = callPantosAPI(sendJson);

			JSONObject header = null;

			if (resultJson == null || !resultJson.has(JSON_HEAD_NAME))
				return false;

			if (resultJson != null && resultJson.has(JSON_HEAD_NAME))
				header = resultJson.getJSONObject(JSON_HEAD_NAME);

			if (RESULT_FAIL.equals(header.getString("result")) && TOKEN_PUBLISHED.equals(header.getString("code"))) {

				String token = header.getString("token");

				if (token == null || token.length() <= length_token)
					return false;

				SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMddHHmmss");
				try {
					sdformat.parse(token.substring(token.length() - 14));
				} catch (ParseException e) {
//					e.printStackTrace();
					return false;
				}

				TOKEN_PANTOS = token;
				return true;

			}

		} catch (JSONException e) {
			e.printStackTrace();
			log.error("setToken JSONException");
		}

		return false;
	}

	/**
	 * 기존에 토큰 값 확인 하는 함수 토근 값이 있을 경우 True, 없을 경우 false
	 * @return
	 */
	public static boolean isToken() {

		if (TOKEN_PANTOS == null || TOKEN_PANTOS.length() <= length_token) {
			TOKEN_PANTOS = null;
			return false;
		}

		Calendar cal = new GregorianCalendar();
		Date nowDay = new Date();

		SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			cal.setTime(nowDay);
			cal.add(Calendar.DATE, -1); // 1 일전
			cal.add(Calendar.MINUTE, 10); // 10분전

			Date cDate = sdformat.parse(TOKEN_PANTOS.substring(TOKEN_PANTOS.length() - 14));

			//발급받은 Tocken 값은 하루 동안 사용 할 수 있다.
			long lDiff = cDate.getTime() - cal.getTime().getTime();
			if (lDiff > 0)
				return true;
			else
				TOKEN_PANTOS = null;
		} catch (ParseException e) {
//			e.printStackTrace();
			log.error("isToken TOCKEN Format ERROR");
			TOKEN_PANTOS = null;
			return false;
		}

		return false;
	}

	public File[] sortFileList(File[] files) {

		Arrays.sort(files, new Comparator<Object>() {
			@Override
			public int compare(Object object1, Object object2) {

				String s1 = "";
				String s2 = "";

				s1 = ((File) object1).lastModified() + "";
				s2 = ((File) object2).lastModified() + "";

				return s1.compareTo(s2);

			}
		});

		return files;
	}

	public List<String> readFile(File file, String charset) {
		BufferedReader br = null;
		List<String> list = new ArrayList<>();
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));

			String line = null;
			while ((line = br.readLine()) != null) {
				list.add(line);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("readFile ERROR UnsupportedEncodingException");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("readFile ERROR FileNotFoundException");
		} catch (IOException e) {
			e.printStackTrace();
			log.error("readFile ERROR IOException");
		} finally {
			if (br != null) 	try {	br.close();	 br = null;	} catch (IOException e) {	}
		}

		return list;

	}

	/**
	 * 판토스 연동시 전송한 값(JSONObject:sendJson), 받은 값(JSONObject:resultJson) 을 DB 에 저장
	 * @param sendJson   보낸 값(JSON)
	 * @param resultJson 받은 값(JSON)
	 * @param masterCode 배송 마스터 코드(kshop)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void setAPILog2DB(JSONObject sendJson, JSONObject resultJson, String masterCode) {
		Map<String, String> parm = new HashMap<>();
		parm.put("masterCode", masterCode);
		parm.put("apiParamJson", sendJson.toString());
		parm.put("apiReturnJson", resultJson.toString());

		try {
			pantosMapper.insertAPILog(parm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("setAPILog2DB DB Exception ERROR");
		}
	}

	/**
	 * 판토스 Tracking 정보 연동 결과 DB에 저장
	 * @param receiveData     연동시 받은 String 값(여러 건) 예)
	 *                   PUS000730934|15|20130312142900^PUS000730483|10|20130312145600
	 * @param masterCode 배송 마스터 코드 값 (System.currentTimeMillis()을 값을 사용하여 처리 함)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void setTrackingLog2DB(String receiveData, String masterCode, String result) {
		// TODO Auto-generated method stub
		Map<String, String> parm = new HashMap<>();
		parm.put("masterCode", masterCode);
		parm.put("tracking", receiveData);
		parm.put("result", result);

		try {
			pantosMapper.insertTrackingLog(parm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("setTrackingLog2DB DB Exception ERROR");
		}
	}

	public JSONObject setOrderDeliveryPantos(List<ShipmentPopupData> param){
		
		boolean isSuccess = true;
		JSONObject rsObjJson =  null;
		
		
		//masterCode 로 배송정보 Setting
		List<PantosDeliveryData> list = setSendApiData(param);
		
		if(list == null || list.isEmpty()) {
			return null;
		}
			
		try {
			//판토스 API 연동
			rsObjJson = this.sendOrderInsert(list);
			log.debug(rsObjJson.toString());
			
			if(rsObjJson == null )  return null;
			

			String isResultInvalidate = null;
			String headerKey= "header";
			if(!rsObjJson.has(headerKey)) return null;
			log.debug(rsObjJson.getJSONObject(headerKey).toString());
			//header 값 확인하여 정상인지 실패인지 확인하는 함
			isResultInvalidate=checkResultHeaderValidate(rsObjJson.getJSONObject(headerKey));
			//rsObjJson = null; // 20.07.20. 에러 발생 905 라인 null exception
			if(ERROR_TOKEN.equals(isResultInvalidate)) { 			//토큰 값이 정상적인지 여부 확인 ERR_004 일 경우 토큰 값 오류 발생
				rsObjJson = this.sendOrderInsert(list);
			}  
			
			
			//if(rsobjJson.isNull("body")) return null ;		// 함창수 차장 : pantos.code.error.token=ERR_004 일때 토큰 만료일때 BODY 값이 [] 으로 옴, 수기로 유저가 재시도하면 다시 토큰을 발급 함
			/*
			if(rsobjJson.isNull("body"))		//  토큰 만료로 인한 body 없을때의 재시도 처리 : 조한두 (20.06.25.목)
			{
				LOGGER.debug("setOrderDeliveryPantos body is Null --------------");
				int retryCount=3;
				if("".equals(TOKEN_PANTOS))
				{
					for(int nCount=0; nCount < retryCount; nCount++)
					{
						rsobjJson = this.sendOrderInsert(list);
						LOGGER.debug("-------retry-------"  + nCount);
						if(rsobjJson != null && !rsobjJson.isNull("body") )
						{	
							LOGGER.debug("body is success");
							break;
						}
					}
				}
			}
			*/
			
			if(rsObjJson.isNull("body"))
			{
				log.debug("body is fail ----------------------");
				return null;
			}
			if(rsObjJson.getJSONObject("body").isNull("resultList")) return null;
			
			JSONArray aryJson = rsObjJson.getJSONObject("body").getJSONArray("resultList");

			//배성정보 DB Update 처리(delivery)
			isSuccess = updateDelivery(aryJson, rsObjJson.getString("apiCode"));

			// 쇼피파이 API 배송상태 업데이트 시작 (Pantos)
			List<String> masterCodeList = new ArrayList<>();
			int arrayLength = aryJson.length();
			for (int i = 0; i < arrayLength; i++) {
				JSONObject obj = aryJson.getJSONObject(i);
				if(RESULT_SUCCESS.equals(obj.getString("result"))) {
					masterCodeList.add(obj.getString("coNo"));
				}
			}
			
			
			if(masterCodeList != null && !masterCodeList.isEmpty()) {
				boolean flag = paymentPopupRestService.shopifyPostOrderIdFullist(getStringArray(masterCodeList), null);
			}
			// 쇼피파이 API 배송상태 업데이트 끝 (Pantos)
			
			rsObjJson = new JSONObject();
			rsObjJson.put("result", isSuccess?RESULT_SUCCESS:RESULT_FAIL);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			log.error(e.toString());
			return null;
		}
		
		return rsObjJson;
	}

	private boolean updateDelivery( JSONArray aryJson, String apiCode) throws JSONException {
		
		boolean isSuccess = true;
		
		int arrayLength = aryJson.length();
		
		
		for (int i = 0; i < arrayLength; i++) {
			
			JSONObject obj = aryJson.getJSONObject(i);
			
			Map parm =new HashMap();
			parm.put("masterCode", obj.getString("coNo"));
			parm.put("soNo", obj.getString("soNo"));
			parm.put("hblNo", obj.getString("hblNo"));
			parm.put("result", obj.getString("result"));
			parm.put("errMsg", obj.getString("errMsg"));
			parm.put("apiCode", apiCode);
			parm.put("stateGrop", "A020000");
			
			if(RESULT_SUCCESS.equals(obj.getString("result"))) {
		
				parm.put("state", "1");
				
			} else {
				parm.put("state", "-99");
				isSuccess = false;
			}
			
			log.debug(parm.toString());
			
			int rsInt = pantosMapper.updateOrderPantos(parm);
			
		}
		
		return isSuccess;
	}

	/**
	 * masterCode List를 배송정보 리스트로 변경
	 * @param List<ShipmentPopupData> param
	 * @return
	 */
	public List<PantosDeliveryData> setSendApiData(List<ShipmentPopupData> param){
		List<PantosDeliveryData> list = pantosMapper.selectDeliveryPantos(param);
		String masterCode = null;
		
		for (int i = 0; i < list.size(); i++) {
			PantosDeliveryData pantosData = list.get(i) ;
			
			try {
				pantosData.setUntprc((int)changeCurrency(pantosData.getUnitCurrency(), pantosData.getUntprc()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("changeCurrency ERROR");
				
			}
			if(pantosData.getHomepageAddr().equals(""))
			{
				String hompageAddr = getProductHandleFullUrl(pantosData.getShopIdx(),pantosData.getProductId());
				if(hompageAddr.equals("") || hompageAddr.equals(null))
					pantosData.setHomepageAddr("https://www.shopify.com/");
				else
					pantosData.setHomepageAddr(hompageAddr);
			}
			
			if(pantosData.getItemCd().equals("Clothes"))
			{
				String productType = getProductTypeUrl(pantosData.getShopIdx(),pantosData.getProductId());
				log.debug("###ProductType:"+productType);
				if(productType.equals("") || productType.equals(null))
					pantosData.setItemCd("Others");
				else
					pantosData.setItemCd(productType);
			}
			pantosData.setCneeNm1(util.nullToEmpty(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, pantosData.getCneeNm1())));
			pantosData.setCneeNm2(util.nullToEmpty(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, pantosData.getCneeNm2())));
			pantosData.setCneeTelNo(util.nullToEmpty(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, pantosData.getCneeTelNo())));
			
			pantosData.setCneeNm(util.nullToEmpty(pantosData.getCneeNm1()) + " " + util.nullToEmpty(pantosData.getCneeNm2())  );

			pantosData.setShppNm(util.nullToEmpty(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, pantosData.getShppNm())));
			pantosData.setShppAddr(pantosData.getShppNm()+"\n"+pantosData.getShppAddr());
			pantosData.setShppTelNo(util.nullToEmpty(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, pantosData.getShppTelNo())));
			pantosData.setWgt(pantosData.getWgt() *0.001);
			
			if(pantosData.getCneeNatnCd().equals("BR")) {
				pantosData.setTaxDscrnNo("394.183.868-79");
			}
			
			if(i == 0 ) {
				masterCode = pantosData.getCoNo();
			} else {
			 	if(masterCode.equals(pantosData.getCoNo())) {
			 		pantosData.setBoxQty("0");
			 		pantosData.setWgt(0);
			 	} else {
			 		masterCode = pantosData.getCoNo();
			 	}
			}
			
		}
		return list;
	}

	public Map<String, Object> popPaymentShipmentProc(List<ShipmentPopupData> shipPopDataList, HttpSession sess){
		
		Map<String, Object> rsMap = new HashMap<>();
		
		try {
			JSONObject rsJson = setOrderDeliveryPantos(shipPopDataList);
			
			if(rsJson == null || rsJson.isNull("result") || RESULT_FAIL.equals(rsJson.getString("result"))) {
				rsMap.put("result", RESULT_FAIL);
				rsMap.put("errCode", false );
				rsMap.put("errMsg", "API Error");
			}  else {
				rsMap.put("result", RESULT_SUCCESS);
				rsMap.put("errCode", true);
				rsMap.put("errMsg", "");
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("setOrderDeliveryPantos ERROR");
			rsMap.put("result", RESULT_FAIL);
			rsMap.put("errCode", false );
			rsMap.put("errMsg", "API Error");
		}		
		
		return rsMap;
	}

	public String[] getStringArray(List<String> arr)  { 
        String str[] = new String[arr.size()]; 
        for (int j = 0; j < arr.size(); j++) { 
            str[j] = arr.get(j); 
        } 
        return str; 
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
	
	public String  getProductTypeUrl(String shopIdx, String productId) {
		
		String url = "";
		String ProductCd = ""; 
		String proudctType = "";             
		String domain = null;
		HttpHeaders httpHeaders = null;
		if ( domain == null ) {
			RestData restData = restService.getRestDataFromShopIdx(shopIdx);
			domain = restData.getShopData().getDomain();
			
			httpHeaders = restData.getHttpHeaders();
		}
		
		url = String.format(this.productUrl, domain, productId);
		JsonNode node = restService.getRestTemplate(httpHeaders, url);
		if(node != null)
		{
			List<JsonNode> productitems =  node.findValues("product");
			
			for( JsonNode item : productitems ) {
				ProductCd = item.findValue("product_type").asText();
				
			}
		}
		if(!"".equals(proudctType))
			ProductCd ="OTHERS";
		return ProductCd;
	}
	
	public String  getProductHandleFullUrl(String shopIdx, String productId) {
		String url = "";
		String urlProductLink = ""; 
		String handle = "";              // 상품의 handle -> SEO 명 
		String domain = null;
		HttpHeaders httpHeaders = null;
		if ( domain == null ) {
			RestData restData = restService.getRestDataFromShopIdx(shopIdx);
			domain = restData.getShopData().getDomain();
			httpHeaders = restData.getHttpHeaders();
		}

		url = String.format(this.productUrl, domain, productId);
		JsonNode node = restService.getRestTemplate(httpHeaders, url);
		if(node != null)
		{
			List<JsonNode> productitems =  node.findValues("product");
			
			for( JsonNode item : productitems ) {
				handle = item.findValue("handle").asText();

			}
		}
		if(!"".equals(handle))
		urlProductLink =	"https://" + domain + "/products/" + handle;
		return urlProductLink;
	}
	
	
}
