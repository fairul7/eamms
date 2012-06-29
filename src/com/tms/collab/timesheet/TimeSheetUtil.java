package com.tms.collab.timesheet;

//import kacang.services.security.SecurityService;
import kacang.Application;

import java.util.Collection;
import java.util.ArrayList;

//import com.tms.collab.timesheet.model.TimeSheetModule;
//import com.tms.collab.project.WormsHandler;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Apr 29, 2005
 * Time: 2:50:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetUtil {

    public static int WORKING_HOUR_PER_DAY=8;
    public static final String TIMESHEET_PERMISSION="com.tms.collab.timesheet.ViewProjects";

    /**
     * return default collection of working days
     * @return workingDay of type Collection
     */
    public static Collection getWorkingDays() {
        Collection workingDay = new ArrayList();
        workingDay.add("1");
        workingDay.add("2");
        workingDay.add("3");
        workingDay.add("4");
        workingDay.add("5");
        workingDay.add("6");
        workingDay.add("7");
        return workingDay;
    }

    public static String getMonthDescription(int iMonth) {
        String sRet = "";
        switch (iMonth) {
            case 1 : {
                    sRet = Application.getInstance().getMessage("general.label.january");
                break;
            }
            case 2 : {
                sRet =  Application.getInstance().getMessage("general.label.february");
                break;
            }
            case 3 : {
                sRet =  Application.getInstance().getMessage("general.label.march");
                break;
            }
            case 4 : {
                sRet =  Application.getInstance().getMessage("general.label.april");
                break;
            }
            case 5 : {
                sRet =  Application.getInstance().getMessage("general.label.may");
                break;
            }
            case 6 : {
                sRet =  Application.getInstance().getMessage("general.label.june");
                break;
            }
            case 7 : {
                sRet =  Application.getInstance().getMessage("general.label.july");
                break;
            }
            case 8 : {
                sRet =  Application.getInstance().getMessage("general.label.august");
                break;
            }
            case 9 : {
                sRet =  Application.getInstance().getMessage("general.label.september");
                break;
            }
            case 10 : {
                sRet =  Application.getInstance().getMessage("general.label.october");
                break;
            }
            case 11 : {
                sRet =  Application.getInstance().getMessage("general.label.november");
                break;
            }
            case 12 : {
                sRet =  Application.getInstance().getMessage("general.label.december");
                break;
            }
        }
        return sRet;
    }

    /**
     * get last day of a month
     */
    public static int getLastDayOfMonth(int iMonth, int iYear) {
        int iRet=0;
        switch(iMonth) {
            case 0 : {
                iRet=31;
                break;
            }
            case 1 : {
                if ((iYear % 4)==0)
                    iRet=29;
                else
                    iRet=28;
                break;
            }
            case 2 : {
                iRet=31;
                break;
            }
            case 3 : {
                iRet=30;
                break;
            }
            case 4 : {
                iRet=31;
                break;
            }
            case 5 : {
                iRet=30;
                break;
            }
            case 6 : {
                iRet=31;
                break;
            }
            case 7 : {
                iRet=31;
                break;
            }
            case 8 : {
                iRet=30;
                break;
            }
            case 9 : {
                iRet=31;
                break;
            }
            case 10 : {
                iRet=30;
                break;
            }
            case 11 : {
                iRet=31;
                break;
            }
        }
        return iRet;
    }

    /**
     * get working hour per day
     */
    public static int getTimeSheetWorkingHour() {
        int iWorkingHourPerDay = WORKING_HOUR_PER_DAY;
        try {
            String sWorkingHour = Application.getInstance().getProperty("com.tms.collab.timesheetWorkingHour");
            Integer obj = new Integer(sWorkingHour);
            if (obj.intValue()!=0)
                iWorkingHourPerDay = obj.intValue();
        }
        catch(Exception e) {}
        return iWorkingHourPerDay;
    }
}
