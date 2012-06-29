package com.tms.cms.portlet;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.InvalidKeyException;
import com.tms.cms.core.ui.AddContentObjectPanel;
import com.tms.cms.core.ui.ContentHelper;
import com.tms.cms.core.ui.ContentSelectBox;
import com.tms.cms.document.Document;
import com.tms.cms.section.ui.SectionSelectBox;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;

import java.util.Map;

public class AddContentPopup extends AddContentObjectPanel {

    private String id;
    private String type;

    public AddContentPopup() {
    }

    public AddContentPopup(String name) {
        super(name);
    }

    protected void addContainerForm(Event evt) {
        super.addContainerForm(evt);

        Form form = getContainerForm();
        form.setTemplate("cms/portlet/addContentPopup");
        CustomSectionSelectBox ssb = new CustomSectionSelectBox("sectionSelectBox");
        ssb.setPermissionId(ContentManager.USE_CASE_CREATE);
        form.addChild(ssb);
        ssb.populateSelectBox(evt);
    }

    protected void addContentSelectForm(Event evt) {
        super.addContentSelectForm(evt);

        ContentSelectBox w = getContentSelectBox();
        if (w != null) {
            String t = getType();
            if (t == null) {
                t = Document.class.getName();
            }
            w.setSelectedOptions(new String[] { t });
            w.setHidden(true);
            addContainerForm(evt);
        }
        Button b = getContentSelectButton();
        if (b != null) {
            b.setHidden(true);
        }
    }

    protected Form getContainerForm() {
        return (Form)getChild("containerForm");
    }

    protected ContentSelectBox getContentSelectBox() {
        Form form = (Form)getChild("contentSelectForm");
        if (form == null)
            return null;
        return (ContentSelectBox)form.getChild("contentSelectBox");
    }

    protected Button getContentSelectButton() {
        Form form = (Form)getChild("contentSelectForm");
        if (form == null)
            return null;
        return (Button)form.getChild("contentSelectButton");
    }

    public void onRequest(Event evt) {
/*
        String key = getId();
        if (key == null) {
            key = ContentManager.CONTENT_TREE_ROOT_ID;
        }
        ContentHelper.setId(evt, key);
        super.onRequest(evt);
*/

        /*// remove existing widgets
        removeChildren();

        // add content select form
        addContentSelectForm(evt);

        // add content specific form
        addContainerForm(evt);

        // show approve button
        approveButton.setHidden(false);*/
        
        if (ContentHelper.getId(evt) == null) {
        	
        	ContentHelper.setId(evt, ContentManager.CONTENT_TREE_ROOT_ID);
        }
        
        //super.onRequest(evt);
    	ContentObject co = ContentHelper.getContentObject(evt, getId());
        if (co != null) {
            if (!co.getId().equals(getId())) {
            	setId(co.getId());            	
            	// remove existing widgets
                removeChildren();
                // add content select form
                addContentSelectForm(evt);

                // add content specific form
                addContainerForm(evt);
                // show approve button
                approveButton.setHidden(false);
            
            }


        }


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    protected ContentObject approveContentObject(ContentObject contentObject) throws InvalidKeyException {
        try {
            Application application = Application.getInstance();
            User user = getWidgetManager().getUser();
            ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);

            // create new object
            if (getId() != null) {
                contentObject.setParentId(getId());
            }
            contentObject = contentManager.createNew(contentObject, user);

            // submit for approval
            contentObject = contentManager.submitForApproval(contentObject.getId(), user);

            if (contentManager.hasPermission(contentObject, user.getId(), ContentManager.USE_CASE_APPROVE)) {
                // approve
                contentManager.approve(contentObject, user);

                // publish
                contentObject.setPublished(true);
                contentManager.publish(contentObject.getId(), false, user);
                return contentObject;
            }
            else {
                return null;
            }

        }
        catch(InvalidKeyException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    class CustomSectionSelectBox extends SectionSelectBox {
        public CustomSectionSelectBox() {
        }

        public CustomSectionSelectBox(String name) {
            super(name);
        }

        public Forward onSubmit(Event evt) {
            super.onSubmit(evt);
            Map selected = getSelectedOptions();
            if (selected != null && selected.size() > 0) {
                // get selected section
                String key = (String)selected.keySet().iterator().next();
                if (key != null && key.trim().length() > 0) {
                    setId(key);
                    ContentHelper.setId(evt, key);
                }
            }
            return null;
        }
    }


}
