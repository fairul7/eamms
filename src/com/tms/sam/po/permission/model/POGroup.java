package com.tms.sam.po.permission.model;

import java.util.ArrayList;

import kacang.model.DefaultDataObject;

public class POGroup extends DefaultDataObject {
	public static String PERM_SUBMIT_NEW_REQUEST = "po.permission.submitNewRequest";
	public static String PERM_ACCESS_SETUP = "po.permission.accessSetup";
	public static String PERM_APPROVE_BUDGET = "po.permission.approveBudget";
	public static String PERM_MANAGE_QUOTATION = "po.permission.manageQuotation";
	
	private String id;
	private String groupName;
	private String groupDescription;
	
	private boolean active;
	
	private String[] userIds;
	private ArrayList permission;
	public boolean isActive() {
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
	public String[] getUserIds() {
		return userIds;
	}
	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
	}
	
	
}
