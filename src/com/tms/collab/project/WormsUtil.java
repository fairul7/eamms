package com.tms.collab.project;

import kacang.Application;

import java.util.Collection;
import java.util.Date;
import java.util.Calendar;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;

public class WormsUtil
{
	/**
	 * Calculates the number of working days from within a date range
	 * @param working A collection of string objects signifying the working days. Sunday = 1 and Saturday = 7.
	 * @param start Start of the date range
	 * @param end End of the date range
	 * @return  number of working days
	 */
	public static int getWorkingDays(Collection working, Date start, Date end)
	{
		/* Verifying compulsory parameters */
		if(working == null || working.size() == 0)
			return 0;
		if(start == null || end == null)
			return 0;

		int days = 0;
		Calendar calStart = Calendar.getInstance();
		Calendar current = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		calStart.setTime(start);
		current.setTime(start);
		calEnd.setTime(end);
        for(int i=0; i<=(getDaysBetween(calStart.getTime(), calEnd.getTime())); i++)
		{
            if(working.contains(String.valueOf(current.get(Calendar.DAY_OF_WEEK))))
				days++;
			current.add(Calendar.DAY_OF_YEAR, 1);
		}
		return days;
	}

	/**
	 * Returns the number of days between 2 dates
	 * @param start Start date
	 * @param end End date
	 * @return number of days between the dates
	 */
	public static int getDaysBetween (Date start, Date end)
	{
		Calendar calStart = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		calStart.setTime(start);
		calEnd.setTime(end);

        if (calStart.after(calEnd))
		{
			/* Swap dates so that d1 is start and d2 is end */
            Calendar swap = calStart;
            calStart = calEnd;
            calEnd = swap;
        }
        int days = calEnd.get(Calendar.DAY_OF_YEAR) - calStart.get(java.util.Calendar.DAY_OF_YEAR);
        int y2 = calEnd.get(Calendar.YEAR);
        if (calStart.get(Calendar.YEAR) != y2)
		{
            calStart = (Calendar) calStart.clone();
            do
			{
                days += calStart.getActualMaximum(Calendar.DAY_OF_YEAR);
                calStart.add(Calendar.YEAR, 1);
            }
			while (calStart.get(Calendar.YEAR) != y2);
        }
        return days;
    }
	
	/**
	 * Returns the number of Months between 2 dates 
	 * @param start Start date
	 * @param end End date
	 * @return number of days between the dates
	 */
	public static int getMonthsBetween (Date start, Date end)
	{
		Calendar calStart = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		calStart.setTime(start);
		calEnd.setTime(end);

        if (calStart.after(calEnd))
		{
			/* Swap dates so that d1 is start and d2 is end */
            Calendar swap = calStart;
            calStart = calEnd;
            calEnd = swap;
        }
        int months = calEnd.get(Calendar.MONTH) - calStart.get(java.util.Calendar.MONTH);
        int y2 = calEnd.get(Calendar.YEAR);
        if (calStart.get(Calendar.YEAR) != y2)
		{
            calStart = (Calendar) calStart.clone();
            do
			{
            	months += 12;
                calStart.add(Calendar.YEAR, 1);
            }
			while (calStart.get(Calendar.YEAR) != y2);
        }
        return months;
    }
	
	/**
	 * Returns the number of Weeks between 2 dates 
	 * @param start Start date
	 * @param end End date
	 * @return number of days between the dates
	 */
	public static int getWeeksBetween (Date start, Date end)
	{
		Calendar calStart = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		calStart.setTime(start);
		calEnd.setTime(end);

        if (calStart.after(calEnd))
		{
			/* Swap dates so that d1 is start and d2 is end */
            Calendar swap = calStart;
            calStart = calEnd;
            calEnd = swap;
        }
        int months = calEnd.get(Calendar.WEEK_OF_YEAR) - calStart.get(java.util.Calendar.WEEK_OF_YEAR);
        int y2 = calEnd.get(Calendar.YEAR);
        if (calStart.get(Calendar.YEAR) != y2)
		{
            calStart = (Calendar) calStart.clone();
            do
			{
            	months += calStart.getActualMaximum(Calendar.WEEK_OF_YEAR);
                calStart.add(Calendar.YEAR, 1);
            }
			while (calStart.get(Calendar.YEAR) != y2);
        }
        return months;
    }

	/**
	 * Utility method to determine if a date is before the compare date. This method compares only the days and
	 * ignores the time
	 * @param date Date to compare with
	 * @param compare Date to compare against
	 * @return true if date is before compare. false otherwise
	 */
	public static boolean before(Date date, Date compare)
	{
        if (compare == null)
            return true;
        Calendar start = Calendar.getInstance();
        start.setTime(date);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        Calendar end = Calendar.getInstance();
        end.setTime(compare);
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        return start.getTime().before(end.getTime());
    }

	/**
	 * Utility method to determine if a date is after the compare date. This method compares only the days and
	 * ignores the time
	 * @param date Date to compare with
	 * @param compare Date to compare against
	 * @return true if date is after compare. false otherwise
	 */
	public static boolean after(Date date, Date compare)
	{
        if (compare == null)
            return true;
        Calendar start = Calendar.getInstance();
        start.setTime(date);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        Calendar end = Calendar.getInstance();
        end.setTime(compare);
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        return start.getTime().after(end.getTime());
    }

    /**
     * utility method to get currency type from system property
     * @return map of currency type
     */

    public static Map getCurrencyType() {
        String allCurrencyType = Application.getInstance().getProperty("com.tms.worms.currencyType");
        String[] cur = allCurrencyType.split(",");

        Map map = new SequencedHashMap();
        // add - Please Select - tag
        map.put("",Application.getInstance().getMessage("project.label.selectCurrencyType"));

        for (int i=0; i<cur.length; i++) {
            map.put(cur[i],cur[i]);
        }
        return map;
    }
}
