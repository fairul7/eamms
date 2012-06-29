package com.tms.fms.facility.ui;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.ValidatorSelectBoxNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.fms.facility.model.CategoryObject;
import com.tms.fms.facility.model.FacilityModule;
import com.tms.fms.facility.model.FacilityObject;
import com.tms.fms.facility.ui.validator.ValidatorTextFieldWithRadioButtonNotNull;
import com.tms.fms.transport.model.SetupObject;
import com.tms.fms.transport.model.TransportModule;

public class FacilityForm extends Form{

	public static final String FORWARD_ADD_SUCCESS = "form.add.success";
	public static final String FORWARD_ADD_FAIL = "form.add.fail";
	public static final String FORWARD_EDIT_SUCCESS = "form.edit.success";
	public static final String FORWARD_EDIT_FAIL = "form.edit.fail";
	
	public static final String FORM_ACTION_ADD = "form.action.add";
	public static final String FORM_ACTION_EDIT = "form.action.edit";
	public static final String FORM_ACTION_VIEW = "form.action.view";
	
	private Label lbCategory;
	private Label lbName;
	private Label lbDescription;
	private Label lbChannel;
	private Label lbMakeType;
	private Label lbModelName;
	private Label lbQuantity;
	private Label lbPM;
	private Label lbPool;
	private Panel pnChildAll;
	
	
	private SelectBox sbCategory;
	private TextField tfName;
	private TextBox tbDescription;
	private SelectBox sbChannel;
	private TextField tfMakeType;
	private TextField tfModelName;
	private Radio rdPMYes;
	private Radio rdPMNo;
	private Radio rdPMMonth;
	private Radio rdPMYear;
	private TextField tfPMMonth;
	private TextField tfPMYear;
	private Radio rdPoolYes;
	private Radio rdPoolNo;
	private Radio rdPoolStudio;
	private Radio rdChildYes;
	private Radio rdChildNo;
	private FacilityPopupSelectBox ssbChild;
	private Button btnSubmit;
	private Button btnEdit;
	private Button btnCancel;
	
