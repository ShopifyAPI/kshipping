package com.shopify.setting;

import java.util.Date;

import lombok.Data;

/**
* Setting Sender Data
* @author : jwh
* @since  : 2019-01-03
* @desc   : /setting 정보 
*/

@Data
public class SettingSenderData {	
	// TB_SHOP_SENDER table
	private int senderIdx;
	private String senderTitle;
	private int shopIdx;
	private String name;
	private String phoneNumber;
	private String zipCode;
	private String addr1;
	private String addr2;
	private String addr1Ename;
	private String addr2Ename;
	private String province;
	private String city;
	private String useDefault;
	private String combineYn;
	private Date regDate;
	
	private String phoneNumber01;
	private String phoneNumber02;
	private String phoneNumber03;
	private String phoneNumber04;
	
	private String email;
	private String encryptKey;
}
