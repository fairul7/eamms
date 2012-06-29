package com.tms.collab.forum.ui;

import com.tms.collab.forum.model.ForumModule;
import com.tms.collab.forum.model.Message;
import com.tms.collab.forum.model.Thread;
import kacang.Application;
import kacang.stdui.Panel;
import kacang.stdui.Table;
import kacang.ui.Event;
import kacang.ui.EventListener;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.util.Log;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: WaiHung
 * Date: May 9, 2003
 * Time: 8:07:43 PM
 * To change this template use Options | File Templates.
 */
public class AdminForumPanel extends Panel implements EventListener
{
    public static final String DEFAULT_TEMPLATE = "forum/adminForumPanel";

    private AdminForumTable adminForumTable;
    private AdminThreadTable adminThreadTable;
    private AdminMessageTable adminMessageTable;
    private CreateForumForm createForumForm;
    private EditForumForm editForumForm;
    private EditThreadForm  editThreadForm;
    private EditMessageForm editMessageForm;

    public AdminForumPanel()
    {
    }

    public AdminForumPanel(String name)
    {
        super(name);
    }

    public void init()
    {
        removeChildren();
        try
        {
            adminForumTable = new AdminForumTable("adminForumTable");
            adminThreadTable = new AdminThreadTable("adminThreadTable");
            adminMessageTable = new AdminMessageTable("adminMessageTable");
            createForumForm = new CreateForumForm("createForumForm");
            editForumForm = new EditForumForm("editForumForm");
            editThreadForm = new EditThreadForm("editThreadForm");
            editMessageForm = new EditMessageForm("editMessageForm");

            addChild(adminForumTable);
            addChild(adminThreadTable);
            addChild(adminMessageTable);
            addChild(createForumForm);
            addChild(editForumForm);
            addChild(editThreadForm);
            addChild(editMessageForm);

            adminForumTable.init();
            adminThreadTable.init();
            adminMessageTable.init();
            createForumForm.init();
            editForumForm.init();
            editThreadForm.init();
            editMessageForm.init();

            adminForumTable.setHidden(false);
            adminThreadTable.setHidden(true);
            adminMessageTable.setHidden(true);
            createForumForm.setHidden(true);
            editForumForm.setHidden(true);
            editThreadForm.setHidden(true);
            editMessageForm.setHidden(true);

            adminForumTable.addEventListener(this);
            adminThreadTable.addEventListener(this);
            adminMessageTable.addEventListener(this);
            createForumForm.addEventListener(this);
            editForumForm.addEventListener(this);
            editThreadForm.addEventListener(this);
            editMessageForm.addEventListener(this);
        }
        catch(Exception e)
        {
            Log log = Log.getLog(this.getClass());
            log.error(e.getMessage(), e);
        }
    }

