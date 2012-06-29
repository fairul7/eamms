package com.tms.fms.abw.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;


import com.tms.crm.sales.misc.DateUtil;
import com.tms.elearning.core.ui.ValidatorNotEmpty;
import com.tms.fms.abw.model.AbwModule;
import com.tms.fms.abw.model.AbwTransferCostObject;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.transport.model.TransportModule;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.TextBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

public class BackdatedExtraction extends Form {
	
	private DatePopupField dateFrom;
	private DatePopupField dateTo;
	private TextBox processedRequestId;
	private String mode;
	static final long MILLIS_IN_A_DAY = 1000*60*60*24; 
	
	public void init() {
		setMethod("POST");
		setColumns(2);
		
		addChild(new Label("lblDateFrom","Date From : "));
		dateFrom = new DatePopupField("dateFrom");
		dateFrom.addChild(new ValidatorNotEmpty("abc"));
		addChild(dateFrom);
		
		addChild(new Label("lblDateTo","Date To : "));
		dateTo = new DatePopupField("dateTo");
		dateTo.addChild(new ValidatorNotEmpty("abc1"));		
		addChild(dateTo);
		
		addChild(new Label("lblProcessedReqId","Processed Request Id : "));
		processedRequestId = new TextBox("processedRequestId");
		processedRequestId.setTemplate("textboxReadonly");
		addChild(processedRequestId);
		
		addChild(new Label("lblZero",""));
		Button submit = new Button("submit","Submit");
		addChild(submit);
		
		
	}
	
	public void onRequest(Event evt) {
		init();
	}
	
	public Forward onSubmit(Event evt) {
		Forward fwd = super.onSubmit(evt);
		if(dateFrom.getDate() !=null){
			if(dateFrom.getDate().after(dateTo.getDate())){
				setInvalid(true);
				return new Forward("dateNotValid");
			}
			
			
			if(dateTo.getDate().after(new Date(new java.util.Date().getTime() - MILLIS_IN_A_DAY))){
				dateTo.setInvalid(true);
				setInvalid(true);
				return new Forward("noToday");
			}
		}
		
		return fwd;
	}
	
	
	public Forward onValidate(Event evt) {
		if("TransportTransferCost".equals(mode)){
			processTransportTransferCostExtraction();			
			return new Forward("TTCSuccess");
		}
		
		if("TransferCost".equals(mode)){
			processTransferCost();			
			return new Forward("TCSuccess");
		}
		
		if("TransportRequest".equals(mode)){
			updateTransportRequestCost();
		}
		
		return new Forward("success");
	}
	
	private void processTransportTransferCostExtraction(){
		Application app = Application.getInstance();
		TransportModule tModule = (TransportModule) app.getModule(TransportModule.class);
		AbwModule aModule = (AbwModule) app.getModule(AbwModule.class);
		
		Collection<AbwTransferCostObject> collAbw = tModule.getInfoToCreateAbwTransferCostObjects(DateUtil.getDateFromStartTime(dateFrom.getDate()), DateUtil.getDateWithEndTime(dateTo.getDate()));
		if(collAbw != null && collAbw.size() > 0){
			UuidGenerator uuid = UuidGenerator.getInstance();
			for(AbwTransferCostObject object : collAbw){
				object.setUniqueId(uuid.getUuid());
				String cost = "0";
				try{
					cost = tModule.getAbwCost(object.getRequestId(), object.getCategoryId(), object.getType());
				}catch(Exception er){
					
				}
				object.setCost(cost);
				object.setCreatedBy(SetupModule.FMS_SYSTEM_ADMIN);
			}
			Collection<AbwTransferCostObject> objectsInserted = aModule.insertAbwTransferCostBackDated(collAbw);
			Log.getLog(getClass()).info("Running Transport Transfer Cost extraction process from "  + dateFrom.getDayOfMonth() + " to " + dateTo.getDate());
			
			String strProcessedRequestId ="";
			int countP=0;
			for(AbwTransferCostObject object1 : objectsInserted){
				strProcessedRequestId +=object1.getRequestId() + "\n";
				countP++;
			}
			strProcessedRequestId += countP +" record(s) processed...";
			processedRequestId.setValue(strProcessedRequestId);
		}
		
	}
	
	private void processTransferCost(){
		EngineeringModule em = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
				
		
		ArrayList statusArr = new ArrayList();
		statusArr.add(EngineeringModule.ASSIGNMENT_STATUS);
		statusArr.add(EngineeringModule.FULFILLED_STATUS);
		statusArr.add(EngineeringModule.CLOSED_STATUS);
		statusArr.add(EngineeringModule.LATE_STATUS);
		
		Collection<DefaultDataObject> objectsInserted = em.pushTransferCostReqBackdated(DateUtil.getDateFromStartTime(dateFrom.getDate()), DateUtil.getDateWithEndTime(dateTo.getDate()), statusArr);
		Log.getLog(getClass()).info("Running Transfer Cost extraction process from "  + dateFrom.getDayOfMonth() + " to " + dateTo.getDate());
		
		String strProcessedRequestId ="";
		int countP=0;
		for(DefaultDataObject object1 : objectsInserted){
			strProcessedRequestId += object1.getProperty("requestId") + "\n";
			countP++;
		}
		strProcessedRequestId += countP +" record(s) processed...";
		processedRequestId.setValue(strProcessedRequestId);
		
	}
	
	public void updateTransportRequestCost(){
		TransportModule mod = (TransportModule)Application.getInstance().getModule(TransportModule.class);
		Collection<DefaultDataObject> objectsInserted  = mod.updateTransportRequestCost(dateFrom.getDate(), dateTo.getDate());
		Log.getLog(getClass()).info("Updating request transport cost from " + dateFrom.getDayOfMonth() + " to " + dateTo.getDate());
		
		String strProcessedRequestId ="";
		int countP=0;
		for(DefaultDataObject object1 : objectsInserted){
			strProcessedRequestId += object1.getId() + "\n";
			countP++;
		}
		strProcessedRequestId += countP +" record(s) processed...";
		processedRequestId.setValue(strProcessedRequestId);
	}


	public String getMode() {
		return mode;
	}


	public void setMode(String mode) {
		this.mode = mode;
	}
	
	

}
