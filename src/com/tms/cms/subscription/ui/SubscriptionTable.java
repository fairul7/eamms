package com.tms.cms.subscription.ui;

import com.tms.cms.subscription.Subscription;
import com.tms.cms.subscription.SubscriptionHandler;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorLike;
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

public class SubscriptionTable extends Table
{
    public SubscriptionTable()
    {
        super();
    }

    public SubscriptionTable(String name)
    {
        super(name);
    }

    public void init()
    {
        super.init();
        setWidth("100%");
        setModel(new SubscriptionTableModel());
    }

    public class SubscriptionTableModel extends TableModel
    {
        public SubscriptionTableModel()
        {
            super();
            //Adding Columns
            Application application = Application.getInstance();
            TableColumn columnName = new TableColumn("name", application.getMessage("general.label.name", "Name"));
            columnName.setUrlParam("id");
            addColumn(columnName);
            addColumn(new TableColumn("price", application.getMessage("general.label.price", "Price")));
            addColumn(new TableColumn("months", application.getMessage("general.label.periodMonths", "Period (Months)")));
            TableColumn columnStatus = new TableColumn("state", application.getMessage("general.label.state", "State"));
            Map map = new HashMap();
            map.put(Subscription.STATE_PENDING, application.getMessage("general.label.pending", "Pending"));
            map.put(Subscription.STATE_ACTIVE, application.getMessage("general.label.active", "Active"));
            columnStatus.setFormat(new TableStringFormat(map));
            addColumn(columnStatus);
            //Adding Actions
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            User current = getWidgetManager().getUser();
            try
            {
                if(service.hasPermission(current.getId(), SubscriptionHandler.PERMISSION_SUBSCRIPTION_ADD, null, null))
                    addAction(new TableAction("add", application.getMessage("security.label.newSubscription", "New Subscription")));
                if(service.hasPermission(current.getId(), SubscriptionHandler.PERMISSION_SUBSCRIPTION_DELETE, null, null))
                    addAction(new TableAction("delete", application.getMessage("general.label.delete", "Delete"), application.getMessage("general.label.deleteSelected", "Delete")));
            }
            catch(Exception e)
            {
                Log.getLog(SubscriptionTable.class).error(e.toString(), e);
            }
            //Adding Filters
            TableFilter filterName = new TableFilter("name", application.getMessage("general.label.name", "Name"));
            TableFilter filterActive = new TableFilter("state", application.getMessage("general.label.state", "State"));
            SelectBox selectActive = new SelectBox();
            selectActive.addOption("-1", application.getMessage("general.label.filterState", "Filter State"));
            selectActive.addOption("0", application.getMessage("general.label.viewPending", "View Pending"));
            selectActive.addOption("1", application.getMessage("general.label.viewActive", "View Active"));
            selectActive.setMultiple(false);
            filterActive.setWidget(selectActive);
            addFilter(filterName);
            addFilter(filterActive);
        }

        public Collection getTableRows()
        {
            SubscriptionHandler handler = (SubscriptionHandler) Application.getInstance().getModule(SubscriptionHandler.class);
            Collection list = new ArrayList();
            try
            {
                list = handler.getSubscriptions(generateDaoProperties(), getStart(), getRows(), getSort(), isDesc());
            }
            catch(Exception e)
            {
                Log.getLog(SubscriptionTable.class).error(e.toString(), e);
            }
            return list;
        }

        public int getTotalRowCount()
        {
            SubscriptionHandler handler = (SubscriptionHandler) Application.getInstance().getModule(SubscriptionHandler.class);
            int count = 0;
            try
            {
                count = handler.getSubscriptionCount(generateDaoProperties());
            }
            catch(Exception e)
            {
                Log.getLog(SubscriptionTable.class).error(e.toString(), e);
            }
            return count;
        }

        private DaoQuery generateDaoProperties()
        {
            DaoQuery properties = new DaoQuery();
            properties.addProperty(new OperatorLike("name", getFilterValue("name"), DaoOperator.OPERATOR_AND));
            String stateFilter = "";
            Collection state = (Collection) getFilterValue("state");
            if(state != null)
                if(!(state.isEmpty()))
                    stateFilter = (String) state.iterator().next();
            if(!("".equals(stateFilter) || "-1".equals(stateFilter)))
                properties.addProperty(new OperatorEquals("state", stateFilter, DaoOperator.OPERATOR_AND));
            return properties;
        }

        public String getTableRowKey()
        {
            return "id";
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys)
        {
            if("delete".equals(action))
            {
                SubscriptionHandler handler = (SubscriptionHandler) Application.getInstance().getModule(SubscriptionHandler.class);
                for(int i = 0; i < selectedKeys.length; i++)
                {
                    try
                    {
                        handler.removeSubscription(selectedKeys[i]);
                    }
                    catch(Exception e)
                    {
                        Log.getLog(SubscriptionTable.class).error(e.toString(), e);
                    }
                }
            }
            return super.processAction(evt, action, selectedKeys);
        }
    }
}
