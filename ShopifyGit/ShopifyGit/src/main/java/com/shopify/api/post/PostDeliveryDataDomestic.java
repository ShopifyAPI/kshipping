package com.shopify.api.post;

 

import com.shopify.api.pantos.PantosDeliveryData;

import lombok.Data;
/**
 * @author SOLUGATE
 * 우체국 API 연동 DATA : 국내용  : XML 요청 -> XML 응답 
 * MainController -> @RequestMapping(value="/selectPost") 참고 
 */
@Data
public class PostDeliveryDataDomestic {
													// 필수/길이/항목설명 
	private String custNo;		// Y/10/고객번호 
	private String apprNo;		// Y/10/계약승인번호
	private String payType;	// Y/1/요금납부구분 (1:일반-즉납or후납, 2:수취인부담)  1 
	private String reqType;	// Y/1/택배신청구분(1:일반택배,2:반품택배)   2
	private String officeSer;	// Y/20/발송지코드 (회수도착지) - 시스템에 등록된 발송지 코드: 솔루게이트 주소
	private String weight;		// N/2/무게(kg)
	private String volume;		// N/3/크기(cm)
	private String microYn;	// Y/1/초소형택배여부
	private String orderNo;	// Y/50/주문번호
	private String ordCompNm;	// Y/100/주문처명
	private String ordNm;				// N/40/주문자명
	private String ordZip;				// N/5/주문자우편번호
	private String ordAddr1;			// N/60/주문자 주소
	private String ordAddr2;			// N/100/주문자 상세주소
	private String ordTel;				// N/12/주문자 전화번호 
	private String ordMob;				// N/12/주문자 휴대전화번호 
	private String recNm;				// N/40/수취인명 
	private String recZip;				// Y/5/수취인 우편번호 
	private String recAddr1;			// Y/60/수취인 주소 
	private String recAddr2;			// Y/100/수취인 상세주소
	private String recTel;				// Y/12/수취인 전화번호
	private String recMob;				// Y/12/수취인 휴대전화번호
	private String contCd;				// Y/3/주요 내용품코드 
	private String goodsNm;			// Y/400/상품명 
	private String goodsCd;			// N/400/상품 코드 
	private String goodsMdl;			// N/400/상품 모델
	private String goodsSize;			// N/30/상품 사이즈 
	private String goodsColor;		// N/40/상품 색상
	private String qty;						// N/5/상품 수량 
	private String delivMsg;			// N/200/배송 메시지 
	private String retReason;			// N/40/ 반품 사유
	private String retVisitYmd;		// N/8/반품 희망 방문일자 
	private String printYn;				// N/1/자체 출력 여부  : 기표지(운송장)를 계약고객전용 시스템에서 출력하지 않고 자체출력인 경우 printYn=Y
	private String testYn;				// N/1/테스트 신청 여부 : 테스트 용도 호출시 testYn=Y
}
