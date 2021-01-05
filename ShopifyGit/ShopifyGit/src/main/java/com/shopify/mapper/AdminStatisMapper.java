package com.shopify.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.shopify.admin.statis.AdminStatisData;
import com.shopify.admin.statis.AdminStatisMakeData;
import com.shopify.admin.statis.AdminStatisRowData;
//import com.shopify.admin.statis.AdminStatisTotalData;
import com.shopify.admin.statis.AdminStatisTotalData;

@Mapper
public interface AdminStatisMapper {
	/**
	 * 관리자 > 정산 > 매출원장
	 */
	public void insertStatisSales(String nowDate);
	public List<AdminStatisData> selectStatisSales(AdminStatisData statis);
	public AdminStatisData selectStatisSalesCount(AdminStatisData statis);
	public AdminStatisData selectStatisSalesLocalCount(AdminStatisData statis);		// 조한두 
	public List<AdminStatisData> selectStatisLocalSales(AdminStatisData statis);		// 조한두 
	public List<AdminStatisTotalData> selectStatisSalesTotal(AdminStatisTotalData statis);
	public List<AdminStatisTotalData> selectStatisSalesTotalNew(AdminStatisTotalData statis);
	public List<AdminStatisData> selectStatisSalesReport(AdminStatisData statis);  // 매출원장 요약 
	
	public void updateCalcul(AdminStatisData statis);													// 택배사/셀러정산 여부 업데이트 
	public void updateVolumeWeightData(Map map);													// 엑셀 부피 무게 업로드 
	public AdminStatisMakeData selectStatisAddSalePrice(AdminStatisMakeData statis);		// 추가요금계산
	public void insertAddFeesVolumePayment(AdminStatisMakeData statis);		// 공시가 추가요금 
	public void insertAddSaleVolumePayment(AdminStatisMakeData statis);		// 매입가 추가요금 
	public void deleteStatisSales(AdminStatisMakeData statis);									// 매출원장 삭제 
	public void deleteStatisPayment(String nowDate);							// 손익통계 삭제 
	public void deleteStatisAddPayment(AdminStatisMakeData statis);					// 추가요금 삭제 
	public void insertStatisSalesNew(AdminStatisMakeData statis);							// 매출원장 입력 
	public void updateWeight(AdminStatisMakeData statis);										// 부피무게 재계산 
	public List<AdminStatisRowData> selectStatisAddPriceDesc(AdminStatisRowData statis);// 추가요금 상세 사유 보기 팝업 
	/**
	 * 관리자 > 정산 > 손익통계
	 */
	public void insertStatisPayment(String nowDate);
	public void insertStatisPaymentNew(AdminStatisMakeData statis);			// 신규 : 손익통계
	public List<AdminStatisData> selectStatisPayment(AdminStatisData statis);
	public List<AdminStatisData> selectStatisPaymentNew(AdminStatisData statis);
}
