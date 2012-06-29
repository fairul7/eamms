package com.tms.fms.transport.ui;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TimeZone;

import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.SecurityService;
import kacang.stdui.Button;
import kacang.stdui.ButtonGroup;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.TimeField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.fms.engineering.model.Sequence;
import com.tms.fms.register.model.FMSRegisterManager;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.OutsourceObject;
import com.tms.fms.transport.model.RateCardObject;
import com.tms.fms.transport.model.SetupObject;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.TransportRequest;
import com.tms.fms.transport.model.VehicleRequest;
import com.tms.fms.util.DateDiffUtil;

public class RequestForm extends Form {
	protected String id;
	protected String requestId;
	protected String newId;

	private TextField title;
	private TextField type;
	private Radio program;
	private Radio nonProgram;
	private ButtonGroup programUnit,bbGroup;

	protected DatePopupField startDate;
	protected DatePopupField endDate;
	protected TimeField startTime;
	protected TimeField endTime;
	private TextField destination;
	private TextBox purpose;
	private TextBox remarks;
	protected Button continueButton, submitButton, saveButton, cancelButton,
			draftButton, deleteDraftButton;
	protected Button cancelReqButton, backToListButton, backToIncomingListBtn,
			resubmitButton;
	protected ProgramSelect programSelect;
	private TransportVehicle transportVehicle;

	private boolean isNew = false;
	private boolean isEdit = false;
	private boolean isCancelView = false;
	private boolean isEditByAdmin = false;
	private boolean isOut = false;
	private boolean isView = false;
	private boolean isOutsource = false;
	protected boolean isReject = false;
	private String requestStatus = "";
	private String forwardName = "";
	private Label titleL;
	private Label typeL;
	private Label startDateL;
	private Label endDateL;
	private Label startTimeL;
	private Label endTimeL;
	private Label destinationL;
	private Label purposeL;
	private Label remarksL;
	private Label statusL;
	private Label programL;
	private Collection request;
	private String outspanel;
	private String anItems;
	private String reason;
	private String status;
	private String submitStatus;
	private Label rateCard;
	private TransportRequest transportRequest;
	private OutsourceObject outsourceObject;
	private Collection items;	
	private Radio bbYes;
	private Radio bbNo;

	public RequestForm() {
	}

