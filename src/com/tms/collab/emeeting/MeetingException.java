package com.tms.collab.emeeting;

import kacang.util.DefaultException;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Sep 23, 2003
 * Time: 2:37:16 PM
 * To change this template use Options | File Templates.
 */
public class MeetingException extends DefaultException
{
    public MeetingException()
    {
        super();
    }

    public MeetingException(String message)
    {
        super(message);
    }

    public MeetingException(String message, Exception e)
    {
        super(message, e);
    }
}
