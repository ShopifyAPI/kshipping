package com.shopify.admin.seller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.shopify.admin.AdminData;
import com.shopify.admin.price.PriceData;
import com.shopify.api.ShopifyOutApiDataCarrier;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.common.util.UtilFunc;
import com.shopify.mapper.AdminSellerMapper;
import com.shopify.order.popup.OrderCourierData;
import com.shopify.shop.ShopData;

import io.micrometer.core.instrument.util.StringUtils;

/**
* 관리자 > Seller Service
* @author : jwh
* @since  : 2020-01-21
* @desc   : Seller 정보 관리 
*/

@Service
@Transactional
public class AdminSellerService {
	private Logger LOGGER = LoggerFactory.getLogger(AdminSellerService.class);
	
	@Autowired 
	private AdminSellerMapper sellerMapper;
	
	@Autowired	
	private UtilFn util;
	
	/**
	 * 관리자 > seller 관리 > 리스트
	 * @return
	 */
	public Map<String, Object> selectAdminSeller (AdminSellerData setting, HttpSession sess) {
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		//ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		//String local = sd.getLocale();
		//setting.setUserLang(local);
		
		int dataCount = sellerMapper.selectAdminSellerCount(setting); // 전체 건수 조회
		int currentPage = setting.getCurrentPage(); //현제 페이지
		int pageSize = setting.getPageSize(); // 페이지 당 데이터 수
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		
		List<AdminSellerData> decList = new ArrayList<AdminSellerData>(); // 복호화 데이터 리스트
		
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			
			setting.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			setting.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			List<AdminSellerData> list = sellerMapper.selectAdminSeller(setting); //데이터 리스트
			
			for(AdminSellerData item : list){
				item.setFirstName(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, item.getFirstName()));
				item.setLastName(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, item.getLastName()));
				item.setPhoneNumber(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, item.getPhoneNumber()));
				
				decList.add(item);
			}
		}

		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", decList);
		map.put("paging", paging);
		
		return map;
	}
	
	/**
	 * 관리자 > seller 관리 > 리스트
	 * @param fillArray 
	 * @return
	 */
	public Map<String, Object> selectSellerDiscount (PriceData priceData) {
		
		// 1. courier list
		List<ShopifyOutApiDataCarrier> courierList = sellerMapper.selectCourierListForeign(priceData);
		int colSize = courierList.size();
		Map<String, Integer> courierMap = toCourierMap(courierList);
		
		
		// 2. seller list
		List<ShopData> sellerList = sellerMapper.selectDiscountSellerList(priceData);
		Map<String, SellerDiscount> sellerMap = new HashMap<>();
		for( ShopData shop : sellerList ) {
			
			String email = shop.getEmail();
			
			if ( sellerMap.containsKey(email) ) {
				SellerDiscount sellerDiscount = sellerMap.get(email);
				sellerDiscount.addDomain(shop.getDomain());
				sellerDiscount.addShopName(shop.getShopName());
			} else {
				SellerDiscount sellerDiscount = new SellerDiscount(shop, colSize);
				sellerMap.put(email, sellerDiscount);
			}
		}
		
		// 3. discount list
		List<OrderCourierData> discountList = sellerMapper.selectDiscountList(priceData);
		
		for( OrderCourierData discount : discountList ) {
			String email = discount.getEmail();
			SellerDiscount sellerDiscount;
			if ( sellerMap.containsKey(email)) {
				sellerDiscount = sellerMap.get(email);
			} else {
				ShopData shopData = new ShopData();
				shopData.setEmail(email);
				shopData.setShopName("");
				shopData.setDomain("");
				sellerDiscount = new SellerDiscount(shopData, colSize);
			}
			
			Integer idx = courierMap.get(discount.getId() + discount.getCode());
			if ( idx == null ) {
				LOGGER.info("INFO : no data for company = {}, carrier = {}", discount.getId(), discount.getCode());
				continue;
			}
			
			sellerDiscount.setValue(idx, discount.getDiscount());
		}
		
		List<SellerDiscount> sellerDiscountList = sellerMap.values().stream().collect(Collectors.toList());
		
		
		// 4. sellerDiscontList 가 없으면 빈 칸을 채운다
		if ( sellerDiscountList.size() == 0 ) {
			ShopData shopData = new ShopData();
			shopData.setEmail("");
			shopData.setShopName("");
			shopData.setDomain("");
			
			sellerDiscountList.add(new SellerDiscount(shopData, colSize));
		}
		
		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("courierList", courierList);
		map.put("sellerDiscountList", sellerDiscountList);
		
		return map;
	}
	
	private Map<String, Integer> toCourierMap(List<ShopifyOutApiDataCarrier> courierList) {
		
		Map<String, Long> countMap = courierList.stream().collect(Collectors.groupingBy(ShopifyOutApiDataCarrier::getId, Collectors.counting()) );
		Map<String, Integer> courierMap = new HashMap<>();   
		
		int colIdx = 0;
		String hold = "x";
		for(  ShopifyOutApiDataCarrier courier : courierList ) {
			String id = courier.getId();
			courierMap.put( id + courier.getCode(), colIdx++);
			
			if ( ! hold.equals( id )) {
				courier.setCarrierCnt((countMap.get(id).intValue() ));
				hold = id;
			}
		}
		
		return courierMap;
	}

	/**
	 * 관리자 > seller 관리 > 리스트
	 * @param seller 
	 * @return
	 */
	public Map<String, Object> selectSellerDiscountFullList (PriceData priceData, AdminSellerData seller) {
		
		// 1. courier list
		List<ShopifyOutApiDataCarrier> courierList = sellerMapper.selectCourierListForeign(priceData);
		int colSize = courierList.size();
		Map<String, Integer> courierMap = toCourierMap(courierList);
		
		String email = priceData.getEmail();
		
		// 2. discount list
		List<OrderCourierData> discountList = sellerMapper.selectDiscountList(priceData);   // SQL 에서 start 날짜 순서대로 order by 한 후
		
		String startDateSpan = "";
		String endDateSpan = "";
		
		if ( discountList.size() > 0 ) {
			// start 와 end 일자 순서로 sort 함
			List<String> startDateList = discountList.stream().map(OrderCourierData::getStartDate).sorted().collect(Collectors.toList());
			startDateSpan = composeDate(startDateList);
			List<String> endDateList = discountList.stream().map(OrderCourierData::getEndDate).sorted().collect(Collectors.toList());
			endDateSpan = composeDate(endDateList);
		}
		
		ShopData shopData = new ShopData();
		shopData.setEmail(email);
		shopData.setShopName("");
		shopData.setDomain("");
		
		// 3. sellerDiscount record 1개 만들기
		SellerDiscount sellerDiscount = new SellerDiscount(shopData, startDateSpan, endDateSpan, colSize);
		
		for( OrderCourierData discount : discountList ) {
			Integer idx = courierMap.get(discount.getId() + discount.getCode());
			if ( idx == null ) {
				LOGGER.info("INFO : no data for company = {}, carrier = {}", discount.getId(), discount.getCode());
				continue;
			}
			
			sellerDiscount.setValue(idx, discount.getDiscount());
		}
		
		// 4. discountHistory List 만들기
		Map<String, SellerDiscount> historyMap = new HashMap<>();
		SellerDiscount historyData = null;
		
		OrderCourierData input = new OrderCourierData();
		input.setEmail(email);
		// endDate 이 전날인 data 까지 읽어 옴
		String yesterday = UtilFunc.calcDays( UtilFunc.today(), -1);
		input.setNowDate(yesterday);
		
		List<OrderCourierData> fullList = sellerMapper.selectSellerDiscount(input);
		
		for( OrderCourierData discount : fullList ) {
			Integer idx = courierMap.get(discount.getId() + discount.getCode());
			if ( idx == null ) {
				LOGGER.info("INFO : no data for company = {}, carrier = {}", discount.getId(), discount.getCode());
				continue;
			}
			
			// historyMap 관리
			String key = discount.getStartDate() + discount.getEndDate();
			if ( historyMap.containsKey(key)) {
				historyData = historyMap.get(key);
			} else {
				ShopData shop = new ShopData();
				shopData.setEmail("");
				historyData = new  SellerDiscount(shop, discount.getStartDate(),  discount.getEndDate(), colSize);
				historyMap.put(key, historyData);
			}
			historyData.setValue(idx, discount.getDiscount());
		}
		List<SellerDiscount> discountHistory = historyMap.values().stream().sorted(Comparator.comparing(SellerDiscount::getStartDate).thenComparing(SellerDiscount::getEndDate)).collect(Collectors.toList());
		
		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("courierList", courierList);
		map.put("sellerDiscount", sellerDiscount);
		map.put("discountListSize", discountList.size());
		map.put("discountHistory", discountHistory);
		
		return map;
	}
	
	private String composeDate(List<String> list) {
		String first = list.get(0);
		String last = list.get( list.size() - 1);
		
		if ( ! first.equals(last) ) {
			first += " ~ " + last;
		}

		return first;
	}

	/**
	 * 관리자 > seller 관리 > 상세보기 (기본정보)
	 * @return
	 */
	public AdminSellerData selectAdminSellerDetail(AdminSellerData setting, HttpSession sess) {
		
		LOGGER.debug("email : " + setting.getEmail());
		
		AdminSellerData seller = sellerMapper.selectAdminSellerDetail(setting);
		
		try {
			seller.setFirstName(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, seller.getFirstName()));
			seller.setLastName(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, seller.getLastName()));
			seller.setPhoneNumber(util.getAESDecrypt(SpConstants.ENCRYPT_KEY, seller.getPhoneNumber()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return seller;
	}
	
	/**
	 * 관리자 > seller 관리 > 상세보기 (샵리스트)
	 * @return
	 */
	public List<AdminShopData> selectAdminShop(AdminShopData setting, HttpSession sess) {
		return sellerMapper.selectAdminShopList(setting);
	}
	
	/**
	 * 관리자 > seller 관리 > 등급 수정 
	 * @return
	 */
	public String updateSellerDiscount(SellerDiscountDateData setting) {
		String result = null;
		
		LOGGER.debug("setting : " + setting.toString());
		
		// 1. 결제방법 / 셀러활동상태에 변경이 있을 경우에만 수정한다.
		if ( ! StringUtils.isBlank( setting.getShopStatus() ) || ! StringUtils.isBlank( setting.getPaymentMethod() ) ) {
			sellerMapper.updateSellerStatus(setting);
		}
		
		
		String startDate = setting.getStartDate();
		
		if (  UtilFunc.isBefore(startDate) ) {
			result = "적용시작일자를 과거일자로 입력할 수 없습니다 . 적용시작일자=" + startDate;
			LOGGER.debug(result + "  " + startDate);
			return result;
		}
		
		
		// endDate 이 전날인 data 까지 읽어 옴
		String yesterday = UtilFunc.calcDays( UtilFunc.today(), -1);
		
		// 2. seller_discount table update
		for ( OrderCourierData data : setting.getDiscountList() ) {
			if ( "Y".equals( data.getUseYn() ) ) {
				data.setEmail(setting.getEmail());
				data.setNowDate(yesterday);
				data.setStartDate(setting.getStartDate());
				
				String update = updateSellerDiscountRecord(data);
				if ( update != null ) {
					result += "\n" + update;
				}
			}
		}
		
		return result;
	}
	
	private String updateSellerDiscountRecord(OrderCourierData data) {
		String result = null;
		
		List<OrderCourierData> discountList = sellerMapper.selectSellerDiscount(data);
		discountList.forEach( d -> 	System.out.println( printData(d) ) );
		
		SellerDiscountManager manager = new SellerDiscountManager(discountList);
		
		result =  manager.processData(data);
		if ( result != null ) {
			return result;
		} 

		System.out.println("after");
		
		for( OrderCourierData record : manager.getChangedRecords() ) {
			
			if ( record.getIdx() == 0 ) {
				sellerMapper.insertSellerDiscount(record);
			} else {
				sellerMapper.updateSellerDiscount(record);
			}
			System.out.println( printData(record) );
		}

		return result;
	}

	public static String printData(OrderCourierData record) {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append(record.getIdx());
		buffer.append("\t");
		
		buffer.append(record.getStartDate());
		buffer.append("\t");
		
		buffer.append(record.getEndDate());
		buffer.append("\t");
		
		buffer.append(record.getEmail());
		buffer.append("\t");
		
		buffer.append(record.getId());
		buffer.append("\t");
		
		buffer.append(record.getCode());
		buffer.append("\t");
		
		buffer.append(record.getDiscount());
		
		
		return buffer.toString();
	}

	/**
	 * 관리자 > seller 관리 > 판매 쇼핑몰 결제모드 수정
	 * @param AdminShopData
	 * @param HttpSession
	 * @return int
	 */
	public int updateShopBilling(AdminShopData setting, HttpSession sess) {
		return sellerMapper.updateShopBilling(setting);
	}	
	
	public int updateActive(AdminShopData setting, HttpSession sess) {
		return sellerMapper.updateActive(setting);
	}	
	/**
	 * 관리자 > seller 관리 > 등급 수정 로그 보기 
	 * @return
	 */
	public List<AdminSellerData> selectSellerRankLog(AdminSellerData setting) {
		return sellerMapper.selectSellerRankLog(setting);
	}
}
