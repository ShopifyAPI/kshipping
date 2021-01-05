package com.shopify.payment.popup;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.common.RestData;
import com.shopify.common.RestService;
import com.shopify.common.util.UtilFunc;
import com.shopify.shop.ShopData;

@Service
@Transactional
public class PaymentPopupRestService {
	private Logger LOGGER = LoggerFactory.getLogger(PaymentPopupRestService.class);
	
	@Value("${Fulfillment.Get.Event.Create.Step1.full}")  private String getFulfillmentCreateStep1Url;
	@Value("${Fulfillment.Get.Event.Create.Step2.full}")  private String getFulfillmentCreateStep2Url;
	@Value("${Fulfillment.Get.Event.Create.Step3.full}")  private String getFulfillmentCreateStep3Url;
	@Value("${Fulfillment.Post.Event.Create.Step4.full}") private String getFulfillmentCreateStep4Url;
	
	@Value("${pantos.tracking.url}")	 private String PANTOS_TRACKING_URL;
//	@Value("${lotte.tracking.url}") 	private String LOTTE_TRACKING_URL;
	
	@Autowired	private RestService restService;
	private ObjectMapper objectMapper = new ObjectMapper();
	
    /**
     * shopify api rest service
     * @param trackingMap 
     * @return
     */
	
	public boolean shopifyPostOrderIdFullist(String[] masterCodeList, Map<String, Object> trackingMap) {
		
		boolean flag = false;
		
		List<RestData> restList = restService.getHeadersFromMaster(masterCodeList);
		
		String url = null;
		
OUTER:	for( RestData rd : restList ) {
			
			HttpHeaders httpHeaders = rd.getHttpHeaders();
			ShopData shopData = rd.getShopData();
			
			// 1. orders
			url = String.format(getFulfillmentCreateStep1Url, shopData.getDomain(), shopData.getOrderCode());
			JsonNode node = restService.getRestTemplate(httpHeaders, url);
			if ( node == null ) {
				break OUTER;
			}
		      List<JsonNode> items =  node.findValue("order").findValues("line_items");
		      String variantId="";
		      String itemId="";
		      for( JsonNode item : items ) {
		         if ( item.isArray() ) {
		            for (JsonNode itemNode : item) {
		            	String tempVariantId= itemNode.findValue("variant_id").asText();
		            	if ( variantId.equals("null")) {
		            		continue;
		            	} else {
		            		variantId = tempVariantId;
		            	}
		               itemId = itemNode.findValue("id").asText();
		               System.out.println("itemId : " + itemId);
		               System.out.println("variantId : " + variantId);
		            }
		         } else {
		            itemId = item.findValue("id").asText();
		            variantId= item.findValue("variant_id").asText();
		         }
		      }			
		
			// 2. varianats
			url = String.format(getFulfillmentCreateStep2Url, shopData.getDomain(), variantId);
			node = restService.getRestTemplate(httpHeaders, url);
			if ( node == null ) {
				break OUTER;
			}
			
			String inventoryId = node.findValue("variant").findValue("inventory_item_id").asText();
			
			// 3. inventory
			url = String.format(getFulfillmentCreateStep3Url, shopData.getDomain(), inventoryId);
			node = restService.getRestTemplate(httpHeaders, url);
			if ( node == null ) {
				break OUTER;
			}
			
			List<JsonNode> locations = node.findValues("inventory_levels");
			for( JsonNode location : locations ) {
				String locationId = location.findValue("location_id").asText();
				
				// post 할 body data 를 생성함
				
				if(shopData.getTrackingNo() == null || "".equals(shopData.getTrackingNo())) {
					shopData.setTrackingNo(UtilFunc.getRandomString(10));
				}
				
				String input = composeFullfillment(itemId, locationId, shopData.getTrackingNo() , shopData.getCourier(), shopData.getMasterCode(), trackingMap);
				
				// 4. inventory
				url = String.format(getFulfillmentCreateStep4Url, shopData.getDomain(), shopData.getOrderCode());
				node = restService.postRestTemplate(httpHeaders, url, input);
				if ( node == null ) {
					break OUTER;
				}
				
				flag = true;
			}
			
		}
		
		return flag;
	}
	
	private String composeFullfillment(String itemId, String locationId, String trackingNumber, String courier, String CoNo, Map<String, Object> trackingMap) {
		
		String json = null;
		
		/*
		 * List<Map<String,String>> mapList = new ArrayList<>(); Map<String,String> m1 =
		 * new LinkedHashMap<>(); m1.put("id", itemId); mapList.add(m1);
		 */
		
		Map<String, Object> payload = new LinkedHashMap<>();
		payload.put("location_id", locationId);
		/* payload.put("tracking_number", trackingNumber); */
//		payload.put("line_items", mapList);
		
		if ( trackingMap != null ) {		
			// 국내택배인 경우, tracking 정보를 map 으로 만들어서 전달해 줌
			payload.putAll(trackingMap);
		} else if("B010030".equals(courier) &&  trackingNumber != null && !"".equals(trackingNumber)) {
			 payload.put("tracking_number", CoNo); 
			payload.put("tracking_urls", Arrays.asList(PANTOS_TRACKING_URL+"="+CoNo));
			payload.put("notify_customer", true);
		} else if("B020020".equals(courier) &&  trackingNumber != null && !"".equals(trackingNumber)) {
			 payload.put("tracking_number", trackingNumber); 
			
		}
		
		
		Map<String, Object> fullfillMap = new HashMap<>();
		fullfillMap.put("fulfillment", payload);

		try {
//			json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(fullfillMap);
			json = this.objectMapper.writeValueAsString(fullfillMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return json;
	}

}