package com.shopify.admin.statis;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.shop.ShopData;

/**
* 관리자 > 정산관리 Controller
* @author : jwh
* @since  : 2020-03-11
* @desc   : 정산관리 정보 관리 
*/
@Controller
@RequestMapping("/admin/statis")
public class AdminStatisController {
	
	private Logger LOGGER = LoggerFactory.getLogger(AdminStatisController.class);
	
	@Autowired private AdminStatisService adminStatisService;
	@Autowired private UtilFn util; 
	
	/**
	 * 관리자 > 정산관리 > 매출원장(해외) 리스트
	 * @param Model
	 * @param AdminStatisData
	 * @param HttpSession
	 * @return page
	 */
	/*
	@GetMapping(value = "/sales")
	public String salesList(Model model, @ModelAttribute AdminStatisData statis, HttpSession sess) {
		
		statis.setSearchPaymentCode("NA");
		
		Map<String, Object> map = adminStatisService.selectStatisSales(statis, sess, false);
		
		model.addAttribute("mode", statis.getSearchPaymentCode());
		model.addAttribute("search", map.get("search"));
		model.addAttribute("sum", map.get("sum"));
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		
		return "/admin/statis/sales";
	}
	*/
	/**
	 * 관리자 > 정산관리 > 매출원장(해외) 리스트 (조한두) -- 신규 
	 * @param Model
	 * @param AdminStatisData
	 * @param HttpSession
	 * @return page
	 */
	@GetMapping(value = "/sales")
	//@GetMapping(value = "/salesNew")
	public String salesListNew(Model model, @ModelAttribute AdminStatisData statis, HttpSession sess) {
		LOGGER.debug("-----------------------[salesNew]-----------------------");
		statis.setSearchPaymentCode("ND"); // 국내 픽업 + 해외배송 
		//statis.setSearchPaymentCodeEx("N%");
		// 날짜 매달 1~30일인데 현재 날짜 기준으로 과거 30일로 검색 기간 조정 
		 String searchDateStart     = statis.getSearchDateStart();
		 String searchDateEnd       = statis.getSearchDateEnd();
		if (true) {
				// 검색기간 정보가 비어있을 경우, 지난 일주일의 데이터를 출력한다.
				int nPrevDays = 30 ;
				String sDateFormat = "YYYY-MM-dd" ;
				// 종료일자 : 오늘
				if (searchDateEnd == null || "".equals(searchDateEnd)) {
					searchDateEnd = util.getToday(sDateFormat) ;
					statis.setSearchDateEnd(searchDateEnd) ;
				}
				// 시작일자 : 30일전
				if (searchDateStart == null || "".equals(searchDateStart)) {
					searchDateStart = util.getPrevDate(nPrevDays, sDateFormat) ; 
					statis.setSearchDateStart(searchDateStart) ;
				}
		}    
		Map<String, Object> map = adminStatisService.selectStatisSales(statis, sess, false);
		
		model.addAttribute("mode", statis.getSearchPaymentCode());
		model.addAttribute("search", map.get("search"));
		model.addAttribute("sum", map.get("sum"));
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		
		return "/admin/statis/salesNew";
	}
	
