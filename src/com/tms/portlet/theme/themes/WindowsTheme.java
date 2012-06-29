package com.tms.portlet.theme.themes;

public class WindowsTheme extends DefaultTheme
{
	public static final String DEFAULT_TEMPLATE = "windows/index";
    public static final String DEFAULT_HEADER = "windows/header";
    public static final String DEFAULT_FOOTER = "windows/footer";
    public static final String DEFAULT_STYLESHEET = "windows/default.css";

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
