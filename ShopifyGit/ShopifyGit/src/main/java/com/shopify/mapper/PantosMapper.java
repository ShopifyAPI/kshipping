package com.shopify.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.shopify.api.pantos.PantosDeliveryData;
import com.shopify.api.pantos.PantosTrackingData;
import com.shopify.shipment.popup.ShipmentPopupData;

@Mapper
public interface PantosMapper {
	
	public int funTrackingProcess(PantosTrackingData ship);
	public void insertAPILog(Map<String, String> parm);
	public void insertTrackingLog(Map<String, String> parm);
    
    public List<PantosDeliveryData> selectDeliveryPantos(List<ShipmentPopupData> parm) ;
    
	public int updateOrderPantos(Map parm);

}
