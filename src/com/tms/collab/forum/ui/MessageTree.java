package com.tms.collab.forum.ui;

import java.io.IOException;

import kacang.ui.LightWeightWidget;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import com.tms.collab.forum.model.ForumModule;
import com.tms.collab.forum.model.ForumException;
import com.tms.collab.forum.model.Thread;
import com.tms.collab.forum.model.Message;

public class MessageTree extends LightWeightWidget
{
    public static final String DEFAULT_TEMPLATE = "forum/messageTree";
    public static final String NO_PERMISSION_TEMPLATE = "forum/noPermission";

    private String threadId;
    private Thread thread;
    private Message message;
    private String forumName;
    private String forumDesc;
    private boolean moderator;
    private boolean direct=false;
    
	public boolean isDirect() {
		return direct;
	}

	public void setDirect(boolean direct) {
		this.direct = direct;
	}

	public boolean isModerator() {
		return moderator;
	}

	public void setModerator(boolean moderator) {
		this.moderator = moderator;
	}

	public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public Thread getThread() {
        return thread;
    }

    public Message getMessage() {
        return message;
    }

    public String getForumName() {
        return forumName;
    }

    public String getForumDesc() {
        return forumDesc;
    }

    public String getDefaultTemplate() {
        if (thread != null)
            return DEFAULT_TEMPLATE;
        else
            return NO_PERMISSION_TEMPLATE;
    }

    public void onRequest(Event evt) {
        try {
        	ForumModule forumMod = (ForumModule)Application.getInstance().getModule(ForumModule.class);
        	// get user ID
            String userId = evt.getWidgetManager().getUser().getId();
        	/*String delete = evt.getRequest().getParameter("delete");*/
        	// get thread ID
            String threadId = (getThreadId() != null) ? getThreadId() : evt.getRequest().getParameter("threadId");
            
            thread = forumMod.getPostedMessagesInThreadedFormat(threadId, userId);
            
            // check permission
            boolean hasPermission = forumMod.isForumUser(thread.getForumId(), userId);
            if (!hasPermission)
            {
                thread = null;
                return;
            }

            forumName = forumMod.getForumName(thread.getForumId());
            forumDesc = forumMod.getForumDesc(thread.getForumId());
            // get message
        	String messageId = evt.getRequest().getParameter("messageId");
            if (messageId != null) {
                message = forumMod.getPostedMessage(messageId, userId);  
               /* if(delete!=null && "true".equals(delete)){
                	forumMod.deleteForum(message);
                	direct=true;
                }
                if(!direct){
                moderator=forumMod.isForumModeratorCheck(thread.getForumId(), userId);
                String ownerid=message.getOwnerId();
                if(ownerid.indexOf("(anonymous)")==-1)
                moderator=false;}*/
            }
            

            
            
            
            
            

        } catch (ForumException e) {
            Log.getLog(getClass()).error("Unable to retrieve forum message tree " + e.toString(), e);
        }
    }

}
