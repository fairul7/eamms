package com.tms.fms.transport.ui;

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

import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.AssignmentObject;
import com.tms.fms.transport.model.SetupObject;
import com.tms.fms.transport.model.TransportDao;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.util.WidgetUtil;


public class BulkAssignmentUpdateListForm extends Form {
	
	private TextField vehicleNum;	
	private SelectBox department;
	private SelectBox program;
	private DatePopupField checkOutDate[];
	private DatePopupField checkInDate[];
	
	private Label lbCheckOutDate[];
	private Label lbCompletionDate[];
	//private Label lbCompletionTime[];	
	private Label lbCheckOutTime[];
	private Label lbCheckInTime[];
	
	private TimeField checkOutTime[];
	private TimeField checkInTime[];
	private SelectBox petrolCard[];
	private TextField meterStart[];
	private TextField meterEnd[];
	private TextField mileage[];
	private TextField remarks[];
	
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
	
	protected EngineeringRequest request;	
	
	private Collection assignments = new ArrayList();
	
	public void onRequest(Event event) {
		this.setInvalid(false);
		assignments = new ArrayList(); 	
		
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
		
		Date timingStart = new Date();
		Log.getLog(getClass()).debug("start initForm() startDate=" + startDate + " endDate=" + endDate);
		
		Application app = Application.getInstance();
		TransportModule mod = (TransportModule) app.getModule(TransportModule.class);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat stf = new SimpleDateFormat("h:mm a");
		SimpleDateFormat stf2 = new SimpleDateFormat("h:mm");
		boolean update = false;
		
		Date stDate = null;
		Date edDate = null;
		
		if (startDate!=null) {
			try {
				stDate = new SimpleDateFormat("dd-MM-yyyy").parse(startDate);
			} catch (ParseException e) {
			}
		}
		
		if (endDate != null) {
			try {
				edDate = new SimpleDateFormat("dd-MM-yyyy").parse(endDate);
			} catch (ParseException e){
			}
		}
		
		try {
			assignments = mod.getAllRequestForBatch(requestId, dept, prog, stDate, edDate);
			Log.getLog(getClass()).debug("assignments.size(): " + assignments.size());
			
			checkOutDate = new DatePopupField[assignments.size()];
			checkInDate = new DatePopupField[assignments.size()];
			lbCheckOutDate = new Label[assignments.size()];
			lbCompletionDate = new Label[assignments.size()];						
			//lbCompletionTime = new Label[assignments.size()];			
			lbCheckOutTime = new Label[assignments.size()];
			lbCheckInTime = new Label[assignments.size()];
			
			checkOutTime = new TimeField[assignments.size()];
			checkInTime = new TimeField[assignments.size()];
			petrolCard = new SelectBox[assignments.size()];
			meterStart = new TextField[assignments.size()];			
			meterEnd = new TextField[assignments.size()];
			mileage = new TextField[assignments.size()];
			remarks = new TextField[assignments.size()];

			if (assignments!=null){
				int count=0;
				Collection colPetrol = new ArrayList();

				TransportDao dao = (TransportDao) Application.getInstance().getModule(TransportModule.class).getDao();
				try {
					colPetrol = dao.selectSetupObject("fms_tran_petrolcard", null, "-1", null, false, 0, -1);					
			    } catch (DaoException e) {
			        Log.getLog(getClass()).error(e.toString());	        
			    }		
			    
				for (Iterator it = assignments.iterator(); it.hasNext();){
					HashMap map=(HashMap)it.next();						
					Date cinDate = (Date)map.get("checkin_date");
					Date coutDate = (Date)map.get("checkout_date");
					
					if (SetupModule.CLOSED_STATUS.equals((String)map.get("status")) || 
							SetupModule.UNFULFILLED_STATUS.equals((String)map.get("status"))) {						

						lbCompletionDate[count] = new Label("lbCompletionDate"+count);
						lbCheckInTime[count] = new Label("lbCheckInTime"+count);
						
						if (cinDate != null) {
							try {
								lbCompletionDate[count].setText(sdf.format(cinDate));
								lbCheckInTime[count].setText(stf.format(cinDate));
							} catch (Exception e) {
								Log.getLog(getClass()).error(e.getMessage(), e);
							}
						}
						addChild(lbCompletionDate[count]);
						addChild(lbCheckInTime[count]);
						
						lbCheckOutDate[count] = new Label("lbCheckOutDate"+count);
						lbCheckOutTime[count] = new Label("lbCheckOutTime"+count);
						if (coutDate != null) {
							lbCheckOutDate[count].setText(sdf.format(coutDate));
							lbCheckOutTime[count].setText(stf.format(coutDate));
						}
						addChild(lbCheckOutDate[count]);
						addChild(lbCheckOutTime[count]);
					} else {
						update = true;
						
						checkOutDate[count] = new DatePopupField("checkOutDate"+count);
						checkOutDate[count].setTemplate("extDatePopupField");
						checkOutDate[count].setOptional(true);
						checkOutDate[count].setSize("8");
						checkOutDate[count].setMaxlength("10");
						checkOutDate[count].setDate(cinDate);
						addChild(checkOutDate[count]);
						
						checkInDate[count] = new DatePopupField("checkInDate"+count);
						checkInDate[count].setTemplate("extDatePopupField");
						checkInDate[count].setOptional(true);
						checkInDate[count].setSize("8");
						checkInDate[count].setMaxlength("10");
						checkInDate[count].setDate(cinDate);
						addChild(checkInDate[count]);
						
						checkInTime[count] = new TimeField("checkInTime"+count);
						WidgetUtil.populateTimeField(checkInTime[count], "00:00");
						addChild(checkInTime[count]);
						
						String coTime = "00:00";
						if (coutDate != null) {
							try {
								coTime = stf2.format(coutDate);
								checkOutDate[count].setDate(coutDate);
							} catch (Exception e) {
								Log.getLog(getClass()).error(e.getMessage(), e);
							}
						}
						
						checkOutTime[count] = new TimeField("checkOutTime"+count);
						WidgetUtil.populateTimeField(checkOutTime[count], coTime);
						addChild(checkOutTime[count]);
						
						petrolCard[count] = new SelectBox("petrolCard"+count);
						petrolCard[count].addOption("-", "Select");
					    
					    for(Iterator ip = colPetrol.iterator(); ip.hasNext(); ){
					    	SetupObject so = (SetupObject) ip.next();
					    	petrolCard[count].addOption(so.getName(), so.getName());	
					    	
					    	if (so.getName().equals((String)map.get("petrolCard"))) {
					    		petrolCard[count].addSelectedOption((String)map.get("petrolCard"));
					    	}
					    }
					    
					    addChild(petrolCard[count]);
					    
					    meterStart[count] = new TextField("meterStart"+count); 		
						meterStart[count].setSize("6");
						if (map.get("meterStart") != null) {
					    	meterStart[count].setValue(map.get("meterStart"));
					    }		
						addChild(meterStart[count]);
						
						meterEnd[count] = new TextField("meterEnd"+count);
						meterEnd[count].setSize("6");
						addChild(meterEnd[count]);
						
						remarks[count] = new TextField("remarks"+count);
						remarks[count].setSize("10");	
						if (map.get("remarks") != null) { 
							remarks[count].setValue(map.get("remarks"));
						}
						addChild(remarks[count]);
					}
					
					count++;
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}     
		
		populateButtons(update);
		
		Date timingEnd = new Date();
		long elapsed = timingEnd.getTime() - timingStart.getTime();
		Log.getLog(getClass()).debug("end initForm() (elapsed: " + elapsed + " ms)");
	}	
	
	public Forward onSubmit(Event evt) {
	    String buttonName = findButtonClicked(evt);
	    kacang.ui.Forward result = super.onSubmit(evt);
	    if (buttonName != null && back.getAbsoluteName().equals(buttonName)) {
	    	init();
	    	return new Forward(Form.CANCEL_FORM_ACTION, "bulkAssignment.jsp" , true);
	    }else if (buttonName != null && update.getAbsoluteName().equals(buttonName)) {
	    	return result;
	    }
		return result;
	}
	
	public Forward onValidate(Event event) {
		TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);		
		
		try {
			if (assignments!=null){
				Collection col = new ArrayList();
				
				int i = 0;
				for (Iterator it = assignments.iterator(); it.hasNext();){
					HashMap map = (HashMap) it.next();	
					
					AssignmentObject AO = new AssignmentObject();
					AO.setAssgId((String) map.get("id"));		
					AO.setStatus((String) map.get("status"));
					
					if (!SetupModule.CLOSED_STATUS.equals((String)map.get("status"))
							&& !SetupModule.UNFULFILLED_STATUS.equals((String)map.get("status"))){
						
						// start : check-in date validation						
						if(checkOutDate[i].getDate() != null && checkInDate[i].getDate() != null){			
							Calendar calOut = Calendar.getInstance();
							calOut.setTime(checkOutDate[i].getDate());
							calOut.set(Calendar.HOUR_OF_DAY, checkOutTime[i].getHour());
							calOut.set(Calendar.MINUTE, checkOutTime[i].getMinute());
							AO.setCheckout_date(calOut.getTime());
							
							Calendar calIn = Calendar.getInstance();
							calIn.setTime(checkInDate[i].getDate());
							calIn.set(Calendar.HOUR_OF_DAY, checkInTime[i].getHour());
							calIn.set(Calendar.MINUTE, checkInTime[i].getMinute());
							AO.setCheckin_date(calIn.getTime());
							
							if (AO.getCheckout_date().after(AO.getCheckin_date())) {
								checkOutTime[i].setInvalid(true);
								checkOutDate[i].setInvalid(true);
								checkInTime[i].setInvalid(true);
								checkInDate[i].setInvalid(true);
								return new Forward("invalid-date");
							}
						
						
							// start : petrol card validation
							if (petrolCard[i].getSelectedOptions() != null && !"-".equals((String) petrolCard[i].getSelectedOptions().keySet().iterator().next())) {
								AO.setPetrolCard((String)petrolCard[i].getSelectedOptions().keySet().iterator().next());
							} else {
								petrolCard[i].setInvalid(true);
								return new Forward("invalid-petrol-card");
							}
							// end : petrol card validation
							
							// start : meter start/end
							String meterS = (String) meterStart[i].getValue();
							String meterE = (String) meterEnd[i].getValue();	
							
							if (meterS != null && meterE != null) {
								try {
									if (Long.parseLong(meterS) > Long.parseLong(meterE)) {
										meterStart[i].setInvalid(true);
										meterEnd[i].setInvalid(true);
										return new Forward("invalid-speedo-meter");
									}
								} catch (NumberFormatException nfe) {
									meterStart[i].setInvalid(true);
									meterEnd[i].setInvalid(true);
									return new Forward("invalid-number");
								}
							}
							AO.setMeterStart(meterS == null?null:Long.parseLong(meterS));
					    	AO.setMeterEnd(meterE == null?null:Long.parseLong(meterE));				    					    	
					    	// end : meter start/end
					    	
					    	AO.setRemarks((String) remarks[i].getValue());				    	
					    	
					    	AO.setVehicle_num((String)map.get("vehicle_num"));
					    	AO.setCreatedBy(getWidgetManager().getUser().getId());				    	
					    	AO.setCreatedDate(new Date());		
					    	AO.setUpdatedBy(getWidgetManager().getUser().getId());				    	
					    	AO.setUpdatedDate(new Date());	
					    	
				    		// add to collection if check-in date is filled.
				    		col.add(AO);
						} 
						// end : check-in date validation 
					}
					i++;
				}	
				
				try{
					if (col != null && col.size()>0) {
			    		for (Iterator y = col.iterator(); y.hasNext();) {
			    			AssignmentObject ao = (AssignmentObject) y.next();
							if (SetupModule.CHECKOUT_STATUS.equals(ao.getStatus())) {
				    			ao.setStatus(SetupModule.CLOSED_STATUS);   
				    			TM.updateAssignmentDetails(ao);
				    		} else {
				    			ao.setStatus(SetupModule.CLOSED_STATUS);  
				    			TM.insertAssignmentDetails(ao);
				    		}
			    		}
					} else {
						return new Forward("update-nothing");
					}
		    	} catch (Exception er) {
		    		Log.getLog(getClass()).error(er);
		    		return new Forward("FAILED");
		    	}
			}
		} catch (Exception e) {
			
			Log.getLog(getClass()).error(e.toString()); 
			return new Forward("FAILED");
		}
			
		// commented out: initForm();
		return new Forward("Update");
	}

	public String getDefaultTemplate(){
		return "fms/transport/bulkassignmentupdatelisttemp";
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

	
	public String getStartDate() {
		return startDate;
	}

	public DatePopupField[] getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(DatePopupField[] checkInDate) {
		this.checkInDate = checkInDate;
	}

	public DatePopupField[] getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(DatePopupField[] checkOutDate) {
		this.checkOutDate = checkOutDate;
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

	public String getRequestTitle() {
		return requestTitle;
	}

	public void setRequestTitle(String requestTitle) {
		this.requestTitle = requestTitle;
	}

	public Label[] getLbCheckOutDate() {
		return lbCheckOutDate;
	}

	public void setLbCheckOutDate(Label[] lbCheckOutDate) {
		this.lbCheckOutDate = lbCheckOutDate;
	}

	public Label[] getLbCompletionDate() {
		return lbCompletionDate;
	}

	public void setLbCompletionDate(Label[] lbCompletionDate) {
		this.lbCompletionDate = lbCompletionDate;
	}

	/*public Label[] getLbCompletionTime() {
		return lbCompletionTime;
	}

	public void setLbCompletionTime(Label[] lbCompletionTime) {
		this.lbCompletionTime = lbCompletionTime;
	}*/

	public TimeField[] getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(TimeField[] checkOutTime) {
		this.checkOutTime = checkOutTime;
	}

	public TimeField[] getCheckInTime() {
		return checkInTime;
	}

	public Label[] getLbCheckOutTime() {
		return lbCheckOutTime;
	}

	public void setLbCheckOutTime(Label[] lbCheckOutTime) {
		this.lbCheckOutTime = lbCheckOutTime;
	}

	public Label[] getLbCheckInTime() {
		return lbCheckInTime;
	}

	public void setLbCheckInTime(Label[] lbCheckInTime) {
		this.lbCheckInTime = lbCheckInTime;
	}

	public void setCheckInTime(TimeField[] checkInTime) {
		this.checkInTime = checkInTime;
	}

	public TextField[] getMeterStart() {
		return meterStart;
	}

	public void setMeterStart(TextField[] meterStart) {
		this.meterStart = meterStart;
	}

	public TextField[] getMeterEnd() {
		return meterEnd;
	}

	public void setMeterEnd(TextField[] meterEnd) {
		this.meterEnd = meterEnd;
	}

	public TextField[] getMileage() {
		return mileage;
	}

	public void setMileage(TextField[] mileage) {
		this.mileage = mileage;
	}

	public TextField[] getRemarks() {
		return remarks;
	}

	public void setRemarks(TextField[] remarks) {
		this.remarks = remarks;
	}

	public SelectBox[] getPetrolCard() {
		return petrolCard;
	}

	public void setPetrolCard(SelectBox[] petrolCard) {
		this.petrolCard = petrolCard;
	}
	
}
