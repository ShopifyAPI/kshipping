package com.shopify.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.shopify.shop.ShopData;
 
@Mapper
public interface TermsMapper {
    
	
	int updateTerms(ShopData sd);
	
}



