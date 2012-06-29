package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import kacang.Application;
import kacang.services.security.Profileable;
import kacang.services.security.ui.Profile;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.TimeField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.ValidatorSelectBoxNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.util.Log;

import com.tms.crm.sales.misc.DateUtil;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.facility.model.WorkingProfile;
import com.tms.fms.util.WidgetUtil;
import com.tms.fms.widgets.BoldLabel;


public class ManpowerForm extends Form {
	
	private Button cancel;
	private Button submit;
	private String type;
	private Label lbManpower;
	private Label lbCompetency;
	private Panel panel;
	private UnitUserSelectBox userSelectBox;
	private Collection profilers;
	private Collection selectedCompetencies;

	public ManpowerForm() {}

	public ManpowerForm(String s) {super(s);}

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
	
	public String getDefaultTemplate()
    {
        return "fms/facility/manpowerForm";
    }

	public void initForm() {
		setMethod("post");
		setColumns(2);
		Application app = Application.getInstance();
		String msgNotEmpty = app.getMessage("lms.elearning.validation.mandatoryField");
		
		
		lbCompetency = new BoldLabel("lbCompetency");
		lbCompetency.setAlign("right");
		lbCompetency.setText(app.getMessage("fms.facility.label.competency")+ " *");

		lbManpower = new BoldLabel("lbManpower");
		lbManpower.setAlign("right");
		lbManpower.setText(app.getMessage("fms.facility.label.selectManpower")+ " *");

		userSelectBox = new UnitUserSelectBox("userSelectBox");
		userSelectBox.init();
		
		panel = new Panel("panel");

		submit = new Button("submit", app.getMessage("fms.facility.submit"));

		cancel = new Button("cancel", app.getMessage("fms.facility.cancel"));
		
		
		panel.addChild(submit);
		panel.addChild(cancel);
		
		addChild(lbManpower);
		addChild(userSelectBox);
		
		addChild(lbCompetency);
		//addChild(workingProfile);
		
		
		
		addChild(new Label("blank"));
		addChild(panel);
		initProfilers();
	}
	
	private void initProfilers()
    {
        String profiles = "com.tms.fms.facility.ui.UserCompetencyForm";
        profilers = new ArrayList();
        if(!(profiles == null || "".equals(profiles)))
        {
            StringTokenizer tokenizer = new StringTokenizer(profiles, ",");
            while(tokenizer.hasMoreTokens())
            {
                String token = tokenizer.nextToken();
                try
                {
                    Profileable profile = (Profileable) Class.forName(token).newInstance();
                    profile.init(getWidgetManager().getUser());
                    Widget widget = profile.getWidget();
                    widget.setName(profile.getProfileableName());
                    addChild(widget);
                    widget.init();
                    profilers.add(profile);

                }
                catch(Exception e)
                {
                    Log.getLog(Profile.class).error(e);
                }
            }
        }
    }

	public void populateFields() {
		
		/*try {
			SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
			WorkingProfile wp=module.getWorkingProfileDuration(workingProfileDurationId);
			if(wp!=null){
				workingProfile.setSelectedOption(wp.getWorkingProfileId());
				fromDate.setDate(wp.getStartDate());
				toDate.setDate(wp.getEndDate());
				userSelectBox.setIds(wp.getUsers());
				//userSelectBox.generateOptionMap();
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.toString());
		}*/
	}

	public Forward onSubmit(Event evt) {
		String buttonName = findButtonClicked(evt);
		kacang.ui.Forward result = super.onSubmit(evt);
		if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
			init();
			return new Forward(Form.CANCEL_FORM_ACTION, "manpowerListing.jsp", true);
		}
		else {return result;}
	}

	public Forward onValidate(Event event) {
		
			/*WorkingProfile wp = new WorkingProfile();
			SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
			wp.setWorkingProfileDurationId(workingProfileDurationId);
			wp.setWorkingProfileId(WidgetUtil.getSbValue(workingProfile));
			wp.setStartDate(fromDate.getDate());
			wp.setEndDate(toDate.getDate());
			wp.setManpowerMap(userSelectBox.getSelectedOptions());
			if ("Add".equals(type)) {
				try {
					
					module.insertWorkingProfileDuration(wp);
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString()); 
					return new Forward("FAILED");
				} 
			}
			if ("Edit".equals(type)) {
				try {
					
					module.updateWorkingProfileDuration(wp);
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString()); 
					return new Forward("FAILED");
				} 
			}*/
		return new Forward("ADDED");
	}

	
	public String getType() {
		return type;
	}

	public void setType(String string) {
		type = string;
	}

	/**
	 * @return the userSelectBox
	 */
	public UnitUserSelectBox getUserSelectBox() {
		return userSelectBox;
	}

	/**
	 * @param userSelectBox the userSelectBox to set
	 */
	public void setUserSelectBox(UnitUserSelectBox userSelectBox) {
		this.userSelectBox = userSelectBox;
	}

	/**
	 * @return the cancel
	 */
	public Button getCancel() {
		return cancel;
	}

	/**
	 * @param cancel the cancel to set
	 */
	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	/**
	 * @return the panel
	 */
	public Panel getPanel() {
		return panel;
	}

	/**
	 * @param panel the panel to set
	 */
	public void setPanel(Panel panel) {
		this.panel = panel;
	}

	/**
	 * @return the profilers
	 */
	public Collection getProfilers() {
		return profilers;
	}

	/**
	 * @param profilers the profilers to set
	 */
	public void setProfilers(Collection profilers) {
		this.profilers = profilers;
	}

	/**
	 * @return the submit
	 */
	public Button getSubmit() {
		return submit;
	}

	/**
	 * @param submit the submit to set
	 */
	public void setSubmit(Button submit) {
		this.submit = submit;
	}

	public Collection getSelectedCompetencies() {
		return selectedCompetencies;
	}

	public void setSelectedCompetencies(Collection selectedCompetencies) {
		this.selectedCompetencies = selectedCompetencies;
	}
	
	

}
