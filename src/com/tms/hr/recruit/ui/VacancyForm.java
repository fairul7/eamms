package com.tms.hr.recruit.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.stdui.Button;
import kacang.stdui.ButtonGroup;
import kacang.stdui.CheckBox;
import kacang.stdui.DateField;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.FormField;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorMessage;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.hr.recruit.model.RecruitModule;
import com.tms.hr.recruit.model.VacancyObj;

public class VacancyForm extends VacancyTempFormDefault{
	public static final String FORWARD_SUBMITED = "submited";
	public static final String FORWARD_ERROR = "error";
	
	protected Label lblVacancyCode;
	protected Label lblTempName;
	protected SelectBox sbVacancyTempCode;
	protected TextField txtVacancyCode;
	protected TextField txtNoOfPosition;
	protected Radio rPriortyNormal;
	protected Radio rPriortyUrgent;
	protected DateField startDate;
	protected DateField endDate;
	protected CheckBox cbVacancyTemp;
	protected TextField txtVacancyTempCode;
	private ButtonGroup radioGroup;
	protected Panel btnPanel;
	protected Button btnSubmit;
	protected Button btnCancel;
	
	protected Label lblcbVacancyTemp;
	protected Label lblVTempName;
	private Map mapVacancyTempCode = new LinkedHashMap();
	
	protected ValidatorMessage vMsgStartDate;
	protected ValidatorMessage vMsgEndDate;
	
	public void init(){
		this.initFormChild();
	}
	
