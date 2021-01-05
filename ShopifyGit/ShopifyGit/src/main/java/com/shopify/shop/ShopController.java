package com.shopify.shop;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shopify.mapper.ShopMapper;

@Controller
public class ShopController {
	@Autowired private ShopMapper shopMapper;
	@Autowired private ShopService shopService;
	
	private Logger LOGGER = LoggerFactory.getLogger(ShopController.class);
	
	public boolean shopInsert(ShopData shop) {
		boolean retYN = false;

    	shopMapper.insertShop(shop);
    	
    	ShopData list = shopMapper.selectOneShop(shop);
        if(list.getAccessToken() != null) retYN = true;
		/*
		 * for(int i=0; i<list.size(); i++){ System.out.println("name : " +
		 * list.get(i).getUserId()); System.out.println("team : " +
		 * list.get(i).getUserName()); }
		 */
		return retYN;
	}
	
	@GetMapping("/shopList")
	public String shopList(Model model, @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage) {
		Map<String, Object> map = shopService.shopList(currentPage);
		model.addAttribute("shopList", map.get("list"));
		model.addAttribute("currentPage", map.get("currentPage"));
		model.addAttribute("lastPage", map.get("lastPage"));
		model.addAttribute("startPageNum", map.get("startPageNum"));
		model.addAttribute("lastPageNum", map.get("lastPageNum"));
		return "shop/shopList";
	}
}