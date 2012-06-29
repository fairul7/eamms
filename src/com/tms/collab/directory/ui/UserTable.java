package com.tms.collab.directory.ui;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorParenthesis;
import kacang.services.security.Group;
import kacang.services.security.SecurityService;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class UserTable extends Table {

    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_SEND_MESSAGE = "sendMessage";

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
            Log.getLog(UserTable.class).error("Error loading groups: " + e.toString(), e);
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

    public void setQuery(String query) {
        TextField q = (TextField)getModel().getFilter("query").getWidget();
        q.setValue(query);
        if (query != null) {
            // reset paging
            setCurrentPage(1);
            TableModel mod = getModel();
            if (mod != null) {
                mod.setStart(0);
            }
        }
    }

    public void setSelectedGroup(String groupId) {
        SelectBox sb = (SelectBox) getModel().getFilter("group").getWidget();
        sb.setSelectedOptions(new String[]{groupId});
    }

    public class UserTableModel extends TableModel {
        public UserTableModel() {
            super();

            Application application = Application.getInstance();
            //Adding Columns
            TableColumn nameColumn = new TableColumn("username", application.getMessage("security.label.username", "Username"));
            nameColumn.setUrlParam("id");
            addColumn(nameColumn);
            addColumn(new TableColumn("firstName", application.getMessage("security.label.firstName", "First Name")));
            addColumn(new TableColumn("lastName", application.getMessage("security.label.lastName", "Last Name")));
            addColumn(new TableColumn("email1", application.getMessage("security.label.email", "Email")));
            addColumn(new TableColumn("telOffice", Application.getInstance().getMessage("addressbook.label.phone","Phone")));
            addColumn(new TableColumn("telMobile", Application.getInstance().getMessage("addressbook.label.mobile","Mobile")));

            //Adding Actions
            addAction(new TableAction("sendMessage", Application.getInstance().getMessage("addressbook.label.sendMessage","Send Message")));

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
                }
                else {
                    return service.getGroupUsersCount(groupId, generateDaoProperties());
                }
            }
            catch (Exception e) {
                Log.getLog(UserTable.class).error(e.toString(), e);
            }
            return 0;
        }

        private DaoQuery generateDaoProperties() {
            DaoQuery properties = new DaoQuery();
            OperatorParenthesis op = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
            op.addOperator(new OperatorLike("username", getFilterValue("query"), null));
            op.addOperator(new OperatorLike("firstName", getFilterValue("query"), DaoOperator.OPERATOR_OR));
            op.addOperator(new OperatorLike("lastName", getFilterValue("query"), DaoOperator.OPERATOR_OR));
            op.addOperator(new OperatorLike("email1", getFilterValue("query"), DaoOperator.OPERATOR_OR));
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
            return "username";
        }

        public Forward processAction(Event event, String action, String[] selectedKeys) {
            try {
                if ("sendMessage".equals(action)) {
                    // send message
                    String addresses = Util.getIntranetAddresses(selectedKeys);
                    Util.setMessagingRecipient(event.getRequest(), addresses, Util.RECIPIENT_TO);
                    return new Forward(FORWARD_SEND_MESSAGE);
                }
                else {
                    return null;
                }
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error processing action " + action + ": " + e.toString(), e);
                return new Forward(FORWARD_ERROR);
            }
        }
    }
}
