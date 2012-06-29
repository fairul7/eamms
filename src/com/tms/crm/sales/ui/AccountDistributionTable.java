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
public class AccountDistributionTable extends Table {
	private String opportunityID;
	private TableColumn tc_UserID;
	
	
	/* Step 1: Initialization */
	public void init() {
		setModel(new AccountDistributionTableModel());
	}
	
	
	/* Step 2: Parameter passing (dynamic) */
	public String getOpportunityID() {
		return (opportunityID);
	}
	
	public void setOpportunityID(String string) {
		opportunityID = string;
	}
	
	
	/* Step 3: Table display and processing */
	public class AccountDistributionTableModel extends TableModel {
	
		public AccountDistributionTableModel() {
			tc_UserID = new TableColumn("userID", Application.getInstance().getMessage("sfa.label.member","Member"));
			addColumn(tc_UserID);
			
			TableColumn tc_DistributionPercentage = new TableColumn("distributionPercentage", Application.getInstance().getMessage("sfa.label.percent","Percent"));
			addColumn(tc_DistributionPercentage);
		}
		
		public Collection getTableRows() {
			Application application          = Application.getInstance();
			AccountDistributionModule module = (AccountDistributionModule) application.getModule(AccountDistributionModule.class);
			return module.getAccountDistribution(opportunityID, getSort(), isDesc(), getStart(), getRows());
		}
		
		public Collection getAllTableRows() {
			Application application          = Application.getInstance();
			AccountDistributionModule module = (AccountDistributionModule) application.getModule(AccountDistributionModule.class);
			return module.getAccountDistribution(opportunityID, getSort(), isDesc(), getStart(), -1);
		}
		
		public int getTotalRowCount() {
			Application application          = Application.getInstance();
			AccountDistributionModule module = (AccountDistributionModule) application.getModule(AccountDistributionModule.class);
			return module.countAccountDistribution(opportunityID);
		}
		
		public String getTableRowKey() {
			return "distributionSequence"; // with opportunityID
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			return null;
		}
	}
	
	public void onRequest(Event evt) {
		Application application       = Application.getInstance();
		AccountManagerModule amModule = (AccountManagerModule) application.getModule(AccountManagerModule.class);
		
		tc_UserID.setFormat(new TableStringFormat(amModule.getAccountManagersMap()));
	}
	
	public String getDefaultTemplate() {
		return "sfa/SalesTable";
	}
}
