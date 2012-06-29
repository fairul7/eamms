package com.tms.hr.recruit.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.ui.Event;
import kacang.ui.Widget;
import kacang.util.Log;

import com.tms.hr.orgChart.model.OrgChartHandler;
import com.tms.hr.orgChart.model.OrgSetup;
import com.tms.hr.recruit.model.RecruitModule;
import com.tms.hr.recruit.model.VacancyObj;

public class CarrerList extends Widget{
	private Collection carrerCol= new ArrayList();
	private String employeeOpprMsg="";
	private String noOpprMsg="";
	public void onRequest(Event evt) {
		carrerCol.clear();
		populateData();
	}
	
	public void populateData(){

		boolean flag=true;
		Application app = Application.getInstance();  	
    	OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);
    	Collection deptsCol = oc.findAllSetup(OrgChartHandler.TYPE_TITLE, null, 0, -1, "shortDesc", false, true);//listing all department 
    	
    	RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class); 
    	try{
    		VacancyObj smbObj = rm.loadMessagebody();
    		if(smbObj.getEmployeeOppor()!=null & !smbObj.getEmployeeOppor().equals("")){
    			employeeOpprMsg = smbObj.getEmployeeOppor();
    		}else{
    			employeeOpprMsg = app.getMessage("recruit.employee.msg.label");
    		}
    	
    	}catch(DataObjectNotFoundException e){
			Log.getLog(getClass()).error("Module Message Body not found" + e);
    	}
    	
		Collection vacancyListCol=rm.listAllVacancy();
		
		int totalPosition=0;
		String strDeptName="";
		HashMap map = new HashMap();
		HashMap mapData = new HashMap();
		HashMap mapSecond = new HashMap();
		
    	for(Iterator iteDept = deptsCol.iterator(); iteDept.hasNext(); ){
    		OrgSetup obj = (OrgSetup) iteDept.next();
    		map.put(obj.getCode(), obj.getCode()); //for comparison 
    		mapData.put(obj.getCode(), obj.getShortDesc()); // for data
    	}
    	
    	for(Iterator iteVacancy = vacancyListCol.iterator(); iteVacancy.hasNext(); ){
    		VacancyObj vacancyObjStore=new VacancyObj();
    		VacancyObj vacancyObj = (VacancyObj)iteVacancy.next();
    		totalPosition = vacancyObj.getNoOfPosition() - vacancyObj.getNoOfPositionOffered();
    		//strDeptName= mapData.get(vacancyObj.getCode()).toString() + " <i>("+ totalPosition +")</i>";
    		strDeptName= mapData.get(vacancyObj.getCode()).toString();
    		vacancyObjStore.setTotalViewed(vacancyObj.getTotalViewed());
    		vacancyObjStore.setNoOfPosition(totalPosition);
    		vacancyObjStore.setCarrerName(strDeptName);
			vacancyObjStore.setVacancyCode(vacancyObj.getVacancyCode());
			
			//validate the noOfPositonOffered==noOfPosition
			if(vacancyObj.getNoOfPositionOffered()==vacancyObj.getNoOfPosition())
				vacancyObjStore.setEqualNoOfPosition(true);
			
			if(vacancyObjStore.getNoOfPosition() > 0){
				if(totalPosition > 0){
					carrerCol.add(vacancyObjStore);
				}
			}
			
			mapSecond.put(vacancyObj.getCode(), vacancyObj.getCode()); //for comparison
    	}
    	
    	if(carrerCol.size()==0){
    		noOpprMsg = app.getMessage("recruit.employee.msg.label");
    	}else{
    		noOpprMsg = "";
    	}
    	
    	/* this is to add non-vacancy department
    	 * for(Iterator iteDept = deptsCol.iterator(); iteDept.hasNext(); ){
    		OrgSetup obj = (OrgSetup) iteDept.next();
    		VacancyObj vacancyObjStore=new VacancyObj();
    		if(!map.get(obj.getCode()).equals(mapSecond.get(obj.getCode()))){
    			//strDeptName= mapData.get(obj.getCode()).toString() + " <i>(0)</i>";
    			strDeptName= mapData.get(obj.getCode()).toString();
    			vacancyObjStore.setNoOfPosition(0);
        		vacancyObjStore.setCarrerName(strDeptName);
        		
    			carrerCol.add(vacancyObjStore);
    		}	
    	}*/
    		
    }
	
	public String getDefaultTemplate() {
		return "recruit/carrerList";
	}
	
	//getter setter
	public Collection getCarrerCol() {
		return carrerCol;
	}

	public void setCarrerCol(Collection carrerCol) {
		this.carrerCol = carrerCol;
	}
	public String getEmployeeOpprMsg() {
		return employeeOpprMsg;
	}

	public void setEmployeeOpprMsg(String employeeOpprMsg) {
		this.employeeOpprMsg = employeeOpprMsg;
	}

	public String getNoOpprMsg() {
		return noOpprMsg;
	}

	public void setNoOpprMsg(String noOpprMsg) {
		this.noOpprMsg = noOpprMsg;
	}
	
}