	public void init() {
		title = new TextField("title");
		title.setSize("30");
		title.addChild(new ValidatorNotEmpty("vEmpty", Application
				.getInstance().getMessage("asset.message.vNotEmpty")));
		type = new TextField("type");
		type.setSize("30");
		program = new Radio("program", "Program");
		program.setOnClick("hasProgram();");
		nonProgram = new Radio("nonProgram", "Non Program");
		nonProgram.setOnClick("hasProgram();");
		nonProgram.setChecked(true);
		programUnit = new ButtonGroup("programUnit", new Radio[] { program,
				nonProgram });
		startDate = new DatePopupField("startDate");
		Calendar TodayDate = Calendar.getInstance();
		startDate.setDate(TodayDate.getTime());
		endDate = new DatePopupField("endDate");
		endDate.setDate(TodayDate.getTime());
		startTime = new TimeField("startTime");
		startTime.setTemplate("calendar/timefield");
		endTime = new TimeField("endTime");
		Date now = TodayDate.getTime();
		now.setHours(now.getHours() + 1);
		endTime.setDate(now);
		destination = new TextField("destination");
		destination.setSize("60");
		destination.addChild(new ValidatorNotEmpty("vEmpty", Application
				.getInstance().getMessage("asset.message.vNotEmpty")));
		purpose = new TextBox("purpose");
		purpose.setMaxlength("10");
		purpose.setRows("4");
		remarks = new TextBox("remarks");
		remarks.setMaxlength("10");
		remarks.setRows("4");
		continueButton = new Button("continueButton");
		continueButton.setText(Application.getInstance().getMessage(
				"com.tms.fms.transport.continue", "Continue"));
		resubmitButton = new Button("resubmitButton");
		resubmitButton.setText(Application.getInstance().getMessage(
				"fms.facility.copyRequest", "Resubmit"));
		submitButton = new Button("submitButton");
		submitButton.setText(Application.getInstance().getMessage(
				"com.tms.fms.transport.submitForApproval", "Submit"));
		saveButton = new Button("saveButton");
		saveButton.setText(Application.getInstance().getMessage(
				"com.tms.fms.transport.saveButton", "Save"));
		cancelButton = new Button("cancelButton");
		cancelButton.setText(Application.getInstance().getMessage(
				"fms.tran.backToList", "Cancel"));
		draftButton = new Button("draftButton");
		draftButton.setText(Application.getInstance().getMessage(
				"com.tms.fms.transport.saveAsDraft", "Save"));
		deleteDraftButton = new Button("deleteDraftButton");
		deleteDraftButton.setText(Application.getInstance().getMessage(
				"com.tms.fms.transport.deleteDraftButton", "Save"));
		programSelect = new ProgramSelect("programSelect");
		programSelect.init();
		transportVehicle = new TransportVehicle("transportVehicle");
		transportVehicle.init();
		cancelReqButton = new Button("cancelReqButton");
		cancelReqButton.setText(Application.getInstance().getMessage(
				"fms.tran.cancelRequest", "Submit"));
		backToListButton = new Button("backToListButton");
		backToListButton.setText(Application.getInstance().getMessage(
				"fms.tran.backToList", "Submit"));
		backToIncomingListBtn = new Button("backToIncomingListBtn");
		backToIncomingListBtn.setText(Application.getInstance().getMessage(
				"fms.tran.backToList", "Back To List"));
		
		Panel pnBB = new Panel("pnBB");
		bbYes = new Radio("bbYes", "Yes");
		bbYes.setGroupName("blockBooking"); 
		bbYes.setChecked(true);
		pnBB.addChild(bbYes);
		bbNo = new Radio("bbNo", "No");
		bbNo.setGroupName("blockBooking");
		//bbNo.setChecked(true);
		pnBB.addChild(bbNo);
		addChild(pnBB);
		
		bbYes = new Radio("bbYes", "Yes");		
		bbNo = new Radio("bbNo", "No");		
		bbYes.setChecked(true);
		bbGroup = new ButtonGroup("bbGroup", new Radio[] { bbYes,
				bbNo });
		
		
		titleL = new Label("titleL");
		typeL = new Label("typeL");
		startDateL = new Label("startDateL");
		endDateL = new Label("endDateL");
		startTimeL = new Label("startTimeL");
		endTimeL = new Label("endTimeL");
		destinationL = new Label("destinationL");
		purposeL = new Label("purposeL");
		remarksL = new Label("remarksL");
		statusL = new Label("statusL");
		programL = new Label("programL");
		rateCard = new Label("rateCard");

		
		addChild(title);
		addChild(type);
		addChild(program);
		addChild(nonProgram);
		addChild(programUnit);
		addChild(startDate);
		addChild(endDate);
		addChild(startTime);
		addChild(endTime);
		addChild(destination);
		addChild(purpose);
		addChild(remarks);
		addChild(continueButton);
		addChild(submitButton);
		addChild(saveButton);
		addChild(cancelButton);
		addChild(draftButton);
		addChild(programSelect);
		addChild(transportVehicle);
		addChild(cancelReqButton);
		addChild(backToListButton);
		addChild(backToIncomingListBtn);
		addChild(deleteDraftButton);
		addChild(resubmitButton);
		addChild(titleL);
		addChild(typeL);
		addChild(startDateL);
		addChild(endDateL);
		addChild(startTimeL);
		addChild(endTimeL);
		addChild(destinationL);
		addChild(purposeL);
		addChild(remarksL);
		addChild(statusL);
		addChild(programL);
		addChild(rateCard);
		
		addChild(bbYes);
		addChild(bbNo);
		addChild(bbGroup);

	}

	public String getDefaultTemplate() {

		if (isCancelView)
			return "fms/viewCancelRequest";

		if (isOutsource)
			return "fms/viewOutsource";

		if (isOut)
			return "fms/viewOut";

		if (isReject)
			return "fms/viewReject";

		if (isEdit || isNew)
			return "fms/tranRequest";

		if ("all".equals(status))
			return "fms/viewRequestAll";
		
		if ("all2".equals(status))
			return "fms/viewRequestAll2";

		return "fms/viewRequest";
	}

