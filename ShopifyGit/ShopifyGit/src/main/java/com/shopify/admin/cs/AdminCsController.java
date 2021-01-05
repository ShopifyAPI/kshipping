package com.shopify.admin.cs;

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

import com.shopify.admin.AdminData;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;

@Controller
@SpringBootApplication

/**
 * CS관리 컨트롤러
 *
 */
public class AdminCsController {
	
	@Autowired private AdminCsService csService;
	@Autowired private UtilFn util;
	
	private Logger LOGGER = LoggerFactory.getLogger(AdminCsController.class);
	
	
	/**
	 * CS관리 > 배송목록
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/admin/cs/adminDeliveryList")
	public String selectAdminDeliveryList(Model model, @ModelAttribute AdminCsData csData, HttpSession sess) {
		int currentPage = csData.getCurrentPage();
        if(currentPage == 0) csData.setCurrentPage(1);
        int pageSize = csData.getPageSize();
        if(pageSize == 0) csData.setPageSize(SpConstants.PAGE_BLOCK_SIZE);

		// 날짜 매달 1~30일인데 현재 날짜 기준으로 과거 30일로 검색 기간 조정 
		 String searchDateStart     = csData.getSearchDateStart();
		 String searchDateEnd       = csData.getSearchDateEnd();
		 if (true) {
				// 검색기간 정보가 비어있을 경우, 지난 일주일의 데이터를 출력한다.
				int nPrevDays = 30 ;
				String sDateFormat = "YYYY-MM-dd" ;
				// 종료일자 : 오늘
				if (searchDateEnd == null || "".equals(searchDateEnd)) {
					searchDateEnd = util.getToday(sDateFormat) ;
					csData.setSearchDateEnd(searchDateEnd) ;
				}
				// 시작일자 : 30일전
				if (searchDateStart == null || "".equals(searchDateStart)) {
					searchDateStart = util.getPrevDate(nPrevDays, sDateFormat) ; 
					csData.setSearchDateStart(searchDateStart) ;
				}
		}    
        // 그룹 코드 지정
        csData.setStateGroup(SpConstants.STATE_GROUP_DELIVERY);

		Map<String, Object> map = csService.selectAdminCsDeliveryList(csData, sess);
		model.addAttribute("search", csData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		return "/admin/cs/adminDeliveryList";
	}
	
	/**
	 * CS관리 > 에러목록
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/admin/cs/adminErrorList")
	public String selectAdminErrorList(Model model, @ModelAttribute AdminCsData csData, HttpSession sess) {
		LOGGER.debug("----------adminErrorList----------------");
		int currentPage = csData.getCurrentPage();
        if(currentPage == 0) csData.setCurrentPage(1);
        int pageSize = csData.getPageSize();
        csData.setSearchState("A020025");
        if(pageSize == 0) csData.setPageSize(SpConstants.PAGE_BLOCK_SIZE);
    	// 날짜 매달 1~30일인데 현재 날짜 기준으로 과거 30일로 검색 기간 조정 
		 String searchDateStart     = csData.getSearchDateStart();
		 String searchDateEnd       = csData.getSearchDateEnd();
		 if (true) {
				// 검색기간 정보가 비어있을 경우, 지난 일주일의 데이터를 출력한다.
				int nPrevDays = 30 ;
				String sDateFormat = "YYYY-MM-dd" ;
				// 종료일자 : 오늘
				if (searchDateEnd == null || "".equals(searchDateEnd)) {
					searchDateEnd = util.getToday(sDateFormat) ;
					csData.setSearchDateEnd(searchDateEnd) ;
				}
				// 시작일자 : 30일전
				if (searchDateStart == null || "".equals(searchDateStart)) {
					searchDateStart = util.getPrevDate(nPrevDays, sDateFormat) ; 
					csData.setSearchDateStart(searchDateStart) ;
				}
		}    
        // 그룹 코드 지정
       // csData.setStateGroup(SpConstants.STATE_GROUP_BACK);

		Map<String, Object> map = csService.selectAdminCsDeliveryList(csData, sess);
		model.addAttribute("search", csData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		return "/admin/cs/adminErrorList";
	}
	
	/**
	 * CS관리 > 반송목록
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/admin/cs/adminBackList")
	public String selectAdminBackList(Model model, @ModelAttribute AdminCsData csData, HttpSession sess) {
		int currentPage = csData.getCurrentPage();
        if(currentPage == 0) csData.setCurrentPage(1);
        int pageSize = csData.getPageSize();
        if(pageSize == 0) csData.setPageSize(SpConstants.PAGE_BLOCK_SIZE);
    	// 날짜 매달 1~30일인데 현재 날짜 기준으로 과거 30일로 검색 기간 조정 
		 String searchDateStart     = csData.getSearchDateStart();
		 String searchDateEnd       = csData.getSearchDateEnd();
		 if (true) {
				// 검색기간 정보가 비어있을 경우, 지난 일주일의 데이터를 출력한다.
				int nPrevDays = 30 ;
				String sDateFormat = "YYYY-MM-dd" ;
				// 종료일자 : 오늘
				if (searchDateEnd == null || "".equals(searchDateEnd)) {
					searchDateEnd = util.getToday(sDateFormat) ;
					csData.setSearchDateEnd(searchDateEnd) ;
				}
				// 시작일자 : 30일전
				if (searchDateStart == null || "".equals(searchDateStart)) {
					searchDateStart = util.getPrevDate(nPrevDays, sDateFormat) ; 
					csData.setSearchDateStart(searchDateStart) ;
				}
		}    
        // 그룹 코드 지정
        csData.setStateGroup(SpConstants.STATE_GROUP_BACK);

		Map<String, Object> map = csService.selectAdminCsDeliveryList(csData, sess);
		model.addAttribute("search", csData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		return "/admin/cs/adminBackList";
	}

	
	/**
	 * CS관리 > 교환목록
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/admin/cs/adminExchangeList")
	public String selectAdminExchangeList(Model model, @ModelAttribute AdminCsData csData, HttpSession sess) {
		
		int currentPage = csData.getCurrentPage();
        if(currentPage == 0) csData.setCurrentPage(1);
        int pageSize = csData.getPageSize();
        if(pageSize == 0) csData.setPageSize(SpConstants.PAGE_BLOCK_SIZE);
    	// 날짜 매달 1~30일인데 현재 날짜 기준으로 과거 30일로 검색 기간 조정 
		 String searchDateStart     = csData.getSearchDateStart();
		 String searchDateEnd       = csData.getSearchDateEnd();
		 if (true) {
				// 검색기간 정보가 비어있을 경우, 지난 일주일의 데이터를 출력한다.
				int nPrevDays = 30 ;
				String sDateFormat = "YYYY-MM-dd" ;
				// 종료일자 : 오늘
				if (searchDateEnd == null || "".equals(searchDateEnd)) {
					searchDateEnd = util.getToday(sDateFormat) ;
					csData.setSearchDateEnd(searchDateEnd) ;
				}
				// 시작일자 : 30일전
				if (searchDateStart == null || "".equals(searchDateStart)) {
					searchDateStart = util.getPrevDate(nPrevDays, sDateFormat) ; 
					csData.setSearchDateStart(searchDateStart) ;
				}
		}    
        // 그룹 코드 지정
        csData.setStateGroup(SpConstants.STATE_GROUP_EXCHANGE);

		Map<String, Object> map = csService.selectAdminCsDeliveryList(csData, sess);
		model.addAttribute("search", csData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		return "/admin/cs/adminExchangeList";
	}
	
	
	/**
	 * CS관리 > 반품목록
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/admin/cs/adminReturnList")
	public String selectAdminReturnList(Model model, @ModelAttribute AdminCsData csData, HttpSession sess) {
		
		int currentPage = csData.getCurrentPage();
        if(currentPage == 0) csData.setCurrentPage(1);
        int pageSize = csData.getPageSize();
        if(pageSize == 0) csData.setPageSize(SpConstants.PAGE_BLOCK_SIZE);
        // 날짜 매달 1~30일인데 현재 날짜 기준으로 과거 30일로 검색 기간 조정 
		 String searchDateStart     = csData.getSearchDateStart();
		 String searchDateEnd       = csData.getSearchDateEnd();
		 if (true) {
				// 검색기간 정보가 비어있을 경우, 지난 일주일의 데이터를 출력한다.
				int nPrevDays = 30 ;
				String sDateFormat = "YYYY-MM-dd" ;
				// 종료일자 : 오늘
				if (searchDateEnd == null || "".equals(searchDateEnd)) {
					searchDateEnd = util.getToday(sDateFormat) ;
					csData.setSearchDateEnd(searchDateEnd) ;
				}
				// 시작일자 : 30일전
				if (searchDateStart == null || "".equals(searchDateStart)) {
					searchDateStart = util.getPrevDate(nPrevDays, sDateFormat) ; 
					csData.setSearchDateStart(searchDateStart) ;
				}
		}    
        // 그룹 코드 지정
        csData.setStateGroup(SpConstants.STATE_GROUP_RETURN);

		Map<String, Object> map = csService.selectAdminCsDeliveryList(csData, sess);
		model.addAttribute("search", csData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		return "/admin/cs/adminReturnList";
	}
	
	/**
	 * CS관리 > 추가요금목록
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/admin/cs/adminPaymentList")
	public String selectPaymentList(Model model, AdminCsData csData, HttpSession sess
			, @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage
			, @RequestParam(value = "searchState", required = false, defaultValue = "") String searchState
			, @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType
			, @RequestParam(value = "searchWord", required = false, defaultValue = "") String searchWord
			, @RequestParam(value = "searchDateStart", required = false, defaultValue = "") String searchDateStart
			, @RequestParam(value = "searchDateEnd", required = false, defaultValue = "") String searchDateEnd
			, @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
		) {
	 
		// 날짜 매달 1~30일인데 현재 날짜 기준으로 과거 30일로 검색 기간 조정 
		 searchDateStart     = csData.getSearchDateStart();
		 searchDateEnd       = csData.getSearchDateEnd();
		 if (true) {
				// 검색기간 정보가 비어있을 경우, 지난 일주일의 데이터를 출력한다.
				int nPrevDays = 30 ;
				String sDateFormat = "YYYY-MM-dd" ;
				// 종료일자 : 오늘
				if (searchDateEnd == null || "".equals(searchDateEnd)) {
					searchDateEnd = util.getToday(sDateFormat) ;
					csData.setSearchDateEnd(searchDateEnd) ;
				}
				// 시작일자 : 30일전
				if (searchDateStart == null || "".equals(searchDateStart)) {
					searchDateStart = util.getPrevDate(nPrevDays, sDateFormat) ; 
					csData.setSearchDateStart(searchDateStart) ;
				}
		}    
		csData.setCurrentPage(currentPage);
		csData.setSearchState(searchState);
		csData.setSearchType(searchType);
		csData.setSearchWord(searchWord);
		csData.setSearchDateStart(searchDateStart);
		csData.setSearchDateEnd(searchDateEnd);
		csData.setPageSize(pageSize);
		
		Map<String, Object> map = csService.selectAdminPaymentList(csData, sess);
		model.addAttribute("search", csData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		return "/admin/cs/adminPaymentList";
	}
	
	/**
     * CS관리 > 배송목록 엑셀다운
     * @param model
     * @return
     */
    @SuppressWarnings("null")
    @GetMapping("/admin/cs/adminDeliveryListExcel")
    public String adminDeliveryListExcel(@ModelAttribute AdminCsData csData, Model model, HttpSession sess 
            ) {
    	LOGGER.debug("-----<EXCEL>-------");
    	AdminData shop = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
        String searchDateStart     = csData.getSearchDateStart();
        String searchDateEnd       = csData.getSearchDateEnd();
        int currentPage               = 0;
        int pageSize                   = csData.getPageSize();
        LOGGER.debug(">searchDateStart:" + searchDateStart);
        LOGGER.debug(">searchDateEnd:" + searchDateEnd);
	    
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
        
        if(pageSize == 0)          pageSize = 10;
        csData.setSearchDateStart(searchDateStart);
        csData.setSearchDateEnd(searchDateEnd);
        csData.setCurrentPage(currentPage);
        csData.setPageSize(pageSize);
        
        // 그룹 코드 지정
        //csData.setStateGroup(SpConstants.STATE_GROUP_DELIVERY);

        Map<String, Object> map = csService.selectAdminDeliveryList(csData, sess);
        model.addAttribute("search", csData);
        model.addAttribute("list", map.get("list"));
        model.addAttribute("paging", map.get("paging"));
       
        return "/admin/cs/adminDeliveryListExcel";
    }
	
