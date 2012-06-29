package com.tms.collab.messaging.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.tms.collab.messaging.model.DaoMessageFilter;
import com.tms.collab.messaging.model.Filter;
import com.tms.collab.messaging.model.FilterActionEnum;
import com.tms.collab.messaging.model.FilterConditionEnum;
import com.tms.collab.messaging.model.FilterCriteriaEnum;
import com.tms.collab.messaging.model.FilterManager;
import com.tms.collab.messaging.model.FilterPredicateEnum;
import com.tms.collab.messaging.model.Folder;
import com.tms.collab.messaging.model.Message;
import com.tms.collab.messaging.model.MessagingException;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.Pop3Account;
import com.tms.collab.messaging.model.Util;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.FormField;
import kacang.stdui.Label;
import kacang.stdui.Radio;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.Validator;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;


/**
 * {@link FilterConfigForm} widget for template at 
 * /WEB-INF/templates/default/messaging/filterConfigForm.jsp
 */
public class FilterConfigForm extends Form implements Serializable {

	private static final String ADD_RULE_BUTTON_ACTION = "btnAddRule";
	private static final String DELETE_RULE_BUTTON_ACTION = "btnDeleteRule";
	private static final String SAVE_BUTTON_ACTION = "btnSave";
	private static final String CANCEL_BUTTON_ACTION = "btnCancel";
	private static final String RUN_BUTTON_ACTION = "btnRun";
	
	
	private static final String MOVE_TO_FOLDER_RADIO_ACTION = "radioMoveToFolder";
	private static final String MARK_AS_READ_RADIO_ACTION = "radioMarkAsRead";
	private static final String DELETE_RADIO_ACTION = "radioDelete";
	private static final String FORWARD_RADIO_ACTION = "radioForward";
	
	
	private static final Forward SUCCESS_FORWARD = new Forward(
			"successForward", "emailFilterListing.jsp", true);
	
	
	// map (folder.getId() <--> folder.getName()) for selBoxMoveToFolder's options
	private Map _moveToFolderOptionsMap;
	// map (folder.getId() <--> folder.getName()) for selBoxRunOnFolder's options
	private Map _runOnFolderOptionsMap;
	// store folder.getId() of DEFAULT selection
	private String _moveToFolderSelectionKey;
	
	
	// components
	private Label lblCondition;
	private Label lblRules;
	private Label lblCriteria;
	private Label lblPredicate;
	private Label lblAction;
	private Label lblName;
	private Label lblSpecialOperation;
	private Label lblRun;
    private Label lblRememberToSave;
    private Label lblRuleNote;
    private Label lblActionNote;
	
	private Button btnAddRule;
	private Button btnDeleteRule;
	private Button btnSave;
	private Button btnCancel;
	private Button btnRun;
	
	private Radio radioMoveToFolder;
	private Radio radioMarkAsRead;
	private Radio radioDelete;
	private Radio radioForward;
	
   
    private Line line;
	
	private CheckBox chkboxActive;
	
	
	// Map (selbox uid key <-> SelectBox)
	private Map selBoxCriterias = new LinkedHashMap();
	// Map (selbox uid key <-> SelectBox)
	private Map selBoxPredicates = new LinkedHashMap();
    // Map (chkbox uid key <-> CheckBox)
    private Map chkboxCriterias = new LinkedHashMap();
    // Map (uid <-> uid)
    private Map _widgetIds = new LinkedHashMap();
    
    
	private SelectBox selBoxMoveToFolder;
	private SelectBox selBoxRunOnFolder;
	
	
	
	
	private TextField txtFldForward;
	// Map (txtFld uid key <-> TextField)
	private Map txtFldPredicates = new LinkedHashMap();
	private TextField txtFldName;
	
	private Radio radioAll;
	private Radio radioAny;
	
	// form params
	
	/**
	 * NOTE:
	 *  - set during onSubmit to grab button action
	 */
	private String _selectedAction;
	
	/**
	 * NOTE:
	 *  - the filter (presence indicate an edit instead of create new)
	 *  - grabbed upon every request (onRequest)
	 */
	private Filter _filter;
	
	public FilterConfigForm() {
		setWidth("100%");
    }
	
