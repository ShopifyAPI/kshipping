package com.shopify.admin.cs;

import lombok.Data;

/**
* Cs Data
* @author : kyh
* @since  : 2020-02-4
* @desc   : CS 정보 
*/

@Data
public class AdminCsData {
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
		private String[] masterCodeList;
		private String combineCode;
		private String sellerName;
		private String sellerPhone;
		private String sellerCountryCode;
		private String sellerCountry;
		private String sellerCity;
		private String sellerZipCode;
		private String sellerAddr1;
		private String sellerAddr2;
		private String hblNo;
		
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
		private String addChargeInfo;
		private String localCode;
		private String localInvoice;
		private String localCompany;
		private String localState;
		private String localBox;
		private int totalPrice;
		private String shipmentData;
		private String paymentDate;
		private String paymentCode;
		private String company;
		
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
		private String stateGroup;
		private String changeReason;
		private String trackingNo;
		
		//상세보기 Detail 관련
		private String price;
		private String goodsType;
		private String hsCode;
		private String origin;
		private int quantity;
		private String weight;
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
	    private String searchPayment;
	    
	    //상태 update 관련 param
	    private String ckBox;
	    private String updateStatus;
	    
	    private int paymentTotal;
	    private int rankPrice;
	    private String paymentStr;
	    private String paymentTotalStr;
	    private String rankPriceStr;
	    private String quantityStr;
	    private int deliveryAmount;
	    private String deliveryAmountStr;
	    
	    private String payWeight;
	    private String customerName;
	    private double orderPrice;
	
	    
	    //엑셀다운 관련
	    private int singlePayWeight;
	    private String singlePayWeightUnit;
	    private int goodsWeight;
	    private String goodsWeightUnit;
	    private String goodSPriceStr;
}
