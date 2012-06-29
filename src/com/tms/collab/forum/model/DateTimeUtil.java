package com.tms.collab.forum.model;

import com.tms.util.FormatUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil
{
    /**
     * Return current datetime string.
     * @return current datetime, pattern: "dd-MM-yyyy HH:mm:ss".
     */
    public static String getDateTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat(FormatUtil.getInstance().getLongDateTimeFormat());
        String dt = sdf.format(new Date());
        return dt;
    }

    /**
     * Return current datetime string.
     * @param pattern format pattern
     * @return current datetime
     */
    public static String getDateTime(String pattern)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String dt = sdf.format(new Date());
        return dt;
    }

    /**
     * Return short format datetime string.
     * @param date java.util.Date
     * @return short format datetime
     */
    public static String shortFmt(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(FormatUtil.getInstance().getLongDateTimeFormat());
        return sdf.format(date);
    }

    public static String format(Date date)
    {
        return format(date, FormatUtil.getInstance().getLongDateTimeFormat());
    }

    public static String format(Date date, String pattern)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * Parse a datetime string.
     * @param param datetime string, pattern: "yyyy-MM-dd HH:mm:ss".
     * @return java.util.Date
     */
    public static Date parse(String param)
    {
        return parse(param, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parse(String param, String pattern)
    {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try
        {
            date = sdf.parse(param);
        }
        catch (ParseException ex)
        {
        }
        return date;
    }

}