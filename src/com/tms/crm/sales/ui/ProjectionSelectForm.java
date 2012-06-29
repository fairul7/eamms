/*
 * Created on Feb 12, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.ui;

import java.util.*;
import kacang.stdui.*;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import com.tms.crm.sales.misc.*;
import com.tms.crm.sales.model.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ProjectionSelectForm extends Form {
	protected SelectBox sel_AccountManager;
	protected SelectBox sel_Year;
	protected Button submit;
	
	private String userID;
	private String year;
	
	
	/* Step 1: Initialization */
	public void init() {
		initForm();
	}
	
	
	/* Step 2: Parameter passing (dynamic) */
	public String getUserID() {
		return (userID);
	}
	
	public String getYear() {
		return (year);
	}
	
	
	/* Step 3: Form display and processing */
	public void initForm() {
		removeChildren();
		setColumns(2);
		setMethod("POST");
		
		Application application = Application.getInstance();
		AccountManagerModule module = (AccountManagerModule) application.getModule(AccountManagerModule.class);
		
		addChild(new Label("lb1", Application.getInstance().getMessage("sfa.label.accountManager","Account Manager")+":"));
		sel_AccountManager = new SelectBox("sel_AccountManager");
		Collection col = module.getAccountManagers();
		Iterator managerIterator = col.iterator();
		while (managerIterator.hasNext()) {
			AccountManager aManager = (AccountManager) managerIterator.next();
			sel_AccountManager.addOption(aManager.getId(), aManager.getFullName());
		}
		addChild(sel_AccountManager);
		
		addChild(new Label("lb2", Application.getInstance().getMessage("sfa.label.year","Year")+":"));
		sel_Year = new SelectBox("sel_Year");
		int currYear = DateUtil.getYear(DateUtil.getDate());
		for (int i=-1; i<2; i++) {
			int year = currYear + i;
			sel_Year.addOption(String.valueOf(year), String.valueOf(year));
		}
		sel_Year.setSelectedOptions(new String[] {String.valueOf(currYear)});
		addChild(sel_Year);
		
		submit = new Button("submit", Application.getInstance().getMessage("sfa.label.submit","Submit"));
		addChild(submit);
	}
	
	public Forward onValidate(Event evt) {
		userID = MyUtil.getSingleValue_SelectBox(sel_AccountManager);
		year   = MyUtil.getSingleValue_SelectBox(sel_Year);
		
		initForm();
		return new Forward("selectProjection");
	}
	
	public String getDefaultTemplate() {
		return "sfa/ProjectionSelect_Form";
	}
}
