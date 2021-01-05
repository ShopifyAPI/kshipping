package com.shopify.admin.code;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.admin.rank.RankData;
import com.shopify.admin.seller.AdminSellerData;
import com.shopify.mapper.AdminCodeMapper;

@Controller
@SpringBootApplication

/**
 * 관리자 코드관리 컨트롤러
 *
 */
public class AdminCodeController {
	
	@Autowired
	private AdminCodeMapper adminCodeMapper;
	@Autowired
	private AdminCodeService adminCodeService;
	@Autowired
    private MessageSource messageSource;

	private Logger LOGGER = LoggerFactory.getLogger(AdminCodeController.class);
	
	/**
	 * 운영관리 > 코드관리
	 * @return
	 */
	@GetMapping(value = "/admin/code/selectCode")
	public String adminSeller (Model model, AdminCodeData codeData, HttpSession sess
			, @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage
			, @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType
			, @RequestParam(value = "searchUseYn", required = false, defaultValue = "") String searchUseYn
			, @RequestParam(value = "searchWord", required = false, defaultValue = "") String searchWord
			, @RequestParam(value = "searchCodeGroup", required = false, defaultValue = "") String searchCodeGroup
			, @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
		
		codeData.setCurrentPage(currentPage);
		codeData.setSearchType(searchType);
		codeData.setSearchUseYn(searchUseYn);
		codeData.setSearchWord(searchWord);
		codeData.setSearchCodeGroup(searchCodeGroup);
		
		codeData.setPageSize(pageSize);
		
		Map<String, Object> map = adminCodeService.selectAdminCode(codeData, sess);
		
		model.addAttribute("search", codeData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		
		return "admin/code/admCodeList";
	}
	
	/**
	 * 운영관리 > 코드관리 > 코드 등록(대분류)
	 * @param model
	 * @return
	 */
	@PostMapping("/admin/code/insertAdminCodeGroup")
	public ModelAndView insertAdminCodeGroup(@RequestBody AdminCodeData codeData, HttpSession sess) throws Exception {
		
		LOGGER.debug("adminCodeData>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+codeData);
		ModelAndView mv = new ModelAndView("jsonView");
		
		int result = adminCodeService.insertAdminCodeGroup(codeData,sess);
		
		if(result == -1) {
			
			mv.addObject("result", result);
			mv.addObject("errCode", false);
			mv.addObject("errMsg", "실패");
			
		}else {
			
			String codeGroup = codeData.getCodeGroup();
			
			mv.addObject("result", result);
			mv.addObject("errCode", true);
			mv.addObject("errMsg", "성공");
			mv.addObject("codeGroup", codeGroup);
			
		}

		return mv;
	}
	
	
	/**
	 * 운영관리 > 코드관리 > 코드 등록(중분류)
	 * @param model
	 * @return
	 */
	@PostMapping("/admin/code/insertAdminCodeId")
	public ModelAndView insertAdminCodeId(@RequestBody AdminCodeData codeData, HttpSession sess) throws Exception {
		
		LOGGER.debug("adminCodeData>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+codeData);
		ModelAndView mv = new ModelAndView("jsonView");
		
		int result = adminCodeService.insertAdminCodeId(codeData,sess);
		
		if(result == -1) {
			
			mv.addObject("result", result);
			mv.addObject("errCode", false);
			mv.addObject("errMsg", "실패");
			
		}else {
			
			mv.addObject("result", result);
			mv.addObject("errCode", true);
			mv.addObject("errMsg", "성공");
			
		}

		return mv;
	}
	
	
	/**
	 * 운영관리 > 코드관리 > 코드 수정
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/admin/code/editAdminCodeGroup")
	public ModelAndView editAdminCodeGroup(@RequestBody AdminCodeData codeData, HttpSession sess) throws Exception {
		
		LOGGER.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!@"+codeData);
		
		int result = adminCodeService.updateAdminCode(codeData, sess);
		
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		String codeGroup = codeData.getCodeGroup();
		
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		mv.addObject("codeGroup", codeGroup);
		
		return mv;
	}
	
	/**
	 * 운영관리 > 코드관리 > 코드 삭제
	 * @param model
	 * @return
	 */
	@PostMapping("/admin/code/deleteAdminCode")
	public ModelAndView deleteAdminCode(Model model, @RequestBody AdminCodeData codeData ,HttpSession sess) throws Exception {
		    	
		ModelAndView mv = new ModelAndView("jsonView");
		
		int result = adminCodeService.deleteAdminCode(codeData,sess);
		
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;		
	}
	
	
}
