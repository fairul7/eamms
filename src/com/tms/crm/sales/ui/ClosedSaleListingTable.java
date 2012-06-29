package com.tms.crm.sales.ui;

import kacang.Application;
import kacang.stdui.*;

import java.util.Collection;

import com.tms.crm.sales.model.OpportunityModule;
import com.tms.crm.sales.model.Opportunity;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jun 25, 2004
 * Time: 11:34:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClosedSaleListingTable extends OpportunityListingTable {

    /* Step 1: Initialization */
    public void init() {
        //super.init();    //To change body of overridden methods use File | Settings | File Templates.
        setAccountManagerID(null);
        setNumbering(true);
        setModel(new ClosedSaleListingTableModel());
    }

    public class ClosedSaleListingTableModel extends TableModel{
        public ClosedSaleListingTableModel() {
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
            Application application  = Application.getInstance();
            OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);
            //Object[] qParams = getQueryParams();
            return module.listOpportunities((String)getFilterValue("search"),null,getAccountManagerID(),null,Opportunity.STATUS_CLOSE.intValue(),false,false,getFromDate(),getToDate(),getSort(),isDesc(),getStart(),getRows());
        }

        public int getTotalRowCount() {
            Application application  = Application.getInstance();
            OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);
            //           Object[] qParams = getQueryParams();
            return module.countOpportunities((String)getFilterValue("search"),null,getAccountManagerID(),null,Opportunity.STATUS_CLOSE.intValue(),false,false,getFromDate(),getToDate());
        }

    }


}
