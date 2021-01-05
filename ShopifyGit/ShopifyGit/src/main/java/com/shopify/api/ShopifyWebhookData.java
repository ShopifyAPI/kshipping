package com.shopify.api;

import lombok.Data;

@Data
public class ShopifyWebhookData{
	private String hookType;
	private String shop_id;
	private String shop_domain;
	private String order_to_redact;
	private String shopId;
	private String shopDomain;
	private String orderToRedact;
	
	private ShopifyWebhookDataCustomer customer;
}