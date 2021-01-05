package com.shopify.common.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.shopify.common.SpConstants;
import com.shopify.setting.SettingSenderData;
import com.shopify.shop.ShopData;

import io.micrometer.core.instrument.util.StringUtils;

public class UtilFunc {
	private static Logger LOGGER = LoggerFactory.getLogger(UtilFunc.class);
	
	private static String stackPrefix = "\n\tSTACK\t";
	
	/**
	 * json data null 체크
	 * @throws JSONException 
	 */
	public static JSONObject jsonNullCk(JSONObject json,String key, String value) throws JSONException {
		if( StringUtils.isEmpty(value) ) {
			json.put(key, " ");
		}
		else if(value.equals("null"))
			json.put(key, " ");
		else 
			json.put(key, value);
		return json;
	}
	
	
	public static String getJsonString(String str) {
		String value;
		 if( "null".equals(str) || StringUtils.isEmpty(str))
				 value = "";
          else
             value = str;
		return value;
	}
	
	
	/**
	 * 공통 > 복호화 하기
	 * @param String : 복호화 키, 복호화할 문자열
	 * @return String
	 */
	public static String getAESDecrypt(HttpSession sess) {

		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		return getAESDecrypt( sd.getAccessToken() );
	}
	
	public static String getAESDecrypt(String str) {
		String returnStr = "";
		 if( str == null || str.equals("") )
	            return "";
		try
	    {
	        SecretKeySpec ks = new SecretKeySpec(generateKey(SpConstants.ENCRYPT_KEY), "AES");
	        Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(2, ks);
	        byte decryptedBytes[] = cipher.doFinal(Base64Coder.decode(str));
	        returnStr = new String(decryptedBytes).toString();
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	    }
		return returnStr;
	}
	
	public static String getAESEncrypt(String str) {
		String returnStr = "";
		try
	    {
			if ( str != null ) {
				SecretKeySpec ks = new SecretKeySpec(generateKey(SpConstants.ENCRYPT_KEY), "AES");
				Cipher cipher = Cipher.getInstance("AES");
				cipher.init(1, ks);
				byte encryptedBytes[] = cipher.doFinal(str.getBytes());
				returnStr = new String(Base64Coder.encode(encryptedBytes));
			}
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	    }
		return returnStr;
	}
	
	
	/**
	 * 공통 > 암호화 하기
	 * @param String : 암호화 키
	 * @return String
	 */
	public static byte[] generateKey(String key) {
		byte desKey[] = new byte[16];
		byte bkey[] = key.getBytes();
		if(bkey.length < desKey.length)
		{
			System.arraycopy(bkey, 0, desKey, 0, bkey.length);
			for(int i = bkey.length; i < desKey.length; i++)
				desKey[i] = 0;

		} else {
			System.arraycopy(bkey, 0, desKey, 0, desKey.length);
		}
		return desKey;
	}
	
	/**
	 * 공통 > 랜덤수 구하기
	 * @param int : 랜덤문자수
	 * @return String
	 */
	public static String getRandomString(int randLength) {
		StringBuffer returnStr = new StringBuffer();
		if(randLength <= 0) randLength = 20;
		Random rnd = new Random();
		for (int i = 0; i < randLength; i++) {
			int rIndex = rnd.nextInt(3);
			switch (rIndex) {
			case 0:
				// a-z
				returnStr.append((char) ((int) (rnd.nextInt(26)) + 97));
				break;
			case 1:
				// A-Z
				returnStr.append((char) ((int) (rnd.nextInt(26)) + 65));
				break;
			case 2:
				// 0-9
				returnStr.append((rnd.nextInt(10)));
				break;
			}
		}
		return returnStr.toString();
	}


	public static HttpHeaders generateHeader(HttpSession sess) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		headers.add("X-Shopify-Access-Token", getAESDecrypt(sess));
		
