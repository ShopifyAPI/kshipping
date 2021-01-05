package com.shopify.api.lotte.delivery;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

/**
 * @author SOLUGATE
 * 롯데 택배 API 주소정제 return data
 */

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class LotteAddressListData {
	
	private String id;	// 거래처코드(6자리 택배코드) 또는 별도부여 ID
	private String network;	// 서비스구분 (default : 00)
	private List<LotteAddressData> list;		// list

}
