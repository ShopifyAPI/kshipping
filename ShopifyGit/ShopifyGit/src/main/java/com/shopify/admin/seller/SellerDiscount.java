package com.shopify.admin.seller;

import java.util.ArrayList;
import java.util.List;

import com.shopify.shop.ShopData;

import lombok.Data;

@Data
public class SellerDiscount {
	
	private String email;
	private List<String> shopNameList = new ArrayList<>();
	private List<String> domainList = new ArrayList<>();
	
	private String id;
	private String code;
	
	private String startDate;
	private String endDate;
	
	private String[] array;

	
	public SellerDiscount(ShopData shop, int size) {
		super();
		this.email = shop.getEmail();
		addShopName(shop.getShopName());
		addDomain(shop.getDomain());
		this.startDate = "";
		this.endDate = "";
		this.array = new String[size];
	}
	
	public SellerDiscount(ShopData shop, String startDate, String endDate, int size) {
		super();
		this.email = shop.getEmail();
		addShopName(shop.getShopName());
		addDomain(shop.getDomain());
		
		this.startDate = startDate;
		this.endDate = endDate;
		this.array = new String[size];
	}

//	public SellerDiscount(String id, String code, String startDate, String endDate, int size) {
//		this.id = id;
//		this.code = code;
//		this.startDate = startDate;
//		this.endDate = endDate;
//		this.array = new String[size];
//	}

	public void setValue(int idx, int discount) {
		this.array[idx] = Integer.toString(discount);
//		this.filled = true;
		
	}
	
	
	public void addDomain(String domain) {
		domainList.add(domain);		
	}
	
	public void addShopName(String shopName) {
		shopNameList.add(shopName);		
	}
	
	public String getAllDomains() {
		return String.join("<br/>", domainList);
	}
	
	public String getAllShopNames() {
		return String.join("<br/>", shopNameList);
	}

}
