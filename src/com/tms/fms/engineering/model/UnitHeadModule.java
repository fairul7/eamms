/**
 * 
 */
package com.tms.fms.engineering.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultDataObject;
import kacang.model.DefaultModule;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.crm.sales.misc.DateUtil;
import com.tms.fms.engineering.ui.ServiceDetailsForm;
import com.tms.fms.facility.model.RateCard;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.util.DateDiffUtil;
import com.tms.fms.util.PagingUtil;

/**
 * @author arunkumar
 *
 */
public class UnitHeadModule extends DefaultModule {
	int countMyHOU = 0;
	
	public Collection getHOURequest(String search, Date requiredDate, String status,String department, String sort,boolean desc,int start,int rows) throws DaoException{
		Collection col=new ArrayList();
		UnitHeadDao dao=(UnitHeadDao)getDao();
		EngineeringDao eDao=(EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
		try {
			col= dao.selectHOURequest(search, requiredDate, status, department, sort, desc,start,rows);
			for(Iterator itr=col.iterator();itr.hasNext();){
				EngineeringRequest eRequest=(EngineeringRequest)itr.next();
				Collection services=eDao.selectRequestServices(eRequest.getRequestId());
				eRequest.setServices(services);
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	// get request list that belong to logged in user
	public Collection getMyHOURequest(String search, Date requiredDate, String status,String department, String userId, String sort,boolean desc,int start,int rows) throws DaoException{
		Collection col=new ArrayList();
		UnitHeadDao dao=(UnitHeadDao)getDao();
		countMyHOU = 0;
		EngineeringDao eDao=(EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
		try {
			Collection tempCol= dao.selectHOURequest(search, requiredDate, status, department, sort, desc, 0, -1);

			for(Iterator itr=tempCol.iterator();itr.hasNext();){
				EngineeringRequest eRequest=(EngineeringRequest)itr.next();
				
				if (EngineeringModule.ASSIGNMENT_STATUS.equals(status)){
					if (!isAssignmentPrepared(eRequest.getRequestId())){
						if (getUnitServicesApprover(eRequest.getRequestId(), userId).size()>0) {
							
							Collection services=eDao.selectRequestServices(eRequest.getRequestId());
							eRequest.setServices(services);
							col.add(eRequest);
							countMyHOU++;
						}
					}
				} else {
					if (getUnitServicesApprover(eRequest.getRequestId(), userId).size()>0) {
						
						Collection services=eDao.selectRequestServices(eRequest.getRequestId());
						eRequest.setServices(services);
						col.add(eRequest);
						countMyHOU++;
					}
				}
				
			}
			
			col = PagingUtil.getPagedCollection(col, start, rows);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public int countMyHOURequest() {
		return countMyHOU;
	}
	
	// updating getMyHOURequest()
	public Collection getMyHOURequestList(Date requiredTo, String search, Date requiredDate, String status,String department, String userId, String sort,boolean desc,int start,int rows) throws DaoException{ 
		Collection col=new ArrayList();
		UnitHeadDao dao=(UnitHeadDao)getDao();
		EngineeringDao eDao=(EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
		col = dao.selectHOURequestList(requiredTo, search, requiredDate, status, department, userId, sort, desc, start, rows);
		
		for(Iterator itr=col.iterator();itr.hasNext();){
			EngineeringRequest eRequest=(EngineeringRequest)itr.next();
			
			Collection services = eDao.selectRequestServices(eRequest.getRequestId());
			eRequest.setServices(services);
		}
		return col;
	}
	
	public Collection getHOUTodaysAssignment(String search, String department, String userId, String sort,boolean desc,int start,int rows){
		UnitHeadDao dao=(UnitHeadDao)getDao();
		EngineeringDao eDao=(EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
		
		int settingValue = dao.selectAssignmentSettingValue();
		
		if(settingValue!=0){
			// Select The Assignment By Setting Value Range
			//Collection col = dao.selectHOUTodaysAssignment(settingValue, search, department, userId, sort, desc, start, rows);
			Collection col = dao.selectHOUTodaysAssignmentNew(settingValue, search, department, userId, sort, desc, start, rows);
			
			for(Iterator itr=col.iterator();itr.hasNext();){
				try {
					EngineeringRequest eRequest=(EngineeringRequest)itr.next();
					Collection services = eDao.selectRequestServices(eRequest.getRequestId());
					eRequest.setServices(services);
				} catch (DaoException e) {
					Log.getLog(getClass()).error(e.getMessage(),e);
				}
			}
			return col;
		}
		return null;
	}
	
	public int countHOURequestList(Date requiredTo, String search, Date requiredDate, String status,String department, String userId){
		int countHOURequestList = 0;
		UnitHeadDao dao = (UnitHeadDao) getDao();		
		
		try {
			countHOURequestList = dao.countHOURequestList(requiredTo, search, requiredDate, status, department, userId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
			return 0;
		}
		return countHOURequestList;
	}
	
	public int countHOUTodaysAssignment(String search,String department, String userId){
		UnitHeadDao dao = (UnitHeadDao) getDao();		
		int settingValue = dao.selectAssignmentSettingValue();
		if (settingValue!=0){
			return dao.countHOUTodaysAssignment(settingValue, search, department, userId);
		}
		return 0;
	}
	
	public int countMyHOURequest(String search, Date requiredDate, String status,String department, String userId, String sort,boolean desc) throws DaoException{
		int result = 0;
		Collection col = new ArrayList();
		col = getMyHOURequest(search, requiredDate, status, department, userId, sort, desc, 0, -1);
		if (col != null && col.size()>0) {
			result = col.size();
		}
		
		return result;
	}

	public static boolean isUnitApprover(String userId){
		boolean result=false;
		try{
			EngineeringDao dao=(EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
			result = dao.isUnitApprover(userId);	    
		}catch(Exception e){
			Log.getLog(UnitHeadModule.class).error(e.getMessage(),e);
		}
		return result;
	}
	public static boolean isUnitApprover(String userId,String requestId){
		boolean result=false;
		try{
			UnitHeadDao dao=(UnitHeadDao)Application.getInstance().getModule(UnitHeadModule.class).getDao();
			return dao.isUnitApprover(userId,requestId);	    
		}catch(Exception e){
			Log.getLog(UnitHeadModule.class).error(e.getMessage(),e);
		}
		return result;
	}
	
	public static boolean isUnitApproverByUnitId(String userId,String unitId){
		boolean result=false;
		try{
			UnitHeadDao dao=(UnitHeadDao)Application.getInstance().getModule(UnitHeadModule.class).getDao();
			return dao.isUnitApproverByUnitId(userId,unitId);	    
		}catch(Exception e){
			Log.getLog(UnitHeadModule.class).error(e.getMessage(),e);
		}
		return result;
	}

	public void prepareModifiedAssignment(String requestId){
		try{
			EngineeringModule eModule=(EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			EngineeringRequest eReq=eModule.getRequestWithService(requestId);
			EngineeringDao dao=(EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
			
			Collection services=eReq.getServices();
			
			Date currentDate=new Date();
			String currentUser=Application.getInstance().getCurrentUser().getUsername();
			
			//delete all request booking
			eModule.deleteBooking(requestId);
									
			for(Iterator<Service> itr=services.iterator();itr.hasNext();){
				Service service=(Service)itr.next();
				String serviceId=service.getServiceId();
				
				Assignment assignment=new Assignment();					
				assignment.setRequestId(requestId);
				assignment.setServiceType(serviceId);
				assignment.setStatus(EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_NEW);
				assignment.setCreatedBy(currentUser);
				assignment.setModifiedBy(currentUser);
				assignment.setCreatedDate(currentDate);
				assignment.setModifiedDate(currentDate);
				
				if(ServiceDetailsForm.SERVICE_SCPMCP.equals(serviceId)){
					Collection col=eModule.getScpService(requestId, serviceId);
					//assignment.setGroupId(UuidGenerator.getInstance().getUuid());
					for(Iterator<ScpService> serItr=col.iterator();serItr.hasNext();){
						ScpService scp=(ScpService)serItr.next();
						assignment.setServiceId(scp.getId());
						assignment.setRateCardId(scp.facilityId);
						assignment.setRequiredFrom(scp.getRequiredFrom());
						assignment.setRequiredTo(scp.getRequiredTo());
						assignment.setFromTime(scp.getDepartureTime());
						assignment.setToTime(scp.getRecordingTo());
						assignment.setBlockBooking(scp.getBlockBooking());
						
						if(null!=scp.getSubmitted() && scp.getSubmitted().equals("0")){
							assignRateCardFacility(assignment,scp.facilityId,1);							
						}
						dao.updateServiceStatus(ServiceDetailsForm.SERVICE_SCPMCP, scp.getId());
					}
				}else if(ServiceDetailsForm.SERVICE_POSTPRODUCTION.equals(serviceId)){
					Collection col=eModule.getPostProductionService(requestId, serviceId);
					//assignment.setGroupId(UuidGenerator.getInstance().getUuid());
					for(Iterator<PostProductionService> serItr=col.iterator();serItr.hasNext();){
						PostProductionService post=(PostProductionService)serItr.next();
						assignment.setServiceId(post.getId());
						assignment.setRateCardId(post.facilityId);
						assignment.setRequiredFrom(post.getRequiredDate());
						assignment.setRequiredTo(post.getRequiredDateTo());
						assignment.setFromTime(post.getFromTime());
						assignment.setToTime(post.getToTime());
						assignment.setBlockBooking(post.getBlockBooking());
						
						if(null!=post.getSubmitted() && post.getSubmitted().equals("0")){
							assignRateCardFacility(assignment,post.facilityId,1);							
						}
							
						dao.updateServiceStatus(ServiceDetailsForm.SERVICE_POSTPRODUCTION, post.getId());
					}
				}else if(ServiceDetailsForm.SERVICE_VTR.equals(serviceId)){
					Collection col=eModule.getVtrService(requestId, serviceId);
					//assignment.setGroupId(UuidGenerator.getInstance().getUuid());
					for(Iterator<VtrService> serItr=col.iterator();serItr.hasNext();){
						VtrService vtr=(VtrService)serItr.next();
						assignment.setServiceId(vtr.getId());
						assignment.setRateCardId(vtr.facilityId);
						assignment.setRequiredFrom(vtr.getRequiredDate());
						assignment.setRequiredTo(vtr.getRequiredDateTo());
						assignment.setFromTime(vtr.getRequiredFrom());
						assignment.setToTime(vtr.getRequiredTo());
						assignment.setBlockBooking(vtr.getBlockBooking());
						
						if(null!=vtr.getSubmitted() && vtr.getSubmitted().equals("0")){
							assignRateCardFacility(assignment,vtr.facilityId,1);
							
						}
							
						dao.updateServiceStatus(ServiceDetailsForm.SERVICE_VTR, vtr.getId());
					}
				}else if(ServiceDetailsForm.SERVICE_MANPOWER.equals(serviceId)){
					Collection col=eModule.getManpowerService(requestId, serviceId);
					//assignment.setGroupId(UuidGenerator.getInstance().getUuid());
					for(Iterator<ManpowerService> serItr=col.iterator();serItr.hasNext();){
						ManpowerService man=(ManpowerService)serItr.next();
						assignment.setServiceId(man.getId());
						assignment.setRateCardId(man.getCompetencyId());
						assignment.setRequiredFrom(man.getRequiredFrom());
						assignment.setRequiredTo(man.getRequiredTo());
						assignment.setFromTime(man.getFromTime());
						assignment.setToTime(man.getToTime());
						assignment.setBlockBooking(man.getBlockBooking());
						
						if(null!=man.getSubmitted() && man.getSubmitted().equals("0")){
							assignRateCardFacility(assignment,man.getCompetencyId(),man.getQuantity());
							
						}
							
						dao.updateServiceStatus(ServiceDetailsForm.SERVICE_MANPOWER, man.getId());
					}
				}else if(ServiceDetailsForm.SERVICE_STUDIO.equals(serviceId)){
					Collection col=eModule.getStudioService(requestId, serviceId);
					//assignment.setGroupId(UuidGenerator.getInstance().getUuid());
					for(Iterator<StudioService> serItr=col.iterator();serItr.hasNext();){
						StudioService studio=(StudioService)serItr.next();
						assignment.setServiceId(studio.getId());
						assignment.setRateCardId(studio.getFacilityId());
						assignment.setRequiredFrom(studio.getBookingDate());
						assignment.setRequiredTo(studio.getBookingDateTo());
						assignment.setFromTime(studio.getRequiredFrom());
						assignment.setToTime(studio.getRequiredTo());
						assignment.setBlockBooking(studio.getBlockBooking());
						if(null!=studio.getSubmitted() && studio.getSubmitted().equals("0")){
							assignRateCardFacility(assignment,studio.getFacilityId(),1);
							
						}	
							
						dao.updateServiceStatus(ServiceDetailsForm.SERVICE_STUDIO, studio.getId());
					}
				}else if(ServiceDetailsForm.SERVICE_OTHER.equals(serviceId)){
					Collection col=eModule.getOtherService(requestId, serviceId);
					//assignment.setGroupId(UuidGenerator.getInstance().getUuid());
					for(Iterator<OtherService> serItr=col.iterator();serItr.hasNext();){
						OtherService other=(OtherService)serItr.next();
						assignment.setServiceId(other.getId());
						assignment.setRateCardId(other.getFacilityId());
						assignment.setRequiredFrom(other.getRequiredFrom());
						assignment.setRequiredTo(other.getRequiredTo());
						assignment.setFromTime(other.getFromTime());
						assignment.setToTime(other.getToTime());
						assignment.setBlockBooking(other.getBlockBooking());
						if(null!=other.getSubmitted() && other.getSubmitted().equals("0")){
							assignRateCardFacility(assignment,other.getFacilityId(),other.getQuantity());
							
						}
							
						dao.updateServiceStatus(ServiceDetailsForm.SERVICE_OTHER, other.getId());
					}
				}else if(ServiceDetailsForm.SERVICE_TVRO.equals(serviceId)){
					Collection col=eModule.getTvroService(requestId, serviceId);
					//assignment.setGroupId(UuidGenerator.getInstance().getUuid());
					for(Iterator<TvroService> serItr=col.iterator();serItr.hasNext();){
						TvroService tvro = (TvroService) serItr.next();
						assignment.setServiceId(tvro.getId());
						assignment.setRateCardId(tvro.getFacilityId());
						assignment.setRequiredFrom(tvro.getRequiredDate());
						assignment.setRequiredTo(tvro.getRequiredDateTo());
						assignment.setFromTime(tvro.getFromTime());
						assignment.setToTime(tvro.getToTime());
						assignment.setBlockBooking(tvro.getBlockBooking());
						if(null!=tvro.getSubmitted() && tvro.getSubmitted().equals("0")){
							assignRateCardFacility(assignment,tvro.getFacilityId(),1);
							
						}
							
						dao.updateServiceStatus(ServiceDetailsForm.SERVICE_TVRO, tvro.getId());
					}
				}
				
			}
			
			//regenerate booking 
			eModule.insertBooking(requestId);
			
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void prepareAssignment(String requestId){
		try{
			EngineeringModule eModule=(EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			EngineeringRequest eReq=eModule.getRequestWithService(requestId);
		
			Collection services=eReq.getServices();
			
			Date currentDate=new Date();
			String currentUser=Application.getInstance().getCurrentUser().getUsername();
			
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(requestId, "PREPARE_ASSIGNMENT", "[" + eReq.getTitle() + "]");
			
			for(Iterator<Service> itr=services.iterator();itr.hasNext();){
				Service service=(Service)itr.next();
				String serviceId=service.getServiceId();
				
				Assignment assignment=new Assignment();
				assignment.setRequestId(requestId);
				assignment.setServiceType(serviceId);
				assignment.setStatus(EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_NEW);
				assignment.setCreatedBy(currentUser);
				assignment.setModifiedBy(currentUser);
				assignment.setCreatedDate(currentDate);
				assignment.setModifiedDate(currentDate);
				
				if(ServiceDetailsForm.SERVICE_SCPMCP.equals(serviceId)){
					Collection col=eModule.getScpService(requestId, serviceId);
					//assignment.setGroupId(UuidGenerator.getInstance().getUuid());
					for(Iterator<ScpService> serItr=col.iterator();serItr.hasNext();){
						ScpService scp=(ScpService)serItr.next();
						assignment.setServiceId(scp.getId());
						assignment.setRateCardId(scp.facilityId);
						assignment.setRequiredFrom(scp.getRequiredFrom());
						assignment.setRequiredTo(scp.getRequiredTo());
						assignment.setFromTime(scp.getDepartureTime());
						assignment.setToTime(scp.getRecordingTo());
						assignment.setBlockBooking(scp.getBlockBooking());
						assignRateCardFacility(assignment,scp.facilityId,1);
					}
				}else if(ServiceDetailsForm.SERVICE_POSTPRODUCTION.equals(serviceId)){
					Collection col=eModule.getPostProductionService(requestId, serviceId);
					//assignment.setGroupId(UuidGenerator.getInstance().getUuid());
					for(Iterator<PostProductionService> serItr=col.iterator();serItr.hasNext();){
						PostProductionService post=(PostProductionService)serItr.next();
						assignment.setServiceId(post.getId());
						assignment.setRateCardId(post.facilityId);
						assignment.setRequiredFrom(post.getRequiredDate());
						assignment.setRequiredTo(post.getRequiredDateTo());
						assignment.setFromTime(post.getFromTime());
						assignment.setToTime(post.getToTime());
						assignment.setBlockBooking(post.getBlockBooking());
						assignRateCardFacility(assignment,post.facilityId,1);
					}
				}else if(ServiceDetailsForm.SERVICE_VTR.equals(serviceId)){
					Collection col=eModule.getVtrService(requestId, serviceId);
					//assignment.setGroupId(UuidGenerator.getInstance().getUuid());
					for(Iterator<VtrService> serItr=col.iterator();serItr.hasNext();){
						VtrService vtr=(VtrService)serItr.next();
						assignment.setServiceId(vtr.getId());
						assignment.setRateCardId(vtr.facilityId);
						assignment.setRequiredFrom(vtr.getRequiredDate());
						assignment.setRequiredTo(vtr.getRequiredDateTo());
						assignment.setFromTime(vtr.getRequiredFrom());
						assignment.setToTime(vtr.getRequiredTo());
						assignment.setBlockBooking(vtr.getBlockBooking());
						assignRateCardFacility(assignment,vtr.facilityId,1);
					}
				}else if(ServiceDetailsForm.SERVICE_MANPOWER.equals(serviceId)){
					Collection col=eModule.getManpowerService(requestId, serviceId);
					//assignment.setGroupId(UuidGenerator.getInstance().getUuid());
					for(Iterator<ManpowerService> serItr=col.iterator();serItr.hasNext();){
						ManpowerService man=(ManpowerService)serItr.next();
						assignment.setServiceId(man.getId());
						assignment.setRateCardId(man.getCompetencyId());
						assignment.setRequiredFrom(man.getRequiredFrom());
						assignment.setRequiredTo(man.getRequiredTo());
						assignment.setFromTime(man.getFromTime());
						assignment.setToTime(man.getToTime());
						assignment.setBlockBooking(man.getBlockBooking());
						assignRateCardFacility(assignment,man.getCompetencyId(),man.getQuantity());
						
						EngineeringDao dao=(EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
						dao.updateServiceStatus(ServiceDetailsForm.SERVICE_MANPOWER, man.getId());
					}
				}else if(ServiceDetailsForm.SERVICE_STUDIO.equals(serviceId)){
					Collection col=eModule.getStudioService(requestId, serviceId);
					//assignment.setGroupId(UuidGenerator.getInstance().getUuid());
					for(Iterator<StudioService> serItr=col.iterator();serItr.hasNext();){
						StudioService studio=(StudioService)serItr.next();
						assignment.setServiceId(studio.getId());
						assignment.setRateCardId(studio.getFacilityId());
						assignment.setRequiredFrom(studio.getBookingDate());
						assignment.setRequiredTo(studio.getBookingDateTo());
						assignment.setFromTime(studio.getRequiredFrom());
						assignment.setToTime(studio.getRequiredTo());
						assignment.setBlockBooking(studio.getBlockBooking());
						assignRateCardFacility(assignment,studio.getFacilityId(),1);
					}
				}else if(ServiceDetailsForm.SERVICE_OTHER.equals(serviceId)){
					Collection col=eModule.getOtherService(requestId, serviceId);
					//assignment.setGroupId(UuidGenerator.getInstance().getUuid());
					for(Iterator<OtherService> serItr=col.iterator();serItr.hasNext();){
						OtherService other=(OtherService)serItr.next();
						assignment.setServiceId(other.getId());
						assignment.setRateCardId(other.getFacilityId());
						assignment.setRequiredFrom(other.getRequiredFrom());
						assignment.setRequiredTo(other.getRequiredTo());
						assignment.setFromTime(other.getFromTime());
						assignment.setToTime(other.getToTime());
						assignment.setBlockBooking(other.getBlockBooking());
						assignRateCardFacility(assignment,other.getFacilityId(),other.getQuantity());
					}
				}else if(ServiceDetailsForm.SERVICE_TVRO.equals(serviceId)){
					Collection col=eModule.getTvroService(requestId, serviceId);
					//assignment.setGroupId(UuidGenerator.getInstance().getUuid());
					for(Iterator<TvroService> serItr=col.iterator();serItr.hasNext();){
						TvroService tvro = (TvroService) serItr.next();
						assignment.setServiceId(tvro.getId());
						assignment.setRateCardId(tvro.getFacilityId());
						assignment.setRequiredFrom(tvro.getRequiredDate());
						assignment.setRequiredTo(tvro.getRequiredDateTo());
						assignment.setFromTime(tvro.getFromTime());
						assignment.setToTime(tvro.getToTime());
						assignment.setBlockBooking(tvro.getBlockBooking());
						assignRateCardFacility(assignment,tvro.getFacilityId(),1);
					}
				}
			}
			
			// add status trail
			eModule.insertRequestStatus(requestId, EngineeringModule.STATUS_TRAIL_PREPARE_ASSIGNMENT , "", "");
		}catch(Exception e){
			Log.getLog(getClass()).error("requestId=" + requestId, e);
		}
	}
	
	public void assignRateCardFacility(Assignment assignment, String rateCardId,int qty){
		try{
			EngineeringDao eDao=(EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
			Collection updateAssignments = eDao.selectAssignmentByServiceId(assignment.getServiceId());
			
			Application application = Application.getInstance();
			SetupModule module = (SetupModule)application.getModule(SetupModule.class);
			Collection facilities = module.getRateCardEquipment(rateCardId, "");
			Collection manpower = module.getRateCardManpower(rateCardId, "");
			
			String facilityId = null;
			
			if(facilities != null && facilities.size() > 0){
				for(Iterator it = facilities.iterator(); it.hasNext(); ){
					RateCard rc= (RateCard) it.next();
					facilityId = rc.getCategoryId();
					
					for (Iterator iter = updateAssignments.iterator(); iter.hasNext();) {
						EngineeringRequest erq= (EngineeringRequest)iter.next();
						try {
							
							eDao.deleteAssignmentByAssignmentId(erq.getAssignmentId());
							eDao.deleteEquipmentAssignment(erq.getAssignmentId());							
							if(facilityId != null)
								eDao.deleteBookingByRequesIdtAndFacilityId(erq.getRequestId(), facilityId);
						} catch (DaoException e) {
							Log.getLog(getClass()).error(e.toString(), e);
						}
					}
					
				}
			}else{			
				for(Iterator it = manpower.iterator(); it.hasNext(); ){
					RateCard rc= (RateCard) it.next();
					facilityId = rc.getCompetencyId();
					
					for (Iterator iter = updateAssignments.iterator(); iter.hasNext();) {
						EngineeringRequest erq= (EngineeringRequest)iter.next();
						try {
							
							eDao.deleteAssignmentByAssignmentId(erq.getAssignmentId());							
							eDao.deleteManpowerAssignment(erq.getAssignmentId());
							if(facilityId != null)
								eDao.deleteBookingByRequesIdtAndFacilityId(erq.getRequestId(), facilityId);
						} catch (DaoException e) {
							Log.getLog(getClass()).error(e.toString(),e);
						}
					}
					
				}
			}
			
			long diff = dateDiff(assignment.getRequiredFrom(), assignment.getRequiredTo());
			Date start = assignment.getRequiredFrom();
			
			if ("1".equals(assignment.getBlockBooking())){
					
				for (int x = 0; x<= diff; x++){
					Calendar cal = Calendar.getInstance();
					cal.setTime(start);
					cal.add(Calendar.DATE, x);
					Date dateChecked = cal.getTime();
					
					assignment.setGroupId(new Sequence(getSequencePrefix(assignment.getServiceType()), Integer.parseInt(DateUtil.formatDate("yyyyMM", assignment.getRequiredFrom()))).generateGroupAssignmentCode());
					assignment.setRequiredFrom(dateChecked);
					assignment.setRequiredTo(dateChecked);
					
					assignFacilities(assignment,facilities,"F",qty);
					assignFacilities(assignment,manpower,"M",qty);
				}
			} else {
				assignment.setGroupId(new Sequence(getSequencePrefix(assignment.getServiceType()), Integer.parseInt(DateUtil.formatDate("yyyyMM", assignment.getRequiredFrom()))).generateGroupAssignmentCode());
				assignFacilities(assignment,facilities,"F",qty);
				assignFacilities(assignment,manpower,"M",qty);
			}
		}catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		
	} 
	
	private void assignFacilities(Assignment assignment,Collection facilities,String type,int qty){
		UnitHeadDao dao=(UnitHeadDao)getDao();
		long diff = dateDiff(assignment.getRequiredFrom(), assignment.getRequiredTo());
		Date start = assignment.getRequiredFrom();
		
		for(Iterator<RateCard> itr=facilities.iterator();itr.hasNext();){
			RateCard rc=(RateCard)itr.next();
			
			int quantity=0;
			if (type!=null && type.equals("F")){				
				assignment.setAssignmentId(UuidGenerator.getInstance().getUuid());
				assignment.setCode(new Sequence(getSequencePrefix(assignment.getServiceType()),Integer.parseInt(DateUtil.formatDate("yyyyMM", assignment.getRequiredFrom()))).genarteAssignmentCode());				
				assignment.setRateCardCategoryId(rc.getCategoryId());
				quantity=rc.getEquipmentQty()*qty;
			}else{
				assignment.setCompetencyId(rc.getCompetencyId());
				quantity=rc.getManpowerQty()*qty;
			}
			assignment.setAssignmentType(type);				
			
			for(int i=0; i<quantity;i++){
				try {
					if(EngineeringModule.ASSIGNMENT_TYPE_MANPOWER.equals(type)){
							assignment.setAssignmentId(UuidGenerator.getInstance().getUuid());
							assignment.setCode(new Sequence(getSequencePrefix(assignment.getServiceType()),Integer.parseInt(DateUtil.formatDate("yyyyMM", assignment.getRequiredFrom()))).genarteAssignmentCode());
							dao.insertManpowerAssignment(assignment);
					}else{
							assignment.setId(UuidGenerator.getInstance().getUuid());
							dao.insertFacilityAssignment(assignment);
					}
				} catch (DaoException e) {
					Log.getLog(getClass()).error(assignment.getRequestId() + " - " + e.getMessage(),e);
				}
			}
		}
		
	}
	
	private String getSequencePrefix(String serviceId){
		try {
			if(ServiceDetailsForm.SERVICE_SCPMCP.equals(serviceId)){
				return Sequence.TYPE_SCP;
			}else if(ServiceDetailsForm.SERVICE_POSTPRODUCTION.equals(serviceId)){
				return Sequence.TYPE_POSTPRODUCTION;
			}else if(ServiceDetailsForm.SERVICE_OTHER.equals(serviceId)){
				return Sequence.TYPE_OTHER;
			}else if(ServiceDetailsForm.SERVICE_STUDIO.equals(serviceId)){
				return Sequence.TYPE_STUDIO;
			}else if(ServiceDetailsForm.SERVICE_MANPOWER.equals(serviceId)){
				return Sequence.TYPE_MANPOWER;
			}else if(ServiceDetailsForm.SERVICE_TVRO.equals(serviceId)){
				return Sequence.TYPE_TVRO;
			}else if(ServiceDetailsForm.SERVICE_VTR.equals(serviceId)){
				return Sequence.TYPE_VTR;
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}
	public boolean isAssignmentPrepared(String requestId){
		boolean prepared=false;
		try {
			UnitHeadDao dao=(UnitHeadDao)getDao();
			prepared=dao.isAssignmentPrepared(requestId);
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return prepared;
	}
	
	public long dateDiff(Date start, Date end){
		
		long diff = Math.round((end.getTime() - start.getTime()) / (DateDiffUtil.MS_IN_A_DAY));
		
		return diff;
	}
	
	public Collection getUnitServicesApprover(String requestId, String userId){
		return getUnitServicesApprover(requestId, userId, false);
	}
	
	public Collection getUnitServicesApprover(String requestId, String userId, boolean isStoreAdmin){
		Collection result = new ArrayList();
		HashSet map = null;
		try{
			EngineeringDao dao=(EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
			UnitHeadDao uHDao=(UnitHeadDao)getDao();
			
			Collection col = dao.selectRequestUnit(requestId);
			if (col!=null){
				if (col.size() > 0){
					for (Iterator i = col.iterator(); i.hasNext();){
						HashMap hm = (HashMap) i.next();
						
						if (uHDao.isUnitApproverByUnitId(userId, (String)hm.get("unitId"))){
							
							if (isStoreAdmin) {
								if(ServiceDetailsForm.SERVICE_SCPMCP.equals((String)hm.get("serviceId"))
									|| ServiceDetailsForm.SERVICE_OTHER.equals((String)hm.get("serviceId"))){
									result.add((String)hm.get("serviceId"));
								}
							} else {
								result.add((String)hm.get("serviceId"));
							}
						}
					}
				}
			}
				    
			map = new HashSet(result);
		}catch(Exception e){
			Log.getLog(UnitHeadModule.class).error(e.getMessage(),e);
		}
		return map;
	}
	
	public String getRateCardCategoryName(String barcode){
		UnitHeadDao dao = (UnitHeadDao) getDao();
		String rateCardCategoryName = "";
		
		rateCardCategoryName = dao.getRateCardCategoryByBarcode(barcode);
		
		return rateCardCategoryName;
	}
	
		
	public Collection getManpowerDetails(String requestId, String userId) {
		UnitHeadDao dao = (UnitHeadDao) getDao();
		try {
			return dao.getManpowerDetails(requestId, userId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Cannot get manpower details");
			return null;
		}
	}
	
	public Collection getManpowerAssignment(String userId, String requestId, String sort, boolean desc, int start,int rows){
		ArrayList args = new ArrayList();
		Application app = Application.getInstance();
		UnitHeadDao dao = (UnitHeadDao) getDao();
		Collection col = dao.selectManpowerAssignmentDetails(userId, requestId, sort, desc, start, rows);
		if(col!=null && col.size()>0){
			for(Iterator iter = col.iterator(); iter.hasNext(); ){
				EngineeringRequest req = (EngineeringRequest) iter.next();
				if(req.getUserId()==null || req.getUserId().equals("")){
					req.setFullName(app.getMessage("fms.facility.msg.notAssignedYet"));
				}
				req.setStatus((String) EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_MAP.get(req.getStatus()));
				args.add(req);
			}
			return args;
		}
		return null; 
	}
	
	public int countManpowerAssignment(String userId, String requestId){
		UnitHeadDao dao = (UnitHeadDao) getDao();
		return dao.countManpowerAssignment(userId, requestId);
	}
	
	public EngineeringRequest getTodaysRequestDetail(String requestId){
		UnitHeadDao dao = (UnitHeadDao) getDao();
		return dao.selectTodaysRequestDetail(requestId);
	}
	
	public Collection getServiceIdsAndQttyFromTiedSstudio(String requestId, Collection competencyId){
		UnitHeadDao dao = (UnitHeadDao) getDao();
		
		return dao.getServiceIdsAndQttyFromTiedSstudio(requestId, competencyId);
	}
	
	public HashMap[] getAssignmentsForAutoAssignment(String requestId,  String serviceId, String unitHeadId){
		UnitHeadDao dao = (UnitHeadDao) getDao();
		
		return dao.getAssignmentsForAutoAssignment(requestId, serviceId, unitHeadId);
	}
	
	public String[] getUserIdFromWorkingProfile(String rateCardId, String unitHeadId, String altApprover, Date dateFrom , Date dateTo){
		UnitHeadDao dao = (UnitHeadDao) getDao();
		
		return dao.getUserIdFromWorkingProfile(rateCardId, unitHeadId, altApprover, dateFrom, dateTo);
	}
	
	public String[] getUserIdFromWorkingProfileForAutoTimeAssign(String rateCardId, String competencyId, Date dateFrom , Date dateTo){
		UnitHeadDao dao = (UnitHeadDao) getDao();
		
		return dao.getUserIdFromWorkingProfileForAutoTimeAssign(rateCardId, competencyId, dateFrom , dateTo);
	}
	
	public boolean searchAssignedEmployee(String requestId, String employeeId) {
		UnitHeadDao dao = (UnitHeadDao) getDao();
		
		return dao.searchAssignedEmployee(requestId, employeeId);
	}
	
	public void updateAssignmentManpower(String userId, String assignmentId){
		UnitHeadDao dao = (UnitHeadDao) getDao();
		dao.updateAssignmentManpower(userId, assignmentId);
	}
	public boolean searchEmpAvailability(String userId, Date dateFrom , Date dateTo, String groupId){
		UnitHeadDao dao = (UnitHeadDao) getDao();
		return dao.searchEmpAvailability(userId, dateFrom, dateTo, groupId);
	}
	
	public boolean isManpowerAvailableFOrToday(String userId, Date dateFrom, Date dateTo){
		UnitHeadDao dao = (UnitHeadDao) getDao();
		return dao.isManpowerAvailableFOrToday(userId, dateFrom, dateTo);
	}
	
	public String getMostNotBusyManpower(String rateCardId, String unitHeadId, Date dateFrom , Date dateTo){
		UnitHeadDao dao = (UnitHeadDao) getDao();
		return dao.getMostNotBusyManpower(rateCardId, unitHeadId, dateFrom, dateTo);
	}
	
	public String[] getUserIdFromLastGroupId(String requestId, String groupId){
		UnitHeadDao dao = (UnitHeadDao) getDao();
		
		return dao.getUserIdFromLastGroupId(requestId, groupId);
	}
	
	public HashMap getUnitHeadId(String competencyId){
		UnitHeadDao dao = (UnitHeadDao) getDao();
		
		return dao.getUnitHeadId(competencyId);
	}

	public Collection getAssignmentsByRequestId(String requestId, boolean notAssignedOnly)
	{
		UnitHeadDao dao = (UnitHeadDao) getDao();
		try
		{
			return dao.getAssignmentsByRequestId(requestId, notAssignedOnly);
		}
		catch(Exception e)
		{
			Log.getLog(getClass()).error("error @ UnitHeadModule.getAssignmentsByRequestId(2)" + e, e);
			return null;
		}
	}

	public String[] getUserIdFromWorkingProfile(String rateCardId, Date requiredFrom, Date requiredTo)
	{
		UnitHeadDao dao = (UnitHeadDao) getDao();
		try
		{
			Collection col = dao.getUserIdFromWorkingProfile(rateCardId, requiredFrom, requiredTo);
			String[] strArr = new String[col.size()];
			if(col != null && !col.isEmpty())
			{
				int i = 0;
				for(Iterator iter = col.iterator(); iter.hasNext();)
				{
					DefaultDataObject obj = (DefaultDataObject) iter.next();
					strArr[i++] = (String)obj.getProperty("userId");
				}
				return strArr;
			}
			return null;
		}
		catch(Exception e)
		{
			Log.getLog(getClass()).error("error @ UnitHeadModule.getUserIdFromWorkingProfile(3)" + e, e);
			return null;
		}
	}
	
	public int autoAssignment(String requestId)
	{
		boolean notAssignedOnly = true;
		Collection services = getStudioRateCardByRequestId(requestId);
		Collection assignments = getAssignmentsByRequestId(requestId, notAssignedOnly); // serviceTypeId = 4(manpower) only
		int foundAny = 0;
		if(services != null && !services.isEmpty())
		{
			for(Iterator itr2 = services.iterator(); itr2.hasNext();)
			{
				DefaultDataObject service = (DefaultDataObject) itr2.next();
				String rateCardId = service.getId();
				
				if(assignments != null && !assignments.isEmpty())
				{
					for(Iterator itr = assignments.iterator(); itr.hasNext();)
					{
						DefaultDataObject assignmentObj = (DefaultDataObject) itr.next();
						String assignmentId = (String) assignmentObj.getProperty("assignmentId");
						
						Date requiredFrom = (Date) assignmentObj.getProperty("requiredFrom");
						Date requiredTo = (Date) assignmentObj.getProperty("requiredTo");
						
						int status = autoAssignment(assignmentId, rateCardId, requiredFrom, requiredTo);
						if(status == 2)
						{
							foundAny++; 
						}
					}
					
					if(foundAny != assignments.size())
					{
						return 2;
					}
					else if (foundAny == 0)
					{
						return 0;
					}
				}
			}
		}
		return 1;
	}

	private int autoAssignment(String assignmentId, String rateCardId, Date requiredFrom, Date requiredTo)
	{
		String assignEmpId = null;
		String[] manpowerIds = getUserIdFromWorkingProfile(rateCardId, requiredFrom, requiredTo);
		int k = 0;
		boolean stop = false;
		if(manpowerIds!=null && manpowerIds.length>0)
		{
			while (!stop && k < manpowerIds.length)
			{
				/*-- Search for manpower to determine availability --*/
				if(searchEmpAvailability(manpowerIds[k], requiredFrom, requiredTo, null))
				{
					/*-- If all manpower assigned on the day, choose manpower which is less busy--*/
					if(k == manpowerIds.length-1)
					{
						//assignEmpId = getMostNotBusyManpower(rateCardId, requiredFrom, requiredTo); //TODO : scrutinize the query again if used in future
						assignEmpId = ""; //no manpower will be assigned
						stop = true;
					}
					else
					{
						k++;
					}
				}
				else
				{
					assignEmpId = manpowerIds[k];
					stop = true;
				}
			}
			
			if(assignEmpId != null && !assignEmpId.equals(""))
			{
				/*-- Update fms_eng_assignment_manpower--*/
				updateAssignmentManpower(assignEmpId, assignmentId);
				return 2; // #2 manpower found
			}
			return 1; // #1 no manpower found
		}
		else
		{
			return 0; // #0 fail
		}
	}

	private Collection getStudioRateCardByRequestId(String requestId)
	{
		UnitHeadDao dao = (UnitHeadDao) getDao();
		try
		{
			return dao.getStudioRateCardByRequestId(requestId);
		}
		catch(Exception e)
		{
			Log.getLog(getClass()).error("error @ UnitHeadModule.getStudioRateCardByRequestId(1)" + e, e);
			return null;
		}
	}

	private String getMostNotBusyManpower(String rateCardId, Date requiredFrom, Date requiredTo)
	{
		UnitHeadDao dao = (UnitHeadDao) getDao();
		try
		{
			return dao.getMostNotBusyManpower(rateCardId, requiredFrom, requiredTo);
		}
		catch(Exception e)
		{
			Log.getLog(getClass()).error("error @ UnitHeadModule.getMostNotBusyManpower(3)" + e, e);
			return null;
		}
	}

	public HashMap getRequestIdsByDateRange(Date startDate, Date endDate)
	{
		UnitHeadDao dao = (UnitHeadDao) getDao();
		try
		{
			HashMap reqIdsMap = new HashMap();
			Collection col = dao.getRequestIdsByDateRange(startDate, endDate);
			if(col != null && !col.isEmpty())
			{
				for(Iterator itr = col.iterator(); itr.hasNext();)
				{
					DefaultDataObject obj = (DefaultDataObject) itr.next();
					String reqId = obj.getId();
					
					if(reqId != null && !reqId.equals(""))
					{
						reqIdsMap.put(reqId, reqId);
					}
				}
				return reqIdsMap;
			}
			return new HashMap();
		}
		catch(Exception e)
		{
			Log.getLog(getClass()).error("error @ UnitHeadModule.getRequestIdsByDateRange(2)" + e, e);
			return null;
		}
	}

	public DefaultDataObject getAutoAssignmentSchedSetting()
	{
		UnitHeadDao dao = (UnitHeadDao) getDao();
		try
		{
			return dao.getAutoAssignmentSchedSetting();
		}
		catch(Exception e)
		{
			Log.getLog(getClass()).error("error @ UnitHeadModule.getAutoAssignmentSchedSetting()" + e, e);
			return null;
		}
	}
}
