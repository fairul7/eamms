package com.tms.collab.forum.model;

import kacang.model.DefaultDataObject;

import java.util.Date;
import java.util.Collection;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: WaiHung
 * Date: Apr 29, 2003
 * Time: 1:53:42 PM
 * To change this template use Options | File Templates.
 */
public class Message extends DefaultDataObject
{
    private String messageId;
    private String parentMessageId;
    private String threadId;
    private String forumId;
    private String email;
    private String ownerId;
    private String subject;
    private String content;
    private Date creationDate;
    private Date modificationDate;
    private Collection messageList = new ArrayList();

    
    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setMessageId(String messageId)
    {
        this.messageId = messageId;
    }

    public void setParentMessageId(String parentMessageId)
    {
        this.parentMessageId = parentMessageId;
    }

    public void setThreadId(String threadId)
    {
        this.threadId = threadId;
    }

    public void setForumId(String forumId)
    {
        this.forumId = forumId;
    }

    public void setOwnerId(String ownerId)
    {
        this.ownerId = ownerId;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    public void setModificationDate(Date modificationDate)
    {
        this.modificationDate = modificationDate;
    }

    public String getMessageId()
    {
        return messageId;
    }

    public String getParentMessageId()
    {
        return parentMessageId;
    }

    public String getThreadId()
    {
        return threadId;
    }

    public String getForumId()
    {
        return forumId;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public String getSubject()
    {
        return subject;
    }

    public String getContent()
    {
        return content;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public Date getModificationDate()
    {
        return modificationDate;
    }

    public String getId()
    {
        return getForumId() + getThreadId() + getMessageId() + getOwnerId();
    }

    public void addMessage(Message message)
    {
        messageList.add(message);
    }

    public Collection getChildren()
    {
        return messageList;
    }
}
