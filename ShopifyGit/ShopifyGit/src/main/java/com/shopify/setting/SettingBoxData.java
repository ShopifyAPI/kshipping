package com.shopify.setting;

import java.util.Date;

import lombok.Data;

/**
* Setting Box Data
* @author : jwh
* @since  : 2019-01-03
* @desc   : /setting 정보 
*/

@Data
public class SettingBoxData {
	// TB_SHOP_BOX table
	private int boxIdx;
	private int shopIdx;
	private String boxTitle;
	private String boxType;
	private int boxLength;
	private int boxWidth;
	private int boxHeight;
	private String boxUnit;
	private String boxWeight;
	private String weightUnit;
	private String useDefault;
	private String combineYn;
	private Date regDate;
	
	private String userLang;  ;
	private String boxTypeName;
	private String email;
	
	private String ckBox;
}

