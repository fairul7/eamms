package com.tms.collab.project;

import kacang.model.DefaultDataObject;

public class ReportRole extends DefaultDataObject{
	private String reportId;
    private String roleId;
    private String roleName;
    private String user;
    
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
    
    

}
