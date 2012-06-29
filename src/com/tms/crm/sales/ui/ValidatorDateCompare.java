/*
 * Created on Dec 15, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.tms.crm.sales.ui;

import java.util.*;
import kacang.stdui.*;
import kacang.stdui.validator.*;

/**
 * @author Paul Pak
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ValidatorDateCompare extends Validator {
	DateField startDate;
	String    compareType;
	
	public ValidatorDateCompare(String name, String compareType, DateField startDate, String text) {
		super.setName(name);
		this.startDate   = startDate;
		this.compareType = compareType;
		setText(text);
	}
	
	public boolean validate(FormField formField) {
		if (compareType != null && (compareType.equals("GT") || compareType.equals("GTE"))) {
			Date sdate = (Date) startDate.getDate();
			Date edate = (Date) ((DateField) formField).getDate();
			
			if (sdate != null && edate != null) {
				if (compareType.equals("GT") && edate.getTime() <= sdate.getTime()) {
					return(false);
				} else if (compareType.equals("GTE") && edate.getTime() < sdate.getTime()) {
					return(false);
				}
				
			} else {
				// invalid input date, no error thrown (should be handled by ValidatorIsDate)
			}
			
		} else {
			setText("Invalid 'compareType'");
			return(false);
		}
		return(true);
	}
}
