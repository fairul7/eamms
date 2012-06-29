package com.tms.portlet;

import kacang.util.DefaultException;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Oct 17, 2003
 * Time: 2:42:11 PM
 * To change this template use Options | File Templates.
 */
public class PortletException extends DefaultException
{
    public PortletException()
    {
    }

    public PortletException(String s)
    {
        super(s);
    }

    public PortletException(Throwable throwable)
    {
        super(throwable);
    }

    public PortletException(String s, Throwable throwable)
    {
        super(s, throwable);
    }
}
