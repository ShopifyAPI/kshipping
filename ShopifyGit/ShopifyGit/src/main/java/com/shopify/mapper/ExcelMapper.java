package com.shopify.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExcelMapper {
	
	public int updateFeesData(Map<String,Object> updateMap);
	public void insertFeesData(Map<String, Object> paramMap);

	
	public int updateSaleData(Map<String,Object> updateMap);
	public void insertSaleData(Map<String, Object> paramMap);
	public String selectCourierId(Map<String, Object> paramMap);

}
