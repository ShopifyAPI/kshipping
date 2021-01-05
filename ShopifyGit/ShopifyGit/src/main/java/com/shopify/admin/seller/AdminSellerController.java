package com.shopify.admin.seller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.admin.AdminData;
import com.shopify.admin.price.PriceData;
import com.shopify.api.ShopifyOutApiDataCarrier;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFunc;


/**
* 관리자 > Seller Controller
* @author : jwh
* @since  : 2020-01-21
* @desc   : Seller 정보 관리 
*/
@Controller
public class AdminSellerController {
	private Logger LOGGER = LoggerFactory.getLogger(AdminSellerController.class);
	
	@Autowired 
	private AdminSellerService sellerService;
	

	/**
	 * 관리자 > 셀러 관리 > 리스트
	 * @return
	 */
	@SuppressWarnings("null")
	@GetMapping(value = "/admin/seller/list")
	public String adminSeller (Model model, AdminSellerData setting, HttpSession sess
			, @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage
			, @RequestParam(value = "searchRankId", required = false, defaultValue = "") String searchRankId
			, @RequestParam(value = "searchType", required = false, defaultValue = "shopId") String searchType
			, @RequestParam(value = "searchWord", required = false, defaultValue = "") String searchWord
			, @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
		) {
		
		setting.setCurrentPage(currentPage);
		setting.setSearchRankId(searchRankId);
		setting.setSearchType(searchType);
		setting.setSearchWord(searchWord);
		setting.setPageSize(pageSize);
		
		Map<String, Object> map = sellerService.selectAdminSeller(setting, sess);
		
		model.addAttribute("search", setting);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		return "/admin/seller/admSellerList";
	}
	
	@GetMapping(value = "/admin/seller/discount")
	public String adminSellerDiscount(Model model, PriceData priceData, HttpSession sess 
			, @RequestParam(value = "searchType", required = false, defaultValue = "shopId") String searchType
			, @RequestParam(value = "searchWord", required = false, defaultValue = "") String searchWord
			) {
		
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    	priceData.setLocale(sd.getLocale());

    	
    	// 검색조건
    	String zoneCodeId = priceData.getZoneCodeId();
    	if ( zoneCodeId == null ) {
    		zoneCodeId = "";
    	}
    	String zoneCodeGroup = priceData.getZoneCodeGroup();
    	if ( zoneCodeGroup == null ) {
    		zoneCodeGroup = "";
    	}
    	
    	// 검색일자 생성 
    	String nowDate = priceData.getNowDate();
    	if(nowDate == null || "".equals(nowDate)) {
    		nowDate = UtilFunc.today();
    		priceData.setNowDate(nowDate);
    	}
		
		Map<String, Object> map = sellerService.selectSellerDiscount(priceData);
		
		model.addAttribute("courierList", map.get("courierList") );
		model.addAttribute("sellerDiscountList",  map.get("sellerDiscountList") );
		
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("partShipCompany", zoneCodeId);
		model.addAttribute("partDeliveryService", zoneCodeGroup);
		
		return "/admin/seller/admSellerDiscount";
	}
	
	@GetMapping(value = "/admin/seller/discountExcel")
	public String adminSellerDiscountExcel(Model model, PriceData priceData, HttpSession sess, HttpServletResponse response) {
		
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		priceData.setLocale(sd.getLocale());
		
		
		// 검색조건
		String zoneCodeId = priceData.getZoneCodeId();
		if ( zoneCodeId == null ) {
			zoneCodeId = "";
		}
		String zoneCodeGroup = priceData.getZoneCodeGroup();
		if ( zoneCodeGroup == null ) {
			zoneCodeGroup = "";
		}
		
		// 검색일자 생성 
		String nowDate = priceData.getNowDate();
		if(nowDate == null || "".equals(nowDate)) {
			nowDate = UtilFunc.today();
			priceData.setNowDate(nowDate);
		}
		
		Map<String, Object> map = sellerService.selectSellerDiscount(priceData);
		
		model.addAttribute("courierList", map.get("courierList") );
		model.addAttribute("sellerDiscountList",  map.get("sellerDiscountList") );
		
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("partShipCompany", zoneCodeId);
		model.addAttribute("partDeliveryService", zoneCodeGroup);
		
		UtilFunc.setExcelResponse(response, "셀러별할인율");
		
		return "/admin/seller/admSellerDiscountExcel";
	}
	
	/**
	 * 관리자 > 셀러 관리 >  셀러 검색 화면
	 * @return
	 */
	@GetMapping(value = {"/admin/seller/sellerSearch", "/admin/statis/sellerSearch", "/admin/common/sellerSearch"})
	public String adminSellerSearch (Model model, @ModelAttribute AdminSellerData setting, HttpSession sess) {
		model.addAttribute("search", setting);
		return "/admin/seller/admSellerSearch";
	}
	
