package com.tms.cms.tdk;

import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.vsection.VSection;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;

public class DisplayVSection extends LightWeightWidget {

    private VSection contentObject;
    private String id;
	private boolean noHeader = false;	

    public VSection getContentObject() {
        return contentObject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
	
	public boolean isNoHeader() {
		return this.noHeader;
	}

	public void setNoHeader(boolean noHeader) {
		this.noHeader = noHeader;
	}

    public void onRequest(Event evt) {
        // get id
//        String key = evt.getRequest().getParameter("id");
        String key = getId();
        if (key == null)
            key = evt.getRequest().getParameter("id");

        // retrieve content object
        if (key != null && key.trim().length() > 0) {
            try {
                User user = ((SecurityService)Application.getInstance().getService(SecurityService.class)).getUser(SecurityService.ANONYMOUS_USER_ID);
                Application application = Application.getInstance();
                ContentPublisher cp = (ContentPublisher)application.getModule(ContentPublisher.class);
                this.contentObject = (VSection)cp.view(key, user);
            }
            catch(DataObjectNotFoundException e) {
                Log.getLog(getClass()).debug("VSection " + key + " not found");
                // ignore
            }
            catch(Exception e) {
                throw new RuntimeException(e.toString());
            }
        }
    }

    public String getDefaultTemplate() {
        if (contentObject == null)
            return "cms/tdk/displayContentNotFound";
        else
            return "cms/tdk/displayVSection";
    }

    public Collection getContentObjectList() {
        try {
            if (contentObject != null) {
                return contentObject.getContentObjectList();
            }
            else {
                return new ArrayList();
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
            return new ArrayList();
        }
    }

}
