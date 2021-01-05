package com.shopify.order.popup;

import lombok.Data;

@Data
public class VolumeticPair {
	
    private int divisor;
    private int divisorCount;
    private int volumetic;

    public VolumeticPair(Pair pair, int volumetic) {
            this.divisor = pair.getDivisor();
            this.divisorCount = pair.getDivisorCount();
            this.volumetic = volumetic;
    }

    public int getWeight() {
            return (int) Math.ceil( (double) volumetic / divisor );
    }

}
