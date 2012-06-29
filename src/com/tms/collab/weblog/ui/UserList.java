package com.tms.collab.weblog.ui;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DaoException;
import com.tms.collab.weblog.model.WeblogModule;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Feb 26, 2004
 * Time: 4:59:01 PM
 * To change this template use Options | File Templates.
 */
public class UserList extends LightWeightWidget
{
    private Collection users;
    public void onRequest(Event event)
    {
        super.onRequest(event);
        WeblogModule wm = (WeblogModule) Application.getInstance().getModule(WeblogModule.class);
        try
        {
            users = wm.getBlogUsers();
        } catch (DaoException e)
        {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
    }

    public String getTemplate()
    {
        return "weblog/userlist";
    }

    public String getDefaultTemplate()
    {
        return "weblog/userlist";
    }

    public Collection getUsers()
    {
        return users;
    }

    public void setUsers(Collection users)
    {
        this.users = users;
    }
}
