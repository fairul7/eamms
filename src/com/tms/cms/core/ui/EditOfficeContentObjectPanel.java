package com.tms.cms.core.ui;

import com.tms.cms.core.model.*;
import com.tms.cms.profile.model.ContentProfile;
import com.tms.cms.profile.model.ContentProfileModule;
import com.tms.cms.profile.ui.ContentProfileAssignmentForm;
import com.tms.cms.profile.ui.ContentProfileForm;
import com.tms.util.MailUtil;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Mailer;
import kacang.util.Log;
import kacang.services.security.User;

public class EditOfficeContentObjectPanel extends EditContentObjectPanel {

    private boolean noDocument;

    public boolean isNoDocument() {
        return noDocument;
    }

    public EditOfficeContentObjectPanel() {
        super();
    }

    public EditOfficeContentObjectPanel(String s) {
        super(s);
    }

    public void onRequest(Event evt) {
        if (getId() == null) {
            noDocument = true;
        }
        else {
            noDocument = false;
            addContainerForm(evt);
        }
    }

    protected void saveContentObject(ContentObject contentObject) {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);
        try {
            if (getId() != null) {
                contentObject.setId(getId());
            }

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
            if (getId() != null) {
                contentObject.setId(getId());
            }

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
            if (getId() != null) {
                contentObject.setId(getId());
            }

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
            if (getId() != null) {
                contentObject.setId(getId());
            }

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
            Log.getLog(getClass()).error("Error sending notification for content " + contentObject, e);
        }
    }

    protected void saveProfile(Event evt, ContentObject contentObject) {
        try {
            Application app = Application.getInstance();
            ContentProfileModule profileMod = (ContentProfileModule)app.getModule(ContentProfileModule.class);
            ContentManager cm = (ContentManager)app.getModule(ContentManager.class);
            User user = getWidgetManager().getUser();

            if (getId() != null) {
                contentObject.setId(getId());
            }

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
