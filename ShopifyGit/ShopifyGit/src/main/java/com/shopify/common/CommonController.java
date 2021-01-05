package com.shopify.common;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shopify.admin.AdminData;

@Controller
public class CommonController {
	private Logger LOGGER = LoggerFactory.getLogger(CommonController.class);
	
	@Autowired private CommonService commonService;
	
	
	//컴포넌트 세팅
	@PostMapping("/common/componentDataSet")
	@ResponseBody
	public List componentDataSet(Model model, HttpServletRequest req, HttpSession sess,@RequestBody List<Map<String, String>> params)		throws Exception {
		
		List resultList = new ArrayList();
		
		resultList = commonService.listComponentData(params,sess);
		
		return resultList;
		
	}
	
	@PostMapping("/common/listCodeGroup")
	@ResponseBody
	public List listCodeGroup(Model model, HttpServletRequest req, CommonData commonData,HttpSession sess) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String,Object>();
		
		AdminData adminData = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
		
		commonData.setLocale(adminData.getLocale());
		
		List resultList = new ArrayList();
		
		resultList = commonService.listCodeGroup(commonData);
		
		resultMap.put("list", resultList);
		
		return resultList;
		
	}

}
