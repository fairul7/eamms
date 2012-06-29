/*
 * Created on Dec 5, 2003
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
import kacang.ui.Widget;
import com.tms.crm.sales.misc.*;
import com.tms.crm.sales.model.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class OpportunityTable extends SalesTable {
	private String linkUrl, linkUrl2;
	private TableColumn tc_OpportunityName;
	private TableColumn tc_CompanyID;
	private String companyID;
	private String accountManagerID;
	private Date   fromDate;
	private Date   toDate;
	private String selectedOpportunityID;
	
	private String type; // possible values: "List_All", "List_All_WithInitial", "Company_Filter", "Company_Filter2", "Current_Own"
	private boolean showAccountManagerFilter = false;
	private boolean acManagerSetInitial      = false;
	private boolean showKeywordFilter        = true;
	private boolean showSelectButton         = false;
	private boolean showStageFilter          = false;
	private boolean filterByDate             = false;
	private boolean firstTime                = false;


	/* Step 1: Initialization */
	public void initTable() {
		if (!MyUtil.isValidChoice(type, new String[] {"List_All", "List_All_WithInitial", "Company_Filter", "Company_Filter2", "Current_Own"})) {
			type = "List_All";
			System.out.println("Error!!! Wrong type passed. OpportunityTable");
		}
		
		String userId = getWidgetManager().getUser().getId();
		if (type.equals("List_All")) {
			showAccountManagerFilter = true;
			showStageFilter          = true;
		} else if (type.equals("List_All_WithInitial")) {
			showAccountManagerFilter = true;
			acManagerSetInitial      = true;
			showStageFilter          = true;
		} else if (type.equals("Company_Filter")) {
			showSelectButton = true;
		} else if (type.equals("Current_Own")) {
			filterByDate = true;
			Date today = DateUtil.getToday(); 
			fromDate   = DateUtil.getDate(DateUtil.getYear(today), DateUtil.getMonth(today), 1);
			toDate     = DateUtil.dateAdd(DateUtil.dateAdd(fromDate, Calendar.MONTH, 3), Calendar.DATE, -1);
			
			accountManagerID = userId;
			showKeywordFilter = false;
		}
		
		// if user is from External Sales, cannot view other people's opportunities
		if (type.equals("List_All_WithInitial")) {
			boolean isExternalSalesPerson = AccessUtil.isExternalSalesPerson(userId);
			if (isExternalSalesPerson) {
				showAccountManagerFilter = false;
				acManagerSetInitial      = false;
				accountManagerID = userId;
			}
		}
		
		setMultipleSelect(false);
		setModel(new OpportunityTableModel());
	}
	
	public void setType(String string) {
		type = string;
	}
	
	
	/* Step 2: Parameter passing (dynamic) */
	public void setLinkUrl(String url) {
		linkUrl = url;
		tc_OpportunityName.setUrl(linkUrl); // Set the link
	}
	
	public void setLinkUrl2(String url) {
		linkUrl2 = url;
		tc_CompanyID.setUrl(linkUrl2); // Set the link
	}
	
	public String getCompanyID() {
		return (companyID);
	}
	
	public void setCompanyID(String string) {
		// for type = "Company_Filter" & "Company_Filter2"
		companyID = string;
	}
	
	public void setFirstTime(String string) {
		// for type = "Company_Filter"
		if (string != null && string.equals("1")) {
			firstTime = true;
		} else {
			firstTime = false;
		}
	}
	
	public String getSelectedOpportunityID() {
		return (selectedOpportunityID);
	}
	
	
	/* Step 3: Table display and processing */
	public class OpportunityTableModel extends TableModel {
	
		public OpportunityTableModel() {
			Application application = Application.getInstance();

			// Getting financial setting : currency symbol
			OpportunityModule om = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
			Opportunity csign = om.getFinancilSetting();
			String sign = csign.getCurrencySymbol();

			tc_OpportunityName = new TableColumn("opportunityName", Application.getInstance().getMessage("sfa.label.opportunity","Opportunity"));


/*
			tc_OpportunityName.setFormat(new OpportunityTableFormat(25,OpportunityTable.this));
*/



//			tc_OpportunityName.setUrl(linkUrl);
			tc_OpportunityName.setUrlParam("opportunityID");

			addColumn(tc_OpportunityName);
			
			if (!type.equals("Company_Filter") && !type.equals("Company_Filter2")) {
				tc_CompanyID = new TableColumn("companyName", Application.getInstance().getMessage("sfa.label.company","Company"));
				if (type.equals("Current_Own")) {
					tc_CompanyID.setUrl(linkUrl2);
					tc_CompanyID.setUrlParam("companyID");
				}
				addColumn(tc_CompanyID);
			}
			
			TableColumn tc_OpportunityStatus = new TableColumn("opportunityStatus", Application.getInstance().getMessage("sfa.label.status","Status"));
			tc_OpportunityStatus.setFormat(new TableStringFormat(Opportunity.getOpportunityStatus_More_Map()));
			addColumn(tc_OpportunityStatus);
			
			TableColumn tc_OpportunityStage = new TableColumn("opportunityStage", Application.getInstance().getMessage("sfa.label.stage","Stage"));
			tc_OpportunityStage.setFormat(new TableStringFormat(Opportunity.getOpportunityStage_Less_Map()));
			addColumn(tc_OpportunityStage);
			
			TableColumn tc_OpportunityValue = new TableColumn("opportunityValue", Application.getInstance().getMessage("sfa.label.value","Value")+" "+"("+sign+")");
			tc_OpportunityValue.setFormat(new TableDecimalFormat("#,##0.00"));
			addColumn(tc_OpportunityValue);
			
			if (type.equals("Current_Own")) {
				TableColumn tc_AdjustedValue = new TableColumn("adjustedValue", Application.getInstance().getMessage("sfa.label.adjValue","Adj. Value")+" "+"("+sign+")");
				tc_AdjustedValue.setFormat(new TableDecimalFormat("#,##0.00"));
				tc_AdjustedValue.setSortable(false);
				addColumn(tc_AdjustedValue);
			}
			
			if (type.equals("List_All") || type.equals("List_All_NoInitial") || type.equals("Current_Own")) {
				HashMap hm = new HashMap();
				hm.put("0", Application.getInstance().getMessage("sfa.label.direct","Direct"));
				hm.put("1", Application.getInstance().getMessage("sfa.label.channel","Channel"));
				TableColumn tc_HasPartner = new TableColumn("hasPartner", Application.getInstance().getMessage("sfa.label.type","Type"));
				tc_HasPartner.setFormat(new TableStringFormat(hm));
				addColumn(tc_HasPartner);
			}
			
			TableColumn tc_OpportunityEnd = new TableColumn("opportunityEnd", Application.getInstance().getMessage("sfa.label.closingDate","Closing Date"));
			tc_OpportunityEnd.setFormat(new TableDateFormat(DisplayConstants.DATE_FORMAT));
			addColumn(tc_OpportunityEnd);
			
			if (!type.equals("Current_Own")) {
				TableColumn tc_Users = new TableColumn("accountManagers", Application.getInstance().getMessage("sfa.label.user(s)","User(s)"));
				tc_Users.setSortable(false);
				addColumn(tc_Users);
			}
			
			// Stage filter
			if (showStageFilter) {
				TableFilter tf_StageLabel = new TableFilter("labelStage");
				tf_StageLabel.setWidget(new Label("lbFilterStage", "<span class='filterLabelStyle'>&nbsp;"+Application.getInstance().getMessage("sfa.label.stage","Stage")+":</span>")); 
				addFilter(tf_StageLabel);
				
				SelectBox sel_StageID = new SelectBox("sel_StageID");
				sel_StageID.addOption("", "-"+ Application.getInstance().getMessage("sfa.label.all","All")+" -");
				Integer[] stageCode = Opportunity.getOpportunityStage_Code();
				short[]  stagePerc = Opportunity.getOpportunityStage_Percent();
				for (int i=0; i<stageCode.length; i++) {
					sel_StageID.addOption(stageCode[i].toString(), stagePerc[i] + "%");
				}
				
				TableFilter tf_StageFilter = new TableFilter("tf_StageFilter");
				tf_StageFilter.setWidget(sel_StageID); 
				addFilter(tf_StageFilter);
			}
			
			// Populate A/C Manager pull-down
			if (showAccountManagerFilter) {
				AccountManagerModule amModule = (AccountManagerModule) application.getModule(AccountManagerModule.class); 
				Collection amCol = amModule.getAccountManagers();
				
				TableFilter tf_AccountManagerID = new TableFilter("tf_AccountManagerID");
				SelectBox sel_AccountManagerID = new SelectBox("sel_AccountManagerID");
				sel_AccountManagerID.addOption("", "-"+Application.getInstance().getMessage("sfa.label.all","All")+" -");
				
				Iterator iterator = amCol.iterator();
				while (iterator.hasNext()) {
					AccountManager accountManager = (AccountManager) iterator.next();
					sel_AccountManagerID.addOption(accountManager.getId(), accountManager.getFullName());
				}
				
				TableFilter tf_LabelAC = new TableFilter("labelAccountManager");
				tf_LabelAC.setWidget(new Label("lbFilterAC", "<span class='filterLabelStyle'>&nbsp;"+Application.getInstance().getMessage("sfa.label.a/CManager","A/C Manager")+":</span>"));
				addFilter(tf_LabelAC);
				tf_AccountManagerID.setWidget(sel_AccountManagerID);
				addFilter(tf_AccountManagerID);
			}
			
			// Select button
			if (showSelectButton) {
				addAction(new TableAction("select_bottom", Application.getInstance().getMessage("sfa.label.select","Select")));
			}
			
			// Keyword Filter
			if (showKeywordFilter) {
				TableFilter tf_LabelKW = new TableFilter("labelKeyword");
				tf_LabelKW.setWidget(new Label("lbFilterSearch", "<span class='filterLabelStyle'>&nbsp;"+Application.getInstance().getMessage("sfa.label.search","Search")+":</span>")); 
				addFilter(tf_LabelKW);
				
				TableFilter tf_Keyword = new TableFilter("keyword");
				TextField keywordField = new TextField("keywordField");
				keywordField.setSize("12");
				tf_Keyword.setWidget(keywordField); 
				addFilter(tf_Keyword);
			}
		}
		
		Object[] getQueryParams() {
			Object[] qParams = new Object[7];
			
			String stageID = "";
			if (showStageFilter) {
				SelectBox sel_stageID = (SelectBox) MyUtil.getTableFilterWidget(this, "tf_StageFilter");
				stageID = MyUtil.getSingleValue_SelectBox(sel_stageID);
			}
			if (showAccountManagerFilter) {
				SelectBox sel_AccountManagerID = (SelectBox) MyUtil.getTableFilterWidget(this, "tf_AccountManagerID");
				accountManagerID = MyUtil.getSingleValue_SelectBox(sel_AccountManagerID);
			}else if(AccessUtil.isExternalSalesPerson(getWidgetManager().getUser().getId())){
                accountManagerID = getWidgetManager().getUser().getId();
            }
			String keyword = "";
			if (showKeywordFilter) {
				keyword = (String) getFilterValue("keyword");
			}
			Integer statusFilter = new Integer(-100);
			if (type.equals("Company_Filter2")) {
				statusFilter = new Integer(-1000);
			}
			
			qParams[0] = keyword;
			qParams[1] = companyID;
			qParams[2] = accountManagerID;
			qParams[3] = stageID;
			qParams[4] = statusFilter;
			qParams[5] = null;
			qParams[6] = null;
			
			if (filterByDate) {
				qParams[5] = fromDate;
				qParams[6] = toDate;
			}
			
			return qParams;
		}
		
		public Collection getTableRows() {
			Application application  = Application.getInstance();
			OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);
			
			String  sort = getSort();
			boolean desc = isDesc();
			
			// firstTime sort
			if (type.equals("Company_Filter") && firstTime) {
				sort = "creationDateTime";
				desc = true;
				firstTime = false;
			}

			Object[] qParams = getQueryParams();
			return module.listOpportunities((String)qParams[0], (String)qParams[1], (String)qParams[2], (String)qParams[3], ((Integer)qParams[4]).intValue(), false,true,(Date)qParams[5], (Date)qParams[6], sort, desc, getStart(), getRows());
		}
		
		public int getTotalRowCount() {
			Application application  = Application.getInstance();
			OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);
			
			Object[] qParams = getQueryParams();
			return module.countOpportunities((String)qParams[0], (String)qParams[1], (String)qParams[2], (String)qParams[3], ((Integer)qParams[4]).intValue(),false,true, (Date)qParams[5], (Date)qParams[6]);
		}
		
		public String getTableRowKey() {
			return "opportunityID";
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			//Application application  = Application.getInstance();
			//OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);
			
			if ("select_bottom".equals(action)) {
				if (selectedKeys.length == 1) {
					selectedOpportunityID = selectedKeys[0];
					return (new Forward("selectOpportunity"));
				}
			}
			return null;
		}
	}
	
	public void onRequest(Event evt) {

		initTable();

		if (type.equals("Company_Filter") && firstTime) {
			// Set page size
			setPageSize(20);
			
			// Do some hacking to refresh the pageSizeSelectBox
			Widget filterForm = getChild("filterForm");
			SelectBox pageSizeSelectBox = (SelectBox) filterForm.getChild("pageSizeSelectBox");
			pageSizeSelectBox.setSelectedOptions(new String[] { "20" });
		}
		
		// Reset filters: A/C Manager
		if (showAccountManagerFilter) {
			SelectBox sel_AccountManagerID = (SelectBox) MyUtil.getTableFilterWidget(getModel(), "tf_AccountManagerID");
			String userId = getWidgetManager().getUser().getId();
			if (acManagerSetInitial) {
				sel_AccountManagerID.setSelectedOptions(new String[] {userId});
			} else {
				sel_AccountManagerID.setSelectedOptions(new String[] {""});
			}
		}
		
		// Reset filters: Keyword
		if (showKeywordFilter) {
			TextField keywordField = (TextField) MyUtil.getTableFilterWidget(getModel(), "keyword");
			keywordField.setValue("");
		}
	}
	
	public String getDefaultTemplate() {
		return "sfa/SalesTable";
	}

    public String getLinkUrl() {
        return linkUrl;
    }
}



