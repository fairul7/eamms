package com.tms.collab.isr.report.ui;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import com.tms.collab.isr.model.StatusObject;
import com.tms.collab.isr.report.model.ReportModel;
import com.tms.collab.isr.report.model.ReportObject;
import com.tms.collab.isr.setting.model.ConfigDetailObject;
import com.tms.collab.isr.setting.model.ConfigModel;

public class CommonStatsPriorityReport extends Form{
	
	DatePopupField fromDate;
	DatePopupField toDate;
	
	Button btnSubmit;
	Button btnPrint;

	private Collection priority;
	private int newNo=0;
	private int progressNo=0;
	private int classificationNo=0;
	private int resolvedNo=0;
	private int closedNo=0;
	
	public void init(){
		initForm();
		
		Calendar now = Calendar.getInstance();
		toDate.setDate(now.getTime());
		
		now.add(Calendar.MONTH, -1);
		fromDate.setDate(now.getTime());
	}
	
	public void onRequest(Event evt) {
		//initForm();
		
		getReport(evt);
		
	}
	
	public Forward onValidate(Event evt) {
		String button = findButtonClicked(evt);
        button = button == null ? "" : button;
        
        if (button.endsWith("btnSubmit")) {
        	getReport(evt);
        	return new Forward("submit");
        	
        }else if(button.endsWith("btnPrint")){
        	//getReport();
        	return new Forward("print");
        	
        }else if(button.endsWith("btnGenCsv")){
        	getReport(evt);
        	
        	evt.getRequest().getSession().setAttribute("reportData1", priority);
    		evt.getRequest().getSession().setAttribute("reportData2", Integer.toString(newNo));
    		evt.getRequest().getSession().setAttribute("reportData3", Integer.toString(progressNo));
    		evt.getRequest().getSession().setAttribute("reportData4", Integer.toString(classificationNo));
    		evt.getRequest().getSession().setAttribute("reportData5", Integer.toString(resolvedNo));
    		evt.getRequest().getSession().setAttribute("reportData6", Integer.toString(closedNo));
    		evt.getRequest().getSession().setAttribute("reportToDate", toDate.getDate());
    		evt.getRequest().getSession().setAttribute("reportFromDate", fromDate.getDate());
        	return new Forward("genCsv");
        }
        else{
        	return new Forward("error");
        	
        }
	}
	
	public void getReport(Event evt){
		ReportModel model = (ReportModel)Application.getInstance().getModule(ReportModel.class);
    	
    	Calendar fd = Calendar.getInstance();
    	if(fromDate.getValue().toString().trim().length() > 0){
    		fd = fromDate.getCalendar();
    		fd.set(Calendar.HOUR, 0);
    		fd.set(Calendar.MINUTE, 0);
    	}
    	Calendar td = Calendar.getInstance();
    	if(toDate.getDate() != null){
    		td = toDate.getCalendar();
    		td.set(Calendar.HOUR, 23);
    		td.set(Calendar.MINUTE, 59);
    	}

    	newNo=0;
    	progressNo=0;
    	classificationNo=0;
    	resolvedNo=0;
    	closedNo=0;
    	
    	ConfigModel configModel = (ConfigModel) Application.getInstance().getModule(ConfigModel.class); 
		priority = configModel.getConfigDetailsByType(ConfigDetailObject.PRIORITY, null);
		for(Iterator i=priority.iterator(); i.hasNext();) {
			ConfigDetailObject config = (ConfigDetailObject) i.next();
			config.setNewTotalReq(model.getCommonStatsPriority(fd.getTime(), td.getTime(),StatusObject.STATUS_ID_NEW, config.getConfigDetailName()));
			newNo+=config.getNewTotalReq();
			config.setProgressTotalReq(model.getCommonStatsPriority(fd.getTime(), td.getTime(),StatusObject.STATUS_ID_IN_PROGRESS, config.getConfigDetailName()));
			progressNo+=config.getProgressTotalReq();
			config.setClarificationTotalReq(model.getCommonStatsPriority(fd.getTime(), td.getTime(),StatusObject.STATUS_ID_CLARIFICATION, config.getConfigDetailName()));
			classificationNo+=config.getClarificationTotalReq();
			config.setResolvedTotalReq(model.getCommonStatsPriority(fd.getTime(), td.getTime(),StatusObject.STATUS_ID_COMPLETED, config.getConfigDetailName()));
			resolvedNo+=config.getResolvedTotalReq();
			config.setClosedTotalReq(model.getCommonStatsPriority(fd.getTime(), td.getTime(),StatusObject.STATUS_ID_CLOSE, config.getConfigDetailName()));
			closedNo+=config.getClosedTotalReq();
		}
		    	
    	
    	//Set into session for CSV report
		evt.getRequest().getSession().setAttribute("reportData1", priority);
		evt.getRequest().getSession().setAttribute("reportData2", Integer.toString(newNo));
		evt.getRequest().getSession().setAttribute("reportData3", Integer.toString(progressNo));
		evt.getRequest().getSession().setAttribute("reportData4", Integer.toString(classificationNo));
		evt.getRequest().getSession().setAttribute("reportData5", Integer.toString(resolvedNo));
		evt.getRequest().getSession().setAttribute("reportData6", Integer.toString(closedNo));
		evt.getRequest().getSession().setAttribute("reportToDate", toDate.getDate());
		evt.getRequest().getSession().setAttribute("reportFromDate", fromDate.getDate());
	}
	
	public void initForm(){
		setMethod("POST");
		removeChildren();
		
		Application app = Application.getInstance();
		
		fromDate = new DatePopupField("fromDate");
		fromDate.setOptional(true);
		addChild(fromDate);
		
		toDate = new DatePopupField("toDate");
		toDate.setOptional(true);
		addChild(toDate);
		
		btnSubmit = new Button("btnSubmit", app.getMessage("isr.label.show"));
		addChild(btnSubmit);
		
		btnPrint = new Button("btnPrint", app.getMessage("isr.label.print"));
		addChild(btnPrint);
		
	}

	public DatePopupField getFromDate() {
		return fromDate;
	}

	public void setFromDate(DatePopupField fromDate) {
		this.fromDate = fromDate;
	}

	public DatePopupField getToDate() {
		return toDate;
	}

	public void setToDate(DatePopupField toDate) {
		this.toDate = toDate;
	}

	public Button getBtnPrint() {
		return btnPrint;
	}

	public void setBtnPrint(Button btnPrint) {
		this.btnPrint = btnPrint;
	}

	public Button getBtnSubmit() {
		return btnSubmit;
	}

	public void setBtnSubmit(Button btnSubmit) {
		this.btnSubmit = btnSubmit;
	}

	public String getDefaultTemplate(){
		return "isr/commonStatsPriorityReportForm";
	}

	public Collection getPriority() {
		return priority;
	}

	public void setPriority(Collection priority) {
		this.priority = priority;
	}

	public int getClassificationNo() {
		return classificationNo;
	}

	public void setClassificationNo(int classificationNo) {
		this.classificationNo = classificationNo;
	}

	public int getClosedNo() {
		return closedNo;
	}

	public void setClosedNo(int closedNo) {
		this.closedNo = closedNo;
	}

	public int getNewNo() {
		return newNo;
	}

	public void setNewNo(int newNo) {
		this.newNo = newNo;
	}

	public int getProgressNo() {
		return progressNo;
	}

	public void setProgressNo(int progressNo) {
		this.progressNo = progressNo;
	}

	public int getResolvedNo() {
		return resolvedNo;
	}

	public void setResolvedNo(int resolvedNo) {
		this.resolvedNo = resolvedNo;
	}	
}
