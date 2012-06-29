package com.tms.fms.transport.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorParenthesis;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.security.ui.UsersSelectBox;
import kacang.services.security.ui.UsersSelectBox.UsersPopupTable;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.ui.Event;
import kacang.util.Log;

import com.tms.fms.department.model.FMSDepartmentDao;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.department.model.FMSUnit;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.facility.ui.UnitUserSelectBox.UnitUserTable;
import com.tms.fms.facility.ui.UnitUserSelectBox.UnitUserTable.UnitUserTableModel;
import com.tms.fms.register.model.FMSRegisterManager;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.util.WidgetUtil;

public class RosterUserSelectBox extends UsersSelectBox {
	private SelectBox unit;
	
	public RosterUserSelectBox() {
        super();
        setSortable(false);
    }

    public RosterUserSelectBox(String name) {
        super(name);
        setSortable(false);
    }

	protected Table initPopupTable() {
		return new UnitUserTable();
	}
	
	public class UnitUserTable extends UsersSelectBox.UsersPopupTable {
		public UnitUserTable() {
			super();
		}

		public UnitUserTable(String name) {
			super(name);
		}

		public void onRequest(Event evt) {
			setWidth("100%");
			setModel(new UnitUserTableModel());
			loadGroups();
		}

		public class UnitUserTableModel extends UsersSelectBox.UsersPopupTable.UserTableModel {
			Collection lstUnit;
			int count = 0;
			public UnitUserTableModel() {
				super();
				
				Application application = Application.getInstance();	
				TableColumn staffId = new TableColumn("property1", application.getMessage("fms.userSelect.staffId"));
				addColumn(staffId);

				TableColumn telMobile = new TableColumn("telMobile", application.getMessage("fms.userSelect.telMobile"));
				addColumn(telMobile);

				TableColumn telOffice = new TableColumn("telOffice", application.getMessage("fms.userSelect.telOffice"));
				addColumn(telOffice);
				
				TableFilter filterUnit = new TableFilter("filterUnit");
				unit = new SelectBox("unit");
		        try {
			    	FMSDepartmentDao dao = (FMSDepartmentDao)Application.getInstance().getModule(FMSDepartmentManager.class).getDao();
			    	
					SetupModule module=(SetupModule)application.getModule(SetupModule.class);
					if(viewAllPermission()){
						lstUnit = dao.selectUnit();//module.getUnits(application.getCurrentUser().getId());//dao.selectUnit();
					}else {
						String userId = application.getCurrentUser().getId(); 
						lstUnit = dao.selectUnitById(userId);
					}
					unit.addOption("-1", "Unit Filter");
				    if (lstUnit.size() > 0) {
				    	for (Iterator i=lstUnit.iterator(); i.hasNext();) {
				        	FMSUnit o = (FMSUnit)i.next();
				        	unit.addOption(o.getId(),o.getName());
				        }
				    }
				}catch (Exception e) {
				    Log.getLog(getClass()).error(e.toString());
				}
				filterUnit.setWidget(unit);
				addFilter(filterUnit);
			}

			public Collection getTableRows() {
				
				Collection list = new ArrayList();
				Application application = Application.getInstance();
				SecurityService service = (SecurityService) application.getService(SecurityService.class);
				String keyword = (String) getFilterValue("query");
				FMSRegisterManager manager = (FMSRegisterManager) application.getModule(FMSRegisterManager.class);
				TransportModule module=(TransportModule)application.getModule(TransportModule.class);
				try{
				
                String sort  = getSort();
                if(sort==null||sort.trim().length()==0)
                    sort = "firstName";
                String groupId = getGroupFilter();
                
                if(viewAllPermission()){
										
					list = manager.getAllUsers(getSelectBoxValue(unit), groupId, generateDaoProperties(), 0, -1, sort, isDesc());
				}else{              
					String unitId = null;
					try{
						User user =  manager.getUser(application.getCurrentUser().getId());						
						unitId = (String) user.getProperty("unit");
					}catch(Exception er){}
					
					list = manager.getUnitUsers(unitId, groupId, generateDaoProperties(), 0, -1, sort, isDesc());
				}
               
                count = list.size();
                                				
				// get selected page
                int sStart = getStart();
                int sEnd = sStart + getRows();
                if (sStart < 0) {
                    sStart = 0;
                }
                else if (sStart > list.size()) {
                    sStart = list.size()-1;
                }
                if (sEnd > list.size()) {
                    sEnd = list.size();
                }
                else if (sEnd <= sStart) {
                    sEnd = sStart + 1;
                }
                list = new ArrayList(list).subList(sStart, sEnd);
                
				}catch (Exception e) {
                    Log.getLog(UsersPopupTable.class).error("Error retrieving calendar users", e);
                }
				return list;
			}

			
			public int getTotalRowCount() {
				
				return count;
				
			}

			public String getUnitFilter(){
				String unitFilter = "";
				unitFilter = WidgetUtil.getSbValue(unit);
				
				if (unitFilter != null && "-1".equals(unitFilter)) unitFilter = null;
				
				return unitFilter;
			}
			
			private String getSelectBoxValue(SelectBox sb) {
			    if (sb != null) {
			    	Map selected = sb.getSelectedOptions();
			    	if (selected.size() == 1) {
			    		return (String)selected.keySet().iterator().next();
			    	}
			    }
			    return null;
			}
			
			public boolean viewAllPermission(){
				//check permission so user can view all m.anpower
				boolean viewAll = false;
				Application app = Application.getInstance();
				SecurityService service = (SecurityService) app.getService(SecurityService.class);
				String userId = getWidgetManager().getUser().getId();	
				try{
					viewAll = service.hasPermission(userId, "com.tms.fms.transport.viewAllManpowerDuty", null, null);
				}catch(Exception er){}
				
				return viewAll;
				
			}
			
			protected DaoQuery generateDaoProperties() {
		         DaoQuery properties = new DaoQuery();
		         OperatorParenthesis op = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
		         op.addOperator(new OperatorLike("username", getFilterValue("query"), null));
		         op.addOperator(new OperatorLike("firstName", getFilterValue("query"), DaoOperator.OPERATOR_OR));
		         op.addOperator(new OperatorLike("lastName", getFilterValue("query"), DaoOperator.OPERATOR_OR));
		         op.addOperator(new OperatorLike("property1", getFilterValue("query"), DaoOperator.OPERATOR_OR));
		         properties.addProperty(op);
		         properties.addProperty(new OperatorEquals("u.active", "1", DaoOperator.OPERATOR_AND));
		         return properties;
		     }
		}
	}

	public SelectBox getUnit() {
		return unit;
	}

	public void setUnit(SelectBox unit) {
		this.unit = unit;
	}
		
}
