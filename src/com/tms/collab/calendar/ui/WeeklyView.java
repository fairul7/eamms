package com.tms.collab.calendar.ui;

import kacang.stdui.Button;
import kacang.ui.Event;
import kacang.ui.Forward;

import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Dec 11, 2003
 * Time: 12:31:21 PM
 * To change this template use Options | File Templates.
 */
public class WeeklyView extends CalendarView
{
    private Button nextButton, prevButton/*, thisButton*/;

    public WeeklyView()
    {
    }

    public WeeklyView(String name)
    {
        super(name);
    }

    public void onRequest(Event evt)
    {
        //super.onRequest(evt);
        setUserIds(new String[]{getWidgetManager().getUser().getId()});
        //getWeeklyEntries();
    }

    public Forward actionPerformed(Event evt)
    {
        //super.actionPerformed(evt);
        String eventType = evt.getRequest().getParameter(Event.PARAMETER_KEY_EVENT_TYPE);
        if (PARAMETER_KEY_DATE_SELECT.equals(eventType)) {
            int month = Integer.parseInt(evt.getRequest().getParameter(getMonthName()));
            int day = Integer.parseInt(evt.getRequest().getParameter(getDayOfMonthName()));
            int year = Integer.parseInt(evt.getRequest().getParameter(getYearName()));
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDate());
            cal.set(Calendar.MONTH,month);
            cal.set(Calendar.DAY_OF_MONTH,day);
            cal.set(Calendar.YEAR,year);
            evt.getRequest().setAttribute("selectedDate",cal.getTime());
            evt.getRequest().setAttribute("dayOfMonth",new Integer(day));
            return new Forward("dateselected");
        }
        else{
            return super.actionPerformed(evt);
        }
    }

    public void init()
    {
        //super.init();
        setView(VIEW_WEEKLY);


    }

    public String getDefaultTemplate()
    {
        return "calendar/weeklyview";
    }

   public Button getNextButton()
    {
        return nextButton;
    }

    public void setNextButton(Button nextButton)
    {
        this.nextButton = nextButton;
    }

    public Button getPrevButton()
    {
        return prevButton;
    }

    public void setPrevButton(Button prevButton)
    {
        this.prevButton = prevButton;
    }

    /*public Button getThisButton()
    {
        return thisButton;
    }

    public void setThisButton(Button thisButton)
    {
        this.thisButton = thisButton;
    }

*/
}
