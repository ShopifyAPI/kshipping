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
public class LotteAddressResultData {
	
	private String masterCode;	// master code : FK
	private String result;	// 처리결과 (success / error) 
	private String message;	// 오류메세지
	private String ukey;		// 자료 매칭을 위한 고유키
	private String cityGunGu;  // 시군구
	private String dong;	// 읍면동
	private String areaNo;	// 정제된 5자리 우편번호
	private String zipNo;	// 정제된 6자리 우편번호
	private String shipFare;	// 도선료
	private String airFare;	// 항공료
	private String tmlCd;		// 터미널 코드
	private String tmlNm;		// 터미널 코드
	private String filtCd;		// 도착지 코드
	private String brnshpNm;	// 주소지 집배달 대리점명
	private String brnshpCd;	// 주소지 집배달 대리점코드
	private String empNm;		//주소지 집배송사원명
	private String bldAnnm;		// 주소지 건물별칭
	private String lttd;		// 주소지 위도
	private String lgtd;		//  주소지 경도


}
