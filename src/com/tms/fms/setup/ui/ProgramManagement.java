package com.tms.fms.setup.ui;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Hidden;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.ValidatorSelectBoxNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.fms.setup.model.ProgramObject;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.util.DecimalUtil;

public class ProgramManagement extends Form{
	
	private TextField name;
	private TextBox description;
	private TextField producer;
	private TextField pfeCode;
	private Hidden pfeCodeHidden;
	private DatePopupField startProductionDate;
	private DatePopupField endProductionDate;
	private SelectBox department;
	private TextField engManpowerBudget;
	private TextField facilitiesBudget;
	private TextField vtrBudget;
	private TextField transportBudget;
	private Radio active;
	private Radio inactive;
	private Button btnsave;
	private Button btncancel;
	
	public String action;
	public String programId;
	
	private String cancelUrl = "";
	private String whoModifyId = "";
	
	public String getCancelUrl(){
		return cancelUrl;
	}
	
	public void setCancelUrl(String cancelUrl){
		this.cancelUrl=cancelUrl;
	}
	
	public String getWhoModifyId(){
		return whoModifyId;
	}
	
	public void setWhoModifyId(String whoModifyId){
		this.whoModifyId=whoModifyId;
	}
	
	public ProgramManagement(){
		super();
	}
	
	public void init(){
		initForm();
	}
	
