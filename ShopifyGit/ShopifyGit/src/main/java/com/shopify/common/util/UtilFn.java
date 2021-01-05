package com.shopify.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import com.shopify.common.SpConstants;
import com.shopify.shop.ShopData;
 
@Controller
public class UtilFn {
	private Logger LOGGER = LoggerFactory.getLogger(UtilFn.class);
	
	public String randomNumber(int n){	//n개의 랜덤숫자
		Random rand = new Random();
		String str = "";
		for(int i = 0 ; i < n ; i++) {
				str += rand.nextInt(30); // 0~29
		}
		return str;
    }
	
	public String randomNumber(int n, int s){ //n개의 s자리 랜덤숫자
        Random rand = new Random();
        String str = "";
        for(int i = 0 ; i < n ; i++) {
                str += rand.nextInt(30); // 0~29
        }
        return str.substring(0,s);
    }
	
	//url빌드
	public static String httpBuildQuery(List<? extends NameValuePair> parameters, String encoding) {
	    return URLEncodedUtils.format(parameters, encoding).replace("*", "%2A");
	}

	/**
	 * 공통 > 날짜포멧 설정
	 * @param String, String : 'Y'=뒤 -5시간 처리
	 * @return String
	 */
	public String dateFormat(String dateStr, String tp) {
		String rtn = "";
		if(dateStr != null) {
			SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
			if("Y".equals(tp)) {
				rtn = dateStr.substring(0, dateStr.length()-6);
				rtn = rtn.replace("T", " ");
			}else {
				rtn = format1.format(dateStr);
			}
			
		}
		return rtn; 
	}
	
	/**
	 * 공통 > 오늘날짜 구해오기(각 요소별로)
	 * @param String : 조회할 날짜요소 종류 yy=년, mm=월, dd=일, hh=시, mi=분, ss=초
	 * @return String
	 */
	public String getDateElement(String dataType) {		//dataType 조회할 종류
		Calendar calendar = new GregorianCalendar(Locale.KOREA);
		String toYear = calendar.get(Calendar.YEAR)+"";
		String toMonth = (calendar.get(Calendar.MONTH) + 1)+"";
		String toDay = calendar.get(Calendar.DAY_OF_MONTH)+"";
		String toHour = calendar.get(Calendar.HOUR_OF_DAY)+"";
		String toMin = calendar.get(Calendar.MINUTE)+"";
		String toSec = calendar.get(Calendar.SECOND)+"";
		String toLday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)+"";
		
		String returnStr = "";
		if("yy".equals(dataType.toLowerCase()))				 returnStr = toYear;
		else if("mm".equals(dataType.toLowerCase()))		 returnStr = toMonth;
		else if("dd".equals(dataType.toLowerCase()))		 returnStr = toDay;
		else if("ld".equals(dataType.toLowerCase()))         returnStr = toLday;
		else if("hh".equals(dataType.toLowerCase()))		 returnStr = toHour;
		else if("mi".equals(dataType.toLowerCase()))		 returnStr = toMin;
		else if("ss".equals(dataType.toLowerCase()))		 returnStr = toSec;
		else if("yymm".equals(dataType.toLowerCase())) {
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM");		
			returnStr = sdf.format(calendar.getTime());
		} else if("yyyymmdd".equals(dataType.toLowerCase())) {
			SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");		
			returnStr = sdf.format(calendar.getTime());
		} else if("today".equals(dataType.toLowerCase())) {
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");		
			returnStr = sdf.format(calendar.getTime());
		} else if("mm-1".equals(dataType.toLowerCase())) {
			   calendar.add(Calendar.MONTH, -1);
			   Date currentTime=calendar.getTime();
			   SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
			   String release_Dt_start=formatter.format(currentTime);
			
			   returnStr = release_Dt_start;
		} else if("dd-1".equals(dataType.toLowerCase())) {
			   calendar.add(Calendar.DATE, -1);
			   Date currentTime=calendar.getTime();
			   SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
			   String release_Dt_start=formatter.format(currentTime);
			
			   returnStr = release_Dt_start;
		}
		else if("full".equals(dataType.toLowerCase())) {
			SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");		
			returnStr = sdf.format(calendar.getTime());
		}

