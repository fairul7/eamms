package com.tms.collab.weblog.ui;

import kacang.stdui.TimeField;
import kacang.ui.Forward;
import kacang.ui.Event;

import java.util.Date;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Feb 27, 2004
 * Time: 12:32:03 PM
 * To change this template use Options | File Templates.
 */
public class BlogTimeField extends TimeField
{
    public static final String SUFFIX_SECOND = "*second";
    public BlogTimeField()
    {
    }

    public BlogTimeField(String s)
    {
        super(s);
    }

    public BlogTimeField(String s, Date date)
    {
        super(s, date);
    }

    public String getDefaultTemplate()
    {
        return "weblog/blogtimefield";
    }

    public String getSecondName(){
        return getAbsoluteName()+ SUFFIX_SECOND ;
    }


    public int getSecond()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate());
        return cal.get(Calendar.SECOND);
    }

    public Forward onSubmit(Event event)
    {
        super.onSubmit(event);
        String secondStr = event.getRequest().getParameter(getSecondName());
        int second  = Integer.parseInt(secondStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate());
        cal.set(Calendar.SECOND,second);
        setDate(cal.getTime());
        performValidation(event);
        return null;
    }


}
