package com.shopify.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.shopify.order.OrderData;
import com.shopify.order.OrderDetailSkuData;
import com.shopify.order.popup.OrderCourierData;
import com.shopify.order.popup.OrderCourierDataWrapper;
import com.shopify.order.popup.OrderDeliveryData;
import com.shopify.order.popup.OrderDeliverySkuData;
import com.shopify.order.popup.LocalDeliveryData;
import com.shopify.shipment.ShipmentData;

@Mapper 
@Repository 
public interface OrderMapper {
//	public int insertOrder (OrderData order);
	public int insertOrderOne (OrderData order);
//	public int updateOrder (OrderData order);
	public int updateOrderOne (OrderData order);
	public int deleteOrder (OrderData order);
	public OrderData selectOneOrder (OrderData order);
	public List<OrderData> selectOneOrderList (OrderData order);
	public List<OrderData> selectAllOrder(OrderData order);
	public List<OrderData> selectOrderCode(List<OrderData> order);
	public int updateOrderCodeMerge(List<OrderData> order);
	public int updateOrderDetailCodeMerge(List<OrderData> order);
	public int selectOrderIdx(String orderCode);
	
	// 합배송
	public OrderData selectDeliveryCombineCount(OrderData order);
	public int insertDeliveryCombine (OrderData order);
	public List<OrderData> selectDeliveryCombine(String mastercode);
	public List<OrderData> selectCombineCheckList(List<OrderData> order);
	
	public int selectOneOrderCount(OrderData order);
	public int selectAllOrderCount(OrderData order);
	
	public int insertOrderDetail (OrderData order);
	public int updateOrderDetail (OrderData order);
	public int mergeOrderDetail (OrderData order);
	public int deleteOrderDetail (OrderData order);
	public OrderData selectOneOrderDetail (OrderData order);
	public List<OrderData> selectAllOrderDetail(OrderData order);
	public int selectOneOrderDetailCount(OrderData order);
	public int selectAllOrderDetailCount();
	
	//popup tb_delivery_sku
	public ShipmentData selectDeliveryAddress(ShipmentData shipment); //배송 주소 가져 오기
	public List<OrderDetailSkuData> selectOrderDetailList(OrderDetailSkuData order); //주문 관세정보 가져 오기
	public List<OrderDetailSkuData> selectDeliverySkuList(OrderDetailSkuData order); //배송 관세정보 가져 오기
	
	public int selectDeliveryCount(OrderDeliveryData deliveryData);
	public List<OrderDeliveryData> selectDeliveryOrderCount(OrderDeliveryData deliveryData);					// 조한두 
	public List<OrderDeliveryData> selectDeliveryOrderCount1(OrderDeliveryData deliveryData);					// 조한두 
    public List<OrderDeliveryData> selectDelivery(OrderDeliveryData deliveryData);
    
    // 팝업 프로세스
    public int insertDelivery(OrderDeliveryData deliveryData);
    public int insertDeliveryOrder(OrderDeliveryData deliveryData);
    public int updateDeliveryOrder(OrderDeliveryData deliveryData);
    
    // 관세
    public int selectDeliverySkuCount(OrderDeliverySkuData sku);
    public int insertDeliverySku(OrderDeliverySkuData sku);
    public int updateDeliverySku(OrderDeliverySkuData sku);
    
    public int selectSettingSkuCount(OrderDeliverySkuData sku);
    public int insertSettingSku(OrderDeliverySkuData sku);
    public int updateSettingSku(OrderDeliverySkuData sku);
    
    //배송서비스
    public List<OrderCourierData> selectCourierList(OrderCourierData courier); 
    public OrderCourierData selectPayment(OrderCourierData courier); 
    
    public int insertDeliveryPayment(OrderCourierData courier);
    public int updateDeliveryPayment(OrderCourierData skcourieru);
    
    public int updateOrderhide(OrderData order);
    
    public ShipmentData selectCombineCarrier(ShipmentData shipment); //합배송 선택정보 가져오기 yr추가[2020.05.26]
    public String selectDefaultCourier(ShipmentData shipment);

    public List<OrderCourierData> selectDomesticCourierList(OrderCourierData courier);
    
    public List<LocalDeliveryData> selectLocalDelivery(LocalDeliveryData localData);
	public List<OrderDeliverySkuData>  selectOrderItemDetail(String orderCode);
	public OrderDeliverySkuData[] selectCombineOrderItemDetail(String[] orderCode);
	public String selectBuyerCountryCode(OrderDeliveryData deliveryData);
	public List<OrderCourierData> selectVolumeticCourierList(OrderCourierData courier);
	public List<OrderCourierData> selectWeightCourierList(OrderCourierData courier);
	public List<OrderCourierDataWrapper> selectWeightCourierListWeightMin(OrderCourierData courier);
	public List<OrderCourierDataWrapper> selectWeightCourierListWeightMax(OrderCourierData courier);
	public List<OrderCourierDataWrapper> selectWeightCourierListVolumeticMin(OrderCourierData courier);
	public List<OrderCourierDataWrapper> selectWeightCourierListVolumeticMax(OrderCourierData courier);
	public void updateOrderDetailGoods(OrderDeliverySkuData item);
	public String selectOrderDetailGoods(OrderDeliverySkuData item);
	public ShipmentData selectDeliveryAddressOrderList(ShipmentData setting);
}
