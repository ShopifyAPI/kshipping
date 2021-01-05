package com.shopify.admin.price;


import lombok.Data;

/**
* Price Data
* @author : kyh
* @since  : 2020-01-20
* @desc   : /price 정보 (할인율 맵핑 관리) 
*/
@Data
public class PriceViewData {
	private String zone;
	private String price;	
}