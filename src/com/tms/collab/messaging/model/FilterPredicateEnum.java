package com.tms.collab.messaging.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.util.Log;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.enums.ValuedEnum;

/**
 * Enumeration for Filter Predicate
 * <ul>
 * 	<li>CONTAINS</li>
 * 	<li>DOES_NOT_CONTAINS</li>
 * 	<li>IS</li>
 * 	<li>IS_NOT</li>
 * 	<li>BEGINS_WITH</li>
 * 	<li>ENDS_WITH< /li>
 * </ul>
 */
public abstract class FilterPredicateEnum extends ValuedEnum {
	
    private static final Log log = Log.getLog(FilterPredicateEnum.class);
    
	private static final String[] NAMES = new String[] {
			"CONTAINS",
			"DOES_NOT_CONTAINS",
			"IS",
			"IS_NOT",
			"BEGINS_WITH",
			"ENDS_WITH"
	};
	
	private static final int[] VALUES = new int[] {
			0, 1, 2, 3, 4, 5
	};
	
	
	private static final String[] I18N_KEYS = new String[] {
			"messaging.filtering.predicate.label.contains",
			"messaging.filtering.predicate.label.doesNotContains",
			"messaging.filtering.predicate.label.is",
			"messaging.filtering.predicate.label.isNot",
			"messaging.filtering.predicate.label.beginsWith",
			"messaging.filtering.predicate.label.endsWith"
	};
	
	
	/**
	 * FilterPredicateEnum : CONTAINS
	 */
	public static final FilterPredicateEnum CONTAINS = new FilterPredicateEnum(NAMES[0], VALUES[0], I18N_KEYS[0]) {
		/**
		 * @see com.tms.collab.messaging.model.FilterPredicateEnum#compare(java.lang.Object, java.lang.String)
		 */
		public boolean compare(Object criteria, String predicateValue) {
			String[] c = (String[]) criteria;
            if (predicateValue == null) { predicateValue = ""; }
			for (int a=0; a< c.length; a++) {
                if (c[a] != null && predicateValue != null) {
                String caseInsensitiveCriteria = c[a].toLowerCase();
                String caseInsensitivePredicateValue = predicateValue.toLowerCase();
				
                // NOTE:
                // This line makes comparison case-sensitive
                //int count = StringUtils.countMatches(c[a], predicateValue);
                
                // NOTE:
                // line bellow makes comparison case-insensitive
                int count = StringUtils.countMatches(caseInsensitiveCriteria, caseInsensitivePredicateValue);
				if (count > 0) {
					return true;
				}
                }
                else {
                    log.warn("WARNING: criteria ["+criteria+"]or predicateValue ["+predicateValue+"] is null");
                }
			}
			return false;
		}

		
	};
	
	/**
	 * FilterPredicateEnum : DOES_NOT_CONTAINS
	 */
	public static final FilterPredicateEnum DOES_NOT_CONTAINS = new FilterPredicateEnum(NAMES[1], VALUES[1], I18N_KEYS[1]) {
		/**
		 * @see com.tms.collab.messaging.model.FilterPredicateEnum#compare(java.lang.Object, java.lang.String)
		 */
		public boolean compare(Object criteria, String predicateValue) {
			String[] c = (String[]) criteria;
            if (predicateValue == null) { predicateValue = ""; }
			for (int a=0; a< c.length; a++) {
                if (c[a] != null && predicateValue != null) {
                String caseInsensitiveCriteria = c[a].toLowerCase();
                String caseInsensitivePredicateValue = predicateValue.toLowerCase();
                
                
                // NOTE:
                // This line makes comparison case sensitive
				// int count = StringUtils.countMatches(c[a], predicateValue);
                
                // NOTE:
                // This line makes comparison case insensitive
                int count = StringUtils.countMatches(caseInsensitiveCriteria, caseInsensitivePredicateValue);
                
				if ( count > 0) {
					return false;
				}
                }
                else {
                    log.warn("WARNING: criteria ["+criteria+"]or predicateValue ["+predicateValue+"] is null");
                }
			}
			return true;
		}
	};
	
	
	/**
	 * FilterPredicateEnum : IS
	 */
	public static final FilterPredicateEnum IS = new FilterPredicateEnum(NAMES[2], VALUES[2], I18N_KEYS[2]) {
		/**
		 * @see com.tms.collab.messaging.model.FilterPredicateEnum#compare(java.lang.Object, java.lang.String)
		 */
		public boolean compare(Object criteria, String predicateValue) {
			String[] c = (String[]) criteria;
            if (predicateValue == null) { predicateValue = ""; }
			for (int a=0; a< c.length; a++) {
                if (c[a] != null && predicateValue != null) {
				if (c[a].equalsIgnoreCase(predicateValue)) {
					return true;
				}
                }
                else {
                    log.warn("WARNING: criteria ["+criteria+"]or predicateValue ["+predicateValue+"] is null");
                }
			}
			return false;
		}
	};
	
