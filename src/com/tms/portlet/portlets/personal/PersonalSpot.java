package com.tms.portlet.portlets.personal;

import com.tms.portlet.Entity;
import com.tms.portlet.PortletPreference;
import com.tms.portlet.theme.ThemeManager;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

public class PersonalSpot extends LightWeightWidget {

    public static final String PREFERENCE_CONTENT = "content";
    public static final String DEFAULT_TEMPLATE = "portal/portlets/personalSpot";

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDefaultTemplate() {
        return DEFAULT_TEMPLATE;
    }

    public void onRequest(Event event) {
        Entity entity = (Entity) event.getRequest().getAttribute(ThemeManager.LABEL_ENTITY);
        PortletPreference preferenceContent = entity.getPreference(PREFERENCE_CONTENT);
        String preferenceValue = "";
        if (preferenceContent != null)
            preferenceValue = preferenceContent.getPreferenceValue();
        setContent(preferenceValue);
    }

}
