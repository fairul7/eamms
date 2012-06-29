package com.tms.wiki.stdui;

import java.util.StringTokenizer;

import kacang.stdui.FormField;
import kacang.stdui.validator.Validator;

public class TagsValidator extends Validator{
	private boolean empty;

	public TagsValidator() {
		super();
	}

	public TagsValidator(String s) {
		super(s);
	}
	
	public TagsValidator(String s, boolean empty) {
		super(s);
		this.empty = empty;
	}
	
	public boolean validate(FormField field) {
		String value = (String) field.getValue();
		if(!empty && value == null )
			return false;
		else {
			StringTokenizer st = new StringTokenizer(value,",");
			while(st.hasMoreTokens()){
				String token = st.nextToken();
				if(token.indexOf(" ")>0)
					return false;
			}
		}
		return true;
	}
	

}
