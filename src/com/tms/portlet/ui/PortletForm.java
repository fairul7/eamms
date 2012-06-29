package com.tms.portlet.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Nov 11, 2003
 * Time: 5:38:59 PM
 * To change this template use Options | File Templates.
 */
public abstract class PortletForm extends Form
{
    public static final String DEFAULT_TEMPLATE = "portal/portletForm";
    public static final String FORWARD_CANCEL = "cancel";
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_FAILED = "fail";

    protected TextField portletName;
    protected TextField portletTitle;
    protected TextField portletClass;
    protected TextBox portletDescription;
    protected ComboSelectBox themes;
    protected Button submit;
    protected Button cancel;

    protected ValidatorNotEmpty validName;
    protected ValidatorNotEmpty validTitle;
    protected ValidatorNotEmpty validClass;

    public PortletForm()
    {
    }

    public PortletForm(String name)
    {
        super(name);
    }

    public void init()
    {
        removeChildren();
        portletName = new TextField("portletName");
        portletTitle = new TextField("portletTitle");
        portletClass = new TextField("portletClass");
        portletDescription = new TextBox("portletDescription");
        themes = new ComboSelectBox("themes");
        submit = new Button("submit");
        submit.setText(Application.getInstance().getMessage("portlet.label.submit","Submit"));
        cancel = new Button("cancel");
        cancel.setText(Application.getInstance().getMessage("portlet.label.cancel","Cancel"));

        validName = new ValidatorNotEmpty("validName");
        validName.setMessage(Application.getInstance().getMessage("portlet.label.specifyValidPortletName","Please Specify A Valid Portlet Name"));
        validTitle = new ValidatorNotEmpty("validTitle");
        validTitle.setMessage(Application.getInstance().getMessage("portlet.label.specifyValidPortletTitle","Please Specify A Valid Portlet Title"));
        validClass = new ValidatorNotEmpty("validClass");
        validClass.setMessage(Application.getInstance().getMessage("portlet.label.specifyValidPortletClass","Please Specify A Valid Portlet Class"));

        portletName.addChild(validName);
        portletTitle.addChild(validTitle);
        portletClass.addChild(validClass);

        addChild(portletName);
        addChild(portletTitle);
        addChild(portletClass);
        addChild(portletDescription);
        addChild(themes);
        addChild(submit);
        addChild(cancel);

        setMethod("post");
        themes.init();
        initThemes();
    }

    public Forward onSubmit(Event evt)
    {
        if(cancel.getAbsoluteName().equals(findButtonClicked(evt)))
            return new Forward(FORWARD_CANCEL);
        else
            return super.onSubmit(evt);
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public void onRequest(Event evt)
    {
        init();
    }


    protected abstract void initThemes();

    public TextField getPortletName()
    {
        return portletName;
    }

    public void setPortletName(TextField portletName)
    {
        this.portletName = portletName;
    }

    public TextField getPortletTitle()
    {
        return portletTitle;
    }

    public void setPortletTitle(TextField portletTitle)
    {
        this.portletTitle = portletTitle;
    }

    public TextField getPortletClass()
    {
        return portletClass;
    }

    public void setPortletClass(TextField portletClass)
    {
        this.portletClass = portletClass;
    }

    public TextBox getPortletDescription()
    {
        return portletDescription;
    }

    public void setPortletDescription(TextBox portletDescription)
    {
        this.portletDescription = portletDescription;
    }

    public ComboSelectBox getThemes()
    {
        return themes;
    }

    public void setThemes(ComboSelectBox themes)
    {
        this.themes = themes;
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
}
