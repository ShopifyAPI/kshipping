package com.shopify.admin.delivery;

import lombok.Data;

@Data
public class DeliveryData {
	private String locale;
	private String id;
	private String idName;
	private String code;
	private String codeId;
	private String codeName;
	private String codeEtc;
	private int minDeliveryDate;
	private int maxDeliveryDate;	
	private String useYn;
	private String shipId;
	private String regDate;
	private String ckBox;
	
	private String searchId;
	private String searchWord;
	private String searchOrder;
	
	//페이징관련 param
	private int currentPage;
	private int startRow;
	private int pageSize;
	private int totalPageNum;
	private int pageBlockSize;
}
