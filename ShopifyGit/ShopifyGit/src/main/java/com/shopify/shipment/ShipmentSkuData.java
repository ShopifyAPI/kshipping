package com.shopify.shipment;

import java.util.Date;

import com.shopify.order.OrderData;

import lombok.Data;

@Data
public class ShipmentSkuData {
	private String orderIdx;
	private String goodsCode;
	private String goods;
	private String goodsType;
	private String goodsSku;
	private int price;
	private int unitCost;
	private String taxable;
	private String barcode;
	private String origin;
	private String hscode;
	private int boxLength;
	private int boxWidth;
	private int boxHeight;
	private String boxUnit;
	private int weight;
	private String weightUnit;
	private Date regDate;
}
