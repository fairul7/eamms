package com.tms.collab.messaging.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.Util;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

/**
 * 
 */
public class FilterListingTable extends Table {
	
	private static final String ADD_ACTION = "add";
	private static final String DELETE_ACTION ="delete";
	private static final String ACTIVATE_ACTION = "activate";
	private static final String DEACTIVATE_ACTION = "deactivate";
    private static final String MOVE_UP_ACTION = "moveup";
    private static final String MOVE_DOWN_ACTION = "movedown";
	
	public static final String ROW_ID = "id";
    
    String _jsAlert = null;
	
	/**
	 * Page where we configure individual email filtering.
	 */
	private static final Forward EMAIL_FILTERING_CONFIG_PAGE = 
		new Forward("fowardToFilterConfigPage", 
				"emailFilterConfig.jsp", true);
	
	/**
	 * Page where we list email filterings available.
	 */
	private static final Forward EMAIL_FILTER_LISTING_PAGE = 
		new Forward("forwardToFilterListingPage",
				"emailFilterListing.jsp", true);
	
	
	// user id (grabbed upon onRequest)
	private String _userId;
	
	/**
	 * @see kacang.stdui.Table#init()
	 */
	public void init() {
		super.init();
		setModel(new FilterListingTableModel());
		setWidth("100%");
		setNumbering(true);
	}

	/**
	 * @see kacang.stdui.Table#onFilter(kacang.ui.Event)
	 */
	public Forward onFilter(Event evt) {
		return super.onFilter(evt);
	}
	
	/**
	 * @see kacang.stdui.Table#onPage(kacang.ui.Event)
	 */
	public Forward onPage(Event evt) {
		return super.onPage(evt);
	}
	
	/**
	 * @see kacang.ui.Widget#onRequest(kacang.ui.Event)
	 */
	public void onRequest(Event evt) {
		super.onRequest(evt);
		
		// grab user id
		_userId = Util.getUser(evt).getId();
	}
	
	/**
	 * @see kacang.stdui.Table#onSelection(kacang.ui.Event)
	 */
	public Forward onSelection(Event evt) {
		return super.onSelection(evt);
	}
	
	/**
	 * @see kacang.stdui.Table#onSort(kacang.ui.Event)
	 */
	public Forward onSort(Event evt) {
		return super.onSort(evt);
	}
	
	/**
	 * @see kacang.stdui.Table#onSubmit(kacang.ui.Event)
	 */
	public Forward onSubmit(Event evt) {
		return super.onSubmit(evt);
	}
	
	/**
	 * @see kacang.stdui.Table#onValidate(kacang.ui.Event)
	 */
	public Forward onValidate(Event evt) {
		String selectedAction = getSelectedAction();

		if (ADD_ACTION.equals(selectedAction)) {
			return doAddAction(evt);
		}
		else if (DELETE_ACTION.equals(selectedAction)) {
			return doDeleteAction(evt);
		}
		else if (ACTIVATE_ACTION.equals(selectedAction)) {
			return doActivateAction(evt);
		}
		else if (DEACTIVATE_ACTION.equals(selectedAction)) {
			return doDeactivateAction(evt);
		}
        else if (MOVE_UP_ACTION.equals(selectedAction)) {
            return doMoveUpAction(evt);
        }
        else if (MOVE_DOWN_ACTION.equals(selectedAction)) {
            return doMoveDownAction(evt);
        }
		throw new IllegalArgumentException("unexpected selected action ["+selectedAction+"]");
	}

	/**
	 * @see kacang.stdui.Table#onValidationFailed(kacang.ui.Event)
	 */
	public Forward onValidationFailed(Event evt) {
		return super.onValidationFailed(evt);
	}
	

    /**
     * @see kacang.stdui.Table#getDefaultTemplate()
     */
    public String getDefaultTemplate() {
        return "messaging/filterListingTable";
    }
	
	
	// inner class ============================================================
	class FilterListingTableModel extends TableModel {
		