	/**
	 * 관리자 > 정산관리 > 매출원장 재정산 
	 * @param Model
	 * @param AdminStatisData
	 * @param HttpSession
	 * @return page
	 */
	@PostMapping(value = "/salesReCalcul")
	public ModelAndView salesReCalcul(Model model, @ModelAttribute AdminStatisData statis, HttpSession sess, HttpServletRequest req) {
		 LOGGER.debug("-----------------------[salesReCalCul START]-----------------------");
		 ModelAndView mav = new ModelAndView("jsonView");
		 String searchDateStart    =  statis.getSearchDateStart();
		 String searchDateEnd      = statis.getSearchDateEnd();
		 LOGGER.debug(">start: " +  searchDateStart);
		 LOGGER.debug(">end: " + searchDateEnd);
		 int ret = 0;

		final String DATE_PATTERN = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		try {
			Date startDate = sdf.parse(searchDateStart);
			Date endDate = sdf.parse(searchDateEnd);
			ArrayList<String> dates = new ArrayList<String>();
			Date currentDate = startDate;
			while (currentDate.compareTo(endDate) <= 0) {
				dates.add(sdf.format(currentDate));
				Calendar c = Calendar.getInstance();
				c.setTime(currentDate);
				c.add(Calendar.DAY_OF_MONTH, 1);
				currentDate = c.getTime();
			}
			for (String date : dates) {
				LOGGER.debug(">date:" + date);
				AdminStatisMakeData inParam = new AdminStatisMakeData();
				inParam.setNowDate(date);
				LOGGER.debug("--------DELETE START---------");
				adminStatisService.deleteStatisAddPayment(inParam);			// tb_add_payment
				LOGGER.debug("<STEP1> deleteStatisAddPayment");
				adminStatisService.deleteStatisSales(inParam);							// tb_statis_sales
				LOGGER.debug("<STEP3> deleteStatisSales");
				adminStatisService.insertStatisSalesNew(inParam);					// tb_statis_sales
				LOGGER.debug("<STEP4> insertStatisSalesNew");
				LOGGER.debug("--------DELETE END---------");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			ret = -1;
		}
		if(ret < 0 )
			 model.addAttribute("errCode", null);
		LOGGER.debug("-----------------------[salesReCalCul END]-----------------------");
 		return mav;
	}
	
	
	/**
	 * 관리자 > 정산관리 > 손익통계 재정산 
	 * @param Model
	 * @param AdminStatisData
	 * @param HttpSession
	 * @return page
	 */
	@PostMapping(value = "/salesRePayment")
	public ModelAndView salesRePayment(Model model, @ModelAttribute AdminStatisData statis, HttpSession sess, HttpServletRequest req) {
		 LOGGER.debug("-----------------------[salesRePayment START]-----------------------");
		 ModelAndView mav = new ModelAndView("jsonView");
		 String searchDateStart    =  statis.getSearchDateStart();
		 String searchDateEnd      = statis.getSearchDateEnd();
		 LOGGER.debug(">start: " +  searchDateStart);
		 LOGGER.debug(">end: " + searchDateEnd);
		 int ret = 0;

		final String DATE_PATTERN = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		try {
			Date startDate = sdf.parse(searchDateStart);
			Date endDate = sdf.parse(searchDateEnd);
			ArrayList<String> dates = new ArrayList<String>();
			Date currentDate = startDate;
			while (currentDate.compareTo(endDate) <= 0) {
				dates.add(sdf.format(currentDate));
				Calendar c = Calendar.getInstance();
				c.setTime(currentDate);
				c.add(Calendar.DAY_OF_MONTH, 1);
				currentDate = c.getTime();
			}
			for (String date : dates) {
				LOGGER.debug(">date:" + date);
				AdminStatisMakeData inParam = new AdminStatisMakeData();
				inParam.setNowDate(date);
				LOGGER.debug("--------DELETE START---------");
				adminStatisService.deleteStatisPayment(date);							// tb_statis_payment_new
				LOGGER.debug("<STEP2> deleteStatisPayment");
				adminStatisService.insertStatisPaymentNew(inParam);			// tb_statis_payment_new
				LOGGER.debug("<STEP5> insertStatisPaymentNew");
				LOGGER.debug("--------DELETE END---------");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			ret = -1;
		}
		if(ret < 0 )
			 model.addAttribute("errCode", null);
		LOGGER.debug("-----------------------[salesRePayment END]-----------------------");
 		return mav;
	}
	
	/**
	 * 관리자 > 정산관리 > 매출원장(해외) 리스트 (조한두) -- 신규 
	 * @param Model
	 * @param AdminStatisData
	 * @param HttpSession
	 * @return page
	 */
	@GetMapping(value = "/salesNewCompany")
	//@GetMapping(value = "/salesNew")
	public String salesListNewCompany(Model model, @ModelAttribute AdminStatisData statis, HttpSession sess) {
		LOGGER.debug("-----------------------[salesNewCompany]-----------------------");
		statis.setSearchPaymentCode("ND"); // 국내 픽업 + 해외배송 
		statis.setCompanyCalcul("A");
	 	Map<String, Object> map = adminStatisService.selectStatisSales(statis, sess, false);
		model.addAttribute("mode", statis.getSearchPaymentCode());
		model.addAttribute("search", map.get("search"));
		model.addAttribute("sum", map.get("sum"));
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		return "/admin/statis/salesNew";
	}
	@GetMapping(value = "/salesNewAllCompany")
	//@GetMapping(value = "/salesNew")
	public String salesListNewAllCompany(Model model, @ModelAttribute AdminStatisData statis, HttpSession sess) {
		LOGGER.debug("-----------------------[salesNewAllCompany]-----------------------");
		statis.setSearchPaymentCode("ND"); // 국내 픽업 + 해외배송 
	 	Map<String, Object> map = adminStatisService.selectStatisSales(statis, sess, false);
		model.addAttribute("mode", statis.getSearchPaymentCode());
		model.addAttribute("search", map.get("search"));
		model.addAttribute("sum", map.get("sum"));
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		return "/admin/statis/salesNew";
	}
	
	/**
	 * 관리자 > 정산관리 > 매출원장(해외) 리스트 (조한두) -- 신규 
	 * @param Model
	 * @param AdminStatisData
	 * @param HttpSession
	 * @return page
	 */
	@GetMapping(value = "/salesNewSeller")
	//@GetMapping(value = "/salesNew")
	public String salesListNewSeller(Model model, @ModelAttribute AdminStatisData statis, HttpSession sess) {
		LOGGER.debug("-----------------------[salesNewSeller]-----------------------");
		statis.setSearchPaymentCode("ND"); // 국내 픽업 + 해외배송 
		//statis.setSellerCalcul(true);
	 	statis.setSellerCalcul("A");
		Map<String, Object> map = adminStatisService.selectStatisSales(statis, sess, false);
		model.addAttribute("mode", statis.getSearchPaymentCode());
		model.addAttribute("search", map.get("search"));
		model.addAttribute("sum", map.get("sum"));
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		return "/admin/statis/salesNew";
	}
	@GetMapping(value = "/salesNewAllSeller")
	//@GetMapping(value = "/salesNew")
	public String salesListNewAllSeller(Model model, @ModelAttribute AdminStatisData statis, HttpSession sess) {
		LOGGER.debug("-----------------------[salesNewSeller]-----------------------");
		statis.setSearchPaymentCode("ND"); // 국내 픽업 + 해외배송 
		Map<String, Object> map = adminStatisService.selectStatisSales(statis, sess, false);
		model.addAttribute("mode", statis.getSearchPaymentCode());
		model.addAttribute("search", map.get("search"));
		model.addAttribute("sum", map.get("sum"));
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		return "/admin/statis/salesNew";
	}
	/**
	 * 관리자 > 정산관리 > 매출원장  > 엑셀 다운로드
	 * @param Model
	 */
	@GetMapping("/sales/salesExcel")
	public String staticExcel(Model model, @ModelAttribute AdminStatisData sData, HttpSession sess, HttpServletRequest req
		 ) {
		LOGGER.debug("------<EXCEL>---------");
		String searchDateStart = ""; 		// 시작 기간 
		String searchDateEnd = "";       // 종료 기간 
		String searchPaymentCode = ""; // NA/ND/EW 
		String searchCompany = "";   
		String searchCourier = "";
		String searchSeller = "";
		String searchMode = "";
		if(req.getParameter("searchDateStartValue") != null)
		{	
			searchDateStart = req.getParameter("searchDateStartValue");
			sData.setSearchDateStart(searchDateStart);
		}
		if(req.getParameter("searchDateEndValue") != null)
		{	
			searchDateEnd = req.getParameter("searchDateEndValue");
			sData.setSearchDateEnd(searchDateEnd);
		}
		if(req.getParameter("searchMode") != null && "EW".equals(searchMode))
		{	
			searchPaymentCode = searchMode;
			sData.setSearchPaymentCode(searchPaymentCode);
		}
		if(req.getParameter("searchCompany") != null)
		{	
			searchCompany = req.getParameter("searchCompany");
			sData.setSearchCompany(searchCompany);
		}
		if(req.getParameter("searchCourier") != null)
		{	
			searchCourier = req.getParameter("searchCourier");
			sData.setSearchCourier(searchCourier);
		}
		if(req.getParameter("searchSeller") != null)
		{	
			searchSeller = req.getParameter("searchSeller");
			sData.setSearchSeller(searchSeller);
		}
		if(req.getParameter("searchMode") != null)
		{	
			searchMode = req.getParameter("searchMode");
		}
		LOGGER.debug("startValue:"+searchDateStart);
		LOGGER.debug("endValue:"+searchDateEnd);
		LOGGER.debug("searchPaymentCode:"+searchPaymentCode);
		LOGGER.debug("searchCompany:"+searchCompany);
		LOGGER.debug("searchCourier:"+searchCourier);
		LOGGER.debug("searchSeller:"+searchSeller);
		LOGGER.debug("searchMode:"+searchMode);
		ShopData shop = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);

		 sData.setCurrentPage(0);
		 sData.setPageSize(1000);
		
		Map<String, Object> map = null;
		if("ND".equals(searchMode)) // 국내 요금 
			map = adminStatisService.selectStatisSales(sData, sess, true);
		else  // 해외, 초과 요금 
			map = adminStatisService.selectStatisSales(sData, sess, false);
		
		model.addAttribute("mode", sData.getSearchPaymentCode());
		model.addAttribute("search", map.get("search"));
		model.addAttribute("sum", map.get("sum"));
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		if("ND".equals(searchMode)) // 국내 요금 
			return "/admin/statis/inc/incSalesLocalExcel";
		else if("NA".equals(searchMode)) // 해외 요금 
			return "/admin/statis/inc/incSalesDeliveryExcel";
		else  // EW 초과 요금 
			return "/admin/statis/inc/incSalesChargeExcel";
	}
	
	/**
	 * 관리자 > 정산관리 > 매출원장  > 엑셀 다운로드
	 * @param Model
	 */
	@GetMapping("/sales/salesExcelNew")
	public String staticExcelNew(Model model, @ModelAttribute AdminStatisData sData, HttpSession sess, HttpServletRequest req
		 ) {
		LOGGER.debug("------<EXCEL NEW>---------");
		LOGGER.debug(">req:" + req.toString());
		String searchDateStart = ""; 		// 시작 기간 
		String searchDateEnd = "";       // 종료 기간 
		String searchPaymentCode = ""; // NA/ND/EW 
		String searchCompany = "";   
		String searchCourier = "";
		String searchSeller = "";
		String searchMode = "";
		// 날짜 매달 1~30일인데 현재 날짜 기준으로 과거 30일로 검색 기간 조정 
	    searchDateStart     = sData.getSearchDateStart();
		searchDateEnd       = sData.getSearchDateEnd();
		if (true) {
				// 검색기간 정보가 비어있을 경우, 지난 일주일의 데이터를 출력한다.
	 			int nPrevDays = 30 ;
				String sDateFormat = "YYYY-MM-dd" ;
				// 종료일자 : 오늘
				if (searchDateEnd == null || "".equals(searchDateEnd)) {
					searchDateEnd = util.getToday(sDateFormat) ;
					sData.setSearchDateEnd(searchDateEnd) ;
				}
				// 시작일자 : 30일전
				if (searchDateStart == null || "".equals(searchDateStart)) {
					searchDateStart = util.getPrevDate(nPrevDays, sDateFormat) ; 
					sData.setSearchDateStart(searchDateStart) ;
				}
		}    
				
		if(req.getParameter("searchDateStartValue") != null)
		{	
			searchDateStart = req.getParameter("searchDateStartValue");
			sData.setSearchDateStart(searchDateStart);
		}
		if(req.getParameter("searchDateEndValue") != null)
		{	
			searchDateEnd = req.getParameter("searchDateEndValue");
			sData.setSearchDateEnd(searchDateEnd);
		}
		/*
		if(req.getParameter("searchMode") != null )
		{	
			searchPaymentCode = searchMode;
			sData.setSearchPaymentCode(searchPaymentCode);
		}
		*/
		if(req.getParameter("searchCompany") != null)
		{	
			searchCompany = req.getParameter("searchCompany");
			sData.setSearchCompany(searchCompany);
		}
		if(req.getParameter("searchCourier") != null)
		{	
			searchCourier = req.getParameter("searchCourier");
			sData.setSearchCourier(searchCourier);
		}
		if(req.getParameter("searchSeller") != null)
		{	
			searchSeller = req.getParameter("searchSeller");
			sData.setSearchSeller(searchSeller);
		}
		if(req.getParameter("searchMode") != null)
		{	
			searchMode = req.getParameter("searchMode");
			searchPaymentCode = searchMode;
			sData.setSearchPaymentCode(searchPaymentCode);
		}
		
		LOGGER.debug("startValue:"+searchDateStart);
		LOGGER.debug("endValue:"+searchDateEnd);
		LOGGER.debug("searchPaymentCode:"+searchPaymentCode);
		LOGGER.debug("searchCompany:"+searchCompany);
		LOGGER.debug("searchCourier:"+searchCourier);
		LOGGER.debug("searchSeller:"+searchSeller);
		LOGGER.debug("searchMode:"+searchMode);
		ShopData shop = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);

		 sData.setCurrentPage(0);
		 sData.setPageSize(1000);
		
		Map<String, Object> map = null;
		if("DO".equals(searchMode)) // 국내 요금 
			map = adminStatisService.selectStatisSales(sData, sess, true);
		else  // 해외, 초과 요금 
			map = adminStatisService.selectStatisSales(sData, sess, false);
		
		model.addAttribute("mode", sData.getSearchPaymentCode());
		model.addAttribute("search", map.get("search"));
		model.addAttribute("sum", map.get("sum"));
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		if("DO".equals(searchMode)) // 국내 요금 
			return "/admin/statis/inc1/incSalesLocalExcel";
		else if("ND".equals(searchMode)) // 해외 요금 
			return "/admin/statis/inc1/incSalesDeliveryExcel";
		else  // EW 초과 요금 
			return "/admin/statis/inc1/incSalesChargeExcel";
	}

