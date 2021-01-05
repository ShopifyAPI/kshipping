package com.shopify.admin;


import java.io.Serializable;

import lombok.Data;

/**
* Admin Data
* @author : kyh
* @since  : 2020-01-10
* @desc   : /admin 정보 
*/
@Data
public class AdminData implements Serializable  {
	private static final long serialVersionUID = -8799988283742620716L;
	/**
	 * 
	 */
	
	private String id;
	private String passwd;
	private String name;
	private String depart;
	private String email;
	private String phoneNumber;
	private String useYn;
	private String useSdate;
	private String useEdate;
	private String scopeId;
	private String regDate;
	private String upDate;

	// 로그인 관련 값
	private String locale;
	private String authority;
	private String redirect;

	// TB_ADMIN table
	private int no;
	private String adminId;
	private String adminPasswd;
	private String adminDepart;
	private String adminName;
	private String adminRegDate;
	private String adminScopeId;
	private String adminPhoneNumber;
	private String adminUseYn;
	private String adminUseSdate;
	private String adminUseEdate;
	private String ckBox;
	private String encryptKey;
	
	
	//페이징관련 param
	private int currentPage;
	private int startRow;
	private int pageSize;
	private int totalPageNum;
	private int pageBlockSize;

	//검색관련 param
	private String searchType;
	private String searchWord;
	private String searchWordAese;
	
}