	/**
	 * @see kacang.ui.Widget#getDefaultTemplate()
	 */
	public String getDefaultTemplate() {
		return "messaging/filterConfigForm";
	}

	
	/**
	 * @see kacang.ui.Widget#init()
	 */
	public void init() {
		super.init();
	}
    
	
	protected void initComponents(Event evt) {
		// initialize components used.
		
		// labels
		lblCondition = new Label("lblCondition", 
				getMessage("messaging.filtering.label.condition"));
		lblRules = new Label("lblRules", 
				getMessage("messaging.filtering.label.rules"));
		lblCriteria = new Label("lblCriteria", 
				getMessage("messaging.filtering.label.criteria"));
		lblPredicate = new Label("lblPredicate", 
				getMessage("messaging.filtering.label.predicate"));
		lblAction = new Label("lblAction", 
				getMessage("messaging.filtering.label.action"));
		lblName = new Label("lblName",
				getMessage("messaging.filtering.label.name")+" *");
		lblSpecialOperation = new Label("lblSpecialOperation",
				getMessage("messaging.filtering.label.specialOperation"));
		lblRun = new Label("lblRun",
				getMessage("messaging.filtering.label.run"));
        lblRememberToSave = new Label("lblRememberToSave", 
                getMessage("messaging.filtering.action.label.rememberToSave"));
        lblRuleNote = new Label("lblRuleNote", 
                getMessage("messaging.filtering.rule.label.note")+" *");
        lblActionNote = new Label("lblActionNote",
                getMessage("messaging.filtering.action.label.note"));
		
		
		
		// buttons
		btnAddRule = new Button(ADD_RULE_BUTTON_ACTION, 
				getMessage("messaging.filtering.button.addRule"));
		btnDeleteRule = new Button(DELETE_RULE_BUTTON_ACTION, 
				getMessage("messaging.filtering.button.deleteRule"));
		btnSave = new Button(SAVE_BUTTON_ACTION, 
				getMessage("messaging.filtering.button.save"));
		btnSave.setOnClick("javascript:return confirm('"+getMessage("messaging.filtering.msg.saveConfirmation")+"');");
		btnCancel = new Button(CANCEL_BUTTON_ACTION, 
				getMessage("messaging.filtering.button.cancel"));
		btnRun = new Button(RUN_BUTTON_ACTION, 
				getMessage("messaging.filtering.button.run"));
		btnRun.setOnClick("javascript:return confirm('"+getMessage("messaging.filtering.msg.runConfirmation")+"');");
		
		
		
		
		// radio
		radioMoveToFolder = new Radio(MOVE_TO_FOLDER_RADIO_ACTION,
				getMessage("messaging.filtering.radio.moveToFolder"), true);
		radioMoveToFolder.setGroupName("radioActionGroup");
		radioMarkAsRead = new Radio(MARK_AS_READ_RADIO_ACTION, 
				getMessage("messaging.filtering.radio.markAsRead"), false);
		radioMarkAsRead.setGroupName("radioActionGroup");
		radioDelete = new Radio(DELETE_RADIO_ACTION, 
				getMessage("messaging.filtering.radio.delete"), false);
		radioDelete.setGroupName("radioActionGroup");
		radioForward = new Radio(FORWARD_RADIO_ACTION, 
				getMessage("messaging.filtering.radio.forward"), false);
		radioForward.setGroupName("radioActionGroup");
		radioAll = new Radio("radioAll", 
				getMessage("messaging.filtering.radio.all"), true);
		radioAll.setGroupName("radioConditionGroup");
		radioAny = new Radio("radioAny", 
				getMessage("messaging.filtering.radio.any"), false);
		radioAny.setGroupName("radioConditionGroup");
		
		
		
		// checkbox
		chkboxActive = new CheckBox("chkboxActive", 
				getMessage("messaging.filtering.checkbox.active"), false);
		chkboxCriterias = new LinkedHashMap();    // Map of CheckBoxs
		
        // line
        line = new Line("line");
        
        _widgetIds = new LinkedHashMap();
		
		// select box
		selBoxCriterias = new LinkedHashMap();        // Map of SelectBoxs
		selBoxPredicates = new LinkedHashMap();       // Map of SelectBoxs
		txtFldPredicates = new LinkedHashMap();       // Map of SelectBoxs
		selBoxMoveToFolder = new SelectBox("selBoxMoveToFolder");     
		selBoxRunOnFolder = new SelectBox("selBoxRunOnFolder");
		
		
		try {
			// make sure there is one rule during init
			
			Map criteriaMap = FilterCriteriaEnum.getPresentationMap();
			Map predicateMap = FilterPredicateEnum.getPresentationMap();
			
			String uid = UuidGenerator.getInstance().getUuid();
            
            // add widget Ids
            _widgetIds.put(uid, uid);
            
            
            // add criteria check box
            CheckBox chkBoxCriteria = new CheckBox("chkBoxCriteria"+uid);
            chkboxCriterias.put(uid, chkBoxCriteria);
            
            
			// add criteria selectbox 
			SelectBox selectBoxCriteria = new SelectBox("selBoxCriteria"+uid, 
					criteriaMap, new HashMap(), false, 1);
			selectBoxCriteria.setSelectedOption((String) criteriaMap.keySet().iterator().next());
			selBoxCriterias.put(uid, selectBoxCriteria);
			
			
			// add predicate selectbox 
			SelectBox selectBoxPredicate = new SelectBox("selBoxPredicate"+uid,
					predicateMap, new HashMap(), false, 1);
			selectBoxPredicate.setSelectedOption((String) predicateMap.keySet().iterator().next());
			selBoxPredicates.put(uid, selectBoxPredicate);
			
			
			// add predicate text field (dynamically on add button click) 
			TextField txtFldPredicate = new TextField("txtFldPredicate"+uid);
            txtFldPredicate.setSize("30");
			txtFldPredicates.put(uid, txtFldPredicate);
			
			
			// CACHE: only when _moveToFolderOptionsMap is null, this is done only once.
			//if (_moveToFolderOptionsMap == null) {
				_moveToFolderOptionsMap = new LinkedHashMap();
				Collection folders = getModule().getFolders(Util.getUser(evt).getId());
                Folder[] folArr = (Folder[]) folders.toArray(new Folder[0]);
                
                // to order Folder(s) accordingly, so they appear in select box 
                // according to the order Inbox, Draft, Sent, Outbox, Trash, Qm etc
                Arrays.sort(folArr, new Comparator() {
					public int compare(Object o1, Object o2) {
                        Folder f1 = (Folder) o1;
                        Folder f2 = (Folder) o2;
                        if (f1.getName().equals(Folder.FOLDER_INBOX)) { return -1; }
                        if (f1.getName().equals(Folder.FOLDER_DRAFT) && (!f2.getName().equals(Folder.FOLDER_INBOX))) { return -1; }
                        if (f1.getName().equals(Folder.FOLDER_SENT) && (!f2.getName().equals(Folder.FOLDER_INBOX)) && (!f2.getName().equals(Folder.FOLDER_DRAFT))  ) { return -1; }
                        if (f1.getName().equals(Folder.FOLDER_OUTBOX) && (!f2.getName().equals(Folder.FOLDER_INBOX)) && (!f2.getName().equals(Folder.FOLDER_DRAFT)) && (!f2.getName().equals(Folder.FOLDER_SENT))) { return -1; }
                        if (f1.getName().equals(Folder.FOLDER_TRASH) && (!f2.getName().equals(Folder.FOLDER_INBOX)) && (!f2.getName().equals(Folder.FOLDER_DRAFT)) && (!f2.getName().equals(Folder.FOLDER_SENT)) && (!f2.getName().equals(Folder.FOLDER_OUTBOX))) { return -1; }
                        if (f1.getName().equals(Folder.FOLDER_QM) && (!f2.getName().equals(Folder.FOLDER_INBOX)) && (!f2.getName().equals(Folder.FOLDER_DRAFT)) && (!f2.getName().equals(Folder.FOLDER_SENT)) && (!f2.getName().equals(Folder.FOLDER_OUTBOX)) && (!f2.getName().equals(Folder.FOLDER_TRASH))) { return -1; }
						return 1;
					}
                });
                
                for (int z=0; z< folArr.length; z++) {
					Folder f = (Folder) folArr[z];
					_moveToFolderOptionsMap.put(f.getId(), f.getName());
				}
			//}
				
				_runOnFolderOptionsMap = new LinkedHashMap();
				for (int z=0; z< folArr.length; z++) {
					Folder f = (Folder) folArr[z];
					_runOnFolderOptionsMap.put(f.getId(), f.getName());
				}
				
				
			// populate move to folder (select box) with user's folders and auto select the first
			selBoxMoveToFolder.setOptionMap(_moveToFolderOptionsMap);
			if (_moveToFolderOptionsMap.size() > 0) {
				selBoxMoveToFolder.setSelectedOption((String) _moveToFolderOptionsMap.keySet().iterator().next());
			}
			
			// populate run on folder (select box) with user's folders and auto select the first
			selBoxRunOnFolder.setOptionMap(_runOnFolderOptionsMap);
			if (_runOnFolderOptionsMap.size() > 0) {
				selBoxRunOnFolder.setSelectedOption((String) _runOnFolderOptionsMap.keySet().iterator().next());
			}
		}
		catch(MessagingException e) {
			Log.getLog(FilterConfigForm.class).error(e.toString(), e);
			throw new RuntimeException(e);
		}
		
        
		// text field
		txtFldForward = new TextField("txtFldForward");
        txtFldForward.setSize("30");
		txtFldName = new TextField("txtFldName");
        txtFldName.setSize("30");
	}
	
