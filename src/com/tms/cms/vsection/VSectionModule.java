package com.tms.cms.vsection;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentModule;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.ui.ContentObjectForm;
import com.tms.cms.core.ui.ContentObjectPanel;
import com.tms.cms.core.ui.ContentObjectView;
import com.tms.cms.vsection.ui.VSectionContentObjectForm;
import com.tms.cms.vsection.ui.VSectionContentObjectPanel;
import com.tms.cms.vsection.ui.VSectionContentObjectView;
import kacang.Application;
import kacang.services.security.User;
import kacang.util.Log;

import java.util.*;


/**
 * VSection Module Handler.
 */
public class VSectionModule extends ContentModule {
    public Class[] getContentObjectClasses() {
        return new Class[] { VSection.class };
    }

    public ContentObjectForm getContentObjectForm(Class clazz) {
        return new VSectionContentObjectForm("contentObjectForm");
    }

    public ContentObjectView getContentObjectView(Class clazz) {
        return new VSectionContentObjectView("contentObjectView");
    }

    public ContentObjectPanel getContentObjectPanel(Class clazz) {
        return new VSectionContentObjectPanel("contentObjectPanel");
    }

    public static Collection getContentObjectList(VSection contentObject, User user) {
        try {
            // get objects
            String[] keys = contentObject.getIds();
            ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
            Collection list = cm.viewListWithContents(keys, null, null, null, null, null, null, null, null, Boolean.FALSE, null, null, false, 0, -1, ContentManager.USE_CASE_VIEW, user);

            // sort
            Map tmpMap = new HashMap();
            for (Iterator i=list.iterator(); i.hasNext();) {
                ContentObject co = (ContentObject)i.next();
                tmpMap.put(co.getId(), co);
            }
            Collection results = new ArrayList();
            for (int j=0; j<keys.length; j++) {
                ContentObject co = (ContentObject)tmpMap.get(keys[j]);
                if (co != null) {
                    results.add(co);
                }
            }
            return results;
        }
        catch (ContentException e) {
            Log.getLog(VSectionModule.class).error(e.toString(), e);
            return new ArrayList();
        }
    }

}
