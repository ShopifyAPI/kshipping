package com.shopify.tracking;

import java.util.Date;

import lombok.Data;

@Data
public class TrackingData {
    private String masterCode;
    private String[] masterCodeList;
    private int[] orderIdxList;
    private int shopIdx;
    private int orderIdx;
    private String orderName;
    private String combineCode;
    private int selectSender;
    private String sellerName;
    private String sellerPhone;
    private String sellerCountryCode;
    private String sellerCountry;
    private String sellerCity;
    private String sellerProvince;
    private String sellerZipCode;
    private String sellerAddr1;
    private String sellerAddr2;
    private String sellerAddr1Ename;
    private String sellerAddr2Ename;
    private String buyerFirstname;
    private String buyerLastname;
    private String buyerPhone;
    private String buyerEmail;
    private String buyerCountryCode;
    private String buyerCountry;
    private String buyerCity;
    private String buyerProvince;
    private String buyerProvinceCode;
    private String buyerZipCode;
    private String buyerAddr1;
    private String buyerAddr2;
    private int selectBox;
    private int boxLength;
    private int boxWidth;
    private int boxHeight;
    private String boxUnit;
    private String boxWeight;
    private String weightUnit;
    private double totalWeight;
    private String totalWeightUnit;
    private String boxType;
    private String state;
    private String stateDate;
    private String stateStr;
    private String stateStrCss;
    private String paymentCode;
    private String deliveryCompany;
    private String origin;
    private float weight;
    private String weightStr;
    private String totalPriceCurrency;
    private String orderCourier;
    
    private String sellerPhone01;
    private String sellerPhone02;
    private String sellerPhone03;
    private String sellerPhone04;
    
    private String buyerPhone01;
    private String buyerPhone02;
    private String buyerPhone03;
    private String buyerPhone04;
    
    private String localCode;
    
    private Date regDate;
    private String regDateStr;
    private int rankPrice;
    private int payment;
    private int paymentVat;
    private int paymentTotal;
    private int quantity;
    
    private String rankPriceStr;
    private String paymentStr;
    private String paymentTotalStr;
    private String boxSize;
    private String[] boxSizeList;
    private String quantityStr;
    private int deliveryAmount;
    private String deliveryAmountStr;

    private String shopName;
    private String combineYn;
    private String ckBox;
    private String userLang;
    
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

    private String email;
    private String goods;
    private String goodsCode;
    private String goodsCodeList;
    private int goodsCnt;
    private String invoice;
    private String courier;
    private String courierStr;
    private String courierChk;
    private String courierCompany;
    private String courierCompanyChk;
    private String orderCode;
    private String orderDate;
    private String payState;
    private String payMethod;					// paypal, bank
    private String payMethodSetting;		// 어드민에서 tb_seller->pay_method 셋팅값: 1 페이팔, 2 무통장, 3: 모두 
    private String payWeight;
    private String pickupServiceCode;
    private String locationId;
    
    private String locale;
    private double orderPrice;
    private String orderPriceStr;
    private String customerName;
    
    private String consigneeName;
    private String consigneeTel;
    private String consigneeCpno;
    private String consigneeZipcode;
    private String consigneeAddr1;
    private String consigneeAddr2;
    
    private String shippingLineName;
    private String shippingLineCode;
    
    private String charge_id;
    private String chargeId;
    private String id;
    
    //이행관련 param
    private String fulId;
    private String fulOrderId;
    private String fulStatus;
    private String fulTrackingCompany;
    private String fulTrackingNumber;
    
}
