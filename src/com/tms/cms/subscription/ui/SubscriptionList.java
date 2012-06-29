package com.tms.cms.subscription.ui;

import com.tms.cms.subscription.Subscriber;
import com.tms.cms.subscription.Subscription;
import com.tms.cms.subscription.SubscriptionException;
import com.tms.cms.subscription.SubscriptionHandler;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorIn;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.FileParser;
import kacang.util.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: May 21, 2003
 * Time: 5:27:10 PM
 * To change this template use Options | File Templates.
 */
public abstract class SubscriptionList extends LightWeightWidget
{
    public static final String ATTRIBUTE_LABEL = "subscriptionList";
    public static final String DEFAULT_TEMPLATE = "cms/subscription/subscriptionList";
    public static final String DEFAULT_URL = "subscriptionList.jsp";
    public static final String DEFAULT_FIELD = "subscriptionListId";

    private User user;
    private Collection subscriptions;
    private String url;
    private String forward;

    public void onRequest(Event evt)
    {
        Object form = evt.getRequest().getAttribute(ATTRIBUTE_LABEL);
        if((form == null)&&(!(form instanceof SubscriptionCart)))
        {
            if(((forward == null) || ("".equals(forward))))
                setForward(FileParser.getInstance().parseFile(evt.getRequest().getRequestURI()));
            if((evt.getRequest().getParameter("action") != null) && ("Subscribe".equals(evt.getRequest().getParameter("action"))))
                processAction(evt.getRequest(), evt.getResponse());
            init(evt.getRequest());
            evt.getRequest().setAttribute(ATTRIBUTE_LABEL, this);
        }
    }

    public void init(HttpServletRequest request)
    {
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        SubscriptionHandler handler = (SubscriptionHandler) Application.getInstance().getModule(SubscriptionHandler.class);
        setUser(service.getCurrentUser(request));
        try
        {
            Collection subscriptions = handler.getSubscriptionsBySubscriber(service.getCurrentUser(request).getId());
            Collection list = new ArrayList();
            Subscription subscription;
            for(Iterator i = subscriptions.iterator(); i.hasNext();)
            {
                subscription = (Subscription) i.next();
                list.add(subscription.getId());
            }
            DaoQuery properties = new DaoQuery();
            properties.addProperty(new OperatorIn("id", list.toArray(), DaoOperator.OPERATOR_NAN));
            properties.addProperty(new OperatorEquals("state", Subscription.STATE_ACTIVE, DaoOperator.OPERATOR_AND));
            setSubscriptions(handler.getSubscriptions(properties, 0, -1, "name", false));
        }
        catch (SubscriptionException e)
        {
            Log.getLog(getClass()).error(e);
        }
        if((getUrl() == null) || ("".equals(getUrl())))
            setUrl(DEFAULT_URL);
    }

    public void processAction(HttpServletRequest request, HttpServletResponse response)
    {
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        SubscriptionHandler handler = (SubscriptionHandler) Application.getInstance().getModule(SubscriptionHandler.class);
        User user = service.getCurrentUser(request);
        if(!(SecurityService.ANONYMOUS_USER_ID.equals(user.getId())))
        {
            String[] subscriptionId = request.getParameterValues(DEFAULT_FIELD);
            DaoQuery properties;
            Collection list;
            Subscriber subscriber;
            for(int i = 0; i < subscriptionId.length; i++)
            {
                try
                {
                    properties = new DaoQuery();
                    properties.addProperty(new OperatorEquals("subscriptionId", subscriptionId[i], DaoOperator.OPERATOR_AND));
                    properties.addProperty(new OperatorEquals("userId", user.getId(), DaoOperator.OPERATOR_AND));
                    list = handler.getSubscribers(properties, 0, 1, null, false);
                    if(list.size() <= 0)
                    {
                        subscriber = new Subscriber();
                        subscriber.setSubscriptionId(subscriptionId[i]);
                        subscriber.setUserId(user.getId());
                        subscriber.setDateSubscribed(new Date());
                        subscriber.setState(Subscription.STATE_PENDING);
                        handler.addSubscriber(subscriber);
                        response.sendRedirect(response.encodeRedirectURL(getForward()));
                    }
                }
                catch (Exception e)
                {
                    Log.getLog(SubscriptionList.class).error(e);
                }
            }
        }
    }

    public abstract String getDefaultTemplate();

    public Collection getSubscriptions()
    {
        return subscriptions;
    }

    public void setSubscriptions(Collection subscriptions)
    {
        this.subscriptions = subscriptions;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getForward()
    {
        return forward;
    }

    public void setForward(String forward)
    {
        this.forward = forward;
    }
}
