package com.shopify.api;

import java.util.List;

import lombok.Data;

@Data
public class ShopifyOutApiDataCarrier{
	//db용 param
	private int carrierCnt;
	private String id;
	private String name;
	private String active;
	private String format;
	private String callbackUrl;
	private String localLocale;
	private String comCode;
	private String comName;
	private String code;
	private String codeName;
	private String zone;
	private String locale;
	private int weight;
	private String price;
	private String minDeliveryDate;
	private String maxDeliveryDate;
	private int code_seq;
	private int comSeq;
	private String email;
	
	private int volumeWeightSix;
	private int volumeWeightFive;
	//in param
	private ShopifyOutApiDataCarrierRate rate;
}