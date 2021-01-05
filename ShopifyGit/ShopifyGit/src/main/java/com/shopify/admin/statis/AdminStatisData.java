package com.shopify.admin.statis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AdminStatisData {
	private String state; 						// 배송상태 
	private String masterCodeList; 	//  화면에서 넘어온 변수 
	private String volumeWeightList; // 화면 - 부피무게 리스트 
	private String volumeCheckList;	// 화면 : 마스터코드|부피무게 
	private String[] arrMasterCodeList; // SQL IN 쿼리용 변수 
	private String paymentDate;
	private String courierCompany;
	private String courier;
	private String courierId;
	private int payment;
	private int paymentVat;				// 
	private int saleVat;						// 매입부가세
	private int totalPayment;			// 
	private int feesPrice;
	private int salePrice;					// 
	private int salePriceVat;
	private int totalSalePrice;
	private int pickupPrice;				// 조한두 
	private String paymentCode;
	private String masterCode;
	private String sellerEmail;
	private String company;
	private String rankId;
	private int shopIdx;
	private String shopName;
	private int totalWeight;
	private String weight; // *****
	private String buyerCountryCode;
	private String courierCompanyName;
	private String courierName;
	private String payType;
	private int rankPrice;
	private String boxType;
	
	private int totalCount;
	private int sumPayment;
	private int sumRankPrice;
	private int sumPaymentVat;
	private int sumSaleVat;					// 매입부가세
	private int sumTotalPayment;
	private int sumSalePrice;
	private int sumFeesPrice;
	private int sumPrice;
	private int sumPriceVat;
	private int sumTotalPrice;
	private int sumPickupPrice; // 조한두
	private int sumPaymentVatPrice; // 조한두 

	
	private int sumAddFeesPrice;
	private int sumAddSalePrice;
	private int sumTotalFeesPrice;
	private int sumTotalSalePrice;
	private int addFeesPrice;				// 
	private int addSalePrice;				// 
	private int feesTotal;						// 
	private int saleTotal;						// 
	private int payCount;
	private int realPayment;
	private int calculCompanyPrice;
	private int calculSellerPrice;
	private int calculCompanyCount;
	private int calculSellerCount;
	
	private String addFeesInfo; // 매출추가요금사유 
	private String addSaleInfo; // 매입추가요금사유
	private String calculType;  // 정산 타입 : COMPANY, SELLER
	//private boolean companyCalcul; // 택배사 정산 여부
	//private boolean sellerCalcul;		// 셀러정산 여부
	private String companyCalcul;
	private String sellerCalcul;
	private String companyCalculDesc; // 택배사 정산 여부
	private String sellerCalculDesc;		// 셀러정산 여부
	
	
	//검색 관련 param
	private String searchPaymentCode;  // 결제코드 (DO:국내, NA,ND:국외, EW:매출초과요금, SW:매입초과요금 )
	private String searchSeller;
	private String searchCompany;
	private String searchCourier;
	private String searchDatetype;
	private String searchMonthStart;
	private String searchMonthEnd;
	private String searchDateStart;
	private String searchDateEnd;
	private String nowDate;
	
	//페이징관련 param
	private int currentPage;
	private int startRow;
	private int pageSize;
	private int totalPageNum;
	private int pageBlockSize;
}
