package com.shopify.api.lotte.delivery;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

/**
 * @author SOLUGATE
 * 롯데 택배 API 연동 DATA
 */

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class LotteDeliveryDataList {
	
	private List<LotteDeliveryData> sndList = new ArrayList<>();
	
	public void addList(LotteDeliveryData data) {
		sndList.add(data);
	}
}
