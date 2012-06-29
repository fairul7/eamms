package com.tms.hr.orgChart.model;

import kacang.model.DefaultDataObject;

public class DepartmentCountryAssociativityObject extends DefaultDataObject {
	private String associativityId = "";
	private String deptCode = "";
	private String deptDesc = "";
	private String countryCode = "";
	private String countryDesc = "";
	
	public String getAssociativityId() {
		return associativityId;
	}
	public void setAssociativityId(String associativityId) {
		this.associativityId = associativityId;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getCountryDesc() {
		return countryDesc;
	}
	public void setCountryDesc(String countryDesc) {
		this.countryDesc = countryDesc;
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
}
