/*
 * Created on Dec 16, 2003
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
public class AccountDistributionForm extends Form {
	protected SelectBox[]        sel_UserID;
	protected TextField[]        tf_DistributionPercentage;
	protected ValidatorMessage[] vMsg;
	protected Button submit;
	
	private String opportunityID;
	private int    maxMembers = 3;
	private Vector vecAccountDistribution;
	private String state="";
	
	public void init() {
		//System.out.println("running init()");
		initForm();
	}
	
	public void initForm() {
		//System.out.println("running initForm()");
		removeChildren();
		setColumns(3);
		setMethod("POST");
		
		addChild(new Label("lb1", " "));
		addChild(new Label("lb2", Application.getInstance().getMessage("sfa.label.member","Member")));
		addChild(new Label("lb3", Application.getInstance().getMessage("sfa.label.percent","Percent")));
		
		sel_UserID                = new SelectBox[maxMembers];
		tf_DistributionPercentage = new TextField[maxMembers];
		vMsg                      = new ValidatorMessage[maxMembers];
		
		for (int i=0; i<maxMembers; i++) {
			addChild(new Label("lbNum_" + i, i + 1 + ". "));
			sel_UserID[i] = new SelectBox("sel_UserID_" + i);
			sel_UserID[i].addOption("", " ");
			addChild(sel_UserID[i]);
			tf_DistributionPercentage[i] = new TextField("tf_DistributionPercentage_" + i);
			tf_DistributionPercentage[i].setSize("3");
			tf_DistributionPercentage[i].setMaxlength("3");
			vMsg[i] = new ValidatorMessage("vMsg_" + i);
			tf_DistributionPercentage[i].addChild(vMsg[i]); 
			addChild(tf_DistributionPercentage[i]);
		}
		
		submit = new Button("submit", Application.getInstance().getMessage("sfa.label.update","Update"));
		addChild(submit);
	}
	
	public void onRequest(Event evt) {
		//System.out.println("running onRequest()");
		initForm();
		populateAccountFields(); // setOpportunityID() should already be called.
	}
	
	public Forward onSubmit(Event evt) {
		//System.out.println("running onSubmit()");
		super.onSubmit(evt);
		
		vecAccountDistribution = new Vector();
		boolean formOk = true;
		int totalPercent = 0;
		Vector users_userID = new Vector();
		
		
		for (int i=0; i<maxMembers; i++) {
			String selectedUser = MyUtil.getSingleValue_SelectBox(sel_UserID[i]);
			if (!selectedUser.equals("")) {
				// check duplicate Account Manager
				if (users_userID.indexOf(selectedUser) == -1) {
					users_userID.add(selectedUser);
				} else {
					sel_UserID[1].setInvalid(true);
					vMsg[i].showError(Application.getInstance().getMessage("sfa.label.cannothaveduplicateAccountManager","Cannot have duplicate Account Manager"));
					formOk = false;
					break;
				}
				
				// check valid percentage
				try {
					int percent = Integer.parseInt((String) tf_DistributionPercentage[i].getValue());
					if (percent > 0) {
						AccountDistribution ad = new AccountDistribution();
						ad.setOpportunityID(opportunityID);
						ad.setDistributionSequence(vecAccountDistribution.size() + 1);
						ad.setUserID(selectedUser);
						ad.setDistributionPercentage(percent);
						vecAccountDistribution.add(ad);
						totalPercent = totalPercent + percent;
					} else {
						throw(new NumberFormatException());
					}
				} catch (NumberFormatException e) {
					// must be numeric and greater than 0
					vMsg[i].showError(Application.getInstance().getMessage("sfa.label.mustbenumericandgreaterthan0","Must be numeric and greater than 0"));
					formOk = false;
				}
			}
		}
		
		// check must have at least one Account Manager
		if (formOk && users_userID.size() == 0) {
			sel_UserID[0].setInvalid(true);
			vMsg[0].showError(Application.getInstance().getMessage("sfa.label.pleaseselectatleastoneAccountManager","Please select at least one Account Manager"), false);
			formOk = false;
		}
		
		// check the total percentage is 100
		if (formOk && totalPercent != 100) {
			vMsg[0].showError(Application.getInstance().getMessage("sfa.label.totalpercentagemustbe100","Total percentage must be 100"));
			formOk = false;
		}
		
		if (!formOk) {
			this.setInvalid(true);
			sel_UserID                = new SelectBox[maxMembers];
			for (int i=0; i<maxMembers; i++) {
				sel_UserID[i] = new SelectBox("sel_UserID_" + i);
				sel_UserID[i].addOption("", " ");
			}
			populateAccountFields();
		}
		
		return null;
	}
	
	public Forward onValidate(Event evt) {
		//System.out.println("running onValidate()");
		Application application = Application.getInstance();
		AccountDistributionModule module = (AccountDistributionModule) application.getModule(AccountDistributionModule.class);
		
		module.deleteAccountDistribution(opportunityID);
		for (int i=0; i<vecAccountDistribution.size(); i++) {
			module.addAccountDistribution((AccountDistribution) vecAccountDistribution.get(i));
		}
		
		removeChildren();
		init();
		
		if (state.equals("closed")) {
      		 state="";
      		 return new Forward("closed");
      	 }else{
      		return new Forward("distributionSaved");
      	 }
		
	}
	
	public String getOpportunityID() {
		return opportunityID;
	}
	
	public void setOpportunityID(String string) {
		opportunityID = string;
	}
	
	private void populateAccountFields() {
		Application application = Application.getInstance();
		AccountDistributionModule adModule = (AccountDistributionModule) application.getModule(AccountDistributionModule.class);
		Collection acountDistributions = adModule.getAccountDistribution(opportunityID, null, false, 0, -1);
		Iterator distributionIterator = acountDistributions.iterator();
		
		AccountManagerModule amModule = (AccountManagerModule) application.getModule(AccountManagerModule.class);
		Collection accountManagers = amModule.getAccountManagers();
		
		for(int i=0; i<maxMembers; i++) {
			SelectBox selBox = sel_UserID[i];
			
			// populate account manager select box
			Iterator managerIterator = accountManagers.iterator();
			while (managerIterator.hasNext()) {
				AccountManager aManager = (AccountManager) managerIterator.next();
				selBox.addOption(aManager.getId(), aManager.getFullName());
			}
			
			// populate selected account manager and percent
			if (distributionIterator.hasNext()) {
				AccountDistribution aDistribution = (AccountDistribution) distributionIterator.next();
				selBox.addSelectedOption(aDistribution.getUserID());
				tf_DistributionPercentage[i].setValue(String.valueOf(aDistribution.getDistributionPercentage()));
			}
		}
	}
	
	public String getDefaultTemplate() {
		return "sfa/AcDistribution_Form";
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
