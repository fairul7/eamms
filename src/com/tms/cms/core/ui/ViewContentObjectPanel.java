package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentModule;
import com.tms.cms.core.model.ContentObject;
import kacang.Application;
import kacang.stdui.Panel;
import kacang.ui.Event;
import kacang.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 21, 2003
 * Time: 4:44:04 PM
 * To change this template use Options | File Templates.
 */
public class ViewContentObjectPanel extends Panel {

    private String id;
    private ContentObjectPanel contentObjectPanel;
    
    protected boolean isDeleted = false;

    public ViewContentObjectPanel() {
    }

    public ViewContentObjectPanel(String name) {
        super(name);
    }

    public void init() {
        setId(ContentManager.CONTENT_TREE_ROOT_ID);
    }

    public void onRequest(Event evt) {
        try {
            ContentObject contentObject = ContentHelper.getContentObject(evt, getId());
            if (contentObject != null) {
            	if (contentObject.isDeleted()){
            		isDeleted = true;
            	}else{
            		isDeleted = false;
            	}
                setId(contentObject.getId());
                // retrieve specific class
                ContentModule module = (ContentModule)Application.getInstance().getModule(contentObject.getContentModuleClass());
                contentObjectPanel = module.getContentObjectPanel(contentObject.getClass());
            }
            else {
                contentObjectPanel = new ContentObjectPanel("panel");
            }

            // initialize content form
            contentObjectPanel.setName("contentObjectPanel");
            contentObjectPanel.init();
            contentObjectPanel.setContentObject(contentObject);

            removeChild(contentObjectPanel);

            // add content form
            addChild(contentObjectPanel);
        }
        catch(Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ContentObjectPanel getContentObjectPanel() {
        return contentObjectPanel;
    }

    public void setContentObjectPanel(ContentObjectPanel contentObjectPanel) {
        this.contentObjectPanel = contentObjectPanel;
    }

	public boolean getIsDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
