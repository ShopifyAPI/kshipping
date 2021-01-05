package com.shopify.shipment;

import java.util.Date;

import lombok.Data;

@Data
public class TrackingData {
	private String masterCode;
	private String tracking;
	private String state;
	private String tranType;
	private String trackingNo;
}
