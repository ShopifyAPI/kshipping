package com.shopify.admin.code.popup;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shopify.admin.AdminData;
import com.shopify.common.SpConstants;
import com.shopify.mapper.AdminCodeMapper;

/**
 * 코드관리 팝업 컨트롤러
 *
 */
@Controller
public class adminCodePopupController {
	
	@Autowired 	private AdminCodeMapper adminCodeMapper;
	@Autowired 	private AdminCodePopupService adminCodeService;
	
	private Logger LOGGER = LoggerFactory.getLogger(adminCodePopupController.class);
	
	/**
	 * 코드관리 > 코드 등록 Popup 
	 * @return
	 */
	@GetMapping(value = "/admin/popup/adminCodeNewPop")
	public String adminCodeNewPop(Model model,HttpSession sess, AdminCodePopupData codePopupData) {
		
//		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		return "admin/popup/popAdminCodeNew";
		
	}
	
	/**
	 * 코드관리 > 코드 수정 Popup 
	 * @return
	 */
	@GetMapping(value = "/admin/popup/adminCodeEditPop")
	public String adminCodeEditPop(Model model,HttpSession sess, AdminCodePopupData codePopupData
			, @RequestParam(value = "codeId", required = false, defaultValue = "") String codeId) {
		
//		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		codePopupData.setCodeId(codeId);
		
		AdminCodePopupData returnCodePopupData = adminCodeService.selectAdminCodeShow(codePopupData);
		
		model.addAttribute("adminCodePopupData", returnCodePopupData);		
		
		return "admin/popup/popAdminCodeEdit";
		
	}
	
	
	
	
	
	
}