	protected void populateComponents(Event evt) {
		if (_filter != null) {
			//we are in edit mode
			FilterActionEnum fae = FilterActionEnum.getEnum(_filter.getFilterAction());
			FilterConditionEnum fce = FilterConditionEnum.getEnum(_filter.getFilterCondition());
			String name = _filter.getName();
			boolean isActive = _filter.isFilterActive();
			Collection rules = _filter.getRules();
	
			
			// name
			txtFldName.setValue(name);
			
			// isActive
			chkboxActive.setChecked(isActive);
			
			// conditions
			if (fce.equals(FilterConditionEnum.ALL)) {
				radioAll.setChecked(true);
				radioAny.setChecked(false);
			}
			else {
				radioAll.setChecked(false);
				radioAny.setChecked(true);
			}
			
			
			// rules
            _widgetIds = new LinkedHashMap();
            chkboxCriterias = new LinkedHashMap();
			selBoxCriterias = new LinkedHashMap();
			selBoxPredicates = new LinkedHashMap();
			txtFldPredicates = new LinkedHashMap();
            
			
			for (Iterator i = rules.iterator(); i.hasNext(); ) {
                
                String uid = UuidGenerator.getInstance().getUuid();
                
				Filter.Rule rule = (Filter.Rule) i.next();
				
				Map criteriaMap = FilterCriteriaEnum.getPresentationMap();
				Map predicateMap = FilterPredicateEnum.getPresentationMap();
				
                
                
                // add widget Ids
                _widgetIds.put(uid, uid);
				
                // add criteria checkbox
                CheckBox chkBoxCriteria = new CheckBox("chkBoxCriteria"+uid);
                chkboxCriterias.put(uid, chkBoxCriteria);
                
                
				// add criteria selectbox 
				SelectBox selectBoxCriteria = new SelectBox("selBoxCriteria"+uid, 
						criteriaMap, new HashMap(), false, 1);
				selectBoxCriteria.setSelectedOption(rule.getCriteria());
				selBoxCriterias.put(uid, selectBoxCriteria);
				
				
				// add predicate selectbox 
				SelectBox selectBoxPredicate = new SelectBox("selBoxPredicate"+uid,
						predicateMap, new HashMap(), false, 1);
				selectBoxPredicate.setSelectedOption(rule.getPredicate());
				selBoxPredicates.put(uid, selectBoxPredicate);
				
				
				// add predicate text field (dynamically on add button click) 
				TextField txtFldPredicate = new TextField("txtFldPredicate"+uid);
                txtFldPredicate.setSize("30");
				txtFldPredicate.setValue(rule.getPredicateValue());
				txtFldPredicates.put(uid, txtFldPredicate);
			}
			
			
			// filters' actions
			if (fae.equals(FilterActionEnum.MOVE_TO_FOLDER)) {
				radioMoveToFolder.setChecked(true);
				selBoxMoveToFolder.setSelectedOption(
						_filter.getFilterValue() 	// folderId
				);
			}
			else if (fae.equals(FilterActionEnum.DELETE)) {
				radioDelete.setChecked(true);
			}
			else if (fae.equals(FilterActionEnum.MARK_AS_READ)) {
				radioMarkAsRead.setChecked(true);
			}
			else if (fae.equals(FilterActionEnum.FORWARD)) {
				radioForward.setChecked(true);
				txtFldForward.setValue(_filter.getFilterValue());
			}
		}		
	}
	
