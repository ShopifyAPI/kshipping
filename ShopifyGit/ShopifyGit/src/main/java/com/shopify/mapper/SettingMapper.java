package com.shopify.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shopify.api.ShopifyOutApiDataCarrierItems;
import com.shopify.order.popup.OrderCourierData;
import com.shopify.setting.CourierCompanyData;
import com.shopify.setting.SettingBoxData;
import com.shopify.setting.SettingData;
import com.shopify.setting.SettingSenderData;
import com.shopify.setting.SettingShopData;
import com.shopify.setting.SettingSkuData;
import com.shopify.setting.ShippingCompanyData;
import com.shopify.shop.ShopData;

/**
* Setting Mapper 
* @author : jwh
* @since  : 2019-01-03
* @desc   : /setting 정보 
*/
@Mapper
public interface SettingMapper {
	/**
	 * 설정 > 계정 관리 > Shop 정보
	 */
	public List<SettingShopData> selectShop(SettingShopData settingShop);
	public int updateUseShop (SettingShopData setting);
	public int deleteShop (SettingShopData setting);
	
	/**
	 * 설정 > 계정 관리 > Seller 정보
	 */
	public SettingData selectSeller (SettingData setting);
	public int updateSeller (SettingData setting);
	public String selectSellerRank(String rankCode);
	
	/**
	 * 설정 > 배송 관리 > 출고지 정보
	 */
	public List<SettingSenderData> selectSender (SettingSenderData setting); 
	public SettingSenderData selectSenderDetail (SettingSenderData setting);
	public SettingSenderData selectSenderDefault (SettingSenderData setting);
	public int insertSender (SettingSenderData setting);
	public int updateSender (SettingSenderData setting);
	public int updateDefaultSender (SettingSenderData setting);
	public int updateResetSender (SettingSenderData setting);
	public int deleteSender (SettingSenderData setting);
	
	/**
	 * 설정 > 배송 관리 > 박스 정보
	 */
	public List<SettingBoxData> selectBox (SettingBoxData setting);
	public SettingBoxData selectBoxDetail (SettingBoxData setting);
	public SettingBoxData selectBoxDefault (SettingBoxData setting); 
	public int insertBox (SettingBoxData setting);
	public int updateBox (SettingBoxData setting);
	public int updateDefaultBox (SettingBoxData setting);
	public int updateResetBox (SettingBoxData setting);
	public int deleteBox (SettingBoxData setting);
	
	/**
	 * 설정 > 배송 관리 > 관세 정보
	 */
	public List<SettingSkuData> selectSku (SettingSkuData setting);
	public SettingSkuData selectSkuDetail (SettingSkuData setting);
	public int selectSkuCount(SettingSkuData setting);
	public int insertSku (SettingSkuData setting);
	public int updateSku (SettingSkuData setting);
	public int deleteSku (SettingSkuData setting);
	public List<SettingSkuData> selectSkuProductType (SettingSkuData setting);
	public int updateSkuBox (SettingSkuData setting);
	
	public List<OrderCourierData> selectShopCourierInit(ShopData sd);
	public OrderCourierData selectShopCourier(OrderCourierData ocd);
	public int insertShopCourier(OrderCourierData input);
	public int updateShopCourier(OrderCourierData input);
//	public List<OrderCourierData> selectShopCourierFromEmail(OrderCourierData input);
	//public List<SettingShopData> selectShopXXX();
	public List<OrderCourierData> selectShopCourierFromEmailOuter(OrderCourierData input);
	public List<CourierCompanyData> selectCourierCompany(CourierCompanyData input);
	public List<OrderCourierData> selectCourierCompanyOrder(CourierCompanyData inputCompany);
	public List<ShippingCompanyData> selectVolumeticCourierCompany();
	public List<ShopifyOutApiDataCarrierItems> selectVolumeWeight(List<ShopifyOutApiDataCarrierItems> productId);
}
