package com.tms.cms.core.ui;

import com.tms.cms.core.model.*;
import com.tms.cms.profile.model.ContentProfile;
import com.tms.cms.profile.model.ContentProfileModule;
import com.tms.cms.profile.ui.ContentProfileAssignmentForm;
import com.tms.cms.profile.ui.ContentProfileForm;
import com.tms.util.MailUtil;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.stdui.event.FormEventAdapter;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.ui.WidgetManager;
import kacang.util.Log;
import kacang.util.Mailer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AddContentObjectPanel extends Panel {

    public static final String FORWARD_FAILED = "failed";

    private String id;

    protected String selectedClass;
    protected Form contentSelectForm;
    protected Form containerForm;
    protected ContentSelectBox contentSelectBox;
    protected Button contentSelectButton;
    protected ContentObjectForm contentObjectForm;
    protected Label invalidKeyLabel;

    protected ContentProfileForm profileForm;
    protected ContentProfileAssignmentForm assignForm;

    protected Button saveButton;
    protected Button submitButton;
    protected Button approveButton;
    protected Button cancelButton;
    protected String INVALID_KEY_MESSAGE = Application.getInstance().getMessage("general.error.id", "Invalid ID or ID already in use");

    public AddContentObjectPanel() {
    }

    public AddContentObjectPanel(String name) {
        super(name);
    }

    public void init() {
    }

    public String getDefaultTemplate() {
        return "cms/admin/addContentObjectPanel";
    }

    public void onRequest(Event evt) {
        ContentObject co = ContentHelper.getContentObject(evt, getId());
        if (co != null) {
            if (!co.getId().equals(getId())) {
                // reset form
                setId(co.getId());

                // remove existing widgets
                removeChildren();

                // add content select form
                addContentSelectForm(evt);

                // add content specific form
                addContainerForm(evt);
            }

            // check permission for buttons
            try {
                ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
                User user = getWidgetManager().getUser();
                if (cm.hasPermission(co, user.getId(), ContentManager.USE_CASE_APPROVE)) {
                    approveButton.setHidden(false);
                }
                else {
                    approveButton.setHidden(true);
                }
            }
            catch (Exception e) {
                ;
            }
        }
    }

    protected void addContentSelectForm(Event evt) {
        // add content select box
        contentSelectBox = new ContentSelectBox("contentSelectBox");
        contentSelectBox.setId(getId());
        contentSelectBox.init();
        if (selectedClass != null) {
            // remember previous selection
            Map optionMap = contentSelectBox.getOptionMap();
            if (optionMap != null && optionMap.containsKey(selectedClass)) {
                List selected = new ArrayList();
                selected.add(selectedClass);
                contentSelectBox.setValue(selected);
            }
            else {
                selectedClass = null;
            }
        }

        // add content select button
        Application application = Application.getInstance();
        contentSelectButton = new Button("contentSelectButton");
        contentSelectButton.setText(application.getMessage("general.label.select", "Select"));

        // add box and button to form
        contentSelectForm = new Form("contentSelectForm");
        contentSelectForm.addChild(contentSelectBox);
        contentSelectForm.addChild(contentSelectButton);
        contentSelectForm.setTemplate("cms/admin/contentSelectForm");

        // add form
        addChild(contentSelectForm);

        // add listener to form
        contentSelectForm.addFormEventListener(new FormEventAdapter() {
            public Forward onValidate(Event evt) {
                // save selected class
                List selected = (List)contentSelectBox.getValue();
                if (selected != null && selected.size() > 0) {
                    selectedClass = (String)selected.get(0);
                }

                // recreate form
                removeChild(containerForm);
                addContainerForm(evt);
                return null;
            }
        });

        // add label
        invalidKeyLabel = new Label("invalidKeyLabel");
        addChild(invalidKeyLabel);
    }

    protected void addContainerForm(Event evt) {
        // add container form
        containerForm = new Form("containerForm");
        containerForm.setMethod("POST");
        addChild(containerForm);

        // get selected class name
        List values = (List) contentSelectBox.getValue();
        String selectedClassName = null;
        if (values != null && !values.isEmpty()) {
            selectedClassName = (String)values.get(0);
        }
        if (selectedClassName == null || selectedClassName.trim().length() == 0) {
            return;
        }

        // get content form
        ContentObject newContentObject = null;
        try {
            newContentObject = (ContentObject)Class.forName(selectedClassName).newInstance();
            ContentModule module = (ContentModule)Application.getInstance().getModule(newContentObject.getContentModuleClass());
            contentObjectForm = module.getContentObjectForm(newContentObject.getClass());
        }
        catch (Exception e) {
            ;
        }
        if (contentObjectForm == null) {
            contentObjectForm = new ContentObjectForm();
        }

        // initialize content form
        contentObjectForm.setName("contentObjectForm");
        contentObjectForm.setWidgetManager(getWidgetManager());
        if (newContentObject != null) {
            contentObjectForm.setContentObject(newContentObject);
        }
        contentObjectForm.init(evt);
        contentObjectForm.populateFields(evt);

        // add content form
        containerForm.addChild(contentObjectForm);

        // NEW: profile form
        try {
            Application app = Application.getInstance();
            ContentProfileModule profileMod = (ContentProfileModule)app.getModule(ContentProfileModule.class);

            if (ContentProfileModule.isProfiledContentParent(selectedClassName)) {
                String parentId = getId();
                assignForm = new ContentProfileAssignmentForm("profileAssignForm");
                assignForm.setId(parentId);
                contentObjectForm.addChild(assignForm);
                assignForm.init();
            }
            else if (ContentProfileModule.isProfiledContent(selectedClassName)) {
                String parentId = getId();
                ContentProfile profile = profileMod.getProfileByParent(parentId);
                profileForm = profileMod.loadForm(profile.getId(), null, null);
                contentObjectForm.addChild(profileForm);
            }
        }
        catch (DataObjectNotFoundException e) {
            Log.getLog(getClass()).debug("Profile form not found for parent " + getId());
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving profile form for parent " + getId(), e);
        }

        // add buttons
        Application application = Application.getInstance();
        saveButton = new Button("saveButton");
        saveButton.setText(application.getMessage("general.label.saveAsDraft", "Save As Draft"));
        containerForm.addChild(saveButton);
        submitButton = new Button("submitButton");
        submitButton.setText(application.getMessage("general.label.submitForApproval", "Submit For Approval"));
        containerForm.addChild(submitButton);
        approveButton = new Button("approveButton");
        approveButton.setText(application.getMessage("general.label.approve", "Approve"));
        approveButton.setHidden(true);
        containerForm.addChild(approveButton);
        cancelButton = new Button(Form.CANCEL_FORM_ACTION);
        cancelButton.setText(application.getMessage("general.label.cancel", "Cancel"));
        containerForm.addChild(cancelButton);

        // add form listener
        containerForm.addFormEventListener(new FormEventAdapter() {
            public Forward onValidate(Event evt) {
                // reset label
                invalidKeyLabel.setText("");

                // get selected button
                WidgetManager wm = evt.getWidgetManager();
                String buttonClicked = containerForm.findButtonClicked(evt);
                Widget button = wm.getWidget(buttonClicked);
                try {
                    if (saveButton.equals(button)) {
                        ContentObject contentObject = contentObjectForm.getContentObject();
                        contentObject = saveContentObject(contentObject);
                        saveProfile(evt, contentObject);

                        //ContentHelper.setId(evt, contentObject.getId());
                        // remove existing widgets
                        removeChildren();

                        // add content select form
                        addContentSelectForm(evt);

                        // add content specific form
                        addContainerForm(evt);
                        return new Forward("save");
                    }
                    else if (submitButton.equals(button)) {
                        ContentObject contentObject = contentObjectForm.getContentObject();
                        contentObject = submitContentObject(contentObject);
                        saveProfile(evt, contentObject);
                        //ContentHelper.setId(evt, contentObject.getId());

                        // send notification
                        sendNotification(evt, contentObject);

                        // remove existing widgets
                        removeChildren();

                        // add content select form
                        addContentSelectForm(evt);

                        // add content specific form
                        addContainerForm(evt);
                        return new Forward("submit");
                    }
                    else if (approveButton.equals(button)) {
                        // approve content
                        ContentObject contentObject = contentObjectForm.getContentObject();
                        contentObject = approveContentObject(contentObject);
                        saveProfile(evt, contentObject);

                        if (contentObject != null) {
                            // schedule publishing
                            Object schedule = contentObjectForm.getChild("scheduleCheckBox");
                            if (schedule != null && (schedule instanceof CheckBox) && ((CheckBox)schedule).isChecked()) {
                                publishContentObject(contentObject, false);
                            }
                        }

                        //ContentHelper.setId(evt, contentObject.getId());
                        // remove existing widgets
                        removeChildren();

                        // add content select form
                        addContentSelectForm(evt);

                        // add content specific form
                        addContainerForm(evt);

                        return (contentObject != null) ? new Forward("approve") : new Forward("submit");
                    }
                    else {
                        return new Forward(Form.CANCEL_FORM_ACTION);
                    }
                }
                catch (InvalidKeyException e) {
                    invalidKeyLabel.setText(INVALID_KEY_MESSAGE);
                    ContentObject contentObject = contentObjectForm.getContentObject();
                    contentObject.setId(null);
                    contentObjectForm.setContentObject(contentObject);
                    return new Forward("invalidKey");
                }
                catch (Exception e)
                {
                    removeChildren();
                    addContentSelectForm(evt);
                    addContainerForm(evt);
                    return new Forward(FORWARD_FAILED);
                }
            }
        });

        // add forwards
        for (Iterator i=getForwardMap().values().iterator(); i.hasNext();) {
            containerForm.addForward((Forward)i.next());
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    protected ContentObject saveContentObject(ContentObject contentObject) throws InvalidKeyException {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);
        try {
            // create new object
            User user = getWidgetManager().getUser();
            if (getId() != null) {
                contentObject.setParentId(getId());
            }
            return contentManager.createNew(contentObject, user);
        }
        catch(InvalidKeyException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    protected ContentObject submitContentObject(ContentObject contentObject) throws InvalidKeyException {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);
        try {
            // create new object
            User user = getWidgetManager().getUser();
            if (getId() != null) {
                contentObject.setParentId(getId());
            }
            contentObject = contentManager.createNew(contentObject, user);

            // submit for approval
            return contentManager.submitForApproval(contentObject.getId(), user);
        }
        catch(InvalidKeyException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    protected ContentObject approveContentObject(ContentObject contentObject) throws InvalidKeyException {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);
        try {
            // create new object
            User user = getWidgetManager().getUser();
            if (getId() != null) {
                contentObject.setParentId(getId());
            }
            contentObject = contentManager.createNew(contentObject, user);

            // approve
            contentObject = contentManager.submitForApproval(contentObject.getId(), user);
            contentManager.approve(contentObject, user);
            return contentObject;
        }
        catch(InvalidKeyException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    protected void publishContentObject(ContentObject contentObject, boolean recursive) {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);
        try {
            // publish
            User user = getWidgetManager().getUser();
            contentManager.publishOnSchedule(contentObject, recursive, user);
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    protected void sendNotification(Event evt, ContentObject contentObject) {
        try {
            Application application = Application.getInstance();
            if (Boolean.valueOf(application.getProperty(ContentManager.APPLICATION_PROPERTY_NOTIFICATION_EMAIL)).booleanValue()) {
                User user = getWidgetManager().getUser();
                String emails = ContentUtil.getUserEmails(contentObject.getId(), ContentManager.USE_CASE_APPROVE, user);
                if (Mailer.isValidEmail(emails)) {
                    String[] subjectArgs = new String[] {contentObject.getName()};
                    String subject = application.getMessage("cms.message.email.submitted.subject", subjectArgs);
                    String serverUrl = "http://" + evt.getRequest().getServerName() + ":" + evt.getRequest().getServerPort() + evt.getRequest().getContextPath() + "/ekms/cmsadmin";
                    String[] contentArgs = new String[] {contentObject.getName(), user.getUsername(), serverUrl, (String)user.getProperty("firstName"), (String)user.getProperty("lastName") };
                    String content = application.getMessage("cms.message.email.submitted.content", contentArgs);
                    MailUtil.sendEmail(null, true, null, emails, subject, content);
                }
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error sending notification to approvers for " + contentObject, e);
        }
    }

    protected void saveProfile(Event evt, ContentObject contentObject) {
        try {
            Application app = Application.getInstance();
            ContentProfileModule profileMod = (ContentProfileModule)app.getModule(ContentProfileModule.class);
            ContentManager cm = (ContentManager)app.getModule(ContentManager.class);
            User user = getWidgetManager().getUser();
            if (ContentProfileModule.isProfiledContentParent(contentObject.getClassName()) && assignForm != null) {
                if (cm.hasPermission(contentObject, user.getId(), ContentManager.USE_CASE_APPROVE)) {
                    profileMod.assignByContent(assignForm.getProfileId(), getId());
                }
            }
            else if (ContentProfileModule.isProfiledContent(contentObject.getClassName()) && profileForm != null) {
                profileMod.storeForm(profileForm.getProfileId(), contentObject.getId(), contentObject.getVersion(), profileForm);
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error storing profile data for content " + contentObject, e);
        }
    }

}
