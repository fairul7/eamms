package com.tms.fms.department.ui;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.ButtonGroup;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Radio;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.collab.calendar.ui.CalendarUsersSelectBox;
import com.tms.fms.department.model.FMSDepartment;
import com.tms.fms.department.model.FMSDepartmentManager;


public class FMSDepartmentForm extends Form {
    
	protected String id;    
	private TextField deptName;
	protected TextBox description;
	
	//protected PopupHODSelectBox hod;
	protected CalendarUsersSelectBox approver;
	
	protected PopupHODSelectBox hod;
	
	protected Radio radioActive;
	protected Radio radioInactive;
	private ButtonGroup statusDept;
	
	protected Button submitButton, cancelButton;    	    
    private Label person;
    
    private boolean isEdit = false;
    
    public FMSDepartmentForm() {

    }
   
	public void init()
	{
		setMethod("post");
		deptName = new TextField("deptName");
		deptName.setSize("60");     
		deptName.addChild(new ValidatorNotEmpty("vEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));
		
		description = new TextBox("description");
		description.setSize("60");
		
		hod = new PopupHODSelectBox("hod");
		hod.addChild(new ValidatorNotEmpty("vEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));	        
		
		submitButton = new Button("submitButton");
        submitButton.setText(Application.getInstance().getMessage("calendar.label.submit", "Submit"));
        
        cancelButton = new Button("cancelButton");
        cancelButton.setText(Application.getInstance().getMessage("calendar.label.cancel", "Cancel"));
        
        
        radioActive = new Radio("radioActive", Application.getInstance().getMessage("security.label.active", "Active"));             
        radioActive.setChecked(true);
        radioInactive = new Radio("radioInactive", Application.getInstance().getMessage("general.label.inactive", "Inactive"));
        
        statusDept = new ButtonGroup("statusDept", new Radio[]{radioActive, radioInactive});
        
        approver = new CalendarUsersSelectBox("approver");
	
		addChild(deptName);
		addChild(description);
		hod.init();
		addChild(hod);
		approver.init();
		addChild(approver);		
		addChild(submitButton);
		addChild(cancelButton);		
		addChild(radioActive);
		addChild(radioInactive);
		
		//hod.init();
		
		
	}	 
	
	
	public String getDefaultTemplate() {    
			
		return "fms/departmentform";
			
    }
	
	public Forward actionPerformed(Event event)
    {
		Forward forward = new Forward();
		FMSDepartmentManager manager = (FMSDepartmentManager)Application.getInstance().getModule(FMSDepartmentManager.class);		
		
		if(cancelButton.getAbsoluteName().equals(findButtonClicked(event))){
            init();
            return new Forward(manager.BACK_TO_DEPT_LIST);
        }   
		
		forward = super.actionPerformed(event);
        return forward;
    }
	
		
	public Forward onValidate(Event evt) {
						
		FMSDepartmentManager manager = (FMSDepartmentManager)Application.getInstance().getModule(FMSDepartmentManager.class);		
		FMSDepartment fmsdept= new FMSDepartment();
		
		Forward fwd = new Forward();
		if(!isEdit){
			try{			
				fmsdept.setId(UuidGenerator.getInstance().getUuid());
				fmsdept.setName((String)deptName.getValue());
				fmsdept.setHOD(hod.getId());
				fmsdept.setDeptApprover(approver.getIds());				
				fmsdept.setDescription((String)description.getValue());
				fmsdept.setStatus(radioActive.isChecked() ? manager.ACTIVE_DEPT : manager.INACTIVE_DEPT);				
							
				manager.addDepartment(fmsdept);
				fwd = new Forward(manager.BACK_TO_DEPT_LIST);
				
			}catch(Exception er){
				Log.getLog(getClass()).error(" : " + er.toString(), er);				
			}
		}else{
			
			try{
				fmsdept.setId(getId());
				fmsdept.setName((String)deptName.getValue());
				fmsdept.setHOD(hod.getId());
				fmsdept.setDeptApprover(approver.getIds());				
				fmsdept.setDescription((String)description.getValue());
				fmsdept.setStatus(radioActive.isChecked() ? manager.ACTIVE_DEPT : manager.INACTIVE_DEPT);	
				
				manager.updateDepartment(fmsdept);
				fwd = new Forward(manager.BACK_TO_DEPT_LIST);
				
			}catch(Exception er){
				Log.getLog(getClass()).error(" : " + er.toString(), er);				
			}
		}		
		return fwd;
	}
		
	
	public void onRequest(Event event){
		setEdit(false);
		super.onRequest(event);
		id = event.getRequest().getParameter("id");	
		if (id != null) {
			populateForm(id);
			//setEditMode(true);
		}else{
			init();
		}
		
	}
	
	public void populateForm(String id){
		
		FMSDepartmentManager manager = (FMSDepartmentManager)Application.getInstance().getModule(FMSDepartmentManager.class);
		try{
			FMSDepartment fdept = manager.getselectFMSDepartment(id);
			deptName.setValue(fdept.getName());
			description.setValue(fdept.getDescription());
			
			String[] hods = new String[1];
			hods[0] = fdept.getHOD();
			hod.setIds(hods);
			
			String[] approvers = new String[fdept.getDeptApprover().length];
			approvers = fdept.getDeptApprover();
			approver.setIds(approvers);
				        
	        if(fdept.getStatus().equals(manager.ACTIVE_DEPT))
	        	radioActive.setChecked(true);
	        else
	        	radioInactive.setChecked(true);
	        
	        setEdit(true);
			
		}catch(Exception e){
			
		}
		
		   
	}
		
	public Button getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(Button cancelButton) {
		this.cancelButton = cancelButton;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Label getPerson() {
		return person;
	}

	public void setPerson(Label person) {
		this.person = person;
	}

	public TextBox getDescription() {
		return description;
	}

	public void setDescription(TextBox description) {
		this.description = description;
	}

	/*public PopupHODSelectBox getHod() {
		return hod;
	}

	public void setHod(PopupHODSelectBox hod) {
		this.hod = hod;
	}*/

	public CalendarUsersSelectBox getApprover() {
		return approver;
	}

	public void setApprover(CalendarUsersSelectBox approver) {
		this.approver = approver;
	}	
	
	public Radio getRadioActive() {
		return radioActive;
	}

	public void setRadioActive(Radio radioActive) {
		this.radioActive = radioActive;
	}

	public Radio getRadioInactive() {
		return radioInactive;
	}

	public void setRadioInactive(Radio radioInactive) {
		this.radioInactive = radioInactive;
	}

	public Button getSubmitButton() {
		return submitButton;
	}

	public void setSubmitButton(Button submitButton) {
		this.submitButton = submitButton;
	}

	public TextField getDeptName() {
		return deptName;
	}

	public void setDeptName(TextField deptName) {
		this.deptName = deptName;
	}

	public PopupHODSelectBox getHod() {
		return hod;
	}

	public void setHod(PopupHODSelectBox hod) {
		this.hod = hod;
	}

	public ButtonGroup getStatusDept() {
		return statusDept;
	}

	public void setStatusDept(ButtonGroup statusDept) {
		this.statusDept = statusDept;
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	
}



