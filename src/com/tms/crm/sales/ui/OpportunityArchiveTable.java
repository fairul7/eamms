/*
 * Created on Feb 24, 2004
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
import com.tms.crm.sales.model.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class OpportunityArchiveTable extends Table {
	private String opportunityID;
	private TableColumn tc_ModifiedDate;
	private TableColumn tc_ModifiedBy;
	private String linkUrl;
	private int archiveLimit = -1;
	
	
	/* Step 1: Initialization */
	public void init() {
		setModel(new OpportunityArchiveTableModel());
	}
	
	
	/* Step 2: Parameter passing (dynamic) */
	public void setLinkUrl(String url) {
		linkUrl = url;
		tc_ModifiedDate.setUrl(linkUrl); // Set the link
	}
	
	public String getOpportunityID() {
		return (opportunityID);
	}
	
	public void setOpportunityID(String string) {
		opportunityID = string;
	}
	
	public void setArchiveLimit(String string) {
		try {
			archiveLimit = Integer.parseInt(string);
		} catch (Exception e) {
			e.printStackTrace();
			archiveLimit = -1;
		}
	}
	
	public String getArchiveLimit() {
		return (String.valueOf(archiveLimit));
	}
	
	public int getRecordCount() {
		TableModel tableModel = getModel();
		int recordCount = tableModel.getTotalRowCount();
		return (recordCount); 
	}
	
	
	/* Step 3: Table display and processing */
	public class OpportunityArchiveTableModel extends TableModel {
	
		public OpportunityArchiveTableModel() {
			tc_ModifiedDate = new TableColumn("modifiedDate", Application.getInstance().getMessage("sfa.label.date","Date"));
			tc_ModifiedDate.setFormat(new TableDateFormat(DisplayConstants.DATE_FORMAT));
			tc_ModifiedDate.setUrl(linkUrl);
			tc_ModifiedDate.setUrlParam("modifiedDate");
			addColumn(tc_ModifiedDate);
			
			tc_ModifiedBy = new TableColumn("modifiedBy", Application.getInstance().getMessage("sfa.label.modifiedBy","Modified By"));
			addColumn(tc_ModifiedBy);
			
			TableColumn tc_OpportunityStatus = new TableColumn("opportunityStatus", Application.getInstance().getMessage("sfa.label.status","Status"));
			tc_OpportunityStatus.setFormat(new TableStringFormat(Opportunity.getOpportunityStatus_Map()));
			addColumn(tc_OpportunityStatus);
			
			TableColumn tc_OpportunityStage = new TableColumn("opportunityStage", Application.getInstance().getMessage("sfa.label.stage","Stage"));
			tc_OpportunityStage.setFormat(new TableStringFormat(Opportunity.getOpportunityStage_Less_Map()));
			addColumn(tc_OpportunityStage);
			
			TableColumn tc_OpValue = new TableColumn("opportunityValue", Application.getInstance().getMessage("sfa.label.value","Value"));
			tc_OpValue.setFormat(new TableDecimalFormat("#,##0.00"));
			addColumn(tc_OpValue);
			
			TableColumn tc_OpportunityEnd = new TableColumn("opportunityEnd", Application.getInstance().getMessage("sfa.label.closingDate","Closing Date"));
			tc_OpportunityEnd.setFormat(new TableDateFormat(DisplayConstants.DATE_FORMAT));
			addColumn(tc_OpportunityEnd);
			
			TableColumn tc_LastRemarks = new TableColumn("opportunityLastRemarks", Application.getInstance().getMessage("sfa.label.remarks","Remarks"));
			addColumn(tc_LastRemarks);
            TableFilter label = new TableFilter("label");
            label.setWidget(new Label(" s"));
            addFilter(label);
		}
		
		public Collection getTableRows() {
			Application application         = Application.getInstance();
			OpportunityArchiveModule module = (OpportunityArchiveModule) application.getModule(OpportunityArchiveModule.class);
			return module.listArchive(opportunityID, getSort(), isDesc(), getStart(), getRows());
		}
		
		public Collection getAllTableRows() {
			Application application         = Application.getInstance();
			OpportunityArchiveModule module = (OpportunityArchiveModule) application.getModule(OpportunityArchiveModule.class);
			this.setDesc(true); // show latest date first
			return module.listArchive(opportunityID, getSort(), isDesc(), getStart(), archiveLimit);
		}
		
		public int getTotalRowCount() {
			Application application         = Application.getInstance();
			OpportunityArchiveModule module = (OpportunityArchiveModule) application.getModule(OpportunityArchiveModule.class);
			return module.countArchive(opportunityID);
		}
		
		public String getTableRowKey() {
			return "modifiedDate"; // with opportunityID
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			return null;
		}
	}
	
	public void onRequest(Event evt) {
		Application application       = Application.getInstance();
		AccountManagerModule amModule = (AccountManagerModule) application.getModule(AccountManagerModule.class);
		
		tc_ModifiedBy.setFormat(new TableStringFormat(amModule.getAccountManagersMap()));
	}
	
	/*public String getDefaultTemplate() {
		return "sfa/SalesTable";
	}*/
}
