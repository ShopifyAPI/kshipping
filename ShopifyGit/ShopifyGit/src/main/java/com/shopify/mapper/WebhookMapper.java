package com.shopify.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.shopify.api.ShopifyWebhookData;
import com.shopify.api.ShopifyWebhookDataCustomer;

@Mapper
public interface WebhookMapper {
	public int insertHook(ShopifyWebhookData hook);
	public int insertHookCustomer(ShopifyWebhookDataCustomer hookCustomer);
	public int deleteCustomer(ShopifyWebhookData hook);
	public int deleteShop(ShopifyWebhookData hook);
	public int selectCustomerCount (ShopifyWebhookData hook);
	public ShopifyWebhookData selectCustomer (ShopifyWebhookData hook);
}
