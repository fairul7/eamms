package com.tms.cms.subscription.ui;

import com.tms.cms.subscription.Subscriber;
import com.tms.cms.subscription.SubscriptionHandler;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: May 14, 2003
 * Time: 10:05:39 PM
 * To change this template use Options | File Templates.
 */
public class SubscriberTable extends Table
{
    private String subscriptionId;

    public SubscriberTable()
    {
        super();
    }

    public SubscriberTable(String name)
    {
        super(name);
    }

    public void init()
    {
        super.init();
        setWidth("100%");
        if(subscriptionId != null)
            if(!("".equals(subscriptionId)))
                setModel(new SubscriberTableModel());
        setSortable(false);
    }

    public String getSubscriptionId()
    {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId)
    {
        this.subscriptionId = subscriptionId;
    }

    public class SubscriberTableModel extends TableModel
    {
        public SubscriberTableModel()
        {
            super();
            //Adding Columns
            Application application = Application.getInstance();
            TableColumn columnName = new TableColumn("username", application.getMessage("general.label.username", "Username"));
            columnName.setUrlParam("userId");
            TableColumn columnSubscribed = new TableColumn("dateSubscribed", application.getMessage("general.label.subscribed", "Subscribed"));
            columnSubscribed.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
            TableColumn columnExpire = new TableColumn("dateExpire", application.getMessage("general.label.expires", "Expires"));
            columnExpire.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()));
            TableColumn columnStatus = new TableColumn("state", application.getMessage("general.label.active", "Active"));
            Map map = new HashMap();
            map.put(Subscriber.STATE_PENDING, application.getMessage("general.label.pending", "Pending"));
            map.put(Subscriber.STATE_ACTIVE, application.getMessage("general.label.active", "Active"));
            map.put(Subscriber.STATE_EXPIRED, application.getMessage("general.label.expired", "Expired"));
            columnStatus.setFormat(new TableStringFormat(map));
            addColumn(columnName);
            addColumn(new TableColumn("firstName", application.getMessage("security.label.firstName", "First Name")));
            addColumn(new TableColumn("lastName", application.getMessage("security.label.lastName", "Last Name")));
            addColumn(columnSubscribed);
            addColumn(columnExpire);
            addColumn(columnStatus);
            //Adding Actions
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            User current = getWidgetManager().getUser();
            try
            {
                if(service.hasPermission(current.getId(), SubscriptionHandler.PERMISSION_SUBSCRIPTION_ADD, null, null))
                    addAction(new TableAction("add", application.getMessage("general.label.add", "Add")));
                if(service.hasPermission(current.getId(), SubscriptionHandler.PERMISSION_SUBSCRIPTION_DELETE, null, null))
                    addAction(new TableAction("delete", application.getMessage("general.label.delete", "Delete"), application.getMessage("general.label.deleteSelected", "Delete")));
            }
            catch(Exception e)
            {
                Log.getLog(SubscriptionTable.class).error(e.toString(), e);
            }
            //Adding Filters
            TableFilter filterActive = new TableFilter("state", application.getMessage("general.label.state", "State"));
            SelectBox selectState = new SelectBox();
            selectState.addOption("-1", application.getMessage("general.label.filterState", "Filter State"));
            selectState.addOption(Subscriber.STATE_PENDING, application.getMessage("general.label.viewPending", "View Pending"));
            selectState.addOption(Subscriber.STATE_ACTIVE, application.getMessage("general.label.viewActive", "View Active"));
            selectState.addOption(Subscriber.STATE_EXPIRED, application.getMessage("general.label.viewExpired", "View Expired"));
            selectState.setMultiple(false);
            filterActive.setWidget(selectState);
            addFilter(filterActive);
        }

        public Collection getTableRows()
        {
            Collection list = new ArrayList();
            try
            {
                SubscriptionHandler handler = (SubscriptionHandler) Application.getInstance().getModule(SubscriptionHandler.class);
                list = handler.getSubscribers(generateDaoProperties(), getStart(), getRows(), "dateSubscribed", true);
            }
            catch(Exception e)
            {
                Log.getLog(SubscriberTable.class).error(e.toString(), e);
            }
            return list;
        }

        public int getTotalRowCount()
        {
            int count = 0;
            try
            {
                SubscriptionHandler handler = (SubscriptionHandler) Application.getInstance().getModule(SubscriptionHandler.class);
                count = handler.getSubscriberCount(generateDaoProperties());
            }
            catch(Exception e)
            {
                Log.getLog(SubscriberTable.class).error(e.toString(), e);
            }
            return count;
        }

        private DaoQuery generateDaoProperties()
        {
            DaoQuery properties = new DaoQuery();
            properties.addProperty(new OperatorEquals("subscriptionId", getSubscriptionId(), DaoOperator.OPERATOR_AND));
            String stateFilter = "";
            Collection state = (Collection) getFilterValue("state");
            if(state != null)
                if(!(state.isEmpty()))
                    stateFilter = (String) state.iterator().next();
            if(!("".equals(stateFilter) || "-1".equals(stateFilter)))
                properties.addProperty(new OperatorEquals("state", stateFilter, DaoOperator.OPERATOR_AND));
            return properties;
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys)
        {
            if("delete".equals(action))
            {
                try
                {
                    SubscriptionHandler handler = (SubscriptionHandler) Application.getInstance().getModule(SubscriptionHandler.class);
                    for(int i = 0; i < selectedKeys.length; i++)
                    {
                        try
                        {
                            handler.removeSubscriber(getSubscriptionId(), selectedKeys[i]);
                        }
                        catch(Exception e)
                        {
                            Log.getLog(SubscriptionTable.class).error(e.toString(), e);
                        }
                    }
                }
                catch(Exception e)
                {
                    Log.getLog(SubscriptionTable.class).error(e.toString(), e);
                }
            }
            return super.processAction(evt, action, selectedKeys);
        }

        public String getTableRowKey()
        {
            return "userId";
        }
    }
}
