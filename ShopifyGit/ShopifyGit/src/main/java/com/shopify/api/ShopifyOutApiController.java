package com.shopify.api;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.mapper.SettingMapper;
import com.shopify.setting.SettingService;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/shopifyOutApi")
public class ShopifyOutApiController{
	@Autowired private ShopifyOutApiService spApiService;
	
	private Logger LOGGER = LoggerFactory.getLogger(ShopifyOutApiController.class);
	
	/**
     * 캐리어서비스 등록
     * @return ModelAndView(jsonView)
     */
	@PostMapping(value="/apiCarrierServiceSelect", produces=MediaType.APPLICATION_JSON_VALUE)
	public String apiCarrierServiceSelect(@RequestHeader Map<String,String> headers, @RequestBody ShopifyOutApiDataCarrier aData) {
	
		String domain = headers.get("X-Shopify-Shop-Domain");
		if ( domain == null ) {
			domain = headers.get("x-shopify-shop-domain");
		}
		
		String reqData = spApiService.apiCarrierServiceSelect(aData, domain);
		return reqData;
	}	
	
	@PostMapping(value="/apiCarrierServiceSelectPrint", produces=MediaType.APPLICATION_JSON_VALUE)
	public String apiCarrierServiceSelectPrint(@RequestHeader Map<String,String> headers, @RequestBody String text) {
		
		ObjectMapper objectMapper =new ObjectMapper();
		try {
			ShopifyOutApiDataCarrier aData = objectMapper.readValue(text, ShopifyOutApiDataCarrier.class);
			System.out.println("--- @RequestHeader headers ---");
			System.out.println(headers);
			System.out.println("--- @RequestBody text ---");
			System.out.println(text);
			System.out.println("--- @RequestBody aData ---");
			System.out.println(aData);
			System.out.println("----------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "{ \"a\":\"ai\"} ";
	}
}