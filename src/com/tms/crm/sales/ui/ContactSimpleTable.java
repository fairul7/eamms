/*
 * Created on Dec 18, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.ui;

import java.util.*;
import kacang.stdui.*;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import com.tms.crm.sales.misc.*;
import com.tms.crm.sales.model.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ContactSimpleTable extends Table {
    //private String linkUrl = "someURL";
    private TableColumn tc_ContactLastName;
    private String companyID;
    private String opportunityID;
    private boolean justTiedContact;

    private String clear = null;
    private Collection contactList = null;
    private String type;		// possible values: "Company_Contacts", "Partner_Contacts", "Contact_NoTieOpportunity"
    private String subType;		// possible values: "Filter_OpportunityContacts", null
    private boolean partnerOnly = false;
    private boolean filterOpportunityContacts = false;
    public static final String FORWARD_MOVE_CONTACT  ="movecontact";

    /* Step 1: Initialization */
    public void init() {
        /*
        * Company_Contacts = Company contact tied to opportunity
        * Partner_Contacts = Partner contact tied to opportunity
        * Contact_NoTieOpportunity = Contact that is not to be tied to a opportunity
        */
        if (!MyUtil.isValidChoice(type, new String[] {"Company_Contacts", "Partner_Contacts", "Contact_NoTieOpportunity"})) {
            type = "Company_Contacts";
            System.out.println("Error!!! Wrong type passed. ContactTable");
        }

        if (type.equals("Partner_Contacts")) {
            partnerOnly = true;
        }

        if (subType != null && subType.equals("Filter_OpportunityContacts")) {
            filterOpportunityContacts = true;
        }
        contactList = new TreeSet();
        setModel(new ContactTableModel());
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

    public Forward actionPerformed(Event event) {
        onSubmit(event);
        return super.actionPerformed(event);    //To change body of overridden methods use File | Settings | File Templates.


    }
    /* Step 2: Parameter passing (dynamic) */
/*
	public void setLinkUrl(String url) {
		linkUrl = url;
	}
*/

    public boolean getJustTiedContact() {
        boolean flag = justTiedContact;
        justTiedContact = false; 		// reset the "just-tied-contact" flag
        return flag;
    }

    public String getCompanyID() {
        return (companyID);
    }

    public void setCompanyID(String string) {
        if(companyID==null||!companyID.equals(string)){
            setCurrentPage(1);
        }
        companyID = string;
    }

    public String getOpportunityID() {
        return opportunityID;
    }

    public void setOpportunityID(String string) {
        opportunityID = string;

        // update the companyID based on the opportunityID
        Application application     = Application.getInstance();
        OpportunityModule oppModule = (OpportunityModule) application.getModule(OpportunityModule.class);
        Opportunity opp = oppModule.getOpportunity(opportunityID);
        if (partnerOnly) {
            setCompanyID(opp.getPartnerCompanyID());
        } else {
            setCompanyID(opp.getCompanyID());
        }
    }


    /* Step 3: Table display and processing */
    public class ContactTableModel extends TableModel {

        public ContactTableModel() {
            TableColumn tc_ContactFirstName = new TableColumn("contactFirstName", Application.getInstance().getMessage("sfa.label.firstName","First Name"));
            tc_ContactFirstName.setUrlParam("contactID");
            addColumn(tc_ContactFirstName);

            tc_ContactLastName = new TableColumn("contactLastName", Application.getInstance().getMessage("sfa.label.lastName","Last Name"));
            tc_ContactLastName.setUrlParam("contactID");
            addColumn(tc_ContactLastName);

            TableColumn tc_ContactDesignation = new TableColumn("contactDesignation", Application.getInstance().getMessage("sfa.label.designation","Designation"));
            addColumn(tc_ContactDesignation);

            TableColumn tc_ContactDirectNum = new TableColumn("contactDirectNum", Application.getInstance().getMessage("sfa.label.directNo","Direct No."));
            addColumn(tc_ContactDirectNum);

            TableColumn tc_ContactMobile = new TableColumn("contactMobile", Application.getInstance().getMessage("sfa.label.mobileNo.","Mobile No."));
            addColumn(tc_ContactMobile);

            TableColumn tc_ContactEmail = new TableColumn("emailLink", Application.getInstance().getMessage("sfa.label.email","Email"));
            addColumn(tc_ContactEmail);

            if (filterOpportunityContacts) {
                TableColumn tc_ContactTypeName = new TableColumn("contactTypeName", Application.getInstance().getMessage("sfa.label.contactType","Contact Type"));
                addColumn(tc_ContactTypeName);
            }
            addAction(new TableAction("move",Application.getInstance().getMessage("sfa.label.move","Move")));
            addFilter(new TableFilter("keyword"));
        }

        public Collection getTableRows() {
            Application application = Application.getInstance();
            ContactModule module    = (ContactModule) application.getModule(ContactModule.class);
            String keyword = (String) getFilterValue("keyword");
            String oppID = (filterOpportunityContacts) ? opportunityID : null;
            return module.listContacts(keyword, companyID, oppID, getSort(), isDesc(), getStart(), getRows());
        }

        public Collection getAllTableRows() {
            Application application = Application.getInstance();
            ContactModule module    = (ContactModule) application.getModule(ContactModule.class);
            String keyword = (String) getFilterValue("keyword");
            String oppID = (filterOpportunityContacts) ? opportunityID : null;
            return module.listContacts(keyword, companyID, oppID, getSort(), isDesc(), getStart(), -1);
        }

        public int getTotalRowCount() {
            Application application = Application.getInstance();
            ContactModule module    = (ContactModule) application.getModule(ContactModule.class);
            String keyword = (String) getFilterValue("keyword");
            String oppID = (filterOpportunityContacts) ? opportunityID : null;
            return module.countContacts(keyword, companyID, oppID);
        }

        public String getTableRowKey() {
            return "contactID";
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            //Application application              = Application.getInstance();
            //	OpportunityContactModule opConModule = (OpportunityContactModule) application.getModule(OpportunityContactModule.class);

            if("move".equals(action)){
                contactList.clear();
                for (int i = 0; i < selectedKeys.length; i++) {
                    String selectedKey = selectedKeys[i];
                    contactList.add(selectedKey);
                }
                return new Forward(FORWARD_MOVE_CONTACT);
            }else if ("select_bottom".equals(action)) {
                if (selectedKeys.length != 0) {
                    String opportunityContactType;
                    if (partnerOnly) {
                        opportunityContactType = OpportunityContact.PARTNER_CONTACT;
                    } else {
                        opportunityContactType = OpportunityContact.COMPANY_CONTACT;
                    }

                    // delete existing contacts tied to the opportunity
                    OpportunityContactModule opConModule = (OpportunityContactModule) Application.getInstance().getModule(OpportunityContactModule.class);
                    opConModule.deleteOpportunityContacts(opportunityID, opportunityContactType);
                    contactList.clear();
                    for (int i = 0; i < selectedKeys.length; i++) {
                        String selectedKey = selectedKeys[i];
                        contactList.add(selectedKey);
                    }
/*


					// tie contacts to the opportunity
					for (int i=0; i<selectedKeys.length; i++) {
						OpportunityContact opCon = new OpportunityContact();
						opCon.setOpportunityID(opportunityID);
						opCon.setOpportunityContactType(opportunityContactType);
						opCon.setContactID(selectedKeys[i]);
						opCon.setContactTypeID(OpportunityContact.DEFAULT);
						opConModule.addOpportunityContact(opCon);
					}
*/
                    justTiedContact = true;
                    return new Forward("contactsSelected");
                }
            }
            return null;
        }
    }

    public void onRequest(Event evt) {
        if(clear!=null &&clear.equals("1")&&contactList!=null){
            contactList.clear();
            clear = null;
        }
        // set the link
        //tc_ContactLastName.setUrl(linkUrl);
    }


    public Collection getContactList() {
        return contactList;
    }

    public void setContactList(Collection contactList) {
        this.contactList = contactList;
    }

    public String getClear() {
        return clear;
    }

    public void setClear(String clear) {
        this.clear = clear;
    }
}
