package com.tms.ekmsadmin.ekplog.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class EkpLogObject extends DefaultDataObject {
	private String fileName = "";
	private String singleFileDownloadUrl = "";
	private Date lastUpdatedDate = null;
	private long fileSize = 0;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public String getSingleFileDownloadUrl() {
		return singleFileDownloadUrl;
	}
	public void setSingleFileDownloadUrl(String singleFileDownloadUrl) {
		this.singleFileDownloadUrl = singleFileDownloadUrl;
	}	
}
