package com.tms.cms.core.ui;

import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentModule;
import com.tms.cms.core.model.ContentObject;
import kacang.Application;
import kacang.util.Log;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Panel;
import kacang.stdui.event.FormEventAdapter;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.ui.WidgetManager;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 21, 2003
 * Time: 4:44:04 PM
 * To change this template use Options | File Templates.
 */
public class UndoCheckOutContentObjectPanel extends Panel {

    private String id;

    private Form containerForm;
    private ContentObjectView contentObjectView;

    private Button undoCheckOutButton;
    private Button cancelButton;

    public UndoCheckOutContentObjectPanel() {
    }

    public UndoCheckOutContentObjectPanel(String name) {
        super(name);
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

                // add buttons
                Application application = Application.getInstance();
                undoCheckOutButton = new Button("undoCheckOutButton");
                undoCheckOutButton.setText(application.getMessage("general.label.undoCheckOut", "Undo Check Out"));
                containerForm.addChild(undoCheckOutButton);
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
                        if (undoCheckOutButton.equals(button)) {
                            ContentObject contentObject = contentObjectView.getContentObject();
                            undoCheckOutContentObject(contentObject);
                            init();
                            return new Forward("undoCheckOut");
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
//            e.printStackTrace();
            Log.getLog(getClass()).error(e);
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }

    }

    public String getDefaultTemplate() {
        return "cms/admin/undoCheckOutContentObjectPanel";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    protected void undoCheckOutContentObject(ContentObject contentObject) {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);
        try {
            // save
            User user = getWidgetManager().getUser();
            contentManager.undoCheckOut(contentObject.getId(), user);
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

}