	/**
	 * 관리자 > 정산관리 > 매출원장(국내) 리스트 - NEW
	 * @param Model
	 * @param AdminStatisData
	 * @param HttpSession
	 * @return page
	 */
	@GetMapping(value = "/salesLocalNew")
	public String salesLocalListNew(Model model, @ModelAttribute AdminStatisData statis, HttpSession sess) {		
		LOGGER.debug("-----------------------[salesLocalNew]-----------------------");
		statis.setSearchPaymentCode("DO");
		// 날짜 매달 1~30일인데 현재 날짜 기준으로 과거 30일로 검색 기간 조정 
	    String searchDateStart     = statis.getSearchDateStart();
		String searchDateEnd       = statis.getSearchDateEnd();
		if (true) {
				// 검색기간 정보가 비어있을 경우, 지난 일주일의 데이터를 출력한다.
				int nPrevDays = 30 ;
		   	    String sDateFormat = "YYYY-MM-dd" ;
				// 종료일자 : 오늘
			    if (searchDateEnd == null || "".equals(searchDateEnd)) {
					searchDateEnd = util.getToday(sDateFormat) ;
					statis.setSearchDateEnd(searchDateEnd) ;
				}
				// 시작일자 : 30일전
				if (searchDateStart == null || "".equals(searchDateStart)) {
					searchDateStart = util.getPrevDate(nPrevDays, sDateFormat) ; 
					statis.setSearchDateStart(searchDateStart) ;
				}
		}    
		Map<String, Object> map = adminStatisService.selectStatisSales(statis, sess, true);
		
		model.addAttribute("mode", statis.getSearchPaymentCode());
		model.addAttribute("search", map.get("search"));
		model.addAttribute("sum", map.get("sum"));
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
 
		return "/admin/statis/salesNew";
	}
	
	
	/**
	 * 관리자 > 정산관리 > 매출원장(국내) 리스트 - 부피무게 재계산 정산 업데이트 
	 * @param Model
	 * @param AdminStatisData
	 * @param HttpSession
	 * @return page
	 */
	@PostMapping(value = "/salesWeightCalculUpdate")
	public ModelAndView salesWeightCalculUpdate(Model model, @ModelAttribute AdminStatisData statis, HttpSession sess) {
		LOGGER.debug("-----------------------[salesWeightCalculUpdate]-----------------------");
		ModelAndView mav = new ModelAndView("jsonView");
		LOGGER.debug(">>statis:" + statis);
		LOGGER.debug(">>masterCodeList:" + statis.getMasterCodeList());
		LOGGER.debug(">>volumeWeight:" + statis.getVolumeWeightList());
		LOGGER.debug(">>volumeCheckList:" + statis.getVolumeCheckList());
		int result = 0;
		String tempStr = statis.getVolumeCheckList();
		if(tempStr != null) {
			String[] tempArr = tempStr.split(",");
			for(String check : tempArr) {
				String masterCode = "";
				String weight = "";
				String[] tt = check.split("@");
				if(tt.length >= 2)
				{
					masterCode = tt[0];
					weight = tt[1];	
				}
				else
				{
					result = -1;
					LOGGER.debug("--ERROR--");
					break;
				}
				
				AdminStatisMakeData inParam = new AdminStatisMakeData();
				inParam.setMasterCode(masterCode);
				inParam.setWeight(weight);
				// 0 값이 중량으로 넘어오면 삭제 처리 한다. 
				if("0".equals(weight)) {
					try {
						LOGGER.debug("--------DELETE START---------");
						adminStatisService.deleteStatisAddPayment(inParam);			// tb_add_payment
						LOGGER.debug("<STEP2> deleteStatisAddPayment");
						adminStatisService.deleteStatisSales(inParam);							// tb_statis_sales
						LOGGER.debug("<STEP3> deleteStatisSales");
						adminStatisService.insertStatisSalesNew(inParam);					// tb_statis_sales
						LOGGER.debug("<STEP4> insertStatisSalesNew");
						LOGGER.debug("--------DELETE END---------");
					}catch(Exception e) {
						e.printStackTrace();
						result = -1;
					}
				}
				else {
					double dweight = Double.parseDouble(weight)* 1000;
					DecimalFormat form = new DecimalFormat("#"); // 소수점 #.###
					String  sweight = form.format(dweight);
					LOGGER.debug(">masterCode:" + masterCode);
					LOGGER.debug(">weight:" + weight);
					LOGGER.debug(">d weight:" + dweight);
					LOGGER.debug(">String weight:" + sweight);
					
					inParam.setVolumeWeight(sweight);
					
					try {
						AdminStatisMakeData ret = adminStatisService.selectStatisAddSalePrice(inParam);
						LOGGER.debug("--------calculAddPrice START---------");
						LOGGER.debug(">masterCode:" + masterCode);
						if(ret != null)
						{
							int addFeesPrice = 0;
							int addSalePrice = 0;
							addFeesPrice =ret.getAddFeesPrice();
							addSalePrice = ret.getAddSalePrice();
							LOGGER.debug(">AddFeesPrice:" + addFeesPrice);
							LOGGER.debug(">AddSalePrice:" + addSalePrice);	
						 
							if(addFeesPrice > 0) {
								LOGGER.debug("----- [1] AddFeesPrice Process--------[START]" +addFeesPrice );
								try {
									adminStatisService.deleteStatisAddPayment(inParam);			// tb_add_payment (중복 재계산 초기화)		
									// insertAddFeesVolumePayment
									inParam.setCourierCompany(ret.getCourierCompany());
									inParam.setCourier(ret.getCourier());
									inParam.setAddFeesPrice(addFeesPrice);
									inParam.setPaymentDate(ret.getPaymentDate());
									adminStatisService.insertAddFeesVolumePayment(inParam);							// tb_add_payment
									LOGGER.debug("<STEP2> insertAddFeesVolumePayment");
																																												// insertAddSaleVolumePayment
									inParam.setAddSalePrice(addSalePrice);
									adminStatisService.insertAddSaleVolumePayment(inParam);							// tb_add_payment
									LOGGER.debug("<STEP2> insertAddSaleVolumePayment");
									// deleteStatisSales
									adminStatisService.deleteStatisSales(inParam);														// tb_statis_sales
									LOGGER.debug("<STEP3> deleteStatisSales");
									// insertStatisSalesNew
									adminStatisService.insertStatisSalesNew(inParam);												// tb_statis_sales
									LOGGER.debug("<STEP4> insertStatisSalesNew");
								}catch(Exception e) {
									e.printStackTrace();
									result = -1;
								}
								LOGGER.debug("----- [1] AddFeesPrice Process--------[END]");
							}
						 
							
						}
						else
							LOGGER.debug("NO DATA");
						
						LOGGER.debug("--------calculAddPrice END---------");
					}catch(Exception e) {
						result = -1;
						e.printStackTrace();
					} // end catch 
				} // end else 
				// 중량 업데이트 
				adminStatisService.updateWeight(inParam);
				
			}// end for
		}// end if
		
 		
		if(result < 0 )
		 model.addAttribute("errCode", null);
 
		 
		return mav;
	}
	
	
	
