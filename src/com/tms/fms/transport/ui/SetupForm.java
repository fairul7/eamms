package com.tms.fms.transport.ui;

import java.util.Date;

import com.tms.fms.transport.model.*;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.*;
import kacang.stdui.validator.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.*;

public class SetupForm extends Form{
	
	public static final String SETUP_ADD_SUCCESS = "setup.success";
	public static final String SETUP_ADD_FAIL = "setup.fail";
	public static final String SETUP_ADD_EXIST = "setup.exist";
	public static final String SETUP_ADD_NAME_INVALID = "name.invalid";
	
	public static final String SETUP_ACTION_ADD = "setup.action.add";
	public static final String SETUP_ACTION_EDIT = "setup.action.edit";
	
	private TextField name;
	private TextBox description;
	private Radio rdEngineering;
	private Radio rdTransport;
	private CheckBox isActive;
	private Panel buttonPanel;
	private Panel checkBoxPanel;
	private Button submit;
	private Button cancel;
	
	private String setupId;
	private String action;
	private String setupType = "";
	private String cancelUrl = "";
	private String setupString = "";
	private String whoModifyId = "";
	
	private Label namelbl;
	
	public String getSetupString() {
		return setupString;
	}

	public void setSetupString(String setupString) {
		this.setupString = setupString;
	}

	public String getSetupId() {
		return setupId;
	}

	public void setSetupId(String setupId) {
		this.setupId = setupId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getSetupType() {
		return setupType;
	}

	public void setSetupType(String setupType) {
		this.setupType = setupType;
	}

	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	public String getWhoModifyId() {
		return whoModifyId;
	}

	public void setWhoModifyId(String whoModifyId) {
		this.whoModifyId = whoModifyId;
	}
	
	public void init() {
		if (action == null || (!SETUP_ACTION_ADD.equals(action) && !SETUP_ACTION_EDIT.equals(action))) {action = SETUP_ACTION_ADD;}
    }
	
	public void onRequest(Event event) {
		if(setupId == null){
			action = SETUP_ACTION_ADD;
		}
		
		initForm();
	    
		if (SETUP_ACTION_EDIT.equals(action)) {
	    	populateFields();
	    }
    }
	
	public void initForm() {
		
		if(setupType.equals(SetupObject.SETUP_CATEGORY)){
			setupString = "category";
		}else if(setupType.equals(SetupObject.SETUP_BODY_TYPE)){
			setupString = "bodyType";
		}else if(setupType.equals(SetupObject.SETUP_CHANNEL)){
			setupString = "channel";
		}else if(setupType.equals(SetupObject.SETUP_FUEL_TYPE)){
			setupString = "fuelType";
		}else if(setupType.equals(SetupObject.SETUP_INACTIVE_REASON)){
			setupString = "inactiveReason";
		}else if(setupType.equals(SetupObject.SETUP_MAKE_TYPE)){
			setupString = "makeType";
		}else if(setupType.equals(SetupObject.SETUP_OUTSOURCE_PANEL)){
			setupString = "outsourcePanel";
		}else if(setupType.equals(SetupObject.SETUP_PETROL_CARD)){
			setupString = "petrolCard";
		}else if(setupType.equals(SetupObject.SETUP_WORKSHOP)){
			setupString = "workshop";
		}else if(setupType.equals(SetupObject.SETUP_LOCATION)){
			setupString = "location";
		}else if(setupType.equals(SetupObject.SETUP_F_INACTIVE_REASON)){
			setupString = "inactiveReason";
		}
		
	    removeChildren();
	    setMethod("post");
	    Application application = Application.getInstance();
	    String msgNotEmpty  = Application.getInstance().getMessage("fms.tran.msg.mandatoryField", "Mandatory Field");
	    String initialSelect = "-1="+Application.getInstance().getMessage("fms.tran.msg.initialSelect", "--- NONE ---");
	    String msgIsNumberic = Application.getInstance().getMessage("fms.tran.msg.numbericField", "Numberic Field");

	    setColumns(2);
	    
	    addChild(new Label("NameLabel", Application.getInstance().getMessage("fms.tran.setup."+ setupString +"Name*", "Name*")));
	    name = new TextField("Name");
	    name.setSize("50");
	    name.setMaxlength("255");
	    //name.addChild(new ValidatorNotEmpty("NameNotEmpty", msgNotEmpty));
	    addChild(name);

	    addChild(new Label("DescriptionLabel", Application.getInstance().getMessage("fms.tran.setup."+ setupString +"Description", "Description")));
	    description = new TextBox("Description");
	    description.setCols("50");
	    description.setRows("4");
	    addChild(description);
	    
	    if((setupType.equals(SetupObject.SETUP_CATEGORY)) || (setupType.equals(SetupObject.SETUP_OUTSOURCE_PANEL))){
		    Panel pnType = new Panel("pnType");
			rdEngineering = new Radio("rdEngineering", Application.getInstance().getMessage("fms.tran.setup.engineering", "Engineering"), true);
			rdTransport = new Radio("rdTransport", Application.getInstance().getMessage("fms.tran.setup.transport", "Transport"));
			rdEngineering.setGroupName("typeGroup");
			rdTransport.setGroupName("typeGroup");
			pnType.addChild(rdEngineering);
			pnType.addChild(rdTransport);
			addChild(pnType);
	    }
	    
	    addChild(new Label("StatusLabel", Application.getInstance().getMessage("fms.tran.setup.status", "Status")));
	    isActive = new CheckBox("IsActive", Application.getInstance().getMessage("fms.tran.setup.active", "Active"));
	    isActive.setChecked(true);
	    addChild(isActive);
	    
	    buttonPanel = new Panel("buttonPanel");
	    if (SETUP_ACTION_ADD.equals(action)) {
	    	submit = new Button("submit", Application.getInstance().getMessage("fms.tran.submit", "Submit"));
		}
		if (SETUP_ACTION_EDIT.equals(action)) {
			submit = new Button("submit", Application.getInstance().getMessage("fms.tran.update", "Update"));
		}   
	    buttonPanel.addChild(submit);
	    cancel = new Button("cancel", Application.getInstance().getMessage("fms.tran.cancel", "Cancel"));
	    buttonPanel.addChild(cancel);
	    addChild(buttonPanel);
	    
	    namelbl = new Label("namelbl");
	    addChild(namelbl);
	}
	
	public void populateFields() {
	    TransportModule module = (TransportModule)Application.getInstance().getModule(TransportModule.class);
	    SetupObject o = new SetupObject();
	    try {
	    	o = module.getSetupObject(setupType, setupId);	    	
	    	name.setValue(String.valueOf(o.getName()));
	    	name.setHidden(true);
	    	namelbl.setText(String.valueOf(o.getName()));
	    	description.setValue(String.valueOf(o.getDescription()));
	    	if((setupType.equals(SetupObject.SETUP_CATEGORY)) || (setupType.equals(SetupObject.SETUP_OUTSOURCE_PANEL))){
		    	if (o.getType().equals("E")){
		    		rdEngineering.setChecked(true);
		    	} else {
		    		rdTransport.setChecked(true);
		    	}
	    	}
	    	isActive.setChecked("1".equals(o.getStatus()));
	    }catch (Exception e) {
	    	Log.getLog(getClass()).error(e.toString());
	    }
	}
	
	public Forward onSubmit(Event evt) {
	    String buttonName = findButtonClicked(evt);
	    kacang.ui.Forward result = super.onSubmit(evt);
	    if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
	    	setupId = null;
	    	init();
	    	return new Forward(Form.CANCEL_FORM_ACTION, cancelUrl, true);
	    }
	    else {return result;}
	}
	