		public FilterListingTableModel() {
			
			// action
			addAction(new TableAction(ADD_ACTION, 
					getMessage("messaging.filtering.button.addFilter")));
			addAction(new TableAction(DELETE_ACTION, 
					getMessage("messaging.filtering.button.deleteFilter"),
					"return confirm('"+getMessage("messaging.filtering.msg.deleteFilterConfirmation")+"');"));
			addAction(new TableAction(ACTIVATE_ACTION, 
					getMessage("messaging.filtering.button.activateFilter"),
					"return confirm('"+getMessage("messaging.filtering.msg.activateFilterConfirmation")+"');"));
			addAction(new TableAction(DEACTIVATE_ACTION, 
					getMessage("messaging.filtering.button.deactivateFilter"),
					"return confirm('"+getMessage("messaging.filtering.msg.deactivateFilterConfirmation")+"');"));
            addAction(new TableAction(MOVE_UP_ACTION,
                    getMessage("messaging.filtering.button.moveUp"),
                    "javascript: return checkOnlyOneCheckboxSelectected();"));
            addAction(new TableAction(MOVE_DOWN_ACTION,
                    getMessage("messaging.filtering.button.moveDown")));
            
            

			// filter
			
            
			
			// column
			TableColumn nameTableColumn = new TableColumn("name", 
					getMessage("messaging.filtering.table.name"), true);
			nameTableColumn.setUrl("emailFilterConfig.jsp");
			nameTableColumn.setUrlParam(ROW_ID);
			addColumn(nameTableColumn);
			
			TableColumn filterActionTableColumn = new TableColumn("i18nFilterAction", 
					getMessage("messaging.filtering.table.action"), true);
			addColumn(filterActionTableColumn);
			
			TableColumn filterActiveTableColumn = new TableColumn("i18nFilterActive", 
					getMessage("messaging.filtering.table.active"), true);
			addColumn(filterActiveTableColumn);
            
            TableColumn filterOrderTableColumn = new TableColumn("filterOrder", 
                    getMessage("messaging.filtering.table.order"), true);
            addColumn(filterOrderTableColumn);
		}
		
		/**
		 * @see kacang.stdui.TableModel#getTableRowKey()
		 */
		public String getTableRowKey() {
			return ROW_ID;
		}

		/**
		 * @see kacang.stdui.TableModel#getTableRows()
		 */
		public Collection getTableRows() {
			return getMessagingModule().getFiltersForUser(_userId);
		}

		/**
		 * @see kacang.stdui.TableModel#getTotalRowCount()
		 */
		public int getTotalRowCount() {
			return getMessagingModule().getFilterCountForUser(_userId);
		}
	}
	
	
	// protected ===============================================================
	protected String getMessage(String key) {
		Application application = Application.getInstance();
		return application.getMessage(key);
	}
	
	protected MessagingModule getMessagingModule() {
		Application application = Application.getInstance();
		return (MessagingModule) application.getModule(MessagingModule.class);
	}
	
	protected Forward doAddAction(Event evt) {
		return EMAIL_FILTERING_CONFIG_PAGE;
	}
	
	protected Forward doDeleteAction(Event evt) {
		
		// map returned with the key as filterId (in String)
		for (Iterator i = getSelectedRowMap().entrySet().iterator(); i.hasNext(); ) {
			Map.Entry entry = (Map.Entry) i.next();
			
			String filterId = (String) entry.getKey();
			getMessagingModule().removeDaoBasedFilter(filterId);
		}
		return EMAIL_FILTER_LISTING_PAGE;
	}
	
	protected Forward doActivateAction(Event evt) {
		
		// map returned with the key as filterId (in String)
		for (Iterator i = getSelectedRowMap().entrySet().iterator(); i.hasNext(); ) {
			// only activate when we have an entry selected, else ignore
			Map.Entry entry = (Map.Entry) i.next();

			String filterId = (String) entry.getKey();
			getMessagingModule().activateDaoBasedFitler(filterId);
		}
		return EMAIL_FILTER_LISTING_PAGE;
	}
	
	protected Forward doDeactivateAction(Event evt) {
		
		// map returned with the key as filterId (in String)
		for (Iterator i = getSelectedRowMap().entrySet().iterator(); i.hasNext(); ) {
			// only passivate when we have an entry selected, else ignore
			Map.Entry entry = (Map.Entry) i.next();
			
			String filterId = (String) entry.getKey();
			getMessagingModule().pasivateDaoBasedFilter(filterId);
		}
		return EMAIL_FILTER_LISTING_PAGE;
	}
	
	protected Forward doMoveUpAction(Event evt) {
        
        Iterator i = getSelectedRowMap().entrySet().iterator();
        if (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            
            String userId = Util.getUser(evt).getId();
            String filterId = (String) entry.getKey();
            getMessagingModule().moveUpFilterOrdering(userId, filterId);
        }
        return EMAIL_FILTER_LISTING_PAGE;
    }
    
    protected Forward doMoveDownAction(Event evt) {

        Iterator i = getSelectedRowMap().entrySet().iterator();
        if (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
         
            String userId = Util.getUser(evt).getId();
            String filterId = (String) entry.getKey();
            getMessagingModule().moveDownFilterOrdering(userId, filterId);
        }
        
        
        return EMAIL_FILTER_LISTING_PAGE;
    }
    
	// private ================================================================
	
	private boolean hasIdFromRequest(Event evt) {
		String id = evt.getParameter(ROW_ID);
		if (id != null && id.trim().length() > 0) {
			return true;
		}
		return false;
	}
}