	 public kacang.ui.Forward onSubmit(kacang.ui.Event evt) {
		 
		kacang.ui.Forward result = super.onSubmit(evt);
		 
		if (startDate.getDate().after(endDate.getDate())) {
			endDate.setInvalid(true);
			setInvalid(true);
			return result;
		}
		
		if (bbYes.isChecked() && startTime.getDate().after(endTime.getDate()))
        {
            startTime.setInvalid(true);
            endTime.setInvalid(true);
            setInvalid(true);       
            return result;         
        }
		 
		if(startDate.getDate().equals(endDate.getDate()) && startTime.getDate().after(endTime.getDate())){
			startTime.setInvalid(true);
            endTime.setInvalid(true);
            setInvalid(true);       
            return result;      
		}
		
		Calendar start = Calendar.getInstance();		
		start.setTime(startDate.getDate());
		start.set(Calendar.HOUR_OF_DAY, startTime.getHour());
				
		Calendar today = Calendar.getInstance();
		today.setTime(new Date());
		today.set(Calendar.HOUR_OF_DAY, 00);
		today.set(Calendar.MINUTE, 00);
		today.set(Calendar.SECOND, 00);
                
		if(start.getTime().before(today.getTime())){
			startDate.setInvalid(true);           
            setInvalid(true);       
            return result;      
		}
		
        return null;
	 }

		 
	public Forward actionPerformed(Event evt) {
	
		if (backToListButton.getAbsoluteName().equals(findButtonClicked(evt))) {
			return new Forward("Back");
		}

		if (backToIncomingListBtn.getAbsoluteName().equals(
				findButtonClicked(evt))) {
			return new Forward("BackToIncomingList");
		}

		if (cancelButton.getAbsoluteName().equals(findButtonClicked(evt))) {
			return new Forward("Cancel");
		}

		if (deleteDraftButton.getAbsoluteName().equals(findButtonClicked(evt))) {
			evt.getRequest().setAttribute("id", id);
			return new Forward("Deleted");
		}

		if (continueButton.getAbsoluteName().equals(findButtonClicked(evt))) {
			submitStatus = SetupModule.DRAFT_STATUS;
			forwardName = "Continue";
		}

		if (submitButton.getAbsoluteName().equals(findButtonClicked(evt))) {
			evt.getRequest().setAttribute("id", id);
			submitStatus = SetupModule.PENDING_STATUS;
			forwardName = "SavePending";
		}

		if (saveButton.getAbsoluteName().equals(findButtonClicked(evt))) {
			evt.getRequest().setAttribute("id", id);
			submitStatus = SetupModule.PROCESS_STATUS;
			forwardName = "Save";
		}

		if (draftButton.getAbsoluteName().equals(findButtonClicked(evt))) {
			submitStatus = SetupModule.DRAFT_STATUS;
			forwardName = "SaveDraft";
		}

		if (resubmitButton.getAbsoluteName().equals(findButtonClicked(evt))) {

			// populateFormEdit(id, null, evt);
			submitStatus = SetupModule.PENDING_STATUS;
			forwardName = "Resubmit";
			/*
			 * try{
			 * evt.getResponse().sendRedirect("addNewRequest.jsp?id="+id+"&newid="+newId); }
			 * catch(Exception e){ Log.getLog(getClass()).error("Error
			 * redirecting to URL " + id, e); }
			 */
			
		}
		
		
		
		return super.actionPerformed(evt);
	}

