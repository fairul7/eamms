package com.tms.fms.engineering.ui;


import kacang.Application;
import kacang.model.DaoException;
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
import com.tms.fms.setup.model.SetupModule;
import com.tms.hr.competency.Competency;
import com.tms.hr.competency.CompetencyHandler;

public class SingleManpowerSelectBox extends PopupSelectBox {
	private String link;
	private String serviceId;
	
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public SingleManpowerSelectBox() {
	}

	public SingleManpowerSelectBox(String name) {
		super(name);
	}

	protected Table initPopupTable() {
		return new SingleManpowerPopupTable();
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
		CompetencyHandler handler = (CompetencyHandler) Application.getInstance().getModule(CompetencyHandler.class);
         
		try {
			Competency competency = handler.getCompetency(id);
			return competency.getCompetencyName();
		} catch (Exception e) {
			Log.getLog(SingleManpowerSelectBox.class).error(e.toString(), e);
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

	public class SingleManpowerPopupTable extends PopupSelectBoxTable {
		public SingleManpowerPopupTable() {
		}

		public SingleManpowerPopupTable(String name) {
			super(name);
		}
		
		public void init() {
			super.init();
			setWidth("100%");
			setMultipleSelect(false);
			setModel(new SingleManpowerPopupTableModel());
		}
		
		public class SingleManpowerPopupTableModel extends PopupSelectBoxTableModel {
			public SingleManpowerPopupTableModel() {
				super();
				
				Application application = Application.getInstance();
                //Adding Columns
                TableColumn tcName=new TableColumn("competencyName",Application.getInstance().getMessage("fms.facility.label.manpower"));
    			//tcName.setUrlParam("programId");
    			
    			TableColumn tcCategory=new TableColumn("competencyType", Application.getInstance().getMessage("fms.facility.label.competencyType"));;
    			
    			TableColumn tcMaketype=new TableColumn("competencyDescription", Application.getInstance().getMessage("fms.facility.label.competencyDescription"));
    			
    			
    			addColumn(tcName);
    			addColumn(tcCategory);
    			addColumn(tcMaketype);
				
				//addAction(new TableAction("clear", application.getMessage("general.label.clear", "Clear")));
				
				addAction(new TableAction(FORWARD_SELECT, application.getMessage("general.label.select", "Select")));
				
				addFilter(new TableFilter("keyword"));
			}
	
			public Collection getTableRows() {
				Collection list = new ArrayList();
				String keyword = (String) getFilterValue("keyword");
				EngineeringDao dao = (EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
                try {
                	list=dao.selectRateCardManpower(keyword,serviceId, getSort(), isDesc(), getStart(), getRows());
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
                	return dao.selectRateCardManpowerCount(keyword,serviceId);
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
				return "competencyId";
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

