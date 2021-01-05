package com.shopify.api;

import java.io.Serializable;

import lombok.Data;

@Data
public class ShopifyOutApiDataCarrierDestination implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String country;
	private String postal_code;
	private String province;
	private String city;
	private String name;
	private String address1;
	private String address2;
	private String address3;
	private String phone;
	private String fax;
	private String email;
	private String address_type;
	private String company_name;
}