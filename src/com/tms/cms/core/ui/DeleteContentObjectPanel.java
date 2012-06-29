package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentModule;
import com.tms.cms.core.model.ContentObject;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.Panel;
import kacang.stdui.event.FormEventAdapter;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.ui.WidgetManager;
import kacang.util.Log;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 21, 2003
 * Time: 4:44:04 PM
 * To change this template use Options | File Templates.
 */
public class DeleteContentObjectPanel extends Panel {

    private String id;

    private Form containerForm;
    private ContentObjectView contentObjectView;

    private CheckBox destroyBox;
    private CheckBox recursiveBox;
    private Button deleteButton;
    private Button cancelButton;

    public DeleteContentObjectPanel() {
    }

    public DeleteContentObjectPanel(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "cms/admin/deleteContentObjectPanel";
    }

    public void init() {
        // remove existing widgets
        removeChildren();

        // add content specific form
        addContainerForm();
    }

    public void onRequest(Event evt) {
        ContentObject co = ContentHelper.getContentObject(evt, getId());
        if (co != null) {
            setId(co.getId());
        }
        init();
    }

    protected void addContainerForm() {
        try {
            if (getId() != null) {
                // retrieve from module
                User user = getWidgetManager().getUser();
                ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
                ContentObject contentObject = cm.view(getId(), user);

                // retrieve specific class
                ContentModule module = (ContentModule)Application.getInstance().getModule(contentObject.getContentModuleClass());
                contentObjectView = module.getContentObjectView(contentObject.getClass());

                // initialize content view
                contentObjectView.setName("contentObjectView");
                contentObjectView.init();
                if (contentObject != null) {
                    contentObjectView.setContentObject(contentObject);
                }

                // add content view
                addChild(contentObjectView);

                // add container form
                containerForm = new Form("containerForm");
                addChild(containerForm);

                // add checkboxes
				Application application = Application.getInstance();
                destroyBox = new CheckBox("destroyBox");
                destroyBox.setText(application.getMessage("general.label.permanentlyDelete", "Permanently Delete"));
                destroyBox.setChecked(false);
                containerForm.addChild(destroyBox);

                recursiveBox = new CheckBox("recursiveBox");
                recursiveBox.setText(application.getMessage("general.label.propagate", "Propagate"));
//                containerForm.addChild(recursiveBox);

                // add buttons
                deleteButton = new Button("deleteButton");
                deleteButton.setText(application.getMessage("general.label.delete", "Delete"));
                containerForm.addChild(deleteButton);
                cancelButton = new Button(Form.CANCEL_FORM_ACTION);
                cancelButton.setText(application.getMessage("general.label.cancel", "Cancel"));
                containerForm.addChild(cancelButton);

                // add form listener
                containerForm.addFormEventListener(new FormEventAdapter() {
                    public Forward onValidate(Event evt) {
                        // get selected button
                        WidgetManager wm = evt.getWidgetManager();
                        String buttonClicked = containerForm.findButtonClicked(evt);
                        Widget button = wm.getWidget(buttonClicked);
                        if (deleteButton.equals(button)) {
                            ContentObject contentObject = contentObjectView.getContentObject();
                            boolean recursive = recursiveBox.isChecked();
                            boolean destroy = destroyBox.isChecked();
                            deleteContentObject(contentObject, destroy, recursive);

                            // set to parent object
                            String parentId = contentObject.getParentId();
                            ContentHelper.setId(evt, parentId);
                            setId(parentId);
                            init();
                            return new Forward("delete");
                        }
                        else {
                            return new Forward(Form.CANCEL_FORM_ACTION);
                        }
                    }
                });

                // add forwards
                for (Iterator i=getForwardMap().values().iterator(); i.hasNext();) {
                    containerForm.addForward((Forward)i.next());
                }
            }
        }
        catch(ContentException e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    protected void deleteContentObject(ContentObject contentObject, boolean destroy, boolean recursive) {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);
        try {
            // save
            User user = getWidgetManager().getUser();
            if (destroy) {
                contentManager.destroy(new String[] {contentObject.getId()}, user);
            }
            else {
                contentManager.delete(contentObject.getId(), true, user);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

}
