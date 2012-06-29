package com.tms.crm.sales.ui;

import kacang.services.security.ui.UsersSelectBox;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.Application;
import kacang.ui.Event;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorParenthesis;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorEquals;
import kacang.util.Log;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

/*
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.calendar.ui.UserUtil;
import com.tms.crm.sales.model.AccountManagerModule;
*/
import com.tms.crm.sales.misc.AccessUtil;

public class SfaUsersSelectBox extends UsersSelectBox {

    public SfaUsersSelectBox() {
        super();
    }

    public SfaUsersSelectBox(String name) {
        super(name);
    }

    protected Table initPopupTable() {
        return new SalesUserTable();
    }

    public class SalesUserTable extends PopupSelectBoxTable {
        public SalesUserTable() {
            super();
        }

        public SalesUserTable(String name) {
            super(name);
        }

        public void init() {
            setWidth("100%");
            setModel(new SalesUserTableModel());

          //  loadGroups();
        }

        public void onRequest(Event event) {
           // super.onRequest(event);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public class SalesUserTableModel extends PopupSelectBoxTableModel {

            int count = 0;

            public SalesUserTableModel() {
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

                addFilter(userNameFilter);
            }

            public Collection getTableRows() {
                Collection list = new ArrayList();
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                try {
                    // get list of users
/*
                    String groupId = getGroupFilter();
                    if (groupId == null || "".equals(groupId) || "-1".equals(groupId)) {
*/
                        list = service.getUsers(generateDaoProperties(), 0, -1, getSort(), isDesc());
/*
                    }
                    else {
                        list = service.getGroupUsers(groupId, generateDaoProperties(), 0, -1, getSort(), isDesc());
                    }
*/

                    // filter only calendar users

              //      Collection userList = UserUtil.getUserListByPermission(CalendarModule.PERMISSION_CALENDARING);
                    String userId;
                    for (Iterator i = list.iterator(); i.hasNext();) {
                        User user = (User) i.next();
                        userId = user.getId();
                        if(!(AccessUtil.isExternalSalesPerson(userId)||AccessUtil.isSalesManager(userId)||AccessUtil.isSalesPerson(userId))){
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
                    Log.getLog(UsersPopupTable.class).error("Error retrieving calendar users", e);
                }
                return list;
            }

            public int getTotalRowCount() {
                return count;
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
}
