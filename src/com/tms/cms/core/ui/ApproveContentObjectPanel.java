package com.tms.cms.core.ui;

import com.tms.cms.core.model.*;
import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Panel;
import kacang.stdui.TextBox;
import kacang.stdui.event.FormEventAdapter;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.ui.WidgetManager;
import kacang.util.Log;

import java.util.Iterator;

public class ApproveContentObjectPanel extends Panel {

    private String id;

    private Form containerForm;
    private ContentObjectView contentObjectView;

    private TextBox commentsBox;

    private Button approveButton;
    private Button rejectButton;
    private Button cancelButton;

    public ApproveContentObjectPanel() {
    }

    public ApproveContentObjectPanel(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "cms/admin/approveContentObjectPanel";
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

                // add comments box
                commentsBox = new TextBox("commentsBox");
                containerForm.addChild(commentsBox);

                // add buttons
                Application application = Application.getInstance();
                approveButton = new Button("approveButton");
                approveButton.setText(application.getMessage("general.label.approve", "Approve"));
                containerForm.addChild(approveButton);
                rejectButton = new Button("rejectButton");
                rejectButton.setText(application.getMessage("general.label.reject", "Reject"));
                containerForm.addChild(rejectButton);
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
                        if (approveButton.equals(button)) {
                            ContentObject contentObject = contentObjectView.getContentObject();
                            contentObject.setComments((String)commentsBox.getValue());
                            approveContentObject(contentObject);

                            // send notification
                            sendNotification(evt, contentObject, true);

                            init();
                            return new Forward("approve");
                        }
                        else if (rejectButton.equals(button)) {
                            ContentObject contentObject = contentObjectView.getContentObject();
                            contentObject.setComments((String)commentsBox.getValue());
                            rejectContentObject(contentObject);

                            // send notification
                            sendNotification(evt, contentObject, false);

                            init();
                            return new Forward("reject");
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

    protected void approveContentObject(ContentObject contentObject) {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);
        try {
            // save
            User user = getWidgetManager().getUser();
            contentManager.approve(contentObject, user);
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    protected void rejectContentObject(ContentObject contentObject) {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);
        try {
            // submit for approval
            User user = getWidgetManager().getUser();
            contentManager.reject(contentObject, user);
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    protected void sendNotification(Event evt, ContentObject contentObject, boolean approved) {
        User user = getWidgetManager().getUser();
        ContentUtil.sendApprovalNotification(contentObject, user, approved);
    }


}
