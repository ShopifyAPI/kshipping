package com.shopify.setting;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
* Setting Data
* @author : jwh
* @since  : 2019-12-26
* @desc   : /setting 정보 
*/

@Data
public class SettingData {
	// TB_SELLER table
	private String email;
	private String sellerId;
	private String emailVerified;
	private String firstName;
	private String lastName;
	private String firstNameEname;
	private String lastNameEname;
	private String passwd;
	private String phoneNumber;
	private String company;
	private String companyEname;
	private String companyNum;
	private String rankId;
	private String useYn;
	private String regDate;
	
	private String phoneNumber01;
	private String phoneNumber02;
	private String phoneNumber03;
	private String phoneNumber04;
	
	private String rankName;
	private String encryptKey;
	//셀러상태 &추가 [2020-05-08 YR]
	private String shopStatus;
	private String paymentMethod;
	private String paymentName;
}
