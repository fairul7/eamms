package com.tms.cms.personalization;

import kacang.util.DefaultException;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Jun 16, 2003
 * Time: 3:39:56 PM
 * To change this template use Options | File Templates.
 */
public class PersonalizationException extends DefaultException
{
    public PersonalizationException() {}

    public PersonalizationException(String e)
    {
        super(e);
    }

    public PersonalizationException(String s, Exception e)
    {
        super(s, e);
    }
}