	public Forward onValidate(Event evt) {

		TransportModule tm = (TransportModule) Application.getInstance()
				.getModule(TransportModule.class);
		TransportRequest tr = new TransportRequest();
		
		FMSRegisterManager FRM = (FMSRegisterManager) Application.getInstance()
				.getModule(FMSRegisterManager.class);
		SecurityService security = (SecurityService) Application.getInstance()
				.getService(SecurityService.class);
		
		long day = 0;
		String reqStatus = "";
		// Date today = new Date();
		Calendar today = Calendar.getInstance();
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();

		today.setTime(new Date());
		today.set(Calendar.HOUR_OF_DAY, startTime.getHour());
		today.set(Calendar.MINUTE, startTime.getMinute());

		start.setTime(startDate.getDate());
		start.set(Calendar.HOUR_OF_DAY, startTime.getHour());
		start.set(Calendar.MINUTE, startTime.getMinute());

		end.setTime(endDate.getDate());
		end.set(Calendar.HOUR_OF_DAY, endTime.getHour());
		end.set(Calendar.MINUTE, endTime.getMinute());

		String userid = getWidgetManager().getUser().getId();

		if (isEdit) {
			try {
				tr = tm.selectTransportRequest(id);
			} catch (Exception er) {
			}
		}

		tr.setRequestTitle((String) title.getValue());
		tr.setRequestType((String) type.getValue());
		tr.setStartDate(start.getTime());
		tr.setEndDate(end.getTime());
		if (program.isChecked()) {

			if (programSelect.getSelectedId() != null
					&& !programSelect.getSelectedId().equals("")) {
				tr.setProgram(programSelect.getSelectedId());
			} else {
				programSelect.setInvalid(true);
				this.setInvalid(true);
			}
		} else
			tr.setProgram("-");
		tr.setDestination((String) destination.getValue());
		tr.setPurpose((String) purpose.getValue());
		tr.setRemarks((String) remarks.getValue());
		tr.setStatus(submitStatus);
		
		String bbcheckold = tr.getBlockBooking();
		String bbchecknew = bbYes.isChecked()?"1":"0";
		tr.setBlockBooking(bbYes.isChecked()?"1":"0");

		if (isEdit) {
			if (tm.countRequestedVehicle(id) <= 0) {			
				tr.setStatus(SetupModule.DRAFT_STATUS);
				if (forwardName.equals("SavePending")) {
					transportVehicle.setInvalid(true);
					tr.setId(id);
					return new Forward("SavePendingFailed");
				} else if (forwardName.equals("Save")) {
					tr.setStatus(SetupModule.PROCESS_STATUS);
				}
			}
		} else {
			if (tm.countRequestedVehicle(requestId) <= 0) {				
				tr.setStatus(SetupModule.DRAFT_STATUS);
				if (forwardName.equals("SavePending")) {
					transportVehicle.setInvalid(true);
					tr.setId(requestId);					
					return new Forward("SavePendingFailed");
				}
			}
		}

		if (!isEdit) { // Add New
			
			String newId =  new Sequence(Sequence.TYPE_TRANSPORT).genarteCode();
			if (resubmitButton.getAbsoluteName().equals(findButtonClicked(evt))) {
				
				try {
					request = tm.getVehicles(id); // get how many vehicle &
													
				} catch (Exception er) {
				}

				VehicleRequest vr = new VehicleRequest();
				
				for (Iterator it = request.iterator(); it.hasNext();) {

					VehicleRequest vreq = (VehicleRequest) it.next();

					vr.setId(UuidGenerator.getInstance().getUuid());
					vr.setCategory_id(vreq.getCategory_id());
					vr.setRequestId(newId);
					vr.setQuantity(vreq.getQuantity());
					vr.setDriver(vreq.getDriver());

					try {
						tm.insertTransportVehicle(vr);
					} catch (Exception e) {
						Log.getLog(getClass()).error("Error save resubmit:" + e);
					}
				}

			}
			
			id = newId;
			tr.setId(id);
			try {
				tr.setRequestBy(getWidgetManager().getUser().getId());
				tr.setRequestDate(new Date());
				tm.addTransportRequest(tr);
				tm.insertRequestStatus(tr.getId(), tr.getStatus(), tr
						.getReason());

			} catch (Exception e) {
				Log.getLog(getClass()).error(e);
			}

			if (!forwardName.equals("") || forwardName != null) {
				if (forwardName.equals("SaveDraft")) {
					return new Forward("SaveDraft");
				}
				
				if(forwardName.equals("Resubmit")){
					tm.updateRequestStatus(tr.getId(), tr.getStatus(), tr
							.getReason());
					return new Forward("SavePending");
				}
			}

		} else {

			Date dateS = getDateFromStartTime(start.getTime());
			Date dateE = getDateToEndTime(end.getTime());
			
			long days = dateDiff(dateS, dateE);
			Log.getLog(getClass()).info(days);
			
			DecimalFormat timeformat = new DecimalFormat("00");
			String timeStart = timeformat.format(startTime.getHour())+":"+timeformat.format(startTime.getMinute()); 
			String timeEnd = timeformat.format(endTime.getHour())+":"+timeformat.format(endTime.getMinute());
			String ratedriver = tm.getRateDriver(id, startDate.getDate(), endDate.getDate(), timeStart, timeEnd, bbYes.isChecked()?"1":"0");
			String rateVehicle = tm.getRateFacility(id, startDate.getDate(), endDate.getDate(), timeStart, timeEnd, bbYes.isChecked()?"1":"0");
			tr.setRateVehicle(rateVehicle);
			tr.setRate(ratedriver);

			String newId = null;
			newId = evt.getRequest().getParameter("newid");
			if (null == newId || "".equals(newId))
				tr.setId(id);
			else
				tr.setId(newId);

			try {
				if(!bbchecknew.equals(bbcheckold)){
					long datediff = dateDiff4Assignment(tr.getStartDate(), tr.getEndDate());
					tm.deleteTransportAssignmentByRequestId(tr.getId());
					tm.deleteTransportVehicleAndDriverByRequestId(tr.getId());
					
//					if("1".equals(bbchecknew)){  				//if block booking is true
//		    			for(int i = 0; i <= datediff; i++){
//		    				
//		    			}
//					}
				}
					
				tr.setUpdatedBy(getWidgetManager().getUser().getId());
				tr.setUpdatedDate(new Date());
				tm.updateTransportRequest(tr);

				//Send Notification 
				try {

					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					
					String wordings = Application.getInstance().getMessage("fms.notification.tran.submitted.body");
					String subject = Application.getInstance().getMessage("fms.label.requestIdWaitingApproval");
					String deptId = FRM.getUserDepartment(userid);
					String sDate = sdf.format(tr.getStartDate()) ;
					String eDate = sdf.format(tr.getEndDate()) ;
					String requiredDate = sDate +" - "+eDate;
					String requestor = "";
					try{requestor = security.getUser(tr.getRequestBy()).getName();}catch(Exception ex){}
					
					if (!(forwardName.equals("SaveDraft")))		//dont sent notification if save as draft
						tm.sendNotificationForApproval(deptId, id, tr.getRequestTitle(), requiredDate, tr.getRemarks(), requestor, subject, wordings);
					
				} catch (Exception er) {
					Log.getLog(getClass())
							.error("ERROR sendNotification " + er);
				}
				
				

			} catch (Exception e) {
				Log.getLog(getClass()).error(e);
			}

			if (forwardName.equals("Save")) {
				return new Forward("Save");
			} else if (forwardName.equals("SaveDraft")) {
				return new Forward("SaveDraft");
			} else if (forwardName.equals("SavePending")) {
				tm.insertRequestStatus(tr.getId(), tr.getStatus(), tr
						.getReason());
				return new Forward("SavePending");
			} else {
				return new Forward("Back");
			}
		}

		try {
			evt.getResponse().sendRedirect("addNewRequest.jsp?id=" + id);
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error redirecting to URL " + id, e);
		}

		return null;
	}

