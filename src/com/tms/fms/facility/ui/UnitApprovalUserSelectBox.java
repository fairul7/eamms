package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorParenthesis;
import kacang.services.security.SecurityService;
import kacang.services.security.ui.UsersSelectBox;
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
import com.tms.fms.register.model.FMSRegisterManager;
import com.tms.fms.util.WidgetUtil;

public class UnitApprovalUserSelectBox extends UsersSelectBox {
	
	public UnitApprovalUserSelectBox() {
        super();
        setSortable(false);
    }

    public UnitApprovalUserSelectBox(String name) {
        super(name);
        setSortable(false);
    }
	
	protected Table initPopupTable() {
		return new UnitApprovalUserTable();
	}
	
	public class UnitApprovalUserTable extends UsersSelectBox.UsersPopupTable {
		public UnitApprovalUserTable() {
			super();
		}

		public UnitApprovalUserTable(String name) {
			super(name);
		}

		public void onRequest(Event evt) {
			setWidth("100%");
			setModel(new UnitApprovalUserTableModel());
			loadGroups();
		}

		public class UnitApprovalUserTableModel extends UsersSelectBox.UsersPopupTable.UserTableModel {
			
			int count = 0;
			public UnitApprovalUserTableModel() {
				super();
				Application application = Application.getInstance();
				
				TableColumn staffId = new TableColumn("property1", application.getMessage("fms.userSelect.staffId"));
				addColumn(staffId);
				
				TableColumn telMobile = new TableColumn("telMobile", application.getMessage("fms.userSelect.telMobile"));
				addColumn(telMobile);
				
				TableColumn telOffice = new TableColumn("telOffice", application.getMessage("fms.userSelect.telOffice"));
				addColumn(telOffice);
				
			}

			public Collection getTableRows() {
				
				Collection list = new ArrayList();
				Application application = Application.getInstance();
				SecurityService service = (SecurityService) application.getService(SecurityService.class);
				String keyword = (String) getFilterValue("query");
				FMSRegisterManager manager = (FMSRegisterManager) application.getModule(FMSRegisterManager.class);
				SetupModule setMod=(SetupModule)application.getModule(SetupModule.class);
				String currentUnit = setMod.getCurrentUnits(application.getCurrentUser().getId());
				try{
				
	                String sort  = getSort();
	                if(sort==null||sort.trim().length()==0)
	                    sort = "firstName";
	                
	                String groupId = getGroupFilter();
	                
	                list = manager.getUnitUsers(currentUnit, groupId, generateDaoProperties(), 0, -1, sort, isDesc());
	               
	                count = list.size();
	                                				
					// get selected page
	                int sStart = getStart();
	                int sEnd = sStart + getRows();
	                
	                if (sStart < 0) {
	                    sStart = 0;
	                } else if (sStart > list.size()) {
	                    sStart = list.size()-1;
	                }
	                
	                if (sEnd > list.size()) {
	                    sEnd = list.size();
	                } else if (sEnd <= sStart) {
	                    sEnd = sStart + 1;
	                }
	                
	                list = new ArrayList(list).subList(sStart, sEnd);
                
				}catch (Exception e) {
                    Log.getLog(UsersPopupTable.class).error("Error retrieving users", e);
                }
				return list;
			}

			
			public int getTotalRowCount() {
				
				return count;
				
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
}
