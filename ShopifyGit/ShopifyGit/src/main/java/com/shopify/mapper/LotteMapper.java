package com.shopify.mapper;

import java.util.List;
import java.util.Map;

import com.shopify.api.lotte.delivery.LotteAddressResultData;
import com.shopify.api.lotte.delivery.LotteDeliveryData;
import com.shopify.api.lotte.delivery.LotteInvnoData;
import com.shopify.api.lotte.delivery.LotteResult;
import com.shopify.shipment.ShipmentData;
import com.shopify.shipment.popup.ShipmentPopupData;

public interface LotteMapper {

	public int updateDelivery(LotteResult lotteResult) ;

	public ShipmentData selectLotteDelivery(String masterCode);
	public int updateOrderLotte(Map parm);
	public int updateLotteApiReceiveReturn(LotteDeliveryData param);
	public int insertLotteApiReceive(LotteDeliveryData param);

	public int insertLotteAddress(LotteAddressResultData zipResult);

	public List<LotteDeliveryData> selectDeliveryLotteList(List<ShipmentPopupData> param);

	public List<String> selectLotteDeliverySku(String masterCode);

	public int insertLotteWaybill(LotteDeliveryData ldd);

	public List<LotteAddressResultData> selectLotteAddress(String[] codeAry);

	public List<LotteDeliveryData> selectLotteWaybill(String[] codeAry);

	public int insertLotteInvnoGenerator(LotteInvnoData seq);

	public LotteInvnoData selectLotteInvnoGenerator();
	
}
