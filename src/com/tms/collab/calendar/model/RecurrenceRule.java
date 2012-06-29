package com.tms.collab.calendar.model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Based on the Basic Recurrence Rule Grammar from the XAPIA's CSA specification.
 * Used in the vCalendar standard.
 */
public class RecurrenceRule {

    public static final int FREQUENCY_DAILY = 1;
    public static final int FREQUENCY_WEEKLY = 2;
    public static final int FREQUENCY_MONTHLY_BY_POSITION = 3;
    public static final int FREQUENCY_MONTHLY_BY_DAY = 4;
    public static final int FREQUENCY_YEARLY_BY_MONTH = 5;
    public static final int FREQUENCY_YEARLY_BY_DAY = 6;

    private String rule;
    private int duration = -1;  // no of occurances
    private int frequency; // type daily, weekly, monthly, yearly
    private int interval; // daily, every third day, etc
    private String modifier; // eg days, etc
    private Date endDate;

    public RecurrenceRule() {
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     *
     * @return Array of integers representing day of week (eg Sunday) specified in the frequency modifier.
     * Refer to the Calendar class for constant values.
     */
    public RecurrenceRuleModifier[] getModifiers() {
        return null;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


}
