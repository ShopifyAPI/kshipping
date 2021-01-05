package com.shopify.cs;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.shop.ShopData;

@Controller
@SpringBootApplication

/**
 * CS관리 컨트롤러
 *
 */
public class CsController {
	
	@Autowired private CsService csService;
	@Autowired private UtilFn util;
	
	private Logger LOGGER = LoggerFactory.getLogger(CsController.class);
	
	/**
	 * CS관리 > 반송목록
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/cs/backList")
	public String selectBackList(Model model, @ModelAttribute CsData csData, HttpSession sess) {
		int currentPage = csData.getCurrentPage();
        if(currentPage == 0) csData.setCurrentPage(1);
        int pageSize = csData.getPageSize();
        if(pageSize == 0) csData.setPageSize(SpConstants.PAGE_BLOCK_SIZE);
        
        if(csData.getSearchDateStart() == null || "".equals(csData.getSearchDateStart())) {
        	String searchDateStart = util.getDateElement("yymm") + "-01"; 
        	csData.setSearchDateStart(searchDateStart);
        }
        
        if(csData.getSearchDateEnd() == null || "".equals(csData.getSearchDateEnd())) {
        	String searchDateEnd = util.getDateElement("yymm") + "-" + util.getDateElement("ld"); 
        	csData.setSearchDateEnd(searchDateEnd);
        }
        
        // 그룹 코드 지정
        csData.setStateGroup(SpConstants.STATE_GROUP_BACK);
		
        Map<String, Object> map = csService.selectCsDeliveryList(csData, sess);
		model.addAttribute("search", csData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		return "/cs/backList";
	}

	
	/**
	 * CS관리 > 교환목록
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/cs/exchangeList")
	public String selectexchangeList(Model model, @ModelAttribute CsData csData, HttpSession sess) {
		
		int currentPage = csData.getCurrentPage();
        if(currentPage == 0) csData.setCurrentPage(1);
        int pageSize = csData.getPageSize();
        if(pageSize == 0) csData.setPageSize(SpConstants.PAGE_BLOCK_SIZE);
        
        if(csData.getSearchDateStart() == null || "".equals(csData.getSearchDateStart())) {
        	String searchDateStart = util.getDateElement("yymm") + "-01"; 
        	csData.setSearchDateStart(searchDateStart);
        }
        
        if(csData.getSearchDateEnd() == null || "".equals(csData.getSearchDateEnd())) {
        	String searchDateEnd = util.getDateElement("yymm") + "-" + util.getDateElement("ld"); 
        	csData.setSearchDateEnd(searchDateEnd);
        }
        
        // 그룹 코드 지정
        csData.setStateGroup(SpConstants.STATE_GROUP_EXCHANGE);
		
        Map<String, Object> map = csService.selectCsDeliveryList(csData, sess);
		model.addAttribute("search", csData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		return "/cs/exchangeList";
	}
	
	
	/**
	 * CS관리 > 반품목록
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/cs/returnList")
	public String selectReturnList(Model model, @ModelAttribute CsData csData, HttpSession sess) {
		
		int currentPage = csData.getCurrentPage();
        if(currentPage == 0) csData.setCurrentPage(1);
        int pageSize = csData.getPageSize();
        if(pageSize == 0) csData.setPageSize(SpConstants.PAGE_BLOCK_SIZE);
        
        if(csData.getSearchDateStart() == null || "".equals(csData.getSearchDateStart())) {
        	String searchDateStart = util.getDateElement("yymm") + "-01"; 
        	csData.setSearchDateStart(searchDateStart);
        }
        
        if(csData.getSearchDateEnd() == null || "".equals(csData.getSearchDateEnd())) {
        	String searchDateEnd = util.getDateElement("yymm") + "-" + util.getDateElement("ld"); 
        	csData.setSearchDateEnd(searchDateEnd);
        }
        
        // 그룹 코드 지정
        csData.setStateGroup(SpConstants.STATE_GROUP_RETURN);
		
        Map<String, Object> map = csService.selectCsDeliveryList(csData, sess);
		model.addAttribute("search", csData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		return "/cs/returnList";
	}
	
	/**
	 * CS관리 > 추가요금목록
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/cs/paymentList")
	public String selectPaymentList(Model model, @ModelAttribute CsData csData, HttpSession sess
			, @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage
			, @RequestParam(value = "searchState", required = false, defaultValue = "") String searchState
			, @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType
			, @RequestParam(value = "searchWord", required = false, defaultValue = "") String searchWord
			, @RequestParam(value = "searchDateStart", required = false, defaultValue = "") String searchDateStart
			, @RequestParam(value = "searchDateEnd", required = false, defaultValue = "") String searchDateEnd
			, @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
		) {
		
		// 기본 기간 검색 추가  (매월1일~매월말일)
        if(searchDateStart == null || "".equals(searchDateStart)) {
        	searchDateStart = util.getDateElement("yymm") + "-01"; 
        	csData.setSearchDateStart(searchDateStart);
        }
        // 기본 기간 검색 추가  (매월1일~매월말일)
        if(searchDateEnd == null || "".equals(searchDateEnd)) {
        	searchDateEnd = util.getDateElement("yymm") + "-" + util.getDateElement("ld");
        	csData.setSearchDateEnd(searchDateEnd);
        }
        
		csData.setCurrentPage(currentPage);
		csData.setSearchState(searchState);
		csData.setSearchType(searchType);
		csData.setSearchWord(searchWord);
		csData.setSearchDateStart(searchDateStart);
		csData.setSearchDateEnd(searchDateEnd);
		csData.setPageSize(pageSize);
		
		Map<String, Object> map = csService.selectPaymentList(csData, sess);
		model.addAttribute("search", csData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		return "/cs/paymentList";
	}
	
	
	/**
     * CS관리 > 반송목록 엑셀다운
     * @param model
     * @return
     */
    @SuppressWarnings("null")
    @GetMapping("/cs/backListExcel")
    public String backListExcel(@ModelAttribute CsData csData, Model model, HttpSession sess) {
        ShopData shop = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
        String searchDateStart     = csData.getSearchDateStart();
        String searchDateEnd       = csData.getSearchDateEnd();
        int currentPage               = 0;
        int pageSize                   = csData.getPageSize();
        if(pageSize == 0)          pageSize = 10;
        
        
        // 기본 기간 검색 추가  (매월1일~매월말일)
        if(searchDateStart == null || "".equals(searchDateStart)) {
        	searchDateStart = util.getDateElement("yymm") + "-01"; 
        	csData.setSearchDateStart(searchDateStart);
        }
        // 기본 기간 검색 추가  (매월1일~매월말일)
        if(searchDateEnd == null || "".equals(searchDateEnd)) {
        	searchDateEnd = util.getDateElement("yymm") + "-" + util.getDateElement("ld");
        	csData.setSearchDateEnd(searchDateEnd);
        }
        
		csData.setCurrentPage(currentPage);
		csData.setSearchDateStart(searchDateStart);
		csData.setSearchDateEnd(searchDateEnd);
		csData.setPageSize(pageSize);
        
        Map<String, Object> map = csService.selectExcelList(csData, sess);
        model.addAttribute("search", csData);
        model.addAttribute("list", map.get("list"));
        model.addAttribute("paging", map.get("paging"));
        
        return "/cs/backListExcel";
    }
    
