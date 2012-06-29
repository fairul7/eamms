package com.tms.hr.leave.model;

import com.tms.hr.employee.model.EmployeeDataObject;
import kacang.model.DataObjectNotFoundException;
import kacang.util.Log;
import kacang.Application;

import java.util.*;

/**
 * Performs calculations for leave entitlement, balance, etc.
 * This is the default implementation. You can implement a new implementation
 * by extending this class and overriding the appropriate methods, and
 * defining the application property "hr.leave.calculator" with the value of
 * fully qualified class name.
 * <p>
 * If using this class standalone, you must set LeaveModule and LeaveSettings
 * before performing calculations.
 */
public class LeaveCalculator {

    private LeaveSettings settings;
    private Map employeeMap; // Map of employeeID -> EmployeeDataObjects

    private boolean fixedCalendar;

    public LeaveModule getModule() {
        Application app = Application.getInstance();
        LeaveModule module = (LeaveModule)app.getModule(LeaveModule.class);
        return module;
    }

    public LeaveSettings getSettings() {
        return settings;
    }

    public void setSettings(LeaveSettings settings) {
        this.settings = settings;
    }

    public Map getEmployeeMap() {
        return employeeMap;
    }

    public void setEmployeeMap(Map employeeMap) {
        this.employeeMap = employeeMap;
    }

    public LeaveCalculator() {
    }

    /**
     * Calculates the entitlement for a type, user and year.
     * @param leaveType
     * @param userId
     * @param year
     * @return
     */
    public float calculateLeaveEntitlement(String leaveType, String userId, int year) throws DataObjectNotFoundException, LeaveException {
        try {
            LeaveModule module = getModule();

            // determine user service class and years of service
            Map map = module.viewEmployeeServiceInfo(userId);
            Date dateJoined = (Date)map.get("dateJoined");
            Date dateResigned = (Date)map.get("dateResigned");
            String serviceClass = (String)map.get("serviceClass");
            Calendar calJoined = Calendar.getInstance();
            calJoined.setTime(dateJoined);
            int yearJoined = calJoined.get(Calendar.YEAR);
/*
            int yearsOfService = year - yearJoined + 1;
*/
            int yearsOfService = calculateServiceYears(year, dateJoined, dateResigned);
            if (yearsOfService <= 0) {
                return 0;
            }
            int yearResigned = 0;
            Calendar calResigned = Calendar.getInstance();
            if (dateResigned != null) {
                calResigned.setTime(dateResigned);
                yearResigned = calResigned.get(Calendar.YEAR);
                if (yearResigned < year) {
                    return 0;
                }
            }

            // get entitlement info
            Collection entitlementList = module.viewLeaveEntitlementList(serviceClass, leaveType, year);

            // determine appropriate entitlement
            float entitlementDays = 0;
            for (Iterator i=entitlementList.iterator(); i.hasNext();) {
                LeaveEntitlement le = (LeaveEntitlement)i.next();
                long serviceYears = le.getServiceYears();
                entitlementDays = le.getEntitlement();
                if (yearsOfService <= serviceYears) {
                    break;
                }
            }

            // pro rata based on join date
            boolean proRata = settings.isProRata();
            if (proRata && (year == yearJoined || year == yearResigned)) {
                // calculate based on day
                Calendar calYear = Calendar.getInstance();
                calYear.set(Calendar.YEAR, year);
                int daysInYear = calYear.getMaximum(Calendar.DAY_OF_YEAR);
                int workDays = 0;

                if (yearJoined == yearResigned) {
                    workDays = calculateDaysBetween(year, dateJoined, dateResigned) + 1;
                }
                else if (year == yearJoined) {
                    workDays = daysInYear - calJoined.get(Calendar.DAY_OF_YEAR) + 1;
                }
                else if (year == yearResigned) {
                    workDays = calResigned.get(Calendar.DAY_OF_YEAR);
                }
                float entitlementPerDay = entitlementDays / daysInYear;
                entitlementDays = entitlementPerDay * workDays;
            }

            return entitlementDays;
        }
        catch (DataObjectNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error calculating leave entitlement for user " + userId, e);
            throw new LeaveException("Error calculating leave entitlement for user " + userId, e);
        }
    }

