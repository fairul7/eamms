package com.tms.collab.calendar.model;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Oct 6, 2003
 * Time: 2:40:37 PM
 * To change this template use Options | File Templates.
 */
public interface CalendaringPermission
{
    public boolean hasEditPermission(String userId);
    public boolean hasDeletePermission(String userId);
}
