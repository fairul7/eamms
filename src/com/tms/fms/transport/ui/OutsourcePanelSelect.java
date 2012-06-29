package com.tms.fms.transport.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.fms.department.model.FMSDepartment;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.facility.model.*;
import com.tms.fms.transport.model.SetupObject;
import com.tms.fms.transport.model.TransportDao;
import com.tms.fms.transport.model.TransportModule;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.PopupSelectBox;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.PopupSelectBoxTable;
import kacang.stdui.PopupSelectBoxTableModel;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableStringFormat;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.util.Log;

public class OutsourcePanelSelect extends PopupSelectBox{

	public OutsourcePanelSelect(){}
	
	public OutsourcePanelSelect(String s){
		super(s);
	}
	
	protected Map generateOptionMap(String[] ids) {
		Map itemMap = new SequencedHashMap();
        if (ids == null || ids.length == 0) {
            return itemMap;
        }
        
        try{
        	Application app = Application.getInstance();
        	TransportModule mod = (TransportModule)app.getModule(TransportModule.class);
        	
        	for (int i=0; i<ids.length; i++) {
        		SetupObject o = (SetupObject)mod.getSetupObject("fms_tran_outsourcepanel", ids[i]);
	    			itemMap.put(ids[i], o.getName());
		    }
        }catch(Exception e){
        	 Log.getLog(getClass()).error("Error retrieving item", e);
        }
		return itemMap;
	}

	protected Table initPopupTable() {
		return new OutsourcePanelSelectTable();
	}

	class OutsourcePanelSelectTable extends PopupSelectBoxTable{
		
		public OutsourcePanelSelectTable(){}
		
		public OutsourcePanelSelectTable(String s){
			super(s);
		}
		 
		public void init() {
	        super.init();
	        setWidth("100%");
	        setModel(new OutsourcePanelSelectTableModel());
        }
		
		public void onRequest(Event evt) {
			init();
        }
		
		class OutsourcePanelSelectTableModel extends PopupSelectBoxTableModel{
			
			public OutsourcePanelSelectTableModel(){
				super();
				Application application = Application.getInstance();
				addAction(new TableAction(FORWARD_SELECT,  application.getMessage("general.label.select", "Select")));
				
			    TableColumn tcName = new TableColumn("name", Application.getInstance().getMessage("fms.tran.setup.outsourcePanel"+"Name", "Name"));
			    tcName.setUrlParam("setup_id");
			    addColumn(tcName);
			    
			    TableColumn tcDescription = new TableColumn("description", Application.getInstance().getMessage("fms.tran.setup.outsourcePanel" +"Description", "Description"));
			    tcDescription.setSortable(false);
			    addColumn(tcDescription);
			    
			    TableColumn tcStatus = new TableColumn("status", Application.getInstance().getMessage("fms.tran.setup.status", "Status"));
				Map mapIsActive = new HashMap(); mapIsActive.put("1", Application.getInstance().getMessage("fms.tran.setup.active", "Active")); mapIsActive.put("0", Application.getInstance().getMessage("fms.tran.setup.inactive", "Inactive"));
				tcStatus.setFormat(new TableStringFormat(mapIsActive));
				addColumn(tcStatus);
				
				TableFilter tfSearchText = new TableFilter("tfSearchText");
				TextField searchText = new TextField("searchText");
				searchText.setSize("20");
				tfSearchText.setWidget(searchText);
				addFilter(tfSearchText);
				
				TableFilter tfStatus = new TableFilter("tfStatus");
				SelectBox statusIsActive = new SelectBox("statusIsActive");
				statusIsActive.setOptions("-1=" + Application.getInstance().getMessage("fms.tran.setup.status", "Status"));
				statusIsActive.setOptions("1=" + Application.getInstance().getMessage("fms.tran.setup.active", "Active"));
				statusIsActive.setOptions("0=" + Application.getInstance().getMessage("fms.tran.setup.inactive", "Inactive"));
				tfStatus.setWidget(statusIsActive);
				addFilter(tfStatus);
			}
			
			public String getStatusIsActive() {
				String returnValue = "-1";
				List lstStatusIsActive = (List)getFilterValue("tfStatus");
				if (lstStatusIsActive.size() > 0) {returnValue = (String)lstStatusIsActive.get(0);}
				return returnValue;
			}
			
			public Collection getTableRows() {
				TransportDao dao = (TransportDao) Application.getInstance().getModule(TransportModule.class).getDao();
				String type = "T";
				try {
			    	return dao.selectOutsourcePanel(getSearchText(), getStatusIsActive(), type, getSort(), isDesc(), getStart(), getRows());
			    } catch (DaoException e) {
			        Log.getLog(getClass()).error(e.toString());
			        return new ArrayList();
			    }
			}
			
			public int getTotalRowCount() {
				TransportDao dao = (TransportDao) Application.getInstance().getModule(TransportModule.class).getDao();
				String type = "T";
				try {
			    	return dao.selectCountOutsourcePanel(getSearchText(), getStatusIsActive(), type);
			    } catch (DaoException e) {
			        Log.getLog(getClass()).error(e.toString());
			        return 0;
			    }
			}
			
			public String getChannel() {
				String returnValue = "-1";
				List lstChannel = (List)getFilterValue("tfChannel");
				if (lstChannel.size() > 0) {returnValue = (String)lstChannel.get(0);}
				return returnValue;
			}
			
			public String getCategory() {
				String returnValue = "-1";
				List lstCategory = (List)getFilterValue("tfCategory");
				if (lstCategory.size() > 0) {returnValue = (String)lstCategory.get(0);}
				return returnValue;
			}
			
			public String getTableRowKey() {
			    return "setup_id";
			}
			
			public String getSearchText() {
				return (String)getFilterValue("tfSearchText");
			}
			
		}
	}
}
