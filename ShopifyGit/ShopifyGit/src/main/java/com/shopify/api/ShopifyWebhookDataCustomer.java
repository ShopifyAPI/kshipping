package com.shopify.api;

import lombok.Data;

@Data
public class ShopifyWebhookDataCustomer{
	private String id;
	private String email;
	private String phone;
}