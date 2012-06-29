package kacang.stdui.validator;

import kacang.stdui.DateField;
import kacang.stdui.FormField;

import java.util.*;

public class ValidatorDateNotNull extends Validator {

	public ValidatorDateNotNull(String name, String text) {
		super.setName(name);
		setText(text);
	}

	public boolean validate(FormField formField) {
		Date edate = (Date) ((DateField) formField).getDate();
		if (edate == null) {
			return(false);
		}
		return(true);
	}
}

