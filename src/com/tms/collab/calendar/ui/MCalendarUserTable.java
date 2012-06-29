package com.tms.collab.calendar.ui;

import kacang.services.security.ui.UsersSelectBox;
import kacang.services.security.*;
import kacang.services.security.SecurityException;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.stdui.*;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorParenthesis;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorEquals;
import kacang.util.Log;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.calendar.ui.UserUtil;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;

public class MCalendarUserTable extends Table{
    private Map usersMap;
    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_SELECT = "select";
    public static final String FORWARD_SELECTED =  "selected";
    private String atype= "1"; // 1 for compulsory , 2 for optional
    private String eventId = null;
    private int edit = 0;


    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEdit() {
        return  ""+edit;
    }

    public void setEdit(String edit) {
        this.edit = Integer.parseInt(edit);
    }

    public String getAtype() {
        return atype;
    }

    public void setAtype(String atype) {
        this.atype = atype;
    }

    public MCalendarUserTable() {
        super();
    }

    public MCalendarUserTable(String name) {
        super(name);
    }

    public void init() {
        usersMap = new SequencedHashMap();
        setWidth("100%");
        setModel(new CalendarUserTableModel());
        loadGroups();
    }

    public Map getUsersMap() {
        return usersMap;
    }

    public void setUsersMap(Map usersMap) {
        this.usersMap = usersMap;
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
            Log.getLog(UsersSelectBox.UsersPopupTable.class).error("Error loading groups: " + e.toString(), e);
        }
        //Adding Filters
        TableFilter groupFilter = getModel().getFilter("group");
        SelectBox groupSelect = (SelectBox) groupFilter.getWidget();
        groupSelect.setOptionMap(groupMap);
        groupSelect.setMultiple(false);
    }

    public class CalendarUserTableModel extends TableModel{

        int count = 0;

        public CalendarUserTableModel() {
            super();
            Application application = Application.getInstance();
            //Adding Columns
         //   TableColumn nameColumn = new TableColumn("username", application.getMessage("security.label.username", "Username"));
         //   nameColumn.setUrlParam("id");
         //   addColumn(nameColumn);
            addColumn(new TableColumn("firstName", application.getMessage("security.label.firstName", "First Name")));
            addColumn(new TableColumn("lastName", application.getMessage("security.label.lastName", "Last Name")));

            //Adding Actions
            addAction(new TableAction(FORWARD_SELECT,  application.getMessage("general.label.select", "Select")));

            //Adding Filters
            TableFilter userNameFilter = new TableFilter("query");
            TableFilter linebreakFilter = new TableFilter("br");
            TableFilter groupFilter = new TableFilter("group");

            Map groupMap = new SequencedHashMap();
            SelectBox groupSelect = new SelectBox();
            groupSelect.setOptionMap(groupMap);
            groupSelect.setMultiple(false);
            groupFilter.setWidget(groupSelect);
            linebreakFilter.setWidget(new Label("linebreak","<br>"));

            addFilter(userNameFilter);
            addFilter(linebreakFilter);
            addFilter(groupFilter);

        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
            if(action.equals(FORWARD_SELECT)){
               usersMap.clear();     
                for (int i = 0; i < selectedKeys.length; i++) {
                    String userId = selectedKeys[i];
                    try {
                        User user = UserUtil.getUser(userId);
                        usersMap.put( userId, user.getProperty("firstName").toString()+" "+user.getProperty("lastName"));
                    } catch (SecurityException e) {
                        Log.getLog(getClass()).error(e.getMessage(), e);  //To change body of catch statement use Options | File Templates.
                    }

                }

                return new Forward(FORWARD_SELECTED);
            }
            return super.processAction(evt, action, selectedKeys);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public Collection getTableRows() {
            Collection list = new ArrayList();
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            try {
                // get list of users
                String sort  = getSort();
                if(sort==null||sort.trim().length()==0)
                    sort = "firstName";
                String groupId = getGroupFilter();
                if (groupId == null || "".equals(groupId) || "-1".equals(groupId)) {
                    list = service.getUsers(generateDaoProperties(), 0, -1, sort, isDesc());
                }
                else {
                    list = service.getGroupUsers(groupId, generateDaoProperties(), 0, -1, sort, isDesc());
                }

                // filter only calendar users
                Collection userList = UserUtil.getUserListByPermission(CalendarModule.PERMISSION_CALENDARING);
                for (Iterator i = list.iterator(); i.hasNext();) {
                    User user = (User) i.next();
                    if (!userList.contains(user)) {
                        i.remove();
                    }
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
            }
            catch (Exception e) {
                Log.getLog(UsersSelectBox.UsersPopupTable.class).error("Error retrieving calendar users", e);
            }
            return list;
        }

        public int getTotalRowCount() {
            return count;
        }

        public String getGroupFilter() {
            Collection group = (Collection) getFilterValue("group");
            if (group != null)
                if (!(group.isEmpty()))
                    return (String) group.iterator().next();
            return "";
        }

        protected DaoQuery generateDaoProperties() {
            DaoQuery properties = new DaoQuery();
            OperatorParenthesis op = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
            op.addOperator(new OperatorLike("username", getFilterValue("query"), null));
            op.addOperator(new OperatorLike("firstName", getFilterValue("query"), DaoOperator.OPERATOR_OR));
            op.addOperator(new OperatorLike("lastName", getFilterValue("query"), DaoOperator.OPERATOR_OR));
            properties.addProperty(op);
            properties.addProperty(new OperatorEquals("u.active", "1", DaoOperator.OPERATOR_AND));
            return properties;
        }

        public String getTableRowKey() {
            return "id";
        }

    }

}