package com.tms.collab.isr.permission.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.FormField;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.Validator;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.isr.permission.model.ISRGroup;
import com.tms.collab.isr.permission.model.PermissionModel;
import com.tms.collab.isr.ui.ValidatorNotEmptySelectBox;
import com.tms.hr.orgChart.ui.HierachyUserSelectBox;

public class PermissionForm extends Form {
	public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
	protected TextField groupName;
	protected SelectBox role;
	protected CheckBox active;
	protected HierachyUserSelectBox userSelectBox;
	protected ValidatorUniqueGroupName validateUniqueGroupName;
	
	protected CheckBox viewProcessRequest;
	protected CheckBox clarification;
	protected CheckBox assign;
	protected CheckBox forceClosure;
	protected CheckBox newRequest;
	protected CheckBox editRequest;
	protected CheckBox withdrawRequest;
	protected CheckBox viewRequest;
	protected CheckBox generalReports;
	protected CheckBox departmentReports;
	protected CheckBox accessSetup;
	protected CheckBox accessGroupPermission;
	
	protected Button btnSubmit;
	protected Button btnCancel;
	
	public void init(){
		initForm();
	}
	
	public void initForm(){
		setMethod("POST");
		removeChildren();
		setColumns(2);
		
		Application application = Application.getInstance();
		
		groupName = new TextField("groupName");
		validateUniqueGroupName = new ValidatorUniqueGroupName("validateUniqueGroupName", application.getMessage("isr.validator.groupNameNotUnique")); 
		groupName.addChild(validateUniqueGroupName);
		groupName.addChild(new ValidatorNotEmpty("groupNameVNE", application.getMessage("isr.validator.groupNameNotEmpty")));
		
		role = new SelectBox("role");
		role.setMultiple(false);
		role.setOptionMap(getRoleOptionList());
		role.addChild(new ValidatorNotEmptySelectBox("roleVNE", application.getMessage("isr.validator.roleNotEmpty")));
		
		active = new CheckBox("active", "");
		active.setChecked(true);
		
		userSelectBox = new HierachyUserSelectBox("userSelectBox");

        Panel permissionPanel = new Panel("permissionPanel");
        permissionPanel.setColumns(1);
        
        Panel p1 = new Panel("p1");
        p1.setColumns(2);
        viewProcessRequest = new CheckBox("viewProcessRequest", application.getMessage(ISRGroup.PERM_VIEW_PROCESS_REQUEST));
        viewProcessRequest.setMouseOverText(application.getMessage(ISRGroup.PERM_VIEW_PROCESS_REQUEST + ".desc"));
        p1.addChild(viewProcessRequest);
        clarification = new CheckBox("clarification", application.getMessage(ISRGroup.PERM_CLARIFICATION));
        clarification.setMouseOverText(application.getMessage(ISRGroup.PERM_CLARIFICATION + ".desc"));
        p1.addChild(clarification);
        
        Panel p3 = new Panel("p3");
        p3.setColumns(2);
        assign = new CheckBox("assign", application.getMessage(ISRGroup.PERM_ASSIGN));
        assign.setMouseOverText(application.getMessage(ISRGroup.PERM_ASSIGN + ".desc"));
        p3.addChild(assign);
        forceClosure = new CheckBox("forceClosure", application.getMessage(ISRGroup.PERM_FORCE_CLOSURE_MANAGEMENT));
        forceClosure.setMouseOverText(application.getMessage(ISRGroup.PERM_FORCE_CLOSURE_MANAGEMENT + ".desc"));
        p3.addChild(forceClosure);
        newRequest = new CheckBox("newRequest", application.getMessage(ISRGroup.PERM_NEW_REQUEST));
        newRequest.setMouseOverText(application.getMessage(ISRGroup.PERM_NEW_REQUEST + ".desc"));
        p3.addChild(newRequest);
        editRequest = new CheckBox("editRequest", application.getMessage(ISRGroup.PERM_EDIT_REQUEST));
        editRequest.setMouseOverText(application.getMessage(ISRGroup.PERM_EDIT_REQUEST + ".desc"));
        p3.addChild(editRequest);
        withdrawRequest = new CheckBox("withdrawRequest", application.getMessage(ISRGroup.PERM_WITHDRAW_REQUEST));
        withdrawRequest.setMouseOverText(application.getMessage(ISRGroup.PERM_WITHDRAW_REQUEST + ".desc"));
        p3.addChild(withdrawRequest);
        viewRequest = new CheckBox("viewRequest", application.getMessage(ISRGroup.PERM_VIEW_REQUEST));
        viewRequest.setMouseOverText(application.getMessage(ISRGroup.PERM_VIEW_REQUEST + ".desc"));
        p3.addChild(viewRequest);
        generalReports = new CheckBox("generalReports", application.getMessage(ISRGroup.PERM_GENERAL_REPORTS));
        generalReports.setMouseOverText(application.getMessage(ISRGroup.PERM_GENERAL_REPORTS + ".desc"));
        p3.addChild(generalReports);
        departmentReports = new CheckBox("departmentReports", application.getMessage(ISRGroup.PERM_DEPARTMENT_REPORTS));
        departmentReports.setMouseOverText(application.getMessage(ISRGroup.PERM_DEPARTMENT_REPORTS + ".desc"));
        p3.addChild(departmentReports);
        accessSetup = new CheckBox("accessSetup", application.getMessage(ISRGroup.PERM_ACCESS_SETUP));
        accessSetup.setMouseOverText(application.getMessage(ISRGroup.PERM_ACCESS_SETUP + ".desc"));
        p3.addChild(accessSetup);
        accessGroupPermission = new CheckBox("accessGroupPermission", application.getMessage(ISRGroup.PERM_ACCESS_GROUP_PERMISSION));
        accessGroupPermission.setMouseOverText(application.getMessage(ISRGroup.PERM_ACCESS_GROUP_PERMISSION + ".desc"));
        p3.addChild(accessGroupPermission);
        
        permissionPanel.addChild(p1);
        permissionPanel.addChild(p3);

        Panel btnPanel = new Panel("btnPanel");
        btnPanel.setColumns(2);
        
        btnSubmit = new Button("btnSubmit", application.getMessage("isr.label.submit"));
        btnSubmit.setOnClick("return confirmSubmit()");
        btnPanel.addChild(btnSubmit);
        
        btnCancel = new Button(Form.CANCEL_FORM_ACTION, application.getMessage("isr.label.cancel"));
        btnCancel.setOnClick("return confirmCancel()");
        btnPanel.addChild(btnCancel);
        
        Label lblGroupName = new Label("lblGroupName", "<span class=\"fieldTitle\">" + application.getMessage("isr.label.groupName", "Group Name") + " *" + "</span>");
        lblGroupName.setAlign("right");
		addChild(lblGroupName);
		addChild(groupName);
		Label lblRole = new Label("lblRole", "<span class=\"fieldTitle\">" + application.getMessage("isr.label.role", "Role") + " *" + "</span>");
		lblRole.setAlign("right");
		addChild(lblRole);
		addChild(role);
		Label lblActive = new Label("lblActive", "<span class=\"fieldTitle\">" + application.getMessage("isr.label.active", "Active") + "</span>");
		lblActive.setAlign("right");
		addChild(lblActive);
		addChild(active);
		Label lblSelectUser = new Label("lblSelectUser", "<span class=\"fieldTitle\">" + application.getMessage("isr.label.selectUsers", "Select Users") + "</span>");
		lblSelectUser.setAlign("right");
        addChild(lblSelectUser);
        addChild(userSelectBox);
        Label lblPermission = new Label("lblPermission", "<span class=\"fieldTitle\">" + application.getMessage("isr.label.permissions", "Permissions") + "</span>");
        lblPermission.setAlign("right");
        addChild(lblPermission);
        addChild(permissionPanel);
        addChild(new Label("dummyLbl", ""));
        addChild(btnPanel);
        
        userSelectBox.init();
	}
	
