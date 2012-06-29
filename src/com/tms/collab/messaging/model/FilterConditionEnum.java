package com.tms.collab.messaging.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;

import org.apache.commons.lang.enums.ValuedEnum;

/**
 * An enumeration of Filter's Condition
 * <ul>
 * 		<li>ANY</li>
 * 		<li>ALL</li>
 * </ul>
 */
public class FilterConditionEnum extends ValuedEnum {

	private static final String[] NAMES = new String[] {
		"ANY",
		"ALL"
	};
	
	private static final int[] VALUES = new int[] {
			0,1
	};
	
	private static final String[] I18N_KEYS = new String[] {
			"messaging.filtering.condition.label.any",
			"messaging.filtering.condition.label.all"
	};
	
	
	/**
	 * FilterConditionEnum: ANY
	 */
	public static final FilterConditionEnum ANY = new FilterConditionEnum(NAMES[0], VALUES[0], I18N_KEYS[0]);
	
	/**
	 * FilterConditionEnum: ALL
	 */
	public static final FilterConditionEnum ALL = new FilterConditionEnum(NAMES[1], VALUES[1], I18N_KEYS[1]);
	
	
	
	private static Map _presentationMap;
	
	
	private String _i18nKey;
	
	
	/**
	 * @param name
	 * @param value
	 */
	protected FilterConditionEnum(String name, int value, String i18nKey) {
		super(name, value);
		_i18nKey = i18nKey;
	}

	
	public static FilterConditionEnum getEnum(String name) {
		return (FilterConditionEnum) getEnum(FilterConditionEnum.class, name);
	}
	
	public static FilterConditionEnum getEnum(int value) {
		return (FilterConditionEnum) getEnum(FilterConditionEnum.class, value);
	}
	
	public static List getEnumList() {
		return getEnumList(FilterConditionEnum.class);
	}
	
	public static Map getEnumMap(){
		return getEnumMap(FilterConditionEnum.class);
	}
	
	/**
	 * return a map (enum name <-> i18n message) for used by presentation layer eg.
	 * SelectBox
	 * 
	 * @return Map
	 */
	public static Map getPresentationMap() {
		
		// we initialize it lazily upon first request
		if (_presentationMap == null) {
			Application application = Application.getInstance();
			
			Map presentationMap = new HashMap();
			for (Iterator i = getEnumList().iterator(); i.hasNext(); ) {
				FilterConditionEnum fce = (FilterConditionEnum) i.next();
				presentationMap.put(fce.getName(), application.getMessage(fce.getI18nKey()));
			}
			_presentationMap = presentationMap;
		}
		return _presentationMap;
	}
	
	public String getI18nKey() {
		return _i18nKey;
	}
}
