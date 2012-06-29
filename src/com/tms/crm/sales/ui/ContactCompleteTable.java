package com.tms.crm.sales.ui;

import kacang.stdui.TableModel;
import kacang.stdui.TableColumn;
import kacang.stdui.TableAction;
import kacang.stdui.TableFilter;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;

import java.util.TreeSet;
import java.util.Collection;

import com.tms.crm.sales.model.ContactModule;
import com.tms.crm.sales.model.OpportunityContact;
import com.tms.crm.sales.model.OpportunityContactModule;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jul 6, 2004
 * Time: 4:14:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContactCompleteTable extends ContactTable {
    /* Step 1: Initialization */
    public static final String FORWARD_MOVE_CONTACT  ="movecontact";

    public void init() {
        contactList = new TreeSet();
        setModel(new ContactCompleteTableModel());
    }

    public class ContactCompleteTableModel extends TableModel{


        public ContactCompleteTableModel() {
            TableColumn tc_ContactFirstName = new TableColumn("contactFirstName", Application.getInstance().getMessage("sfa.label.firstName","First Name"));
            tc_ContactFirstName.setUrlParam("contactID");
            addColumn(tc_ContactFirstName);


            TableColumn tc_ContactLastName = new TableColumn("contactLastName", Application.getInstance().getMessage("sfa.label.lastName","Last Name"));
            tc_ContactLastName.setUrlParam("contactID");
            //	tc_ContactLastName.setUrl(linkUrl);   saf
            //	tc_ContactLastName.setUrlParam("contactID");
            /*  tc_ContactLastName.setFormat(new TableFormat(){
            public String format(Object o) {
            Contact c = (Contact)getCurrentRow();
            return "<a href=\"#\" onClick=\"window.open('pop_contactview.jsp?contactID="+c.getContactID()+ "','','resizable=yes,width=750,height=420,scrollbars=yes')\">"+o.toString()+"</a>";
            //return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
            });*/
            addColumn(tc_ContactLastName);


            TableColumn tc_ContactDesignation = new TableColumn("contactDesignation", Application.getInstance().getMessage("sfa.label.designation","Designation"));
            addColumn(tc_ContactDesignation);

            TableColumn tc_ContactDirectNum = new TableColumn("contactDirectNum", Application.getInstance().getMessage("sfa.label.directNo","Direct No."));
            addColumn(tc_ContactDirectNum);

            TableColumn tc_ContactMobile = new TableColumn("contactMobile", Application.getInstance().getMessage("sfa.label.mobileNo.","Mobile No."));
            addColumn(tc_ContactMobile);

            TableColumn tc_ContactEmail = new TableColumn("emailLink", Application.getInstance().getMessage("sfa.label.email","Email"));
            addColumn(tc_ContactEmail);


            addFilter(new TableFilter("keyword"));
            addAction(new TableAction("move",Application.getInstance().getMessage("sfa.label.move","Move")));
        }

        public Collection getTableRows() {
            Application application = Application.getInstance();
            ContactModule module    = (ContactModule) application.getModule(ContactModule.class);
            String keyword = (String) getFilterValue("keyword");
            return module.listContacts(keyword, null, null, getSort(), isDesc(), getStart(), getRows());
        }

        public Collection getAllTableRows() {
            Application application = Application.getInstance();
            ContactModule module    = (ContactModule) application.getModule(ContactModule.class);
            String keyword = (String) getFilterValue("keyword");
            String oppID = null;
            return module.listContacts(keyword, null, oppID, getSort(), isDesc(), getStart(), -1);
        }

        public int getTotalRowCount() {
            Application application = Application.getInstance();
            ContactModule module    = (ContactModule) application.getModule(ContactModule.class);
            String keyword = (String) getFilterValue("keyword");
            String oppID = null;
            return module.countContacts(keyword, null, oppID);
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
            }
            return null;
        }





    }


}