    /**
     * CS관리 > 교환목록 엑셀다운
     * @param model
     * @return
     */
    @SuppressWarnings("null")
    @GetMapping("/cs/exchangeListExcel")
    public String exchangeListExcel(@ModelAttribute CsData csData, Model model, HttpSession sess 
            ) {
        ShopData shop = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
        String searchDateStart     = csData.getSearchDateStart();
        String searchDateEnd       = csData.getSearchDateEnd();
        String searchState       = csData.getSearchState();
        String searchType       = csData.getSearchType();
        String searchWord       = csData.getSearchWord();
          
        int currentPage               = 0;
        int pageSize                   = csData.getPageSize();
        if(pageSize == 0)          pageSize = 10;
        
        // 기본 기간 검색 추가  (매월1일~매월말일)
        if(searchDateStart == null || "".equals(searchDateStart)) {
        	searchDateStart = util.getDateElement("yymm") + "-01"; 
        	csData.setSearchDateStart(searchDateStart);
        }
        // 기본 기간 검색 추가  (매월1일~매월말일)
        if(searchDateEnd == null || "".equals(searchDateEnd)) {
        	searchDateEnd = util.getDateElement("yymm") + "-" + util.getDateElement("ld");
        	csData.setSearchDateEnd(searchDateEnd);
        }	 
        
		csData.setCurrentPage(currentPage);
		csData.setSearchState(searchState);
		csData.setSearchType(searchType);
		csData.setSearchWord(searchWord);
		csData.setSearchDateStart(searchDateStart);
		csData.setSearchDateEnd(searchDateEnd);
		csData.setPageSize(pageSize);
        
        Map<String, Object> map = csService.selectExcelList(csData, sess);
        model.addAttribute("search", csData);
        model.addAttribute("list", map.get("list"));
        model.addAttribute("paging", map.get("paging"));
        
        return "/cs/exchangeListExcel";
    }
    
