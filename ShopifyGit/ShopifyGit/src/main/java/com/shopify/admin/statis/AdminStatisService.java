package com.shopify.admin.statis;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.shopify.common.SpConstants;
import com.shopify.common.util.UtilFn;
import com.shopify.mapper.AdminStatisMapper;
import com.shopify.order.OrderData;

/**
* 정산 배치
* @author : jwh
* @since  : 2020-03-10
* @desc   : 손익통계 / 매출원장 배치 
*/

@Service
@Transactional
public class AdminStatisService {
	
	private Logger LOGGER = LoggerFactory.getLogger(AdminStatisService.class);
	
	@Autowired private UtilFn util;
	@Autowired private AdminStatisMapper adminStatisMapper;
	
	/**
	 * 관리자 > 정산 > 매출원장/손익통계 저장
	 * @param String nowDate (어제일자)
	 * @return void
	 */
	public void insertStatisSales(String nowDate) {
		if(nowDate == null || "".equals(nowDate)) {
			nowDate = util.getDateElement("dd-1");
		}
		AdminStatisMakeData inParam = new AdminStatisMakeData();
		inParam.setNowDate(nowDate);
		inParam.setCrontab("System");
		adminStatisMapper.insertStatisSalesNew(inParam);
		adminStatisMapper.insertStatisPaymentNew(inParam);
	}
	/**
	 *  손익통계 입력 
	 * @param nowDate
	 */
	public void insertStatisPaymentNew(AdminStatisMakeData statis) {
		adminStatisMapper.insertStatisPaymentNew(statis);
	}
	
	/* 
	 *  정산 업데이트: 택배사/셀러 
	 */
	public void updateCalcul(AdminStatisData statis) throws Exception {
		adminStatisMapper.updateCalcul(statis);
	}
	
	/*
	 *  초과요금 계산 
	 */
	public AdminStatisMakeData selectStatisAddSalePrice(AdminStatisMakeData statis) throws Exception {
		return adminStatisMapper.selectStatisAddSalePrice(statis);
	}
	/*
	 * 손익통계 삭제 
	 */
	public void deleteStatisPayment(String nowDate) throws Exception {
		adminStatisMapper.deleteStatisPayment(nowDate);
	}
	/*
	 * 추가요금 삭제 
	 */
	public void deleteStatisAddPayment(AdminStatisMakeData statis) throws Exception {
		adminStatisMapper.deleteStatisAddPayment(statis);
	}
	/*
	 * 매출원장 삭제 
	 */
	public void deleteStatisSales(AdminStatisMakeData statis) throws Exception {
		adminStatisMapper.deleteStatisSales(statis);
	}
	/*
	 * 추가요금 입력: 공시가 
	 */
	public void insertAddFeesVolumePayment(AdminStatisMakeData statis) throws Exception {
		adminStatisMapper.insertAddFeesVolumePayment(statis);
	}
	/*
	 * 추가요금 입력: 매입가
	 */
	public void insertAddSaleVolumePayment(AdminStatisMakeData statis) throws Exception {
		adminStatisMapper.insertAddSaleVolumePayment(statis);
	}
	/*
	 * 매출원장 입력 
	 */
	public void insertStatisSalesNew(AdminStatisMakeData statis) throws Exception {
		adminStatisMapper.insertStatisSalesNew(statis);
	}
	/*
	 *  부피무게 업데이트 
	 */
	public void updateWeight(AdminStatisMakeData statis) {
		adminStatisMapper.updateWeight(statis);
	}
	/*
	 * 부피무게 엑셀 업로드 
	 */
	public void updateVolumeWeightData(Map map) {
		adminStatisMapper.updateVolumeWeightData(map);
	}
	
