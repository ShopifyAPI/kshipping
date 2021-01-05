package com.shopify.order;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
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

import com.shopify.api.ShopifyApiService;
import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.shop.ShopData;

import io.micrometer.core.instrument.util.StringUtils;



/**
 * 주문 컨트롤러
 *
 */

@Controller
public class OrderController {
	private Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired private OrderService orderService;
	@Autowired private ShopifyApiService spApiService;
	@Autowired private UtilFn util;
	

	/**
     * 주문 > 주문목록
     * @param model
     * @return
     */
    @GetMapping(value = "/order")
    public String orderList(Model model, @ModelAttribute OrderData order, HttpSession sess, HttpServletRequest req) {
        ShopData shop = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
        Map<String,Object> apiMap = new HashMap<String, Object>();
		
        
        String searchDestType      = order.getSearchDestType();
		if ( StringUtils.isBlank(searchDestType) ) {
			order.setSearchDestType("all");
		}
        // 주문목록 조회전..api를통하여 목록 갱신처리..
        if (order.getLoadCheck() != null && "Y".equals(order.getLoadCheck())) {
        	LOGGER.debug("orderList [LoadCheck=Y] : shopifyGetOrder Start");
        	apiMap = spApiService.shopifyAllGetOrder(req, sess); // apiMap = orderService.shopifyGetOrder(model, req, sess);
        }

		if (true) {
			// 검색기간 정보가 비어있을 경우, 지난 일주일의 데이터를 출력한다.
			int nPrevDays = 30 ;
			String sDateFormat = "YYYY-MM-dd" ;
		    String searchDateStart = order.getSearchDateStart();
		    String searchDateEnd = order.getSearchDateEnd();
			// 종료일자 : 오늘
			if (searchDateEnd == null || "".equals(searchDateEnd)) {
				searchDateEnd = util.getToday(sDateFormat) ;
				order.setSearchDateEnd(searchDateEnd) ;
			}
			// 시작일자 : 7일전
			if (searchDateStart == null || "".equals(searchDateStart)) {
				searchDateStart = util.getPrevDate(nPrevDays, sDateFormat) ; 
				order.setSearchDateStart(searchDateStart) ;
			}
		}
		
        int currentPage = order.getCurrentPage();
        if(currentPage == 0) order.setCurrentPage(1);
        int pageSize = order.getPageSize();
        if(pageSize == 0) order.setPageSize(10);
        order.setEmail(shop.getEmail());

        Map<String, Object> map = orderService.orderList(order, model, sess) ;

        model.addAttribute("search", order);
        model.addAttribute("list", map.get("list"));
        model.addAttribute("paging", map.get("paging"));
        model.addAttribute("status", "OK");
        model.addAttribute("statusApi", apiMap.get("status"));
        model.addAttribute("pageInfo", "order");

        return "/order/orderList";
    }
    
    
    @SuppressWarnings("null")
    @GetMapping(value = "/orderSession")
    public String orderSession(Model model , @ModelAttribute OrderData order , HttpSession sess, HttpServletRequest req
    		) {
    	ShopData shop = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
    	
    	long lastAccessedTime = sess.getLastAccessedTime();   // : returns the time session was last accessed
    	int maxInactiveInterval = sess.getMaxInactiveInterval();  // : returns the timeout period.
    	
    	
    	Date d = new Date(lastAccessedTime);
    	System.out.println("id = " + sess.getId());
    	System.out.println("last access = " + d.toString());
    	System.out.println("max Inactive = " + maxInactiveInterval);
    	
    	Enumeration keys = sess.getAttributeNames();
    	while (keys.hasMoreElements())
    	{
    	  String key = (String)keys.nextElement();
    	  System.out.println(key + ": " + sess.getValue(key) + "<br>");
    	}
    	
    	return "/order/orderSession";
    }
	
    /**
     * 주문 > 주문삭제
     * @param model
     * @return
     */
	@PostMapping(value="/order/delete")
    public ModelAndView orderDelete(Model model, @RequestBody OrderData order, HttpSession sess) throws Exception {
    	
		LOGGER.debug("order : " + order.toString());
		LOGGER.debug("getShopIdxChk : " + order.getShopIdxChk());
		LOGGER.debug("getOrderIdxChk : " + order.getOrderIdxChk());
		
		int result = orderService.deleteOrder(order,sess);
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
    }
	
	/**
     * 주문 > 주문목록 > 엑셀다운
     * @param model
     * @return
     */
	@SuppressWarnings("null")
    @GetMapping("/order/orderExcel")
	public String orderExceldown(Model model, @ModelAttribute OrderData order, HttpSession sess, HttpServletRequest req
        ) {
	    ShopData shop = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
        //주문목록 조회전..api를통하여 목록 갱신처리..
        //Map<String,Object> apiMap = orderService.shopifyGetOrder(model, req, sess);

        order.setCurrentPage(0);
        order.setEmail(shop.getEmail());

        Map<String, Object> map = orderService.orderList(order, model, sess);
        model.addAttribute("search", order);
        model.addAttribute("list", map.get("list"));
        model.addAttribute("paging", map.get("paging"));
        model.addAttribute("status", "OK");
        //model.addAttribute("statusApi", apiMap.get("status"));
        model.addAttribute("pageInfo", "order");

        return "/order/orderExcel";
    }
	
	/**
	 * 주문 > 해외특송 > 저장
	 * @return ModelAndView(jsonView)
	 */
    @PostMapping(value="/order/orderCreateCustomsProc")
    public ModelAndView orderCreateCustomsProc(Model model, HttpSession sess
            ,@RequestBody OrderData order
    	) {
            
        ModelAndView mav = new ModelAndView("jsonView");
        
        LOGGER.debug("order : " + order.toString());
        
        int chk = orderService.updateShimentList(order);
        
        if(chk == 0) {
            model.addAttribute("result", chk);
            model.addAttribute("errCode", false);
            model.addAttribute("errMsg", "실패");
        } else {
            model.addAttribute("result", chk);
            model.addAttribute("errCode", true);
            model.addAttribute("errMsg", "성공");
        }

        return mav;
    }

}