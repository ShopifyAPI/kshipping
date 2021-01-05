package com.shopify.api.lotte.delivery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class LotteDeliveryController {
	private Logger LOGGER = LoggerFactory.getLogger(LotteDeliveryController.class);
	
	// 롯데 국내택배 tracking
	@GetMapping("/api/lotte/homeTracking")
	public ModelAndView shopifyGetAccessToken(@RequestParam("invNo") String invNo) {
		ModelAndView mav = new ModelAndView("tracking/lotteHomeTracking");
		mav.addObject("invNo", invNo);
		return mav;
	}
	
}