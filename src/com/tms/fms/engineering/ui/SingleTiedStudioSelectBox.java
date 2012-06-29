package com.tms.fms.engineering.ui;


import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.*;
import kacang.util.*;

import org.apache.commons.collections.SequencedHashMap;
import java.util.*;

import com.tms.fms.engineering.model.EngineeringDao;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.facility.model.FacilityDao;
import com.tms.fms.facility.model.FacilityModule;
import com.tms.fms.facility.model.FacilityObject;
import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;

public class SingleTiedStudioSelectBox extends PopupSelectBox {
	private String link;
	private String serviceId;
	
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public SingleTiedStudioSelectBox() {
	}

	public SingleTiedStudioSelectBox(String name) {
		super(name);
	}

	protected Table initPopupTable() {
		return new SingleFacilityPopupTable();
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
		SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		try {
			RateCard rc=module.getRateCard(id);
			return rc.getName();
		} catch (Exception e) {
			Log.getLog(SingleTiedStudioSelectBox.class).error(e.toString(), e);
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

	public class SingleFacilityPopupTable extends PopupSelectBoxTable {
		public SingleFacilityPopupTable() {
		}

		public SingleFacilityPopupTable(String name) {
			super(name);
		}
		
		public void init() {
			super.init();
			setWidth("100%");
			setMultipleSelect(false);
			setModel(new SingleFacilityPopupTableModel());
		}
		
		public class SingleFacilityPopupTableModel extends PopupSelectBoxTableModel {
			public SingleFacilityPopupTableModel() {
				super();
				
				Application application = Application.getInstance();
                //Adding Columns
                TableColumn tcName=new TableColumn("name",Application.getInstance().getMessage("fms.label.equipment"));
    			//tcName.setUrlParam("programId");
    			
    			TableColumn tcRemarks=new TableColumn("remarksRequest", Application.getInstance().getMessage("fms.facility.label.remarks"));;
    			
    			addColumn(tcName);
    			addColumn(tcRemarks);
				
				//addAction(new TableAction("clear", application.getMessage("general.label.clear", "Clear")));
				
				addAction(new TableAction(FORWARD_SELECT, application.getMessage("general.label.select", "Select")));
				
				addFilter(new TableFilter("keyword"));
			}
	
			public Collection getTableRows() {
				Collection list = new ArrayList();
				String keyword = (String) getFilterValue("keyword");
				User currentUser;  
				currentUser =Application.getInstance().getCurrentUser();
				String userId = currentUser.getId();
				EngineeringDao dao = (EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
                try {
                	list=dao.selectTiedRateCardStudio(keyword,userId, getSort(), isDesc(), getStart(), getRows());
                }
                catch (Exception e) {
                    Log.getLog(getClass()).error(e.toString(), e);
                }
				return list;
			}
	
			public int getTotalRowCount() {
				String keyword = (String) getFilterValue("keyword");
				try {
					EngineeringDao dao = (EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
                	return dao.selectRateCardFacilityCount(keyword,serviceId);
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
				return "id";
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

