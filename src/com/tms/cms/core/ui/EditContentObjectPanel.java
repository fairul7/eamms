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
import kacang.util.Mailer;

import javax.servlet.http.HttpSession;
import java.util.Iterator;

public class EditContentObjectPanel extends Panel {

    public static final String FORWARD_FAILED = "failed";

    private String id;

    protected Form containerForm;
    protected ContentObjectForm contentObjectForm;

    protected Button saveButton;
    protected Button submitButton;
    protected Button approveButton;
    protected Button cancelButton;
    protected Button undoCheckoutButton;

    protected ContentProfileForm profileForm;
    protected ContentProfileAssignmentForm assignForm;

    public static final String SESSION_KEY_MSOFFICE_PATH = "path";
    public static final String SESSION_KEY_MSOFFICE_FILE = "file";

    public EditContentObjectPanel() {
    }

    public EditContentObjectPanel(String name) {
        super(name);
    }

    public void init() {
    }

    public void onRequest(Event evt) {
        HttpSession session = evt.getRequest().getSession();
        String sessionDocID = (String) session.getAttribute(SESSION_KEY_MSOFFICE_PATH);
        ContentObject co;
        if (getId() == null)
            co = ContentHelper.getContentObject(evt, sessionDocID);
        else
            co = ContentHelper.getContentObject(evt, getId());
        if (co != null) {
            setId(co.getId());
        }
        addContainerForm(evt);
    }

