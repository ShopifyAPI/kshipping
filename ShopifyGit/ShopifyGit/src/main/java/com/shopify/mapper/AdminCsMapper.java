package com.shopify.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shopify.admin.cs.AdminCsData;
import com.shopify.admin.cs.popup.AdminCsPopupData;
import com.shopify.cs.CsData;

/**
* Cs Mapper 
* @author : kyh
* @since  : 2020-02-06
* @desc   : cs 정보 
*/
@Mapper
public interface AdminCsMapper {
	
	/**
	 * CS관리 > 리스트 정보 NEW
	 */
	public int selectAdminCsDeliveryListCount(AdminCsData csData);
	public List<AdminCsData> selectAdminCsDeliveryList(AdminCsData csData);
	public AdminCsData selectAdminCsDeliveryView(AdminCsData csData); 
	public List<AdminCsPopupData> selectAdminCsDeliverySku(AdminCsData csData);
	
	public int updateAdminCsPaymentStatus(AdminCsData csData);
	
	//public List<AdminCsData> selectAdminDeliveryDetailList (AdminCsData csData);
	
	
	/**
	 * CS관리 > 배송정보
	 */
	public int selectAdminDeliveryListCount(AdminCsData csData);
	public List<AdminCsData> selectAdminDeliveryList (AdminCsData csData);
	public List<AdminCsData> selectAdminDeliveryDetailList (AdminCsData csData);
	
	/**
	 * CS관리 > 반송정보
	 */
	public int selectAdminBackListCount(AdminCsData csData);
	public List<AdminCsData> selectAdminBackList (AdminCsData csData);
	public List<AdminCsData> selectAdminBackDetailList (AdminCsData csData);
	
	/**
	 * CS관리 > 교환정보
	 */
	public int selectAdminExchangeListCount(AdminCsData csData);
	public List<AdminCsData> selectAdminExchangeList (AdminCsData csData);
	public List<AdminCsData> selectAdminExchangeDetailList (AdminCsData csData);
	
	
	/**
	 * CS관리 > 반품정보
	 */
	public int selectAdminReturnListCount(AdminCsData csData);
	public List<AdminCsData> selectAdminReturnList (AdminCsData csData);
	public List<AdminCsData> selectAdminReturnDetailList (AdminCsData csData);
	
	
	/**
	 * CS관리 > 추가요금 정보
	 */
	public int selectAdminPaymentListCount(AdminCsData csData);
	public List<AdminCsData> selectAdminPaymentList (AdminCsData csData);
	public List<AdminCsData> selectAdminPaymentDetailList (AdminCsData csData);
	
	/**
	 * CS관리 > 상태 체크
	 */
	public String csStatusChk(AdminCsData csData);
	
	/**
	 * CS관리 > 상태변경
	 */
	public int updateAdminCsStatus(AdminCsData csData);
	
/*반송 상세조회 2020-02-20*/
	
	/**
	 * CS상세조회 count
	 */
	
	
	public int selectAdminCsDeliveryCount(AdminCsData adminCsData);
	
	public int selectAdminCsBackCount(AdminCsData adminCsData);
	
	public int selectAdminCsExchangeCount(AdminCsData adminCsData);
	
	public int selectAdminCsReturnCount(AdminCsData adminCsData);
	
	public int selectAdminCsPaymentCount(AdminCsData adminCsData);
	
	/**
	 * CS상세조회 상세 DATA
	 */
	
	public AdminCsData selectAdminCsDeliveryDetail (AdminCsData adminCsData);
	
	public AdminCsData selectAdminCsBackDetail (AdminCsData adminCsData);
	
	public AdminCsData selectAdminCsExchangeDetail (AdminCsData adminCsData);
	
	public AdminCsData selectAdminCsReturnDetail (AdminCsData adminCsData);
	
	public AdminCsData selectAdminCsPaymentDetail (AdminCsData adminCsData);
	
	public List<AdminCsData> selectAdminCsPaymentList (AdminCsData adminCsData);
	
	
	/**
	 * CS상세조회 관세정보 LIST
	 */
	public List<AdminCsPopupData> selectPopAdminCsDeliverySkuList (AdminCsPopupData CsPopData);
	
	public List<AdminCsPopupData> selectPopAdminCsBackSkuList (AdminCsPopupData CsPopData);
	
	public List<AdminCsPopupData> selectPopAdminCsExchangeSkuList (AdminCsPopupData CsPopData);
	
	public List<AdminCsPopupData> selectPopAdminCsReturnSkuList (AdminCsPopupData CsPopData);
	
	public List<AdminCsPopupData> selectPopAdminCsPaymentSkuList (AdminCsPopupData CsPopData);

	/**
	 * CS상세조회 관세정보 count
	 */
	public int selectPopAdminCsDeliverySkuCount(AdminCsPopupData CsPopData);
	
	public int selectPopAdminCsBackSkuCount(AdminCsPopupData CsPopData);
	
	public int selectPopAdminCsExchangeSkuCount(AdminCsPopupData CsPopData);
	
	public int selectPopAdminCsReturnSkuCount(AdminCsPopupData CsPopData);
	
	public int selectPopAdminCsPaymentSkuCount(AdminCsPopupData CsPopData);
	
	/**
	 * CS 엑셀다운 관련
	 */
	
	public int selectExcelListCount(AdminCsData adminCsData);
	public List<AdminCsData> selectExcelList (AdminCsData adminCsData);
	
}