	/**
	 * 관리자 > 정산관리 > 매출원장(국내) 리스트 - 정산 업데이트 
	 * @param Model
	 * @param AdminStatisData
	 * @param HttpSession
	 * @return page
	 */
	@PostMapping(value = "/salesCalculUpdate")
	public ModelAndView salesCalculUpdate(Model model, @ModelAttribute AdminStatisData statis, HttpSession sess) {
		LOGGER.debug("-----------------------[salesCalculUpdate]-----------------------");
		ModelAndView mav = new ModelAndView("jsonView");
		LOGGER.debug(">>statis:" + statis);
		LOGGER.debug(">>calcultype:"+ statis.getCalculType());
		LOGGER.debug(">>masterCodeList:" + statis.getMasterCodeList());
		String mList = statis.getMasterCodeList();
		String[] arr = mList.split(",");
		for(String a : arr)
		{
			LOGGER.debug(">masterCode:" + a);
		}
		statis.setArrMasterCodeList(arr);
		int ret = 0;
		try {
			adminStatisService.updateCalcul(statis);
		}catch(Exception e) {
			e.printStackTrace();
			ret = -1;
		}
		if(ret < 0 )
		 model.addAttribute("errCode", null);
		return mav;
	}
	
	/**
	 * 관리자 > 정산관리 > 매출원장(국내) 리스트
	 * @param Model
	 * @param AdminStatisData
	 * @param HttpSession
	 * @return page
	 */
	@GetMapping(value = "/salesLocal")
	public String salesLocalList(Model model, @ModelAttribute AdminStatisData statis, HttpSession sess) {		
		
		statis.setSearchPaymentCode("ND");
		
		Map<String, Object> map = adminStatisService.selectStatisSales(statis, sess, true);
		
		model.addAttribute("mode", statis.getSearchPaymentCode());
		model.addAttribute("search", map.get("search"));
		model.addAttribute("sum", map.get("sum"));
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		return "/admin/statis/sales";
	}
	
