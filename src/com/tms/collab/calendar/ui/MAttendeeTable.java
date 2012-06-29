package com.tms.collab.calendar.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.ui.Widget;
import kacang.model.DaoQuery;
import kacang.services.security.*;
import kacang.services.security.SecurityException;

import java.util.*;

import com.tms.collab.emeeting.ui.MMeetingForm;
import com.tms.collab.calendar.model.CalendarModule;

public class MAttendeeTable extends Table
{
    public static final String DEFAULT_TEMPLATE = "calendar/attendeeTable";
    public static final String USER_KEY = "uid";

    private MAttendeeTableModel model;
    private String callingWidget;

    public MAttendeeTable()
    {
        super();
    }

    public MAttendeeTable(String name)
    {
        super(name);
    }

    public Forward actionPerformed(Event evt)
    {
        Forward forward = super.actionPerformed(evt);
        if(PARAMETER_KEY_SELECTION.equals(evt.getType()))
        {
            String uid = evt.getParameter(USER_KEY);
            if(!(uid == null || "".equals(uid)))
                model.removeAttendee(uid);
        }
        refreshCaller();
        return forward;
    }

    public Forward onValidate(Event evt)
    {
        Forward forward = super.onValidate(evt);
        refreshCaller();
        return forward;
    }

    public void init()
    {
        super.init();
        setWidth("100%");
        model = new MAttendeeTableModel();
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
        if(widget != null && (widget instanceof MCalendarForm || widget instanceof MMeetingForm))
        {
            if(widget instanceof MCalendarForm)
            {
                MCalendarForm form = (MCalendarForm) widget;
                setAttendeeMap(form.getAttendeeMap());
                setCompulsoryMap(form.getCompulsoryMap());
            }
            else
            {
                MMeetingForm form = (MMeetingForm) widget;
                setAttendeeMap(form.getAttendeeMap());
                setCompulsoryMap(form.getCompulsoryMap());
            }
        }
    }

    private void refreshCaller()
    {
        if(!(callingWidget == null || "".equals(callingWidget)))
        {
            Widget widget = getWidgetManager().getWidget(callingWidget);
            if(widget != null && (widget instanceof MCalendarForm || widget instanceof MMeetingForm))
            {
                if(widget instanceof MCalendarForm)
                {
                    MCalendarForm form = (MCalendarForm) widget;
                    form.setAttendeeMap(getAttendeeMap());
                    form.setCompulsoryMap(getCompulsoryMap());
                }
                else
                {
                    MMeetingForm form = (MMeetingForm) widget;
                    form.setAttendeeMap(getAttendeeMap());
                    form.setCompulsoryMap(getCompulsoryMap());
                }
            }
        }
    }

    public void setAttendeeMap(Map map)
    {
        model.setAttendeeMap(map);
    }

    public Map getAttendeeMap()
    {
        return model.getAttendeeMap();
    }

    public void setCompulsoryMap(Map map)
    {
        model.setCompulsoryMap(map);
    }

    public Map getCompulsoryMap()
    {
        return model.getCompulsoryMap();
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public class MAttendeeTableModel extends TableModel
    {
        private Map attendeeMap;
        private Map compulsoryMap;
        private int count = 0;

        public MAttendeeTableModel()
        {
            attendeeMap = new HashMap();
            compulsoryMap = new HashMap();
            addColumn(new TableColumn("name", Application.getInstance().getMessage("security.label.name")));
            addAction(new TableAction("addAttendee", Application.getInstance().getMessage("calendar.label.optional")));
            addAction(new TableAction("addCompulsory", Application.getInstance().getMessage("emeeting.label.compulsory")));
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
            catch (SecurityException e)
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

        public int getTotalRowCount()
        {
            return count;
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
            if("addAttendee".equals(action))
            {
                for(int i = 0; i < selectedKeys.length; i++)
                {
                    String id = selectedKeys[i];
                    if(!(compulsoryMap.containsKey(id)))
                    {
                        try
                        {
                            attendeeMap.put(id, service.getUser(id));
                        }
                        catch (SecurityException e) {}
                    }
                }
            }
            else if("addCompulsory".equals(action))
            {
                for(int i = 0; i < selectedKeys.length; i++)
                {
                    String id = selectedKeys[i];
                    if(!(attendeeMap.containsKey(id)))
                    {
                        try
                        {
                            compulsoryMap.put(id, service.getUser(id));
                        }
                        catch (SecurityException e) {}
                    }
                }
            }
            return super.processAction(evt, action, selectedKeys);
        }

        public String getTableRowKey()
        {
            return "id";
        }

        public Map getAttendeeMap()
        {
            return attendeeMap;
        }

        public void setAttendeeMap(Map attendeeMap)
        {
            this.attendeeMap = attendeeMap;
        }

        public Map getCompulsoryMap()
        {
            return compulsoryMap;
        }

        public void setCompulsoryMap(Map compulsoryMap)
        {
            this.compulsoryMap = compulsoryMap;
        }

        private void removeAttendee(String key)
        {
            attendeeMap.remove(key);
            compulsoryMap.remove(key);
        }
    }
}