	/**
	 * 관리자 > 셀러 관리 >  셀러 검색 리스트
	 * @return
	 */
	@PostMapping(value = {"/admin/seller/sellerSearchProc", "/admin/statis/sellerSearchProc", "/admin/common/sellerSearchProc"})
	public ModelAndView adminSellerSearchList (Model model, @RequestBody AdminSellerData setting, HttpSession sess) {
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		int currentPage = setting.getCurrentPage();
	    int pageSize = setting.getPageSize();
	    
	    if(currentPage == 0) currentPage = 1;
	    if(pageSize == 0) pageSize = 500;

	    setting.setSearchType("total");
		setting.setCurrentPage(currentPage);
		setting.setPageSize(pageSize);
		
		Map<String, Object> map = sellerService.selectAdminSeller(setting, sess);
		
		model.addAttribute("search", setting);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		
		mv.addObject("search", setting);
		mv.addObject("list", map.get("list"));
		mv.addObject("paging", map.get("paging"));
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
	/**
	 * 관리자 > 셀러 관리 > 상세보기
	 * @return
	 */
	@GetMapping(value = "/admin/seller/viewSeller")
	public String seller(Model model, AdminSellerData setting, AdminShopData settingShop, HttpSession sess
			, @RequestParam(value = "email", required = false, defaultValue = "") String email
			, @RequestParam(value = "currentPage", required = false, defaultValue = "") int currentPage
			, @RequestParam(value = "searchRankId", required = false, defaultValue = "") String searchRankId
			, @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType
			, @RequestParam(value = "searchWord", required = false, defaultValue = "") String searchWord
			, @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
		) {
		
		setting.setEmail(email);
		setting.setCurrentPage(currentPage);
		setting.setSearchRankId(searchRankId);
		setting.setSearchType(searchType);
		setting.setSearchWord(searchWord);
		setting.setPageSize(pageSize);
				
		settingShop.setEmail(email); 
		
		
		
		List<AdminShopData> list = sellerService.selectAdminShop(settingShop, sess);
		AdminSellerData seller = sellerService.selectAdminSellerDetail(setting, sess);
		model.addAttribute("search", setting);
		model.addAttribute("seller", seller);
		model.addAttribute("list", list);
		
		// seller 별 할인율 가져오기
		PriceData priceData = new PriceData();
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		priceData.setLocale(sd.getLocale());
		priceData.setNowDate(UtilFunc.today());
		priceData.setEmail(email);
		Map<String, Object> map = sellerService.selectSellerDiscountFullList(priceData, seller);
		
		model.addAttribute("nowDate", UtilFunc.today() );
		model.addAttribute("courierList", map.get("courierList") );
		model.addAttribute("sellerDiscount", map.get("sellerDiscount") );
		model.addAttribute("discountListSize", map.get("discountListSize") );
		model.addAttribute("discountHistory", map.get("discountHistory") );
		
		return "/admin/seller/admSellerView";
	}
	
	/**
	 * 관리자 > 셀러 관리 > 등급 update
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/admin/seller/updateSellerDiscount")
	public ModelAndView updateSellerDiscount(Model model, @RequestBody SellerDiscountDateData setting, HttpSession sess) throws Exception {
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		
		String result = sellerService.updateSellerDiscount(setting);
		
//		mv.addObject("result", result);
		
		if ( result == null ) {
			mv.addObject("errCode", true);
			mv.addObject("errMsg", "성공");
		} else {
			mv.addObject("errCode", false);
			mv.addObject("errMsg", result);
		}
		
		return mv;
	}
	
	/**
	 * 관리자 > 셀러 관리 > 상세보기
	 * @return
	 */
	@GetMapping(value = "/admin/popup/seller/popSellerRankLog")
	public String popSellerRankLog(Model model, @ModelAttribute AdminSellerData setting, HttpSession sess) {
		
		List<AdminSellerData> list = sellerService.selectSellerRankLog(setting);
		
		model.addAttribute("list", list);
		
		return "/admin/popup/popSellerRankLog";
	}

	/**
	 * 관리자 > 셀러 관리 > 판매 쇼핑몰 결제상태 수정
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/admin/seller/updateShopBilling")
	public ModelAndView updateShopBilling(@RequestBody AdminShopData setting, HttpSession sess) throws Exception {
		
		ModelAndView mv = new ModelAndView("jsonView");
		int result = sellerService.updateShopBilling(setting, sess);
		
		//mv.addObject("procCode", "button.edit");
		mv.addObject("billingYn", setting.getBillingYn());
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	/**
	 * 관리자 > 셀러 관리 > 셀러 계약 상태 업데이트
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/admin/seller/updateActive")
	public ModelAndView updateActive(@RequestBody AdminShopData setting, HttpSession sess) throws Exception {
		
		ModelAndView mv = new ModelAndView("jsonView");
		int result = sellerService.updateActive(setting, sess);
		
		//mv.addObject("procCode", "button.edit");
		mv.addObject("activeYn", setting.getActiveYn());
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
}

