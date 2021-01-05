package com.shopify.shipment.popup;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)		//JSON 스트링에는 resultCode, resultMessage name이 존재하지만 ConstraintData 클래스에는 해당 멤버 변수가 존재하지 않아도 처리
@Data
public class ShipmentPopupData {
    private String masterCode;
    private String[] masterCodeList;
    private String hblNo;
    private int orderIdx;
    private String goodsCode;
    private String goods;
    private String goodsType;
    private String goodsTypeStr;
    private String goodsSku;
    private int price;
    private double unitCost;
    private String unitCostStr;
    private String taxable;
    private String barcode;
    private String origin;
    private String originStr;
    private String hscode;
    private int boxLength;
    private int boxWidth;
    private int boxHeight; 
    private String state;
    private String stateStr;
    private String stateGroup;
    private String reason;
    private String domain;
    private String itemLink;
    private String brand;
    private String repreItemNm;
    private String repreItemNmRu;
    private String priceCurrency;
    
    
    private String boxUnit;
    private double weight;
    private String weightStr;
    private String weightUnit;
    private int quantity;
    private String regDate;
    private int paymentTotal;
    private int payment;
    private String paymentTotalStr;
    private String paymentStr;
    private int rankPrice;
    private String rankPriceStr;
    private String quantityStr;
    private int paymentVat;		// 조한두 
    private int pickupPrice; 		// 조한두 
    private String pickupCode; // 조한두 : L_PICKUP(롯데픽업), T_PICKUP(판토스픽업), P_PICKUP(우체국픽업)
    private String paymentCode;
    private int deliveryAmount;
    private String deliveryAmountStr;
    private String priceUsd; // paymentTotal 을 krw -> usd 변환 금액 
    private String billingYn; // 결제모드: Y : 실결제, N: 테스트 결제 

    private String invoice;
    private String courier;
    private String courierStr;
    private String courierChk;
    private String courierCompany;
    private String courierCompanyChk;
    private String buyerFirstname;
    private String buyerLastname;
    private String buyerProvince;
    private String buyerProvinceCode;
    private String buyerPhone;
    private String buyerCountryCode;
    private int shopIdx;
    private String combineYn;
    private String shopName;
    private String boxSize;
    private String[] boxSizeList;
    private String combineCode;
    
    
    private String changeReason;
    
    //이행관련 param
    private String locationId;
    private String fulId;
    private String fulOrderId;
    private String fulStatus;
    private String fulTrackingCompany;
    private String fulTrackingNumber;
    
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
    private String shopId;
    private int goodsCnt;
    private String orderCode;
    private String orderDate;
    private String orderName;
    private String payState;
    private String pickupServiceCode;
    
    //로컬관련 param
    private String localCode;
    private String deliveryCompany;
    private String stateCode;
    private String stateGroupCode;
    private String deliveryCode;

    private String locale;
    
    //결제api 관련 param
    private String apiClientId;
    private String apiStatus;
    private String apiName;
    private String apiPrice;
    private String apiTest;
    private String apiCreateDate;
    private String apiUpdateDate;
    private String apiConfirmationUrl;
    private String apiId;
    private String apiPayId;
    private String apiReturnUrl;
    private String apiReturnJson;
    private Boolean apiReturnChk;
    private String apiParamJson;
    private String charge_id;
    
    private int localPayment;
    private int localPaymentVat;
    
    private String localCompany;
    private String localBoxSize;
    private int localPrice;
    private String[] localMcode;
    
    private List<ShipmentPopupDataLocalData> localData;
    
    private String trackingNo;
    private String[] trackingNoList;
    
    
    //payinfo 관련 param
    private String payId;
    private String payCompany;
    private String payType;
    private int priceUnit; 	            // 1개 결제 금액 
    private int totalAmountSum;	// N개 결제시 합계 결제 금액 - paypal 용 
    private int totalAmount;			// 결제금액 
    private int remainAmount;
    private int cancelAmount;
    private String payMethod;
    private String financename;
    private String cardinstallmonth;
    private String cardnointyn;
    private String payYn;
    private String payUpdate;
}