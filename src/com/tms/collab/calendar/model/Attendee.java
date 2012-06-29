package com.tms.collab.calendar.model;

import kacang.model.DefaultDataObject;


/**
 * Represents an attendee for a calendar event.
 * Properties for an attendee are:
 * <li>eventId - Unique identifier for the event</li>
 * <li>instanceId - An ID to identify specific recurrences of an event</li>
 * <li>status - Acceptance status for the attendee. Use constants in CalendarModule for this.</li>
 * <li>comments - Comments made by the attendee for the event.</li>
 * <li>attendeeId - ID to identify the attendee row.</li>
 * <li>userId - ID to identify the user.</li>
 * <li>compulsory - Indicates whether the attendee's attendance is compulsory.</li>
 */
public class Attendee extends DefaultDataObject implements Comparable {

    private String eventId;
    private String userId;
    private String status;
    private String comments;
    private String attendeeId;
    private String instanceId;
    private boolean compulsory;
    private boolean nonEKPUser; //for bussiness or personal contacts
    private boolean ekpUser; //for outside users


    /**
     * Returns a unique ID for the attendee, consisting of the event ID,
     * event instance ID and user ID.
     * @return the unique ID.
     */
    public String getId() {
        return getEventId() + "_" + getInstanceId() + "_" + getUserId();
    }

    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * Checks an object for equivalence.
     * @param obj
     * @return true if obj is an Attendee object are equal if their Object IDs are equal.
     */
    public boolean equals(Object obj) {
        return (obj instanceof Attendee) && getId().equals(((Attendee)obj).getId());
    }

    /**
     * Returns the full name of the attendee, ie the first name followed by the last name.
     * @return The full name.
     */
    public String getName() {
       String name= (String)getProperty("firstName");
    	if(getProperty("lastName")!=null)
    	name= name + " " + getProperty("lastName");
        return name;
    }

    /**
     * Compares this object to another based on the full name.
     * @param o
     * @return > 0 if object o attendee's full name is alphabetically greater, 0 if equivalent, -1 otherwise
     */
    public int compareTo(Object o) {
    	if (getName()== null)
    		return -1;
        return getName().compareTo(((Attendee)o).getName());
    }

    public String toString() {
        return getId().toString();
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public boolean isCompulsory() {
        return compulsory;
    }

    public void setCompulsory(boolean compulsory) {
        this.compulsory = compulsory;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    //for bussiness or personal contacts
	public boolean isNonEKPUser() {
		return nonEKPUser;
	}

	public void setNonEKPUser(boolean nonEKPUser) {
		this.nonEKPUser = nonEKPUser;
	}

	//for outside attendees
	public boolean isEkpUser() {
		return ekpUser;
	}

	public void setEkpUser(boolean ekpUser) {
		this.ekpUser = ekpUser;
	}
}
