package com.tms.sam.po.ui;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.sam.po.model.DepartmentModule;

public class DepartmentListing extends Table{
	public void init(){
		setWidth("100%");
		setModel(new DepartmentListingModel());

	}
	public class DepartmentListingModel extends TableModel{
		public DepartmentListingModel(){
			 Application app = Application.getInstance();
	        	
	         TableColumn idCol = new TableColumn("deptCode", app.getMessage("department.label.code","Department Code"));
	         idCol.setUrlParam("deptID");
	         addColumn(idCol);
	            
	         
	         addColumn(new TableColumn("deptName", app.getMessage("po.label.department", "Department")));
	         addColumn(new TableColumn("username", app.getMessage("department.label.hod","Head of Deparment")));
	            
	         addFilter(new TableFilter("name"));
	           
	         SelectBox searchCol = new SelectBox("searchCol");
	         searchCol.setMultiple(false);
	         Map searchColMap = new SequencedHashMap();
	         searchColMap.put("deptCode", app.getMessage("po.label.department", "Department"));
	         searchColMap.put("deptName", app.getMessage("department.label.hod","Head of Deparment"));
	    
	         searchCol.setOptionMap(searchColMap);
	            
	         TableFilter searchColFilter = new TableFilter("searchColFilter");
	         searchColFilter.setWidget(searchCol);
	         addFilter(searchColFilter);
	         
	         addAction(new TableAction("delete", "Delete", "Confirm to delete?"));
	           
		}
		
		public Collection getTableRows() {
		   String name = (String) getFilterValue("name");
			
           SelectBox searchCol = (SelectBox) getFilter("searchColFilter").getWidget();
           List cols = (List) searchCol.getValue();
           String searchColValue = "";
           if (cols.size() > 0) {
               searchColValue = (String) cols.get(0);
           }
           
			Application app = Application.getInstance();
			DepartmentModule module = (DepartmentModule) app.getModule(DepartmentModule.class);
			
			return module.getDepartmentListing(name, searchColValue, getSort(), isDesc(), getStart(), getRows());
		}
		
		public int getTotalRowCount() {
			Application app = Application.getInstance();
           String name = (String) getFilterValue("name");
           SelectBox searchCol = (SelectBox) getFilter("searchColFilter").getWidget();
           List cols = (List) searchCol.getValue();
           String searchColValue = "";
           if (cols.size() > 0) {
               searchColValue = (String) cols.get(0);
           }
          
           DepartmentModule module = (DepartmentModule) app.getModule(DepartmentModule.class);
           
           return module.countListing(name, searchColValue);
		}
		
		public String getTableRowKey() {
		    return "deptID";
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
           Application app = Application.getInstance();
           DepartmentModule module = (DepartmentModule) app.getModule(DepartmentModule.class);
         
           if ("delete".equals(action)) {
               for (int i=0; i<selectedKeys.length; i++) {
                 module.deleteDept(selectedKeys[i]);
               }
           }
           
           return null;
		}
	}
	
		
}
