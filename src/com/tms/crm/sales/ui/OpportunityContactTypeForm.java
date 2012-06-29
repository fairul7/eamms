/*
 * Created on Dec 18, 2003
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
public class OpportunityContactTypeForm extends Form {
    protected SelectBox[] sel_ContactTypes;
    protected Label[] lbContactNames;
    protected Button submit;

    private Collection contactList;
    private Collection contacts;
    private String[] contactIDs;
    private String opportunityID;
    private String opportunityContactType;
    private int numContacts;

  //  private String type; // possible values: "Company_Tie", "Partner_Tie"


    /* Step 1: Initialization */
    public void init() {
        opportunityContactType = OpportunityContact.COMPANY_CONTACT;
        initForm();
    }

   /* public void setType(String string) {
        type = string;
    }
*/

    /* Step 2: Parameter passing (dynamic) */
    public String getOpportunityID() {
        return opportunityID;
    }

    public void setOpportunityID(String string) {
        opportunityID = string;
    }

    public String getNumContacts() {
        return String.valueOf(numContacts);
    }


    /* Step 3: Form display and processing */
    public void initForm() {
        removeChildren();
        setColumns(2);
        setMethod("POST");



        // query contacts tied to the opportunity
        //Collection col = module.listOpportunityContacts(opportunityID, opportunityContactType, null, false, 0, -1);

        // define SelectBox array, Label array & String array

        // initialize SelectBox array & Label array

        initFormElements();

        submit = new Button("submit", Application.getInstance().getMessage("sfa.label.submit","Submit"));
        addChild(submit);
    }

    private void initFormElements(){
        int i = 0;
        //contacts = null;
        Application application         = Application.getInstance();
        OpportunityContactModule module = (OpportunityContactModule) application.getModule(OpportunityContactModule.class);
        if(contactList!=null){
            sel_ContactTypes = new SelectBox[contactList.size()];
            lbContactNames   = new Label[contactList.size()];
            contactIDs       = new String[contactList.size()];
          Iterator iterator = contactList.iterator();
            while (iterator.hasNext()) {
                String contactId = (String)iterator.next();
                ContactModule cm = (ContactModule) Application.getInstance().getModule(ContactModule.class);
                Contact c = cm.getContact(contactId);
                contactIDs[i]       = contactId;
                lbContactNames[i]   = new Label("lbContactNames_" + i, c.getContactFirstName() + " " + c.getContactLastName());
                sel_ContactTypes[i] = new SelectBox("sel_ContactTypes_" + i);
                MyUtil.populate_SelectBox(sel_ContactTypes[i], module.getContactTypeCollection(), "contactTypeID", "contactTypeName");
                addChild(lbContactNames[i]);
                addChild(sel_ContactTypes[i]);
                i++;
            }
        }
        numContacts = i;

    }

    public void onRequest(Event evt) {
        initForm();
        if(contactList== null)
            contacts = null;
    }

    public Forward onValidate(Event evt) {
        //	Application application         = Application.getInstance();
        //	OpportunityContactModule module = (OpportunityContactModule) application.getModule(OpportunityContactModule.class);


        if(contactList!=null){
            //if(contacts == null)
            contacts = new ArrayList();
            for (int i=0; i<sel_ContactTypes.length; i++) {
                OpportunityContact opCon = new OpportunityContact();
                opCon.setOpportunityContactType(opportunityContactType);
                opCon.setContactID(contactIDs[i]);
                opCon.setContactTypeID(MyUtil.getSingleValue_SelectBox(sel_ContactTypes[i]));
                contacts.add(opCon);
            }
        }



/*
		for (int i=0; i<sel_ContactTypes.length; i++) {
			OpportunityContact opCon = new OpportunityContact();
			opCon.setOpportunityID(opportunityID);
			opCon.setOpportunityContactType(opportunityContactType);
			opCon.setContactID(contactIDs[i]);
			opCon.setContactTypeID(MyUtil.getSingleValue_SelectBox(sel_ContactTypes[i]));
			module.updateOpportunityContact(opCon);
		}

		OpportunityModule opModule = (OpportunityModule) application.getModule(OpportunityModule.class);
		Opportunity opp = opModule.getOpportunity(opportunityID);
*/

        // set status to open if conditions are right
/*
		if (opp.getOpportunityStatus().equals(Opportunity.STATUS_INCOMPLETE)) {
			boolean hasPartner = (opp.getHasPartner().equals("1") ? true : false);
			if ((type.equals("Company_Tie") && !hasPartner) || (type.equals("Partner_Tie") && hasPartner)) {
				// set status to open
				opp.setOpportunityStatus(Opportunity.STATUS_OPEN);
				opModule.updateOpportunity(opp);
			}
		}
*/

        initForm();
        contactList=  null;

        return new Forward("updatedOpportunityContactType");
    }

    public String getDefaultTemplate() {
        return "sfa/OpportunityContactType_Form";
    }

    public Collection getContactList() {
        return contactList;
    }

    public void setContactList(Collection contactList) {
        this.contactList = contactList;
        initFormElements();
    }

    public Collection getContacts() {
        return contacts;
    }

    public void setContacts(Collection contacts) {
        this.contacts = contacts;
    }
}
