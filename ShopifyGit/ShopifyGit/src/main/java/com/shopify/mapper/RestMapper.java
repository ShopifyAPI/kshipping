package com.shopify.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.shopify.common.RestApiData;
import com.shopify.schedule.CronJobData;

@Mapper
public interface RestMapper {
	 public int insertRestApiData (RestApiData data);

	public int updateRestApiData(RestApiData restApiData);

	public void insertCronJobData(CronJobData cronData);
}
