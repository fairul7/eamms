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
import com.tms.collab.isr.model.RequestObject;
import com.tms.collab.isr.report.model.ReportModel;
import com.tms.collab.isr.report.model.ReportObject;

public class CommonStatsReportForm extends Form{
	
	DatePopupField fromDate;
	DatePopupField toDate;
	
	Button btnSubmit;
	Button btnPrint;
	
	//test
	Button btnGenCsv;
	
	private Collection priorityCol;
	private Collection statusCol;
	private Collection requestTypeCol;
	private Collection deptCol;
	
	private int totalPriority;
	private int totalStatus;
	private int totalRequestType;
	private int totalDeptReq;
	private int totalDeptRec;
	
	public void init(){
		initForm();
		
		Calendar now = Calendar.getInstance();
		toDate.setDate(now.getTime());
		toDate.setSize("7");
		
		now.add(Calendar.MONTH, -1);
		fromDate.setDate(now.getTime());
		fromDate.setSize("7");
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
        	
        	evt.getRequest().getSession().setAttribute("reportData1", priorityCol);
    		evt.getRequest().getSession().setAttribute("reportData2", statusCol);
    		evt.getRequest().getSession().setAttribute("reportData3", deptCol);
    		evt.getRequest().getSession().setAttribute("reportData4", requestTypeCol);    		
    		evt.getRequest().getSession().setAttribute("reportData5", Integer.toString(totalPriority));
    		evt.getRequest().getSession().setAttribute("reportData6", Integer.toString(totalStatus));
    		evt.getRequest().getSession().setAttribute("reportData7", Integer.toString(totalRequestType));
    		evt.getRequest().getSession().setAttribute("reportData8", Integer.toString(totalDeptReq));
    		evt.getRequest().getSession().setAttribute("reportData9", Integer.toString(totalDeptRec));
    		
    		evt.getRequest().getSession().setAttribute("reportToDate", toDate);
    		evt.getRequest().getSession().setAttribute("reportFromDate", fromDate);
    		
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
    	totalPriority=0;
    	totalStatus=0;
    	totalRequestType=0;
    	totalDeptReq=0;
    	totalDeptRec=0;

    	priorityCol = model.getPriorityNoOfReq(fd.getTime(), td.getTime());   	
    	for(Iterator itr=priorityCol.iterator(); itr.hasNext();) {
    		ReportObject ro = (ReportObject) itr.next();
    		try{
    		totalPriority+=Integer.parseInt(ro.getNoOfReq());
    		}catch(Exception error) {
				Log.getLog(getClass()).error(error, error);
			}
    	}
    	statusCol = model.getStatusNoOfReq(fd.getTime(), td.getTime());
    	for(Iterator itr=statusCol.iterator(); itr.hasNext();) {
    		ReportObject ro = (ReportObject) itr.next();
    		try{
    			totalStatus+=Integer.parseInt(ro.getNoOfReq());
    		}catch(Exception error) {
				Log.getLog(getClass()).error(error, error);
			}
    	}
    	deptCol = model.getDepartmentNoOfReq(fd.getTime(), td.getTime());
    	for(Iterator itr=deptCol.iterator(); itr.hasNext();) {
    		ReportObject ro = (ReportObject) itr.next();
    		try{
    			totalDeptReq+=Integer.parseInt(ro.getReqDeptNoOfReq());
    			totalDeptRec+=Integer.parseInt(ro.getRecDeptNoOfReq());
    		}catch(Exception error) {
				Log.getLog(getClass()).error(error, error);
			}
    	}
    	requestTypeCol = model.getRequestTypeNoOfReq(fd.getTime(), td.getTime());
    	for(Iterator itr=requestTypeCol.iterator(); itr.hasNext();) {
    		ReportObject ro = (ReportObject) itr.next();
    		try{
    			totalRequestType+=Integer.parseInt(ro.getNoOfReq());
    		}catch(Exception error) {
				Log.getLog(getClass()).error(error, error);
			}
    	}
    	
    	//Set into session for CSV report
		evt.getRequest().getSession().setAttribute("reportData1", priorityCol);
		evt.getRequest().getSession().setAttribute("reportData2", statusCol);
		evt.getRequest().getSession().setAttribute("reportData3", deptCol);
		evt.getRequest().getSession().setAttribute("reportData4", requestTypeCol);
		evt.getRequest().getSession().setAttribute("reportData5", Integer.toString(totalPriority));
		evt.getRequest().getSession().setAttribute("reportData6", Integer.toString(totalStatus));
		evt.getRequest().getSession().setAttribute("reportData7", Integer.toString(totalRequestType));
		evt.getRequest().getSession().setAttribute("reportData8", Integer.toString(totalDeptReq));
		evt.getRequest().getSession().setAttribute("reportData9", Integer.toString(totalDeptRec));
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
		
		btnGenCsv = new Button("btnGenCsv", "Generate CSV");
		addChild(btnGenCsv);
		
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
		return "isr/commonStatsReportForm";
	}

	public Collection getDeptCol() {
		return deptCol;
	}

	public void setDeptCol(Collection deptCol) {
		this.deptCol = deptCol;
	}

	public Collection getPriorityCol() {
		return priorityCol;
	}

	public void setPriorityCol(Collection priorityCol) {
		this.priorityCol = priorityCol;
	}

	public Collection getRequestTypeCol() {
		return requestTypeCol;
	}

	public void setRequestTypeCol(Collection requestTypeCol) {
		this.requestTypeCol = requestTypeCol;
	}

	public Collection getStatusCol() {
		return statusCol;
	}

	public void setStatusCol(Collection statusCol) {
		this.statusCol = statusCol;
	}

	public Button getBtnGenCsv() {
		return btnGenCsv;
	}

	public void setBtnGenCsv(Button btnGenCsv) {
		this.btnGenCsv = btnGenCsv;
	}

	public int getTotalDeptRec() {
		return totalDeptRec;
	}

	public void setTotalDeptRec(int totalDeptRec) {
		this.totalDeptRec = totalDeptRec;
	}

	public int getTotalDeptReq() {
		return totalDeptReq;
	}

	public void setTotalDeptReq(int totalDeptReq) {
		this.totalDeptReq = totalDeptReq;
	}

	public int getTotalPriority() {
		return totalPriority;
	}

	public void setTotalPriority(int totalPriority) {
		this.totalPriority = totalPriority;
	}

	public int getTotalRequestType() {
		return totalRequestType;
	}

	public void setTotalRequestType(int totalRequestType) {
		this.totalRequestType = totalRequestType;
	}

	public int getTotalStatus() {
		return totalStatus;
	}

	public void setTotalStatus(int totalStatus) {
		this.totalStatus = totalStatus;
	}
	
	
}