	/**
	 * 관리자 > 정산 > 매출원장 요약
	 * @param Map<String, Object>
	 * @param HttpSession
	 * @return List<AdminStatisData>
	 */
	public Map<String, Object> selectStatisSalesReport(AdminStatisData statis, HttpSession sess) {
		List<AdminStatisData> list = new ArrayList<AdminStatisData>(); //데이터 리스트 변수 선언 
		list = adminStatisMapper.selectStatisSalesReport(statis);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		return map;
	}
	/**
	 * 관리자 > 정산 > 매출원장 리스트
	 * @param Map<String, Object>
	 * @param HttpSession
	 * @return List<AdminStatisData>
	 */
	public Map<String, Object> selectStatisSales(AdminStatisData statis, HttpSession sess, boolean isLocal) {

		String searchDateStart = statis.getSearchDateStart();
	    String searchDateEnd = statis.getSearchDateEnd();
	    int currentPage = statis.getCurrentPage();
	    int pageSize = statis.getPageSize();
		
	    // 기본 기간 검색 추가  (매월1일~매월말일)
        if(searchDateStart == null || "".equals(searchDateStart)) {
        	searchDateStart = util.getDateElement("yymm") + "-01"; 
        	statis.setSearchDateStart(searchDateStart);
        }
        
        // 기본 기간 검색 추가  (매월1일~매월말일)
        if(searchDateEnd == null || "".equals(searchDateEnd)) {
        	searchDateEnd = util.getDateElement("yymm") + "-" + util.getDateElement("ld");
        	statis.setSearchDateEnd(searchDateEnd);
        }
        
        if(currentPage == 0) currentPage = 1;
	    if(pageSize == 0) pageSize = 10;
	    
	    statis.setCurrentPage(currentPage);
	    statis.setPageSize(pageSize);
		
		AdminStatisData statisSum = null;
		if(isLocal)
			statisSum =  adminStatisMapper.selectStatisSalesLocalCount(statis);
		else
			statisSum =	adminStatisMapper.selectStatisSalesCount(statis);
		
		int dataCount = statisSum.getTotalCount(); // 전체 건수 조회
		int pageBlockSize = SpConstants.PAGE_BLOCK_SIZE; // 페이지 블럭 수
		
		Map<String, Object> paging = new HashMap<String, Object>();  //페이징 정보 리스트 선언
		List<AdminStatisData> list = new ArrayList<AdminStatisData>(); //데이터 리스트 변수 선언 
		
		// 데이터가 0보다 크면 페이징 및 데이터 받아 오기
		if (dataCount > 0) {
			paging = util.getPagingData(dataCount, pageSize, pageBlockSize, currentPage); //페이징 데이터 생성
			
			statis.setStartRow(Integer.parseInt(paging.get("startRow").toString())); // 데이터 시작 위치
			statis.setTotalPageNum(Integer.parseInt(paging.get("totalPageNum").toString())); // 최종데이터
			if(isLocal)
				list = adminStatisMapper.selectStatisLocalSales(statis);
			else
				list = adminStatisMapper.selectStatisSales(statis);
		}

		// 리턴 변수 선언
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("search", statis);
		map.put("sum", statisSum);
		map.put("list", list);
		map.put("paging", paging);
		
		return map;
	}

	
	/**
	 * 관리자 > 정산 > 손익통계 리스트 ***********************************************************************************
	 * @param AdminStatisData
	 * @param HttpSession
	 * @return List<AdminStatisData>
	 * @throws ParseException 
	 */
	public Map<String, Object> selectStatisPayment(AdminStatisData statis, HttpSession sess) throws ParseException {
		String searchMonthStart = statis.getSearchMonthStart();
	    String searchMonthEnd = statis.getSearchMonthEnd();
	    String searchDateStart = "";
	    String searchDateEnd = "";
	    
	    // 검색 시작일자 세팅
		if(searchMonthStart == null || "".equals(searchMonthStart)) {
			searchMonthStart = util.getDateElement("yy") + "-01";
			searchDateStart = searchMonthStart + "-01";
			
			statis.setSearchMonthStart(searchMonthStart); // view 변수
			statis.setSearchDateStart(searchDateStart); // 검색용 변수
		} else {
			searchMonthStart = statis.getSearchMonthStart();
			searchDateStart = searchMonthStart + "-01";
			
			statis.setSearchDateStart(searchDateStart); // 검색용 변수
		}
		
		// 검색 마지막일자 세팅
		if(searchMonthEnd == null || "".equals(searchMonthEnd)) {
			searchMonthEnd = util.getDateElement("yymm"); 
			searchDateEnd = searchMonthEnd + "-" + util.getDateElement("ld");
			
			statis.setSearchMonthEnd(searchMonthEnd); // view 변수
			statis.setSearchDateEnd(searchDateEnd); // 검색용 변수
		} else {
			searchMonthEnd = statis.getSearchMonthEnd();
			String tempDate = searchMonthEnd + "-01";
			searchDateEnd = searchMonthEnd + "-" + util.getLastDayElement(tempDate);
			
			statis.setSearchDateEnd(searchDateEnd); // 검색용 변수
		}
        
//        List<AdminStatisData> list = adminStatisMapper.selectStatisPayment(statis);
		 List<AdminStatisData> list = adminStatisMapper.selectStatisPaymentNew(statis);
        Map<String, Object> map = new HashMap<String, Object>();
		map.put("search", statis);
		map.put("list", list);
        
		return map;
	}
	
