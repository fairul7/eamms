package com.tms.fms.engineering.model;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultDataObject;
import kacang.model.DefaultModule;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import org.apache.axis.collections.SequencedHashMap;
import com.tms.collab.forum.model.StringUtil;
import com.tms.fms.abw.model.AbwDao;
import com.tms.fms.abw.model.AbwModule;
import com.tms.fms.abw.model.TransferCostCancellationObject;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.engineering.ui.ServiceDetailsForm;
import com.tms.fms.facility.model.FacilityDao;
import com.tms.fms.facility.model.FacilityModule;
import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.register.model.FMSRegisterManager;
import com.tms.fms.transport.model.FmsNotification;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.TransportRequest;
import com.tms.fms.transport.model.VehicleRequest;
import com.tms.fms.transport.ui.AbwEmailsSetup;
import com.tms.fms.util.DateDiffUtil;
import com.tms.util.FMSMailUtil;

public class EngineeringModule extends DefaultModule {
	public static SequencedHashMap STATUS_MAP=new SequencedHashMap();
	public static SequencedHashMap HOD_STATUS_MAP = new SequencedHashMap();
	public static SequencedHashMap FC_HEAD_STATUS_MAP = new SequencedHashMap();	
	public static SequencedHashMap FC_HEAD_STATUS_NO_CANCEL_MAP = new SequencedHashMap();
	public static SequencedHashMap FC_STATUS_MAP = new SequencedHashMap();
	public static SequencedHashMap UNIT_HEAD_STATUS_MAP = new SequencedHashMap();
	public static SequencedHashMap ASSIGNMENT_MANPOWER_STATUS_MAP=new SequencedHashMap();
	public static SequencedHashMap ASSIGNMENT_FACILITY_STATUS_MAP=new SequencedHashMap();
	public static SequencedHashMap ASSIGNMENT_HOU_FACILITY_STATUS_MAP=new SequencedHashMap();
	public static SequencedHashMap ASSIGNMENT_ALL_STATUS_MAP = new SequencedHashMap();
	public static SequencedHashMap STATE_MAP=new SequencedHashMap();
	public static SequencedHashMap SERVICES_MAP=new SequencedHashMap();
	public static SequencedHashMap REQUEST_TYPE_MAP=new SequencedHashMap();
	public static SequencedHashMap PROGRAM_TYPE_MAP=new SequencedHashMap();
	public static SequencedHashMap CANCELLATION_REASONS_MAP=new SequencedHashMap();
	public static SequencedHashMap SERVICE_PARTICULARS_MAP=new SequencedHashMap();
	public static SequencedHashMap VTR_FORMAT_MAP=new SequencedHashMap();
	public static SequencedHashMap VTR_INGEST_QC_FORMAT_MAP = new SequencedHashMap();
	public static SequencedHashMap VTR_CONVERSION_MAP=new SequencedHashMap();
	public static SequencedHashMap RESOURCE_UTIL_MAP = new SequencedHashMap();
	public static SequencedHashMap SUBMIT_REQUEST_MAP = new SequencedHashMap();
	public static SequencedHashMap DAY_SETTING = new SequencedHashMap();
	public static SequencedHashMap DAY_AUTO_SETTING = new SequencedHashMap();
	
	public static SequencedHashMap QUESTIONS_SCP_MAP 	= new SequencedHashMap();
	public static SequencedHashMap QUESTIONS_POST_MAP	= new SequencedHashMap();
	public static SequencedHashMap QUESTIONS_VTR_MAP	= new SequencedHashMap();
	public static SequencedHashMap QUESTIONS_MAN_MAP 	= new SequencedHashMap();
	public static SequencedHashMap QUESTIONS_STD_MAP	= new SequencedHashMap();
	public static SequencedHashMap QUESTIONS_OTH_MAP	= new SequencedHashMap();
	public static SequencedHashMap QUESTIONS_TVRO_MAP	= new SequencedHashMap();
	
	public static String REQUEST_TYPE_INTERNAL="I";
	public static String REQUEST_TYPE_EXTERNAL="E";
	public static String REQUEST_TYPE_NONPROGRAM="N";
	public static String PROGRAM_TYPE_LIVE="L";
	public static String PROGRAM_TYPE_RECORDING="R";
	
	public static final String MODIFY_STATUS 		= "M";
	public static final String DRAFT_STATUS 		= "D";
	public static final String PENDING_STATUS 		= "H";
	public static final String PROCESS_STATUS 		= "P";
	public static final String FC_ASSIGNED_STATUS 	= "Q";
	public static final String REJECTED_STATUS 		= "R";
	public static final String CANCELLED_STATUS 	= "C";
	public static final String ASSIGNMENT_STATUS 	= "A";
	public static final String FULFILLED_STATUS 	= "F";
	public static final String CLOSED_STATUS 		= "U";
	public static final String LATE_STATUS 			= "L";
	public static final String APPLIED_CANCELLATION = "T";
	public static final String OUTSOURCED_STATUS	= "O";
	//public static final String ACCEPT_STATUS		= "B";		//before prepare assignment
	
	// note: for status trail only
	public static final String STATUS_TRAIL_PREPARE_ASSIGNMENT = "K";
	
	public static final String SUBMIT_NEW_REQUEST = "N";
	public static final String COPY_NEW_REQUEST = "C";
	
	public static final String ASSIGNMENT_MANPOWER_STATUS_NEW="N";
	public static final String ASSIGNMENT_MANPOWER_STATUS_ASSIGNED="A";
	public static final String ASSIGNMENT_MANPOWER_STATUS_COMPLETED="C";
	public static final String ASSIGNMENT_MANPOWER_STATUS_UNFULFILLED="U";
	public static final String ASSIGNMENT_MANPOWER_STATUS_CANCEL="X";
	
	public static final String ASSIGNMENT_FACILITY_STATUS_NEW="N";
	public static final String ASSIGNMENT_FACILITY_STATUS_CHECKOUT="O";
	public static final String ASSIGNMENT_FACILITY_STATUS_CHECKIN="I";
	public static final String ASSIGNMENT_FACILITY_STATUS_UNFULFILLED="U";
	public static final String ASSIGNMENT_FACILITY_STATUS_CANCEL="X";
	
	public static final String ASSIGNMENT_TYPE_MANPOWER="M";
	public static final String ASSIGNMENT_TYPE_FACILITY="F";
	
	public static final String BLOCK_BOOKING_YES="Y";
	public static final String BLOCK_BOOKING_NO="N";
	
	public static final String STATE_NORMAL="N";
	public static final String STATE_ADHOC="A";
	public static final String STATE_LATE="L";
	
	public static final String UTILIZED_ITEM="1";
	public static final String NOT_UTILIZED_ITEM="0";
	
	public static final String VTR_FILE_PATH = "/fms/engineering/vtr";
	public static final String ENGINEERING_ASSIGNMENT_FILE_PATH = "/fms/engineering/eng_assignment/";
	
	public static final String FMS_AMDINISTRATOR_PERMISSION="com.tms.fms.facility.permission.fmsAdmin";
	//public static final String FC_HEAD_PERMISSION="com.tms.fms.facility.permission.facilityControllerHead";
	public static final String FC_HEAD_PERMISSION="com.tms.fms.facility.permission.facilityController";
	public static final String ENGINEERING_DEPARTMENT_PROPERTY="fms.facilities.engineering.engineeringDepartmentId";
	public static final String ENGINEERING_UNIT_PROPERTY="fms.facilities.engineering.engineeringUnitId";
	public static final String FC_GROUP_PROPERTY="fms.facilities.engineering.fcGroupId";
	public static final String ALLOW_ACCEPT_WITH_INVALID_RATE_CARDS="fms.facilities.engineering.allowAcceptWithInvalidRateCards";
	
	public static String ENGINEERING_DEPARTMENT_ID;
	public static String ENGINEERING_UNIT_ID;
	public static String FC_GROUP_ID;
	
	public static SequencedHashMap TIMEZONES=new SequencedHashMap();
	public static SequencedHashMap TIMEMEASURES=new SequencedHashMap();
	
	public static String INVALID_SERVICE_DATE = "invalidServiceDate";
	public static String INVALID_SERVICE_ITEM = "invalidServiceItem";
	public static String SERVICE_UNAVAILABLE = "serviceNotAvailable";
	public static final String ASSIGNMENT_FACILITY_STATUS_PREPARE_CHECKOUT = "P";
	public static final String ACTION_PREPARE_CHECKOUT="prepareCheckout";
	public static final String ACTION_CHECKOUT="checkout";
	
	{
		Application app=Application.getInstance();
		TIMEZONES.put("m", app.getMessage("fms.facility.label.mst"));
		TIMEZONES.put("g",app.getMessage("fms.facility.label.gmt"));
		TIMEMEASURES.put("m",app.getMessage("fms.facility.label.min"));
		TIMEMEASURES.put("h",app.getMessage("fms.facility.label.hour"));
		
		CANCELLATION_REASONS_MAP.put("1", app.getMessage("fms.facility.label.changeLocation"));
		CANCELLATION_REASONS_MAP.put("2", app.getMessage("fms.facility.label.changeDate"));
		CANCELLATION_REASONS_MAP.put("3", app.getMessage("fms.facility.label.talentNotAvailable"));
		CANCELLATION_REASONS_MAP.put("4", app.getMessage("fms.facility.label.eventCancelled"));
		CANCELLATION_REASONS_MAP.put("5", app.getMessage("fms.facility.label.notUtilised"));
		CANCELLATION_REASONS_MAP.put("6", app.getMessage("fms.facility.label.productionPostponed"));
		CANCELLATION_REASONS_MAP.put("7", app.getMessage("fms.facility.label.clientCancelled"));
		CANCELLATION_REASONS_MAP.put("8", app.getMessage("fms.facility.label.sponsorCancelled"));
		CANCELLATION_REASONS_MAP.put("9", app.getMessage("fms.facility.label.materialNotAvailable"));
		CANCELLATION_REASONS_MAP.put("10", app.getMessage("fms.facility.label.outsideRecording"));
		CANCELLATION_REASONS_MAP.put("11", app.getMessage("fms.facility.label.recordingDone"));
		CANCELLATION_REASONS_MAP.put("12", app.getMessage("fms.facility.label.locationNotAvailable"));
		CANCELLATION_REASONS_MAP.put("13", app.getMessage("fms.facility.label.compereNotAvailable"));
		
		VTR_FORMAT_MAP.put("0", app.getMessage("fms.facility.label.na"));
		VTR_FORMAT_MAP.put("1", app.getMessage("fms.facility.label.dgVtr"));
		//VTR_FORMAT_MAP.put("2", app.getMessage("fms.facility.label.dgBeta"));
		VTR_FORMAT_MAP.put("2", app.getMessage("fms.facility.label.spVtr"));
		VTR_FORMAT_MAP.put("3", app.getMessage("fms.facility.label.dvcPro"));
		VTR_FORMAT_MAP.put("4", app.getMessage("fms.facility.label.dvd"));
		//VTR_FORMAT_MAP.put("5", app.getMessage("fms.facility.label.sp"));
		VTR_FORMAT_MAP.put("5", app.getMessage("fms.facility.label.vcd"));
		VTR_FORMAT_MAP.put("6", app.getMessage("fms.facility.label.vhs"));
		VTR_FORMAT_MAP.put("7", app.getMessage("fms.facility.label.offAir"));	
		VTR_FORMAT_MAP.put("8", app.getMessage("fms.facility.label.server"));
		VTR_FORMAT_MAP.put("9", app.getMessage("fms.facility.label.miniDV"));
		//VTR_FORMAT_MAP.put("6", app.getMessage("fms.facility.label.spBeta"));
		//VTR_FORMAT_MAP.put("7", app.getMessage("fms.facility.label.vcd"));
		//VTR_FORMAT_MAP.put("8", app.getMessage("fms.facility.label.vhs"));
		
		//VTR_INGEST_QC_FORMAT_MAP.put("0", app.getMessage("fms.facility.label.na"));
		VTR_INGEST_QC_FORMAT_MAP.put("a", app.getMessage("fms.facility.label.ingestDgVtr"));
		VTR_INGEST_QC_FORMAT_MAP.put("b", app.getMessage("fms.facility.label.ingestSpVtr"));
		VTR_INGEST_QC_FORMAT_MAP.put("c", app.getMessage("fms.facility.label.ingestDvcPro"));
		VTR_INGEST_QC_FORMAT_MAP.put("d", app.getMessage("fms.facility.label.ingestImx"));
		
		VTR_CONVERSION_MAP.put("0", app.getMessage("fms.facility.label.na"));
		VTR_CONVERSION_MAP.put("1", app.getMessage("fms.facility.label.ntsc"));
		VTR_CONVERSION_MAP.put("2", app.getMessage("fms.facility.label.pal"));
		
		SERVICE_PARTICULARS_MAP.put("1", app.getMessage("fms.facility.label.diretcTransfer"));
		SERVICE_PARTICULARS_MAP.put("2", app.getMessage("fms.facility.label.recording"));
		SERVICE_PARTICULARS_MAP.put("3", app.getMessage("fms.facility.label.programIngest"));
		SERVICE_PARTICULARS_MAP.put("4", app.getMessage("fms.facility.label.ingestSubtitle"));
		SERVICE_PARTICULARS_MAP.put("5", app.getMessage("fms.facility.label.qualityCheck"));
		SERVICE_PARTICULARS_MAP.put("6", app.getMessage("fms.facility.label.directTransferDubbing"));
		
		// Feedback questions
		QUESTIONS_SCP_MAP.put("1", app.getMessage("fms.feedback.scp.question1"));
		QUESTIONS_SCP_MAP.put("2", app.getMessage("fms.feedback.scp.question2"));
		QUESTIONS_SCP_MAP.put("3", app.getMessage("fms.feedback.scp.question3"));
		QUESTIONS_SCP_MAP.put("4", app.getMessage("fms.feedback.scp.question4"));
		QUESTIONS_SCP_MAP.put("5", app.getMessage("fms.feedback.scp.question5"));
		
		QUESTIONS_POST_MAP.put("1", app.getMessage("fms.feedback.post.question1"));
		QUESTIONS_POST_MAP.put("2", app.getMessage("fms.feedback.post.question2"));
		QUESTIONS_POST_MAP.put("3", app.getMessage("fms.feedback.post.question3"));
		QUESTIONS_POST_MAP.put("4", app.getMessage("fms.feedback.post.question4"));
		QUESTIONS_POST_MAP.put("5", app.getMessage("fms.feedback.post.question5"));
		
		QUESTIONS_VTR_MAP.put("1", app.getMessage("fms.feedback.vtr.question1"));
		QUESTIONS_VTR_MAP.put("2", app.getMessage("fms.feedback.vtr.question2"));
		QUESTIONS_VTR_MAP.put("3", app.getMessage("fms.feedback.vtr.question3"));
		QUESTIONS_VTR_MAP.put("4", app.getMessage("fms.feedback.vtr.question4"));
		QUESTIONS_VTR_MAP.put("5", app.getMessage("fms.feedback.vtr.question5"));
		
		QUESTIONS_MAN_MAP.put("1", app.getMessage("fms.feedback.man.question1"));
		QUESTIONS_MAN_MAP.put("2", app.getMessage("fms.feedback.man.question2"));
		QUESTIONS_MAN_MAP.put("3", app.getMessage("fms.feedback.man.question3"));
		QUESTIONS_MAN_MAP.put("4", app.getMessage("fms.feedback.man.question4"));
		QUESTIONS_MAN_MAP.put("5", app.getMessage("fms.feedback.man.question5"));
		
		QUESTIONS_STD_MAP.put("1", app.getMessage("fms.feedback.std.question1"));
		QUESTIONS_STD_MAP.put("2", app.getMessage("fms.feedback.std.question2"));
		QUESTIONS_STD_MAP.put("3", app.getMessage("fms.feedback.std.question3"));
		QUESTIONS_STD_MAP.put("4", app.getMessage("fms.feedback.std.question4"));
		QUESTIONS_STD_MAP.put("5", app.getMessage("fms.feedback.std.question5"));
		
		QUESTIONS_OTH_MAP.put("1", app.getMessage("fms.feedback.oth.question1"));
		QUESTIONS_OTH_MAP.put("2", app.getMessage("fms.feedback.oth.question2"));
		QUESTIONS_OTH_MAP.put("3", app.getMessage("fms.feedback.oth.question3"));
		QUESTIONS_OTH_MAP.put("4", app.getMessage("fms.feedback.oth.question4"));
		QUESTIONS_OTH_MAP.put("5", app.getMessage("fms.feedback.oth.question5"));
		
		QUESTIONS_TVRO_MAP.put("1", app.getMessage("fms.feedback.tvro.question1"));
		QUESTIONS_TVRO_MAP.put("2", app.getMessage("fms.feedback.tvro.question2"));
		QUESTIONS_TVRO_MAP.put("3", app.getMessage("fms.feedback.tvro.question3"));
		QUESTIONS_TVRO_MAP.put("4", app.getMessage("fms.feedback.tvro.question4"));
		QUESTIONS_TVRO_MAP.put("5", app.getMessage("fms.feedback.tvro.question5"));
		
	}