	/**
	 * 관리자 > 정산관리 > 매출원장(추가요금) 리스트
	 * @param Model
	 * @param AdminStatisData
	 * @param HttpSession
	 * @return page
	 */
	@GetMapping(value = "/salesCharge")
	public String salesChargeList(Model model, @ModelAttribute AdminStatisData statis, HttpSession sess) {		
		
		statis.setSearchPaymentCode("EW");
		
		Map<String, Object> map = adminStatisService.selectStatisSales(statis, sess, false);
		
		model.addAttribute("mode", statis.getSearchPaymentCode());
		model.addAttribute("search", map.get("search"));
		model.addAttribute("sum", map.get("sum"));
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		return "/admin/statis/sales";
	}
	
	/**
	 * 관리자 > 정산관리 > 매출원장(추가요금) 리스트 - New
	 * @param Model
	 * @param AdminStatisData
	 * @param HttpSession
	 * @return page
	 */
	@GetMapping(value = "/salesChargeNew")
	public String salesChargeListNew(Model model, @ModelAttribute AdminStatisData statis, HttpSession sess) {		
		LOGGER.debug("-----------------------[salesChargeNew]-----------------------");
		statis.setSearchPaymentCode("EW");
		//statis.setSearchPaymentCode("NA");
		// 날짜 매달 1~30일인데 현재 날짜 기준으로 과거 30일로 검색 기간 조정 
		String searchDateStart     = statis.getSearchDateStart();
		String searchDateEnd       = statis.getSearchDateEnd();
		if (true) {
				// 검색기간 정보가 비어있을 경우, 지난 일주일의 데이터를 출력한다.
				int nPrevDays = 30 ;
				String sDateFormat = "YYYY-MM-dd" ;
				// 종료일자 : 오늘
				if (searchDateEnd == null || "".equals(searchDateEnd)) {
					searchDateEnd = util.getToday(sDateFormat) ;
					statis.setSearchDateEnd(searchDateEnd) ;
				}
				// 시작일자 : 30일전
				if (searchDateStart == null || "".equals(searchDateStart)) {
					searchDateStart = util.getPrevDate(nPrevDays, sDateFormat) ; 
					statis.setSearchDateStart(searchDateStart) ;
				}
		}    
		Map<String, Object> map = adminStatisService.selectStatisSales(statis, sess, false);
		
		model.addAttribute("mode", statis.getSearchPaymentCode());
		model.addAttribute("search", map.get("search"));
		model.addAttribute("sum", map.get("sum"));
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		
		return "/admin/statis/salesNew";
	}
	
