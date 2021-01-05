package com.shopify.api.lotte.delivery;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class LotteDeliveryResultList {
	
	private String status;			  // success / 
	private String message;				// Invalid Token Error
	private String code;				// EGTA4011 /
	
	private List<LotteDeliveryResult> rtnList = new ArrayList<>();
	
	
	//------------------------
	public int size() {
		return rtnList.size();
	}
	
	public LotteDeliveryResult get(int idx) {
		if ( idx < rtnList.size() ) {
			return rtnList.get(idx);
		} else {
			return null;
		}
	}
}
