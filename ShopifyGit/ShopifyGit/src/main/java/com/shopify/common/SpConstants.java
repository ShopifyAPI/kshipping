package com.shopify.common;

public class SpConstants {
	public SpConstants() {}
	
	public static final String HTTP_SESSION_KEY = "SHOPIFY_SESSION";						//사용자 세션
	public static final String TOKEN_SESSION_KEY = "SHOPIFY_TOKEN_SESSION";			//사용자 토큰 세션
	public static final String ADMIN_SESSION_KEY = "SHOPIFY_ADMIN_SESSION";			//관리자 세션
	public static final String MSG_SESSION_KEY = "SHOPIFY_MESSAGE_SESSION";			//메세지 세션
	public static final String REFFRER_SESSION_KEY = "SHOPIFY_PREV_SESSION";			//이전페이지 세션
	public static final String LOCALE_SESSION_KEY = "SHOPIFY_LOCALE_SESSION";   	//언어 세션
	public static final String ENCRYPT_KEY = "SHOPIFY_ENKEY";									//암호화 키
	public static final String FIND_PASSWD_KEY = "SHOPIFY_EMAIL_ENKEY";					//비번찾기 암호화 키
	public static final String ECOMMERCE_KEY = "shopify";											//SHOP테이블내 ECOMMERCE 키
	public static final int PAGE_BLOCK_SIZE = 10;														//페이징 사이즈
	
	// 배송 그룹 코드 
	public static final String  STATE_GROUP_DELIVERY = "A020000";               				//배송 그룹 코드
	public static final String  STATE_GROUP_BACK = "A030000";									//반송 그룹 코드
	public static final String  STATE_GROUP_EXCHANGE = "A040000";							//교환 그룹 코드
	public static final String  STATE_GROUP_RETURN = "A050000";								//반품 그룹 코드
	
	public static final String  PAYMENT_STATE_END = "Y";											//추가요금 결재완료
	
	public static final String  DELIVERY_LOCAL = "ND";												//국내픽업
	public static final String  DELIVERY_EXTERNAL = "NA";											//국외특송
	public static final String  DELIVERY_CHARGE = "EW";												//추가요금

}