	/**
	 *    요약 팝업 상세보기
	 */
	@GetMapping(value = "/popup/salesReport")
	public String salesReport(Model model, @ModelAttribute AdminStatisData statis, HttpSession sess) {		
		LOGGER.debug("-----------------------[salesReport]-----------------------");
		Map<String, Object> map = adminStatisService.selectStatisSalesReport(statis, sess); 
		model.addAttribute("list", map.get("list"));
		return "/admin/statis/popup/salesReport";
	}	
	
	/**
	 *    추가 요금 팝업 상세보기 : 부피중량 외에 cs에서 반송/교환/반품의 케이스시 상세 내용을 보기 위함 
	 */
	@GetMapping(value = "/popup/salesChargePopup")
	public String salesChargePopup(Model model, @ModelAttribute AdminStatisRowData statis, HttpSession sess) {		
		LOGGER.debug("-----------------------[salesChargePopup]-----------------------");
		LOGGER.debug(">masterCode:" + statis.getMasterCode());
		LOGGER.debug(">paymentCode:" + statis.getPaymentCode());
		Map<String, Object> map = adminStatisService.selectStatisAddPriceDesc(statis, sess); 
		model.addAttribute("list", map.get("list"));
		model.addAttribute("mode", statis.getPaymentCode());
		return "/admin/statis/popup/salesChargePopup";
	}	
	
