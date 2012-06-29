package com.tms.collab.calendar.model;

import kacang.model.DefaultDataObject;

/**
 * Represents a resource for a calendar event.
 */
public class Resource extends DefaultDataObject {

    private String eventId;
    private String resourceId;
    private String name;
    private String description;

    public String getId() {
        return getEventId() + "_" + getResourceId();
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
