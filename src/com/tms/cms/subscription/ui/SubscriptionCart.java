package com.tms.cms.subscription.ui;

import com.tms.cms.subscription.SubscriptionException;
import com.tms.cms.subscription.SubscriptionHandler;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.FileParser;
import kacang.util.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: May 21, 2003
 * Time: 3:18:02 PM
 * To change this template use Options | File Templates.
 */
public abstract class SubscriptionCart extends LightWeightWidget
{
    public static final String DEFAULT_TEMPLATE = "cms/subscription/subscriptionCart";
    public static final String ATTRIBUTE_LABEL = "subscriptionCart";
    public static final String DEFAULT_URL = "subscriptionCart.jsp";
    public static final String DEFAULT_FIELD = "subscriptionCartId";

    private Collection subscriptions;
    private String url;
    private String forward;

    public void onRequest(Event evt)
    {
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        User user = service.getCurrentUser(evt.getRequest());
        if(!(user.getId().equals(SecurityService.ANONYMOUS_USER_ID)))
        {
            if(((forward == null) || ("".equals(forward))))
                setForward(FileParser.getInstance().parseFile(evt.getRequest().getRequestURI()));
            Object form = evt.getRequest().getAttribute(ATTRIBUTE_LABEL);
            if((form == null)&&(!(form instanceof SubscriptionCart)))
            {
                if((evt.getRequest().getParameter("action") != null) && ("Unsubscribe".equals(evt.getRequest().getParameter("action"))))
                    processAction(evt.getRequest(), evt.getResponse());
                init(evt.getRequest());
                evt.getRequest().setAttribute(ATTRIBUTE_LABEL, this);
            }
        }
    }

    public void init(HttpServletRequest request)
    {
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        User user = service.getCurrentUser(request);
        SubscriptionHandler handler = (SubscriptionHandler) Application.getInstance().getModule(SubscriptionHandler.class);
        try
        {
            DaoQuery properties = new DaoQuery();
            properties.addProperty(new OperatorEquals("userId", user.getId(), DaoOperator.OPERATOR_AND));
            setSubscriptions(handler.getSubscribers(properties, 0, -1, "dateSubscribed", true));
        }
        catch (SubscriptionException e)
        {
            Log.getLog(SubscriptionCart.class).error(e);
        }
        if((getUrl() == null) || ("".equals(getUrl())))
            setUrl(DEFAULT_URL);
    }

    public void processAction(HttpServletRequest request, HttpServletResponse response)
    {
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        SubscriptionHandler handler = (SubscriptionHandler) Application.getInstance().getModule(SubscriptionHandler.class);
        User user = service.getCurrentUser(request);
        String[] subscriptionId = request.getParameterValues(DEFAULT_FIELD);
        for(int i = 0; i < subscriptionId.length; i++)
        {
            try
            {
                handler.removeSubscriber(subscriptionId[i], user.getId());
                response.sendRedirect(response.encodeRedirectURL(getForward()));
            }
            catch (Exception e)
            {
                Log.getLog(SubscriptionCart.class).error(e);
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

    public String getForward()
    {
        return forward;
    }

    public void setForward(String forward)
    {
        this.forward = forward;
    }
}
