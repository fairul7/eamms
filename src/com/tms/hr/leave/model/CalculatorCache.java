package com.tms.hr.leave.model;

import java.util.*;

public class CalculatorCache {
	private Map calcMap;
	
	public CalculatorCache() {
		calcMap = new HashMap();
	}
	
	public LeaveCalculator getCalculator(int year) throws LeaveException {
		String yearStr = String.valueOf(year);
		LeaveCalculator calc;
		if (calcMap.containsKey(yearStr)) {
			calc = (LeaveCalculator) calcMap.get(yearStr);
		} else {
			calc = LeaveModule.getLeaveCalculator(year);
			calcMap.put(yearStr, calc);
		}
		return calc;
	}
	
	public LeaveCalculator getCalculator(Date startDate) throws LeaveException {
		int year = HrUtil.getYear(startDate);
		return getCalculator(year);
	}
}