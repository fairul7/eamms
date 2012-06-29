package kacang.stdui.validator;

import kacang.stdui.DateField;
import kacang.stdui.FormField;

import java.util.*;

/**
 * Validator to compare dates.
 */
public class ValidatorDateCompare extends Validator {

    public static final String COMPARE_GREATER = "GT";
    public static final String COMPARE_GREATER_EQUALS = "GTE";
    public static final String COMPARE_LESS = "LT";
    public static final String COMPARE_LESS_EQUALS = "LTE";

	DateField startDate;
	String    compareType;

    /**
     *
     * @param name Name of the validator
     * @param compareType Use COMPARE_GREATER or COMPARE_GREATER_EQUALS
     * @param startDate The DateField to compare to
     * @param text The error message
     */
	public ValidatorDateCompare(String name, String compareType, DateField startDate, String text) {
		super.setName(name);
		this.startDate   = startDate;
		this.compareType = compareType;
		setText(text);
	}

	public boolean validate(FormField formField) {
		if (compareType != null && (compareType.equalsIgnoreCase(COMPARE_GREATER) || compareType.equalsIgnoreCase(COMPARE_GREATER_EQUALS) || compareType.equalsIgnoreCase(COMPARE_LESS) || compareType.equalsIgnoreCase(COMPARE_LESS_EQUALS))) {
			Date sdate = (Date) startDate.getDate();
			Date edate = (Date) ((DateField) formField).getDate();
			if (sdate != null && edate != null) {
				if (compareType.equals(COMPARE_GREATER) && edate.getTime() <= sdate.getTime()) {
					return(false);
				} else if (compareType.equals(COMPARE_GREATER_EQUALS) && edate.getTime() < sdate.getTime()) {
					return(false);
				}else if (compareType.equals(COMPARE_LESS) && edate.getTime() >= sdate.getTime()) {
					return(false);
				}else if (compareType.equals(COMPARE_LESS_EQUALS) && edate.getTime() > sdate.getTime()) {
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
