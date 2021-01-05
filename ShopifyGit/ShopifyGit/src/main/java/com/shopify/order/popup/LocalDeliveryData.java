package com.shopify.order.popup;

import java.util.Date;

import lombok.Data;

@Data
public class LocalDeliveryData {
		private int idx;
		private String id;
		private String code;
		private String zone;
		private int weight;
		private int price;
		private String startDate;
		private String endDate;
		private String useYn;
		private String codeKname;
		private String codeEname;
		private String codeName;
		private String regDate;
		private String locale;
		private String courierName;
		private String courierId;
		
		private String nowDate;
}
    
