package com.shopify.order.popup;

import java.util.Date;

import lombok.Data;

@Data
public class OrderCourierData {
	private int idx;
	private String id;
	private String code;
	private String zone;
	private int weight;
	private int price;
	private String startDate;
	private String endDate;
	private String useYn;
	private Date regDate;
	private String comCode;
	private String comName;
	private String codeName;
	private String boxType;
	private int minDeliveryDate;
	private int maxDeliveryDate;

	private String serviceCode;
	private String nationCode;
	
	private int paymentIdx;
	private String masterCode;
	private String payId;
	private String paymentCode;
	private String invoice;
	private String courier;
	private String courierId;
	private String courierCompany;
	private int payment;
	private int rankPrice;
	private String payWeight;
	private String payWeightUnit;
	private String payState;
	private Date paymentDate;
	private Date paymentUpdate;
	private int salePrice;
	private int orderCourier;
	
	
	private String email;
	private String nowDate;
	private int shopIdx;
	private String shopName;
	private String orderCode;
	private String proc;
	private String locale;
	
	private String shippingLineName;
    private String shippingLineCode;

    private String codeKname;
	private String codeEname;

	private int discount;
	private int divisor;
    
}





