package com.shopify.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.common.util.UtilFunc;
import com.shopify.mapper.RestMapper;
import com.shopify.mapper.ShopMapper;
import com.shopify.schedule.CronJobData;
import com.shopify.shop.ShopData;

@Service
@Transactional
public class RestService {
	private Logger LOGGER = LoggerFactory.getLogger(RestService.class);
	
	@Autowired 	private ObjectMapper objectMapper;
	@Autowired	private RestTemplate restTemplate;
	
	@Value("${solugate.rest.debug}") private boolean restDebug;
	
	@Value("${lotteLogis.authorization.key}")
	String lotteAuthorization; // 전송 파라미터(JSON) 중 header JSON String(일부 추가 setting 필요)

	@Autowired	private ShopMapper shopMapper;
	@Autowired	private RestMapper restMapper;
	
    /**
     * shopify api rest service
     * @return
     */
	
	@PostConstruct
	 private void postConstruct() {
		
		 HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
	        /*
	         * 타임아웃 설정
	         */
	        //httpRequestFactory.setConnectTimeout(timeout);
	        //httpRequestFactory.setReadTimeout(timeout);
	        //httpRequestFactory.setConnectionRequestTimeout(connectionRequestTimeout);
	        HttpClient httpClient = HttpClientBuilder.create()
	                                                 .setMaxConnTotal(150)
	                                                 .setMaxConnPerRoute(50)
	                                                 .build();
	        httpRequestFactory.setHttpClient(httpClient);
	        restTemplate.setRequestFactory(httpRequestFactory);
	}
	
