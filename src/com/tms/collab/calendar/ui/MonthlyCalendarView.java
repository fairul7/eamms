package com.tms.collab.calendar.ui;

import kacang.ui.Event;
import kacang.ui.Forward;

/**
 * Represents a monthly calendar view.
 */
public class MonthlyCalendarView extends CalendarView {

    public MonthlyCalendarView() {
    }

    public String getDefaultTemplate() {
        return "cms/calendar/monthlyCalendarView";
    }

    public Forward onDateSelect(Event evt) {
        Forward f1 = super.onDateSelect(evt);
        Forward f2 = fireActionEvent(evt);
        return (f1 != null) ? f1 : f2;
    }

    public String getView() {
        return VIEW_MONTHLY;
    }

}
