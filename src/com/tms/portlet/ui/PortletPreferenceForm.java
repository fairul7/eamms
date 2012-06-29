package com.tms.portlet.ui;

import kacang.stdui.Form;
import kacang.stdui.TextField;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.Application;
import com.tms.portlet.PortletHandler;
import com.tms.portlet.Portlet;
import com.tms.portlet.PortletPreference;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Nov 13, 2003
 * Time: 1:25:28 PM
 * To change this template use Options | File Templates.
 */
public class PortletPreferenceForm extends Form
{
    public static final String DEFAULT_TEMPLATE = "portal/portletPreferences";
    public static final String FORWARD_CANCEL = "cancel";
    public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_FAILURE = "fail";

    private String portletId;
    private String name;
    private PortletPreference preference;

    private TextField preferenceName;
    private TextField preferenceValue;
    private CheckBox preferenceEditable;
    private Button update;
    private Button delete;
    private Button cancel;
    private ValidatorNotEmpty validName;

    public PortletPreferenceForm()
    {
    }

    public PortletPreferenceForm(String name)
    {
        super(name);
    }

    public void init()
    {
        preference = null;
        if(portletId != null || "".equals(portletId))
        {
            try
            {
                if(name != null || "".equals(name))
                {
                    PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
                    Portlet portlet = handler.getPortlet(portletId);
                    if(portlet.getPortletPreferences().containsKey(name))
                        preference = (PortletPreference) portlet.getPortletPreferences().get(name);
                }
                preferenceName = new TextField("preferenceName");
                preferenceValue = new TextField("preferenceValue");
                preferenceEditable = new CheckBox("preferenceEditable");
                preferenceEditable.setText(Application.getInstance().getMessage("portlet.label.editable","Editable"));
                if(preference != null)
                {
                    preferenceName.setValue(preference.getPreferenceName());
                    preferenceValue.setValue(preference.getPreferenceValue());
                    preferenceEditable.setChecked(preference.isPreferenceEditable());
                }
                update = new Button("update");
                update.setText(Application.getInstance().getMessage("portlet.label.update","Update"));
                if(preference != null)
                {
                    delete =  new Button("delete");
                    delete.setText(Application.getInstance().getMessage("portlet.label.delete","Delete"));
                    addChild(delete);
                }
                else
                    delete = null;
                cancel = new Button("cancel");
                cancel.setText(Application.getInstance().getMessage("portlet.label.cancel","Cancel"));
                validName = new ValidatorNotEmpty("validName");
                preferenceName.addChild(validName);

                addChild(preferenceName);
                addChild(preferenceValue);
                addChild(preferenceEditable);
                addChild(update);
                addChild(cancel);
            }
            catch(Exception e)
            {
                Log.getLog(PortletPreferenceForm.class).error(e);
            }
        }
    }

    public Forward onValidate(Event evt)
    {
        Forward forward = null;
        try
        {
            PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
            Portlet portlet = handler.getPortlet(portletId);
            Map preferences = portlet.getPortletPreferences();
            if(update.getAbsoluteName().equals(findButtonClicked(evt)))
            {
                PortletPreference preference = new PortletPreference();
                preference.setPreferenceName((String) preferenceName.getValue());
                preference.setPreferenceValue((String) preferenceValue.getValue());
                preference.setPreferenceEditable(preferenceEditable.isChecked());
                preferences.put(preferenceName.getValue(), preference);
            }
            else if(delete.getAbsoluteName().equals(findButtonClicked(evt)))
                preferences.remove(preferenceName.getValue());
            portlet.setPortletPreferences(preferences);
            handler.editPortlet(portlet);
            forward = new Forward(FORWARD_SUCCESS);
        }
        catch(Exception e)
        {
            Log.getLog(getClass()).error(e);
            forward = new Forward(FORWARD_FAILURE);
        }
        return forward;
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

    public String getPortletId()
    {
        return portletId;
    }

    public void setPortletId(String portletId)
    {
        this.portletId = portletId;
        init();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public TextField getPreferenceName()
    {
        return preferenceName;
    }

    public void setPreferenceName(TextField preferenceName)
    {
        this.preferenceName = preferenceName;
    }

    public TextField getPreferenceValue()
    {
        return preferenceValue;
    }

    public void setPreferenceValue(TextField preferenceValue)
    {
        this.preferenceValue = preferenceValue;
    }

    public CheckBox getPreferenceEditable()
    {
        return preferenceEditable;
    }

    public void setPreferenceEditable(CheckBox preferenceEditable)
    {
        this.preferenceEditable = preferenceEditable;
    }

    public Button getUpdate()
    {
        return update;
    }

    public void setUpdate(Button update)
    {
        this.update = update;
    }

    public Button getDelete()
    {
        return delete;
    }

    public void setDelete(Button delete)
    {
        this.delete = delete;
    }

    public Button getCancel()
    {
        return cancel;
    }

    public void setCancel(Button cancel)
    {
        this.cancel = cancel;
    }

    public PortletPreference getPreference()
    {
        return preference;
    }

    public void setPreference(PortletPreference preference)
    {
        this.preference = preference;
    }
}
