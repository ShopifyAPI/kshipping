package com.shopify.admin.monitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopify.mapper.MonitorMapper;

import io.micrometer.core.instrument.util.StringUtils;

/**
 * Admin 서비스
 *
 */

@Service
@Transactional
public class MonitorService {
	
	private Logger LOGGER = LoggerFactory.getLogger(MonitorService.class);
	
	@Value("${monitor.names}")    
	private String[] instNames;
	
	@Autowired 
	private MonitorMapper monitorMapper;


	public List<ExceptionLine> lastExceptionLine(String dateLine, String id) {

		List<ExceptionLine> selectExceptionList = null;
		
		Map<String, Object> map = new HashMap<>();
		map.put("instNames", this.instNames);
		
		if ( StringUtils.isBlank(dateLine) || StringUtils.isBlank(id) ) {
			selectExceptionList = monitorMapper.selectLastExceptionList(map);
		} else {
			map.put("dateLine", dateLine);
			map.put("id", id);
			selectExceptionList = monitorMapper.selectExceptionList(map);
		}
		
		return selectExceptionList;
	}
	
}