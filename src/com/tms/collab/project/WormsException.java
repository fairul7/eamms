package com.tms.collab.project;

import kacang.util.DefaultException;

public class WormsException extends DefaultException
{
    public WormsException()
    {
    }

    public WormsException(String s)
    {
        super(s);
    }

    public WormsException(Throwable throwable)
    {
        super(throwable);
    }

    public WormsException(String s, Throwable throwable)
    {
        super(s, throwable);
    }
}
