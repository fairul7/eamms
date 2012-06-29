package com.tms.collab.messenger.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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

import com.tms.collab.calendar.ui.UserUtil;

public class MessengerGroupPopOut extends UsersSelectBox 
{

	public static final String PERMISSION_EKMS_USER = "kacang.services.security.ekms.ekmsUser";
	
    public MessengerGroupPopOut() 
    {
        super();
        setSortable(false);
    }

    public MessengerGroupPopOut(String name) 
    {
        super(name);
        setSortable(false);
    }

    protected Table initPopupTable() 
    {
        return new MessengerGroupUserTable();
    }

    public class MessengerGroupUserTable extends UsersSelectBox.UsersPopupTable 
    {
        public MessengerGroupUserTable() {
            super();
        }

        public MessengerGroupUserTable(String name) 
        {
            super(name);
        }

        public void init() 
        {
            setWidth("100%");
            setModel(new MessengerGroupUserTableModel());
            loadGroups();
        }

        public class MessengerGroupUserTableModel extends UsersSelectBox.UsersPopupTable.UserTableModel {

            int count = 0;

            public MessengerGroupUserTableModel() 
            {
                super();
            }

            public Collection getTableRows() 
            {
                Collection list = new ArrayList();
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                try 
                {
                    // get list of users
                    String sort  = getSort();
                    if(sort==null||sort.trim().length()==0)
                        sort = "firstName";
                    String groupId = getGroupFilter();
                    if (groupId == null || "".equals(groupId) || "-1".equals(groupId)) 
                    {
                        list = service.getUsers(generateDaoProperties(), 0, -1, sort, isDesc());
                    }
                    else 
                    {
                        list = service.getGroupUsers(groupId, generateDaoProperties(), 0, -1, sort, isDesc());
                    }

                    // filter only calendar users
                    Collection userList = UserUtil.getUserListByPermission(PERMISSION_EKMS_USER);
                    for (Iterator i = list.iterator(); i.hasNext();) 
                    {
                        User user = (User) i.next();
                        if (!userList.contains(user) || user.getId().equals(getWidgetManager().getUser().getId())) 
                        {
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
                catch (Exception e) 
                {
                    Log.getLog(UsersPopupTable.class).error("Error retrieving calendar users", e);
                }
                return list;
            }

            public int getTotalRowCount()
            {
                return count;
            }

            protected DaoQuery generateDaoProperties() 
            {
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
