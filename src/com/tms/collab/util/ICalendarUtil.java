package com.tms.collab.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import com.tms.crm.sales.misc.DateUtil;

public class ICalendarUtil{

	public static Date convertToMysqlDate(String date) {
		Date returnDate = new Date();
		try {
			if (date != null) {
				SimpleTimeZone myTz = new SimpleTimeZone(TimeZone.getDefault().getRawOffset(),TimeZone.getDefault().toString());
				int y = Integer.parseInt(date.substring(0, 4));
				int mo = Integer.parseInt(date.substring(4, 6));
				int d = Integer.parseInt(date.substring(6, 8));
				int h = Integer.parseInt(date.substring(9, 11));
				int mi = Integer.parseInt(date.substring(11, 13));
				int s = Integer.parseInt(date.substring(13, 15));
				String tempDate = y + "-" + mo + "-" + d + " " + h + ":" + mi
						+ ":" + s;
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date gmtDate = sdf.parse(tempDate);
				long time =gmtDate.getTime();
				String tzName=myTz.getDisplayName();
				if(tzName.indexOf("+")>0)
					time+=myTz.getRawOffset();
				else if(tzName.indexOf("-")>0)
					time=time-myTz.getRawOffset();
				returnDate=new Date(time);
			}
			return returnDate;
		} catch (Exception e) {
			//System.out.println("Exception"+ e.getMessage());
			return returnDate;
		}
	}
	public static Date convertToGMTDate(Date date) {
		Date returnDate=date;
		try{
		SimpleTimeZone myTz = new SimpleTimeZone(TimeZone.getDefault().getRawOffset(),TimeZone.getDefault().toString());
		//System.out.println("Date"+ date);
		long time =date.getTime();
		String tzName=myTz.getDisplayName();
		if(tzName.indexOf("+")>0)
			time=time-myTz.getRawOffset();
		else if(tzName.indexOf("-")>0)
			time+=myTz.getRawOffset();
		returnDate=new Date(time);
		//System.out.println("GMT " + returnDate+" TZ offset "+myTz.getDisplayName());
		}catch(Exception e){			}
		return returnDate;
	}
}