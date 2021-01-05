package com.shopify.api.lotte.delivery;

import lombok.Data;

@Data
public class LotteDeliveryResult {
	
	private String rtnCd;		// 결과 값 (S:성공 / E:오류)
	private String rtnMsg;		// 오류메시지
	private String ustRtgSctCd;	// 출고반품구분 (01:출고 02:반품)
	private String ordSct;		// 오더구분 (1:일반 2:교환 3:AS)
	private String fareSctCd;	// 운임구분 (03:신용 04:복합)
	private String ordNo;		// 주문번호
	private String invNo;		// 운송장번호

}
