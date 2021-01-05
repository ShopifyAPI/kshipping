package com.shopify.tracking;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.shop.ShopData;

@Controller
public class TrackingController {
	private Logger LOGGER = LoggerFactory.getLogger(TrackingController.class);
	
	@Autowired 
	private TrackingService trackingService;
	
	@Autowired	
	private UtilFn util;
	
	/**
	 * 배송 > 배송목록
	 * @param model
	 * @return
	 */
	@SuppressWarnings("null")
	@GetMapping("/tracking")
	public String tracking(@ModelAttribute TrackingData sData, Model model, HttpSession sess) {
	    ShopData shop = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
	    String searchDateStart = sData.getSearchDateStart();
	    String searchDateEnd = sData.getSearchDateEnd();
	    int currentPage = sData.getCurrentPage();
	    int pageSize = sData.getPageSize();
		if (true) {
			// 검색기간 정보가 비어있을 경우, 지난 일주일의 데이터를 출력한다.
			int nPrevDays = 7 ;
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
	    if(currentPage == 0)      currentPage = 1;
	    if(pageSize == 0)          pageSize = 10;
        sData.setCurrentPage(currentPage);
        sData.setPageSize(pageSize);
		Map<String, Object> map = trackingService.selectTrackingList(sData, sess);
		model.addAttribute("search", sData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		model.addAttribute("pageInfo", "shipment");
		return "/tracking/trackingList";
	}
}
