package com.tms.fms.setup.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tms.fms.setup.model.ProgramObject;
import com.tms.fms.setup.model.SetupModule;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TableStringFormat;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class ProgramManagementTable extends Table{
	public static String FORWARD_LISTING_ADD="program.listing.add";
	public static String FORWARD_LISTING_INACTIVE="program.listing.inactive";
	public static String FORWARD_LISTING_DELETE="program.listing.delete";
	
	public void init(){
		super.init();
        setPageSize(20);
        setModel(new ProgramTableModel());
        setWidth("100%");
	}

	public class ProgramTableModel extends TableModel{
		public ProgramTableModel(){
			TableColumn tcName=new TableColumn("programName",Application.getInstance().getMessage("fms.setup.table.programName", "Program Name"));
			tcName.setUrlParam("programId");
			TableColumn tcDescription=new TableColumn("description", Application.getInstance().getMessage("fms.setup.table.description", "Description"));;
			TableColumn tcProducer = new TableColumn("producer", Application.getInstance().getMessage("fms.setup.table.producer", "Producer"), false);
			TableColumn tcPFECode=new TableColumn("pfeCode", Application.getInstance().getMessage("fms.setup.table.pfeCode", "PFE Code"));
			TableColumn tcProductionDate=new TableColumn("startProductionDate", Application.getInstance().getMessage("fms.setup.table.productionDate", "Production Date"));
			tcProductionDate.setFormat(new TableDateFormat("dd-MM-yyyy"));
			TableColumn tcDepartment=new TableColumn("departmentName", Application.getInstance().getMessage("fms.setup.table.department","Client/Department"));
			TableColumn tcStatus=new TableColumn("status",Application.getInstance().getMessage("fms.setup.table.status", "Status"));
			Map mapIsActive = new HashMap(); mapIsActive.put("1", Application.getInstance().getMessage("fms.tran.setup.active", "Active")); mapIsActive.put("0", Application.getInstance().getMessage("fms.tran.setup.inactive", "Inactive"));
			tcStatus.setFormat(new TableStringFormat(mapIsActive));
			
			/*tcStatus.setFormat(new TableFormat() {
                public String format(Object value) {
                	String status = value.toString();
                	if(status.equals("1")){
                		status="active";
                	}else{
                		status="Inactive";
                	}
                	return status;
                }
			});*/
			
			addColumn(tcName);
			addColumn(tcDescription);
			addColumn(tcProducer);
			addColumn(tcPFECode);
			addColumn(tcProductionDate);
			addColumn(tcDepartment);
			addColumn(tcStatus);
			
			TableFilter filterSearch = new TableFilter("filterSearch");
			TextField searchText = new TextField("searchText");
			searchText.setSize("20");
			filterSearch.setWidget(searchText);
			addFilter(filterSearch);
			
			TableFilter filterStatus = new TableFilter("filterStatus");
			SelectBox statusIsActive = new SelectBox("statusIsActive");
			statusIsActive.setOptions("-1=" + Application.getInstance().getMessage("fms.setup.status", "Status"));
			statusIsActive.setOptions("1=" + Application.getInstance().getMessage("fms.setup.active", "Active"));
			statusIsActive.setOptions("0=" + Application.getInstance().getMessage("fms.setup.inactive", "Inactive"));
			filterStatus.setWidget(statusIsActive);
			addFilter(filterStatus);
			
			addAction(new TableAction("add", Application.getInstance().getMessage("fms.setup.addProgram", "Add New Program")));
		    addAction(new TableAction("setActive", Application.getInstance().getMessage("fms.setup.setActive", "Set Active")));
		    addAction(new TableAction("setInactive", Application.getInstance().getMessage("fms.setup.setInactive", "Set Inactive")));
		    addAction(new TableAction("delete", Application.getInstance().getMessage("fms.setup.deleteProgram", "Delete Program")));
		}
		
		public String getSearchText() {
			return (String)getFilterValue("filterSearch");
		}
		
		public String getStatusIsActive() {
			String returnValue = "-1";
			List lstStatusIsActive = (List)getFilterValue("filterStatus");
			if (lstStatusIsActive.size() > 0) {returnValue = (String)lstStatusIsActive.get(0);}
			return returnValue;
		}
		
		public Collection getTableRows() {
			SetupModule mod = (SetupModule)Application.getInstance().getModule(SetupModule.class);
			Collection list=new ArrayList();
			
			try {
				list=mod.selectProgram(getSearchText(), getStatusIsActive() , getSort(), isDesc(), getStart(), getRows());
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString(), e);
			}
			
			return list;
		}
		
		public int getTotalRowCount() {
			SetupModule mod = (SetupModule)Application.getInstance().getModule(SetupModule.class);
			int result=0;
			
			try {
				result=mod.selectProgramCount(getSearchText(), getStatusIsActive());
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString(), e);
			}
			return result;
		}
		
		public String getTableRowKey() {
		    return "programId";
		}
		
		
		 public Forward processAction(Event event, String action, String[] selectedKeys) {
			
			SetupModule mod = (SetupModule)Application.getInstance().getModule(SetupModule.class);
			
			if ("add".equals(action)) {
				return new Forward(FORWARD_LISTING_ADD);
			}else if ("setActive".equals(action)) {
		    	for (int i=0; i<selectedKeys.length; i++) {
					ProgramObject p = mod.getProgram(selectedKeys[i]);
					if("0".equals(p.getStatus())){
						p.setStatus("1");
						mod.updateProgram(p);
					}
			    }
		    }else if ("setInactive".equals(action)) {
		    	     for(int i=0; i<selectedKeys.length; i++){
		    	    	 ProgramObject p = mod.getProgram(selectedKeys[i]);
		    	    	 if("1".equals(p.getStatus())){
		    	    		 p.setStatus("0");
		    	    		 mod.updateProgram(p);
		    	    	 } 
		    	     }
		    }else if ("delete".equals(action)){
		    		for (int i=0; i<selectedKeys.length; i++){
		 			 mod.deleteProgram(selectedKeys[i]);
		 			 return new Forward(FORWARD_LISTING_DELETE);
		 		}	
		    }
			return super.processAction(event, action, selectedKeys);
		}	 	
	}		
}