	public void onRequest(Event event) {

		setEdit(false);
		setCancelView(false);
		setOut(false);
		setOutsource(false);
		setNew(false);
		isReject = false;

		super.onRequest(event);
		// if(null == id)
		id = event.getRequest().getParameter("id"); // for edit form

		requestId = event.getRequest().getParameter("requestId"); // for view
																	// form
		status = event.getRequest().getParameter("status");
		requestStatus = "";

		if ("outsource".equals(status)) {
			setOutsource(true);
			populateFormOutsource(id);
		}

		if ("out".equals(status)) { // for allrequest
			setOut(true);
			populateFormOutsource(id);
		}

		if ("cancelable".equals(status)) {
			setCancelView(true);
			populateForm(id);
		} else {

			if (!(id == null || "".equals(id))) {

				if ("reject".equals(status)) {
					isReject = true;
					populateFormEdit(id, null, event);
					populateForm(id);

				} else {
					setEdit(true);
					populateFormEdit(id, null, event);
				}

			} else {

				if (!(requestId == null || "".equals(requestId))) {
					init();
					populateForm(requestId);
				} else {
					setNew(true);
					init();
				}
			}

		}

	}

	
	public void populateForm(String id) {

		TransportModule TM = (TransportModule) Application.getInstance()
				.getModule(TransportModule.class);
		
		SetupModule SM = (SetupModule) Application.getInstance().getModule(
				SetupModule.class);
		request = null;

		try {

			transportRequest = TM.selectTransportRequest(id);
			if (transportRequest != null) {
				titleL.setText(transportRequest.getRequestTitle());
				typeL.setText(transportRequest.getRequestType());

				try {
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					startDateL.setText(sdf.format(transportRequest.getStartDate()));
					endDateL.setText(sdf.format(transportRequest.getEndDate()));

					SimpleDateFormat stf = new SimpleDateFormat("k:mm");
					startTimeL.setText(stf.format(transportRequest.getStartDate()));
					endTimeL.setText(stf.format(transportRequest.getEndDate()));

				} catch (Exception er) {
					Log.getLog(getClass()).error(
							"SimpleDateFormat error converting:" + er);
				}

				destinationL.setText(transportRequest.getDestination());
				purposeL.setText(transportRequest.getPurpose());
				remarksL.setText(transportRequest.getRemarks());

				statusL.setText(TM.selectStatus(transportRequest.getStatus()));

				programL.setText(SM.selectProgName(transportRequest.getProgram()));

				request = TM.getVehicles(id);
				
				String rate = transportRequest.getRate();
				if(null == rate || "".equals(rate))
					rate = "0";				
				rateCard.setText("RM " + rate);
			}

		} catch (Exception er) {
			Log.getLog(getClass()).error("We got the prob:" + er);
		}

		if ("reject".equals(status)) {
			newId = new Sequence(Sequence.TYPE_TRANSPORT).genarteCode();
			
		}
	}