	protected void initForm(Event evt) {
		Application application = Application.getInstance();
		
		removeChildren();
		setMethod("POST");
		
		// add hierarchy
		addChild(lblCondition);
		addChild(lblAction);
		addChild(lblCriteria);
		addChild(lblPredicate);
		addChild(lblRules);
		addChild(lblName);
		addChild(lblRun);
		addChild(lblSpecialOperation);
        addChild(lblRememberToSave);
        addChild(lblRuleNote);
        addChild(lblActionNote);
		
		addChild(btnAddRule);
		addChild(btnDeleteRule);
		addChild(btnSave);
		addChild(btnCancel);
		addChild(btnRun);
		
		addChild(radioDelete);
		addChild(radioForward);
		addChild(radioMarkAsRead);
		addChild(radioMoveToFolder);
		addChild(chkboxActive);
		
        for (Iterator i = chkboxCriterias.values().iterator(); i.hasNext(); ) {
            addChild((CheckBox) i.next());
        }
        
		for (Iterator i = selBoxCriterias.values().iterator(); i.hasNext(); ) {
			addChild((SelectBox) i.next());
		}
        
		addChild(selBoxMoveToFolder);
		for (Iterator i = selBoxPredicates.values().iterator(); i.hasNext(); ) {
			addChild((SelectBox) i.next());
		}
		addChild(txtFldForward);
		for (Iterator i = txtFldPredicates.values().iterator(); i.hasNext(); ) {
            TextField tf = (TextField) i.next();
            tf.setSize("30");
			addChild(tf);
		}
		
		addChild(selBoxRunOnFolder);
		
		addChild(txtFldName);
		
		addChild(radioAll);
		addChild(radioAny);
        
        addChild(line);
	}
	
