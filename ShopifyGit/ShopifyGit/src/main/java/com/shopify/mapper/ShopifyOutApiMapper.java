package com.shopify.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shopify.api.ShopifyOutApiDataCarrier;
import com.shopify.shop.ShopData;
/**
* ShopifyOutApi Mapper
* @desc   : /shopify에서 우리서버로 api 정보 요청 하는 것 
*/
@Mapper
public interface ShopifyOutApiMapper {
	/**
	 * 캐리어 서비스 
	 */
	public List<ShopifyOutApiDataCarrier> selectCarrier(ShopifyOutApiDataCarrier spApi);
	public ShopifyOutApiDataCarrier selectCarrierCount(ShopifyOutApiDataCarrier spApi);
	public int insertCarrier(ShopifyOutApiDataCarrier spApi);
	public int updateCarrier(ShopifyOutApiDataCarrier spApi);
	public int deleteCarrier(ShopifyOutApiDataCarrier spApi);
	public ShopData selectEmailFromDomain(String domain);
	
}
