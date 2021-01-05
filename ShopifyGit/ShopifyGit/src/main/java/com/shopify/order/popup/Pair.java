package com.shopify.order.popup;

import lombok.Data;

@Data
public class Pair {

	private int divisor;
	private int divisorCount;

	public Pair(Integer divisor, int divisorCount) {
		this.divisor = divisor;
		this.divisorCount = divisorCount;
	}

}
