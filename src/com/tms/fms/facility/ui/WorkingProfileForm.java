package com.tms.fms.facility.ui;

import java.util.Calendar;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.TextField;
import kacang.stdui.TimeField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.crm.sales.misc.DateUtil;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.facility.model.WorkingProfile;
import com.tms.fms.widgets.BoldLabel;


public class WorkingProfileForm extends Form {
	
	private Button cancel;
	private Button submit;
	private String id;
	private String type;
	private Label lbName;
	private Label lbDisplayName;
	private Label lbStartTime;
	private Label lbEndTime;
	private Label lbDefault;
	private Panel panel;
	private TextField tfName;
	private TimeField startTime;
	private TimeField endTime;
	private CheckBox defaultProfile;
	private String workingProfileId;
	
	private TextField description;

	public WorkingProfileForm() {}

	public WorkingProfileForm(String s) {super(s);}

	public void init() {
		if (type == null || (!"Add".equals(type) && !"Edit".equals(type))) {
			type = "Add";
		}
	}

	public void onRequest(Event event) {
		initForm();
		if ("Edit".equals(type)) {
			populateFields();
		}
	}
	
	@Override
	public String getDefaultTemplate() {
		if ("Edit".equals(type)){
			return "fms/workingprofileedittemp";
		} else {
			return "fms/workingprofiletemp";
		}
	}

	public void initForm() {
		setMethod("post");
		setColumns(2);
		
		Application app = Application.getInstance();
		String msgNotEmpty = app.getMessage("lms.elearning.validation.mandatoryField");
		
		lbName = new BoldLabel("lbName");
		lbName.setAlign("right");
		lbName.setText(app.getMessage("fms.facility.label.workingProfileName") + " *");
		
		lbDisplayName = new BoldLabel("lbDisplayName");
		lbDisplayName.setAlign("left");
		
		lbStartTime = new BoldLabel("lbStartTime");
		lbStartTime.setAlign("right");
		lbStartTime.setText(app.getMessage("fms.facility.label.startTime"));
		
		lbEndTime = new BoldLabel("lbEndTime");
		lbEndTime.setAlign("right");
		lbEndTime.setText(app.getMessage("fms.facility.label.endTime"));
		
		lbDefault = new BoldLabel("lbDefault");
		lbDefault.setAlign("right");
		lbDefault.setText(app.getMessage("fms.facility.label.defaultProfile"));

		tfName = new TextField("name");
		tfName.setSize("50");
		tfName.setMaxlength("255");
		tfName.addChild(new ValidatorNotEmpty("nameNotEmpty", msgNotEmpty));
		
		description = new TextField("description");
		description.setSize("50");
		description.setMaxlength("255");
				

		startTime = new TimeField("startTime");
		endTime = new TimeField("endTime");
				
		defaultProfile = new CheckBox("defaultProfile");

		panel = new Panel("panel");

		submit = new Button("submit", app.getMessage("fms.facility.submit"));

		cancel = new Button("cancel", app.getMessage("fms.facility.cancel"));
		
		
		panel.addChild(submit);
		panel.addChild(cancel);
		
		addChild(lbName);
		if ("Edit".equals(type)) {
			addChild(lbDisplayName);
		}else{
			addChild(tfName);
		}
		
		addChild(description);
		addChild(lbStartTime);
		addChild(startTime);

		addChild(lbEndTime);
		addChild(endTime);
		
		addChild(lbDefault);
		addChild(defaultProfile);
		
		addChild(new Label("blank"));
		addChild(panel);
	}

	public void populateFields() {
		
		try {
			SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
			WorkingProfile wp=module.getWorkingProfile(workingProfileId);
			if(wp!=null){
				tfName.setValue(wp.getName());
				lbDisplayName.setText(wp.getName());
				Calendar TodayDate = Calendar.getInstance();
				 java.util.Date now=TodayDate.getTime();
				 java.util.Date now1=TodayDate.getTime();
				 now.setHours(wp.getStartTimeHour());
				 now.setMinutes(wp.getStartTimeMinute());
				 startTime.setDate(now);
				 now1.setHours(wp.getEndTimeHour());
				 now1.setMinutes(wp.getEndTimeMinute());
				 endTime.setDate(now1);
				 description.setValue(wp.getDescription());
				 if(wp.isDefaultProfile()){
					 defaultProfile.setChecked(true);
				 }
				 
				 
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.toString());
		}
	}

