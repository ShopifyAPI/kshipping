package com.shopify.admin.code;

import lombok.Data;

/**
* Code Data
* @author : kyh
* @since  : 2020-01-31
* @desc   : tb_use_code 정보 
*/

@Data
public class AdminCodeData {
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
	private String codeRef;
	
	//검색관련 param
	private String searchType;
	private String searchUseYn;
	private String searchWord;
	private String userlang;
	private String ckBox;
	private String searchCodeGroup;
	
	
	
	//페이징관련 param
	private int currentPage;
	private int startRow;
	private int pageSize;
	private int totalPageNum;
	private int pageBlockSize;
	
}
