package com.shopify.api.lotte.delivery;

import lombok.Data;

@Data
public class ResultPair {
	
	private String data;			// 성공인 경우 data 만 set 함
	private String msg;				// 에러인 경우 msg 만 set 함

}
