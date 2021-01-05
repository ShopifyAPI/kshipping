package com.shopify.api;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class ShopifyOutApiDataCarrierRate implements Serializable{
	private static final long serialVersionUID = 1L;
	//in param
	private ShopifyOutApiDataCarrierOrigin origin;
	private ShopifyOutApiDataCarrierDestination destination;
	private List<ShopifyOutApiDataCarrierItems> items;
	private String currency;
	private String locale;
}