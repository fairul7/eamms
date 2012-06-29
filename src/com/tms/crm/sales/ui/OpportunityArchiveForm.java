/*
 * Created on Feb 24, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.ui;

import java.text.*;
import java.util.*;
import kacang.stdui.*;
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
public class OpportunityArchiveForm extends Form {
	private Label lbCompanyName;
	private Label lbOpportunityName;
	private Label lbOpportunityStatus;
	private Label lbOpportunityStage;
	private Label lbHasPartner;
	private Label lbModifiedBy;
	private Label lbOpportunityValue;
	private Label lbOpportunityStart;
	private Label lbOpportunityEnd;
	private Label lbOpportunityLastRemarks;
	
	private String companyID;
	private String companyName = "";
	private String opportunityID;
	private Date   modifiedDate;
	
	
	/* Step 1: Initialization */
	public void init() {
	}
	
	
	/* Step 2: Parameter passing (dynamic) */
	public String getOpportunityID() {
		return (opportunityID);
	}
	
	public void setOpportunityID(String string) {
		opportunityID = string;
	}
	
	public String getModifiedDate() {
		return (DateUtil.getDateString(modifiedDate));
	}
	
	public void setModifiedDate(String string) {
		modifiedDate = DateUtil.getDateFromDateString(string);
	}
	
	
	/* Step 3: Form display and processing */
	public void initForm() {
		removeChildren();
		setColumns(2);
		setMethod("POST");
		
		addChild(new Label("lb1", Application.getInstance().getMessage("sfa.label.company","Company")+":"));
		lbCompanyName = new Label("lbCompanyName", "");
		addChild(lbCompanyName);
		
		addChild(new Label("lb2", Application.getInstance().getMessage("sfa.label.opportunity","Opportunity")+":"));
		lbOpportunityName = new Label("lbOpportunityName", "");
		addChild(lbOpportunityName);
		
		addChild(new Label("lb3a", Application.getInstance().getMessage("sfa.label.status","Status")+":"));
		lbOpportunityStatus = new Label("lbOpportunityStatus", "");
		addChild(lbOpportunityStatus);
		
		addChild(new Label("lb3c", Application.getInstance().getMessage("sfa.label.stage","Stage")+":"));
		lbOpportunityStage = new Label("lbOpportunityStage", "");
		addChild(lbOpportunityStage);
		
		addChild(new Label("lb4", Application.getInstance().getMessage("sfa.label.type","Type")+":"));
		lbHasPartner = new Label("lbHasPartner", "");
		addChild(lbHasPartner);
		
		addChild(new Label("lb5", Application.getInstance().getMessage("sfa.label.startdate","Start date")+":"));
		lbOpportunityStart = new Label("lbOpportunityStart", "");
		addChild(lbOpportunityStart);
		
		addChild(new Label("lb6", Application.getInstance().getMessage("sfa.label.estimatedclosingdate","Estimated closing date")+":"));
		lbOpportunityEnd = new Label("lbOpportunityEnd", "");
		addChild(lbOpportunityEnd);
		
		addChild(new Label("lb6b", Application.getInstance().getMessage("sfa.label.modifiedBy","Modified By")+":"));
		lbModifiedBy = new Label("lbModifiedBy", "");
		addChild(lbModifiedBy);
		
		addChild(new Label("lb6c", Application.getInstance().getMessage("sfa.label.value","Value")+":"));
		lbOpportunityValue = new Label("lbOpportunityValue", "");
		addChild(lbOpportunityValue);
		
		addChild(new Label("lb7", Application.getInstance().getMessage("sfa.label.lastRemarks","Last Remarks")+":"));
		lbOpportunityLastRemarks = new Label("lbOpportunityLastRemarks", "");
		addChild(lbOpportunityLastRemarks);
	}
	
	public void onRequest(Event evt) {
		initForm();
		
		populateView();
		
		NaviUtil naviUtil = new NaviUtil(null); 
		companyID = naviUtil.getCompanyID4Opportunity(opportunityID);
		populateCompanyName();
	}
	
	public void populateCompanyName() {
		Application application = Application.getInstance();
		CompanyModule comModule = (CompanyModule) application.getModule(CompanyModule.class);
		CompanyDao comDao       = (CompanyDao) comModule.getDao();
		
		try {
			Company com = comDao.selectRecord(companyID);
			companyName = com.getCompanyName();
		} catch (Exception e) {
			companyName = Application.getInstance().getMessage("sfa.label.errorgettingCompanyName","Error getting Company Name");
		}
		
		lbCompanyName.setText(companyName);
	}
	
	public void populateView() {
		Application application         = Application.getInstance();
		OpportunityModule opModule      = (OpportunityModule) application.getModule(OpportunityModule.class);
		AccountManagerModule amModule   = (AccountManagerModule) application.getModule(AccountManagerModule.class);
		OpportunityArchiveModule module = (OpportunityArchiveModule) application.getModule(OpportunityArchiveModule.class);
		OpportunityArchive oppArc       = module.getOpportunityArchive(opportunityID, modifiedDate);
		
		SimpleDateFormat sdf = new SimpleDateFormat(DisplayConstants.DATE_FORMAT);
		lbOpportunityName.setText(oppArc.getOpportunityName());
		lbOpportunityStatus.setText((String) Opportunity.getOpportunityStatus_Map().get(oppArc.getOpportunityStatus()));
		lbOpportunityStage.setText((String) Opportunity.getOpportunityStage_Map().get(oppArc.getOpportunityStage()));
		if (oppArc.getHasPartner().equals("1")) {
			lbHasPartner.setText(Application.getInstance().getMessage("sfa.label.channel","Channel"));
		} else {
			lbHasPartner.setText(Application.getInstance().getMessage("sfa.label.direct","Direct"));
		}
		lbOpportunityStart.setText(sdf.format(oppArc.getOpportunityStart()));
		lbOpportunityEnd.setText(sdf.format(oppArc.getOpportunityEnd()));
		lbModifiedBy.setText((String) amModule.getAccountManagersMap().get(oppArc.getModifiedBy()));
		NumberFormat numFormat = NumberFormat.getInstance();
		lbOpportunityValue.setText(String.valueOf(numFormat.format(oppArc.getOpportunityValue())));
		lbOpportunityLastRemarks.setText(oppArc.getOpportunityLastRemarks());
	}
	
	public String getDefaultTemplate() {
		return "sfa/OpportunityArchive_Form";
	}
}