	/*protected void initValidators(Event event) {
		
		// validators
		txtFldName.addChild(new ValidatorNotEmpty("notEmpty"));
		for (Iterator i = selBoxCriterias.values().iterator(); i.hasNext(); ) {
			SelectBox sb = (SelectBox) i.next();
			sb.addChild(new ValidatorNotEmpty("notEmpty"));
		}
		
		for (Iterator i = selBoxPredicates.values().iterator(); i.hasNext(); ) {
			SelectBox sb = (SelectBox) i.next();
			sb.addChild(new ValidatorNotEmpty("notEmpty"));
		}
		
		for (Iterator i = txtFldPredicates.values().iterator(); i.hasNext(); ) {
			TextField tf = (TextField) i.next();
			tf.addChild(new ValidatorNotEmpty("notEmpty"));
		}
		
		radioMoveToFolder.addChild(new Validator("ifCheckThenMustNotEmpty1") {
			
			public boolean validate(FormField formField) {
				
				if (radioMoveToFolder.isChecked()) {
					if (selBoxMoveToFolder.getSelectedOptions().isEmpty()) {
						return false;
					}
				}
				return true;
			}
		});
		
		txtFldForward.addChild(new Validator("ifCheckThenMustNotEmpty2") {
			public boolean validate(FormField formField) {
				if (radioForward.isChecked()) {
					if (((String)txtFldForward.getValue()).trim().length() <= 0) {
						return false;
					}
				}
				return true;
			}
		});
			
	}*/
	
	
	/**
	 * @see kacang.ui.Widget#onRequest(kacang.ui.Event)
	 */
	public void onRequest(Event evt) {
		// see if we are in 'edit' or 'create new' mode
		if (evt.getParameter(FilterListingTable.ROW_ID) != null &&
				evt.getParameter(FilterListingTable.ROW_ID).trim().length() > 0) {
			String filterId = evt.getParameter(FilterListingTable.ROW_ID);
			_filter = getModule().getFilter(filterId);
		}
		else { // not in edit mode
			_filter = null;
		}
		
		initComponents(evt);
		
		if (_filter != null) {
			populateComponents(evt);
		}
		
		initForm(evt);
		//initValidators(evt);
	}
	
	
	/**
	 * @see kacang.stdui.Form#onSubmit(kacang.ui.Event)
	 */
	public Forward onSubmit(Event evt) {
		// this is to grab the action (button) once every time a submit occurred

		// we want to get form and validators 'init'ed before calling super
		kacang.ui.Forward f =super.onSubmit(evt);
		String action = null;
        String buttonClicked = findButtonClicked(evt);
        if (buttonClicked != null) {
            if (buttonClicked.length() > getAbsoluteName().length()) {
                action = buttonClicked.substring(getAbsoluteName().length() + 1);
            }
        }
        setSelectedAction(action);
        
        
        
        //initComponents(evt);
		initForm(evt);
		
		
		
		
		//	validate only on submit (save button pressed)
		if (getSelectedAction().equals(SAVE_BUTTON_ACTION)) {		
			/*initValidators(evt);*/
			
			
		}

		if (getSelectedAction().equals(CANCEL_BUTTON_ACTION)) {
			f= doCancelAction(evt);
		}
		
        return f;
	}
	
	
	/**
	 * @see kacang.stdui.Form#onValidate(kacang.ui.Event)
	 */
	public Forward onValidate(Event evt) {
		super.onValidate(evt);
		
		if (getSelectedAction().equals(ADD_RULE_BUTTON_ACTION)) {
			Forward f = doAddRuleAction(evt);
			return f;
		}
		if (getSelectedAction().equals(DELETE_RULE_BUTTON_ACTION)) {
			return doDeleteRuleAction(evt);
		}
		if (getSelectedAction().equals(SAVE_BUTTON_ACTION)) {
			if (((String)txtFldName.getValue()).trim().length() <= 0){
				setInvalid(true);
				txtFldName.setInvalid(true);
			}
			for (Iterator i = selBoxCriterias.values().iterator(); i.hasNext(); ) {
				SelectBox sb = (SelectBox) i.next();
				if (sb.getSelectedOptions().isEmpty()){
					setInvalid(true);
					sb.setInvalid(true);
				}				
			}
			
			for (Iterator i = selBoxPredicates.values().iterator(); i.hasNext(); ) {
				SelectBox sb = (SelectBox) i.next();
				if (sb.getSelectedOptions().isEmpty()){
					setInvalid(true);
					sb.setInvalid(true);
				}
			}
			
			for (Iterator i = txtFldPredicates.values().iterator(); i.hasNext(); ) {
				TextField tf = (TextField) i.next();
				if (((String)tf.getValue()).trim().length() <= 0){
					setInvalid(true);
					tf.setInvalid(true);
				}
			}
			if (radioMoveToFolder.isChecked()) {
				if (selBoxMoveToFolder.getSelectedOptions().isEmpty()) {
					setInvalid(true);
					selBoxMoveToFolder.setInvalid(true);
				}
			}
			if (radioForward.isChecked()) {
				if (((String)txtFldForward.getValue()).trim().length() <= 0) {
					setInvalid(true);
					txtFldForward.setInvalid(true);
				}
			}if(this.isInvalid())
				return null;
			
			return doSaveAction(evt);
		}
		if (getSelectedAction().equals(CANCEL_BUTTON_ACTION)) {
			return doCancelAction(evt);
		}
		if (getSelectedAction().equals(RUN_BUTTON_ACTION))  {
			return doRunAction(evt);
		}
		throw new IllegalArgumentException("selection action ["+getSelectedAction()+"] not identified");
	}
	
	
	/**
	 * @see kacang.stdui.Form#onValidationFailed(kacang.ui.Event)
	 */
	public Forward onValidationFailed(Event evt) {
		return super.onValidationFailed(evt);
	}
	
	
	// properties =============================================================
	/**
     * Return the criteria checkbox, criteria sel box, predicate selbox, predicate txt fld pair's
     * unique id. These components are generated on the fly and are identified through these
     * ids.
	 */
	public List getRulesWidgetIds() {
	    return new ArrayList(_widgetIds.keySet());
    }
	
	
	// protected ==============================================================
	protected Forward doRunAction(Event evt) {
		try {
			Log.getLog(FilterConfigForm.class).debug("start filtering (on demand)");
			// run filter against folder
			FilterManager fm = new FilterManager(getModule());
			fm.addFilter(new DaoMessageFilter());
		
			Collection messages = getModule().getMessages((String) selBoxRunOnFolder.getSelectedOptions().keySet().iterator().next());
		
			
			String userId = Util.getUser(evt).getId();
			Pop3Account p3acc = getModule().getPop3Account(userId);
			
		
			// do filtering based on a single filter
			fm.filter((Message[]) messages.toArray(new Message[0]), 
				new Object[] {
					userId, p3acc, _filter.getId()
				});	
		
			Log.getLog(FilterConfigForm.class).debug("finnish filtering (on demand)");
			return SUCCESS_FORWARD;
		}
		catch(MessagingException e) {
			Log.getLog(FilterConfigForm.class).error(e.toString(), e);
			throw new RuntimeException(e);
		}
	}
	
	
	protected Forward doAddRuleAction(Event evt){
        String uid = UuidGenerator.getInstance().getUuid();
		
		Map criteriaMap = FilterCriteriaEnum.getPresentationMap();
		Map predicateMap = FilterPredicateEnum.getPresentationMap();
		
		
        // add widget ids
        _widgetIds.put(uid, uid);
        
        // add criteria chkbox (dynamically when add button is clicked)
        CheckBox chkboxCriteria = new CheckBox("chkBoxCriteria"+ uid);
        chkboxCriterias.put(uid, chkboxCriteria);
        
        
		// add criteria selectbox (dynamicaly on add button click)
		SelectBox selectBoxCriteria = new SelectBox("selBoxCriteria"+ uid, 
				criteriaMap, new HashMap(), false, 1);
		selectBoxCriteria.setSelectedOption((String) criteriaMap.keySet().iterator().next());
		selBoxCriterias.put(uid, selectBoxCriteria);
		
		
		// add predicate selectbox (dynamicaly on add button click)
		SelectBox selectBoxPredicate = new SelectBox("selBoxPredicate"+ uid,
				predicateMap, new HashMap(), false, 1);
		selectBoxPredicate.setSelectedOption((String) predicateMap.keySet().iterator().next());
		selBoxPredicates.put(uid, selectBoxPredicate);
		
		
		// add predicate text field (dynamically on add button click) 
		txtFldPredicates.put(uid, new TextField("txtFldPredicate"+ uid));
		
		
		
		initForm(evt);
		
		return null;
	}
	
