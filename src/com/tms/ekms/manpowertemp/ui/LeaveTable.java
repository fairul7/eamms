package com.tms.ekms.manpowertemp.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.crm.sales.misc.DateUtil;
import com.tms.ekms.manpowertemp.model.ManpowerModule;
import com.tms.fms.department.model.FMSUnit;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.util.PagingUtil;
import com.tms.fms.util.WidgetUtil;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class LeaveTable extends Table{
	private int year;
	protected SelectBox selectYear;
	protected SelectBox unit;
	protected DatePopupField fromDateFilter; 
	protected DatePopupField toDateFilter; 
	
	public LeaveTable(){
	}
	
	public LeaveTable(String s){
		super(s);
	}
	
	public static String FORWARD_LISTING_ADD="leave.listing.add";
	public static String FORWARD_LISTING_DELETE="leave.listing.delete";
	
	public void init(){
		super.init();
		setPageSize(20);
        setModel(new LeaveTableModel());
        setWidth("100%");
        setSort("dateFrom");
	}
	
	public class LeaveTableFilterForm extends Form {
		public LeaveTableFilterForm() {
		}
		
		public LeaveTableFilterForm(String name) {
			super(name);
		}
		
		public void init() {
			Application application = Application.getInstance();
			SetupModule module = (SetupModule) application.getModule(SetupModule.class);

			// current year
		    Calendar cal = Calendar.getInstance();
		    year = cal.get(Calendar.YEAR);
			
			// filter: from date
			addChild(new Label("fromDate1", application.getMessage("fms.facility.label.fromDate")));
			fromDateFilter = new DatePopupField("fromDateFilter");
			fromDateFilter.setOptional(true);
			fromDateFilter.setDate(DateUtil.getToday());
			addChild(fromDateFilter);
			
			// filter: to date
			addChild(new Label("toDate1", application.getMessage("fms.facility.label.toDate")));
			toDateFilter = new DatePopupField("toDateFilter");
			toDateFilter.setOptional(true);
			toDateFilter.setDate(DateUtil.getDate(year, Calendar.DECEMBER, 31));
			addChild(toDateFilter);
			
			// year selection
			Map yearMap = new SequencedHashMap();
		    yearMap.put((String) Integer.toString(year - 2),
		    		(String) Integer.toString(year - 2));
		    yearMap.put((String) Integer.toString(year - 1),
		    		(String) Integer.toString(year - 1));
		    yearMap.put((String) Integer.toString(year),
		    		(String) Integer.toString(year));
		    yearMap.put((String) Integer.toString(year+1),
		    		(String) Integer.toString(year+1));
		    yearMap.put((String) Integer.toString(year+2),
		    		(String) Integer.toString(year+2));
		    
			selectYear = new SelectBox("selectYear");
			selectYear.setAlign(Form.ALIGN_RIGHT);
		    selectYear.setOptionMap(yearMap);
		    selectYear.setSelectedOption(Integer.toString(year));
		    addChild(selectYear);
		    
		    // unit selection
		    unit = new SelectBox("unit");
	        try {
		    	Collection lstUnit = module.getUnits(application.getCurrentUser().getId());
				unit.addOption("-1", "Unit Filter");
			    if (lstUnit.size() > 0) {
			    	for (Iterator i=lstUnit.iterator(); i.hasNext();) {
			        	FMSUnit o = (FMSUnit)i.next();
			        	unit.addOption(o.getId(),o.getName());
			        }
			    }
			} catch (Exception e) {
			    Log.getLog(getClass()).error(e.toString(), e);
			}
	        addChild(unit);
		}
		
		public Forward onSubmit(Event evt) {
			Forward fwd = super.onSubmit(evt);
			
			String year = WidgetUtil.getSbValue(selectYear);
            if (year == null) {
            	year = "2009";
            }
            
            Date fromDate = fromDateFilter.getDate();
			Date toDate = toDateFilter.getDate();
			
			// check date filter
			try {
				int yearNum = Integer.parseInt(year);
				if (fromDate != null || toDate != null) {
					if (fromDate == null || DateUtil.getYear(fromDate) != yearNum) {
						fromDateFilter.setInvalid(true);
						setInvalid(true);
					}
					
					if (toDate == null || DateUtil.getYear(toDate) != yearNum) {
						toDateFilter.setInvalid(true);
						setInvalid(true);
					}
				}
			} catch (NumberFormatException e) {
			}
			
			return fwd;
		}
	}
	
	public class LeaveTableModel extends TableModel{
		int count;
		public LeaveTableModel(){
			TableColumn nameTab = new TableColumn("manpowerName","Name");
			nameTab.setUrlParam("id");			
			
			TableColumn tcLeaveType= new TableColumn("leaveType", "Leave Type");
			TableColumn tcDateFrom= new TableColumn("dateFrom","Date From");
			tcDateFrom.setFormat(new TableDateFormat("dd-MM-yyyy"));
			TableColumn tcDateTo= new TableColumn("dateTo", "Date To");
			tcDateTo.setFormat(new TableDateFormat("dd-MM-yyyy"));
			
			addColumn(nameTab);
			addColumn(tcLeaveType);
			addColumn(tcDateFrom);
			addColumn(tcDateTo);
			
			// filter form
			Form myFilterForm = new LeaveTableFilterForm("myFilterForm");
			myFilterForm.init();
			TableFilter myFilter = new TableFilter("myFilter");
			myFilter.setWidget(myFilterForm);
			addFilter(myFilter);
			
		    addAction(new TableAction("add",Application.getInstance().getMessage("fms.manpower.table.addBtn","Add")));
		    addAction(new TableAction("delete",Application.getInstance().getMessage("fms.manpower.table.deleteBtn","Delete")));
		}
		
		public Collection getTableRows(){
			Application application = Application.getInstance();
			
            String year = WidgetUtil.getSbValue(selectYear);
            if (year == null) {
            	year = "2009";
            }
            
            // return empty result
            if (fromDateFilter.isInvalid() || toDateFilter.isInvalid()) {
            	count = 0;
            	return new ArrayList();
            }
            
            Date fromDate = fromDateFilter.getDate();
			Date toDate = toDateFilter.getDate();
			
			// advance 1 day
			if (toDate != null) {
				toDate = DateUtil.dateAdd(toDate, Calendar.DATE, 1);
			}
            
            ManpowerModule mod=(ManpowerModule)Application.getInstance().getModule(ManpowerModule.class);
            Collection list = new ArrayList();
            try{
            	list	= mod.selectLeave(year, fromDate, toDate, application.getCurrentUser().getId(), getUnitFilter(), getSort(), isDesc(), 0, -1);
            	count 	= list.size();
            	list 	= PagingUtil.getPagedCollection(list, getStart(), getRows());
            	
            }catch (DaoException e){
            	Log.getLog(getClass()).info(e.toString(), e);
            }
            return list;
		}
		
		public int getTotalRowCount(){
			return count;
		}
		
		public String getTableRowKey() {
			return "manpowerLeaveId";
		}
		
		public String getUnitFilter(){
			String unitFilter = "";
			unitFilter = WidgetUtil.getSbValue(unit);
			
			if (unitFilter != null && "-1".equals(unitFilter)) unitFilter = null;
			
			return unitFilter;
		}
		
		public Forward processAction(Event event, String action, String[] selectedKeys) {
			ManpowerModule mod = (ManpowerModule)Application.getInstance().getModule(ManpowerModule.class);
			if ("add".equals(action)) {
				return new Forward(FORWARD_LISTING_ADD);
			}else if ("delete".equals(action)){
		    	for (int i=0; i<selectedKeys.length; i++){
		 			mod.deleteManpowerLeaveById(selectedKeys[i]);
		    		//Log.getLog(getClass()).info("ManpowerLeaveId : " + selectedKeys[i]);
		 		}	
		    	return new Forward(FORWARD_LISTING_DELETE);	
		    }
			return super.processAction(event, action, selectedKeys);
		}
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public SelectBox getSelectYear() {
		return selectYear;
	}

	public void setSelectYear(SelectBox selectYear) {
		this.selectYear = selectYear;
	}
}