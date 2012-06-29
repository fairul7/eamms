package com.tms.fms.transport.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorMessage;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.util.DateDiffUtil;


public class BulkAssignmentUpdateForm extends Form {
	
	private TextField vehicleNum;
	private DatePopupField dtStart;
	private DatePopupField dtEnd;
	private ValidatorMessage vmsg_dtEnd;
	private SelectBox department;
	private SelectBox program;
	private DatePopupField completionDate[];
	
	protected Button cancel;
	protected Button search;
	protected Panel buttonPanel;
	protected Panel pnType;
	
	protected String requestId;
	protected EngineeringRequest request;	
	
	private Collection assignments = new ArrayList();
	
	public void onRequest(Event event) {
		this.setInvalid(false);
		assignments = new ArrayList(); 
		init();
	}
	
	public void populateButtons() {
		Application app = Application.getInstance();
		buttonPanel = new Panel("panel");
		search = new Button("search", app.getMessage("fms.facility.search"));
		cancel = new Button("cancel", app.getMessage("fms.facility.cancel"));
		buttonPanel.addChild(search);
		buttonPanel.addChild(cancel);
		addChild(new Label(("tupuku")));
		addChild(buttonPanel);
	}

	public void init() {
		setMethod("post");
		setColumns(2);
		
		vehicleNum = new TextField("vehicleNum");
		addChild(vehicleNum);
	
		dtStart = new DatePopupField("dtStart");
    	dtStart.setFormat("dd-MM-yyyy");
    	dtStart.setDate(new Date());
    	dtStart.setOptional(false);
		addChild(dtStart);
	
		dtEnd = new DatePopupField("dtEnd");
    	dtEnd.setFormat("dd-MM-yyyy");
    	dtEnd.setDate(new Date());
    	dtEnd.setOptional(false);
		addChild(dtEnd);
		
		vmsg_dtEnd = new ValidatorMessage("vmsg_dtEnd");
		dtEnd.addChild(vmsg_dtEnd);
		
		department = new SelectBox("department");
		Map cmap = null;
        try {
            cmap = ((FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class)).getFMSDepartments();
        }
        catch (DaoException e) {
            Log.getLog(getClass()).error(e);
        }
        department.addOption("-1", "");
        for (Iterator i = cmap.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();
            department.addOption(key, (String) cmap.get(key));
        }
        addChild(department);
        
        program = new SelectBox("program");
        program.addOption("-1", "");
        
        Collection listx = new ArrayList();        
        SetupModule mod = (SetupModule)Application.getInstance().getModule(SetupModule.class);
        try {
        	listx = mod.selectProgram("", "1" , null, false, 0, -1);
        	
        	for (Iterator iList = listx.iterator(); iList.hasNext();){
        		HashMap mapx = (HashMap) iList.next();
        		program.addOption((String) mapx.get("programId"), (String)mapx.get("programName"));
        	}
        }
        catch (Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
        addChild(program);       
		
		populateButtons();
		
	}	
	
	public Forward onValidate(Event event) {
		try {
			String selectedDept = "";
			String selectedProg = "";
			assignments = new ArrayList(); 
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

			requestId 	= (String) vehicleNum.getValue();
			
			Date stDate	= dtStart.getDate();
			String startDate = sdf.format(stDate);
			Date edDate	= dtEnd.getDate();
			String endDate = sdf.format(edDate);
			
			// compare date checking : start v.s. end
			if(stDate.after(edDate)){
				dtEnd.setInvalid(true);
				this.setInvalid(true);
				return new Forward("FAILED");
			} else {
				// check: day range not more than 31 days
				long diff = DateDiffUtil.dayDiff(stDate, edDate);
				if (diff >= 31) {
					vmsg_dtEnd.showError("Date Range more than 31 days");
					this.setInvalid(true);
					return new Forward("FAILED");
				}
			}
			
			selectedDept  = getSelectBoxValue(department);
			selectedProg  = getSelectBoxValue(program);
			
			String fwd = "bulkAssignmentUpdateList.jsp?requestId=" + requestId + "&startDate=" + startDate + "&endDate="+endDate;
			fwd += "&dept="+selectedDept+"&prog="+selectedProg;

			return new Forward("SUBMIT", fwd, true);
			
		} catch (Exception e) {
			
			Log.getLog(getClass()).error(e.toString()); 
			return new Forward("FAILED");
		} 
	}
	
	private String getSelectBoxValue(SelectBox sb) {
	    if (sb != null) {
	    	Map selected = sb.getSelectedOptions();
	    	if (selected.size() == 1) {
	    		return (String)selected.keySet().iterator().next();
	    	}
	    }
	    return null;
	}
	
	public String getDefaultTemplate(){
		return "fms/transport/bulkassignmentupdatetemp";
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

	
	public TextField getVehicleNum() {
		return vehicleNum;
	}

	public void setVehicleNum(TextField vehicleNum) {
		this.vehicleNum = vehicleNum;
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
	
	
}
