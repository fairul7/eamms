package com.tms.collab.project.ui;

import com.tms.collab.project.WormsHandler;
import com.tms.collab.calendar.ui.CalendarUsersSelectBox;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorParenthesis;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.security.ui.UsersSelectBox;
import kacang.stdui.Table;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ProjectUsersSelectBox extends CalendarUsersSelectBox {

    private String projectId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public ProjectUsersSelectBox() {
        super();
    }

    public ProjectUsersSelectBox(String name) {
        super(name);
    }

    protected Table initPopupTable() {
        return new ProjectUserTable();
    }

    public class ProjectUserTable extends UsersSelectBox.UsersPopupTable {
        public ProjectUserTable() {
            super();
        }

        public ProjectUserTable(String name) {
            super(name);
        }

        public void init() {
            setWidth("100%");
            setModel(new CalendarUserTableModel());
            loadGroups();
        }

        public class CalendarUserTableModel extends UsersSelectBox.UsersPopupTable.UserTableModel {

            int count = 0;

            public CalendarUserTableModel() {
                super();
            }

            public Collection getTableRows() {
                Collection list = new ArrayList();
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                try {
                    // get list of users
                    String groupId = getGroupFilter();
                    if (groupId == null || "".equals(groupId) || "-1".equals(groupId)) {
                        list = service.getUsers(generateDaoProperties(), 0, -1, getSort(), isDesc());
                    }
                    else {
                        list = service.getGroupUsers(groupId, generateDaoProperties(), 0, -1, getSort(), isDesc());
                    }

                    // filter only project users
                    WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                    Collection userList = worms.getProjectInvolvedUsers(projectId);
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
                    Log.getLog(getClass()).error(Application.getInstance().getResourceBundle().getString("project.error.retrieveUsers"), e);
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

        }

    }
}
