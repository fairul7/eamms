/*
 * Created on Dec 18, 2003
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
import org.apache.commons.collections.SequencedHashMap;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ContactForm extends Form {

    
	protected TextField tf_ContactLastName;
	protected TextField tf_ContactFirstName;
	protected TextField tf_ContactDesignation;
	protected SelectBox sel_SalutationID;
	protected TextField tf_ContactStreet1;
	protected TextField tf_ContactStreet2;
	protected TextField tf_ContactCity;
	protected TextField tf_ContactState;
	protected TextField tf_ContactPostcode;
	protected CountrySelectBox csb_ContactCountry;
	protected TextField tf_ContactDirectNum;
	protected TextField tf_ContactMobile;
	protected TextField tf_ContactEmail;
	protected TextBox   tb_ContactRemarks;
    protected SelectBox sel_Companies;
	protected Button submit,cancel;
	
	private Label lbContactLastName;
	private Label lbContactFirstName;
	private Label lbContactDesignation;
	private Label lbSalutationID;
	private Label lbContactStreet1;
	private Label lbContactStreet2;
	private Label lbContactCity;
	private Label lbContactState;
	private Label lbContactPostcode;
	private Label lbContactCountry;
	private Label lbContactDirectNum;
	private Label lbContactMobile;
	private Label lbContactEmail;
	private Label lbContactRemarks;
	
	private String justCreatedID = "";
	private String contactID;
	private String companyID;
	private String companyName = "";
	private String opportunityID;
	private Label lbCompanyName;
	
	private String type;		// possible values: "View", "Add", "Edit"
	private String subType;		// possible values: "Partner", null
	private boolean createPartnerContact;
	
	protected String status;
	
	
	/* Step 1: Initialization */
	public void init() {
		if (!MyUtil.isValidChoice(type, new String[] {"View", "Add", "Edit"})) {
			type = "Add";
			System.out.println("Error!!! Wrong type passed. ContactForm");
		}
		
		createPartnerContact = false;
		if (subType != null && subType.equals("Partner")) {
			if (type.equals("Add")) {
				createPartnerContact = true;
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
	
	public String getContactID() {
		return contactID;
	}
	
	public void setContactID(String string) {
		contactID = string;
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
        populateCompanyID();
	}
	
	
	/* Step 3: Form display and processing */
	public void initForm() {
		removeChildren();
		setColumns(2);
		setMethod("POST");
		
		addChild(new Label("lb1", Application.getInstance().getMessage("sfa.label.company","Company")+":"));
		lbCompanyName = new Label("lbCompanyName", companyName);
		addChild(lbCompanyName);
		
		addChild(new Label("lb2", Application.getInstance().getMessage("sfa.label.lastName","Last Name")+":"));
		if (type.equals("View")) {
			lbContactLastName = new Label("lbContactLastName", "");
			addChild(lbContactLastName);
		} else {
			tf_ContactLastName = new TextField("tf_ContactLastName");
			tf_ContactLastName.setMaxlength("100");
			tf_ContactLastName.setSize("40");
			ValidatorNotEmpty vne = new ValidatorNotEmpty("vne", Application.getInstance().getMessage("sfa.label.mustnotbeempty","Must not be empty"));
			tf_ContactLastName.addChild(vne);
			addChild(tf_ContactLastName);
		}
		
		addChild(new Label("lb2_b", Application.getInstance().getMessage("sfa.label.firstName","First Name")+":"));
		if (type.equals("View")) {
			lbContactFirstName = new Label("lbContactFirstName", "");
			addChild(lbContactFirstName);
		} else {
			tf_ContactFirstName = new TextField("tf_ContactFirstName");
			tf_ContactFirstName.setMaxlength("100");
			tf_ContactFirstName.setSize("40");
			addChild(tf_ContactFirstName);
		}
		
		addChild(new Label("lb3", Application.getInstance().getMessage("sfa.label.designation","Designation")+":"));
		if (type.equals("View")) {
			lbContactDesignation = new Label("lbContactDesignation", "");
			addChild(lbContactDesignation);
		} else {
			tf_ContactDesignation = new TextField("tf_ContactDesignation");
			tf_ContactDesignation.setMaxlength("100");
			tf_ContactDesignation.setSize("40");
			addChild(tf_ContactDesignation);
		}
		
		Application application = Application.getInstance();
		ContactModule module = (ContactModule) application.getModule(ContactModule.class);
		
		addChild(new Label("lb3_b", Application.getInstance().getMessage("sfa.label.salutation","Salutation")+":"));
		if (type.equals("View")) {
			lbSalutationID = new Label("lbSalutationID", "");
			addChild(lbSalutationID);
		} else {
			sel_SalutationID = new SelectBox("sel_SalutationID");
			Collection col = module.getSalutationCollection();
			Iterator iterator = col.iterator();
			while (iterator.hasNext()) {
				HashMap hm = (HashMap) iterator.next();
				sel_SalutationID.addOption((String) hm.get("salutationID"), (String) hm.get("salutationText"));
			}
			addChild(sel_SalutationID);
		}

        if(!type.equals("View")){
            sel_Companies = new SelectBox("sel_Companies");
            CompanyModule cm = (CompanyModule) Application.getInstance().getModule(CompanyModule.class);

            TreeMap sortedMap = new TreeMap();
            Map cmap =cm.getCompanyMap();
            for (Iterator iterator = cmap.keySet().iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                sortedMap.put(cmap.get(key),key);
            }
            SequencedHashMap companiesMap = new SequencedHashMap();
            for (Iterator iterator = sortedMap.keySet().iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                companiesMap.put(sortedMap.get(key),key);
            }

            sel_Companies.setOptionMap(companiesMap);
            addChild(sel_Companies);

        }





		addChild(new Label("lb4", Application.getInstance().getMessage("sfa.label.address","Address")+":"));
		if (type.equals("View")) {
			lbContactStreet1 = new Label("lbContactStreet1", "");
			addChild(lbContactStreet1);
		} else {
			tf_ContactStreet1 = new TextField("tf_ContactStreet1");
			tf_ContactStreet1.setMaxlength("255");
			tf_ContactStreet1.setSize("40");
			addChild(tf_ContactStreet1);
		}
		
		addChild(new Label("lb5", ""));
		if (type.equals("View")) {
			lbContactStreet2 = new Label("lbContactStreet2", "");
			addChild(lbContactStreet2);
		} else {
			tf_ContactStreet2 = new TextField("tf_ContactStreet2");
			tf_ContactStreet2.setMaxlength("255");
			tf_ContactStreet2.setSize("40");
			addChild(tf_ContactStreet2);
		}
		
		addChild(new Label("lb6", Application.getInstance().getMessage("sfa.label.city","City")+":"));
		if (type.equals("View")) {
			lbContactCity = new Label("lbContactCity", "");
			addChild(lbContactCity);
		} else {
			tf_ContactCity = new TextField("tf_ContactCity");
			tf_ContactCity.setMaxlength("50");
			tf_ContactCity.setSize("20");
			addChild(tf_ContactCity);
		}
		
		addChild(new Label("lb6_b", Application.getInstance().getMessage("sfa.label.state","State")+":"));
		if (type.equals("View")) {
			lbContactState = new Label("lbContactState", "");
			addChild(lbContactState);
		} else {
			tf_ContactState = new TextField("tf_ContactState");
			tf_ContactState.setMaxlength("50");
			tf_ContactState.setSize("20");
			addChild(tf_ContactState);
		}
		
		addChild(new Label("lb7", Application.getInstance().getMessage("sfa.label.postcode","Postcode")+":"));
		if (type.equals("View")) {
			lbContactPostcode = new Label("lbContactPostcode", "");
			addChild(lbContactPostcode);
		} else {
			tf_ContactPostcode = new TextField("tf_ContactPostcode");
			tf_ContactPostcode.setMaxlength("20");
			tf_ContactPostcode.setSize("20");
			addChild(tf_ContactPostcode);
		}
		
		addChild(new Label("lb8", Application.getInstance().getMessage("sfa.label.country","Country")+":"));
		if (type.equals("View")) {
			lbContactCountry = new Label("lbContactCountry", "");
			addChild(lbContactCountry);
		} else {
			csb_ContactCountry = new CountrySelectBox("csb_ContactCountry");
			addChild(csb_ContactCountry);
		}
		
		addChild(new Label("lb9", Application.getInstance().getMessage("sfa.label.directNo","Direct No.")+":"));
		if (type.equals("View")) {
			lbContactDirectNum = new Label("lbContactDirectNum", "");
			addChild(lbContactDirectNum);
		} else {
			tf_ContactDirectNum = new TextField("tf_ContactDirectNum");
			tf_ContactDirectNum.setMaxlength("30");
			tf_ContactDirectNum.setSize("20");
			addChild(tf_ContactDirectNum);
		}
		
		addChild(new Label("lb10", Application.getInstance().getMessage("sfa.label.mobileNo.","Mobile No.")+":"));
		if (type.equals("View")) {
			lbContactMobile = new Label("lbContactMobile", "");
			addChild(lbContactMobile);
		} else {
			tf_ContactMobile = new TextField("tf_ContactMobile");
			tf_ContactMobile.setMaxlength("30");
			tf_ContactMobile.setSize("20");
			addChild(tf_ContactMobile);
		}
		
		addChild(new Label("lb11", Application.getInstance().getMessage("sfa.label.email","Email")+":"));
		if (type.equals("View")) {
			lbContactEmail = new Label("lbContactEmail", "");
			addChild(lbContactEmail);
		} else {
			tf_ContactEmail = new TextField("tf_ContactEmail");
			tf_ContactEmail.addChild(new ValidatorEmail("email_validator"));
			tf_ContactEmail.setMaxlength("100");
			tf_ContactEmail.setSize("40");
			addChild(tf_ContactEmail);
		}
		
		addChild(new Label("lb12", Application.getInstance().getMessage("sfa.label.remarks","Remarks")+":"));
		if (type.equals("View")) {
			lbContactRemarks = new Label("lbContactRemarks", "");
			addChild(lbContactRemarks);
		} else {
			tb_ContactRemarks = new TextBox("tb_ContactRemarks");
			tb_ContactRemarks.setRows("4");
			tb_ContactRemarks.setCols("40");
			addChild(tb_ContactRemarks);
		}

		if (type.equals("Edit")) {
			submit = new Button("submit", Application.getInstance().getMessage("sfa.label.update","Update"));
			cancel = new Button("cancel", Application.getInstance().getMessage("sfa.label.cancel","Cancel"));
		} else {
			submit = new Button("submit", Application.getInstance().getMessage("sfa.label.submit","Submit"));
			cancel = new Button("cancel",Application.getInstance().getMessage("sfa.label.cancel","Cancel"));
		}
        addChild(cancel);
        addChild(submit);
        populateCompanyName();
	}
	
	public void onRequest(Event evt) {
		initForm();
		
		if (type.equals("View")) {
			populateView();
		} else if (type.equals("Add")) {
	//		if (opportunityID != null && !opportunityID.equals("")) {
/*
            if(companyID != null && companyID.trim().length()>0){
				populateCompanyID();
			}
*/
		} else if (type.equals("Edit")) {
			populateEdit();
		}
		
		populateCompanyName();
	}

    public Forward onSubmit(Event event) {
        if(cancel.getAbsoluteName().equals(findButtonClicked(event)))
			if("closed".equals(status)){
				return new Forward("cancelClosed");
			}else{
				return new Forward("cancel");
			}
        return super.onSubmit(event);    //To change body of overridden methods use File | Settings | File Templates.
    }

	public Forward onValidate(Event evt) {
        Forward myForward = null;
        if(submit.getAbsoluteName().equals(findButtonClicked(evt))){
            if (type.equals("Add")) {
                myForward = addContact();
            } else if (type.equals("Edit")) {
                myForward = editContact();
            }
            initForm();
        }
		return myForward;
	}
	
	private Forward addContact() {
		Application application = Application.getInstance();
		ContactModule module = (ContactModule) application.getModule(ContactModule.class);
		
		Contact con = new Contact();
		UuidGenerator uuid = UuidGenerator.getInstance();
		String conID = uuid.getUuid();
		con.setContactID(conID);
        String companyId = null;
        if(companyID!=null&&companyID.trim().length()>0)
            companyId = companyID;
        else {
       		companyId = (String)sel_Companies.getSelectedOptions().keySet().iterator().next();
        }
        con.setCompanyID(companyId);
		con.setContactLastName((String) tf_ContactLastName.getValue());
		con.setContactFirstName((String) tf_ContactFirstName.getValue());
		con.setContactDesignation((String) tf_ContactDesignation.getValue());
		con.setSalutationID(MyUtil.getSingleValue_SelectBox(sel_SalutationID));
		con.setContactStreet1((String) tf_ContactStreet1.getValue());
		con.setContactStreet2((String) tf_ContactStreet2.getValue());
		con.setContactCity((String) tf_ContactCity.getValue());
		con.setContactState((String) tf_ContactState.getValue());
		con.setContactPostcode((String) tf_ContactPostcode.getValue());
		con.setContactCountry((String) csb_ContactCountry.getValue());
		con.setContactDirectNum((String) tf_ContactDirectNum.getValue());
		con.setContactMobile((String) tf_ContactMobile.getValue());
		con.setContactEmail((String) tf_ContactEmail.getValue());
		con.setContactRemarks((String) tb_ContactRemarks.getValue());
		
		module.addContact(con);
		initForm();
		justCreatedID = conID;
		return new Forward("contactAdded");
	}
	
	private Forward editContact() {
		Application application = Application.getInstance();
		ContactModule module = (ContactModule) application.getModule(ContactModule.class);
		
		Contact con = module.getContact(contactID);
		con.setContactLastName((String) tf_ContactLastName.getValue());
		con.setContactFirstName((String) tf_ContactFirstName.getValue());
		con.setContactDesignation((String) tf_ContactDesignation.getValue());
		con.setSalutationID(MyUtil.getSingleValue_SelectBox(sel_SalutationID));
		con.setContactStreet1((String) tf_ContactStreet1.getValue());
		con.setContactStreet2((String) tf_ContactStreet2.getValue());
		con.setContactCity((String) tf_ContactCity.getValue());
		con.setContactState((String) tf_ContactState.getValue());
		con.setContactPostcode((String) tf_ContactPostcode.getValue());
		con.setContactCountry((String) csb_ContactCountry.getValue());
		con.setContactDirectNum((String) tf_ContactDirectNum.getValue());
		con.setContactMobile((String) tf_ContactMobile.getValue());
		con.setContactEmail((String) tf_ContactEmail.getValue());
		con.setContactRemarks((String) tb_ContactRemarks.getValue());
		
		module.updateContact(con);
		initForm();
		
		return new Forward("contactUpdated");
	}
	
	public void populateCompanyID() {
		// update the companyID based on the opportunityID
		Application application     = Application.getInstance();
		OpportunityModule oppModule = (OpportunityModule) application.getModule(OpportunityModule.class);
		Opportunity opp = oppModule.getOpportunity(opportunityID);
		if (createPartnerContact) {
			companyID = opp.getPartnerCompanyID();
		} else {
			companyID = opp.getCompanyID();
		}
	}
	
	public void populateCompanyName() {
		Application application = Application.getInstance();
		CompanyModule comModule = (CompanyModule) application.getModule(CompanyModule.class);
		CompanyDao comDao       = (CompanyDao) comModule.getDao();
		Company com= null;
		try {
			com = comDao.selectRecord(companyID);
			companyName = com.getCompanyName();
		} catch (Exception e) {
			companyName = Application.getInstance().getMessage("sfa.label.errorgettingCompanyName","Error getting Company Name");
		}
        if(!type.equals("View"))
        {
            if(com!=null && (tf_ContactDirectNum.getValue()==null || "".equals(tf_ContactDirectNum.getValue()))){
                tf_ContactDirectNum.setValue(com.getCompanyTel());
            }
        }

		lbCompanyName.setText(companyName);

	}
	
	public void populateView() {
		Application application = Application.getInstance();
		ContactModule module    = (ContactModule) application.getModule(ContactModule.class);
		Contact con             = module.getContact(contactID);
		
		companyID = con.getCompanyID();
		lbContactLastName.setText(con.getContactLastName());
		lbContactFirstName.setText(con.getContactFirstName());
		lbContactDesignation.setText(con.getContactDesignation());
		lbSalutationID.setText((String) module.getSalutationMap().get(con.getSalutationID()));
		lbContactStreet1.setText(con.getContactStreet1());
		lbContactStreet2.setText(con.getContactStreet2());
		lbContactCity.setText(con.getContactCity());
		lbContactState.setText(con.getContactState());
		lbContactPostcode.setText(con.getContactPostcode());
		
		CountrySelectBox csb_temp = new CountrySelectBox("csb_temp");
		lbContactCountry.setText((String) csb_temp.getOptionMap().get(con.getContactCountry()));
		
		lbContactDirectNum.setText(con.getContactDirectNum());
		lbContactMobile.setText(con.getContactMobile());
		lbContactEmail.setText(con.getEmailLink());
		lbContactRemarks.setText(con.getContactRemarks());
	}
	
	public void populateEdit() {
		Application application = Application.getInstance();
		ContactModule module    = (ContactModule) application.getModule(ContactModule.class);
		Contact con             = module.getContact(contactID);
		
		companyID = con.getCompanyID();
		tf_ContactLastName.setValue(con.getContactLastName());
		tf_ContactFirstName.setValue(con.getContactFirstName());
		tf_ContactDesignation.setValue(con.getContactDesignation());
		sel_SalutationID.setSelectedOptions(new String[] { con.getSalutationID() });
		tf_ContactStreet1.setValue(con.getContactStreet1());
		tf_ContactStreet2.setValue(con.getContactStreet2());
		tf_ContactCity.setValue(con.getContactCity());
		tf_ContactState.setValue(con.getContactState());
		tf_ContactPostcode.setValue(con.getContactPostcode());
		csb_ContactCountry.setValue(con.getContactCountry());
		tf_ContactDirectNum.setValue(con.getContactDirectNum());
		tf_ContactMobile.setValue(con.getContactMobile());
		tf_ContactEmail.setValue(con.getContactEmail());
		tb_ContactRemarks.setValue(con.getContactRemarks());
	}
	
	public String getDefaultTemplate() {
		return "sfa/Contact_Form";
	}

    public Button getCancel() {
        return cancel;
    }

    public void setCancel(Button cancel) {
        this.cancel = cancel;
    }

    public SelectBox getSel_Companies() {
        return sel_Companies;
    }

    public void setSel_Companies(SelectBox sel_Companies) {
        this.sel_Companies = sel_Companies;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
