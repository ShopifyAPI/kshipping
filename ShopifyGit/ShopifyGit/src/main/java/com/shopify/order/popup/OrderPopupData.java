package com.shopify.order.popup;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)		//JSON 스트링에는 resultCode, resultMessage name이 존재하지만 ConstraintData 클래스에는 해당 멤버 변수가 존재하지 않아도 처리
@Data
public class OrderPopupData {
	private String orderIdxChk;
	private String orderCode;
	private int shopIdx;
	private String arrOrderCode;
	private String arrShopIdx;
	
	private String code;
	private String idx;
	
	
	private String nationCode;
	private int weight;
	
	private String[] orderCodeList;
}

