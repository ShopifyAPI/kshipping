package com.shopify.order.popup;

import java.util.Date;

import lombok.Data;

@Data
public class OrderInputData {
	
	// from OrderDeliveryData
	private int orderIdx;
	private String masterCode;
	private String orderCode;
	private String orderDate;
    private int shopIdx;
    private String combineCode;
    private int selectSender;
    private String sellerName;
    private String sellerPhone;
    private String sellerCountryCode;
    private String sellerCountry;
    private String sellerCity;
    private String sellerZipCode;
    private String sellerAddr1;
    private String sellerAddr2;
    private String sellerAddr1Ename;
    private String sellerAddr2Ename;
    private String sellerProvince;
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
    private String boxUnit;
    private String weightUnit;
    private String state;
    private Date upDate;
    private Date shipmentData;
    private Date regDate;
    
    private String sellerPhone01;
    private String sellerPhone02;
    private String sellerPhone03;
    private String sellerPhone04;
    
    private String buyerPhone01;
    private String buyerPhone02;
    private String buyerPhone03;
    private String buyerPhone04;
    
    private int selectBox;
    private String boxType;
    private int boxLength;
    private int boxWidth;
    private int boxHeight;
    private int boxWeight;
    private String totalWeight;
    
    //합배송 관련 param
    private int parentCodeCnt;
    private String parentCode;
    private String childCode;
    private String arrShopIdx;
    private String arrOrderCode;
    
    // 저장용 포장재 정보
    private String arrSkuIdx;
    private String arrGoodsCode;
    private String arrGoods;
    private String arrGoodsSku;
    private String arrOrigin;
    private String arrHscode;
    private String arrGoodsType;
    private String arrWeightUnit;
    private String arrWeight;
    private String arrUnitCost;
    private String arrQuantity;
    private String arrBoxLength;
    private String arrBoxWidth;
    private String arrBoxHeight;
    private String arrBoxUnit;
    private String arrPriceCurrency;
    private String arrGoodsItemId;

    // sku 정보
    private String goodsCode;
    private String goods;
    private String goodsType;
    private String goodsSku;
//    private String price;
    private String unitCost;
    private String taxable;
    private String barcode;
    private String origin;
    private String hscode;
    private double weight;
    private String quantity;
    private String priceCurrency;
    private String goodsItemId;
    
    private String combineChk;
    
    
    // from OrderCourierData
	private int idx;
	private String id;
	private String code;
	private String zone;
//	private int weight;
	private int price;
	private String startDate;
	private String endDate;
	private String useYn;
//	private Date regDate;
	private String comCode;
	private String comName;
	private String codeName;
	private int minDeliveryDate;
	private int maxDeliveryDate;

	private String serviceCode;
	private String nationCode;
	
	private int paymentIdx;
//	private String masterCode;
	private String payId;
	private String paymentCode;
	private String invoice;
	private String courier;
	private String courierId;
	private String courierCompany;
	private int payment;
	private int rankPrice;
	private String payWeight;
	private String payWeightUnit;
	private String payState;
	private Date paymentDate;
	private Date paymentUpdate;
	private int salePrice;
	private int orderCourier;
	
	
	private String email;
	private String nowDate;
//	private int shopIdx;
//	private String orderCode;
	private String proc;
	private String locale;
	
	private String shippingLineName;
    private String shippingLineCode;
}





