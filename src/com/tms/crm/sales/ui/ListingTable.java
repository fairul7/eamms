/*
 * Created on Feb 17, 2004
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
public class ListingTable extends SalesTable {
	private String userID;
	private String groupID;
	private Date fromDate;
	private Date toDate;
	
	private String type; 		// possible values: "Opportunity_List", "ClosedSale_List"
	private String subType;		// possible values: "All", "Group", "User"
	private boolean closedSaleList = false;
	private boolean userOnlyList   = false;
	private boolean groupOnlyList  = false;
	
	
	/* Step 1: Initialization */
	public void init() {
		if (!MyUtil.isValidChoice(type, new String[] {"Opportunity_List", "ClosedSale_List"})) {
			type = "Opportunity_List";
			System.out.println("Error!!! Wrong type passed. ListingTable");
		}
		
		if (!MyUtil.isValidChoice(subType, new String[] {"All", "Group", "User"})) {
			type = "All";
			System.out.println("Error!!! Wrong subType passed. ListingTable");
		}
		
		if (type.equals("ClosedSale_List")) {
			closedSaleList = true;
		}
		
		if (subType.equals("User")) {
			userOnlyList = true;
		} else if (subType.equals("Group")) {
			groupOnlyList = true;
		}
		
		setModel(new ListingTableModel());
	}
	
	public void setType(String string) {
		type = string;
	}
	
	public void setSubType(String string) {
		subType = string;
	}
	
	
	/* Step 2: Parameter passing (dynamic) */
	public String getUserID() {
		return (userID);
	}
	
	public void setUserID(String string) {
		userID = string;
	}
	
	public String getGroupID() {
		return (groupID);
	}
	
	public void setGroupID(String string) {
		groupID = string;
	}
	
	public String getGroupName() {
		Application application = Application.getInstance();
		AccountManagerModule amModule = (AccountManagerModule) application.getModule(AccountManagerModule.class);
		String groupName = amModule.getGroup(groupID).getGroupName();
		return groupName;
	}
	
	public String getFromDate() {
		return (DateUtil.getDateString(fromDate));
	}
	
	public void setFromDate(String string) {
		fromDate = DateUtil.getDateFromDateString(string);
	}
	
	public String getToDate() {
		return (DateUtil.getDateString(toDate));
	}
	
	public void setToDate(String string) {
		toDate = DateUtil.getDateFromDateString(string);
	}
	
	
	/* Step 3: Table display and processing */
	public class ListingTableModel extends TableModel {
	
		public ListingTableModel() {
			Application application = Application.getInstance();
			CompanyModule comModule = (CompanyModule) application.getModule(CompanyModule.class);
			
			TableColumn tc_OpportunityName = new TableColumn("opportunityName", Application.getInstance().getMessage("sfa.label.opportunity","Opportunity"));
			tc_OpportunityName.setFormat(new TableLimitStringFormat(25));
			addColumn(tc_OpportunityName);
			
			TableColumn tc_CompanyID = new TableColumn("companyID", Application.getInstance().getMessage("sfa.label.company","Company"));
			tc_CompanyID.setFormat(new TableLimitStringFormat(comModule.getCompanyMap(), 25));
			addColumn(tc_CompanyID);
			
			if (closedSaleList) {
				TableColumn tc_CloseReferenceNo = new TableColumn("closeReferenceNo", Application.getInstance().getMessage("sfa.label.referenceNo.","Reference No."));
				addColumn(tc_CloseReferenceNo);
			} else {
				TableColumn tc_OpportunityStatus = new TableColumn("opportunityStatus", Application.getInstance().getMessage("sfa.label.status","Status"));
				tc_OpportunityStatus.setFormat(new TableStringFormat(Opportunity.getOpportunityStatus_Map()));
				addColumn(tc_OpportunityStatus);
				
				TableColumn tc_OpportunityStage = new TableColumn("opportunityStage", Application.getInstance().getMessage("sfa.label.stage","Stage"));
				tc_OpportunityStage.setFormat(new TableStringFormat(Opportunity.getOpportunityStage_Less_Map()));
				addColumn(tc_OpportunityStage);

                SelectBox statusList = new SelectBox("status");
                
            }
			
			if (userOnlyList) {
				TableColumn tc_DistributionPercentage = new TableColumn("distributionPercentage", "%");
				addColumn(tc_DistributionPercentage);
			}
			
			TableColumn tc_OpportunityValue = new TableColumn("opportunityValue", Application.getInstance().getMessage("sfa.label.value","Value"));
			tc_OpportunityValue.setFormat(new TableDecimalFormat("#,##0.00"));
			addColumn(tc_OpportunityValue);
			
			TableColumn tc_OpportunityEnd = new TableColumn("opportunityEnd", Application.getInstance().getMessage("sfa.label.closingDate","Closing Date"));
			tc_OpportunityEnd.setFormat(new TableDateFormat(DisplayConstants.DATE_FORMAT));
			addColumn(tc_OpportunityEnd);
		    setShowPageSize(true);
            addFilter(new TableFilter("search"));
        }
		
		public Collection getTableRows() {
			Application application = Application.getInstance();
			ListingModule module    = (ListingModule) application.getModule(ListingModule.class);
			
			if (userOnlyList) {
				return module.listUserOpportunities((String)getFilterValue("search"),closedSaleList, userID, fromDate, toDate, getSort(), isDesc(), getStart(), getRows());
			} else if (groupOnlyList) {
				return module.listGroupOpportunities((String)getFilterValue("search"),closedSaleList, groupID, fromDate, toDate, getSort(), isDesc(), getStart(), getRows());
			} else {
				return module.listAllOpportunities((String)getFilterValue("search"),closedSaleList, fromDate, toDate, getSort(), isDesc(), getStart(), getRows());
			}
		}
		
		public int getTotalRowCount() {
			Application application = Application.getInstance();
			ListingModule module    = (ListingModule) application.getModule(ListingModule.class);
			
			if (userOnlyList) {
				return module.countUserOpportunities(closedSaleList, userID, fromDate, toDate);
			} else if (groupOnlyList) {
				return module.countGroupOpportunities(closedSaleList, groupID, fromDate, toDate);
			} else {
				return module.countAllOpportunities(closedSaleList, fromDate, toDate);
			}
		}
		
		public String getTableRowKey() {
			return "opportunityID";
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			/*
			Application application = Application.getInstance();
			ListingModule module    = (ListingModule) application.getModule(ListingModule.class);
			String userId = getWidgetManager().getUser().getId();
			*/
			return null;
		}
	}
	
	public String getDefaultTemplate() {
		return "sfa/SalesTable";
	}
}
