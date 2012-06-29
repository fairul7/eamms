package com.tms.ekms.manpowertemp.ui;

import java.util.ArrayList;
import java.util.Collection;
import com.tms.ekms.manpowertemp.model.ManpowerModule;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;

public class LeaveTypeTable extends Table {
	public static String FORWARD_LISTING_ADD="leavetype.listing.add";
	public static String FORWARD_LISTING_DELETE="leavetype.listing.delete";
	
	public void init(){
		super.init();
		setPageSize(20);
        setModel(new LeaveTypeTableModel());
        setWidth("100%");
	}
	
	public class LeaveTypeTableModel extends TableModel{
		public LeaveTypeTableModel(){
			TableColumn tcType =new TableColumn("leaveType", Application.getInstance().getMessage("fms.manpower.table.leaveType", "Leave Type"));
			tcType.setUrlParam("id");
			TableColumn tcTypeName = new TableColumn("leaveTypeName", Application.getInstance().getMessage("fms.manpower.table.leaveTypeName", "Leave Type Name"));
			TableColumn tcDescription = new TableColumn("description", Application.getInstance().getMessage("fms.manpower.table.leaveDescription", "Description"));
			
			addColumn(tcType);
			addColumn(tcTypeName);
			addColumn(tcDescription);
			
			TableFilter filterSearch = new TableFilter("filterSearch");
			TextField searchText = new TextField("searchText");
			searchText.setSize("20");
			filterSearch.setWidget(searchText);
			addFilter(filterSearch);
			
			addAction(new TableAction("add",Application.getInstance().getMessage("fms.manpower.table.addBtn", "Add")));
            addAction(new TableAction("delete",Application.getInstance().getMessage("fms.manpower.table.deleteBtn", "Delete")));
		}
		
		public String getSearchText(){
			return (String)getFilterValue("filterSearch");
		}
		
		public Collection getTableRows(){
			ManpowerModule mod = (ManpowerModule)Application.getInstance().getModule(ManpowerModule.class);
			Collection col = new ArrayList();
			try{
			col =mod.selectLeaveType(getSearchText(), getSort(), isDesc(), getStart(), getRows());
			}catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return col;
		}
		
		public int getTotalRowCount(){
			ManpowerModule mod=(ManpowerModule)Application.getInstance().getModule(ManpowerModule.class);
			int result=0;
			try{
			result = mod.selectLeaveTypeCount(getSearchText(), getSort(), isDesc(), getStart(), getRows());
			}catch (DaoException e) {
				// TODO Auto-generated catch block
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
		 			 mod.deleteLeaveType(selectedKeys[i]);
		 			 return new Forward(FORWARD_LISTING_DELETE);
		 		}	
		    }
			return super.processAction(event, action, selectedKeys);
		}	 	
	}

}
