package com.shopify.mapper;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Mapper;

import com.shopify.common.CommonData;
 
@Mapper
public interface CommonMapper {
    
	public List<CommonData> listComponentData(List<Map<String, String>> params);
	public List<CommonData> listComponentNationData(Map<String, String> params);
	
	public List<CommonData> listCodeGroup(CommonData commonData);
	
}



