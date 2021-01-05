package com.shopify.api;

import java.io.Serializable;

import lombok.Data;

@Data
public class ShopifyOutApiDataCarrierItems implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private String name;
	private String sku;
	private int quantity;
	private int grams;
	private int price;
	private String vendor;
	private String requires_shipping;
	private String taxable;
	private String fulfillment_service;
	//private String properties;
	private String productCode;
	private String product_id;
	private String variantId;
	private int volumeWeightSix;
	private int volumeWeightFive;
	private int shopIdx;

}