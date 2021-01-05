package com.shopify.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopify.mapper.WebhookMapper;

/**
 * 웹후크
 *
 */
@Service
@Transactional
public class ShopifyWebhookService {
	private Logger LOGGER = LoggerFactory.getLogger(ShopifyWebhookService.class);
	@Autowired private WebhookMapper hookMapper;
	
	public int insertWebhook(ShopifyWebhookData hook){
		return hookMapper.insertHook(hook);
	}
	
	public int insertWebhookCustomer(ShopifyWebhookDataCustomer hookCustomer){
		return hookMapper.insertHookCustomer(hookCustomer);
	}
    
}
	