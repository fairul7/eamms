package com.tms.util;

import kacang.Application;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: jui
 * Date: Aug 25, 2004
 * Time: 10:03:42 AM
 * To change this template use Options | File Templates.
 */
public class FormatUtil {

    public static final String LONG_DATE = "globalDateLong";
    public static final String SHORT_DATE = "globalDateShort";
    public static final String LONG_DATE_TIME = "globalDatetimeLong";
    public static final String SHORT_DATE_TIME = "globalDatetimeShort";
    public static final String LONG_TIME = "globalTimeLong";
    public static final String SHORT_TIME = "globalTimeShort";
    public static final String CURRENCY = "globalCurrency";
    public static final String DECIMAL = "globalDecimal";

    private static FormatUtil util;

    private FormatUtil() {
    }

    public static FormatUtil getInstance()
    {
        if(util == null)
            util = new FormatUtil();
        return util;
    }

    public String getProperty(String property)
    {
        String app = Application.getInstance().getProperty(property);
        if(app==null || "".equals(app))
        {
            if(LONG_DATE.equals(property) || SHORT_DATE.equals(property) || LONG_DATE_TIME.equals(property) ||
                SHORT_DATE_TIME.equals(property) || LONG_TIME.equals(property) || SHORT_TIME.equals(property))
                app = "dd MMMM yyyy hh:mm a";
            else
                app = "#,###,##0.00";
        }
        return app;
    }

    public String[] getProperties()
    {
        return new String[]{LONG_DATE,SHORT_DATE,LONG_DATE_TIME,SHORT_DATE_TIME,
                            LONG_TIME,SHORT_TIME,CURRENCY,DECIMAL};
    }

    public String getLongDateFormat() {
        return getProperty(LONG_DATE);
    }

    public String getShortDateFormat() {
        return getProperty(SHORT_DATE);
    }

    public String getLongDateTimeFormat() {
        return getProperty(LONG_DATE_TIME);
    }

    public String getShortDateTimeFormat() {
        return getProperty(SHORT_DATE_TIME);
    }

    public String getLongTimeFormat() {
        return getProperty(LONG_TIME);
    }

    public String getShortTimeFormat() {
        return getProperty(SHORT_TIME);
    }

    public String getCurrencyFormat() {
        return getProperty(CURRENCY);
    }

    public String getDecimalFormat() {
        return getProperty(DECIMAL);
    }

    // TODO: need to have separate sdf for each 'property'
    private static SimpleDateFormat sdf = new SimpleDateFormat();

    public SimpleDateFormat getDateFormat(String property)
    {
        if(!(property == null || "".equals(property)))
        {
            String pattern = getProperty(property);
            sdf.applyPattern(pattern);
            return sdf;
        }
        return sdf;
    }

}
