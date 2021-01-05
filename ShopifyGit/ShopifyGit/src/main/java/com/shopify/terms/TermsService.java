package com.shopify.terms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopify.common.SpConstants;
import com.shopify.mapper.TermsMapper;
import com.shopify.shop.ShopData;

 
@Service
@Transactional

/**
 * 공통 서비스
 *
 */

public class TermsService {
 
    @Autowired private TermsMapper termsMapper;
    
    private Logger LOGGER = LoggerFactory.getLogger(TermsService.class);
        
    /**
	 * 약관동의
	 * @return
	 */
	public int updateTerms(ShopData shopdata, HttpSession sess) {
		
		ShopData sd = (ShopData) sess.getAttribute(SpConstants.HTTP_SESSION_KEY);
		String email = sd.getEmail();
		shopdata.setEmail(email);
		
		shopdata.setPrivatechk("Y");
		shopdata.setPublicchk("Y");
		String eventKey = shopdata.getEventchk();
		shopdata.setEventchk(eventKey);

		return termsMapper.updateTerms(shopdata);
		
	}
    
    
    
}
