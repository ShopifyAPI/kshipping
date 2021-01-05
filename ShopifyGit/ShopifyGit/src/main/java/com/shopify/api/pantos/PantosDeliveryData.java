package com.shopify.api.pantos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @author SOLUGATE
 * 판토스 API 연동 DATA
 */

@Data
public class PantosDeliveryData {
	
	private String coNo  ;            	// Order No					masterCode
	private String boxQty  ;          	// Box 수량
	private String hblNo  ;           	// HBL번호(Pantos System 자동채번)
	private String cneeNm  ;          	// ShipTo  Name (수취인 이름)
	private String cneeNm1  ;         	// ShipTo First Name (수취인 이름)
	private String cneeNm2  ;         	// ShipTo Last Name (수취인 성)
	private String cneeAddr1  ;       	// ShipTo Address 1(상세주소)
	private String cneeAddr2  ;       	// ShipTo Address 2(상세주소)
	private String cneeAddr3  ;       	// ShipTo City
	private String cneeAddr4  ;       	// ShipTo State(미국 캐나다만 필수)
	private String cneeZipcd  ;       	// ShipTo Postal Code
	private String cneeNatnCd  ;      	// ShipTo Country
	private String cneeTelNo  ;       	// ShipTo Phone
	private String cneeEmailAddr  ;   	// ShipTo Person Email(필수)
	private String polCd  ;           	// 출발지 공항( KRICN 고정)
	private String podCd  ;           	// POD (도착공항)
	private String itemCd  ;          	// item category (메모 참조)
	private String itemNm  ;          	// Item Name 
	private String orgnNatnCd  ;      	// Origin country
	private double untprc  ;          	// Unit Price (Item 별)
	private String curCd  ;           	// Currency Code (Item 별)
	private String itemQty  ;        	// Quantity (Item 별)
	private String hsCd  ;            	// HS Code
	private String homepageAddr  ;    	// Goods Link URL
	private String wthLen  ;          	// Width (Cm)
	private String vertLen  ;         	// Length (Cm)
	private String hgt  ;             	// High (Cm)
	private double wgt  ;             	// Weight (중량은 필수)
	private String wgtUnitCd  ;       	// measure (1:Kg, 2:Lbs)  <- 기본은 1
	private String rmk  ;             	// Memo  
	private String etcNm1  ;          	// 옵션명  
	private String frgttermCd  ;      	// Freight Term (Prepaid:P,Collect:C) <- 기본은 P
	private String etcNm3  ;	            // 
	private String etcNm4  ;	            // 
	private String etcNm5  ;	            // 
	private String carrCd  ;				// 운송사
	private String expsSvcTypeCd  ;		// 운송사 서비스코드
	private String shppNm;				// 보내는 사람
	private String shppTelNo;			// 보내는 사람 전화번호
	private String shppAddr;			// 보내는 사람 주소
	private String taxDscrnNo;			//세금식별번호
	
	@JsonIgnore
	private String unitCurrency;		//제품Unit통화
	@JsonIgnore
	private String productId;
	@JsonIgnore
	private String shopIdx;
//	private String resStatus, etcNm12, soNo, currentSt, paymComNm, paymId, itemId, mblNo ;
	

}
