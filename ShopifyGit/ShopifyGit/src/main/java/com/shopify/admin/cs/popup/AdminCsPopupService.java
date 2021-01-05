package com.shopify.admin.cs.popup;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopify.api.ShopifyApiController;
import com.shopify.cs.popup.CsPopupData;
import com.shopify.mapper.AdminCsMapper;

/**
 * 배송 팝업 서비스
 *
 */
@Service
@Transactional
public class AdminCsPopupService {
	private Logger LOGGER = LoggerFactory.getLogger(AdminCsPopupService.class);
	@Autowired private AdminCsMapper adminCsMapper;
	@Autowired private ShopifyApiController sApiController;
	
	/**
	 * cs 상세정보 관세정보 count
	 * */
	
	public int selectPopAdminCsDeliverySkuCount(AdminCsPopupData csPopData) {
        return adminCsMapper.selectPopAdminCsDeliverySkuCount(csPopData);
    }
	
	public int selectPopAdminCsBackSkuCount(AdminCsPopupData csPopData) {
        return adminCsMapper.selectPopAdminCsBackSkuCount(csPopData);
    }
	
	public int selectPopAdminCsExchangeSkuCount(AdminCsPopupData csPopData) {
        return adminCsMapper.selectPopAdminCsExchangeSkuCount(csPopData);
    }
	
	public int selectPopAdminCsReturnSkuCount(AdminCsPopupData csPopData) {
        return adminCsMapper.selectPopAdminCsReturnSkuCount(csPopData);
    }
	
	public int selectPopAdminCsPaymentSkuCount(AdminCsPopupData csPopData) {
        return adminCsMapper.selectPopAdminCsPaymentSkuCount(csPopData);
    }
	
	/**
	 * cs 상세정보 관세정보 List
	 * */
	public List<AdminCsPopupData> selectPopAdminCsDeliverySkuList(AdminCsPopupData csPopData, HttpSession sess) {
	    return adminCsMapper.selectPopAdminCsDeliverySkuList(csPopData);
	}
	
	public List<AdminCsPopupData> selectPopAdminCsBackSkuList(AdminCsPopupData csPopData, HttpSession sess) {
	    return adminCsMapper.selectPopAdminCsBackSkuList(csPopData);
	}
	
	public List<AdminCsPopupData> selectPopAdminCsExchangeSkuList(AdminCsPopupData csPopData, HttpSession sess) {
	    return adminCsMapper.selectPopAdminCsExchangeSkuList(csPopData);
	}
	
	public List<AdminCsPopupData> selectPopAdminCsReturnSkuList(AdminCsPopupData csPopData, HttpSession sess) {
	    return adminCsMapper.selectPopAdminCsReturnSkuList(csPopData);
	}
	
	public List<AdminCsPopupData> selectPopAdminCsPaymentSkuList(AdminCsPopupData csPopData, HttpSession sess) {
	    return adminCsMapper.selectPopAdminCsPaymentSkuList(csPopData);
	}
	
    
    

    
}
