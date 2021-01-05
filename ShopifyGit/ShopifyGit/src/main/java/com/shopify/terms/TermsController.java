package com.shopify.terms;
 
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.common.SpConstants;
import com.shopify.setting.SettingBoxData;
import com.shopify.shop.ShopData;

@Controller
public class TermsController {
//	private Logger LOGGER = LoggerFactory.getLogger(TermsController.class);
	
	@Autowired private TermsService termsService;
	
	/**
	 * 약관화면 View
	 * @return
	 */
	@GetMapping("/terms/selectTerms")
	public String selectTerms(Model model, HttpSession sess) throws Exception{
		
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		model.addAttribute("ShopData", sd);
		return "terms/terms";
	}
	
	/**
	 * 약관동의
	 * @return
	 */
	@PostMapping("/terms/updateTerms")
	public ModelAndView updateTerms(Model model, @RequestBody ShopData shopdata, HttpSession sess) throws Exception {
		
		ModelAndView mv = new ModelAndView("jsonView");
				
		int result = termsService.updateTerms(shopdata, sess);
		
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		if(result==1){sd.setPrivatechk(shopdata.getPrivatechk());
		sd.setPublicchk(shopdata.getPublicchk());
		sd.setEventchk(shopdata.getEventchk());
		}
		sess.setAttribute(SpConstants.HTTP_SESSION_KEY, sd);
				
		return mv;
	}
	
	/**
	 * 계약 요청화면 View
	 * @return
	 */
	@GetMapping("/terms/requestContact")
	public String requestContact(Model model, HttpSession sess) throws Exception{
		
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		model.addAttribute("ShopData", sd);
		return "terms/contact";
	}	
	
}
