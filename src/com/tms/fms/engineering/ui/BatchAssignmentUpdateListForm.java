package com.tms.fms.engineering.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.TimeField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.FacilitiesCoordinatorModule;
import com.tms.fms.util.WidgetUtil;

/**
 * 
 * @author fahmi
 *
 */
public class BatchAssignmentUpdateListForm extends Form {
	
	private TextField tfRequestId;
	private DatePopupField dtStart;
	private DatePopupField dtEnd;
	private SelectBox department;
	private SelectBox program;
	private DatePopupField completionDate[];
	private TimeField completionTime[];
	private Label lbCompletionDate[];
	private Label lbCompletionTime[];
	
	protected Button back;
	protected Button update;
	protected Panel buttonPanel;
	protected Panel pnType;
	
	private String requestId;
	private String requestTitle;
	private String startDate;
	private String endDate;
	private String dept;
	private String prog;
	
	private String tempRequestId;
	
	protected EngineeringRequest request;	
	
	private Collection assignments = new ArrayList();
	
	public void onRequest(Event event) {
		this.setInvalid(false);
		assignments = new ArrayList(); 	
		
		tempRequestId = requestId!=null?requestId:"";
		
		initForm();
	}
	
	public void populateButtons(boolean isUpdate) {
		Application app = Application.getInstance();
		buttonPanel = new Panel("panel");
		update = new Button("update", app.getMessage("fms.facility.update"));
		back = new Button("back", app.getMessage("fms.facility.back"));
		if (isUpdate) {
			buttonPanel.addChild(update);
		}
		buttonPanel.addChild(back);
		addChild(buttonPanel);
	}

