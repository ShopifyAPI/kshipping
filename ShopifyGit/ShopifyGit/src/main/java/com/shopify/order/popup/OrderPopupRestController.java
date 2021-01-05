package com.shopify.order.popup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFunc;
import com.shopify.shipment.ShipmentData;
import com.shopify.shop.ShopData;


@RestController
@RequestMapping("/order/popup")
public class OrderPopupRestController {
    private Logger LOGGER = LoggerFactory.getLogger(OrderPopupRestController.class);
    
    @Autowired private OrderPopupService orderPopupService;
    @Autowired private OrderPopupRestService orderPopupRestService;
    

    @PostMapping(value="/popOrderCheckCustoms", produces=MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView popOrderCheckCustoms(@RequestBody HashMap bodyMap, Model model, HttpServletRequest req, HttpSession sess) {
    	ModelAndView mav = new ModelAndView("jsonView");
    	
    	ShipmentData setting = new ShipmentData();
    	
    	setting.setOrderCode( (String) bodyMap.get("code"));
    	setting.setShopIdx(Integer.parseInt((String)bodyMap.get("idx")));
    	Map<String, Object> map = orderPopupService.checkAddressAndBox(setting, sess);
    	
//    	LOGGER.debug("sleep start ");
//    	try {
//			Thread.sleep(3 * 1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	LOGGER.debug("sleep end ");
    	
    	 model.addAttribute("msgCode", map.get("msgCode"));
     
        return mav;
    }
    
    @PostMapping(value="/getHscode", produces=MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView getHscode(@RequestBody List<Map<String, Object>> mapList, Model model, HttpServletRequest request, HttpSession sess) {
    	ModelAndView mav = new ModelAndView("jsonView");
    	
    	List<Map<String, Object>> resultMap = orderPopupRestService.getHscode(mapList);
    	
    	for( Map<String, Object> map : resultMap ) {
    		String id = (String) map.get("id");
    		String goodsCode = (String) map.get("goodsCode");
    		String hscode =  (String) map.get("hscode");
    		
    		LOGGER.debug("id = " + id + ", goodsCode = " + goodsCode + ", hscode = " + hscode);
    	}
    	
    	 model.addAttribute("mapList", resultMap);
        return mav;
    }

    
    @PostMapping(value="/selectVolumeticCourierList", produces=MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView selectVolumeticCourierList(@RequestBody Map<String, Object> input, Model model, HttpServletRequest request, HttpSession sess) {
    	ModelAndView mav = new ModelAndView("jsonView");
    	
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		String locale = sd.getLocale();
		String buyerCountryCode = (String)input.get("buyerCountryCode");
		String totalWeight = (String)input.get("totalWeight");
		String boxLength = (String)input.get("boxLength");
		String boxWidth = (String)input.get("boxWidth");
		String boxHeight = (String)input.get("boxHeight");
    	
		List<OrderCourierDataWrapper> list = orderPopupRestService.selectVolumeticCourierList(email, buyerCountryCode, locale, totalWeight, boxLength, boxWidth, boxHeight);
    		
    	model.addAttribute("list", list);
    	return mav;
    }
    
    
   

}