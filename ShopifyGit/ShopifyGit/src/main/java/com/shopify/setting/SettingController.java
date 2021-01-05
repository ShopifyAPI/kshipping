package com.shopify.setting;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.order.popup.OrderCourierData;
import com.shopify.shipment.ShipmentData;

/**
* Setting Controller
* @author : jwh
* @since  : 2019-12-26
* @desc   : /setting 정보 
*/

@Controller
@RequestMapping("/setting")
public class SettingController {
	
	private Logger LOGGER = LoggerFactory.getLogger(SettingController.class);
	
	@Autowired 
	private SettingService settingService;
	
	/**
	 * 설정 > 계정 관리 View 페이지
	 * @param Model
	 * @param SettingData
	 * @param SettingShopData
	 * @param HttpSession
	 * @return page
	 */
	@GetMapping(value = "/seller")
	public String seller(Model model, SettingData setting, SettingShopData settingShop, HttpSession sess) {		
		List<SettingShopData> list = settingService.selectShop(settingShop, sess);
		SettingData seller = settingService.selectSeller(setting, sess);

		model.addAttribute("seller", seller);
		model.addAttribute("list", list);
		
		return "/setting/seller";
	}
	
	/**
	 * 설정 > 계정 관리 > 쇼핑몰 삭제
	 * @return
	 */
	@PostMapping(value = "/deleteMultiShop")
	public ModelAndView deleteMultiShop(@RequestBody SettingShopData setting, HttpSession sess) throws Exception {
		int result = settingService.deleteMultiShop(setting, sess);
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
	/**
	 * 설정 > 계정 관리 > 쇼핑몰 연동 수정
	 * @return
	 */
	@PostMapping(value = "/editUseShop")
	public ModelAndView editUseShop(@RequestBody SettingShopData setting, HttpSession sess) throws Exception {
		int result = settingService.updateUseShop(setting, sess);
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		mv.addObject("useYn", setting.getUseYn());
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
	
	
	/**
	 * 설정 > 계정 관리 View 데이터
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/editSeller")
	public ModelAndView editSeller(@RequestBody SettingData setting, HttpSession sess) throws Exception {
		int result = settingService.updateSeller(setting, sess);
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
	/**
	 * 설정 > 배송관리 > 출고지 View
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/listSender")
	public String settingSender(Model model, SettingSenderData setting, HttpSession sess) {
		List<SettingSenderData> list = settingService.selectSender(setting, sess);
		model.addAttribute("list", list);
		
		return "/setting/senderList";
	}

	
	/**
	 * 설정 > 배송관리 > 출고지 insert
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/insertSender")
	public ModelAndView insertSender(@RequestBody SettingSenderData setting, HttpSession sess) throws Exception {
		int result = settingService.insertSender(setting, sess);
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		mv.addObject("procCode", "button.insert");
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
	/**
	 * 설정 > 배송관리 > 출고지 update
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/updateSender")
	public ModelAndView updateSender(@RequestBody SettingSenderData setting, HttpSession sess) throws Exception {
		int result = settingService.updateSender(setting, sess);
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		mv.addObject("procCode", "button.edit");
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
	/**
	 * 설정 > 배송관리 > 출고지 default update
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/updateDefaultSender")
	public ModelAndView updateDefaultSender(@RequestBody SettingSenderData setting, HttpSession sess) throws Exception {
		int result = settingService.updateDefaultSender(setting, sess);
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
	/**
	 * 설정 > 배송관리 > 출고지 delete
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/deleteSender")
	public ModelAndView deleteSender(@RequestBody SettingSenderData setting, HttpSession sess) throws Exception {
		int result = settingService.deleteSender(setting, sess);
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		mv.addObject("procCode", "button.delete");
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
	
	/**
	 * 설정 > 배송관리 > 포장재 View
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/listBox")
	public String settingBox(Model model, SettingBoxData setting, HttpSession sess) {
		List<SettingBoxData> list = settingService.selectBox(setting, sess);
		model.addAttribute("list", list);
		
		return "/setting/boxList";
	}
	
	/**
	 * 설정 > 배송관리 > 포장재 insert
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/insertBox")
	public ModelAndView insertBox(@RequestBody SettingBoxData setting, HttpSession sess) throws Exception {
		
		int result = settingService.insertBox(setting, sess);
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		mv.addObject("procCode", "button.insert");
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
	/**
	 * 설정 > 배송관리 > 포장재 update
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/updateBox")
	public ModelAndView updateBox(@RequestBody List<SettingBoxData> list, HttpSession sess) throws Exception {
		
		int result = 0;
		
		for( SettingBoxData setting : list ) {
			result = settingService.updateBox(setting, sess);
		}
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		mv.addObject("procCode", "button.edit");
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
	/**
	 * 설정 > 배송관리 > 포장재 default update
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/updateDefaultBox")
	public ModelAndView updateDefaultBox(@RequestBody SettingBoxData setting, HttpSession sess) throws Exception {
		int reset = settingService.updateResetBox(setting, sess);
		int result = settingService.updateDefaultBox(setting, sess);
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
	/**
	 * 설정 > 배송관리 > 포장재 delete
	 * @param SettingBoxData
	 * @param HttpSession
	 * @return ModelAndView(jsonView)
	 */
	@PostMapping(value = "/deleteBox")
	public ModelAndView deleteBox(@RequestBody  List<SettingBoxData> list, HttpSession sess) throws Exception {

		int result = 0;
		
		for( SettingBoxData setting : list ) {
			result = settingService.deleteBox(setting, sess);
		}
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		mv.addObject("procCode", "button.delete");
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
	/**
	 * 설정 > 배송관리 > 관세정보 View
	 * @param model
	 * @return
	 */
	@SuppressWarnings("null")
	@GetMapping(value = "/listSku")
	public String settingSku(Model model, SettingSkuData setting, HttpSession sess
			, @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage
			, @RequestParam(value = "searchBoxType", required = false, defaultValue = "") String searchBoxType
			, @RequestParam(value = "searchType", required = false, defaultValue = "") String searchType
			, @RequestParam(value = "searchWord", required = false, defaultValue = "") String searchWord
			, @RequestParam(value = "searchProductType", required = false, defaultValue = "") String searchProductType
			, @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
		) {
		
		setting.setCurrentPage(currentPage);
		setting.setSearchBoxType(searchBoxType);
		setting.setSearchType(searchType);
		setting.setSearchWord(searchWord);
		setting.setSearchProductType(searchProductType);
		setting.setPageSize(pageSize);
						
        // 제품 업로드 새로고침 체크
        if (setting.getLoadCheck() != null && "Y".equals(setting.getLoadCheck())) {
        	LOGGER.debug("settingList [LoadCheck=Y] : shopifyGetProduct Start");
        	settingService.getAllProduct(setting, sess); 
        }
        //상품 타입 가져오기
        Map<String, Object> product = settingService.selectProduct(setting, sess);
        
        //상품 List 조회
		Map<String, Object> map = settingService.selectSku(setting, sess);
		model.addAttribute("search", setting);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("paging", map.get("paging"));
		model.addAttribute("productTypeList", product.get("productType"));
		model.addAttribute("boxList", map.get("boxList"));
		return "/setting/skuList";
	}
	
	/**
	 * 설정 > 배송관리 > 관세정보 insert
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/insertSku")
	public ModelAndView insertSku(@RequestBody SettingSkuData setting, HttpSession sess) throws Exception {
		int result = settingService.insertSku(setting, sess);
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		mv.addObject("procCode", "button.insert");
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
	/**
	 * 설정 > 배송관리 > 관세정보 update
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/updateSku")
	public ModelAndView updateSku(@RequestBody SettingSkuData setting, HttpSession sess) throws Exception {
		int result = settingService.updateSku(setting, sess);
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		mv.addObject("procCode", "button.edit");
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
	
	@PostMapping(value = "/updateSkuBox")
	public ModelAndView updateSkuBox(@RequestBody List<SettingSkuData> setting, HttpSession sess) throws Exception {
		
		int result = 0;

		for(int i=0; i<setting.size(); i++ ) {
		result = settingService.updateSkuBox(setting.get(i), sess);
		}
		ModelAndView mv = new ModelAndView("jsonView");
		
		mv.addObject("procCode", "button.edit");
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
		
	
	/**
	 * 설정 > 배송관리 > 포장재 delete
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/deleteMultiSku")
	public ModelAndView deleteMultiSku(@RequestBody SettingSkuData setting, HttpSession sess) throws Exception {
		int result = settingService.deleteMultiSku(setting, sess);
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		mv.addObject("procCode", "button.delete");
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
	
	/**
	 * 설정 > 배송관리 > 출고지 View
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/listCourier")
	public String listCourier(Model model, SettingSenderData setting, HttpSession sess) {
		
		Map<String, Object> map = settingService.selectOrderCustoms(sess, true);

    	model.addAttribute("shopList", map.get("shopList"));
    	model.addAttribute("companyList", map.get("companyList"));
    	model.addAttribute("boxList", map.get("boxList"));
    	
		
		return "/setting/courierList";
	}
	
	
	/**
	 * 설정 > 배송관리 > 출고지 insert
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/updateCourier")
	public ModelAndView updateCourier(@RequestBody List<OrderCourierData> list, HttpSession sess) throws Exception {
		
		int count = list.size();
		int result = settingService.udpateCourier(list, sess);
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		mv.addObject("result", result);
		mv.addObject("procCode", "button.edit");
		
		if ( count == result) {
			mv.addObject("errCode", true);
			mv.addObject("errMsg", "성공");
		} else {
			mv.addObject("errCode", false);
			mv.addObject("errMsg", "실패");
		}
		
		return mv;
	}
}
