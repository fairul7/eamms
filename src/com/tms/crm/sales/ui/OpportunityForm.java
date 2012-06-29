/*
 * Created on Dec 8, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.ui;

import java.text.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Date;

import kacang.stdui.*;
import kacang.stdui.validator.*;
import kacang.util.*;
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
public class OpportunityForm extends Form {
    public static final String FORWARD_CANCEL = "cancel";
    public static final String FORWARD_CLOSED = "closed";

	protected TextField tf_OpportunityName;
	protected SelectBox sel_OpportunityStatus;
	protected SelectBox sel_OpportunityStage;
	protected SelectBox sel_HasPartner;
	protected DateField df_OpportunityStart;
	protected DateField df_OpportunityEnd;
	protected SelectBox sel_OpportunitySource;
	protected TextBox   tb_OpportunityLastRemarks;
	protected TextField tf_CloseReferenceNo;
	protected Button submit;
	protected Button cancel;

	private Label lbCompanyName;
	private Label lbOpportunityName;
	private Label lbOpportunityStatus;
	private Label lbOpportunityStage;
	private Label lbHasPartner;
	private Label lbOpportunityValue;
	private Label lbOpportunityStart;
	private Label lbOpportunityEnd;
	private Label lbOpportunitySource;
	private Label lbOpportunityLastRemarks;
    private Collection contacts;
	private String justCreatedID = "";
	private String companyID;
	private String companyName = "";
	private String opportunityID;
    private Date lastModified = null;
	private boolean editStatusUseLabel;
	private boolean changedToChannel;
	private String type; // possible values: "View", "Add", "Edit"
	private String state="";
	
	/* Step 1: Initialization */
	public void init() {
		state="";
		if (!MyUtil.isValidChoice(type, new String[] {"View", "Add", "Edit"})) {
			type = "Add";
			System.out.println("Error!!! Wrong type passed. OpportunityForm");
		}
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String string) {
		type = string;
	}
	
	
	/* Step 2: Parameter passing (dynamic) */
	public String getJustCreatedID() {
		return (justCreatedID);
	}
	
	public boolean getEditStatusUseLabel() {
		return (editStatusUseLabel);
	}
	
	public void setCompanyID(String string) {
		companyID = string;
	}
	
	public String getOpportunityID() {
		return (opportunityID);
	}
	
	public void setOpportunityID(String string) {
		opportunityID = string;
	}
	
	public boolean isChangedToChannel() {
		return (changedToChannel);
	}
	
	
	/* Step 3: Form display and processing */
	public void initForm() {
		removeChildren();
		setColumns(2);
		setMethod("POST");
		
		Application application  = Application.getInstance();
		OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);
		
		addChild(new Label("lb1", Application.getInstance().getMessage("sfa.label.company","Company")+":"));
		lbCompanyName = new Label("lbCompanyName", "");
		addChild(lbCompanyName);
		
		
		if (type.equals("View")) {
			addChild(new Label("lb2", Application.getInstance().getMessage("sfa.label.opportunity","Opportunity")+":"));
			lbOpportunityName = new Label("lbOpportunityName", "");
			addChild(lbOpportunityName);
		} else {
			if(state.equals("closed")){
				addChild(new Label("sale", Application.getInstance().getMessage("sfa.label.sale","Sale")+":"));
			}else
			{
				addChild(new Label("opp", Application.getInstance().getMessage("sfa.label.opportunity","Opportunity")+":"));
			}
			tf_OpportunityName = new TextField("tf_OpportunityName");
			tf_OpportunityName.setMaxlength("100");
			tf_OpportunityName.setSize("40");
			ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", Application.getInstance().getMessage("sfa.label.mustnotbeempty","Must not be empty"));
			tf_OpportunityName.addChild(vne);
			addChild(tf_OpportunityName);
		}
		
		addChild(new Label("lb3a", Application.getInstance().getMessage("sfa.label.status","Status")+":"));
		lbOpportunityStatus = new Label("lbOpportunityStatus", Opportunity.STATUS_INCOMPLETE_TEXT);
		addChild(lbOpportunityStatus);
		
		if (type.equals("Edit") ) {
			addChild(new Label("lb3b", Application.getInstance().getMessage("sfa.label.status","Status")+":"));
			sel_OpportunityStatus = new SelectBox("sel_OpportunityStatus");
			
			if(state.equals("closed")){
				sel_OpportunityStatus.addOption("100", "Closed");
			}else{
				Integer[] opportunityStatus_Code = Opportunity.getOpportunityStatus_Code();
				String[] opportunityStatus_Text = Opportunity.getOpportunityStatus_Text();
				for (int i=0; i<opportunityStatus_Code.length; i++) {
					sel_OpportunityStatus.addOption(opportunityStatus_Code[i].toString(), opportunityStatus_Text[i]);
				}
			}
			
			addChild(sel_OpportunityStatus);
		}
		
		addChild(new Label("lb3c", Application.getInstance().getMessage("sfa.label.stage","Stage")+":"));
		if (type.equals("View")) {
			lbOpportunityStage = new Label("lbOpportunityStage", "");
			addChild(lbOpportunityStage);
		} else {
			sel_OpportunityStage = new SelectBox("sel_OpportunityStage");
			Integer[] opportunityStage_Code = Opportunity.getOpportunityStage_Code();
			String[] opportunityStage_Text = Opportunity.getOpportunityStage_Text();
			for (int i=0; i<opportunityStage_Code.length; i++) {
				sel_OpportunityStage.addOption(opportunityStage_Code[i].toString(), opportunityStage_Text[i]);
			}
			addChild(sel_OpportunityStage);
		}
		
		addChild(new Label("lb4", Application.getInstance().getMessage("sfa.label.type","Type")+":"));
		if (type.equals("View")) {
			lbHasPartner = new Label("lbHasPartner", "");
			addChild(lbHasPartner);
		} else {
			sel_HasPartner = new SelectBox("sel_HasPartner");
			sel_HasPartner.addOption("0", Application.getInstance().getMessage("sfa.label.direct","Direct"));
			sel_HasPartner.addOption("1", Application.getInstance().getMessage("sfa.label.channel","Channel"));
			addChild(sel_HasPartner);
		}
		
		addChild(new Label("lb5", Application.getInstance().getMessage("sfa.label.startdate","Start date")+":"));
		if (type.equals("View")) {
			lbOpportunityStart = new Label("lbOpportunityStart", "");
			addChild(lbOpportunityStart);
		} else {
			df_OpportunityStart = new DateField("df_OpportunityStart");
			addChild(df_OpportunityStart);
		}
		
		addChild(new Label("lb6", Application.getInstance().getMessage("sfa.label.estimatedclosingdate","Estimated closing date")+":"));
		if (type.equals("View")) {
			lbOpportunityEnd = new Label("lbOpportunityEnd", "");
			addChild(lbOpportunityEnd);
		} else {
			df_OpportunityEnd = new DateField("df_OpportunityEnd");
			ValidatorDateCompare vdatecomp = new ValidatorDateCompare("vdatecomp", "GTE", df_OpportunityStart, Application.getInstance().getMessage("sfa.label.estimatedclosedatemustbegreaterorequalstoStartdate","Estimated close date must be greater or equals to Start date"));
			df_OpportunityEnd.addChild(vdatecomp);
			addChild(df_OpportunityEnd);
		}
		
		addChild(new Label("lb6b", Application.getInstance().getMessage("sfa.label.source","Source")+":"));
		if (type.equals("View")) {
			lbOpportunitySource = new Label("lbOpportunitySource", "");
			addChild(lbOpportunitySource);
		} else {
			sel_OpportunitySource = new SelectBox("sel_OpportunitySource");
			MyUtil.populate_SelectBox(sel_OpportunitySource, module.getSourceCollection(), "sourceID", "sourceText");
			addChild(sel_OpportunitySource);
		}
		
		addChild(new Label("lb6c", Application.getInstance().getMessage("sfa.label.value","Value")+":"));
		if (type.equals("View")) {
			lbOpportunityValue = new Label("lbOpportunityValue", "");
			addChild(lbOpportunityValue);
		}
				
		addChild(new Label("lb7", Application.getInstance().getMessage("sfa.label.lastRemarks","Last Remarks")+":"));
		if (type.equals("View")) {
			lbOpportunityLastRemarks = new Label("lbOpportunityLastRemarks", "");
			addChild(lbOpportunityLastRemarks);
		} else {
			tb_OpportunityLastRemarks = new TextBox("tb_LastRemarks");
			tb_OpportunityLastRemarks.setRows("4");
			tb_OpportunityLastRemarks.setCols("40");
			addChild(tb_OpportunityLastRemarks);
		}

        cancel = new Button("Cancel", Application.getInstance().getMessage("sfa.label.cancel","Cancel"));
        addChild(cancel);
		if (!type.equals("View") && (!type.equals("Edit"))) {
			submit = new Button("submit", Application.getInstance().getMessage("sfa.label.submit","Submit"));
            addChild(submit);
		} else if (type.equals("Edit")) {
			submit = new Button("submit", Application.getInstance().getMessage("sfa.label.update","Update"));
            addChild(submit);
		}
	}
	
	public void onRequest(Event evt) {
		initForm();
		
		if (type.equals("View")) {
			populateView();
		} else if (type.equals("Edit")) {
			populateEdit();
		}
		populateCompanyName();
	}

    public Forward onSubmit(Event event) {
        if(cancel.getAbsoluteName().equals(findButtonClicked(event))){
        	 if (state.equals("closed")) {
        		 state="";
        		 return new Forward(FORWARD_CLOSED);
        	 }else{
        		 return new Forward(FORWARD_CANCEL);
        	 }
           
        }
        return super.onSubmit(event);    //To change body of overridden methods use File | Settings | File Templates.
    }

	public Forward onValidate(Event evt) {
		Forward myForward = null;
		if(submit.getAbsoluteName().equals(findButtonClicked(evt)))
        {if (type.equals("Add")) {
			myForward = addOpportunity();
		} else if (type.equals("Edit")) {
			myForward = editOpportunity();
		}
		initForm();
        }
		return myForward;
	}
	
	private Forward addOpportunity() {
		Application application  = Application.getInstance();
		OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);
		String userId            = getWidgetManager().getUser().getId();
		
		Opportunity opp = new Opportunity();
		UuidGenerator uuid = UuidGenerator.getInstance();
		String oppID = uuid.getUuid();
		opp.setOpportunityID(oppID);
		opp.setOpportunityName((String) tf_OpportunityName.getValue());
		opp.setCompanyID(companyID);
		opp.setOpportunityStatus(Opportunity.STATUS_INCOMPLETE);
		opp.setOpportunityStage(new Integer(MyUtil.getSingleValue_SelectBox(sel_OpportunityStage)));
		opp.setHasPartner(MyUtil.getSingleValue_SelectBox(sel_HasPartner));
		opp.setOpportunityValue(0);
		opp.setOpportunityStart(df_OpportunityStart.getDate());
		opp.setOpportunityEnd(df_OpportunityEnd.getDate());
		opp.setOpportunitySource(MyUtil.getSingleValue_SelectBox(sel_OpportunitySource));
		opp.setOpportunityLastRemarks((String) tb_OpportunityLastRemarks.getValue());
		opp.setPartnerCompanyID(null);
		opp.setCreationDateTime(DateUtil.getDate());
		opp.setModifiedDate(DateUtil.getToday());
		opp.setModifiedBy(userId);
		opp.setCloseReferenceNo(null);
		
		AccountDistributionModule accDistModule = (AccountDistributionModule) application.getModule(AccountDistributionModule.class);
		AccountDistribution accDist = new AccountDistribution();
		accDist.setOpportunityID(oppID);
		accDist.setDistributionSequence(1);
		accDist.setUserID(userId);
		accDist.setDistributionPercentage(100);
		
		module.addOpportunity(opp);
		accDistModule.addAccountDistribution(accDist);

        if(contacts!=null ){
             OpportunityContactModule opConModule = (OpportunityContactModule) application.getModule(OpportunityContactModule.class);
             for (Iterator iterator = contacts.iterator(); iterator.hasNext();) {
                 OpportunityContact contact = (OpportunityContact)iterator.next();
                 contact.setOpportunityID(oppID);
                 opConModule.addOpportunityContact(contact);
             }
        }
		justCreatedID = oppID;
		return new Forward("opportunityAdded");
	}
	
	private Forward editOpportunity() {
		Application application  = Application.getInstance();
		OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);
		String userId            = getWidgetManager().getUser().getId();
		
		Opportunity opp = module.getOpportunity(opportunityID);
		
		String newHasPartner = MyUtil.getSingleValue_SelectBox(sel_HasPartner);
		// change from channel opportunity to direct opportunity
		if (opp.getHasPartner().equals("1") && newHasPartner.equals("0")) {
			opp.setPartnerCompanyID(null);
			OpportunityContactModule opConModule = (OpportunityContactModule) application.getModule(OpportunityContactModule.class);
			opConModule.deleteOpportunityContacts(opportunityID, OpportunityContact.PARTNER_CONTACT);
		}
		
		// change from direct opportunity to channel opportunity
		if (opp.getHasPartner().equals("0") && newHasPartner.equals("1")) {
			changedToChannel = true;
		} else {
			changedToChannel = false;
		}
		
		opp.setOpportunityName((String) tf_OpportunityName.getValue());
		if (!opp.getOpportunityStatus().equals(Opportunity.STATUS_INCOMPLETE)) {
			opp.setOpportunityStatus(new Integer(MyUtil.getSingleValue_SelectBox(sel_OpportunityStatus)));
		}
		opp.setOpportunityStage(new Integer(MyUtil.getSingleValue_SelectBox(sel_OpportunityStage)));
		opp.setHasPartner(newHasPartner);
		opp.setOpportunityStart(df_OpportunityStart.getDate());
		opp.setOpportunityEnd(df_OpportunityEnd.getDate());
		opp.setOpportunitySource(MyUtil.getSingleValue_SelectBox(sel_OpportunitySource));
		opp.setOpportunityLastRemarks((String) tb_OpportunityLastRemarks.getValue());
		opp.setModifiedDate(DateUtil.getToday());
		opp.setModifiedBy(userId);
		
		module.updateOpportunity(opp);
		if (state.equals("closed")) {
	   		 state="";
	   		 return new Forward(FORWARD_CLOSED);
   	 	}else{
   	 		return new Forward("opportunityUpdated");
   	 	}
		
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
		Application application  = Application.getInstance();
		OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);
		Opportunity opp          = module.getOpportunity(opportunityID);
		
		SimpleDateFormat sdf = new SimpleDateFormat(DisplayConstants.DATE_FORMAT);
		lbOpportunityName.setText(opp.getOpportunityName());
		lbOpportunityStatus.setText((String) Opportunity.getOpportunityStatus_More_Map().get(opp.getOpportunityStatus()));
		lbOpportunityStage.setText((String) Opportunity.getOpportunityStage_Map().get(opp.getOpportunityStage()));
		if (opp.getHasPartner().equals("1")) {
			lbHasPartner.setText(Application.getInstance().getMessage("sfa.label.channel","Channel"));
		} else {
			lbHasPartner.setText(Application.getInstance().getMessage("sfa.label.direct","Direct"));
		}
		lbOpportunityStart.setText(sdf.format(opp.getOpportunityStart()));
		lbOpportunityEnd.setText(sdf.format(opp.getOpportunityEnd()));
		lbOpportunitySource.setText((String) module.getSourceMap().get(opp.getOpportunitySource()));
		NumberFormat numFormat = NumberFormat.getInstance();
		lbOpportunityValue.setText(String.valueOf(numFormat.format(opp.getOpportunityValue())));
		lbOpportunityLastRemarks.setText(opp.getOpportunityLastRemarks());
		setCompanyID(opp.getCompanyID());
        lastModified = opp.getModifiedDate();
	}
	
	public void populateEdit() {
		Application application  = Application.getInstance();
		OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);
		Opportunity opp          = module.getOpportunity(opportunityID);
		
		tf_OpportunityName.setValue(opp.getOpportunityName());
		if (opp.getOpportunityStatus().equals(Opportunity.STATUS_INCOMPLETE)) {
			editStatusUseLabel = true;
			lbOpportunityStatus.setText(Opportunity.STATUS_INCOMPLETE_TEXT);
		} else {
			editStatusUseLabel = false;
			sel_OpportunityStatus.setSelectedOptions(new String[] { opp.getOpportunityStatus().toString() });
		}
		sel_OpportunityStage.setSelectedOptions(new String[] { opp.getOpportunityStage().toString() });
		sel_HasPartner.setSelectedOptions(new String[] { opp.getHasPartner() });
		df_OpportunityStart.setDate(opp.getOpportunityStart());
		df_OpportunityEnd.setDate(opp.getOpportunityEnd());
		sel_OpportunitySource.setSelectedOptions(new String[] { opp.getOpportunitySource() });
		tb_OpportunityLastRemarks.setValue(opp.getOpportunityLastRemarks());
		setCompanyID(opp.getCompanyID());
	}
	
	public String getDefaultTemplate() {
		return "sfa/Opportunity_Form";
	}

    public Collection getContacts() {
        return contacts;
    }

    public void setContacts(Collection contacts) {
        this.contacts = contacts;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
