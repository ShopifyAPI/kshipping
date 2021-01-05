package com.shopify.order;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)		//JSON 스트링에는 resultCode, resultMessage name이 존재하지만 ConstraintData 클래스에는 해당 멤버 변수가 존재하지 않아도 처리
@Data
public class OrderData {
	private int orderIdx;
	private int shopIdx;
	private String shopName;
	private String orderCode;
	private String orderName;
	private String hideYn;
	private String orderDate;
	private String regDate;
	private String locale;
	private String companyName;
	
	private int detailCnt;
	private int totalQuantity;
	private String goodsCode;
	private String goods;
	private String variants;
	private double price;
	private double unitCost;
	private String priceCurrency;
	private String taxable;
	private String barcode;
	private String origin;
	private String hscode;
	private String orderStatusUrl;
	private String itemLink;
	private String vendor;
	private String sku;
	private double weight;
	private int orderWeight;
	private String weightUnit;
	private String customerId;
	private String customerName;
	private String delYn;
	private int combineOrderIdx;
	private String combineOrderCode;
	private String combineYn;
	private String payStatus;
    private String encryptKey;
    private String email;
    private int quantity;
    private String goodsItemId;
    private String financialStatus;
    private String orderCourier;
    private String fulfillmentStatus;
    private String loadCheck;
    
    private String buyerFirstname;
    private String buyerLastname;
    private String buyerPhone;
    private String buyerEmail;
    private String buyerCountryCode;
    private String buyerCountry;
    private String buyerCity;
    private String buyerProvince;
    private String buyerProvinceCode;
    private String orderZipCode;
    private String buyerZipCode;
    private String buyerAddr1;
    private String buyerAddr2;
    private String locationId;
    private String shippingLineName;
    private String shippingLineCode;
    
    private int payment; //특송업체 지정여부
    private double totalPrice;//세금포함 주문가격
    private String totalPriceCurrency;
    
    //합배송 관련 param
    private int parentCodeCnt;
    private String parentCode;
    private String childCode;
    private int childShopIdx;
    
	
	//페이징관련 param
	private int startRow;
	private int rowPerPage;
	private int lastPage;
	private int currentPage;
	private int pageSize;
	private int totalPageNum;
	private int pageBlockSize;
	
	
	//검색관련 param
	private String searchDateStart;
	private String searchDateEnd;
	private String searchType;
	private String searchWord;
    private String searchDestType;
    private String searchCompany;
	
	private String searchPay;
	private String searchOrder;
	private int searchOrderStatus ;
	
	private String orderIdxChk;
	private String shopIdxChk;
	
	private String sortOrder;
}