    protected void addContainerForm(Event evt) {
        // remove existing widgets
        removeChildren();

        // add container form
        containerForm = new Form("containerForm");
        containerForm.setMethod("POST");
        addChild(containerForm);

        // get content form
        try {
            if (getId() != null) {
                boolean refreshProfile = false;

                // retrieve from module
                User user = getWidgetManager().getUser();
                ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
                ContentObject contentObject = cm.checkOutToEdit(getId(), user);

                // retrieve specific class
                ContentModule module = (ContentModule)Application.getInstance().getModule(contentObject.getContentModuleClass());

                if (contentObjectForm == null || !contentObjectForm.getContentObject().getId().equals(getId())) {
                    contentObjectForm = module.getContentObjectForm(contentObject.getClass());

                    // initialize content form
                    contentObjectForm.setName("contentObjectForm");
                    contentObjectForm.setWidgetManager(getWidgetManager());
                    contentObjectForm.setContentObject(contentObject);
                    contentObjectForm.init(evt);

                    refreshProfile = true;
                }
                else {
                    contentObjectForm.setContentObject(contentObject);
                }

                // add content form
                containerForm.addChild(contentObjectForm);
                contentObjectForm.populateFields(evt);

                // set multipart if necessary
                if ("multipart/form-data".equalsIgnoreCase(contentObjectForm.getEnctype())) {
                    Form rootForm = contentObjectForm.getRootForm();
                    rootForm.setEnctype("multipart/form-data");
                    rootForm.setMethod("POST");
                }

                // NEW: profile form
                try {
                    Application app = Application.getInstance();
                    ContentProfileModule profileMod = (ContentProfileModule)app.getModule(ContentProfileModule.class);

                    if (ContentProfileModule.isProfiledContentParent(contentObject.getClassName()) && (refreshProfile || assignForm == null || !assignForm.isInvalid())) {
                        assignForm = new ContentProfileAssignmentForm("profileAssignForm");
                        assignForm.setId(getId());
                        contentObjectForm.addChild(assignForm);
                        assignForm.init();
                    }
                    else if (ContentProfileModule.isProfiledContent(contentObject.getClassName()) && (refreshProfile || profileForm == null || !profileForm.isInvalid())) {
                        String parentId = contentObject.getParentId();
                        ContentProfile profile = profileMod.getProfileByParent(parentId);
                        profileForm = profileMod.loadForm(profile.getId(), contentObject.getId(), contentObject.getVersion());
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
				undoCheckoutButton = new Button("undoCheckoutButton", application.getMessage("general.label.undoCheckOut"));
				containerForm.addChild(undoCheckoutButton);
                cancelButton = new Button(Form.CANCEL_FORM_ACTION);
                cancelButton.setText(application.getMessage("general.label.cancel", "Cancel"));
                containerForm.addChild(cancelButton);

                // check permission for buttons
                try {
                    if (cm.hasPermission(contentObject, getWidgetManager().getUser().getId(), ContentManager.USE_CASE_APPROVE)) {
                        approveButton.setHidden(false);
                    }
                }
                catch (ContentException e) {
                    ;
                }

                // add form listener
                containerForm.addFormEventListener(new FormEventAdapter() {
                    public Forward onValidate(Event evt) {
                        // get selected button
                        WidgetManager wm = evt.getWidgetManager();
                        String buttonClicked = containerForm.findButtonClicked(evt);
                        Widget button = wm.getWidget(buttonClicked);
                        if (saveButton.equals(button)) {
                            try
                            {
                                ContentObject contentObject = contentObjectForm.getContentObject();
                                saveContentObject(contentObject);
                                saveProfile(evt, contentObject);
                                addContainerForm(evt);
                                return new Forward("save");
                            }
                            catch(Exception e)
                            {
                                addContainerForm(evt);
                                return new Forward(FORWARD_FAILED);
                            }
                        }
                        else if (submitButton.equals(button)) {
                            try
                            {
                                ContentObject contentObject = contentObjectForm.getContentObject();
                                submitContentObject(contentObject);
                                saveProfile(evt, contentObject);
                                // send notification
                                sendNotification(evt, contentObject);
                                return new Forward("submit");
                            }
                            catch(Exception e)
                            {
                                addContainerForm(evt);
                                return new Forward(FORWARD_FAILED);
                            }
                        }
                        else if (approveButton.equals(button)) {
                            try
                            {
                                // approve content
                                ContentObject contentObject = contentObjectForm.getContentObject();
                                contentObject = approveContentObject(contentObject);
                                saveProfile(evt, contentObject);
                                if (contentObject == null) {
                                    return new Forward("submit");
                                }
                                // schedule publishing
                                Object schedule = contentObjectForm.getChild("scheduleCheckBox");
                                if (schedule != null && (schedule instanceof CheckBox) && ((CheckBox)schedule).isChecked()) {
                                    publishContentObject(contentObject, false);
                                }
                                return new Forward("approve");
                            }
                            catch(Exception e)
                            {
                                addContainerForm(evt);
                                return new Forward(FORWARD_FAILED);
                            }
                        }
						else if (undoCheckoutButton.equals(button))
						{
                            try
                            {
                                //undo checkout
                                ContentObject contentObject = contentObjectForm.getContentObject();
                                undoCheckoutContentObject(contentObject);
                                return new Forward("undo");
                            }
                            catch(Exception e)
                            {
                                addContainerForm(evt);
                                return new Forward(FORWARD_FAILED);
                            }
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

    protected void saveContentObject(ContentObject contentObject) {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);
        try {
            // save
            User user = getWidgetManager().getUser();
            contentManager.save(contentObject, user);
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    protected void submitContentObject(ContentObject contentObject) {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);
        try {
            // submit for approval
            User user = getWidgetManager().getUser();
            contentObject = contentManager.save(contentObject, user);
            contentManager.submitForApproval(contentObject.getId(), user);
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    protected ContentObject approveContentObject(ContentObject contentObject) {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);
        try {
            // approve
            User user = getWidgetManager().getUser();
            contentObject = contentManager.save(contentObject, user);
            contentObject = contentManager.submitForApproval(contentObject.getId(), user);
            contentManager.approve(contentObject, user);
            return contentObject;
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

	protected void undoCheckoutContentObject(ContentObject contentObject)
	{
		Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);
		User user = getWidgetManager().getUser();
		try
		{
			contentManager.undoCheckOut(contentObject.getId(), user);
		}
		catch (Exception e)
		{
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

	//Getter and Setter
	public Button getUndoCheckoutButton()
	{
		return undoCheckoutButton;
	}

	public void setUndoCheckoutButton(Button undoCheckoutButton)
	{
		this.undoCheckoutButton = undoCheckoutButton;
	}

}
