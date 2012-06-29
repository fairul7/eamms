package com.tms.fms.facility.ui;

import java.util.Collection;
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
import kacang.stdui.TableFilter;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.fms.facility.model.CategoryObject;
import com.tms.fms.facility.model.FacilityDao;
import com.tms.fms.facility.model.FacilityModule;
import com.tms.fms.facility.model.FacilityObject;
import com.tms.fms.transport.model.SetupObject;
import com.tms.fms.transport.model.TransportModule;

public class FacilitiesPopupSelectBox extends PopupSelectBox{

	public FacilitiesPopupSelectBox(){}
	
	public FacilitiesPopupSelectBox(String s){
		super(s);
	}
	
	protected Map generateOptionMap(String[] ids) {
		Map itemMap = new SequencedHashMap();
        if (ids == null || ids.length == 0) {
            return itemMap;
        }
        
        try{
        	Application app = Application.getInstance();
        	//SetupModule module = (SetupModule)app.getModule(SetupModule.class);
        	FacilityModule mod = (FacilityModule)app.getModule(FacilityModule.class);
        	
        	for (int i=0; i<ids.length; i++) {
	    		FacilityObject o = (FacilityObject)mod.getFacility(ids[i]);
        		//RateCard rc = (RateCard) module.getRateCardCategory(ids[i]);
	    		String name = o.getName();
	    		if (o.getStatus().equals("0")) {
	    			name = name + " (inactive)";
	    		}
	    		itemMap.put(ids[i], name);
		    }
        }catch(Exception e){
        	 Log.getLog(getClass()).error("Error retrieving item", e);
        }
		return itemMap;
	}

	protected Table initPopupTable() {
		return new FacilityCategoryPopupSelectBoxTable();
	}

	class FacilityCategoryPopupSelectBoxTable extends PopupSelectBoxTable{
		
		public FacilityCategoryPopupSelectBoxTable(){}
		
		public FacilityCategoryPopupSelectBoxTable(String s){
			super(s);
		}
		 
		public void init() {
	        super.init();
	        setWidth("100%");
	        setModel(new FacilityCategoryPopupSelectBoxTableModel());
        }
		
		public void onRequest(Event evt) {
			init();
        }
		
		class FacilityCategoryPopupSelectBoxTableModel extends PopupSelectBoxTableModel{
			
			public FacilityCategoryPopupSelectBoxTableModel(){
				super();
				Application application = Application.getInstance();
				addAction(new TableAction(FORWARD_SELECT,  application.getMessage("general.label.select", "Select")));
				
				TableColumn tcName = new TableColumn("name", Application.getInstance().getMessage("fms.facility.table.name", "Name"));
				addColumn(tcName);
			    
			    TableColumn tcDescription = new TableColumn("description", Application.getInstance().getMessage("fms.facility.table.description", "Description"));
			    addColumn(tcDescription);
			    
			    TableColumn tcCategory = new TableColumn("category_name", Application.getInstance().getMessage("fms.facility.table.category", "Category"));
			    addColumn(tcCategory);
			    
			    TableColumn tcChannel = new TableColumn("channel_name", Application.getInstance().getMessage("fms.facility.table.channel", "Channel"));
			    addColumn(tcChannel);		 
			    
			    TableFilter tfSearchText = new TableFilter("tfSearchText");
				TextField searchText = new TextField("searchText");
				searchText.setSize("20");
				tfSearchText.setWidget(searchText);
				addFilter(tfSearchText);
				
				TableFilter tfCategory = new TableFilter("tfCategory");
				SelectBox sbCategory = new SelectBox("sbCategory");
				sbCategory.setOptions("-1=" + Application.getInstance().getMessage("fms.facility.table.category", "Category"));
			    try {
					FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
					Collection lstCategory = mod.selectCategory("","","","",false,"","",false,0,-1);
				    if (lstCategory.size() > 0) {
				    	for (Iterator i=lstCategory.iterator(); i.hasNext();) {
				        	CategoryObject o = (CategoryObject)i.next();
				        	sbCategory.setOptions(o.getId()+"="+o.getName());
				        }
				    }
				}catch (Exception e) {
				    Log.getLog(getClass()).error(e.toString());
				}
				tfCategory.setWidget(sbCategory);
				addFilter(tfCategory);
				
				TableFilter tfChannel = new TableFilter("tfChannel");
				SelectBox sbChannel = new SelectBox("sbChannel");
			    sbChannel.setOptions("-1=" + Application.getInstance().getMessage("fms.tran.table.Channel", "Channel"));
			    try {
					TransportModule mod = (TransportModule)Application.getInstance().getModule(TransportModule.class);
					Collection lstChannel = mod.selectSetupObject(SetupObject.SETUP_CHANNEL, "", "1", "", false, 0, -1);
				    if (lstChannel.size() > 0) {
				    	for (Iterator i=lstChannel.iterator(); i.hasNext();) {
				        	SetupObject o = (SetupObject)i.next();
				        	sbChannel.setOptions(o.getSetup_id()+"="+o.getName());
				        }
				    }
				}catch (Exception e) {
				    Log.getLog(getClass()).error(e.toString());
				}
				tfChannel.setWidget(sbChannel);
				addFilter(tfChannel);
			}
			
			public Collection getTableRows() {
//				Application application = Application.getInstance();
//				SetupModule module = (SetupModule) application.getModule(SetupModule.class);
//
//				return module.getRateCardAllCategory(getSearchText(), getSort(), isDesc(), getStart(), getRows());
				Collection col = null;
				FacilityDao dao = (FacilityDao)Application.getInstance().getModule(FacilityModule.class).getDao();
				try {
					col = dao.selectFacility(getSearchText(), getCategory(), getChannel(), "1", getSort(), isDesc(), getStart(), getRows());
				} catch (DaoException e) {
					Log.getLog(getClass()).error(e.toString(), e);
				}
				return col;
			}

			public int getTotalRowCount() {
//				Application application = Application.getInstance();
//				SetupModule module = (SetupModule) application.getModule(SetupModule.class);
//				
//				return module.countRateCardAllCategory(getSearchText());
				FacilityDao dao = (FacilityDao)Application.getInstance().getModule(FacilityModule.class).getDao();
				try {
			    	return dao.selectFacilityCount(getSearchText(), getCategory(), getChannel(), "1");
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
			    return "id";
			}
			
			public String getSearchText() {
				return (String)getFilterValue("tfSearchText");
			}
		}
	}
}
