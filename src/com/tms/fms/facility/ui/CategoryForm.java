package com.tms.fms.facility.ui;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import com.tms.fms.facility.model.*;
import com.tms.fms.department.model.*;
import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.*;
import kacang.stdui.validator.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.*;

public class CategoryForm extends Form{
	public static final String FORWARD_ADD_SUCCESS = "form.add.success";
	public static final String FORWARD_ADD_FAIL = "form.add.fail";
	public static final String FORWARD_ADD_EXIST = "form.add.exist";
	public static final String FORWARD_EDIT_SUCCESS = "form.edit.success";
	public static final String FORWARD_EDIT_FAIL = "form.edit.fail";
	
	public static final String FORM_ACTION_ADD = "form.action.add";
	public static final String FORM_ACTION_EDIT = "form.action.edit";
	
	private TextField tfName;
	private Hidden hdName;
	private TextBox tbDescription;
	private SelectBox sbDepartment;
	private SelectBox sbUnit;
	private Radio rdYes;
	private Radio rdNo;
	private SelectBox sbCategory;
	private Radio rdActive;
	private Radio rdInactive;
	private Button btnSubmit;
	private Button btnCancel;
	
	private String id;
	private String action;
	private String cancelUrl = "";
	private String whoModifyId = "";
	private String isParent="false";
	
	private Label tfNamelbl;
	
	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

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
	
	public String getWhoModifyId() {
		return whoModifyId;
	}
	
	public void setWhoModifyId(String whoModifyId) {
		this.whoModifyId = whoModifyId;
	}
	
	public void onRequest(Event event) {
		initForm();
	    if (FORM_ACTION_EDIT.equals(action)) {populateFields();}
    }
	
