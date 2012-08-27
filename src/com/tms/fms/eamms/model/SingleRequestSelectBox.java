package com.tms.fms.eamms.model;

import java.util.Collection;
import java.util.Map;

import kacang.Application;
import kacang.stdui.PopupSelectBox;
import kacang.stdui.PopupSelectBoxTable;
import kacang.stdui.PopupSelectBoxTableModel;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

public class SingleRequestSelectBox extends PopupSelectBox {
	private String programId;
	
	public SingleRequestSelectBox() {
	}

	public SingleRequestSelectBox(String name) {
		super(name);
	}

	protected Table initPopupTable() {
		return new SingleRequestPopupTable();
	}

	protected Map generateOptionMap(String[] ids) {
		Map map = new SequencedHashMap();
		if (ids != null && ids.length == 1) {
			String name = getName(ids[0]);
			if (name == null) {
				name = "---";
			}
			
			name = ids[0] + " | " + name;
			map.put(ids[0], name);
		}
		return map;
	}
	
	public static String getName(String id) 
	{
		EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
		
		return em.getReqTitle(id);
	}
	
	public String getSelectedId() {
		Collection values = (Collection) getValue();
		if (values != null && values.size() != 0) {
			return (String) values.iterator().next();
		}
		return null;
	}
	
	public String getDefaultTemplate() {
		return "fms/eamms/singleReqSelectBox";
	}

	public class SingleRequestPopupTable extends PopupSelectBoxTable {
		public SingleRequestPopupTable() {
		}

		public SingleRequestPopupTable(String name) {
			super(name);
		}
		
		public void init() {
			super.init();
			setWidth("100%");
			setMultipleSelect(false);
			setModel(new SingleRequestPopupTableModel());
		}
		
		public class SingleRequestPopupTableModel extends PopupSelectBoxTableModel {
			public SingleRequestPopupTableModel() {
				super();
				
				Application application = Application.getInstance();
				
                TableColumn requestId=new TableColumn("requestId",Application.getInstance().getMessage("eamms.feed.list.msg.requestId", "Request ID"));
                addColumn(requestId);
    			
    			TableColumn title=new TableColumn("title", Application.getInstance().getMessage("eamms.feed.list.msg.requestTitle", "Request Title"));
    			addColumn(title);
    			
    			TableColumn description=new TableColumn("description", Application.getInstance().getMessage("eamms.feed.log.msg.description", "Description"), false);
    			addColumn(description);
    			
    			TableColumn requiredFrom=new TableColumn("requiredFrom", Application.getInstance().getMessage("eamms.feed.log.msg.requiredFrom", "Required From"));
    			requiredFrom.setFormat(new TableDateFormat("dd-MM-yyyy"));
    			addColumn(requiredFrom);
    			
    			TableColumn requiredTo=new TableColumn("requiredTo", Application.getInstance().getMessage("eamms.feed.log.msg.requiredTo", "Required To"));
    			requiredTo.setFormat(new TableDateFormat("dd-MM-yyyy"));
    			addColumn(requiredTo);
				
    			addFilter(new TableFilter("keyword"));
				
    			addAction(new TableAction("clear", application.getMessage("general.label.clear", "Clear")));
				addAction(new TableAction(FORWARD_SELECT, application.getMessage("general.label.select", "Select")));
			}
	
			public Collection getTableRows() 
			{
				EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
				String keyword = (String) getFilterValue("keyword");
				
				Collection result = em.getFmsRequest(programId, keyword, getSort(), isDesc(), getStart(), getRows());
				return result;
			}
	
			public int getTotalRowCount() 
			{
				EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
				String keyword = (String) getFilterValue("keyword");
				
				int result = em.getCountFmsRequest(programId, keyword);
				return result;
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
				return "id";
			}
		}
	}

	public String getProgramId()
	{
		return programId;
	}

	public void setProgramId(String programId)
	{
		this.programId = programId;
	}
}

