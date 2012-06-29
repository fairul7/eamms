package com.tms.fms.engineering.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;

import org.apache.axis.collections.SequencedHashMap;

import com.tms.fms.facility.model.FacilityDao;
import com.tms.fms.facility.model.FacilityModule;
import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.engineering.model.FacilityObject;
import com.tms.fms.engineering.model.EngineeringModule;

/**
 * @author fahmi
 *
 */
public class FacilitiesCoordinatorModule extends DefaultModule {
	public static SequencedHashMap REJECT_REASONS_MAP=new SequencedHashMap();
	
	public static final String OUTSOURCE_FILE_PATH = "/fms/engineering/outsource";

	public static final String FACILITY_CONTROLLER_PERMISSION = "com.tms.fms.facility.permission.facilityController";
	
	public void init() {
		Application app= Application.getInstance();
		//Reject Map Setup
		try {
			REJECT_REASONS_MAP.put("1", app.getMessage("fms.facility.label.cameraNotAvailable"));
			REJECT_REASONS_MAP.put("2", app.getMessage("fms.facility.label.obNotAvailable"));
			REJECT_REASONS_MAP.put("3", app.getMessage("fms.facility.label.mcpNotAvailable"));
			REJECT_REASONS_MAP.put("4", app.getMessage("fms.facility.label.manpowerNotAvailable"));
		} catch (Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		
		super.init();
	}
	
	public static boolean isFC(String userId){
		SecurityService security=(SecurityService)Application.getInstance().getService(SecurityService.class);
		boolean hasPermission = false;
		try {
			hasPermission = security.hasPermission(userId, FACILITY_CONTROLLER_PERMISSION , null, null);
		}
		catch(SecurityException error) {
		}
	
		return hasPermission;
	}
	
	public Collection getRequest(String search, String departmentId, String sort,boolean desc,int start,int rows) throws DaoException{
		Collection col=new ArrayList();
		FacilitiesCoordinatorDao dao=(FacilitiesCoordinatorDao)getDao();
		try {
			col= dao.selectIncomingRequestFC(search,departmentId, Application.getInstance().getCurrentUser().getId() ,sort,desc,start,rows);
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
	
	public Collection getRequestList(Date requiredTo, String search, Date requiredDate, String departmentId, String sort,boolean desc,int start,int rows) throws DaoException{
		Collection col=new ArrayList();
		FacilitiesCoordinatorDao dao=(FacilitiesCoordinatorDao)getDao();
		try {
			col= dao.selectIncomingRequestFCList(requiredTo, search, requiredDate, departmentId, Application.getInstance().getCurrentUser().getId() ,sort,desc,start,rows);
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
	
	public Collection getRequest(String search, String departmentId, String status, String sort,boolean desc,int start,int rows) throws DaoException{
		Collection col=new ArrayList();
		FacilitiesCoordinatorDao dao=(FacilitiesCoordinatorDao)getDao();
		try {
			col= dao.selectIncomingRequestFC(search,departmentId, Application.getInstance().getCurrentUser().getId() , status, sort,desc,start,rows);
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
	
	public Collection getRequestFCTable(String search,Date requiredDate, String departmentId, String status, String sort,boolean desc,int start,int rows) throws DaoException{
		Collection col=new ArrayList();
		FacilitiesCoordinatorDao dao=(FacilitiesCoordinatorDao)getDao();
		try {
			col= dao.selectIncomingRequestFCTable(search,requiredDate,departmentId, Application.getInstance().getCurrentUser().getId() , status, sort,desc,start,rows);
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
	
	public Collection getAllRequest(String search, String departmentId, String status, Date requiredFrom, Date requiredTo, String sort,boolean desc,int start,int rows) throws DaoException{
		SimpleDateFormat sdf = new SimpleDateFormat(SetupModule.DATE_FORMAT);
		try{
			FacilitiesCoordinatorDao dao=(FacilitiesCoordinatorDao)getDao();
			SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
			Collection finalCol = new ArrayList();
			Collection col = dao.selectAllRequestFC(search, departmentId, status, requiredFrom, requiredTo, sort, desc, start, rows);
			Collection colManpower = dao.selectAllRequestFCManpower(search, departmentId, status, requiredFrom, requiredTo, sort, desc, start, rows);
			
			for(Iterator<HashMap> itr=col.iterator();itr.hasNext();){
				HashMap map=(HashMap)itr.next();
				String rateCardId=(String)map.get("rateCardId");
				String rateCardCategoryId=(String)map.get("rateCardCategoryId");
				Integer qty=(Integer)map.get("qty");
				String items="";
				try{
					Collection facilities = module.getRateCardEquipment(rateCardId, rateCardCategoryId);
					for(Iterator<RateCard> rateItr=facilities.iterator();rateItr.hasNext();){
						RateCard rate=(RateCard)rateItr.next();
						if("".equals(items)){
							items=rate.getEquipment()+"("+qty+")";
						}else{
							items+=",<br>" + rate.getEquipment()+"("+qty+")";
						}
					}
				}catch (Exception e) {
					Log.getLog(getClass()).error(e.getMessage(), e);
				}
				String requiredDateFrom =  sdf.format(map.get("requiredFrom"));
				String requiredDateTo = sdf.format(map.get("requiredTo"));
				
				if (requiredDateFrom != null && requiredDateTo != null && requiredDateFrom.equals(requiredDateTo)){
					String requiredDateTime = requiredDateFrom +" "+map.get("fromTime")+ " - " + map.get("toTime");
					map.put("requiredDateTime", requiredDateTime);
				}else{
					String requiredDateTime = requiredDateFrom + " - " + requiredDateTo+" "+map.get("fromTime") +" - "+map.get("toTime");
					map.put("requiredDateTime", requiredDateTime);
				}
				//String requiredTime=map.get("fromTime")+" - "+map.get("toTime");
				//map.put("requiredTime", requiredTime);
				map.put("items", items);
				map.put("status", EngineeringModule.ASSIGNMENT_FACILITY_STATUS_MAP.get(getFinalStatus((map.get("assignmentId")+""))));
			}
			for(Iterator<HashMap> itrm = colManpower.iterator();itrm.hasNext();) {
				HashMap mapM = (HashMap) itrm.next();
				
				String requiredDateFrom =  sdf.format(mapM.get("fromDateTime"));
				String requiredDateTo = sdf.format(mapM.get("requiredTo"));
				if (requiredDateFrom != null && requiredDateTo != null && requiredDateFrom.equals(requiredDateTo)){
					String requiredDateTime = requiredDateFrom +" "+mapM.get("fromTime")+ " - " + mapM.get("toTime");
					mapM.put("requiredDateTime", requiredDateTime);
				}else{
					String requiredDateTime = requiredDateFrom + " - " + requiredDateTo+" "+mapM.get("fromTime")+ " - "+mapM.get("toTime");
					mapM.put("requiredDateTime", requiredDateTime);
				}
				//String requiredTime=mapM.get("fromTime")+" - "+mapM.get("toTime");
				//mapM.put("requiredTime", requiredTime);
				mapM.put("status", EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_MAP.get(mapM.get("status")+""));
			}
			finalCol.addAll(col);
			finalCol.addAll(colManpower);
			
			return finalCol;
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return new ArrayList();
	}
	
	public Collection getAllRequestForBatch(String search, String departmentId, String programId, Date fromDate, Date toDate) throws DaoException{
		try{
			FacilitiesCoordinatorDao dao=(FacilitiesCoordinatorDao)getDao();
			FacilityDao fdao = (FacilityDao)Application.getInstance().getModule(FacilityModule.class).getDao();
			Collection col = dao.selectAllRequestFCForBatch(search, departmentId, programId, fromDate, toDate);
			
			for(Iterator<HashMap> itr=col.iterator();itr.hasNext();){
				HashMap map=(HashMap)itr.next();
				
				String requiredTime=map.get("fromTime")+" - "+map.get("toTime");
				if (map.get("userId") != null) {
					map.put("pic", fdao.getFullName((String)map.get("userId")));
				}
				map.put("requiredTime", requiredTime);
				map.put("statusLabel", EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_MAP.get(map.get("status")+""));
			}
			return col;
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return new ArrayList();
	}
	
	public String getFinalStatus(String assignmentId){
		String status="";
		try{
			UnitHeadDao uDao=(UnitHeadDao)Application.getInstance().getModule(UnitHeadModule.class).getDao();
			Collection col=uDao.getChildFacilityAssignments(assignmentId);
			int in=0;
			int newStat=0;
			boolean out=false;
			for(Iterator<Assignment> itr=col.iterator();itr.hasNext();){
				Assignment assignment=(Assignment)itr.next();
				if(EngineeringModule.ASSIGNMENT_FACILITY_STATUS_CHECKOUT.equals(assignment.getStatus())){
					out=true;
					break;
				}else{
					if(EngineeringModule.ASSIGNMENT_FACILITY_STATUS_CHECKIN.equals(assignment.getStatus())){
						in++;
					}else{
						newStat++;
					}
				}
			}
			if(out){
				status=EngineeringModule.ASSIGNMENT_FACILITY_STATUS_CHECKOUT;
			}else{
				if(in==col.size()){
					status=EngineeringModule.ASSIGNMENT_FACILITY_STATUS_CHECKIN;
				}else{
					status=EngineeringModule.ASSIGNMENT_FACILITY_STATUS_NEW;
				}
			}
		}catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return status;
	}
	
	public Collection getOutsource(String search, String outsourceType, String requestId, String sort, boolean desc, int start, int rows) throws DaoException {
		Collection col = new ArrayList();
		
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao)getDao();
		try {
			col = dao.selectOutsource(search, outsourceType, requestId, sort, desc, start, rows);
		} catch (DaoException e){
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		
		return col;
	}
	
	public int getOutsourceCount(String search, String outsourceType, String requestId, String sort, boolean desc, int start, int rows) throws DaoException {
		int outsourceCount = 0;		
		
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao)getDao();
		try {
			return dao.selectOutsourceCount(search, outsourceType, requestId, sort, desc, start, rows);
		} catch (DaoException e){
			Log.getLog(getClass()).error(e.getMessage(), e);
			return outsourceCount;
		}
	}
	
	public Collection getOutsourceCompany(){
		Collection col = new ArrayList();
		
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao) getDao();
		try {
			col = dao.selectOutsourceCompany();
		} catch (DaoException e){
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		
		return col;
	}
	
	public EngineeringRequest getRequestById(String requestId){
		EngineeringRequest eRequest = null;
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao)getDao();
		try {
			eRequest = dao.selectOutsourceById(requestId);
		} catch (DaoException e){
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return eRequest;
	}
	
	public EngineeringRequest getRequestById(String requestId, String outsourceId){
		EngineeringRequest eRequest = null;
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao)getDao();
		try {
			eRequest = dao.selectOutsourceById(requestId,outsourceId);
		} catch (DaoException e){
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return eRequest;
	}
	
	public int selectRequestCount(String searchBy, String departmentId){
		int countRcm = 0;
		
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao) getDao();
		try {
			return dao.selectRequestCount(searchBy, departmentId, Application.getInstance().getCurrentUser().getId());
		} catch (DaoException e){
			return countRcm;
		}
	}
	
	public int selectRequestCount(String searchBy, String departmentId, String status){
		int countRcm = 0;
		
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao) getDao();
		try {
			return dao.selectRequestCount(searchBy, departmentId, status, Application.getInstance().getCurrentUser().getId());
		} catch (DaoException e){
			return countRcm;
		}
	}
	
	public int selectRequestFCTableCount(String searchBy, Date requiredDate, String departmentId, String status){
		int countRcm = 0;
		
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao) getDao();
		try {
			return dao.selectRequestFCTableCount(searchBy, requiredDate, departmentId, status, Application.getInstance().getCurrentUser().getId());
		} catch (DaoException e){
			return countRcm;
		}
	}
	
	
	public int selectRequestCountList(Date requiredTo, String searchBy, Date requiredDate, String departmentId){
		int countRcm = 0;
		
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao) getDao();
		try {
			return dao.selectRequestCountList(requiredTo, searchBy, requiredDate, departmentId, Application.getInstance().getCurrentUser().getId());
		} catch (DaoException e){
			return countRcm;
		}
	}
	
	public void insertOutsource(EngineeringRequest er){
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao) getDao();
		try {
			dao.insertOutsource(er);
		} catch (DaoException e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void updateOutsource(EngineeringRequest er){
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao) getDao();
		try {
			dao.updateOutsource(er);
		} catch (DaoException e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void updateRequestFCCancel(EngineeringRequest er){
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao) getDao();
		
		Application application = Application.getInstance();
		EngineeringModule module = (EngineeringModule) application.getModule(EngineeringModule.class);
		EngineeringDao engineeringDao = (EngineeringDao) application.getModule(EngineeringModule.class).getDao();
		FacilityModule facilityModule = (FacilityModule) application.getModule(FacilityModule.class);
		TransLogModule transLog = (TransLogModule) application.getModule(TransLogModule.class);
		
		try {
			// logging
			transLog.info(er.getRequestId(), "CANCEL_REQUEST", "cancellationCharges=" + er.getCancellationCharges());
			
			// update for cancellation 
			dao.updateRequestFCCancel(er);
			
			// status trail
			module.insertRequestStatus(er.getRequestId(), EngineeringModule.CANCELLED_STATUS , "", "");
			
			// get current user
			User user = Application.getInstance().getCurrentUser();
			String userId = null;
			if (user != null) {
				userId = user.getId();
			}
			
			// delete assignment
			Collection assignment = module.getAssignmentByRequestId(er.getRequestId());
			if (assignment!=null && assignment.size()>0){
				for (Iterator<Assignment> itr = assignment.iterator(); itr.hasNext();){
					Assignment asg = (Assignment) itr.next();
					String assignmentId = asg.getAssignmentId();
					
					if (EngineeringModule.ASSIGNMENT_TYPE_FACILITY.equals(asg.getAssignmentType())){
						// get checked out equipment
						Collection checkedOutList = dao.getCheckedOutEquipmentForCancelledRequest(assignmentId);
						
						for (Iterator iterator = checkedOutList.iterator(); iterator.hasNext();) {
							Assignment checkedOutItem = (Assignment) iterator.next();
							String barcode = checkedOutItem.getBarcode();
							
							boolean extraCheckout = ("-".equals(checkedOutItem.getAssignmentId()));
							if (extraCheckout) {
								// logging
								transLog.info(er.getRequestId(), "CANCEL_CHECK_OUT", "Extra: barcode=" + barcode + " assignmentId=" + assignmentId);
								
								// delete assignment equipment for extra check out
								engineeringDao.deleteEquipmentAssignmentById(checkedOutItem.getId());
							} else {
								// logging
								transLog.info(er.getRequestId(), "CANCEL_CHECK_OUT", "Assignment: barcode=" + barcode + " assignmentId=" + assignmentId);
							}
							
							// undo check out for item
							facilityModule.updateEquipmentStatus2CheckedIn(barcode, userId);
						}
						
						// delete assignment equipment
						module.deleteEquipmentAssignment(assignmentId);
					} else {
						module.deleteManpowerAssignment(assignmentId);
					}
				}
				
				module.deleteAssignment(er.getRequestId());
			}
			
			//send email to 
			module.sendCancellationEmail(er.getRequestId(), false, true, false, false);
		} catch (DaoException e){
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
	}
	public void updateRequestLate(EngineeringRequest er){
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao) getDao();
		EngineeringModule module =(EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		try {
			dao.updateRequestLate(er);
			
			//send email to "requestor" to inform the late completion charges
			module.sendLateCompletionFCEmail(er.getRequestId(), er.getLateCharges());
		} catch (DaoException e){
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
	}
	
	public void insertOutsourceAttachment(EngineeringRequest er){
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao) getDao();
		try {
			dao.insertOutsourceAttachment(er);
		} catch (DaoException e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void deleteOutsource(String outsourceId){
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao) getDao();
		try {
			dao.deleteOutsource(outsourceId);
		} catch (DaoException e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void deleteOutsourceFile(String fileId){
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao) getDao();
		try {
			dao.deleteOutsourceFile(fileId);
		} catch (DaoException e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public Collection getFiles(String outsourceId){
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao) getDao();
		Collection col= new ArrayList();
		try {
			col= dao.selectFiles(outsourceId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public EngineeringRequest getFile(String fileId){
		EngineeringRequest eRequest = null;
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao)getDao();
		try {
			eRequest = dao.selectFile(fileId);
		} catch (DaoException e){
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return eRequest;
	}
	
	public double selectOutsourceCost(String requestId){
		double sumOutsourceCost = 0;
		
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao) getDao();
		try {
			return dao.selectOutsourceCost(requestId);
		} catch (DaoException e){
			return sumOutsourceCost;
		}
	}
	
	public double selectOutsourceActualCost(String requestId){
		double sumOutsourceCost = 0;
		
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao) getDao();
		try {
			return dao.selectOutsourceActualCost(requestId);
		} catch (DaoException e){
			return sumOutsourceCost;
		}
	}
	
	public void rejectRequest(String requestId, String reason, String otherReason){
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao) getDao();
		
		try {
			EngineeringRequest eRequest=new EngineeringRequest();
			EngineeringModule module = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			EngineeringDao eDao= (EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
			
			eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			eRequest.setModifiedOn(now);
			eRequest.setStatus(EngineeringModule.REJECTED_STATUS);
			eRequest.setRequestId(requestId);
			dao.updateRequestStatus(eRequest);
			eDao.deleteBooking(requestId);
			module.insertRequestStatus(requestId, EngineeringModule.REJECTED_STATUS, otherReason, reason);
			
			module.sendRejectionFCEmail(requestId, reason + "." + otherReason);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public Collection getAvailability(String ckType, Date startDate, Date endDate){
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao) getDao();
		Collection col= new ArrayList();
		try {
			int totalDays = 0;
			totalDays = dao.countDays(startDate, endDate);
			
			for (int i=0; i< totalDays; i++){
				
			}
			
			//col= dao.selectFiles();
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public FacilityObject getFacility(String id){
		FacilityObject c = new FacilityObject();
		try{
			FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao) getDao();
			Collection rows = dao.selectFacility(id);
			if (rows.size() > 0) {c = (FacilityObject)rows.iterator().next();}
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getFacility(1)", e);
		}
		return c;
	}
	
	public Collection getRequestFCTable(String search,Date requiredDate,Date requiredTo, String departmentId, String status, String sort,boolean desc,int start,int rows) throws DaoException{
		Collection col=new ArrayList();
		FacilitiesCoordinatorDao dao=(FacilitiesCoordinatorDao)getDao();
		try {
			col= dao.selectIncomingRequestFCTable(search,requiredDate, requiredTo, departmentId, Application.getInstance().getCurrentUser().getId() , status, sort,desc,start,rows);
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
	
	public int selectRequestFCTableCount(String searchBy, Date requiredDate, Date requiredTo, String departmentId, String status){
		int countRcm = 0;
		
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao) getDao();
		try {
			return dao.selectRequestFCTableCount(searchBy, requiredDate, requiredTo,  departmentId, status, Application.getInstance().getCurrentUser().getId());
		} catch (DaoException e){
			return countRcm;
		}
	}
	public double selectTotalSystemCalculatedCharges(String requestId){
		int countRcm = 0;
		
		FacilitiesCoordinatorDao dao = (FacilitiesCoordinatorDao) getDao();
		try {
			return dao.selectTotalSystemCalculatedCharges(requestId);
		} catch (DaoException e){
			return countRcm;
		}
	}
	
}
