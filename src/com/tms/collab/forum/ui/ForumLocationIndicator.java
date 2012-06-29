package com.tms.collab.forum.ui;

import com.tms.collab.forum.model.ForumModule;
import com.tms.collab.forum.model.Thread;
import kacang.Application;
import kacang.stdui.Label;
import kacang.stdui.Link;
import kacang.ui.Event;
import kacang.ui.EventListener;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.util.Log;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: WaiHung
 * Date: May 30, 2003
 * Time: 10:44:24 AM
 * To change this template use Options | File Templates.
 */
public class ForumLocationIndicator extends Widget implements EventListener
{
    public static final String DEFAULT_TEMPLATE = "forum/forumLocationIndicator";

    private Link forumListing;
    private Link currentForum;
    private Label currentForumLabel;
    private Link currentThread;
    private Label currentThreadLabel;
    private Label currentMessageLabel;

    public ForumLocationIndicator()
    {
    }

    public ForumLocationIndicator(String name)
    {
        super(name);
    }

    public void init()
    {
        removeChildren();
        try
        {
	        Application application = Application.getInstance();
            forumListing = new Link("forumListing");
            forumListing.setText(application.getMessage("general.label.forums", "Forums"));
            currentForum = new Link("currentForum");
            currentForumLabel = new Label("currentForumLabel");
            currentThread = new Link("currentThread");
            currentThreadLabel = new Label("currentThreadLabel");
            currentMessageLabel = new Label("currentMessageLabel");

            addChild(forumListing);
            addChild(currentForum);
            addChild(currentForumLabel);
            addChild(currentThread);
            addChild(currentThreadLabel);
            addChild(currentMessageLabel);

            forumListing.init();
            currentForum.init();
            currentForumLabel.init();
            currentThread.init();
            currentThreadLabel.init();
            currentMessageLabel.init();

            forumListing.setHidden(true);
            currentForum.setHidden(true);
            currentForumLabel.setHidden(true);
            currentThread.setHidden(true);
            currentThreadLabel.setHidden(true);
            currentMessageLabel.setHidden(true);

            forumListing.addEventListener(this);
            currentForum.addEventListener(this);
            currentForumLabel.addEventListener(this);
            currentThread.addEventListener(this);
            currentThreadLabel.addEventListener(this);
            currentMessageLabel.addEventListener(this);
        }
        catch(Exception e)
        {
            Log log = Log.getLog(this.getClass());
            log.error(e.getMessage(), e);
        }
    }

