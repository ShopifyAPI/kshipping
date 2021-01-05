package com.shopify.setting.popup;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shopify.common.CommonService;
import com.shopify.common.util.UtilFn;
import com.shopify.setting.SettingBoxData;
import com.shopify.setting.SettingSenderData;
import com.shopify.setting.SettingService;
import com.shopify.setting.SettingShopData;
import com.shopify.setting.SettingSkuData;

/**
* Setting Popup Controller
* @author : jwh
* @since  : 2019-01-13
* @desc   : /setting 정보 
*/

@Controller
@RequestMapping("/setting/popup")
public class SettingPopUpController {
	
	private Logger LOGGER = LoggerFactory.getLogger(SettingPopUpController.class);
	
	@Autowired 
	private SettingService settingService;
	
	@Autowired 
	private CommonService commonservice;
	
	@Autowired	
	private UtilFn util; 
	
	
	/**
	 * 설정 > 계정 관리 View 페이지
	 * @return
	 */
	@GetMapping(value = "/popChannl")
	public String channlPop(Model model, HttpSession sess) {

		return "/setting/popup/popChannl";
	}
	
	/**
	 * 설정 > 배송관리 > 출고지 등록
	 * @return
	 */
	@GetMapping(value = "/popAddSender")
	public String popAddSender(Model model, HttpSession sess, HttpServletRequest req
			, @RequestParam(value = "idx", required = false, defaultValue = "0") int idx
			) {

		SettingSenderData setting = new SettingSenderData();
		SettingShopData settingShop = new SettingShopData();
		
		List<SettingShopData> list = settingService.selectShop(settingShop, sess);
		SettingSenderData sender = new SettingSenderData();
		
		// 모드가 수정일 경우 SenderIdx로 검색
		if(idx > 0) {
			setting.setSenderIdx(idx);
			sender = settingService.selectSenderDetail(setting, sess); 
		} 
		
		model.addAttribute("sender", sender);
		model.addAttribute("list", list);
		
		return "/setting/popup/popSenderAdd";
	}
	
	/**
	 * 설정 > 배송관리 > 박스 등록
	 * @return
	 */
	@GetMapping(value = "/popAddBox")
	public String popAddBox(Model model, HttpSession sess, HttpServletRequest req
			, @RequestParam(value = "idx", required = false, defaultValue = "0") int idx
			) {

		SettingBoxData setting = new SettingBoxData();
		SettingShopData settingShop = new SettingShopData();
		
		//LOGGER.debug("idx : " + idx);
		
		List<SettingShopData> list = settingService.selectShop(settingShop, sess); // 쇼핑몰 리스트
		SettingBoxData box = new SettingBoxData();
		
		
		
		// 모드가 수정일 경우 idx로 검색
		if(idx > 0) {
			setting.setBoxIdx(idx);
			box = settingService.selectBoxDetail(setting, sess); 
		} 
		
		model.addAttribute("box", box);
		model.addAttribute("list", list);
		
		return "/setting/popup/popBoxAdd";
	}
	
	/**
	 * 설정 > 배송관리 > 관세 등록
	 * @return
	 */
	@GetMapping(value = "/popAddSku")
	public String popAddSku(Model model, HttpSession sess, HttpServletRequest req
			, @RequestParam(value = "idx", required = false, defaultValue = "0") int idx
			) {

		SettingSkuData setting = new SettingSkuData();
		SettingShopData settingShop = new SettingShopData();
		
		//LOGGER.debug("idx : " + idx);
		
		List<SettingShopData> list = settingService.selectShop(settingShop, sess); // 쇼핑몰 리스트
		SettingSkuData sku = new SettingSkuData();
		
		// 모드가 수정일 경우 idx로 검색
		if(idx > 0) {
			setting.setSkuIdx(idx);
			sku = settingService.selectSkuDetail(setting, sess); 
		} 
		
		model.addAttribute("sku", sku);
		model.addAttribute("list", list);
		
		return "/setting/popup/popSkuAdd";
	}
}
