package com.shopify.payment;

import java.util.Map;

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
import org.springframework.web.servlet.ModelAndView;

import com.shopify.common.util.UtilFn;
import com.shopify.shipment.ShipmentData;

import io.micrometer.core.instrument.util.StringUtils;

@Controller
public class PaymentController {
	private Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);
	
	@Autowired 
	private PaymentService payservice;
	
	@Autowired	
	private UtilFn util;
	
	/**
	 * 배송 > 배송목록
	 * @param model
	 * @return
	 */
	@GetMapping("/payment")
	public String payment(@ModelAttribute ShipmentData sData, Model model, HttpSession sess) {

		String searchDestType      = sData.getSearchDestType();
		if ( StringUtils.isBlank(searchDestType) ) {
			 sData.setSearchDestType("all");
		}
	    String searchDateStart     = sData.getSearchDateStart();
	    String searchDateEnd       = sData.getSearchDateEnd();
	    int currentPage               = sData.getCurrentPage();
	    int pageSize                   = sData.getPageSize();
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
	    if(currentPage == 0)      currentPage = 1;
	    if(pageSize == 0)          pageSize = 10;
        sData.setCurrentPage(currentPage);
        sData.setPageSize(pageSize);
		Map<String, Object> map = payservice.selectShimentList(sData, sess);
		model.addAttribute("search", sData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		model.addAttribute("pageInfo", "payment");
		return "/payment/paymentList";
	}
	
	/**
     * 배송 > 배송목록 엑셀다운
     * @param model
     * @return
     */
    @GetMapping("/payment/paymentExcel")
    public String paymentExcel(@ModelAttribute ShipmentData sData, Model model, HttpSession sess) {

		String searchDestType      = sData.getSearchDestType();
		if ( StringUtils.isBlank(searchDestType) ) {
			 sData.setSearchDestType("all");
		}
		
        String searchDateStart     = sData.getSearchDateStart();
        String searchDateEnd       = sData.getSearchDateEnd();
        int currentPage            = 0; // 리스트 전체 
        int pageSize               = sData.getPageSize();
		if (true) {
			// 검색기간 정보가 비어있을 경우, 지난 일주일의 데이터를 출력한다.
			int nPrevDays = 30 ;
			String sDateFormat = "YYYY-MM-dd" ;
			// 종료일자 : 오늘
			if (searchDateEnd == null || "".equals(searchDateEnd)) {
				searchDateEnd = util.getToday(sDateFormat) ;
				sData.setSearchDateEnd(searchDateEnd) ;
			}
			// 시작일자 : 7일전
			if (searchDateStart == null || "".equals(searchDateStart)) {
				searchDateStart = util.getPrevDate(nPrevDays, sDateFormat) ; 
				sData.setSearchDateStart(searchDateStart) ;
			}
		}
        sData.setCurrentPage(currentPage);
        sData.setPageSize(pageSize);
        Map<String, Object> map = payservice.selectShimentList(sData, sess);
        model.addAttribute("list", map.get("list"));
        return "/payment/paymentExcel";
    }
	
	//삭제
	@PostMapping("/payment/deleteMultiPayment")
    public ModelAndView paymentDeleteMultiPayment(@RequestBody ShipmentData ship, HttpSession sess ) throws Exception {
	    ModelAndView mv = new ModelAndView("jsonView");
	    ship.setMasterCode(ship.getChkmasterCode());
	    int cnt = payservice.deletePayment(ship, sess);
        mv.addObject("status", cnt);
        return mv;
    }
}