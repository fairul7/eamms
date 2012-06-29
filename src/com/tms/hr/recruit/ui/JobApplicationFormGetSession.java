package com.tms.hr.recruit.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import kacang.Application;
import kacang.stdui.Form;
import kacang.ui.Event;

import com.tms.hr.recruit.model.RecruitAppModule;

public class JobApplicationFormGetSession extends Form{
	
	//	for editing status
	private String codeStatusE;
	private String vacancyCodeE;
	private String applicantIdE;
	private String needRedirect;
	
	public void onRequest(Event evt) {	
		//always remove the editing status session
		if(getCodeStatusE()!=null && getCodeStatusE().equals("edit")){
			evt.getRequest().getSession().setAttribute("JAFtype", "edit");
			//validate within 24 hours 
			boolean withinTime = false;
			boolean applicantStatusIn=false;
			Date getDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			Application app = Application.getInstance();
			RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
			HashMap dataMap = (HashMap)ram.loadApplicantPersonal(getApplicantIdE()).iterator().next();
			Date createdDate = (Date)dataMap.get("createdDate");
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(createdDate);
			cal.add(cal.DATE, 1); //24 hour mean 1 day
			
			if(getDate.before(cal.getTime())){
				withinTime = true;
				if(dataMap.get("applicantStatus").equals("New")){
					applicantStatusIn=true;
				}else{
					applicantStatusIn=false;
				}
			}else{
				withinTime = false;
			}
			
			if(withinTime){
				if(withinTime && applicantStatusIn){
					HashMap map = new HashMap();
					Collection editColData = new ArrayList();
					map.put("codeStatusE", getCodeStatusE());
					map.put("applicantIdE", getApplicantIdE());
					map.put("vacancyCodeE", getVacancyCodeE());
					editColData.add(map);
					
					evt.getRequest().getSession().setAttribute("editData", editColData);
					setNeedRedirect("no");
				}else{
					setNeedRedirect("yesAndUndergoing");
				}	
			}else{
				//redirect to cannot edit this page
				setNeedRedirect("yes");
			}
		}
		else{
			//evt.getRequest().getSession().setAttribute("JAFtype", "add");
			evt.getRequest().getSession().removeAttribute("JAFtype");
			evt.getRequest().getSession().removeAttribute("editData");
			setNeedRedirect("no");
		}
	}

	//getter setter
	public String getApplicantIdE() {
		return applicantIdE;
	}

	public void setApplicantIdE(String applicantIdE) {
		this.applicantIdE = applicantIdE;
	}

	public String getCodeStatusE() {
		return codeStatusE;
	}

	public void setCodeStatusE(String codeStatusE) {
		this.codeStatusE = codeStatusE;
	}

	public String getNeedRedirect() {
		return needRedirect;
	}

	public void setNeedRedirect(String needRedirect) {
		this.needRedirect = needRedirect;
	}

	public String getVacancyCodeE() {
		return vacancyCodeE;
	}

	public void setVacancyCodeE(String vacancyCodeE) {
		this.vacancyCodeE = vacancyCodeE;
	}
	
}
