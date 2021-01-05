package com.shopify.api;

import java.io.Serializable;

import lombok.Data;

@Data
public class ShopifyOutApiDataCarrierRates implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String serviceName;
	private String serviceCode;
	private String totalPrice;
	private String description;
	private String currency;
	private String minDeliveryDate;
	private String maxDeliveryDate;


}