	public void populateFormEdit(String id, String status, Event evt) {

		init();
		setId(id);
		TransportModule TM = (TransportModule) Application.getInstance().getModule(TransportModule.class);
		TransportRequest TR = new TransportRequest();

		request = null;

		try {
			TR = TM.selectTransportRequest(id);

			title.setValue(TR.getRequestTitle());
			
			if (!("-".equals(TR.getProgram()))) {
				program.setChecked(true);
				nonProgram.setChecked(false);
				String[] progs = new String[1];
				progs[0] = TR.getProgram();
				programSelect.setIds(progs);
			}
			startDate.setDate(TR.getStartDate());
			endDate.setDate(TR.getEndDate());
			startTime.setDate(TR.getStartDate());
			endTime.setDate(TR.getEndDate());
			destination.setValue(TR.getDestination());
			purpose.setValue(TR.getPurpose());
			remarks.setValue(TR.getRemarks());
			statusL.setText(TM.selectStatus(TR.getStatus()));
			requestStatus = TR.getStatus();
			rateCard.setText("RM " + TR.getRate());
			
			if ("1".equals(TR.getBlockBooking())){
				bbYes.setChecked(true);
				bbNo.setChecked(false);
			} else {
				bbNo.setChecked(true);
				bbYes.setChecked(false);
			}
			
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error on populateFormEdit:" + e);
		}
	}

	public void populateFormOutsource(String id) {

		TransportModule TM = (TransportModule) Application.getInstance().getModule(TransportModule.class);
		outsourceObject = new OutsourceObject();
		String outsourceId = "";
		try {

			outsourceObject = TM.selectTransportOutsource(id);
			outsourceId = outsourceObject.getId();

			int veh = 0;
			int dri = 0;

			request = TM.getVehicles(id);
			for (Iterator it = request.iterator(); it.hasNext();) {
				VehicleRequest vr = (VehicleRequest) it.next();
				veh += vr.getQuantity();
				dri += vr.getDriver();
			}

			anItems = " - ";
			if (veh > 0 || dri > 0)
				anItems = veh + " Car," + dri + " Driver";

			String outsourceItem = "";
			String setupId = "";
			for (int i = 0; i < outsourceObject.getSetup_id().length; i++) {
				setupId = (outsourceObject.getSetup_id()[i]);
				SetupObject o = (SetupObject) TM.getSetupObject(
						"fms_tran_outsourcepanel", setupId);
				outsourceItem += o.getName() + ", ";
			}

			outspanel = outsourceItem;
		} catch (Exception er) {
			Log.getLog(getClass()).error("We got the prob:" + er);
		}
	}
	
	 public long dateDiff(Date start, Date end){
		 
			long diff = Math.round((end.getTime() - start.getTime()) / (DateDiffUtil.MS_IN_A_DAY));			
			return diff;
		}

