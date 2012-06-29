package com.tms.portlet.ui;

import kacang.stdui.Form;
import kacang.stdui.Button;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import com.tms.portlet.PortletHandler;
import com.tms.portlet.Entity;
import com.tms.portlet.PortletPreference;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: Dec 3, 2003
 * Time: 5:48:44 PM
 * To change this template use Options | File Templates.
 */
public class EntityPreferenceForm extends Form
{
    public static final String DEFAULT_TEMPLATE = "portal/entityPreferences";

    public static final String FORWARD_SUCCESSFUL = "successful";
    public static final String FORWARD_FAILED = "failed";
    public static final String FORWARD_CANCEL = "cancel";

    private String entityId;
    private Button update;
    private Button cancel;

    public void init()
    {
        if(!(entityId == null || "".equals(entityId)))
        {
            PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
            update = null;
            removeChildren();
            try
            {
                boolean editable = false;
                Entity entity = handler.getEntity(entityId);
                for(Iterator i = entity.getPreferences().keySet().iterator(); i.hasNext();)
                {
                    PortletPreference preference = (PortletPreference) entity.getPreferences().get(i.next());
                    if(preference.isPreferenceEditable())
                    {
                        TextField field = new TextField(preference.getPreferenceName());
                        field.setValue(preference.getPreferenceValue());
                        addChild(field);
                        editable = true;
                    }
                }
                if(editable)
                {
                    update = new Button("update");
                    update.setText(Application.getInstance().getMessage("portlet.label.update","Update"));
                    addChild(update);
                }
                cancel = new Button("cancel");
                cancel.setText(Application.getInstance().getMessage("portlet.label.cancel","Cancel"));
                addChild(cancel);
            }
            catch(Exception e)
            {
                Log.getLog(EntityPreferenceForm.class).error(e);
            }
        }
    }

    public Forward onValidate(Event evt)
    {
        Forward forward = new Forward();
        if(!(entityId == null || "".equals(entityId)))
        {
            PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
            try
            {
                Entity entity = handler.getEntity(entityId);
                for(Iterator i = entity.getPreferences().keySet().iterator(); i.hasNext();)
                {
                    PortletPreference preference = (PortletPreference) entity.getPreferences().get(i.next());
                    if(preference.isPreferenceEditable() && childMap.containsKey(preference.getPreferenceName()))
                        preference.setPreferenceValue((String) ((TextField) childMap.get(preference.getPreferenceName())).getValue());
                    handler.editEntity(entity);
                    forward = new Forward(FORWARD_SUCCESSFUL);
                }
            }
            catch(Exception e)
            {
                Log.getLog(EntityPreferenceForm.class).error(e);
                forward = new Forward(FORWARD_FAILED);
            }
        }
        return forward;
    }

    public void onRequest(Event evt)
    {
        init();
    }

    public Forward onSubmit(Event evt)
    {
        if(findButtonClicked(evt).equals(cancel.getAbsoluteName()))
            return new Forward(FORWARD_CANCEL);
        else
            return super.onSubmit(evt);
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    public String getEntityId()
    {
        return entityId;
    }

    public void setEntityId(String entityId)
    {
        this.entityId = entityId;
        init();
    }

    public Button getUpdate()
    {
        return update;
    }

    public void setUpdate(Button update)
    {
        this.update = update;
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
