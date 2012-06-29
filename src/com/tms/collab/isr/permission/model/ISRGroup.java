package com.tms.collab.isr.permission.model;

import java.util.ArrayList;

import kacang.model.DefaultDataObject;

public class ISRGroup extends DefaultDataObject {
	public static String PERM_VIEW_PROCESS_REQUEST = "isr.permission.viewProcessRequest";
	public static String PERM_CLARIFICATION = "isr.permission.clarification";
	public static String PERM_ASSIGN = "isr.permission.assign";
	public static String PERM_FORCE_CLOSURE_MANAGEMENT = "isr.permission.forceClosureManagement";
	public static String PERM_NEW_REQUEST = "isr.permission.newRequest";
	public static String PERM_EDIT_REQUEST = "isr.permission.editRequest";
	public static String PERM_WITHDRAW_REQUEST = "isr.permission.withdrawRequest";
	public static String PERM_VIEW_REQUEST = "isr.permission.viewRequest";
	public static String PERM_GENERAL_REPORTS = "isr.permission.generalReports";
	public static String PERM_DEPARTMENT_REPORTS = "isr.permission.departmentReports";
	public static String PERM_ACCESS_SETUP = "isr.permission.accessSetup";
	public static String PERM_ACCESS_GROUP_PERMISSION = "isr.permission.accessGroupPermission";
	public static String ROLE_DEPT_ADMIN = "isr.role.deptAdmin";
	public static String ROLE_SECTION_ADMIN = "isr.role.sectionAdmin";
	public static String ROLE_STAFF = "isr.role.staff";
	public static String ROLE_SYSTEM = "isr.role.system";
	
	private String id;
	private String groupName;
	private String groupDescription;
	private String role;
	private boolean active;
	
	private String[] userIds;
	private ArrayList permission;
	
	public boolean getActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getGroupDescription() {
		return groupDescription;
	}
	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ArrayList getPermission() {
		return permission;
	}
	public void setPermission(ArrayList permission) {
		this.permission = permission;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String[] getUserIds() {
		return userIds;
	}
	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
	}
}
