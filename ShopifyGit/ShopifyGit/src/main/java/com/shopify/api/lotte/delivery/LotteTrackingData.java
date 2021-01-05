package com.shopify.api.lotte.delivery;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LotteTrackingData {
	
	@JsonProperty("EMP_TEL")
	private String empTel;
	
	@JsonProperty("BRNSHP_NM")
	private String brnshpNm;
	
	@JsonProperty("BRNSHP_CD")
	private String brnshpCd;
	
	@JsonProperty("SCAN_YMD")
	private String scanYmd;
	
	@JsonProperty("SCAN_TME")
	private String scanTme;
	
	@JsonProperty("STATUS")
	private String status;
	
	@JsonProperty("GODS_STAT_NM")
	private String godsStatNm;
	
	@JsonProperty("GODS_STAT_CD")
	private String godsStatCd;
	
	@JsonProperty("BRNSHP_TEL")
	private String brnshpTel;
	
	@JsonProperty("PTN_BRNSHP_TEL")
	private String ptnBrnshpTel;
	
	@JsonProperty("EMP_NM")
	private String empNm;
	
	@JsonProperty("PTN_BRNSHP_NM")
	private String ptnBrnshpNm;
	
	@JsonProperty("EMP_NO")
	private String empNo;
	
	@JsonProperty("PTN_BRNSHP_CD")
	private String ptnBrnshpCd;

}
