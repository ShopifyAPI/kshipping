package com.shopify.board;

import java.util.Date;

import lombok.Data;

/**
* Board Data
* @author : kyh
* @since  : 2020-01-07
* @desc   : /board 정보 
*/

@Data
public class BoardData {
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
	
	//TB_BOARD DATA
	private int idx;
	private int refIdx;
	private int seq;
	private String division;
	private String partCode;
	private String partName;
	private String title;
	private String content;
	private String answer;
	private String writer;
	private String regDate;
	private String ansDate;
	private String upWriter;
	private String status;
	private int boardCount;
	private String userType;
	private String part;
	
	//페이징관련 param
	private int currentPage;
	private int startRow;
	private int pageSize;
	private int totalPageNum;
	private int pageBlockSize;

	//검색관련 param
	private String searchType;
	private String searchWord;
}
