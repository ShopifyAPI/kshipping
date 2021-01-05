package com.shopify.admin.board;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.admin.AdminData;
import com.shopify.board.BoardData;
import com.shopify.common.SpConstants;
import com.shopify.mapper.AdminBoardMapper;
import com.shopify.setting.SettingData;
import com.shopify.shop.ShopData;

@Controller
@SpringBootApplication

/**
 * 관리자 컨트롤러
 *
 */
public class AdminBoardController {
	
	@Autowired
	private AdminBoardMapper adminBoardMapper;
	@Autowired
	private AdminBoardService adminBoardService;
	@Autowired
    private MessageSource messageSource;
    @Autowired
    LocaleResolver localeResolver;
	
	private Logger LOGGER = LoggerFactory.getLogger(AdminBoardController.class);
	
	/**
	 * 고객센터 > 관리자 Q&A 조회 
	 * @return
	 */
	@GetMapping("/admin/board/selectQna")
	public String selectQna(Locale locale,Model model, HttpSession sess,AdminBoardData boardData
			, @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage
			, @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType
			, @RequestParam(value = "searchWord", required = false, defaultValue = "") String searchWord
			, @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
		) {
		
		boardData.setCurrentPage(currentPage);
		boardData.setSearchType(searchType);
		boardData.setSearchWord(searchWord);
		boardData.setPageSize(pageSize);
		
		Map<String, Object> map = adminBoardService.selectQna(boardData, sess); 
		
		model.addAttribute("search", boardData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		return "admin/board/admQnaList";
		
	}
	
	/**
	 * Q&A 상세 조회(관리자 답변등록)
	 * @return
	 */
	@GetMapping("/admin/board/admQnaAnswer")
	public String selectAdmQnaAnswer(Model model, AdminBoardData boardData, HttpSession sess) throws Exception {
		
		AdminBoardData adminBoardData = adminBoardService.selectAdmQnaAnswer(boardData,sess);
		
		model.addAttribute("adminBoardData", adminBoardData);		
		
		return "admin/board/admQnaAnswer";
	}
	
	/**
	 * Q&A 수정(관리자 답변 등록)
	 * @return
	 */
	@PostMapping("/admin/board/updateBoardAnswer")
	public ModelAndView updateBoardAnswer(Model model, @RequestBody AdminBoardData boardData, HttpSession sess) throws Exception {
		 
		ModelAndView mv = new ModelAndView("jsonView");
		
		int result = adminBoardService.updateBoardAnswer(boardData,sess);
		
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}	
	
	/**
	 * 관리자 FAQ 조회
	 * @return
	 */
	@GetMapping("/admin/board/selectFaq")
	public String selectFaq(Model model,HttpSession sess, AdminBoardData boardData
			, @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage
			, @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType
			, @RequestParam(value = "searchWord", required = false, defaultValue = "") String searchWord
			, @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
		) {
		
		boardData.setCurrentPage(currentPage);
		boardData.setSearchType(searchType);
		boardData.setSearchWord(searchWord);
		boardData.setPageSize(pageSize);
		
		Map<String, Object> map = adminBoardService.selectFaq(boardData, sess); 
		
		model.addAttribute("search", boardData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		return "admin/board/admFaqList";
		
		

	}
	
	/**
	 * FAQ 상세조회
	 * @return
	 */
	@GetMapping("/admin/board/admEditFaq")
	public String selectFaqShow(Model model, AdminBoardData boardData, HttpSession sess) throws Exception {
		
		AdminBoardData data = adminBoardService.selectFaqShow(boardData,sess);
		
		model.addAttribute("adminBoardData", data);
		
		return "admin/board/admFaqEdit";
		
	}
	
	/**
	 * 관리자 FAQ 등록
	 * @return
	 */
	@PostMapping("/admin/board/insertBoard")
	public ModelAndView insertBoard(Model model, @RequestBody AdminBoardData boardData,HttpSession sess) throws Exception {
		    	
		ModelAndView mv = new ModelAndView("jsonView");
		
		int result = adminBoardService.insertBoard(boardData,sess);
		
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
	/**
	 * 관리자 FAQ 수정
	 * @return
	 */
	@PostMapping("/admin/board/updateFaq")
	public ModelAndView updateFaq(Model model, @RequestBody AdminBoardData boardData, HttpSession sess) throws Exception {
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		
		if(sd == null) {
			LOGGER.debug("AAAAAAAAAAAAAAAAAAAAAAAAA"+sd);
		}
			int result = adminBoardService.updateFaq(boardData,sess);

			mv.addObject("result", result);
			mv.addObject("errCode", true);
			mv.addObject("errMsg", "성공");
			
			return mv;
		
	}
	
	/**
	 * 화면 이동 (관리자 FAQ 등록 화면)
	 * @return
	 */
	@GetMapping("/admin/board/admNewFaq")
	public ModelAndView admNewFaq(HttpServletRequest req, HttpSession sess) throws Exception {
		
        ModelAndView mav = new ModelAndView("admin/board/admFaqNew");
		
		return mav;

	}
	
	/**
	 * 게시물 삭제
	 * @return
	 * 여러개의 row를 삭제처리 하기 위해 Map으로 처리하였음
	 */
	@PostMapping("/admin/board/deleteBoard")
	public ModelAndView deleteBoard(Model model, @RequestBody AdminBoardData boardData,HttpSession sess) throws Exception {
		    	
		ModelAndView mv = new ModelAndView("jsonView");
		
		int result = adminBoardService.deleteBoard(boardData,sess);
		
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
}
