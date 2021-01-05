package com.shopify.common;

import org.springframework.http.HttpHeaders;

import com.shopify.shop.ShopData;

import lombok.Data;

@Data
public class RestData {
	private ShopData shopData;
	private HttpHeaders httpHeaders; 
}
