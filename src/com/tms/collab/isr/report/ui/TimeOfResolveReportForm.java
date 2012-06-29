package com.tms.collab.isr.report.ui;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.DateField;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import com.tms.collab.isr.model.RequestModel;
import com.tms.collab.isr.model.StatusObject;
import com.tms.collab.isr.report.model.ReportModel;
import com.tms.hr.orgChart.model.DepartmentCountryAssociativityObject;
import com.tms.hr.orgChart.model.OrgChartHandler;

public class TimeOfResolveReportForm extends Form{
	
	DatePopupField fromDate;
	DatePopupField toDate;
	
	Date from;
	Date to;
	
	SelectBox sbStatus;
	String statusId;
	String statusName;
	
	Button btnSubmit;
	Button btnPrint;
	
	private String departmentName;
	private String departmentId;
	private Collection result;
	
	public void init(){
		initForm();
		
		Calendar now = Calendar.getInstance();
		toDate.setDate(now.getTime());
		toDate.setSize("7");
		to = now.getTime();
		
		now.add(Calendar.MONTH, -1);
		fromDate.setDate(now.getTime());
		fromDate.setSize("7");
		from = now.getTime();
		
		List statusList = (List) sbStatus.getValue();
        if (statusList.size() > 0) {
        	statusId = (String) statusList.get(0);
        } else {
        	statusId = "";
        }
	}
	
	public void onRequest(Event evt) {
		//initForm();
		
		Application application = Application.getInstance();
		RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
		
		departmentName = requestModel.getDeptName(application.getCurrentUser().getId());
		
		getReport(evt);
	}
	
	public Forward onValidate(Event event) {
		String button = findButtonClicked(event);
        button = button == null ? "" : button;
        
        if (button.endsWith("btnSubmit")) {
        	getReport(event);
        	
        	to = toDate.getDate();
        	from = fromDate.getDate();
        	List statusList = (List) sbStatus.getValue();
            if (statusList.size() > 0) {
            	statusId = (String) statusList.get(0);
            } else {
            	statusId = "";
            }
        	
        	return new Forward("submit");
        	
        }else if(button.endsWith("btnPrint")){
        	from = fromDate.getDate();
        	to = toDate.getDate();
        	
        	List statusList = (List) sbStatus.getValue();
            if (statusList.size() > 0) {
            	statusId = (String) statusList.get(0);
            } else {
            	statusId = "";
            }
            
            if(statusId.equals("")){
            	statusName = "Closed and Resolved";
            }
            else{
            	Application application = Application.getInstance();
        		RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
        		Collection statusCols = requestModel.selectAllStatus();
        		for(Iterator i=statusCols.iterator(); i.hasNext(); ) {
        			StatusObject obj = (StatusObject) i.next();
        			if(obj.getStatusId().equals(statusId)){
        				statusName = obj.getStatusName();
        			}
        		}
            }
            
        	getReport(event);
        	
        	return new Forward("print");
        	
        }else{
        	return new Forward("error");
        	
        }
	}
	
	public void getReport(Event evt){
		OrgChartHandler orgChartModel = (OrgChartHandler) Application.getInstance().getModule(OrgChartHandler.class);
		ReportModel model = (ReportModel)Application.getInstance().getModule(ReportModel.class);
		
		DepartmentCountryAssociativityObject requesterDept = orgChartModel.getAssociatedCountryDept(Application.getInstance().getCurrentUser().getId());
    	
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
    	
    	String status = new String();
		List statusList = (List) sbStatus.getValue();
        if (statusList.size() > 0) {
        	status = (String) statusList.get(0);
        } else {
        	status = "";
        }
    	
    	result = model.getTimeOfResolveReqCol(fd.getTime(), td.getTime(), requesterDept.getAssociativityId(), status);
    	
    	//set into session variable to generate CSV report
		evt.getRequest().getSession().setAttribute("reportData", result);
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
		
		sbStatus = new SelectBox("sbStatus");
		sbStatus.addOption("", "--- Closed and Resolved ---");
		sbStatus.addOption(StatusObject.STATUS_ID_COMPLETED, "Resolved");
		sbStatus.addOption(StatusObject.STATUS_ID_CLOSE, "Closed");
		addChild(sbStatus);
		
		btnSubmit = new Button("btnSubmit", app.getMessage("isr.label.show"));
		addChild(btnSubmit);
		
		btnPrint = new Button("btnPrint", app.getMessage("isr.label.print"));
		addChild(btnPrint);
	}

	public DateField getFromDate() {
		return fromDate;
	}

	public void setFromDate(DatePopupField fromDate) {
		this.fromDate = fromDate;
	}

	public DateField getToDate() {
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
		return "isr/timeOfResolveReportForm";
	}

	public Collection getResult() {
		return result;
	}

	public void setResult(Collection result) {
		this.result = result;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public SelectBox getSbStatus() {
		return sbStatus;
	}

	public void setSbStatus(SelectBox sbStatus) {
		this.sbStatus = sbStatus;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
}
