package com.shopify.admin.code.popup;

import lombok.Data;

/**
* Code Data(Popup)
* @author : kyh
* @since  : 2020-02-03
* @desc   : tb_use_code 정보 
*/

@Data
public class AdminCodePopupData {
	//세션정보
	private int shop_idx;
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
	
	//TB_BOARD TABLE
	private String codeId;
	private String codeGroup;
	private int codeSeq;
	private String codeKname;
	private String codeEname;
	private String codeEtc;
	private String codeDiscript;
	private String codeUseYn;
	private String codeRegDate;
	
}