    public Forward actionPerformed(Event evt)
    {
        ForumLocationIndicator locIndicator = (ForumLocationIndicator)evt.getWidgetManager().getWidget("forumAdminPage.forumLocationIndicator");
        if(locIndicator == null)
            locIndicator = new ForumLocationIndicator("forumLocationIndicator");

        Widget widget = evt.getWidget();
        HttpServletRequest req = evt.getRequest();
        Log log = Log.getLog(this.getClass());

        try
        {
	        Application application = Application.getInstance();
            log.debug("~~~ widget = " + widget.getName());
            if(widget.getName().equals(this.getName()))
            {
                if(evt.getType().equals(adminForumTable.getName()))
                {
                    adminForumTable.setHidden(false);
                    adminThreadTable.setHidden(true);
                    adminMessageTable.setHidden(true);
                    createForumForm.setHidden(true);
                    editForumForm.setHidden(true);
                    editThreadForm.setHidden(true);
                    editMessageForm.setHidden(true);

                    locIndicator.getForumListing().setHidden(true);
                    locIndicator.getCurrentForum().setHidden(true);
                    locIndicator.getCurrentForumLabel().setHidden(true);
                    locIndicator.getCurrentThread().setHidden(true);
                    locIndicator.getCurrentThreadLabel().setHidden(true);
                    locIndicator.getCurrentMessageLabel().setHidden(true);
                }
                else if(evt.getType().equals(createForumForm.getName()))
                {
                    adminForumTable.setHidden(true);
                    adminThreadTable.setHidden(true);
                    adminMessageTable.setHidden(true);
                    editForumForm.setHidden(true);
                    editThreadForm.setHidden(true);
                    editMessageForm.setHidden(true);
                    createForumForm.init();
                    createForumForm.setHidden(false);

                    locIndicator.getForumListing().setHidden(true);
                    locIndicator.getCurrentForum().setHidden(true);
                    locIndicator.getCurrentForumLabel().setHidden(true);
                    locIndicator.getCurrentThread().setHidden(true);
                    locIndicator.getCurrentThreadLabel().setHidden(true);
                    locIndicator.getCurrentMessageLabel().setHidden(true);
                }
            }
            else if(widget.getName().equals(adminForumTable.getName()))
            {
                Table table = (Table) widget;
                log.debug("~~~ evt.getType() = " + evt.getType());
                if(evt.getType().equals(Table.PARAMETER_KEY_ACTION))
                {
                    if(table.getSelectedAction() != null && table.getSelectedAction().equalsIgnoreCase("add"))
                    {
                        adminForumTable.setHidden(true);
                        adminThreadTable.setHidden(true);
                        editForumForm.setHidden(true);
                        editThreadForm.setHidden(true);
                        adminMessageTable.setHidden(true);
                        editMessageForm.setHidden(true);
                        createForumForm.init();
                        createForumForm.setHidden(false);

                        req.setAttribute("location", application.getMessage("forum.label.addNewForum", "Add New Forum"));
                    }
                    else if(table.getSelectedAction() != null && table.getSelectedAction().equalsIgnoreCase("delete"))
                    {
                        adminForumTable.setHidden(false);
                        adminThreadTable.setHidden(true);
                        createForumForm.setHidden(true);
                        editForumForm.setHidden(true);
                        editThreadForm.setHidden(true);
                        adminMessageTable.setHidden(true);
                        editMessageForm.setHidden(true);
                    }
                }
                else if(evt.getType().equals(Table.PARAMETER_KEY_SELECTION))
                {
                    String forumId = evt.getRequest().getParameter("forumId");
                    String id = evt.getRequest().getParameter("id");

                    if(id != null && !id.trim().equals(""))
                    {
                        log.debug("~~~ id = " + id);

                        adminThreadTable.setForumId(id);
                        adminThreadTable.init();
                        adminThreadTable.setHidden(false);

                        adminForumTable.setHidden(true);
                        editForumForm.setHidden(true);
                        editThreadForm.setHidden(true);
                        createForumForm.setHidden(true);
                        adminMessageTable.setHidden(true);
                        editMessageForm.setHidden(true);

                        locIndicator.getForumListing().setHidden(false);
                        locIndicator.getCurrentForum().setHidden(true);
                        locIndicator.getCurrentThreadLabel().setHidden(true);

                        ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
                        locIndicator.getCurrentForumLabel().setText(forumModule.getForumName(id));
                        locIndicator.getCurrentForumLabel().setHidden(false);

                        req.setAttribute("location", application.getMessage("forum.label.topicsListing", "Topics Listing"));
                    }
                    else if(forumId != null && !forumId.trim().equals(""))
                    {
                        log.debug("~~~ forumId = " + forumId);
                        editForumForm.setForumId(forumId);
                        editForumForm.init();
                        editForumForm.setHidden(false);

                        adminForumTable.setHidden(true);
                        adminThreadTable.setHidden(true);
                        editThreadForm.setHidden(true);
                        createForumForm.setHidden(true);
                        adminMessageTable.setHidden(true);
                        editMessageForm.setHidden(true);

                        locIndicator.getForumListing().setHidden(false);
                        locIndicator.getCurrentForumLabel().setHidden(true);
                        locIndicator.getCurrentThreadLabel().setHidden(true);

                        ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
                        locIndicator.getCurrentForum().setText(forumModule.getForumName(forumId));
                        locIndicator.getCurrentForum().setParameter("forumId", forumId);
                        locIndicator.getCurrentForum().setHidden(false);

                        req.setAttribute("location", application.getMessage("forum.label.editForum", "Edit Forum"));
                    }
                }
                else if(evt.getType().equals(Table.PARAMETER_KEY_SORT))
                {
                    if(adminForumTable.isHidden())
                    {
                        editForumForm.setHidden(true);
                        adminForumTable.setHidden(false);
                        adminThreadTable.setHidden(true);
                        editThreadForm.setHidden(true);
                        createForumForm.setHidden(true);
                        adminMessageTable.setHidden(true);
                        editMessageForm.setHidden(true);
                    }
                }
            }
            else if(widget.getName().equals(adminThreadTable.getName()))
            {
                Table table = (Table) widget;
                if(evt.getType().equals(Table.PARAMETER_KEY_ACTION))
                {
                    log.debug("~~~ table.getSelectedAction() = " + table.getSelectedAction());
                    if(table.getSelectedAction()!=null && table.getSelectedAction().equalsIgnoreCase("delete"))
                    {
                        adminForumTable.setHidden(true);
                        adminThreadTable.setHidden(false);
                        createForumForm.setHidden(true);
                        editForumForm.setHidden(true);
                        editThreadForm.setHidden(true);
                        locIndicator.getForumListing().setHidden(false);
                        req.setAttribute("location", application.getMessage("forum.label.topicsListing", "Topics Listing"));
                    }
                }
                else if(evt.getType().equals(Table.PARAMETER_KEY_SELECTION))
                {
                    String threadId = evt.getRequest().getParameter("threadId");
                    String id = evt.getRequest().getParameter("id");

                    if(threadId != null && !threadId.trim().equals(""))
                    {
                        log.debug("~~~ threadId = " + threadId);
                        editThreadForm.setThreadId(threadId);
                        editThreadForm.init();
                        editForumForm.setHidden(true);
                        adminForumTable.setHidden(true);
                        adminThreadTable.setHidden(true);
                        editThreadForm.setHidden(false);
                        createForumForm.setHidden(true);


                        locIndicator.getForumListing().setHidden(false);
                        locIndicator.getCurrentForumLabel().setHidden(true);
                        locIndicator.getCurrentThreadLabel().setHidden(true);
                        locIndicator.getCurrentMessageLabel().setHidden(true);

                        ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);

                        String forumId = forumModule.getForumId(threadId, Thread.class.getName());
                        locIndicator.getCurrentForum().setText(forumModule.getForumName(forumId));
                        locIndicator.getCurrentForum().setParameter("forumId", forumId);
                        locIndicator.getCurrentForum().setHidden(false);

                        locIndicator.getCurrentThread().setText(editThreadForm.getSubject().getText());
                        locIndicator.getCurrentThread().setParameter("threadId", threadId);
                        locIndicator.getCurrentThread().setHidden(false);

                        req.setAttribute("location", application.getMessage("forum.label.editTopic", "Edit Topic"));
                    }
                    else if(id != null && !id.trim().equals(""))
                    {
                        log.debug("~~~~~~~~~ id = " + id);
                        adminMessageTable.setThreadId(id);
                        adminMessageTable.init();
                        adminMessageTable.setHidden(false);

                        adminForumTable.setHidden(true);
                        editForumForm.setHidden(true);
                        editThreadForm.setHidden(true);
                        createForumForm.setHidden(true);
                        adminThreadTable.setHidden(true);
                        editMessageForm.setHidden(true);

                        locIndicator.getForumListing().setHidden(false);
                        locIndicator.getCurrentForumLabel().setHidden(true);
                        locIndicator.getCurrentThread().setHidden(true);
                        locIndicator.getCurrentMessageLabel().setHidden(true);

                        ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);

                        String forumId = forumModule.getForumId(id, Thread.class.getName());
                        locIndicator.getCurrentForum().setText(forumModule.getForumName(forumId));
                        locIndicator.getCurrentForum().setParameter("forumId", forumId);
                        locIndicator.getCurrentForum().setHidden(false);

                        locIndicator.getCurrentThreadLabel().setText(forumModule.getThreadSubject(id));
                        locIndicator.getCurrentThreadLabel().setHidden(false);

                        req.setAttribute("location", application.getMessage("forum.label.messageListing", "Message Listing"));

                    }

                }
                else if(evt.getType().equals(Table.PARAMETER_KEY_SORT))
                {
                    if(adminThreadTable.isHidden())
                    {
                        editForumForm.setHidden(true);
                        adminForumTable.setHidden(true);
                        adminThreadTable.setHidden(false);
                        editThreadForm.setHidden(true);
                        createForumForm.setHidden(true);
                        adminMessageTable.setHidden(true);
                        editMessageForm.setHidden(true);
                    }

                }
            }
            else if(widget.getName().equals(adminMessageTable.getName()))
            {
                Table table = (Table) widget;
                if(evt.getType().equals(Table.PARAMETER_KEY_ACTION))
                {
                    log.debug("~~~ table.getSelectedAction() = " + table.getSelectedAction());
                    if(table.getSelectedAction()!=null && table.getSelectedAction().equalsIgnoreCase("delete"))
                    {
                        adminMessageTable.setHidden(false);
                        adminForumTable.setHidden(true);
                        editForumForm.setHidden(true);
                        editThreadForm.setHidden(true);
                        createForumForm.setHidden(true);
                        adminThreadTable.setHidden(true);
                        editMessageForm.setHidden(true);
                        locIndicator.getForumListing().setHidden(false);
                        req.setAttribute("location", application.getMessage("forum.label.messagesListing", "Messages Listing"));
                    }
                }
                else if(evt.getType().equals(Table.PARAMETER_KEY_SELECTION))
                {
                    String messageId = evt.getRequest().getParameter("messageId");

                    editMessageForm.setMessageId(messageId);
                    editMessageForm.init();
                    editMessageForm.setHidden(false);

                    adminMessageTable.setHidden(true);
                    adminForumTable.setHidden(true);
                    editForumForm.setHidden(true);
                    editThreadForm.setHidden(true);
                    createForumForm.setHidden(true);
                    adminThreadTable.setHidden(true);

                    locIndicator.getForumListing().setHidden(false);
                    locIndicator.getCurrentForumLabel().setHidden(true);
                    locIndicator.getCurrentThreadLabel().setHidden(true);

                    ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);

                    String forumId = forumModule.getForumId(messageId, Message.class.getName());
                    String threadId = forumModule.getThreadId(messageId);

                    locIndicator.getCurrentForum().setText(forumModule.getForumName(forumId));
                    locIndicator.getCurrentForum().setParameter("forumId", forumId);
                    locIndicator.getCurrentForum().setHidden(false);

                    locIndicator.getCurrentThread().setText(forumModule.getThreadSubject(threadId));
                    locIndicator.getCurrentThread().setParameter("threadId", threadId);
                    locIndicator.getCurrentThread().setHidden(false);

                    locIndicator.getCurrentMessageLabel().setText(forumModule.getMessageSubject(messageId));
                    locIndicator.getCurrentMessageLabel().setHidden(false);

                    req.setAttribute("location", application.getMessage("forum.label.editMessage", "Edit Message"));
                }
                else if(evt.getType().equals(Table.PARAMETER_KEY_SORT))
                {
                    if(adminMessageTable.isHidden())
                    {
                        editForumForm.setHidden(true);
                        adminForumTable.setHidden(true);
                        adminThreadTable.setHidden(true);
                        editThreadForm.setHidden(true);
                        createForumForm.setHidden(true);
                        adminMessageTable.setHidden(false);
                        editMessageForm.setHidden(true);
                    }
                }

            }
            else if(widget.getName().equals(createForumForm.getName()))
            {
                String buttonClicked = createForumForm.findButtonClicked(evt);
                log.debug("~~~ button clicked = " + buttonClicked);
                if(buttonClicked.equalsIgnoreCase(createForumForm.getCancel().getAbsoluteName()) ||
                        (!createForumForm.isInvalid() && buttonClicked.equalsIgnoreCase(createForumForm.getCreateForum().getAbsoluteName())) )
                {
                    adminForumTable.setHidden(false);
                    adminThreadTable.setHidden(true);
                    editForumForm.setHidden(true);

                    createForumForm.getForumName().setInvalid(false);
                    createForumForm.getUserGroup().setInvalid(false);
                    createForumForm.setHidden(true);
                    createForumForm.setInvalid(false);

                    editThreadForm.setHidden(true);
                    adminMessageTable.setHidden(true);
                    editMessageForm.setHidden(true);


                    locIndicator.getForumListing().setHidden(true);
                    locIndicator.getCurrentForum().setHidden(true);
                    locIndicator.getCurrentForumLabel().setHidden(true);
                    locIndicator.getCurrentThreadLabel().setHidden(true);

                    req.setAttribute("location", application.getMessage("forum.label.forumListing", "Forum Listing"));
                }
            }
            else if(widget.getName().equals(editForumForm.getName()))
            {
                String buttonClicked = editForumForm.findButtonClicked(evt);
                log.debug("~~~ button clicked = " + buttonClicked);
                if(buttonClicked.equalsIgnoreCase(editForumForm.getCancel().getAbsoluteName()) ||
                        (!editForumForm.isInvalid() && buttonClicked.equalsIgnoreCase(editForumForm.getUpdateForum().getAbsoluteName())) )
                {
                    adminForumTable.setHidden(false);
                    adminThreadTable.setHidden(true);
                    editForumForm.setHidden(true);
                    editThreadForm.setHidden(true);
                    createForumForm.setHidden(true);
                    adminMessageTable.setHidden(true);
                    editMessageForm.setHidden(true);

                    locIndicator.getForumListing().setHidden(true);
                    locIndicator.getCurrentForum().setHidden(true);
                    locIndicator.getCurrentForumLabel().setHidden(true);
                    locIndicator.getCurrentThreadLabel().setHidden(true);

                    req.setAttribute("location", application.getMessage("forum.label.forumListing", "Forum Listing"));

                }
            }
            else if(widget.getName().equals(editThreadForm.getName()))
            {
                String buttonClicked = editThreadForm.findButtonClicked(evt);
                if(buttonClicked.equalsIgnoreCase(editThreadForm.getUpdateThread().getAbsoluteName()) || buttonClicked.equalsIgnoreCase(editThreadForm.getCancel().getAbsoluteName()))
                {
                    adminForumTable.setHidden(true);
                    adminThreadTable.setHidden(false);
                    editForumForm.setHidden(true);
                    editThreadForm.setHidden(true);
                    createForumForm.setHidden(true);
                    adminMessageTable.setHidden(true);
                    editMessageForm.setHidden(true);
                    locIndicator.getForumListing().setHidden(false);
                    locIndicator.getCurrentForumLabel().setHidden(false);
                    locIndicator.getCurrentForum().setHidden(true);
                    locIndicator.getCurrentThreadLabel().setHidden(true);
                    locIndicator.getCurrentThread().setHidden(true);
                    req.setAttribute("location", application.getMessage("forum.label.topicsListing", "Topics Listing"));
                }
            }
            else if(widget.getName().equals(editMessageForm.getName()))
            {
                String buttonClicked = editMessageForm.findButtonClicked(evt);
                if(buttonClicked.equalsIgnoreCase(editMessageForm.getUpdateMessage().getAbsoluteName()) || buttonClicked.equalsIgnoreCase(editMessageForm.getCancel().getAbsoluteName()))
                {
                    adminMessageTable.setHidden(false);
                    adminForumTable.setHidden(true);
                    editForumForm.setHidden(true);
                    editThreadForm.setHidden(true);
                    createForumForm.setHidden(true);
                    adminThreadTable.setHidden(true);
                    editMessageForm.setHidden(true);

                    locIndicator.getForumListing().setHidden(false);
                    locIndicator.getCurrentForumLabel().setHidden(true);
                    locIndicator.getCurrentThread().setHidden(true);
                    locIndicator.getCurrentMessageLabel().setHidden(true);
                    locIndicator.getCurrentForum().setHidden(false);
                    locIndicator.getCurrentThreadLabel().setHidden(false);

                    req.setAttribute("location", application.getMessage("forum.label.messageListing", "Message Listing"));
                }
            }
        }
        catch(Exception e)
        {
            log.error(e.getMessage(), e);
        }
        return new Forward();
    }

    public void setAdminForumTable(AdminForumTable adminForumTable)
    {
        this.adminForumTable = adminForumTable;
    }

    public void setCreateForumForm(CreateForumForm createForumForm)
    {
        this.createForumForm = createForumForm;
    }

    public void setEditForumForm(EditForumForm editForumForm)
    {
        this.editForumForm = editForumForm;
    }

    public void setAdminThreadTable(AdminThreadTable adminThreadTable)
    {
        this.adminThreadTable = adminThreadTable;
    }

    public void setEditThreadForm(EditThreadForm editThreadForm)
    {
        this.editThreadForm = editThreadForm;
    }

    public AdminForumTable getAdminForumTable()
    {
        return adminForumTable;
    }

    public CreateForumForm getCreateForumForm()
    {
        return createForumForm;
    }

    public EditForumForm getEditForumForm()
    {
        return editForumForm;
    }

    public AdminThreadTable getAdminThreadTable()
    {
        return adminThreadTable;
    }

    public EditThreadForm getEditThreadForm()
    {
        return editThreadForm;
    }

    public AdminMessageTable getAdminMessageTable()
    {
        return adminMessageTable;
    }

    public void setAdminMessageTable(AdminMessageTable adminMessageTable)
    {
        this.adminMessageTable = adminMessageTable;
    }

    public EditMessageForm getEditMessageForm()
    {
        return editMessageForm;
    }

    public void setEditMessageForm(EditMessageForm editMessageForm)
    {
        this.editMessageForm = editMessageForm;
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }
}
