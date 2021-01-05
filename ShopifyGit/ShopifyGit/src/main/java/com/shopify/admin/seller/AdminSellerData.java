package com.shopify.admin.seller;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.shopify.mapper.SellerMapper;
import com.shopify.setting.SettingData;

import lombok.Data;

/**
* 관리자 > Seller Controller
* @author : jwh
* @since  : 2020-01-21
* @desc   : Seller 정보 관리 
*/

@Data
public class AdminSellerData extends SettingData {
	// TB_SELLER table
	
	private String userLang;
	private String editAdmin;
	
	
	//페이징관련 param
	private int currentPage;
	private int startRow;
	private int pageSize;
	private int totalPageNum;
	private int pageBlockSize;

	//검색관련 param
	private String searchRankId;
	private String searchType;
	private String searchWord;
	
	// TB_SHOP table
	private String shopName;
	private String shopId;
	private int subCnt;
	private String activeYn;	//yr 추가 [2020.05.18]
	private String sortOrder;
	
}