	/**
     * CS관리 > 에러목록 엑셀다운
     * @param model
     * @return
     */
    @SuppressWarnings("null")
    @GetMapping("/admin/cs/adminErrorListExcel")
    public String adminErrorListExcel(@ModelAttribute AdminCsData csData, Model model, HttpSession sess 
            ) {
    	LOGGER.debug("-----<EXCEL>-------");
    	AdminData shop = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
        String searchDateStart     = csData.getSearchDateStart();
        String searchDateEnd       = csData.getSearchDateEnd();
        int currentPage               = 0;
        int pageSize                   = csData.getPageSize();
        csData.setSearchState("A020025");
        LOGGER.debug(">searchDateStart:" + searchDateStart);
        LOGGER.debug(">searchDateEnd:" + searchDateEnd);
	    
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
        
        if(pageSize == 0)          pageSize = 10;
        csData.setSearchDateStart(searchDateStart);
        csData.setSearchDateEnd(searchDateEnd);
        csData.setCurrentPage(currentPage);
        csData.setPageSize(pageSize);
        
        // 그룹 코드 지정
        //csData.setStateGroup(SpConstants.STATE_GROUP_DELIVERY);

        Map<String, Object> map = csService.selectAdminDeliveryList(csData, sess);
        model.addAttribute("search", csData);
        model.addAttribute("list", map.get("list"));
        model.addAttribute("paging", map.get("paging"));
       
        return "/admin/cs/adminErrorListExcel";
    }
	
	
	/**
     * CS관리 > 반송목록 엑셀다운
     * @param model
     * @return
     */
    @SuppressWarnings("null")
    @GetMapping("/admin/cs/adminBackListExcel")
    public String adminBackListExcel(@ModelAttribute AdminCsData csData, Model model, HttpSession sess 
            ) {
    	AdminData shop = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
        String searchDateStart     = csData.getSearchDateStart();
        String searchDateEnd       = csData.getSearchDateEnd();
        int currentPage               = 0;
        int pageSize                   = csData.getPageSize();
        
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
        
        if(pageSize == 0)          pageSize = 10;
        csData.setSearchDateStart(searchDateStart);
        csData.setSearchDateEnd(searchDateEnd);
        csData.setCurrentPage(currentPage);
        csData.setPageSize(pageSize);
        
        Map<String, Object> map = csService.selectAdminExcelList(csData, sess);
        model.addAttribute("search", csData);
        model.addAttribute("list", map.get("list"));
        model.addAttribute("paging", map.get("paging"));
        
        return "/admin/cs/adminBackListExcel";
    }
    
