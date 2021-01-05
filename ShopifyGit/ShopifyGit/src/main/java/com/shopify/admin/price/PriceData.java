package com.shopify.admin.price;


import lombok.Data;

/**
* Price Data
* @author : kyh
* @since  : 2020-01-20
* @desc   : /price 정보 (할인율 맵핑 관리) 
*/
@Data
public class PriceData {
	// 로그인 관련 값
	private String locale;
	private String email;
	private String authority;
	private String redirect;
	private String shopId;
	
	// TB_ADMIN table
	
	private int idx;
	private String priceId;
	private String code;
	private String zone;
	private String weight;
	private String price;
	private String startDate;
	private String endDate;
	private String priceUseYn;
	private String priceRegDate;
	private String shipCompany;	
	private String codeKname;
	private String codeEtc;
	
	/* 요금 조회*/
	private String country;
	private int feesPrice;
	private int salesPrice;
	
	/* 검색조건 */
	private String zoneCodeId;
	private String zoneCodeName;
	private String zoneCodeGroup;
	private String zoneCodeKname;
	private String zoneCodeEname;
	private String zoneCodeEtc;
	private String codeGroup;
	private String nowDate;
	private String searchType;
	private String searchWord;
	
	private String weightCode;
	private String weightKg;
	
	private String companyId;
	
	//페이징관련 param
	private int startRow;
	private int rowPerPage;
	private int lastPage;
	private String encryptKey;
	private int currentPage;
	
}