	/**
	 * FilterPredicateEnum : IS_NOT
	 */
	public static final FilterPredicateEnum IS_NOT = new FilterPredicateEnum(NAMES[3], VALUES[3], I18N_KEYS[3]) {
		/**
		 * @see com.tms.collab.messaging.model.FilterPredicateEnum#compare(java.lang.Object, java.lang.String)
		 */
		public boolean compare(Object criteria, String predicateValue) {
			String[] c = (String[]) criteria;
            if (predicateValue == null) { predicateValue = ""; }
			for (int a=0; a< c.length; a++) {
                if (c[a] != null && predicateValue != null) {
				if ((c[a].equalsIgnoreCase(predicateValue))  ) {
					return false;
				}
                }
                else {
                    log.warn("WARNING: criteria ["+criteria+"]or predicateValue ["+predicateValue+"] is null");
                }   
			}
			return true;
		}
	};
	
	
	/**
	 * FilterPredicateEnum : BEGINS_WITH
	 */
	public static final FilterPredicateEnum BEGINS_WITH = new FilterPredicateEnum(NAMES[4], VALUES[4], I18N_KEYS[4]) {
		/**
		 * @see com.tms.collab.messaging.model.FilterPredicateEnum#compare(java.lang.Object, java.lang.String)
		 */
		public boolean compare(Object criteria, String predicateValue) {
			String[] c = (String[]) criteria;
			if (predicateValue == null) { predicateValue = ""; }
			for (int a=0; a< c.length; a++) {
       
                if (c[a] != null && predicateValue != null) {
                String caseInsensitiveCriteria = c[a].toLowerCase();
                String caseInsensitivePredicateValue = predicateValue.toLowerCase();
                
                // NOTE:
                // This line makes comparison case-sensitive
				//if (c[a].startsWith(predicateValue)) {
                
                // NOTE:
                // This line makes comparison case-insensitive
                if (caseInsensitiveCriteria.startsWith(caseInsensitivePredicateValue)) {
					return true;
				}
                }
                else {
                    log.warn("WARNING: criteria ["+criteria+"]or predicateValue ["+predicateValue+"] is null");
                }
			}
			return false;
		}
	};
	
	/**
	 * FilterPredicateEnum : ENDS_WITH
	 */
	public static final FilterPredicateEnum ENDS_WITH = new FilterPredicateEnum(NAMES[5], VALUES[5], I18N_KEYS[5]) {
		/**
		 * @see com.tms.collab.messaging.model.FilterPredicateEnum#compare(java.lang.Object, java.lang.String)
		 */
		public boolean compare(Object criteria, String predicateValue) {
			String[] c = (String[]) criteria;
            if (predicateValue == null) { predicateValue = ""; }
			for (int a=0; a< c.length; a++) {
                if (c[a] != null && predicateValue != null) {
                String caseInsensitiveCriteria = c[a].toLowerCase();
                String caseInsensitivePredicateValue = predicateValue.toLowerCase();

                // NOTE:
                // This line makes comparison case-sensitive
				//if (c[a].endsWith(predicateValue)) {
                
                // NOTE;
                // This line makes comparison case-insensitive
                if (caseInsensitiveCriteria.endsWith(caseInsensitivePredicateValue)) {
					return true;
				}
                }
                else {
                    log.warn("WARNING: criteria ["+criteria+"]or predicateValue ["+predicateValue+"] is null");
                }
			}
			return false;
		}
	};
	
	
	private static Map _presentationMap;
	
	
	private String _i18nKey;
	
	/**
	 * @param name
	 * @param value
	 */
	protected FilterPredicateEnum(String name, int value, String i18nKey) {
		super(name, value);
		_i18nKey = i18nKey;
	}

	public static FilterPredicateEnum getEnum(String name) {
		if (name.equals(NAMES[0])) {
			return FilterPredicateEnum.CONTAINS;
		}
		if (name.equals(NAMES[1])) {
			return FilterPredicateEnum.DOES_NOT_CONTAINS;
		}
		if (name.equals(NAMES[2])) {
			return FilterPredicateEnum.IS;
		}
		if (name.equals(NAMES[3])) {
			return FilterPredicateEnum.IS_NOT;
		}
		if (name.equals(NAMES[4])) {
			return FilterPredicateEnum.BEGINS_WITH;
		}
		if (name.equals(NAMES[5])) {
			return FilterPredicateEnum.ENDS_WITH;
		}
		throw new IllegalArgumentException(""+name+" is not recognized by "+FilterPredicateEnum.class);
	}
	
	public static List getEnumList() {
		List l = new ArrayList();
		l.add(FilterPredicateEnum.CONTAINS);
		l.add(FilterPredicateEnum.DOES_NOT_CONTAINS);
		l.add(FilterPredicateEnum.IS);
		l.add(FilterPredicateEnum.IS_NOT);
		l.add(FilterPredicateEnum.BEGINS_WITH);
		l.add(FilterPredicateEnum.ENDS_WITH);
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
				FilterPredicateEnum fce = (FilterPredicateEnum) i.next();
				presentationMap.put(fce.getName(), application.getMessage(fce.getI18nKey()));
			}
			_presentationMap = presentationMap;
		}
		return _presentationMap;
	}
	
	
	// instance level ==========================================================
	
	public String getI18nKey() {
		return _i18nKey;
	}
	
	public abstract boolean compare(Object criteria, String predicateValue);
}