    /**
     * CS관리 > 교환목록 엑셀다운
     * @param model
     * @return
     */
    @SuppressWarnings("null")
    @GetMapping("/admin/cs/adminExchangeListExcel")
    public String adminExchangeListExcel(@ModelAttribute AdminCsData csData, Model model, HttpSession sess 
            ) {
    	AdminData shop = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
        String searchDateStart     = csData.getSearchDateStart();
        String searchDateEnd       = csData.getSearchDateEnd();
        int currentPage               = 0;
        int pageSize                   = csData.getPageSize();
        
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
        
        if(pageSize == 0)          pageSize = 10;
        csData.setSearchDateStart(searchDateStart);
        csData.setSearchDateEnd(searchDateEnd);
        csData.setCurrentPage(currentPage);
        csData.setPageSize(pageSize);
        
        Map<String, Object> map = csService.selectAdminExcelList(csData, sess);
        model.addAttribute("search", csData);
        model.addAttribute("list", map.get("list"));
        model.addAttribute("paging", map.get("paging"));
        
        return "/admin/cs/adminExchangeListExcel";
    }
    
    /**
     * CS관리 > 반품목록 엑셀다운
     * @param model
     * @return
     */
    @SuppressWarnings("null")
    @GetMapping("/admin/cs/adminReturnListExcel")
    public String adminReturnListExcel(@ModelAttribute AdminCsData csData, Model model, HttpSession sess 
            ) {
    	AdminData shop = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
        String searchDateStart     = csData.getSearchDateStart();
        String searchDateEnd       = csData.getSearchDateEnd();
        int currentPage               = 0;
        int pageSize                   = csData.getPageSize();
        
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
        
        if(pageSize == 0)          pageSize = 10;
        csData.setSearchDateStart(searchDateStart);
        csData.setSearchDateEnd(searchDateEnd);
        csData.setCurrentPage(currentPage);
        csData.setPageSize(pageSize);
        
        Map<String, Object> map = csService.selectAdminExcelList(csData, sess);
        model.addAttribute("search", csData);
        model.addAttribute("list", map.get("list"));
        model.addAttribute("paging", map.get("paging"));
        
        return "/admin/cs/adminReturnListExcel";
    }
	