	public void initFormChild(){
    	setColumns(2);
    	setMethod("POST");
    	//removeChildren();
    	Application app = Application.getInstance();
    	
    	sbVacancyTempCode = new SelectBox("sbVacancyTempCode");
        lblTempName= new Label("lblTempName", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.tempName") + "</span>");
    	lblTempName.setAlign("right");
    	addChild(lblTempName);
    	addChild(sbVacancyTempCode);
    	
    	//set the selectbox (key,value)
    	RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
    	Collection colVacancyTempCode = rm.findALLVacancyTempCode(null, 0, -1, "vacancyTempCode", false);
    	
    	mapVacancyTempCode.put("---", app.getMessage("recruit.general.hierachy.selectVacancyTempCode"));
    	for(Iterator itr = colVacancyTempCode.iterator(); itr.hasNext();){
    		VacancyObj vacancyTempObj = (VacancyObj) itr.next();
    		mapVacancyTempCode.put(vacancyTempObj.getVacancyTempCode(), vacancyTempObj.getVacancyTempCode());
    	}
    	
    	sbVacancyTempCode.setOptionMap(mapVacancyTempCode);
    	sbVacancyTempCode.setOnChange("submit()"); //add javascript function name
    	//end setup Vacancy Template SelectBox
    	
    	txtVacancyCode = new TextField("txtVacancyCode");
    	txtVacancyCode.setSize("30");
    	txtVacancyCode.setMaxlength("20");
    	//txtVacancyCode.addChild(new ValidateVacancyTempCode("txtVacancyTempCodeVdc",app.getMessage("recruit.general.warn.codeKey")));
    	lblVacancyCode= new Label("lblVacancyCode",  "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.vacancyCode")  + "*</span>");
    	lblVacancyCode.setAlign("right");
    	addChild(lblVacancyCode);
    	addChild(txtVacancyCode);
    	
    	//calling VacancyTempFormDefault
    	super.init();
    	
    	Label lblNoOfPosition= new Label("lblNoOfPosition", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.noOfPosition") + "*</span>");
    	lblNoOfPosition.setAlign("right");
    	txtNoOfPosition = new TextField("txtNoOfPosition");
    	txtNoOfPosition.setSize("5");
    	txtNoOfPosition.setMaxlength("3");
    	
    	addChild(lblNoOfPosition);
    	addChild(txtNoOfPosition);
    	
    	Label lblPriorty= new Label("lblPriorty", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.priorty") + "</span>");
    	lblPriorty.setAlign("right");
    	rPriortyNormal = new Radio("rPriortyNormal", app.getMessage("recruit.general.label.priortyNormal") ,true);
    	rPriortyUrgent = new Radio("rPriortyUrgent", app.getMessage("recruit.general.label.priortyUrgent"));
    	
    	radioGroup = new ButtonGroup("radioGroup");
    	radioGroup.setType(ButtonGroup.RADIO_TYPE);
    	radioGroup.setColumns(2);
    	radioGroup.addButton(rPriortyNormal);
    	radioGroup.addButton(rPriortyUrgent);
    	addChild(lblPriorty);
    	addChild(radioGroup);
    	
    	Calendar TodayDate = Calendar.getInstance();
    	
    	startDate = new DatePopupField("startDate");
    	startDate.setDate(TodayDate.getTime());
    	vMsgStartDate = new ValidatorMessage("vMsgStartDate");
    	startDate.addChild(vMsgStartDate);
    	
    	endDate = new DatePopupField("EndDate");
    	endDate.setDate(TodayDate.getTime());
    	vMsgEndDate = new ValidatorMessage("vMsgEndDate");
    	endDate.addChild(vMsgEndDate);
    	
    	Label lblStartDate= new Label("lblStartDate", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.vacancyStartDate") + "*</span>");
    	Label lblEndDate= new Label("lblEndDate", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.vacancyEndDate") + "*</span>");
    	lblStartDate.setAlign("right");
    	lblEndDate.setAlign("right");
    	
    	addChild(lblStartDate);
    	addChild(startDate);
    	addChild(lblEndDate);
    	addChild(endDate);
    	
    	//checkbox
    	lblcbVacancyTemp= new Label("lblcbVacancyTemp", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.cbVacancyTemp") + "</span>");
    	lblcbVacancyTemp.setAlign("right");
    	cbVacancyTemp = new CheckBox("cbVacancyTemp");
    	cbVacancyTemp.setOnClick("show_row(1);");
    	//cbVacancyTemp.setOnClick("document.getElementById('info1').style.visibility = this.checked ? 'visible' : 'hidden',  document.getElementById('info2').style.visibility = this.checked ? 'visible' : 'hidden'");
    	addChild(lblcbVacancyTemp);
    	addChild(cbVacancyTemp);
    	
    	txtVacancyTempCode = new TextField("txtVacancyTempCode");
    	txtVacancyTempCode.setSize("30");
    	txtVacancyTempCode.setMaxlength("20");
    	
    	lblVTempName= new Label("lblVTempName",  "<span class=\"fieldTitle\">" 
    			+ app.getMessage("recruit.general.label.tempName")  + "</span>");
    	addChild(lblVTempName);
    	addChild(txtVacancyTempCode);
    	//end of checkbox
    	
    	btnSubmit = new Button("btnSubmit", app.getMessage("recruit.general.label.submit","Submit"));
    	btnCancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("recruit.general.label.cancel","Cancel"));
    	
    	btnPanel = new Panel("btnPanel");
    	btnPanel.setColspan(2);
    	btnPanel.addChild(btnSubmit);
    	btnPanel.addChild(btnCancel);
    	Label lblspace = new Label("lblspace","");
    	addChild(lblspace);
    	addChild(btnPanel);
	}
	
	//checking to see if same position already in database
	public boolean lookUpPosition(FormField ffPosition, FormField ffCountry, FormField ffDepartment, String vacancyCode){
		boolean flag=false;
		boolean hasFoundFlag=false;
		Application app = Application.getInstance();
		RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
		List listPos = (List) ffPosition.getValue();
        String strSbPos = (String) listPos.get(0);
        
        List listCountry = (List) ffCountry.getValue();
        String strSbCountry = (String) listCountry.get(0);
        
        List listDept = (List) ffDepartment.getValue();
        String strSbDeprt = (String) listDept.get(0);
        
        Collection findPos = rm.lookUpPosition(vacancyCode);
        for(Iterator ite=findPos.iterator(); ite.hasNext();){
        	HashMap map = (HashMap)ite.next();
        	//first validation-checking position, country, department(JUST check)
	        if(map.get("positionId").equals(strSbPos) && map.get("countryId").equals(strSbCountry) && map.get("departmentId").equals(strSbDeprt)){
	        	hasFoundFlag=true;
	        }else{
	        	hasFoundFlag=false;
	        }
	        
	        //second validation-checking vacancy date-crashing mode
	        if(hasFoundFlag){
	        	try{
	        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
	        		Date getTodayDate = new Date(); //get todayDate
	        		Date dbStartDate = sdf.parse(map.get("startDate").toString()); //db start date
		        	Date dbEndDate = sdf.parse(map.get("endDate").toString()); //db end date
		        	
		        	String getStartDate=sdf.format(startDate.getDate()); //selected start date
		        	String getEndDate=sdf.format(endDate.getDate()); //selected end date
		        	
		        	
		        	if(dbEndDate.after(getTodayDate)){ //ignore the due date vacancy
		        		if(getStartDate.equals(map.get("endDate").toString()) || dbEndDate.after(startDate.getDate())){	
		        			if(getStartDate.equals(map.get("startDate").toString()) || dbStartDate.before(startDate.getDate()))
		        				flag=true;
		        		}
		        		
		        		if(getEndDate.equals(map.get("endDate").toString()) || dbEndDate.after(endDate.getDate())){	
		        			if(getEndDate.equals(map.get("startDate").toString()) || dbStartDate.before(endDate.getDate()))
		        				flag=true;
		        		}else{
		        			if(dbStartDate.after(startDate.getDate()) && dbStartDate.before(endDate.getDate())){
		        				flag=true;
		        			}
		        		}
		        	}
		        	  
	        	}catch(Exception e){
	        		//nothing
	        	}
	        }
	        
        }
		return flag;
	}
	
	//onSubmit where validate function is called
	public Forward onSubmit(Event evt) {
		Forward forward = super.onSubmit(evt); 
		String action = findButtonClicked(evt);
		Application app = Application.getInstance();
		boolean hasPositionDateExist=false;
		if(action!=null && action.equals(btnSubmit.getAbsoluteName())){
			validate(txtVacancyCode,"codeExist", "vacancyCode");
			validate(txtNoOfPosition, "txtInt", null);
			
			validate(sbPosition, "sb", null);
			validate(sbCountry, "sb", null);
			validate(sbDepartment,"sb", null);
			
			//first validation-checking position, country, department
			//second validation-checking vacancy date-crashing mode
			if(validate(sbPosition, "sb", null) && validate(sbCountry, "sb", null) && validate(sbDepartment,"sb", null))
				hasPositionDateExist=lookUpPosition(sbPosition, sbCountry, sbDepartment, "");
			
			/*if(hasPositionDateExist){	
				vaMsgPositionFound.showError(app.getMessage("recruit.vacancy.alert.positionFound"));
				sbPosition.setInvalid(true);
				setInvalid(true);	
			}*/
			
			//checking date validation
			VacancyObj cDateObj = new VacancyObj();
			try{
				boolean csDate = cDateObj.checkDate(startDate.getDate());
				if(csDate || hasPositionDateExist){
					if(hasPositionDateExist){
						vMsgStartDate.showError(app.getMessage("recruit.vacancy.alert.startDateExist"));
					}
					if(csDate){
						vMsgStartDate.showError("");
					}
						
					startDate.setInvalid(true);
					setInvalid(true);
				}
				
				boolean ceDate= cDateObj.checkDate(endDate.getDate());
				if(ceDate || hasPositionDateExist){
					if(hasPositionDateExist){
						vMsgEndDate.showError(app.getMessage("recruit.vacancy.alert.endDateExist"));
					}
					if(ceDate){
						vMsgEndDate.showError("");
					}
					
					endDate.setInvalid(true);
					setInvalid(true);
				}
			}catch(Exception e){
				//nothing
			}
						
			boolean cseDate = cDateObj.checkDate(startDate.getDate(), endDate.getDate());
			if(cseDate){
				startDate.setInvalid(true);
				endDate.setInvalid(true);
				setInvalid(true);
			}
			//end date validation
			
			validate(tbJobRespon, "tb", null);
			validate(tbJobRequire, "tb", null);
			
			if(cbVacancyTemp.isChecked()){
				validate(txtVacancyTempCode,"codeExist", "vacancyTempCode");
			}
			
    	 }else{
    		 //vaMsgPositionFound.showError("", false);
    		 vMsgStartDate.showError("", false);
    		 populateData();
    	 }
		
		return forward;
	}
	
	public void onRequest(Event evt) {
		init();
	}
	
	//	onValidate-data is saved to database
    public Forward onValidate(Event evt) {
    	 String action = findButtonClicked(evt);
    	 if(action!=null && action.equals(btnSubmit.getAbsoluteName())){ //action cannot be null
    		 //auditObj
			 VacancyObj auditObj = new VacancyObj();
			 String actionTaken="";
    		 Application app = Application.getInstance();
    		 RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
    		 
    		 //get Date
    		 Date getDate = new Date();
    		 
    		 //get the selectbox value
    		 String selectedPosition = (String) sbPosition.getSelectedOptions().keySet().iterator().next(); 
    		 String selectedCountry = (String) sbCountry.getSelectedOptions().keySet().iterator().next();
    		 String selectedDept = (String) sbDepartment.getSelectedOptions().keySet().iterator().next();
    		 
    		 //create obj and put value into obj
    		 VacancyObj vacancyObj = new VacancyObj();
    		 vacancyObj.setVacancyCode((String)txtVacancyCode.getValue());
    		 vacancyObj.setPositionId(selectedPosition);
    		 vacancyObj.setCountryId(selectedCountry);
    		 vacancyObj.setDepartmentId(selectedDept);
    		 vacancyObj.setNoOfPosition(Integer.parseInt(txtNoOfPosition.getValue().toString()));
    		 
    		 if(rPriortyNormal.isChecked())
    			 vacancyObj.setPriority(false);
    		 else
    			 vacancyObj.setPriority(true);
    			 
    		 vacancyObj.setResponsibilities((String)tbJobRespon.getValue());
    		 vacancyObj.setRequirements((String)tbJobRequire.getValue());
    		 vacancyObj.setStartDate(startDate.getDate());
    		 vacancyObj.setEndDate(endDate.getDate()); 
    		 vacancyObj.setCreatedBy(app.getCurrentUser().getId());
    		 vacancyObj.setCreatedDate(getDate); 
    		 vacancyObj.setLastUpdatedBy(app.getCurrentUser().getId());
    		 vacancyObj.setLastUpdatedDate(getDate);
    		 vacancyObj.setTotalApplied(0);
    		 rm.insertVacancy(vacancyObj);
    		 
    		 //audit
			 actionTaken="Add Vacancy";
			 auditObj.setAndInsertAudit(vacancyObj.getVacancyCode(), "", actionTaken);
    		 
    		 if(cbVacancyTemp.isChecked()){
    			 VacancyObj vacancyTempObj = new VacancyObj();
    			 
    			 vacancyTempObj.setVacancyTempCode((String)txtVacancyTempCode.getValue());
        		 vacancyTempObj.setPositionId(selectedPosition);
        		 vacancyTempObj.setCountryId(selectedCountry);
        		 vacancyTempObj.setDepartmentId(selectedDept);
        		 vacancyTempObj.setResponsibilities((String)tbJobRespon.getValue());
        		 vacancyTempObj.setRequirements((String)tbJobRequire.getValue());
        		 vacancyTempObj.setCreatedBy(app.getCurrentUser().getId());
        		 vacancyTempObj.setCreatedDate(getDate); 
        		 vacancyTempObj.setLastUpdatedBy(app.getCurrentUser().getId());
        		 vacancyTempObj.setLastUpdatedDate(getDate);
        		 rm.insertVacancyTemp(vacancyTempObj);
        		 
        		 //audit
    			 actionTaken="Add Vacancy Template";
    			 auditObj.setAndInsertAudit(vacancyTempObj.getVacancyTempCode(), "", actionTaken);
    		 }
    		 
    		 init();
    		 return new Forward(FORWARD_SUBMITED);
    	 }else
    		 return new Forward(FORWARD_ERROR); 
    }
    
    //populate the data into FormField when selectbox is selected
    public void populateData(){
    	boolean byPassSb = validate(sbVacancyTempCode, "sb", null);
    	if(byPassSb){
    		Application app = Application.getInstance();
    		RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
    		
    		List list = (List) sbVacancyTempCode.getValue();
	        String strVacancyTempCode = (String) list.get(0);
    		try{
    			VacancyObj vacancyTempObj= rm.loadVacancyTemp(strVacancyTempCode);
    			sbPosition.setSelectedOption(vacancyTempObj.getPositionId());
				sbDepartment.setSelectedOption(vacancyTempObj.getDepartmentId());
				sbCountry.setSelectedOption(vacancyTempObj.getCountryId());
				tbJobRespon.setValue(vacancyTempObj.getResponsibilities());
				tbJobRequire.setValue(vacancyTempObj.getRequirements());
    			
    		}catch(DataObjectNotFoundException e){
    			Log.getLog(getClass()).error("Module " + strVacancyTempCode + " not found");
    		}
    	}
    	else{
    		sbVacancyTempCode.setInvalid(false);
    		setInvalid(false);
    		sbPosition.setSelectedOption("---");
    		sbDepartment.setSelectedOption("---");
    		sbCountry.setSelectedOption("---");
    		tbJobRespon.setValue("");
    		tbJobRequire.setValue("");
    	}	
    }

    public String getDefaultTemplate() {
    	return "recruit/vacancyForm";
    }

    //getter setter
	public Button getBtnCancel() {
		return btnCancel;
	}

	public void setBtnCancel(Button btnCancel) {
		this.btnCancel = btnCancel;
	}

	public Panel getBtnPanel() {
		return btnPanel;
	}

	public void setBtnPanel(Panel btnPanel) {
		this.btnPanel = btnPanel;
	}

	public Button getBtnSubmit() {
		return btnSubmit;
	}

	public void setBtnSubmit(Button btnSubmit) {
		this.btnSubmit = btnSubmit;
	}

	public CheckBox getCbVacancyTemp() {
		return cbVacancyTemp;
	}

	public void setCbVacancyTemp(CheckBox cbVacancyTemp) {
		this.cbVacancyTemp = cbVacancyTemp;
	}

	public DateField getEndDate() {
		return endDate;
	}

	public void setEndDate(DateField endDate) {
		this.endDate = endDate;
	}

	public Label getLblcbVacancyTemp() {
		return lblcbVacancyTemp;
	}

	public void setLblcbVacancyTemp(Label lblcbVacancyTemp) {
		this.lblcbVacancyTemp = lblcbVacancyTemp;
	}

	public Label getLblTempName() {
		return lblTempName;
	}

	public void setLblTempName(Label lblTempName) {
		this.lblTempName = lblTempName;
	}

	public Label getLblVacancyCode() {
		return lblVacancyCode;
	}

	public void setLblVacancyCode(Label lblVacancyCode) {
		this.lblVacancyCode = lblVacancyCode;
	}

	public Label getLblVTempName() {
		return lblVTempName;
	}

	public void setLblVTempName(Label lblVTempName) {
		this.lblVTempName = lblVTempName;
	}

	public Radio getRPriortyNormal() {
		return rPriortyNormal;
	}

	public void setRPriortyNormal(Radio priortyNormal) {
		rPriortyNormal = priortyNormal;
	}

	public Radio getRPriortyUrgent() {
		return rPriortyUrgent;
	}

	public void setRPriortyUrgent(Radio priortyUrgent) {
		rPriortyUrgent = priortyUrgent;
	}

	public SelectBox getSbVacancyTempCode() {
		return sbVacancyTempCode;
	}

	public void setSbVacancyTempCode(SelectBox sbVacancyTempCode) {
		this.sbVacancyTempCode = sbVacancyTempCode;
	}

	public DateField getStartDate() {
		return startDate;
	}

	public void setStartDate(DateField startDate) {
		this.startDate = startDate;
	}

	public TextField getTxtNoOfPosition() {
		return txtNoOfPosition;
	}

	public void setTxtNoOfPosition(TextField txtNoOfPosition) {
		this.txtNoOfPosition = txtNoOfPosition;
	}

	public TextField getTxtVacancyCode() {
		return txtVacancyCode;
	}

	public void setTxtVacancyCode(TextField txtVacancyCode) {
		this.txtVacancyCode = txtVacancyCode;
	}

	public TextField getTxtVacancyTempCode() {
		return txtVacancyTempCode;
	}

	public void setTxtVacancyTempCode(TextField txtVacancyTempCode) {
		this.txtVacancyTempCode = txtVacancyTempCode;
	}

	public Map getMapVacancyTempCode() {
		return mapVacancyTempCode;
	}

	public void setMapVacancyTempCode(Map mapVacancyTempCode) {
		this.mapVacancyTempCode = mapVacancyTempCode;
	}

	public ButtonGroup getRadioGroup() {
		return radioGroup;
	}

	public void setRadioGroup(ButtonGroup radioGroup) {
		this.radioGroup = radioGroup;
	}

}























