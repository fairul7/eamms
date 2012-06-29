package com.tms.crm.sales.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.ui.Event;

import java.util.Date;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;

import com.tms.crm.sales.model.Opportunity;
import com.tms.crm.sales.model.AccountManagerModule;
import com.tms.crm.sales.model.AccountManager;
import com.tms.crm.sales.model.OpportunityModule;
import com.tms.crm.sales.misc.AccessUtil;
import com.tms.crm.sales.misc.MyUtil;


public class ClosedSaleCompleteTable extends Table{

    private String userId = null;

	public void onRequest(Event event) {
		initTable();
	}

	public void initTable() {
        super.init();    //To change body of overridden methods use File | Settings | File Templates.
        setSort("modifiedDate");
        setModel(new ClosedSaleCompleteTableModel());
    }

    public class ClosedSaleCompleteTableModel extends TableModel{
        private Radio viewAll;
        private Radio viewMonthly;
        private MonthFilter monthFilter;

        public ClosedSaleCompleteTableModel() {

			OpportunityModule om = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
			Opportunity csign = om.getFinancilSetting();
			String sign = csign.getCurrencySymbol();

            Application application = Application.getInstance();

            TableColumn tc_OpportunityName = new TableColumn("opportunityName", Application.getInstance().getMessage("sfa.label.opportunity","Opportunity"));
            tc_OpportunityName.setFormat(new TableLimitStringFormat(25));
            tc_OpportunityName.setUrlParam("opportunityID");

            addColumn(tc_OpportunityName);

            TableColumn tc_CompanyID = new TableColumn("companyName", Application.getInstance().getMessage("sfa.label.company","Company"));
            tc_CompanyID.setUrlParam("companyID");
            tc_CompanyID.setFormat(new TableLimitStringFormat(25));
            addColumn(tc_CompanyID);

            TableColumn tc_CloseReferenceNo = new TableColumn("closeReferenceNo", Application.getInstance().getMessage("sfa.label.referenceNo.","Reference No."));
            addColumn(tc_CloseReferenceNo);

            TableColumn tc_OpportunityValue = new TableColumn("opportunityValue", Application.getInstance().getMessage("sfa.label.value","Value")+" "+"("+sign+")");
            tc_OpportunityValue.setFormat(new TableDecimalFormat("#,##0.00"));
            addColumn(tc_OpportunityValue);

            HashMap hm = new HashMap();
            hm.put("0", Application.getInstance().getMessage("sfa.label.direct","Direct"));
            hm.put("1", Application.getInstance().getMessage("sfa.label.channel","Channel"));
            TableColumn tc_HasPartner = new TableColumn("hasPartner", Application.getInstance().getMessage("sfa.label.type","Type"));
            tc_HasPartner.setFormat(new TableStringFormat(hm));
            addColumn(tc_HasPartner);

            TableColumn tc_OpportunityEnd = new TableColumn("opportunityEnd", Application.getInstance().getMessage("sfa.label.closingDate","Closing Date"));
            tc_OpportunityEnd.setFormat(new TableDateFormat(DisplayConstants.DATE_FORMAT));
            addColumn(tc_OpportunityEnd);

            if (userId == null) {
                TableColumn tc_Users = new TableColumn("accountManagers", Application.getInstance().getMessage("sfa.label.a/CManager","A/C Manager"));
                tc_Users.setSortable(false);
                addColumn(tc_Users);
            }

            TableFilter viewAllFilter = new TableFilter("viewfilter");
            TableFilter viewMonthlyFilter = new TableFilter("monthlyfilter");

            viewAll = new Radio("vewall",Application.getInstance().getMessage("sfa.label.viewAll","View All"));
            viewAll.setChecked(true);
            viewAllFilter.setWidget(viewAll);

            viewMonthly = new Radio("viewmonthly",Application.getInstance().getMessage("sfa.label.viewMonthly","View Monthly"));
            viewMonthlyFilter.setWidget(viewMonthly);

            ButtonGroup viewGroup = new ButtonGroup("viewGroup");
            viewGroup.addButton(viewAll);
            viewGroup.addButton(viewMonthly);
            addFilter(viewAllFilter);
            addFilter(viewMonthlyFilter);

            monthFilter = new MonthFilter("monthfilterwidget");
            TableFilter monthlyFilter= new TableFilter("monthfilter");
            monthlyFilter.setWidget(monthFilter);
            addFilter(monthlyFilter);

            String tmpId;
            tmpId = getWidgetManager().getUser().getId();
            if(AccessUtil.isSalesManager(tmpId)&&(userId==null||userId.trim().length()==0)){
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

            TableFilter tf_LabelKW = new TableFilter("labelKeyword");
            tf_LabelKW.setWidget(new Label("lbFilterSearch", "<span class='filterLabelStyle'>&nbsp;"+Application.getInstance().getMessage("sfa.label.search","Search")+":</span>"));
            addFilter(tf_LabelKW);

            TableFilter tf_Keyword = new TableFilter("keyword");
            TextField keywordField = new TextField("keywordField");
            keywordField.setSize("12");
            tf_Keyword.setWidget(keywordField);
            addFilter(tf_Keyword);
        }

        public Collection getTableRows() {
            Application application  = Application.getInstance();
            OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);

            String  sort = getSort();
            boolean desc = isDesc();

            // firstTime sort
            Object[] qParams = getQueryParams();
            return module.listOpportunities((String)qParams[0], (String)qParams[1], (String)qParams[2], (String)qParams[3], ((Integer)qParams[4]).intValue(),false,false,  (Date)qParams[5], (Date)qParams[6],sort, desc, getStart(), getRows());
        }

        public int getTotalRowCount() {
            Application application  = Application.getInstance();
            OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);

            Object[] qParams = getQueryParams();
            return module.countOpportunities((String)qParams[0], (String)qParams[1], (String)qParams[2], (String)qParams[3], ((Integer)qParams[4]).intValue(),false,false, (Date)qParams[5], (Date)qParams[6]);
        }

        Object[] getQueryParams() {
            Object[] qParams = new Object[7];

            String stageID = "";
/*
            SelectBox sel_stageID = (SelectBox) MyUtil.getTableFilterWidget(this, "tf_StageFilter");
            stageID = MyUtil.getSingleValue_SelectBox(sel_stageID);
*/

            if(userId== null&&AccessUtil.isSalesManager(getWidgetManager().getUser().getId())) {
                SelectBox sel_AccountManagerID = (SelectBox) MyUtil.getTableFilterWidget(this, "tf_AccountManagerID");
                userId = MyUtil.getSingleValue_SelectBox(sel_AccountManagerID);
            }

            String keyword = "";
            keyword = (String) getFilterValue("keyword");

            Integer statusFilter = Opportunity.STATUS_CLOSE;
            //statusFilter = new Integer(-1000);

            qParams[0] = keyword;
            qParams[1] = null;
            if(getFilter("tf_AccountManagerID")==null)
                qParams[2] = userId;
            else
            {
                SelectBox sel_AccountManagerID = (SelectBox) MyUtil.getTableFilterWidget(this, "tf_AccountManagerID");
                qParams[2] = MyUtil.getSingleValue_SelectBox(sel_AccountManagerID);
            }

            qParams[3] = stageID;
            qParams[4] = statusFilter;
            qParams[5] = null;
            qParams[6] = null;

            if (viewMonthly.isChecked()) {
                qParams[5] = monthFilter.getFromDate();
                qParams[6] = monthFilter.getToDate();
            }

            return qParams;
        }

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
