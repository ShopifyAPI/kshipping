package com.shopify.admin.user;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.admin.AdminData;
import com.shopify.admin.AdminService;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;

/**
* 관리자 개인정보 설정
* @author : 
* @since  : 
* @desc   :  
*/

@Controller
@RequestMapping("/admin/")
public class AdminUserController {
	
	private Logger LOGGER = LoggerFactory.getLogger(AdminUserController.class);

	@Autowired private UtilFn util; 
	@Autowired private AdminService adminService;
	
	/**
	 * 관리자 개인정보
	 * @param Model
	 * @param SettingData
	 * @param SettingShopData
	 * @param HttpSession
	 * @return page
	 */
	@GetMapping(value = "/userInfo")
	public String userInfo(Model model, AdminData admin, HttpSession sess) {		
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		String email = sd.getEmail();
		
		admin.setEmail(email);
		admin.setId(email);
		admin.setAdminId(email);
		
		AdminData adminInfo = adminService.selectAdminShow(admin, sess);
		
		model.addAttribute("adminData", adminInfo);
		
		//return "admin/showAdmin";
		
		return "/admin/userInfo";
	}
	
	/**
	 * 관리자 개인정보 > 관리자 비밀번호 수정
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/admin/userInfo/editPassword")
	public ModelAndView editPassword(@RequestBody AdminData admin, HttpSession sess) throws Exception {
		LOGGER.debug("editPassword start #########################################################");
		
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		String email = sd.getEmail();
		
		LOGGER.debug("editPassword : " + email);
		
//		admin.setEmail(email);
//		admin.setId(email);
//		
//		int result = adminService.editAdminPassword(admin, sess);
		
		ModelAndView mv = new ModelAndView("jsonView");
		
//		mv.addObject("result", result);
//		mv.addObject("errCode", true);
//		mv.addObject("errMsg", "성공");
		mv.addObject("procCode", "button.edit");
		
		return mv;
	}
}
