package com.shopify.payinfo;

import java.util.Date;

import lombok.Data;

@Data
public class PayinfoData {
	private String payId;
	private String[] payIdList;
	private String masterCode;
	private String[] masterCodeList;
	private String payCompany;
	private String paytype;
	private String totalAmount;
	private String remainAmount;
	private String cancelAmount;
	private String payMethod;
	private String financename;
	private String cardinstallmonth;
	private String cardnointyn;
	private String payYn;
	private String payDate;
	private String payUpdate;
	private String regDate;
	private String deliveryCompany;

}
