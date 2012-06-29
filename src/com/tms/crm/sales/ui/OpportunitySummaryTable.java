package com.tms.crm.sales.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.ui.WidgetManager;

import java.util.*;

import com.tms.crm.sales.misc.DateUtil;
import com.tms.crm.sales.model.Opportunity;
import com.tms.crm.sales.model.OpportunityModule;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jun 9, 2004
 * Time: 6:37:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class OpportunitySummaryTable extends Table{


    private String accountManagerID;
    private Date   fromDate;
    private Date   toDate;
	public static final int green = 0;
    public  static final int red = 21;
    public  static final int orange = 14;



    /* Step 1: Initialization */
    public void init() {
        String userId = getWidgetManager().getUser().getId();
        Date today = DateUtil.getToday();
        fromDate   = DateUtil.getDate(DateUtil.getYear(today), DateUtil.getMonth(today), 1);
        toDate     = DateUtil.dateAdd(DateUtil.dateAdd(fromDate, Calendar.MONTH, 3), Calendar.DATE, -1);
        accountManagerID = userId;
        // if user is from External Sales, cannot view other people's opportunities
        setSort("opportunityEnd");
        setNumbering(true);
        setModel(new OpportunityTableModel());
    }



    /* Step 3: Table display and processing */
    public class OpportunityTableModel extends TableModel {

        public OpportunityTableModel() {

			// Getting financial settings : currency symbol
		   	OpportunityModule om = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
			Opportunity csign = om.getFinancilSetting();
			String sign = csign.getCurrencySymbol();


            TableColumn status = new TableColumn("modifiedDate","&#149;",true);
            status.setFormat(new TableFormat(){
                public String format(Object o) {
                    Date modifiedDate = (Date)o;
                    Date today = new Date();
                    long diff = today.getTime() - modifiedDate.getTime();
                    int day = (int)(diff/(1000*60*60*24));
                    if(day>=red){
                        return "<img src=\""+ getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH) +  "/ekms/sfa/images/03.gif\" />";
                    }else if(day>=orange){
                        return "<img src=\""+ getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH) +  "/ekms/sfa/images/02.gif\" />";
                    }
                    return /*"<img src=\""+ getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH) +  "/ekms/sfa/images/01.gif\" />";*/ null;
                }
            } );
            addColumn(status);


            TableColumn tc_OpportunityName = new TableColumn("opportunityName", Application.getInstance().getMessage("sfa.label.opportunity","Opportunity"));
            tc_OpportunityName.setFormat(new TableLimitStringFormat(25));
			tc_OpportunityName.setUrlParam("opportunityID");
            addColumn(tc_OpportunityName);

            TableColumn tc_CompanyID = new TableColumn("companyName", Application.getInstance().getMessage("sfa.label.company","Company"));
            tc_CompanyID.setUrlParam("companyID");
            tc_CompanyID.setFormat(new TableLimitStringFormat(25));
            addColumn(tc_CompanyID);

            TableColumn tc_OpportunityStatus = new TableColumn("opportunityStatus", Application.getInstance().getMessage("sfa.label.status","Status"));
            tc_OpportunityStatus.setFormat(new TableStringFormat(Opportunity.getOpportunityStatus_More_Map()));
            addColumn(tc_OpportunityStatus);

            TableColumn tc_OpportunityStage = new TableColumn("opportunityStage", Application.getInstance().getMessage("sfa.label.stage","Stage"));
            tc_OpportunityStage.setFormat(new TableStringFormat(Opportunity.getOpportunityStage_Less_Map()));
            addColumn(tc_OpportunityStage);

            TableColumn tc_OpportunityValue = new TableColumn("opportunityValue", Application.getInstance().getMessage("sfa.label.value","Value")+" "+"("+sign+")");
            tc_OpportunityValue.setFormat(new TableDecimalFormat("#,##0.00"));
            addColumn(tc_OpportunityValue);

            TableColumn tc_AdjustedValue = new TableColumn("adjustedValue", Application.getInstance().getMessage("sfa.label.adjValue","Adj. Value")+" "+"("+sign+")");
            tc_AdjustedValue.setFormat(new TableDecimalFormat("#,##0.00"));
            tc_AdjustedValue.setSortable(false);
            addColumn(tc_AdjustedValue);

            HashMap hm = new HashMap();
            hm.put("0", Application.getInstance().getMessage("sfa.label.direct","Direct"));
            hm.put("1", Application.getInstance().getMessage("sfa.label.channel","Channel"));
            TableColumn tc_HasPartner = new TableColumn("hasPartner", Application.getInstance().getMessage("sfa.label.type","Type"));
            tc_HasPartner.setFormat(new TableStringFormat(hm));
            addColumn(tc_HasPartner);

            TableColumn tc_OpportunityEnd = new TableColumn("opportunityEnd", Application.getInstance().getMessage("sfa.label.closingDate","Closing Date"));
            tc_OpportunityEnd.setFormat(new TableDateFormat(DisplayConstants.DATE_FORMAT));
            addColumn(tc_OpportunityEnd);

        /*    TableColumn lastModified =new TableColumn("modifiedDate",Application.getInstance().getMessage("sfa.label.lastModified","Last Modified"),true);
            lastModified.setFormat(new TableDateFormat(DisplayConstants.DATE_FORMAT));
            addColumn(lastModified);
*/
        }

        Object[] getQueryParams() {
/*
            Object[] qParams = new Object[7];

            String stageID = "";
            if (showStageFilter) {
                SelectBox sel_stageID = (SelectBox) MyUtil.getTableFilterWidget(this, "tf_StageFilter");
                stageID = MyUtil.getSingleValue_SelectBox(sel_stageID);
            }
            if (showAccountManagerFilter) {
                SelectBox sel_AccountManagerID = (SelectBox) MyUtil.getTableFilterWidget(this, "tf_AccountManagerID");
                accountManagerID = MyUtil.getSingleValue_SelectBox(sel_AccountManagerID);
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
*/       return null;
        }

        public Collection getTableRows() {
            Application application  = Application.getInstance();
            OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);
            //Object[] qParams = getQueryParams();
            return module.listOpportunities(null,null,accountManagerID,null,-100,false,true,fromDate,toDate,getSort(),isDesc(),0,-1);
//            return module.listOpportunities((String)qParams[0], (String)qParams[1], (String)qParams[2], (String)qParams[3], ((Integer)qParams[4]).intValue(), (Date)qParams[5], (Date)qParams[6], getSort(),isDesc(), getStart(), getRows());
        }

        public int getTotalRowCount() {
            Application application  = Application.getInstance();
            OpportunityModule module = (OpportunityModule) application.getModule(OpportunityModule.class);
 //           Object[] qParams = getQueryParams();
            return module.countOpportunities(null,null,accountManagerID,null,-100,false,true,fromDate,toDate);
        }

        public String getTableRowKey() {
            return "opportunityID";
        }

    }


 /*   public String getDefaultTemplate() {
        return "sfa/SalesTable";
    }*/


    public String getAccountManagerID() {
        return accountManagerID;
    }

    public void setAccountManagerID(String accountManagerID) {
        this.accountManagerID = accountManagerID;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

}
