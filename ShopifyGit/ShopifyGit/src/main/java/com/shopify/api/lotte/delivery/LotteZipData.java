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
public class LotteZipData {
	
	private String id;	//거래처코드(6자리 택배코드) 또는 별도부여 ID
	private String network;	// 서비스구분 (필수, 00:일반, 01:특화)
	private String areaNo;	// 새 우편번호 (옵션)
	private String zipNo;		// 구 우편번호 (옵션)
	private String address;		// 주소 (필수)
}
