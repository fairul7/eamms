package com.tms.collab.forum.model;

import kacang.model.DefaultDataObject;

import java.util.Date;
import java.util.Collection;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: WaiHung
 * Date: Apr 29, 2003
 * Time: 1:26:11 PM
 * To change this template use Options | File Templates.
 */
public class Thread extends DefaultDataObject
{
    private String threadId;
    private String forumId;
    private String rootMessageId;
    private Date creationDate;
    private Date modificationDate;
    private String ownerId;
    private String email;
    private boolean isPublic = true;
    private boolean isActive = true;

    private String subject;
    private String content;
    private long numOfMessage;
    private Date lastPostDate;

    private Collection messageList = new ArrayList();

    public void setThreadId(String threadId)
    {
        this.threadId = threadId;
        this.setId(threadId);
    }

    public void setForumId(String forumId)
    {
        this.forumId = forumId;
    }

    public void setRootMessageId(String rootMessageId)
    {
        this.rootMessageId = rootMessageId;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    public void setModificationDate(Date modificationDate)
    {
        this.modificationDate = modificationDate;
    }

    public void setOwnerId(String ownerId)
    {
        this.ownerId = ownerId;
    }

    public void setIsPublic(boolean isPublic)
    {
        this.isPublic = isPublic;
    }

    public void setActive(boolean isActive)
    {
        this.isActive = isActive;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setNumOfMessage(long numOfMessage)
    {
        this.numOfMessage = numOfMessage;
    }

    public String getThreadId()
    {
        return threadId;
    }

    public String getForumId()
    {
        return forumId;
    }

    public String getRootMessageId()
    {
        return rootMessageId;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public Date getModificationDate()
    {
        return modificationDate;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public boolean getIsPublic()
    {
        return this.isPublic;
    }

    public boolean isActive()
    {
        return this.isActive;
    }

    public String getSubject()
    {
        return subject;
    }

    public String getContent()
    {
        return content;
    }

    public long getNumOfMessage()
    {
        return numOfMessage;
    }

    public Date getLastPostDate()
    {
        return lastPostDate;
    }

    public void setLastPostDate(Date lastPostDate)
    {
        this.lastPostDate = lastPostDate;
    }

    public void setStrLastPostDate(String strLastPostDate)
    {
        this.lastPostDate = DateTimeUtil.parse(strLastPostDate);
    }

    public String getId()
    {
        //return getThreadId() + getThreadId() + getOwnerId();
        return getThreadId();
    }

    public void addMessage(Message message)
    {
        messageList.add(message);
    }

    public Collection getChildren()
    {
        return messageList;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    
}

