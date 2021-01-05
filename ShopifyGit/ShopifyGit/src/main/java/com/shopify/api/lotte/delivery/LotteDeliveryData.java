package com.shopify.api.lotte.delivery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author SOLUGATE
 * 롯데 택배 API 연동 DATA
 */
@Data
public class LotteDeliveryData {
	
	@JsonProperty("ordNo")
	private String masterCode;        // master code - 롯데에는 "ordNo" 로 전송됨   - 주문번호
	@JsonIgnore
	private String orderCode;		//주문번호
	@JsonIgnore
	private String orderName;		//주문번호
	private String invNo;				//운송장번호
	private String orglInvNo;			//원송장번호
	
	private String jobCustCd;  	        //거래처코드(6자리 택배코드)
	private String ustRtgSctCd;			//출고반품구분 (01:출고  02:반품)
	private String ordSct;				//오더구분(1:일반  2:교환  3:AS)
	private String fareSctCd;			//운임구분(03:신용  04:복합)
	private String snperNm;				//송하인명
	private String snperTel;			//송하인전화번호
	private String snperCpno;			//송하인휴대전화번호
	private String snperZipcd;			//송하인우편번호
	private String snperAdr;			//송하인주소(기본주소 + 상세주소)
	private String acperNm;				//수하인명
	private String acperTel;			//수하인전화번호
	private String acperCpno;			//수하인휴대전화번호
	private String acperZipcd;			//수하인우편번호
	private String acperAdr;			//수하인주소(기본주소 + 상세주소)
	private String boxTypCd;			//박스크기(A,B,C,D,E,F)
	private String gdsNm;				//상품명
	
	@JsonIgnore
	private String gdsNames;				//상품명 json format
	
	private String dlvMsgCont;			//배달메세지내용
	private String cusMsgCont;			//고객메세지내용
	private String pickReqYmd;			//집하요청일
	private String bdpkSctCd;			//합포장여부(Y/N)
	private String bdpkKey;				//합포장 KEY;
	private String bdpkRpnSeq;			//합포장 KEY;
	
    @JsonIgnore
	private String acperNm1;			//수하인명 1
    @JsonIgnore
    private String acperNm2;			//수하인명 2
    @JsonIgnore
	private String paramJson;			//배송신청 json
    @JsonIgnore
    private String returnJson;			//응답 json

    @JsonIgnore
    public String getFormatedInvNo() {
    	String result = null;
    	
    	if ( invNo != null && invNo.length() == 12 ) {
    		result = String.join("-", invNo.substring(0, 4), invNo.substring(4, 8), invNo.substring(8, 12) );
    	} else {
    		result = "";
    	}
    	
    	return result;
    }
}
