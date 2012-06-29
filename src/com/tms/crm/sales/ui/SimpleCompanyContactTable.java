package com.tms.crm.sales.ui;

import kacang.stdui.*;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;

import java.util.Collection;

import com.tms.crm.sales.model.*;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jun 9, 2004
 * Time: 2:00:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleCompanyContactTable extends SalesTable{

    private String companyID;
    private String opportunityID;


    /* Step 1: Initialization */
    public void init() {
        /*
        * Company_Contacts = Company contact tied to opportunity
        * Partner_Contacts = Partner contact tied to opportunity
        * Contact_NoTieOpportunity = Contact that is not to be tied to a opportunity
        */
        setNumbering(false);
        setModel(new ContactTableModel());
    }


    public Forward actionPerformed(Event event) {
        return super.actionPerformed(event);    //To change body of overridden methods use File | Settings | File Templates.


    }
    /* Step 2: Parameter passing (dynamic) */
/*
	public void setLinkUrl(String url) {
		linkUrl = url;
	}
*/


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
        setCompanyID(opp.getCompanyID());
    }


    /* Step 3: Table display and processing */
    public class ContactTableModel extends TableModel {

        public ContactTableModel() {
            TableColumn tc_ContactFirstName = new TableColumn("contactFirstName", Application.getInstance().getMessage("sfa.label.firstName","First Name"));
            tc_ContactFirstName.setUrlParam("contactID");
            addColumn(tc_ContactFirstName);

            TableColumn tc_ContactLastName = new TableColumn("contactLastName", Application.getInstance().getMessage("sfa.label.lastName","Last Name"));
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

            TableColumn tc_ContactTypeName = new TableColumn("contactTypeName", Application.getInstance().getMessage("sfa.label.contactType","Contact Type"));
            addColumn(tc_ContactTypeName);

        }

        public Collection getTableRows() {
            Application application = Application.getInstance();
            ContactModule module    = (ContactModule) application.getModule(ContactModule.class);
            String keyword = (String) getFilterValue("keyword");
         //   String oppID = (filterOpportunityContacts) ? opportunityID : null;
            return module.listContacts(keyword, companyID, opportunityID, getSort(), isDesc(), getStart(), getRows());
        }

        public Collection getAllTableRows() {
            Application application = Application.getInstance();
            ContactModule module    = (ContactModule) application.getModule(ContactModule.class);
            String keyword = (String) getFilterValue("keyword");
//            String oppID = (filterOpportunityContacts) ? opportunityID : null;
            return module.listContacts(keyword, companyID, opportunityID, getSort(), isDesc(), getStart(), -1);
        }

        public int getTotalRowCount() {
            Application application = Application.getInstance();
            ContactModule module    = (ContactModule) application.getModule(ContactModule.class);
            String keyword = (String) getFilterValue("keyword");
//            String oppID = (filterOpportunityContacts) ? opportunityID : null;
            return module.countContacts(keyword, companyID, opportunityID);
        }

        public String getTableRowKey() {
            return "contactID";
        }
    }

    /*public String getDefaultTemplate() {
        return "sfa/contactTable";
    }*/

}
