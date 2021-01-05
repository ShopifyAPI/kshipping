package com.shopify.admin.popup;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.shopify.admin.AdminData;
import com.shopify.admin.seller.AdminSellerData;
import com.shopify.common.SpConstants;
import com.shopify.shop.ShopData;

/**
 * 관리자 팝업 컨트롤러
 *
 */
@Controller
public class AdminNewPopupController {
	
	private Logger LOGGER = LoggerFactory.getLogger(AdminNewPopupController.class);
	
	/**
	 * 운영관리 > 관리자 등록 Popup 
	 * @return
	 */
	@GetMapping(value = "/admin/admin/popup/adminNewPop")
	public String adminList(Model model,HttpSession sess, AdminData admin) {
		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);

		return "/admin/popup/popAdminNew";
		
	}
	
	/**
	 * 관리자 > 셀러관리 > 랭크수정 Popup 
	 * @return
	 */
	@GetMapping(value = "/admin/popup/seller/popSellerRank")
	public String adminSellerRank(Model model, AdminSellerData setting, HttpSession sess
			, @RequestParam(value = "email", required = false, defaultValue = "") String email
			, @RequestParam(value = "rankId", required = false, defaultValue = "") String rankId
			, @RequestParam(value = "shopStatus", required = false, defaultValue = "") String shopStatus
			, @RequestParam(value = "paymentMethod", required = false, defaultValue = "") String paymentMethod
		) {
		
		setting.setEmail(email);
		setting.setRankId(rankId);
		setting.setShopStatus(shopStatus);
		setting.setPaymentMethod(paymentMethod);
		
		
		model.addAttribute("seller", setting);
		
		return "/admin/popup/popSellerRank";
	}	
}