	protected Forward doSaveAction(Event evt) {
		// do saving
		
		// list of FilterVo.RuleVo
		List rules = new ArrayList();
		
		Iterator existingRulesItr = null;
		if (_filter != null) {
			existingRulesItr = _filter.getRules().iterator();
		}
		
		// get all the Rule
        for (Iterator i = _widgetIds.keySet().iterator(); i.hasNext(); ) {
            String uid = (String) i.next();
			SelectBox criteriaSelectBox = (SelectBox) selBoxCriterias.get(uid);
			SelectBox predicateSelectBox = (SelectBox) selBoxPredicates.get(uid);
			TextField predicateTxtFld = (TextField) txtFldPredicates.get(uid);
			
			
			Filter.Rule rule = null;
			if (existingRulesItr != null && existingRulesItr.hasNext()) {
				// edit mode
				Filter.Rule tmpRule = (Filter.Rule) existingRulesItr.next();
				// we explicitly add an id, so MessagingDao will persist using UPDATE statement
				rule = new Filter.Rule(
					tmpRule.getId(),
					(String) criteriaSelectBox.getSelectedOptions().keySet().iterator().next(),
					(String) predicateSelectBox.getSelectedOptions().keySet().iterator().next(),
					(String) predicateTxtFld.getValue()
				);
			}
			else {
				rule = new Filter.Rule(
					(String) criteriaSelectBox.getSelectedOptions().keySet().iterator().next(),
					(String) predicateSelectBox.getSelectedOptions().keySet().iterator().next(),
					(String) predicateTxtFld.getValue()
				);
			}
			rules.add(rule);
		}

		// then generate the Filter
		Filter filter = null;
		
		if (_filter == null) {
			filter = new Filter(
				Util.getUser(evt).getId(),
				(String) txtFldName.getValue(),
				getActionValue(),
				chkboxActive.isChecked(),
				getActionName(), 
				(radioAll.isChecked()) ? (FilterConditionEnum.ALL.getName()) : (FilterConditionEnum.ANY.getName()),
				rules
			);
		}
		else {
			// we 'explicitly' add an Id, so dao would do an UPDATE statement
			filter = new Filter(
					_filter.getId(),
					Util.getUser(evt).getId(),
					(String) txtFldName.getValue(),
					getActionValue(),
					chkboxActive.isChecked(),
					getActionName(), 
					(radioAll.isChecked()) ? (FilterConditionEnum.ALL.getName()) : (FilterConditionEnum.ANY.getName()),
					rules
				);
            filter.setFilterOrder(_filter.getFilterOrder());
		}
        
		// let module do the adding of filter
		getModule().addOrUpdateDaoBasedFilter(filter, Util.getUser(evt).getId());
		
		return SUCCESS_FORWARD;
	}
	
