package com.tms.ekms.manpowertemp.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.ekms.manpowertemp.model.ManpowerModule;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.Form;
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

public class HolidayTable extends Table {
	private int year;
	
	protected SelectBox yearselect;
	
	public HolidayTable(){
	}
	
	public HolidayTable(String s){
		super(s);
	}
	
	public static String FORWARD_LISTING_ADD="holiday.listing.add";
	public static String FORWARD_LISTING_DELETE="holiday.listing.delete";
	
	public void init(){
		super.init();
        setPageSize(20);
        setModel(new HolidayTableModel());
        setWidth("100%");
	}
	
	public class HolidayTableModel extends TableModel{
		public HolidayTableModel(){
			TableColumn tcDateFrom=new TableColumn("dateFrom",Application.getInstance().getMessage("fms.manpower.table.dateFrom", "Date"));
			tcDateFrom.setFormat(new TableDateFormat("dd-MM-yyyy"));
			tcDateFrom.setUrlParam("id");
			//TableColumn tcDateTo=new TableColumn("dateTo","Date To");
			//tcDateTo.setFormat(new TableDateFormat("dd-MM-yyyy"));
			TableColumn tcHoliday=new TableColumn("holiday",Application.getInstance().getMessage("fms.manpower.table.holiday", "Holiday Desc"));
			
			addColumn(tcDateFrom);
			//addColumn(tcDateTo);
			addColumn(tcHoliday);
			
			yearselect = new SelectBox("yearSelect");
		    yearselect.setAlign(Form.ALIGN_RIGHT);

		    Map yearMap = new SequencedHashMap();
		    Calendar cal = Calendar.getInstance();
		    year = cal.get(Calendar.YEAR);
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
		    yearselect.setOptionMap(yearMap);
		    yearselect.setSelectedOption(Integer.toString(year));
		    TableFilter tfRead = new TableFilter("year");

		    tfRead.setWidget(yearselect);
		    addFilter(tfRead);
		       
		    addAction(new TableAction("add", Application.getInstance().getMessage("fms.manpower.table.addBtn","Add")));
			addAction(new TableAction("delete",Application.getInstance().getMessage("fms.manpower.table.deleteBtn","Delete")));
		}
		
		/*public String getSearchYear() {
			return (String)getFilterValue("tfRead");
		} */
		
		public Collection getTableRows(){
			Log log = Log.getLog(this.getClass());
            List readStatus = (List) getFilterValue("year");
            	String year= "";
            		if (readStatus.size() > 0)
            			year = (String) readStatus.get(0);
            		if (year != null && (year.trim().equalsIgnoreCase("null") || year.trim().equals("")))
            			year = "";
                          
			ManpowerModule mod = (ManpowerModule)Application.getInstance().getModule(ManpowerModule.class);
			Collection lst=new ArrayList();
			try{
				lst=mod.selectHoliday(year, getSort(), isDesc(), getStart(), getRows());
			}catch (DaoException e){
				e.printStackTrace();
			}
			return lst;
		}
		
		public int getTotalRowCount(){
			Log log = Log.getLog(this.getClass());
            List readStatus = (List) getFilterValue("year");
            	String year= "";
            	if (readStatus.size() > 0)
            		year = (String) readStatus.get(0);
            	if (year != null && (year.trim().equalsIgnoreCase("null") || year.trim().equals("")))
            		year = "";
            
			ManpowerModule mod = (ManpowerModule)Application.getInstance().getModule(ManpowerModule.class);
			int result=0;
			try{
				result=mod.selectHolidayCount(year, getSort(), isDesc(), getStart(), getRows());
			}catch (DaoException e){
				e.printStackTrace();
			}
			return result;
		}
		
		public String getTableRowKey() {
		    return "id";
		}
		
		public Forward processAction(Event event, String action, String[] selectedKeys) {
			ManpowerModule mod = (ManpowerModule)Application.getInstance().getModule(ManpowerModule.class);
			
			if ("add".equals(action)) {
				return new Forward(FORWARD_LISTING_ADD);
			}else if ("delete".equals(action)){
		    		for (int i=0; i<selectedKeys.length; i++){
		 			 mod.deleteHolidayLeave(selectedKeys[i]);
		 			 return new Forward(FORWARD_LISTING_DELETE);
		 		}	
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

	public SelectBox getYearselect() {
		return yearselect;
	}

	public void setYearselect(SelectBox yearselect) {
		this.yearselect = yearselect;
	}

}
