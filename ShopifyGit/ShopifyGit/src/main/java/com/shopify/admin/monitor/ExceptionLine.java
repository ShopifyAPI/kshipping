package com.shopify.admin.monitor;

import lombok.Data;

@Data
public class ExceptionLine {

	private int id;
	private String instName;
	private String fileName;
	private int lineNo;
	private String dateLine;
	private String type;
	private String className;
	private String content;
	
//	public ExceptionLine(String instName, String fileName) {
//		this.instName = instName;
//		this.fileName = fileName;
//	}
//
//	public ExceptionLine() {
//		// TODO Auto-generated constructor stub
//	}
}
