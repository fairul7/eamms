package com.tms.fms.department.ui;

import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.Button;
import kacang.stdui.ButtonGroup;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Radio;
import kacang.stdui.SelectBox;
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
import com.tms.fms.department.model.FMSUnit;


public class FMSUnitForm extends Form {
    
	protected String id;    
	private TextField unitName;
	protected TextBox description;
		
	protected CalendarUsersSelectBox approver;
	
	protected PopupHODSelectBox hou;
	
	protected SelectBox department;
	protected Radio radioActive;
	protected Radio radioInactive;
	private ButtonGroup statusUnit;
	
	protected Button submitButton, cancelButton;    	    
    private Label person;
    private boolean isEdit = false;
    
    public FMSUnitForm() {

    }
   
	public void init()
	{
		setMethod("post");
		unitName = new TextField("unitName");
		unitName.setSize("60");     
		unitName.addChild(new ValidatorNotEmpty("vEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));
		
		description = new TextBox("description");
		description.setSize("60");
		
		hou = new PopupHODSelectBox("hou");
		hou.addChild(new ValidatorNotEmpty("vEmpty", Application.getInstance().getMessage("asset.message.vNotEmpty")));	        
		
		submitButton = new Button("submitButton");
        submitButton.setText(Application.getInstance().getMessage("calendar.label.submit", "Submit"));
        
        cancelButton = new Button("cancelButton");
        cancelButton.setText(Application.getInstance().getMessage("calendar.label.cancel", "Cancel"));
        
        
        radioActive = new Radio("radioActive", Application.getInstance().getMessage("security.label.active", "Active"));             
        radioActive.setChecked(true);
        radioInactive = new Radio("radioInactive", Application.getInstance().getMessage("general.label.inactive", "Inactive"));
        
        statusUnit = new ButtonGroup("statusUnit", new Radio[]{radioActive, radioInactive});
        
        approver = new CalendarUsersSelectBox("approver");
        
        department = new SelectBox("department");
		Map cmap = null;
        try {
            cmap = ((FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class)).getFMSDepartments();
        }
        catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        for (Iterator i = cmap.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();
            department.addOption(key, (String) cmap.get(key));
        }
	 
		addChild(unitName);
		addChild(description);
		hou.init();
		addChild(hou);
		approver.init();
		addChild(approver);		
		addChild(submitButton);
		addChild(cancelButton);		
		addChild(radioActive);
		addChild(radioInactive);
		addChild(department);
		
		//hod.init();
		
		
	}	 
	
	
	public String getDefaultTemplate() {    
			
		return "fms/unitform";
			
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
		FMSUnit fmsunit= new FMSUnit();
		
		Forward fwd = new Forward(manager.BACK_TO_DEPT_LIST);
		if(!isEdit){
			try{			
				fmsunit.setId(UuidGenerator.getInstance().getUuid());
				fmsunit.setName((String)unitName.getValue());
				fmsunit.setHOU(hou.getId());
				fmsunit.setDeptApprover(approver.getIds());				
				fmsunit.setDescription((String)description.getValue());
				fmsunit.setDepartment_id((String)department.getSelectedOptions().keySet().iterator().next());
				fmsunit.setStatus(radioActive.isChecked() ? manager.ACTIVE_DEPT : manager.INACTIVE_DEPT);					
				
				manager.addUnit(fmsunit);
				
			
			}catch(Exception er){
				Log.getLog(getClass()).error(" : " + er.toString(), er);				
			}
		}else{
			try{
				fmsunit.setId(getId());
				fmsunit.setName((String)unitName.getValue());
				fmsunit.setHOU(hou.getId());
				fmsunit.setDeptApprover(approver.getIds());				
				fmsunit.setDescription((String)description.getValue());
				fmsunit.setDepartment_id((String)department.getSelectedOptions().keySet().iterator().next());
				fmsunit.setStatus(radioActive.isChecked() ? manager.ACTIVE_DEPT : manager.INACTIVE_DEPT);		
				
				manager.updateUnit(fmsunit);
				
				
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
			FMSUnit funit = manager.getselectFMSUnit(id);			
			unitName.setValue(funit.getName());
			description.setValue(funit.getDescription());
			
			String[] hous = new String[1];
			hous[0] = funit.getHOU();
			hou.setIds(hous);
			
			String[] approvers = new String[funit.getDeptApprover().length];
			approvers = funit.getDeptApprover();
			approver.setIds(approvers);
			
			department.setSelectedOptions(new String[]{funit.getDepartment_id()});
			
	        if(funit.getStatus().equals(manager.ACTIVE_DEPT))
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

	public TextField getUnitName() {
		return unitName;
	}

	public void setUnitName(TextField unitName) {
		this.unitName = unitName;
	}

	public PopupHODSelectBox getHou() {
		return hou;
	}

	public void setHou(PopupHODSelectBox hou) {
		this.hou = hou;
	}

	public ButtonGroup getStatusUnit() {
		return statusUnit;
	}

	public void setStatusUnit(ButtonGroup statusUnit) {
		this.statusUnit = statusUnit;
	}

	public SelectBox getDepartment() {
		return department;
	}

	public void setDepartment(SelectBox department) {
		this.department = department;
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}
	
}



