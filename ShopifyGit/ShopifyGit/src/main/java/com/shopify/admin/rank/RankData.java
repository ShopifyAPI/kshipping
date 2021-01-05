package com.shopify.admin.rank;

import java.util.Date;

import lombok.Data;

/**
* Rank Data
* @author : kyh
* @since  : 2020-01-07
* @desc   : /rank 정보 
*/

@Data
public class RankData {
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
	
	// TB_SELLER_RANK table
	private int idx;
	private String rankId;
	private String rankCode;
	private String discount;
	private String discountName;
	private String discountCode;
	private String startDate;
	private Date regDate;
	private String ckBox;
	
}