	public Forward onSubmit(Event evt) {
		String buttonName = findButtonClicked(evt);
		kacang.ui.Forward result = super.onSubmit(evt);
		if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
			init();
			return new Forward(Form.CANCEL_FORM_ACTION, "workingProfileListing.jsp", true);
		}
		else {return result;}
	}

	public Forward onValidate(Event event) {
		
			WorkingProfile wp = new WorkingProfile();
			SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);

			wp.setWorkingProfileId(workingProfileId);
			wp.setName(tfName.getValue().toString());
			wp.setStartTime(startTime.getHour()+":"+startTime.getMinute());
			wp.setEndTime(endTime.getHour()+":"+endTime.getMinute());
			wp.setDescription((String) description.getValue());
			if(defaultProfile.isChecked()){
				
				wp.setDefaultProfile(true);
			}else{
				wp.setDefaultProfile(false);	
			}
			
			if ("Add".equals(type)) {
				try {
					if(module.isDuplicate("fms_working_profile", "name", wp.getName(), null, null)){
						tfName.setInvalid(true);
						this.setInvalid(true);
						return new Forward("EXISTS");
					}
					if(defaultProfile.isChecked()){
						if(module.isDuplicate("fms_working_profile", "defaultProfile", "1", null, null)){
							defaultProfile.setInvalid(true);
							this.setInvalid(true);
							return new Forward("defaultProfileEXISTS");
						}
					}
					module.insertWorkingProfile(wp);}
				catch (Exception e) {
					Log.getLog(getClass()).error(e.toString()); 
					return new Forward("FAILED");
				} 
			}
			if ("Edit".equals(type)) {
				try {
					if(module.isDuplicate("fms_working_profile", "name", wp.getName(), "workingProfileId", wp.getWorkingProfileId())){
						tfName.setInvalid(true);
						this.setInvalid(true);
						return new Forward("EXISTS");
					}
					if(defaultProfile.isChecked()){
						if(module.isDuplicate("fms_working_profile", "defaultProfile", "1", "workingProfileId", wp.getWorkingProfileId())){
							defaultProfile.setInvalid(true);
							this.setInvalid(true);
							return new Forward("defaultProfileEXISTS");
						}
					}
					module.updateWorkingProfile(wp);
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString()); 
					return new Forward("FAILED");
				} 
			}
		
		populateFields();
		return new Forward("ADDED");
	}

	public String getId() {
		return id;
	}

	public void setId(String string) {
		id = string;
	}

	public String getType() {
		return type;
	}

	public void setType(String string) {
		type = string;
	}

	/**
	 * @return the workingProfileId
	 */
	public String getWorkingProfileId() {
		return workingProfileId;
	}

	/**
	 * @param workingProfileId the workingProfileId to set
	 */
	public void setWorkingProfileId(String workingProfileId) {
		this.workingProfileId = workingProfileId;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public Button getSubmit() {
		return submit;
	}

	public void setSubmit(Button submit) {
		this.submit = submit;
	}

	public Label getLbDisplayName() {
		return lbDisplayName;
	}

	public void setLbDisplayName(Label lbDisplayName) {
		this.lbDisplayName = lbDisplayName;
	}

	public Panel getPanel() {
		return panel;
	}

	public void setPanel(Panel panel) {
		this.panel = panel;
	}

	public TextField getTfName() {
		return tfName;
	}

	public void setTfName(TextField tfName) {
		this.tfName = tfName;
	}

	public TimeField getStartTime() {
		return startTime;
	}

	public void setStartTime(TimeField startTime) {
		this.startTime = startTime;
	}

	public TimeField getEndTime() {
		return endTime;
	}

	public void setEndTime(TimeField endTime) {
		this.endTime = endTime;
	}

	public CheckBox getDefaultProfile() {
		return defaultProfile;
	}

	public void setDefaultProfile(CheckBox defaultProfile) {
		this.defaultProfile = defaultProfile;
	}

	public TextField getDescription() {
		return description;
	}

	public void setDescription(TextField description) {
		this.description = description;
	}

	


}
