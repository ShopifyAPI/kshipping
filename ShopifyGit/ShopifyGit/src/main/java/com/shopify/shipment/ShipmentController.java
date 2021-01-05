package com.shopify.shipment;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.shipment.popup.ShipmentPopupData;
import com.shopify.shop.ShopData;

@Controller
public class ShipmentController {
	private Logger LOGGER = LoggerFactory.getLogger(ShipmentController.class);

	@Autowired 
	private ShipmentService shipmentservice;

	@Autowired	
	private UtilFn util;

	/**
	 * 배송 > 배송목록
	 * @param model
	 * @return
	 */
	@GetMapping("/shipment")
	public String shipment(@ModelAttribute ShipmentData sData, Model model, HttpSession sess) {
		// ShopData shop = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String searchDateStart = sData.getSearchDateStart();
		String searchDateEnd = sData.getSearchDateEnd();
		int currentPage = sData.getCurrentPage();
		int pageSize = sData.getPageSize();
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
		if (currentPage == 0) currentPage = 1 ;
		if (pageSize == 0) pageSize = 10 ;
		sData.setCurrentPage(currentPage);
		sData.setPageSize(pageSize);
		Map<String, Object> map = shipmentservice.selectShimentList(sData, sess);
		model.addAttribute("search", sData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		model.addAttribute("pageInfo", "shipment");
		return "/shipment/shipmentList";
	}

	/**
	 * 배송 > 배송목록 엑셀다운
	 * @param model
	 * @return
	 */
	@GetMapping("/shipment/shipmentExcel")
	public String shipmentExcel(@ModelAttribute ShipmentData sData, Model model, HttpSession sess) {
		// ShopData shop = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String searchDateStart = sData.getSearchDateStart();
		String searchDateEnd = sData.getSearchDateEnd();
		int currentPage = 0; // 리스트 전체 
		int pageSize = sData.getPageSize();
		if (true) {
			// 검색기간 정보가 비어있을 경우, 지난 일주일의 데이터를 출력한다.
			int nPrevDays = 30 ;
			String sDateFormat = "YYYY-MM-dd" ;
			// 종료일자 : 오늘
			if(searchDateEnd == null || "".equals(searchDateEnd)) {
				searchDateEnd = util.getToday(sDateFormat) ;
				sData.setSearchDateEnd(searchDateEnd) ;
			}
			// 시작일자 : 7일전
			if(searchDateStart == null || "".equals(searchDateStart)) {
				searchDateStart = util.getPrevDate(nPrevDays, sDateFormat) ; 
				sData.setSearchDateStart(searchDateStart) ;
			}
		}
		sData.setCurrentPage(currentPage);
		sData.setPageSize(pageSize);
		Map<String, Object> map = shipmentservice.selectShimentList(sData, sess);
		model.addAttribute("list", map.get("list"));
		return "/shipment/shipmentExcel";
	}

	/**
	 * 배송 > 배송목록 엑셀다운 (펌뱅킹)
	 * @param model
	 * @return
	 */
	@GetMapping("/shipment/shipmentExcelBank")
	public String shipmentExcelBank(@ModelAttribute ShipmentData sData, Model model, HttpSession sess) {
		// ShopData shop = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String searchDateStart = sData.getSearchDateStart();
		String searchDateEnd = sData.getSearchDateEnd();
		int currentPage = 0; // 리스트 전체 
		int pageSize = sData.getPageSize();
		// 기본 기간 검색 추가  (매월1일~매월말일)
		if(searchDateStart == null || "".equals(searchDateStart)) {
			searchDateStart = util.getDateElement("yymm") + "-01"; 
			sData.setSearchDateStart(searchDateStart);
		}
		// 기본 기간 검색 추가  (매월1일~매월말일)
		if(searchDateEnd == null || "".equals(searchDateEnd)) {
			searchDateEnd = util.getDateElement("yymm") + "-" + util.getDateElement("ld");
			sData.setSearchDateEnd(searchDateEnd);
		}
		sData.setCurrentPage(currentPage);
		sData.setPageSize(pageSize);
		Map<String, Object> map = shipmentservice.selectShimentListBank(sData, sess);
		model.addAttribute("list", map.get("list"));
		return "/shipment/shipmentExcelBank";
	}

	//삭제
	@PostMapping("/shipment/deleteMultiShipment")
	public ModelAndView shipmentDeleteMultiShipment(@RequestBody ShipmentData ship, HttpSession sess ) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		LOGGER.debug("###############11111111111111aaaaaaaaashipshipaaa=ship=>/"+ship);
		int cnt = shipmentservice.deleteShipment(ship, sess);

		mv.addObject("status", cnt);

		return mv;
	}


	/**
	 * 배송 > 컨펌url 이후 return 데이터 처리
	 * @param model
	 * @return
	 */
	@GetMapping("/shipment/confirm/")
	public String shipmentConfirm(@ModelAttribute ShipmentPopupData ship, Model model, HttpServletRequest req, HttpSession sess) {
		ShopData sessionData = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		ship.setApiId(ship.getCharge_id());
		ship.setEmail(sessionData.getEmail());
		ship.setShopId(sessionData.getShopId());
		LOGGER.debug("###############shipmentConfirm=sessionData=>/"+sessionData);
		 ShipmentPopupData spData = shipmentservice.selectPaymentChangeInfo(ship, req, sess);
		return "redirect:/shipment";
	}
}