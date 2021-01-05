package com.shopify.admin.price;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.admin.AdminData;
import com.shopify.admin.delivery.DeliveryData;
import com.shopify.common.SpConstants;
import com.shopify.mapper.PriceMapper;

@Controller
@SpringBootApplication

/**
 * 요금 맵핑 관리 컨트롤러
 *
 */
public class PriceController {
	
	@Autowired
	private PriceMapper priceMapper;
	@Autowired
	private PriceService priceService;
	
	private Logger LOGGER = LoggerFactory.getLogger(PriceController.class);
	
	/**
	 * 특송요금관리 > 요금맵핑관리 VIew 페이지
	 * (EMS , 공시요금)
	 * @return
	 */
	@GetMapping(value = "/admin/price/feesPriceMappingList")
	public String feesPriceMappingList(Model model, HttpSession sess, PriceData price) {
		
		Map<String, Object> map = priceService.feesPriceMappingList(price,sess);

		model.addAttribute("headerList", map.get("headerList"));
		model.addAttribute("dataList", map.get("dataList"));
		model.addAttribute("weightList", map.get("weightList"));
		model.addAttribute("partShipCompany", map.get("partShipCompany"));
		model.addAttribute("partDeliveryService", map.get("partDeliveryService"));
		model.addAttribute("nowDate", map.get("nowDate"));
		model.addAttribute("gbn", "fees");
		
		return "admin/price/adminPriceMappingList";
		
	}
	
	/**
	 * 특송요금관리 > 요금맵핑관리 VIew 페이지
	 * (EMS , 공시요금)
	 * @return
	 */
	@GetMapping(value = "/admin/price/salePriceMappingList")
	public String salePriceMappingList(Model model, HttpSession sess, PriceData price) {
		
		Map<String, Object> map = priceService.salePriceMappingList(price,sess);

		model.addAttribute("headerList", map.get("headerList"));
		model.addAttribute("dataList", map.get("dataList"));
		model.addAttribute("weightList", map.get("weightList"));
		model.addAttribute("partShipCompany", map.get("partShipCompany"));
		model.addAttribute("partDeliveryService", map.get("partDeliveryService"));
		model.addAttribute("nowDate", map.get("nowDate"));
		model.addAttribute("gbn", "sale");
		
		return "admin/price/adminPriceMappingList";
		
	}
	
	/**
	 * price > 배송서비스 
	 * @return
	 */
	@PostMapping("/admin/price/selectShipService")
	public ModelAndView selectShipServiceList(Model model, @RequestBody PriceData priceData ,HttpSession sess) throws Exception {
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		LOGGER.debug("priceData : " + priceData.toString());
		
		List<PriceData> list =  priceService.selectShipServiceList(priceData);
		
		mv.addObject("list", list);
		
		return mv;
	}
	
	
	/**
	 * 특송요금관리 > 요금맵핑관리 VIew 페이지 > 요금 조회 (공시/매입요금)
	 * (EMS , 공시요금)
	 * @return
	 */
	@PostMapping(value = "/admin/price/priceSearch")
	public ModelAndView salePriceTest(Model model, HttpSession sess, @RequestBody PriceData price) {
		LOGGER.debug("---------------TEST-----------------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		
		LOGGER.debug("shipCompany:" + price.getShipCompany());
		LOGGER.debug("code:" + price.getCode());
		LOGGER.debug("codeGroup:" + price.getCodeGroup());
		LOGGER.debug("country:" + price.getCountry());
		LOGGER.debug("weight:" + price.getWeight());
		
		PriceData ret = priceService.selectPrice(price);
		LOGGER.debug("feesPrice:" + ret.getFeesPrice());
		LOGGER.debug("salesPrice:" + ret.getSalesPrice());
		
		mv.addObject("price", ret);
		
		return mv;
		
	}
}