	protected Forward doDeleteRuleAction(Event evt) {
        // NOTE:
        // BUG #2291, add checkbox and remove only rules that are checked
        
        List toBeDeleted = new ArrayList();
        for (Iterator i = chkboxCriterias.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            String uid = (String) entry.getKey();
            CheckBox chkBox = (CheckBox) entry.getValue();
            
            if (chkBox.isChecked()) {
                toBeDeleted.add(uid);
            }
        }
        
        for (Iterator i = toBeDeleted.iterator(); i.hasNext(); ) {
            String uid = (String) i.next();
            
            _widgetIds.remove(uid);
            chkboxCriterias.remove(uid);
            selBoxCriterias.remove(uid);
            selBoxPredicates.remove(uid);
            txtFldPredicates.remove(uid);
        }
        
        
        initForm(evt);
        
		return null;
	}
	
	protected Forward doCancelAction(Event evt) {
		// do cancelling
		return SUCCESS_FORWARD;
	}
	
	
	protected String getMessage(String key) {
		Application application = Application.getInstance();
		return application.getMessage(key);
	}
	
	protected MessagingModule getModule() {
		Application application = Application.getInstance();
		return (MessagingModule) 
			application.getModule(MessagingModule.class);
	}
	
	protected String getSelectedAction() { return _selectedAction; }
	
	
	

