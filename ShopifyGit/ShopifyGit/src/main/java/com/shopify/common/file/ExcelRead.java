package com.shopify.common.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.shopify.mapper.ExcelMapper;

public class ExcelRead {

	  private static final Logger LOGGER = LoggerFactory.getLogger(ExcelRead.class);
	  
	  @Autowired private static ExcelMapper excelMapper;
	  
	  public static List<Map<String, String>> read(ExcelReadOption excelReadOption) {

	    // 엑셀 파일 자체
	    // 엑셀파일을 읽어 들인다.
	    // FileType.getWorkbook() <-- 파일의 확장자에 따라서 적절하게 가져온다.
	    Workbook wb = ExcelFileType.getWorkbook(excelReadOption.getFilePath());

	    //	엑셀 파일에서 첫번째 시트를 가지고 온다.
	    Sheet sheet = wb.getSheetAt(0);

	    // sheet에서 유효한(데이터가 있는) 행의 개수를 가져온다.
	    int numOfRows = sheet.getPhysicalNumberOfRows();
	    int numOfCells = 0;
	    int numOfCell = 0;

	    Row row = null;
	    Cell cell = null;

	    String cellName = "";
	    
	    Map<String, String> map = null;
	    /*
	     * 각 Row를 리스트에 담는다. 하나의 Row를 하나의 Map으로 표현되며 List에는 모든 Row가 포함될 것이다.
	     */
	    List<Map<String, String>> result = new ArrayList<Map<String, String>>();
	    
	    List zoneList = new ArrayList();
	    List weightList = new ArrayList();
	    String price = "";
	    
	    	
	    	//첫번째 row의 data (zone의 갯수)  : KG | A|B|C|D......N 모두 같은 패턴임 
		    row = sheet.getRow(0);
		    
		    /*
	         * 가져온 Row의 Cell의 개수를 구한다.
	         */
	        numOfCell = row.getPhysicalNumberOfCells();
	        LOGGER.debug(">numOfCell:" + numOfCell);
	        //ZONE 갯수만큼 반복한다. 
	        for (int no = 1; no < numOfCell; no++) {
	        	
		        //ZONE List를 만든다.
		        if(no == 1) {
		        	for(int inndex = 1; inndex < numOfCell; inndex++) {
			        	Cell zone = row.getCell(inndex);
			        	String temp = ExcelCellRef.getValue(zone);
			        	LOGGER.debug(">zone:" + temp);
			        	zoneList.add(temp);
			        }
		        }
		        
		        //무게 갯수만큼 반복한다.
		        for (int rowIndex = 1; rowIndex < numOfRows; rowIndex++) {
		        	boolean check = true;
		        	
		        	row = sheet.getRow(rowIndex);
		        	//무게 List를 만든다.
		        	Cell tempWeightCell = row.getCell(0);
		    	        	
		    	    String tempWeight = ExcelCellRef.getValue(tempWeightCell);//0.5
		    	    LOGGER.debug(">weight:" + tempWeight);
		    	    double double_weight = Double.parseDouble(tempWeight);
		    	    int calcWeight = (int) (double_weight * 1000);		
		    	    
		    		weightList.add(calcWeight);

		    		cell = row.getCell(no);
		        	price = ExcelCellRef.getValue(cell);
		        	
		        	double double_price = 0;
		        	int calcPrice = 0;
		        	
		        	try {
		        		double_price = Double.parseDouble(price);
		        		calcPrice = (int) double_price;
		        		price = Integer.toString(calcPrice);
		        		LOGGER.debug(">price:" + price);
					} catch (Exception e) {
						check = false;
					}
		        	
		        	/*
			         * 데이터를 담을 맵 객체 초기화
			         */
		        	map = new HashMap<String, String>();
		        	if(check) { // price가 있을 경우 처리 
		        		//무게 , 금액, zone data를 맵에 넣는다.
			        	String zone = (String) zoneList.get(no-1);
			        	int integer_weight =  (int) weightList.get(rowIndex-1);
			        	String weight = Integer.toString(integer_weight);
			        	
		        		map.put("zone", zone.trim());
				        map.put("weight", weight.trim());
				        map.put("price", price.trim());
				        result.add(map);
		        	}
		        }
	        }
        
	    return result;
	  }
	  // 부피무게 엑셀 파싱 *********************************************************************************************
	  public static List<Map<String, String>> readVolumeWeight(ExcelReadOption excelReadOption) {
		    // 엑셀 파일 자체
		    // 엑셀파일을 읽어 들인다.
		    // FileType.getWorkbook() <-- 파일의 확장자에 따라서 적절하게 가져온다.
		    Workbook wb = ExcelFileType.getWorkbook(excelReadOption.getFilePath());
		    //	엑셀 파일에서 첫번째 시트를 가지고 온다.
		    Sheet sheet = wb.getSheetAt(0);
		    // sheet에서 유효한(데이터가 있는) 행의 개수를 가져온다.
		    int numOfRows = sheet.getPhysicalNumberOfRows();
		    int numOfCells = 0;
		    int numOfCell = 0;
		    Row row = null;
		    Cell cell = null;
		    String cellName = "";
		    Map<String, String> map = null;
		    /*
		     * 각 Row를 리스트에 담는다. 하나의 Row를 하나의 Map으로 표현되며 List에는 모든 Row가 포함될 것이다.
		     */
		    List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		    List rowList = new ArrayList();
		    List masterCodeList = new ArrayList();
		    String weight = "";
		    	//첫번째 row의 data (가로열의 갯수)
			    row = sheet.getRow(0);
			    /*
		         * 가져온 Row의 Cell의 개수를 구한다.
		         */
		        numOfCell = row.getPhysicalNumberOfCells();
		        LOGGER.debug(">numOfCell:" + numOfCell);
		        //ZONE 갯수만큼 반복한다. 
		        for (int no = 1; no < numOfCell; no++) {
			        //ZONE List를 만든다.
			        if(no == 1) {
			        	for(int inndex = 1; inndex < numOfCell; inndex++) {
				        	Cell header = row.getCell(inndex);
				        	String temp = ExcelCellRef.getValue(header);
				        	LOGGER.debug(">Header:" + temp);
				        	rowList.add(temp);
				        }
			        }
			        //마스터코드 갯수만큼 반복한다.
			        for (int rowIndex = 1; rowIndex < numOfRows; rowIndex++) {
			        	boolean check = true;
			        	row = sheet.getRow(rowIndex);
			        	//마스터코드 List를 만든다.
			        	Cell tempMaseterCodeCell = row.getCell(0); //*******************************************************************************************************
			    	    String tempMasterCode = ExcelCellRef.getValue(tempMaseterCodeCell);
			    	    LOGGER.debug(">masterCode:" + tempMasterCode);
			    		masterCodeList.add(tempMasterCode);
			    		cell = row.getCell(no);	//************************************************************************************************************************
			        	weight = ExcelCellRef.getValue(cell);
			        	double double_weight = 0;
			        	try {
			        		double_weight = Double.parseDouble(weight);
			        		LOGGER.debug(">weight:" + double_weight);
						} catch (Exception e) {
							check = false;
						}
			        	/*
				         * 데이터를 담을 맵 객체 초기화
				         */
			        	map = new HashMap<String, String>();
			        	if(check) { // price가 있을 경우 처리 
			        		//무게 , 금액, zone data를 맵에 넣는다.
				        	String zone = (String) rowList.get(no-1);
				            String masterCode=  (String)masterCodeList.get(rowIndex-1);
				        	
			        		//map.put("data", zone.trim());
					        map.put("masterCode", masterCode.trim());
					        map.put("weight", weight.trim());
					        //map.put(masterCode.trim(), weight.trim());
				            result.add(map);
			        	}
			        }
		        }
	        
		    return result;
		  }
	  
}
