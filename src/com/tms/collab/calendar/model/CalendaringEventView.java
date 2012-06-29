package com.tms.collab.calendar.model;

import kacang.ui.Widget;
import kacang.ui.Forward;
import kacang.stdui.Form;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Sep 26, 2003
 * Time: 12:57:40 PM
 * To change this template use Options | File Templates.
 */
public interface CalendaringEventView extends Serializable
{

    public static final String DELETE_SUCCESSFUL = "ds";
    public static final String DELETE_FAILED = "df";


    public Widget getEventView();

    public Form getEventEdit();

    public Forward deleteEvent(String eventId,String userId);
}
