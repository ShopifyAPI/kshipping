package com.shopify.api;

import java.math.BigDecimal;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/*
 * 2020.04.21: 조한두 
 * 용도 : applicationChage 의 price 환율 요금 조회 : KRW -> USD
 * 안드로이드 앱: https://play.google.com/store/apps/details?id=net.xelnaga.exchanger
 *   위 안드로이드앱의 차트 환율 시각별 요율 확인 
 */
@Service
public class ExchangeApiService {
	
	
	private Logger LOGGER = LoggerFactory.getLogger(ExchangeApiService.class);
	
	String requestUriKRWUSD = "https://earthquake.kr:23490/query/KRWUSD";
	String requestUriRUBUSD  = "https://earthquake.kr:23490/query/RUBUSD";
	String requestUriCADUSD = "https://earthquake.kr:23490/query/CADUSD";
	
	
	// 20.11.16.월요일 : 위 개인 사이트가 폐쇄 됨으로 아래로 변경. 유럽중앙은행의 환율 기준 적용
	String openApiUrlKRWUSD = "https://api.exchangeratesapi.io/latest?base=KRW";
	String openApiUrlRUBUSD = "https://api.exchangeratesapi.io/latest?base=RUB";
	String openApiUrlCADUSD = "https://api.exchangeratesapi.io/latest?base=CAD";
    /*
    >> result >>{"rates":{"
    CAD":0.0011836809,              ***********************************
    "HKD":0.0069831687,
    "ISK":0.1234144408,
    "PHP":0.0434153555,
    "DKK":0.0056766069,
    "HUF":0.2711534943,
    "CZK":0.020170905,
    "GBP":6.836428E-4,
    "RON":0.0037121905,
    "SEK":0.0078162733,
    "IDR":12.7621203805,
    "INR":0.067223137,
    "BRL":0.004917368,
    "RUB":0.0698342023,           ************************************
    "HRK":0.0057720454,
    "JPY":0.0944322478,
    "THB":0.027172521,
    "CHF":8.236523E-4,
    "EUR":7.622881E-4,
    "MYR":0.0037128766,
    "BGN":0.001490883,
    "TRY":0.0069599189,
    "CNY":0.0059512593,
    "NOK":0.0082420874,
    "NZD":0.0013190633,
    "ZAR":0.0140312843,
    "USD":9.006434E-4,             ***********************************
    "MXN":0.0184655903,
    "SGD":0.0012146298,
    "AUD":0.0012425296,
    "ILS":0.0030331443,
    "KRW":1,
    "PLN":0.0034217588},
    "base":"KRW","date":"2020-11-13"}
    */
	
	 public String openExchange(String priceKRW, String country) throws Exception {
		 double price = Double.valueOf(priceKRW);
		 return String.valueOf(openExchange(price, country));
	 }
	
