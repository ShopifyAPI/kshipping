package com.shopify.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shopify.admin.delivery.DeliveryData;
 
@Mapper
public interface AdminDeliveryMapper {
    
	public List<DeliveryData> listShipCompany(DeliveryData deliveryData);
	public int selectCompanyCount(DeliveryData deliveryData);
	public int insertShipCompany(DeliveryData deliveryData);
	public int chkShipCompany(DeliveryData deliveryData);
    public int deleteShipCompany(DeliveryData deliveryData);
    public int updateShipCompany(DeliveryData deliveryData);

    public List<DeliveryData> selectDeliveryCompanyService(DeliveryData deliveryData);
    public DeliveryData selectShipCompanyView(DeliveryData deliveryData);
    public List<DeliveryData> selectShipCompany(DeliveryData deliveryData);
    public List<DeliveryData> selectShipService(DeliveryData deliveryData);
	public String selectCodeId(DeliveryData deliveryData);
}



