package com.tms.hr.leave.model;

import java.util.*;

public class HrUtil {
	public static int getYear(Date date) {
		GregorianCalendar gcal = new GregorianCalendar();
		gcal.setTime(date);
		return gcal.get(Calendar.YEAR);
	}
	
	public static Date getDateOnly(Date date) {
		GregorianCalendar gcal = new GregorianCalendar();
		gcal.setTime(date);
		int year  = gcal.get(Calendar.YEAR);
		int month = gcal.get(Calendar.MONTH);
		int day   = gcal.get(Calendar.DATE);
		gcal = new GregorianCalendar(year, month, day);
		return gcal.getTime();
	}
	
	public static Date addDate(Date date, int field, int num) {
		GregorianCalendar gcal = new GregorianCalendar();
		gcal.setTime(date);
		gcal.add(field, num);
		return gcal.getTime();
	}
}