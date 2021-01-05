package com.shopify.admin.rank;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import com.shopify.mapper.RankMapper;

@RequestMapping("/admin")
@Controller
/**
 * 할인율 컨트롤러
 *
 */
public class RankController {
	
	
	@Autowired	private RankMapper rankMapper;
	@Autowired	private RankService rankService;
	@Autowired  private MessageSource messageSource;
    @Autowired  LocaleResolver localeResolver;
	
	private Logger LOGGER = LoggerFactory.getLogger(RankController.class);
	
	/**
	 * rank > rank view 페이지
	 * @return
	 */
	@GetMapping(value = "/rank/rankList")
	public String listRank(Model model, RankData rankData, HttpSession sess) {

		List<RankData> list = rankService.listRank(rankData,sess);
		
		model.addAttribute("list", list);
		
		return "/admin/rankList";
	}
	
	/**
	 * rank > rank insert
	 * @param model
	 * @return
	 */
	@PostMapping("/rank/insertRank")
	public ModelAndView insertRank(Locale locale,@RequestBody RankData rankData ,HttpSession sess) throws Exception {
		
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		int result = rankService.insertRank(locale,rankData,sess);
		
		if(result == -1) {
			
			mv.addObject("result", result);
			mv.addObject("errCode", false);
			mv.addObject("errMsg", "실패");
			
		}else {
			
			mv.addObject("result", result);
			mv.addObject("errCode", true);
			mv.addObject("errMsg", "성공");
			
		}

		return mv;
	}
	
	/**
	 * rank > rank delete
	 * @return
	 * 
	 */
	@PostMapping("/rank/deleteRank")
	public ModelAndView deleteRank(Model model, @RequestBody RankData rankData ,HttpSession sess) throws Exception {
		    	
		ModelAndView mv = new ModelAndView("jsonView");
		
		int result = rankService.deleteRank(rankData,sess);
		
		mv.addObject("result", result);
		mv.addObject("errCode", true);
		mv.addObject("errMsg", "성공");
		
		return mv;		
	}
	
}
