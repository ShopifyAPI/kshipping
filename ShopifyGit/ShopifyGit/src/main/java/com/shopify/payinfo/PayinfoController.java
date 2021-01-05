package com.shopify.payinfo;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.shop.ShopData;

@Controller
public class PayinfoController {
	private Logger LOGGER = LoggerFactory.getLogger(PayinfoController.class);
	
	@Autowired 
	private PayinfoService payinfoservice;
	
	/**
	 * 결제저장
	 * @param model
	 * @return
	 */
	public int shipment(PayinfoData pData) {
	    
	    int cnt = payinfoservice.insertPayinfo(pData);
		return cnt;
	}
	
}