	private Map getRoleOptionList() {
    	Map optionList = new SequencedHashMap();
    	Application application = Application.getInstance();
    	
    	optionList.put("", "---" + application.getMessage("isr.label.selectRole") + "---");
    	optionList.put(ISRGroup.ROLE_DEPT_ADMIN, application.getMessage(ISRGroup.ROLE_DEPT_ADMIN));
    	optionList.put(ISRGroup.ROLE_SECTION_ADMIN, application.getMessage(ISRGroup.ROLE_SECTION_ADMIN));
    	optionList.put(ISRGroup.ROLE_STAFF, application.getMessage(ISRGroup.ROLE_STAFF));
    	
    	return optionList;
    }
	
	public Forward onSubmit(Event event) {
    	Forward forward = super.onSubmit(event);
        
        return forward;
    }
	
	public Forward onValidate(Event event) {
		PermissionModel model = (PermissionModel)Application.getInstance().getModule(PermissionModel.class);
    	
    	String[] userIds = userSelectBox.getIds();
    	ArrayList permission = getSelectedPermission();
    	String selectedRole = (String) role.getSelectedOptions().keySet().iterator().next();
    	
    	ISRGroup gp = new ISRGroup();
    	gp.setId(UuidGenerator.getInstance().getUuid());
    	gp.setGroupName((String)groupName.getValue());
    	gp.setRole(selectedRole);
    	gp.setActive(active.isChecked());
    	gp.setPermission(permission);
    	gp.setUserIds(userIds);
    	
    	if(model.addGroup(gp)) {
    		return new Forward(FORWARD_SUCCESS);
    	}
    	else {
    		return new Forward(FORWARD_ERROR);
    	}
	}
	
