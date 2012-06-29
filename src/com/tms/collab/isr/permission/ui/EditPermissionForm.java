package com.tms.collab.isr.permission.ui;

import java.util.ArrayList;

import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Forward;
import com.tms.collab.isr.permission.model.ISRGroup;
import com.tms.collab.isr.permission.model.PermissionModel;

public class EditPermissionForm extends PermissionForm {
	private String groupId = "";
	
	public void init() {
		super.init();
	}
	
	public void onRequest(Event ev) {
		if(groupId != null && 
				!"".equals(groupId)){
			populateForm();
		}
	}
	
	public void populateForm() {
		PermissionModel model = (PermissionModel)Application.getInstance().getModule(PermissionModel.class);
		ISRGroup gp = new ISRGroup();
		gp = model.getGroup(groupId);
		
		groupName.setValue(gp.getGroupName());
		validateUniqueGroupName.setOriValue(gp.getGroupName().toLowerCase());
		role.setSelectedOption(gp.getRole());
		active.setChecked(gp.getActive());

		if(gp.getUserIds() != null) {
			if(gp.getUserIds().length > 0) {
				userSelectBox.setIds(gp.getUserIds());
			}
		}
		
		ArrayList permission = new ArrayList();
		permission = gp.getPermission();
		
		if(permission != null && permission.size() > 0){
			for(int i=0; i<permission.size(); i++){
				if(((String)permission.get(i)).equals(ISRGroup.PERM_VIEW_PROCESS_REQUEST)){
					viewProcessRequest.setChecked(true);
				}
				else if(((String)permission.get(i)).equals(ISRGroup.PERM_CLARIFICATION)){
					clarification.setChecked(true);
				}
				else if(((String)permission.get(i)).equals(ISRGroup.PERM_ASSIGN)){
					assign.setChecked(true);
				}
				else if(((String)permission.get(i)).equals(ISRGroup.PERM_FORCE_CLOSURE_MANAGEMENT)){
					forceClosure.setChecked(true);
				}
				else if(((String)permission.get(i)).equals(ISRGroup.PERM_NEW_REQUEST)){
					newRequest.setChecked(true);
				}
				else if(((String)permission.get(i)).equals(ISRGroup.PERM_EDIT_REQUEST)){
					editRequest.setChecked(true);
				}
				else if(((String)permission.get(i)).equals(ISRGroup.PERM_WITHDRAW_REQUEST)){
					withdrawRequest.setChecked(true);
				}
				else if(((String)permission.get(i)).equals(ISRGroup.PERM_VIEW_REQUEST)){
					viewRequest.setChecked(true);
				}
				else if(((String)permission.get(i)).equals(ISRGroup.PERM_GENERAL_REPORTS)){
					generalReports.setChecked(true);
				}
				else if(((String)permission.get(i)).equals(ISRGroup.PERM_DEPARTMENT_REPORTS)){
					departmentReports.setChecked(true);
				}
				else if(((String)permission.get(i)).equals(ISRGroup.PERM_ACCESS_SETUP)){
					accessSetup.setChecked(true);
				}
				else if(((String)permission.get(i)).equals(ISRGroup.PERM_ACCESS_GROUP_PERMISSION)){
					accessGroupPermission.setChecked(true);
				}
			}
		}
	}
	
	public Forward onValidate(Event event) {
		if(groupId != null && 
				!"".equals(groupId)){
			PermissionModel model = (PermissionModel)Application.getInstance().getModule(PermissionModel.class);
	    	
	    	String[] userIds = userSelectBox.getIds();
	    	ArrayList permission = getSelectedPermission();
	    	String selectedRole = (String) role.getSelectedOptions().keySet().iterator().next();
	    	
	    	ISRGroup gp = new ISRGroup();
	    	gp.setId(groupId);
	    	gp.setGroupName((String)groupName.getValue());
	    	gp.setRole(selectedRole);
	    	gp.setActive(active.isChecked());
	    	gp.setPermission(permission);
	    	gp.setUserIds(userIds);
	    	
	    	if(model.updateGroup(gp)) {
	    		return new Forward(FORWARD_SUCCESS);
	    	}
	    	else {
	    		return new Forward(FORWARD_ERROR);
	    	}
		}
		else {
			return new Forward(FORWARD_ERROR);
		}
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}
