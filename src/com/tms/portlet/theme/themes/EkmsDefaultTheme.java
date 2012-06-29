package com.tms.portlet.theme.themes;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Jan 9, 2004
 * Time: 9:54:38 AM
 * To change this template use Options | File Templates.
 */
public class EkmsDefaultTheme extends DefaultTheme
{
    public static final String DEFAULT_TEMPLATE = "ekms/index";
    public static final String DEFAULT_HEADER = "ekms/header";
    public static final String DEFAULT_FOOTER = "ekms/footer";
    public static final String DEFAULT_STYLESHEET = "ekms/style.css";

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
