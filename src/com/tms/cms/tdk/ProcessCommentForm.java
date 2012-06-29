package com.tms.cms.tdk;

import com.tms.cms.comment.Comment;
import com.tms.cms.core.model.*;
import com.tms.ekms.setup.model.SetupModule;
import com.tms.util.MailUtil;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;
import kacang.util.Mailer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class ProcessCommentForm extends LightWeightWidget {

    public static final String SETUP_KEY_COMMENT_APPROVAL = "siteCommentApproval";

    private String url;
    private String scoreRequired;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getScoreRequired() {
        return scoreRequired;
    }

    public void setScoreRequired(String scoreRequired) {
        this.scoreRequired = scoreRequired;
    }

    public void onRequest(Event evt) {
        HttpServletRequest request = evt.getRequest();

        // get id
        String id = request.getParameter("id"); // commentary id
        String commentId = request.getParameter("commentId"); // comment id, if any
        String parentId = (commentId != null && commentId.trim().length() > 0) ? commentId : id;

        // check permission
        User user = evt.getWidgetManager().getUser();
        ContentPublisher cp = (ContentPublisher) Application.getInstance().getModule(ContentPublisher.class);
        ContentObject co = null;
        try {
            co = cp.view(id, user);
            if (!ContentUtil.hasPermission(co, user.getId(), ContentManager.USE_CASE_VIEW)) {
                return;
            }
        }
        catch (DataObjectNotFoundException e) {
            return;
        }
        catch (ContentException e) {
            throw new RuntimeException(e.toString());
        }

        // perform validation
        String action = request.getParameter("action");
        String title = request.getParameter("title");
        String score = request.getParameter("score");
        String comment = request.getParameter("comment");
        if (action == null || !action.equals("postComment")) {
            return;
        }
        else if (title == null || title.trim().length() == 0) {
            return;
        }
        if (score == null && Boolean.valueOf(getScoreRequired()).booleanValue()) {
            return;
        }

        // parse score
        float fScore = 0.0f;
        try {
            fScore = Float.parseFloat(score);
        }
        catch (Exception e) {
            ;
        }

        // create comment
        Comment commentObject = new Comment();
        commentObject.setParentId(parentId);
        commentObject.setName(title);
        commentObject.setSummary(comment);
        commentObject.setScore(fScore);
        commentObject.setAuthor(user.getUsername());
        try {
            ContentManager cm = (ContentManager) Application.getInstance().getModule(ContentManager.class);
            ContentObject result = cm.createNew(commentObject, user, false);
            result = cm.submitForApproval(result.getId(), user, false);

            // check for auto approval and publishing
            Application application = Application.getInstance();
            try {
                SetupModule setup = (SetupModule) application.getModule(SetupModule.class);
                if (!Boolean.valueOf(setup.get(SETUP_KEY_COMMENT_APPROVAL)).booleanValue()) {
                    cm.approve(result, user, false);
                    cm.publish(result.getId(), false, user, false);
                }
                else {
                    sendApprovalNotification(evt, result);
                }
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error retrieving setup property: " + e.toString());
            }

            // notify the last user that updated the content
            if (co != null) {
                sendUserNotification(evt, result, co.getParentId());
            }

        }
        catch (Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }

        // redirect
        try {
            if (url != null && url.trim().length() > 0) {
                evt.getResponse().sendRedirect(url);
            }
        }
        catch (IOException e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }

    }

    protected void sendUserNotification(Event evt, ContentObject comment, String contentId) {
        try {
            Application application = Application.getInstance();
            ContentManager cm = (ContentManager) application.getModule(ContentManager.class);
            User poster = evt.getWidgetManager().getUser();
            ContentObject contentObject = cm.view(contentId, poster);
            String lastUserId = contentObject.getSubmissionUserId();
            if (lastUserId == null) {
                Collection history = ((ContentManagerDao)cm.getDao()).selectVersions(contentId, null, false, 0, -1); // retrieve direct from dao to ignore permission checking
                for (Iterator i=history.iterator(); i.hasNext();) {
                    ContentObject tmp = (ContentObject)i.next();
                    lastUserId = tmp.getSubmissionUserId();
                    if (lastUserId != null) {
                        break;
                    }
                }
            }

/*
            cms.message.email.comment.subject=Comments Notification: {0}
            cms.message.email.comment.content=Title: <a href="{0}">{1}</a><p>{2}<p><hr size='1'>Comment Submitted By: {4} {3}<br><br>Comment: {5}<br>{6}
*/

            // formulate message
            SetupModule setup = (SetupModule)application.getModule(SetupModule.class);
            String firstName = (poster.getProperty("firstName") != null) ? (String)poster.getProperty("firstName") : "";
            String lastName = (poster.getProperty("lastName") != null) ? (String)poster.getProperty("lastName") : "";
            String[] subjectArgs = new String[] {contentObject.getName()};
            String subject = application.getMessage("cms.message.email.comment.subject", subjectArgs);
            String serverUrl = setup.get("siteUrl");
            String contentUrl = serverUrl + "/cms/content.jsp?id=" + contentObject.getId();
            String[] contentArgs = new String[] {contentUrl, contentObject.getName(), ContentUtil.makeAbsolute(evt.getRequest(), contentObject.getSummary()), firstName, lastName, comment.getName(), comment.getSummary() };
            String content = application.getMessage("cms.message.email.comment.content", contentArgs);

            // send user notification
            SecurityService ss = (SecurityService)application.getService(SecurityService.class);
            User lastUser = ss.getUser(lastUserId);
            String email = (String)lastUser.getProperty("email1");
            if (Mailer.isValidEmail(email)) {
                MailUtil.sendEmail(null, true, null, email, subject, content);
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Unable to send notification for content " + contentId, e);
        }
    }

    protected void sendApprovalNotification(Event evt, ContentObject contentObject) {
        try {
            Application application = Application.getInstance();
            if (Boolean.valueOf(application.getProperty(ContentManager.APPLICATION_PROPERTY_NOTIFICATION_EMAIL)).booleanValue()) {
                User user = evt.getWidgetManager().getUser();
                String emails = ContentUtil.getUserEmails(contentObject.getId(), ContentManager.USE_CASE_APPROVE, user);
                if (Mailer.isValidEmail(emails)) {
                    String[] subjectArgs = new String[]{contentObject.getName()};
                    String subject = application.getMessage("cms.message.email.submitted.subject", subjectArgs);
                    String serverUrl = "http://" + evt.getRequest().getServerName() + ":" + evt.getRequest().getServerPort() + evt.getRequest().getContextPath() + "/ekms/cmsadmin";
                    String[] contentArgs = new String[]{contentObject.getName(), user.getUsername(), serverUrl, (String)user.getProperty("firstName"), (String)user.getProperty("lastName") };
                    String content = application.getMessage("cms.message.email.submitted.content", contentArgs);
                    MailUtil.sendEmail(null, true, null, emails, subject, content);
                }
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Unable to send approval notification for comment " + contentObject, e);
        }
    }

    public String getDefaultTemplate() {
        return "";
    }

}
