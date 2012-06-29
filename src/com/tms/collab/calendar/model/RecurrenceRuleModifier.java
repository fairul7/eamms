package com.tms.collab.calendar.model;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Aug 15, 2003
 * Time: 2:34:57 PM
 * To change this template use Options | File Templates.
 */
public class RecurrenceRuleModifier {

    /* private int[] values;
    private int position;*/
    private List weekDay;
    private List position;
    // private boolean backwards;

    public RecurrenceRuleModifier(){
        weekDay = new ArrayList(0);
        position = new ArrayList(0);
    }

    public boolean hasWeekDay()
    {
        return(weekDay.size()>0);
    }

    public boolean hasPosition(){
        return (position.size()>0);
    }

    public void addWeekDay(String weekday){
        weekDay.add(weekday);
    }

    public void addPosition(String pos){
        position.add(pos);
    }

    public List getWeekDay()
    {
        return weekDay;
    }

    public List getPosition()
    {
        return position;
    }
    /*   public int[] getValues() {
    return values;
    }

    public void setValues(int[] values) {
    this.values = values;
    }

    public int getPosition() {
    return position;
    }

    public void setPosition(int position) {
    this.position = position;
    }*/

    /*    public boolean isBackwards() {
    return backwards;
    }

    public void setBackwards(boolean backwards) {
    this.backwards = backwards;
    }*/
}

