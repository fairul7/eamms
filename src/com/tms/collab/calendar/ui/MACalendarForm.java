package com.tms.collab.calendar.ui;

import kacang.Application;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Jun 24, 2005
 * Time: 2:12:23 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MACalendarForm extends CalendarForm {
    public MACalendarForm() {

    }

    public MACalendarForm(String name) {
        super(name);   
    }

    public void init() {
        super.init();
        compulsory.setTemplate("kPopupSelectBox");
        optional.setTemplate("kPopupSelectBox");
        resources.setTemplate("kComboSelectBox");
        description.setRows("5");
        agenda.setRows("5");
        resources.setRows(5);
        notifyNote.setRows("5");
        // set recurrence to default
        recurrence.setOptionMap(new HashMap());
		recurrence.addOption("0", Application.getInstance().getMessage("emeeting.label.na"));
        recurrence.setSelectedOption("0");
        recurrence.setHidden(true);
        removeChild(everyDay);
        removeChild(everyWeekDay);
        removeChild(dailyFrequentTextField);
        removeChild(weeklyFrequentTextField);
        removeChild(monthlyFrequentTextField);
        removeChild(certainDayOfMonth);
        removeChild(weekDayOrder);
        removeChild(everyMonth);
        removeChild(weekDay);
        removeChild(monthlyFrequentTextField2);
        removeChild(yearlyFrequentTextField);
        removeChild(occurenceTimes);
        removeChild(occurenceTimesTF);
        removeChild(endOccurenceDate);
        removeChild(endOccurenceDateDF);
                
    }

    protected String getRecurrenceRule() {
        return "";
    }
}
