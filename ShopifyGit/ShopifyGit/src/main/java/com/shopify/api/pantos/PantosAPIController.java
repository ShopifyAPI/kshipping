package com.shopify.api.pantos;

import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
@Controller
@Slf4j
public class PantosAPIController {
	
	@Autowired 		PantosService pantosService;

	
//	@RequestMapping(value = "/api/pantos/tracking", method = RequestMethod.POST, consumes="text/plain") ///  @RequestBody
//	@RequestMapping(value = "/api/pantos/tracking", method = RequestMethod.POST)
	@RequestMapping(value = "/api/pantos/tracking", method = RequestMethod.POST, consumes="application/x-www-form-urlencoded")
	@ResponseBody
	public String tracking(  @RequestHeader Map<String,String> headers, @RequestParam Map<String, String> map ) {
		
		String ifData = (String) map.get("ifdata");
		
		log.info("tracking START 2 : "+ifData);
		String resultString = null;

		try {
			
			String userId = "";
			String userPwd = "";
//			String userId = headers.get("bizptrid");
//			String userPwd = headers.get("bizptrpw");
			
			resultString = pantosService.updateTracking(ifData, userId, userPwd);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("tracking error : "+ifData);
		}
		log.info("tracking END : "+ifData);
		
		return resultString;
		
	}

}