	public void populateHiddenForm(boolean show) {

		title.setHidden(show);
		program.setHidden(show);
		nonProgram.setHidden(show);
		programSelect.setHidden(show);
		startDate.setHidden(show);
		endDate.setHidden(show);
		startTime.setHidden(show);
		endTime.setHidden(show);
		destination.setHidden(show);
		purpose.setHidden(show);
		remarks.setHidden(show);
		statusL.setHidden(show);
	}

	protected Date getDateToEndTime(Date xDate){
    	
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(xDate);
        endTime.set(Calendar.HOUR_OF_DAY, 24);
        endTime.set(Calendar.MINUTE, 00);
        endTime.set(Calendar.SECOND, 00); 
        xDate = endTime.getTime();
        
        return xDate;      
    }
    
    protected Date getDateFromStartTime(Date xDate){
    	
        Calendar start = Calendar.getInstance();
        start.setTime(xDate);
        start.set(Calendar.HOUR_OF_DAY, 00);
        start.set(Calendar.MINUTE, 00);
        start.set(Calendar.SECOND, 00);
        xDate = start.getTime();
        
        return xDate;      
    }
    
	
	
	
	public long dateDiff4Assignment(Date start, Date end){		 
		long diff = Math.round((end.getTime() - start.getTime()) / (DateDiffUtil.MS_IN_A_DAY));			
		return diff;
	}	
	
