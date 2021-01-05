package com.shopify.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shopify.admin.price.PriceData;
import com.shopify.admin.seller.AdminSellerData;
import com.shopify.admin.seller.AdminShopData;
import com.shopify.admin.seller.SellerDiscountDateData;
import com.shopify.api.ShopifyOutApiDataCarrier;
import com.shopify.order.popup.OrderCourierData;
import com.shopify.shop.ShopData;

@Mapper
public interface AdminSellerMapper {

	/**
	 * 관리자 > seller 관리
	 */
	public List<AdminSellerData> selectAdminSeller (AdminSellerData setting);
	public AdminSellerData selectAdminSellerDetail (AdminSellerData setting);
	public List<AdminShopData> selectAdminShopList (AdminShopData setting);
	public int selectAdminSellerCount(AdminSellerData setting);
	public int updateSellerStatus(SellerDiscountDateData setting);
	public List<AdminSellerData> selectSellerRankLog(AdminSellerData setting);
	public int updateShopBilling(AdminShopData setting);
	public int updateActive(AdminShopData setting);
	public List<ShopData> selectDiscountSellerList(PriceData priceData);
	public List<ShopifyOutApiDataCarrier> selectCourierListForeign(PriceData priceData);
	public List<OrderCourierData> selectDiscountList(PriceData priceData);
	public void updateSellerDiscountEndDate(SellerDiscountDateData setting);
	public List<OrderCourierData> selectSellerDiscount(OrderCourierData data);
	public void insertSellerDiscount(OrderCourierData record);
	public void updateSellerDiscount(OrderCourierData record);
}
