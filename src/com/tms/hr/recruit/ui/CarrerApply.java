package com.tms.hr.recruit.ui;

import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.hr.recruit.model.RecruitAppModule;
import com.tms.hr.recruit.model.RecruitModule;
import com.tms.hr.recruit.model.VacancyObj;

public class CarrerApply extends Form{
	public static final String TYPE_USAGE = "big";
	public static final String FORWARD_APPLY = "apply";
	public static final String FORWARD_ERROR = "error";
	
	private String vacancyCode;
	private VacancyObj vacancyObj;
	private String hodId;
	
	private Label lblRecPosition;
	private Label lblRecCountry;
	private Label lblRecDepartment;
	private Label lblRecNoOfPosition;
	private Label lblRecPriority;
	private Label lblRecRespon;
	private Label lblRecRequire;
	private Label lblRecEndDate;
	private Button btnApply;
	
	public void init(){
		initForm();
	}
	
	public void initForm(){
		setMethod("POST");
		setColumns(2);
		Application app = Application.getInstance();
		
		Label lblPosition = new Label("lblPosition", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.position") + "</span>");
		lblPosition.setAlign("right");
		lblRecPosition = new Label("lblRecPosition","");
		addChild(lblPosition);
		addChild(lblRecPosition);
		
		Label lblCountry = new Label("lblCountry", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.country") + "</span>");
		lblCountry.setAlign("right");
		lblRecCountry = new Label("lblRecCountry","");
		addChild(lblCountry);
		addChild(lblRecCountry);
		
		Label lblDepartment = new Label("lblDepartment", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.department") + "</span>");
		lblDepartment.setAlign("right");
		lblRecDepartment = new Label("lblRecDepartment","");
		addChild(lblDepartment);
		addChild(lblRecDepartment);
		
		Label lblNoOfPosition = new Label("lblNoOfPosition", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.noOfPosition") + "</span>");
		lblNoOfPosition.setAlign("right");
		lblRecNoOfPosition = new Label("lblRecNoOfPosition","");
		addChild(lblNoOfPosition);
		addChild(lblRecNoOfPosition);
		
		Label lblPriority = new Label("lblPriority", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.priorty") + "</span>");
		lblPriority.setAlign("right");
		lblRecPriority = new Label("lblRecPriority","");
		addChild(lblPriority);
		addChild(lblRecPriority);
		
		Label lblRespon = new Label("lblRespon", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.tbJobRespon") + "</span>");
		lblRespon.setAlign("right");
		lblRecRespon = new Label("lblRecRespon",""); 
		addChild(lblRespon);
		addChild(lblRecRespon);

		Label lblRequire= new Label("lblRequire", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.tbJobRequire") + "</span>");
		lblRequire.setAlign("right");
		lblRecRequire = new Label("lblRecRequire","");
		addChild(lblRequire);
		addChild(lblRecRequire);
		
		Label lblEndDate= new Label("lblEndDate", "<span class=\"fieldTitle\">" + 
				app.getMessage("recruit.general.label.vacancyEndDate") + "</span>");
		lblEndDate.setAlign("right");
		lblRecEndDate= new Label("lblRecEndDate","");
		addChild(lblEndDate);
		addChild(lblRecEndDate);
		
		btnApply = new Button("btnApply", app.getMessage("recruit.general.label.apply","Apply"));
		Label lblspace = new Label("lblspace","");
    	addChild(lblspace);
    	addChild(btnApply);
	}
	
	public Forward onValidate(Event evt) {
		String action = findButtonClicked(evt);
		 if(action.equals(btnApply.getAbsoluteName())){
			 vacancyCode = getVacancyCode();
			 evt.getRequest().getSession().setAttribute("positionTitle", lblRecPosition.getText());
			 evt.getRequest().getSession().setAttribute("hodId", getHodId());
			 return new Forward(FORWARD_APPLY);
		 }	 
		 else
			 return new Forward(FORWARD_ERROR);
	}
	
	public void onRequest(Event evt) {
		vacancyHitInfo(evt);
		populateData();
	}
	
	public void vacancyHitInfo(Event evt){
		if(getVacancyCode()!=null){
			Application app = Application.getInstance();
			RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			
			//insert the vacancy hit
			String ipAddr = evt.getRequest().getRemoteAddr();
			VacancyObj vacancyHitObj = new VacancyObj();
			vacancyHitObj.setVacancyCode(getVacancyCode());
			vacancyHitObj.setIpAddr(ipAddr);
			rm.insertVacancyHit(vacancyHitObj);
			
			//update the total part
			VacancyObj vacancyObj = new VacancyObj();
			ApplicantList appList = new ApplicantList();
			
			vacancyObj = appList.setVacancyTotal(getVacancyCode(), "totalViewed", false);
			ram.updateVacancyTotal(vacancyObj); //update the total viewed
		}
	}
	
	public void populateData(){
		if(getVacancyCode()!=null){
			Application app = Application.getInstance();
			RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
			int total=0;
			try{
				vacancyObj = rm.loadVacancy(getVacancyCode(), TYPE_USAGE);
				lblRecPosition.setText(vacancyObj.getPositionId());
				lblRecCountry.setText(vacancyObj.getCountryId());
				lblRecDepartment.setText(vacancyObj.getDepartmentId());
				
				total = vacancyObj.getNoOfPosition() - vacancyObj.getNoOfPositionOffered();
					
				//lblRecNoOfPosition.setText("("+vacancyObj.getNoOfPositionOffered() + "/" + vacancyObj.getNoOfPosition() + " positions)");
				lblRecNoOfPosition.setText("("+ total + " positions)");
				
				lblRecPriority.setText(vacancyObj.getPriorityName());
				lblRecRespon.setText(vacancyObj.getResponsibilities());
				lblRecRequire.setText(vacancyObj.getRequirements());
				lblRecEndDate.setText(vacancyObj.getEndDate().toString());
				setHodId(vacancyObj.getHodId());
				}catch(DataObjectNotFoundException e){
					Log.getLog(getClass()).error("Module " + getVacancyCode() + " not found");
					init();
				}
		}
	}
	
	public String getDefaultTemplate() {
		return "recruit/carrerView";
	}
	
	//getter setter
	public String getVacancyCode() {
		return vacancyCode;
	}

	public void setVacancyCode(String vacancyCode) {
		this.vacancyCode = vacancyCode;
	}

	public VacancyObj getVacancyObj() {
		return vacancyObj;
	}

	public void setVacancyObj(VacancyObj vacancyObj) {
		this.vacancyObj = vacancyObj;
	}

	public Label getLblRecPosition() {
		return lblRecPosition;
	}

	public void setLblRecPosition(Label lblRecPosition) {
		this.lblRecPosition = lblRecPosition;
	}

	public String getHodId() {
		return hodId;
	}

	public void setHodId(String hodId) {
		this.hodId = hodId;
	}

	//29-12-06
	public Button getBtnApply() {
		return btnApply;
	}

	public void setBtnApply(Button btnApply) {
		this.btnApply = btnApply;
	}

	public Label getLblRecCountry() {
		return lblRecCountry;
	}

	public void setLblRecCountry(Label lblRecCountry) {
		this.lblRecCountry = lblRecCountry;
	}

	public Label getLblRecDepartment() {
		return lblRecDepartment;
	}

	public void setLblRecDepartment(Label lblRecDepartment) {
		this.lblRecDepartment = lblRecDepartment;
	}

	public Label getLblRecEndDate() {
		return lblRecEndDate;
	}

	public void setLblRecEndDate(Label lblRecEndDate) {
		this.lblRecEndDate = lblRecEndDate;
	}

	public Label getLblRecNoOfPosition() {
		return lblRecNoOfPosition;
	}

	public void setLblRecNoOfPosition(Label lblRecNoOfPosition) {
		this.lblRecNoOfPosition = lblRecNoOfPosition;
	}

	public Label getLblRecPriority() {
		return lblRecPriority;
	}

	public void setLblRecPriority(Label lblRecPriority) {
		this.lblRecPriority = lblRecPriority;
	}

	public Label getLblRecRequire() {
		return lblRecRequire;
	}

	public void setLblRecRequire(Label lblRecRequire) {
		this.lblRecRequire = lblRecRequire;
	}

	public Label getLblRecRespon() {
		return lblRecRespon;
	}

	public void setLblRecRespon(Label lblRecRespon) {
		this.lblRecRespon = lblRecRespon;
	}		
}