	public Button getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(Button cancelButton) {
		this.cancelButton = cancelButton;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Button getSubmitButton() {
		return submitButton;
	}

	public void setSubmitButton(Button submitButton) {
		this.submitButton = submitButton;
	}

	public TextField getTitle() {
		return title;
	}

	public void setTitle(TextField title) {
		this.title = title;
	}

	public TextField getType() {
		return type;
	}

	public void setType(TextField type) {
		this.type = type;
	}

	public Radio getProgram() {
		return program;
	}

	public void setProgram(Radio program) {
		this.program = program;
	}

	public Radio getNonProgram() {
		return nonProgram;
	}

	public void setNonProgram(Radio nonProgram) {
		this.nonProgram = nonProgram;
	}

	public ButtonGroup getProgramUnit() {
		return programUnit;
	}

	public void setProgramUnit(ButtonGroup programUnit) {
		this.programUnit = programUnit;
	}

	public DatePopupField getStartDate() {
		return startDate;
	}

	public void setStartDate(DatePopupField startDate) {
		this.startDate = startDate;
	}

	public DatePopupField getEndDate() {
		return endDate;
	}

	public void setEndDate(DatePopupField endDate) {
		this.endDate = endDate;
	}

	public TimeField getStartTime() {
		return startTime;
	}

	public void setStartTime(TimeField startTime) {
		this.startTime = startTime;
	}

	public TimeField getEndTime() {
		return endTime;
	}

	public void setEndTime(TimeField endTime) {
		this.endTime = endTime;
	}

	public TextField getDestination() {
		return destination;
	}

	public void setDestination(TextField destination) {
		this.destination = destination;
	}

	public TextBox getPurpose() {
		return purpose;
	}

	public void setPurpose(TextBox purpose) {
		this.purpose = purpose;
	}

	public TextBox getRemarks() {
		return remarks;
	}

	public void setRemarks(TextBox remarks) {
		this.remarks = remarks;
	}

	public Button getContinueButton() {
		return continueButton;
	}

	public void setContinueButton(Button continueButton) {
		this.continueButton = continueButton;
	}

	public Button getDraftButton() {
		return draftButton;
	}

	public void setDraftButton(Button draftButton) {
		this.draftButton = draftButton;
	}

	public ProgramSelect getProgramSelect() {
		return programSelect;
	}

	public void setProgramSelect(ProgramSelect programSelect) {
		this.programSelect = programSelect;
	}

	public TransportVehicle getTransportVehicle() {
		return transportVehicle;
	}

	public void setTransportVehicle(TransportVehicle transportVehicle) {
		this.transportVehicle = transportVehicle;
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	public Label getTitleL() {
		return titleL;
	}

	public void setTitleL(Label titleL) {
		this.titleL = titleL;
	}

	public Label getTypeL() {
		return typeL;
	}

	public void setTypeL(Label typeL) {
		this.typeL = typeL;
	}

	public Label getStartDateL() {
		return startDateL;
	}

	public void setStartDateL(Label startDateL) {
		this.startDateL = startDateL;
	}

	public Label getEndDateL() {
		return endDateL;
	}

	public void setEndDateL(Label endDateL) {
		this.endDateL = endDateL;
	}

	public Label getStartTimeL() {
		return startTimeL;
	}

	public void setStartTimeL(Label startTimeL) {
		this.startTimeL = startTimeL;
	}

	public Label getEndTimeL() {
		return endTimeL;
	}

	public void setEndTimeL(Label endTimeL) {
		this.endTimeL = endTimeL;
	}

	public Label getDestinationL() {
		return destinationL;
	}

	public void setDestinationL(Label destinationL) {
		this.destinationL = destinationL;
	}

	public Collection getItems() {
		return items;
	}

	public void setItems(Collection items) {
		this.items = items;
	}

	public Label getPurposeL() {
		return purposeL;
	}

	public void setPurposeL(Label purposeL) {
		this.purposeL = purposeL;
	}

	public Label getRemarksL() {
		return remarksL;
	}

	public void setRemarksL(Label remarksL) {
		this.remarksL = remarksL;
	}

	public Label getStatusL() {
		return statusL;
	}

	public void setStatusL(Label statusL) {
		this.statusL = statusL;
	}

	public Collection getRequest() {
		return request;
	}

	public void setRequest(Collection request) {
		this.request = request;
	}

	public Label getProgramL() {
		return programL;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setProgramL(Label programL) {
		this.programL = programL;
	}

	public Button getCancelReqButton() {
		return cancelReqButton;
	}

	public void setCancelReqButton(Button cancelReqButton) {
		this.cancelReqButton = cancelReqButton;
	}

	public Button getBackToListButton() {
		return backToListButton;
	}

	public void setBackToListButton(Button backToListButton) {
		this.backToListButton = backToListButton;
	}

	public Button getDeleteDraftButton() {
		return deleteDraftButton;
	}

	public void setDeleteDraftButton(Button deleteDraftButton) {
		this.deleteDraftButton = deleteDraftButton;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isEditByAdmin() {
		return isEditByAdmin;
	}

	public void setEditByAdmin(boolean isEditByAdmin) {
		this.isEditByAdmin = isEditByAdmin;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public Button getSaveButton() {
		return saveButton;
	}

	public void setSaveButton(Button saveButton) {
		this.saveButton = saveButton;
	}

	public Button getBackToIncomingListBtn() {
		return backToIncomingListBtn;
	}

	public void setBackToIncomingListBtn(Button backToIncomingListBtn) {
		this.backToIncomingListBtn = backToIncomingListBtn;
	}

	public Label getRateCard() {
		return rateCard;
	}

	public void setRateCard(Label rateCard) {
		this.rateCard = rateCard;
	}

	public boolean isOut() {
		return isOut;
	}

	public void setOut(boolean isOut) {
		this.isOut = isOut;
	}

	public OutsourceObject getOutsourceObject() {
		return outsourceObject;
	}

	public void setOutsourceObject(OutsourceObject outsourceObject) {
		this.outsourceObject = outsourceObject;
	}

	public boolean isCancelView() {
		return isCancelView;
	}

	public void setCancelView(boolean isCancelView) {
		this.isCancelView = isCancelView;
	}

	public boolean isView() {
		return isView;
	}

	public boolean isOutsource() {
		return isOutsource;
	}

	public String getOutspanel() {
		return outspanel;
	}

	public void setOutspanel(String outspanel) {
		this.outspanel = outspanel;
	}

	public String getAnItems() {
		return anItems;
	}

	public void setAnItems(String anItems) {
		this.anItems = anItems;
	}

	public void setOutsource(boolean isOutsource) {
		this.isOutsource = isOutsource;
	}

	public void setView(boolean isView) {
		this.isView = isView;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public Radio getBbYes() {
		return bbYes;
	}

	public void setBbYes(Radio bbYes) {
		this.bbYes = bbYes;
	}

	public Radio getBbNo() {
		return bbNo;
	}

	public void setBbNo(Radio bbNo) {
		this.bbNo = bbNo;
	}

	public Button getResubmitButton() {
		return resubmitButton;
	}
	
	public void setResubmitButton(Button resubmitButton) {
		this.resubmitButton = resubmitButton;
	}

	public TransportRequest getTransportRequest() {
		return transportRequest;
	}

	public void setTransportRequest(TransportRequest transportRequest) {
		this.transportRequest = transportRequest;
	}
	
	

}
