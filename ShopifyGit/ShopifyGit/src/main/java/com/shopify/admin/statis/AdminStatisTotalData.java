package com.shopify.admin.statis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shopify.order.OrderData;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AdminStatisTotalData {
	private String paymentDate;

	// 국내,해외 택배비
	private int ndCount;
	private int ndPayment;
	private int ndAddPayment;
	private int ndVat;
	private int ndRankPrice;
	private int ndPaymentVat;
	private int ndTotalPayment;
	private int ndASalePrice;
	private int ndFeesPrice;
	private int ndTotalPrice;
	//국내택배(롯데)
	private int doCount;
	private int doPayment;
	private int doVat;
	private int doAddPayment;
	private int doTotalPrice;
	// 해외특송비
	private int naCount;
	private int naPayment;
	private int naRankPrice;
	private int naPaymentVat;
	private int naTotalPayment;
	private int naASalePrice;
	private int naFeesPrice;
	
	// 초과요금비
	private int ewCount;
	private int ewPayment;
	private int ewVat;
	private int ewRankPrice;
	private int ewPaymentVat;
	private int ewTotalPayment;
	private int ewASalePrice;
	private int ewFeesPrice;
	
	// 총합계
	private int totalCount;
	private int sumPayment;
	private int sumAddPayment;
	private int sumRankPrice;
	private int sumPaymentVat;
	private int sumTotalPayment;
	private int sumSalePrice;
	private int sumFeesPrice;
	
	//검색 관련 param
	private String searchPaymentCode;  // 결제코드 (ND:국내, NA:국외, EW:초과요금)
	private String searchSeller;
	private String searchCompany;
	private String searchCourier;
	private String searchDatetype;
	private String searchMonthStart;
	private String searchMonthEnd;
	private String searchDateStart;
	private String searchDateEnd;
	private String searchDate;
	
	// 추가 (조한두)
	private String companyCalcul;
	private String sellerCalcul;
	private String companyCalculDesc; // 택배사 정산 여부
	private String sellerCalculDesc;		// 셀러정산 여부
	
}
