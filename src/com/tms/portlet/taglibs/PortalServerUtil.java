package com.tms.portlet.taglibs;

import kacang.runtime.RenderManager;
import kacang.runtime.taglib.TagUtil;
import kacang.ui.WidgetManager;
import kacang.ui.Widget;
import kacang.util.Log;
import kacang.Application;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletRequest;

import com.tms.portlet.theme.ThemeManager;
import com.tms.portlet.ui.PortalServer;
import com.tms.portlet.PortletHandler;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Jan 9, 2004
 * Time: 11:21:33 AM
 * To change this template use Options | File Templates.
 */
public class PortalServerUtil extends TagSupport
{
    public static final String PROPERTY_HEADER = "header";
    public static final String PROPERTY_FOOTER = "footer";
    public static final String PROPERTY_STYLESHEET = "stylesheet";
    public static final String PROPERTY_IMAGE_PATH = "imagePath";
    public static final String DEFAULT_VAR = "value";

    private String name;
    private String property;
    private String var;

    public int doStartTag() throws JspException
    {
        int result = EVAL_BODY_INCLUDE;
        name = TagUtil.evaluate("name", getName(), String.class, this, pageContext);
        property = TagUtil.evaluate("property", getProperty(), String.class, this, pageContext);
        var = TagUtil.evaluate("var", getVar(), String.class, this, pageContext);
        if(name == null || "".equals(name) || property == null || "".equals(property))
            result = SKIP_BODY;
        return result;
    }

    public int doEndTag() throws JspException
    {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        //Retrieving Widget
        WidgetManager wm = WidgetManager.getWidgetManager(request);
        Widget widget = wm.getWidget(getName());
        if(widget == null)
            return debug("Widget " + getName() + " not found");
        if(!(widget instanceof PortalServer))
            return debug("Widget " + getName() + " is not a Theme Manager");
        if(var == null || "".equals(var))
            var = DEFAULT_VAR;
        //Retrieving Property
        try
        {
            PortalServer portal = (PortalServer) widget;
            ThemeManager manager = portal.getManager();
            //Determine current template
            String file = null;
            if(PROPERTY_STYLESHEET.equals(property))
            {
                file = manager.getStyleSheets();
                String path = request.getContextPath() + Application.getInstance().getProperty(PortletHandler.PROPERTY_IMAGE_PATH);
                file = path + file;
            }
            else if(PROPERTY_IMAGE_PATH.equals(property))
            {
                file = request.getContextPath() + Application.getInstance().getProperty(PortletHandler.PROPERTY_IMAGE_PATH);
            }
            else
            {
                if(PROPERTY_HEADER.equals(property))
                {
                    file = manager.getThemeHeader();
                }
                else if(PROPERTY_FOOTER.equals(property))
                {
                    file = manager.getThemeFooter();
                }
                file = RenderManager.getInstance().generateUrl(pageContext, wm, ThemeManager.DEFAULT_THEME_ROOT + file);
            }
            pageContext.setAttribute(var, file);
        }
        catch (Exception e)
        {
            Log.getLog(PortalServerUtil.class).error(e.getMessage(), e);
        }
        return EVAL_PAGE;
    }

    private int debug(String message)
    {
        Log.getLog(PortalServerUtil.class).debug(message);
        return EVAL_PAGE;
    }

    /* Getters and Setters */
    public String getProperty()
    {
        return property;
    }

    public void setProperty(String property)
    {
        this.property = property;
    }

    public String getVar()
    {
        return var;
    }

    public void setVar(String var)
    {
        this.var = var;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
