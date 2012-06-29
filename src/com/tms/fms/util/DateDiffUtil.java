package com.tms.fms.util;

import java.util.Date;

public class DateDiffUtil {
	public static final long MS_IN_A_DAY = 1000 * 60 * 60 * 24;
	
	public static long dayDiff(Date start, Date end) {
		if (start != null && end != null) {
			long startDays = start.getTime() / MS_IN_A_DAY;
			long endDays = end.getTime() / MS_IN_A_DAY;
			return endDays - startDays;
		}
		return 0;
	}
}
