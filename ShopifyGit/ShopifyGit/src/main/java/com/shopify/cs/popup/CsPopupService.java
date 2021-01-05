package com.shopify.cs.popup;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopify.api.ShopifyApiController;
import com.shopify.mapper.CsMapper;

/**
 * 배송 팝업 서비스
 *
 */
@Service
@Transactional
public class CsPopupService {
	private Logger LOGGER = LoggerFactory.getLogger(CsPopupService.class);
	@Autowired private CsMapper csMapper;
	@Autowired private ShopifyApiController sApiController;
	
	/**
	 * cs 상세정보 관세정보 count
	 * */
	public int selectPopCsBackSkuCount(CsPopupData csPopData) {
        return csMapper.selectPopCsBackSkuCount(csPopData);
    }
	
	public int selectPopCsExchangeSkuCount(CsPopupData csPopData) {
        return csMapper.selectPopCsExchangeSkuCount(csPopData);
    }
	
	public int selectPopCsReturnSkuCount(CsPopupData csPopData) {
        return csMapper.selectPopCsReturnSkuCount(csPopData);
    }
	
	public int selectPopCsPaymentSkuCount(CsPopupData csPopData) {
        return csMapper.selectPopCsPaymentSkuCount(csPopData);
    }
	
	/**
	 * cs 상세정보 관세정보 List
	 * */
	public List<CsPopupData> selectPopCsBackSkuList(CsPopupData csPopData, HttpSession sess) {
	    return csMapper.selectPopCsBackSkuList(csPopData);
	}
	
	public List<CsPopupData> selectPopCsExchangeSkuList(CsPopupData csPopData, HttpSession sess) {
	    return csMapper.selectPopCsExchangeSkuList(csPopData);
	}
	
	public List<CsPopupData> selectPopCsReturnSkuList(CsPopupData csPopData, HttpSession sess) {
	    return csMapper.selectPopCsReturnSkuList(csPopData);
	}
	
	public List<CsPopupData> selectPopCsPaymentSkuList(CsPopupData csPopData, HttpSession sess) {
	    return csMapper.selectPopCsPaymentSkuList(csPopData);
	}
	
    
    

    
}
