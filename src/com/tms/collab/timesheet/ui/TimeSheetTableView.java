package com.tms.collab.timesheet.ui;

import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
//import kacang.ui.Event;
import kacang.Application;

import java.util.Date;
import java.util.Collection;
import java.util.Calendar;

import com.tms.collab.timesheet.model.TimeSheetModule;
import com.tms.crm.sales.misc.DateUtil;
import com.tms.hr.claim.model.ClaimFormIndex;
import com.tms.hr.claim.model.ClaimFormIndexModule;
import com.tms.hr.claim.model.ClaimFormItem;
import com.tms.hr.claim.model.ClaimFormItemModule;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: May 3, 2005
 * Time: 1:46:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetTableView extends Table {

    public void init() {
        //setWidth("100%");
        setModel(new TimeSheetTblViewModel());
    }

    public class TimeSheetTblViewModel extends TableModel {
        public TimeSheetTblViewModel() {
            TableDateFormat dateFormat = new TableDateFormat("dd MMM yyyy");
            TableColumn tcDate = new TableColumn("tsDate",Application.getInstance().getMessage("timesheet.label.date","Date"));
            tcDate.setFormat(dateFormat);
            tcDate.setUrl("timesheetEDForm.jsp");
            tcDate.setUrlParam("id");
            TableColumn tcCategory = new TableColumn("categoryName",Application.getInstance().getMessage("timesheet.label.category","Category"));
            TableColumn tcTask = new TableColumn("taskName",Application.getInstance().getMessage("timesheet.label.task","Task"));
            TableColumn tcDuration = new TableColumn("duration",Application.getInstance().getMessage("timesheet.label.duration","Duration"));
            TableColumn tcDescription = new TableColumn("description",Application.getInstance().getMessage("timesheet.label.description","Description"));
            TableColumn tcAdjustedDuration = new TableColumn("adjustedDuration",Application.getInstance().getMessage("timesheet.label.adjustedduration","Adjusted Duration"));
            TableColumn tcAdjustmentBy = new TableColumn("adjustmentBy",Application.getInstance().getMessage("timesheet.label.adjustmentby","Adjustment By"));
            TableColumn tcCreatedDate = new TableColumn("createdDateTime",Application.getInstance().getMessage("timesheet.label.createddate","Created Date"));
            tcCreatedDate.setFormat(dateFormat);

            addColumn(tcDate);
            addColumn(tcCategory);
            addColumn(tcTask);
            addColumn(tcDuration);
            addColumn(tcDescription);
            addColumn(tcAdjustedDuration);
            addColumn(tcAdjustmentBy);
            addColumn(tcCreatedDate);

            DateField startDate = new DatePopupField("startDate");
            startDate.setDate(Calendar.getInstance().getTime());
            DateField endDate = new DatePopupField("endDate");
            endDate.setDate(Calendar.getInstance().getTime());
            TableFilter startDateFilter = new TableFilter("startDateFilter");
            TableFilter endDateFilter = new TableFilter("endDateFilter");
            startDateFilter.setWidget(startDate);
            endDateFilter.setWidget(endDate);
            addFilter(startDateFilter);
            addFilter(endDateFilter);

            addAction(new TableAction("delete", "Delete", "Delete Selected Items?"));


        }

        public Collection getTableRows() {
            DateField dfStart = (DateField)getFilter("startDateFilter").getWidget();
            DateField dfEnd = (DateField)getFilter("endDateFilter").getWidget();
            Date dateStart = dfStart.getDate();
            Date dateEnd = dfEnd.getDate();

            Calendar startCal = Calendar.getInstance();
            Calendar endCal = Calendar.getInstance();
            if (dateStart!=null)  {
                startCal.setTime(dateStart);
            }
            startCal.set(Calendar.HOUR,0);
            startCal.set(Calendar.MINUTE,0);
            startCal.set(Calendar.SECOND,0);

            if (dateEnd!=null) {
                endCal.setTime(dateEnd);
            }
            endCal.set(Calendar.HOUR,23);
            endCal.set(Calendar.MINUTE,59);
            endCal.set(Calendar.SECOND,59);

            TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
            if (dateStart==null)
                return mod.getList(startCal.getTime(),endCal.getTime(),getWidgetManager().getUser().getId(),getSort(),isDesc(),getStart(), getRows());
            else
                return mod.getListByTimesheet(startCal.getTime(),endCal.getTime(),getWidgetManager().getUser().getId(),getSort(),isDesc(),getStart(), getRows());

        }

        public int getTotalRowCount() {
            DateField dfStart = (DateField)getFilter("startDateFilter").getWidget();
            DateField dfEnd = (DateField)getFilter("endDateFilter").getWidget();
            Date dateStart = dfStart.getDate();
            Date dateEnd = dfEnd.getDate();

            Calendar startCal = Calendar.getInstance();
            Calendar endCal = Calendar.getInstance();

            if (dateStart!=null)
                startCal.setTime(dateStart);
            startCal.set(Calendar.HOUR,0);
            startCal.set(Calendar.MINUTE,0);
            startCal.set(Calendar.SECOND,0);
            if (dateEnd!=null) {
                endCal.setTime(dateEnd);
            }
            endCal.set(Calendar.HOUR,23);
            endCal.set(Calendar.MINUTE,59);
            endCal.set(Calendar.SECOND,59);

            TimeSheetModule mod = (TimeSheetModule)Application.getInstance().getModule(TimeSheetModule.class);
            if (dateStart==null)
                return mod.getListCount(startCal.getTime(),endCal.getTime(),getWidgetManager().getUser().getId());
            else
                return mod.getListCountByTimesheet(startCal.getTime(),endCal.getTime(),getWidgetManager().getUser().getId());
        }

        public String getTableRowKey()
        { return "id"; }


        public Forward processAction(Event evt, String action, String[] selectedKeys)
        {



           if ("delete".equals(action))
           {

        	   if(selectedKeys.length <=0) return new Forward("empty");

              Application application = Application.getInstance();
              TimeSheetModule module = (TimeSheetModule) application.getModule(TimeSheetModule.class);
              for (int i=0; i<selectedKeys.length; i++)
              {
                        module.delete(selectedKeys[i]);
              }

              return new Forward("deleted");
           }


           return null;
        }

    }

	public void setStartDate(String start) {
		Date startDate = DateUtil.getDateFromDateString(start);
		if (startDate != null) {
			DateField dfStart = (DateField) getModel().getFilter("startDateFilter").getWidget();
			dfStart.setDate(startDate);
		}
	}

	public void setEndDate(String end) {
		Date endDate = DateUtil.getDateFromDateString(end);
		if (endDate != null) {
			DateField dfEnd = (DateField) getModel().getFilter("endDateFilter").getWidget();
			dfEnd.setDate(endDate);
		}
	}
}
