package com.shopify.mapper;

import java.util.List;
import java.util.Map;

import com.shopify.admin.monitor.ExceptionLine;

public interface MonitorMapper {

	List<ExceptionLine> selectExceptionList(Map<String, Object> map);

	List<ExceptionLine> selectLastExceptionList(Map<String, Object> map);

	List<ExceptionLine> selectLastExceptionListFromDate(Map<String, Object> map);
}
