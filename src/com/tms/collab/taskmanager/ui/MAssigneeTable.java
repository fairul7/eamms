package com.tms.collab.taskmanager.ui;

import kacang.stdui.*;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.ui.Widget;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.services.security.*;
import kacang.services.security.SecurityException;

import java.util.*;

import com.tms.collab.calendar.ui.UserUtil;
import com.tms.collab.calendar.model.CalendarModule;

public class MAssigneeTable extends Table
{
    public static final String DEFAULT_TEMPLATE = "taskmanager/assigneeTable";
    public static final String USER_KEY = "uid";

    protected MAssigneeTableModel model;
    private String callingWidget;

    public MAssigneeTable()
    {
        super();
    }

    public MAssigneeTable(String name)
    {
        super(name);
    }

    public void init()
    {
        super.init();
        setWidth("100%");
        model = new MAssigneeTableModel();
        setModel(model);
        setNumbering(false);
        setShowPageSize(false);
    }

    public String getCallingWidget()
    {
        return callingWidget;
    }

    public void setCallingWidget(String callingWidget)
    {
        this.callingWidget = callingWidget;
        Widget widget = getWidgetManager().getWidget(callingWidget);
        if(widget != null && widget instanceof MTaskForm)
        {
            MTaskForm form = (MTaskForm) widget;
            setAssigneeMap(form.getAssigneeMap());
        }
    }

    public Forward actionPerformed(Event evt)
    {
        Forward forward = super.actionPerformed(evt);
        if(PARAMETER_KEY_SELECTION.equals(evt.getType()))
        {
            String uid = evt.getParameter(USER_KEY);
            if(!(uid == null || "".equals(uid)))
                model.removeAssignee(uid);
        }
        refreshCaller();
        return forward;
    }

    private void refreshCaller()
    {
        if(!(callingWidget == null || "".equals(callingWidget)))
        {
            Widget widget = getWidgetManager().getWidget(callingWidget);
            if(widget != null && widget instanceof MTaskForm)
            {
                MTaskForm form = (MTaskForm) widget;
                form.setAssigneeMap(getAssigneeMap());
            }
        }
    }

    public void setAssigneeMap(Map map)
    {
        model.setAssigneeMap(map);
    }

    public Map getAssigneeMap()
    {
        return model.getAssigneeMap();
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public class MAssigneeTableModel extends TableModel
    {
        private Map assigneeMap;
        private int count = 0;

        public MAssigneeTableModel()
        {
            assigneeMap = new HashMap();
            addColumn(new TableColumn("name", Application.getInstance().getMessage("security.label.name")));
            addAction(new TableAction("addAssignee", Application.getInstance().getMessage("emeeting.label.assignees")));
            TableFilter filterGroup = new TableFilter("filterGroup");
            SelectBox groupSelect = new SelectBox("group");
            groupSelect.addOption("-1", Application.getInstance().getMessage("security.label.filterGroup"));
            groupSelect.setSelectedOption("-1");
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            try
            {
                Collection groups = service.getGroups(new DaoQuery(), 0, -1, "groupName", false);
                for (Iterator i = groups.iterator(); i.hasNext();)
                {
                    Group group = (Group) i.next();
                    groupSelect.addOption(group.getId(), group.getName());
                }
            }
            catch (kacang.services.security.SecurityException e)
            {
            }
            filterGroup.setWidget(groupSelect);
            addFilter(filterGroup);
        }

        public Collection getTableRows()
        {
            Collection list = new ArrayList();
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            String groupId = getGroupId();
            try
            {
                if (groupId == null || "".equals(groupId) || "-1".equals(groupId))
                    list = service.getUsers(new DaoQuery(), 0, -1, "firstName", isDesc());
                else
                    list = service.getGroupUsers(groupId, new DaoQuery(), getStart(), getRows(), "firstName", isDesc());
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
            catch (SecurityException e)
            {
            }
            return list;
        }

        private String getGroupId()
        {
            Map map = ((SelectBox)getFilter("filterGroup").getWidget()).getSelectedOptions();
            if(map.size() > 0)
                return (String) map.keySet().iterator().next();
            return "-1";
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys)
        {
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            for(int i = 0; i < selectedKeys.length; i++)
            {
                String id = selectedKeys[i];
                try
                {
                    assigneeMap.put(id, service.getUser(id));
                }
                catch (SecurityException e) {}
            }
            return super.processAction(evt, action, selectedKeys);
        }

        public int getTotalRowCount()
        {
            return count;
        }

        public String getTableRowKey()
        {
            return "id";
        }

        public Map getAssigneeMap()
        {
            return assigneeMap;
        }

        public void setAssigneeMap(Map assigneeMap)
        {
            this.assigneeMap = assigneeMap;
        }

        public void removeAssignee(String id)
        {
            assigneeMap.remove(id);
        }
    }
}
