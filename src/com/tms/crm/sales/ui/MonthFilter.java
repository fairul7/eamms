package com.tms.crm.sales.ui;

import kacang.stdui.FormField;
import kacang.stdui.Form;
import kacang.ui.Forward;
import kacang.ui.Event;

import java.util.Date;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jun 25, 2004
 * Time: 2:59:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class MonthFilter extends FormField{
    public static final String SUFFIX_MONTH = "*month";
    public static final String SUFFIX_YEAR = "*year";
    private int month, year;


    public MonthFilter() {
        setDate(new Date());
    }

    public MonthFilter(String s) {
        super(s);
        setDate(new Date());
    }

    public MonthFilter(String s,Date date) {
        super(s);
        setDate(date);
    }

    public void setDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
    }

    public Date getFromDate(){
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setLenient(false);
        cal.set(Calendar.DAY_OF_MONTH,1);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.YEAR,year);
        return cal.getTime();
    }

    public Date getToDate(){
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setLenient(false);
        cal.set(Calendar.DAY_OF_MONTH,1);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.YEAR,year);
        cal.add(Calendar.MONTH,1);
        cal.add(Calendar.SECOND,-1);
        return cal.getTime();
    }
    public String getMonthName() {
        return getAbsoluteName() + SUFFIX_MONTH;
    }

    /**
     * Return the name for the HTML Year select box's name attribute.
     * @return The name.
     */
    public String getYearName() {
        return getAbsoluteName() + SUFFIX_YEAR;
    }

    public Forward onSubmit(Event evt) {
        try {
            // get parameters
            String monthStr = evt.getRequest().getParameter(getMonthName());
            String yearStr = evt.getRequest().getParameter(getYearName());
            month = Integer.parseInt(monthStr);
            year = Integer.parseInt(yearStr);
            // perform validation
            performValidation(evt);
            return null;
        } catch (Exception e) {
            setInvalid(true);
            Form form = getParentForm();
            if (form != null)
                form.setInvalid(true);
            return null;
        }
    }

    public String getDefaultTemplate() {
        return "sfa/monthfilter";
    }


    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