    /**
     * CS관리 > 추가요금 엑셀다운
     * @param model
     * @return
     */
    @SuppressWarnings("null")
    @GetMapping("/admin/cs/adminPaymentListExcel")
    public String paymentListExcel(@ModelAttribute AdminCsData csData, Model model, HttpSession sess 
            ) {
    	AdminData shop = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
        String searchDateStart     = csData.getSearchDateStart();
        String searchDateEnd       = csData.getSearchDateEnd();
        int currentPage               = 0;
        int pageSize                   = csData.getPageSize();
        
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
        
        if(pageSize == 0)          pageSize = 10;
        csData.setSearchDateStart(searchDateStart);
        csData.setSearchDateEnd(searchDateEnd);
        csData.setCurrentPage(currentPage);
        csData.setPageSize(pageSize);
        
        Map<String, Object> map = csService.selectAdminExcelList(csData, sess);
        model.addAttribute("search", csData);
        model.addAttribute("list", map.get("list"));
        model.addAttribute("paging", map.get("paging"));
        
        return "/admin/cs/adminPaymentListExcel";
    }
    
	
    
    /**
	 * CS 관리 > 추가요금 상태변경
	 * @return
	 */
	@PostMapping(value = "/admin/cs/adminUpdateStatus")
	public ModelAndView adminUpdateStatus(@RequestBody AdminCsData csData, HttpSession sess) throws Exception {
		int result = csService.updateAdminCsPaymentStatus(csData, sess);
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		if(result > 0) {	
			mv.addObject("result", result);
			mv.addObject("errCode", true);
			mv.addObject("errMsg", "성공");
		}else {
			mv.addObject("result", result);
			mv.addObject("errCode", false);
			mv.addObject("errMsg", "실패");
		}
		
		return mv;
	}
	
	/**
	 * CS 관리 > 메일발송
	 * @return
	 */
	@PostMapping(value = "/admin/cs/adminSendMail")
	public ModelAndView adminSendMail(@RequestBody AdminCsData csData, HttpSession sess) throws Exception {
		int result = csService.sendMailAdminCs(csData, sess);
		
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