    /**
     * CS관리 > 반품목록 엑셀다운
     * @param model
     * @return
     */
    @SuppressWarnings("null")
    @GetMapping("/cs/returnListExcel")
    public String returnListExcel(@ModelAttribute CsData csData, Model model, HttpSession sess 
            ) {
        ShopData shop = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
        String searchDateStart     = csData.getSearchDateStart();
        String searchDateEnd       = csData.getSearchDateEnd();
        String searchState       = csData.getSearchState();
        String searchType       = csData.getSearchType();
        String searchWord       = csData.getSearchWord();
        int currentPage               = 0;
        int pageSize                   = csData.getPageSize();
        if(pageSize == 0)          pageSize = 10;
        
        // 기본 기간 검색 추가  (매월1일~매월말일)
        if(searchDateStart == null || "".equals(searchDateStart)) {
        	searchDateStart = util.getDateElement("yymm") + "-01"; 
        	csData.setSearchDateStart(searchDateStart);
        }
        // 기본 기간 검색 추가  (매월1일~매월말일)
        if(searchDateEnd == null || "".equals(searchDateEnd)) {
        	searchDateEnd = util.getDateElement("yymm") + "-" + util.getDateElement("ld");
        	csData.setSearchDateEnd(searchDateEnd);
        }
        
		csData.setCurrentPage(currentPage);
		csData.setSearchState(searchState);
		csData.setSearchType(searchType);
		csData.setSearchWord(searchWord);
		csData.setSearchDateStart(searchDateStart);
		csData.setSearchDateEnd(searchDateEnd);
		csData.setPageSize(pageSize);
        
        Map<String, Object> map = csService.selectExcelList(csData, sess);
        model.addAttribute("search", csData);
        model.addAttribute("list", map.get("list"));
        model.addAttribute("paging", map.get("paging"));
        
        return "/cs/returnListExcel";
    }
	
    /**
     * CS관리 > 추가요금목록 엑셀다운
     * @param model
     * @return
     */
    @SuppressWarnings("null")
    @GetMapping("/cs/paymentListExcel")
    public String paymentListExcel(@ModelAttribute CsData csData, Model model, HttpSession sess 
            ) {
        ShopData shop = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
        String searchDateStart     = csData.getSearchDateStart();
        String searchDateEnd       = csData.getSearchDateEnd();
        String searchState       = csData.getSearchState();
        String searchType       = csData.getSearchType();
        String searchWord       = csData.getSearchWord();
        int currentPage               = 0;
        int pageSize                   = csData.getPageSize();
        if(pageSize == 0)          pageSize = 10;
        
        // 기본 기간 검색 추가  (매월1일~매월말일)
        if(searchDateStart == null || "".equals(searchDateStart)) {
        	searchDateStart = util.getDateElement("yymm") + "-01"; 
        	csData.setSearchDateStart(searchDateStart);
        }
        // 기본 기간 검색 추가  (매월1일~매월말일)
        if(searchDateEnd == null || "".equals(searchDateEnd)) {
        	searchDateEnd = util.getDateElement("yymm") + "-" + util.getDateElement("ld");
        	csData.setSearchDateEnd(searchDateEnd);
        }
        
		csData.setCurrentPage(currentPage);
		csData.setSearchState(searchState);
		csData.setSearchType(searchType);
		csData.setSearchWord(searchWord);
		csData.setSearchDateStart(searchDateStart);
		csData.setSearchDateEnd(searchDateEnd);
		csData.setPageSize(pageSize);
        
        Map<String, Object> map = csService.selectExcelList(csData, sess);
        model.addAttribute("search", csData);
        model.addAttribute("list", map.get("list"));
        model.addAttribute("paging", map.get("paging"));
        
        return "/cs/paymentListExcel";
    }
    
	
    
    /**
	 * CS 관리 > 상태변경
	 * @return
	 */
	@PostMapping(value = "/cs/updateStatus")
	public ModelAndView updateStatus(@RequestBody CsData csData, HttpSession sess) throws Exception {
		
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		
		int result = csService.updateCsStatus(csData, sess);
		
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
	
}