	private String id;
	private String action;
	private String cancelUrl = "";
	private String editUrl = "";
	private String whoModifyId = "";
	

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getCancelUrl() {
		return cancelUrl;
	}
	
	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}
	
	public String getEditUrl() {
		return editUrl;
	}

	public void setEditUrl(String editUrl) {
		this.editUrl = editUrl;
	}
	
	public String getWhoModifyId() {
		return whoModifyId;
	}
	
	public void setWhoModifyId(String whoModifyId) {
		this.whoModifyId = whoModifyId;
	}
	
	public void onRequest(Event event) {
		initForm();
	    if (FORM_ACTION_EDIT.equals(action) || FORM_ACTION_VIEW.equals(action)) {populateFields();}
    }
	
	public void initForm() {
		
	    Application application = Application.getInstance();
	    String msgNotEmpty  = Application.getInstance().getMessage("fms.tran.msg.mandatoryField", "Mandatory Field");
	    String initialSelect = "-1="+Application.getInstance().getMessage("fms.tran.msg.initialSelect", "--- NONE ---");
	    String msgIsNumberic = Application.getInstance().getMessage("fms.tran.msg.numbericField", "Numberic Field");

	    removeChildren();
	    setMethod("post");
	    setColumns(2);
	    
	    addChild(new Label("lb1", Application.getInstance().getMessage("fms.facility.form.category", "Category")));
	    if (FORM_ACTION_ADD.equals(action) || FORM_ACTION_EDIT.equals(action)) {
		    sbCategory = new SelectBox("sbCategory");
		    sbCategory.setOptions(initialSelect);
		    try {
				FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
				Collection lstCategory = mod.selectCategoryWithParent("","","","", false,"","",false,0,-1);
			    if (lstCategory.size() > 0) {
			    	for (Iterator i=lstCategory.iterator(); i.hasNext();) {
			        	CategoryObject o = (CategoryObject)i.next();
			        	sbCategory.setOptions(o.getId()+"="+o.getParent_cat_name()+"-"+o.getName());
			        }
			    }
			}catch (Exception e) {
			    Log.getLog(getClass()).error(e.toString());
			}
			sbCategory.addChild(new ValidatorSelectBoxNotEmpty("sbCategoryNotEmpty", msgNotEmpty));
		    addChild(sbCategory);
	    }
	    if (FORM_ACTION_VIEW.equals(action)){
	    	lbCategory = new Label("lbCategory");
	    	addChild(lbCategory);
	    }
	    
	    addChild(new Label("lb2", Application.getInstance().getMessage("fms.facility.form.itemName*", "Item Name*")));
	    if (FORM_ACTION_ADD.equals(action) || FORM_ACTION_EDIT.equals(action)) {
		    tfName = new TextField("tfName");
		    tfName.setSize("50");
		    tfName.setMaxlength("255");
		    tfName.addChild(new ValidatorNotEmpty("tfNameNotEmpty", msgNotEmpty));
		    addChild(tfName);
	    }
	    if (FORM_ACTION_VIEW.equals(action)){
	    	lbName = new Label("lbName");
	    	addChild(lbName);
	    }
	    
	    addChild(new Label("lb3", Application.getInstance().getMessage("fms.facility.form.itemDescription", "Item Description")));
	    if (FORM_ACTION_ADD.equals(action) || FORM_ACTION_EDIT.equals(action)) {
		    tbDescription = new TextBox("tbDescription");
		    tbDescription.setCols("50");
		    tbDescription.setRows("4");
		    addChild(tbDescription);
	    }
	    if (FORM_ACTION_VIEW.equals(action)){
	    	lbDescription = new Label("lbDescription");
	    	addChild(lbDescription);
	    }
	    
	    addChild(new Label("lb4", Application.getInstance().getMessage("fms.facility.form.channel", "Channel")));
	    if (FORM_ACTION_ADD.equals(action) || FORM_ACTION_EDIT.equals(action)) {
		    sbChannel = new SelectBox("sbChannel");
		    sbChannel.setOptions(initialSelect);
		    try {
				TransportModule mod = (TransportModule)Application.getInstance().getModule(TransportModule.class);
				Collection lstChannel = mod.selectSetupObject(SetupObject.SETUP_CHANNEL, "", "1", "name", false, 0, -1);
			    if (lstChannel.size() > 0) {
			    	for (Iterator i=lstChannel.iterator(); i.hasNext();) {
			        	SetupObject o = (SetupObject)i.next();
			        	sbChannel.setOptions(o.getSetup_id()+"="+o.getName());
			        }
			    }
			}catch (Exception e) {
			    Log.getLog(getClass()).error(e.toString());
			}
			sbChannel.addChild(new ValidatorSelectBoxNotEmpty("sbChannelNotEmpty", msgNotEmpty));
		    addChild(sbChannel);
	    }
	    if (FORM_ACTION_VIEW.equals(action)){
	    	lbChannel = new Label("lbChannel");
	    	addChild(lbChannel);
	    }
	    
	    addChild(new Label("lb5", Application.getInstance().getMessage("fms.facility.form.makeType", "Make Type")));
	    if (FORM_ACTION_ADD.equals(action) || FORM_ACTION_EDIT.equals(action)) {
		    tfMakeType = new TextField("tfMakeType");
		    tfMakeType.setSize("50");
		    tfMakeType.setMaxlength("255");
		    addChild(tfMakeType);
	    }
	    if (FORM_ACTION_VIEW.equals(action)){
	    	lbMakeType = new Label("lbMakeType");
	    	addChild(lbMakeType);
	    }
	    
	    addChild(new Label("lb6", Application.getInstance().getMessage("fms.facility.form.modelName", "Model Name")));
	    if (FORM_ACTION_ADD.equals(action) || FORM_ACTION_EDIT.equals(action)) {
		    tfModelName = new TextField("tfModelName");
		    tfModelName.setSize("50");
		    tfModelName.setMaxlength("255");
		    addChild(tfModelName);
	    }
	    if (FORM_ACTION_VIEW.equals(action)){
	    	lbModelName = new Label("lbModelName");
	    	addChild(lbModelName);
	    	
	    	addChild(new Label("lb16", Application.getInstance().getMessage("fms.facility.form.quantity", "Quantity")));
	    	lbQuantity = new Label("lbQuantity");
	    	addChild(lbQuantity);
	    }
	    
	    addChild(new Label("lb7", Application.getInstance().getMessage("fms.facility.form.preventiveMaintenance", "Preventive Maintenance")));
	    if (FORM_ACTION_ADD.equals(action) || FORM_ACTION_EDIT.equals(action)) {
		    Panel pnPM = new Panel("pnPM");
		    rdPMYes = new Radio("rdPMYes", Application.getInstance().getMessage("fms.facility.form.yes", "Yes"));
		    rdPMYes.setGroupName("rdPMGroup1");
		    rdPMYes.setOnClick("hideShowPM()");
		    pnPM.addChild(rdPMYes);
		    rdPMNo = new Radio("rdPMNo", Application.getInstance().getMessage("fms.facility.form.no", "No"), true);
		    rdPMNo.setGroupName("rdPMGroup1");
		    rdPMNo.setOnClick("hideShowPM()");
		    pnPM.addChild(rdPMNo);
		    addChild(pnPM);
		    
		    addChild(new Label("lb8", ""));
		    Panel pnPM1 = new Panel("pnPM1");
		    pnPM1.setColumns(2);
		    rdPMMonth = new Radio("rdPMMonth", Application.getInstance().getMessage("fms.facility.form.byMonth", "By Month"));
		    rdPMMonth.setGroupName("rdPMGroup2");
		    rdPMMonth.setOnClick("hideShowPMChild()");
		    pnPM1.addChild(rdPMMonth);
		    Panel pnPM2 = new Panel("pnPM2");
		    pnPM2.addChild(new Label("lb9", Application.getInstance().getMessage("fms.facility.form.every", "Every")));
		    tfPMMonth = new TextField("tfPMMonth");
		    tfPMMonth.setValue("0");
		    tfPMMonth.setSize("20");
		    tfPMMonth.setMaxlength("255");
		    tfPMMonth.addChild(new ValidatorTextFieldWithRadioButtonNotNull("tfPMMonthNotEmpty", rdPMMonth, msgNotEmpty));
		    tfPMMonth.addChild(new ValidatorIsNumeric("tfPMMonthIsNumberic", msgIsNumberic, true));
		    pnPM2.addChild(tfPMMonth);
		    pnPM2.addChild(new Label("lb10", Application.getInstance().getMessage("fms.facility.form.months", "Months")));
		    pnPM1.addChild(pnPM2);
		    rdPMYear = new Radio("rdPMYear", Application.getInstance().getMessage("fms.facility.form.byYear", "By Year"));
		    rdPMYear.setGroupName("rdPMGroup2");
		    rdPMYear.setOnClick("hideShowPMChild()");
		    pnPM1.addChild(rdPMYear);
		    Panel pnPM3 = new Panel("pnPM3");
		    pnPM3.addChild(new Label("lb11", Application.getInstance().getMessage("fms.facility.form.every", "Every")));
		    tfPMYear = new TextField("tfPMYear");
		    tfPMYear.setValue("0");
		    tfPMYear.setSize("20");
		    tfPMYear.setMaxlength("255");
		    tfPMYear.addChild(new ValidatorTextFieldWithRadioButtonNotNull("tfPMYearNotEmpty", rdPMYear, msgNotEmpty));
		    tfPMYear.addChild(new ValidatorIsNumeric("tfPMYearIsNumberic", msgIsNumberic, true));
		    pnPM3.addChild(tfPMYear);
		    pnPM3.addChild(new Label("lb12", Application.getInstance().getMessage("fms.facility.form.years", "Years")));
		    pnPM1.addChild(pnPM3);
		    addChild(pnPM1);
	    }
	    if (FORM_ACTION_VIEW.equals(action)){
	    	lbPM = new Label("lbPM");
	    	addChild(lbPM);
	    }
	    
	    addChild(new Label("lb13", Application.getInstance().getMessage("fms.facility.form.poolableItem", "Usage")));
	    if (FORM_ACTION_ADD.equals(action) || FORM_ACTION_EDIT.equals(action)) {
		    Panel pnPool = new Panel("pnPool");
		    rdPoolYes = new Radio("rdPoolYes", Application.getInstance().getMessage("fms.facility.form.poolable", "Poolable"), true);
		    rdPoolYes.setGroupName("rdPoolGroup");
		    pnPool.addChild(rdPoolYes);
		    rdPoolNo = new Radio("rdPoolNo", Application.getInstance().getMessage("fms.facility.form.nonPoolale", "Non-poolable"));
		    rdPoolNo.setGroupName("rdPoolGroup");
		    pnPool.addChild(rdPoolNo);
		    rdPoolStudio = new Radio("rdPoolStudio", Application.getInstance().getMessage("fms.facility.form.studioMode", "Studio Mode"));
		    rdPoolStudio.setGroupName("rdPoolGroup");
		    pnPool.addChild(rdPoolStudio);
		    addChild(pnPool);
	    }
	    if (FORM_ACTION_VIEW.equals(action)){
	    	lbPool = new Label("lbPool");
	    	addChild(lbPool);
	    	
	    }
	    
	    addChild(new Label("lb14", Application.getInstance().getMessage("fms.facility.form.relatedChildItem", "Related / Child Item(s)")));
	    if (FORM_ACTION_ADD.equals(action) || FORM_ACTION_EDIT.equals(action)) {
		    Panel pnChild = new Panel("pnChild");
		    rdChildYes = new Radio("rdChildYes", Application.getInstance().getMessage("fms.facility.form.yes", "Yes"));
		    rdChildYes.setGroupName("rdChildGroup");
		    rdChildYes.setOnClick("hideShowChild()");
		    pnChild.addChild(rdChildYes);
		    rdChildNo = new Radio("rdChildNo", Application.getInstance().getMessage("fms.facility.form.no", "No"), true);
		    rdChildNo.setGroupName("rdChildGroup");
		    rdChildNo.setOnClick("hideShowChild()");
		    pnChild.addChild(rdChildNo);
		    addChild(pnChild);
		    addChild(new Label("lb15", ""));
		    Panel pnChild2 = new Panel("pnChild2");
		    pnChild2.setColumns(2);
		    addChild(new Label("lb14", Application.getInstance().getMessage("fms.facility.form.selectRelatedChildItem", "Select Related / Child Item(s)")));
		    ssbChild = new FacilityPopupSelectBox("ssbChild");
		    ssbChild.setSortable(false);
		    addChild(ssbChild);
		    ssbChild.init();
		    addChild(pnChild2);
	    }
	    if (FORM_ACTION_VIEW.equals(action)){
	    	pnChildAll = new Panel("pnChildAll");
	    	addChild(pnChildAll);
	    }
	    
		addChild(new Label("lbbutton", ""));
		Panel pnButton = new Panel("pnButton");
	    if (FORM_ACTION_ADD.equals(action)) {
	    	btnSubmit = new Button("btnSubmit", Application.getInstance().getMessage("fms.facility.submit", "Submit"));
	    	pnButton.addChild(btnSubmit);
	    }
		if (FORM_ACTION_EDIT.equals(action)) {
			btnSubmit = new Button("btnSubmit", Application.getInstance().getMessage("fms.facility.update", "Update"));
			pnButton.addChild(btnSubmit);
		}  
		if (FORM_ACTION_VIEW.equals(action)) {
			btnEdit = new Button("btnEdit", Application.getInstance().getMessage("fms.facility.edit", "Edit"));
	    	pnButton.addChild(btnEdit);
			btnCancel = new Button("btnCancel", Application.getInstance().getMessage("fms.facility.backToListing", "Back To Listing"));
	    	pnButton.addChild(btnCancel);
		}else{
			btnCancel = new Button("btnCancel", Application.getInstance().getMessage("fms.facility.cancel", "Cancel"));
	    	pnButton.addChild(btnCancel);
		}
	    addChild(pnButton);  
	}
	
	public void populateFields() {
		if (FORM_ACTION_EDIT.equals(action)){
			FacilityObject o = new FacilityObject();
			FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
			try {
				o = mod.getFacility(id);
			}catch (Exception e) {
				Log.getLog(getClass()).error(e.toString());
			}
			
			sbCategory.setSelectedOption(o.getCategory_id());
			tfName.setValue(o.getName());
			tbDescription.setValue(o.getDescription());
			sbChannel.setSelectedOption(o.getChannel_id());
			tfMakeType.setValue(o.getMaketype());
			tfModelName.setValue(o.getModel_name());
			rdPMYes.setChecked("Y".equals(o.getIs_pm()));
			rdPMNo.setChecked("N".equals(o.getIs_pm()));
			if("Y".equals(o.getIs_pm())){
				rdPMMonth.setChecked("M".equals(o.getPm_type()));
				rdPMYear.setChecked("Y".equals(o.getPm_type()));
				tfPMMonth.setValue(o.getPm_month());
				tfPMYear.setValue(o.getPm_year());
			}else{
				rdPMMonth.setChecked(false);
				rdPMYear.setChecked(false);
				tfPMMonth.setValue("");
				tfPMYear.setValue("");
			}
			rdPoolYes.setChecked("P".equals(o.getIs_pool()));
			rdPoolNo.setChecked("N".equals(o.getIs_pool()));
			rdPoolStudio.setChecked("S".equals(o.getIs_pool()));
			rdChildYes.setChecked("Y".equals(o.getHave_child()));
			rdChildNo.setChecked("N".equals(o.getHave_child()));
			
			Map optionMap = new SequencedHashMap();
			try{
				Collection lstOption = mod.selectRelatedItem(id);
			    if (lstOption.size() > 0) {
			    	for (Iterator i=lstOption.iterator(); i.hasNext();) {
			        	FacilityObject f = (FacilityObject)i.next();
			        	optionMap.put(f.getId(),f.getName());
			        }
			    }
	        }catch(Exception e){
	        	 Log.getLog(getClass()).error("Error retrieving item", e);
	        }
			ssbChild.setOptionMap(optionMap);
		}
		if(FORM_ACTION_VIEW.equals(action)){
			FacilityObject o = new FacilityObject();
			FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
			try {
				o = mod.getFacility(id);
			}catch (Exception e) {
				Log.getLog(getClass()).error(e.toString());
			}
			
			lbCategory.setText(o.getCategory_name());
			lbName.setText(o.getName());
			lbDescription.setText(o.getDescription());
			lbChannel.setText(o.getChannel_name());
			lbMakeType.setText(o.getMaketype());
			lbModelName.setText(o.getModel_name());
			lbQuantity.setText(Integer.toString(o.getQuantity()));
			String stringPM = " - ";
			if("Y".equals(o.getIs_pm())){
				if("M".equals(o.getPm_type()) && (!"".equals(o.getPm_month()) || !"null".equals(o.getPm_month()) || o.getPm_month() != null)){
					stringPM = "Maintenance every " + o.getPm_month() + " month(s)";
				}
				if("Y".equals(o.getPm_type())&& (!"".equals(o.getPm_year()) || !"null".equals(o.getPm_year()) || o.getPm_year() != null)){
					stringPM = "Maintenance every " + o.getPm_year() + " year(s)";
				}
			}
			lbPM.setText(stringPM);
			if("P".equals(o.getIs_pool())){
				lbPool.setText(Application.getInstance().getMessage("fms.facility.form.poolable"));
			}else if("N".equals(o.getIs_pool())){
				lbPool.setText(Application.getInstance().getMessage("fms.facility.form.nonPoolable"));
			}else if("S".equals(o.getIs_pool())){
				lbPool.setText(Application.getInstance().getMessage("fms.facility.form.studioMode"));
			}
			if("Y".equals(o.getHave_child())){
				try {
					Collection lstChild = mod.selectRelatedItem(id);
				    if (lstChild.size() > 0) {
				    	pnChildAll.setColumns(1);
				    	int k=0;
				    	for (Iterator i=lstChild.iterator(); i.hasNext();) {
				        	FacilityObject j = (FacilityObject)i.next();
				        	pnChildAll.addChild(new Label("child"+k, j.getName()));
				        	k++;
				        }
				    }else{
				    	pnChildAll.addChild(new Label("noChild", "No related items."));
				    }
				}catch (Exception e) {
				    Log.getLog(getClass()).error(e.toString());
				}
			}else{
				pnChildAll.addChild(new Label("noChild", "No related items."));
			}
			
		}
	}
	
	public Forward onSubmit(Event evt) {
	    String buttonName = findButtonClicked(evt);
	    kacang.ui.Forward result = super.onSubmit(evt);
	    
	    if (buttonName != null && btnCancel.getAbsoluteName().equals(buttonName)) {
	    	init();
	    	return new Forward(Form.CANCEL_FORM_ACTION, cancelUrl, true);
	    } else if(FORM_ACTION_VIEW.equals(action)){
	    	if (buttonName != null && btnEdit.getAbsoluteName().equals(buttonName)) {
	    		init();
	    		return new Forward(Form.CANCEL_FORM_ACTION, editUrl, true);
	    	}else{
	    		return super.onSubmit(evt);
	    	}
    	} else if (FORM_ACTION_EDIT.equals(action) && (buttonName != null && btnSubmit.getAbsoluteName().equals(buttonName))) {
    		return onValidate(evt);
		} else{
	    	return super.onSubmit(evt);
	    }
	}
	
	public Forward onValidate(Event event) {

		if (FORM_ACTION_EDIT.equals(action) || FORM_ACTION_ADD.equals(action)) {
			FacilityObject o = new FacilityObject();
			FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
			
			if (FORM_ACTION_EDIT.equals(action)) {
				try {
					o = mod.getFacility(id);
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString());
				}
			}
			
			o.setName(tfName.getValue().toString());
			o.setDescription(tbDescription.getValue().toString());
			o.setCategory_id(getSelectBoxValue(sbCategory));
			o.setChannel_id(getSelectBoxValue(sbChannel));
			o.setMaketype(tfMakeType.getValue().toString());
			o.setModel_name(tfModelName.getValue().toString());
			o.setIs_pm((rdPMYes.isChecked())? "Y": "N");
			if(rdPMYes.isChecked()){
				o.setPm_type((rdPMMonth.isChecked())? "M": "Y");
				o.setPm_month(tfPMMonth.getValue().toString());
				o.setPm_year(tfPMYear.getValue().toString());
			}else{
				o.setPm_type("N");
				o.setPm_month("");
				o.setPm_year("");
			}
			if(rdPoolYes.isChecked()){
				o.setIs_pool("P");
			}else if(rdPoolNo.isChecked()){
				o.setIs_pool("N");
			}else if(rdPoolStudio.isChecked()){
				o.setIs_pool("S");
			}
			o.setHave_child((rdChildYes.isChecked())? "Y":"N");
			
			if (FORM_ACTION_ADD.equals(action)) {
				try {
					o.setCreatedby(getWhoModifyId());
					o.setCreatedby_date(new Date());
					o.setId(UuidGenerator.getInstance().getUuid());
					o.setQuantity(0);
					o.setStatus("1");
					mod.insertFacility(o);
					
					mod.insertRelatedItem(o.getId(), ssbChild.getIds());
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString()); 
					return new Forward(FORWARD_ADD_FAIL);} 
			}
			if (FORM_ACTION_EDIT.equals(action)) {
				try {
					o.setUpdatedby(getWhoModifyId());
					o.setUpdatedby_date(new Date());
					
					mod.updateFacility(o);
					mod.insertRelatedItem(id, ssbChild.getIds());
					return new Forward(FORWARD_EDIT_SUCCESS);
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString()); 
					return new Forward(FORWARD_EDIT_FAIL);
				} 
			}
		}
		return new Forward(FORWARD_ADD_SUCCESS);
	}	
	
	public String getDefaultTemplate() {
		return "fms/facility/facilityForm";
	}
	
	private String getSelectBoxValue(SelectBox sb) {
	    if (sb != null) {
	    	Map selected = sb.getSelectedOptions();
	    	if (selected.size() == 1) {
	    		return (String)selected.keySet().iterator().next();
	    	}
	    }
	    return null;
	}
}
