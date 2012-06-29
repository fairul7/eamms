package kacang.services.security.ui;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorParenthesis;
import kacang.services.security.*;
import kacang.services.security.SecurityException;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

public class UserTable extends Table {
    public UserTable() {
    }

    public UserTable(String name) {
        super(name);
    }

    public void init() {
        super.init();
        setWidth("100%");
        setModel(new UserTableModel());
        loadGroups();
    }

    protected void loadGroups() {
        Application application = Application.getInstance();
        SecurityService service = (SecurityService) application.getService(SecurityService.class);
        Map groupMap = new SequencedHashMap();
        try {
            Collection list = service.getGroups(new DaoQuery(), 0, -1, "groupName", false);
            groupMap.put("-1", application.getMessage("security.label.filterGroup", "Filter Group"));
            for (Iterator i = list.iterator(); i.hasNext();) {
                Group group = (Group) i.next();
                groupMap.put(group.getId(), group.getName());
            }
        } catch (Exception e) {
            Log.getLog(UserTable.class).error("Error loading groups: " + e.toString(), e);
        }
        //Adding Filters
        TableFilter groupFilter = getModel().getFilter("group");
        SelectBox groupSelect = (SelectBox)groupFilter.getWidget();
        groupSelect.setOptionMap(groupMap);
        groupSelect.setMultiple(false);
    }

    public void onRequest(Event evt) {
        loadGroups();
    }

    public class UserTableModel extends TableModel {
        public UserTableModel() {
            super();

            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            User current = getWidgetManager().getUser();
            Application application = Application.getInstance();
            //Adding Columns
            TableColumn groupNameColumn = new TableColumn("username", application.getMessage("security.label.username", "Username"));
            try
            {
                if (service.hasPermission(current.getId(), SecurityService.PERMISSION_USER_EDIT, null, null))
                    groupNameColumn.setUrlParam("id");
            }
            catch (SecurityException e)
            {
                Log.getLog(UserTable.class).error(e.getMessage(), e);
            }
            addColumn(groupNameColumn);
            addColumn(new TableColumn("firstName", application.getMessage("security.label.firstName", "First Name")));
            addColumn(new TableColumn("lastName", application.getMessage("security.label.lastName", "Last Name")));
            addColumn(new TableColumn("email1", application.getMessage("security.label.email", "Email")));
            addColumn(new TableColumn("property1", application.getMessage("fms.label.staffID", "Staff ID")));

            String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
            TableColumn statusColumn = new TableColumn("active", application.getMessage("security.label.active", "Active"));
            statusColumn.setFormat(booleanFormat);
            addColumn(statusColumn);

            //Adding Actions
            try {
                if (service.hasPermission(current.getId(), SecurityService.PERMISSION_USER_EDIT, null, null))
                    addAction(new TableAction("activate", application.getMessage("security.label.activate", "Activate")));
                if (service.hasPermission(current.getId(), SecurityService.PERMISSION_USER_ADD, null, null))
                    addAction(new TableAction("add", application.getMessage("security.label.newUser", "New User")));
                if (service.hasPermission(current.getId(), SecurityService.PERMISSION_USER_DELETE, null, null))
                    addAction(new TableAction("delete", application.getMessage("security.label.delete", "Delete"), application.getMessage("security.message.deleteUser", "Are You Sure You Want To Delete These User(s) ?")));
                //Collection list = service.getGroups(new DaoQuery(), 0, -1, "groupName", false);
            } catch (Exception e) {
                Log.getLog(UserTable.class).error("Error loading groups: " + e.toString(), e);
            }
            //Adding Filters
/*
            TableFilter userNameFilter = new TableFilter("username", "Username");
            TableFilter groupFilter = new TableFilter("group", "Group");
            TableFilter stateFilter = new TableFilter("active", "Active");
*/
            TableFilter userNameFilter = new TableFilter("username");
            TableFilter groupFilter = new TableFilter("group");
            TableFilter stateFilter = new TableFilter("active");

            Map groupMap = new SequencedHashMap();
            SelectBox groupSelect = new SelectBox();
            groupSelect.setOptionMap(groupMap);
            groupSelect.setMultiple(false);
            groupFilter.setWidget(groupSelect);

            SelectBox stateSelect = new SelectBox("selectActive");
            stateSelect.addOption("-1", application.getMessage("security.label.filterStatus", "Filter Status"));
            stateSelect.addOption(DefaultPrincipal.STATE_ACTIVE, application.getMessage("security.label.active", "Active"));
            stateSelect.addOption(DefaultPrincipal.STATE_PENDING, application.getMessage("security.label.pending", "Pending"));
            stateSelect.setMultiple(false);
            stateFilter.setWidget(stateSelect);

            addFilter(userNameFilter);
            addFilter(groupFilter);
            addFilter(stateFilter);
        }

