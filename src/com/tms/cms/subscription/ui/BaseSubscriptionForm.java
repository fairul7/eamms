package com.tms.cms.subscription.ui;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.services.security.Group;
import kacang.services.security.SecurityService;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: May 12, 2003
 * Time: 1:45:08 PM
 * To change this template use Options | File Templates.
 */
public class BaseSubscriptionForm extends Form
{
    public static final String DEFAULT_TEMPLATE = "cms/subscription/subscription";

    protected String id;
    protected TextField subscriptionName;
    protected TextBox description;
    protected TextField price;
    protected TextField months;
    protected SelectBox group;
    protected CheckBox subscriptionState;
    protected Button subscriptionButton;
    protected Button cancelButton;
    protected boolean state;

    protected ValidatorNotEmpty validSubscriptionName;
    protected ValidatorIsNumeric validMonths;

    public BaseSubscriptionForm() {}

    public BaseSubscriptionForm(String name)
    {
        super(name);
    }

    public void init()
    {
        removeChildren();

        Application application = Application.getInstance();
        subscriptionName = new TextField("subscriptionName");
        subscriptionName.setSize("30");
        description = new TextBox("description");
        description.setCols("25");
        description.setRows("7");
        price = new TextField("price");
        price.setSize("5");
        months = new TextField("months");
        months.setSize("5");
        group = new SelectBox("group");
        group.setMultiple(false);
        subscriptionButton = new Button("subscriptionButton");
        subscriptionButton.setText(application.getMessage("general.label.save", "Save"));
        cancelButton = new Button("cancelButton");
        cancelButton.setText(application.getMessage("general.label.cancel", "Cancel"));
        subscriptionState = new CheckBox("subscriptionState");

        validSubscriptionName = new ValidatorNotEmpty("validSubscriptionName");
        validMonths = new ValidatorIsNumeric("validMonths");

        addChild(subscriptionName);
        addChild(description);
        addChild(price);
        addChild(months);
        addChild(group);
        addChild(subscriptionState);
        addChild(subscriptionButton);
        addChild(cancelButton);

        subscriptionName.addChild(validSubscriptionName);
        months.addChild(validMonths);

        initGroups();
    }

    public Forward onSubmit(Event evt)
    {
        Forward forward = new Forward();
        if(cancelButton.getAbsoluteName().equals(findButtonClicked(evt)))
            setState(true);
        else
            forward = super.onSubmit(evt);
        return forward;
    }

    protected void initGroups()
    {
        try
        {
            Map map = new SequencedHashMap();
            Group group;
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
            Collection list = service.getGroups(new DaoQuery(), 0, -1, "groupName", false);
            for(Iterator i = list.iterator(); i.hasNext();)
            {
                group = (Group) i.next();
                map.put(group.getId(), group.getGroupName());
            }
            this.group.setOptionMap(map);
        }
        catch(Exception e)
        {
            Log.getLog(BaseSubscriptionForm.class).error(e.toString(), e);
        }
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public TextField getSubscriptionName()
    {
        return subscriptionName;
    }

    public void setSubscriptionName(TextField subscriptionName)
    {
        this.subscriptionName = subscriptionName;
    }

    public TextBox getDescription()
    {
        return description;
    }

    public void setDescription(TextBox description)
    {
        this.description = description;
    }

    public TextField getPrice()
    {
        return price;
    }

    public void setPrice(TextField price)
    {
        this.price = price;
    }

    public TextField getMonths()
    {
        return months;
    }

    public void setMonths(TextField months)
    {
        this.months = months;
    }

    public SelectBox getGroup()
    {
        return group;
    }

    public void setGroup(SelectBox group)
    {
        this.group = group;
    }

    public CheckBox getSubscriptionState()
    {
        return subscriptionState;
    }

    public void setSubscriptionState(CheckBox subscriptionState)
    {
        this.subscriptionState = subscriptionState;
    }

    public Button getSubscriptionButton()
    {
        return subscriptionButton;
    }

    public void setSubscriptionButton(Button subscriptionButton)
    {
        this.subscriptionButton = subscriptionButton;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public Button getCancelButton()
    {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton)
    {
        this.cancelButton = cancelButton;
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }
}