	// set during onSubmit 
	private void setSelectedAction(String selectedAction) { _selectedAction = selectedAction; }
	
	private String getActionName() {
		if (radioMoveToFolder.isChecked()) {
			return FilterActionEnum.MOVE_TO_FOLDER.getName();
		}
		else if (radioMarkAsRead.isChecked()) {
			return FilterActionEnum.MARK_AS_READ.getName();
		}
		else if (radioDelete.isChecked()) {
			return FilterActionEnum.DELETE.getName();
		}
		else if (radioForward.isChecked()) {
			return FilterActionEnum.FORWARD.getName();
		}
		throw new IllegalArgumentException(
				"no action radio button selected [THIS SHOULD NOT HAPPENED, VALIDATION " +
				"DONE at the presentation layer");
	}
	
	private String getActionValue() {
		if (radioMoveToFolder.isChecked()) {
			// folder id 
			return (String) selBoxMoveToFolder.getSelectedOptions().keySet().iterator().next();
		}
		else if (radioForward.isChecked()) {
			// forwarder's mail address
			return (String) txtFldForward.getValue();
		}
		// other conditions does not require filterValue
		else if (radioMarkAsRead.isChecked() ||
				 radioDelete.isChecked()) {
			return null;
		}
		throw new IllegalArgumentException(
				"no action radio button selected [THIS SHOULD NOT HAPPENED, VALIDATION " +
				"DONE at the presentation layer");
	}
}


