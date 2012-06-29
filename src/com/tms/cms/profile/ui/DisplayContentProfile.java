package com.tms.cms.profile.ui;

import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.profile.model.ContentProfileModule;
import com.tms.cms.profile.model.ContentProfileField;
import kacang.Application;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Iterator;

import org.apache.commons.collections.SequencedHashMap;

/**
 * Displays the profile data for a specified content object.
 */
public class DisplayContentProfile extends LightWeightWidget {

    private String id;
    private String version;
    private Collection fieldList;

    public DisplayContentProfile() {
    }

    public String getDefaultTemplate() {
        return "/cms/tdk/displayContentProfile";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Collection getFieldList() {
        return fieldList;
    }

    /**
     *
     * @param fieldList A Collection of ContentProfileField objects
     */
    public void setFieldList(Collection fieldList) {
        this.fieldList = fieldList;
    }

    /**
     *
     * @return A Map of name(String)=ContentProfileField object
     */
    public Map getFieldMap() {
        Map fieldMap = new SequencedHashMap();
        if (fieldList != null) {
            for (Iterator i=fieldList.iterator(); i.hasNext();) {
                ContentProfileField field = (ContentProfileField)i.next();
                fieldMap.put(field.getName(), field);
            }
        }
        return fieldMap;
    }

    public void onRequest(Event event) {

        try {
            if (ContentProfileModule.isProfileEnabled()) {
                // get latest version
                Application app = Application.getInstance();
                User user = event.getWidgetManager().getUser();
                ContentPublisher cp = (ContentPublisher)app.getModule(ContentPublisher.class);
                if (getId() != null) {
                    ContentProfileModule cpm = (ContentProfileModule)app.getModule(ContentProfileModule.class);
                    if (getVersion() == null) {
                        ContentObject co = cp.view(getId(), user);
                        fieldList = cpm.getProfileData(co.getId(), co.getVersion());
                    }
                    else {
                        fieldList = cpm.getProfileData(getId(), getVersion());
                    }
                }
                else {
                    fieldList = new ArrayList();
                }
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error loading profile data for " + getId());
            fieldList = new ArrayList();
        }
    }

}
