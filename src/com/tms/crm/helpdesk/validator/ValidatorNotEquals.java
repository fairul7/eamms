package com.tms.crm.helpdesk.validator;

import kacang.stdui.validator.Validator;
import kacang.stdui.FormField;

import java.util.Collection;
import java.util.Map;

public class ValidatorNotEquals extends Validator
{
	private String check = null;

	public ValidatorNotEquals()
	{
		super();
	}

	public ValidatorNotEquals(String s)
	{
		super(s);
	}

	public ValidatorNotEquals(String s, String check)
	{
        super(s);
		this.check = check;
	}

	public boolean validate(FormField formField)
	{
		Object value = formField.getValue();
		if(value != null)
		{
			String compare = null;
			if(value instanceof Collection)
			{
				if(((Collection) value).size() > 0)
					compare = (String) ((Collection) value).iterator().next();
			}
			else if(value instanceof Map)
			{
				if(((Map) value).keySet().size() > 0)
					compare = (String) ((Map) value).keySet().iterator().next();
			}
			else if(value instanceof String)
				compare = (String) value;
			return check.equals(compare) ? false: true;
		}
		return false;
	}

	public String getCheck()
	{
		return check;
	}

	public void setCheck(String check)
	{
		this.check = check;
	}
}
