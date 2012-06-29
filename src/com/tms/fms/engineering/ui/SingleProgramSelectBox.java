package com.tms.fms.engineering.ui;


import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.*;
import kacang.ui.*;
import kacang.util.*;

import org.apache.commons.collections.SequencedHashMap;
import java.util.*;

import com.tms.fms.setup.model.SetupModule;

public class SingleProgramSelectBox extends PopupSelectBox {
	private String link;
	
	public SingleProgramSelectBox() {
	}

	public SingleProgramSelectBox(String name) {
		super(name);
	}

	protected Table initPopupTable() {
		return new SingleProgramPopupTable();
	}

	protected Map generateOptionMap(String[] ids) {
		Map map = new SequencedHashMap();
		if (ids != null && ids.length == 1) {
			String name = getName(ids[0]);
			if (name == null) {
				name = "---";
			}
			map.put(ids[0], name);
		}
		return map;
	}
	
	public static String getName(String id) {
		SetupModule mod = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		try {
			return mod.selectProgName(id);
		} catch (DaoException e) {
			Log.getLog(SingleProgramSelectBox.class).error(e.toString(), e);
		}
		return null;
	}
	
	public String getSelectedId() {
		Collection values = (Collection) getValue();
		if (values != null && values.size() != 0) {
			return (String) values.iterator().next();
		}
		return null;
	}
	
	public String getDefaultTemplate() {
		return "fms/engineering/singleUserSelectBox";
	}

	public class SingleProgramPopupTable extends PopupSelectBoxTable {
		public SingleProgramPopupTable() {
		}

		public SingleProgramPopupTable(String name) {
			super(name);
		}
		
		public void init() {
			super.init();
			setWidth("100%");
			setMultipleSelect(false);
			setModel(new SingleProgramPopupTableModel());
		}
		
		public class SingleProgramPopupTableModel extends PopupSelectBoxTableModel {
			public SingleProgramPopupTableModel() {
				super();
				
				Application application = Application.getInstance();
                //Adding Columns
                TableColumn tcName=new TableColumn("programName",Application.getInstance().getMessage("fms.setup.table.programName", "Program Name"));
    			tcName.setUrlParam("programId");
    			
    			TableColumn tcDescription=new TableColumn("description", Application.getInstance().getMessage("fms.setup.table.description", "Description"));
    			
    			TableColumn tcProducer=new TableColumn("producer", Application.getInstance().getMessage("fms.setup.table.producer", "Producer"), false);
    			
    			TableColumn tcPFECode=new TableColumn("pfeCode", Application.getInstance().getMessage("fms.setup.table.pfeCode", "PFE Code"));
    			
    			TableColumn tcProductionDate=new TableColumn("startProductionDate", Application.getInstance().getMessage("fms.setup.table.productionDate", "Production Date"));
    			tcProductionDate.setFormat(new TableDateFormat("dd-MM-yyyy"));
    			
    			TableColumn tcDepartment=new TableColumn("departmentName", Application.getInstance().getMessage("fms.setup.table.department","Client/Department"));
                
    			addColumn(tcName);
    			addColumn(tcDescription);
    			addColumn(tcProducer);
    			addColumn(tcPFECode);
    			addColumn(tcProductionDate);
    			addColumn(tcDepartment);
				
				addAction(new TableAction("clear", application.getMessage("general.label.clear", "Clear")));
				
				addAction(new TableAction(FORWARD_SELECT, application.getMessage("general.label.select", "Select")));
				
				addFilter(new TableFilter("keyword"));
			}
	
			public Collection getTableRows() {
				Collection list = new ArrayList();
				String keyword = (String) getFilterValue("keyword");
				SetupModule mod = (SetupModule)Application.getInstance().getModule(SetupModule.class);
                try {
                	list=mod.selectProgram(keyword, "1" , getSort(), isDesc(), getStart(), getRows());
                    
                }
                catch (Exception e) {
                    Log.getLog(getClass()).error(e.toString(), e);
                }
				return list;
			}
	
			public int getTotalRowCount() {
				String keyword = (String) getFilterValue("keyword");
				try {
                	SetupModule mod = (SetupModule)Application.getInstance().getModule(SetupModule.class);
                	return mod.selectProgramCount(keyword, "1");
                }
                catch (Exception e) {
                    Log.getLog(getClass()).error(e.toString(), e);
                }
                return 0;
			}
	
			public Forward processAction(Event event, String action, String[] selectedKeys) {
				try {
					if (PopupSelectBox.FORWARD_SELECT.equals(action)) {
						if (getPopupSelectBox() != null) {
							getPopupSelectBox().setIds(selectedKeys);
							return new Forward(PopupSelectBox.FORWARD_SELECT);
						}
					} else if ("clear".equals(action)) {
						return new Forward("clear");
					}
					return null;
				} catch (Exception e) {
					Log.getLog(getClass()).error("Error processing action " + action + ": " + e.toString(), e);
					return new Forward(PopupSelectBox.FORWARD_ERROR);
				}
			}
			
			public String getTableRowKey() {
				return "programId";
			}
		}
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}

