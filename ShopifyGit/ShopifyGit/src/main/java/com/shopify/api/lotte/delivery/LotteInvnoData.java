package com.shopify.api.lotte.delivery;

import lombok.Data;

/**
 * @author SOLUGATE
 * 롯데 택배 API 연동 DATA
 */
@Data
public class LotteInvnoData {
	
	private long invNo;				//운송장번호
	private String masterCode;        // master code - 롯데에는 "ordNo" 로 전송됨   - 주문번호

}
