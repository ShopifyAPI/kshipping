package com.shopify.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shopify.shipment.ShipmentData;
import com.shopify.shipment.popup.LocalDeliveryPaymentData;
import com.shopify.shipment.popup.ShipmentPopupData;

/**
* Payment Mapper
* @author : jwh
* @since  : 2019-01-20
* @desc   : /배송 정보 
*/
@Mapper
public interface PaymentMapper {
	/**
	 * 배송
	 */
	public List<ShipmentData> selectPayment (ShipmentData ship);
	public ShipmentData selectPaymentDetail (ShipmentData ship);
	public int selectPaymentDeleteCount(ShipmentData ship);
	public int selectPaymentDeleteCounts(ShipmentData ship);
	public int selectPaymentCount(ShipmentData ship);
	public int selectPaymentOrderCount(ShipmentData ship);
	public int selectPaymentOrderCountOrder(ShipmentData ship);
	public int insertPayment (ShipmentData ship);
	public int insertPaymentOrder(ShipmentData ship);
	public int updatePayment (ShipmentData ship);
	public int deletePayment (ShipmentData ship);
	public int deletePayments (ShipmentData ship);
	public int deletePaymentOrder (ShipmentData ship);
	public int deletePaymentsOrder (ShipmentData ship);
	
	
	/**
     * 배송 팝업
     */
	public int selectPopPaymentSkuCount(ShipmentPopupData ship);
	public List<ShipmentPopupData> selectPopPaymentSkuList (ShipmentPopupData ship);
	public int selectPopPaymentCount(ShipmentPopupData ship);
	public int selectPopPaymentCombineCount(ShipmentPopupData ship);
	public List<ShipmentPopupData> selectPopPaymentList (ShipmentPopupData ship);
	public List<ShipmentPopupData> selectPopPaymentCombineList (ShipmentPopupData ship);
	public int updatePopPaymentStat(ShipmentPopupData ship);
	
	public List<LocalDeliveryPaymentData> selectLocalDeliveryPayment(LocalDeliveryPaymentData payment);
	
	
	
	/**
	 * 바코드 팝업
	 */
	public List<ShipmentPopupData> selectPopPaymentDeliveryList (ShipmentPopupData ship);
	public List<ShipmentPopupData> selectPopPaymentDeliveryArrayList(ShipmentPopupData ship);
	
	/**
	 * 결제처리
	 */
	public int updatePopPaymentPaymentDelivery (ShipmentPopupData ship);
	public int updatePopPaymentPaymentDeliveryPayment (ShipmentPopupData ship);
	public int insertPopPaymentPaymentDeliveryPayment (ShipmentPopupData ship);
	public int insertPopPaymentPaymentDeliveryLocal (ShipmentPopupData ship);
	public int insertPopPaymentPaymentDeliveryLocalData (ShipmentPopupData ship);
	public int selectPaymentPrice(ShipmentPopupData ship);
	public ShipmentPopupData selectPaymentDelivery (ShipmentPopupData ship);
	public ShipmentPopupData selectPaymentPopAccept(ShipmentPopupData ship);
	public List<ShipmentPopupData> selectPaymentChangeInfo(ShipmentPopupData ship);	// 조한두  (20.06.01)
	public int insertPaymentPayInfo (ShipmentPopupData ship);
	public int insertPaymentPaymentApi (ShipmentPopupData ship);
	public int insertPaymentCashApi(ShipmentPopupData ship); //박승현
	public int updatePaymentChangeInfo (ShipmentPopupData ship); // 조한두 
	public int updatePaymentPaymentApi (ShipmentPopupData ship);
	public int updatePaymentBoxSize(ShipmentPopupData shipPopPayment); //박승현
	
	/**
	 * 롯데 로지스 API 연동
	 */
	public List<ShipmentPopupData> selectLocalCodeList(ShipmentPopupData ship);
	public List<ShipmentData> selectLocalCodeDelivery(ShipmentPopupData ship);
	public List<ShipmentData> selectDeliveryList(ShipmentPopupData ship);
	public List<ShipmentData> selectPaymentErrorList(ShipmentData ship);
	public List<ShipmentData> selectPaymentTrackingList(ShipmentData ship);
	public List<ShipmentData> selectPaymentTrackingLocalList(ShipmentData ship);
	public List<ShipmentData> selectPaymentTrackingMasterCodeNAList(ShipmentData ship);
	public List<ShipmentData> selectPaymentTrackingMasterCodeNDList(ShipmentData ship);
	
	public int updateDeliveryState(ShipmentPopupData ship);
	
	/**
	 * 관리자 배송비 결제 방법 
	 */
	public String selectAdminPayMethod(String email);		// 조한두 
	

}
