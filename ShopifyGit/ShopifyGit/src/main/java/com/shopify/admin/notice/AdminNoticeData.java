package com.shopify.admin.notice;

import lombok.Data;

/**
* Board Data
* @author : kyh
* @since  : 2020-01-07
* @desc   : /board 정보 
*/

@Data
public class AdminNoticeData {
	
	public AdminNoticeData() {
		//
	}
	
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
	private String collaborator;
	private String useYN;
	
	//TB_NOTICE DATA	
	private int noti_idx;
	private int idx;
	private String flagTop;
	private String noti_svc;
	private String noti_type;
	private String type;
	private String notiFromDate;
	private String notiToDate;
	private String division;
	private String part;
	private String partCode;
	private String partName;
	private String title;
	private String content;
	private String reg_admin;
	private String writer;
	private String regDate;
	private String reg_date;
	private String ansDate;
	private String status;
	private int boardCount;
	private String userType;
	private String answer;

	private String admName;
	private String ckBox;
	private String depart;
	
	//페이징관련 param
	private int currentPage;
	private int startRow;
	private int pageSize;
	private int totalPageNum;
	private int pageBlockSize;

	//검색관련 param
	private String searchType;
	private String searchWord;

	private String locale;
	
	//파일 업로드 관린param
	private String realName;
	private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}