	public void initForm() {
		
	    Application application = Application.getInstance();
	    String msgNotEmpty  = Application.getInstance().getMessage("fms.tran.msg.mandatoryField", "Mandatory Field");
	    String initialSelect = "-1="+Application.getInstance().getMessage("fms.tran.msg.initialSelect", "--- NONE ---");
	    String msgIsNumberic = Application.getInstance().getMessage("fms.tran.msg.numbericField", "Numberic Field");

	    removeChildren();
	    setMethod("post");
	    setColumns(2);
	    
	    addChild(new Label("lb1", Application.getInstance().getMessage("fms.facility.form.categoryName*", "Category Name*")));
	    tfName = new TextField("tfName");
	    tfName.setSize("50");
	    tfName.setMaxlength("255");
	    if (FORM_ACTION_ADD.equals(action)) {
	    	tfName.addChild(new ValidatorNotEmpty("tfNameNotEmpty", msgNotEmpty));
	    }
	    addChild(tfName);
	    
	    hdName = new Hidden("hdName");
	    addChild(hdName);
		
	    addChild(new Label("lb2", Application.getInstance().getMessage("fms.facility.form.categoryDescription", "Category Description")));
	    tbDescription = new TextBox("tbDescription");
	    tbDescription.setCols("50");
	    tbDescription.setRows("4");
	    addChild(tbDescription);
	    
	    addChild(new Label("lb3", Application.getInstance().getMessage("fms.facility.form.department", "Department")));
	    sbDepartment = new SelectBox("sbDepartment");
	    sbDepartment.setOptions(initialSelect);
	    try {
			FMSDepartmentDao dao = (FMSDepartmentDao)Application.getInstance().getModule(FMSDepartmentManager.class).getDao();
			Collection lstDepartment = dao.selectDepartment();
		    if (lstDepartment.size() > 0) {
		    	for (Iterator i=lstDepartment.iterator(); i.hasNext();) {
		        	FMSDepartment o = (FMSDepartment)i.next();
		        	sbDepartment.setOptions(o.getId()+"="+o.getName());
		        }
		    }
		}catch (Exception e) {
		    Log.getLog(getClass()).error(e.toString());
		}
		sbDepartment.addChild(new ValidatorSelectBoxNotEmpty("sbDepartmentNotEmpty", msgNotEmpty));
		sbDepartment.setOnChange("setDepartmentChange()");
	    addChild(sbDepartment);
	    
	    addChild(new Label("lb4", Application.getInstance().getMessage("fms.facility.form.unit", "Unit")));
	    sbUnit = new SelectBox("sbUnit");
	    sbUnit.setOptions(initialSelect);
	    sbUnit.addChild(new ValidatorSelectBoxNotEmpty("sbUnitNotEmpty", msgNotEmpty));
	    addChild(sbUnit);
	    
	    addChild(new Label("lb5", Application.getInstance().getMessage("fms.facility.form.parentCategory", "Parent Category")));
	    Panel pnParent = new Panel("pnParent");
		rdYes = new Radio("rdYes", Application.getInstance().getMessage("fms.facility.form.yes", "Yes"));
		rdNo = new Radio("rdNo", Application.getInstance().getMessage("fms.facility.form.no", "No"), true);
		rdYes.setGroupName("parentGroup");
		rdYes.setOnClick("hideShowCategory()");
		rdNo.setGroupName("parentGroup");
		rdNo.setOnClick("hideShowCategory()");
		pnParent.addChild(rdYes);
		pnParent.addChild(rdNo);
		addChild(pnParent);
	    
	    addChild(new Label("lb6", Application.getInstance().getMessage("fms.facility.form.selectParentCategory", "Select Parent Category")));
	    sbCategory = new SelectBox("sbCategory");
	    sbCategory.setOptions(initialSelect);
	    try {
			FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
			Collection lstCategory = mod.selectCategory("","","","",true,"1","",false,0,-1);
		    if (lstCategory.size() > 0) {
		    	for (Iterator i=lstCategory.iterator(); i.hasNext();) {
		        	CategoryObject o = (CategoryObject)i.next();
		        	sbCategory.setOptions(o.getId()+"="+o.getName());
		        }
		    }
		}catch (Exception e) {
		    Log.getLog(getClass()).error(e.toString());
		}
		sbCategory.addChild(new ValidatorParentSelected("sbCategoryNotEmpty", rdNo, msgNotEmpty));
	    addChild(sbCategory);
	    
	    addChild(new Label("lb5", Application.getInstance().getMessage("fms.facility.form.status", "Status")));
	    Panel pnStatus = new Panel("pnStatus");
		rdActive = new Radio("rdActive", Application.getInstance().getMessage("fms.facility.form.active", "Active"), true);
		rdInactive = new Radio("rdInactive", Application.getInstance().getMessage("fms.facility.form.inactive", "Inactive"));
		rdActive.setGroupName("statusGroup");
		rdInactive.setGroupName("statusGroup");
		pnStatus.addChild(rdActive);
		pnStatus.addChild(rdInactive);
		addChild(pnStatus);
	    
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
		btnCancel = new Button("btnCancel", Application.getInstance().getMessage("fms.facility.cancel", "Cancel"));
	    pnButton.addChild(btnCancel);
	    addChild(pnButton);  
	    
	    tfNamelbl = new Label("tfNamelbl");
	    addChild(tfNamelbl);
	}
	
	public void populateFields() {
		
		if (FORM_ACTION_EDIT.equals(action)){
			CategoryObject o = new CategoryObject();
			FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
			try {
				o = mod.getCategory(id);
			}catch (Exception e) {
				Log.getLog(getClass()).error(e.toString());
			}
			tfName.setValue(o.getName().toString());
			tfName.setHidden(true);			
			hdName.setValue(o.getName().toString());
			tfNamelbl.setText(o.getName().toString());
			tbDescription.setValue(o.getDescription().toString());
			sbDepartment.setSelectedOption(o.getDepartment_id());
			try {
				FMSDepartmentDao dao = (FMSDepartmentDao)Application.getInstance().getModule(FMSDepartmentManager.class).getDao();
				Collection lstUnit = dao.selectUnitBaseOnDepartment(o.getDepartment_id());
			    if (lstUnit.size() > 0) {
			    	for (Iterator i=lstUnit.iterator(); i.hasNext();) {
			        	FMSUnit d = (FMSUnit)i.next();
			        	sbUnit.setOptions(d.getId()+"="+d.getName());
			        }
			    }
			}catch (Exception e) {
			    Log.getLog(getClass()).error(e.toString());
			}
			sbUnit.setSelectedOption(o.getUnit_id());
			rdYes.setChecked("Y".equals(o.getParent_cat()));
			isParent = ("Y".equals(o.getParent_cat())?"true":"false");
			rdNo.setChecked("N".equals(o.getParent_cat()));
			sbCategory.setSelectedOption((!"".equals(o.getParent_cat_id()) && !"null".equals(o.getParent_cat_id()) && o.getParent_cat_id() != null)? o.getParent_cat_id(): "-1");
			rdActive.setChecked("1".equals(o.getStatus()));
			rdInactive.setChecked("0".equals(o.getStatus()));
			
		}
	}
	
