package com.shopify.admin.delivery;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SpringBootApplication

/**
 * 공통 컨트롤러
 *
 */
public class DeliveryController {
	
	private Logger LOGGER = LoggerFactory.getLogger(DeliveryController.class);
	
	@Autowired
	private DeliveryService deliveryService;
	@Autowired
    private MessageSource messageSource;
    @Autowired
    LocaleResolver localeResolver;
	
	/**
	 * delivery > 배송사 view 페이지
	 * @return
	 */
	@SuppressWarnings("null")
	@GetMapping(value="/admin/delivery/listShipCompany")
	public String listShipCompany(Model model , @ModelAttribute DeliveryData deliveryData , HttpSession sess) throws Exception {
		
		int currentPage = deliveryData.getCurrentPage();
	    int pageSize = deliveryData.getPageSize();
		
		if(currentPage == 0) currentPage = 1;
	    if(pageSize == 0) pageSize = 40;
		
		deliveryData.setCurrentPage(currentPage);
		deliveryData.setPageSize(pageSize);
		
		Map<String, Object> map = deliveryService.listShipCompany(deliveryData,sess);
		
		model.addAttribute("search", deliveryData);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));

		return "admin/delivery/shipCompanyList";
		
	}
	
	/**
	 * delivery > 배송서비스 
	 * @return
	 */
	@PostMapping("/admin/delivery/selectShipService")
	public ModelAndView selectShipService(Model model, @RequestBody DeliveryData deliveryData ,HttpSession sess) throws Exception {
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		List<DeliveryData> list =  deliveryService.selectShipService(deliveryData);
		
		mv.addObject("list", list);
		
		return mv;
	}
	
	/**
	 * delivery > 배송사 insert
	 * @return
	 */
	@PostMapping("/admin/delivery/insertShipCompany")
	public ModelAndView insertShipCompany(Locale locale , @RequestBody DeliveryData deliveryData ,HttpSession sess) throws Exception {
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		int result = deliveryService.insertShipCompany(deliveryData,sess);
		
		LOGGER.debug("insertShipCompany [result] : " + result);
		
		if(result == -1) {
			String[] args = {"id", "code"};
			String msg = messageSource.getMessage("error.duplicate2",args,locale);
			mv.addObject("result", result);
			mv.addObject("errCode", false);
			mv.addObject("errMsg", msg);
			
		}else {
			mv.addObject("result", result);
			mv.addObject("errCode", true);
			mv.addObject("errMsg", "성공");
		}
		
		return mv;
	}
		
	/**
	 * 관리자 > 배송사 Popup > 배송비 사용 여부 
	 * @param Model
	 * @param DeliveryData
	 * @param HttpSession
	 * @return jsonView
	 */
	@PostMapping("/admin/delivery/editShipServiceUseYn")
	public ModelAndView editShipServiceUseYn(@RequestBody DeliveryData deliveryData, HttpSession sess) {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		LOGGER.debug("editShipServiceUseYn : " + deliveryData.toString());
		
		LOGGER.debug("editShipServiceUseYn [useYn1] : " + deliveryData.getUseYn());
		
		String useYn = ("Y".equals(deliveryData.getUseYn())) ? "N" : "Y"; // 사용여부 반전
		
		LOGGER.debug("editShipServiceUseYn [useYn2] : " + useYn);
		
		deliveryData.setUseYn(useYn);
		int result = deliveryService.updateShipServiceUseYn(deliveryData);

		mav.addObject("useYn", useYn);
		mav.addObject("result", result);
		mav.addObject("errCode", true);
		mav.addObject("errMsg", "성공");
		
		return mav;
	}

	/**
	 * delivery > 배송사 delete
	 * @return
	 * 여러 Row삭제를 위해 Map으로 처리함
	 */
	@PostMapping("/admin/delivery/deleteShipCompany")
	public ModelAndView deleteShipCompany(Model model, @RequestBody DeliveryData deliveryData ,HttpSession sess) throws Exception {
		    	
		//LOGGER.debug("DeliveryData : " + deliveryData.toString());
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		int result = deliveryService.deleteShipCompany(deliveryData);
		
		mv.addObject("result",result);
		mv.addObject("errCode",true);
		mv.addObject("errMsg","성공");
		
		return mv;
	}
	
}
