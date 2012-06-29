package com.tms.portlet.portlets.notes.model;

import kacang.model.DefaultDataObject;

import java.util.Date;
/**
 * Represents a notes.
 * Can also represent a reminder or a recurrence of an original event,
 * indicated by the methods isReminder() and isRecurrence().
 * <li>notesId - Unique identifier for the notes</li>
 * <li>userId - The user ID of the owner of the notes</li>
 * <li>content - Content of the notes</li>
 */

	
public class Notes extends DefaultDataObject implements Comparable, Cloneable  {
	
	private String notesId;  //unique identifier
	private String userId;	//owner userID
	private String content;  //notes content
	private Date lastModified; //notes last modified date 
	private Date creationDate;
	public Notes() {
	}
	
	public int compareTo(Object o) {
		return 0;
	}

	
	public void setNotesId(String notesId) {
		this.notesId = notesId;
	}
	
	public String getNotesId() {
		return this.notesId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserId() {
		return this.userId;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
	public Date getLastModified() {
		return this.lastModified;
	}

    public Date getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

}
