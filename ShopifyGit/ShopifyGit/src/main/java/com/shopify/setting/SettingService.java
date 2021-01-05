
package com.shopify.setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.shopify.common.JsonNodeHead;
import com.shopify.common.RestApiData;
import com.shopify.common.RestData;
import com.shopify.common.RestService;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.common.util.UtilFunc;
import com.shopify.mapper.OrderMapper;
import com.shopify.mapper.SettingMapper;
import com.shopify.order.popup.OrderCourierData;
import com.shopify.shop.ShopData;

/**
*  : 2019-12-26
* @desc   : 세팅 정보 서비스 
*/

@Service
@Transactional
public class SettingService {
	private Logger LOGGER = LoggerFactory.getLogger(SettingService.class);
	
	@Autowired 
	private SettingMapper settingMapper;
	
	@Autowired 
	private OrderMapper orderMapper;
	
	@Autowired	
	private UtilFn util;

	@Autowired	private RestService restService;
	
	@Value("${Product.Get.All}") private String getproductUrl;
	@Value("${Order.Inventory}") private String inventoryUrl;
	private static final Pattern urlPattern = Pattern.compile("^<(.*)>; rel=\"(\\w+)\"");
	/**
	 * 설정 > 계정관리 > 채널리스트
	 * @param SettingData
	 * @param HttpSession
	 * @return SettingData
	 */
	public SettingData selectSeller(SettingData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		SettingData seller = settingMapper.selectSeller(setting);
		seller.setRankName(settingMapper.selectSellerRank(seller.getRankId()));
		
		try {
			seller.setFirstName(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, seller.getFirstName()));
			seller.setLastName(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, seller.getLastName()));
			seller.setFirstNameEname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, seller.getFirstNameEname()));
			seller.setLastNameEname(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, seller.getLastNameEname()));
			seller.setPhoneNumber(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, seller.getPhoneNumber()));
			
			
			String[] arrPhoneNumber = seller.getPhoneNumber().split("-");
			if(arrPhoneNumber.length == 4) {
				seller.setPhoneNumber01(arrPhoneNumber[0]);
				seller.setPhoneNumber02(arrPhoneNumber[1]);
				seller.setPhoneNumber03(arrPhoneNumber[2]);
				seller.setPhoneNumber04(arrPhoneNumber[3]);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return seller;
	}
	
	/**
	 * 설정 > 계정관리 > 기본정보상세
	 * @param SettingShopData
	 * @param HttpSession
	 * @return List<SettingShopData>
	 */
	public List<SettingShopData> selectShop(SettingShopData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		return settingMapper.selectShop(setting);
	}
	
	/**
	 * 설정 > 계정관리 > 기본정보업데이트
	 * @param SettingData
	 * @param HttpSession
	 * @return int
	 */
	public int updateSeller(SettingData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		String phoneNumber = setting.getPhoneNumber01() + "-" + setting.getPhoneNumber02() + "-" + setting.getPhoneNumber03() + "-" + setting.getPhoneNumber04();
		
		// 개인 정보 암호화 처리 
		try {
			setting.setFirstName(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, setting.getFirstName()));
			setting.setLastName(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, setting.getLastName()));
			setting.setFirstNameEname(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, setting.getFirstNameEname()));
			setting.setLastNameEname(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, setting.getLastNameEname()));
			setting.setPhoneNumber(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, phoneNumber));
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return settingMapper.updateSeller(setting);
	}
	
	/**
	 * 설정 > 계정관리 > 판매 쇼핑몰 연동 수정
	 * @param SettingShopData
	 * @param HttpSession
	 * @return int
	 */
	public int updateUseShop(SettingShopData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		return settingMapper.updateUseShop(setting);
	}
	
	/**
	 * 설정 > 계정관리 > 판매 쇼핑몰 삭제
	 * @param SettingShopData
	 * @param HttpSession
	 * @return int
	 */
	public int deleteMultiShop(SettingShopData setting, HttpSession sess) {	
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		String[] delIdx = setting.getCkBox().split(",");
		int result = 0;
		
		for (String idx : delIdx){
			if(!idx.equals("")) {
				int shopIdx = Integer.parseInt(idx);
				setting.setShopIdx(shopIdx);
				
				settingMapper.deleteShop(setting);
				result ++;
			}
		}
		
		return result;
	}
	
	/**
	 * 설정 > 배송관리 > 출고지 리스트
	 * @param SettingSenderData
	 * @param HttpSession
	 * @return List<SettingSenderData>
	 */
	public List<SettingSenderData> selectSender(SettingSenderData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		List<SettingSenderData> list = settingMapper.selectSender(setting);
		List<SettingSenderData> decList = new ArrayList<SettingSenderData>();
		for(SettingSenderData item : list){
			try {
				item.setName(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, item.getName()));
			} catch (Exception e) {
				e.getStackTrace();
			}
			
			try {
				item.setPhoneNumber(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, item.getPhoneNumber()));
			} catch (Exception e) {
				e.getStackTrace();
			}
			
			decList.add(item);
		}
		
		return decList;
	}
	
	/**
	 * 설정 > 배송관리 > 출고지 상세보기
	 * @param SettingSenderData
	 * @param HttpSession
	 * @return SettingSenderData
	 */
	public SettingSenderData selectSenderDetail(SettingSenderData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		SettingSenderData sender = settingMapper.selectSenderDetail(setting);
		try {
			sender.setName(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sender.getName()));
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		try {
			sender.setPhoneNumber(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, sender.getPhoneNumber()));
		} catch (Exception e) {
			e.getStackTrace();
		}

		String[] arrPhoneNumber = sender.getPhoneNumber().split("-");
		if(arrPhoneNumber.length == 4) {
			sender.setPhoneNumber01(arrPhoneNumber[0]);
			sender.setPhoneNumber02(arrPhoneNumber[1]);
			sender.setPhoneNumber03(arrPhoneNumber[2]);
			sender.setPhoneNumber04(arrPhoneNumber[3]);
		}
		
		return sender;
	}

	/**
	 * 설정 > 배송관리 > 출고지 등록
	 * @param SettingSenderData
	 * @param HttpSession
	 * @return int
	 */
	public int insertSender(SettingSenderData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		// 기존 디폴트 데이터 초기화
		if("Y".equals(setting.getUseDefault())) {
			LOGGER.debug(UtilFunc.getStackTrace(setting));
			settingMapper.updateResetSender(setting);
		}
		
		String phoneNumber;
		// 개인 정보 암호화 처리 
		if("".equals(setting.getPhoneNumber01())) {
			 phoneNumber = setting.getPhoneNumber02() + "-" + setting.getPhoneNumber03() + "-" + setting.getPhoneNumber04(); 
		}
		else {
			 phoneNumber = setting.getPhoneNumber01() + "-" + setting.getPhoneNumber02() + "-" + setting.getPhoneNumber03() + "-" + setting.getPhoneNumber04(); 
		}
		try {
			setting.setName(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, setting.getName()));
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		try {
			setting.setPhoneNumber(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, phoneNumber));
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		
		setting.setCombineYn("Y");
		
		return settingMapper.insertSender(setting);
	}
	
	/**
	 * 설정 > 배송관리 > 출고지 수정
	 * @param SettingSenderData
	 * @param HttpSession
	 * @return int
	 */
	public int updateSender(SettingSenderData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		// 기존 디폴트 데이터 초기화
		if("Y".equals(setting.getUseDefault())) {
			LOGGER.debug(UtilFunc.getStackTrace(setting));
			settingMapper.updateResetSender(setting);
		}
		
		String phoneNumber;
		// 개인 정보 암호화 처리 
		if("".equals(setting.getPhoneNumber01())) {
			 phoneNumber = setting.getPhoneNumber02() + "-" + setting.getPhoneNumber03() + "-" + setting.getPhoneNumber04(); 
		}
		else {
			 phoneNumber = setting.getPhoneNumber01() + "-" + setting.getPhoneNumber02() + "-" + setting.getPhoneNumber03() + "-" + setting.getPhoneNumber04(); 
		}
		
		try {
			setting.setName(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, setting.getName()));
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		try {
			setting.setPhoneNumber(util.getAESEncrypt(SpConstants.ENCRYPT_KEY, phoneNumber));
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		
		
		setting.setCombineYn("Y");
				
		LOGGER.debug(UtilFunc.getStackTrace(setting));
		return settingMapper.updateSender(setting);
	}
	
	/**
	 * 설정 > 배송관리 > 출고지 Default 초기화
	 * @param SettingSenderData
	 * @param HttpSession
	 * @return int
	 */
	public int updateResetSender(SettingSenderData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
				
		LOGGER.debug(UtilFunc.getStackTrace(setting));
		return settingMapper.updateResetSender(setting);
	}
	
	/**
	 * 설정 > 배송관리 > 출고지 Default 수정
	 * @param SettingSenderData
	 * @param HttpSession
	 * @return int
	 */
	public int updateDefaultSender(SettingSenderData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		int result = 0;

		LOGGER.debug(UtilFunc.getStackTrace(setting));
		result = settingMapper.updateResetSender(setting);
		result += settingMapper.updateDefaultSender(setting);
		
		return result;
	}
	
	/**
	 * 설정 > 배송관리 > 출고지 삭제
	 * @param SettingSenderData
	 * @param HttpSession
	 * @return int
	 */
	public int deleteSender(SettingSenderData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		return settingMapper.deleteSender(setting);
	}
	
	
	/**
	 * 설정 > 배송관리 > 박스 리스트
	 * @param SettingBoxData
	 * @param HttpSession
	 * @return List<SettingBoxData>
	 */
	public List<SettingBoxData> selectBox(SettingBoxData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		String local = sd.getLocale();
		setting.setEmail(email);
		setting.setUserLang(local);
		
		return settingMapper.selectBox(setting);
	}
	
	/**
	 * 설정 > 배송관리 > 박스 박스상세보기
	 * @param SettingBoxData
	 * @param HttpSession
	 * @return SettingBoxData
	 */
	public SettingBoxData selectBoxDetail(SettingBoxData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		return settingMapper.selectBoxDetail(setting);
	}
	
	
	
	/**
	 * 설정 > 배송관리 > 박스 등록
	 * @param SettingBoxData
	 * @param HttpSession
	 * @return int
	 */
	public int insertBox(SettingBoxData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		setting.setCombineYn("Y");
		
		// 기존 디폴트 데이터 초기화
		if("Y".equals(setting.getUseDefault())) {
			settingMapper.updateResetBox(setting);
		}
		
		return settingMapper.insertBox(setting);
	}
	
	/**
	 * 설정 > 배송관리 > 박스 수정
	 * @param SettingBoxData
	 * @param HttpSession
	 * @return int
	 */
	public int updateBox(SettingBoxData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
//		String[] editIdx = setting.getCkBox().split(",");
//		String[] boxTitle = setting.getBoxTitle().split(",");
//		String[] boxWeight = setting.getBoxWeight().split(",");
		
		setting.setBoxIdx(Integer.parseInt(setting.getCkBox())); 
		
		
		int result = 0;
		
		LOGGER.debug("############# setting : " + setting.toString());
		
		// 기존 디폴트 데이터 초기화
		if("Y".equals(setting.getUseDefault())) {
			settingMapper.updateResetBox(setting);
		}

		return settingMapper.updateBox(setting);
	}
	
	/**
	 * 설정 > 배송관리 > 박스 default 초기화
	 * @param SettingBoxData
	 * @param HttpSession
	 * @return int
	 */
	public int updateResetBox(SettingBoxData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		return settingMapper.updateResetBox(setting);
	}
	
	/**
	 * 설정 > 배송관리 > 박스 default 수정
	 * @param SettingBoxData
	 * @param HttpSession
	 * @return int
	 */
	public int updateDefaultBox(SettingBoxData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		return settingMapper.updateDefaultBox(setting);
	}
	
	/**
	 * 설정 > 배송관리 > 박스 삭제
	 * @param SettingBoxData
	 * @param HttpSession
	 * @return int
	 */
	public int deleteBox(SettingBoxData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		String[] delIdx = setting.getCkBox().split(",");
		int result = 0;
		
		for(String idx : delIdx) {
			if(!idx.equals("")) {
				int boxIdx = Integer.parseInt(idx);
				setting.setBoxIdx(boxIdx);
				
				settingMapper.deleteBox(setting);
				result++;
			}
		}
		
		return result;
	}
	
	
	/**
	 * 설정 > 배송관리 > 관세 리스트 카운트
	 * @param SettingSkuData
	 * @param HttpSession
	 * @return int
	 */
	public int selectSkuCount(SettingSkuData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		return settingMapper.selectSkuCount(setting);
	}
	
	private List<SettingSkuData> apiData2settingData(JsonNode product, RestData restData) {
		
		List<SettingSkuData> skuList = new ArrayList<SettingSkuData>();
		SettingSkuData master = new SettingSkuData();
		
		master.setItemCode(product.findValue("id").asText());					//제품코드
		master.setItemName(product.findValue("title").asText());				//제품이름
		master.setVendor(product.findValue("vendor").asText());					//제조사
		master.setProductType(product.findValue("product_type").asText());		//제품타입, 
		master.setShopIdx(restData.getShopData().getShopIdx());					//ShopIdx
		JsonNode variants = product.findValue("variants");						//제품세부항목
		
		if (variants.isArray()) {
			//for (JsonNode variant : variants) {
			JsonNode variant=variants.get(0);
			SettingSkuData setting = new SettingSkuData();
			BeanUtils.copyProperties(master, setting);				
			
			String inventoryId = variant.findValue("inventory_item_id").asText();
			setting.setLineitemId(inventoryId);
			setting.setVariantId(variant.findValue("id").asText());
			setting.setItemPrice(variant.findValue("price").asInt());
			setting.setItemWeight(variant.findValue("grams").asInt());
			setting.setItemSku(variant.findValue("sku").asText());
			setting.setWeightUnit("g");
			setting.setItemOption(variant.findValue("option1").asText());
			//// HScode 조회 //// 
			//String url = String.format(this.inventoryUrl, restData.getShopData().getDomain(), inventoryId);
			//JsonNode getHScode = restService.getRestTemplate(restData.getHttpHeaders(), url);    		
			//String hscode = UtilFunc.findPath(getHScode, "harmonized_system_code");
			setting.setHscode("");
			//// Price Currency 조회////
			String currency = variant.findValue("presentment_prices").findValue("price").findValue("currency_code").asText();
			setting.setPriceCurrency(currency);	
			setting.setItemQty(1);
			setting.setItemOrigin("KR");
			
			LOGGER.debug("SettingSkuData: "+setting);
			
			skuList.add(setting);
		}	
			
		return skuList;	
	}


		
/**
 * 설정 > 배송관리 > 관세 리스트 > 제품가져오기
 * @param SettingSkuData
 * @param HttpSession
 * @return Map<String, Object>
 */
public void getAllProduct(SettingSkuData setting, HttpSession sess) {
//public void getAllProduct(SettingSkuData setting, ShopData sd) {	
	ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
         
	String domain = null;
	String url = "";
	String direction ="";
	HttpHeaders httpHeaders = null;
	//urlPattern = Pattern.compile("^<(.*)>; rel=\"(\\w+)\"");
	Matcher matcher = urlPattern.matcher("");
	
	RestData restData = null;	
	if ( domain == null ) {
		restData = restService.getRestDataFromShopIdxIncludePrice(Integer.toString(sd.getShopIdx()));
		domain = restData.getShopData().getDomain();
		httpHeaders = restData.getHttpHeaders();
	
	}
	url = String.format(this.getproductUrl, domain);
	
	while(direction.equals("next") || direction.equals("")){
		JsonNodeHead node = restService.getRestTemplateSku(httpHeaders, url);
		JsonNode products = node.getJsonNode().findValue("products");
	
		if (!node.getLink().equals("") && matcher.reset(node.getLink()).find() ) {
			url= matcher.group(1);
			direction=matcher.group(2);
		}
		
		List<SettingSkuData> list = new ArrayList<SettingSkuData>();
		if (products.isArray()) {
			for (JsonNode product : products) {
				
				list = apiData2settingData(product,restData);
				for(SettingSkuData skudata: list) {
					int result = settingMapper.insertSku(skudata);
				}
			}
		}
		if(!direction.equals("next"))
			direction= "end";
	}

	
}

	
	/**
	 * 설정 > 배송관리 > 관세 리스트
	 * @param SettingSkuData
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectSku(SettingSkuData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		String local = sd.getLocale();
		setting.setEmail(email);
		setting.setUserLang(local);
		setting.setShopIdx(sd.getShopIdx());
	
		int dataCount = settingMapper.selectSkuCount(setting); // 전체 건수 조회
		int currentPage = setting.getCurrentPage(); //현제 페이지
		int pageSize = setting.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		//LOGGER.debug("dataCount : " + dataCount);
		//LOGGER.debug("currentPage : " + currentPage);
		//LOGGER.debug("pageSize : " + pageSize);
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		List<SettingSkuData> list = new ArrayList<SettingSkuData>(); //데이터 리스트 변수 선언 
				
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			
			setting.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			setting.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			
			list = settingMapper.selectSku(setting);
		}
		
		// 박스 정보 가져오기
		List<SettingBoxData> boxList = new ArrayList<SettingBoxData>();
		SettingBoxData settingBox = new SettingBoxData();
		settingBox.setEmail(email);
		boxList= settingMapper.selectBox(settingBox);
		
		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("paging", paging);
		map.put("boxList", boxList);
		return map;
	}	
	
	// 상품 타입 가져오기
	public Map<String, Object> selectProduct(SettingSkuData setting, HttpSession sess) {	
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		String local = sd.getLocale();
		setting.setEmail(email);
		setting.setUserLang(local);
		
		List<SettingSkuData> typelist = settingMapper.selectSkuProductType(setting);
		List<SettingSkuData> productType = new ArrayList<SettingSkuData>();
		for(SettingSkuData item : typelist){
			if(item.getProductType()=="" || item.getProductType()==null|| item.getProductType().length()==0 )
				continue;
			productType.add(item);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("productType", productType);
		
		return map;
	}	
	
	/**
	 * 설정 > 배송관리 > 관세 상세보기
	 * @param SettingSkuData
	 * @param HttpSession
	 * @return SettingSkuData
	 */
	public SettingSkuData selectSkuDetail(SettingSkuData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		String local = sd.getLocale();
		setting.setEmail(email);
		setting.setUserLang(local);
		
		return settingMapper.selectSkuDetail(setting);
	}

	/**
	 * 설정 > 배송관리 > 관세 등록
	 * @param SettingSkuData
	 * @param HttpSession
	 * @return int
	 */
	public int insertSku(SettingSkuData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
				
		setting.setCombineYn("Y");
		
		return settingMapper.insertSku(setting);
	}
	
	/**
	 * 설정 > 배송관리 > 관세 수정
	 * @param SettingSkuData
	 * @param HttpSession
	 * @return int
	 */
	public int updateSku(SettingSkuData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);

		
		return settingMapper.updateSku(setting);
	}
	
	public int updateSkuBox(SettingSkuData setting, HttpSession sess) {
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		int shopIdx = sd.getShopIdx();
		setting.setShopIdx(shopIdx);
		return settingMapper.updateSkuBox(setting);
	}
	
	/**
	 * 설정 > 배송관리 > 관세 다중 삭제
	 * @param SettingSkuData
	 * @param HttpSession
	 * @return int
	 */  
	public int deleteMultiSku(SettingSkuData setting, HttpSession sess) {
		// 세션 이메일 받아 오기
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		setting.setEmail(email);
		
		String[] delIdx = setting.getCkBox().split(",");
		int result = 0;
		
		for (String idx : delIdx){
			if(!idx.equals("")) {
				int skuIdx = Integer.parseInt(idx);
				setting.setSkuIdx(skuIdx);
				
				settingMapper.deleteSku(setting);
				result ++;
			}
		}
		
		return result;
	}
	
	
	/**
	 * 주문 > 팝업 > 해외특송 선택
	 * @param getShop 
	 * @param TrackingData
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectOrderCustomsOrder(HttpSession sess, boolean getShop){
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = "";
		String locale = "";
		
		if(sd != null) {
			email = sd.getEmail();
			locale = sd.getLocale();
		}
		
		List<OrderCourierData> shopList;
		
		if ( getShop == true ) {
			shopList = selectHomeDefaultCourier(email, 0);
		} else {
			shopList = new ArrayList<>();
		}
//		//// FAKE : 1
//		OrderCourierData ocd = new OrderCourierData();
//		ocd.setShopIdx(95);
//		ocd.setShopName("kshiptest");
//		shopList.add(ocd);
		
		// 2. domestic company list
		CourierCompanyData inputCompany = new CourierCompanyData();
		inputCompany.setCourierDomesticYn("Y");
		List<OrderCourierData> companyList = settingMapper.selectCourierCompanyOrder(inputCompany);
		
		
		// 3. company list
		OrderCourierData courier = new OrderCourierData();
	    courier.setEmail(email);
	    courier.setLocale(locale);
	    courier.setNationCode("KR");
		courier.setWeight(1);
		String nowDate = util.getDateElement("today");
	    courier.setNowDate(nowDate);
		
		List<OrderCourierData> boxList = orderMapper.selectDomesticCourierList(courier); //특송업체 리스트
//		List<OrderCourierData> tempBoxList = orderMapper.selectDomesticCourierList(courier); //특송업체 리스트
//		List<OrderCourierData> boxList = new ArrayList<>();
//		
//		
//		for(  OrderCourierData company : tempBoxList ) {
//			boxList.add(company);
//		}
//		for(  OrderCourierData company : tempBoxList ) {
//			OrderCourierData clone = new OrderCourierData();
//			BeanUtils.copyProperties(company, clone);
//			clone.setComCode("B010030");
//			clone.setCode("P_HOME");
//			clone.setPrice(clone.getPrice() + 10000);
//			clone.setCourierId("B140010");
//			boxList.add(clone);
//		}

		
//		if ( getShop ) {
//			shopList.stream().forEach( d -> UtilFunc.printValue(d) );
//		}
//		companyList.stream().forEach( d -> UtilFunc.printValue(d) );
//		boxList.stream().forEach( d ->  UtilFunc.printValue(d) );
		
		// return setting
		Map<String, Object> map = new HashMap<String, Object>();
		if ( getShop ) {
			map.put("shopList", shopList);
		}
		map.put("companyList", companyList);
		map.put("boxList", boxList);
		
		return map;
	}
	
	
	/**
	 * 주문 > 팝업 > 해외특송 선택
	 * @param getShop 
	 * @param TrackingData
	 * @param HttpSession
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectOrderCustoms(HttpSession sess, boolean getShop){
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = "";
		String locale = "";
		
		if(sd != null) {
			email = sd.getEmail();
			locale = sd.getLocale();
		}
		
		List<OrderCourierData> shopList;
		
		if ( getShop == true ) {
			shopList = selectHomeDefaultCourier(email, 0);
		} else {
			shopList = new ArrayList<>();
		}
//		//// FAKE : 1
//		OrderCourierData ocd = new OrderCourierData();
//		ocd.setShopIdx(95);
//		ocd.setShopName("kshiptest");
//		shopList.add(ocd);
		
		// 2. domestic company list
		CourierCompanyData inputCompany = new CourierCompanyData();
		inputCompany.setCourierDomesticYn("Y");
		List<CourierCompanyData> companyList = settingMapper.selectCourierCompany(inputCompany);
		
		
		// 3. company list
		OrderCourierData courier = new OrderCourierData();
		courier.setEmail(email);
		courier.setLocale(locale);
		courier.setNationCode("KR");
		courier.setWeight(1);
		String nowDate = util.getDateElement("today");
		courier.setNowDate(nowDate);
		
		List<OrderCourierData> boxList = orderMapper.selectDomesticCourierList(courier); //특송업체 리스트
//		List<OrderCourierData> tempBoxList = orderMapper.selectDomesticCourierList(courier); //특송업체 리스트
//		List<OrderCourierData> boxList = new ArrayList<>();
//		
//		
//		for(  OrderCourierData company : tempBoxList ) {
//			boxList.add(company);
//		}
//		for(  OrderCourierData company : tempBoxList ) {
//			OrderCourierData clone = new OrderCourierData();
//			BeanUtils.copyProperties(company, clone);
//			clone.setComCode("B010030");
//			clone.setCode("P_HOME");
//			clone.setPrice(clone.getPrice() + 10000);
//			clone.setCourierId("B140010");
//			boxList.add(clone);
//		}
		
		
//		if ( getShop ) {
//			shopList.stream().forEach( d -> UtilFunc.printValue(d) );
//		}
//		companyList.stream().forEach( d -> UtilFunc.printValue(d) );
//		boxList.stream().forEach( d ->  UtilFunc.printValue(d) );
		
		// return setting
		Map<String, Object> map = new HashMap<String, Object>();
		if ( getShop ) {
			map.put("shopList", shopList);
		}
		map.put("companyList", companyList);
		map.put("boxList", boxList);
		
		return map;
	}

	public List<OrderCourierData> selectHomeDefaultCourier(String email, int shopIdx) {
		// 1. shop - courier
		OrderCourierData input = new OrderCourierData();
		input.setEmail(email);
		input.setShopIdx(shopIdx);
		input.setNationCode("KR");
		List<OrderCourierData> shopList = settingMapper.selectShopCourierFromEmailOuter(input);
		return shopList;
	}
	

	public int udpateCourier(List<OrderCourierData> list, HttpSession sess) {

		int count = 0;
		
		for( OrderCourierData ocd : list ) {
			ocd.setNationCode("KR");
			
			// https://mariadb.com/kb/en/insert-on-duplicate-key-update/#:~:text=INSERT%20...,API's%20CLIENT_FOUND_ROWS%20flag%20is%20set.
			// INSERT ... ON DUPLICATE KEY UPDATE  는 insert : 1, update : 2 를 return 한다.
			int updated = settingMapper.insertShopCourier(ocd);
			if ( updated > 0 ) {
				count++;
			}
		}


//		OrderCourierData data = settingMapper.selectShopCourier(input);
//		if (data == null) {
//			count = settingMapper.insertShopCourier(input);
//		} else {
//			input.setIdx(data.getIdx());
//			count = settingMapper.updateShopCourier(input);
//		}
		
		return count;

	}

	
	
}
