package com.shopify.setting;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
* Setting Shop Data
* @author : jwh
* @since  : 2019-12-26
* @desc   : /setting 정보 
*/

@Data
public class SettingShopData {
	// TB_SHOP table
	private int shopIdx;
	
	private String email;
	private String shopId;
	private String shopName;
	private String ecommerce;
	private String accessToken;
	private String accessKey;
	private String scope;
	private String expiresIn;
	private String combineYn;
	private String accountOwner;
	private String locale;
	private String collaborator;
	private String useYn;
	private String delYn;
	private Date regDate;
	private String companyEname;
	
	private String ckBox;
	private String encryptKey;
	private String billingYn;
	private String activeYn;
}