	public ArrayList getSelectedPermission(){
		ArrayList permission = new ArrayList();
		if(viewProcessRequest.isChecked()){
			permission.add(ISRGroup.PERM_VIEW_PROCESS_REQUEST);
		}
		if(clarification.isChecked()){
			permission.add(ISRGroup.PERM_CLARIFICATION);
		}
		if(assign.isChecked()){
			permission.add(ISRGroup.PERM_ASSIGN);
		}
		if(forceClosure.isChecked()){
			permission.add(ISRGroup.PERM_FORCE_CLOSURE_MANAGEMENT);
		}
		if(newRequest.isChecked()){
			permission.add(ISRGroup.PERM_NEW_REQUEST);
		}
		if(editRequest.isChecked()){
			permission.add(ISRGroup.PERM_EDIT_REQUEST);
		}
		if(withdrawRequest.isChecked()){
			permission.add(ISRGroup.PERM_WITHDRAW_REQUEST);
		}
		if(viewRequest.isChecked()){
			permission.add(ISRGroup.PERM_VIEW_REQUEST);
		}
		if(generalReports.isChecked()){
			permission.add(ISRGroup.PERM_GENERAL_REPORTS);
		}
		if(departmentReports.isChecked()){
			permission.add(ISRGroup.PERM_DEPARTMENT_REPORTS);
		}
		if(accessSetup.isChecked()){
			permission.add(ISRGroup.PERM_ACCESS_SETUP);
		}
		if(accessGroupPermission.isChecked()){
			permission.add(ISRGroup.PERM_ACCESS_GROUP_PERMISSION);
		}
		return permission;
	}

	public CheckBox getAccessGroupPermission() {
		return accessGroupPermission;
	}

	public CheckBox getAccessSetup() {
		return accessSetup;
	}

	public CheckBox getActive() {
		return active;
	}

	public CheckBox getAssign() {
		return assign;
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public Button getBtnSubmit() {
		return btnSubmit;
	}

	public CheckBox getClarification() {
		return clarification;
	}

	public CheckBox getDepartmentReports() {
		return departmentReports;
	}

	public CheckBox getEditRequest() {
		return editRequest;
	}

	public CheckBox getForceClosure() {
		return forceClosure;
	}

	public CheckBox getGeneralReports() {
		return generalReports;
	}

	public TextField getGroupName() {
		return groupName;
	}

	public CheckBox getNewRequest() {
		return newRequest;
	}

	public SelectBox getRole() {
		return role;
	}

	public HierachyUserSelectBox getUserSelectBox() {
		return userSelectBox;
	}

	public CheckBox getViewProcessRequest() {
		return viewProcessRequest;
	}

	public CheckBox getViewRequest() {
		return viewRequest;
	}

	public CheckBox getWithdrawRequest() {
		return withdrawRequest;
	}
	
	protected class ValidatorUniqueGroupName extends Validator {
		private String oriValue = null;
		
		public ValidatorUniqueGroupName(String name, String text) {
	        super(name);
	        setText(text);
	    }

		public String getOriValue() {
			return oriValue;
		}

		public void setOriValue(String oriValue) {
			this.oriValue = oriValue;
		}
		
		public boolean validate(FormField formField) {
			boolean validationPass = true;
			
			Object value = formField.getValue();
	        if(value != null)
	        {
	            String strValue = null;
	            if(value instanceof Collection)
	            {
	                if(((Collection) value).size() > 0)
	                	strValue = (String) ((Collection) value).iterator().next();
	            }
	            else if(value instanceof Map)
	            {
	                if(((Map) value).keySet().size() > 0)
	                	strValue = (String) ((Map) value).keySet().iterator().next();
	            }
	            else if(value instanceof String)
	            	strValue = (String) value;
	            
	            if(strValue != null && 
	            		!"".equals(strValue)) {
	            	if(! strValue.toLowerCase().equals(oriValue)) {
	            		PermissionModel model = (PermissionModel)Application.getInstance().getModule(PermissionModel.class);
	            		if(model.isGroupNameExist(strValue)) {
	            			validationPass = false;
	            		}
	            	}
	            }
	        }
	        
	        return validationPass;
	    }
	}
}
