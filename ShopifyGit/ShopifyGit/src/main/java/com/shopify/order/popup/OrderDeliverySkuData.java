package com.shopify.order.popup;

import java.util.Date;

import lombok.Data;

@Data
public class OrderDeliverySkuData {
	//공용 컬럼
	private String hscode;
	private String boxType;
    private int boxLength;
    private int boxWidth;
    private int boxHeight;
    private String boxUnit;
    private String weightUnit;
    private int quantity;
    private Date regDate;
    
	//tb_delivery_sku용 컬럼
    private String masterCode;
    private int orderIdx;
    private String orderCode;
    private String goodsCode;
    private String goods;
    private String goodsType;
    private String goodsSku;
    private double unitCost;
    private String taxable;
    private String barcode;
    private String origin;
    private String weight;   
    private String totalWeight;
    private String selectBox;
    private int price;
    private String priceCurrency;
    private String goodsItemId;
    private String repreItemNm;
    private String repreItemNmRu;
    private String itemLink;
    private String brand;

    //tb_shop_sku용 컬럼
    private int skuIdx;
	private int shopIdx;
	private String itemCode;
	private String itemName;
	private String itemType;
	private String itemSku;
	private double itemPrice;
	private int itemQty;
    private String itemOrigin;
    private String itemWeight;
    private String regId;
    private String combineYn;
}
