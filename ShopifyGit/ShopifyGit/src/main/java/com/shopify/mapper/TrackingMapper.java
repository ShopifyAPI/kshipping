package com.shopify.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shopify.tracking.TrackingData;

/**
* Tracking Mapper
* @author : jwh
* @since  : 2019-01-20
* @desc   : /배송 정보 
*/
@Mapper
public interface TrackingMapper {
	/**
	 * 배송
	 */
	public List<TrackingData> selectTracking (TrackingData ship);  // **
	public int selectTrackingCount(TrackingData ship);  // **
   
}
