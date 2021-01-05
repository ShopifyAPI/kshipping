package com.shopify.admin.rank.popup;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.servlet.ModelAndView;

import com.shopify.admin.AdminData;
import com.shopify.admin.rank.RankData;
import com.shopify.admin.rank.RankService;
import com.shopify.common.SpConstants;
import com.shopify.mapper.RankMapper;
import com.shopify.shop.ShopData;

@RequestMapping("/admin")
@Controller
/**
 * 할인율 팝업 컨트롤러
 *
 */
public class RankPopupController {
	
	@Autowired
	private RankMapper rankMapper;
	@Autowired
	private RankService rankService;
	private Logger LOGGER = LoggerFactory.getLogger(RankPopupController.class);
	
	/**
	 * rank > 화면 이동 (할인율 등록 팝업)
	 * @return
	 */
	@GetMapping("/rank/popup/newRankPop")
	public String newShipCompany(HttpServletRequest req, HttpSession sess) throws Exception {
		
//		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		return "admin/popup/popRankNew";
	}
	
	/**
	 * 운영관리 > 할인율 수정 POPUP
	 * @param 
	 * @return
	 */
	@GetMapping("/rank/popup/adminEditPop")
	public String editAdmin(Model model, RankData rankData, HttpSession sess ) throws Exception {
		
//		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		RankData rank = rankService.showRank(rankData,sess);
		
		model.addAttribute("rankData", rank);
		
		return "admin/popup/popRankEdit";
		
	}
	
	/**
	 * 운영관리 > 할인율 수정
	 * @param 
	 * @return
	 */
	@PostMapping("/rank/popup/editRank")
	public ModelAndView updateBoardAnswer(Model model, @RequestBody RankData rankData, HttpSession sess) throws Exception {
//		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		AdminData sd = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		int result = rankService.updateRank(rankData,sess);
		
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;
	}
	
	
	
	
}
