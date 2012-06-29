/*
 * Created on Dec 3, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.ui;

import java.util.*;
import java.lang.reflect.Array;

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
public class MoveContactCompanyTable extends SalesTable  {
	private TableColumn tc_CompanyName;
	private String opportunityID;
	private String selectedCompanyID;

    private Collection contactList;
	private String type;        // possible values: "Company_List", "Partner_List"
	private boolean partnerOnly = false;
    public static final String FORWARD_CONTACTS_MOVED = "contactsmoved";
	
	/* Step 1: Initialization */
	public void init() {
		if (!MyUtil.isValidChoice(type, new String[] {"Company_List", "Partner_List"})) {
			type = "Company_List";
			System.out.println("Error!!! Wrong type passed. CompanyTable");
		}
		
		if (type.equals("Partner_List")) {
			partnerOnly = true;
		}
		

		
		setMultipleSelect(false);
		setModel(new CompanyTableModel());
        setSort("lastModified");
        setDesc(true);
	}
	
	public void setType(String string) {
		type = string;
	}
	

	
	
	/* Step 2: Parameter passing (dynamic) */

	public String getOpportunityID() {
		return (opportunityID);
	}
	
	public void setOpportunityID(String string) {
		opportunityID = string;
	}
	
	public String getSelectedCompanyID() {
		return (selectedCompanyID);
	}

    public void setSelectedCompanyID(String selectedCompanyID) {
        getSelectedRowList().add(selectedCompanyID);
//        getSelectedRowMap().put(selectedCompanyID, Boolean.TRUE);
    }

	/* Step 3: Table display and processing */
	public class CompanyTableModel extends TableModel {
	
		public CompanyTableModel() {

			tc_CompanyName = new TableColumn("companyName", Application.getInstance().getMessage("sfa.label.company","Company"));
			tc_CompanyName.setUrlParam("companyID");
			addColumn(tc_CompanyName);
			
			TableColumn tc_CompanyType = new TableColumn("companyType", Application.getInstance().getMessage("sfa.label.type","Type"));
			tc_CompanyType.setFormat(new TableStringFormat(Company.getCompanyType_Map()));
			addColumn(tc_CompanyType);
			
			TableColumn tc_CompanyTel = new TableColumn("companyTel", Application.getInstance().getMessage("sfa.label.telephone","Telephone"));
			addColumn(tc_CompanyTel);
			
			TableColumn tc_CompanyFax = new TableColumn("companyFax", Application.getInstance().getMessage("sfa.label.fax","Fax"));
			addColumn(tc_CompanyFax);
			
			HashMap hm = new HashMap();
			hm.put("", Application.getInstance().getMessage("sfa.label.no","No"));
			TableColumn tc_PartnerType = new TableColumn("companyPartnerTypeID", Application.getInstance().getMessage("sfa.label.partner","Partner"));
			TableLimitStringFormat tlsf = new TableLimitStringFormat(hm, 10);
			tlsf.setDefaultValue(Application.getInstance().getMessage("sfa.label.yes","Yes"));
			tc_PartnerType.setFormat(tlsf);
			addColumn(tc_PartnerType);
			
			addAction(new TableAction("select_bottom", Application.getInstance().getMessage("sfa.label.move","Move")));
			
			addFilter(new TableFilter("keyword"));
		}
		
		public Collection getTableRows() {
			Application application = Application.getInstance();
			CompanyModule module    = (CompanyModule) application.getModule(CompanyModule.class);
			String keyword = (String) getFilterValue("keyword");
			
			if (partnerOnly) {
				return module.listPartners(keyword, getSort(), isDesc(), getStart(), getRows());
			} else {
				return module.listCompanies(keyword, getSort(), isDesc(), getStart(), getRows());
			}
		}
		
		public int getTotalRowCount() {
			Application application = Application.getInstance();
			CompanyModule module    = (CompanyModule) application.getModule(CompanyModule.class);
			String keyword = (String) getFilterValue("keyword");
			
			if (partnerOnly) {
				return module.countPartners(keyword);
			} else {
				return module.countCompanies(keyword);
			}
		}
		
		public String getTableRowKey() {
			return "companyID";
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {

			if ("select_bottom".equals(action) && selectedKeys.length == 1) {
                if(contactList!=null&&contactList.size()>0){
                    ContactModule cm = (ContactModule) Application.getInstance().getModule(ContactModule.class);
                    Contact contact;
                    for (Iterator iterator = contactList.iterator(); iterator.hasNext();) {
                        String contactId = (String) iterator.next();
                        contact = cm.getContact(contactId);
                        contact.setCompanyID(selectedKeys[0]);
                        cm.updateContact(contact);
                    }
                    selectedCompanyID = selectedKeys[0];
                    return new Forward(FORWARD_CONTACTS_MOVED);
                }
            }
			return null;
		}
	}
	

	public String getDefaultTemplate() {
		return "sfa/SalesTable";
	}

    public Collection getContactList() {
        return contactList;
    }

    public void setContactList(Collection contactList) {
        this.contactList = contactList;
    }
}
