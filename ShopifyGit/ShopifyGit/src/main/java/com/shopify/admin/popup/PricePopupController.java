package com.shopify.admin.popup;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.shopify.admin.AdminData;
import com.shopify.admin.price.PriceController;
import com.shopify.admin.price.PriceData;
import com.shopify.admin.price.PriceService;
import com.shopify.common.SpConstants;
import com.shopify.mapper.PriceMapper;

/**
 * 요금 맵핑 관리 팝업 컨트롤러
 *
 */
@Controller
public class PricePopupController {
	
	private Logger LOGGER = LoggerFactory.getLogger(PricePopupController.class);
	
	@Autowired
	private PriceMapper priceMapper;
	@Autowired
	private PriceService priceService;
	
	/**
	 * 요금매핑관리 > 공시가격 등록 Popup 
	 * @return
	 */
	@GetMapping(value = "/admin/popup/feesPriceNewPop")
	public String feesPriceNewPop(Model model,HttpSession sess, @ModelAttribute PriceData price) {
//		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		
		String code = price.getCode();
		String shipCompany = price.getShipCompany();
		
		model.addAttribute("shipCompany", shipCompany);
		model.addAttribute("code", code);
		
		return "/admin/popup/popFeesPriceNew";
		
	}
	
	/**
	 * 요금매핑관리 > 할인가격 등록 Popup 
	 * @return
	 */
	@GetMapping(value = "/admin/popup/salePriceNewPop")
	public String salePriceNewPop(Model model,HttpSession sess, @ModelAttribute PriceData price) {
		
//		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		
		String code = price.getCode();
		String shipCompany = price.getShipCompany();
		
		model.addAttribute("shipCompany", shipCompany);
		model.addAttribute("code", code);
		
		return "/admin/popup/popSalePriceNew";
		
	}
	
}
