package com.shopify.api.pantos;

import lombok.Getter;
import lombok.Setter;

/**
 * @author SOLUGATE
 * 판토스 Tracking 연동 DATA
 */
@Getter
@Setter
public class PantosTrackingData {
	private String hblNo;
	private String statusCd;
//	private String deliveryGroupCd;
//	private String deliveryCd;
	private String eventDate;
	private int upCnt;

	public PantosTrackingData() {}

	public PantosTrackingData(String[] deliveryPantosData) {
		this.hblNo = deliveryPantosData[0];
		this.statusCd = deliveryPantosData[1];
		this.eventDate = deliveryPantosData[2];
	}

	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("hblNo : ").append(hblNo)
		.append("	statusCd : ").append(statusCd)
//		.append("	deliveryGroupCd : ").append(deliveryGroupCd)
//		.append("	deliveryCd : ").append(deliveryCd)
		.append("	eventDate : ").append(eventDate) 
		;
		
		return sb.toString();
		
	}
	
}
