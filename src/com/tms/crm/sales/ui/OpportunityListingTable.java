package com.tms.crm.sales.ui;

import kacang.stdui.TableFilter;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableColumn;
import kacang.Application;

import java.util.Collection;

import com.tms.crm.sales.model.OpportunityModule;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jun 24, 2004
 * Time: 3:49:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class OpportunityListingTable extends OpportunitySummaryTable{
    public OpportunityListingTable() {
        super();
    }

    /* Step 1: Initialization */
    public void init() {
        super.init();    //To change body of overridden methods use File | Settings | File Templates.
        setAccountManagerID(null);
        setModel(new OpportunityListingTableModel());
    }


    public class OpportunityListingTableModel extends OpportunitySummaryTable.OpportunityTableModel{
        public OpportunityListingTableModel() {
            super();
/*
            TableColumn lastModified =new TableColumn("modifiedDate",Application.getInstance().getMessage("sfa.label.lastModified","Last Modified"),true);
            lastModified.setFormat(new TableDateFormat(DisplayConstants.DATE_FORMAT));
            addColumn(lastModified);
*/
            setShowPageSize(true);
            addFilter(new TableFilter("search"));
        }

        public Collection getTableRows() {
            Application application  = Application.getInstance();
            OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);
            //Object[] qParams = getQueryParams();
            return module.listOpportunities((String)getFilterValue("search"),null,getAccountManagerID(),null,1,false,false,getFromDate(),getToDate(),getSort(),isDesc(),getStart(),getRows());
//            return module.listOpportunities((String)qParams[0], (String)qParams[1], (String)qParams[2], (String)qParams[3], ((Integer)qParams[4]).intValue(), (Date)qParams[5], (Date)qParams[6], getSort(),isDesc(), getStart(), getRows());
        }

        public int getTotalRowCount() {
            Application application  = Application.getInstance();
            OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);
            //           Object[] qParams = getQueryParams();
            return module.countOpportunities((String)getFilterValue("search"),null,getAccountManagerID(),null,1,false,false,getFromDate(),getToDate());
        }

    }


}
