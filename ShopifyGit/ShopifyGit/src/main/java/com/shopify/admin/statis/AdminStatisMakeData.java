package com.shopify.admin.statis;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AdminStatisMakeData {
	private String crontab;
	private String nowDate; 	
	private String masterCode;
	private String courierCompany;
	private String courier;
	private int payment;
	private String volumeWeight; // 부피무게  (g) 단위 
	private String  weight;  // 부피무게(화면입력) (kg) 단위 
	private Date paymentDate;
	private int addFeesPrice;
	private int addSalePrice;
}
