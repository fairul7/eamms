package com.tms.fms.department.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorIn;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorParenthesis;
import kacang.services.security.Group;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.PopupSelectBox;
import kacang.stdui.PopupSelectBoxTable;
import kacang.stdui.PopupSelectBoxTableModel;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

	public class PopupHODSelectBox extends PopupSelectBox {
	    public PopupHODSelectBox() {
	        super();
	        super.setMultiple(false);
	    }

	    public PopupHODSelectBox(String name) {
	        super(name);
	        super.setMultiple(false);
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
	            Collection userList = security.getUsers(query, 0, -1, "firstName", false);

	            // build users map
	            Map tmpMap = new SequencedHashMap();
	            for (Iterator i=userList.iterator(); i.hasNext();) {
	                User user = (User)i.next();
	                tmpMap.put(user.getId(), user.getName());
	            }

	            // sort
	            for (int j=0; j<ids.length; j++) {
	                String name = (String)tmpMap.get(ids[j]);
	                if (name == null) {
	                    name = "---";
	                }
	                usersMap.put(ids[j], name);
	            }
	        }
	        catch (kacang.services.security.SecurityException e) {
	            Log.getLog(getClass()).error("Error retrieving users", e);
	        }

	        return usersMap;
	    }

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

	    public class UsersPopupTable extends PopupSelectBoxTable {

	        public UsersPopupTable() {
	        }

	        public UsersPopupTable(String name) {
	            super(name);
	            
	        }

	        public void init() {
	            super.init();
	            setWidth("100%");
	            setMultipleSelect(false);
	            setModel(new UsersPopupTable.UserTableModel());
	            loadGroups();
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
	                addColumn(nameColumn);
	                addColumn(new TableColumn("firstName", application.getMessage("security.label.firstName", "First Name")));
	                addColumn(new TableColumn("lastName", application.getMessage("security.label.lastName", "Last Name")));
	                
	                TableColumn staffId = new TableColumn("property1", application.getMessage("fms.userSelect.staffId"));
					addColumn(staffId);
					
					TableColumn telMobile = new TableColumn("telMobile", application.getMessage("fms.userSelect.telMobile"));
					addColumn(telMobile);
					
					TableColumn telOffice = new TableColumn("telOffice", application.getMessage("fms.userSelect.telOffice"));
					addColumn(telOffice);
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
	                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
	                try {
	                    String groupId = getGroupFilter();
	                    if (groupId == null || "".equals(groupId) || "-1".equals(groupId)) {
	                        list = service.getUsers(generateDaoProperties(), getStart(), getRows(), getSort(), isDesc());
	                    }
	                    else {
	                        list = service.getGroupUsers(groupId, generateDaoProperties(), getStart(), getRows(), getSort(), isDesc());
	                    }
	                }
	                catch (Exception e) {
	                    Log.getLog(UsersPopupTable.class).error(e.toString(), e);
	                }
	                return list;
	            }

	            public int getTotalRowCount() {
	                try {
	                    SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
	                    String groupId = getGroupFilter();
	                    if (groupId == null || "".equals(groupId) || "-1".equals(groupId)) {
	                        return service.getUserCount(generateDaoProperties());
	                    }
	                    else {
	                        return service.getGroupUsersCount(groupId, generateDaoProperties());
	                    }
	                }
	                catch (Exception e) {
	                    Log.getLog(UsersPopupTable.class).error(e.toString(), e);
	                }
	                return 0;
	            }
	            
	            public Forward processAction(Event event, String action, String[] selectedKeys) {
	                try {
	                    if (PopupSelectBox.FORWARD_SELECT.equals(action)) {
	                    	if (getPopupSelectBox() != null && selectedKeys != null && selectedKeys.length > 0) {
	                            getPopupSelectBox().setIds(selectedKeys);
	                            
	                            SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
	                            User user = ss.getUser(selectedKeys[0]);
	                            
	                            //add the user into session and pick up by hierarchyform to populate related info to the fields
	                            event.getRequest().getSession().setAttribute("hierachy_user", user);
	                            
	                            return new Forward(PopupSelectBox.FORWARD_SELECT);
	                        }
	                        else {
	                            return null;
	                        }
	                    }
	                    else {
	                        return null;
	                    }
	                }
	                catch (Exception e) {
	                    Log.getLog(getClass()).error("Error processing action " + action + ": " + e.toString(), e);
	                    return new Forward(PopupSelectBox.FORWARD_ERROR);
	                }
	            }

	            private DaoQuery generateDaoProperties() {
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
