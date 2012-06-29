package com.tms.sam.po.report.model;

import kacang.model.DefaultDataObject;

public class StatusReportObject extends DefaultDataObject{
	String deptDesc;
	String deptCode;
	String associativityId;
	String totalRequest;
	String status;
	
	
	public String getAssociativityId() {
		return associativityId;
	}
	public void setAssociativityId(String associativityId) {
		this.associativityId = associativityId;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptDesc() {
		return deptDesc;
	}
	public void setDeptDesc(String deptDesc) {
		this.deptDesc = deptDesc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTotalRequest() {
		return totalRequest;
	}
	public void setTotalRequest(String totalRequest) {
		this.totalRequest = totalRequest;
	}

}
