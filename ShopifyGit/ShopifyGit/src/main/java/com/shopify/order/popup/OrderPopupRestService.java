package com.shopify.order.popup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
import com.shopify.common.RestData;
import com.shopify.common.RestService;
import com.shopify.common.util.UtilFunc;
import com.shopify.mapper.OrderMapper;
import com.shopify.mapper.SettingMapper;
import com.shopify.setting.ShippingCompanyData;

@Service
@Transactional
public class OrderPopupRestService {
	private Logger LOGGER = LoggerFactory.getLogger(OrderPopupRestService.class);
	
	@Value("${Order.Variants}") private String variantsUrl;
	@Value("${Order.Inventory}") private String inventoryUrl;
	@Value("${Order.ProductLinks}") private String productUrl;
	
	@Autowired	private RestService restService;
	@Autowired private SettingMapper settingMapper;
	@Autowired private OrderMapper orderMapper;

	private List<Pair> pairList = new ArrayList<>();
	
	/**
     * shopify api rest service
     * @return
     */
	
	@PostConstruct
	public void postConstruct() {
		
		List<ShippingCompanyData> list = settingMapper.selectVolumeticCourierCompany();
		Map<Integer, List<ShippingCompanyData>> divisorMap = list.stream().collect(Collectors.groupingBy(ShippingCompanyData::getDivisor));
		
		
		for( Entry<Integer, List<ShippingCompanyData>> e : divisorMap.entrySet() ) {
			int divisor = e.getKey();
			int divisorCount = e.getValue().size();
			this.pairList.add(new Pair(divisor, divisorCount) );
		}
		
		
	}
	
		
	public List<Map<String, Object>> getHscode(List<Map<String, Object>> mapList) {
		// shopify api 호출용 httpheaders 와 domain  가져오는 방법
		// 1. session 에서 가져올 때
//		String domain = UtilFunc.getShopDomain(sess);
//		HttpHeaders httpHeaders = UtilFunc.generateHeader(sess);
		
		String domain = null;
		HttpHeaders httpHeaders = null;
		
    	for( Map<String, Object> map : mapList ) {
    		
    		String hscode = "";
    		
    		// shopify api 호출용 httpheaders 와 domain  가져오는 방법
    		// 2. shop_idx 가 주어졌을 때
    		// hscode 가져오는 data 는 무조건 동일한 shop_idx 에 대해서만 processing 되므로, 
    		// domain 과 httpHeaders 는 단 한번만 가져오도록 한다.
    		if ( domain == null || httpHeaders == null ) {
    			String shopIdx = (String) map.get("shopIdx");
    			RestData restData = restService.getRestDataFromShopIdx(shopIdx);
    			domain = restData.getShopData().getDomain();
    			httpHeaders = restData.getHttpHeaders();
    		}
    		String goodsCode = (String) map.get("goodsCode");
    		// 1. orders
    		String url = String.format(this.variantsUrl, domain, goodsCode);
    		JsonNode node = restService.getRestTemplate(httpHeaders, url);
    		if ( node == null ) {
    			continue;
    		}
    		List<JsonNode> items =  node.findValues("variants");
    		for( JsonNode item : items ) {
    			String inventoryId = item.findValue("inventory_item_id").asText();
    			
    			// 2. orders
    			url = String.format(this.inventoryUrl, domain, inventoryId);
    			node = restService.getRestTemplate(httpHeaders, url);
        		if ( node == null ) {
        			continue;
        		}
        		
        		hscode = UtilFunc.findPath(node, "harmonized_system_code");
        		if ( ! org.apache.commons.lang.StringUtils.isBlank(hscode) ) {
        			break;
        		}
    		}
    		
    		map.put("hscode", hscode);
    	}
    	return mapList;
	}

