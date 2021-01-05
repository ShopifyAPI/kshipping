package com.shopify.admin.popup;

import java.util.List;
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

import com.shopify.admin.delivery.DeliveryData;
import com.shopify.admin.delivery.DeliveryService;
import com.shopify.common.util.UtilFn;

/**
* 관리자 > 배송사 Popup 컨트롤러
* @author : jwh
* @since  : 2019-12-26
* @desc   : /setting 정보 
*/
@Controller
public class DeliveryPopupController {
	
	private Logger LOGGER = LoggerFactory.getLogger(DeliveryPopupController.class);
	
	@Autowired private DeliveryService deliveryService;
	@Autowired private UtilFn util; 
	
	/**
	 * 관리자 > 배송사 Popup > 배송사 등록
	 * @param Model
	 * @param HttpSession
	 * @return page
	 */
	@GetMapping("/admin/delivery/popup/newShipCompany")
	public String newShipCompany(Model model, HttpSession sess) throws Exception {
		
		return "admin/popup/popShipCompanyNew";
	}
	
	/**
	 * 관리자 > 배송사 Popup > 배송사별 배송비 리스트 배송비 
	 * @param Model
	 * @param DeliveryData
	 * @param HttpSession
	 * @return jsonView
	 */
	@PostMapping("/admin/delivery/popup/newShipService")
	public ModelAndView newShipService(@RequestBody DeliveryData deliveryData, HttpSession sess) throws Exception {
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		List<DeliveryData> delivery = deliveryService.selectDeliveryCompanyService(deliveryData);

		mv.addObject("delivery", delivery);
		
		return mv;
	}
	
	/**
	 * 관리자 > 배송사 Popup > 수정 
	 * @param Model
	 * @param DeliveryData
	 * @param HttpSession
	 * @return jsonView
	 */
	@GetMapping("/admin/delivery/popup/editShipCompany")
	public String editShipCompany(Model model, @ModelAttribute DeliveryData deliveryData, HttpSession sess) throws Exception {
		
		DeliveryData delivery = deliveryService.selectDeliveryCompanyServiceView(deliveryData);
		
		model.addAttribute("delivery", delivery);

		return "admin/popup/popShipCompanyNew";
	}
	
	/**
	 * 관리자 > 배송사 Popup > 수정 
	 * @param Model
	 * @param DeliveryData
	 * @param HttpSession
	 * @return jsonView
	 */
	@PostMapping("/admin/delivery/editShipCompanyProc")
	public ModelAndView editShipCompanyProc(Model model, @RequestBody DeliveryData deliveryData, HttpSession sess) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		int result = deliveryService.updateShipCompany(deliveryData);
		
		mav.addObject("result", result);
		mav.addObject("errCode", true);
		mav.addObject("errMsg", "성공");
		
		return mav;
	}
}
