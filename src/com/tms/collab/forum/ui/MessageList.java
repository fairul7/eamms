package com.tms.collab.forum.ui;

import com.tms.collab.forum.model.ForumException;
import com.tms.collab.forum.model.ForumModule;
import com.tms.collab.forum.model.Thread;
import kacang.Application;
import kacang.util.Log;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

import java.util.ArrayList;
import java.util.Collection;

public class MessageList extends LightWeightWidget
{
    public static final String DEFAULT_TEMPLATE = "forum/messageList";
    public static final String NO_PERMISSION_TEMPLATE = "forum/noPermission";

    private final static String DEFAULT_REC_PER_PAGE = "10";
    private final static int DEFAULT_PAGE_IN_GROUP = 5;

    private ForumModule forumModule = (ForumModule) Application.getInstance().getModule(ForumModule.class);
    private Collection postedMessages = new ArrayList();
    private Thread thread;
    private String forumName;
    private String forumDesc;
    private String userId;
    private String message;
    private String currentPage = "1";
    private boolean desc = false;
    private int msgPerPage = Integer.parseInt(DEFAULT_REC_PER_PAGE);

    public void onRequest(Event evt)
    {
        try
        {
            userId = evt.getWidgetManager().getUser().getId();
            thread = forumModule.getThread(evt.getRequest().getParameter("threadId"), userId);

            // check permission
            boolean hasPermission = forumModule.isForumUser(thread.getForumId(), userId);
            if (!hasPermission)
            {
                thread = null;
                return;
            }

            setForumName(forumModule.getForumName(thread.getForumId()));
            setForumDesc(forumModule.getForumDesc(thread.getForumId()));
            setCurrentPage(evt.getRequest().getParameter("currentPage"));
            postedMessages = getPage(evt.getRequest().getParameter("iPage"));
        }
        catch (Exception e)
        {
            Log.getLog(MessageList.class).error(e.getMessage(), e);
        }

    }

    public Collection getPostedMessages()
    {
        return postedMessages;
    }

    public void setPostedMessages(Collection postedMessages)
    {
        this.postedMessages = postedMessages;
    }

    public Thread getThread()
    {
        return thread;
    }

    public void setThread(Thread thread)
    {
        this.thread = thread;
    }

    public String getForumName()
    {
        return forumName;
    }

    public void setForumName(String forumName)
    {
        this.forumName = forumName;
    }

    public String getForumDesc()
    {
        return forumDesc;
    }

    public void setForumDesc(String forumDesc)
    {
        this.forumDesc = forumDesc;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public boolean isDesc()
    {
        return desc;
    }

    public void setDesc(boolean desc)
    {
        this.desc = desc;
    }

    public String getDefaultTemplate()
    {
        if (thread != null)
            return DEFAULT_TEMPLATE;
        else
            return NO_PERMISSION_TEMPLATE;
    }

    public int getCurrentPage()
    {
        try
        {
            return Integer.parseInt(currentPage);
        }
        catch(NumberFormatException e)
        {
            return 1;
        }
    }

    public void setCurrentPage(String currentPage)
    {
        try
        {
            Integer.parseInt(currentPage);
            this.currentPage = currentPage;
        }
        catch(NumberFormatException e)
        {
            this.currentPage = "1";
        }
    }

    public void setMsgPerPage(int idisplay)
    {
        msgPerPage = idisplay;
    }

    public Collection getPage(int iPageIndex)
    {
        Collection pageMsg = new ArrayList();
        try
        {
            iPageIndex = validatePageNo(iPageIndex);
            int iPageRec = (iPageIndex - 1) * msgPerPage;
            pageMsg = forumModule.getPostedMessages(thread.getThreadId(), userId, iPageRec, msgPerPage, "creationDate", desc);
        }
        catch (ForumException e)
        {
            Log.getLog(MessageList.class).error(e.getMessage(), e);
        }
        return pageMsg;
    }

    public Collection getPage(String sPageIndex)
    {
        if (sPageIndex == null)
        {
            return getPage(1);
        }
        else
        {
            return getPage(Integer.parseInt(sPageIndex));
        }
    }

    public int getGroupStartPage()
    {
        return getGroupStartPage(getCurrentPage());

    }

    protected int getGroupStartPage(int iPageIndex)
    {
        iPageIndex = validatePageNo(iPageIndex);
        return
                (int) ((Math.floor((double) (iPageIndex - 1) / (double) getPageInGroup())) * getPageInGroup()) + 1;
    }

    public int getGroupEndPage()
    {
        return getGroupEndPage(getCurrentPage());
    }

    protected int getGroupEndPage(int iPageIndex)
    {
        iPageIndex = validatePageNo(iPageIndex);
        int iPageEnd = getGroupStartPage(iPageIndex) + getPageInGroup() - 1;
        int iPageRec = 0;
        if (iPageEnd > getTotalPage())
        {
            iPageRec = getTotalPage();

        }
        else
        {
            iPageRec = iPageEnd;
        }
        return iPageRec;
    }

    public int getMsgPerPage()
    {
        return msgPerPage;
    }

    public int getTotalPage()
    {
        int totalMsg = 0;
        try
        {
            totalMsg = forumModule.getNumOfMessageByThread(thread.getThreadId(), null, null);
        }
        catch (ForumException e)
        {
            Log.getLog(MessageList.class).error(e.getMessage(), e);
        }
        if (totalMsg == 0)
        {
            return 0;
        }
        double iMaxPage = (double) totalMsg / (double) msgPerPage;
        return (int) (Math.ceil(iMaxPage));
    }

    public int getPageInGroup()
    {
        return DEFAULT_PAGE_IN_GROUP;
    }

    private int validatePageNo(int iPageIndex)
    {
        if (iPageIndex >= getTotalPage())
        {
            iPageIndex = getTotalPage();
        }
        else if (iPageIndex < 1)
        {
            iPageIndex = 1;
        }
        return iPageIndex;
    }
}
