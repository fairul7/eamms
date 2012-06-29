package com.tms.sam.po.permission.ui;

import java.util.ArrayList;

import kacang.Application;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.sam.po.permission.model.POGroup;
import com.tms.sam.po.permission.model.PermissionModel;

public class EditPermission extends PermissionForm {
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
		POGroup po = new POGroup();
		po = model.getGroup(groupId);
		
		groupName.setValue(po.getGroupName());
		validateUniqueGroupName.setOriValue(po.getGroupName().toLowerCase());
		
		active.setChecked(po.isActive());

		if(po.getUserIds() != null) {
			if(po.getUserIds().length > 0) {
				userSelectBox.setIds(po.getUserIds());
			}
		}
		
		ArrayList permission = new ArrayList();
		permission = po.getPermission();
		
		if(permission != null && permission.size() > 0){
			for(int i=0; i<permission.size(); i++){
				if(((String)permission.get(i)).equals(POGroup.PERM_ACCESS_SETUP)){
					accessSetup.setChecked(true);
				}
				else if(((String)permission.get(i)).equals(POGroup.PERM_APPROVE_BUDGET)){
					approveBudget.setChecked(true);
				}
				else if(((String)permission.get(i)).equals(POGroup.PERM_MANAGE_QUOTATION)){
					manageQuotation.setChecked(true);
				}
				else if(((String)permission.get(i)).equals(POGroup.PERM_SUBMIT_NEW_REQUEST)){
					submitNewRequest.setChecked(true);
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
	    
	    	POGroup po = new POGroup();
	    	po.setId(groupId);
	    	po.setGroupName((String)groupName.getValue());
	    	po.setActive(active.isChecked());
	    	po.setPermission(permission);
	    	po.setUserIds(userIds);
	    	
	    	if(model.updateGroup(po)) {
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
