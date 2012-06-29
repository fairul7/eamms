package com.tms.fms.validator;

import kacang.Application;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorMessage;
import com.tms.crm.sales.misc.DateUtil;
import java.util.*;
import java.text.DecimalFormat;

public class ValidatorUtil {
	public static boolean isValidNumber(float num, int precision, int scale) {
		DecimalFormat df = new DecimalFormat("0.0");
		return isValidNumber(df.format(num), precision, scale);
	}
	
	public static boolean isValidNumber(String numStr, int precision, int scale) {
		numStr = numStr.replaceAll("-", "");
		int dotIndex = numStr.indexOf(".");
		
		String front, back;
		if (dotIndex == -1) {
			front = numStr;
			back  = "";
		} else {
			front = numStr.substring(0, dotIndex);
			back  = numStr.substring(dotIndex + 1);
		}
		
		//System.out.println("front: " + front);
		//System.out.println("back : " + back);
		
		if (back.length() > scale) {
			return false;
		}
		
		if ((precision - scale) == 0) {
			if (!front.equals("0") && !front.equals("")) {
				return false;
			}
		} else {
			if (front.length() > (precision - scale)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static int getIntegralLength(float num) {
		DecimalFormat df = new DecimalFormat("0.0");
		return getIntegralLength(df.format(num));
	}
	
	public static int getIntegralLength(String numStr) {
		numStr = numStr.replaceAll("-", "");
		int dotIndex = numStr.indexOf(".");
		
		String front;
		if (dotIndex == -1) {
			front = numStr;
		} else {
			front = numStr.substring(0, dotIndex);
		}
		return front.length();
	}
	
	public static void validateFloat(Form form, FormField formField, ValidatorMessage vmsg, boolean optional) {
		Application application = Application.getInstance();
		
		String value = (String) formField.getValue();
		if (optional && value.equals("")) {
			return;
		} else if (!optional && value.equals("")) {
			vmsg.showError(application.getMessage("lms.classroom.general.validation.mandatoryField"));
			form.setInvalid(true);
		} else {
			try {
				float f = Float.parseFloat(value);
				if (f < 0) {
					vmsg.showError(application.getMessage("lms.classroom.general.validation.negativeNumber"));
					form.setInvalid(true);
				} else {
					if (ValidatorUtil.getIntegralLength(f) > 7) {
						vmsg.showError(application.getMessage("lms.classroom.general.validation.numberTooLarge"));
						form.setInvalid(true);
					} else if (!ValidatorUtil.isValidNumber(f, 10, 2)) {
						vmsg.showError(application.getMessage("lms.classroom.general.validation.moreThan2DecimalPlaces"));
						form.setInvalid(true);
					}
				}
			} catch (NumberFormatException e) {
				vmsg.showError(application.getMessage("lms.classroom.general.validation.invalidNumber"));
				form.setInvalid(true);
			}
		}
	}
	
	public static void validateInteger(Form form, FormField formField, ValidatorMessage vmsg, boolean optional) {
		Application application = Application.getInstance();
		
		String value = (String) formField.getValue();
		if (optional && value.equals("")) {
			return;
		} else if (!optional && value.equals("")) {
			vmsg.showError(application.getMessage("lms.classroom.general.validation.mandatoryField"));
			form.setInvalid(true);
		} else {
			try {
				int i = Integer.parseInt(value);
				if (i < 0) {
					vmsg.showError(application.getMessage("lms.classroom.general.validation.negativeNumber"));
					form.setInvalid(true);
				}
			} catch (NumberFormatException e) {
				vmsg.showError(application.getMessage("lms.classroom.general.validation.invalidInteger"));
				form.setInvalid(true);
			}
		}
	}
	
	public static long daysRange(Date start, Date end) {
		return Math.abs(daysDiff(start, end)) + 1;
	}
	
	public static long daysDiff(Date start, Date end) {
		final int day_multiplier = 1000 * 60 * 60 * 24;
		long startnum = DateUtil.getDateOnly(start).getTime();
		long endnum= DateUtil.getDateOnly(end).getTime();
		
		return (endnum / day_multiplier) - (startnum / day_multiplier);
	}
}
