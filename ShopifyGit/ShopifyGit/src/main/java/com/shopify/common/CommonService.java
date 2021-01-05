package com.shopify.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopify.admin.AdminData;
import com.shopify.mapper.CommonMapper;
import com.shopify.shop.ShopData;

 
@Service
@Transactional

/**
 * 공통 서비스
 *
 */

public class CommonService {
 
    @Autowired private CommonMapper commonMapper;
    
    private Logger LOGGER = LoggerFactory.getLogger(CommonService.class);
        
    //컴포넌트 데이타 세팅
    public List listComponentData(List<Map<String, String>> params, HttpSession sess){
    	
    	// 국가 코드 세팅
    	ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
    	String locale = "";
    	if(sd == null) {
    		AdminData ad = (AdminData) sess.getAttribute(SpConstants.ADMIN_SESSION_KEY);
    		locale = ad.getLocale();
    	} else {
    		locale = sd.getLocale();
    	}
    	
    	//String locale = sd.getLocale();
    	Map<String, String> paramsMap = params.get(0);
    	String codeGroup = codeGroup = paramsMap.get("codeGroup");
    	
        List returnList = new ArrayList();
        
        if("F010000".equals(codeGroup)) { // 국가 코드 
        	returnList = commonMapper.listComponentNationData(paramsMap);
        } else { // 기타 코드
        	returnList = commonMapper.listComponentData(params);
        }
        
		return returnList;
    }
    
 public List listCodeGroup(CommonData commonData){
        
        //Map<String, Object> resultMap = new HashMap<String, Object>();
        
        List returnList = new ArrayList();
        
        returnList = commonMapper.listCodeGroup(commonData);
        
        LOGGER.debug("list>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+returnList);
        
		return returnList;
    }
    
}
