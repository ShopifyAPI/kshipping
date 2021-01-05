package com.shopify.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.shopify.admin.rank.RankData;
 
@Mapper
public interface RankMapper {
    
	/**
	 * rank > 할인율 정보
	 */
	public List<RankData> listRank(RankData rankData);
	public RankData showRank(RankData rankData);
	public int updateRank(RankData rankData);
	public int chkRank(RankData rankData);
	public int insertRank(RankData rankData);
    public void deleteRank(String rankId);
    
    
	
	
}



