package com.shopify.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.shopify.payinfo.PayinfoData;

/**
* 결제 Mapper
* @desc   : /결제 정보 
*/
@Mapper
public interface PayinfoMapper {
	/**
	 * 결제저장
	 */
	public int insertPayinfo (PayinfoData payinfo);
    
}
