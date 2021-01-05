package com.shopify.admin;

import java.util.Locale;
import java.util.Map;

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
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.mapper.AdminMapper;

@Controller
//@SpringBootApplication

/**
 * 관리자 컨트롤러
 *
 */
public class AdminController {
	
	@Autowired
	private AdminMapper adminMapper;
	@Autowired
	private AdminService adminService;
	@Autowired
    private MessageSource messageSource;
    @Autowired
    LocaleResolver localeResolver;
	
	private Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
	
	/**
	 * 운영관리 > 관리자 목록 조회 View 페이지 
	 * @return
	 */
	@GetMapping(value = "/admin/admin/adminList")
	public String adminList(Model model , HttpSession sess, AdminData admin
			, @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage
			, @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType
			, @RequestParam(value = "searchWord", required = false, defaultValue = "") String searchWord
			, @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize	) {
		
		admin.setCurrentPage(currentPage);
		admin.setSearchType(searchType);
		admin.setSearchWord(searchWord);
		admin.setPageSize(pageSize);
		
		Map<String, Object> map = adminService.selectAdminList(sess, admin); 

		model.addAttribute("search", admin);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		return "admin/adminList";
		
	}
	
	/**
	 * 운영관리 > 관리자 삭제
	 * @param model
	 * @return
	 * 다수 데이타 삭제처리를 위해 Map<String, Object> 우선 params으로 처리 @RequestBody(DATA)로 처리 해야함
	 */
	@PostMapping("/admin/admin/deleteAdminList")
	public ModelAndView deleteAdminList(@RequestBody AdminData adminData, HttpSession sess) throws Exception {
		
		int result = adminService.deleteAdminList(adminData,sess);
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
	/**
	 * 운영관리 > 관리자 수정
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/admin/admin/editAdminList")
	public ModelAndView editAdminList(@RequestBody AdminData admin, HttpSession sess) throws Exception {
		
		int result = adminService.updateAdminList(admin,sess);
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
	/**
	 * 운영관리 > 관리자 관리 상세보기
	 * @param 
	 * @return
	 */
	@GetMapping("/admin/admin/showAdmin")
	public String showAdmin(Locale locale, Model model, AdminData adminData, HttpSession sess ) throws Exception {
		
		AdminData admin = adminService.selectAdminShow(adminData,sess);
		
		model.addAttribute("adminData", admin);
		
		return "admin/showAdmin";
		
	}
	
	/**
	 * 운영관리 > 관리자 등록
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/admin/admin/insertAdminListPop")
	public ModelAndView insertAdminListPop(@RequestBody AdminData admin, HttpSession sess) throws Exception {
		
		int result = adminService.insertAdminListPop(admin,sess);
		
		ModelAndView mv = new ModelAndView("jsonView");

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
	 * 운영관리 > 관리자 비밀번호 수정
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/admin/admin/editAdminPassword")
	public ModelAndView editAdminPassword(@RequestBody AdminData admin, HttpSession sess) throws Exception {
		
		int result = adminService.editAdminPassword(admin,sess);
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
	@GetMapping(value = "/admin/admin/exceptionList")
	public String adminList(Model model) {
		return "admin/exceptionList";
	}
	
}
