package com.tms.portlet.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorIn;
import kacang.model.operator.DaoOperator;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.util.Log;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

import com.tms.portlet.PortletHandler;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Nov 11, 2003
 * Time: 11:56:15 AM
 * To change this template use Options | File Templates.
 */
public abstract class ThemeForm extends Form
{
    public static final String DEFAULT_TEMPLATE = "portal/themeForm";
    public static final String FORWARD_CANCEL = "cancel";
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_FAILED = "fail";

    protected TextField themeName;
    protected TextField themeManagerClass;
    protected TextBox themeDescription;
    protected ComboSelectBox defaultPortlets;
    protected ComboSelectBox groups;
    protected Button submit;
    protected Button cancel;
    protected String message;

    protected ValidatorNotEmpty validThemeName;
    protected ValidatorNotEmpty validManagerClass;

    public ThemeForm()
    {
    }

    public ThemeForm(String name)
    {
        super(name);
    }

    public void init()
    {
        removeChildren();
        themeName = new TextField("themeName");
        themeManagerClass = new TextField("themeManagerClass");
        themeDescription = new TextBox("themeDescription");
        submit = new Button("submit");
        submit.setText(Application.getInstance().getMessage("portlet.label.submit","Submit"));
        cancel = new Button("cancel");
        cancel.setText(Application.getInstance().getMessage("portlet.label.cancel","Cancel"));
        defaultPortlets = new ComboSelectBox("defaultPortlets");
        groups = new ComboSelectBox("groups");

        addChild(themeName);
        addChild(themeManagerClass);
        addChild(themeDescription);
        addChild(submit);
        addChild(cancel);
        addChild(defaultPortlets);
        addChild(groups);

        validThemeName = new ValidatorNotEmpty("validThemeName");
        validThemeName.setMessage(Application.getInstance().getMessage("portlet.label.specifyValidThemeName","Please Specify A Valid Theme Name"));
        validManagerClass = new ValidatorNotEmpty("validManagerClass");
        validManagerClass.setMessage(Application.getInstance().getMessage("portlet.label.specifyValidManagerClass","Please Specify A Valid Manager Class"));
        themeName.addChild(validThemeName);
        themeManagerClass.addChild(validManagerClass);

        setMethod("post");
        defaultPortlets.init();
        initDefaultPortlets();
        groups.init();
        initGroups();
    }

    public Forward onSubmit(Event evt)
    {
        if(cancel.getAbsoluteName().equals(findButtonClicked(evt)))
            return new Forward(FORWARD_CANCEL);
        else
            return super.onSubmit(evt);
    }

    public void onRequest(Event evt)
    {
        init();
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    protected Collection retrieveDefaultPortlets()
    {
        Collection portletId = new ArrayList();
        Collection portlets = new ArrayList();
        PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
        for(Iterator i = defaultPortlets.getRightValues().keySet().iterator(); i.hasNext();)
            portletId.add(i.next());
        if(portletId.size() > 0)
        {
            try
            {
                DaoQuery portletQuery = new DaoQuery();
                portletQuery.addProperty(new OperatorIn("portletId", portletId.toArray(), DaoOperator.OPERATOR_AND));
                portlets = handler.getPortlets(portletQuery, 0, -1, null, false, false);
            }
            catch(Exception e)
            {
                Log.getLog(ThemeForm.class).error(e);
            }
        }
        return portlets;
    }

    protected Collection retrieveGroups()
    {
        Collection groupId = new ArrayList();
        Collection groups = new ArrayList();
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        for(Iterator i = this.groups.getRightValues().keySet().iterator(); i.hasNext();)
            groupId.add(i.next());
        if(groupId.size() > 0)
        {
            try
            {
                DaoQuery groupQuery = new DaoQuery();
                groupQuery.addProperty(new OperatorIn("id", groupId.toArray(), DaoOperator.OPERATOR_AND));
                groups = service.getGroups(groupQuery, 0, -1, null, false);
            }
            catch(Exception e)
            {
                Log.getLog(ThemeForm.class).error(e);
            }
        }
        return groups;
    }

    public abstract void initDefaultPortlets();

    public abstract void initGroups();

    public TextField getThemeName()
    {
        return themeName;
    }

    public void setThemeName(TextField themeName)
    {
        this.themeName = themeName;
    }

    public TextField getThemeManagerClass()
    {
        return themeManagerClass;
    }

    public void setThemeManagerClass(TextField themeManagerClass)
    {
        this.themeManagerClass = themeManagerClass;
    }

    public TextBox getThemeDescription()
    {
        return themeDescription;
    }

    public void setThemeDescription(TextBox themeDescription)
    {
        this.themeDescription = themeDescription;
    }

    public Button getSubmit()
    {
        return submit;
    }

    public void setSubmit(Button submit)
    {
        this.submit = submit;
    }

    public Button getCancel()
    {
        return cancel;
    }

    public void setCancel(Button cancel)
    {
        this.cancel = cancel;
    }

    public ComboSelectBox getDefaultPortlets()
    {
        return defaultPortlets;
    }

    public void setDefaultPortlets(ComboSelectBox defaultPortlets)
    {
        this.defaultPortlets = defaultPortlets;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public ComboSelectBox getGroups()
    {
        return groups;
    }

    public void setGroups(ComboSelectBox groups)
    {
        this.groups = groups;
    }
}
