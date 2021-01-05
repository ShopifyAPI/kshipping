package com.shopify.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.shopify.shop.ShopData;
 
@Mapper
public interface ShopMapper {
    public int insertShop (ShopData shop);
    public int updateShop (ShopData shop);
    public int deleteShop (ShopData shop);
    public ShopData selectOneShop (ShopData shop);
    public List<ShopData> selectShopOrderDomain(ShopData shop);
    public List<ShopData> selectAllShop(ShopData shop);
    public int selectOneShopCount(ShopData shop);
    public int selectAllShopCount();
	public List<ShopData> selectShopFromMaster(String[] array);
	public ShopData selectLotteTrackingFromMaster(String mastercode);
}
