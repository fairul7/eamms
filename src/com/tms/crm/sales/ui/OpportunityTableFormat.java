package com.tms.crm.sales.ui;

import com.tms.crm.sales.model.Opportunity;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jun 8, 2004
 * Time: 4:59:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class OpportunityTableFormat extends TableLimitStringFormat{
    private OpportunityTable table;
    public OpportunityTableFormat(Map valueMap, int maxLength) {
        super(valueMap, maxLength);
    }

    public OpportunityTableFormat(int maxLength,OpportunityTable table) {
        super(maxLength);
        this.table = table;
    }

    public String format(Object value) {

        String oppName = super.format(value);    //To change body of overridden methods use File | Settings | File Templates.
        return "<a href=\"#\" onclick=\"window.open('"+  table.getLinkUrl() +"?opportunityID="+ ((Opportunity)table.getCurrentRow()).getOpportunityID() + "','','resizable=yes,width=750,height=420,scrollbars=yes')\">"+oppName + "</a>";
    }


}
