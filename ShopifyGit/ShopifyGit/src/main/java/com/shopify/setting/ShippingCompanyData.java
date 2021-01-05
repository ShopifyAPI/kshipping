package com.shopify.setting;

import lombok.Data;

@Data
public class ShippingCompanyData {
	
	private String id;
	private String code;
	private String courier;
	private String codeName;
	private int divisor;

}