	public void initForm(){
		Application application = Application.getInstance();
	    String msgNotEmpty  = "Mandatory Field";
	    
		removeChildren();
		setMethod("POST");
		setColumns(2);
		
		name=new TextField("name");
		name.setSize("50");
		name.setMaxlength("255");
		name.addChild(new ValidatorNotEmpty("NameNotEmpty",msgNotEmpty));
		description=new TextBox("description");
		description.setCols("50");
		description.setRows("4");
		
		producer = new TextField("producer");
		producer.setSize("25");
		producer.setMaxlength("255");
		
		pfeCode=new TextField("pfeCode");
		pfeCode.addChild(new ValidatorNotEmpty("CodeNotEmpty", msgNotEmpty));
		pfeCode.setSize("25");
		pfeCode.setMaxlength("255");
		
		pfeCodeHidden = new Hidden("pfeCodeHidden");
		addChild(pfeCodeHidden);
		
		startProductionDate= new DatePopupField("startProductionDate");
		startProductionDate.setFormat("dd-MM-yyyy");
		endProductionDate= new DatePopupField("endProductionDate");
		endProductionDate.setFormat("dd-MM-yyyy");
		department= new SelectBox("department");
		//department.addChild(new ValidatorNotEmpty("deptNotEmpty", msgNotEmpty));
		department.addChild(new ValidatorSelectBoxNotEmpty("sbDepartmentNotEmpty", msgNotEmpty));
		SetupModule mod = (SetupModule)application.getModule(SetupModule.class);
		Collection deptList=new ArrayList();
		try {
			deptList=mod.getDepartment();
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		Map mpkey=new SequencedHashMap();
		mpkey.put("-1","-----None-----");
		for (Iterator iterator = deptList.iterator(); iterator.hasNext();) {
			Map mp = (Map) iterator.next();
			mpkey.put(mp.get("id").toString(),mp.get("name").toString());
			department.setOptionMap(mpkey);
		}
		addChild(name);
		addChild(description);
		addChild(producer);
		addChild(pfeCode);
		addChild(startProductionDate);
		addChild(endProductionDate);
		addChild(department);
		
		Panel status = new Panel("status");
		active=new Radio("active",Application.getInstance().getMessage("fms.setup.active", "Active"));
		inactive=new Radio("inactive", Application.getInstance().getMessage("fms.setup.inactive", "Inactive"));
		active.setGroupName("statusGroup");
		active.setChecked(true);
		inactive.setGroupName("statusGroup");
		status.addChild(active);
		status.addChild(inactive);
		addChild(status);
		
		engManpowerBudget = new TextField("engManpowerBudget");
		engManpowerBudget.setSize("25");
		engManpowerBudget.setMaxlength("255");
		addChild(engManpowerBudget);
		
		facilitiesBudget = new TextField("facilitiesBudget");
		facilitiesBudget.setSize("25");
		facilitiesBudget.setMaxlength("255");
		addChild(facilitiesBudget);
		
		vtrBudget = new TextField("vtrBudget");
		vtrBudget.setSize("25");
		vtrBudget.setMaxlength("255");
		addChild(vtrBudget);
		
		transportBudget = new TextField("transportBudget");
		transportBudget.setSize("25");
		transportBudget.setMaxlength("255");
		addChild(transportBudget);
		
		Panel pnbtn = new Panel("pnbtn");
		btnsave=new Button("btnsave", Application.getInstance().getMessage("fms.setup.btnSave", "Save"));
		btncancel=new Button("btncancel", Application.getInstance().getMessage("fms.setup.btnCancel", "Cancel"));
		pnbtn.addChild(btnsave);
		pnbtn.addChild(btncancel);
		addChild(pnbtn);
	}
	
	public void onRequest(Event evt){
		if(evt.getParameter("action")!=null){
			if(evt.getParameter("action").equals("edit")){
				populateData(evt.getParameter("programId"));
				setAction("edit");
				setProgramId(evt.getParameter("programId"));
			}else{
				init();
				setAction("add");
				setProgramId("");
			}
		}else{
			init();
			setAction("add");
			setProgramId("");
		}

	}
	
	public void populateData(String programId){
		SetupModule mod = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		ProgramObject p = new ProgramObject();
		try{
			p = mod.getProgram(programId);
			if (p != null && p.getProgramId() != null) {
			    if(p.getProgramName()!=null){
			    	name.setValue(p.getProgramName().toString());
			    }else{
			    	name.setValue("");
			    }
			    if(p.getDescription()!=null){
			    	description.setValue(p.getDescription().toString());
			    }else{
			    	description.setValue("");
			    }
			    
			    if(p.getProducer()!=null){
			    	producer.setValue(p.getProducer().toString());
			    }else{
			    	producer.setValue("");
			    }
			    
			    if(p.getPfeCode()!=null){
			    	pfeCode.setValue(p.getPfeCode().toString());
			    	pfeCodeHidden.setValue(p.getPfeCode().toString());
			    }else{
			    	pfeCode.setValue("");
			    	pfeCodeHidden.setValue("");
			    }
			    
			    if(p.getStartProductionDate()!=null){
			    	startProductionDate.setDate(p.getStartProductionDate());
			    }else{
			    	startProductionDate.setDate(null);
			    }
			    
			    if(p.getEndProductionDate()!=null){
			    	endProductionDate.setDate(p.getEndProductionDate());
			    }else{
			    	endProductionDate.setDate(null);
			    }
			    if(p.getDepartment()!=null){
			    	department.setSelectedOption(p.getDepartment());
			    }else{
			    	department.setValue(null);
			    }
			    
			    if(p.getEngManpowerBudget()!=null){
			    	engManpowerBudget.setValue(p.getEngManpowerBudget().toString());
			    	formatFloat(engManpowerBudget);
			    }else{
			    	engManpowerBudget.setValue("");
			    }
			    
			    if(p.getFacilitiesBudget()!=null){
			    	facilitiesBudget.setValue(p.getFacilitiesBudget().toString());
			    	formatFloat(facilitiesBudget);
			    }else{
			    	facilitiesBudget.setValue("");
			    }
			    
			    if(p.getVtrBudget()!=null){
			    	vtrBudget.setValue(p.getVtrBudget().toString());
			    	formatFloat(vtrBudget);
			    }else{
			    	vtrBudget.setValue("");
			    }
			    
			    if(p.getTransportBudget()!=null){
			    	transportBudget.setValue(p.getTransportBudget().toString());
			    	formatFloat(transportBudget);
			    }else{
			    	transportBudget.setValue("");
			    }
			    
			    active.setChecked("1".equals(p.getStatus()));
			    inactive.setChecked("0".equals(p.getStatus()));
				}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.toString());
		}
	}
	
	public Forward onSubmit(Event evt){
		Forward result = super.onSubmit(evt);

	    //determine which button was clicked
	    String buttonName = findButtonClicked(evt);

	    //if the cancel button was pressed
	    if (buttonName != null && btncancel.getAbsoluteName().equals(buttonName)) {
	      init();
	      return new Forward(Form.CANCEL_FORM_ACTION, getCancelUrl(), true);
	    } else {
	    	// validate fields
	    	
	    	if (!validDecimal(engManpowerBudget)) {
	    		engManpowerBudget.setInvalid(true);
	    		setInvalid(true);
	    	}
	    	
	    	if (!validDecimal(facilitiesBudget)) {
	    		facilitiesBudget.setInvalid(true);
	    		setInvalid(true);
	    	}
	    	
	    	if (!validDecimal(vtrBudget)) {
	    		vtrBudget.setInvalid(true);
	    		setInvalid(true);
	    	}
	    	
	    	if (!validDecimal(transportBudget)) {
	    		transportBudget.setInvalid(true);
	    		setInvalid(true);
	    	}
	    	
	    	return result;
	    }
	}
	
