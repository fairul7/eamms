package com.tms.cms.subscription;

import kacang.util.DefaultException;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: May 10, 2003
 * Time: 9:35:17 PM
 * To change this template use Options | File Templates.
 */
public class SubscriptionException extends DefaultException
{
    public SubscriptionException() {}

    public SubscriptionException(String s)
    {
        super(s);
    }

    public SubscriptionException(String s, Exception e)
    {
        super(s, e);
    }
}
