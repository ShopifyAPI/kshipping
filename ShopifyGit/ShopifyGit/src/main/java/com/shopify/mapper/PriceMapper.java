package com.shopify.mapper;

import java.util.List;
import java.util.Map;

import com.shopify.admin.price.PriceData;
import com.shopify.admin.price.PriceViewData;

public interface PriceMapper {
	
	/**
	 * 특송 요금 관리 > 요금맵핑관리 VIew 페이지
	 * (공시요금)
	 */
	
	public String selectCodeGroup(String zoneCodeGroup);
	public List<PriceData> headerList(PriceData priceData);
	public List<PriceData> feesWeightList(PriceData headerList);
	public List<PriceViewData> feesPriceMappingList(Map<String,Object> weightList);
	public List<PriceData> selectShipServiceList(PriceData priceData);
	public PriceData selectPrice(PriceData priceData);		// 조한두 
	
	/**
	 * 특송 요금 관리 > 요금맵핑관리 VIew 페이지
	 * (할인요금)
	 */
	public List<PriceData> saleWeightList(PriceData headerList);
	public List<PriceViewData> salePriceMappingList(Map<String,Object> weightList);	
	
	
}
