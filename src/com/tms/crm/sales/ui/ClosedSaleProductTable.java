/*
 * Created on Jan 19, 2004
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
public class ClosedSaleProductTable extends Table {
	private String linkUrl;
	private TableColumn tc_ProductID;
	private String opportunityID;
	private TableColumn category;
	
	
	/* Step 1: Initialization */
	public void init() {
		setModel(new OpportunityProductTableModel());
	}
	
	
	/* Step 2: Parameter passing (dynamic) */
	public void setLinkUrl(String url) {
		linkUrl = url;
		tc_ProductID.setUrl(linkUrl); // Set the link
	}
	
	public String getOpportunityID() {
		return (opportunityID);
	}
	
	public void setOpportunityID(String string) {
		opportunityID = string;
	}
	
	
	/* Step 3: Table display and processing */
	public class OpportunityProductTableModel extends TableModel {
	
		public OpportunityProductTableModel() {
			tc_ProductID = new TableColumn("productID", Application.getInstance().getMessage("sfa.label.product","Product"));
/*
			tc_ProductID.setUrl(linkUrl);
*/
			addColumn(tc_ProductID);
			
			category = new TableColumn("categoryName", Application.getInstance().getMessage("sfa.label.category","Product Category"));
			addColumn(category);
			
			TableColumn tc_OpValue = new TableColumn("opValue", Application.getInstance().getMessage("sfa.label.value","Value"));
			tc_OpValue.setFormat(new TableDecimalFormat("#,##0.00"));
			addColumn(tc_OpValue);
			
			TableColumn tc_OpDesc = new TableColumn("opDesc", Application.getInstance().getMessage("sfa.label.description","Description"));
			addColumn(tc_OpDesc);
			
			addAction(new TableAction("delete", Application.getInstance().getMessage("sfa.label.delete","Delete")));
		}
		
		public Collection getTableRows() {
			Application application         = Application.getInstance();
			OpportunityProductModule module = (OpportunityProductModule) application.getModule(OpportunityProductModule.class);
			return module.listOpportunityProduct(opportunityID, getSort(), isDesc(), getStart(), getRows());
		}
		
		public Collection getAllTableRows() {
			Application application         = Application.getInstance();
			OpportunityProductModule module = (OpportunityProductModule) application.getModule(OpportunityProductModule.class);
			return module.listOpportunityProduct(opportunityID, getSort(), isDesc(), getStart(), -1);
		}
		
		public int getTotalRowCount() {
			Application application         = Application.getInstance();
			OpportunityProductModule module = (OpportunityProductModule) application.getModule(OpportunityProductModule.class);
			return module.countOpportunityProduct(opportunityID);
		}
		
		public String getTableRowKey() {
			return "productSeq"; // with opportunityID
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			Application application  = Application.getInstance();
			OpportunityProductModule module = (OpportunityProductModule) application.getModule(OpportunityProductModule.class);
			
			if ("delete".equals(action)) {
				for (int i=selectedKeys.length-1; i>=0; i--) {
					// debug: System.out.println("selectedKeys[" + i + "] " + selectedKeys[i]);
					module.deleteOpportunityProduct(opportunityID, Integer.parseInt(selectedKeys[i]));
				}
			}
			return null;
		}
	}
	
	public void onRequest(Event evt) {
		Application application = Application.getInstance();
		ProductModule proModule = (ProductModule) application.getModule(ProductModule.class);
		tc_ProductID.setFormat(new TableLimitStringFormat(proModule.getProductMap(true), 25));
	}
	
/*	public String getDefaultTemplate() {
		return "sfa/OpportunityProductTable";
	}*/
}
