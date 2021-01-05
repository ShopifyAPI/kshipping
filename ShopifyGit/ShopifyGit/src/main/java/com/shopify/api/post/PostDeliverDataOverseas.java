package com.shopify.api.post;

import lombok.Data;
/**
 * @author SOLUGATE
 * 우체국 API 연동 DATA : 해외용 : XML 요청 -> XML 응답 
 */
@Data
public class PostDeliverDataOverseas {
	private String custno;									// Y/10/고객번호 
	private String apprno;									// Y/10/계약승인번호
	private String premiumcd;							// Y/2/국제우편물 구분코드
	private String em_ee;									// Y/2/국제우편물 종류코드
	private String countrycd;								// Y/2/도착국가코드
	private String totweight;								// Y/7/우편물 총중량(g)
	private String boyn;										// Y/1/보험가입 여부 
	private String boprc;										// N/15/보험가입 금액(KRW)
	private String nextdayreserveyn;				// N/1/익일 오전 예약신청 여부 
	private String reqhhmi;								// N/4/익일 오전 예약신청 시간 0900
	private String orderno;									// N/50/업체측 주문번호 
	private String premiumexportyn;				// N/1/수출서류 제출 여부 (EMS프리미엄용)
	private String cdremark;								// N/100/세금식별번호 : 브라질 
	private String sender;									// Y/35/발송인 이름 
	private String senderzipcode;						// Y/6/발송인 우편번호
	private String senderaddr1;						// Y/60/발송인 주소1(상세)
	private String senderaddr2;						// Y/140/발송인 주소2(기본)
	private String sendertelno1;						// Y/4/발송인전화번호: 82
	private String sendertelno2;						// Y/4/발송인전화번호: 2
	private String sendertelno3;						// Y/4/발송인전화번호: 123
	private String sendertelno4;						// Y/4/발송인전화번호: 4567
	private String sendermobile1;					// Y/4/발송인휴대전화번호: 82
	private String sendermobile2;					// Y/4/발송인휴대전화번호: 10
	private String sendermobile3;					// Y/4/발송인휴대전화번호: 1234
	private String sendermobile4;					// Y/4/발송인휴대전화번호: 5678
	private String senderemail;							// N/60/발송인 이메일 주소
	private String snd_message;						// N/100/배송 메시지(집하지시내역)
	private String receivename;						// Y/35/수취인 이름 
	private String receivezipcode;					// Y/20/수취인 우편번호
	private String receiveaddr1;						// N/140/수취인 주소1(주/도)
	private String receiveaddr2;						// N/200/수취인 주소2(시/군)
	private String receiveaddr3;						// Y/300/수취인 주소3(상세)
	private String buildnm;								// N/200/수취인 건물명(K-Packet,미국행인 경우)
	private String receivetelno1;						// Y/4/수취인 전화번호:81
	private String receivetelno2;						// Y/4/수취인 전화번호:1245
	private String receivetelno3;						// Y/4/수취인 전화번호:4857
	private String receivetelno4;						// Y/4/수취인 전화번호:4563
	private String receivetelno;							// Y/40/수취인 전체 전화번호 : 010-1234-1234
	private String receiveemail;						// N/60/수취인 이메일주소
	private String orderprsnzipcd;					// N/6/주문자 우편번호
	private String orderprsnaddr1;					// N/60/주문자 주소1(상세)
	private String orderprsnaddr2;					// N/140/주문자 주소2(기본)
	private String orderprsnnm;						// N/40/주문자명
	private String orderprsntelnno;					// N/4/주문자 전화번호: 82
	private String orderprsntelfno;					// N/4/주문자 전화번호: 42
	private String orderprsntelmno;					// N/4/주문자 전화번호: 860
	private String orderprsntellno;					// N/4/주문자 전화번호: 1004
	private String orderprsntelno;						// N/40/주문자 전체 전화번호: 02-123-1234
	private String orderprsnhtelfno;					// N/4/주문자 휴대전화번호: 10
	private String orderprsnhtelmno;				// N/4/주문자 휴대전화번호: 1111
	private String orderprsnhtellno;					// N/4/주문자 휴대전화번호: 2222
	private String orderprsnhtelno;					// N/40//주문자 전체 휴대전화번호: 010-1111-2222
	private String orderprsnemailid;				// N/60/주문자 이메일주소
	private String EM_gubun;							// Y/12/상품구분 (내용품 유형)
	private String contents;								// N/70/내용품명: clocks;toy;milk
	private String number;									// N/7/개수: 1;2;3
	private String weight;									// N/10/순중량(g) : 개당 중량 * 개수 
	private String value;										// N/15/가격(USD): 단가*개수
	private String hs_code;								// N/10/HS-Code: 1234567890;1234567890;1234567890;
	private String origin;										// N/20/생산지: KR;KR;US
	private String modelno;								// N/100/규격(모델명)
	private String ecommerceyn;						// N/1/관세청 수출우편물 정보제공 여부: Y
	private String exportsendprsnnm;				// N/35/수출화주 성명 또는 상호
	private String exportsendprsnaddr;			// N/105/수출화주 주소
	private String bizregno;								// N/10/사업자번호
	private String xprtnoyn;								// N1/수출이행 등록 여부 
	private String xprtno1;									// N/15/수출신고번호
	private String xprtno2;									//	N/15/수출신고번호
	private String xprtno3;									// N/15/수출신고번호
	private String xprtno4;									// N/15/수출신고번호
	private String totdivsendyn1;						// N/1/전량분할 발송 여부 (Y:전량, N:분할)
	private String totdivsendyn2;						// N/1/전량분할 발송 여부 (Y:전량, N:분할)
	private String totdivsendyn3;						// N/1/전량분할 발송 여부 (Y:전량, N:분할)
	private String totdivsendyn4;						// N/1/전량분할 발송 여부 (Y:전량, N:분할)
	private String wrapcnt1;								// N/5/선기적 포장 개수 
	private String wrapcnt2;								// N/5/선기적 포장 개수 
	private String wrapcnt3;								// N/5/선기적 포장 개수 
	private String wrapcnt4;								// N/5/선기적 포장 개수 
	private String recomporegipocd;				// N/5/추천 우체국기호
	private String skustockmgmtno;				// N/50/SKU 재고관리번호
	private String paytypecd;							// N/2/결제수단
	private String currunit;									// N/3/결제통화: KRW
	private String payapprno;							// N/50/결제승인번호
	private String dutypayprsncd;						// N/1/관세납부자
	private String dutypayamt;							// N/10/납부 관세액
	private String dutypaycurr;							// N/3/관세 납부통화
	private String boxlength;								// N/7/우편물 가로길이(cm)
	private String boxwidth;								// N/7/우편물 가로길이(cm)
	private String boxheight;								// N/7/우편물 가로길이(cm)
}
