package com.tms.collab.messaging.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.util.Log;
import kacang.util.UuidGenerator;

/**
 * The Filter (value object) representing filter queried from db. Each filter
 * has many rules associated with it.
 */
public class Filter extends DefaultDataObject {
	
	private static final Log log = Log.getLog(Filter.class);
	
    private String userId;
    private String name;

    private String filterValue;
    private boolean filterActive;
    private String filterAction;
    private String filterCondition;
    private int filterOrdering = -1; 
    
    
    private List rules = new ArrayList();
    private FilterActionEnum _filterActionEnum;
    private FilterConditionEnum _filterConditionEnum;
    
    private String _folderName;
    
    /**
     * To indicate if this {@link Filter} is to be Persisted (not in db yet) or
     * it is already persisted (already in db) on the DB, depending on the 
     * constructor used to construct this {@link Filter}.
     */
    private boolean _persist;
    
    /**
     * Use this constructor to create an existing {@link Filter}, 
     * {@link MessagingModule#addOrUpdateDaoBasedFilter(Filter)} will do an 
     * UPDATE on the Database instead of an INSERT.
     * 
     * @param filterId
     * @param userId
     * @param name
     * @param filterValue
     * @param filterActive
     * @param filterAction
     * @param filterCondition
     * @param rules
     */
    public Filter(String filterId, String userId, String name, String filterValue,
    	boolean filterActive, String filterAction, String filterCondition, 
        List rules) {
    	
    	this.userId = userId;
    	this.name = name;
    	this.filterValue = filterValue;
    	this.filterActive = filterActive;
    	this.filterAction = filterAction;
    	this.filterCondition = filterCondition;
    	this.rules = rules;
    	setId( filterId == null ? UuidGenerator.getInstance().getUuid() : filterId);
    	
    	_filterActionEnum = FilterActionEnum.getEnum(filterAction);
    	_filterConditionEnum = FilterConditionEnum.getEnum(filterCondition);
    	_persist = false;
    }
    
    
    /**
     * Use this constructor to create a new {@link Filter}, 
     * {@link MessagingModule#addOrUpdateDaoBasedFilter(Filter)} will do an 
     * INSERT on the database instead of an UPDATE.
     * 
     * 
     * @param userId
     * @param name
     * @param filterValue
     * @param filterActive
     * @param filterAction
     * @param filterCondition
     * @param rules
     */
    public Filter(String userId, String name, String filterValue, boolean filterActive, 
    	String filterAction, String filterCondition, List rules) {
    	this(null, userId, name, filterValue, filterActive, filterAction, 
    			filterCondition, rules);
    	_persist = true;
    }
    
    
    /**
     * Retrieve the i18n message regarding the Filter's action
     * to be displayed in the {@link com.tms.collab.messaging.ui.FilterListingTable}.
     * 
     * @return String
     */
    public String getI18nFilterAction() {
    	StringBuffer buf = new StringBuffer("<b>"+_filterActionEnum.getI18nMessage());
    	// if there is a folder related to it, we wanna display its name
    	if (FilterActionEnum.MOVE_TO_FOLDER.equals(_filterActionEnum)) {
    	//if (getFolderName() != null && getFolderName().trim().length() >= 0) {
    		buf.append(" ");
    		buf.append("[");
    		if (getFolderName() != null) {
    			buf.append(getFolderName());
    		}
    		else {
    			buf.append(getI18nMessage("messaging.filtering.action.msg.noSuchFolder"));
    		}
    		buf.append("]");
    	}
    	// if there is a filter value (eg. forward) then we want to display it
    	if (FilterActionEnum.FORWARD.equals(_filterActionEnum)) {
    	//else if (getFilterValue() != null && getFilterValue().trim().length() >= 0) {
    		buf.append(" ");
    		buf.append("[");
    		buf.append(getFilterValue());
    		buf.append("]");
    	}
    	buf.append("</b>");
    	// now we add in a rules
    	buf.append("<ol>");
    	for (Iterator i = getRules().iterator(); i.hasNext(); ) {
    		Rule r = (Rule) i.next();
    		buf.append("<li>");
    		buf.append(r.getI18nMessage());
    		buf.append("</li>");
    	}
    	buf.append("</ol>");
    	
    	
    	return buf.toString();
    }
    
