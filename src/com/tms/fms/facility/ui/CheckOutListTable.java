package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.DatePopupField;
import kacang.stdui.Label;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableBooleanFormat;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.crm.sales.misc.DateUtil;
import com.tms.fms.facility.model.FacilityDao;
import com.tms.fms.facility.model.FacilityModule;
import com.tms.fms.facility.model.SetupDao;
import com.tms.fms.facility.model.SetupModule;

public class CheckOutListTable extends Table {
	DatePopupField fromDateFilter=new DatePopupField(); 
	DatePopupField toDateFilter=new DatePopupField(); 
	public CheckOutListTable() {
	}

	public CheckOutListTable(String s) {
		super(s);
	}

	public void init() {
		super.init();
		setPageSize(20);
		setModel(new CheckOutListTableModel());
		setWidth("100%");
	}

	public void onRequest(Event event) {
		setModel(new CheckOutListTableModel());
	}

	class CheckOutListTableModel extends TableModel {
		CheckOutListTableModel() {
			removeChildren();
			Application app = Application.getInstance();
			
			// table columns
			TableColumn checkout_date = new TableColumn("checkout_date", app.getMessage("fms.facility.form.date"));
			checkout_date.setFormat(new TableDateFormat(SetupModule.DATE_TIME_FORMAT));
			checkout_date.setUrlParam("groupId");
			addColumn(checkout_date);
			
			TableColumn checkout_by = new TableColumn("checkout_by", app.getMessage("fms.facility.form.requestor"));
			addColumn(checkout_by);
			
			TableColumn location = new TableColumn("location", app.getMessage("fms.facility.table.location"));
			addColumn(location);

			TableColumn purpose = new TableColumn("purpose", app.getMessage("fms.facility.form.purpose"));
			addColumn(purpose);

			
			TableColumn noOfCheckedOut = new TableColumn("noOfCheckedOut", app.getMessage("fms.facility.form.totalCheckedOut"));
			addColumn(noOfCheckedOut);

			TableColumn noOfCheckedIn = new TableColumn("noOfCheckedIn", app.getMessage("fms.facility.form.totalCheckedIn"));
			addColumn(noOfCheckedIn);

			
			TableFilter tfSearchBy = new TableFilter("searchBy");
			TextField tfSearchText = new TextField("tfSearchText");
			tfSearchText.setSize("20");
			tfSearchBy.setWidget(tfSearchText);
			addFilter(tfSearchBy);
			
			TableFilter label1=new TableFilter("fromDateLabel");
			label1.setWidget(new Label("fromDate1",app.getMessage("fms.facility.label.fromDate")));
			addFilter(label1);
			
			TableFilter tfFromDate=new TableFilter("fromDateFilter");
			
			fromDateFilter.setOptional(true);
			tfFromDate.setWidget(fromDateFilter);
			addFilter(tfFromDate);
			
			TableFilter label2=new TableFilter("toDateLabel");
			label2.setWidget(new Label("toDate1",app.getMessage("fms.facility.label.toDate")));
			addFilter(label2);
			
			TableFilter tfEndDate=new TableFilter("toDateFilter");
			
			toDateFilter.setOptional(true);
			tfEndDate.setWidget(toDateFilter);
			addFilter(tfEndDate);

		}

		public Collection getTableRows() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			Date fromDate=(Date)fromDateFilter.getDate();
			Date toDate=null;
			if(toDateFilter.getDate()!=null){
				toDate=DateUtil.dateAdd(toDateFilter.getDate(), Calendar.DATE, 1);
			}
			FacilityDao dao = (FacilityDao) Application.getInstance().getModule(FacilityModule.class).getDao();
			try {
				return dao.selectCheckOutList(searchBy,fromDate,toDate, getSort(), isDesc(), getStart(), getRows());
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return new ArrayList();
			}
		}

		public int getTotalRowCount() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			Date fromDate=(Date)fromDateFilter.getDate();
			Date toDate=null;
			if(toDateFilter.getDate()!=null){
				toDate=DateUtil.dateAdd(toDateFilter.getDate(), Calendar.DATE, 1);
			}
			FacilityDao dao = (FacilityDao) Application.getInstance().getModule(FacilityModule.class).getDao();
			try {
				return dao.selectCheckOutListCount(searchBy,fromDate,toDate);
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return 0;
			}
		}

		public String getTableRowKey() {
				return "groupId";
		}

		/*public Forward processAction(Event event, String action,
				String[] selectedKeys) {
			SetupModule module = (SetupModule) Application.getInstance().getModule(SetupModule.class);
			if ("add".equals(action))
				return new Forward("ADD");
			else if ("delete".equals(action)) {
				try {
					for (int i = 0; i < selectedKeys.length; i++){
						module.deleteWorkingProfile(selectedKeys[i]);
					}
					
				} catch (Exception e) {
					Log.getLog(getClass()).error(e.toString());
					return new Forward("NOTDELETED");
				}
			}
			return super.processAction(event, action, selectedKeys);
			
		}*/
	}

}
