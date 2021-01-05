package com.shopify.admin;


import java.util.Date;

import lombok.Data;

/**
* Admin Scope Data
* @author : kyh
* @since  : 2020-01-10
* @desc   : /admin 권한 정보 
*/
@Data
public class AdminScopeData {
	private String scopeId;
	private String menuId;
	private String redirect;
	private String sysPart;
	private String regDate;
	
	private String locale;
}