package com.tms.fms.transport.ui;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.PopupSelectBox;
import kacang.stdui.PopupSelectBoxTable;
import kacang.stdui.PopupSelectBoxTableModel;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableStringFormat;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.fms.transport.model.SetupObject;
import com.tms.fms.transport.model.TransportDao;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.VehicleObject;

public class VehicleSelect extends PopupSelectBox{
	private String id;
	public VehicleSelect(){}
	
	public VehicleSelect(String s){
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
        			VehicleObject o = (VehicleObject)mod.getVehicle(ids[i]);
	    			itemMap.put(ids[i], o.getVehicle_num());
		    }
        }catch(Exception e){
        	 Log.getLog(getClass()).error("Error retrieving item", e);
        }
		return itemMap;
	}

	protected Table initPopupTable() {
		return new VehicleSelectTable();
	}

	class VehicleSelectTable extends PopupSelectBoxTable{
		
		public VehicleSelectTable(){}
		
		public VehicleSelectTable(String s){
			super(s);
		}
		 
		public void init() {
	        super.init();
	        setWidth("100%");
	        setModel(new VehicleSelectTableModel());
        }
		
		public void onRequest(Event evt) {
			if(id == null)
				id = evt.getRequest().getParameter("vehicleId");
			init();
        }
		
		class VehicleSelectTableModel extends PopupSelectBoxTableModel{
			
			public VehicleSelectTableModel(){
				super();
				
				
				Application application = Application.getInstance();
				addAction(new TableAction(FORWARD_SELECT,  application.getMessage("general.label.select", "Select")));
				 
			    TableColumn tcVehicleNum = new TableColumn("vehicle_num", Application.getInstance().getMessage("fms.tran.table.vehicleNum", "Vehicle No."));
			    tcVehicleNum.setUrlParam("vehicle_num");
			    addColumn(tcVehicleNum);
			    
			    TableColumn tcChannel = new TableColumn("channel_name", Application.getInstance().getMessage("fms.tran.table.channel", "Channel"));
			    addColumn(tcChannel);
			    
			    TableColumn tcName = new TableColumn("category_name", Application.getInstance().getMessage("fms.tran.table.vehicleCategory", "Vehicle Categoty"));
			    addColumn(tcName);

			    TableFilter tfSearchText = new TableFilter("tfSearchText");
				TextField searchText = new TextField("searchText");
				searchText.setSize("20");
				tfSearchText.setWidget(searchText);
				addFilter(tfSearchText);
				
				
				//
				TableFilter tfCategory = new TableFilter("tfCategory");
				SelectBox selectCat = new SelectBox("selectCat");
				selectCat.addOption("-1", "-- SELECT ALL --");
				TransportModule tran = (TransportModule) Application.getInstance().getModule(TransportModule.class);        		
	    		Collection collcat = tran.selectSetupObject("fms_tran_category",null,"-1", "name",false,0,-1);
	    		for(Iterator it = collcat.iterator(); it.hasNext(); ){
	    			SetupObject so = (SetupObject) it.next();
	    			String id = so.getSetup_id();
	    			String name = so.getName();
	    			selectCat.addOption(id, name);
	    		}
				tfCategory.setWidget(selectCat);
				addFilter(tfCategory);
				
				
			}
			
			public String getType() {
				String returnValue = "-1";
				/*List lstType = (List)getFilterValue("tfType");
				if (lstType.size() > 0) {returnValue = (String)lstType.get(0);}*/
				return returnValue;
			}
			
			public String getStatusIsActive() {
				String returnValue = "-1";
				
				return returnValue;
			}
						
			public Collection getTableRows() {
				TransportDao dao = (TransportDao) Application.getInstance().getModule(TransportModule.class).getDao();
				try {
			    	return dao.selectVehicle(getSearchText(), getType(), getChannel(), getCategory(), "", "", "", getStatusIsActive(), getSort(), isDesc(), getStart(), getRows());
			    } catch (DaoException e) {
			        Log.getLog(getClass()).error(e.toString());
			        return new ArrayList();
			    }
			}
			
			public int getTotalRowCount() {
				TransportDao dao = (TransportDao) Application.getInstance().getModule(TransportModule.class).getDao();
				try {
			    	return dao.selectVehicleCount(getSearchText(), getType(), getChannel(), getCategory(), "", "", "", getStatusIsActive());
			    } catch (DaoException e) {
			        Log.getLog(getClass()).error(e.toString());
			        return 0;
			    }
			}
			
			public String getChannel() {
				String returnValue = "-1";
				/*List lstChannel = (List)getFilterValue("tfChannel");
				if (lstChannel.size() > 0) {returnValue = (String)lstChannel.get(0);}*/
				return returnValue;
			}
			
			public String getCategory() {
				
				String returnValue = "-1";
				
				List lstCategory = (List)getFilterValue("tfCategory");
				if(lstCategory!=null && lstCategory.size()>0)                    
					returnValue = (String)lstCategory.get(0);
				
				return returnValue;
				
				
			}
		
			
			public String getTableRowKey() {
			    return "vehicle_num";
			}
			
			
			public String getSearchText() {
				return (String)getFilterValue("tfSearchText");
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
