package com.tms.sam.po.report.ui;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import kacang.Application;
import kacang.stdui.DatePopupField;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

public class FinancialReportListing extends Table{
	private Date fromDate;
	private Date toDate;
	
	public FinancialReportListing(){
		
	}
	
	public FinancialReportListing(String name){
		super(name);
	}
	
	public void init(){
		super.init();
        setPageSize(50);
        setModel(new FinancialReportListingModel());
        setWidth("100%");
	}
	
	public class FinancialReportListingModel extends TableModel{
		private DatePopupField dfFromDate;
		private DatePopupField dfToDate;
		
		public FinancialReportListingModel() {
			Application app = Application.getInstance();
			
			TableColumn pCode = new TableColumn("purchaseCode", app.getMessage("purchaseRequest.label.purchaseCode"));
			pCode.setUrlParam("reportLink");
			addColumn(pCode);
			
			TableColumn dept = new TableColumn("dept", app.getMessage("po.label.department"));
			addColumn(dept);
			
			TableColumn budget = new TableColumn("budget", app.getMessage("report.label.budget"));
			
			addColumn(budget);
			
			TableColumn amount = new TableColumn("amount", app.getMessage("po.label.amount"));
			addColumn(amount);
			
			dfFromDate = new DatePopupField("fromDate");
			dfFromDate.setOptional(true);
			TableFilter fromDateFilter = new TableFilter("fromDateFilter");
			fromDateFilter.setWidget(dfFromDate);
			addFilter(fromDateFilter);
			
			dfToDate = new DatePopupField("toDate");
			dfToDate.setOptional(true);
			TableFilter toDateFilter = new TableFilter("toDateFilter");
			toDateFilter.setWidget(dfToDate);
			addFilter(toDateFilter);
			
			Calendar now = Calendar.getInstance();
			dfToDate.setDate(now.getTime());
			toDate = now.getTime();
			
			now.add(Calendar.MONTH, -1);
			dfFromDate.setDate(now.getTime());
			fromDate = now.getTime();
			
			addAction(new TableAction("print", "Print"));
		}
		
		@Override
		public Collection getTableRows() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public int getTotalRowCount() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			// TODO Auto-generated method stub
			return super.processAction(evt, action, selectedKeys);
		}
	}
	
	@Override
	public String getDefaultTemplate() {
		return "po/financialReport";
	}
	
}
