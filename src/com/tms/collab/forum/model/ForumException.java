package com.tms.collab.forum.model;

import kacang.util.DefaultException;

/**
 * Created by IntelliJ IDEA.
 * User: WaiHung
 * Date: May 5, 2003
 * Time: 3:07:17 PM
 * To change this template use Options | File Templates.
 */
public class ForumException extends DefaultException
{
    public ForumException()
    {
        super();
    }

    public ForumException(Throwable throwable)
    {
        super(throwable);
    }

    public ForumException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public ForumException(String message)
    {
        super(message);
    }

}
