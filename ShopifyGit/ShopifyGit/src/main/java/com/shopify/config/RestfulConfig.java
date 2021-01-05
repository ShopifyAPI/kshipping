package com.shopify.config;
 
import java.io.IOException;
import java.util.Collections;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
 
@Configuration
public class RestfulConfig {
 
	@Value("${restTemplate.factory.readTimeout}")
	private int READ_TIMEOUT;
	
	@Value("${restTemplate.factory.connectTimeout}")
	private int CONNECT_TIMEOUT;
	
	@Value("${restTemplate.httpClient.maxConnTotal}")
	private int MAX_CONN_TOTAL;
	
	@Value("${restTemplate.httpClient.maxConnPerRoute}")
	private int MAX_CONN_PER_ROUTE;
	
	@Value("${shopify.AppId}")
	private String AppId;
	
	@Value("${shopify.AppPw}")
	private String AppPw;
	
	@Value("${shopify.ShopUrl}")
	private String ShopUrl;
	
	
	@Bean
	public RestTemplate restTemplate() {
		
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(READ_TIMEOUT); 
		factory.setConnectTimeout(CONNECT_TIMEOUT); 
		
		HttpClient httpClient = HttpClientBuilder.create() 
				.setMaxConnTotal(MAX_CONN_TOTAL) 
				.setMaxConnPerRoute(MAX_CONN_PER_ROUTE) 
				.build();
 
		factory.setHttpClient(httpClient);
		
		RestTemplate restTemplate = new RestTemplate(factory);
		
		restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor(){
			@Override
	        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
	        	request.getHeaders().set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
	        	request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
	        	request.getHeaders().setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	        	return execution.execute(request, body);
	        }
		}); 
 
		return restTemplate;
	}
	
	//앱id와 앱pw를 가지고 api를 전달해야하기에 병합하는 것을 가져옴
	@Bean
	public String makeURI() {
		return "https://"+AppId+":"+AppPw+"@"+ShopUrl;
	}
	
	@Bean
	public String buildURI() {
		return "https://"+ShopUrl;
	}
}
