package com.shopify.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shopify.admin.cs.AdminCsData;
import com.shopify.admin.cs.popup.AdminCsPopupData;
import com.shopify.cs.CsData;
import com.shopify.cs.popup.CsPopupData;

/**
* Cs Mapper 
* @author : kyh
* @since  : 2020-02-04
* @desc   : cs 정보 
*/
@Mapper
public interface CsMapper {
	/**
	 * CS관리 > 리스트 정보 NEW
	 */
	public int selectCsDeliveryListCount(CsData csData);
	public List<CsData> selectCsDeliveryList(CsData csData);
	public CsData selectCsDeliveryView(CsData csData); 
	public List<CsData> selectCsDeliverySku(CsData csData);
	
	public int updateCsPaymentStatus(CsData csData);
	
	
	public String selectPaymentCode(CsData csData); //yr추가 [2020.05.21]
	
	
	
	
	/**
	 * CS관리 > 반송정보
	 */
	public int selectBackListCount(CsData csData);
	public List<CsData> selectBackList (CsData csData);
	public CsData selectBackDetailList (CsData csData);
	public int selectExcelListCount(CsData csData);
	public List<CsData> selectExcelList (CsData csData);
	
	
	
	/**
	 * CS관리 > 교환정보
	 */
	public int selectExchangeListCount(CsData csData);
	public List<CsData> selectExchangeList (CsData csData);
	public List<CsData> selectExchangeDetailList (CsData csData);
	
	
	/**
	 * CS관리 > 반품정보
	 */
	public int selectReturnListCount(CsData csData);
	public List<CsData> selectReturnList (CsData csData);
	public List<CsData> selectReturnDetailList (CsData csData);
	
	
	/**
	 * CS관리 > 추가요금 정보
	 */
	public int selectPaymentListCount(CsData csData);
	public List<CsData> selectPaymentList (CsData csData);
	public List<CsData> selectPaymentDetailList (CsData csData);
	
	/**
	 * CS관리 > 상태 체크
	 */
	public String csStatusChk(CsData csData);
	
	/**
	 * CS관리 > 상태변경
	 */
	public int updateCsStatus(CsData csData);
	
	
	/*반송 상세조회 2020-02-20*/
	
	/**
	 * CS상세조회 count
	 */
	public int selectCsBackCount(CsData csData);
	
	public int selectCsExchangeCount(CsData csData);
	
	public int selectCsReturnCount(CsData csData);
	
	public int selectCsPaymentCount(CsData csData);
	
	/**
	 * CS상세조회 상세 DATA
	 */
	public CsData selectCsBackDetail (CsData csData);
	
	public CsData selectCsExchangeDetail (CsData csData);
	
	public CsData selectCsReturnDetail (CsData csData);
	
	public CsData selectCsPaymentDetail (CsData csData);
	
	
	/**
	 * CS상세조회 관세정보 LIST
	 */
	public List<CsPopupData> selectPopCsBackSkuList (CsPopupData CsPopData);
	
	public List<CsPopupData> selectPopCsExchangeSkuList (CsPopupData CsPopData);
	
	public List<CsPopupData> selectPopCsReturnSkuList (CsPopupData CsPopData);
	
	public List<CsPopupData> selectPopCsPaymentSkuList (CsPopupData CsPopData);

	/**
	 * CS상세조회 관세정보 count
	 */
	public int selectPopCsBackSkuCount(CsPopupData CsPopData);
	
	public int selectPopCsExchangeSkuCount(CsPopupData CsPopData);
	
	public int selectPopCsReturnSkuCount(CsPopupData CsPopData);
	
	public int selectPopCsPaymentSkuCount(CsPopupData CsPopData);
	
	
}
