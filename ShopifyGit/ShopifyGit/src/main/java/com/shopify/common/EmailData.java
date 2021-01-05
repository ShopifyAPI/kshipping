package com.shopify.common;

import lombok.Data;

@Data
public class EmailData {
	private int id;
	String toEmail;
	String subject;
	String content;
	boolean result;
}