	/**
	 *    부피 중량 파일 업로드 
	 */
	@GetMapping(value = "/popup/salesVolumeWeight")
	public String salesVolumeWeight(Model model, @ModelAttribute AdminStatisRowData statis, HttpSession sess) {		
		LOGGER.debug("-----------------------[salesVolumeWeight]-----------------------");
 
		return "/admin/statis/popup/salesVolumeWeight";
	}
	
	
	/**
	 * 관리자 > 정산관리 > 손익통계 리스트
	 * @param Model
	 * @param AdminStatisData
	 * @param HttpSession
	 * @return page
	 * @throws ParseException 
	 */
	@GetMapping(value = "/payment")
	public String paymentList(Model model, @ModelAttribute AdminStatisData statis, HttpSession sess) throws ParseException {
		
		statis.setSearchDatetype("month");
		
		Map<String, Object> map = adminStatisService.selectStatisPayment(statis, sess);
		
		model.addAttribute("search", map.get("search"));
		model.addAttribute("list", map.get("list"));
		
		return "/admin/statis/paymentNew";
	}
	
	/**
	 * 관리자 > 정산관리 > 월별통계 리스트
	 * @param Model
	 * @param AdminStatisTotalData
	 * @param HttpSession
	 * @return page
	 * @throws ParseException 
	 */
	@GetMapping(value = "/statistics")
	public String statisticsList(Model model, @ModelAttribute AdminStatisTotalData statis, HttpSession sess) throws ParseException {
		
		statis.setSearchDatetype("month");
		
		Map<String, Object> map = adminStatisService.selectStatisSalesTotal(statis, sess);
		
		model.addAttribute("search", map.get("search"));
		model.addAttribute("list", map.get("list"));
		
		return "/admin/statis/statisticsNew";
	}
	
	/**
	 * 관리자 > 정산관리 > 일별통계 리스트
	 * @param Model
	 * @param AdminStatisTotalData
	 * @param HttpSession
	 * @return page
	 * @throws ParseException 
	 */
	@GetMapping(value = "/statisticsDaily")
	public String statisticsDailyList(Model model, @ModelAttribute AdminStatisTotalData statis, HttpSession sess) throws ParseException {
		
		statis.setSearchDatetype("day");
		
		Map<String, Object> map = adminStatisService.selectStatisSalesTotal(statis, sess);
		
		model.addAttribute("search", map.get("search"));
		model.addAttribute("list", map.get("list"));
		
		return "/admin/statis/statistics";
	}
}
