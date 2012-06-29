package com.tms.sam.po.permission.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.FormField;
import kacang.stdui.Panel;
import kacang.stdui.TextField;
import kacang.stdui.validator.Validator;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import com.tms.hr.orgChart.ui.HierachyUserSelectBox;
import com.tms.sam.po.permission.model.POGroup;
import com.tms.sam.po.permission.model.PermissionModel;

public class PermissionForm extends Form {
	public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
	protected TextField groupName;
	
	protected CheckBox active;
	protected HierachyUserSelectBox userSelectBox;
	//protected ValidatorUniqueGroupName validateUniqueGroupName;
	
	protected CheckBox submitNewRequest;
	protected CheckBox approveBudget;
	protected CheckBox manageQuotation;
	protected CheckBox accessSetup;
	
	protected Button btnSubmit;
	protected Button btnCancel;
	
	protected ValidatorUniqueGroupName validateUniqueGroupName;
	
	protected Panel permissionPanel;
	protected Panel btnPanel;
	
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
		groupName.addChild(new ValidatorNotEmpty("groupNameVNE", ""/*application.getMessage("po.validator.countryNameNotUnique")*/));
		
		active = new CheckBox("active", "");
		active.setChecked(true);
		
		userSelectBox = new HierachyUserSelectBox("userSelectBox");

        permissionPanel = new Panel("permissionPanel");
        permissionPanel.setColumns(1);
        
        Panel p1 = new Panel("p1");
        p1.setColumns(2);
        submitNewRequest = new CheckBox("submitNewRequest", application.getMessage(POGroup.PERM_SUBMIT_NEW_REQUEST));
        submitNewRequest.setMouseOverText(application.getMessage(POGroup.PERM_SUBMIT_NEW_REQUEST + ".decs"));
        p1.addChild(submitNewRequest);
        
        approveBudget = new CheckBox("approveBudget", application.getMessage(POGroup.PERM_APPROVE_BUDGET));
        approveBudget.setMouseOverText(application.getMessage(POGroup.PERM_APPROVE_BUDGET + ".decs"));
        p1.addChild(approveBudget);
        
        manageQuotation = new CheckBox("manageQuotation", application.getMessage(POGroup.PERM_MANAGE_QUOTATION));
        manageQuotation.setMouseOverText(application.getMessage(POGroup.PERM_MANAGE_QUOTATION + ".decs"));
        p1.addChild(manageQuotation);
        
        accessSetup = new CheckBox("accessSetup", application.getMessage(POGroup.PERM_ACCESS_SETUP));
        accessSetup.setMouseOverText(application.getMessage(POGroup.PERM_ACCESS_SETUP + ".decs"));
        p1.addChild(accessSetup);
        
        permissionPanel.addChild(p1);
        
        btnPanel = new Panel("btnPanel");
        btnPanel.setColumns(2);
        
        btnSubmit = new Button("btnSubmit", application.getMessage("po.label.submit"));
        btnPanel.addChild(btnSubmit);
        
        btnCancel = new Button(Form.CANCEL_FORM_ACTION, application.getMessage("po.label.cancel"));
        btnPanel.addChild(btnCancel);
        
        
       
		addChild(groupName);
		addChild(active);
        addChild(userSelectBox);
        addChild(permissionPanel);
        addChild(btnPanel);
        
        userSelectBox.init();
	}
	
	public Forward onSubmit(Event event) {
    	Forward forward = super.onSubmit(event);
        
        return forward;
    }
	
	public Forward onValidate(Event event) {
		PermissionModel model = (PermissionModel)Application.getInstance().getModule(PermissionModel.class);
    	
    	String[] userIds = userSelectBox.getIds();
    	ArrayList permission = getSelectedPermission();
    	
    	POGroup po = new POGroup();
    	po.setId(UuidGenerator.getInstance().getUuid());
    	po.setGroupName((String)groupName.getValue());
    	po.setActive(active.isChecked());
    	po.setPermission(permission);
    	po.setUserIds(userIds);
    	
    	if(model.addGroup(po)) {
    		return new Forward(FORWARD_SUCCESS);
    	}
    	else {
    		return new Forward(FORWARD_ERROR);
    	}
	}
	
	public ArrayList getSelectedPermission(){
		ArrayList permission = new ArrayList();
		if(submitNewRequest.isChecked()){
			permission.add(POGroup.PERM_SUBMIT_NEW_REQUEST);
		}
		if(approveBudget.isChecked()){
			permission.add(POGroup.PERM_APPROVE_BUDGET);
		}
		if(manageQuotation.isChecked()){
			permission.add(POGroup.PERM_MANAGE_QUOTATION);
		}
		if(accessSetup.isChecked()){
			permission.add(POGroup.PERM_ACCESS_SETUP);
		}
		return permission;
	}
	
	public String getTemplate() {
		return "po/addPermission";
	}

	public CheckBox getAccessSetup() {
		return accessSetup;
	}

	public void setAccessSetup(CheckBox accessSetup) {
		this.accessSetup = accessSetup;
	}

	public CheckBox getActive() {
		return active;
	}

	public void setActive(CheckBox active) {
		this.active = active;
	}

	public CheckBox getApproveBudget() {
		return approveBudget;
	}

	public void setApproveBudget(CheckBox approveBudget) {
		this.approveBudget = approveBudget;
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public void setBtnCancel(Button btnCancel) {
		this.btnCancel = btnCancel;
	}

	public Panel getBtnPanel() {
		return btnPanel;
	}

	public void setBtnPanel(Panel btnPanel) {
		this.btnPanel = btnPanel;
	}

	public Button getBtnSubmit() {
		return btnSubmit;
	}

	public void setBtnSubmit(Button btnSubmit) {
		this.btnSubmit = btnSubmit;
	}

	public TextField getGroupName() {
		return groupName;
	}

	public void setGroupName(TextField groupName) {
		this.groupName = groupName;
	}

	public CheckBox getManageQuotation() {
		return manageQuotation;
	}

	public void setManageQuotation(CheckBox manageQuotation) {
		this.manageQuotation = manageQuotation;
	}

	public Panel getPermissionPanel() {
		return permissionPanel;
	}

	public void setPermissionPanel(Panel permissionPanel) {
		this.permissionPanel = permissionPanel;
	}

	public CheckBox getSubmitNewRequest() {
		return submitNewRequest;
	}

	public void setSubmitNewRequest(CheckBox submitNewRequest) {
		this.submitNewRequest = submitNewRequest;
	}

	public HierachyUserSelectBox getUserSelectBox() {
		return userSelectBox;
	}

	public void setUserSelectBox(HierachyUserSelectBox userSelectBox) {
		this.userSelectBox = userSelectBox;
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