		return returnStr;		
	}
	
	/**
	 * 공통 > 오늘 날짜를 지정된 포멧으로 출력한다.
	 * @param String : 출력받고자 하는 날짜 포멧
	 * @return String
	 */
	public String getToday(String sDateFormat) {
		Calendar calendar = new GregorianCalendar(Locale.KOREA) ;
		if (!(sDateFormat != null && sDateFormat.trim().length() > 0)) {
			sDateFormat = "YYYY-MM-dd" ;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(sDateFormat) ;		
		return sdf.format(calendar.getTime()) ; 		
	}
	
	/**
	 * 공통 > 오늘을 기준으로 지정된 일수 만큼 과거의 날짜를 지정된 포멧으로 출력한다.
	 * @param int : 일 수(오늘로부터 며칠 전의 날짜인지)
	 * @param String : 출력받고자 하는 날짜 포멧
	 * @return String
	 */
	public String getPrevDate(int nDays, String sDateFormat) {
		Calendar calendar = new GregorianCalendar(Locale.KOREA) ;
		if (nDays > 0) {
			nDays *= -1 ;
		}
		calendar.add(Calendar.DATE, nDays) ;
		if (!(sDateFormat != null && sDateFormat.trim().length() > 0)) {
			sDateFormat = "YYYY-MM-dd" ;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(sDateFormat) ;		
		return sdf.format(calendar.getTime()) ; 		
	}
	
	/**
	 * 공통 > 특정 당 마지막 날 계산 
	 * @param String : 날짜
	 * @return int
	 * @throws ParseException 
	 */
	public int getLastDayElement(String strDate) throws ParseException {	
		
		// 형변환 날짜 세팅
		SimpleDateFormat transeDate = new SimpleDateFormat("yyyy-MM-dd");
		Date tdate = transeDate.parse(strDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(tdate);
		
		//입력받은 달의 마지막일을 구한다.
		int endDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		
		return endDay; 
	}
	
	
	/**
	 * 공통 > 랜덤수 구하기
	 * @param int : 랜덤문자수
	 * @return String
	 */
	public String getRandomString(int randLength) {
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
	
	/**
	 * 공통 > 암호화 하기
	 * @param String : 암호화 키, 엄호화할 문자열
	 * @return String
	 */
	public String getAESEncrypt(String enKey,String str) {
		String returnStr = "";
		try
	    {
			if ( str != null ) {
				SecretKeySpec ks = new SecretKeySpec(generateKey(enKey), "AES");
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
	public byte[] generateKey(String key) {
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
	 * 공통 > 복호화 하기
	 * @param String : 복호화 키, 복호화할 문자열
	 * @return String
	 */
	public String getAESDecrypt(String deKey,String str) {
		String returnStr = "";
		 if(str.equals("") || str == null)
	            return "";
		try
	    {
	        SecretKeySpec ks = new SecretKeySpec(generateKey(deKey), "AES");
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
	
	/**
	 * 공통 > 단방향암호화
	 * @param String : 복호화할 문자열
	 * @return String
	 */
	public String getBCryptDecrypt(String str) {
		return new BCryptPasswordEncoder().encode(str);
	}
	
	/**
	 * 공통 > MD5
	 * @param String : 문자열
	 * @return String
	 */
	public String getMD5Encrypt(String str) {
	    String MD5 = "";
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        md.update(str.getBytes());
	        byte byteData[] = md.digest();
	        StringBuffer sb = new StringBuffer();
	        
	        for (int i = 0; i < byteData.length; i++) {
	            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        
	        MD5 = sb.toString();
	 
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	        MD5 = null;
	    }
	    return MD5;
	}
	
	/**
	 * 공통 > 세션 이메일 받아 오기
	 * @param HttpSession
	 * @return String
	 */
	public String getUserEmail(HttpSession sess) {
		String email = "";
		
		//try {
			ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
			email = sd.getEmail();
		//} catch (Exception e) {
		//	e.printStackTrace();
		//}

		return email;
	}
	
	/**
	 * 공통 > 세션 도메인 앞자리 받아 오기
	 * @param HttpSession
	 * @return String
	 */
	public String getUserDomain(HttpSession sess) {
		String domain = "";
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		domain = sd.getDomain();
		if(domain != null) {
			String[] dmAry = domain.split("\\.");
			domain = dmAry[0];
		}else {
			domain = sd.getShopName();
		}
		

		return domain;
	}
	
	/**
	 * 공통 > 세션 도메인 앞자리 받아 오기
	 * @param HttpSession
	 * @return String
	 */
	public String getUserVoDomain(ShopData sd) {
		String domain = "";
		domain = sd.getDomain();
		if(domain != null) {
			String[] dmAry = domain.split("\\.");
			domain = dmAry[0];
		}else {
			domain = sd.getShopName();
		}
		

		return domain;
	}
	
	/**
     * 공통 > 세션에따라서 데이터 변형
     * @param HttpSession
     * @return String
     */
	public String getLocaleChangeStr(HttpSession sess,String koStr, String enStr) {
		String str = "";
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String locale = sd.getLocale();
		if("ko".equals(locale)) {
			str = koStr;
		}else {
			str = enStr;
		}
		return str;
	}
    
    /**
     * 공통 > 세션 다국어 받아 오기
     * @param HttpSession
     * @return String
     */
    public String getUserLocale(HttpSession sess) {
        String locale = "";
        
        //try {
            ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
            locale = sd.getLocale();
        //} catch (Exception e) {
        //  e.printStackTrace();
        //}

        return locale;
    }
	
	/**
	 * 공통 > 무게 g단위로 변환
	 * @param fromWidth : 무게
	 * @param fromUnit : 단위(g, kg, oz, ld)
	 * @return int
	 */
	public int wigthConversion(double fromWeight, String fromUnit) {
		
		double toWidth = 0;
		
		if("g".equals(fromUnit)) {
//			toWidth += (double) fromWeight;
			toWidth += fromWeight; 
		} else if("kg".equals(fromUnit)) {
//			toWidth += (double) fromWeight * 1000;
			toWidth +=  fromWeight * 1000;
		} else if("oz".equals(fromUnit)) {
//			toWidth += (double) (fromWeight) / 35.274;
			toWidth += (fromWeight) / 35.274;
		} else if("ld".equals(fromUnit)) {
//			toWidth += (double) (fromWeight) / 2.2046226218487757;
			toWidth += (fromWeight) / 2.2046226218487757;
		}		
		
		return (int) toWidth;
	}
	
	/**
	 * 공통 > null 값 공백으로 치환
	 * @param str : 체크 문자
	 * @return String
	 */
	public String nullToEmpty(String str) {
		if(str == null || "".equalsIgnoreCase(str)) {
			str = "";
		}
		
		return str;
	}
	
	/**
	 * 공통 > 특수 문자 치환
	 * @param str : 체크 문자
	 * @return String
	 */
	public String strReplace(String str) {
		return str.replaceAll("[&<>\\\\'/#]", " ");
		
//	    	.replaceAll("<", "&lt;")
//	    	.replaceAll(">", "&gt;")
//	    	.replaceAll("\\\"", "&quot;")	    	
//	    	.replaceAll("'", "&#x27;")
//	    	.replaceAll("/", "&#x2F;")
//	    	.replaceAll("#", "&#35;");
//		return reStr;
	}
	
//	/**
//	 * 공통 > 특수 문자 치환 복원
//	 * @param str : 체크 문자
//	 * @return String
//	 */
//	public String strConvert(String str) {
//		String reStr = str.replaceAll("&lt;", "<")
//	    	.replaceAll("&gt;", ">")
//	    	.replaceAll("&quot;", "\\\"")	    	
//	    	.replaceAll("&#x27;", "'")
//	    	.replaceAll("&#x2F;", "/")
//	    	.replaceAll("&amp;","&")
//	    	.replaceAll("&#35;", "#");
//		return reStr;
//	}
	
	/**
	 * 공통 > 페이징 데이터 받아 오기
	 * @param dataCount : 전체 데이터 수
	 * @param pageSize : 페이지 당 데이터 수
	 * @param pageBlockSize : 페이징 블럭 사이즈
	 * @param currentPage :현제 페이지
	 * @return Map<String, Object>
	 */
	public Map<String, Object> getPagingData(int dataCount, int pageSize, int pageBlockSize, int currentPage) {
		int startRow = (currentPage - 1) * pageSize; //데이터 시작 위치
		
		int startPageNum = (int) Math.floor(((double) currentPage - 1) / (double) pageBlockSize) * pageBlockSize + 1; //페이징 시작 위치
		int endPageNum = (startPageNum + pageBlockSize) - 1; //페이징 마감위치
		
		double tmpTotalPageNum = Math.ceil(((double) dataCount)/((double) pageSize)); //전체 페이지 수
		int totalPageNum = (int) tmpTotalPageNum; //전체 페이지 수
		
		endPageNum = (endPageNum > totalPageNum ? totalPageNum : endPageNum); //페이징 마감위치
		
		//LOGGER.debug("totalPageNum : " + totalPageNum);
		//LOGGER.debug("currentPage : " + currentPage);
		
		int nextPage = (totalPageNum <= currentPage ? 0 : currentPage + 1); //다음 페이지 
		int prePage = (currentPage <= 1 ? 0 : currentPage - 1); //이전 페이지
		
//		LOGGER.debug("startPageNum : " + startPageNum);
//		LOGGER.debug("endPageNum : " + endPageNum);
		
		int nextPageBlock = (endPageNum >= totalPageNum ? 0 : endPageNum + 1); //다음 페이지 블럭
		int prePageBlock = (startPageNum <= 1 ? 0 : startPageNum - pageBlockSize); //이전 페이지 블럭
		
		int countNum = dataCount - (currentPage - 1) * pageSize;
		
//		LOGGER.debug("###################################### startRow : " + startRow);
//		LOGGER.debug("###################################### endPageNum : " + endPageNum);
		
		
		Map<String, Object> paging = new HashMap<String, Object>();
		paging.put("startRow", startRow);            // 데이터 시작 위치
		paging.put("startPageNum", startPageNum);    // 페이징 시작 위치
		paging.put("endPageNum", endPageNum);        // 페이징 마감위치
		paging.put("currentPage", currentPage);      // 현재 페이지
		paging.put("nextPage", nextPage);            // 다음 페이지
		paging.put("prePage", prePage);              // 이전 페이지
		paging.put("nextPageBlock", nextPageBlock);  // 다음 페이지 블럭 
		paging.put("prePageBlock", prePageBlock);    // 이전 페이지 블럭
		paging.put("totalPageNum", totalPageNum);    // 전체 페이지 수
		paging.put("countNum", countNum);            // 리스트 No

		return paging;
	}
	
	/**
	 * 공통 > host ip 받아 오기 
	 * @return String
	 */
	public String getHostIpAddress() {
		String hostIpAddress = "";
		
		try {
			Enumeration<NetworkInterface> nienum = NetworkInterface.getNetworkInterfaces();
			
			while (nienum.hasMoreElements()) {
				NetworkInterface ni = nienum.nextElement();
				Enumeration<InetAddress> kk= ni.getInetAddresses();
				
				while (kk.hasMoreElements()) {
					InetAddress inetAddress = kk.nextElement();

					if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
						hostIpAddress = inetAddress.getHostAddress().toString();
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return hostIpAddress;
	}

	
	/**
	 * json data null 체크
	 * @throws JSONException 
	 */
	public JSONObject jsonNullCk(JSONObject json,String key, String value) throws JSONException {
		if(value == null || value.equals("")) {
			json.put(key, " ");
		}
		else if(value.equals("null"))
			json.put(key, " ");
		else 
			json.put(key, value);
		return json;
	}
	
	public String getUid() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.KOREA) ;
		return formatter.format(new java.util.Date()) ;
	}
	
	public String nPad(int nLen, int nVal) {
		String sValue = String.valueOf(nVal) ;
		String sResult = "00000000" ;
		if (nLen > sValue.length()) {
			sResult += sValue ;
			sResult = sResult.substring(sResult.length()-nLen) ;
		} else {
			sResult = sValue ;
		}
		return sResult ;
	}
	
	
}