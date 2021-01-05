package com.shopify.admin.cs.popup;

import java.util.Date;

import lombok.Data;

@Data
public class AdminCsPopupData {
	//세션정보
		private int shop_idx;
		private String shopName;
		private String email;
		private String id;
		private String shop;
		private String shop_id;
		private String ecommerce;
		private String access_token;
		private String access_key;
		private String scope;
		private String expires_in;
		private String combine_yn;
		private String account_owner;
		private String locale;
		private String collaborator;
		private String use_yn;
		
		//CS DATA
		private String masterCode;
		private String combineCode;
		private String sellerName;
		private String sellerPhone;
		private String sellerCountryCode;
		private String sellerCountry;
		private String sellerCity;
		private String sellerZipCode;
		private String sellerAddr1;
		private String sellerAddr2;
		
		private String buyerFirstname;
		private String buyerLastname;
		private String buyerPhone;
		private String buyerEmail;
		private String buyerCountryCode;
		private String buyerCountry;
		private String buyerCity;
		private String buyerProvince;
		private String buyerZipCode;
		private String buyerAddr1;
		private String buyerAddr2;
		
		private int boxLength;
		private int boxWidth;
		private int boxHeight;
		private String boxUnit;
		private int boxWeight;
		private String weightUnit;
		private String state;
		private String regDate;
		private String regDateStr;
		private String invoice;
		private String courier;
		private String courierCode;
		private String courierCompany;
		private String courierCompanyName;
		private String priceCurrency;
		
		private String goods;
		private String goodsCnt;
		private String orderCode;
		private String orderName;
		private String orderDate;
		private String payState;
		private String stateStr;
		private String stateStrCss;
		private String shipmentDate;
		private String reason;
		private String reasonStr;
		private String reasonStrCss;
		private String boxType;
		private int payment;
		
		//상세보기 Detail 관련
		private int price;
		private String goodsType;
		private String hscode;
		private String origin;
		private int quantity;
		private int weight;
		private String goodsSku;
		private String goodsCode;
		private String orderIdx;
		
		//페이징관련 param
		private int currentPage;
		private int startRow;
		private int pageSize;
		private int totalPageNum;
		private int pageBlockSize;

		//검색관련 param
		private String searchState;
	    private String searchDateStart;
	    private String searchDateEnd;
	    private String searchType;
	    private String searchWord;
	    private String searchWordAese;
	    private String searchBoxType;
	    
	    //상태 update 관련 param
	    private String ckBox;
	    private String updateStatus;
	    
	    private int PaymentTotal;
	    private int rankPrice;
	    
	    private String paymentStr;
	    private String paymentTotalStr;
	    private String rankPriceStr;
	    private String quantityStr;
//	    private String hscode;
	    private String goodsTypeStr;
	    private int unitCost;
	    private String unitCostStr;
    
    
}
