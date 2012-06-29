package com.tms.collab.messaging.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;

import org.apache.commons.lang.enums.ValuedEnum;

/**
 * An enumeration of Filter's Rule's Criteria
 * <ul>
 * 		<li>SUBJECT</li>
 * 		<li>BODY</li>
 * 		<li>TO</li>
 * 		<li>CC</li>
 * 		<li>FROM</li>
 * </ul>
 */
public abstract class FilterCriteriaEnum extends ValuedEnum {
	
	private static final String[] NAMES = new String[] {
		"SUBJECT",
		"BODY",
		"TO",
		"CC",
		"FORM"
	};
	
	private static final int[] VALUES = new int[] {
		0, 1, 2, 3, 4
	};
	
	private static final String[] I18N_KEYS = new String[] {
			"messaging.filtering.criteria.label.subject",
			"messaging.filtering.criteria.label.body",
			"messaging.filtering.criteria.label.to",
			"messaging.filtering.criteria.label.cc",
			"messaging.filtering.criteria.label.from"
	};
	

	/**
	 * FilterCriteriaEnum : SUBJECT
	 */
	public static final FilterCriteriaEnum SUBJECT = new FilterCriteriaEnum(NAMES[0], VALUES[0], I18N_KEYS[0]) {
		/**
		 * @see com.tms.collab.messaging.model.FilterCriteriaEnum#getCriteriaFromMessage(Message)
		 */
		public Object getCriteriaFromMessage(Message message)
				throws MessagingException, IOException {
			return new String[] { 
                    message.getSubject() == null ? "" : message.getSubject() 
            };
		}
	};
	
	
	
	/**
	 * FilterCriteriaEnum : BODY
	 */
	public static final FilterCriteriaEnum BODY = new FilterCriteriaEnum(NAMES[1], VALUES[1], I18N_KEYS[1]) {
		/**
		 * @see com.tms.collab.messaging.model.FilterCriteriaEnum#getCriteriaFromMessage(Message)
		 */
		public Object getCriteriaFromMessage(Message message)
				throws MessagingException, IOException {
			Object content = message.getBody();
			if (content instanceof String) {
				return new String[] { 
                        (String) content == null ? "" : (String) content
                };
			}
			return "";
		}
	};
	
	/**
	 * FilterCriteriaEnum : TO
	 */
	public static final FilterCriteriaEnum TO = new FilterCriteriaEnum(NAMES[2], VALUES[2], I18N_KEYS[2]) {
		/**
		 * @see com.tms.collab.messaging.model.FilterCriteriaEnum#getCriteriaFromMessage(Message)
		 */
		public Object getCriteriaFromMessage(Message message)
				throws MessagingException, IOException {
			List ad = message.getToList() == null ? new ArrayList() : message.getToList();
			return ad.toArray(new String[0]);
		}
	};
    
	
	/**
	 * FilterCriteriaEnum : CC
	 */
	public static final FilterCriteriaEnum CC = new FilterCriteriaEnum(NAMES[3], VALUES[3], I18N_KEYS[3]) {
		/**
		 * @see com.tms.collab.messaging.model.FilterCriteriaEnum#getCriteriaFromMessage(Message)
		 */
		public Object getCriteriaFromMessage(Message message)
				throws MessagingException, IOException {
			List ad = message.getCcList() == null ? new ArrayList() : message.getCcList();
			return ad.toArray(new String[0]);
		}
	};
    
    
	
	/**
	 * FilterCriteriaEnum : FROM
	 */
	public static final FilterCriteriaEnum FROM = new FilterCriteriaEnum(NAMES[4], VALUES[4], I18N_KEYS[4]) {
		/**
		 * @see com.tms.collab.messaging.model.FilterCriteriaEnum#getCriteriaFromMessage(Message)
		 */
		public Object getCriteriaFromMessage(Message message)
				throws MessagingException, IOException {
			String ad = message.getFrom() == null ? "" : message.getFrom();
			String[] adInString = new String[] { ad };
			return adInString;
		}
	};
	
	
	private static Map _presentationMap = null;
	
	
	// i18n key
	private String _i18nKey;
	
	
	/**
	 * @param name
	 * @param value
	 */
	protected FilterCriteriaEnum(String name, int value, String i18nKey) {
		super(name, value);
		_i18nKey = i18nKey;
	}

	public static FilterCriteriaEnum getEnum(String name) {
		if (name.equals(NAMES[0])) {
			return FilterCriteriaEnum.SUBJECT;
		}
		if (name.equals(NAMES[1])) {
			return FilterCriteriaEnum.BODY;
		}
		if (name.equals(NAMES[2])) {
			return FilterCriteriaEnum.TO;
		}
		if (name.equals(NAMES[3])) {
			return FilterCriteriaEnum.CC;
		}
		if (name.equals(NAMES[4])) {
			return FilterCriteriaEnum.FROM;
		}
		throw new IllegalArgumentException(""+name+" is not recognized by "+FilterCriteriaEnum.class);
	}
	
	public static List getEnumList() {
		
		List l = new ArrayList();
		l.add(FilterCriteriaEnum.SUBJECT);
		l.add(FilterCriteriaEnum.BODY);
		l.add(FilterCriteriaEnum.TO);
		l.add(FilterCriteriaEnum.CC);
		l.add(FilterCriteriaEnum.FROM);
		
		return l;
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
				FilterCriteriaEnum fce = (FilterCriteriaEnum) i.next();
				presentationMap.put(fce.getName(), application.getMessage(fce.getI18nKey()));
			}
			_presentationMap = presentationMap;
		}
		return _presentationMap;
	}
	
	
	// instance level ==========================================================
	
	/**
	 * Generate the I18N key for this Filter's Criteria.
	 * 
	 * @return String
	 */
	public String getI18nKey() {
		return _i18nKey;
	}
	
	/**
	 * Return the criteria of this rule based on the 
	 * <code>message</code> supplied
	 * 
	 * @param message
	 * @return Object
	 */
	public abstract Object getCriteriaFromMessage(Message message) 
	throws MessagingException, IOException;
}
