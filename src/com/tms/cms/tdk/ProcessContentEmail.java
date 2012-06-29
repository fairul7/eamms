package com.tms.cms.tdk;

import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.core.model.ContentUtil;
import com.tms.util.MailUtil;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;
import kacang.util.Mailer;

import java.text.SimpleDateFormat;

public class ProcessContentEmail extends LightWeightWidget {

    private boolean invalid;
    private boolean sent;
    private boolean successful;

    public void onRequest(Event evt) {
        String action = evt.getRequest().getParameter("action");
        if ("Send".equals(action)) {

            String id = evt.getRequest().getParameter("id");
            String emailFrom = evt.getRequest().getParameter("emailfrom");
            String emailTo = evt.getRequest().getParameter("emailto");
            String comments = evt.getRequest().getParameter("comments");

            if (Mailer.isValidEmail(emailFrom) && Mailer.isValidEmail(emailTo)) {
                try {
                    // get content
                    Application application = Application.getInstance();
                    ContentPublisher cp = (ContentPublisher)application.getModule(ContentPublisher.class);
                    User user = ((SecurityService)application.getService(SecurityService.class)).getCurrentUser(evt.getRequest());
                    ContentObject co = cp.view(id, user);

                    // construct message
                    StringBuffer message = new StringBuffer();
                    message.append("<pre>");
                    if (comments != null && comments.trim().length() > 0) {
                        message.append(application.getMessage("article.label.emailCommentTitle", "{0}'s comments on this article", new Object[] { emailFrom }));
                        message.append(":\n");
                        message.append("<b>");
                        message.append(comments);
                        message.append("</b>");
                    }
                    message.append("\n\n---------------------------</pre>");

                    message.append("<h1>" + co.getName() + "</h1>");
                    message.append("<i>" + co.getAuthor() + " " + new SimpleDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()).format(co.getDate()) + "</i>");
                    message.append("<br><br>");
                    message.append(ContentUtil.makeAbsolute(evt.getRequest(), co.getContents()));

                    message.append("<pre>--- " + application.getMessage("article.label.end", "end") + " ---");
                    message.append("\n\n--- " + application.getMessage("article.label.information", "Article Information") + " ---\n");
                    String serverUrl = "http://" + evt.getRequest().getServerName() + ":" + evt.getRequest().getServerPort() + evt.getRequest().getContextPath() + "/cms";
                    message.append(application.getMessage("article.label.emailedFrom", "This article was emailed from {0}", new Object[] {"<a href=\"" + serverUrl + "\">" }));
                    message.append(serverUrl + "</a>.\n");
                    message.append(application.getMessage("article.label.url", "Article's URL") + ": <a href=\"" + serverUrl + "/content.jsp?id=" + co.getId() + "\">");
                    message.append(serverUrl + "/content.jsp?id=" + co.getId() + "</a>");
                    message.append("</pre>");

                    // send email
                    MailUtil.sendEmail(null, true, emailFrom, emailTo, co.getName(), message.toString());
                    sent = true;
                    successful = true;
                    invalid = false;
                }
                catch (Exception e) {
                    Log.getLog(getClass()).error("Unable to send article email: " + e.toString());
                    sent = true;
                    successful = false;
                    invalid = false;
                }
            }
            else {
                invalid = true;
                sent = false;
                successful = false;
            }
        }
    }

    public boolean isInvalid() {
        return invalid;
    }

    public boolean isSent() {
        return sent;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getDefaultTemplate() {
        return "cms/admin/processContentEmail";
    }

}
