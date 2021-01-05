package com.shopify.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.shopify.seller.SellerData;

@Mapper
public interface SellerMapper {
	public int insertSeller(SellerData seller);
	public int updateSeller(SellerData seller);
	public int updateSellerPasswd(SellerData seller);
	public int selectSellerCount (SellerData seller);
	public SellerData selectSeller (SellerData seller);
}
