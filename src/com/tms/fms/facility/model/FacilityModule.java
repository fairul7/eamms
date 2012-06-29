package com.tms.fms.facility.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultDataObject;
import kacang.model.DefaultModule;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.fms.engineering.model.Assignment;
import com.tms.fms.engineering.model.EngineeringDao;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.TransLogModule;
import com.tms.fms.engineering.model.UnitHeadDao;
import com.tms.fms.engineering.model.UnitHeadModule;
import com.tms.fms.util.PagingUtil;

public class FacilityModule extends DefaultModule {
	public static final String ITEM_STATUS_CHECKED_IN = "1";
	public static final String ITEM_STATUS_CHECKED_OUT = "C";
	public static final String ITEM_STATUS_PREPARE_CHECKOUT = "P";
	public static final String ITEM_STATUS_MISSING = "M";
	public static final String ITEM_STATUS_WRITE_OFF = "W";
	public static final String ITEM_STATUS_INACTIVE = "0";
	
	/*inactive*/
	public void insertFInactive(FInactiveObject i){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			dao.insertFInactive(i);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error insertFInactive(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void deleteFInactive(String id){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			dao.deleteFInactive(id);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error deleteFInactive(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public Collection selectFInactive(String search, String item_barcode, String reason_id, String sort, boolean desc, int start, int rows){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			return dao.selectFInactive(search, item_barcode, reason_id, sort, desc, start, rows);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectFInsurance(7)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public int selectFInactiveCount(String search, String item_barcode, String reason_id){
		int total = 0;
		try{
			FacilityDao dao = (FacilityDao) getDao();
			total = dao.selectFInactiveCount(search, item_barcode, reason_id);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectFInactiveCount(3)", e);
		}
		return total;
	}
	
	//check out
	public void insertCheckOut(FacilityObject c){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			dao.insertCheckOut(c);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error insertCheckOut(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void updateCheckOut(FacilityObject c){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			dao.updateCheckOut(c);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error updateCheckOut(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void updateCheckOutTakenBy(FacilityObject fo) {
		try {
			FacilityDao dao = (FacilityDao) getDao();
			dao.updateCheckOutTakenBy(fo);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error updateCheckOutTakenBy(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void undoCheckOut(String id) {
		TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
		
		User user = Application.getInstance().getCurrentUser();
		String username = null;
		String userId = null;
		if (user != null) {
			username = user.getUsername();
			userId = user.getId();
		}
		
		Log.getLog(getClass()).warn("Undo check out barcode=" + id + " username=" + username);
		
		try{
			FacilityDao dao = (FacilityDao) getDao();
			int undoInternal = dao.undoInternalCheckOut(id);
			if (undoInternal > 0) {
				// logging
				transLog.info("N/A", "UNDO_CHECK_OUT", "Internal: barcode=" + id);
			}
			
			updateEquipmentStatus2CheckedIn(id, userId);
			
			updateCheckOutEquipment(id);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error undoCheckOut(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void updateEquipmentStatus2CheckedIn(String barcode, String userId) {
		FacilityObject item = getItem(barcode);
		item.setStatus(ITEM_STATUS_CHECKED_IN);
		item.setUpdatedby(userId);
		item.setUpdatedby_date(new Date());
		updateItem(item);
	}
	
	public boolean hasInternalCheckOut(String barcode) {
		FacilityDao dao = (FacilityDao) getDao();
		try {
			return dao.hasInternalCheckOut(barcode);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return false;
	}
	
	public void updateCheckOutEquipment(String barcode) {
		EngineeringDao dao = (EngineeringDao) Application.getInstance().getModule(EngineeringModule.class).getDao();
		TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
		try {
			// get equipment checked out
			Collection col = dao.selectEquipmentByBc(barcode);
			
			for (Iterator i = col.iterator(); i.hasNext();){
				EngineeringRequest er = (EngineeringRequest) i.next();
				String id = er.getAssignmentEquipmentId();
				String assignmentId = er.getAssignmentId();
				String groupId = er.getGroupId();
				
				boolean extraCheckout = ("-".equals(assignmentId));
				if (extraCheckout) {
					// get requestId by groupId
					String requestId = dao.selectRequestIdByGroupId(groupId);
					
					// logging
					transLog.info(requestId, "UNDO_CHECK_OUT", "Extra: barcode=" + barcode + " groupId=" + groupId + " id=" + id);
					
					dao.deleteEquipmentAssignmentById(id);
				} else {
					EngineeringRequest eRequest = new EngineeringRequest();
					eRequest.setStatus(EngineeringModule.ASSIGNMENT_FACILITY_STATUS_NEW);
					eRequest.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
					Date now=new Date();
					eRequest.setModifiedOn(now);
					eRequest.setCheckedOutDate(now);
					eRequest.setBarcode(barcode);
					eRequest.setId(id);
					
					// get requestId by assignmentId
					String requestId = dao.selectRequestId(assignmentId);
					
					// logging
					transLog.info(requestId, "UNDO_CHECK_OUT", "Assignment: barcode=" + barcode + " assignmentId=" + assignmentId + " id=" + id);
					
					dao.undoCheckOutEquipment(eRequest);
				}

			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
	}
	
	public boolean hasAssignmentCheckOut(String barcode) {
		EngineeringDao dao = (EngineeringDao) Application.getInstance().getModule(EngineeringModule.class).getDao();
		try {
			return dao.hasAssignmentCheckOut(barcode);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return false;
	}
	
	//Write Off
	public void insertWriteoff(ClosedItemObject c){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			dao.insertWriteoff(c);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error insertWriteoff(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public ClosedItemObject getWriteoff(String id){
		ClosedItemObject c = new ClosedItemObject();
		try{
			FacilityDao dao = (FacilityDao) getDao();
			Collection rows = dao.selectWriteoff(id);
			if (rows.size() > 0) {c = (ClosedItemObject)rows.iterator().next();}
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getWriteoff(1)", e);
		}
		return c;
	}
	
	//Missing
	public void insertMissing(ClosedItemObject c){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			dao.insertMissing(c);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error insertMissing(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public ClosedItemObject getMissing(String id){
		ClosedItemObject c = new ClosedItemObject();
		try{
			FacilityDao dao = (FacilityDao) getDao();
			Collection rows = dao.selectMissing(id);
			if (rows.size() > 0) {c = (ClosedItemObject)rows.iterator().next();}
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getMissing(1)", e);
		}
		return c;
	}
	
	//item
	public void insertItem(FacilityObject c){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			dao.insertItem(c);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error insertItem(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void updateItem(FacilityObject c){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			dao.updateItem(c);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error updateItem(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public FacilityObject getItem(String id){
		FacilityObject c = new FacilityObject();
		try{
			FacilityDao dao = (FacilityDao) getDao();
			Collection rows = dao.selectItem(id);
			if (rows.size() > 0) {c = (FacilityObject)rows.iterator().next();}
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getItem(1)", e);
		}
		return c;
	}
	
	public Collection selectItem(String search, String facility_id, String location_id, String status, String sort, boolean desc, int start, int rows){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			return dao.selectItem(search, facility_id, location_id, status, sort, desc, start, rows);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectItem(8)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public int selectItemCount(String search, String facility_id, String location_id, String status){
		int total = 0;
		try{
			FacilityDao dao = (FacilityDao) getDao();
			return dao.selectItemCount(search, facility_id, location_id, status);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectItemCount(3)", e);
		}
		return total;
	}
	
	//facility
	public void insertFacility(FacilityObject c){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			dao.insertFacility(c);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error insertFacility(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void updateFacility(FacilityObject c){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			dao.updateFacility(c);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error updateFacility(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void deleteFacility(String id){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			dao.deleteFacility(id);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error deleteFacility(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public FacilityObject getFacility(String id){
		FacilityObject c = new FacilityObject();
		try{
			FacilityDao dao = (FacilityDao) getDao();
			Collection rows = dao.selectFacility(id);
			if (rows.size() > 0) {c = (FacilityObject)rows.iterator().next();}
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getFacility(1)", e);
		}
		return c;
	}
	
	public Collection selectFacility(String search, String category_id, String channel_id, String status, String sort, boolean desc, int start, int rows){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			return dao.selectFacility(search, category_id, channel_id, status, sort, desc, start, rows);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectFacility(8)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public Collection selectFacilityAvailability(String ids[]){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			return dao.selectFacilityAvailability(ids);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectFacilityAvailability(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public int selectFacilityCount(String search, String category_id, String channel_id, String status){
		int total = 0;
		try{
			FacilityDao dao = (FacilityDao) getDao();
			return dao.selectFacilityCount(search, category_id, channel_id, status);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectFacilityCount(4)", e);
		}
		return total;
	}
	
	public void insertRelatedItem(String id, String[] ids){
		deleteRelatedItem(id);
		try{
			FacilityDao dao = (FacilityDao) getDao();
			for (int i=0; i<ids.length; i++) {
				FacilityObject o = new FacilityObject();
				o.setId(UuidGenerator.getInstance().getUuid());
				o.setFacility_id(id);
				o.setRelated_id(ids[i]);
				dao.insertRelatedItem(o);
			}
		}catch(Exception e){
			Log.getLog(getClass()).error("Error insertRelatedItem(2)", e);
		}
	}
	
	public void deleteRelatedItem(String id){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			dao.deleteRelatedItem(id);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error deleteRelatedItem(1)", e);
		}
	}
	
	public Collection selectRelatedItem(String id){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			return dao.selectRelatedItem(id);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectRelatedItem(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public FacilityObject getLatestItemCheckedOut(){
		FacilityObject fo = new FacilityObject();
		try{
			FacilityDao dao = (FacilityDao) getDao();
			String createdBy = Application.getInstance().getCurrentUser().getId();
			return dao.selectLatestGroupId(createdBy);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getLatestItemCheckedOut()", e);
		}
		return fo;
	}
	
	public Collection getFacilityByGroupId(String groupId){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			return dao.selectFacilityByGroupId(groupId);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getFacilityByGroupId()", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	//category
	public void insertCategory(CategoryObject c){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			dao.insertCategory(c);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error insertCategory(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void updateCategory(CategoryObject c){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			dao.updateCategory(c);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error updateCategory(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void deleteCategory(String id){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			dao.deleteCategory(id);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error deleteCategory(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public CategoryObject getCategory(String id){
		CategoryObject c = new CategoryObject();
		try{
			FacilityDao dao = (FacilityDao) getDao();
			Collection rows = dao.selectCategory(id);
			if (rows.size() > 0) {c = (CategoryObject)rows.iterator().next();}
			
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getCategory(1)", e);
		}
		return c;
	}
	
	public Collection selectCategoryWithParent(String search, String department_id, String unit_id, String parent_id, boolean isParent, String status, String sort, boolean desc, int start, int rows){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			return dao.selectCategoryWithParent(search, department_id, unit_id, parent_id, isParent, status, sort, desc, start, rows);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectCategoryWithParent(10)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public Collection selectCategory(String search, String department_id, String unit_id, String parent_id, boolean isParent, String status, String sort, boolean desc, int start, int rows){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			return dao.selectCategory(search, department_id, unit_id, parent_id, isParent, status, sort, desc, start, rows);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectCategory(10)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public int selectCategoryCount(String search, String department_id, String unit_id, String parent_id, boolean isParent, String status){
		int total = 0;
		try{
			FacilityDao dao = (FacilityDao) getDao();
			return dao.selectCategoryCount(search, department_id, unit_id, parent_id, isParent, status);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectCategoryCount(6)", e);
		}
		return total;
	}
	
	public FacilityObject selectFacility(String itemName,String barcode){
		
		FacilityObject fO = null;
		try{
		FacilityDao dao = (FacilityDao) getDao();
		fO =  dao.selectFacility(itemName, barcode);
		
		}catch(DaoException e){
			Log.getLog(getClass()).error(e);			
		}
		
		return fO;
		}
	
	
	public Collection selectCategory(String id){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			return dao.getCategory(id);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getCategory", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public Collection selectBarcodeSerialNo(String barcode){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			return dao.getBarcodeSerialNo(barcode);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectBarcodeSerialNo", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public int countTodaysAssignments(String search, String department, Date fromDate, Date toDate, String sort, boolean desc, boolean isToday) throws DaoException{
		int result = 0;
		Collection col = new ArrayList();
		col = getTodaysAssignments(search, department, fromDate, toDate, sort, desc, isToday, 0, -1);
		if (col != null && col.size()>0) {
			result = col.size();
		}
		
		return result;
	}
	
	public Collection getTodaysAssignments(String search, String department, Date fromDate, Date toDate, String sort, boolean desc, boolean isToday, int start, int rows){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
			UnitHeadModule uHmod = (UnitHeadModule)Application.getInstance().getModule(UnitHeadModule.class);
			String userId = Application.getInstance().getCurrentUser().getId();
			Collection result = new ArrayList();
			Collection col=dao.selectTodaysAssignments(search, department, fromDate, toDate, sort, desc, isToday, 0, -1);
			
			for(Iterator<HashMap> itr=col.iterator();itr.hasNext();){
				HashMap map=(HashMap)itr.next();
				String service = (String)map.get("serviceType");
				boolean addMap = false;
				Collection units = uHmod.getUnitServicesApprover((String)map.get("requestId"), userId, true);
	
				if (units.contains(service)){		
				
					Collection facilities = new ArrayList();
					Collection requestItems = new ArrayList();
					String groupId = (String)map.get("groupId");
					
					Collection rcs = dao.getRateCardByGroupId(groupId);
					if(rcs != null && rcs.size()>0){
						
						for (Iterator it = rcs.iterator(); it.hasNext();){
							HashMap mrcs = (HashMap) it.next();
							
							if (mrcs.get("rateCardId") != null && mrcs.get("rateCardCategoryId") != null){
								requestItems = module.getRateCardEquipment((String)mrcs.get("rateCardId"), (String)mrcs.get("rateCardCategoryId"));
								facilities.addAll(requestItems);
								
								if (isFacilityApprover((String)mrcs.get("rateCardId"), (String)map.get("requestId"), userId)){
									addMap = true;
								}
							}
						}
					}		
					
					String items="";
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
					String requiredTime=map.get("fromTime")+" - "+map.get("toTime");
					map.put("items", items);
					map.put("requiredTime", requiredTime);
					map.put("status", getFinalStatusByGroupId((String)map.get("groupId")));
					
					if (addMap) {
						result.add(map);
					}
				}
			}
			//return col;
			return PagingUtil.getPagedCollection(result, start, rows);
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return new ArrayList();
	}
	
	public boolean isFacilityApprover(String rateCardId, String requestId, String userId){
		boolean facilityApprover = false;
		try{
			EngineeringDao dao=(EngineeringDao)Application.getInstance().getModule(EngineeringModule.class).getDao();
			UnitHeadDao uHDao=(UnitHeadDao)Application.getInstance().getModule(UnitHeadModule.class).getDao();
			
			Collection col = dao.selectRequestUnitByRc(rateCardId, requestId);
			
			if (col!=null){
				if (col.size() > 0){
					for(Iterator i = col.iterator(); i.hasNext();){
						HashMap map = (HashMap) i.next();
						if (uHDao.isUnitApproverByUnitId(userId, (String)map.get("unitId"))) {
							facilityApprover = true;
						}
					}
				}
			}
				    
		}catch(Exception e){
			Log.getLog(UnitHeadModule.class).error(e.getMessage(),e);
		}
		return facilityApprover;
	}
	
	public String getFinalStatus(String groupId){
		String status="";
		try{
			UnitHeadDao uDao=(UnitHeadDao)Application.getInstance().getModule(UnitHeadModule.class).getDao();
			//Collection col=uDao.getChildFacilityAssignments(assignmentId);
			
			Collection col = uDao.getChildFacilityAssignmentsByGroupIdMap(groupId);
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
	
	public String getFinalStatusByGroupId(String groupId){
		String status="";
		try{
			UnitHeadDao uDao=(UnitHeadDao)Application.getInstance().getModule(UnitHeadModule.class).getDao();
			//Collection col=uDao.getChildFacilityAssignments(assignmentId);
			
			Collection col = uDao.getChildFacilityAssignmentsByGroupIdMap(groupId);
			int in=0;
			int outQ=0;
			int newStat=0;
			int unfulfilled = 0;
			
			for(Iterator<Assignment> itr=col.iterator();itr.hasNext();){
				Assignment assignment=(Assignment)itr.next();
				
				if(EngineeringModule.ASSIGNMENT_FACILITY_STATUS_CHECKOUT.equals(assignment.getStatus())){
					outQ++;
					//out=true;
					//break;
				}else{
					if(EngineeringModule.ASSIGNMENT_FACILITY_STATUS_CHECKIN.equals(assignment.getStatus())){
						in++;
					} else if (EngineeringModule.ASSIGNMENT_FACILITY_STATUS_UNFULFILLED.equals(assignment.getStatus())){
						unfulfilled++;
					} else{
						newStat++;
					}
				}
			}
			
			if(outQ >= col.size()){
				status=EngineeringModule.ASSIGNMENT_FACILITY_STATUS_CHECKOUT;
			} else if (in >= col.size()){
				status=EngineeringModule.ASSIGNMENT_FACILITY_STATUS_CHECKIN;
			} else if (newStat >= col.size()){
				status=EngineeringModule.ASSIGNMENT_FACILITY_STATUS_NEW;
			} else if (unfulfilled >= col.size()){
				status=EngineeringModule.ASSIGNMENT_FACILITY_STATUS_UNFULFILLED;
			} else {
				if (newStat > 0){
					status = EngineeringModule.ASSIGNMENT_FACILITY_STATUS_NEW;
				} else if (outQ > 0) {
					status = EngineeringModule.ASSIGNMENT_FACILITY_STATUS_CHECKOUT;
				}
			}
		}catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return status;
	}
	
	public int countTodaysAssignmentsHOU(String search, String department, Date fromDate, Date toDate, String sort, boolean desc, boolean isToday) throws DaoException{
		int result = 0;
		Collection col = new ArrayList();
		col = getTodaysAssignmentsHOU(search, department, fromDate, toDate, sort, desc, isToday, 0, -1);
		if (col != null && col.size()>0) {
			result = col.size();
		}
		
		return result;
	}
	
	public Collection getTodaysAssignmentsHOU(String search, String department, Date fromDate, Date toDate, String sort, boolean desc, boolean isToday, int start, int rows){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			UnitHeadModule uHmod = (UnitHeadModule)Application.getInstance().getModule(UnitHeadModule.class);
			SetupModule module = (SetupModule)Application.getInstance().getModule(SetupModule.class);
			String userId = Application.getInstance().getCurrentUser().getId();
			Collection result = new ArrayList();
			Collection col=dao.selectTodaysAssignmentsHOU(search, department, fromDate, toDate, sort, desc, isToday, 0, -1);
			
			
			for(Iterator<HashMap> itr=col.iterator();itr.hasNext();){
				HashMap map=(HashMap)itr.next();
				boolean addMap = false;
				String service = (String)map.get("serviceType");
				String groupId = (String)map.get("groupId");
				String unitId = (String) map.get("unitId");
	
				if (uHmod.isUnitApproverByUnitId(userId, unitId)){					
					Collection facilities = new ArrayList();
					Collection requestItems = new ArrayList();
					Collection rcs = dao.getRateCardByGroupId(groupId);
					if(rcs != null && rcs.size()>0){
						
						for (Iterator it = rcs.iterator(); it.hasNext();){
							HashMap mrcs = (HashMap) it.next();
							
							if (mrcs.get("rateCardId") != null && mrcs.get("rateCardCategoryId") != null){
								requestItems = module.getRateCardEquipment((String)mrcs.get("rateCardId"), (String)mrcs.get("rateCardCategoryId"));
								facilities.addAll(requestItems);
							}
						}
					}		
					String items="";
					try{
						//Collection facilities = module.getRateCardEquipment(rateCardId, rateCardCategoryId);					
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
					String requiredTime=map.get("fromTime")+" - "+map.get("toTime");
					map.put("items", items);
					map.put("requiredTime", requiredTime);
					map.put("status", getFinalStatusByGroupId((String)map.get("groupId")));
					
					result.add(map);
				}
			}
			//return col;
			return PagingUtil.getPagedCollection(result, start, rows);
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return new ArrayList();
	}
	
	public Collection getRequestListing(String search, String department, Date fromDate, Date toDate, String sort, boolean desc, boolean isToday, int start,int rows){
		FacilityDao dao = (FacilityDao) getDao();
		try {
			return dao.getRequestListing(search, department, fromDate, toDate, sort, desc, isToday, start, rows);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Cannot get request listing from fms_eng_request",e);
			return null;
		}
	}
	
	public int countRequestListing(String search, String department, Date fromDate, Date toDate, boolean isToday){
		FacilityDao dao = (FacilityDao) getDao();
		try {
			return dao.countRequestListing(search, department, fromDate, toDate, isToday);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return 0;
		}
		
	}
	
		
	public Collection getRateCardIdByRequestId(String requestId){
		FacilityDao dao = (FacilityDao) getDao();
		try {
			return dao.getRateCardIdByRequestId(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Unable to get item lists from requestId given",e);
			return null;
		}
	}
	
	public FacilityObject itemNameByBarcode(String barcode){
		try{
			FacilityDao dao = (FacilityDao) getDao();
			return dao.getItemNameByBarcode(barcode);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getItemName from barcode: "+barcode, e);
			return null;
		}
		
	}
		
	public Collection getServiceType(String requestId){
		FacilityDao dao = (FacilityDao) getDao();
		try {
			return dao.getServiceType(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Unable to get service type from fms_eng_assignment",e);
			return null;
		}
	}
	
	public void removePrepareItem(String id, String columnName){
		FacilityDao dao = (FacilityDao) getDao();
		try {
			dao.removePrepareItem(id, columnName);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Unable to remove item from prepare check out",e);
		}
	}
	
	public Collection getRequestItems(String requestId, boolean today){
		FacilityDao dao = (FacilityDao) getDao();
		try {
			return dao.selectRequestItems(requestId, today);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Unable to get request items list from requestId given",e);
			return null;
		}
	}
	
	public Collection selectRequestItemsGroupAssignment(String requestId, String groupId, Date reqFrom, Date reqTo,boolean today){
		FacilityDao dao = (FacilityDao) getDao();
		try {
			return dao.selectRequestItemsGroupAssignment(requestId, groupId, reqFrom, reqTo,today);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Unable to get request items list from requestId given",e);
			return null;
		}
	}
	
	public Collection getRequestItemsForCheckedOut(String requestId, boolean today){
		FacilityDao dao = (FacilityDao) getDao();
		try {
			return dao.selectRequestItemsForCheckOut(requestId, today);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Unable to get request items list from requestId given",e);
			return null;
		}
	}
	
	public Collection getExtraRequestItems(String requestId, boolean today){
		FacilityDao dao = (FacilityDao) getDao();
		try {
			return dao.selectExtraRequestItems(requestId, today);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Unable to get request items list from requestId given",e);
			return null;
		}
	}
	public Collection selectExtraRequestItemsByGroup(String requestId, String groupId, Date reqFrom, Date reqTo, boolean today){
		FacilityDao dao = (FacilityDao) getDao();
		try {
			return dao.selectExtraRequestItemsByGroup(requestId, groupId, reqFrom, reqTo,today);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Unable to get request items list from requestId given",e);
			return null;
		}
	}
	
	public Collection getPreparedItems(String requestId){
		FacilityDao dao = (FacilityDao) getDao();
		try {
			return dao.getPreparedItems(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Unable to get prepare items to check out",e);
			return null;
		}
	}
	
	public Assignment selectAssignmentCheckOutDetails(String requestId){
		FacilityDao dao = (FacilityDao) getDao();
		try {
			return dao.selectAssignmentCheckOutDetails(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error on selectAssignmentCheckOutDetails",e);
			return null;
		}
	}
	
	public Collection getRequestListingByTime(String search, String department, Date fromDate, Date toDate, String sort, boolean desc, boolean isToday, int start,int rows){
		FacilityDao dao = (FacilityDao) getDao();
		try {
			return dao.getRequestListingByTime(search, department, fromDate, toDate, sort, desc, isToday, start, rows);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Cannot get request listing from fms_eng_request",e);
			return null;
		}
	}
	
	public boolean isItemNotUtilized(String requestId){
		FacilityDao dao = (FacilityDao) getDao();
		
		return dao.isItemNotUtilized(requestId);
	}
	
	public void updateNotUtilizedItem(String requestId) throws DaoException{
		FacilityDao dao = (FacilityDao) getDao();
		dao.updateNotUtilizedItem(requestId);
	}
	
	public void updateReasonNotUtilizedItem(String requestId, String reason) throws DaoException{
		FacilityDao dao = (FacilityDao) getDao();
		dao.updateReasonNotUtilizedItem(requestId, reason);
	}

	public boolean isExistRequestId(String requestId) {
		FacilityDao dao = (FacilityDao) getDao();
		try {
			return dao.isExistRequestId(requestId.trim());
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return false;
		}
	}

	public Collection getItemNotCheckin(String assignmentId) {
		FacilityDao dao = (FacilityDao) getDao();
		try {
			return dao.getItemNotCheckin(assignmentId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return new ArrayList();
		}
	}

	public DefaultDataObject getAssignmentInfo(String requestId) {
		FacilityDao dao = (FacilityDao) getDao();
		try {
			return dao.getAssignmentInfo(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return new DefaultDataObject();
		}
	}
}
