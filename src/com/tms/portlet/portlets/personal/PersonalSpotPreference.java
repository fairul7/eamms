package com.tms.portlet.portlets.personal;

import com.tms.portlet.Entity;
import com.tms.portlet.PortletException;
import com.tms.portlet.PortletHandler;
import com.tms.portlet.PortletPreference;
import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.RichTextBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class PersonalSpotPreference extends Form {
    public static final String FORWARD_SUCCESSFUL = "forward_successful";
    public static final String FORWARD_FAILED = "forward_failed";
    public static final String FORWARD_CANCEL = "forward_cancel";
    public static final String DEFAULT_TEMPLATE = "portal/portlets/personalSpotPreference";

    private Entity entity;
    private String entityId;

    private RichTextBox textBox;
    private Button submit;
    private Button cancel;

    public PersonalSpotPreference() {
    }

    public PersonalSpotPreference(String s) {
        super(s);
    }

    public void init() {
        setMethod("POST");
        textBox = new RichTextBox("textBox");
        submit = new Button("submit");
        submit.setText(Application.getInstance().getMessage("portlet.label.save","Save"));
        cancel = new Button("cancel");
        cancel.setText(Application.getInstance().getMessage("portlet.label.cancel","Cancel"));

        addChild(textBox);
        addChild(submit);
        addChild(cancel);

        refresh();
    }

    private void refresh() {
        if (entity != null) {
            PortletPreference preferenceContent = entity.getPreference(PersonalSpot.PREFERENCE_CONTENT);
            String preferenceValue = "";
            if (preferenceContent != null)
                preferenceValue = preferenceContent.getPreferenceValue();
            textBox.setValue(preferenceValue);
        }
    }

    public void onRequest(Event event) {
        refresh();
    }

    public Forward onValidate(Event event) {
        Forward forward = null;
        if (entity != null) {
            String content = (String) textBox.getValue();
            forward = savePreference(content);
        }
        return forward;
    }

    private Forward savePreference(String content) {
        Forward forward = null;
        PortletPreference preferenceContent = null;
        if (entity.getPreference(PersonalSpot.PREFERENCE_CONTENT) == null) {
            preferenceContent = new PortletPreference();
            preferenceContent.setPreferenceName(PersonalSpot.PREFERENCE_CONTENT);
            preferenceContent.setPreferenceEditable(true);
            preferenceContent.setPreferenceValue("");
        }
        else
            preferenceContent = entity.getPreference(PersonalSpot.PREFERENCE_CONTENT);
        preferenceContent.setPreferenceValue(content);
        entity.getPreferences().put(PersonalSpot.PREFERENCE_CONTENT, preferenceContent);
        PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
        try {
            handler.editEntity(entity);
            forward = new Forward(FORWARD_SUCCESSFUL);
        }
        catch (PortletException e) {
            Log.getLog(PersonalSpotPreference.class).error(e);
            forward = new Forward(FORWARD_FAILED);
        }
        return forward;
    }

    public String getDefaultTemplate() {
        return DEFAULT_TEMPLATE;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
        PortletHandler handler = (PortletHandler) Application.getInstance().getModule(PortletHandler.class);
        try {
            entity = handler.getEntity(entityId);
        }
        catch (PortletException e) {
            Log.getLog(PersonalSpotPreference.class).error(e.getMessage(), e);
        }
    }

}
