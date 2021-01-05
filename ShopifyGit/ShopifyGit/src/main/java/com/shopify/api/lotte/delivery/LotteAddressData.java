package com.shopify.api.lotte.delivery;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

/**
 * @author SOLUGATE
 * 롯데 택배 API 주소정제 return data
 */

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class LotteAddressData {
	
	private String ukey;	// 자료 매칭을 위한 고유키
	private String areaNo;	// 새 우편번호 (옵션)
	private String zipNo;		// 구 우편번호 (옵션)
	private String address;		// 주소 (필수)
}
