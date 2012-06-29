package com.tms.collab.calendar.model;

import java.util.Collection;

/**
 * Exception thrown when calendar scheduling conflicts occur.
 * Call getConflictList() to retrieve a list of CalendarEvents
 * representing the conflicting events.
 */
public class ConflictException extends Exception {

    private Collection conflictList;
    private Collection resourcesList;
    public ConflictException() {
        super();
    }

    public ConflictException(String message) {
        super(message);
    }

    public Collection getConflictList() {
        return conflictList;
    }

    public void setConflictList(Collection conflictList) {
        this.conflictList = conflictList;
    }

    public Collection getResourcesList()
    {
        return resourcesList;
    }

    public void setResourcesList(Collection resourcesList)
    {
        this.resourcesList = resourcesList;
    }

}