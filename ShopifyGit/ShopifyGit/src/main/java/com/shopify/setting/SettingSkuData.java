package com.shopify.setting;

import java.util.Date;

import lombok.Data;

/**
* Setting Sku Data
* @author : jwh
* @since  : 2019-01-03
* @desc   : /setting 정보 
*/

@Data
public class SettingSkuData {
	// TB_SHOP_SKU table
	private int skuIdx;
	private int shopIdx;
	private String itemSku;
	private String itemCode;
	private String itemName;
	private String itemOption;
	private int boxLength;
	private int boxWidth;
	private int boxHeight;
	private String boxUnit;
	private int itemWeight;
	private String weightUnit;
	private int itemQty;
	private int itemPrice;
	private String itemOrigin;
	private String itemOriginName;
	private String itemType;
	private String itemTypeName;
	private String hscode;
	private String repreItemNm;
	private String repreItemNmRu;
	private String combineYn;
	private String regId;
	private String vendor;
	private String productType;
	private String variantId;
	private String lineitemId;
	
	private String boxTitle;
	private String boxType;
	private String boxSelect;  
	
	private Date regDate;
	
	private int selectBox;			// ADD : tb_shop_box 선택 값  
	private String useYn;
	
	private String loadCheck;
	private String priceCurrency;
	
	private String ckBox;
	private String userLang;
	
	// 부피무게 
	private int itemWidth;
	private int itemHeight;
	private int itemLength;
	private int itemVolumeweight;
	
	
	//페이징관련 param
	private int currentPage;
	private int startRow;
	private int pageSize;
	private int totalPageNum;
	private int pageBlockSize;


	//검색관련 param
	private String searchBoxType;
	private String searchType;
	private String searchWord;
	private String searchProductType;
	
	private String email;
}
