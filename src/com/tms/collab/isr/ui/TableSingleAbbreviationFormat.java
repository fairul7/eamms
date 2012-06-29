package com.tms.collab.isr.ui;

import kacang.stdui.TableFormat;

public class TableSingleAbbreviationFormat implements TableFormat {
	public TableSingleAbbreviationFormat() {
	}
	
	/**
	 * Format the given instance to return its first char as abbreviation
	 */
	public String format(Object value) {
		if (value == null) {
            return "";
        }
		else {
			if(!"".equals(value.toString())) {
				return String.valueOf(value.toString().charAt(0));
			}
			else {
				return "";
			}
		}
	}
}
