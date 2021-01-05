package com.shopify.main;

import lombok.Data;

/**
* DashBoard 데이터
* @author : jwh
* @since  : 2020-02-17
* @desc   : DashBoard 데이터 
*/

@Data
public class MainData {
	private String email;
	private String nowDate;
	private String startDate;
	private String endDate;
	
	private int orderDay;
	private int orderMonth;
	
	private int shipmentDay;
	private int shipmentMonth;
	
	private int salesDay;
	private int salesMonth;
	
//	private int totalCount;
//	private int totalPage;
//	private int countPerPage;
//	private int currentPage;
	
	//페이징관련 param
	private int currentPage;
	private int startRow;
	private int pageSize;
	private int totalPageNum;
	private int pageBlockSize;

	/* YR 추가 */
	
	//TB_NOTICE DATA	
	private int noti_idx;
	private int idx;
	private String flagTop;
	private String noti_svc;
	private String type;
	private String notiFromDate;
	private String notiToDate;
	private String regDate;
	private String division;
	private String part;
	private String partCode;
	private String partName;
	private String title;
	private String writer;
	private String content;
	private String reg_admin;
	private String ansDate;
	private String status;
	private int flagCount;
	private String userType;
	private String deletedYn;

	private String realName;
	private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
	
	//검색관련 param
	private String searchType;
	private String searchWord;	
	
	/* YR 추가 */
	

  	private int seq;
/*	private String title;
	private String content;*/
	private String writeday;
	private String group_name;
	private String attachfilenm;
/*	private int idx;*/
	private String locale;
	
	//private String error;
}
