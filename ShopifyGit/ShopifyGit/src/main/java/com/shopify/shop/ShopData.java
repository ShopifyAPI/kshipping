package com.shopify.shop;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class ShopData implements Serializable {
	private static final long serialVersionUID = -7690410338487183976L;
	/**
	 * 
	 */
	private int shopIdx;
	private String email;
	private String id;
	private String shop;
	private String shopId;
	private String shopName;
	private String appName;
	private String ecommerce;
	private String accessToken;
	private String access_token;
	private String accessKey;
	private String scope;
	private String expiresIn;
	private String combineYn;
	private String accountOwner;
	private String locale;
	private String domain;
	private String collaborator;
	private String useYn;
	private String activeYn;    //YR 추가 2020.05.15
	private String billingYn;   //YR 추가 2020.05.15
	private String delYn;
	private String regDate;
	private String privatechk;
	private String publicchk;
	private String eventchk;
	private String chk_date;
	private String buyerCountryCode;

	
	private int shopCount;
	
	private int startRow;
	private int rowPerPage;
	private int lastPage;
	private String encryptKey;
	
	private int rankRate;
	private String rankId;
	
	//주문별 domain처리 관련 param
	private String masterCode;
	private String[] masterCodeList;
	private String orderCode;
	private String orderIdx;
	
	private String trackingNo;
	private String courier;
}
