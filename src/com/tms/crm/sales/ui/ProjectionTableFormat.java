package com.tms.crm.sales.ui;

import kacang.stdui.TableFormat;
import kacang.Application;
import com.tms.crm.sales.model.OpportunityModule;
import com.tms.crm.sales.model.OPPModule;

import java.util.Date;
import java.util.Calendar;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jun 30, 2004
 * Time: 2:08:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectionTableFormat implements TableFormat{

    private int year;

    public ProjectionTableFormat() {
    }

    public ProjectionTableFormat(int year) {
        this.year = year;
    }

    public String format(Object value) {
     /*   String userId = (String) value;
        OPPModule om = (OPPModule) Application.getInstance().getModule(OPPModule.class);
        Date date = new Date();
        Calendar  cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR,year);*/
        return NumberFormat.getInstance().format(value);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
