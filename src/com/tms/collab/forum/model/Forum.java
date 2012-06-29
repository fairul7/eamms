package com.tms.collab.forum.model;

import kacang.model.DefaultDataObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: WaiHung
 * Date: Apr 29, 2003
 * Time: 12:42:36 PM
 * To change this template use Options | File Templates.
 */
public class Forum extends DefaultDataObject  implements Comparable
{

    private String forumId;
    private String name;
    private String description;
    private long numOfThread;
    private long numOfMessage;
    private String ownerId;
    private Date creationDate;
    private Date modificationDate;
    private boolean isPublic = true;
    private boolean isActive = true;
    private String assigneeId;
    private Date lastPostDate;
    private Map userGroup;
    private Map moderatorGroup;   
    private String category;
    //new
    private List users= new ArrayList();
    private List moderators= new ArrayList(); 
   
    public List getModerators() {
		return moderators;
	}

	public void setModerators(List moderators) {
		this.moderators = moderators;
	}

	public List getUsers() {
		return users;
	}

	public void setUsers(List users) {
		this.users = users;
	}

	public void setForumId(String forumId)
    {
        this.forumId = forumId;
        this.setId(forumId);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setNumOfThread(long numOfThread)
    {
        this.numOfThread = numOfThread;
    }

    public void setNumOfMessage(long numOfMessage)
    {
        this.numOfMessage = numOfMessage;
    }

    public void setOwnerId(String ownerId)
    {
        this.ownerId = ownerId;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    public void setModificationDate(Date modificationDate)
    {
        this.modificationDate = modificationDate;
    }

    public void setIsPublic(boolean isPublic)
    {
        this.isPublic = isPublic;
    }

    public void setActive(boolean isActive)
    {
        this.isActive = isActive;
    }

    public void setAssigneeId(String assigneeId)
    {
        this.assigneeId = assigneeId;
    }

    public void setUserGroup(Map userGroup)
    {
        this.userGroup = userGroup;
    }

   public String getForumId()
    {
        return forumId;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public long getNumOfThread()
    {
        return numOfThread;
    }

    public long getNumOfMessage()
    {
        return numOfMessage;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public Date getModificationDate()
    {
        return modificationDate;
    }

    public boolean getIsPublic()
    {
        return this.isPublic;
    }

    public boolean isActive()
    {
        return this.isActive;
    }

    public String getAssigneeId()
    {
        return assigneeId;
    }

    public Map getUserGroup()
    {
        return userGroup;
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
        return getForumId();
    }

    public Map getModeratorGroup()
    {
        return moderatorGroup;
    }

    public void setModeratorGroup(Map moderatorGroup)
    {
        this.moderatorGroup = moderatorGroup;
    }

    public int compareTo( Object o )
    {
        return compareTo( ( Forum ) o );
    }

    public int compareTo( Forum o )
    {
        return o.getCreationDate().compareTo(getCreationDate());
    }

    public boolean equals( Object o )
    {
        return equals( ( Forum ) o );
    }

    public boolean equals( Forum o )
    {
        return creationDate.equals( o.getCreationDate() );
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }
}
