/*
 * Created on Dec 3, 2003
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
public class CompanySimpleTable extends Table  {
	private String linkUrl = "someURL";
	private TableColumn tc_CompanyName;
	private String opportunityID;
	private String selectedCompanyID;

	private String type;        // possible values: "Company_List", "Partner_List"
	private String subType;     // possible values: "Reassign", null
	private boolean partnerOnly = false;
	private boolean reassign    = false;


	/* Step 1: Initialization */
	public void init() {
		if (!MyUtil.isValidChoice(type, new String[] {"Company_List", "Partner_List"})) {
			type = "Company_List";
			System.out.println("Error!!! Wrong type passed. CompanyTable");
		}
		
		if (type.equals("Partner_List")) {
			partnerOnly = true;
		}

		if (subType != null && subType.equals("Reassign")) {
			reassign = true;
		}

		setMultipleSelect(false);
		setModel(new CompanyTableModel());
        setSort("lastModified");
        setDesc(true);
	}

	public void setType(String string) {
		type = string;
	}

	public void setSubType(String string) {
		subType = string;
	}


	/* Step 2: Parameter passing (dynamic) */
	public void setLinkUrl(String url) {
		linkUrl = url;
	}

	public String getOpportunityID() {
		return (opportunityID);
	}

	public void setOpportunityID(String string) {
		opportunityID = string;
	}

	public String getSelectedCompanyID() {
		return (selectedCompanyID);
	}


	/* Step 3: Table display and processing */
	public class CompanyTableModel extends TableModel {

		public CompanyTableModel() {
//			Application application  = Application.getInstance();
//			CompanyModule comModule = (CompanyModule) application.getModule(CompanyModule.class);

			tc_CompanyName = new TableColumn("companyName", Application.getInstance().getMessage("sfa.label.company","Company"));
			tc_CompanyName.setUrl(linkUrl);
			tc_CompanyName.setUrlParam("companyID");
			addColumn(tc_CompanyName);

			TableColumn tc_CompanyType = new TableColumn("companyType", Application.getInstance().getMessage("sfa.label.type","Type"));
			//tc_CompanyType.setFormat(new TableStringFormat(Company.getCompanyType_Map()));
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
            addAction(new TableAction("delete",Application.getInstance().getMessage("sfa.label.delete","Delete"),Application.getInstance().getMessage("sfa.label.suretodelete","Delete company will also delete all its contacts.Are you sure you want to continue?")));

/*
			addAction(new TableAction("select_bottom", Application.getInstance().getMessage("sfa.label.select","Select")));
*/

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

/*
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			Application application = Application.getInstance();
			OpportunityModule opModule = (OpportunityModule) application.getModule(OpportunityModule.class);

			if ("select_bottom".equals(action) && selectedKeys.length == 1) {
				if (partnerOnly) {
					// tie partner to opportunity (or reassign partner)
					Opportunity opp = opModule.getOpportunity(opportunityID);
					opp.setPartnerCompanyID(selectedKeys[0]);
					opModule.updateOpportunity(opp);
				} else if (!partnerOnly && reassign) {
					// reassign another company to opportunity
					Opportunity opp = opModule.getOpportunity(opportunityID);
					opp.setCompanyID(selectedKeys[0]);
					opModule.updateOpportunity(opp);
				}

				selectedCompanyID = selectedKeys[0];
				return (new Forward("selectCompany"));
			}

			return null;
		}
*/
        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if("delete".equals(action)){
                String companyId = null;
                for (int i = 0; i < selectedKeys.length; i++) {
                    companyId = selectedKeys[i];

                }
                if(companyId!=null&&companyId.trim().length()>0){
                    CompanyModule cm = (CompanyModule) Application.getInstance().getModule(CompanyModule.class);
                    int closedSales = cm.countClosedSales(companyId);
                    if(closedSales>0)
                        return new Forward("closedsales");
                    int opportunites = cm.countOpportunites(companyId);
                    if(opportunites>0)
                        return new Forward("opportunities");

                    cm.deleteCompany(companyId);
                    return new Forward("deleted");
                }
            }

            return super.processAction(evt, action, selectedKeys);    //To change body of overridden methods use File | Settings | File Templates.
        }
	}



	public void onRequest(Event evt) {
		// Set the link
		tc_CompanyName.setUrl(linkUrl);
	}

/*
	public String getDefaultTemplate() {
		return "sfa/SalesTable";
	}
*/

}
