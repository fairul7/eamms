package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;

import kacang.Application;
import kacang.stdui.PopupSelectBox;
import kacang.stdui.PopupSelectBoxTable;
import kacang.stdui.PopupSelectBoxTableModel;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class RequestPopupSelect extends PopupSelectBox {
	public RequestPopupSelect(){
		
	}
	
	public RequestPopupSelect(String name){
		super(name);
	}
	
	protected Table initPopupTable(){
		return new RequestPopupTable();
	}
	
	protected Map generateOptionMap(String[] ids) {
		Map map = new SequencedHashMap();
		if (ids != null && ids.length == 1) {
			String id = ids[0];
			if (id != null && !id.equals("")) {
				EngineeringRequest req = getOptionMap(id);
				String requestId = req.getRequestId();
				String name = req.getTitle();
				if (requestId == null && name == null) {
					requestId = "";
					name = "---";
				}
				map.put(id, requestId +" - " +name);
			}
		}
		return map;
	}
	
	public String getSelectedId() {
		Collection values = (Collection) getValue();
		if (values != null && values.size() != 0) {
			return (String) values.iterator().next();
		}
		return null;
	}
	
	private EngineeringRequest getOptionMap(String requestId) {
		Application app = Application.getInstance();
		EngineeringModule module = (EngineeringModule) app.getModule(EngineeringModule.class);
		return module.getRequest(requestId);
	}

	public String getDefaultTemplate() {
		return "fms/engineering/singleUserSelectBox";
	}

	public class RequestPopupTable extends PopupSelectBoxTable {
		public RequestPopupTable() {
		}

		public RequestPopupTable(String name) {
			super(name);
		}
		
		public void init() {
			super.init();
			setWidth("100%");
			setMultipleSelect(false);
			setModel(new RequestPopupTableModel());
		}
		
		public class RequestPopupTableModel extends PopupSelectBoxTableModel {
			
			public RequestPopupTableModel() {
				super();
				Application app = Application.getInstance();
				
				TableColumn requestId = new TableColumn("requestId", app.getMessage("fms.request.label.requestId"));
				addColumn(requestId);
				
				TableColumn title = new TableColumn("title", app.getMessage("fms.request.label.requestTitle"));
				addColumn(title);
				
				TableColumn programName = new TableColumn("programName", app.getMessage("fms.request.label.program"));
				addColumn(programName);
				
				TableColumn requiredFrom = new TableColumn("requiredFrom", app.getMessage("fms.request.label.dateRequiredFrom"));
				addColumn(requiredFrom);
				
				TableColumn requiredTo = new TableColumn("requiredTo", app.getMessage("fms.request.label.dateRequiredTo"));
				addColumn(requiredTo);
				
				addAction(new TableAction("clear", app.getMessage("general.label.clear", "Clear")));
				
				addAction(new TableAction(FORWARD_SELECT, app.getMessage("general.label.select", "Select")));
				
				addFilter(new TableFilter("keyword"));	
			}
			
			public Collection getTableRows() {
				Application app = Application.getInstance();
				String userName = app.getCurrentUser().getUsername();
				EngineeringModule module = (EngineeringModule) app.getModule(EngineeringModule.class);
				Collection list = new ArrayList();
				String keyword = (String) getFilterValue("keyword");
                try {
                	list= module.selectRequestList(keyword, userName, getSort(), isDesc(), getStart(), getRows());
                }
                catch (Exception e) {
                    Log.getLog(getClass()).error(e.toString(), e);
                }
				return list;
			}

			public int getTotalRowCount() {
				Application app = Application.getInstance();
				String userName = app.getCurrentUser().getUsername();
				EngineeringModule module = (EngineeringModule) app.getModule(EngineeringModule.class);
				String keyword = (String) getFilterValue("keyword");
				
				return module.countRequestList(keyword, userName);
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
				return "requestId";
			}
		}
	}
}
