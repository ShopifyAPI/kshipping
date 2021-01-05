package com.shopify.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.shopify.api.pantos.PantosDeliveryData;
import com.shopify.shipment.ShipmentData;
import com.shopify.shipment.TrackingData;
import com.shopify.shipment.popup.LocalDeliveryPaymentData;
import com.shopify.shipment.popup.ShipmentPopupData;

/**
* Shipment Mapper
* @author : jwh
* @since  : 2019-01-20
* @desc   : /배송 정보 
*/
@Mapper
public interface ShipmentMapper {
	/**
	 * 배송
	 */
	public List<ShipmentData> selectShipment (ShipmentData ship);  // **
	public List<ShipmentData> selectShipmentBank (ShipmentData ship);  // **
	public ShipmentData selectShipmentDetail (ShipmentData ship);
	public int selectShipmentDeleteCount(ShipmentData ship);
	public int selectShipmentDeleteCounts(ShipmentData ship);
	public int selectShipmentCount(ShipmentData ship);  // **
	public int selectShipmentCountBank(ShipmentData ship);  // **
	public int selectShipmentOrderCount(ShipmentData ship);
	public int selectShipmentOrderCountOrder(ShipmentData ship);
	public int insertShipment (ShipmentData ship);
	public int insertShipmentOrder(ShipmentData ship);
	public int updateShipment (ShipmentData ship);
	public int deleteShipment (ShipmentData ship);
	public int deleteShipments (ShipmentData ship);
	public int deleteShipmentOrder (ShipmentData ship);
	public int deleteShipmentsOrder (ShipmentData ship);
	
	
	/**
     * 배송 팝업
     */
	public int selectPopShipmentSkuCount(ShipmentPopupData ship);
	public List<ShipmentPopupData> selectPopShipmentSkuList (ShipmentPopupData ship);
	public int selectPopShipmentCount(ShipmentPopupData ship);
	public int selectPopShipmentCombineCount(ShipmentPopupData ship);
	public List<ShipmentPopupData> selectPopShipmentList (ShipmentPopupData ship);
	public List<ShipmentPopupData> selectPopShipmentCombineList (ShipmentPopupData ship);
	public int updatePopShipmentStat(ShipmentPopupData ship);
	
	public List<LocalDeliveryPaymentData> selectLocalDeliveryPayment(LocalDeliveryPaymentData payment);
	
	
	
	/**
     * 바코드 팝업
     */
    public List<ShipmentPopupData> selectPopShipmentDeliveryList (ShipmentPopupData ship);
    public List<ShipmentPopupData> selectPopShipmentDeliveryArrayList(ShipmentPopupData ship);
    
    /**
     * 결제처리
     */
    public int updatePopShipmentPaymentDelivery (ShipmentPopupData ship);
    public int updatePopShipmentPaymentDeliveryPayment (ShipmentPopupData ship);
    public int insertPopShipmentPaymentDeliveryPayment (ShipmentPopupData ship);
    public int insertPopShipmentPaymentDeliveryLocal (ShipmentPopupData ship);
    public int insertPopShipmentPaymentDeliveryLocalData (ShipmentPopupData ship);
    public int selectShipmentPrice(ShipmentPopupData ship);
    public ShipmentPopupData selectShipmentDelivery (ShipmentPopupData ship);
    public int insertShipmentPayInfo (ShipmentPopupData ship);
    public int insertShipmentPaymentApi (ShipmentPopupData ship);
    public int updateShipmentPaymentApi (ShipmentPopupData ship);
    public ShipmentPopupData selectShipmentActivate (ShipmentPopupData ship);
    public ShipmentPopupData selectShipmentActivatePayId (ShipmentPopupData ship);
    
    /**
     * 롯데 로지스 API 연동
     */
    public List<ShipmentPopupData> selectLocalCodeList(ShipmentPopupData ship);
    public List<ShipmentData> selectLocalCodeDelivery(ShipmentPopupData ship);
    public List<ShipmentData> selectDeliveryList(ShipmentPopupData ship);
    public List<ShipmentData> selectShipmentErrorList(ShipmentData ship);
    public List<ShipmentData> selectShipmentTrackingList(ShipmentData ship);
    public List<ShipmentData> selectShipmentTrackingLocalList(ShipmentData ship);
    public List<ShipmentPopupData> selectShipmentTrackingMasterCodeNAList(ShipmentPopupData ship);
    public List<ShipmentPopupData> selectShipmentTrackingMasterCodeNDList(ShipmentPopupData ship);
    public int insertShipmentPaymentLotteApi (ShipmentPopupData ship);

    public int updateDeliveryState(ShipmentPopupData ship);
    public int insertTrackingLotteApi(TrackingData ship);
    public int updateTrackingDelivery(TrackingData ship);
    public int updateTranType(TrackingData ship);
    public TrackingData selectTranType(String masterCode);
    
    
    public int insertChangeReason(ShipmentPopupData ship);
    
	public List<Map> selectDeliveryByCompanys(List<ShipmentPopupData> shipPopDataList);
	public int updateShipmentData(List<String> masterCodeList);
	public List<ShipmentData> listLotteHomeTracking(ShipmentData ship);
    
}
