package com.shopify.admin.seller;

import java.util.List;

import com.shopify.order.popup.OrderCourierData;

import lombok.Data;

@Data
public class SellerDiscountDateData {

	private String email;
	private String startDate;
	private String endDate;
	private String paymentMethod;
	private String shopStatus;
	private List<OrderCourierData> discountList;
}