	public void init() {
		Application app= Application.getInstance();
		//Status Map Setup
		try {
			ENGINEERING_DEPARTMENT_ID = app.getProperty(ENGINEERING_DEPARTMENT_PROPERTY);
			ENGINEERING_UNIT_ID = app.getProperty(ENGINEERING_UNIT_PROPERTY);
			FC_GROUP_ID = app.getProperty(FC_GROUP_PROPERTY);
			
			STATUS_MAP.put(MODIFY_STATUS, app.getMessage("fms.engineering.request.status.modified"));
			STATUS_MAP.put(DRAFT_STATUS, app.getMessage("fms.engineering.request.status.draft"));
			STATUS_MAP.put(PENDING_STATUS, app.getMessage("fms.engineering.request.status.pending"));
			STATUS_MAP.put(PROCESS_STATUS, app.getMessage("fms.engineering.request.status.process"));
			STATUS_MAP.put(FC_ASSIGNED_STATUS, app.getMessage("fms.engineering.request.status.fcAssigned"));
			STATUS_MAP.put(REJECTED_STATUS, app.getMessage("fms.engineering.request.status.rejected"));
			STATUS_MAP.put(CANCELLED_STATUS, app.getMessage("fms.engineering.request.status.cancelled"));
			STATUS_MAP.put(ASSIGNMENT_STATUS, app.getMessage("fms.engineering.request.status.assignment"));
			STATUS_MAP.put(FULFILLED_STATUS, app.getMessage("fms.engineering.request.status.fulfilled"));
			STATUS_MAP.put(CLOSED_STATUS, app.getMessage("fms.engineering.request.status.closed"));
			STATUS_MAP.put(LATE_STATUS, app.getMessage("fms.engineering.request.status.fulfilledDelay"));
			STATUS_MAP.put(APPLIED_CANCELLATION, app.getMessage("fms.engineering.request.status.appliedForCancellation"));
			STATUS_MAP.put(OUTSOURCED_STATUS, app.getMessage("fms.engineering.request.status.outsource"));
			STATUS_MAP.put(STATUS_TRAIL_PREPARE_ASSIGNMENT, app.getMessage("fms.engineering.request.statusTrail.prepareAssignment"));
			//STATUS_MAP.put(ACCEPT_STATUS, app.getMessage("fms.engineering.request.status.accepted"));
			
			// for HOD_STATUS_MAP
			HOD_STATUS_MAP.put(PROCESS_STATUS, app.getMessage("fms.engineering.request.status.process"));
			HOD_STATUS_MAP.put(FC_ASSIGNED_STATUS, app.getMessage("fms.engineering.request.status.fcAssigned"));
			HOD_STATUS_MAP.put(REJECTED_STATUS, app.getMessage("fms.engineering.request.status.rejected"));
			HOD_STATUS_MAP.put(CANCELLED_STATUS, app.getMessage("fms.engineering.request.status.cancelled"));
			HOD_STATUS_MAP.put(ASSIGNMENT_STATUS, app.getMessage("fms.engineering.request.status.assignment"));
			HOD_STATUS_MAP.put(FULFILLED_STATUS, app.getMessage("fms.engineering.request.status.fulfilled"));
			HOD_STATUS_MAP.put(CLOSED_STATUS, app.getMessage("fms.engineering.request.status.closed"));
			HOD_STATUS_MAP.put(LATE_STATUS, app.getMessage("fms.engineering.request.status.fulfilledDelay"));
			HOD_STATUS_MAP.put(APPLIED_CANCELLATION, app.getMessage("fms.engineering.request.status.appliedForCancellation"));
			HOD_STATUS_MAP.put(OUTSOURCED_STATUS, app.getMessage("fms.engineering.request.status.outsource"));
			
			// for FC_HEAD_STATUS_MAP
			//FC_HEAD_STATUS_MAP.put(ACCEPT_STATUS, app.getMessage("fms.engineering.request.status.accepted"));
			FC_HEAD_STATUS_MAP.put(FC_ASSIGNED_STATUS, app.getMessage("fms.engineering.request.status.fcAssigned"));
			FC_HEAD_STATUS_MAP.put(REJECTED_STATUS, app.getMessage("fms.engineering.request.status.rejected"));
			FC_HEAD_STATUS_MAP.put(CANCELLED_STATUS, app.getMessage("fms.engineering.request.status.cancelled"));
			FC_HEAD_STATUS_MAP.put(ASSIGNMENT_STATUS, app.getMessage("fms.engineering.request.status.assignment"));
			FC_HEAD_STATUS_MAP.put(FULFILLED_STATUS, app.getMessage("fms.engineering.request.status.fulfilled"));
			FC_HEAD_STATUS_MAP.put(CLOSED_STATUS, app.getMessage("fms.engineering.request.status.closed"));
			FC_HEAD_STATUS_MAP.put(LATE_STATUS, app.getMessage("fms.engineering.request.status.fulfilledDelay"));
			FC_HEAD_STATUS_MAP.put(APPLIED_CANCELLATION, app.getMessage("fms.engineering.request.status.appliedForCancellation"));
			FC_HEAD_STATUS_MAP.put(OUTSOURCED_STATUS, app.getMessage("fms.engineering.request.status.outsource"));
			
			
			
			// for resource utilization status
			RESOURCE_UTIL_MAP.put(ASSIGNMENT_STATUS, app.getMessage("fms.engineering.request.status.assignment"));
			RESOURCE_UTIL_MAP.put(CANCELLED_STATUS, app.getMessage("fms.engineering.request.status.cancelled"));
			RESOURCE_UTIL_MAP.put(CLOSED_STATUS, app.getMessage("fms.engineering.request.status.closed"));
			RESOURCE_UTIL_MAP.put(FULFILLED_STATUS, app.getMessage("fms.engineering.request.status.fulfilled"));
			RESOURCE_UTIL_MAP.put(ASSIGNMENT_MANPOWER_STATUS_NEW, app.getMessage("fms.engineering.request.status.new"));
			
			// for Submit Request Requirement #9470
			SUBMIT_REQUEST_MAP.put(SUBMIT_NEW_REQUEST, app.getMessage("fms.engineering.request.submit.submitNew"));
			SUBMIT_REQUEST_MAP.put(COPY_NEW_REQUEST, app.getMessage("fms.engineering.request.submit.copyRequest"));
			
			// for FC_HEAD_STATUS_MAP W/O Cancelled
			FC_HEAD_STATUS_NO_CANCEL_MAP.put(FC_ASSIGNED_STATUS, app.getMessage("fms.engineering.request.status.fcAssigned"));
			FC_HEAD_STATUS_NO_CANCEL_MAP.put(REJECTED_STATUS, app.getMessage("fms.engineering.request.status.rejected"));
			//FC_HEAD_STATUS_MAP.put(CANCELLED_STATUS, app.getMessage("fms.engineering.request.status.cancelled"));
			FC_HEAD_STATUS_NO_CANCEL_MAP.put(ASSIGNMENT_STATUS, app.getMessage("fms.engineering.request.status.assignment"));
			FC_HEAD_STATUS_NO_CANCEL_MAP.put(FULFILLED_STATUS, app.getMessage("fms.engineering.request.status.fulfilled"));
			FC_HEAD_STATUS_NO_CANCEL_MAP.put(CLOSED_STATUS, app.getMessage("fms.engineering.request.status.closed"));
			FC_HEAD_STATUS_NO_CANCEL_MAP.put(LATE_STATUS, app.getMessage("fms.engineering.request.status.fulfilledDelay"));
			FC_HEAD_STATUS_NO_CANCEL_MAP.put(APPLIED_CANCELLATION, app.getMessage("fms.engineering.request.status.appliedForCancellation"));
			FC_HEAD_STATUS_NO_CANCEL_MAP.put(OUTSOURCED_STATUS, app.getMessage("fms.engineering.request.status.outsource"));
			
			// for FC_STATUS_MAP
			FC_STATUS_MAP.put(REJECTED_STATUS, app.getMessage("fms.engineering.request.status.rejected"));
			FC_STATUS_MAP.put(CANCELLED_STATUS, app.getMessage("fms.engineering.request.status.cancelled"));
			FC_STATUS_MAP.put(ASSIGNMENT_STATUS, app.getMessage("fms.engineering.request.status.assignment"));
			FC_STATUS_MAP.put(FULFILLED_STATUS, app.getMessage("fms.engineering.request.status.fulfilled"));
			FC_STATUS_MAP.put(CLOSED_STATUS, app.getMessage("fms.engineering.request.status.closed"));
			FC_STATUS_MAP.put(LATE_STATUS, app.getMessage("fms.engineering.request.status.fulfilledDelay"));
			FC_STATUS_MAP.put(APPLIED_CANCELLATION, app.getMessage("fms.engineering.request.status.appliedForCancellation"));
			FC_STATUS_MAP.put(OUTSOURCED_STATUS, app.getMessage("fms.engineering.request.status.outsource"));
			
			// for UNIT_HEAD_STATUS_MAP
			//UNIT_HEAD_STATUS_MAP.put(REJECTED_STATUS, app.getMessage("fms.engineering.request.status.rejected"));
			UNIT_HEAD_STATUS_MAP.put(CANCELLED_STATUS, app.getMessage("fms.engineering.request.status.cancelled"));
			UNIT_HEAD_STATUS_MAP.put(ASSIGNMENT_STATUS, app.getMessage("fms.engineering.request.status.assignment"));
			UNIT_HEAD_STATUS_MAP.put(FULFILLED_STATUS, app.getMessage("fms.engineering.request.status.fulfilled"));
			UNIT_HEAD_STATUS_MAP.put(CLOSED_STATUS, app.getMessage("fms.engineering.request.status.closed"));
			UNIT_HEAD_STATUS_MAP.put(LATE_STATUS, app.getMessage("fms.engineering.request.status.fulfilledDelay"));
			UNIT_HEAD_STATUS_MAP.put(APPLIED_CANCELLATION, app.getMessage("fms.engineering.request.status.appliedForCancellation"));
			
			// for Assignment Setting Day
			DAY_SETTING.put("1", app.getMessage("fms.facility.assignment.oneDay"));
			DAY_SETTING.put("2", app.getMessage("fms.facility.assignment.twoDay"));
			DAY_SETTING.put("3", app.getMessage("fms.facility.assignment.threeDay"));
			
			DAY_AUTO_SETTING.put("1", app.getMessage("fms.facility.assignment.oneDay"));
			DAY_AUTO_SETTING.put("2", app.getMessage("fms.facility.assignment.twoDay"));
			DAY_AUTO_SETTING.put("3", app.getMessage("fms.facility.assignment.threeDay"));
			DAY_AUTO_SETTING.put("4", app.getMessage("fms.facility.assignment.fourDay"));
			DAY_AUTO_SETTING.put("5", app.getMessage("fms.facility.assignment.fiveDay"));
			DAY_AUTO_SETTING.put("6", app.getMessage("fms.facility.assignment.sixDay"));
			DAY_AUTO_SETTING.put("7", app.getMessage("fms.facility.assignment.sevenDay"));
			
			
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		
		//Service Map Setup
		EngineeringDao dao= (EngineeringDao)getDao();
		try {
			Collection col=dao.selectServices();
			for(Iterator itr=col.iterator();itr.hasNext();){
				Service service=(Service)itr.next();
					SERVICES_MAP.put(service.getServiceId(), service.getDisplayTitle());
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		
		//Request Type Map Setup
		try {
			REQUEST_TYPE_MAP.put(REQUEST_TYPE_INTERNAL, app.getMessage("fms.facility.requestType.I"));
			REQUEST_TYPE_MAP.put(REQUEST_TYPE_EXTERNAL, app.getMessage("fms.facility.requestType.E"));
			REQUEST_TYPE_MAP.put(REQUEST_TYPE_NONPROGRAM, app.getMessage("fms.facility.requestType.N"));
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		
		//Program Type Map Setup
		try {
			PROGRAM_TYPE_MAP.put(PROGRAM_TYPE_LIVE, app.getMessage("fms.facility.programType.L"));
			PROGRAM_TYPE_MAP.put(PROGRAM_TYPE_RECORDING, app.getMessage("fms.facility.programType.R"));
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		
//		Program Type Map Setup
		try {
			STATE_MAP.put(STATE_ADHOC, app.getMessage("fms.facility.state.adhoc"));
			STATE_MAP.put(STATE_LATE, app.getMessage("fms.facility.state.late"));
			STATE_MAP.put(STATE_NORMAL, app.getMessage("fms.facility.state.normal"));
			STATE_MAP.put("", "-");
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		
		//Assignment Status Map Setup
		try {
			ASSIGNMENT_MANPOWER_STATUS_MAP.put(ASSIGNMENT_MANPOWER_STATUS_NEW, app.getMessage("fms.engineering.request.status.new"));
			ASSIGNMENT_MANPOWER_STATUS_MAP.put(ASSIGNMENT_MANPOWER_STATUS_ASSIGNED, app.getMessage("fms.engineering.request.status.assigned"));
			ASSIGNMENT_MANPOWER_STATUS_MAP.put(ASSIGNMENT_MANPOWER_STATUS_COMPLETED, app.getMessage("fms.engineering.request.status.completed"));
			ASSIGNMENT_MANPOWER_STATUS_MAP.put(ASSIGNMENT_MANPOWER_STATUS_UNFULFILLED, app.getMessage("fms.engineering.request.status.unfulfilled"));
			ASSIGNMENT_MANPOWER_STATUS_MAP.put(ASSIGNMENT_MANPOWER_STATUS_CANCEL, app.getMessage("fms.engineering.request.status.cancelled"));
			
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		
		try {
			ASSIGNMENT_FACILITY_STATUS_MAP.put(ASSIGNMENT_FACILITY_STATUS_NEW, app.getMessage("fms.engineering.request.status.new"));
			ASSIGNMENT_FACILITY_STATUS_MAP.put(ASSIGNMENT_FACILITY_STATUS_PREPARE_CHECKOUT, app.getMessage("fms.facility.prepareCheckout"));
			ASSIGNMENT_FACILITY_STATUS_MAP.put(ASSIGNMENT_FACILITY_STATUS_CHECKOUT, app.getMessage("fms.engineering.request.status.checkedOut"));
			ASSIGNMENT_FACILITY_STATUS_MAP.put(ASSIGNMENT_FACILITY_STATUS_CHECKIN, app.getMessage("fms.engineering.request.status.checkedIn"));
			ASSIGNMENT_FACILITY_STATUS_MAP.put(ASSIGNMENT_FACILITY_STATUS_UNFULFILLED, app.getMessage("fms.engineering.request.status.unfulfilled"));
			ASSIGNMENT_FACILITY_STATUS_MAP.put(ASSIGNMENT_FACILITY_STATUS_CANCEL, app.getMessage("fms.engineering.request.status.cancelled"));
			ASSIGNMENT_FACILITY_STATUS_MAP.put(NOT_UTILIZED_ITEM, app.getMessage("fms.facility.notUtilized"));
			
			ASSIGNMENT_HOU_FACILITY_STATUS_MAP.put(ASSIGNMENT_FACILITY_STATUS_NEW, app.getMessage("fms.engineering.request.status.new"));
			ASSIGNMENT_HOU_FACILITY_STATUS_MAP.put(ASSIGNMENT_FACILITY_STATUS_CHECKIN, app.getMessage("fms.engineering.request.status.completed"));
			ASSIGNMENT_HOU_FACILITY_STATUS_MAP.put(ASSIGNMENT_FACILITY_STATUS_UNFULFILLED, app.getMessage("fms.engineering.request.status.unfulfilled"));
			
			ASSIGNMENT_ALL_STATUS_MAP.put(ASSIGNMENT_MANPOWER_STATUS_NEW, app.getMessage("fms.engineering.request.status.new"));
			ASSIGNMENT_ALL_STATUS_MAP.put(ASSIGNMENT_MANPOWER_STATUS_ASSIGNED, app.getMessage("fms.engineering.request.status.assigned"));
			ASSIGNMENT_ALL_STATUS_MAP.put(ASSIGNMENT_MANPOWER_STATUS_COMPLETED, app.getMessage("fms.engineering.request.status.completed"));
			ASSIGNMENT_ALL_STATUS_MAP.put(ASSIGNMENT_FACILITY_STATUS_CHECKIN, app.getMessage("fms.engineering.request.status.checkedIn"));
			ASSIGNMENT_ALL_STATUS_MAP.put(ASSIGNMENT_FACILITY_STATUS_CHECKOUT, app.getMessage("fms.engineering.request.status.checkedOut"));
			ASSIGNMENT_ALL_STATUS_MAP.put(ASSIGNMENT_MANPOWER_STATUS_UNFULFILLED, app.getMessage("fms.engineering.request.status.unfulfilled"));
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		
		super.init();
	}
	
	public static boolean isFMSAdmin(String userId){
		SecurityService security=(SecurityService)Application.getInstance().getService(SecurityService.class);
		boolean hasPermission = false;
		try {
			hasPermission = security.hasPermission(userId, FMS_AMDINISTRATOR_PERMISSION, null, null);
		}
		catch(SecurityException error) {
		}
		return hasPermission;
	}
	
	public static boolean isFCHead(String userId){
		SecurityService security=(SecurityService)Application.getInstance().getService(SecurityService.class);
		boolean hasPermission = false;
		try {
			hasPermission = security.hasPermission(userId, FC_HEAD_PERMISSION, null, null);
		}
		catch(SecurityException error) {
		}
		return hasPermission;
	}
	
	public void insertRequestStatus(String requestId, String status,String remarks,String additionalInfo){
		EngineeringDao dao=(EngineeringDao)getDao();
		
		try {
			Status statusObj=new Status();
			statusObj.setRequestId(requestId);
			statusObj.setStatus(status);
			statusObj.setRemarks(remarks);
			statusObj.setAdditionalInfo(additionalInfo);
			statusObj.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			statusObj.setCreatedDate(now);
			dao.insertStatus(statusObj);
			
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(requestId, "STATUS_INSERT", "[" + statusObj.getStatusLabel() + "] remarks=" + remarks + " additionalInfo=" + additionalInfo);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Cannot insert to status trail (requestId=" + requestId + " status=" + status + ")", e);
		}
	}
	
	public void updateRequestStatus(String requestId, String status) {
		/* Note: does not update the remarks & additionalInfo field */
		EngineeringDao dao=(EngineeringDao)getDao();
		
		try {
			Status statusObj=new Status();
			statusObj.setRequestId(requestId);
			statusObj.setStatus(status);
			statusObj.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			statusObj.setCreatedDate(now);
			dao.updateStatusPartial(statusObj);
			
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(requestId, "STATUS_UPDATE", "[" + statusObj.getStatusLabel() + "]");
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Cannot update to status trail (requestId=" + requestId + " status=" + status + ")", e);
		}
	}
	
	public void updateRequestStatus(String requestId, String status, String remarks, String additionalInfo) {
		EngineeringDao dao=(EngineeringDao)getDao();
		
		try {
			Status statusObj=new Status();
			statusObj.setRequestId(requestId);
			statusObj.setStatus(status);
			statusObj.setRemarks(remarks);
			statusObj.setAdditionalInfo(additionalInfo);
			statusObj.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			statusObj.setCreatedDate(now);
			dao.updateStatusFull(statusObj);
			
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(requestId, "STATUS_UPDATE", "[" + statusObj.getStatusLabel() + "] remarks=" + remarks + " additionalInfo=" + additionalInfo);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
		}
	}
	
	public void insertRequest(EngineeringRequest eRequest){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			eRequest.setRequestId(new Sequence(Sequence.TYPE_ENGINEERING).genarteCode());
			eRequest.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			eRequest.setCreatedOn(now);
			eRequest.setModifiedOn(now);
			eRequest.setCancelFlag("0");
			
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(eRequest, "CREATE_REQUEST", null);
			
			dao.insertRequest(eRequest);
			insertRequestService(eRequest);
			insertRequestStatus(eRequest.getRequestId(),DRAFT_STATUS,"","");
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void insertRequestService(EngineeringRequest eRequest){
		TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			int deletedRows = dao.deleteRequestService(eRequest.getRequestId());
			
			// logging
			transLog.info(eRequest.getRequestId(), "REQUEST_SERVICE_CLEAR", "deletedRows=" + deletedRows);
			
			Collection services=(Collection)eRequest.getServices();
			if(services!=null){
				for(Iterator itr=services.iterator();itr.hasNext();){
					Service service=(Service)itr.next();
					service.setRequestId(eRequest.getRequestId());
					dao.insertRequestService(service);
					
					// logging
					String title = Application.getInstance().getMessage("fms.facility.msg.service." + service.getServiceId());
					transLog.info(eRequest.getRequestId(), "REQUEST_SERVICE_INSERT", "[" + title + "] serviceId=" + service.getServiceId());
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void updateRequestService(String feedType,String requestId,String serviceId){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			Service service=new Service();
			service.setFeedType(feedType);
			service.setRequestId(requestId);
			service.setServiceId(serviceId);
			dao.updateRequestService(service);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void updateRequest(EngineeringRequest eRequest){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			eRequest.setModifiedOn(now);
			dao.updateRequest(eRequest);
			insertRequestService(eRequest);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void editRequest(EngineeringRequest eRequest){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			eRequest.setModifiedOn(now);

			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(eRequest, "UPDATE_REQUEST", null);
			
			dao.updateRequest(eRequest);
			
			
			if (dao.searchModifyStatus(eRequest.getRequestId())) {
				updateRequestStatus(eRequest.getRequestId(),MODIFY_STATUS);
			} else {
				insertRequestStatus(eRequest.getRequestId(),MODIFY_STATUS,"","");
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void updateCheckOutEquipment(EngineeringRequest eRequest) {
		EngineeringDao dao = (EngineeringDao)getDao();
		try {
			eRequest.setStatus(ASSIGNMENT_FACILITY_STATUS_CHECKOUT);
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			eRequest.setModifiedOn(now);
			eRequest.setCheckedOutDate(now);
			
			dao.updateCheckOutEquipment(eRequest);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
	}
	
	public void insertCheckOutEquipment(EngineeringRequest eRequest) {
		EngineeringDao dao = (EngineeringDao)getDao();
		try {
			
			eRequest.setAssignmentEquipmentId(UuidGenerator.getInstance().getUuid());
			eRequest.setStatus(ASSIGNMENT_FACILITY_STATUS_CHECKOUT);
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			eRequest.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			eRequest.setModifiedOn(now);
			eRequest.setCreatedOn(now);
			eRequest.setCheckedOutDate(now);
			
			dao.insertCheckOutEquipment(eRequest);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
	}
	
	public void insertCheckOutEquipmentByGroup(EngineeringRequest eRequest) {
		EngineeringDao dao = (EngineeringDao)getDao();
		try {
			
			eRequest.setAssignmentEquipmentId(UuidGenerator.getInstance().getUuid());
			eRequest.setStatus(ASSIGNMENT_FACILITY_STATUS_CHECKOUT);
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			eRequest.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			eRequest.setModifiedOn(now);
			eRequest.setCreatedOn(now);
			eRequest.setCheckedOutDate(now);
			
			dao.insertCheckOutEquipmentByGroup(eRequest);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
	}
	
	public void updateCheckInEquipment(EngineeringRequest eRequest) {
		EngineeringDao dao = (EngineeringDao)getDao();
		try {
			eRequest.setStatus(ASSIGNMENT_FACILITY_STATUS_CHECKIN);
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now = new Date();
			eRequest.setModifiedOn(now);
			eRequest.setCheckedInDate(now);
			
			dao.updateCheckInEquipment(eRequest);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
	}
	
	public void copyRequest(EngineeringRequest eRequest, String oldRequestId){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			
			eRequest.setStatus(DRAFT_STATUS);
			eRequest.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			eRequest.setCreatedOn(now);
			eRequest.setModifiedOn(now);
			eRequest.setState(null);
			
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(eRequest.getRequestId(), "COPY", "oldRequestId=" + oldRequestId);
			
			copyServices(eRequest, oldRequestId);
			
			dao.insertRequest(eRequest);
			insertRequestService(eRequest);
			insertRequestStatus(eRequest.getRequestId(),DRAFT_STATUS , "", "");
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	/**
	 * 
	 * Used in copyRequest()
	 * 
	 * @param eRequest
	 * @param oldRequestId
	 */
	public void copyServices(EngineeringRequest eRequest, String oldRequestId) {
		Collection scpCol 	= getScpService(oldRequestId, ServiceDetailsForm.SERVICE_SCPMCP);
		Collection postCol 	= getPostProductionService(oldRequestId, ServiceDetailsForm.SERVICE_POSTPRODUCTION);
		Collection vtrCol	= getVtrService(oldRequestId, ServiceDetailsForm.SERVICE_VTR);
		Collection manCol 	= getManpowerService(oldRequestId, ServiceDetailsForm.SERVICE_MANPOWER);
		Collection stdCol	= getStudioService(oldRequestId, ServiceDetailsForm.SERVICE_STUDIO);
		Collection othCol	= getOtherService(oldRequestId, ServiceDetailsForm.SERVICE_OTHER);
		Collection tvroCol	= getTvroService(oldRequestId, ServiceDetailsForm.SERVICE_TVRO);
		
		if (scpCol!=null && scpCol.size()>0) {
			for (Iterator iscp = scpCol.iterator(); iscp.hasNext();){
				ScpService scp = (ScpService)iscp.next();
				scp.setRequestId(eRequest.getRequestId());
				insertScpService(scp);
			}
		}
		
		if (postCol!=null && postCol.size()>0) {
			for (Iterator ipost = postCol.iterator(); ipost.hasNext();){
				PostProductionService post = (PostProductionService)ipost.next();
				post.setRequestId(eRequest.getRequestId());
				insertPostProductionService(post);
			}
		}
		
		if (vtrCol!=null && vtrCol.size()>0) {
			for (Iterator ivtr = vtrCol.iterator(); ivtr.hasNext();) {
				VtrService vtr = (VtrService)ivtr.next();
				vtr.setRequestId(eRequest.getRequestId());
				
				Collection files = getFiles(vtr.getId());
				
				vtr.setId(UuidGenerator.getInstance().getUuid());
				
				if (files!=null && files.size()>0){
					for (Iterator vtrFiles = files.iterator(); vtrFiles.hasNext();){
						VtrService vtrFile = (VtrService)vtrFiles.next();
						
						vtrFile.setId(vtr.getId());
						vtrFile.setFileId(UuidGenerator.getInstance().getUuid());
						vtrFile.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
						Date now=new Date();						
						vtrFile.setModifiedDate(now);
						
						insertVtrAttachment(vtrFile);
					}
				}
				
				insertCopyVtrService(vtr);
			}
		}
		
		if (manCol!=null && manCol.size()>0) {
			for (Iterator iman = manCol.iterator(); iman.hasNext();){
				ManpowerService man = (ManpowerService)iman.next();
				man.setRequestId(eRequest.getRequestId());
				insertManpowerService(man);
			}
		}
		
		if (stdCol!=null && stdCol.size()>0) {
			for (Iterator istd = stdCol.iterator(); istd.hasNext();){
				StudioService std = (StudioService)istd.next();
				std.setRequestId(eRequest.getRequestId());
				insertStudioService(std);
			}
		}
		
		if (othCol!=null && othCol.size()>0) {
			for (Iterator ioth = othCol.iterator(); ioth.hasNext();) {
				OtherService oth = (OtherService)ioth.next();
				oth.setRequestId(eRequest.getRequestId());
				insertOtherService(oth);
			}
		}
		
		if (tvroCol!=null && tvroCol.size()>0){
			for (Iterator itvro = tvroCol.iterator(); itvro.hasNext();){
				TvroService tvro = (TvroService)itvro.next();
				tvro.setRequestId(eRequest.getRequestId());
				insertTvroService(tvro);
			}
		}
	}
	
	public void submitRequest(String requestId){
		submitRequest(requestId,false);
	}
	
	public void submitRequest(String requestId, boolean hasConflict){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			EngineeringRequest eRequest=new EngineeringRequest();
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			eRequest.setModifiedOn(now);
			eRequest.setSubmittedDate(now);
			EngineeringRequest request=getRequestWithService(requestId);
			if(ASSIGNMENT_STATUS.equals(request.getStatus()))
				eRequest.setStatus(ASSIGNMENT_STATUS);
			else
				eRequest.setStatus(PENDING_STATUS);
			
			eRequest.setRequestId(requestId);
			dao.submitRequest(eRequest);
			
			if (hasConflict) {
				String additionalInfo = "<font style='background-color:red;color:#fff'>Some facilities are not available</font>";
				
				if (ASSIGNMENT_STATUS.equals(request.getStatus())) {
					if (dao.searchModifyStatus(eRequest.getRequestId())) {
						updateRequestStatus(eRequest.getRequestId(),MODIFY_STATUS);
					} else {
						insertRequestStatus(eRequest.getRequestId(),MODIFY_STATUS,"","");
					}
				} else {
					insertRequestStatus(requestId,PENDING_STATUS , "", additionalInfo);
				}
			} else {
				insertRequestStatus(requestId,PENDING_STATUS , "", "");
			}
			
			sendSubmissionEmail(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
		}
	}
	
	public void submitModifyRequest(String requestId){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			EngineeringRequest eRequest=new EngineeringRequest();
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			eRequest.setModifiedOn(now);
			eRequest.setSubmittedDate(now);
			eRequest.setStatus(ASSIGNMENT_STATUS);
			eRequest.setRequestId(requestId);
			dao.submitRequest(eRequest);		
			
			// remove pending modification additionalInfo
			if (dao.searchModifyStatus(eRequest.getRequestId())) {
				updateRequestStatus(eRequest.getRequestId(),MODIFY_STATUS,"","");
			} else {
				insertRequestStatus(eRequest.getRequestId(),MODIFY_STATUS,"","");
			}
			
			try{
				// create transport request if any
				EngineeringRequest tRequest= this.getRequestWithService(requestId);
				this.createTransportRequest(tRequest);
			}catch(Exception e){
				Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
			}
			sendSubmissionEmail(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
		}
	}
	
	public void pendingModifyRequest(String requestId) {
		EngineeringDao dao = (EngineeringDao) getDao();
		try {
			String additionalInfo = "<font style='background-color:#cc00cc;color:#ffffff'>Pending modification</font>";
			
			// add pending modification additionalInfo
			if (dao.searchModifyStatus(requestId)) {
				updateRequestStatus(requestId, MODIFY_STATUS, "", additionalInfo);
			} else {
				insertRequestStatus(requestId, MODIFY_STATUS, "", additionalInfo);
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
		}
	}
	
	public void cancelRequest(String requestId, String reason, String status){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			boolean toRequestor = false;
			boolean toHOD = false;
			boolean toAllFC = false;
			boolean toFC = false;
			
			EngineeringRequest eRequest=new EngineeringRequest();
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			eRequest.setModifiedOn(now);
			eRequest.setRequestId(requestId);
			
			if (ASSIGNMENT_STATUS.equals(status) 
					|| APPLIED_CANCELLATION.equals(status) 
					|| FC_ASSIGNED_STATUS.equals(status)
					|| OUTSOURCED_STATUS.equals(status)){
				if(dao.isOutsource(requestId))
					eRequest.setStatus(CANCELLED_STATUS);
				else	
					eRequest.setStatus(APPLIED_CANCELLATION);
				toFC = true;
			} else if (PROCESS_STATUS.equals(status)) {
				eRequest.setStatus(CANCELLED_STATUS);
				toAllFC = true;
				toHOD = true;
			} else if(PENDING_STATUS.equals(status)){
				eRequest.setStatus(CANCELLED_STATUS);
				toHOD = true;
			} else {
				eRequest.setStatus(CANCELLED_STATUS);
				//toFC = false;
			}
			
			dao.updateRequestStatus(eRequest);
			dao.deleteBooking(requestId);
			dao.deleteReport(requestId);
			insertRequestStatus(requestId, eRequest.getStatus() , "", reason);
			
			sendCancellationEmail(requestId, toFC, toRequestor, toAllFC, toHOD);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
		}
	}
	
	public void deleteReport(String requestId){
		
		EngineeringDao dao=(EngineeringDao)getDao();
		try{
			dao.deleteReport(requestId);
		}catch(DaoException e){
			Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
		}
	}
	
	public void assignFC(String requestId,String fcId){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			EngineeringRequest eRequest=new EngineeringRequest();
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			eRequest.setModifiedOn(now);
			eRequest.setStatus(FC_ASSIGNED_STATUS);
			eRequest.setRequestId(requestId);
			eRequest.setFcId(fcId);
			dao.assignFC(eRequest);
			insertRequestStatus(requestId,FC_ASSIGNED_STATUS , "", "");
			
			sendAssignEmail(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
		}
	}
	
	public void approveRequest(String requestId, String state, String remarks){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			EngineeringRequest eRequest=new EngineeringRequest();
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			eRequest.setModifiedOn(now);
			eRequest.setStatus(PROCESS_STATUS);
			eRequest.setApprovedBy(Application.getInstance().getCurrentUser().getUsername());
			eRequest.setApproverRemarks(remarks);
			eRequest.setApprovedDate(now);
			eRequest.setRequestId(requestId);
			eRequest.setState(state);
			dao.approveRequest(eRequest);
			insertRequestStatus(requestId,PROCESS_STATUS ,remarks, "");
			
			sendApprovalEmail(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
		}
	}
	
	public void rejectRequest(String requestId,String remarks){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			EngineeringRequest eRequest=new EngineeringRequest();
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			eRequest.setModifiedOn(now);
			eRequest.setStatus(REJECTED_STATUS);
			eRequest.setRequestId(requestId);
			dao.updateRequestStatus(eRequest);
			insertRequestStatus(requestId,REJECTED_STATUS ,remarks, "");
			
			sendRejectionEmail(requestId, remarks);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
		}
	}
	
	public void acceptRequest(String requestId, boolean isOutsourced){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			String status = ASSIGNMENT_STATUS;
			//String status = ACCEPT_STATUS;
			
			if (isOutsourced) {
				status = OUTSOURCED_STATUS;
			}
			EngineeringRequest eRequest=new EngineeringRequest();
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			eRequest.setModifiedOn(now);
			eRequest.setStatus(status);
			eRequest.setRequestId(requestId);
			
			if (isOutsourced) {
			} else {	
				insertBooking(requestId);
			}
			dao.updateRequestStatus(eRequest);
			insertRequestStatus(requestId, status , "", "");
			
			sendAcceptanceEmail(requestId, isOutsourced);
		} catch (Exception e) {
			Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
		}
	}
	
	public void closeRequest(String requestId, String remarks){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			EngineeringRequest eRequest=new EngineeringRequest();
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			eRequest.setModifiedOn(now);
			eRequest.setStatus(CLOSED_STATUS);
			eRequest.setRequestId(requestId);
			eRequest.setRemarks(remarks);
			
			dao.closeRequest(eRequest);
			insertRequestStatus(requestId,CLOSED_STATUS , "" , "");
			
		} catch (Exception e) {
			Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
		}
	}
		
	public void createTransportRequest(EngineeringRequest eRequest) {
		SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		TransportModule tm = (TransportModule) Application.getInstance().getModule(TransportModule.class);
		
		Collection scpCol 	= getScpService(eRequest.getRequestId(), ServiceDetailsForm.SERVICE_SCPMCP);
		Collection postCol 	= getPostProductionService(eRequest.getRequestId(), ServiceDetailsForm.SERVICE_POSTPRODUCTION);
		Collection vtrCol	= getVtrService(eRequest.getRequestId(), ServiceDetailsForm.SERVICE_VTR);
		Collection manCol 	= getManpowerService(eRequest.getRequestId(), ServiceDetailsForm.SERVICE_MANPOWER);
		Collection stdCol	= getStudioService(eRequest.getRequestId(), ServiceDetailsForm.SERVICE_STUDIO);
		Collection othCol	= getOtherService(eRequest.getRequestId(), ServiceDetailsForm.SERVICE_OTHER);
		Collection tvroCol	= getTvroService(eRequest.getRequestId(), ServiceDetailsForm.SERVICE_TVRO);
		
		// prepare for TransportRequest object
		TransportRequest tr = new TransportRequest();
		VehicleRequest vr = new VehicleRequest();
		
		tr.setRequestTitle(eRequest.getTitle());
		tr.setRequestType(eRequest.getRequestType());
		if (!(eRequest.getProgram()==null || "".equals(eRequest.getProgram()))){
			tr.setProgram(eRequest.getProgram());
		}
		String purpose = "Forwarded request from Facility Request (" + eRequest.getRequestId() + "). " +
		"Requestor : " + Application.getInstance().getCurrentUser().getName();

		tr.setPurpose(purpose);
		//tr.setRemarks(eRequest.getDescription());
		tr.setRemarks("");
		tr.setStatus(com.tms.fms.setup.model.SetupModule.PROCESS_STATUS);
		
		tr.setRequestBy(Application.getInstance().getCurrentUser().getId());
		tr.setRequestDate(new Date());
		tr.setUpdatedBy(Application.getInstance().getCurrentUser().getId());
		tr.setUpdatedDate(new Date());
		tr.setStatusRequest(STATE_ADHOC.equals(eRequest.getState())?"H":eRequest.getState());
		tr.setEngineeringRequestId(eRequest.getRequestId());
		
		try {
		if (scpCol!=null && scpCol.size()>0) {
			for (Iterator iscp = scpCol.iterator(); iscp.hasNext();){
				ScpService scp = (ScpService)iscp.next();
				
				RateCard rc = module.getRateCard(scp.getFacilityId());
				
				if ("1".equals(rc.getTransportRequest()) && (rc.getVehicleCategoryId()!=null)){
					
					tr.setId(new Sequence(Sequence.TYPE_TRANSPORT).genarteCode());
					
					Calendar start = Calendar.getInstance();				 
					Calendar end = Calendar.getInstance();	
					
					start.setTime(scp.getRequiredFrom());
					start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(scp.getDepartureTime().split(":")[0]));
					start.set(Calendar.MINUTE, Integer.parseInt(scp.getDepartureTime().split(":")[1]));		
					
					end.setTime(scp.getRequiredTo());
					end.set(Calendar.HOUR_OF_DAY,Integer.parseInt(scp.getRecordingTo().split(":")[0]));
					end.set(Calendar.MINUTE,Integer.parseInt(scp.getRecordingTo().split(":")[1]));
					
					tr.setStartDate(start.getTime());
					tr.setEndDate(end.getTime());
					tr.setBlockBooking(scp.getBlockBooking());
					tr.setDestination(scp.getLocation());	
					
					vr.setId(UuidGenerator.getInstance().getUuid());
					vr.setCategory_id(rc.getVehicleCategoryId());
					vr.setRequestId(tr.getId());
					vr.setQuantity(1);
					vr.setDriver(1);					
					
					tm.addTransportRequest(tr);
					tm.insertTransportVehicle(vr);
					tm.insertRequestStatus(tr.getId(), tr.getStatus(), tr.getReason());
				}
			}
		}
		
		if (postCol!=null && postCol.size()>0) {
			for (Iterator ipost = postCol.iterator(); ipost.hasNext();){
				PostProductionService post = (PostProductionService)ipost.next();
				
				RateCard rc = module.getRateCard(post.getFacilityId());
				
				if ("1".equals(rc.getTransportRequest()) && (rc.getVehicleCategoryId()!=null)){
					
					tr.setId(new Sequence(Sequence.TYPE_TRANSPORT).genarteCode());
					
					Calendar start = Calendar.getInstance();				 
					Calendar end = Calendar.getInstance();	
					
					start.setTime(post.getRequiredDate());
					start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(post.getFromTime().split(":")[0]));
					start.set(Calendar.MINUTE, Integer.parseInt(post.getFromTime().split(":")[1]));		
					
					end.setTime(post.getRequiredDate());
					end.set(Calendar.HOUR_OF_DAY,Integer.parseInt(post.getToTime().split(":")[0]));
					end.set(Calendar.MINUTE,Integer.parseInt(post.getToTime().split(":")[1]));
					
					tr.setStartDate(start.getTime());
					tr.setEndDate(end.getTime());
					tr.setBlockBooking(post.getBlockBooking());
					tr.setDestination("");					
					
					vr.setId(UuidGenerator.getInstance().getUuid());
					vr.setCategory_id(rc.getVehicleCategoryId());
					vr.setRequestId(tr.getId());
					vr.setQuantity(1);
					vr.setDriver(1);
					
					tm.addTransportRequest(tr);
					tm.insertTransportVehicle(vr);
					tm.insertRequestStatus(tr.getId(), tr.getStatus(), tr.getReason());
				}
			}
		}
		
		if (vtrCol!=null && vtrCol.size()>0) {
			for (Iterator ivtr = vtrCol.iterator(); ivtr.hasNext();){
				VtrService vtr = (VtrService)ivtr.next();
				
				RateCard rc = module.getRateCard(vtr.getFacilityId());
				
				if ("1".equals(rc.getTransportRequest()) && (rc.getVehicleCategoryId()!=null)){
					
					tr.setId(new Sequence(Sequence.TYPE_TRANSPORT).genarteCode());
					
					Calendar start = Calendar.getInstance();				 
					Calendar end = Calendar.getInstance();	
					
					start.setTime(vtr.getRequiredDate());
					start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(vtr.getRequiredFrom().split(":")[0]));
					start.set(Calendar.MINUTE, Integer.parseInt(vtr.getRequiredFrom().split(":")[1]));		
					
					end.setTime(vtr.getRequiredDate());
					end.set(Calendar.HOUR_OF_DAY,Integer.parseInt(vtr.getRequiredTo().split(":")[0]));
					end.set(Calendar.MINUTE,Integer.parseInt(vtr.getRequiredTo().split(":")[1]));
					
					tr.setStartDate(start.getTime());
					tr.setEndDate(end.getTime());
					tr.setBlockBooking(vtr.getBlockBooking());
					tr.setDestination("");			
					tr.setRemarks(vtr.getRemarks());
					
					vr.setId(UuidGenerator.getInstance().getUuid());
					vr.setCategory_id(rc.getVehicleCategoryId());
					vr.setRequestId(tr.getId());
					vr.setQuantity(1);
					vr.setDriver(1);
					
					tm.addTransportRequest(tr);
					tm.insertTransportVehicle(vr);
					tm.insertRequestStatus(tr.getId(), tr.getStatus(), tr.getReason());
				}
			}
		}
		
		
		if (manCol!=null && manCol.size()>0) {
			for (Iterator iman = manCol.iterator(); iman.hasNext();){
				ManpowerService man = (ManpowerService)iman.next();
				
				RateCard rc = module.getRateCard(man.getCompetencyId());
				
				if ("1".equals(rc.getTransportRequest()) && (rc.getVehicleCategoryId()!=null)){
					
					tr.setId(new Sequence(Sequence.TYPE_TRANSPORT).genarteCode());
					
					Calendar start = Calendar.getInstance();				 
					Calendar end = Calendar.getInstance();	
					
					start.setTime(man.getRequiredFrom());
					start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(man.getFromTime().split(":")[0]));
					start.set(Calendar.MINUTE, Integer.parseInt(man.getFromTime().split(":")[1]));		
					
					end.setTime(man.getRequiredTo());
					end.set(Calendar.HOUR_OF_DAY,Integer.parseInt(man.getToTime().split(":")[0]));
					end.set(Calendar.MINUTE,Integer.parseInt(man.getToTime().split(":")[1]));
					
					tr.setStartDate(start.getTime());
					tr.setEndDate(end.getTime());
					tr.setBlockBooking(man.getBlockBooking());
					tr.setDestination("");			
					tr.setRemarks(man.getRemarks());
					
					vr.setId(UuidGenerator.getInstance().getUuid());
					vr.setCategory_id(rc.getVehicleCategoryId());
					vr.setRequestId(tr.getId());
					vr.setQuantity(1);
					vr.setDriver(1);
					
					tm.addTransportRequest(tr);
					tm.insertTransportVehicle(vr);
					tm.insertRequestStatus(tr.getId(), tr.getStatus(), tr.getReason());
				}
			}
		}
		
		if (stdCol!=null && stdCol.size()>0) {
			for (Iterator istd = stdCol.iterator(); istd.hasNext();){
				StudioService std = (StudioService)istd.next();
				
				RateCard rc = module.getRateCard(std.getFacilityId());
				
				if ("1".equals(rc.getTransportRequest()) && (rc.getVehicleCategoryId()!=null)){
					
					tr.setId(new Sequence(Sequence.TYPE_TRANSPORT).genarteCode());
					
					Calendar start = Calendar.getInstance();				 
					Calendar end = Calendar.getInstance();	
					
					start.setTime(std.getBookingDate());
					start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(std.getRequiredFrom().split(":")[0]));
					start.set(Calendar.MINUTE, Integer.parseInt(std.getRequiredFrom().split(":")[1]));		
					
					end.setTime(std.getBookingDate());
					end.set(Calendar.HOUR_OF_DAY,Integer.parseInt(std.getRequiredTo().split(":")[0]));
					end.set(Calendar.MINUTE,Integer.parseInt(std.getRequiredTo().split(":")[1]));
					
					tr.setStartDate(start.getTime());
					tr.setEndDate(end.getTime());
					tr.setBlockBooking(std.getBlockBooking());
					tr.setDestination("");			
					
					vr.setId(UuidGenerator.getInstance().getUuid());
					vr.setCategory_id(rc.getVehicleCategoryId());
					vr.setRequestId(tr.getId());
					vr.setQuantity(1);
					vr.setDriver(1);
					
					tm.addTransportRequest(tr);
					tm.insertTransportVehicle(vr);
					tm.insertRequestStatus(tr.getId(), tr.getStatus(), tr.getReason());
				}
			}
		}
		
		if (othCol!=null && othCol.size()>0) {
			for (Iterator ioth = othCol.iterator(); ioth.hasNext();){
				OtherService oth = (OtherService)ioth.next();
				
				RateCard rc = module.getRateCard(oth.getFacilityId());
				
				if ("1".equals(rc.getTransportRequest()) && (rc.getVehicleCategoryId()!=null)){
					
					tr.setId(new Sequence(Sequence.TYPE_TRANSPORT).genarteCode());
					
					Calendar start = Calendar.getInstance();				 
					Calendar end = Calendar.getInstance();	
					
					start.setTime(oth.getRequiredFrom());
					start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(oth.getFromTime().split(":")[0]));
					start.set(Calendar.MINUTE, Integer.parseInt(oth.getFromTime().split(":")[1]));		
					
					end.setTime(oth.getRequiredTo());
					end.set(Calendar.HOUR_OF_DAY,Integer.parseInt(oth.getToTime().split(":")[0]));
					end.set(Calendar.MINUTE,Integer.parseInt(oth.getToTime().split(":")[1]));
					
					tr.setStartDate(start.getTime());
					tr.setEndDate(end.getTime());
					tr.setBlockBooking(oth.getBlockBooking());
					tr.setDestination("");			
					tr.setRemarks(oth.getRemarks());
					
					vr.setId(UuidGenerator.getInstance().getUuid());
					vr.setCategory_id(rc.getVehicleCategoryId());
					vr.setRequestId(tr.getId());
					vr.setQuantity(1);
					vr.setDriver(1);
					
					tm.addTransportRequest(tr);
					tm.insertTransportVehicle(vr);
					tm.insertRequestStatus(tr.getId(), tr.getStatus(), tr.getReason());
				}
			}
		}
		
		if (tvroCol!=null && tvroCol.size()>0) {
			for (Iterator itvro = tvroCol.iterator(); itvro.hasNext();){
				TvroService tvro = (TvroService)itvro.next();
				
				//RateCard rcService = module.getRateCardByService(ServiceDetailsForm.SERVICE_TVRO);
				//if (rcService!=null){
					RateCard rc = module.getRateCard(tvro.getFacilityId());
					if ("1".equals(rc.getTransportRequest()) && (rc.getVehicleCategoryId()!=null)){
						
						tr.setId(new Sequence(Sequence.TYPE_TRANSPORT).genarteCode());
						
						Calendar start = Calendar.getInstance();				 
						Calendar end = Calendar.getInstance();	
						
						start.setTime(tvro.getRequiredDate());
						start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tvro.getFromTime().split(":")[0]));
						start.set(Calendar.MINUTE, Integer.parseInt(tvro.getFromTime().split(":")[1]));		
						
						end.setTime(tvro.getRequiredDate());
						end.set(Calendar.HOUR_OF_DAY,Integer.parseInt(tvro.getToTime().split(":")[0]));
						end.set(Calendar.MINUTE,Integer.parseInt(tvro.getToTime().split(":")[1]));
						
						tr.setStartDate(start.getTime());
						tr.setEndDate(end.getTime());
						tr.setBlockBooking(tvro.getBlockBooking());
						tr.setDestination(tvro.getLocation());			
						tr.setRemarks(tvro.getRemarks());
						
						vr.setId(UuidGenerator.getInstance().getUuid());
						vr.setCategory_id(rc.getVehicleCategoryId());
						vr.setRequestId(tr.getId());
						vr.setQuantity(1);
						vr.setDriver(1);
						
						tm.addTransportRequest(tr);
						tm.insertTransportVehicle(vr);
						tm.insertRequestStatus(tr.getId(), tr.getStatus(), tr.getReason());
					}
				//}
			}
		}
		
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error Add Transport Request" + e.getMessage(), e);
		}
	}

	public void insertBooking(String requestId){
		try{
			EngineeringRequest request=getRequestWithService(requestId);
			Collection services=request.getServices();
			for(Iterator<Service> itr=services.iterator();itr.hasNext();){
				Service service=(Service)itr.next();
				String serviceId=service.getServiceId();
				if(ServiceDetailsForm.SERVICE_SCPMCP.equals(serviceId)){
					Collection col=getScpService(requestId, serviceId);
					for(Iterator<ScpService> serItr=col.iterator();serItr.hasNext();){
						ScpService scp=(ScpService)serItr.next();
						bookRateCardFacility(scp.getFacilityId(),requestId,scp.getRequiredFrom(),scp.getRequiredTo(),scp.getDepartureTime(),scp.getRecordingTo(),1, scp.getBlockBooking());
					}
				}else if(ServiceDetailsForm.SERVICE_POSTPRODUCTION.equals(serviceId)){
					Collection col=getPostProductionService(requestId, serviceId);
					for(Iterator<PostProductionService> serItr=col.iterator();serItr.hasNext();){
						PostProductionService post=(PostProductionService)serItr.next();
						bookRateCardFacility(post.getFacilityId(),requestId,post.getRequiredDate(),post.getRequiredDateTo(),post.getFromTime(),post.getToTime(),1, post.getBlockBooking());
					}
				}else if(ServiceDetailsForm.SERVICE_VTR.equals(serviceId)){
					Collection col=getVtrService(requestId, serviceId);
					for(Iterator<VtrService> serItr=col.iterator();serItr.hasNext();){
						VtrService vtr=(VtrService)serItr.next();
						bookRateCardFacility(vtr.getFacilityId(),requestId, vtr.getRequiredDate(),vtr.getRequiredDateTo(),vtr.getRequiredFrom(),vtr.getRequiredTo(),1, vtr.getBlockBooking());
					}
				}else if(ServiceDetailsForm.SERVICE_MANPOWER.equals(serviceId)){
					Collection col=getManpowerService(requestId, serviceId);
					for(Iterator<ManpowerService> serItr=col.iterator();serItr.hasNext();){
						ManpowerService man=(ManpowerService)serItr.next();
						bookRateCardFacility(man.getCompetencyId(),requestId,man.getRequiredFrom(),man.getRequiredTo(),man.getFromTime(),man.getToTime(),man.getQuantity(), man.getBlockBooking());
					}
				}else if(ServiceDetailsForm.SERVICE_STUDIO.equals(serviceId)){
					Collection col=getStudioService(requestId, serviceId);
					for(Iterator<StudioService> serItr=col.iterator();serItr.hasNext();){
						StudioService studio=(StudioService)serItr.next();
						bookRateCardFacility(studio.getFacilityId(),requestId,studio.getBookingDate(),studio.getBookingDateTo(),studio.getRequiredFrom(),studio.getRequiredTo(),1 , studio.getBlockBooking());
					}
				}else if(ServiceDetailsForm.SERVICE_OTHER.equals(serviceId)){
					Collection col=getOtherService(requestId, serviceId);
					for(Iterator<OtherService> serItr=col.iterator();serItr.hasNext();){
						OtherService other=(OtherService)serItr.next();
						bookRateCardFacility(other.getFacilityId(),requestId,other.getRequiredFrom(),other.getRequiredTo(),other.getFromTime(),other.getToTime(),other.getQuantity(), other.getBlockBooking());
					}
				}else if(ServiceDetailsForm.SERVICE_TVRO.equals(serviceId)) {
					Collection col=getTvroService(requestId, serviceId);
					for(Iterator<TvroService> serItr=col.iterator();serItr.hasNext();){
						TvroService tvro = (TvroService) serItr.next();
						bookRateCardFacility(tvro.getFacilityId(),requestId,tvro.getRequiredDate(), tvro.getRequiredDateTo(),tvro.getFromTime(),tvro.getToTime(), 1, tvro.getBlockBooking());
					}
				}
			}
		}catch (Exception e) {
			Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
		}  
	}
	
	public void bookRateCardFacility(String rateCardId,String requestId,Date fromDate,Date toDate,String fromTime,String toTime,int qty, String blockBooking){
		try{
			Application application = Application.getInstance();
			SetupModule module = (SetupModule)application.getModule(SetupModule.class);
			Collection facilities = module.getRateCardEquipment(rateCardId, "");
			Collection manpower = module.getRateCardManpower(rateCardId, "");
			bookFacilities(facilities,requestId,fromDate,toDate,fromTime,toTime,"F",qty, blockBooking);
			bookFacilities(manpower,requestId,fromDate,toDate,fromTime,toTime,"M",qty, blockBooking);
		}catch (Exception e) {
			Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
		}
		
	} 
	
	private void bookFacilities(Collection facilities,String requestId,Date fromDate,Date toDate,String fromTime,String toTime,String type,int qty, String blockBooking){
		EngineeringDao dao=(EngineeringDao)getDao();
		for(Iterator<RateCard> itr=facilities.iterator();itr.hasNext();){
			RateCard rc=(RateCard)itr.next();
			
			String id="";
			int quantity=0;
			if (type!=null && type.equals("F")){
				id=rc.getCategoryId();
				quantity=rc.getEquipmentQty()*qty;
			}else{
				id=rc.getCompetencyId();
				quantity=rc.getManpowerQty()*qty;
			}
			if(id!=null){
				if(blockBooking.equals("0")){
					FacilityObject facility=new FacilityObject();
					facility.setId(UuidGenerator.getInstance().getUuid());
					facility.setFacilityId(id);
					facility.setBookFrom(fromDate);
					facility.setBookTo(toDate);
					facility.setTimeFrom(fromTime.replaceAll(":", ""));
					facility.setTimeTo(toTime.replaceAll(":", ""));
					facility.setCreatedby(Application.getInstance().getCurrentUser().getUsername());
					facility.setCreatedby_date(new Date());
					facility.setUpdatedby(facility.getCreatedby());
					facility.setUpdatedby_date(facility.getCreatedby_date());
					facility.setBookingType(type);
					facility.setQuantity(quantity);
					facility.setRequestId(requestId);
					try {
						dao.insertBooking(facility);
					} catch (DaoException e) {
						Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
					}
				}else if(blockBooking.equals("1")){
					//long interval = toDate.getDate() - fromDate.getDate();
					long interval = dateDiff4Assignment(fromDate,toDate);
					for (int i = 0; i<=interval; i++){
						Date book = (Date) fromDate.clone();
						book.setDate(fromDate.getDate()+i);
						
						FacilityObject facility=new FacilityObject();
						facility.setId(UuidGenerator.getInstance().getUuid());
						facility.setFacilityId(id);
						facility.setBookFrom(book);
						facility.setBookTo(book);
						facility.setTimeFrom(fromTime.replaceAll(":", ""));
						facility.setTimeTo(toTime.replaceAll(":", ""));
						facility.setCreatedby(Application.getInstance().getCurrentUser().getUsername());
						facility.setCreatedby_date(new Date());
						facility.setUpdatedby(facility.getCreatedby());
						facility.setUpdatedby_date(facility.getCreatedby_date());
						facility.setBookingType(type);
						facility.setQuantity(quantity);
						facility.setRequestId(requestId);
						try {
							dao.insertBooking(facility);
						} catch (DaoException e) {
							Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
						}
					}
				}
			}
		}
		
	}
	
	public EngineeringRequest getRequestWithService(String requestId){
		return getRequest(requestId,true);
	}
	public EngineeringRequest getRequest(String requestId){
		return getRequest(requestId,false);
	}
	
	public EngineeringRequest getRequest(String requestId, boolean getServices){
		EngineeringRequest eRequest=null;
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
				eRequest=dao.selectRequest(requestId);
				//eRequest.setCreatedBy(dao.selectName(eRequest.getCreatedBy()));
				//eRequest.setApprovedBy(dao.selectName(eRequest.getApprovedBy()));
				
			/*	if (eRequest==null || eRequest.getProgramType().equals(EngineeringModule.REQUEST_TYPE_EXTERNAL) 
						|| eRequest.getProgramType().equals(EngineeringModule.REQUEST_TYPE_NONPROGRAM)){
					eRequest = dao.selectRequestWOProgram(requestId);
				}*/
				
				if(eRequest!=null && getServices){
					Collection services=dao.selectRequestServices(eRequest.getRequestId());
					eRequest.setServices(services);
				}
		} catch (DaoException e) {
			Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
		}
		return eRequest;
	}
	public Collection getRequest(String search, String requestType, String status, String sort,boolean desc,int start,int rows) throws DaoException{
		Collection col=new ArrayList();
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			col= dao.selectRequest(search,requestType,status,sort,desc,start,rows);
			for(Iterator itr=col.iterator();itr.hasNext();){
				EngineeringRequest eRequest=(EngineeringRequest)itr.next();
				Collection services=dao.selectRequestServices(eRequest.getRequestId());
				eRequest.setServices(services);
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}

	public Collection getHODRequest(String search, String sort,boolean desc,int start,int rows) throws DaoException{
		Collection col=new ArrayList();
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			col= dao.selectHODRequest(search,sort,desc,start,rows);
			for(Iterator itr=col.iterator();itr.hasNext();){
				EngineeringRequest eRequest=(EngineeringRequest)itr.next();
				Collection services=dao.selectRequestServices(eRequest.getRequestId());
				eRequest.setServices(services);
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getHODRequest() throws DaoException{
		Collection col=new ArrayList();
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			col= dao.selectHODRequest();
			for(Iterator itr=col.iterator();itr.hasNext();){
				EngineeringRequest eRequest=(EngineeringRequest)itr.next();
				Collection services=dao.selectRequestServices(eRequest.getRequestId());
				eRequest.setServices(services);
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getAllHODRequest(String search, String status, String sort, boolean desc,int start,int rows) throws DaoException{
		Collection col=new ArrayList();
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			col= dao.selectAllHODRequest(search, status, sort, desc, start, rows);
			for(Iterator itr=col.iterator();itr.hasNext();){
				EngineeringRequest eRequest=(EngineeringRequest)itr.next();
				Collection services=dao.selectRequestServices(eRequest.getRequestId());
				eRequest.setServices(services);
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getHOURequest() throws DaoException{
		Collection col=new ArrayList();
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			col= dao.selectHOURequest();
			for(Iterator itr=col.iterator();itr.hasNext();){
				EngineeringRequest eRequest=(EngineeringRequest)itr.next();
				Collection services=dao.selectRequestServices(eRequest.getRequestId());
				eRequest.setServices(services);
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection selectNotification(String requestId, String type) throws DaoException {
		Collection col = new ArrayList();
		EngineeringDao dao=(EngineeringDao)getDao();
		
		try {			
			col = dao.selectNotification(requestId, type);
		} catch (Exception e){
			Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
		}
		return col;
	}
	
	public void insertNotification(String requestId, String type){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			String notificationId = UuidGenerator.getInstance().getUuid();
			Date now = new Date();
			
			dao.insertNotification(requestId, notificationId, now, type);
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
		}
	}
	
	public Collection getFCHeadRequest(Date requiredFrom, Date requiredTo, String departmentId, String search, String sort,boolean desc,int start,int rows) throws DaoException{
		Collection col=new ArrayList();
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			col= dao.selectFCHeadRequest(requiredFrom, requiredTo, departmentId,search,sort,desc,start,rows);
			for(Iterator itr=col.iterator();itr.hasNext();){
				EngineeringRequest eRequest=(EngineeringRequest)itr.next();
				Collection services=dao.selectRequestServices(eRequest.getRequestId());
				eRequest.setServices(services);
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getFCHeadAllRequestListByBarcode( String barcode, int top ) {
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			return dao.getFCHeadAllRequestListByBarcode( barcode, top );			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return new ArrayList();
	}
	
	public Collection getFCHeadRequestListByBarcode( String barcode ) {
		EngineeringDao dao=(EngineeringDao)getDao();
		try {			
			return dao.getFCHeadRequestListByBarcode(barcode);
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return new ArrayList();
		
	}
	
	public Collection getInternalCheckoutListByBarcode( String barcode ) {
		EngineeringDao dao=(EngineeringDao)getDao();
		try {			
			return dao.getInternalCheckoutListByBarcode(barcode);
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return new ArrayList();
		
	}
	
	public Collection getFCHeadAllRequest(Date requiredFrom, Date requiredTo, String departmentId, String search, String status, String sort,boolean desc,int start,int rows) throws DaoException{
		Collection col=new ArrayList();
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			col= dao.selectFCHeadAllRequest(requiredFrom, requiredTo, departmentId, search, status, sort, desc, start, rows);
			for(Iterator itr=col.iterator();itr.hasNext();){
				EngineeringRequest eRequest=(EngineeringRequest)itr.next();
				Collection services=dao.selectRequestServices(eRequest.getRequestId());
				eRequest.setServices(services);
				eRequest.setFullName(dao.getPICFullName(eRequest.getFcId()));
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getStatusTrail(String requestId){
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
				col=dao.selectStatusTrail(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("requestId = " + requestId + " " + e.toString(), e);
		}
		return col;
	}
	public void insertPostProductionService(PostProductionService service){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			service.setId(UuidGenerator.getInstance().getUuid());
			service.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			service.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			service.setCreatedDate(now);
			service.setModifiedDate(now);
			
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(service, "SERVICE_POSTPRODUCTION_INSERT", null);
			
			dao.insertPostProductionService(service);
			insertRequestUnit(service.getRequestId(), service.getServiceId());
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	public void updatePostProductionService(PostProductionService service){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			service.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			service.setModifiedDate(now);
			
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(service, "SERVICE_POSTPRODUCTION_UPDATE", null);
			
			dao.updatePostProductionService(service);
			insertRequestUnit(service.getRequestId(), service.getServiceId());
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public Collection getPostProductionService(String requestId,String serviceId){
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
				col=dao.selectPostProductionService(requestId,serviceId,false);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getPostProductionServiceIncludeInvalidRateCard(String requestId, String serviceId){
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
			col=dao.selectPostProductionService(requestId, serviceId, true);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getPostProductionServiceForTemplate(String requestId,String serviceId){
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
				col=dao.selectPostProductionServiceForTemplate(requestId,serviceId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public PostProductionService getPostProductionService(String id){
		EngineeringDao dao=(EngineeringDao)getDao();
		PostProductionService pps=new PostProductionService();
		try {
			Collection col=dao.selectPostProductionService(id);
			if(col.size()>0){
				pps=(PostProductionService)col.iterator().next();
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return pps;
	}
	
	public void insertScpService(ScpService service){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			service.setId(UuidGenerator.getInstance().getUuid());
			service.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			service.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			service.setCreatedDate(now);
			service.setModifiedDate(now);
			
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(service, "SERVICE_SCP_INSERT", null);
			
			dao.insertScpService(service);
			insertRequestUnit(service.getRequestId(), service.getServiceId());
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	public void updateScpService(ScpService service){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			service.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			service.setModifiedDate(now);
			
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(service, "SERVICE_SCP_UPDATE", null);
			
			dao.updateScpService(service);
			insertRequestUnit(service.getRequestId(), service.getServiceId());
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public Collection getScpService(String requestId,String serviceId){
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
				col=dao.selectScpService(requestId,serviceId,false);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getScpServiceIncludeInvalidRateCard(String requestId, String serviceId) {
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
			col=dao.selectScpService(requestId, serviceId, true);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getScpServiceForTemplate(String requestId,String serviceId){
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
				col=dao.selectScpServiceForTemplate(requestId,serviceId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	 
	public ScpService getScpService(String id){
		EngineeringDao dao=(EngineeringDao)getDao();
		ScpService pps=new ScpService();
		try {
			Collection col=dao.selectScpService(id);
			if(col.size()>0){
				pps=(ScpService)col.iterator().next();
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return pps;
	}
	
	// for Copy Request Function
	public void insertCopyVtrService(VtrService service){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			service.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			service.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			service.setCreatedDate(now);
			service.setModifiedDate(now);
			dao.insertVtrService(service);
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void insertVtrService(VtrService service){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			service.setId(UuidGenerator.getInstance().getUuid());
			service.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			service.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			service.setCreatedDate(now);
			service.setModifiedDate(now);
			
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(service, "SERVICE_VTR_INSERT", null);
			
			dao.insertVtrService(service);
			insertRequestUnit(service.getRequestId(), service.getServiceId());
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	public void updateVtrService(VtrService service){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			service.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			service.setModifiedDate(now);
			
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(service, "SERVICE_VTR_UPDATE", null);
			
			dao.updateVtrService(service);
			insertRequestUnit(service.getRequestId(), service.getServiceId());
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public Collection getVtrService(String requestId,String serviceId){
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
				col=dao.selectVtrService(requestId,serviceId,false);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getVtrServiceIncludeInvalidRateCard(String requestId, String serviceId) {
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
			col=dao.selectVtrService(requestId, serviceId, true);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getVtrServiceForTemplate(String requestId,String serviceId){
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
				col=dao.selectVtrServiceForTemplate(requestId,serviceId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public VtrService getVtrService(String id){
		EngineeringDao dao=(EngineeringDao)getDao();
		VtrService pps=new VtrService();
		try {
			Collection col=dao.selectVtrService(id);
			if(col.size()>0){
				pps=(VtrService)col.iterator().next();
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return pps;
	}
	
	public void insertOtherService(OtherService service){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			service.setId(UuidGenerator.getInstance().getUuid());
			service.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			service.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			service.setCreatedDate(now);
			service.setModifiedDate(now);
			
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(service, "SERVICE_OTHER_INSERT", null);
			
			dao.insertOtherService(service);
			insertRequestUnit(service.getRequestId(), service.getServiceId());
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	public void updateOtherService(OtherService service){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			service.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			service.setModifiedDate(now);
			
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(service, "SERVICE_OTHER_UPDATE", null);
			
			dao.updateOtherService(service);
			insertRequestUnit(service.getRequestId(), service.getServiceId());
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public Collection getOtherService(String requestId,String serviceId){
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
				col=dao.selectOtherService(requestId,serviceId,false);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getOtherServiceIncludeInvalidRateCard(String requestId, String serviceId) {
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
			col=dao.selectOtherService(requestId, serviceId, true);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getOtherServiceForTemplate(String requestId,String serviceId){
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
				col=dao.selectOtherServiceForTemplate(requestId,serviceId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public OtherService getOtherService(String id){
		EngineeringDao dao=(EngineeringDao)getDao();
		OtherService pps=new OtherService();
		try {
			Collection col=dao.selectOtherService(id);
			if(col.size()>0){
				pps=(OtherService)col.iterator().next();
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return pps;
	}
	
	public void insertManpowerService(ManpowerService service){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			service.setId(UuidGenerator.getInstance().getUuid());
			service.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			service.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			service.setCreatedDate(now);
			service.setModifiedDate(now);
			
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(service, "SERVICE_MANPOWER_INSERT", null);
			
			dao.insertManpowerService(service);
			insertRequestUnit(service.getRequestId(), service.getServiceId());
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	public void updateManpowerService(ManpowerService service){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			service.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			service.setModifiedDate(now);
			
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(service, "SERVICE_MANPOWER_UPDATE", null);
			
			dao.updateManpowerService(service);
			insertRequestUnit(service.getRequestId(), service.getServiceId());
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public Collection getManpowerService(String requestId,String serviceId){
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
				col=dao.selectManpowerService(requestId,serviceId, false);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getManpowerServiceIncludeInvalidRateCard(String requestId, String serviceId) {
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
			col=dao.selectManpowerService(requestId, serviceId, true);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getManpowerServiceForTemplate(String requestId,String serviceId){
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
				col=dao.selectManpowerServiceForTemplate(requestId,serviceId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public ManpowerService getManpowerService(String id){
		EngineeringDao dao=(EngineeringDao)getDao();
		ManpowerService pps=new ManpowerService();
		try {
			Collection col=dao.selectManpowerService(id);
			if(col.size()>0){
				pps=(ManpowerService)col.iterator().next();
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return pps;
	}
	
	public void insertStudioService(StudioService service){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			service.setId(UuidGenerator.getInstance().getUuid());
			service.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			service.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			service.setCreatedDate(now);
			service.setModifiedDate(now);
			
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(service, "SERVICE_STUDIO_INSERT", null);
			
			dao.insertStudioService(service);
			insertRequestUnit(service.getRequestId(), service.getServiceId());
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	public void updateStudioService(StudioService service){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			service.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			service.setModifiedDate(now);
			
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(service, "SERVICE_STUDIO_UPDATE", null);
			
			dao.updateStudioService(service);
			insertRequestUnit(service.getRequestId(), service.getServiceId());
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public Collection getStudioService(String requestId,String serviceId){
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
				col=dao.selectStudioService(requestId,serviceId,false);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getStudioServiceIncludeInvalidRateCard(String requestId, String serviceId) {
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
			col=dao.selectStudioService(requestId, serviceId, true);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getStudioServiceForTemplate(String requestId,String serviceId){
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
				col=dao.selectStudioServiceForTemplate(requestId,serviceId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public StudioService getStudioService(String id){
		EngineeringDao dao=(EngineeringDao)getDao();
		StudioService pps=new StudioService();
		try {
			Collection col=dao.selectStudioService(id);
			if(col.size()>0){
				pps=(StudioService)col.iterator().next();
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return pps;
	}
	
	public void insertTvroService(TvroService service){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			service.setId(UuidGenerator.getInstance().getUuid());
			service.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			service.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			service.setCreatedDate(now);
			service.setModifiedDate(now);
			
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(service, "SERVICE_TVRO_INSERT", null);
			
			dao.insertTvroService(service);
			insertRequestUnit(service.getRequestId(), service.getServiceId());
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	public void updateTvroService(TvroService service){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			service.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			service.setModifiedDate(now);
			
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(service, "SERVICE_TVRO_UPDATE", null);
			
			dao.updateTvroService(service);
			insertRequestUnit(service.getRequestId(), service.getServiceId());
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public Collection getTvroService(String requestId,String serviceId){
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
				col=dao.selectTvroService(requestId,serviceId, false);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getTvroServiceIncludeInvalidRateCard(String requestId, String serviceId) {
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
			col=dao.selectTvroService(requestId, serviceId, true);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getTvroServiceForTemplate(String requestId,String serviceId){
		EngineeringDao dao=(EngineeringDao)getDao();
		Collection col=new ArrayList();
		try {
				col=dao.selectTvroServiceForTemplate(requestId,serviceId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public TvroService getTvroService(String id){
		EngineeringDao dao=(EngineeringDao)getDao();
		TvroService pps=new TvroService();
		try {
			Collection col=dao.selectTvroService(id);
			if(col.size()>0){
				pps=(TvroService)col.iterator().next();
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return pps;
	}
	
	public static boolean isHOD(String userId){
		boolean result=false;
		try{
			FMSRegisterManager module = (FMSRegisterManager) Application.getInstance().getModule(FMSRegisterManager.class);
			FMSDepartmentManager departmentManager = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);	
			String department = module.getUserDepartment(userId);
			result = departmentManager.userIsHOD(userId, department);	    
		}catch(Exception e){
			Log.getLog(EngineeringModule.class).error(e.getMessage(),e);
		}
		return result;
	}
	
	public static boolean isUnitApprover(String userId){
		boolean result=false;
		try{
			EngineeringDao dao=(EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
			result = dao.isUnitApprover(userId);	    
		}catch(Exception e){
			Log.getLog(EngineeringModule.class).error(e.getMessage(),e);
		}
		return result;
	}
	
	public static SequencedHashMap getAllFCMap() {
		SecurityService ss=(SecurityService)Application.getInstance().getService(SecurityService.class);
		SequencedHashMap fcMap=new SequencedHashMap();
		Collection cUsers;
		try {
			cUsers = ss.getUsersByPermission(FacilitiesCoordinatorModule.FACILITY_CONTROLLER_PERMISSION, Boolean.TRUE, "firstName",false, 0, -1);
			for(Iterator itr=cUsers.iterator();itr.hasNext();){
				User user=(User)itr.next();
				fcMap.put(user.getId(), user.getName());
			}
		} catch (SecurityException e) {
			Log.getLog(EngineeringModule.class).error(e.getMessage(),e);
		}
		
		return fcMap;
	}
	
	public void insertVtrAttachment(VtrService vtr){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			dao.insertVtrAttachment(vtr);
		} catch (DaoException e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public Collection getFiles(String vtrId){
		EngineeringDao dao = (EngineeringDao) getDao();
		Collection col= new ArrayList();
		try {
			col= dao.selectFiles(vtrId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public VtrService getFile(String fileId){
		VtrService vtrFile = null;
		EngineeringDao dao = (EngineeringDao)getDao();
		try {
			vtrFile = dao.selectFile(fileId);
		} catch (DaoException e){
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return vtrFile;
	}
	
	public void deleteVtrFile(String fileId){
		EngineeringDao dao = (EngineeringDao) getDao();
		try {
			dao.deleteVtrFile(fileId);
		} catch (DaoException e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		
	}
	
	public double getTotalRate(String requestId, String rateType){
		EngineeringDao dao = (EngineeringDao) getDao();
		double totalInternalRate = 0;
		double totalRateSCP 	= 0;
		double totalRatePost 	= 0;
		double totalRateOther 	= 0;
		double totalRateStudio	= 0;
		double totalRateVtr		= 0;
		double totalRateManpower= 0;
		double totalRateTvro	= 0;
		
		try {
			totalRateSCP 		= dao.selectTotalRate("scp", requestId, rateType) ;
			totalRatePost		= dao.selectTotalRate("post", requestId, rateType) ;
			totalRateOther		= dao.selectTotalRate("other", requestId, rateType) ;
			totalRateStudio		= dao.selectTotalRate("studio", requestId, rateType) ;
			totalRateVtr		= dao.selectTotalRate("vtr", requestId, rateType) ;
			totalRateManpower	= dao.selectTotalRate("manpower", requestId, rateType) ;
			totalRateTvro		= dao.selectTotalRate("tvro", requestId, rateType) ;
			
			totalInternalRate = totalRateSCP + totalRatePost + totalRateOther + totalRateStudio + 
								totalRateVtr + totalRateManpower + totalRateTvro;
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		
		return totalInternalRate;
	}
	
	
	// Assignment details
	public EngineeringRequest getAssignment(String assignmentId){
		EngineeringRequest eRequest=null;
		EngineeringDao dao=(EngineeringDao)getDao();
		
		try {
			eRequest = dao.selectAssignment(assignmentId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return eRequest;
	}
	
	public EngineeringRequest getEquipmentAssignment(String assignmentId){
		EngineeringRequest eRequest=null;
		EngineeringDao dao=(EngineeringDao)getDao();
		
		try {
			eRequest = dao.selectEquipmentAssignment(assignmentId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return eRequest;
	}
	
	public Map selectManpowerByCompetencyId(String manpowerId, String assignmentId, Date startDate, Date endDate, String fromTime, String toTime) throws DaoException {
		HashMap manpowerMap = new HashMap();
		EngineeringDao dao=(EngineeringDao)getDao();
		
		try {			
			manpowerMap = (HashMap) dao.selectManpowerAssignment(manpowerId, assignmentId, startDate, endDate, fromTime, toTime);
		} catch (Exception e){
			Log.getLog(getClass()).info(e.getMessage(), e);
		}
		return manpowerMap;
	}
	
	public void updateManpowerAssignment(String asgId, String userId, String charge, String call){
		Application app = Application.getInstance();
		EngineeringDao dao=(EngineeringDao)getDao();
		EngineeringRequest eRequest = new EngineeringRequest();
		AssignmentLog log = new AssignmentLog();
		AssignmentLogModule mod = (AssignmentLogModule) app.getModule(AssignmentLogModule.class);
		try {
			Date now=new Date();
			UuidGenerator uuid = UuidGenerator.getInstance();
			
			eRequest.setAssignmentId(asgId);
			eRequest.setManpowerId(userId);
			eRequest.setStatus(ASSIGNMENT_MANPOWER_STATUS_ASSIGNED);
			eRequest.setChargeBack(charge);
			eRequest.setCallBack(call);
			eRequest.setModifiedBy(app.getCurrentUser().getUsername());
			eRequest.setModifiedOn(now);
			
			//Set Log
			log.setLogId(uuid.getUuid());
			log.setAssignmentId(asgId);
			log.setUserId(userId);
			log.setAssignBy(app.getCurrentUser().getId());
			log.setAssignDate(now);
			
			dao.updateManpowerAssignment(eRequest);
			mod.addLog(log);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	/*
	 * 
	 */
	public Collection getAssignmentByUserId(String userId, String search, String status, String sort, boolean desc, int start, int rows){
		EngineeringDao dao = (EngineeringDao) getDao();		
		Collection col= new ArrayList();
		
		try {
			col= dao.selectAssignmentByUserId(userId, search, status, sort, desc, start, rows);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public int countAssignmentByUserId(String userId, String search, String status){
		int countRateCard = 0;
		EngineeringDao dao = (EngineeringDao) getDao();		
		
		try {
			countRateCard = dao.countAssignmentByUserId(userId, search, status);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return countRateCard;
	}
	
	public void completeAssignment(EngineeringRequest eRequest){
		EngineeringDao dao=(EngineeringDao)getDao();
		
		try {
			Date now=new Date();			
			
			eRequest.setStatus(ASSIGNMENT_MANPOWER_STATUS_COMPLETED);
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			eRequest.setModifiedOn(now);
			
			dao.completeManpowerAssignment(eRequest);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void completeEquipmentAssignment(EngineeringRequest eRequest){
		EngineeringDao dao=(EngineeringDao)getDao();
		
		try {
			Date now=new Date();			
			
			eRequest.setStatus(ASSIGNMENT_FACILITY_STATUS_CHECKIN);
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			eRequest.setModifiedOn(now);
			
			dao.completeEquipmentAssignment(eRequest);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void unfulfilledEquipmentAssignment(EngineeringRequest eRequest){
		EngineeringDao dao=(EngineeringDao)getDao();
		
		try {
			Date now=new Date();			
			
			eRequest.setStatus(ASSIGNMENT_FACILITY_STATUS_UNFULFILLED);
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			eRequest.setModifiedOn(now);
			
			dao.unfulfilledEquipmentAssignment(eRequest);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void unfulfillAssignment(EngineeringRequest eRequest){
		EngineeringDao dao=(EngineeringDao)getDao();
		
		try {
			Date now=new Date();			
			
			eRequest.setStatus(ASSIGNMENT_MANPOWER_STATUS_UNFULFILLED);
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			eRequest.setModifiedOn(now);
			
			dao.unfulfillManpowerAssignment(eRequest);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public Collection getManpowerFiles(String id){
		EngineeringDao dao = (EngineeringDao) getDao();
		Collection col= new ArrayList();
		try {
			col= dao.selectManpowerFiles(id);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public void insertAttachment(EngineeringRequest er){
		EngineeringDao dao = (EngineeringDao) getDao();
		try {
			Date now=new Date();
			er.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			er.setCreatedOn(now);
			er.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			er.setModifiedOn(now);
			dao.insertAttachment(er);
		} catch (DaoException e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public Collection getCategoryByBarcode(String barcode, String rateCardId){
		EngineeringDao dao = (EngineeringDao) getDao();
		Collection col= new ArrayList();
		try {
			col= dao.selectCategoryByBarcode(barcode, rateCardId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return col;
	}
	
	public Collection getCategoryByBarcode(String barcode){
		EngineeringDao dao = (EngineeringDao) getDao();
		Collection col= new ArrayList();
		try {
			col= dao.selectCategoryByBarcode(barcode);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return col;
	}
	
	public Collection getRateCardIdByGroup(String groupId) {
		EngineeringDao dao = (EngineeringDao) getDao();
		Collection col = new ArrayList();
		try {
			col = dao.selectRateCardIdByGroup(groupId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return col;
	}
	
	public Collection getAssignmentEquipment(String assignmentId, String rateCardCategoryId){
		EngineeringDao dao = (EngineeringDao) getDao();
		Collection col= new ArrayList();
		try {
			col= dao.selectCheckOutEquipment(assignmentId, rateCardCategoryId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getGroupEquipment(String groupId, String rateCardCategoryId){
		EngineeringDao dao = (EngineeringDao) getDao();
		Collection col= new ArrayList();
		try {
			col= dao.selectCheckOutEquipmentByGroup(groupId, rateCardCategoryId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getAssignmentEquipmentByBc(String assignmentId, String barcode) {
		EngineeringDao dao = (EngineeringDao) getDao();
		Collection col= new ArrayList();
		try {
			col= dao.selectEquipmentByBc(assignmentId, barcode);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getAssignmentEquipmentByBcGroupId(String groupId, String barcode) {
		EngineeringDao dao = (EngineeringDao) getDao();
		Collection col= new ArrayList();
		try {
			col= dao.selectEquipmentByBcGroupId(groupId, barcode);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getAssignmentEquipmentByBc(String barcode) {
		EngineeringDao dao = (EngineeringDao) getDao();
		Collection col= new ArrayList();
		try {
			col= dao.selectEquipmentByBc(barcode);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getAssignmentEquipment(String assignmentId) {
		EngineeringDao dao = (EngineeringDao)getDao();
		Collection col = new ArrayList();
		try {
			col = dao.selectAssignmentEquipment(assignmentId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return col;
	}
	
	public Collection getAssignmentEquipmentByGroup(String groupId) {
		EngineeringDao dao = (EngineeringDao)getDao();
		Collection col = new ArrayList();
		try {
			col = dao.selectAssignmentEquipmentByGroup(groupId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return col;
	}
	
	public Collection getAssignmentByRequestId(String requestId){
		EngineeringDao dao = (EngineeringDao) getDao();
		Collection col = new ArrayList();
		try {
			col = dao.selectAssignmentByRequestId(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return col;
	}
	
	public void deleteAssignment(String requestId) {
		EngineeringDao dao = (EngineeringDao) getDao();
		try {
			dao.deleteAssignment(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
	}
	
	public void deleteAssignmentByServiceId(String serviceId) {
		EngineeringDao dao = (EngineeringDao) getDao();
		try {
			dao.deleteAssignmentByServiceId(serviceId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
	}
	
	public void deleteEquipmentAssignment(String assignmentId) {
		EngineeringDao dao = (EngineeringDao) getDao();
		try {
			dao.deleteEquipmentAssignment(assignmentId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
	}
	
	public void deleteManpowerAssignment(String assignmentId) {
		EngineeringDao dao = (EngineeringDao) getDao();
		try {
			dao.deleteManpowerAssignment(assignmentId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
	}
	
	public void sendSubmissionEmail(String requestId) {
		sendSubmissionEmail(requestId, true);
	}
	
	public void sendSubmissionEmail(String requestId, boolean isHOD) {
		FmsNotification notification = new FmsNotification();		
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		EngineeringRequest er = getRequest(requestId);
		String requestor = er.getCreatedUserName(); 
		String requestTitle = er.getTitle();
		String requiredDate = sdf.format(er.getRequiredFrom()) + " - " + sdf.format(er.getRequiredTo());
		String erSubject = Application.getInstance().getMessage("fms.notification.requestSubmitted.subject");
		String erBody = Application.getInstance().getMessage("fms.notification.requestSubmitted.body");
		String appBody = Application.getInstance().getMessage("fms.notification.requestSubmitted.bodyApprover");
		String eSubject = replaceSubjectWithDetails(requestId, erSubject);		
		String eBody = replaceBodyWithDetails(requestId, requestTitle, requiredDate, "", erBody);
		
		if (isHOD) {
			try {
				String emailToRequestor[] = getRequestorEmail(requestId);
				String emailToApprover[] = getApproverEmail(requestId);
				ArrayList emailList = new ArrayList();
				
				for (int i=0; i<emailToRequestor.length; i++) {
					emailList.add(emailToRequestor[i]);
				}
				for (int i=0; i<emailToApprover.length; i++) {
					emailList.add(emailToApprover[i]);
				}
				
				notification.send(emailList, eSubject, eBody);
				
			} catch (Exception e) {
				Log.getLog(getClass()).error("EngineeringModule: ERROR sendNotification " + er);
			}
			
//			sendNotificationToRequestor(requestId, requestTitle, requiredDate, "", erSubject, erBody);
//			sendNotificationToApprover(requestId, requestTitle, requiredDate, requestor, erSubject, appBody);
		} else {
			sendNotificationToAltApprover(requestId, requestTitle, requiredDate, requestor, erSubject, appBody);
		}
	}
	
	public void sendHOUEmail(String requestId) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		EngineeringRequest er = getRequest(requestId);
		String requestor = er.getCreatedUserName(); 
		String requestTitle = er.getTitle();
		String requiredDate = sdf.format(er.getRequiredFrom()) + " - " + sdf.format(er.getRequiredTo());
		String erSubject = Application.getInstance().getMessage("fms.notification.requestHOU.subject");
		String appBody = Application.getInstance().getMessage("fms.notification.requestHOU.bodyApprover");
		
		sendNotificationToHOU(requestId, requestTitle, requiredDate, requestor, erSubject, appBody);
	}
	
	public void sendRejectionEmail(String requestId, String remarks) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		EngineeringRequest er = getRequest(requestId);
		//String requestor = er.getCreatedUserName(); 
		String requestTitle = er.getTitle();
		String requiredDate = sdf.format(er.getRequiredFrom()) + " - " + sdf.format(er.getRequiredTo());
		String erSubject = Application.getInstance().getMessage("fms.notification.requestRejected.subject");
		String erBody = Application.getInstance().getMessage("fms.notification.requestRejected.body");
		//String appBody = Application.getInstance().getMessage("fms.notification.requestSubmitted.bodyApprover");
		
		sendNotificationToRequestor(requestId, requestTitle, requiredDate, remarks, erSubject, erBody);
		//sendNotificationToApprover(requestId, requestTitle, requiredDate, requestor, erSubject, appBody);
	}
	
	public void sendRejectionFCEmail(String requestId, String remarks) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		EngineeringRequest er = getRequest(requestId);
		//String requestor = er.getCreatedUserName(); 
		String requestTitle = er.getTitle();
		String requiredDate = sdf.format(er.getRequiredFrom()) + " - " + sdf.format(er.getRequiredTo());
		String erSubject = Application.getInstance().getMessage("fms.notification.requestRejectedFC.subject");
		String erBody = Application.getInstance().getMessage("fms.notification.requestRejectedFC.body");
		//String appBody = Application.getInstance().getMessage("fms.notification.requestSubmitted.bodyApprover");
		
		sendNotificationToRequestor(requestId, requestTitle, requiredDate, remarks, erSubject, erBody);
		//sendNotificationToApprover(requestId, requestTitle, requiredDate, requestor, erSubject, appBody);
	}
	
	public void sendFeedbackEmail(String requestId) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		EngineeringRequest er = getRequest(requestId);
		String requestTitle = er.getTitle();
		String requiredDate = sdf.format(er.getRequiredFrom()) + " - " + sdf.format(er.getRequiredTo());
		String totalRate = "";
		if (REQUEST_TYPE_EXTERNAL.equals(er.getRequestType())){
			totalRate = formatAmount(er.getTotalExternalRate());
		} else {
			totalRate = formatAmount(er.getTotalInternalRate());
		}
		String erSubject = Application.getInstance().getMessage("fms.notification.feedBack.subject");
		String erBody = Application.getInstance().getMessage("fms.notification.feedBack.body");
		
		String links = "<a href=\"";
		links += Application.getInstance().getMessage("fms.notification.globalUrl");
		links += "ekms/fms/engineering/feedbackForm.jsp?requestId=" + requestId;
		links += "\">";
		links += Application.getInstance().getMessage("fms.notification.globalUrl");
		links += "ekms/fms/engineering/feedbackForm.jsp?requestId=" + requestId;
		links += "</a>";
		
		sendFeedbackToRequestor(requestId, requestTitle, requiredDate, totalRate, links, erSubject, erBody);
	}
	
	public void sendLateCompletionFCEmail(String requestId, String remarks) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		EngineeringRequest er = getRequest(requestId);
		//String requestor = er.getCreatedUserName(); 
		String requestTitle = er.getTitle();
		String requiredDate = sdf.format(er.getRequiredFrom()) + " - " + sdf.format(er.getRequiredTo());
		String erSubject = Application.getInstance().getMessage("fms.notification.requestLateCompletionFC.subject");
		String erBody = Application.getInstance().getMessage("fms.notification.requestLateCompletionFC.body");
		//String appBody = Application.getInstance().getMessage("fms.notification.requestSubmitted.bodyApprover");
		
		sendNotificationToRequestor(requestId, requestTitle, requiredDate, remarks, erSubject, erBody);
		//sendNotificationToApprover(requestId, requestTitle, requiredDate, requestor, erSubject, appBody);
	}
	
	public void sendApprovalEmail(String requestId) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		EngineeringRequest er = getRequest(requestId);
		String requestor = er.getCreatedUserName(); 
		String requestTitle = er.getTitle();
		String requiredDate = sdf.format(er.getRequiredFrom()) + " - " + sdf.format(er.getRequiredTo());
		String erSubject = Application.getInstance().getMessage("fms.notification.requestApproved.subject");
		String erBody = Application.getInstance().getMessage("fms.notification.requestApproved.body");
		String appBody = Application.getInstance().getMessage("fms.notification.requestApproved.bodyAllFC");
		
		sendNotificationToRequestor(requestId, requestTitle, requiredDate, "", erSubject, erBody);
		sendNotificationToAllFC(requestId, requestTitle, requiredDate, requestor, erSubject, appBody);
		//sendNotificationToApprover(requestId, requestTitle, requiredDate, requestor, erSubject, appBody);
	}
	
	public void sendAssignEmail(String requestId) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		EngineeringRequest er = getRequest(requestId);
		String requestor = er.getCreatedUserName(); 
		String requestTitle = er.getTitle();
		String requiredDate = sdf.format(er.getRequiredFrom()) + " - " + sdf.format(er.getRequiredTo());
		String erSubject = Application.getInstance().getMessage("fms.notification.requestAssigned.subject");
		String erBody = Application.getInstance().getMessage("fms.notification.requestAssigned.body");
		String appBody = Application.getInstance().getMessage("fms.notification.requestAssigned.bodyApprover");
		
		sendNotificationToRequestor(requestId, requestTitle, requiredDate, "", erSubject, erBody);
		sendNotificationToFC(requestId, requestTitle, requiredDate, requestor, erSubject, appBody);
	}
	
	public void sendAcceptanceEmail(String requestId, boolean isOutsourced) {
		Application app = Application.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		FmsNotification notification = new FmsNotification();		
		EngineeringRequest er = getRequest(requestId);
		//String requestor = er.getCreatedUserName(); 
		String requestTitle = er.getTitle();
		String requiredDate = sdf.format(er.getRequiredFrom()) + " - " + sdf.format(er.getRequiredTo());
		
		String totalAmount = app.getMessage("fms.request.label.currency") + " " + "0.0";
		if (REQUEST_TYPE_EXTERNAL.equals(er.getRequestType())){
			totalAmount = app.getMessage("fms.request.label.currency") + " " + formatAmount(er.getTotalExternalRate());
		} else {
			totalAmount = app.getMessage("fms.request.label.currency") + " " + formatAmount(er.getTotalInternalRate());
		}
		
		String erSubject = app.getMessage("fms.notification.requestFCAccepted.subject");
		String erBody = app.getMessage("fms.notification.requestFCAccepted.body");
		//String appBody = app.getMessage("fms.notification.requestFCAccepted.bodyApprover");
		
		if (!isOutsourced) {
			String eSubject = replaceSubjectWithDetails(requestId, erSubject);		
			String eBody = replaceBodyWithDetails(requestId, requestTitle, requiredDate, totalAmount, erBody);
			try {
				String emailToRequestor[] = getRequestorEmail(requestId);	
				String emailToUnitAltApp[] = getUnitAlternateApprover(requestId);			
				String emailToEngUnitAltApp[] = getEngUnitAltApp(requestId); 
				ArrayList emailList = new ArrayList();
				
				for (int i=0; i<emailToRequestor.length; i++) {
					emailList.add(emailToRequestor[i]);
				}
				for (int i=0; i<emailToUnitAltApp.length; i++) {
					emailList.add(emailToUnitAltApp[i]);
				}
				for (int i=0; i<emailToEngUnitAltApp.length; i++) {
					emailList.add(emailToEngUnitAltApp[i]);
				}
				
				notification.send(emailList, eSubject, eBody);
			} catch (Exception e) {
				Log.getLog(getClass()).error("EngineeringModule: ERROR sendNotification " + er);
			}
//			sendNotificationToRequestor(requestId, requestTitle, requiredDate, totalAmount, erSubject, erBody);
//			sendNotificationToUnitAltApp(requestId, requestTitle, requiredDate, requestor, erSubject, appBody);
//			sendNotificationToEngUnitAltApp(requestId, requestTitle, requiredDate, requestor, erSubject, appBody);
		} else {
			String outsources = " ";
			String totalEstimatedCost = "";
			String totalActualCost = "";
			
			erBody = app.getMessage("fms.notification.requestFCAcceptedOutsource.body");
			Map mapOutsourceType = new HashMap(); 
			mapOutsourceType.put("C", "Company"); 
			mapOutsourceType.put("I", "Individual");
			FacilitiesCoordinatorModule fcm = (FacilitiesCoordinatorModule)app.getModule(FacilitiesCoordinatorModule.class);
			Collection col = new ArrayList();
			try {
				col = fcm.getOutsource("", "", requestId, "id", true, 0, -1);
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString(), e);
			}
			
			if (col != null && col.size()>0) {
				totalEstimatedCost = fcm.selectOutsourceCost(requestId) + "";
				totalActualCost = fcm.selectOutsourceActualCost(requestId) + "";
				
				outsources = "<table width='80%' cellpadding='0' cellspacing='0' border='1'>";
				outsources += "<tr>";
				outsources += "<th>" + app.getMessage("fms.facility.table.label.outsourceType", "Outsource Type") + "</th>";
				outsources += "<th>" + app.getMessage("fms.facility.table.label.companyName", "Company Name") + "</th>";
				outsources += "<th>" + app.getMessage("fms.facility.table.label.estimatedCost", "Estimated Cost") + " (" +app.getMessage("fms.request.label.currency")+") </th>";
				outsources += "<th>" + app.getMessage("fms.facility.table.label.actualCost", "Actual Cost")+ " (" +app.getMessage("fms.request.label.currency")+") </th>";
				outsources += "<th>" + app.getMessage("fms.label.outsourceRemarks") + "</th>";
				outsources += "</tr>";
				
				for (Iterator i = col.iterator(); i.hasNext();) {
					EngineeringRequest erOut = (EngineeringRequest) i.next();
					String companyName = "";
					if (erOut.getClientName()!=null){
						companyName = erOut.getClientName();
					}
					
					outsources += "<tr><td>" + mapOutsourceType.get(erOut.getOutsourceType()) + "</td>" ;
					outsources += "<td>" + companyName + "</td>";
					outsources += "<td>" + (erOut.getEstimatedCost()!=null?erOut.getEstimatedCost():"0") + "</td>";
					outsources += "<td>" + (erOut.getActualCost()!=null?erOut.getActualCost():"0") + "</td>";
					outsources += "<td>" + (erOut.getRemarks()!=null?erOut.getRemarks():"0") + "</td></tr>";
				}
				outsources +="</table>";
			}
			
			sendNotificationOutsourceToRequestor(requestId, requestTitle, requiredDate, totalEstimatedCost, totalActualCost, outsources, erSubject, erBody);
		}
		//sendNotificationToApprover(requestId, requestTitle, requiredDate, requestor, erSubject, appBody);
	}
	
	//public void sendCancellationEmail(String requestId) {
	//	sendCancellationEmail(requestId, true);
	//}
	
	public void sendCancellationEmail(String requestId, boolean toFC, boolean toRequestor, boolean toAllFC, boolean toHOD) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		EngineeringRequest er = getRequest(requestId);
		String requestor = er.getCreatedUserName(); 
		String requestTitle = er.getTitle();
		String requiredDate = sdf.format(er.getRequiredFrom()) + " - " + sdf.format(er.getRequiredTo());
		String cancellationCharges = Application.getInstance().getMessage("fms.request.label.currency") + " " + er.getCancellationCharges();
		
		String erSubject = Application.getInstance().getMessage("fms.notification.requestCancelled.subject");
		String erSubjectCanceled = Application.getInstance().getMessage("fms.notification.requestCancelled.subjectCancelled");
		String erBody = Application.getInstance().getMessage("fms.notification.requestCancelled.body");
		String appBody = Application.getInstance().getMessage("fms.notification.requestCancelled.bodyApprover");
		String appsBody = Application.getInstance().getMessage("fms.notification.requestCancelled.bodyApprovers");
		
		if (er.getCancellationCharges() == null) {
			cancellationCharges = "-";
			erBody = Application.getInstance().getMessage("fms.notification.requestCancelled.bodyRequestor");			
		} 	
		
		if (toFC){
			sendNotificationToFC(requestId, requestTitle, requiredDate, requestor, erSubject, appBody);	
		} 
		
		if (toRequestor) {
			if (CANCELLED_STATUS.equals(er.getStatus())){
				erSubject = erSubjectCanceled;
			}
			sendNotificationToRequestor(requestId, requestTitle, requiredDate, cancellationCharges, erSubject, erBody);
		}
		
		if (toAllFC) {
			sendNotificationToAllFC(requestId, requestTitle, requiredDate, requestor, erSubjectCanceled, appsBody);
		}
		
		if (toHOD) {
			sendNotificationToApprover(requestId, requestTitle, requiredDate, requestor, erSubjectCanceled, appsBody);
		}
	}
	
	public void sendCancellationEmailToManpower(String requestId, String requestTitle, String reason){
		Application app = Application.getInstance();
		EngineeringDao dao = (EngineeringDao) getDao();
		EngineeringRequest er = getRequest(requestId);
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		String requiredDate = sdf.format(er.getRequiredFrom()) + " - " + sdf.format(er.getRequiredTo());
		String cancellationCharges = app.getMessage("fms.request.label.currency") + " " + er.getCancellationCharges();
		
		String erSubject = app.getMessage("fms.notification.requestCancelled.subjectCancelled");
		String erBody = app.getMessage("fms.notification.requestCancelled.bodyRequestor");
		
		Collection col = dao.selectManpowerAssignmentId(requestId);
		for(Iterator iterate = col.iterator(); iterate.hasNext(); ){
			EngineeringRequest engine = (EngineeringRequest) iterate.next();
			if (engine!=null && engine.getAssignmentType().equals("M")){
				EngineeringRequest man = dao.selectManpowerStatus(engine.getAssignmentId());
				if (man.getStatus().equals("A") && man.getUserId()!=null){
					SecurityService sec = (SecurityService) app.getService(SecurityService.class);
					try {
						User user = sec.getUser(man.getUserId());
						String email = (String) user.getProperty("email1");
						sendNotificationToManpower(requestId, email, requestTitle, requiredDate, cancellationCharges, erSubject, erBody);
					} catch (SecurityException e) {
						Log.getLog(getClass()).error(e.getMessage(), e);
					}
				}
			}
		}
	}
	
	public void sendNotificationToManpower(String requestId, String email, String requestTitle, String requiredDate, String remarks, String subject, String body) {
		FmsNotification notification = new FmsNotification();		
		String eSubject = replaceSubjectWithDetails(requestId, subject);	
		String eBody = replaceBodyWithDetails(requestId, requestTitle, requiredDate, remarks, body);
		notification.send(new String[] {email}, eSubject, eBody);
	}
	
	public void sendNotificationToManpowerUsingBGProcess(String requestId, String email, String requestTitle, String requiredDate, String remarks, String subject, String body) {
		String eSubject = replaceSubjectWithDetailsService(requestId, requestTitle, subject);
		String eBody = replaceBodyWithDetails(requestId, requestTitle, requiredDate, remarks, body);
		try{			
			
			//System.out.println("EMAILS : "+email);
			FMSMailUtil.sendEmail(null, true, null, email, eSubject, eBody);
			//notification.send(emailTo, eSubject, eBody);
			
		} catch (Exception er) {
			Log.getLog(getClass()).error("EngineeringModule: ERROR sendNotification " + er);
		}			
		//notification.send(new String[] {email}, eSubject, eBody);
	}
	
	public void sendNotificationToRequestor(String requestId, String requestTitle, String requiredDate, String remarks, String subject, String body){		
		FmsNotification notification = new FmsNotification();		
		String eSubject = replaceSubjectWithDetails(requestId, subject);		
		String eBody = replaceBodyWithDetails(requestId, requestTitle, requiredDate, remarks, body);
		try{			
			String emailTo[] = getRequestorEmail(requestId);			
			notification.send(emailTo, eSubject, eBody);
			
		} catch (Exception er) {
			Log.getLog(getClass()).error("EngineeringModule: ERROR sendNotification " + er);
		}					
	}
	
	public void sendNotificationServiceToRequestor(String requestId, String requestTitle, String assignmentId, String serviceTitle, String subject, String body){		
		FmsNotification notification = new FmsNotification();		
		String eSubject = replaceSubjectWithDetailsService(requestId, requestTitle, subject);		
		String eBody = replaceBodyWithDetailsService(requestId, requestTitle, serviceTitle, body);
		try{			
			String emailTo[] = getRequestorEmail(requestId);			
			notification.send(emailTo, eSubject, eBody);
			
		} catch (Exception er) {
			Log.getLog(getClass()).error("EngineeringModule: ERROR sendNotification " + er);
		}					
	}
	
	public void sendNotificationServiceToRequestorUsingBGProcess(String requestId, String requestTitle, String assignmentId, String serviceTitle, String subject, String body){		
		String eSubject = replaceSubjectWithDetailsService(requestId, requestTitle, subject);		
		String eBody = replaceBodyWithDetailsService(requestId, requestTitle, serviceTitle, body);
		try{			
			String emailTo[] = getRequestorEmail(requestId);	
			String emails = "";
			for(int i=0;i<emailTo.length;i++){
				if(i>0)
					emails=emails+","+emailTo[i];
				else
					emails=emailTo[i];
			}
			
			//System.out.println("EMAILS : "+emails);
			FMSMailUtil.sendEmail(null, true, null, emails, eSubject, eBody);
			
			//notification.send(emailTo, eSubject, eBody);
			
		} catch (Exception er) {
			Log.getLog(getClass()).error("EngineeringModule: ERROR sendNotification " + er);
		}					
	}
	
	public void sendNotificationOutsourceToRequestor(String requestId, String requestTitle, String requiredDate, 
			String totalEstimatedCost, String totalActualCost, String outsources, 
			String subject, String body){		
		FmsNotification notification = new FmsNotification();		
		String eSubject = replaceSubjectWithDetails(requestId, subject);		
		String eBody = replaceBodyOutsourceWithDetails(requestId, requestTitle, requiredDate, totalEstimatedCost, totalActualCost, outsources, body);
		try{			
			String emailTo[] = getRequestorEmail(requestId);			
			notification.send(emailTo, eSubject, eBody);
			
		} catch (Exception er) {
			Log.getLog(getClass()).error("EngineeringModule: ERROR sendNotification " + er);
		}					
	}
	
	public void sendFeedbackToRequestor(String requestId, String requestTitle, String requiredDate, String totalRate, String remarks, String subject, String body){		
		FmsNotification notification = new FmsNotification();		
		String eSubject = replaceSubjectWithDetails(requestId, subject);		
		String eBody = replaceBodyWithLinks(requestId, requestTitle, requiredDate, totalRate, remarks, body);
		try{			
			String emailTo[] = getRequestorEmail(requestId);			
			notification.send(emailTo, eSubject, eBody);
			
		} catch (Exception er) {
			Log.getLog(getClass()).error("EngineeringModule: ERROR sendNotification " + er);
		}					
	}
	
	public void sendNotificationToApprover(String requestId, String requestTitle, String requiredDate, String requestor, String subject, String body){		
		FmsNotification notification = new FmsNotification();		
		String eSubject = replaceSubjectWithDetails(requestId, subject);		
		String eBody = replaceBodyApproverWithDetails(requestId, requestTitle, requiredDate, requestor, "", body);
		try{			
			String emailTo[] = getApproverEmail(requestId);			
			notification.send(emailTo, eSubject, eBody);
			
		} catch (Exception er) {
			Log.getLog(getClass()).error("EngineeringModule: ERROR sendNotification " + er);
		}					
	}
	
	public void sendNotificationToAltApprover(String requestId, String requestTitle, String requiredDate, String requestor, String subject, String body){		
		FmsNotification notification = new FmsNotification();		
		String eSubject = replaceSubjectWithDetails(requestId, subject);		
		String eBody = replaceBodyApproverWithDetails(requestId, requestTitle, requiredDate, requestor, "", body);
		try{			
			String emailTo[] = getAlternateApproverDeptEmail(requestId);			
			notification.send(emailTo, eSubject, eBody);
			
		} catch (Exception er) {
			Log.getLog(getClass()).error("EngineeringModule: ERROR sendNotification " + er);
		}					
	}
	
	public void sendNotificationToAllFC(String requestId, String requestTitle, String requiredDate, String requestor, String subject, String body){		
		FmsNotification notification = new FmsNotification();		
		String eSubject = replaceSubjectWithDetails(requestId, subject);		
		String eBody = replaceBodyApproverWithDetails(requestId, requestTitle, requiredDate, requestor, "", body);
		try{			
			String emailTo[] = getAllFCEmail();			
			notification.send(emailTo, eSubject, eBody);
			
		} catch (Exception er) {
			Log.getLog(getClass()).error("EngineeringModule: ERROR sendNotification " + er);
		}					
	}
	
	public void sendNotificationToFC(String requestId, String requestTitle, String requiredDate, String requestor, String subject, String body){		
		FmsNotification notification = new FmsNotification();		
		String eSubject = replaceSubjectWithDetails(requestId, subject);		
		String eBody = replaceBodyApproverWithDetails(requestId, requestTitle, requiredDate, requestor, "", body);
		try{			
			String emailTo[] = getFCEmail(requestId);			
			notification.send(emailTo, eSubject, eBody);
			
		} catch (Exception er) {
			Log.getLog(getClass()).error("EngineeringModule: ERROR sendNotification " + er);
		}					
	}
	
	public void sendNotificationToHOU(String requestId, String requestTitle, String requiredDate, String requestor, String subject, String body){		
		FmsNotification notification = new FmsNotification();		
		String eSubject = replaceSubjectWithDetails(requestId, subject);		
		String eBody = replaceBodyApproverWithDetails(requestId, requestTitle, requiredDate, requestor, "", body);
		try{			
			String emailTo[] = getHOUEmail(requestId);			
			notification.send(emailTo, eSubject, eBody);
			
		} catch (Exception er) {
			Log.getLog(getClass()).error("EngineeringModule: ERROR sendNotification " + er);
		}					
	}
	
	public void sendNotificationToHOUUsingBGProcess(String requestId, String assignmentId, String requestTitle, String requiredDate, String requestor, String subject, String body){		
		//String eSubject = replaceSubjectWithDetails(requestId, subject);	
		String eSubject = replaceSubjectWithDetailsService(requestId, requestTitle, subject);
		String eBody = replaceBodyApproverWithDetailsService(requestId, "Manpower", requestTitle, requiredDate, requestor, "", body);
		try{			
			String emailTo[] = getHOUEmailByAssignmentId(assignmentId);		
			
			for(int i=0;i<emailTo.length;i++){
				//System.out.println("EMAILS : "+emailTo[i]);
				FMSMailUtil.sendEmail(null, true, null, emailTo[i], eSubject, eBody);
			}
			
			
			//notification.send(emailTo, eSubject, eBody);
			
		} catch (Exception er) {
			Log.getLog(getClass()).error("EngineeringModule: ERROR sendNotification " + er);
		}					
	}
	
	
	public void sendNotificationToHOUStoreUsingBGProcess(String requestId, String serviceType, String requestTitle, String requiredDate, String requestor, String subject, String body){		
		EngineeringDao dao = (EngineeringDao) getDao();
		String serviceTitle="";
		//String eSubject = replaceSubjectWithDetails(requestId, subject);	
		if(serviceType.equals(ServiceDetailsForm.SERVICE_OTHER))
			serviceTitle = "Other Facilities Equipment";
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_SCPMCP))
			serviceTitle = "SCP/MCP/OB/SNG/VSAT";
		String eSubject = replaceSubjectWithDetailsService(requestId, requestTitle, subject);
		String eBody = replaceBodyApproverWithDetailsService(requestId, serviceTitle, requestTitle, requiredDate, requestor, "", body);
		try{			
			
			//Map map =  new HashMap();
			//Collection list = dao.getUnitAlternateApproverEmail(rateCardId);	
			String[] emailTo =dao.getEngineeringUnitAppEmail(ENGINEERING_UNIT_ID);
			//String emailTo[]= new String[list.size()];
			//int listCounter=0;
			//for(Iterator it = list.iterator(); it.hasNext(); ){
			//	map = (HashMap) it.next();	
			//	emailTo[listCounter] = (String)map.get("email");
			//	listCounter++;
			//}
			
			
			for(int i=0;i<emailTo.length;i++){
				//System.out.println("EMAILS : "+emailTo[i]);
				FMSMailUtil.sendEmail(null, true, null, emailTo[i], eSubject, eBody);
			}
			
			
			//notification.send(emailTo, eSubject, eBody);
			
		} catch (Exception er) {
			Log.getLog(getClass()).error("EngineeringModule: ERROR sendNotification " + er);
		}					
	}
	
	public void sendNotificationToUnitAltApp(String requestId, String requestTitle, String requiredDate, String requestor, String subject, String body){		
		FmsNotification notification = new FmsNotification();		
		String eSubject = replaceSubjectWithDetails(requestId, subject);		
		String eBody = replaceBodyApproverWithDetails(requestId, requestTitle, requiredDate, requestor, "", body);
		try{			
			String emailTo[] = getUnitAlternateApprover(requestId);			
			notification.send(emailTo, eSubject, eBody);
			
		} catch (Exception er) {
			Log.getLog(getClass()).error("EngineeringModule: ERROR sendNotification " + er);
		}					
	}
	
	public void sendNotificationToEngUnitAltApp(String requestId, String requestTitle, String requiredDate, String requestor, String subject, String body) {
		FmsNotification notification = new FmsNotification();
		String eSubject = replaceSubjectWithDetails(requestId, subject);
		String eBody = replaceBodyApproverWithDetails(requestId, requestTitle, requiredDate, requestor, "", body);
		try {
			String emailTo[] = getEngUnitAltApp(requestId); 
			notification.send(emailTo, eSubject, eBody);
			
		} catch (Exception er) {
			Log.getLog(getClass()).error("EngineeringModule: ERROR sendNotificationToEngUnitAltApp " + er );
		}
	}
	
	private String replaceSubjectWithDetails(String requestId, String wordings) {
		String subject = wordings.replace("{REQUESTID}", requestId);
		return subject;
	}
	
	private String replaceSubjectWithDetailsService(String requestId, String requestTitle, String wordings) {
		String subject = wordings.replace("{REQUESTID}", requestId);
		subject = subject.replace("{REQUESTTITLE}", requestTitle);
		return subject;
	}
	
	private String replaceBodyWithLinks(String requestId, String requestTitle, String requiredDate, String totalRate, String remarks, String wordings) {
		String subject = wordings.replace("{REQUESTID}", requestId); 
		subject = subject.replace("{REQUESTTITLE}", requestTitle);
		subject = subject.replace("{REQUIREDDATE}", requiredDate);
		subject = subject.replace("{REMARKS}", totalRate);
		subject = subject.replace("{FEEDBACK_LINK}", remarks);
		
		return subject;
	}
	
	private String replaceBodyWithDetails(String requestId, String requestTitle, String requiredDate, String remarks, String wordings) {
		String subject = wordings.replace("{REQUESTID}", requestId);
		subject = subject.replace("{REQUESTTITLE}", requestTitle);
		subject = subject.replace("{REQUIREDDATE}", requiredDate);
		if (remarks!=null || !"".equals(remarks)){
			subject = subject.replace("{REMARKS}", remarks);
		}
		return subject;
	}
	
	private String replaceBodyWithDetailsService(String requestId, String requestTitle, String serviceTitle, String wordings) {
		String subject = wordings.replace("{REQUESTID}", requestId);
		subject = subject.replace("{REQUESTTITLE}", requestTitle);
		subject = subject.replace("{SERVICETITLE}", serviceTitle);
		
		return subject;
	}
	
	private String replaceBodyOutsourceWithDetails(String requestId, String requestTitle, String requiredDate, 
			String totalEstimatedCost, String totalActualCost, String outsources, String wordings){
		String subject = wordings.replace("{REQUESTID}", requestId);
		subject = subject.replace("{REQUESTTITLE}", requestTitle);
		subject = subject.replace("{REQUIREDDATE}", requiredDate);
		subject = subject.replace("{ESTIMATEDCOST}", totalEstimatedCost);
		subject = subject.replace("{ACTUALCOST}", totalActualCost);
		subject = subject.replace("{OUTSOURCESTABLE}", outsources);
		
		return subject;
	}
	
	private String replaceBodyApproverWithDetails(String requestId, String requestTitle, String requiredDate, String requestor, String remarks, String wordings) {
		String subject = wordings.replace("{REQUESTID}", requestId);
		subject = subject.replace("{REQUESTTITLE}", requestTitle);
		subject = subject.replace("{REQUIREDDATE}", requiredDate);
		subject = subject.replace("{REQUESTOR}", requestor);
		if (remarks!=null || !"".equals(remarks)){
			subject = subject.replace("{REMARKS}", remarks);
		}
		return subject;
	}
	
	private String replaceBodyApproverWithDetailsService(String requestId, String serviceTitle, String requestTitle, String requiredDate, String requestor, String remarks, String wordings) {
		String subject = wordings.replace("{REQUESTID}", requestId);
		subject = subject.replace("{REQUESTTITLE}", requestTitle);
		subject = subject.replace("{REQUIREDDATE}", requiredDate);
		subject = subject.replace("{REQUESTOR}", requestor);
		subject = subject.replace("{SERVICETITLE}", serviceTitle);
		if (remarks!=null || !"".equals(remarks)){
			subject = subject.replace("{REMARKS}", remarks);
		}
		return subject;
	}
	
	private String[] getRequestorEmail(String requestId) {
		EngineeringDao dao = (EngineeringDao) getDao();
		
		try {
			return dao.getRequestorEmail(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		
		return null;
	}
	
	private String[] getApproverEmail(String requestId) {
		EngineeringDao dao = (EngineeringDao) getDao();
		
		try {
			return dao.getApproverEmail(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		
		return null;
	}
	
	private String[] getAlternateApproverDeptEmail(String requestId) {
		EngineeringDao dao = (EngineeringDao) getDao();
		
		try {
			return dao.getAlternateApproverDeptEmail(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		
		return null;
	}
	
	public String[] getUnitAlternateApprover(String requestId){
		try{
			Collection emails = new ArrayList();
			
			EngineeringDao dao = (EngineeringDao) getDao();
			EngineeringRequest request=getRequestWithService(requestId);
			Collection services=request.getServices();
			
			for(Iterator<Service> itr=services.iterator();itr.hasNext();){
				Service service=(Service)itr.next();
				String serviceId=service.getServiceId();
				if(ServiceDetailsForm.SERVICE_SCPMCP.equals(serviceId)){
					Collection col=getScpService(requestId, serviceId);
					for(Iterator<ScpService> serItr=col.iterator();serItr.hasNext();){
						ScpService scp=(ScpService)serItr.next();
						emails.addAll(dao.getUnitAlternateApproverEmail(scp.getFacilityId()));
					}
				}else if(ServiceDetailsForm.SERVICE_POSTPRODUCTION.equals(serviceId)){
					Collection col=getPostProductionService(requestId, serviceId);
					for(Iterator<PostProductionService> serItr=col.iterator();serItr.hasNext();){
						PostProductionService post=(PostProductionService)serItr.next();
						emails.addAll(dao.getUnitAlternateApproverEmail(post.getFacilityId()));
					}
				}else if(ServiceDetailsForm.SERVICE_VTR.equals(serviceId)){
					Collection col=getVtrService(requestId, serviceId);
					for(Iterator<VtrService> serItr=col.iterator();serItr.hasNext();){
						VtrService vtr=(VtrService)serItr.next();
						emails.addAll(dao.getUnitAlternateApproverEmail(vtr.getFacilityId()));
					}
				}else if(ServiceDetailsForm.SERVICE_MANPOWER.equals(serviceId)){
					Collection col=getManpowerService(requestId, serviceId);
					for(Iterator<ManpowerService> serItr=col.iterator();serItr.hasNext();){
						ManpowerService man=(ManpowerService)serItr.next();
						emails.addAll(dao.getUnitAlternateApproverEmail(man.getCompetencyId()));
					}
				}else if(ServiceDetailsForm.SERVICE_STUDIO.equals(serviceId)){
					Collection col=getStudioService(requestId, serviceId);
					for(Iterator<StudioService> serItr=col.iterator();serItr.hasNext();){
						StudioService studio=(StudioService)serItr.next();
						emails.addAll(dao.getUnitAlternateApproverEmail(studio.getFacilityId()));						
					}
				}else if(ServiceDetailsForm.SERVICE_OTHER.equals(serviceId)){
					Collection col=getOtherService(requestId, serviceId);
					for(Iterator<OtherService> serItr=col.iterator();serItr.hasNext();){
						OtherService other=(OtherService)serItr.next();
						emails.addAll(dao.getUnitAlternateApproverEmail(other.getFacilityId()));
					}
				}else if(ServiceDetailsForm.SERVICE_TVRO.equals(serviceId)) {
					Collection col=getTvroService(requestId, serviceId);
					for(Iterator<TvroService> serItr=col.iterator();serItr.hasNext();){
						TvroService tvro = (TvroService) serItr.next();
						emails.addAll(dao.getUnitAlternateApproverEmail(tvro.getFacilityId()));
					}
				}
			}
			
			HashSet email = new HashSet(emails);
			
			return (String[])email.toArray(new String[0]);
		}catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}  
		
		return null;
	}
	
	public String[] getEngUnitAltApp(String requestId) {
		try {
			EngineeringDao dao = (EngineeringDao) getDao();
			EngineeringRequest request = getRequestWithService(requestId);
			Collection services = request.getServices();
			
			for(Iterator<Service> itr = services.iterator();itr.hasNext();){
				Service service = (Service) itr.next();
				String serviceId = service.getServiceId();
				if(ServiceDetailsForm.SERVICE_SCPMCP.equals(serviceId)){
					Collection col=getScpService(requestId, serviceId);
					
					if (col != null && col.size()>0){
						return dao.getEngineeringUnitAppEmail(ENGINEERING_UNIT_ID);
					}
					
				}else if(ServiceDetailsForm.SERVICE_POSTPRODUCTION.equals(serviceId)){
					Collection col=getPostProductionService(requestId, serviceId);
					
					if (col != null && col.size()>0){
						return dao.getEngineeringUnitAppEmail(ENGINEERING_UNIT_ID);
					}
					
				}else if(ServiceDetailsForm.SERVICE_VTR.equals(serviceId)){
					Collection col=getVtrService(requestId, serviceId);
					
					if (col != null && col.size()>0){
						return dao.getEngineeringUnitAppEmail(ENGINEERING_UNIT_ID);
					}

				}else if(ServiceDetailsForm.SERVICE_MANPOWER.equals(serviceId)){
					Collection col=getManpowerService(requestId, serviceId);
					
					if (col != null && col.size()>0){
						return dao.getEngineeringUnitAppEmail(ENGINEERING_UNIT_ID);
					}
					
				}else if(ServiceDetailsForm.SERVICE_STUDIO.equals(serviceId)){
					Collection col=getStudioService(requestId, serviceId);
					
					if (col != null && col.size()>0){
						return dao.getEngineeringUnitAppEmail(ENGINEERING_UNIT_ID);
					}
					
				}else if(ServiceDetailsForm.SERVICE_OTHER.equals(serviceId)){
					Collection col=getOtherService(requestId, serviceId);
					
					if (col != null && col.size()>0){
						return dao.getEngineeringUnitAppEmail(ENGINEERING_UNIT_ID);
					}
					
				}else if(ServiceDetailsForm.SERVICE_TVRO.equals(serviceId)) {
					Collection col=getTvroService(requestId, serviceId);
					
					if (col != null && col.size()>0){
						return dao.getEngineeringUnitAppEmail(ENGINEERING_UNIT_ID);
					}
				}
			}
			
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		
		return null;
	}
	
	private String[] getAllFCEmail () {
		EngineeringDao dao = (EngineeringDao) getDao();
		
		try {
			return dao.getAllFCEmail();
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		
		return null;
	}
	
	private String[] getFCEmail(String requestId) {
		EngineeringDao dao = (EngineeringDao) getDao();
		
		try {
			return dao.getFCEmail(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		
		return null;
	}
	
	private String[] getHOUEmail(String requestId) {
		EngineeringDao dao = (EngineeringDao) getDao();
		
		try {
			return dao.getHOUEmails(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		
		return null;
	}
	
	private String[] getHOUEmailByAssignmentId(String assignmentId) {
		EngineeringDao dao = (EngineeringDao) getDao();
		
		try {
			return dao.getHOUEmailsByAssignmentId(assignmentId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		
		return null;
	}
	
	public void updateRequestStatus(String requestId) {
		EngineeringDao dao = (EngineeringDao) getDao();
		UnitHeadDao uDao=(UnitHeadDao)Application.getInstance().getModule(UnitHeadModule.class).getDao();
		
		boolean completedFacilityAssignment = false;
		boolean completedManpowerAssignment = false;
		boolean completedDelayFA = false;
		boolean completedDelayMA = false;
		
		Collection manpowerAssignments = uDao.getManpowerAssignments(requestId);
		Collection facilityAssignments = uDao.getFacilityAssignments(requestId);

		int facilityCompleted = 0;
		for(Iterator<Assignment> facItr=facilityAssignments.iterator();facItr.hasNext();){
			Assignment a=(Assignment)facItr.next();
		
			if (ASSIGNMENT_FACILITY_STATUS_CHECKIN.equals(a.getStatus())){			
				Date requiredDate = a.getRequiredTo();
				requiredDate.setHours(Integer.parseInt(a.getToTime().split(":")[0]));
				requiredDate.setMinutes(Integer.parseInt(a.getToTime().split(":")[1]));
				
				if (requiredDate != null) {
					if (a.getCheckedInDate().after(requiredDate)){
						completedDelayFA = true;
					}
				}
				
				facilityCompleted++;
				
				if (facilityCompleted == facilityAssignments.size()) {
					completedFacilityAssignment = true;
				}
			}
		}		
		
		int manpowerCompleted = 0;
		for(Iterator<Assignment> manItr=manpowerAssignments.iterator();manItr.hasNext();){
			Assignment a=(Assignment)manItr.next();
			
			if (ASSIGNMENT_MANPOWER_STATUS_COMPLETED.equals(a.getStatus())){
				Date requiredDate = a.getRequiredTo();
				requiredDate.setHours(Integer.parseInt(a.getToTime().split(":")[0]));
				requiredDate.setMinutes(Integer.parseInt(a.getToTime().split(":")[1]));
				
				if (requiredDate != null) {
					if (a.getCompletionDate().after(requiredDate)){
						completedDelayMA = true;
					}
				}
				
				manpowerCompleted++;
				
				if (manpowerCompleted == manpowerAssignments.size()) {
					completedManpowerAssignment = true;
				}
			}
		}
		
		if (facilityAssignments.size()<=0) completedFacilityAssignment = true;
		if (manpowerAssignments.size()<=0) completedManpowerAssignment = true;
		
		if (completedFacilityAssignment && completedManpowerAssignment) {
			EngineeringRequest eRequest=new EngineeringRequest();
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			eRequest.setModifiedOn(now);
			eRequest.setRequestId(requestId);
			if (completedDelayFA || completedDelayMA) {
				eRequest.setStatus(LATE_STATUS);
			} else {
				eRequest.setStatus(FULFILLED_STATUS);
			}
			
			try {
				dao.updateRequestStatus(eRequest);
				
				if (FULFILLED_STATUS.equals(eRequest.getStatus()) || LATE_STATUS.equals(eRequest.getStatus())){
					sendFeedbackEmail(requestId);
				}
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.getMessage(), e);
			}
		}
		
	}
	
	public void insertFeedBack(Collection col){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			String id = UuidGenerator.getInstance().getUuid();
			dao.insertFeedBack(col, id);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void updateTotalAmountRequest(String requestId, Double totalInternalRate, Double totalExternalRate){
		EngineeringDao dao=(EngineeringDao)getDao();
		EngineeringRequest eRequest=new EngineeringRequest();
		try {
			eRequest.setRequestId(requestId);
			eRequest.setTotalInternalRate(totalInternalRate);
			eRequest.setTotalExternalRate(totalExternalRate);
			dao.updateTotalAmountRequest(eRequest);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		
	}
	
	public void updateTranportRequest(String requestId) {
		// cancel transport request (OB Van) if any
		Application application = Application.getInstance();
		TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
    	FMSRegisterManager FRM = (FMSRegisterManager) Application.getInstance().getModule(FMSRegisterManager.class);
    	
    	String reason = application.getMessage("fms.label.transportRequestCancelled");
    	String status = com.tms.fms.setup.model.SetupModule.CANCELLED_STATUS;
    	String userId = Application.getInstance().getCurrentUser().getId();
    	
    	TM.updateStatusFromEngineering(status, reason, requestId, userId, new Date());
    	try {
			TransportRequest TR = TM.selectTransportRequestByEngId(requestId);
			
			if (TR != null) {
				if (TR.getId()!=null) {
					TM.insertRequestStatus(TR.getId(), status, reason);
					
					try{
						String department = FRM.getUserDepartment(userId);
						String wordings = application.getMessage("fms.label.requestIdCancelled");
						TM.sendNotificationToApprovers(department,TR.getId(),wordings);
						
					} catch (Exception ex) {
						Log.getLog(getClass()).error("ERROR sendNotification "+ex);
					}
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}
	
	public String formatAmount(Double amount) {
		String totalAmount = "0.0";
		if (amount != null){
			NumberFormat n = NumberFormat.getCurrencyInstance(Locale.getDefault());
			totalAmount = n.format(amount);
			totalAmount = totalAmount.substring(1);
		}
		
		return totalAmount;
	}
	
	public Collection selectUnitByRequestId(String requestId, String serviceId) throws DaoException {
		Collection col = new ArrayList();
		Collection facilitiesUnitId = new ArrayList();
		Collection manpowerUnitId = new ArrayList();
		EngineeringDao dao=(EngineeringDao)getDao();
		
		try {			
			
			if(ServiceDetailsForm.SERVICE_SCPMCP.equals(serviceId)){
				facilitiesUnitId = dao.selectUnitByRequestId(requestId, "fms_eng_service_scp", "facilityId");	
				manpowerUnitId = dao.selectUnitManpowerByRequestId(requestId, "fms_eng_service_scp", "facilityId");				
			}else if(ServiceDetailsForm.SERVICE_POSTPRODUCTION.equals(serviceId)){
				facilitiesUnitId = dao.selectUnitByRequestId(requestId, "fms_eng_service_postproduction", "facilityId");	
				manpowerUnitId = dao.selectUnitManpowerByRequestId(requestId, "fms_eng_service_postproduction", "facilityId");
			}else if(ServiceDetailsForm.SERVICE_OTHER.equals(serviceId)){
				col = dao.selectUnitByRequestId(requestId, "fms_eng_service_other", "facilityId");	
				manpowerUnitId = dao.selectUnitManpowerByRequestId(requestId, "fms_eng_service_other", "facilityId");
			}else if(ServiceDetailsForm.SERVICE_STUDIO.equals(serviceId)){
				col = dao.selectUnitByRequestId(requestId, "fms_eng_service_studio", "facilityId");	
				manpowerUnitId = dao.selectUnitManpowerByRequestId(requestId, "fms_eng_service_studio", "facilityId");
			}else if(ServiceDetailsForm.SERVICE_MANPOWER.equals(serviceId)){
				manpowerUnitId = dao.selectUnitManpowerByRequestId(requestId, "fms_eng_service_manpower", "competencyId");
				facilitiesUnitId = dao.selectUnitByRequestId(requestId, "fms_eng_service_manpower", "competencyId");	
			}else if(ServiceDetailsForm.SERVICE_TVRO.equals(serviceId)){
				facilitiesUnitId = dao.selectUnitByRequestId(requestId, "fms_eng_service_tvro", "facilityId");	
				manpowerUnitId = dao.selectUnitManpowerByRequestId(requestId, "fms_eng_service_tvro", "facilityId");	
			}else if(ServiceDetailsForm.SERVICE_VTR.equals(serviceId)){
				facilitiesUnitId = dao.selectUnitByRequestId(requestId, "fms_eng_service_vtr", "facilityId");	
				manpowerUnitId = dao.selectUnitManpowerByRequestId(requestId, "fms_eng_service_vtr", "facilityId");	
			}

			if (facilitiesUnitId != null && facilitiesUnitId.size()>0) 
				col.addAll(facilitiesUnitId);
			
			if (manpowerUnitId != null && manpowerUnitId.size()>0)
				col.addAll(manpowerUnitId);
		} catch (Exception e){
			Log.getLog(getClass()).info(e.getMessage(), e);
		}
		return col;
	}
	
	public void insertRequestUnit(String requestId, String serviceId){
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			dao.deleteRequestUnit(requestId, serviceId);
			
			Collection units = selectUnitByRequestId(requestId, serviceId);
			if (units != null) {
				if (units.size() > 0){
					for (Iterator i = units.iterator(); i.hasNext();) {
						HashMap map = (HashMap) i.next();
						String rateCardId = "";
						if (map.get("facilityId")!=null){
							rateCardId = (String)map.get("facilityId");
						}
						dao.insertRequestUnit(UuidGenerator.getInstance().getUuid(), requestId, (String)map.get("unitId"), serviceId, rateCardId);
					}
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public Collection getEquipmentAssignmentByRequestId(String requestId){
		Collection col = new ArrayList();
		EngineeringDao dao=(EngineeringDao)getDao();
		
		try {
			col = dao.selectEquipmentAssignmentByRequestId(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public EngineeringRequest getAssignmentRequest(String groupId){
		EngineeringRequest request = null;
		EngineeringDao dao=(EngineeringDao)getDao();
		
		try {
			request = dao.selectAssignmentRequest(groupId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return request;
	}
	
	public String getRequestedItems(String groupId){
		Collection facilities = new ArrayList();
		Collection requestItems = new ArrayList();
		Collection rcs;
		SetupModule setupModule = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		FacilityDao fDao = (FacilityDao)Application.getInstance().getModule(FacilityModule.class).getDao();
		String items="";
		
		try {
			rcs = fDao.getRateCardByGroupId(groupId);
		
			if(rcs != null && rcs.size()>0){
				
				for (Iterator it = rcs.iterator(); it.hasNext();){
					HashMap mrcs = (HashMap) it.next();
					
					if (mrcs.get("rateCardId") != null && mrcs.get("rateCardCategoryId") != null){
						requestItems = setupModule.getRateCardEquipment((String)mrcs.get("rateCardId"), (String)mrcs.get("rateCardCategoryId"));
						facilities.addAll(requestItems);
					}
				}
			}					
			
			try{			
				for(Iterator<RateCard> rateItr=facilities.iterator();rateItr.hasNext();){
					RateCard rate=(RateCard)rateItr.next();
					if("".equals(items)){
						items=rate.getEquipment()+"("+rate.getEquipmentQty()+")";
					}else{
						items+=",<br>" + rate.getEquipment()+"("+rate.getEquipmentQty()+")";
					}
				}
			}catch (Exception e) {
				Log.getLog(getClass()).error(e.getMessage(), e);
			}
		} catch (DaoException e1) {
			Log.getLog(getClass()).error(e1.toString(), e1);
		}
		
		return items;
	}
	
	public boolean isValidCompletion(Date requiredFrom, Date today, Date completionDate) {
		boolean validCompletion = true;
		
		if ((completionDate.before(requiredFrom)) || (completionDate.after(today))){
			validCompletion = false;
		} 
		
		return validCompletion;
	}
	
	public String getUser(String userId){
		EngineeringDao dao = (EngineeringDao) getDao();
		String userName = "";
		
		userName = dao.getFullName("", userId);
		
		return userName;
	}
	
	/**
	 * Check services required date. Services required date must be within request date from and request date to.
	 *  
	 * @param requestId		
	 * @param requiredFrom	Request Date From 
	 * @param requiredTo	Request Date To
	 * @param service		Collection of the services in a request
	 * @return 
	 */
	public String checkServicesRequiredDate(String requestId, Date requiredFrom, Date requiredTo, Collection service) {
		String check = "";
		
		Collection scpCol 		= new ArrayList();
		Collection postCol 		= new ArrayList();
		Collection manpowCol	= new ArrayList();
		Collection studioCol	= new ArrayList();
		Collection otherCol		= new ArrayList();
		Collection tvroCol		= new ArrayList();
		Collection vtrCol 		= new ArrayList();
		
		if (service != null && service.size() > 0) {
			for(Iterator itr=service.iterator();itr.hasNext();){
				Service svc=(Service)itr.next();
				if (ServiceDetailsForm.SERVICE_SCPMCP.equals(svc.getServiceId())){
					// get collection of SCP service
					scpCol = getScpService(requestId, svc.getServiceId());
					
					// do checking
					if (scpCol != null && scpCol.size()>0) {
						for (Iterator i = scpCol.iterator(); i.hasNext();) {
							ScpService scp = (ScpService) i.next();
							
							if (scp.getRequiredFrom() != null && scp.getRequiredTo() != null && requiredFrom != null && requiredTo != null) {
								if (scp.getRequiredFrom().before(requiredFrom) || scp.getRequiredFrom().after(requiredTo)
									|| scp.getRequiredTo().before(requiredFrom) || scp.getRequiredTo().after(requiredTo)){
									check = INVALID_SERVICE_DATE;
									return check;
								}
							}
						}
					}else{
						check = INVALID_SERVICE_ITEM;
						return check;
					}
				} else if (ServiceDetailsForm.SERVICE_POSTPRODUCTION.equals(svc.getServiceId())){
					// get collection of POST PRODUCTION service
					postCol = getPostProductionService(requestId, svc.getServiceId());
					
					// do checking
					if (postCol != null && postCol.size()>0) {
						for (Iterator i = postCol.iterator(); i.hasNext();) {
							PostProductionService post = (PostProductionService) i.next();
							
							if (post.getRequiredDate() != null && post.getRequiredDateTo() != null && requiredFrom != null && requiredTo != null) {
								if (post.getRequiredDate().before(requiredFrom) || post.getRequiredDate().after(requiredTo)
									|| post.getRequiredDateTo().before(requiredFrom) || post.getRequiredDateTo().after(requiredTo)) {
									check = INVALID_SERVICE_DATE;
									return check;
								}
							}
						}
					}else{
						check = INVALID_SERVICE_ITEM;
						return check;
					}
					
				} else if (ServiceDetailsForm.SERVICE_VTR.equals(svc.getServiceId())){
					// get collection of VTR service
					vtrCol = getVtrService(requestId, svc.getServiceId());
					
					// do checking
					if (vtrCol != null && vtrCol.size()>0) {
						for (Iterator i = vtrCol.iterator(); i.hasNext();) {
							VtrService vtr = (VtrService) i.next();
							
							if (vtr.getRequiredDate() != null && vtr.getRequiredDateTo() != null && requiredFrom != null && requiredTo != null) {
								if (vtr.getRequiredDate().before(requiredFrom) || vtr.getRequiredDate().after(requiredTo)
									|| vtr.getRequiredDateTo().before(requiredFrom) || vtr.getRequiredDateTo().after(requiredTo)) {
									check = INVALID_SERVICE_DATE;
									return check;
								}
							}
						}
					}else{
						check = INVALID_SERVICE_ITEM;
						return check;
					}
				} else if (ServiceDetailsForm.SERVICE_OTHER.equals(svc.getServiceId())){
					// get collection of OTHER service
					otherCol = getOtherService(requestId, svc.getServiceId());
					
					// do checking
					if (otherCol != null && otherCol.size()>0) {
						for (Iterator i = otherCol.iterator(); i.hasNext();) {
							OtherService oth = (OtherService) i.next();
							
							if (oth.getRequiredFrom() != null && oth.getRequiredTo() != null && requiredFrom != null && requiredTo != null) {
								if (oth.getRequiredFrom().before(requiredFrom) || oth.getRequiredFrom().after(requiredTo)
									|| oth.getRequiredTo().before(requiredFrom) || oth.getRequiredTo().after(requiredTo)) {
									check = INVALID_SERVICE_DATE;
									return check;
								}
							}
						}
					}else{
						check = INVALID_SERVICE_ITEM;
						return check;
					}
				} else if (ServiceDetailsForm.SERVICE_STUDIO.equals(svc.getServiceId())){
					// get collection of STUDIO service
					studioCol = getStudioService(requestId, svc.getServiceId());
					
					// do checking
					if (studioCol != null && studioCol.size()>0) {
						for (Iterator i = studioCol.iterator(); i.hasNext();) {
							StudioService std = (StudioService) i.next();
							
							if (std.getBookingDate() != null && std.getBookingDateTo() != null && requiredFrom != null && requiredTo != null) {
								if (std.getBookingDate().before(requiredFrom) || std.getBookingDate().after(requiredTo)
									|| std.getBookingDateTo().before(requiredFrom) || std.getBookingDateTo().after(requiredTo)) {
									check = INVALID_SERVICE_DATE;
									return check;
								}
							}
						}
					}else{
						check = INVALID_SERVICE_ITEM;
						return check;
					}
				} else if (ServiceDetailsForm.SERVICE_MANPOWER.equals(svc.getServiceId())){
					// get collection of MANPOWER service
					manpowCol = getManpowerService(requestId, svc.getServiceId());					

					// do checking
					if (manpowCol != null && manpowCol.size()>0) {
						for (Iterator i = manpowCol.iterator(); i.hasNext();) {
							ManpowerService manpower = (ManpowerService) i.next();
							
							if (manpower.getRequiredFrom() != null && manpower.getRequiredTo() != null && requiredFrom != null && requiredTo != null) {
								if (manpower.getRequiredFrom().before(requiredFrom) || manpower.getRequiredFrom().after(requiredTo)
									|| manpower.getRequiredTo().before(requiredFrom) || manpower.getRequiredTo().after(requiredTo)) {
									check = INVALID_SERVICE_DATE;
									return check;
								}
							}
						}
					}else{
						check = INVALID_SERVICE_ITEM;
						return check;
					}
				} else if (ServiceDetailsForm.SERVICE_TVRO.equals(svc.getServiceId())){
					// get collection of TVRO service
					tvroCol = getTvroService(requestId, svc.getServiceId());
					
					// do checking
					if (tvroCol != null && tvroCol.size()>0) {
						for (Iterator i = tvroCol.iterator(); i.hasNext();) {
							TvroService tvro = (TvroService) i.next();
							
							if (tvro.getRequiredDate() != null && tvro.getRequiredDateTo() != null && requiredFrom != null && requiredTo != null) {
								if (tvro.getRequiredDate().before(requiredFrom) || tvro.getRequiredDate().after(requiredTo)
									|| tvro.getRequiredDateTo().before(requiredFrom) || tvro.getRequiredDateTo().after(requiredTo)) {
									check = INVALID_SERVICE_DATE;
									return check;
								}
							}
						}
					}else{
						check = INVALID_SERVICE_ITEM;
						return check;
					}
				}
			}
		}
		
		/*if (scpCol.size()<=0 && postCol.size()<=0 && manpowCol.size()<=0 && studioCol.size()<=0 && otherCol.size()<=0
				&& tvroCol.size()<=0 && vtrCol.size()<=0) { */
		/*if (scpCol.size()<=0 || postCol.size()<=0 || manpowCol.size()<=0 || studioCol.size()<=0 || otherCol.size()<=0 || tvroCol.size()<=0 || vtrCol.size()<=0) { 
			check = INVALID_SERVICE_ITEM;
			return check;
		}*/
		
		CheckAvailabilityModule mod = (CheckAvailabilityModule)Application.getInstance().getModule(CheckAvailabilityModule.class);
		try {
			if (mod.checkSCP(scpCol) 
				|| mod.checkPOST(postCol) 
				|| mod.checkManpower(manpowCol) 
				|| mod.checkStudio(studioCol) 
				|| mod.checkOther(otherCol) 
				|| mod.checkTvro(tvroCol) 
				|| mod.checkVtr(vtrCol)) {
				check = SERVICE_UNAVAILABLE;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error CheckAvailability DAO ", e);
		}
		
		return check;
	}
	
	public boolean isBlockBooking(String assignmentId){
		boolean result=false;
		try{
			EngineeringDao dao = (EngineeringDao) getDao();
			result = dao.isBlockBooking(assignmentId);	    
		}catch(Exception e){
			Log.getLog(EngineeringModule.class).error(e.getMessage(),e);
		}
		return result;
	}
	
	public String getServiceBlockBooking(String servId, String servType){
		String result="";
		try{
			EngineeringDao dao = (EngineeringDao) getDao();
			result = dao.getServiceBlockBooking(servId, servType);	    
		}catch(Exception e){
			Log.getLog(EngineeringModule.class).error(e.getMessage(),e);
		}
		return result;
	}
	
	public String[] getOtherAssignmentId(String assignmentId, String requestId) {
		EngineeringDao dao = (EngineeringDao) getDao();		
		String[] assignmentIds = null;
		
		try {
			// get serviceId based on assignmentId
			String serviceId = dao.selectServiceId(assignmentId);
			String currentManpowerId = dao.selectCurrentManpowerId(assignmentId);
			
			if (serviceId != null && !"".equals(serviceId)) {
				// get collection of 'group assignment id'
				Collection colGroupId = dao.selectGroupId(requestId, serviceId);
				
				if (colGroupId != null && colGroupId.size()>0) {
					
					assignmentIds = new String[colGroupId.size()];
					
					int x = 0;
					for (Iterator i = colGroupId.iterator(); i.hasNext();) {
						EngineeringRequest er = (EngineeringRequest) i.next();
						
						String othAsgId = dao.getOtherAssignmentId(assignmentId, er.getGroupId(), currentManpowerId);
						if (othAsgId != null && !"".equals(othAsgId)) {
							assignmentIds[x] = othAsgId;
							x++;
						}
					}
				}
			}
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return assignmentIds;
	}
	
	public boolean isGlobalAssignmentExist(){
		boolean result=false;
		try{
			EngineeringDao dao = (EngineeringDao) getDao();
			result = dao.isGlobalAssignmentExist();	    
		}catch(Exception e){
			Log.getLog(EngineeringModule.class).error(e.getMessage(),e);
		}
		return result;
	}
	
	public EngineeringRequest getGlobalAssignment() {
		EngineeringRequest request = null;
		EngineeringDao dao=(EngineeringDao)getDao();
		
		try {
			request = dao.selectGlobalAssignment();
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return request;
	}
	
	public void insertGlobalAssignment(EngineeringRequest eRequest) {
		EngineeringDao dao = (EngineeringDao)getDao();
		try {
			
			Date now=new Date();			

			eRequest.setGlobalAssignmentId(UuidGenerator.getInstance().getUuid());		
			eRequest.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			eRequest.setCreatedOn(now);			
			
			dao.insertGlobalAssignment(eRequest);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
	}
	
	public void updateGlobalAssignment(EngineeringRequest eRequest) {
		EngineeringDao dao = (EngineeringDao)getDao();
		try {
			
			Date now=new Date();			

			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			eRequest.setModifiedOn(now);			
			
			dao.updateGlobalAssignment(eRequest);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
	}
	
	public void setAssignmentSetting(int value){
		EngineeringDao dao = (EngineeringDao)getDao();
		Date scheduledDate = new Date();
		dao.insertAssignmentSetting(value, scheduledDate);
	}
	
	public void updateAssignmentSetting(int value){
		EngineeringDao dao = (EngineeringDao)getDao();
		Date scheduledDate = new Date();
		String settingId = dao.searchAssignmentSetting();
		if (settingId != null && !settingId.equals("")){
			dao.updateAssignmentSetting(settingId, value, scheduledDate);
		}else{
			dao.insertAssignmentSetting(value, scheduledDate);
		}
	}
	
	public void updateAutoAssignmentSetting(int value){
		EngineeringDao dao = (EngineeringDao)getDao();
		Date scheduledDate = new Date();
		String settingId = dao.searchAutoAssignmentSetting();
		if (settingId != null && !settingId.equals("")){
			dao.updateAutoAssignmentSetting(settingId, value, scheduledDate);
		}else{
			dao.insertAutoAssignmentSetting(value, scheduledDate);
		}
	}
	
	public int getSettingValue(){
		EngineeringDao dao = (EngineeringDao)getDao();
		int settingValue = dao.selectSettingValue();
		if(settingValue!=0){
			return settingValue;
		}
		return 0;
	}
	
	public int getAutoSettingValue(){
		EngineeringDao dao = (EngineeringDao)getDao();
		int settingValue = dao.selectAutoSettingValue();
		if(settingValue!=0){
			return settingValue;
		}
		return 0;
	}
	
	public Collection getAssignmentInformation(Date startDate, Date endDate) {
		Collection col=new ArrayList();
		EngineeringDao dao=(EngineeringDao)getDao();
		try {
			col= dao.selectAssignmentInformation(startDate, endDate);
			if (col!=null && col.size() >0){
				for(Iterator itr=col.iterator();itr.hasNext();){
					EngineeringRequest eRequest=(EngineeringRequest)itr.next();
					Collection manpowerAssignments =dao.getManpowerAssignments(eRequest.getRequestId(), startDate, endDate);
					eRequest.setAssignments(manpowerAssignments);
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public void revalidateBooking() {
		Collection col = new ArrayList();
		EngineeringDao dao = (EngineeringDao)getDao();
		
		// get all the request which has 'post production', 'vtr', 'studio', or 'tvro',
		// and status of request is Assignment or Outsourced
		try {
			col = dao.selectAllRequestToRevalidate();
			if (col != null && col.size()>0) {
				for (Iterator itr = col.iterator(); itr.hasNext();) {
					EngineeringRequest eRequest = (EngineeringRequest) itr.next();
					
					// delete all facilities booking of the request
					deleteBooking(eRequest.getRequestId());
					
					// create new booking
					insertBooking(eRequest.getRequestId());
				}
			}
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void deleteBooking(String requestId){
		EngineeringDao dao = (EngineeringDao) getDao();
		try {
			dao.deleteBooking(requestId);
		} catch (DaoException e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		
	}
	
	//start added new method for CR# 158
	public void updatePrepareCheckOutEquipment(EngineeringRequest eRequest) {
		EngineeringDao dao = (EngineeringDao) getDao();
		try {
			eRequest.setStatus(ASSIGNMENT_FACILITY_STATUS_PREPARE_CHECKOUT);
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser()
					.getUsername());
			Date now = new Date();
			eRequest.setModifiedOn(now);
			eRequest.setPrepareCheckOutDate(now);

			dao.updatePrepareCheckOutEquipment(eRequest);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
	}

	
	public Collection getGroupIdsList(String requestId) {
		EngineeringDao dao = (EngineeringDao) getDao();
		try {
			return dao.getGroupIdsList(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
			return null;
		}
	}

	// added on 15-03-2010 for CR#: 158
	public void insertPrepareCheckOutEquipmentByGroup(
			EngineeringRequest eRequest) {
		EngineeringDao dao = (EngineeringDao) getDao();
		try {

			eRequest.setAssignmentEquipmentId(UuidGenerator.getInstance()
					.getUuid());
			eRequest.setStatus(ASSIGNMENT_FACILITY_STATUS_PREPARE_CHECKOUT);
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser()
					.getUsername());
			eRequest.setCreatedBy(Application.getInstance().getCurrentUser()
					.getUsername());
			Date now = new Date();
			eRequest.setModifiedOn(now);
			eRequest.setCreatedOn(now);
			eRequest.setPrepareCheckOutDate(now);

			dao.insertPrepareCheckOutEquipmentByGroup(eRequest);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
	}

	
	public boolean updateExtraCheckOutEquipment(EngineeringRequest eRequest,
			String status) {
		EngineeringDao dao = (EngineeringDao) getDao();
		try {
			Date now = new Date();
			if (status.equals(ACTION_PREPARE_CHECKOUT)) {
				eRequest.setStatus(ASSIGNMENT_FACILITY_STATUS_PREPARE_CHECKOUT);
				eRequest.setPrepareCheckOutDate(now);
			} else {
				eRequest.setStatus(ASSIGNMENT_FACILITY_STATUS_CHECKOUT);
				eRequest.setCheckedOutDate(now);
			}

			eRequest.setModifiedBy(Application.getInstance().getCurrentUser()
					.getUsername());
			eRequest.setModifiedOn(now);

			return dao.updateExtraPrepareCheckOutEquipment(eRequest, status);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
			return false;
		}
	}
	
	public Collection getEquipmentListFromRequestId(String requestId, String rateCardCategoryId, boolean today) {
		EngineeringDao dao = (EngineeringDao) getDao();
		Collection col = new ArrayList();
		try {
			col = dao.getEquipmentListFromRequestId(requestId, rateCardCategoryId, today);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Unable getting result from the requestId", e);
		}
		return col;
	}
	
	public Collection getExtraEquipmentListFromRequestId(String requestId, String barcode, String rateCardCategoryId) {
		EngineeringDao dao = (EngineeringDao) getDao();
		Collection col = new ArrayList();
		try {
			col = dao.getExtraEquipmentListFromRequestId(requestId, barcode, rateCardCategoryId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Unable getting result from the requestId", e);
		}
		return col;
	}
	
	public EngineeringRequest getTopGroupId(String requestId) {
		EngineeringDao dao = (EngineeringDao) getDao();
		EngineeringRequest obj = new EngineeringRequest();
		try {
			obj = dao.selectTopGroupId(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Unable getting the first groupId from the requestId", e);
		}
		return obj;
	}
	//end added new method for CR# 158
	public EngineeringRequest selectParticularAssignment(String assignmentId, String assignmentType){
		EngineeringDao dao = (EngineeringDao) getDao();
		return dao.selectParticularAssignment(assignmentId, assignmentType);
	}
	public void cancelRequestItem(EngineeringRequest engine, String userId, Date today) throws DaoException {
		EngineeringDao dao = (EngineeringDao) getDao();
		EngineeringRequest req = engine;
//		if (engine.getAssignmentType().equals("M")){
//			req = dao.selectManpowerRecord(assignmentId);
//		}else {
//			req = dao.selectEquipmentRecord(assignmentId);
//		}
//		req.setServiceType(engine.getServiceType());
//		req.setCompetencyId(engine.getCompetencyId());
//		req.setRequestId(engine.getRequestId());
//		req.setAssignmentCode(engine.getAssignmentCode());
//		req.setAssignmentType(engine.getAssignmentType());
		req.setStatus(ASSIGNMENT_FACILITY_STATUS_CANCEL);
		req.setFlagMainCharges(engine.getFlagMainCharges());
		req.setCancelBy(userId);
		req.setCancelDate(today);
		//req.setSystemCalculatedCharges(dao.selectSystemCalculatedCharges(requestId, assignmentId, reqType));
		dao.insertCancelLog(req);
	}
	
	public String selectSystemCalculatedCharges(String requestId, String assignmentId, String reqType, String servType){
		EngineeringDao dao = (EngineeringDao) getDao();
		String charges = "";
		try{
			charges = dao.selectSystemCalculatedCharges(requestId, assignmentId, reqType, servType);
		}
		catch(Exception e){
			
		}
		return charges;
	}
	
	public Collection selectCancelRecord(String assignmentId, String assignmentType){
		EngineeringDao dao = (EngineeringDao) getDao();
		return dao.selectCancelRecord(assignmentId, assignmentType);
	}
	public void decreaseQuantityService(String serviceId){
		EngineeringDao dao = (EngineeringDao) getDao();
		dao.decreaseQuantityService(serviceId);
	}
	
	public Collection getFCsManpowerAssignments(String requestId, boolean isHOU){
		Collection col = new ArrayList();
		EngineeringDao dao = (EngineeringDao) getDao();
		SecurityService sec = (SecurityService)Application.getInstance().getService(SecurityService.class);
		UnitHeadDao uDao=(UnitHeadDao)Application.getInstance().getModule(UnitHeadModule.class).getDao();
		User user;
		
		Collection manpowerAssignments=uDao.getManpowerAssignments(requestId);
		Collection colOfCancelAssigmnt = null; 
		if(!isHOU)
			colOfCancelAssigmnt = dao.selectCancelRecord(requestId, "M");
		if (manpowerAssignments!=null && manpowerAssignments.size()>0){
			col.addAll(manpowerAssignments);
			
		}
		if (colOfCancelAssigmnt!=null && colOfCancelAssigmnt.size()>0){
			for(Iterator iterate = colOfCancelAssigmnt.iterator(); iterate.hasNext(); ){
				Assignment ass = (Assignment) iterate.next();
				try {
					user = sec.getUser(ass.getCancelBy());
					ass.setCancelBy("Cancelled by: "+ user.getProperty("firstName")+" "+user.getProperty("lastName"));
				} catch (SecurityException e) {
					Log.getLog(getClass()).error(e.getMessage(),e);
				}
				col.add(ass);
			}
		}
		return col;
	}
	public Collection getFCsFacilityAssignments(String requestId){
		Collection col = new ArrayList();
		EngineeringDao dao = (EngineeringDao) getDao();
		SecurityService sec = (SecurityService)Application.getInstance().getService(SecurityService.class);
		UnitHeadDao uDao=(UnitHeadDao)Application.getInstance().getModule(UnitHeadModule.class).getDao();
		User user;
		
		Collection facilityAssignments=uDao.getFacilityAssignments(requestId);
		Collection colOfCancelAssigmnt = dao.selectCancelRecord(requestId, "F");
		if (facilityAssignments!=null && facilityAssignments.size()>0){
			col.addAll(facilityAssignments);
		}
		if (colOfCancelAssigmnt!=null && colOfCancelAssigmnt.size()>0){
			for(Iterator iterate = colOfCancelAssigmnt.iterator(); iterate.hasNext(); ){
				Assignment ass = (Assignment) iterate.next();
				try {
					user = sec.getUser(ass.getCancelBy());
					ass.setCancelBy("Cancelled by: "+ user.getProperty("firstName")+" "+user.getProperty("lastName"));
				} catch (SecurityException e) {
					Log.getLog(getClass()).error(e.getMessage(),e);
				}
				col.add(ass);
			}
		}
		return col;
	}
	
	
	public Collection checkAssignmentByServiceId(String serviceId){
		EngineeringDao dao = (EngineeringDao) getDao();
		return dao.selectAssignmentByServiceId(serviceId);
	}
	
	public void deleteService(String serviceId, String requestId, String serviceType){
		EngineeringDao dao = (EngineeringDao) getDao();
		dao.deleteService(serviceId, requestId, serviceType);
	}
	
	public Collection selectManpowerAssignmentId(String requestId){
		EngineeringDao dao = (EngineeringDao) getDao();
		return dao.selectManpowerAssignmentId(requestId);
	}
	
	public void cancelFacilityBooking(String requestId, String facilityId, boolean reduce, Date bookFrom, Date bookTo, String timeFrom, String timeTo) {
		EngineeringDao dao = (EngineeringDao) getDao();
		
		// remove colon for time string
		timeFrom = StringUtil.replace(timeFrom, ":", "");
		timeTo = StringUtil.replace(timeTo, ":", "");
		
		// get only one facility booking that meets the criteria
		String toDelete = null;
		int quantity = 0;
		Collection col = dao.selectFacilityBooking(requestId, facilityId, bookFrom, bookTo, timeFrom, timeTo);
		if (col != null && col.size() > 0) {
			DefaultDataObject data = (DefaultDataObject) col.iterator().next();
			toDelete = data.getId();
			quantity = Integer.parseInt(data.getProperty("quantity").toString());
		}
		
		// delete one booking
		if (toDelete != null) {
			if (reduce && quantity > 1) {
				dao.reduceFacilityBookingById(toDelete);
			} else {
				dao.deleteFacilityBookingById(toDelete);
			}
		}
	}
	
	public boolean isOutsource(String requestId) {
		EngineeringDao dao = (EngineeringDao) getDao();
		boolean isOutSource = false;
		isOutSource=dao.isOutsource(requestId);
		
		return isOutSource;
	}
	
	public Collection selectRequestList(String keyword, String userName, String sort, boolean desc, int startIndex,int maxRows){
		EngineeringDao dao = (EngineeringDao) getDao();
		
		return dao.getRequestList(keyword, userName, sort, desc, startIndex, maxRows);
	}
	
	public Collection selectAssignmentByRequestIdAndServiceType(String requestId, Collection servType){
		EngineeringDao dao = (EngineeringDao) getDao();
		
		return dao.selectAssignmentByRequestIdAndServiceType(requestId, servType);
	}
	
	public int countRequestList(String keyword, String userName) {
		EngineeringDao dao = (EngineeringDao) getDao();
		
		return dao.countRequestList(keyword, userName);
	}
	
	public void copyServicesForTemplate(EngineeringRequest eRequest, String oldRequestId) {
		EngineeringDao dao 	= (EngineeringDao)getDao();
		Collection scpCol 	= getScpServiceForTemplate(oldRequestId, ServiceDetailsForm.SERVICE_SCPMCP);
		Collection postCol 	= getPostProductionServiceForTemplate(oldRequestId, ServiceDetailsForm.SERVICE_POSTPRODUCTION);
		Collection vtrCol	= getVtrServiceForTemplate(oldRequestId, ServiceDetailsForm.SERVICE_VTR);
		Collection manCol 	= getManpowerServiceForTemplate(oldRequestId, ServiceDetailsForm.SERVICE_MANPOWER);
		Collection stdCol	= getStudioServiceForTemplate(oldRequestId, ServiceDetailsForm.SERVICE_STUDIO);
		Collection othCol	= getOtherServiceForTemplate(oldRequestId, ServiceDetailsForm.SERVICE_OTHER);
		Collection tvroCol	= getTvroServiceForTemplate(oldRequestId, ServiceDetailsForm.SERVICE_TVRO);
		
		if (scpCol!=null && scpCol.size()>0 && !dao.searchScpService(eRequest.getRequestId())) {
			for (Iterator iscp = scpCol.iterator(); iscp.hasNext();){
				ScpService scp = (ScpService)iscp.next();
				scp.setRequestId(eRequest.getRequestId());
				scp.setRequiredFrom(eRequest.getRequiredFrom());
				scp.setRequiredTo(eRequest.getRequiredTo());
				scp.setSubmitted("0");
				//scp.setSettingFrom(eRequest.getFromTime());
				//scp.setSettingTo(eRequest.getToTime());
				insertScpService(scp);
			}
		}
		
		if (postCol!=null && postCol.size()>0 && !dao.searchPostProductionService(eRequest.getRequestId())) {
			for (Iterator ipost = postCol.iterator(); ipost.hasNext();){
				PostProductionService post = (PostProductionService)ipost.next();
				post.setRequestId(eRequest.getRequestId());
				post.setRequiredDate(eRequest.getRequiredFrom());
				post.setRequiredDateTo(eRequest.getRequiredTo());
				post.setSubmitted("0");
				insertPostProductionService(post);
			}
		}
		
		if (vtrCol!=null && vtrCol.size()>0 && !dao.searchVtrServiceByRequestId(eRequest.getRequestId())) {
			for (Iterator ivtr = vtrCol.iterator(); ivtr.hasNext();) {
				VtrService vtr = (VtrService)ivtr.next();
				vtr.setSubmitted("0");
				vtr.setRequestId(eRequest.getRequestId());
				vtr.setRequiredDate(eRequest.getRequiredFrom());
				vtr.setRequiredDateTo(eRequest.getRequiredTo());
				
				Collection files = getFiles(vtr.getId());
				
				vtr.setId(UuidGenerator.getInstance().getUuid());
				
				if (files!=null && files.size()>0){
					for (Iterator vtrFiles = files.iterator(); vtrFiles.hasNext();){
						VtrService vtrFile = (VtrService)vtrFiles.next();
						
						vtrFile.setId(vtr.getId());
						vtrFile.setFileId(UuidGenerator.getInstance().getUuid());
						vtrFile.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
						Date now=new Date();						
						vtrFile.setModifiedDate(now);
						
						insertVtrAttachment(vtrFile);
					}
				}
				
				insertCopyVtrService(vtr);
			}
		}
		
		if (manCol!=null && manCol.size()>0 && !dao.searchManpowerByRequestId(eRequest.getRequestId())) {
			for (Iterator iman = manCol.iterator(); iman.hasNext();){
				ManpowerService man = (ManpowerService)iman.next();
				man.setRequestId(eRequest.getRequestId());
				man.setRequiredFrom(eRequest.getRequiredFrom());
				man.setRequiredTo(eRequest.getRequiredTo());
				man.setSubmitted("0");
				insertManpowerService(man);
			}
		}
		
		if (stdCol!=null && stdCol.size()>0 && !dao.searchStudioServiceByRequestId(eRequest.getRequestId())) {
			for (Iterator istd = stdCol.iterator(); istd.hasNext();){
				StudioService std = (StudioService)istd.next();
				std.setRequestId(eRequest.getRequestId());
				std.setBookingDate(eRequest.getRequiredFrom());
				std.setBookingDateTo(eRequest.getRequiredTo());
				std.setSubmitted("0");
				insertStudioService(std);
			}
		}
		
		if (othCol!=null && othCol.size()>0 && !dao.searchOtherServiceByRequestId(eRequest.getRequestId())) {
			for (Iterator ioth = othCol.iterator(); ioth.hasNext();) {
				OtherService oth = (OtherService)ioth.next();
				oth.setRequestId(eRequest.getRequestId());
				oth.setRequiredFrom(eRequest.getRequiredFrom());
				oth.setRequiredTo(eRequest.getRequiredTo());
				oth.setSubmitted("0");
				insertOtherService(oth);
			}
		}
		
		if (tvroCol!=null && tvroCol.size()>0){
			for (Iterator itvro = tvroCol.iterator(); itvro.hasNext();){
				TvroService tvro = (TvroService)itvro.next();
				tvro.setRequestId(eRequest.getRequestId());
				tvro.setRequiredDate(eRequest.getRequiredFrom());
				tvro.setRequiredDateTo(eRequest.getRequiredTo());
				tvro.setSubmitted("0");
				insertTvroService(tvro);
			}
		}
	}
	
	public Collection getBlockBookingServices(String serviceTable){
		EngineeringDao dao 	= (EngineeringDao)getDao();
		
		return dao.collectBlockBookingServices(serviceTable);
	}
	
	public Collection getNonBlockBookingInParticularRequest(String requestId, String facilityId, String serviceTable){
		EngineeringDao dao 	= (EngineeringDao)getDao();
		
		return dao.collectNonBlockBookingInParticularRequest(requestId, facilityId, serviceTable);
	}	
	
	public void removeFacilityBooking(String requestId, String facilityId, Date bookFrom, Date bookTo){
		EngineeringDao dao 	= (EngineeringDao)getDao();
		
		dao.deleteFacilityBooking(requestId, facilityId, bookFrom, bookTo);
	}
	
	public Collection getAssignment(String requestId, String seviceId, Date requiredFrom, Date requiredTo, String type){
		EngineeringDao dao 	= (EngineeringDao)getDao();
		
		return dao.collectAssignment(requestId, seviceId, requiredFrom, requiredTo, type);
	}
	
	public int countDeletedFacilityBooking(String requestId, String facilityId, Date bookFrom, Date bookTo){
		EngineeringDao dao 	= (EngineeringDao)getDao();
		return dao.countDeletedFacilityBooking(requestId, facilityId, bookFrom, bookTo);
	}
	
	public void updateServiceByRequestId(String serviceType, String requestId) throws DaoException {
		EngineeringDao dao 	= (EngineeringDao)getDao();
		dao.updateServiceByRequestId(serviceType, requestId);
	}
	
	public long dateDiff4Assignment(Date start, Date end){		 
		long diff = Math.round((end.getTime() - start.getTime()) / (DateDiffUtil.MS_IN_A_DAY));			
		return diff;
	}
	
	public Collection scanDefectedTransportRequest(String reqId){
		EngineeringRequest er = getRequest(reqId);
		return scanDefectedTransportRequest(er);
	}
	public Collection scanDefectedTransportRequest(EngineeringRequest eRequest) {
		SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		EngineeringDao dao 	= (EngineeringDao)getDao();
		
		Collection scannedTR = new ArrayList();
		if(null!=eRequest){
		Collection scpCol 	= getScpService(eRequest.getRequestId(), ServiceDetailsForm.SERVICE_SCPMCP);
		Collection postCol 	= getPostProductionService(eRequest.getRequestId(), ServiceDetailsForm.SERVICE_POSTPRODUCTION);
		Collection vtrCol	= getVtrService(eRequest.getRequestId(), ServiceDetailsForm.SERVICE_VTR);
		Collection manCol 	= getManpowerService(eRequest.getRequestId(), ServiceDetailsForm.SERVICE_MANPOWER);
		Collection stdCol	= getStudioService(eRequest.getRequestId(), ServiceDetailsForm.SERVICE_STUDIO);
		Collection othCol	= getOtherService(eRequest.getRequestId(), ServiceDetailsForm.SERVICE_OTHER);
		Collection tvroCol	= getTvroService(eRequest.getRequestId(), ServiceDetailsForm.SERVICE_TVRO);
		
		
		// prepare for TransportRequest object
		TransportRequest tr = new TransportRequest();
		
		tr.setRequestTitle(eRequest.getTitle());
		tr.setEngineeringRequestId(eRequest.getRequestId());
		
		try {
		if (scpCol!=null && scpCol.size()>0) {
			for (Iterator iscp = scpCol.iterator(); iscp.hasNext();){
				ScpService scp = (ScpService)iscp.next();
				
				RateCard rc = module.getRateCard(scp.getFacilityId());
				
				if ("1".equals(rc.getTransportRequest()) && (rc.getVehicleCategoryId()!=null)){
					
					if(!dao.isTRExist(eRequest.getRequestId()))
					{
						tr.setRemarks(scp.getFacility());
						scannedTR.add(tr);}
				}
			}
		}
		
		if (postCol!=null && postCol.size()>0) {
			for (Iterator ipost = postCol.iterator(); ipost.hasNext();){
				PostProductionService post = (PostProductionService)ipost.next();
				
				RateCard rc = module.getRateCard(post.getFacilityId());
				
				if ("1".equals(rc.getTransportRequest()) && (rc.getVehicleCategoryId()!=null)){
					
					if(!dao.isTRExist(eRequest.getRequestId())){
						tr.setRemarks(post.getFacility());		
						scannedTR.add(tr);
					}
				}
			}
		}
		
		if (vtrCol!=null && vtrCol.size()>0) {
			for (Iterator ivtr = vtrCol.iterator(); ivtr.hasNext();){
				VtrService vtr = (VtrService)ivtr.next();
				
				RateCard rc = module.getRateCard(vtr.getFacilityId());
				
				if ("1".equals(rc.getTransportRequest()) && (rc.getVehicleCategoryId()!=null)){
					if(!dao.isTRExist(eRequest.getRequestId())){
					tr.setRemarks(vtr.getFacility());	
					scannedTR.add(tr);}

				}
			}
		}
		
		
		if (manCol!=null && manCol.size()>0) {
			for (Iterator iman = manCol.iterator(); iman.hasNext();){
				ManpowerService man = (ManpowerService)iman.next();
				
				RateCard rc = module.getRateCard(man.getCompetencyId());
				
				if ("1".equals(rc.getTransportRequest()) && (rc.getVehicleCategoryId()!=null)){
					if(!dao.isTRExist(eRequest.getRequestId())){		
					tr.setRemarks(man.getCompetencyName());
					scannedTR.add(tr);}

				}
			}
		}
		
		if (stdCol!=null && stdCol.size()>0) {
			for (Iterator istd = stdCol.iterator(); istd.hasNext();){
				StudioService std = (StudioService)istd.next();
				
				RateCard rc = module.getRateCard(std.getFacilityId());
				
				if ("1".equals(rc.getTransportRequest()) && (rc.getVehicleCategoryId()!=null)){
					
					if(!dao.isTRExist(eRequest.getRequestId())){
					tr.setRemarks(std.getFacility());
					scannedTR.add(tr);}
				}
			}
		}
		
		if (othCol!=null && othCol.size()>0) {
			for (Iterator ioth = othCol.iterator(); ioth.hasNext();){
				OtherService oth = (OtherService)ioth.next();
				
				RateCard rc = module.getRateCard(oth.getFacilityId());
				
				if ("1".equals(rc.getTransportRequest()) && (rc.getVehicleCategoryId()!=null)){
					
					if(!dao.isTRExist(eRequest.getRequestId())){	
					tr.setRemarks(oth.getFacility());
					scannedTR.add(tr);}
				}
			}
		}
		
		if (tvroCol!=null && tvroCol.size()>0) {
			for (Iterator itvro = tvroCol.iterator(); itvro.hasNext();){
				TvroService tvro = (TvroService)itvro.next();
				
				//RateCard rcService = module.getRateCardByService(ServiceDetailsForm.SERVICE_TVRO);
				//if (rcService!=null){
					RateCard rc = module.getRateCard(tvro.getFacilityId());
					if ("1".equals(rc.getTransportRequest()) && (rc.getVehicleCategoryId()!=null)){
						
						if(!dao.isTRExist(eRequest.getRequestId())){		
						tr.setRemarks(tvro.getFacility());
						scannedTR.add(tr);}
					}
				//}
			}
		}
		//return scannedTR;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error Add Transport Request" + e.getMessage());
		}
		
		}
		return scannedTR;
	}
	public Collection getRequestIdLinkToTR(){
		EngineeringDao dao 	= (EngineeringDao)getDao();
		Collection allReqs = new ArrayList();
		
		for(int i =1;i<=7;i++){
			Collection reqs =null;
			if (i!=4){
				reqs = dao.getRequestIdLinkToTR(String.valueOf(i));
				if(null!=reqs){
					for (Iterator iterator = reqs.iterator(); iterator.hasNext();) {
						String s = String.valueOf(iterator.next());
						allReqs.add(s);
					}
				}
					
			}
		}

		return allReqs;
	}
	public Collection getDefectedUnitHeadRequest(String requestId){
		EngineeringDao dao = (EngineeringDao) getDao();		
		Collection col= new ArrayList();
		
		try {
			col= dao.getDefectedUnitHeadRequest(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public void cancelAssignment(String assignmentId, String serviceType, String status) throws DaoException, SecurityException {
		Application app = Application.getInstance();
		EngineeringDao eDao = (EngineeringDao) app.getModule(EngineeringModule.class).getDao();
		EngineeringModule mod = (EngineeringModule) app.getModule(EngineeringModule.class);
		SecurityService sec = (SecurityService) app.getService(SecurityService.class);
		
		// get request info
		String requestId = eDao.selectRequestId(assignmentId);
		String requestTitle = eDao.selectRequestTitle(requestId);
		String requestType = eDao.selectRequestType(requestId);
		
		// get created username
		EngineeringRequest er = mod.getRequest(requestId);
		String createdUserName = er.getCreatedUserName();
		
		// display variables
		String erBody = Application.getInstance().getMessage("fms.notification.serviceCancelled.bodyRequestor");
		String erSubject = Application.getInstance().getMessage("fms.notification.serviceCancelled.subject");
		
		String flagMainCharges = "1";
		boolean firstTime = true;
		
		// reduce booking if quantity can be specified during service request (4=manpower 6=other)
		boolean reduce = (serviceType.equals("4") || serviceType.equals("6"));
		
		// get facilities for assignment
		Collection facilities = eDao.selectFacilityByAssignment(requestId, assignmentId);
		
		// logging
		TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
		transLog.info(requestId, "CANCEL_ASSIGNMENT", "assignmentId=" + assignmentId + " facilities=" + facilities.size());
		
		// loop facility
		for (Iterator iterate = facilities.iterator(); iterate.hasNext();) {
			EngineeringRequest facilityToCancel = (EngineeringRequest) iterate.next();
			String rateCardId = facilityToCancel.getRateCardId();
			
			EngineeringRequest engine = eDao.selectOneAssignmentFacility(assignmentId, facilityToCancel.getAssignmentType(), facilityToCancel.getFacilityId());
			if (engine != null) {
				// logging
				transLog.info(requestId, "CANCEL_BOOKING", "assignmentId=" + assignmentId 
						+ " requiredFrom=" + transLog.formatDate(facilityToCancel.getRequiredFrom()) 
						+ " requiredTo=" + transLog.formatDate(facilityToCancel.getRequiredTo()) 
						+ " facilityId=" + facilityToCancel.getFacilityId());
				
				// cancel booking
				mod.cancelFacilityBooking(requestId, facilityToCancel.getFacilityId(), reduce, facilityToCancel.getRequiredFrom(), facilityToCancel.getRequiredTo(), facilityToCancel.getFromTime(), facilityToCancel.getToTime());
				
				if (firstTime) {
					firstTime = false;
					
					// format the required date
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					String requiredDate = df.format(facilityToCancel.getRequiredFrom());
					
					// set charges to cancel log
					engine.setSystemCalculatedCharges(mod.selectSystemCalculatedCharges(requestId, facilityToCancel.getAssignmentId(), requestType, facilityToCancel.getServiceType()));
					engine.setFlagMainCharges(flagMainCharges);
					flagMainCharges = "0"; // for first item in the rate card set it to 1 else set it to 0
					
					// write to cancel log
					try {
						mod.cancelRequestItem(engine, app.getCurrentUser().getId(), new Date());
					} catch (DaoException e) {
						// note: should not occur since it loops only the first time
						Log.getLog(getClass()).error("Duplicate in fms_eng_cancel_service_log (assignmentId=" + assignmentId + ")");
					}
					
					// notification
					if (serviceType.equals("1") || serviceType.equals("6")) {
						mod.sendNotificationToHOUStoreUsingBGProcess(requestId, serviceType, requestTitle, requiredDate, createdUserName, erSubject, erBody);
					}
					
					// notification
					try {
						if (status.equals("Assigned")) {
							if (engine.getUserId() != null) {
								User user = sec.getUser(engine.getUserId());
								String email = (String) user.getProperty("email1");
						
								mod.sendNotificationToManpowerUsingBGProcess(requestId, email, requestTitle, requiredDate, "", erSubject, app.getMessage("fms.notification.requestCancelled.FCcancelAssignmentBody"));
							}
						}
					} catch (SecurityException e) {
						Log.getLog(getClass()).error(e.getMessage(),e);
					}
				}
			}
			
			// clean up request unit
			if (!eDao.isRateCardExistForRequest(requestId, rateCardId)) {
				eDao.deleteRateCardByReqIdFromRequestUnit(requestId, rateCardId); 
			}
		}
		
		// delete from fms_eng_assignment
		eDao.deleteAssignmentByAssignmentId(assignmentId);
		
		// clean up request service
		if (!eDao.isServiceTypeExist(requestId, serviceType)) {
			eDao.deleteServiceFromRequestServices(requestId, serviceType);
		}
		
		// notification to requestor
		String serviceTypes = eDao.selectServiceTitle(serviceType);
		String serviceTitle = Application.getInstance().getMessage(serviceTypes);
		mod.sendNotificationServiceToRequestorUsingBGProcess(requestId, requestTitle, assignmentId, serviceTitle, erSubject, erBody);
	}
	
	/** for abw integration ********************************************/
	public DefaultDataObject selectGlobalSetup(String taskId) {
			
		EngineeringDao dao = (EngineeringDao) getDao();
		try {
			return dao.selectGlobalSetup(taskId);
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return new DefaultDataObject();
		}
			
	}
	public void insertOrUpdateGlobalSetup(DefaultDataObject obj) {
		EngineeringDao dao = (EngineeringDao) getDao();
		try {
			if(dao.isExistGlobalSetup((String)obj.getProperty("taskId"))){
				dao.updateGlobal(obj);
			}else{
				dao.insertGlobalSetup(obj);
			}
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		
	}
	public void insertProgramCode(DefaultDataObject obj) {
		EngineeringDao dao = (EngineeringDao) getDao();
		AbwDao abwdao = (AbwDao)Application.getInstance().getModule(AbwModule.class).getDao();
		try {			
			dao.insertProgramCode(obj);
			abwdao.updateProgramCodeDateExtracted((String)obj.getProperty("programId"));
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		
	}
	
	/** end code for abw integration ********************************************/
	
	public DefaultDataObject getAbWEmailSetup()
	{
		EngineeringDao dao = (EngineeringDao) getDao();
		try 
		{
			Collection col = dao.getAbWEmailSetup();
			if(col != null && !col.isEmpty())
			{
				return (DefaultDataObject) col.iterator().next();
			}
			return null;
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error(" error @ EngineeringModule.getAbWEmailSetup() : " +e, e);
			return null;
		}
	}
	
	public String[] getAbWEmails()
	{
		DefaultDataObject obj = getAbWEmailSetup();
		ArrayList<String> arr = new ArrayList();
		for(int i = 0; i < AbwEmailsSetup.NUM_OF_EMAIL; i++)
		{
			if(obj.getProperty("email"+(i+1)) != null && !obj.getProperty("email"+(i+1)).equals(""))
			{
				arr.add((String)obj.getProperty("email"+(i+1)));
			}
		}
		
		String emailsArr[] = new String[arr.size()];
		int i = 0;
		for(String email : arr)
		{
			emailsArr[i] = email;
			i++;
		}
		
		return emailsArr;
	}

	public void insertAbWEmailSetup(DefaultDataObject obj)
	{
		EngineeringDao dao = (EngineeringDao) getDao();
		try 
		{
			dao.insertAbWEmailSetup(obj);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error(" error @ EngineeringModule.insertAbWEmailSetup(1) : " +e, e);
		}
	}

	public void deleteAbWEmailSetup()
	{
		EngineeringDao dao = (EngineeringDao) getDao();
		try 
		{
			dao.deleteAbWEmailSetup();
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error(" error @ EngineeringModule.deleteAbWEmailSetup() : " +e, e);
		}
	}

	public void pushTransferCostReq(Date reqDateFrom, Date reqDateTo, ArrayList statusArr)
	{
		EngineeringDao dao = (EngineeringDao) getDao();
		try 
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Collection<DefaultDataObject> col = dao.getRequest(null, sdf.format(reqDateFrom) + " 00:00:00", 
					sdf.format(reqDateTo) + " 23:59:59", statusArr);
			if(col != null && !col.isEmpty())
			{
				for(DefaultDataObject obj : col)
				{
					String requestId = (String) obj.getProperty("requestId");
					String pfeCode = (String) obj.getProperty("pfeCode");
					if(pfeCode == null || pfeCode.equals(""))
					{
						continue; // skip if program code not present #13016
					}
					
					DefaultDataObject pushObj = new DefaultDataObject();
					pushObj.setProperty("projectCode", pfeCode);
					pushObj.setProperty("requestId", requestId);
					pushObj.setProperty("createdDate", new Date());
					pushObj.setProperty("createdBy", SetupModule.FMS_SYSTEM_ADMIN);
					pushObj.setProperty("rateType", null);
					
					//manpower
					{
						pushObj.setProperty("serviceName", "manpower");
						Collection serviceCol = getManpowerByRequestId(requestId);
						pushTransferCostToABW(serviceCol, pushObj);
					}
					
					//scp
					{
						pushObj.setProperty("serviceName", "scp");
						Collection serviceCol = getScpByRequestId(requestId);
						pushTransferCostToABW(serviceCol, pushObj);
					}
					
					//other
					{
						pushObj.setProperty("serviceName", "other");
						Collection serviceCol = getOtherByRequestId(requestId);
						pushTransferCostToABW(serviceCol, pushObj);
					}
					
					//postproduction
					{
						pushObj.setProperty("serviceName", "post");
						Collection serviceCol = getPostproductionByRequestId(requestId);
						pushTransferCostToABW(serviceCol, pushObj);
					}
					
					//studio
					{
						pushObj.setProperty("serviceName", "studio");
						Collection serviceCol = getStudioByRequestId(requestId);
						pushTransferCostToABW(serviceCol, pushObj);
					}
					
					//tvro
					{
						pushObj.setProperty("serviceName", "tvro");
						Collection serviceCol = getTvroByRequestId(requestId);
						pushTransferCostToABW(serviceCol, pushObj);
					}
					
					//vtr
					{
						pushObj.setProperty("serviceName", "vtr");
						Collection serviceCol = getVtrByRequestId(requestId);
						pushTransferCostToABW(serviceCol, pushObj);
					}
				}
			}
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error(" error @ EngineeringModule.pushTransferCostReq(2) : " +e, e);
		}
	}
	
	private void pushTransferCostToABW(Collection serviceCol, DefaultDataObject pushObj)
	{
		if(serviceCol != null && !serviceCol.isEmpty())
		{
			EngineeringDao dao = (EngineeringDao) getDao();
			AbwModule am = (AbwModule)Application.getInstance().getModule(AbwModule.class);
			for(Iterator itr = serviceCol.iterator(); itr.hasNext();)
			{
				DefaultDataObject serviceObj = (DefaultDataObject) itr.next();
				double totalCost = 0;
				try
				{
					totalCost = dao.selectTotalRate((String)pushObj.getProperty("serviceName"), serviceObj.getId()) ;
				}
				catch (DaoException e)
				{
					Log.getLog(getClass()).error(e, e);
					totalCost = 0;
				}
				
				pushObj.setProperty("uniqueId", UuidGenerator.getInstance().getUuid());
				pushObj.setProperty("ratecardAbwCode", serviceObj.getProperty("ratecardAbwCode"));
				pushObj.setProperty("noOfUnit", serviceObj.getProperty("noOfUnit"));
				pushObj.setProperty("requiredDateFrom", serviceObj.getProperty("requiredDateFrom"));
				pushObj.setProperty("requiredDateTo", serviceObj.getProperty("requiredDateTo"));
				pushObj.setProperty("cost", totalCost);
				pushObj.setProperty("type", serviceObj.getProperty("serviceId"));
				pushObj.setProperty("blockBooking", EngineeringModule.BLOCK_BOOKING_NO);
				// change block booking indicator to Y or N  #13015
				if(serviceObj.getProperty("blockBooking") != null && serviceObj.getProperty("blockBooking").equals("1"))
				{
					pushObj.setProperty("blockBooking", EngineeringModule.BLOCK_BOOKING_YES);
				}
				
				am.insertAbwEngTransferCost(pushObj);
			}
		}
	}
	
	public Collection getManpowerByRequestId(String requestId)
	{
		EngineeringDao dao = (EngineeringDao) getDao();
		try 
		{
			return dao.getManpowerByRequestId(requestId);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error(" error @ EngineeringModule.getManpowerByRequestId(1) : " +e, e);
			return null;
		}
	}
	
	public Collection getScpByRequestId(String requestId)
	{
		EngineeringDao dao = (EngineeringDao) getDao();
		try 
		{
			return dao.getScpByRequestId(requestId);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error(" error @ EngineeringModule.getScpByRequestId(1) : " +e, e);
			return null;
		}
	}
	
	public Collection getOtherByRequestId(String requestId)
	{
		EngineeringDao dao = (EngineeringDao) getDao();
		try 
		{
			return dao.getOtherByRequestId(requestId);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error(" error @ EngineeringModule.getOtherByRequestId(1) : " +e, e);
			return null;
		}
	}

	public Collection getPostproductionByRequestId(String requestId)
	{
		EngineeringDao dao = (EngineeringDao) getDao();
		try 
		{
			return dao.getPostproductionByRequestId(requestId);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error(" error @ EngineeringModule.getPostproductionByRequestId(1) : " +e, e);
			return null;
		}
	}
	
	public Collection getStudioByRequestId(String requestId)
	{
		EngineeringDao dao = (EngineeringDao) getDao();
		try 
		{
			return dao.getStudioByRequestId(requestId);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error(" error @ EngineeringModule.getStudioByRequestId(1) : " +e, e);
			return null;
		}
	}
	
	public Collection getTvroByRequestId(String requestId)
	{
		EngineeringDao dao = (EngineeringDao) getDao();
		try 
		{
			return dao.getTvroByRequestId(requestId);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error(" error @ EngineeringModule.getTvroByRequestId(1) : " +e, e);
			return null;
		}
	}
	
	public Collection getVtrByRequestId(String requestId)
	{
		EngineeringDao dao = (EngineeringDao) getDao();
		try 
		{
			return dao.getVtrByRequestId(requestId);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error(" error @ EngineeringModule.getVtrByRequestId(1) : " +e, e);
			return null;
		}
	}
	
		
	public Collection pushTransferCostReqBackdated(Date reqDateFrom, Date reqDateTo, ArrayList statusArr)
	{
		EngineeringDao dao = (EngineeringDao) getDao();
		AbwModule abwmodule = (AbwModule)Application.getInstance().getModule(AbwModule.class);
		Collection colRet = new ArrayList();
		try 
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Collection<DefaultDataObject> col = dao.getRequest(null, sdf.format(reqDateFrom) + " 00:00:00", 
					sdf.format(reqDateTo) + " 23:59:59", statusArr);
			if(col != null && !col.isEmpty())
			{
				for(DefaultDataObject obj : col)
				{
					if(!abwmodule.existAbwEngTransferCost((String)obj.getProperty("requestId"))){
						
					
					String requestId = (String) obj.getProperty("requestId");
					String pfeCode = (String) obj.getProperty("pfeCode");
					if(pfeCode == null || pfeCode.equals(""))
					{
						continue; // skip if program code not present #13016
					}
					
					DefaultDataObject pushObj = new DefaultDataObject();
					pushObj.setProperty("projectCode", pfeCode);
					pushObj.setProperty("requestId", requestId);
					pushObj.setProperty("createdDate", new Date());
					pushObj.setProperty("createdBy", SetupModule.FMS_SYSTEM_ADMIN);
					pushObj.setProperty("rateType", null);
					
					//manpower
					{
						pushObj.setProperty("serviceName", "manpower");
						Collection serviceCol = getManpowerByRequestId(requestId);
						pushTransferCostToABW(serviceCol, pushObj);
					}
					
					//scp
					{
						pushObj.setProperty("serviceName", "scp");
						Collection serviceCol = getScpByRequestId(requestId);
						pushTransferCostToABW(serviceCol, pushObj);
					}
					
					//other
					{
						pushObj.setProperty("serviceName", "other");
						Collection serviceCol = getOtherByRequestId(requestId);
						pushTransferCostToABW(serviceCol, pushObj);
					}
					
					//postproduction
					{
						pushObj.setProperty("serviceName", "post");
						Collection serviceCol = getPostproductionByRequestId(requestId);
						pushTransferCostToABW(serviceCol, pushObj);
					}
					
					//studio
					{
						pushObj.setProperty("serviceName", "studio");
						Collection serviceCol = getStudioByRequestId(requestId);
						pushTransferCostToABW(serviceCol, pushObj);
					}
					
					//tvro
					{
						pushObj.setProperty("serviceName", "tvro");
						Collection serviceCol = getTvroByRequestId(requestId);
						pushTransferCostToABW(serviceCol, pushObj);
					}
					
					//vtr
					{
						pushObj.setProperty("serviceName", "vtr");
						Collection serviceCol = getVtrByRequestId(requestId);
						pushTransferCostToABW(serviceCol, pushObj);
					}
					
					colRet.add(obj);
					}//end if
				}
			}
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error(" error @ EngineeringModule.pushTransferCostReq(2) : " +e, e);
			return new ArrayList();
		}
		return colRet;
	}
	
	public double getTotalRateFacilities(String requestId, String rateType){
		EngineeringDao dao = (EngineeringDao) getDao();
		
		double totalRateFacilities = 0;
		double totalRateSCP 	= 0;
		double totalRatePost 	= 0;
		double totalRateOther 	= 0;
		double totalRateStudio	= 0;
		double totalRateVtr		= 0;
		double totalRateTvro	= 0;
		
		try {
			totalRateSCP 		= dao.selectTotalRate("scp", requestId, rateType) ;
			totalRatePost		= dao.selectTotalRate("post", requestId, rateType) ;
			totalRateOther		= dao.selectTotalRate("other", requestId, rateType) ;
			totalRateStudio		= dao.selectTotalRate("studio", requestId, rateType) ;
			totalRateVtr		= dao.selectTotalRate("vtr", requestId, rateType) ;
			totalRateTvro		= dao.selectTotalRate("tvro", requestId, rateType) ;
			
			totalRateFacilities = totalRateSCP + totalRatePost + totalRateOther + totalRateStudio + totalRateVtr + totalRateTvro;
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return totalRateFacilities;
	}
	
	public double getTotalRateManpower(String requestId, String rateType){
		EngineeringDao dao = (EngineeringDao) getDao();
		double totalRateManpower= 0;
		
		try {
			totalRateManpower	= dao.selectTotalRate("manpower", requestId, rateType) ;
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return totalRateManpower;
	}
	
	public void pushCancelCostToABW(Collection serviceCol, EngineeringRequest request){
		AbwModule am = (AbwModule)Application.getInstance().getModule(AbwModule.class);
		EngineeringDao dao = (EngineeringDao) getDao();
		if(serviceCol != null){
			for (Iterator iterator = serviceCol.iterator(); iterator.hasNext();) {
				DefaultDataObject serviceObj = (DefaultDataObject) iterator.next();
				
				TransferCostCancellationObject transferCostObj = new TransferCostCancellationObject();
				
				double totalCost = 0;
				try {
					totalCost = dao.selectTotalRate((String)request.getProperty("serviceName"), serviceObj.getId()) ;
				} catch (DaoException e) {
					e.printStackTrace();
				}
				
				transferCostObj.setUniqueId(UuidGenerator.getInstance().getUuid());
				transferCostObj.setProjectCode((String) request.getProperty("pfecode"));
				transferCostObj.setRequestId(request.getRequestId());
				transferCostObj.setRatecardAbwCode((String) serviceObj.getProperty("ratecardAbwCode"));	
				transferCostObj.setNoOfUnit((Integer) serviceObj.getProperty("noOfUnit"));
				transferCostObj.setRequiredDateFrom((Date) serviceObj.getProperty("requiredDateFrom"));
				transferCostObj.setRequiredDateTo((Date) serviceObj.getProperty("requiredDateTo"));
				transferCostObj.setCost(totalCost);
				
				if(serviceObj.getProperty("blockBooking").equals("1")){
					transferCostObj.setBlockBooking("Y");
				}else{
					transferCostObj.setBlockBooking("N");
				}
				
				transferCostObj.setType(request.getServiceId());
				transferCostObj.setCreatedDate(new Date());
				transferCostObj.setCreatedBy(SetupModule.FMS_SYSTEM_ADMIN);  //fairul said can hardcode
				transferCostObj.setStatus("N");
				transferCostObj.setCancellation_ind("Y"); //b4 end date, P
				transferCostObj.setCancellation_remark(getAdditionalInfo(request.getRequestId())); 
				
				am.insertTransferCostReversal(transferCostObj);
			}
		}
	}
	
	public String getServiceName (String serviceId){
		String serviceName = "";
		
		if(serviceId.equals(ServiceDetailsForm.SERVICE_SCPMCP)){
			serviceName = "scp";
		}else if (serviceId.equals(ServiceDetailsForm.SERVICE_POSTPRODUCTION)){
			serviceName = "post";
		}else if (serviceId.equals(ServiceDetailsForm.SERVICE_VTR)){
			serviceName = "vtr";
		}else if(serviceId.equals(ServiceDetailsForm.SERVICE_MANPOWER)){
			serviceName = "manpower";
		}else if(serviceId.equals(ServiceDetailsForm.SERVICE_STUDIO)){
			serviceName = "studio";
		}else if(serviceId.equals(ServiceDetailsForm.SERVICE_OTHER)){
			serviceName = "other";
		}else if(serviceId.equals(ServiceDetailsForm.SERVICE_TVRO)){
			serviceName = "tvro";
		}
		return serviceName;
	}
	
	public String getAdditionalInfo (String requestId){
		EngineeringDao dao = (EngineeringDao) getDao();
		String remark = "";
		try {
			remark = dao.getAdditionalInfo(requestId);
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return remark;
	}
}