	public String  getProductHandleFullUrl(String shopIdx, String productId) {
		LOGGER.debug(">>getProductHandleFullUrl---------------------------------------------------");
		String url = "";
		String urlProductLink = ""; 
		String handle = "";              // 상품의 handle -> SEO 명 
		String domain = null;
		HttpHeaders httpHeaders = null;
		if ( domain == null ) {
			RestData restData = restService.getRestDataFromShopIdx(shopIdx);
			domain = restData.getShopData().getDomain();
			LOGGER.debug(">>DOMAIN:" + domain);
			httpHeaders = restData.getHttpHeaders();
		}
		LOGGER.debug("===============[START]=================");
		url = String.format(this.productUrl, domain, productId);
		JsonNode node = restService.getRestTemplate(httpHeaders, url);
		if(node != null)
		{
			List<JsonNode> productitems =  node.findValues("product");
			
			for( JsonNode item : productitems ) {
				handle = item.findValue("handle").asText();
				LOGGER.debug(">>handle:" + handle);
			}
		}
		if(!"".equals(handle))
		urlProductLink =	"https://" + domain + "/products/" + handle;
		LOGGER.debug(">urlProductLink:" + urlProductLink);
		LOGGER.debug("==============[END]==================");
		return urlProductLink;
	}
	
	public List<OrderCourierDataWrapper> selectVolumeticCourierList(String email, String buyerCountryCode, String locale, String weight, String length, String width, String height){
		
		int weightValue = UtilFunc.parseInt(weight);
				
		OrderCourierData courier = new OrderCourierData();
		courier.setEmail(email);
		courier.setLocale(locale == null ? "ko" : locale);
		courier.setNationCode(buyerCountryCode);
		courier.setNowDate(UtilFunc.today());
		
		List<VolumeticPair> volumeticPairList = new ArrayList<>();
		int volumeticWeight = Integer.parseInt(length) * Integer.parseInt(width) * Integer.parseInt(height);
		
		for( Pair pair : this.pairList ) {
			VolumeticPair volumeticPair = new VolumeticPair(pair, volumeticWeight);   // 나눗셈은 constructor 안에서 발생하므로 먼저 new 를 함
			if ( weightValue < volumeticPair.getWeight() ) {     // 부피질량이 포장질량보다 클 경우에만 부피질량을 계산함
				volumeticPairList.add(volumeticPair);
			}
		}
		
		List<OrderCourierDataWrapper> list = null;
		Set<OrderCourierDataWrapper> set = new HashSet<>();
		
		if ( volumeticPairList.size() > 0 ) {
			// set 는 key 가 이미 존재하면 add 하지 않으므로, volumetic 을 먼저 실행함
			for( VolumeticPair volumeticPair : volumeticPairList ) {
				// case-1 : 부피질량을 계산하는 배송사만 부피질량으로 계산하여 set 에 추가한 후,
				courier.setWeight(volumeticPair.getWeight());
				courier.setDivisor(volumeticPair.getDivisor());
				List<OrderCourierDataWrapper> added = orderMapper.selectWeightCourierListVolumeticMin(courier);
				set.addAll(added);
				if ( added.size() != volumeticPair.getDivisorCount() ) {
					// 주어진 무게를 기준으로  min query 값이 없으면, 최대무게인 부피중량을 가져온다.
					added = orderMapper.selectWeightCourierListVolumeticMax(courier);
					set.addAll(added);  // 빠진 것은 여기서 다시 추가함
				}
			}
		} 
		
		// case-2 : 전체 배송사를 총 무게로 계산하고 ( divisor 가 6, 5, 166 등 여러 case 가 있으므로, 무조건 전체를 계산한다. )
		courier.setWeight(weightValue);
		set.addAll( orderMapper.selectWeightCourierListWeightMin(courier) );
		
		List<OrderCourierDataWrapper> maxList = orderMapper.selectWeightCourierListWeightMax(courier);
		if ( set.size() != maxList.size() ) {
			set.addAll( maxList );   // min 에서 값이 나오지 않을 경우, max 값으로 채움, 이미 있으면 무시됨
		}
		
		// SQL 에서 사용한 field 로 sort 함
		list = new LinkedList<>(set);
		list.sort( Comparator.comparing(OrderCourierDataWrapper::getServiceCode).thenComparing(OrderCourierDataWrapper::getNationCode));
		
//		list.forEach(UtilFunc::printValue);
		return list;
	}

}