    /**
     * Retrieve the I18n message regarding the Filter's active state.
     * 
     * @return String
     */
    public String getI18nFilterActive() {
    	Application application = Application.getInstance();
    	return (isFilterActive() ? 
    			application.getMessage("messaging.filtering.table.active.active") : 
    		    application.getMessage("messaging.filtering.table.active.notActive"));
    }
    
    
    // misc ====================================================================
    
    /**
     * Set the folder name related to this filter action if there is one. Being
     * called by {@link MessagingDao}.
     * 
     * @param folderName
     */
    public void setFolderName(String folderName) { _folderName = folderName; }
    public String getFolderName() { return _folderName; }
    
    /**
     * Does the {@link Message} match the rules of this filter in order for 
     * action to be taken on it. True if it match, false otherwise.
     * 
     * @param message
     * @return boolean
     */
    public boolean match(Message message) 
    throws MessagingException, IOException {
    	
    	if (isFilterActive()) {
    		// NOTE: there will be at least one RULE, that is mandatoried by
    		//       the front end.
    		
    		
    		log.debug("filter "+getId()+" is active");
    		
    		// store if each rule match the Message or not
    		boolean[] rulesMatch = new boolean[getRules().size()];
		
    		int a=0;
    		for (Iterator ii = getRules().iterator(); ii.hasNext(); a++) {
    			Filter.Rule r = (Filter.Rule) ii.next();
    			rulesMatch[a] = r.match(message);
    			
    			log.debug("applying rule "+r.getId()+" ["+rulesMatch[a]+"]");
    		}
		
    		// any
    		boolean result = false;
    		if (isAny()) {
    			result = false;
    			for (int b=0; b< rulesMatch.length; b++) {
    				result = (result | rulesMatch[b]);
    			}
    			log.debug("composed rules result based on codition [ANY] = "+result);
    		}
    		
    		// all
    		else {
    			result = true;
    			for (int b=0; b< rulesMatch.length; b++) {
    				result = (result & rulesMatch[b]);
    			}
    			log.debug("composed rules result based on condition [ALL] = "+result);
    		}
    		return result;
    	}
    	else {
    		log.debug("filter "+getId()+" is not active");
    		// not active, dun match
    		return false;
    	}
    }

    /**
     * Retrun if the Condition of this filter is 'ANY' (meaning that it will
     * filter when any of its rules matches.
     * 
     * @return boolean
     */
    public boolean isAny() { return _filterConditionEnum.equals(FilterConditionEnum.ANY); }
    
    /**
     * Return if the Filter is 'new' and 'to be persisted' or otherwise.
     * 
     * @return boolean
     */
    public boolean isToBePersist() { return _persist; }
    
    /**
     * Returned Filter action
     * 
     * @return FilterActionEnum
     */
    public FilterActionEnum getFilterActionEnum() { return _filterActionEnum; }
    
    /**
     * Returned Fitler Condition
     * 
     * @return FilterConditionEnum
     */
    public FilterConditionEnum getFilterConditionEnum() { return _filterConditionEnum; }
    
    
    protected String getI18nMessage(String key) {
    	Application application = Application.getInstance();
    	return application.getMessage(key);
    }
    
    
    // === [ getters/setters ] =================================================
    public String getFilterId() {
    	return getId();
    }

