package com.tms.ekms.security.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorIn;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorParenthesis;
import kacang.services.security.Group;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.security.ui.UsersSelectBox;
import kacang.stdui.PopupSelectBoxTable;
import kacang.stdui.PopupSelectBoxTableModel;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.ui.Event;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.hr.leave.model.LeaveModule;


public class LeaveUserSelectBox extends UsersSelectBox {

    private boolean showUsername;
    private String userId;
    
    public LeaveUserSelectBox() {
        super();
    }

    public LeaveUserSelectBox(String name) {
        super(name);
    }

    
    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
		
		
		
		
	}

	public boolean isShowUsername() {
        return showUsername;
    }

    public void setShowUsername(boolean showUsername) {
        this.showUsername = showUsername;
    }

    protected Table initPopupTable() {
        return new UsersPopupTable();
    }

    protected Map generateOptionMap(String[] ids) {
        Map usersMap = new SequencedHashMap();
        if (ids == null || ids.length == 0) {
            return usersMap;
        }

        try {
            Application app = Application.getInstance();
            SecurityService security = (SecurityService)app.getService(SecurityService.class);
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorIn("id", ids, DaoOperator.OPERATOR_AND));
            String sort = (isShowUsername()) ? "username" : "firstName";
            Collection userList = security.getUsers(query, 0, -1, sort, false);

            // build users map
            Map tmpMap = new SequencedHashMap();
            for (Iterator i=userList.iterator(); i.hasNext();) {
                User user = (User)i.next();
                String label = (isShowUsername()) ? "[" + user.getUsername() + "]" + user.getName() : user.getName();
                tmpMap.put(user.getId(), label);
            }

            // sort
            for (int j=0; j<ids.length; j++) {
                String name = (String)tmpMap.get(ids[j]);
                if (name == null) {
                    name = "---";
                }
                else if (name.trim().length() > 50) {
                    name = name.substring(0, 50) + "..";
                }
                usersMap.put(ids[j], name);
            }
        }
        catch (SecurityException e) {
            Log.getLog(getClass()).error("Error retrieving users", e);
        }

        return usersMap;
    }

    public class UsersPopupTable extends PopupSelectBoxTable {

        public UsersPopupTable() {
        }

        public UsersPopupTable(String name) {
            super(name);
        }

        public void init() {
            super.init();
            setWidth("100%");
            setModel(new UsersPopupTable.UserTableModel());
            loadGroups();
            getModel().setSort("username");
        }

        protected void loadGroups() {
            Application application = Application.getInstance();
            SecurityService service = (SecurityService) application.getService(SecurityService.class);
            Map groupMap = new SequencedHashMap();
            try {
                DaoQuery q = new DaoQuery();
                q.addProperty(new OperatorEquals("active", "1", DaoOperator.OPERATOR_AND));
                Collection list = service.getGroups(q, 0, -1, "groupName", false);
                groupMap.put("-1", application.getMessage("security.label.filterGroup", "Filter Group"));
                for (Iterator i = list.iterator(); i.hasNext();) {
                    Group group = (Group) i.next();
                    groupMap.put(group.getId(), group.getName());
                }
            }
            catch (Exception e) {
                Log.getLog(UsersPopupTable.class).error("Error loading groups: " + e.toString(), e);
            }
            //Adding Filters
            TableFilter groupFilter = getModel().getFilter("group");
            SelectBox groupSelect = (SelectBox) groupFilter.getWidget();
            groupSelect.setOptionMap(groupMap);
            groupSelect.setMultiple(false);
        }

        public void onRequest(Event evt) {
            loadGroups();
        }

        public class UserTableModel extends PopupSelectBoxTableModel {
            public UserTableModel() {
                super();

                Application application = Application.getInstance();
                //Adding Columns
                TableColumn nameColumn = new TableColumn("username", application.getMessage("security.label.username", "Username"));
                nameColumn.setUrlParam("id");
                addColumn(nameColumn);
                addColumn(new TableColumn("firstName", application.getMessage("security.label.firstName", "First Name")));
                addColumn(new TableColumn("lastName", application.getMessage("security.label.lastName", "Last Name")));

                //Adding Actions
                addAction(new TableAction(FORWARD_SELECT,  application.getMessage("general.label.select", "Select")));

                //Adding Filters
                TableFilter userNameFilter = new TableFilter("query");
                TableFilter groupFilter = new TableFilter("group");

                Map groupMap = new SequencedHashMap();
                SelectBox groupSelect = new SelectBox();
                groupSelect.setOptionMap(groupMap);
                groupSelect.setMultiple(false);
                groupFilter.setWidget(groupSelect);

                addFilter(userNameFilter);
                addFilter(groupFilter);
            }

            public Collection getTableRows() {
                Collection list = new ArrayList();
                Application app = Application.getInstance();
                LeaveModule handler = (LeaveModule) app.getInstance().getModule(LeaveModule.class);
                
            	SecurityService service = (SecurityService) app.getService(SecurityService.class);
           
                try {
                    String groupId = getGroupFilter();
                    String queryString = (String)getFilterValue("query");
                   

                    
                    boolean leaveManager = service.hasPermission(userId, LeaveModule.PERMISSION_MANAGE_LEAVE, null, null);
                    if (leaveManager) { 
                        if(groupId !=null && !groupId.equals("-1") && !"".equals(groupId))
                        	list = service.getGroupUsers(groupId, generateDaoProperties(), getStart(), getRows(), getSort(), isDesc());
                        else                        	
                        	list = service.getUsers(generateDaoProperties(), getStart(), getRows(), getSort(), isDesc());
                    }
                    else {
                        //list = handler.viewEmployeeListForApprover(userId, generateDaoProperties(), getStart(), getRows(), getSort(), isDesc());
                        list = handler.viewEmployeeListForApproverWithAllHierarchy(userId, groupId, queryString, generateDaoProperties(), getStart(), getRows(), getSort(), isDesc());
                        
                        
                    
                    }
                }
                catch (Exception e) {
                    Log.getLog(UsersPopupTable.class).error(e.toString(), e);
                }
                return list;
            }

            public int getTotalRowCount() {
                try {
                	Application app = Application.getInstance();
                	SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                    LeaveModule handler = (LeaveModule) app.getInstance().getModule(LeaveModule.class);
                    String groupId = getGroupFilter();
                    String queryString = (String) getFilterValue("query");
                   
                    
                    boolean leaveManager = service.hasPermission(userId, LeaveModule.PERMISSION_MANAGE_LEAVE, null, null);
                    if (leaveManager) {
                    	if(groupId !=null && !groupId.equals("-1") && !groupId.equals(""))
                        	return service.getGroupUsers(groupId, generateDaoProperties(), 0,-1, getSort(), isDesc()).size();
                        else                        	
                           return  service.getUsers(generateDaoProperties(), 0, -1, getSort(), isDesc()).size();
                    	
                    }
                    else{
                    	
                    	 return handler.viewEmployeeListForApproverWithAllHierarchy(userId, groupId, queryString, generateDaoProperties(), 0, -1, getSort(), isDesc()).size();
                         
                    }
                    
/*                    if (groupId == null || "".equals(groupId) || "-1".equals(groupId)) {
                        return service.getUserCount(generateDaoProperties());
                    }
                    else {
                        return service.getGroupUsersCount(groupId, generateDaoProperties());
                    }*/
                    
                    
                }
                catch (Exception e) {
                    Log.getLog(UsersPopupTable.class).error(e.toString(), e);
                }
                return 0;
            }

            private DaoQuery generateDaoProperties() {
                DaoQuery properties = new DaoQuery();
                OperatorParenthesis op = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                op.addOperator(new OperatorLike("username", getFilterValue("query"), null));
                op.addOperator(new OperatorLike("firstName", getFilterValue("query"), DaoOperator.OPERATOR_OR));
                op.addOperator(new OperatorLike("lastName", getFilterValue("query"), DaoOperator.OPERATOR_OR));
                properties.addProperty(op);
                properties.addProperty(new OperatorEquals("u.active", "1", DaoOperator.OPERATOR_AND));
                return properties;
            }

            public String getGroupFilter() {
                Collection group = (Collection) getFilterValue("group");
                if (group != null)
                    if (!(group.isEmpty()))
                        return (String) group.iterator().next();
                return "";
            }

            public String getTableRowKey() {
                return "id";
            }

        }
    }

}