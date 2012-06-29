package com.tms.fms.department.model;

import java.util.Date;

import kacang.model.DefaultDataObject;

public class FMSUnit extends DefaultDataObject{
	private String id;    
    private String name;
    private String description;
    private String HOU;
    private String department_id;
    private String[] deptApprover;
    private String status;
    
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHOU() {
		return HOU;
	}

	public void setHOU(String hou) {
		HOU = hou;
	}

	public String getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}

	public String[] getDeptApprover() {
		return deptApprover;
	}

	public void setDeptApprover(String[] deptApprover) {
		this.deptApprover = deptApprover;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