	/**
	 *  REST Get 호출
	 *  호출한 transaction 이 exception 이 발생해도, rest call log 은 무조건 commit 해야 하므로 "Propagation.REQUIRES_NEW" 를 사용함
	 * @param httpHeaders
	 * @param url
	 * @return JsonNode : success
	 *          null   : rest call 에서 에러가 발생했을 때.
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public JsonNode getRestTemplate(HttpHeaders httpHeaders, String url) {
		
		HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);
		
		return restExchange(url, HttpMethod.GET, httpEntity);
	}
		
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public JsonNodeHead getRestTemplateSku(HttpHeaders httpHeaders, String url) {
		
		HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);
		
		return restExchangeIncludeHeader(url, HttpMethod.GET, httpEntity);
	}
	
	/**
	 * REST Poste 호출
	 * 호출한 transaction 이 exception 이 발생해도, rest call log 은 무조건 commit 해야 하므로 "Propagation.REQUIRES_NEW" 를 사용함
	 * @param httpHeaders
	 * @param url
	 * @param json : post 할 body data ( json format )
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public JsonNode postRestTemplate(HttpHeaders httpHeaders, String url, String json) {
		
		HttpEntity<String> httpEntity = new HttpEntity<String>(json, httpHeaders);
		
		return  restExchange(url, HttpMethod.POST, httpEntity);
	}

	/**
	 * REST Put 호출
	 * 호출한 transaction 이 exception 이 발생해도, rest call log 은 무조건 commit 해야 하므로 "Propagation.REQUIRES_NEW" 를 사용함
	 * @param httpHeaders
	 * @param url
	 * @param json : post 할 body data ( json format )
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public JsonNode putRestTemplate(HttpHeaders httpHeaders, String url, String json) {
		
		HttpEntity<String> httpEntity = new HttpEntity<String>(json, httpHeaders);
		
		return  restExchange(url, HttpMethod.PUT, httpEntity);
	}

	private JsonNode restExchange(String url, HttpMethod method, HttpEntity<String> httpEntity) {
		JsonNode node = null;

		if (restDebug == true) {
			LOGGER.debug("REST CALL : url     = {} ", url);
			LOGGER.debug("REST CALL : method  = {} ", method.toString());
			LOGGER.debug("REST CALL : headers = {} ", httpEntity.getHeaders());
			LOGGER.debug("REST CALL : body    = {} ", httpEntity.getBody());
		}

		ResponseEntity<String> responseEntityStr = null;
		String message = null;

		try {
			try {

				responseEntityStr = this.restTemplate.exchange(url, method, httpEntity, String.class);
				HttpStatus statusCode = responseEntityStr.getStatusCode();

				if (statusCode.is2xxSuccessful()) {
					String response = responseEntityStr.getBody();
					if (response != null) {
						node = this.objectMapper.readTree(response);
						if (node.isNull()) {
							node = null;
						}
					}
				}
			} catch (Exception rce) {
				message = rce.getMessage();
				rce.printStackTrace();
			}

			if (restDebug == true) {
				LOGGER.debug("REST RESULLT : {} ", responseEntityStr);
			}

		} finally {
			insertRestApiData(url, method, httpEntity, responseEntityStr, message);
		}
		
		return node;
	}

	private JsonNodeHead restExchangeIncludeHeader(String url, HttpMethod method, HttpEntity<String> httpEntity) {
		JsonNodeHead jsonHead = new JsonNodeHead();
		JsonNode node = null;

		ResponseEntity<String> responseEntityStr = null;
		String message = null;
		String link = "";

//		try {
		try {

			responseEntityStr = this.restTemplate.exchange(url, method, httpEntity, String.class);
			HttpStatus statusCode = responseEntityStr.getStatusCode();

			if (statusCode.is2xxSuccessful()) {
				String response = responseEntityStr.getBody();
				HttpHeaders res = responseEntityStr.getHeaders();

				if(res.containsKey(HttpHeaders.LINK) ) {
					List<String> list = res.get(HttpHeaders.LINK);
					int size = list.size();
					if(size>0)
						link = list.get(0);
				}
				
				if (response != null) {
					node = this.objectMapper.readTree(response);
					if (node.isNull()) {
						node = null;
					}
				}
			}
		} catch (Exception rce) {
			message = rce.getMessage();
			rce.printStackTrace();
		}

		if (restDebug == true) {
			LOGGER.debug("REST RESULLT : {} ", responseEntityStr);
		}

//		} finally {
//			
//			insertRestApiData(url, method, httpEntity, responseEntityStr, message);
//		}
		
		jsonHead.setJsonNode(node);
		jsonHead.setLink(link);
		
		return jsonHead;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public <T> JsonNode  postForEntity(HttpHeaders headers, String url, T value, Class<T> valueType) {
		 HttpEntity<T> entity = new HttpEntity<>(value, headers);
		 HttpMethod method =  HttpMethod.POST;
		 

			JsonNode node = null;

			if (restDebug == true) {
				LOGGER.debug("REST CALL : url     = {} ", url);
				LOGGER.debug("REST CALL : method  = {} ", method.toString());
				LOGGER.debug("REST CALL : headers = {} ", entity.getHeaders());
				LOGGER.debug("REST CALL : body    = {} ", entity.getBody());
			}

			ResponseEntity<String> responseEntityStr = null;
			String message = null;

			try {
				try {
//					ResponseEntity<LotteAddressResultData> postForEntity = 

					responseEntityStr = restTemplate.postForEntity(url, entity, String.class);
					//responseEntityStr = this.restTemplate.exchange(url, method, httpEntity, String.class);
					HttpStatus statusCode = responseEntityStr.getStatusCode();

					if (statusCode.is2xxSuccessful()) {
						String response = responseEntityStr.getBody();

						if (response != null) {
							node = this.objectMapper.readTree(response);
							if (node.isNull()) {
								node = null;
							}
						}
					}
				} catch (Exception rce) {
					message = rce.getMessage();
					rce.printStackTrace();
				}

				if (restDebug == true) {
					LOGGER.debug("REST RESULLT : {} ", responseEntityStr);
				}

			} finally {
				insertRestApiData(url, method, entity, responseEntityStr, message);
			}
			
			return node;
	}

//
//	private void insertRestApiData(String url, HttpMethod method, HttpEntity<String> httpEntity, ResponseEntity<String> responseEntityStr, String message) {
//		
//		RestApiData restApiData = new RestApiData();
//		restApiData.setUrl(url);
//		restApiData.setMethod(method.toString());
//		restApiData.setRequestHeaders(httpEntity.getHeaders().toString());
//		restApiData.setRequestBody(httpEntity.getBody());
//		if ( responseEntityStr != null ) {
//			restApiData.setStatus(responseEntityStr.getStatusCode().toString());
//			restApiData.setResponseHeaders(responseEntityStr.getHeaders().toString());
//			restApiData.setResponseBody(responseEntityStr.getBody());
//		}
//		if ( message != null ) {
//			restApiData.setExceptionMessage(message);
//		}
//		
//		int count = restMapper.insertRestApiData(restApiData);
//		LOGGER.debug("RestApiData insert : count = {}", count);
//		
//	}
	
	private <T> void insertRestApiData(String url, HttpMethod method, HttpEntity<T> httpEntity, // Class<T> valueType, 
			ResponseEntity<String> responseEntityStr, String message) {
		
		RestApiData restApiData = new RestApiData();
		restApiData.setUrl(url);
		restApiData.setMethod(method.toString());
		restApiData.setRequestHeaders(httpEntity.getHeaders().toString());
		
		String body = null;
		try {
			body = objectMapper.writeValueAsString(httpEntity.getBody());
		} catch (JsonProcessingException e) {
			body = httpEntity.getBody().toString();
			e.printStackTrace();
		}
		restApiData.setRequestBody(body);
		
		if ( responseEntityStr != null ) {
			restApiData.setStatus(responseEntityStr.getStatusCode().toString());
			restApiData.setResponseHeaders(responseEntityStr.getHeaders().toString());
			restApiData.setResponseBody(responseEntityStr.getBody());
		}
		if ( message != null ) {
			restApiData.setExceptionMessage(message);
		}
		
		int count = restMapper.insertRestApiData(restApiData);
		LOGGER.debug("RestApiData insert : count = {}", count);
		
	}
	
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
//	private void updateRestApiData(RestApiData restApiData, ResponseEntity<String> responseEntityStr) {
//		
//		restApiData.setStatus(responseEntityStr.getStatusCode().toString());
//		restApiData.setResponseHeaders(responseEntityStr.getHeaders().toString());
//		restApiData.setResponseBody(responseEntityStr.getBody());
//			
//		int count = restMapper.updateRestApiData(restApiData);
//		LOGGER.debug("RestApiData update : count = {}", count);
//	}

	public List<RestData> getHeadersFromMaster(String[] masterCodeList) {
		
		List<RestData> list = new ArrayList<>();
//		String[] masterCodeListX = { "200428130601773K", "20041713240156Q3", "200416164620t61l" };
		
		List<ShopData> shopDataList = shopMapper.selectShopFromMaster(masterCodeList);
		
		Map<String, HttpHeaders> headerMap = new HashMap<>();
		
		shopDataList.stream().forEach( sd -> {
			if ( ! headerMap.containsKey(sd.getDomain())) {
				HttpHeaders httpHeaders = composeHeaders(sd);
				headerMap.put(sd.getDomain(), httpHeaders);
			}
			
			RestData restData = new RestData();
			restData.setShopData(sd);
			restData.setHttpHeaders(headerMap.get(sd.getDomain()));

			list.add(restData);
		});
		
		return list;
	}

	private HttpHeaders composeHeaders(ShopData sd) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("X-Shopify-Access-Token", UtilFunc.getAESDecrypt(sd.getAccessToken()));
//			httpHeaders.add("Authorization", accessToken);
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return httpHeaders;
	}

	public HttpHeaders composeLotteHeaders(boolean authInclude) {
		HttpHeaders httpHeaders = new HttpHeaders();
		
		if ( authInclude ) {
			httpHeaders.add("authorization", lotteAuthorization);
		}
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		return httpHeaders;
	}
	
	public RestData getRestDataFromShopIdx(String shopIdx) {
		
		ShopData input = new ShopData();
		input.setShopIdx(Integer.parseInt(shopIdx));
		
		ShopData shopData = shopMapper.selectOneShop(input);
		
		RestData restData = new RestData();
		restData.setShopData(shopData);
		restData.setHttpHeaders(composeHeaders(shopData));

		return restData;
	}
		
	public RestData getRestDataFromShopIdxIncludePrice(String shopIdx) {
		
		ShopData input = new ShopData();
		input.setShopIdx(Integer.parseInt(shopIdx));
		
		ShopData shopData = shopMapper.selectOneShop(input);
		
		RestData restData = new RestData();
		restData.setShopData(shopData);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("X-Shopify-Access-Token", UtilFunc.getAESDecrypt(shopData.getAccessToken()));
//			httpHeaders.add("Authorization", accessToken);
		httpHeaders.add("X-Shopify-Api-Features", "include-presentment-prices");
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		restData.setHttpHeaders(httpHeaders);

		return restData;
	}
	
	public String writeValueAsString(Object object) {
		String result = null;
		
		try {
			result = this.objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			result = "";
		}
		return result;
	}
	
	 public <T> T readValue(String content, TypeReference<T> valueTypeRef) {
		T result = null;
		
		if ( ! StringUtils.isBlank(content) ) {
			try {
				result = this.objectMapper.readValue(content, valueTypeRef);
			} catch (IOException e) {
				e.printStackTrace();
				result = null;
			}
		}
		return result;
	}

	public <T> T treeToValue(JsonNode node, Class<T> valueType) {
		T value = null;
		
		try {
			value = this.objectMapper.treeToValue(node, valueType);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return value;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void insertCronJobData(CronJobData cronData) {
		restMapper.insertCronJobData(cronData);
	}

	

}