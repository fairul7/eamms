package com.tms.hr.recruit.ui;

import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;

import kacang.Application;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.ui.Event;
import kacang.util.Log;

import com.tms.hr.recruit.model.RecruitAppModule;

public class JobAppAdditionalView extends Form{
	private String applicantId;
	
	private Label lblRecDownloadResume;
	private Label lblRecExpectedSalary;
	/*private Label lblRecNego;*/
	private Label lblRecWillingTravel;
	private Label lblRecWillingRelocate;
	private Label lblRecOwnTransport;
	
	public void onRequest(Event evt) {
		if(evt.getRequest().getSession().getAttribute("applicantId")!=null && !evt.getRequest().getSession().getAttribute("applicantId").equals("")){
			applicantId=evt.getRequest().getSession().getAttribute("applicantId").toString();
			populateData();
		}	
	}
	
	public void init(){
		initForm();
	}
	
	public void initForm(){
		setMethod("POST");
		setColumns(2);
		Application app = Application.getInstance();
		
		Label lblDownloadResume= new Label("lblDownloadResume", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.downloadResume") + "</span>");
		lblDownloadResume.setAlign("right");
		lblRecDownloadResume = new Label("lblRecDownloadResume","");
    	addChild(lblDownloadResume);
    	addChild(lblRecDownloadResume);
    	
    	Label lblExpectedSalary= new Label("lblExpectedSalary", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.expectedSalary") + "</span>");
    	lblExpectedSalary.setAlign("right");
    	lblRecExpectedSalary = new Label("lblRecExpectedSalaryType","");
    	addChild(lblExpectedSalary);
    	addChild(lblRecExpectedSalary);
    	
    	/*Label lblNego= new Label("lblNego", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.negotiable") + "</span>");
    	lblNego.setAlign("right");
    	lblRecNego = new Label("lblRecNego","");
    	addChild(lblNego);
    	addChild(lblRecNego);
    	*/
    	Label lblTravel= new Label("lblTravel", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.travel") + "</span>");
    	lblTravel.setAlign("right");
    	lblRecWillingTravel = new Label("lblRecWillingTravel","");
    	addChild(lblTravel);
    	addChild(lblRecWillingTravel);
		
    	Label lblRelocate= new Label("lblRelocate", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.relocate") + "</span>");
    	lblRelocate.setAlign("right");
    	lblRecWillingRelocate = new Label("lblRecWillingRelocate","");
    	addChild(lblRelocate);
    	addChild(lblRecWillingRelocate);
    	
    	Label lblTransport= new Label("lblTransport", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.transport") + "</span>");
    	lblTransport.setAlign("right");
    	lblRecOwnTransport = new Label("lblRecOwnTransport","");
    	addChild(lblTransport);
    	addChild(lblRecOwnTransport);
	}	
	
	public void populateData(){
		if(getApplicantId()!=null){
			Application app = Application.getInstance();
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			
			Collection col=ram.loadApplicantPersonal(getApplicantId());
			HashMap map = (HashMap) col.iterator().next();
				
			if(map.get("resumePath")!=null && !map.get("resumePath").equals("")){
				/*String[] resumePathFile = map.get("resumePath").toString().split(" ");
				StringBuffer sb = new StringBuffer();
				for(int i=0;i<resumePathFile.length;i++){
					sb.append(resumePathFile[i]);
					sb.append("%20");
				}*/
				try{
				lblRecDownloadResume.setText("<a href=/recruit/downloadFile?resumeId="+URLEncoder.encode(map.get("resumePath").toString(), "UTF-8")+">Download</a>");
				}catch(Exception e){
					Log.getLog(getClass()).error("error in encode URL: " + e.getMessage(), e);
				}
			}else
				lblRecDownloadResume.setText("-  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
						"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
						"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			
			JobAppAdditional jobAppAdditional = new JobAppAdditional();
			String salary="";
			String negotiable="";
			if(map.get("negotiable").equals("1"))
				negotiable = "Negotiable";
			else
				negotiable = "Non-negotiable";
				
			if(map.get("expectedTypeSalary")!=null && !map.get("expectedTypeSalary").equals(""))
				salary = jobAppAdditional.getCurrencyMap().get(map.get("expectedTypeSalary")).toString();
			
			if(map.get("expectedSalary")!=null && !map.get("expectedSalary").equals(""))
				lblRecExpectedSalary.setText(salary + " " + map.get("expectedSalary").toString() + " (" + negotiable + ")");
			else
				lblRecExpectedSalary.setText("-  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
						"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
						"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			
			if(map.get("willingTravel").equals("No"))
				lblRecWillingTravel.setText("No");
			else if(map.get("willingTravel").equals("Light"))
				lblRecWillingTravel.setText("Light");
			else if(map.get("willingTravel").equals("Moderate "))
				lblRecWillingTravel.setText("Moderate ");
			else
				lblRecWillingTravel.setText("-");
			
			if(map.get("willingRelocate").equals("No"))
				lblRecWillingRelocate.setText("No");
			else if(map.get("willingRelocate").equals("Will consider"))
				lblRecWillingRelocate.setText("Will consider");
			else
				lblRecWillingRelocate.setText("-");
			
			if(map.get("ownTransport").equals("No"))
				lblRecOwnTransport.setText("No");
			else if(map.get("ownTransport").equals("Yes"))
				lblRecOwnTransport.setText("Yes");
			else
				lblRecOwnTransport.setText("-");
		}
	}

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}
}
