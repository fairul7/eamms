package com.tms.portlet.theme.themes;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Jan 9, 2004
 * Time: 4:35:27 PM
 * To change this template use Options | File Templates.
 */
public class EkmsSimpleTheme extends DefaultTheme
{
    public static final String DEFAULT_TEMPLATE = "simple/index";
    public static final String DEFAULT_HEADER = "simple/header";
    public static final String DEFAULT_FOOTER = "simple/footer";
    public static final String DEFAULT_STYLESHEET = "simple/simple.css";

    public String getThemeTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public String getThemeHeader()
    {
        return DEFAULT_HEADER;
    }

    public String getThemeFooter()
    {
        return DEFAULT_FOOTER;
    }

    public String getStyleSheets()
    {
        return DEFAULT_STYLESHEET;
    }
}