	 public double openExchange(double priceKRW, String country) throws Exception {
		    double returnValue = 0;
	    	int THREE_MINUTES = 3 * 60 * 1000;
			RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(THREE_MINUTES).setConnectTimeout(
			        THREE_MINUTES).setConnectionRequestTimeout(THREE_MINUTES).setStaleConnectionCheckEnabled(true).build();
			// json
			CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
			HttpGet httpget = null;
	        if("USD".equals(country))
	        	httpget = new HttpGet(openApiUrlKRWUSD);
	        else if("RUB".equals(country))
	        	httpget = new HttpGet(openApiUrlRUBUSD);
	        else if("CAD".equals(country))
	        	httpget = new HttpGet(openApiUrlCADUSD);
	        LOGGER.debug("#### getExchangePrice [getURI] : " + httpget.getURI().toString());
	        System.out.println("country:" + country + " Source price:" + priceKRW);
	        CloseableHttpResponse response = client.execute(httpget);
	        String resultContent = EntityUtils.toString(response.getEntity(), "UTF-8");
	        LOGGER.debug("#### getExchangePrice [resultContent] : " + resultContent);
	        JSONObject jsonData = new JSONObject();
			try {
				jsonData = new JSONObject(resultContent);
				System.out.println(">> result >>" + jsonData);
				JSONObject jsonrates = (JSONObject)jsonData.get("rates");
				BigDecimal exchangeRate = new BigDecimal(0.01);
				//exchangeRate = exchangeRate.valueOf((double) jsonrates.get(country));
				exchangeRate = exchangeRate.valueOf((double) jsonrates.get("USD"));
				System.out.println("exchangeRate:" + exchangeRate);
				returnValue = Double.valueOf(priceKRW) * exchangeRate.doubleValue();
				System.out.println("return price:" + returnValue  + " (USD)");
				if("USD".equals(country) && returnValue < 0.5)
				{	
					returnValue= 0.5;
					LOGGER.debug("<<USD MIN Value>>" + returnValue);
				}
				LOGGER.debug("<<ret>>" + returnValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return returnValue;
	    }

	// 원화 -> 달라 
	public String getExchangePrice(String priceKRW) throws Exception {
		return openExchange(priceKRW,"USD");
		/*
		String retPrice = "";
		
		int THREE_MINUTES = 3 * 60 * 1000;
		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(THREE_MINUTES).setConnectTimeout(
		        THREE_MINUTES).setConnectionRequestTimeout(THREE_MINUTES).setStaleConnectionCheckEnabled(true).build();
		// json
		CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        HttpGet httpget = new HttpGet(requestUriKRWUSD);
        LOGGER.debug("#### getExchangePrice [getURI] : " + httpget.getURI().toString());
        CloseableHttpResponse response = client.execute(httpget);
        String resultContent = EntityUtils.toString(response.getEntity(), "UTF-8");
        LOGGER.debug("#### getExchangePrice [resultContent] : " + resultContent);
        String update = "";
		String exchangeNoList = "";
		JSONObject jsonData = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		try {
			jsonData = new JSONObject(resultContent);
			update = jsonData.getString("update");
			LOGGER.debug(">> Exchange update:" + update);	
			jsonArray = jsonData.getJSONArray("KRWUSD");
			LOGGER.debug(">>ARRAY LEN:" + jsonArray.length() );
//			LOGGER.debug(">>double ARRAY[0]" + (double)jsonArray.getDouble(0) );
//			LOGGER.debug(">>string ARRAY[0]" + (String)jsonArray.getString(0) );
			Object jj = jsonArray.get(0); 		// 7개의 환율 변동 값이 있는데 [0] 값이 가장 최신임 
//				LOGGER.debug("<<1>> price:" + jj.toString());
//				LOGGER.debug("<<2>> price:" + (double)jj);
			LOGGER.debug("<<2>> priceKRW:" + priceKRW);
			BigDecimal exchangeRate = new BigDecimal(0.01);
			double returnValue = 0;
			exchangeRate = exchangeRate.valueOf((double)jj);
			LOGGER.debug("<<3>> price:" + exchangeRate);
			returnValue = Double.valueOf(priceKRW) * exchangeRate.doubleValue();
			if(returnValue < 0.5)
			{	
				returnValue= 0.5;
				LOGGER.debug("<<USD MIN Value>>" + returnValue);
			}
			LOGGER.debug("<<4>>" + returnValue);
			retPrice = String.valueOf(returnValue);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.debug("#### getExchangePrice [ParseException] : " + EntityUtils.toString(response.getEntity(), "UTF-8"));
		}
		return retPrice;
		*/
	}
	// 루블 -> 달라 
	public double getExchangePriceRUBTOUSD(double priceRUB) throws Exception {
		return openExchange(priceRUB,"RUB");
	   /*
		String retPrice = "";
		int THREE_MINUTES = 3 * 60 * 1000;
		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(THREE_MINUTES).setConnectTimeout(
		        THREE_MINUTES).setConnectionRequestTimeout(THREE_MINUTES).setStaleConnectionCheckEnabled(true).build();
		// json
		CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        HttpGet httpget = new HttpGet(requestUriRUBUSD);
        LOGGER.debug("#### getExchangePrice [getURI] : " + httpget.getURI().toString());
        CloseableHttpResponse response = client.execute(httpget);
        String resultContent = EntityUtils.toString(response.getEntity(), "UTF-8");
        LOGGER.debug("#### getExchangePrice [resultContent] : " + resultContent);
        String update = "";
		String exchangeNoList = "";
		JSONObject jsonData = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		double returnValue = 0;
		try {
			jsonData = new JSONObject(resultContent);
			update = jsonData.getString("update");
			LOGGER.debug(">> Exchange update:" + update);	
			jsonArray = jsonData.getJSONArray("RUBUSD");
			LOGGER.debug(">>ARRAY LEN:" + jsonArray.length() );
//			LOGGER.debug(">>double ARRAY[0]" + (double)jsonArray.getDouble(0) );
//			LOGGER.debug(">>string ARRAY[0]" + (String)jsonArray.getString(0) );
			Object jj = jsonArray.get(0); 		// 7개의 환율 변동 값이 있는데 [0] 값이 가장 최신임 
//				LOGGER.debug("<<1>> price:" + jj.toString());
//				LOGGER.debug("<<2>> price:" + (double)jj);
			LOGGER.debug("<<2>> priceRUB:" + priceRUB);
			BigDecimal exchangeRate = new BigDecimal(0.01);
			
			exchangeRate = exchangeRate.valueOf((double)jj);
			LOGGER.debug("<<3>> price:" + exchangeRate);
			returnValue = priceRUB * exchangeRate.doubleValue();
			if(returnValue < 0.5)
			{	
				returnValue= 0.5;
				LOGGER.debug("<<USD MIN Value>>" + returnValue);
			}
			LOGGER.debug("<<4>>" + returnValue);
			retPrice = String.valueOf(returnValue);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.debug("#### getExchangePrice [ParseException] : " + EntityUtils.toString(response.getEntity(), "UTF-8"));
		}
		return returnValue;
		*/
	}
	

	// CAD -> 달라 
	public double getExchangePriceCADTOUSD(double priceCAD) throws Exception {
		return openExchange(priceCAD,"CAD");
		/*
		String retPrice = "";
		int THREE_MINUTES = 3 * 60 * 1000;
		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(THREE_MINUTES).setConnectTimeout(
		        THREE_MINUTES).setConnectionRequestTimeout(THREE_MINUTES).setStaleConnectionCheckEnabled(true).build();
		// json
		CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        HttpGet httpget = new HttpGet(requestUriCADUSD);
        LOGGER.debug("#### getExchangePrice [getURI] : " + httpget.getURI().toString());
        CloseableHttpResponse response = client.execute(httpget);
        String resultContent = EntityUtils.toString(response.getEntity(), "UTF-8");
        LOGGER.debug("#### getExchangePrice [resultContent] : " + resultContent);
        String update = "";
		String exchangeNoList = "";
		JSONObject jsonData = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		double returnValue = 0;
		try {
			jsonData = new JSONObject(resultContent);
			update = jsonData.getString("update");
			LOGGER.debug(">> Exchange update:" + update);	
			jsonArray = jsonData.getJSONArray("CADUSD");
			LOGGER.debug(">>ARRAY LEN:" + jsonArray.length() );
//			LOGGER.debug(">>double ARRAY[0]" + (double)jsonArray.getDouble(0) );
//			LOGGER.debug(">>string ARRAY[0]" + (String)jsonArray.getString(0) );
			Object jj = jsonArray.get(0); 		// 7개의 환율 변동 값이 있는데 [0] 값이 가장 최신임 
//				LOGGER.debug("<<1>> price:" + jj.toString());
//				LOGGER.debug("<<2>> price:" + (double)jj);
			LOGGER.debug("<<2>> priceCAD:" + priceCAD);
			BigDecimal exchangeRate = new BigDecimal(0.01);
			
			exchangeRate = exchangeRate.valueOf((double)jj);
			LOGGER.debug("<<3>> price:" + exchangeRate);
			returnValue = priceCAD * exchangeRate.doubleValue();
			if(returnValue < 0.5)
			{	
				returnValue= 0.5;
				LOGGER.debug("<<USD MIN Value>>" + returnValue);
			}
			LOGGER.debug("<<4>>" + returnValue);
			retPrice = String.valueOf(returnValue);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.debug("#### getExchangePrice [ParseException] : " + EntityUtils.toString(response.getEntity(), "UTF-8"));
		}
		return returnValue;
		*/
	}
	
}