    /**
     * Calculates the start and end dates for the specified year.
     * @param year
     * @param startDate
     * @param endDate
     */
    public void calculateYearStartEndDates(int year, Date startDate, Date endDate) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, Calendar.JANUARY, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        startDate.setTime(cal.getTime().getTime());
        cal.set(year, Calendar.DECEMBER, 31);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        endDate.setTime(cal.getTime().getTime());
    }

    /**
     * Calculates the number of credited leave for the year
     * @param leaveType
     * @param userId
     * @param year
     * @return
     */
    public float calculateLeaveCredited(String leaveType, String userId, int year) throws LeaveException {
        Date startDate = new Date();
        Date endDate = new Date();
        LeaveModule module = getModule();
        calculateYearStartEndDates(year, startDate, endDate);

        float days = 0;
        Collection creditList = module.viewCreditLeaveList(startDate, endDate, new String[] { userId }, null, leaveType, new String[] { LeaveModule.STATUS_APPROVED }, null, null, false, 0, -1);
        for (Iterator i=creditList.iterator(); i.hasNext();) {
            LeaveEntry entry = (LeaveEntry)i.next();

            // calc days between
            float daysBetween = calculateActualDaysForCreditLeave(year, entry, true);

            days += daysBetween;
        }
        return days;
    }

    /**
     * Calculates the difference between 2 dates within the current year.
     * If start date is before 1 Jan, then 1 Jan is used.
     * If end date is after 31 Dec, then 31 Dec is used.
     * @param year
     * @param first
     * @param second
     * @return
     */
    public int calculateDaysBetween(int year, Date first, Date second) {
        Calendar d1 = Calendar.getInstance();
        d1.setTime(first);
        Calendar d2 = Calendar.getInstance();
        d2.setTime(second);
        if (d1.after(d2)) {  // swap dates so that d1 is start and d2 is end
            java.util.Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        if (d1.get(Calendar.YEAR) < year) {
            d1.set(year, Calendar.JANUARY, 1);
        }
        if (d2.get(Calendar.YEAR) > year) {
            d2.set(year, Calendar.DECEMBER, 31);
        }
        int days = d2.get(java.util.Calendar.DAY_OF_YEAR) - d1.get(java.util.Calendar.DAY_OF_YEAR);
        return days;
    }

    /**
     * Calculates the leave deducted to date (submitted, approved)
     * @param leaveType
     * @param userId
     * @param year
     * @return
     */
    public float calculateLeaveDeducted(String leaveType, String userId, int year) throws LeaveException {

        LeaveModule module = getModule();
        Date startDate = new Date();
        Date endDate = new Date();
        calculateYearStartEndDates(year, startDate, endDate);

        float days = 0;

        // get entries
        Collection entryList = module.viewLeaveList(startDate, endDate, new String[] { userId }, null, leaveType, new String[] { LeaveModule.STATUS_SUBMITTED, LeaveModule.STATUS_APPROVED, LeaveModule.STATUS_CANCEL_SUBMITTED, LeaveModule.STATUS_CANCEL_REJECTED }, null, null, false, 0, -1);

        // calc days
        for (Iterator i=entryList.iterator(); i.hasNext();) {
            LeaveEntry entry = (LeaveEntry)i.next();

            // calc days between
            float actualDays = calculateActualDaysForLeave(year, entry, false);
            days += actualDays;
        }
        return days;
    }

    /**
     * Returns the number of adjustments for the year
     * @param leaveType
     * @param userId
     * @param year
     * @return
     */
    public float calculateAdjustments(String leaveType, String userId, int year) throws LeaveException {
        Date startDate = new Date();
        Date endDate = new Date();
        LeaveModule module = getModule();

        calculateYearStartEndDates(year, startDate, endDate);

        float days = 0;
        Collection adjList = module.viewAdjustmentList(startDate, endDate, new String[] { userId }, null, leaveType, new String[] { LeaveModule.STATUS_APPROVED }, null, null, false, 0, -1);
        for (Iterator i=adjList.iterator(); i.hasNext();) {
            LeaveEntry entry = (LeaveEntry)i.next();

            // calc days between
            Float duration = entry.getDays();
            if (duration != null) {
                days += duration.floatValue();
            }
        }
        return days;
    }

    /**
     * Calculates the amount of days carried forward from the previous year
     * @param leaveType
     * @param userId
     * @param year The current year
     * @return
     */
    public float calculateCarryForward(String leaveType, String userId, int year) throws LeaveException {
        try {
            LeaveModule module = getModule();
            float result = module.viewCarryForward(leaveType, userId, year);
            float carryForward = calculateCarryForward(result);
            return carryForward;
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving leave carry forward", e);
            throw new LeaveException("Error retrieving leave carry forward", e);
        }
    }

    /**
     * Calculates the allowable amount of days carried forward from the balance
     * @param balance
     * @return
     */
    public float calculateCarryForward(float balance) throws LeaveException {
        try {
            // check against maximum carry forward
            float maxCarryForward = 0;
            try {
                maxCarryForward = Float.parseFloat(settings.getCarryForwardMaxDays());
            }
            catch (Exception e) {
                Log.getLog(getClass()).debug("Error parsing settings carry forward max day " + settings.getCarryForwardMaxDays());
            }
            float carryForward = Math.min(balance, maxCarryForward);

            return carryForward;
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving leave carry forward", e);
            throw new LeaveException("Error retrieving leave carry forward", e);
        }
    }

    /**
     * Calculates the actual number of days to deduct, excluding weekends and holidays
     * @param year
     * @param entry
     * @param alwaysRecalculate Force to always recalculate
     * @return
     */
    public float calculateActualDaysForLeave(int year, LeaveEntry entry, boolean alwaysRecalculate ) throws LeaveException {

        LeaveModule module = getModule();
        boolean realTime = settings.isRealTime();
        boolean isShiftWorker = false;
        if (settings.isAllowShift()) {
            Map employeeMap = getEmployeeMap();
            if (employeeMap != null) {
                EmployeeDataObject edo = (EmployeeDataObject)employeeMap.get(entry.getUserId());
                if (edo != null) {
                    isShiftWorker = edo.isShiftWorker();
                }
            }
        }

        if (!realTime && !alwaysRecalculate) { // grab from entry days

            Float f = entry.getDays();
            return (f != null) ? f.floatValue() : 0;

        }
        else { // calculate on the fly

            // set appropriate start and end dates, including ensuring within same year
            Date start = entry.getStartDate();
            Date end = entry.getEndDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            if (cal.get(Calendar.YEAR) < year) {
                cal.set(year, Calendar.JANUARY, 1);
                start = cal.getTime();
            }
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(end);
            endCal.set(Calendar.HOUR_OF_DAY, 23);
            endCal.set(Calendar.MINUTE, 59);
            endCal.set(Calendar.SECOND, 59);
            if (endCal.get(Calendar.YEAR) > year) {
                endCal.set(year, Calendar.DECEMBER, 31);
                end = endCal.getTime();
            }

            float actualDays = 0;
            float daysBetween = (float)calculateDaysBetween(year, start, end) + 1;
            float weekendDays = 0;
            float holidays = 0;

            // check for half day
            if (entry.isHalfDay() && daysBetween == 1.0f) {
                daysBetween = 0.5f;
            }

            if (isShiftWorker) {
                // shift worker, ignore weekends and holidays
                actualDays = daysBetween;
            }
            else { // non-shift workers
                // check for weekends and holidays
                boolean fridayWeekend = settings.isFridayWeekend();
                boolean alternateWeekend = settings.isAlternateWeekend();
                String workingDays = settings.getWorkingDays(); // supports only 5, 5.5 or 6
                int primaryRestDay = (fridayWeekend) ? Calendar.FRIDAY : Calendar.SUNDAY;
                int secondaryRestDay = ("6".equals(workingDays)) ? -1 : Calendar.SATURDAY;

                if ("5.5".equals(workingDays)) { // 5 and 1/2 day week

                    // count weekends
                    while(cal.before(endCal)) {
                        if (cal.get(Calendar.DAY_OF_WEEK) == primaryRestDay) {
                            weekendDays++;
                        }
                        else if (cal.get(Calendar.DAY_OF_WEEK) == secondaryRestDay) {
                            if (alternateWeekend) {
                                if (cal.get(Calendar.WEEK_OF_MONTH) == 1 || cal.get(Calendar.WEEK_OF_MONTH) == 3) {
                                    weekendDays += 0.5f;
                                }
                            }
                            else {
                                weekendDays += 0.5f;
                            }
                        }
                        cal.add(Calendar.DAY_OF_YEAR, 1);
                    }

                    // count holidays
                    Collection holidayList = module.viewHolidayList(start, end, null, "date", false, 0, -1);
                    Calendar hol = Calendar.getInstance();
                    for (Iterator i=holidayList.iterator(); i.hasNext();) {
                        LeaveHoliday holiday = (LeaveHoliday)i.next();
                        hol.setTime(holiday.getDate());
                        if (hol.get(Calendar.DAY_OF_WEEK) == primaryRestDay) {
                            ;
                        }
                        else if (hol.get(Calendar.DAY_OF_WEEK) == secondaryRestDay) {
                            holidays += 0.5f;
                        }
                        else {
                            holidays++;
                        }
                    }

                    if(fixedCalendar)
                    actualDays = daysBetween;
                    else
                    // calc actual
                    actualDays = daysBetween - weekendDays - holidays;

                }
                else { // 5 or 6 day week

                    // count weekends
                    while(cal.before(endCal)) {
                        if (cal.get(Calendar.DAY_OF_WEEK) == primaryRestDay) {
                            weekendDays++;
                        }
                        else if (cal.get(Calendar.DAY_OF_WEEK) == secondaryRestDay) {
                            if (alternateWeekend) {
                                if (cal.get(Calendar.WEEK_OF_MONTH) == 1 || cal.get(Calendar.WEEK_OF_MONTH) == 3) {
                                    weekendDays++;
                                }
                            }
                            else {
                                weekendDays++;
                            }
                        }
                        cal.add(Calendar.DAY_OF_YEAR, 1);
                    }

                    // count holidays
                    Collection holidayList = module.viewHolidayList(start, end, null, "date", false, 0, -1);
                    Calendar hol = Calendar.getInstance();
                    for (Iterator i=holidayList.iterator(); i.hasNext();) {
                        LeaveHoliday holiday = (LeaveHoliday)i.next();
                        hol.setTime(holiday.getDate());
                        if (hol.get(Calendar.DAY_OF_WEEK) != primaryRestDay && hol.get(Calendar.DAY_OF_WEEK) != secondaryRestDay) {
                            holidays++;
                        }
                    }

                   if(fixedCalendar)
                   actualDays = daysBetween;
                    else
                    // calc actual
                    actualDays = daysBetween - weekendDays - holidays;
                }
            } // end non-shift worker

            if (actualDays < 0) {
                actualDays = 0;
            }

            return -actualDays;
        }

    }

    /**
     * Calculates the actual number of days to deduct, excluding weekends and holidays
     * @param year
     * @param entry
     * @param alwaysRecalculate Force to always recalculate, no effect in this implementation
     * @return
     */
    public float calculateActualDaysForCreditLeave(int year, LeaveEntry entry, boolean alwaysRecalculate) throws LeaveException {
        // calc days between
        Date start = entry.getStartDate();
        Date end = entry.getEndDate();
        float daysBetween = (float)calculateDaysBetween(year, start, end) + 1;

        // check for half day
        if (entry.isHalfDay() && daysBetween == 1.0f) {
            daysBetween = 0.5f;
        }

        return daysBetween;
    }

    /**
     * Calculates the balance for based on the values provided
     * @param entitlementDays
     * @param carryForward
     * @param creditedDays
     * @param takenDays
     * @param adjustments
     * @return
     */
    public float calculateBalance(float entitlementDays, float carryForward, float creditedDays, float takenDays, float adjustments) {
        float balance = entitlementDays + carryForward + creditedDays + takenDays + adjustments;
        return balance;
    }

    /**
     * Utility method to calculate the number of service years
     * @param joinDate Start date
     * @param resignationDate End date
     * @return number of years
     */
    public int calculateServiceYears(int year, Date joinDate, Date resignationDate) {

        Calendar calStart = Calendar.getInstance();
        calStart.setTime(joinDate);

        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);
        if (year != current.get(Calendar.YEAR)) {
            current.set(Calendar.MONTH, Calendar.DECEMBER);
            current.set(Calendar.DAY_OF_MONTH, 31);
        }
        current.set(Calendar.YEAR, year);

        Calendar calEnd = (Calendar)current.clone();
        if (resignationDate == null || resignationDate.after(current.getTime())) {
            calEnd.setTime(current.getTime());
        }
        else {
            calEnd.setTime(resignationDate);
        }
        calEnd.set(Calendar.HOUR_OF_DAY, 0);
        calEnd.set(Calendar.MINUTE, 0);
        calEnd.set(Calendar.SECOND, 0);

        if (year != currentYear && calStart.after(calEnd)) {
            return 0;
        }
        else if (year != currentYear && calStart.after(current)) {
            return 0;
        }

        int years = 0;
        while (calStart.before(calEnd) || calStart.equals(calEnd)) {
            years++;
            calStart.add(Calendar.YEAR, 1);
        }

        if (years == 0) {
            years = 1;
        }
        return years;
    }

    public void fixedCalendar(boolean fixedCalendar){

        this.fixedCalendar = fixedCalendar;
    }

}