    public void setFilterId(String filterId) {
    	setId(filterId);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilterAction() {
    	return filterAction;
    }
    
    public void setFilterAction(String filterAction) {
    	
    	this.filterAction = filterAction;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public boolean isFilterActive() {
        return filterActive;
    }

    public void setFilterActive(boolean filterActive) {
        this.filterActive = filterActive;
    }

    public void addRule(Rule rule) {
    	rules.add(rule);
    }
    
    public Collection getRules() {
    	return rules;
    }
    
    public String getFilterCondition() {
		return filterCondition;
	}
    
	public void setFilterCondition(String filterCondition) {
		this.filterCondition = filterCondition;
	}
	
    public int getFilterOrder() {
        return filterOrdering;
    }
	
    public void setFilterOrder(int filterOrder) {
        this.filterOrdering = filterOrder;
    }
	
    
    // inner class (Filter.Rule) =============================================
	
	/**
	 * The Rule (value object) representing rules queried from db. 
	 */
    public static class Rule extends DefaultDataObject {

    	private String _criteria;
    	private String _predicate;
    	private String _predicateValue;
    	
    	private boolean _isPersist;
    	private FilterCriteriaEnum _criteriaEnum;
    	private FilterPredicateEnum _predicateEnum;
    	
    	/**
    	 * Create an instance of Rule, marking it as NOT needing to be persisted.
    	 * 
    	 * @param id
    	 * @param criteria
    	 * @param predicate
    	 * @param predicateValue
    	 */
    	public Rule(String id, String criteria, String predicate, 
    			String predicateValue) {
    		_criteria = criteria;
    		_predicate = predicate;
    		_predicateValue = predicateValue;
    		setId(id == null ? UuidGenerator.getInstance().getUuid() : id);
    		_isPersist = false;
    		_criteriaEnum = FilterCriteriaEnum.getEnum(criteria);
    		_predicateEnum = FilterPredicateEnum.getEnum(predicate);
    	}
    	
    	/**
    	 * Create an instance of Rule, marking it as needed to be persisted.
    	 * 
    	 * @param criteria
    	 * @param predicate
    	 * @param predicateValue
    	 */
    	public Rule(String criteria, String predicate, String predicateValue) {
    		this (null, criteria, predicate, predicateValue);
    		_isPersist = true;
    	}
    	
    	
    	// misc ===============================================================
    	
    	/**
    	 * Return if the Filter is 'new' and 'to be persisted' or otherwise.
    	 * 
    	 * @return boolean
    	 */
    	public boolean isToBePersist() { return _isPersist; }
    	
    	/**
    	 * Return this Filter's Rule's Criteria.
    	 * 
    	 * @return FilterCriteriaEnum
    	 */
    	public FilterCriteriaEnum getFilterCriteriaEnum() { return _criteriaEnum; }
    	
    	/**
    	 * Return this Filter's Rule's Predicate.
    	 * 
    	 * @return FilterCriteriaEnum
    	 */
    	public FilterPredicateEnum getFilterPredicateEnum() { return _predicateEnum; }
    	
    	/**
    	 * Does the details of this {@link Filter.Rule} match the details of the 
    	 * {@link Message}.
    	 * 
    	 * @param message
    	 * @return
    	 */
    	public boolean match(Message message) 
    	throws MessagingException, IOException {
    		
    		// get criteria from message
    		Object criteria = _criteriaEnum.getCriteriaFromMessage(message);
    		
    		// compare
    		return _predicateEnum.compare(criteria, _predicateValue);
    	}
    	
    	
    	/**
    	 * Returns this Rule's i18n message to be used in {@link com.tms.collab.messaging.ui.FilterListingTable}
    	 * action column.
    	 * 
    	 * @return String
    	 */
    	public String getI18nMessage() {
    		Application application = Application.getInstance();
    		
    		String criteria = application.getMessage(_criteriaEnum.getI18nKey());
    		String predicate = application.getMessage(_predicateEnum.getI18nKey());
    		return criteria+" "+predicate+" "+ _predicateValue;
    	}
    	
    	
    	// getters / setters ==================================================
		public String getCriteria() {
			return _criteria;
		}
		public void setCriteria(String criteria) {
			this._criteria = criteria;
		}
		public String getPredicate() {
			return _predicate;
		}
		public void setPredicate(String predicate) {
			this._predicate = predicate;
		}
		public String getPredicateValue() {
			return _predicateValue;
		}
		public void setPredicateValue(String predicateValue) {
			this._predicateValue = predicateValue;
		}
		public String getRuleId() {
			return getId();
		}
		public void setRuleId(String ruleId) {
			setId(ruleId);
		}
    }
}
