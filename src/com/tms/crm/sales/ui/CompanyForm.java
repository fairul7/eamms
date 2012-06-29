/*
 * Created on Dec 3, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.ui;

import java.util.*;
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
public class CompanyForm extends Form {
	protected TextField tf_CompanyName;
	protected SelectBox sel_CompanyType;
	protected TextField tf_CompanyStreet1;
	protected TextField tf_CompanyStreet2;
	protected TextField tf_CompanyCity;
	protected TextField tf_CompanyState;
	protected TextField tf_CompanyPostcode;
	protected CountrySelectBox csb_CompanyCountry;
	protected TextField tf_CompanyTel;
	protected TextField tf_CompanyFax;
	protected TextField tf_CompanyWebsite;
	protected Radio     rd_IsPartner_Yes;
	protected Radio     rd_IsPartner_No;
	protected Radio[]   rd_PartnerType;
	protected ButtonGroup      btngrp_PartnerType;
	protected ValidatorMessage vMsg;
	protected Button submit,cancel;
	
	private Label lbCompanyName;
	private Label lbCompanyType;
	private Label lbCompanyStreet1;
	private Label lbCompanyStreet2;
	private Label lbCompanyCity;
	private Label lbCompanyState;
	private Label lbCompanyPostcode;
	private Label lbCompanyCountry;
	private Label lbCompanyTel;
	private Label lbCompanyFax;
	private Label lbCompanyWebsite;
	private Label lbIsPartner;
	private Label lbPartnerType;
	private Collection partnerTypeRadioNames;
	private int[]      partnerTypeIDs;
	
	private String justCreatedID = "";
	private String companyID;
	private String opportunityID;
	
	private String type; 		// possible values: "View", "Add", "Edit"
	private String subType;		// possible values: "Partner", null
	private boolean createPartner;
	
	public static final String FORWARD_CLOSED = "closed";
	public static final String FORWARD_CANCEL = "cancel";
	private String status="";
	

	/* Step 1: Initialization */
	public void init() {
		status="";
		if (!MyUtil.isValidChoice(type, new String[] {"View", "Add", "Edit"})) {
			type = "Add";
			System.out.println("Error!!! Wrong type passed. CompanyForm");
		}
		
		createPartner = false;
		if (subType != null && subType.equals("Partner")) {
			if (type.equals("Add")) {
				createPartner = true;
			} else {
				subType = "";
			}
		}
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String string) {
		type = string;
	}
	
	public String getSubType() {
		return subType;
	}
	
	public void setSubType(String string) {
		subType = string;
	}
	
	
	/* Step 2: Parameter passing (dynamic) */
	public String getJustCreatedID() {
		return (justCreatedID);
	}
	
	public String getCompanyID() {
		return companyID;
	}
	
	public void setCompanyID(String string) {
		companyID = string;
	}
	
	public String getOpportunityID() {
		return opportunityID;
	}
	
	public void setOpportunityID(String string) {
		opportunityID = string;
	}
	
	public Collection getPartnerTypeRadioNames() {
		return partnerTypeRadioNames;
	}
	
	
	/* Step 3: Form display and processing */
	public void initForm() {
		removeChildren();
		setColumns(2);
		setMethod("POST");
		
		addChild(new Label("lb1", Application.getInstance().getMessage("sfa.label.company","Company")+ ":"));
		if (type.equals("View")) {
			lbCompanyName = new Label("lbCompanyName","");
			addChild(lbCompanyName);
		} else {
			tf_CompanyName = new TextField("tf_CompanyName");
			tf_CompanyName.setMaxlength("255");
			tf_CompanyName.setSize("40");
			ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", Application.getInstance().getMessage("sfa.label.mustnotbeempty","Must not be empty"));
			tf_CompanyName.addChild(vne);
			addChild(tf_CompanyName);
		}
		
		addChild(new Label("lb2", Application.getInstance().getMessage("sfa.label.type","Type")+":"));
		if (type.equals("View")) {
			lbCompanyType = new Label("lbCompanyType", "");
			addChild(lbCompanyType);
		} else {
			sel_CompanyType = new SelectBox("sel_CompanyType");

            CompanyTypeModule ctm = (CompanyTypeModule) Application.getInstance().getModule(CompanyTypeModule.class);
            try {
                Collection col  = ctm.getCompanyTypes();
                for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                    CompanyType companyType = (CompanyType) iterator.next();
                    sel_CompanyType.addOption(companyType.getId(),companyType.getType());
                }
            } catch (CompanyTypeException e) {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }

/*
            String[] companyType_Code = Company.getCompanyType_Code();
			String[] companyType_Text = Company.getCompanyType_Text();
			for (int i=0; i<companyType_Code.length; i++) {
				sel_CompanyType.addOption(companyType_Code[i], companyType_Text[i]);
			}
*/
			addChild(sel_CompanyType);
		}
		
		addChild(new Label("lb3", Application.getInstance().getMessage("sfa.label.address","Address")+":"));
		if (type.equals("View")) {
			lbCompanyStreet1 = new Label("lbCompanyStreet1", "");
			addChild(lbCompanyStreet1);
		} else {
			tf_CompanyStreet1 = new TextField("tf_CompanyStreet1");
			tf_CompanyStreet1.setMaxlength("255");
			tf_CompanyStreet1.setSize("40");
			addChild(tf_CompanyStreet1);
		}
		
		addChild(new Label("lb4", ""));
		if (type.equals("View")) {
			lbCompanyStreet2 = new Label("lbCompanyStreet2", "");
			addChild(lbCompanyStreet2);
		} else {
			tf_CompanyStreet2 = new TextField("tf_CompanyStreet2");
			tf_CompanyStreet2.setMaxlength("255");
			tf_CompanyStreet2.setSize("40");
			addChild(tf_CompanyStreet2);
		}
		
		addChild(new Label("lb5", Application.getInstance().getMessage("sfa.label.city","City")+":"));
		if (type.equals("View")) {
			lbCompanyCity = new Label("lbCompanyCity", "");
			addChild(lbCompanyCity);
		} else {
			tf_CompanyCity = new TextField("tf_CompanyCity");
			tf_CompanyCity.setMaxlength("50");
			tf_CompanyCity.setSize("20");
			addChild(tf_CompanyCity);
		}
		
		addChild(new Label("lb5b", Application.getInstance().getMessage("sfa.label.state","State")+":"));
		if (type.equals("View")) {
			lbCompanyState = new Label("lbCompanyState", "");
			addChild(lbCompanyState);
		} else {
			tf_CompanyState = new TextField("tf_CompanyState");
			tf_CompanyState.setMaxlength("50");
			tf_CompanyState.setSize("20");
			addChild(tf_CompanyState);
		}
		
		addChild(new Label("lb6", Application.getInstance().getMessage("sfa.label.postcode","Postcode")+":"));
		if (type.equals("View")) {
			lbCompanyPostcode = new Label("lbCompanyPostcode", "");
			addChild(lbCompanyPostcode);
		} else {
			tf_CompanyPostcode = new TextField("tf_CompanyPostcode");
			tf_CompanyPostcode.setMaxlength("20");
			tf_CompanyPostcode.setSize("20");
			addChild(tf_CompanyPostcode);
		}
		
		addChild(new Label("lb7", Application.getInstance().getMessage("sfa.label.country","Country")+":"));
		if (type.equals("View")) {
			lbCompanyCountry = new Label("lbCompanyCountry", "");
			addChild(lbCompanyCountry);
		} else {
			csb_CompanyCountry = new CountrySelectBox("csb_CompanyCountry");
            addChild(csb_CompanyCountry);
		}
		
		addChild(new Label("lb8", Application.getInstance().getMessage("sfa.label.telephone","Telephone")+":"));
		if (type.equals("View")) {
			lbCompanyTel = new Label("lbCompanyTel", "");
			addChild(lbCompanyTel);
		} else {
			tf_CompanyTel = new TextField("tf_CompanyTel");
			tf_CompanyTel.setMaxlength("30");
			tf_CompanyTel.setSize("20");
			addChild(tf_CompanyTel);
		}
		
		addChild(new Label("lb9", Application.getInstance().getMessage("sfa.label.fax","Fax")+":"));
		if (type.equals("View")) {
			lbCompanyFax = new Label("lbCompanyFax", "");
			addChild(lbCompanyFax);
		} else {
			tf_CompanyFax = new TextField("tf_CompanyFax");
			tf_CompanyFax.setMaxlength("30");
			tf_CompanyFax.setSize("20");
			addChild(tf_CompanyFax);
		}
		
		addChild(new Label("lb10", Application.getInstance().getMessage("sfa.label.website","Website")+":"));
		if (type.equals("View")) {
			lbCompanyWebsite = new Label("lbCompanyWebsite", "");
			addChild(lbCompanyWebsite);
		} else {
			tf_CompanyWebsite = new TextField("tf_CompanyWebsite");
			tf_CompanyWebsite.setMaxlength("255");
			tf_CompanyWebsite.setSize("40");
			addChild(tf_CompanyWebsite);
		}
		
		Application application = Application.getInstance();
		PartnerModule partnerModule = (PartnerModule) application.getModule(PartnerModule.class);
		if (type.equals("View")) {
			lbIsPartner   = new Label("lbIsPartner", "");
			lbPartnerType = new Label("lbPartnerStatus", "");
			addChild(lbIsPartner);
			addChild(lbPartnerType);
		} else {
			addChild(new Label("lb11", Application.getInstance().getMessage("sfa.label.partner","Partner")+":"));
			rd_IsPartner_Yes = new Radio("rd_IsPartner_Yes", Application.getInstance().getMessage("sfa.label.yes","Yes")); 
			rd_IsPartner_No  = new Radio("rd_IsPartner_No", Application.getInstance().getMessage("sfa.label.no","No"));
			rd_IsPartner_Yes.setGroupName("rd_IsPartner");
			rd_IsPartner_No.setGroupName("rd_IsPartner");
			addChild(rd_IsPartner_Yes);
			addChild(rd_IsPartner_No);
			
			addChild(new Label("lb12", Application.getInstance().getMessage("sfa.label.partnerStatus","Partner Status")+":"));
			Collection colPartnerTypes = partnerModule.getPartnerTypes();
			rd_PartnerType        = new Radio[colPartnerTypes.size()];
			partnerTypeRadioNames = new Vector();
			partnerTypeIDs        = new int[colPartnerTypes.size()];
			btngrp_PartnerType    = new ButtonGroup("btngrp_PartnerType");
			Iterator iterator = colPartnerTypes.iterator();
			int i = 0;
			while (iterator.hasNext()){
				Hashtable ht = (Hashtable) iterator.next();
				String radioName = "rd_PartnerStatus_" + ht.get("companyPartnerTypeID");
				String radioText = (String) ht.get("companyPartnerTypeName");
				rd_PartnerType[i] = new Radio(radioName, radioText);
				partnerTypeRadioNames.add(radioName);
				partnerTypeIDs[i] = ((Number) ht.get("companyPartnerTypeID")).intValue();
				rd_PartnerType[i].setGroupName("rd_PartnerStatus");
				btngrp_PartnerType.addChild(rd_PartnerType[i]);
				i++;
			}
			
			vMsg = new ValidatorMessage("vMsg");
			btngrp_PartnerType.addChild(vMsg);
			addChild(btngrp_PartnerType);
		}
		
		
		if (!type.equals("View") && (!type.equals("Edit"))) {
			submit = new Button("submit", Application.getInstance().getMessage("sfa.label.submit","Submit"));
			addChild(submit);
            cancel = new Button("cancel",Application.getInstance().getMessage("sfa.label.cancel","Cancel"));
            addChild(cancel);
		} else if (type.equals("Edit")) {
			submit = new Button("submit", Application.getInstance().getMessage("sfa.label.update","Update"));
			addChild(submit);
            cancel = new Button("cancel",Application.getInstance().getMessage("sfa.label.cancel","Cancel"));
            addChild(cancel);
		}
	}
	
	public void onRequest(Event evt) {
		initForm();
		
		if (type.equals("View")) {
			populateView();
		} else if (type.equals("Edit")) {
			populateEdit();
		}
	}
	
	public Forward onSubmit(Event evt) {

		if(cancel.getAbsoluteName().equals(findButtonClicked(evt))){
			return null;
		}
		
        super.onSubmit(evt);
		
		boolean formOk = true;
		boolean gotPartnerType = false;



		for (int i=0; i<rd_PartnerType.length; i++) {
			if (rd_PartnerType[i].isChecked()) {
				gotPartnerType = true;
				break;
			}
		}
		
		if (createPartner || rd_IsPartner_Yes.isChecked()) {
			if (!gotPartnerType) {
				btngrp_PartnerType.setInvalid(true);
				vMsg.showError(Application.getInstance().getMessage("sfa.label.pleaseselectone","Please select one"), false);
				formOk = false;
			}
		}
		
		if (!formOk) {
			this.setInvalid(true);
		}
		
		return null;
	}
	

	public Forward onValidate(Event evt) {
		Forward myForward = null;
		if(cancel.getAbsoluteName().equals(findButtonClicked(evt))){
			 if (status.equals("closed")) {
				 status="";
        		 myForward= new Forward(FORWARD_CLOSED);
        	 }else{
        		 myForward= new Forward(FORWARD_CANCEL);
        	 }
           
        }else{
        if (type.equals("Add")) {
			myForward = addCompany();
		} else if (type.equals("Edit")) {
			myForward = editCompany();
		}
		initForm();
        }
		return myForward;
	}




	private void forSaving(Company com) {
		com.setCompanyName((String) tf_CompanyName.getValue());
		com.setCompanyType(MyUtil.getSingleValue_SelectBox(sel_CompanyType));
		com.setCompanyStreet1((String) tf_CompanyStreet1.getValue());
		com.setCompanyStreet2((String) tf_CompanyStreet2.getValue());
		com.setCompanyCity((String) tf_CompanyCity.getValue());
		com.setCompanyState((String) tf_CompanyState.getValue());
		com.setCompanyPostcode((String) tf_CompanyPostcode.getValue());
		com.setCompanyCountry((String) csb_CompanyCountry.getValue());
		com.setCompanyTel((String) tf_CompanyTel.getValue());
		com.setCompanyFax((String) tf_CompanyFax.getValue());
		com.setCompanyWebsite((String) tf_CompanyWebsite.getValue());
		
		Integer partnerTypeID = null;
		for (int i=0; i<rd_PartnerType.length; i++) {
			if (rd_PartnerType[i].isChecked()) {
				partnerTypeID = new Integer(partnerTypeIDs[i]);
				break;
			}
		}
		if (rd_IsPartner_No.isChecked()) {
			partnerTypeID = null;
		}
		com.setCompanyPartnerTypeID(partnerTypeID);
	}
	
	private Forward addCompany() {
		Application application = Application.getInstance();
		CompanyModule module = (CompanyModule) application.getModule(CompanyModule.class);
		
		Company com = new Company();
		UuidGenerator uuid = UuidGenerator.getInstance();
		String comID = uuid.getUuid();
		com.setCompanyID(comID);
		forSaving(com);
		
		if (!module.isUnique(com)) {
			return new Forward("companyDuplicate");
		}

        com.setLastModified(new Date());
		module.addCompany(com);
		
		// tie partner to opportunity
		if (createPartner) {
			OpportunityModule opModule = (OpportunityModule) application.getModule(OpportunityModule.class);
			Opportunity opp = opModule.getOpportunity(opportunityID);
			opp.setPartnerCompanyID(comID);
			opModule.updateOpportunity(opp);
		}
		
		justCreatedID = comID;
		return new Forward("companyAdded");
	}
	
	private Forward editCompany() {
		Application application = Application.getInstance();
		CompanyModule module = (CompanyModule) application.getModule(CompanyModule.class);
		
		Company com = module.getCompany(companyID);
		forSaving(com);
		
		if (!module.isUnique(com)) {
			return new Forward("companyDuplicate");
		}
		
		module.updateCompany(com);
		if (status.equals("closed")) {
			status="";
	   		 return new Forward("closed");
	   	 }else{
	   		return new Forward("companyUpdated");
	   	 }
		
	}
	
	public void populateView() {
		Application application = Application.getInstance();
		CompanyModule module    = (CompanyModule) application.getModule(CompanyModule.class);
		Company com             = module.getCompany(companyID);
		
		String typeText = String.valueOf(com.getCompanyType());
		String[] companyType_Code = Company.getCompanyType_Code();
		String[] companyType_Text = Company.getCompanyType_Text();
		for (int i=0; i<companyType_Code.length; i++) {
			if (typeText.equals(companyType_Code[i])) {
				typeText = companyType_Text[i];
				break;
			}
		}
		
		lbCompanyName.setText(com.getCompanyName());
		lbCompanyType.setText(typeText);
		lbCompanyStreet1.setText(com.getCompanyStreet1());
		lbCompanyStreet2.setText(com.getCompanyStreet2());
		lbCompanyCity.setText(com.getCompanyCity());
		lbCompanyState.setText(com.getCompanyState());
		lbCompanyPostcode.setText(com.getCompanyPostcode());
		
		CountrySelectBox csb_temp = new CountrySelectBox("csb_temp");
		lbCompanyCountry.setText((String) csb_temp.getOptionMap().get(com.getCompanyCountry()));
		
		lbCompanyTel.setText(com.getCompanyTel());
		lbCompanyFax.setText(com.getCompanyFax());
		lbCompanyWebsite.setText(com.getWebsiteLink(""));
		
		PartnerModule partnerModule = (PartnerModule) application.getModule(PartnerModule.class);
		if (com.getCompanyPartnerTypeID() == null) {
			lbIsPartner.setText(Application.getInstance().getMessage("sfa.label.no","No"));
			lbPartnerType.setText("");
		} else {
			lbIsPartner.setText(Application.getInstance().getMessage("sfa.label.yes","Yes"));
			lbPartnerType.setText(partnerModule.getPartnerTypeName(com.getCompanyPartnerTypeID().intValue()));
		}
	}
	
	public void populateEdit() {
		Application application = Application.getInstance();
		CompanyModule module    = (CompanyModule) application.getModule(CompanyModule.class);
		Company com             = module.getCompany(companyID);
		
		tf_CompanyName.setValue(com.getCompanyName());
		sel_CompanyType.setSelectedOptions(new String[] { com.getCompanyType() });
		tf_CompanyStreet1.setValue(com.getCompanyStreet1());
		tf_CompanyStreet2.setValue(com.getCompanyStreet2());
		tf_CompanyCity.setValue(com.getCompanyCity());
		tf_CompanyState.setValue(com.getCompanyState());
		tf_CompanyPostcode.setValue(com.getCompanyPostcode());
		csb_CompanyCountry.setValue(com.getCompanyCountry());
		tf_CompanyTel.setValue(com.getCompanyTel());
		tf_CompanyFax.setValue(com.getCompanyFax());
		tf_CompanyWebsite.setValue(com.getCompanyWebsite());
		if (com.getCompanyPartnerTypeID() == null) {
			rd_IsPartner_No.setChecked(true);
		} else {
			rd_IsPartner_Yes.setChecked(true);
			for (int i=0; i<partnerTypeIDs.length; i++) {
				if (com.getCompanyPartnerTypeID().intValue() == partnerTypeIDs[i]) {
					rd_PartnerType[i].setChecked(true);
					break;
				}
			}
		} 
	}
	
	public String getDefaultTemplate() {
		return "sfa/Company_Form";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	 
}
