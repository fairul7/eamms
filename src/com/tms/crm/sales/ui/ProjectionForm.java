/*
 * Created on Feb 12, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.ui;

import java.util.*;
import kacang.stdui.*;
import kacang.stdui.validator.*;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.*;
import kacang.Application;
import com.tms.crm.sales.misc.*;
import com.tms.crm.sales.model.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ProjectionForm extends Form {
	protected TextField[] tf_MonthProj;
	protected Label[] monthLabel;
	protected Button submit,cancel;
	
	private String projectionID;
	private String userID;
	private String year;
	
	
	/* Step 1: Initialization */
	/*
	public void init() {
		initForm();
	}*/
	
	
	/* Step 2: Parameter passing (dynamic) */
	public String getUserID() {
		return (userID);
	}
	
	public void setUserID(String string) {
		userID = string;
	}
	
	public String getYear() {
		return (year);
	}
	
	public void setYear(String string) {
		year = string;
	}
	
	
	/* Step 3: Form display and processing */
	public void initForm() {
		removeChildren();
		setColumns(2);
		setMethod("POST");
		
		Application application = Application.getInstance();
		AccountManagerModule module = (AccountManagerModule) application.getModule(AccountManagerModule.class);
		
		monthLabel   = new Label[12];
		tf_MonthProj = new TextField[12];
		int intYear = Integer.parseInt(year);
		for (int i=0; i<12; i++) {
			Date currMonth = DateUtil.getDate(intYear, i, 1);
			monthLabel[i] = new Label("monthLabel_" + i, DateUtil.formatDate("MMMM", currMonth));
			addChild(monthLabel[i]);
			
			tf_MonthProj[i] = new TextField("tf_MonthProj_" + i);
			tf_MonthProj[i].setMaxlength("20");
			tf_MonthProj[i].setSize("20");
			ValidatorRange vr = new ValidatorRange("vr_" + i, Application.getInstance().getMessage("sfa.label.invalidprojection","Invalid projection"), new Double(0), new Double(Integer.MAX_VALUE));
			tf_MonthProj[i].addChild(vr);
			addChild(tf_MonthProj[i]);
		}
		
		submit = new Button("submit", Application.getInstance().getMessage("sfa.label.submit","Submit"));
		cancel = new Button(CANCEL_FORM_ACTION, Application.getInstance().getMessage("sfa.label.cancel","Cancel"));
        addChild(cancel);
        addChild(submit);
	}
	
	public void onRequest(Event evt) {
		initForm();
		populateProjection();
	}

    public Forward onSubmit(Event evt) {
		Forward forward = super.onSubmit(evt);
        if(cancel.getAbsoluteName().equals(findButtonClicked(evt)))
            return new Forward("cancel");
        return forward;
    }

	private void populateProjection() {
		Application application = Application.getInstance();
		OPPModule module = (OPPModule) application.getModule(OPPModule.class);
		
		Projection proj = module.selectProjection(userID, year);
		if (proj == null) {
			proj = new Projection();
		}
		
		for (int i=0; i<12; i++) {
			tf_MonthProj[i].setValue(proj.getMonth(i));
		}
		projectionID = proj.getProjectionID();
	}
	
	
	public Forward onValidate(Event evt) {
		Application application = Application.getInstance();
		OPPModule module = (OPPModule) application.getModule(OPPModule.class);
		
		boolean isUpdate = true;
		if (projectionID == null || projectionID.equals("")) {
			UuidGenerator uuid = UuidGenerator.getInstance();
			projectionID = uuid.getUuid();
			isUpdate = false; 
		}
		
		Projection proj = new Projection();
		proj.setProjectionID(projectionID);
		proj.setUserID(userID);
		proj.setYear(new Integer(year));
		for (int i=0; i<12; i++) {
			proj.setMonth(i, new Integer((String) tf_MonthProj[i].getValue()));
		}
		
		if (isUpdate) {
			module.updateProjection(proj);
		} else {
			module.insertProjection(proj);
		}
		
		initForm();
		return new Forward("updateProjection");
	}
	
	public String getDefaultTemplate() {
		return "sfa/Projection_Form";
	}

    public Button getCancel() {
        return cancel;
    }

    public void setCancel(Button cancel) {
        this.cancel = cancel;
    }
}