    public Forward actionPerformed(Event evt)
    {
	    Application application = Application.getInstance();
        Log log = Log.getLog(this.getClass());
        AdminForumPanel adminForumPanel = (AdminForumPanel)evt.getWidgetManager().getWidget("forumAdminPage.forumAdminPortlet.adminForumPanel");
        if(adminForumPanel == null)
            adminForumPanel = new AdminForumPanel("adminForumPanel");

        Widget widget = evt.getWidget();
        HttpServletRequest req = evt.getRequest();

        try
        {
            log.debug("~~~ widget = " + widget.getName());
            if(widget.getName().equals(adminForumPanel.getName()))
            {
                if(evt.getType().equals("adminForumTable"))
                {
                    forumListing.setHidden(true);
                    currentForum.setHidden(true);
                    currentForumLabel.setHidden(true);
                    currentThreadLabel.setHidden(true);
                    currentThread.setHidden(true);
                    currentMessageLabel.setHidden(true);
                }
                else if(evt.getType().equals("createForumForm"))
                {
                    forumListing.setHidden(true);
                    currentForum.setHidden(true);
                    currentForumLabel.setHidden(true);
                    currentThreadLabel.setHidden(true);
                    currentThread.setHidden(true);
                    currentMessageLabel.setHidden(true);
                }
            }
            else if(widget.getName().equals(forumListing.getName()))
            {
                adminForumPanel.getAdminForumTable().setHidden(false);
                adminForumPanel.getAdminThreadTable().setHidden(true);
                adminForumPanel.getAdminMessageTable().setHidden(true);
                adminForumPanel.getCreateForumForm().setHidden(true);
                adminForumPanel.getEditForumForm().setHidden(true);
                adminForumPanel.getEditThreadForm().setHidden(true);
                adminForumPanel.getEditMessageForm().setHidden(true);
                forumListing.setHidden(true);
                currentForum.setHidden(true);
                currentForumLabel.setHidden(true);
                currentThreadLabel.setHidden(true);
                currentThread.setHidden(true);
                currentMessageLabel.setHidden(true);

                req.setAttribute("location", application.getMessage("interactive.label.forumsListing", "Forums Listing"));

            }
            else if(widget.getName().equals(currentForum.getName()))
            {
                String forumId = evt.getRequest().getParameter("forumId");

                if(forumId != null && !forumId.trim().equals(""))
                {
                    log.debug("~~~ forumId = " + forumId);

                    adminForumPanel.getAdminThreadTable().setForumId(forumId);
                    adminForumPanel.getAdminThreadTable().init();
                    adminForumPanel.getAdminThreadTable().setHidden(false);

                    adminForumPanel.getEditForumForm().setHidden(true);
                    adminForumPanel.getAdminForumTable().setHidden(true);
                    adminForumPanel.getAdminMessageTable().setHidden(true);
                    adminForumPanel.getEditThreadForm().setHidden(true);
                    adminForumPanel.getCreateForumForm().setHidden(true);
                    adminForumPanel.getEditMessageForm().setHidden(true);
                    forumListing.setHidden(false);
                    currentForum.setHidden(true);
                    currentThreadLabel.setHidden(true);
                    currentThread.setHidden(true);
                    currentMessageLabel.setHidden(true);

                    ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
                    currentForumLabel.setText(forumModule.getForumName(forumId));
                    currentForumLabel.setHidden(false);

                    req.setAttribute("location", application.getMessage("interactive.label.topicListing", "Topic Listing"));

                }
            }
            else if(widget.getName().equals(currentThread.getName()))
            {
                String threadId = evt.getRequest().getParameter("threadId");

                if(threadId != null && !threadId.trim().equals(""))
                {
                    adminForumPanel.getAdminMessageTable().setThreadId(threadId);
                    adminForumPanel.getAdminMessageTable().init();
                    adminForumPanel.getAdminMessageTable().setHidden(false);

                    adminForumPanel.getEditThreadForm().setHidden(true);
                    adminForumPanel.getAdminForumTable().setHidden(true);
                    adminForumPanel.getAdminThreadTable().setHidden(true);
                    adminForumPanel.getEditForumForm().setHidden(true);
                    adminForumPanel.getCreateForumForm().setHidden(true);
                    adminForumPanel.getEditMessageForm().setHidden(true);
                    forumListing.setHidden(false);
                    currentForumLabel.setHidden(true);
                    currentThread.setHidden(true);
                    currentMessageLabel.setHidden(true);

                    ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
                    String forumId = forumModule.getForumId(threadId, Thread.class.getName());
                    currentForum.setText(forumModule.getForumName(forumId));
                    currentForum.setParameter("forumId", forumId);
                    currentForum.setHidden(false);

                    currentThreadLabel.setText(forumModule.getThreadSubject(threadId));
                    currentThreadLabel.setHidden(false);

                    req.setAttribute("location", application.getMessage("interactive.label.messageListing", "Message Listing"));

                }

            }

        }
        catch(Exception e)
        {
            log.error(e.getMessage(), e);
        }
        return new Forward();
    }


    public Link getForumListing()
    {
        return forumListing;
    }

    public void setForumListing(Link forumListing)
    {
        this.forumListing = forumListing;
    }

    public Link getCurrentForum()
    {
        return currentForum;
    }

    public void setCurrentForum(Link currentForum)
    {
        this.currentForum = currentForum;
    }

    public Label getCurrentForumLabel()
    {
        return currentForumLabel;
    }

    public void setCurrentForumLabel(Label currentForumLabel)
    {
        this.currentForumLabel = currentForumLabel;
    }

    public Label getCurrentThreadLabel()
    {
        return currentThreadLabel;
    }

    public void setCurrentThreadLabel(Label currentThreadLabel)
    {
        this.currentThreadLabel = currentThreadLabel;
    }

    public Link getCurrentThread()
    {
        return currentThread;
    }

    public void setCurrentThread(Link currentThread)
    {
        this.currentThread = currentThread;
    }

    public Label getCurrentMessageLabel()
    {
        return currentMessageLabel;
    }

    public void setCurrentMessageLabel(Label currentMessageLabel)
    {
        this.currentMessageLabel = currentMessageLabel;
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }


}