	public Forward onValidate(Event event){
		Forward forw=null;
		SetupModule mod=(SetupModule)Application.getInstance().getModule(SetupModule.class);
		boolean isexist=false;
		
		String oldPfeCode = "";
		if (getAction().equals("edit")){
			if (pfeCodeHidden.getValue()!=null){
				oldPfeCode = pfeCodeHidden.getValue().toString();
				
				if (!oldPfeCode.equals(pfeCode.getValue().toString())){
					try {
						isexist = mod.isExistPfeCode(pfeCode.getValue().toString());
					} catch (DaoException e) {
						Log.getLog(getClass()).error(e.toString(), e);
					}
				}
			}
		} else if (getAction().equals("add")){ 
			try {
				isexist=mod.isExistPfeCode(pfeCode.getValue().toString());
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString(), e);
			}
		}
		
		// data object
		ProgramObject p = new ProgramObject();
		p.setProgramName((String) name.getValue());
		p.setProducer((String)producer.getValue());
		p.setDescription(description.getValue().toString());
		p.setPfeCode(pfeCode.getValue().toString());
		p.setStartProductionDate(startProductionDate.getDate());
		p.setEndProductionDate(endProductionDate.getDate());
		p.setDepartment((String)department.getSelectedOptions().keySet().iterator().next());			
		p.setDepartment(getSelectBoxValue(department));
		
		p.setEngManpowerBudget(getDecimal(engManpowerBudget, false));
		p.setFacilitiesBudget(getDecimal(facilitiesBudget, false));
		p.setVtrBudget(getDecimal(vtrBudget, false));
		p.setTransportBudget(getDecimal(transportBudget, false));
		if (active.isChecked()) {
			p.setStatus("1");
		} else {
			p.setStatus("0");
		}
		
		if(getAction().equals("edit")){
			p.setUpdatedBy(getWhoModifyId());
            p.setUpdatedDate(new Date());
            p.setProgramId(getProgramId());
            
            if(!isexist){
				mod.updateProgram(p);
				forw = new Forward("edit");
			}else{
				pfeCode.setInvalid(true);
				forw = new Forward("duplicatePfeCode");
			}
		}else if(getAction().equals("add")){
			p.setCreatedBy(getWhoModifyId());
			p.setCreatedDate(new Date());
			p.setProgramId(UuidGenerator.getInstance().getUuid());
			
			if(!isexist){
				mod.addProgram(p);
				forw = new Forward("save");
			}else{
				pfeCode.setInvalid(true);
				forw = new Forward("duplicatePfeCode");
			}
		}
		return forw;
	}
	
	private void formatFloat(TextField field) {
		String val = getDecimal(field, true);
		field.setValue(val);
	}
	
	private String getDecimal(TextField field, boolean format) {
		String val = (String) field.getValue();
		if (!val.equals("")) {
			BigDecimal num = DecimalUtil.getDecimal(val);
			if (num != null) {
				if (format) {
					return DecimalUtil.formatDecimal(num, 2);
				} else {
					return DecimalUtil.getPlainDecimal(num, 2);
				}
			}
		}
		return "";
	}
	
	private boolean validDecimal(TextField field) {
		if (field.getValue().equals("")) {
			return true;
		} else {
			if (!getDecimal(field, false).equals("")) {
				return true;
			}
		}
		return false;
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
	
	public String getDefaultTemplate() {
		return "fms/progManagementTemp";
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getProgramId() {
		return programId;
	}
	
	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public TextField getProducer() {
		return producer;
	}

	public void setProducer(TextField producer) {
		this.producer = producer;
	}

	public TextField getEngManpowerBudget() {
		return engManpowerBudget;
	}

	public void setEngManpowerBudget(TextField engManpowerBudget) {
		this.engManpowerBudget = engManpowerBudget;
	}

	public TextField getFacilitiesBudget() {
		return facilitiesBudget;
	}

	public void setFacilitiesBudget(TextField facilitiesBudget) {
		this.facilitiesBudget = facilitiesBudget;
	}

	public TextField getVtrBudget() {
		return vtrBudget;
	}

	public void setVtrBudget(TextField vtrBudget) {
		this.vtrBudget = vtrBudget;
	}

	public TextField getTransportBudget() {
		return transportBudget;
	}

	public void setTransportBudget(TextField transportBudget) {
		this.transportBudget = transportBudget;
	}
	
}
