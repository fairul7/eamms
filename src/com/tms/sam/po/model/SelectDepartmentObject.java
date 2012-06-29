package com.tms.sam.po.model;

import kacang.model.DataSourceDao;

public class SelectDepartmentObject extends DataSourceDao {
    private String deptID = "";
    private String deptName = "";
    
    //getter and setter -------------------------
	public String getDeptID() {
		return deptID;
	}
	public void setDeptID(String deptID) {
		this.deptID = deptID;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}   
}