	/**
	 * 관리자 > 정산 > 월별/일별통계 리스트 *************************************************************************************************
	 * @param AdminStatisData
	 * @param HttpSession
	 * @return List<AdminStatisData>
	 * @throws ParseException 
	 */
	public Map<String, Object> selectStatisSalesTotal(AdminStatisTotalData statis, HttpSession sess) throws ParseException {
		// 기간 체크 
		String searchDatetype = statis.getSearchDatetype();
		
		String searchMonthStart = statis.getSearchMonthStart();
	    String searchMonthEnd = statis.getSearchMonthEnd();
	    String searchDateStart = "";
	    String searchDateEnd = "";
	    
	    // 월별 검색 
		if(searchDatetype == null || "".equals(searchDatetype) || "month".equals(searchDatetype)) {
			statis.setSearchDateStart("month");
			
			// 검색 시작일자 세팅
			if(searchMonthStart == null || "".equals(searchMonthStart)) {
				searchMonthStart = util.getDateElement("yy") + "-01";
				searchDateStart = searchMonthStart + "-01";
				
				statis.setSearchMonthStart(searchMonthStart); // view 변수
				statis.setSearchDateStart(searchDateStart); // 검색용 변수
			} else {
				searchMonthStart = statis.getSearchMonthStart();
				searchDateStart = searchMonthStart + "-01";
				
				statis.setSearchDateStart(searchDateStart); // 검색용 변수
			}
			
			// 검색 마지막일자 세팅
			if(searchMonthEnd == null || "".equals(searchMonthEnd)) {
				searchMonthEnd = util.getDateElement("yymm"); 
				searchDateEnd = searchMonthEnd + "-" + util.getDateElement("ld");
				
				statis.setSearchMonthEnd(searchMonthEnd); // view 변수
				statis.setSearchDateEnd(searchDateEnd); // 검색용 변수
			} else {
				searchMonthEnd = statis.getSearchMonthEnd();
				String tempDate = searchMonthEnd + "-01";
				searchDateEnd = searchMonthEnd + "-" + util.getLastDayElement(tempDate);
				
				statis.setSearchDateEnd(searchDateEnd); // 검색용 변수
			}
		} else { // 일별 통계
			String searchDate = statis.getSearchDate();
			
			if(searchDate == null || "".equals(searchDate)) {
				searchDate = util.getDateElement("yymm");
				statis.setSearchDate(searchDate);
			} 
			
			searchDateStart = searchDate + "-01"; 
			searchDateEnd = searchDate + "-" + util.getLastDayElement(searchDateStart);
			statis.setSearchDateStart(searchDateStart);
			statis.setSearchDateEnd(searchDateEnd);
		}
		
		LOGGER.debug("statis : " + statis.toString());
		
		//List<AdminStatisTotalData> list = adminStatisMapper.selectStatisSalesTotal(statis);
		List<AdminStatisTotalData> list = adminStatisMapper.selectStatisSalesTotalNew(statis);
        Map<String, Object> map = new HashMap<String, Object>();
		map.put("search", statis);
		map.put("list", list);
		
		return map;
	}	
	
	
	/**
	 * 관리자 > 정산 > 상세사유보기 팝업 
	 * @param Map<String, Object>
	 * @param HttpSession
	 * @return List<AdminStatisData>
	 */
	public Map<String, Object> selectStatisAddPriceDesc(AdminStatisRowData statis, HttpSession sess) {
		List<AdminStatisRowData> list = new ArrayList<AdminStatisRowData>(); //데이터 리스트 변수 선언 
		list = adminStatisMapper.selectStatisAddPriceDesc(statis);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		return map;
	}
	
    
}
