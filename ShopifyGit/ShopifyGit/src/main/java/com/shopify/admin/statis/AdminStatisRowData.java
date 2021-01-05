package com.shopify.admin.statis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AdminStatisRowData {
	private String masterCode;				// 마스터코드 
	private String paymentCode;			// 매출/매입
	private int payment;						// 추가요금 금액 
	private String regDate;					// 등록일 
	private String addChargeInfo;		// 추가요금 사유 
}
