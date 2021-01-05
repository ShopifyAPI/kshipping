package com.shopify.seller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopify.mapper.SellerMapper;

/**
 * 셀러 서비스
 *
 */

@Service
@Transactional
public class SellerService {
	private Logger LOGGER = LoggerFactory.getLogger(SellerService.class);
	
	@Autowired private SellerMapper sellerMapper;
	
	public int insertSeller(SellerData seller){
		return sellerMapper.insertSeller(seller);
	}
	
	public int updateSeller(SellerData seller){
		return sellerMapper.updateSeller(seller);
	}
	
	public int updateSellerPasswd(SellerData seller){
		return sellerMapper.updateSellerPasswd(seller);
	}

	public int selectSellerCount(SellerData seller){
		return sellerMapper.selectSellerCount(seller);
	}
	
	public SellerData selectSeller(SellerData seller){
		return sellerMapper.selectSeller(seller);
	}
	
}