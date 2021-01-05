package com.shopify.shipment.popup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)		//JSON 스트링에는 resultCode, resultMessage name이 존재하지만 ConstraintData 클래스에는 해당 멤버 변수가 존재하지 않아도 처리
@Data
public class LocalDeliveryPaymentData {
	private int idx;
	private String id;
	private String code;
	private String zone;
	private int weight;
	private int price;
	private String startDate;
	private String endDate;
	private String useYn;
	private String codeKname;
	private String codeEname;
	private String codeName;
	private String regDate;
	private String locale;
	
	private String nowDate;
}