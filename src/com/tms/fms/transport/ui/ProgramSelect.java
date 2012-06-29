package com.tms.fms.transport.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import kacang.ui.Forward;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.fms.setup.model.SetupModule;

	public class ProgramSelect extends PopupSelectBox {
		
		public static String FORWARD_LISTING_ADD="program.listing.add";
		public static String FORWARD_LISTING_INACTIVE="program.listing.inactive";
		public static String FORWARD_LISTING_DELETE="program.listing.delete";
		
	    public ProgramSelect() {
	        super();
	        super.setMultiple(false);
	    }

	    public ProgramSelect(String name) {
	        super(name);
	        super.setMultiple(false);
	    }

	    protected Table initPopupTable() {
	        return new ProgramPopTable();
	    }

	    protected Map generateOptionMap(String[] ids) {
			Map map = new SequencedHashMap();
			if (ids != null && ids.length == 1) {
				SetupModule setupModule = (SetupModule) Application.getInstance().getModule(SetupModule.class);
				String name;
				try {
					name = setupModule.selectProgName(ids[0]);
					if (name == null) {
						name = "---";
					}
					map.put(ids[0], name);
					
				} catch (DaoException e) {
					Log.getLog(getClass()).error(e.toString(), e);
				}				
			}
			return map;
		}
	    
//	    protected Map generateOptionMap(String[] ids) {
//	    	String id = null;
//	    	String name = null;
//	    	
//	    	Map tmpMaps = new SequencedHashMap();
//			if (ids == null || ids.length == 0){
//				return tmpMaps;
//		    }			
//			
//			try{
//			
//			SetupModule setupModule = (SetupModule) Application.getInstance().getModule(SetupModule.class);
//			id = ids[0];
//			if(!(id == null))
//				name = setupModule.selectProgName(id);
//			
//			if(name == null)
//				name = "---";
//			
//			tmpMaps.put(id,name);
//			
//			}catch(Exception er){
//				Log.getLog(getClass()).error("Error during getting name:"+er);
//			}		
//			return tmpMaps;
//	    	
//	        }

	    public String getDefaultTemplate() {
	    	return "fms/popupSingleSelectUser";
	    }

	    public String getId() {
	        Map optionMap = getOptionMap();
	        if (optionMap != null) {
	            Collection idSet = optionMap.keySet();
	            idSet.remove("");
	            String[] idArray = (String[])idSet.toArray(new String[0]);
	            return idArray[0];
	        }
	        else {
	            return new String();
	        }
	    }
		
	    public String getSelectedId() {
			Collection values = (Collection) getValue();
			if (values != null && values.size() != 0) {
				return (String) values.iterator().next();
			}
			return null;
		}

	    public class ProgramPopTable extends PopupSelectBoxTable {

	        public ProgramPopTable() {
	        }

	        public ProgramPopTable(String name) {
	            super(name);
	        }

	        public void init() {
	            super.init();
	            setWidth("100%");
	            setMultipleSelect(false);
	            setModel(new ProgramPopTable.ProgramTableModel());
	           
	        }       

	        public class ProgramTableModel extends PopupSelectBoxTableModel {
	            public ProgramTableModel() {
	                super();

	                Application application = Application.getInstance();
	                TableColumn tcName=new TableColumn("programName",Application.getInstance().getMessage("fms.setup.table.programName", "Program Name"));
	    			tcName.setUrlParam("programId");
	    			TableColumn tcDescription=new TableColumn("description", Application.getInstance().getMessage("fms.setup.table.description", "Description"));;
	    			TableColumn tcPFECode=new TableColumn("pfeCode", Application.getInstance().getMessage("fms.setup.table.pfeCode", "PFE Code"));
	    			TableColumn tcProductionDate=new TableColumn("startProductionDate", Application.getInstance().getMessage("fms.setup.table.productionDate", "Production Date"));
	    			tcProductionDate.setFormat(new TableDateFormat("dd-MM-yyyy"));
	    			TableColumn tcDepartment=new TableColumn("departmentName", Application.getInstance().getMessage("fms.setup.table.department","Client/Department"));
	    			TableColumn tcStatus=new TableColumn("status",Application.getInstance().getMessage("fms.setup.table.status", "Status"));
	    			Map mapIsActive = new HashMap(); mapIsActive.put("1", Application.getInstance().getMessage("fms.tran.setup.active", "Active")); mapIsActive.put("0", Application.getInstance().getMessage("fms.tran.setup.inactive", "Inactive"));
	    			tcStatus.setFormat(new TableStringFormat(mapIsActive));
	    				    			
	    			addColumn(tcName);
	    			addColumn(tcDescription);
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
	    			
	    			addAction(new TableAction(FORWARD_SELECT,  application.getMessage("general.label.select", "Select")));
	                
	                
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
	    				list=mod.selectProgram(getSearchText(), "1" , getSort(), isDesc(), getStart(), getRows());
	    			} catch (DaoException e) {
	    				Log.getLog(getClass()).error(e.toString(), e);
	    			}
	    			
	    			return list;
	    		}
	    		
	    		public int getTotalRowCount() {
	    			SetupModule mod = (SetupModule)Application.getInstance().getModule(SetupModule.class);
	    			int result=0;
	    			
	    			try {
	    				result=mod.selectProgramCount(getSearchText(), "1");
	    			} catch (DaoException e) {
	    				Log.getLog(getClass()).error(e.toString(), e);
	    			}
	    			return result;
	    		}
	    		
	    		public String getTableRowKey() {
	    		    return "programId";
	    		}
	    		
	    		
	    		public Forward processAction(Event event, String action, String[] selectedKeys) {
	                try {
	                	if (PopupSelectBox.FORWARD_SELECT.equals(action)) 
	 	    		   {
	 	    			   if (getPopupSelectBox() != null && selectedKeys != null && selectedKeys.length > 0) 
	 	    			   {
	 	    				   getPopupSelectBox().setIds(selectedKeys);	 	                      
	 	                       return new Forward(PopupSelectBox.FORWARD_SELECT);
	 	                   }
	 	                   else 
	 	                   {
	 	                	   return null;
	 	                   }
	 	    		   }
	 	               else 
	 	               {
	 	            	   return null;
	 	               }
	                }catch (Exception e) {
	                    Log.getLog(getClass()).error("Error processing action " + action + ": " + e.toString(), e);
	                    return new Forward(PopupSelectBox.FORWARD_ERROR);
	                }
	            }	            
	           
	        }
	    }
	}
