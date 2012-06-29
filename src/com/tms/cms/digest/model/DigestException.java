package com.tms.cms.digest.model;

import kacang.util.DefaultException;

public class DigestException extends DefaultException
{
    public DigestException()
    {
        super();
    }

    public DigestException(Throwable throwable)
    {
        super(throwable);
    }

    public DigestException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public DigestException(String message)
    {
        super(message);
    }

}