	public void initForm() {
		setMethod("post");
		setColumns(2);
		Application app = Application.getInstance();
		FacilitiesCoordinatorModule mod = (FacilitiesCoordinatorModule) app.getModule(FacilitiesCoordinatorModule.class);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		boolean update = false;
		
		Date timingStart = new Date();
		Log.getLog(getClass()).debug("start initForm() startDate=" + startDate + " endDate=" + endDate);
		
		Date stDate = null;
		Date edDate = null;
		
		if (startDate!=null) {
			try {
				stDate = new SimpleDateFormat("dd-MM-yyyy").parse(startDate);
			} catch (ParseException e) {
				Log.getLog(getClass()).error(e.toString(), e);
			}
		}
		
		if (endDate != null) {
			try {
				edDate = new SimpleDateFormat("dd-MM-yyyy").parse(endDate);
			} catch (ParseException e){
				Log.getLog(getClass()).error(e.toString(), e);
			}
		}
		
		try {
			assignments = mod.getAllRequestForBatch(requestId, dept, prog, stDate, edDate);
			Log.getLog(getClass()).debug("assignments.size(): " + assignments.size());
			
			completionDate = new DatePopupField[assignments.size()];
			lbCompletionDate = new Label[assignments.size()];
			completionTime = new TimeField[assignments.size()];
			lbCompletionTime = new Label[assignments.size()];

			if (assignments!=null){
				int count=0;
				for (Iterator it = assignments.iterator(); it.hasNext();){
					HashMap map=(HashMap)it.next();						
					Date cDate = null;					
					
					completionDate[count] = new DatePopupField("completionDate"+count);
					completionDate[count].setTemplate("extDatePopupField");
					lbCompletionDate[count] = new Label("lbCompletionDate"+count);
					
					completionDate[count].setOptional(true);
					completionDate[count].setSize("10");
					completionDate[count].setMaxlength("10");
					
					if (EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_COMPLETED.equals((String)map.get("status"))){
						cDate = (Date)map.get("completionDate");
						completionDate[count].setHidden(true);
						completionDate[count].setInvalid(false);
						lbCompletionDate[count].setText(sdf.format(cDate));
					} else {
						update = true;
						lbCompletionDate[count].setText("");
					}
					completionDate[count].setDate(cDate);
					addChild(completionDate[count]);
					addChild(lbCompletionDate[count]);
					
					lbCompletionTime[count] = new Label("lbCompletionTime"+count); 
					                 
					if (EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_COMPLETED.equals((String)map.get("status"))){
						String cTime = hourToString(cDate.getHours()) + ":" + minuteToString(cDate.getMinutes());
						lbCompletionTime[count].setText(cTime);	
						
					} else {
						completionTime[count] = new TimeField("completionTime"+count);	
						WidgetUtil.populateTimeField(completionTime[count], "00:00");
						lbCompletionTime[count].setText("");
						addChild(completionTime[count]);
					}
					
					addChild(lbCompletionTime[count]);
					
					requestTitle = (String)map.get("title");
					
					count++;
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}     
		
		populateButtons(update);
		
		Date timingEnd = new Date();
		long elapsed = timingEnd.getTime() - timingStart.getTime();
		Log.getLog(getClass()).info("end initForm() (elapsed: " + elapsed + " ms)");
	}	
	
	public Forward onSubmit(Event evt) {
	    String buttonName = findButtonClicked(evt);
	    kacang.ui.Forward result = super.onSubmit(evt);
	    if (buttonName != null && back.getAbsoluteName().equals(buttonName)) {
	    	init();
	    	return new Forward(Form.CANCEL_FORM_ACTION, "batchAssignmentUpdate.jsp" , true);
	    }else if (buttonName != null && update.getAbsoluteName().equals(buttonName)) {
	    	return result;
	    }
		return result;
	}
	
	public Forward onValidate(Event event) {
		try {
			if (assignments!=null){
				Application app = Application.getInstance();
//				FacilitiesCoordinatorModule mod = (FacilitiesCoordinatorModule) app.getModule(FacilitiesCoordinatorModule.class);
				EngineeringModule mod = (EngineeringModule)app.getModule(EngineeringModule.class);
				EngineeringRequest er = new EngineeringRequest();
				
				int i = 0;
				for (Iterator it = assignments.iterator(); it.hasNext();){
					HashMap map=(HashMap)it.next();			
					
					if (completionDate[i].getDate() != null && 
							!EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_COMPLETED.equals((String)map.get("status"))){
						Date cDate = completionDate[i].getDate();
						cDate.setHours(completionTime[i].getHour());
						cDate.setMinutes(completionTime[i].getMinute());
						
						er.setAssignmentId((String)map.get("assignmentId"));
						er.setCompletionDate(cDate);
						
						mod.completeAssignment(er);
						
						// check completed request
						EngineeringRequest erC = mod.getAssignment(er.getAssignmentId());
						
						if (erC!=null) {
							mod.updateRequestStatus(erC.getRequestId());
						}
					}
					i++;
				}			
			}
			
			String fwd = "batchAssignmentUpdateList.jsp?requestId=" + tempRequestId + "&startDate=" + startDate + "&endDate="+endDate;
			fwd += "&dept="+dept+"&prog="+prog;
			
			return new Forward("SUBMIT2", fwd, true);			
		} catch (Exception e) {
			
			Log.getLog(getClass()).error(e.toString()); 
			return new Forward("FAILED");
		} 
	}
	
	public String getDefaultTemplate(){
		return "fms/engineering/batchassignmentupdatelisttemp";
	}
	
	public String hourToString(int hour){
		String H = "00";
		if (hour >= 0 && hour < 24) {
			if (hour < 10) {
				H = "0" + hour;
			} else {
				H = hour + "";
			}
		}
		return H;
	}
	
	public String minuteToString(int minute){
		String M = "00";
		if (minute >= 0 && minute <= 60) {
			if (minute < 10) {
				M = "0" + minute;
			} else {
				M = minute + "";
			}
		}
		return M;
	}
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public DatePopupField getDtStart() {
		return dtStart;
	}

	public void setDtStart(DatePopupField dtStart) {
		this.dtStart = dtStart;
	}

	public DatePopupField getDtEnd() {
		return dtEnd;
	}

	public void setDtEnd(DatePopupField dtEnd) {
		this.dtEnd = dtEnd;
	}

	public Panel getButtonPanel() {
		return buttonPanel;
	}

	public void setButtonPanel(Panel buttonPanel) {
		this.buttonPanel = buttonPanel;
	}

	public Panel getPnType() {
		return pnType;
	}

	public void setPnType(Panel pnType) {
		this.pnType = pnType;
	}

	public Collection getAssignments() {
		return assignments;
	}

	public void setAssignments(Collection assignments) {
		this.assignments = assignments;
	}

	public TextField getTfRequestId() {
		return tfRequestId;
	}

	public void setTfRequestId(TextField tfRequestId) {
		this.tfRequestId = tfRequestId;
	}

	public SelectBox getDepartment() {
		return department;
	}

	public void setDepartment(SelectBox department) {
		this.department = department;
	}

	public SelectBox getProgram() {
		return program;
	}

	public void setProgram(SelectBox program) {
		this.program = program;
	}

	public DatePopupField[] getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(DatePopupField[] completionDate) {
		this.completionDate = completionDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getProg() {
		return prog;
	}

	public void setProg(String prog) {
		this.prog = prog;
	}

	public TimeField[] getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(TimeField[] completionTime) {
		this.completionTime = completionTime;
	}

	public String getRequestTitle() {
		return requestTitle;
	}

	public void setRequestTitle(String requestTitle) {
		this.requestTitle = requestTitle;
	}

	public Label[] getLbCompletionDate() {
		return lbCompletionDate;
	}

	public void setLbCompletionDate(Label[] lbCompletionDate) {
		this.lbCompletionDate = lbCompletionDate;
	}

	public Label[] getLbCompletionTime() {
		return lbCompletionTime;
	}

	public void setLbCompletionTime(Label[] lbCompletionTime) {
		this.lbCompletionTime = lbCompletionTime;
	}

	public String getTempRequestId() {
		return tempRequestId;
	}

	public void setTempRequestId(String tempRequestId) {
		this.tempRequestId = tempRequestId;
	}
	
	
}
