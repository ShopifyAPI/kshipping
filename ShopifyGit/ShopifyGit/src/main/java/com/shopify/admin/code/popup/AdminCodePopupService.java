package com.shopify.admin.code.popup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopify.common.util.UtilFn;
import com.shopify.mapper.AdminCodeMapper;

/**
 * Admin 코드관리 서비스
 *
 */

@Service
@Transactional
public class AdminCodePopupService {
	private Logger LOGGER = LoggerFactory.getLogger(AdminCodePopupService.class);
	
	@Autowired private AdminCodeMapper adminCodeMapper;
    @Autowired private UtilFn util;
	
	/**
	 * 운영 관리 > 코드 관리 > 조회
	 * @return
	 */
    public AdminCodePopupData selectAdminCodeShow(AdminCodePopupData codePopupData){
			
		return adminCodeMapper.selectAdminCodeShow(codePopupData);
		
    }
    
}