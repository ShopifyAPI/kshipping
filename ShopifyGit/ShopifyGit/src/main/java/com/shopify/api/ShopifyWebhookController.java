package com.shopify.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ShopifyWebhookController {
	private Logger LOGGER = LoggerFactory.getLogger(ShopifyWebhookController.class);
	
	@Autowired ShopifyWebhookService hook;
	
	
	//고객 정보 요청
	@PostMapping("/customerRequest")
	public String webhookCustomerRequest(@RequestBody ShopifyWebhookData swd, HttpServletRequest req, HttpSession sess) {
		
		swd.setHookType("1");
		swd.setShopId(swd.getShop_id());
		swd.setShopDomain(swd.getShop_domain());
		swd.setOrderToRedact(swd.getOrder_to_redact());

		hook.insertWebhook(swd);
		hook.insertWebhookCustomer(swd.getCustomer());
		
		return "ok";
	}
	
	//고객 정보 삭제요청
	@PostMapping("/customerRedact")
	public String webhookCustomerRedat(@RequestBody ShopifyWebhookData swd, HttpServletRequest req, HttpSession sess) {
		
		swd.setHookType("2");
		swd.setShopId(swd.getShop_id());
		swd.setShopDomain(swd.getShop_domain());
		swd.setOrderToRedact(swd.getOrder_to_redact());
		
		hook.insertWebhook(swd);
		hook.insertWebhookCustomer(swd.getCustomer());
		
		return "ok";
	}

	
	//샵 정보 삭제요청
	@PostMapping("/shopRedact")
	public String webhookShopRedat(@RequestBody ShopifyWebhookData swd, HttpServletRequest req, HttpSession sess) {
		
		swd.setHookType("3");
		swd.setShopId(swd.getShop_id());
		swd.setShopDomain(swd.getShop_domain());
		swd.setOrderToRedact(swd.getOrder_to_redact());
		
		hook.insertWebhook(swd);
		hook.insertWebhookCustomer(swd.getCustomer());
		
		return "ok";
	}
}