	public Forward onValidate(Event event) {

		if (SETUP_ACTION_EDIT.equals(action) || SETUP_ACTION_ADD.equals(action)) {
			SetupObject o = new SetupObject();
			TransportModule module = (TransportModule)Application.getInstance().getModule(TransportModule.class);

			String tmpName = "";
			
			if (SETUP_ACTION_ADD.equals(action)) {
				tmpName = name.getValue().toString();
				
				if (tmpName == null || tmpName.equals("")) {
					name.setInvalid(true);
					return new Forward(SETUP_ADD_NAME_INVALID);
				} else if(checkExist(tmpName)){
					return new Forward(SETUP_ADD_EXIST);
				} 
			} else if (SETUP_ACTION_EDIT.equals(action)) {
				try {
					o = module.getSetupObject(setupType, setupId);
					tmpName = o.getName();
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString());
				}
			} 	
			
			o.setName(tmpName);
			o.setDescription(description.getValue().toString());
			
			if((setupType.equals(SetupObject.SETUP_CATEGORY)) || (setupType.equals(SetupObject.SETUP_OUTSOURCE_PANEL))){
				if(rdEngineering.isChecked()){
					o.setType("E");
				}else if(rdTransport.isChecked()){
					o.setType("T");
				}
			}
			o.setStatus(isActive.isChecked()? "1" : "0");
	
			if (SETUP_ACTION_ADD.equals(action)) {
				try {
					o.setSetup_id(UuidGenerator.getInstance().getUuid());
					o.setCreatedby(getWhoModifyId());
					o.setCreatedby_date(new Date());
					if((setupType.equals(SetupObject.SETUP_CATEGORY)) || (setupType.equals(SetupObject.SETUP_OUTSOURCE_PANEL))){
						module.insertCategoryObject(setupType, o);
					} else {
						module.insertSetupObject(setupType, o);
					}
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString()); 
					return new Forward(SETUP_ADD_FAIL);} 
			}
			if (SETUP_ACTION_EDIT.equals(action)) {
				try {
					o.setUpdatedby(getWhoModifyId());
					o.setUpdatedby_date(new Date());
					if((setupType.equals(SetupObject.SETUP_CATEGORY)) || (setupType.equals(SetupObject.SETUP_OUTSOURCE_PANEL))){
						module.updateCategoryObject(setupType, o);
					} else {
						module.updateSetupObject(setupType, o);
					}
					setupId = null;
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString()); 
					return new Forward(SETUP_ADD_FAIL);
				} 
			}
	    }
	    
		name.setValue("");
    	description.setValue("");
	    return new Forward(SETUP_ADD_SUCCESS);
	}
	
	public boolean checkExist(String tmpName){
		boolean exist = false;
		TransportDao dao = (TransportDao) Application.getInstance().getModule(TransportModule.class).getDao();
		try {
	    	if (dao.selectSetupObjectCount(setupType, tmpName) != 0){
	    		exist = true;
	    	}
	    } catch (DaoException e) {
	        Log.getLog(getClass()).error(e.toString());
	    }
		return exist;
	}
	
	public String getDefaultTemplate() {
		return "fms/transport/setupForm";
	}
	
}
