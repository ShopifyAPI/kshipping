package com.shopify.common.file; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopify.admin.statis.AdminStatisService;
import com.shopify.mapper.ExcelMapper;


@Service
@Transactional
public class ExcelService {

	@Autowired 
	private ExcelMapper excelMapper;
	private Logger LOGGER = LoggerFactory.getLogger(ExcelService.class);
	@Autowired private AdminStatisService adminStatisService;
	/**
	 * 부피중량 엑셀 파일 업로드 
	 * @param destFile
	 * @param filePath
	 */
	public  List<Map<String, String>> volumeWeightUpload(String destFile, String filePath) {
		ExcelReadOption excelReadOption = new ExcelReadOption();
        LOGGER.debug("ExcelService!!!!!!!!!!!");
        List<Map<String, String>> excelContent = new ArrayList<Map<String, String>>();
        //파일경로 추가
        excelReadOption.setFilePath(filePath);
        //시작 행
        excelReadOption.setStartRow(1);
        try {
        	  excelContent = ExcelRead.readVolumeWeight(excelReadOption); // **********************************************************
        	 
        	 Iterator rootItr = excelContent.iterator();
 			while (rootItr.hasNext()) {
 				Map tmpMap = (Map) rootItr.next();
 				LOGGER.debug(">>>tmpMap>>> : " + tmpMap.toString());
 				adminStatisService.updateVolumeWeightData(tmpMap);
 			}
        }catch (Exception e) {
	    	LOGGER.debug("ExcelService Exception : " + e.toString());
 	    }
        return excelContent;
	}
   /**
    * 요금매핑 가격 업로드 
    * @param destFile
    * @param filePath
    * @param shipCompany
    * @param deliveryService
    * @param startDate
    * @return
    */
	public int feesPriceExcelUpload(String destFile, String filePath, String shipCompany, String deliveryService, String startDate) {

	  	ExcelReadOption excelReadOption = new ExcelReadOption();
        LOGGER.debug("ExcelService!!!!!!!!!!!");
        //파일경로 추가
        excelReadOption.setFilePath(filePath);
        //시작 행
        excelReadOption.setStartRow(1);
        
        try {
	        List<Map<String, String>>excelContent = ExcelRead.read(excelReadOption);
	
	        Map<String, Object> paramMap = new HashMap<String, Object>();
	        Map<String, Object> updateMap = new HashMap<String, Object>();
	        
	        updateMap.put("shipCompany", shipCompany);
	        updateMap.put("deliveryService", deliveryService);
	        updateMap.put("startDate", startDate);
	
	        //기존에 있던 data를 useYn N 처리 , endDate 현재일자 -1로 수정, where : id, code,useYn,endDate
	        int updateFees = excelMapper.updateFeesData(updateMap);
	        
	        LOGGER.debug("feesPriceExcelUpload [ updateFees ]: " + updateFees);
	        
	        Iterator rootItr = excelContent.iterator();
			while (rootItr.hasNext()) {
				Map tmpMap = (Map) rootItr.next();
				tmpMap.put("shipCompany",shipCompany);
				tmpMap.put("deliveryService",deliveryService);
				tmpMap.put("startDate",startDate);
				
				String courierId = excelMapper.selectCourierId(tmpMap);
				
				tmpMap.put("courierId", courierId);
						
				LOGGER.debug("feesPriceExcelUpload : " + tmpMap.toString());
				
	        	excelMapper.insertFeesData(tmpMap);
	
			}
	        
	    } catch (Exception e) {
	    	LOGGER.debug("ExcelService Exception : " + e.toString());
	    	return 0;
	    }
	        
        return 1;
  }
	
	
	public int salePriceExcelUpload(String destFile, String filePath, String shipCompany, String deliveryService, String startDate) {

		//LOGGER.debug("salePriceExcelUpload : [Start]");
		
	  	ExcelReadOption excelReadOption = new ExcelReadOption();

        //파일경로 추가
        excelReadOption.setFilePath(filePath);
        //시작 행
        excelReadOption.setStartRow(1);
        
        try {
	        List<Map<String, String>>excelContent = ExcelRead.read(excelReadOption);
	
	        //LOGGER.debug("salePriceExcelUpload [excelContent] : " + excelContent.toString());
	        
	        Map<String, Object> paramMap = new HashMap<String, Object>();
	        Map<String, Object> updateMap = new HashMap<String, Object>();
	
	        updateMap.put("shipCompany", shipCompany);
	        updateMap.put("deliveryService", deliveryService);
	        updateMap.put("startDate", startDate);
	
	        //기존에 있던 data를 useYn N 처리 , endDate 현재일자로 수정, where : id, code,useYn,endDate
	        int updateFees = excelMapper.updateSaleData(updateMap);
	        
	        Iterator rootItr = excelContent.iterator();
			while (rootItr.hasNext()) {
				Map tmpMap = (Map) rootItr.next();
				tmpMap.put("shipCompany",shipCompany);
				tmpMap.put("deliveryService",deliveryService);
				tmpMap.put("startDate",startDate);
				
				//LOGGER.debug("salePriceExcelUpload : " + tmpMap.toString());
				
	        	excelMapper.insertSaleData(tmpMap);
			}
        
	    } catch (Exception e) {
	      // TODO Auto-generated catch block
	    	 return 0;
	    }
        
        return 1;
	}
  
}