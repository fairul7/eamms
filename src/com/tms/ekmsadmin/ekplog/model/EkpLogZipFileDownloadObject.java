package com.tms.ekmsadmin.ekplog.model;

import kacang.model.DefaultDataObject;

public class EkpLogZipFileDownloadObject extends DefaultDataObject {
	private String zipOutputFileName = "";
	private String zipDownloadFileName = "";
	
	public String getZipDownloadFileName() {
		return zipDownloadFileName;
	}
	public void setZipDownloadFileName(String zipDownloadFileName) {
		this.zipDownloadFileName = zipDownloadFileName;
	}
	public String getZipOutputFileName() {
		return zipOutputFileName;
	}
	public void setZipOutputFileName(String zipOutputFileName) {
		this.zipOutputFileName = zipOutputFileName;
	}
}