	public Forward onSubmit(Event evt) {
	    String buttonName = findButtonClicked(evt);
	    kacang.ui.Forward result = super.onSubmit(evt);
	    if (buttonName != null && btnCancel.getAbsoluteName().equals(buttonName)) {
	    	init();
	    	return new Forward(Form.CANCEL_FORM_ACTION, cancelUrl, true);
	    } else {
	    	return result;
	    }
	}
	
	public Forward onValidate(Event event) {

		if (FORM_ACTION_EDIT.equals(action) || FORM_ACTION_ADD.equals(action)) {
			CategoryObject o = new CategoryObject();
			FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
			
			if (FORM_ACTION_EDIT.equals(action)) {
				try {
					o = mod.getCategory(id);
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString());
				}
			}
			
			if (FORM_ACTION_ADD.equals(action)){
				String tmpName = tfName.getValue().toString();
				if(checkExist(tmpName) && !tmpName.equals(o.getName())){
					return new Forward(FORWARD_ADD_EXIST);
				}
			}
			
			if (FORM_ACTION_ADD.equals(action)){
				o.setName(tfName.getValue().toString());
			} else if (FORM_ACTION_EDIT.equals(action)){
				o.setName(hdName.getValue().toString());
			}
			o.setDescription(tbDescription.getValue().toString());
			o.setDepartment_id(getSelectBoxValue(sbDepartment));
			o.setUnit_id(getSelectBoxValue(sbUnit));
			o.setParent_cat_id(getSelectBoxValue(sbCategory));
			if(rdYes.isChecked()){
				o.setParent_cat("Y");
			}else if(rdNo.isChecked()){
				o.setParent_cat("N");
			}
			
			if (FORM_ACTION_ADD.equals(action)) {
				try {
					o.setCreatedby(getWhoModifyId());
					o.setCreatedby_date(new Date());
					o.setId(UuidGenerator.getInstance().getUuid());
					o.setStatus("1");
					mod.insertCategory(o);
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.toString()); 
					return new Forward(FORWARD_ADD_FAIL);} 
			}
			if (FORM_ACTION_EDIT.equals(action)) {
				try {
					o.setUpdatedby(getWhoModifyId());
					o.setUpdatedby_date(new Date());
					
					mod.updateCategory(o);
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
		return "fms/facility/categoryForm";
	}
	
	public boolean checkExist(String tmpName){
		boolean exist = false;
		FacilityDao dao = (FacilityDao) Application.getInstance().getModule(FacilityModule.class).getDao();
		try {
	    	if (dao.selectCategoryCount(tmpName) != 0){
	    		exist = true;
	    	}
	    } catch (DaoException e) {
	        Log.getLog(getClass()).error(e.toString());
	    }
		return exist;
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
	
	public class ValidatorParentSelected extends Validator {
		private Radio rdButton;
		
        public ValidatorParentSelected() {
        }

        public ValidatorParentSelected(String name) {
            super(name);
        }
        
        public ValidatorParentSelected(String name, Radio rdButton, String text){
        	super(name);
        	this.rdButton = rdButton;
        	setText(text);
        }

        public boolean validate(FormField formField) {
        	String value = getSelectBoxValue((SelectBox)formField);
            if(rdButton.isChecked() && ("-1".equals(value) || value == null)){
            	return false;
            }else{
            	return true;
            }
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
}
