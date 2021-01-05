package com.shopify.common.file;
import lombok.Data;

@Data
public class FileUploadData {
	private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
	
    public FileUploadData(String fileName, String fileDownloadUri, String fileType, long size) {
    	this.fileName = fileName;
    	this.fileDownloadUri = fileDownloadUri;
    	this.fileType = fileType;
    	this.size = size;
    }
}