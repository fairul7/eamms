package com.tms.crm.sales.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.util.Log;

import java.util.Collection;

import com.tms.crm.sales.model.LeadModule;
import com.tms.crm.sales.model.LeadException;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jul 22, 2004
 * Time: 5:14:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class LeadTable extends Table {
    private String userId;

    public void init() {
        super.init();    //To change body of overridden methods use File | Settings | File Templates.
        setWidth("100%");
        setModel(new LeadTableModel());
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public class LeadTableModel extends TableModel{
        public LeadTableModel() {
            TableColumn companyColumn =new TableColumn("companyName","Company");
            companyColumn.setUrlParam("id");
            addColumn(companyColumn);


            addColumn(new TableColumn("tel","Tel"));
            addColumn(new TableColumn("contactName","Contact Person"));
            TableColumn remarkColumn = new TableColumn("remarks","Remarks");
            remarkColumn.setFormat(new TableLimitStringFormat(25));
            addColumn(remarkColumn);
            
            TableColumn creationDateColumn = new TableColumn("creationDate","Date Created");
            creationDateColumn.setFormat(new TableDateFormat(DisplayConstants.DATE_FORMAT));
            addColumn(creationDateColumn);

            addFilter(new TableFilter("search"));
        }


        public Collection getTableRows() {
            LeadModule lm = (LeadModule) Application.getInstance().getModule(LeadModule.class);
            try {
                return lm.listLeads((String)getFilterValue("search"),userId,getSort(),isDesc(),getStart(),getRows());
            } catch (LeadException e) {
                Log.getLog(getClass()).error(e+e.getMessage(),e);  //To change body of catch statement use Options | File Templates.
            }
            return null;
        }

        public int getTotalRowCount() {
            LeadModule lm = (LeadModule) Application.getInstance().getModule(LeadModule.class);
            try {
                return lm.countLeads((String)getFilterValue("search"),userId);
            } catch (LeadException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);  //To change body of catch statement use Options | File Templates.
            }
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }


    }





}
