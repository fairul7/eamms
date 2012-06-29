package com.tms.crm.sales.misc;

import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
	/* -------------------------------------------------- */
	/* Date creation */
	/* -------------------------------------------------- */
	
	/**
	 * Note: month is zero based.
	 */
	public static Date getDate(int year, int month, int date, int hour, int minute, int second) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setLenient(false);
		cal.clear();
		
		Date retDate = null;
		try {
			cal.set(year, month, date, hour, minute, second);
			retDate = cal.getTime();
		} catch (Exception e) { }
		return(retDate);
	}
	
	/**
	 * Note: month is zero based.
	 */
	public static Date getDate(int year, int month, int date) {
		return(getDate(year, month, date, 0, 0, 0));
	}
	
	/**
	 * Note: month is zero based.
	 */
	public static Date getDate(String year, String month, String date) {
		Date retDate = null;
		try {
			retDate = getDate(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(date));
		} catch (NumberFormatException e) { }
		return(retDate);
	}
	
	/**
	 * Returns the current date and time.
	 */
	public static Date getDate() {
		GregorianCalendar cal = new GregorianCalendar();
		return(cal.getTime());
	}
	
	/**
	 * Returns the current date without the time
	 */
	public static Date getToday() {
		return(getDateOnly(getDate()));
	}
	
	/**
	 * Please refer to <code>java.util.Calendar.add(int field, int amount)</code>.
	 */
	public static Date dateAdd(Date date, int field, int amount) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(field, amount);
		return(gc.getTime());
	}
	
	/**
	 * Similar <code>dateAdd(Date date, int field, int amount)</code> except it will 
	 * take into account the day of the week to exclude and the unit is in days.
	 * @param  date          the initial date
	 * @param  amount        the amount of days to add or subtract 
	 * @param  excludedDays  days of the week to ignore (example: Calendar.SUNDAY)
	 */
	public static Date dateAdd(Date date, int amount, int[] excludedDays) {
		if (amount == 0) {
			return(date);
			
		} else if (excludedDays == null) {
			return(dateAdd(date, Calendar.DATE, amount));
			
		} else {
			int step, needToFind;
			if (amount > 0) { step = 1; } else { step = -1; }
			needToFind = Math.abs(amount);
			
			// Calendar.SUNDAY = 1; Calendar.SATURDAY = 7
			boolean[] includedDays = new boolean[7];
			Arrays.fill(includedDays, true);
			for (int i=0; i<excludedDays.length; i++) {
				includedDays[excludedDays[i] - 1] = false;
			}
			
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(date);
			
			Date returnDate = date;
			int includedDaysFound = 0;
			while (includedDaysFound < needToFind) {
				gc.add(Calendar.DATE, step);
				if (includedDays[gc.get(Calendar.DAY_OF_WEEK) - 1]) {
					includedDaysFound++;
				}
			}
			if (includedDaysFound == needToFind) {
				returnDate = gc.getTime();
			}
			
			return(returnDate);
		}
	}
	
	
	/* -------------------------------------------------- */
	/* Date part */
	/* -------------------------------------------------- */
	
	/**
	 * Returns the 1st of January of the year.
	 */
	public static Date beginningOfYear(int year) {
		return(getDate(year, Calendar.JANUARY, 1));
	}
	
	public static Date beginningOfYear(Date date) {
		return(beginningOfYear(getYear(date)));
	}
	
	/**
	 * Returns the 31st of December of the year.
	 */
	public static Date endOfYear(int year) {
		return(getDate(year, Calendar.DECEMBER, 31));
	}
	
	public static Date endOfYear(Date date) {
		return(endOfYear(getYear(date)));
	}
	
	public static Date beginningOfMonth(Date date) {
		return(getDate(getYear(date), getMonth(date), 1));
	}
	
	public static Date endOfMonth(Date date) {
		Date beginningOfMonth = beginningOfMonth(date);
		return(dateAdd(dateAdd(beginningOfMonth, Calendar.MONTH, 1), Calendar.DATE, -1));
	}
	
	public static Date beginningOfQuarter(Date date) {
		return(beginningOfQuarter(date, Calendar.JANUARY));
	}
	
	public static Date beginningOfQuarter(Date date, int beginningMonth) {
		int currMonth = getMonth(date);
		
		// generate a list of beginning months for the quaters
		int[] lookup = new int[4];
		for (int i=0; i<4; i++) {
			lookup[i] = beginningMonth + (i * 3);
			if (lookup[i] >= 12) {
				lookup[i] = lookup[i] - 12;
			}
		}
		Arrays.sort(lookup);
		
		// select the beginning month of the quarter
		int startMonth = -1;
		for (int i=0; i<4; i++) {
			if (currMonth == lookup[i]) {
				startMonth = lookup[i];
				break;
			} else if (currMonth > lookup[i]) {
				startMonth = lookup[i];
			}
		}
		if (startMonth == -1) {
			startMonth = lookup[3];
		}
		
		// adjust year if neccessary
		int startYear = getYear(date);
		if ((startMonth == Calendar.NOVEMBER || startMonth == Calendar.DECEMBER) && 
			(currMonth == Calendar.JANUARY || currMonth == Calendar.FEBRUARY)) {
			startYear = startYear - 1;
		}
		
		return getDate(startYear, startMonth, 1);
	}
	
	public static Date endOfQuarter(Date date) {
		return(endOfQuarter(date, Calendar.JANUARY));
	}
	
	public static Date endOfQuarter(Date date, int beginningMonth) {
		Date beginningOfQuarter = beginningOfQuarter(date, beginningMonth);
		return dateAdd(dateAdd(beginningOfQuarter, Calendar.MONTH, 3), Calendar.DATE, -1);
	}
	
	public static int thisYear() {
		GregorianCalendar cal = new GregorianCalendar();
		return(cal.get(Calendar.YEAR));
	}
	
	public static int getYear(Date date) {
		return(getDatePart(date, Calendar.YEAR));
	}
	
	/**
	 * Note: month is zero based.
	 */
	public static int getMonth(Date date) {
		return(getDatePart(date, Calendar.MONTH));
	}
	
	public static int getDay(Date date) {
		return(getDatePart(date, Calendar.DATE));
	}
	
	/**
	 * Please refer to <code>java.util.Calendar.get(int field)</code>.
	 */
	public static int getDatePart(Date date, int field) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		return(gc.get(field));
	}
	
	/**
	 * Returns the date with the time removed.
	 */
	public static Date getDateOnly(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		return(getDate(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE)));
	}
	
	/* -------------------------------------------------- */
	/* Date formating */
	/* -------------------------------------------------- */
	
	/**
	 * Formats the date.
	 *
	 * @see        java.text.SimpleDateFormat#SimpleDateFormat(String)
	 */
	public static String formatDate(String dateFormat, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return(sdf.format(date));
	}
	
	/**
	 * Convert a <code>Date</code> into a string.<br>
	 *
	 * @param  date  the date to convert
	 * @return       a string in the format "yyyy-MM-dd HH:mm:ss" 
	 *                 or null if date is null
	 * @see          #formatDate(String, Date)
	 */
	public static String getDateTimeString(Date date) {
		if (date != null) {
			return(formatDate("yyyy-MM-dd HH:mm:ss", date));
		} else {
			return(null);
		}
	}
	
	/**
	 * Converts the output of <code>getDateTimeString</code> or similarly formatted string
	 * back to a <code>Date</code>.
	 */
	public static Date getDateFromDateTimeString(String dateTimeString) {
		Date retDate = null;
		try {
			StringTokenizer st = new StringTokenizer(dateTimeString, "- :");
			retDate = getDate(
								Integer.parseInt(st.nextToken()), 
								Integer.parseInt(st.nextToken()) - 1, 
								Integer.parseInt(st.nextToken()),
								Integer.parseInt(st.nextToken()),
								Integer.parseInt(st.nextToken()),
								Integer.parseInt(st.nextToken())
			);
		} catch (Exception e) { }
		return(retDate);
	}
	
	/**
	 * Convert a <code>Date</code> into a string.<br>
	 *
	 * @param  date  the date to convert
	 * @return       a string in the format "yyyy-MM-dd" 
	 *                 or null if date is null
	 * @see          #formatDate(String, Date)
	 */
	public static String getDateString(Date date) {
		if (date != null) {
			return(formatDate("yyyy-MM-dd", date));
		} else {
			return(null);
		}
	}
	
	/**
	 * Converts the output of <code>getDateString</code> or similarly formatted string
	 * back to a <code>Date</code>.
	 */
	public static Date getDateFromDateString(String dateString) {
		Date retDate = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			retDate = sdf.parse(dateString);
		} catch (Exception e) { }
		return(retDate);
	}
	
	public static Date getDateWithEndTime(Date endDate){
		
		//set date with end of time 23:59:59
		Calendar dateEndTime = Calendar.getInstance();
		if (endDate != null) {			
			dateEndTime.setTime(endDate);                
			dateEndTime.set(Calendar.HOUR_OF_DAY, 23);
			dateEndTime.set(Calendar.MINUTE, 59);
			dateEndTime.set(Calendar.SECOND, 59); 
			return dateEndTime.getTime(); 
		}		 
		else{
			return null;
		}		
	}
	
	public static Date getDateFromStartTime(Date startDate){
	    	
			//set date with beginning of time 00:00:00
	        Calendar start = Calendar.getInstance();
	        if (startDate != null) {	
		        start.setTime(startDate);
		        start.set(Calendar.HOUR_OF_DAY, 00);
		        start.set(Calendar.MINUTE, 00);
		        start.set(Calendar.SECOND, 00);
		        startDate = start.getTime();	        
		        return startDate;      
	        }		 
			else{
				return null;
			}	
	    }
}