        public Collection getTableRows() {
            Collection list = new ArrayList();
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            try {
                String groupId = getGroupFilter();
                if (groupId == null || "".equals(groupId) || "-1".equals(groupId)) {
                    list = service.getUsers(generateDaoProperties(), getStart(), getRows(), getSort(), isDesc());
                } else {
                    list = service.getGroupUsers(groupId, generateDaoProperties(), getStart(), getRows(), getSort(), isDesc());
                }
            } catch (Exception e) {
                Log.getLog(UserTable.class).error(e.toString(), e);
            }
            return list;
        }

        public int getTotalRowCount() {
            try {
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                String groupId = getGroupFilter();
                if (groupId == null || "".equals(groupId) || "-1".equals(groupId)) {
                    return service.getUserCount(generateDaoProperties());
                } else {
                    return service.getGroupUsersCount(groupId, generateDaoProperties());
                }
            } catch (Exception e) {
                Log.getLog(UserTable.class).error(e.toString(), e);
            }
            return 0;
        }

        private DaoQuery generateDaoProperties() {
            Collection list;
            DaoQuery properties = new DaoQuery();
            OperatorParenthesis op = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
            op.addOperator(new OperatorLike("username", getFilterValue("username"), null));
            op.addOperator(new OperatorLike("firstName", getFilterValue("username"), DaoOperator.OPERATOR_OR));
            op.addOperator(new OperatorLike("lastName", getFilterValue("username"), DaoOperator.OPERATOR_OR));
            op.addOperator(new OperatorLike("email1", getFilterValue("username"), DaoOperator.OPERATOR_OR));
            op.addOperator(new OperatorLike("property1", getFilterValue("username"), DaoOperator.OPERATOR_OR));
            properties.addProperty(op);
            list = (Collection) getFilterValue("active");
            String active = "";
            if (list != null)
                if (!(list.isEmpty()))
                    active = (String) list.iterator().next();
            if (!("".equals(active) || "-1".equals(active)))
                properties.addProperty(new OperatorEquals("u.active", active, DaoOperator.OPERATOR_AND));

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

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if ("delete".equals(action)) {
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                for (int i = 0; i < selectedKeys.length; i++) {
                    try {
                        User user = service.getUser(selectedKeys[i]);
                        service.removeUser(selectedKeys[i]);

                        //-- Logging transaction
                        User currentUser = getWidgetManager().getUser();
                        Log.getLog(getClass()).info("User [id:" + user.getId() + ",username:" + user.getUsername() + ",name:" + user.getName() + "] deleted by [" + currentUser.getUsername() + "] on " + new Date());
						Log.getLog(getClass()).write(new Date(), user.getId(), currentUser.getId(), "Security: User deleted",
							"User " + user.getName() + " with ID " + user.getId() + " deleted successfully", evt.getRequest().getRemoteAddr(),
							evt.getRequest().getSession().getId());

                    } catch (Exception e) {
                        Log.getLog(UserTable.class).error("Error deleting user id " + selectedKeys[i], e);
                    }
                }
            }
            else if("activate".equals(action))
            {
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                for (int i = 0; i < selectedKeys.length; i++) {
                    try {
                        service.setUserStatus(selectedKeys[i], "1");
                    } catch (Exception e) {
                        Log.getLog(UserTable.class).error("Error activating user id " + selectedKeys[i], e);
                    }
                }
            }
            return super.processAction(evt, action, selectedKeys);
        }
    }
}