		return headers;
	}


	public static String getShopDomain(HttpSession sess) {
    	ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		return sd.getDomain();
	}


	public static String getRestTemplate(RestTemplate restTemplate, HttpHeaders httpHeaders, String url) {
		
		HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);
		
		return restExchange(restTemplate, url, HttpMethod.GET, httpEntity);
		
	}
	
	public static String postRestTemplate(RestTemplate restTemplate, HttpHeaders httpHeaders, String url, JSONObject parameters) {
		
		HttpEntity<String> httpEntity = new HttpEntity<String>(parameters.toString(), httpHeaders);
		
		return restExchange(restTemplate, url, HttpMethod.POST, httpEntity);
		
	}
	
	private static String restExchange(RestTemplate restTemplate, String url, HttpMethod method, HttpEntity<String> httpEntity) {
		
		String response = null;
		
//		try {
			ResponseEntity<String> responseEntityStr = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
			HttpStatus statusCode = responseEntityStr.getStatusCode();
			
			if (  statusCode.is2xxSuccessful() ) {
				response = responseEntityStr.getBody();
			}
//		} catch (RestClientException rce){
//			rce.printStackTrace();
//		}
		return response;
	}
	
//	public static String debugRestRequest( ) {
//		
//	}
	
	
	/***
	 * JsonNode findPath() 가 isNull() 인 경우, null 을 return 하는 utility 
	 * @param node
	 * @param name
	 * @return
	 */
	public static String findPath(JsonNode node, String name) {
		JsonNode result = node.findPath(name);
		
		return ( result == null || result.isNull() ) ? "" : result.asText();
	}
	
	public static String getStackTrace(String data) {
		
		StringBuilder buffer = new StringBuilder();
		
		buffer.append(stackPrefix);
		buffer.append(data);
		buffer.append(stackPrefix);
		
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		for( StackTraceElement ele : ste ) {
			buffer.append(ele.toString());
			buffer.append(stackPrefix);
		}
		return buffer.toString();
	}


	public static String getStackTrace(SettingSenderData setting) {

		String data = String.format("INPUT : useDefault=%s, senderIdx=%d, email=%s, shopIdx=%d", 
				setting.getUseDefault(),
				setting.getSenderIdx(),
				setting.getEmail(),
				setting.getShopIdx() );
		
		return getStackTrace(data);
		
	}
	
	public static void printValue(Object obj) {
		
		System.out.println(obj.getClass().getName());
		
		ReflectionUtils.doWithFields(obj.getClass(), new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
            	boolean flag = false;
                ReflectionUtils.makeAccessible(field);
                Class<?> type = field.getType();
                Object fieldValue = ReflectionUtils.getField(field, obj);
                if ( fieldValue != null ) {
                	String value = fieldValue.toString();
                	
                	if ( type.isPrimitive() || obj instanceof Number ) {
                		if ( ! "0".equals(value)  ) {
                			flag = true;
                		}
                	} else {
                		if ( obj != null ) {
                			flag = true;
                		}
                	}
                }
                if ( flag ) {
                	System.out.println(String.format("\t%10s %-12s %s", field.getType().getName().replaceAll("^java.lang.", ""), field.getName(), fieldValue));
                }
            }
        });
        
	}
	
	public static String toValueString(Object obj) {
		
		StringBuilder buffer = new StringBuilder();
		
		buffer.append(obj.getClass().getSimpleName());
		buffer.append( "( ");
		
		ReflectionUtils.doWithFields(obj.getClass(), new ReflectionUtils.FieldCallback() {
			@Override
			public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
				boolean flag = false;
				ReflectionUtils.makeAccessible(field);
				Class<?> type = field.getType();
				Object fieldValue = ReflectionUtils.getField(field, obj);
				if ( fieldValue != null ) {
					String value = fieldValue.toString();
					
					if ( type.isPrimitive() || obj instanceof Number ) {
						if ( ! "0".equals(value)  ) {
							flag = true;
						}
					} else {
						if ( obj != null ) {
							flag = true;
						}
					}
				}
				if ( flag ) {
					buffer.append(String.format("%s=%s, ", field.getName(), fieldValue));
				}
			}
		});
		
		if(buffer.length() > 1){
			buffer.deleteCharAt(buffer.length() - 2);
		}

		buffer.append( " )");
		return buffer.toString();		
	}
	
	public static void printValueString(Object obj) {
		System.out.println(toValueString(obj));
	}
	
	public static void initializeBean(Object object) {
		
		Field[] declaredFields = object.getClass().getDeclaredFields();
		
		for( Field f : declaredFields ) {
			String name = f.getName();
			System.out.println(name);
			Class<?> type = f.getType();
			f.setAccessible(true);
			
			try {
				if ( type.equals(String.class) ) {
					f.set(object, "");
				} else {
					throw new RuntimeException("not yet defined !!!!");
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}


	public static String calcDays(String from, int gap) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate fromDate = LocalDate.parse(from, formatter);
		LocalDate toDate = fromDate.plusDays(gap);
		String to = toDate.format(formatter);
		
		return to;
	}

	public static String plusDays(int gap, String pattern) {
		
		LocalDate localDate = LocalDate.now().plusDays(gap);
		String date = localDate.format( DateTimeFormatter.ofPattern(pattern));
		
		return date;
	}
	
	public static String plusDays(int gap) {
		return plusDays(gap, "yyyy-MM-dd");
	}
	

	public static boolean isBefore(String date) {
		LocalDate today = LocalDate.now();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(date, formatter);
		
		return(localDate.isBefore(today));
	}


	public static String getHostIpAddress() {

		String ip = null;
		
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			ip = "";
			e.printStackTrace();
		}

		return ip;
	}


	public static String today(String format) {
		LocalDateTime localDate = LocalDateTime.now();
		return localDate.format( DateTimeFormatter.ofPattern(format));
	}
	
	public static String today() {
		return today("yyyy-MM-dd");
	}
	
	public static String todayTime() {
		return today("yyyy-MM-dd HH:mm:ss");
	}
	
	public static String trimZipCode(String text, String nation) {
		if(nation.equals("RU") || nation.equals("CA")) {
			return text;
		}
		String compact = text.replaceAll("\\D+", "");
		return compact.length() > 5 ? compact.substring(0, 5) : compact;
	}


	public static void setExcelResponse(HttpServletResponse response, String name) {
		
		String fileName = name + "_" + today();
//		response.setHeader("Content-Type", "application/vnd.ms-xls");
		response.setHeader("Content-Type", "application/vnd.ms-excel;charset=UTF-8");
//		response.setHeader("Content-Language", "ko");
		String filename = null;
		try {
			filename = new String((fileName).getBytes("UTF-8"), "8859_1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".xls");
		
	}


	public static int parseInt(String weight) {
		if ( weight.contains(",")) {
			weight = weight.replaceAll(",", "");
		}
		return Integer.parseInt(weight);
	}


	public static void unCommaMap(Map<String, String> input, String... keys) {
		for (Entry<String, String> e : input.entrySet()) {
			for( String key : keys ) {
				if (e.getKey().equals(key) && e.getValue().contains(",")) {
					input.put(key, e.getValue().replaceAll(",", ""));
				}
			}
		}
	}
	
	public static String DeliverySellerPhone(String sellerPhone) {
		// 연락처 설정
					String[] sellerMobile = new String[4];
					String deliverySellerPhone = sellerPhone;
					
					if(sellerPhone != null) {
						String[] arrSellerMobile = sellerPhone.split("-");
						
						if(arrSellerMobile.length < 4) {
							deliverySellerPhone = sellerPhone;
						} else {
							int i = 0;
							for (String phone : arrSellerMobile) {
								if(i < 4) {
									sellerMobile[i] = phone;
									i++;
								}
							}
							if(sellerMobile[1].equals("10")) {
								sellerMobile[1] = "010"; 
							}
							deliverySellerPhone = sellerMobile[1] + sellerMobile[2] + sellerMobile[3];
						}
					}
		return deliverySellerPhone;
	}
	
	public static String[] splitComma(String input, int count) {
		
		String[] array = null;
		
		if ( input == null ) {
			array = new String[count];
	        for( int i = 0; i < count; i++ ) {
	        	array[i] = "";
	        }
		} else {
//			array = input.split(",", count);
			array = input.split(",",  org.apache.commons.lang.StringUtils.countMatches(input, ",") + 1);
		}
		
		return array;
	}


	
}