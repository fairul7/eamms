package com.tms.cms.core.jobs;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.core.model.ContentSubscription;
import com.tms.cms.section.Section;
import com.tms.cms.document.Document;
import com.tms.cms.article.Article;
import com.tms.ekms.setup.model.SetupModule;
import com.tms.util.MailUtil;
import kacang.Application;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.util.Mailer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class ContentSubscriptionTask extends BaseJob {

    public static final String[] SUBSCRIPTION_CLASSES = new String[] {
        Section.class.getName(),
        Article.class.getName(),
        Document.class.getName(),
    };

    protected static Collection subscriptionClassList;

    static {
        subscriptionClassList = new ArrayList();
        for (int i=0; i<SUBSCRIPTION_CLASSES.length; i++) {
            subscriptionClassList.add(SUBSCRIPTION_CLASSES[i]);
        }
    }

    public void execute(JobTaskExecutionContext context) throws SchedulingException {
        String id = context.getJobTaskData().getString("id");
        String version = context.getJobTaskData().getString("version");  // previous published version
        String userId = context.getJobTaskData().getString("userId");

        Log.getLog(getClass()).debug("=== Subscription notification for content " + id + " ===");

        try {
            Application application = Application.getInstance();
            SecurityService ss = (SecurityService)application.getService(SecurityService.class);
            User user = ss.getUser(userId);
            ContentManager cm = (ContentManager)application.getModule(ContentManager.class);
            ContentPublisher cp = (ContentPublisher)application.getModule(ContentPublisher.class);
            SetupModule setup = (SetupModule)application.getModule(SetupModule.class);

            // get content
            ContentObject contentObject = cp.view(id, user);
            if (subscriptionClassList.contains(contentObject.getClassName())) {
                String parentId = contentObject.getParentId();
                String[] subjectArgs = new String[] {contentObject.getName()};
                String subject = application.getMessage("cms.message.email.subscription.subject", subjectArgs);
                String serverUrl = setup.get("siteUrl");
                String contentUrl = serverUrl + "/cms/content.jsp?id=" + contentObject.getId();
                String author = contentObject.getAuthor();
                if (author == null) {
                    author = "";
                }
                String unsubscribeUrl = setup.get("siteUrl") + "/cms/subscription.jsp";
                boolean isNew = (version == null);
                String status = (isNew) ? "New" : "Updated";
                String[] contentArgs = new String[] {contentUrl, contentObject.getName(), author, status, contentObject.getVersion(), unsubscribeUrl };
                String content = application.getMessage("cms.message.email.subscription.content", contentArgs);

                // get list of user ids subscribed to the content (both itself and its parent)
                Collection subscribedUserIdList = new HashSet();
                Collection subscriptionList = cm.getSubscriptionsByContent(id, 0, -1);
                Collection parentSubscriptionList = cm.getSubscriptionsByContent(parentId, 0, -1);

                // get users with permissions
                Collection userIdList = new ArrayList();
                Collection userList = new ArrayList();
                if (subscriptionList.size() > 0 || parentSubscriptionList.size() > 0) {
                    userList = ss.getUsersByPermission(ContentManager.PERMISSION_SUBSCRIBE_CONTENT, Boolean.TRUE, null, false, 0, -1);
                    for (Iterator i=userList.iterator(); i.hasNext();) {
                        User u = (User)i.next();
                        userIdList.add(u.getId());
                    }
                }

                for (Iterator i=subscriptionList.iterator(); i.hasNext();) {
                    ContentSubscription cs = (ContentSubscription)i.next();
                    if (userIdList.contains(cs.getUserId())) {
                        subscribedUserIdList.add(cs.getUserId());
                    }
                }
                for (Iterator i=parentSubscriptionList.iterator(); i.hasNext();) {
                    ContentSubscription cs = (ContentSubscription)i.next();
                    if (userIdList.contains(cs.getUserId())) {
                        subscribedUserIdList.add(cs.getUserId());
                    }
                }

                if (subscribedUserIdList.size() > 0) {
                    // iterate and send email to users
                    for (Iterator j=userList.iterator(); j.hasNext();) {
                        User tmpUser = (User)j.next();
                        if (subscribedUserIdList.contains(tmpUser.getId())) {
                            String email = (String)tmpUser.getProperty("email1");
                            if (Mailer.isValidEmail(email)) {
                                MailUtil.sendEmail(null, true, null, email, subject, content);
                            }
                        }
                    }
                }
            }

        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error notifying subscription for content " + id, e);
        }

    }

}
