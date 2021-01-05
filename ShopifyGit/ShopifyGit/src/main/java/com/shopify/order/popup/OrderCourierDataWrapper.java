package com.shopify.order.popup;

public class OrderCourierDataWrapper extends OrderCourierData {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getComCode() == null) ? 0 : getComCode().hashCode());
		result = prime * result + ((getCourierId() == null) ? 0 : getCourierId().hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		OrderCourierDataWrapper other = (OrderCourierDataWrapper) obj;
		if (getComCode() == null) {
			if (other.getComCode() != null)
				return false;
		} else if (!getComCode().equals(other.getComCode()))
			return false;
		if (getCourierId() == null) {
			if (other.getCourierId() != null)
				return false;
		} else if (!getCourierId().equals(other.getCourierId()))
			return false;
		
		return true;
	}
	
	
}
