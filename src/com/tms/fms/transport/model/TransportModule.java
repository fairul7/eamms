package com.tms.fms.transport.model;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.services.security.SecurityService;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.crm.sales.misc.DateUtil;
import com.tms.ekms.manpowertemp.model.ManpowerAssignmentObject;
import com.tms.ekms.manpowertemp.model.ManpowerLeaveObject;
import com.tms.ekms.manpowertemp.model.ManpowerObject;
import com.tms.fms.transport.model.Status;
import com.tms.fms.util.DateDiffUtil;
import com.tms.fms.abw.model.AbwTransferCostObject;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.engineering.model.AssignmentLog;
import com.tms.fms.engineering.model.AssignmentLogModule;
import com.tms.fms.engineering.model.TransLogModule;
import com.tms.fms.setup.model.SetupModule;

public class TransportModule extends DefaultModule {
	
	
			
	public static final String FORWARD_DUPLICATE_ITEM = "item duplicate";
	public static final String FORWARD_NULL_ID = "id null";
	public static final String FORWARD_NULL_VALUE = "value null";
	public static final String CATEGORY_SHEET = "CATEGORY";
	public static final String FACILITY_SHEET = "FACILITY";
	public static final String CHANNEL_SHEET = "CHANNEL";
	public static final String DEPARTMENT_SHEET = "DEPARTMENT";
	public static final String LOCATION_SHEET = "LOCATION";
	public static final String UNIT_SHEET = "UNIT";
	
	
	/*Rate card*/
	public RateCardObject getRateCard(String id, Date effcDate){
		RateCardObject o = new RateCardObject();
		try{
			TransportDao dao = (TransportDao) getDao();
			Collection rows = dao.selectRateCard(id, effcDate);
			if (rows.size() > 0) {o = (RateCardObject)rows.iterator().next();}
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getRateCard(2)", e);
		}
		return o;
	}
	
	public RateCardObject selectRateCardByNameDate(String name, Date effDate) throws DaoException {
		
		TransportDao dao = (TransportDao) getDao();
		return dao.selectRateCardByNameDate(name, effDate);
	}
	
	public boolean isRateCardExist(String name){
		try{
			TransportDao dao = (TransportDao) getDao();
			return (dao.selectRateCardCount(name)!=0);
		}catch(Exception e){
			return false;
		}
	}
	
	public void insertRateCard(RateCardObject o){
		try{
			TransportDao dao = (TransportDao) getDao();
			dao.insertRateCard(o);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error insertRateCard(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void updateRateCard(RateCardObject o){
		try{
			TransportDao dao = (TransportDao) getDao();
			dao.updateRateCard(o);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error updateRateCard(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	/*writeoff*/
	public void insertWriteoff(WriteoffObject w){
		try{
			TransportDao dao = (TransportDao) getDao();
			dao.insertWriteoff(w);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error insertWriteoff(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public WriteoffObject getWriteoff(String vehicle_num){
		WriteoffObject w = new WriteoffObject();
		try{
			TransportDao dao = (TransportDao) getDao();
			Collection rows = dao.selectWriteoff(vehicle_num);
			if (rows.size() > 0) {w = (WriteoffObject)rows.iterator().next();}
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getwriteoff(1)", e);
		}
		return w;
	}
	
	/*inactive*/
	public void insertInactive(InactiveObject i){
		try{
			TransportDao dao = (TransportDao) getDao();
			dao.insertInactive(i);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error insertInactive(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void deleteInactive(String id){
		try{
			TransportDao dao = (TransportDao) getDao();
			dao.deleteInactive(id);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error deleteInactive(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public Collection selectInactive(String search, String vehicle_num, int reason_id, String sort, boolean desc, int start, int rows){
		try{
			TransportDao dao = (TransportDao) getDao();
			return dao.selectInactive(search, vehicle_num, reason_id, sort, desc, start, rows);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectInsurance(7)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public int selectInactiveCount(String search, String vehicle_num, int reason_id){
		int total = 0;
		try{
			TransportDao dao = (TransportDao) getDao();
			total = dao.selectInactiveCount(search, vehicle_num, reason_id);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectInactiveCount(3)", e);
		}
		return total;
	}
	
	/*insurance*/
	public void insertInsurance(InsuranceObject i){
		try{
			TransportDao dao = (TransportDao) getDao();
			dao.insertInsurance(i);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error insertInsurance(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void updateInsurance(InsuranceObject i){
		try{
			TransportDao dao = (TransportDao) getDao();
			dao.updateInsurance(i);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error updateInsurance(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void deleteInsurance(String id){
		try{
			TransportDao dao = (TransportDao) getDao();
			dao.deleteInsurance(id);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error deleteInsurance(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public InsuranceObject getInsurance(String id){
		InsuranceObject i = new InsuranceObject();
		try{
			TransportDao dao = (TransportDao) getDao();
			Collection rows = dao.selectInsurance(id);
			if (rows.size() > 0) {i = (InsuranceObject)rows.iterator().next();}
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getInsurance(1)", e);
		}
		return i;
	}
	
	public InsuranceObject getLastestInsurance(String vehicle_num){
		InsuranceObject i = new InsuranceObject();
		try{
			TransportDao dao = (TransportDao) getDao();
			Collection rows = dao.selectInsurance("", vehicle_num, "rt_renew", true, 0, 1);
			if (rows.size() > 0) {i = (InsuranceObject)rows.iterator().next();}
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getLastestInsurance(1)", e);
		}
		return i;
	}
	
	public Collection selectInsurance(String search, String vehicle_num, String sort, boolean desc, int start, int rows){
		try{
			TransportDao dao = (TransportDao) getDao();
			return dao.selectInsurance(search, vehicle_num, sort, desc, start, rows);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectInsurance(6)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public int selectInsuranceCount(String search, String vehicle_num){
		int total = 0;
		try{
			TransportDao dao = (TransportDao) getDao();
			total = dao.selectInsuranceCount(search, vehicle_num);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectInsuranceCount(2)", e);
		}
		return total;
	}
	
	/*maintenance*/
	public void insertMaintenance(MaintenanceObject m){
		try{
			TransportDao dao = (TransportDao) getDao();
			dao.insertMaintenance(m);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error insertMaintenance(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void updateMaintenance(MaintenanceObject m){
		try{
			TransportDao dao = (TransportDao) getDao();
			dao.updateMaintenance(m);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error updateMaintenance(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void deleteMaintenance(String id){
		try{
			TransportDao dao = (TransportDao) getDao();
			dao.deleteMaintenance(id);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error deleteMaintenance(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public MaintenanceObject getMaintenance(String id){
		MaintenanceObject m = new MaintenanceObject();
		try{
			TransportDao dao = (TransportDao) getDao();
			Collection rows = dao.selectMaintenance(id);
			if (rows.size() > 0) {m = (MaintenanceObject)rows.iterator().next();}
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getMaintenance(1)", e);
		}
		return m;
	}
	
	public Collection selectMaintenance(String search, String vehicle_num, String sort, boolean desc, int start, int rows){
		try{
			TransportDao dao = (TransportDao) getDao();
			return dao.selectMaintenance(search, vehicle_num, sort, desc, start, rows);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectMaintenance(6)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public int selectMaintenanceCount(String search, String vehicle_num){
		int total = 0;
		try{
			TransportDao dao = (TransportDao) getDao();
			total = dao.selectMaintenanceCount(search, vehicle_num);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectMaintenanceCount(2)", e);
		}
		return total;
	}
	
	public int selectWorkshopUsedCount(String workshop_id){
		int total = 0;
		try{
			TransportDao dao = (TransportDao) getDao();
			total = dao.selectWorkshopUsedCount(workshop_id);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectWorkshopUsedCount(1)", e);
		}
		return total;
	}
	
	/*vehicle*/
	public void insertvehicle(VehicleObject v){
		try{
			TransportDao dao = (TransportDao) getDao();
			dao.insertVehicle(v);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error insertVehicle(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void updateVehicle(VehicleObject v){
		try{
			TransportDao dao = (TransportDao) getDao();
			dao.updateVehicle(v);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error updateVehicle(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void deleteVehicle(String vehicle_num){
		try{
			TransportDao dao = (TransportDao) getDao();
			dao.deleteVehicle(vehicle_num);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error deleteVehicle(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public VehicleObject getVehicle(String vehicle_num){
		VehicleObject v = new VehicleObject();
		try{
			TransportDao dao = (TransportDao) getDao();
			Collection rows = dao.selectVehicle(vehicle_num);
			if (rows.size() > 0) {v = (VehicleObject)rows.iterator().next();}
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getVehicle(1)", e);
		}
		return v;
	}
	
	public Collection selectVehicle(String search, String type, String channel, String category, String makeType, String bodyType, String fuelType, String status, String sort, boolean desc, int start, int rows){
		try{
			TransportDao dao = (TransportDao) getDao();
			return dao.selectVehicle(search, type, channel, category, makeType, bodyType, fuelType, status, sort, desc, start, rows);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectVehicle(12)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public int selectVehicleCount(String search, String type, String channel, String category, String makeType, String bodyType, String fuelType, String status){
		int total = 0;
		try{
			TransportDao dao = (TransportDao) getDao();
			total = dao.selectVehicleCount(search, type, channel, category, makeType, bodyType, fuelType, status);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectVehicleCount(8)", e);
		}
		return total;
	}
	
	/*setup*/
	public void insertCategoryObject(String tableName, SetupObject o){
		try{
			TransportDao dao = (TransportDao) getDao();
			dao.insertSetupObject(tableName, o);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error insertCategoryObject("+tableName+")", e);
			throw new RuntimeException("DAO Error");
		}
	}
	public void insertSetupObject(String tableName, SetupObject o){
		try{
			TransportDao dao = (TransportDao) getDao();
			dao.insertSetupObject(tableName, o);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error insertSetupObject("+tableName+")", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void updateCategoryObject(String tableName, SetupObject o){
		try{
			TransportDao dao = (TransportDao) getDao();
			dao.updateCategoryObject(tableName, o);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error updateCategoryObject("+tableName+")", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void updateSetupObject(String tableName, SetupObject o){
		try{
			TransportDao dao = (TransportDao) getDao();
			dao.updateSetupObject(tableName, o);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error updateSetupObject("+tableName+")", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void deleteSetupObject(String tableName, String setupId){
		try{
			TransportDao dao = (TransportDao) getDao();
			dao.deleteSetupObject(tableName, setupId);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error deleteSetupObject("+tableName+")", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public SetupObject getSetupObject(String tableName, String setupId){
		SetupObject o = new SetupObject();
		try{
			TransportDao dao = (TransportDao) getDao();
			Collection rows = dao.selectSetupObject(tableName, setupId);
			if (rows.size() > 0) {o = (SetupObject)rows.iterator().next();}
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getSetupObject("+tableName+")", e);
		}
		return o;
	}
	
	public Collection selectSetupObject(String tableName ,String search, String status, String sort, boolean desc, int start, int rows){
		try{
			TransportDao dao = (TransportDao) getDao();
			return dao.selectSetupObject(tableName, search, status, sort, desc, start, rows);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectSetupObject("+tableName+")", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public int selectSetupObjectCount(String tableName ,String search, String status){
		int total = 0;
		try{
			TransportDao dao = (TransportDao) getDao();
			total = dao.selectSetupObjectCount(tableName, search, status);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error selectSetupObjectCount("+tableName+")", e);
		}
		return total;
	}
	
	public Collection selectChannel(String id){
		try{
			TransportDao dao = (TransportDao) getDao();
			return dao.getChannel(id);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getChannel", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public Collection selectLocation(String id){
		try{
			TransportDao dao = (TransportDao) getDao();
			return dao.getLocation(id);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error getLocation", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	 public Collection getVehicles(String reqId) throws DaoException
	    {    	
	    	
		 	TransportDao dao = (TransportDao) getDao();       
	        Collection col = dao.selectVehicles(reqId);
	         
	        return col;
	    }
	 
	 public Collection getVehicleByType(String reqId, String type) throws DaoException
	    {    	
	    	
		 	TransportDao dao = (TransportDao) getDao();       
	        Collection col = dao.selectVehicleByType(reqId, type);
	         
	        return col;
	    }
	 
	 public void insertTransportVehicle(VehicleRequest vr){
			try{
				TransportDao dao = (TransportDao) getDao();
				dao.insertTranVehicle(vr);
			}catch(DaoException e){
				Log.getLog(getClass()).error("Error VehicleRequest"+e);
				throw new RuntimeException("DAO Error");
			}
		}
	 
	 public void deleteTransportVehicle(String id){
			try{
				TransportDao dao = (TransportDao) getDao();
				dao.deleteTranVehicle(id);
			}catch(DaoException e){
				Log.getLog(getClass()).error("Error deleteTransportVehicle"+e);
				throw new RuntimeException("DAO Error");
			}
		}
	 
	 public void deleteAssignedVehicle(String assgId, String vehicle_num){
		try{
			TransportDao dao = (TransportDao) getDao();
			dao.deleteAssignedVehicle(assgId, vehicle_num);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error deleteAssignedVehicle"+e);
			throw new RuntimeException("DAO Error");
		}
	}
	 
	 public void updateTransportRequest(TransportRequest tr){
			try{
				TransportDao dao = (TransportDao) getDao();
				dao.updateTransportRequest(tr);
			}catch(DaoException e){
				Log.getLog(getClass()).error("Error updateTransportRequest"+e);
				throw new RuntimeException("DAO Error");
			}
		}
	 
	public void addTransportRequest(TransportRequest tr){
		try {
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(tr.getEngineeringRequestId(), "CREATE_TRANS_REQUEST", "id=" + tr.getId());
			
			TransportDao dao = (TransportDao) getDao();
			dao.insertTransportRequest(tr);
		} catch(DaoException e) {
			Log.getLog(getClass()).error("Error addTransportRequest"+e);
			throw new RuntimeException("DAO Error");
		}
	}
	 
	 public Collection getAllTranRequest(String filter, String filStatus, String department, String sort, String userId, boolean desc, int startIndex,int maxRows) throws DaoException{
	    	
		 	TransportDao dao = (TransportDao) getDao();
		    Collection col = dao.selectAllTransportRequest(filter, filStatus, department, sort, userId, desc, startIndex, maxRows);
		    
		    return col;
	    }
	 
	 public int getAllCountTranRequest(String filter, String filStatus, String department, String userId) throws DaoException{
	    	
		 	int allcount = 0; 
		 	TransportDao dao = (TransportDao) getDao();
		 	allcount = dao.selectAllCountTransportRequest(filter, filStatus, department, userId);
		    
		    return allcount;
	    }
	 
	 public Collection getIncomingRequestHOD(String filter, String filStatus, String department, String sort, String userId, boolean desc, int startIndex,int maxRows) throws DaoException{
	    	
		 	TransportDao dao = (TransportDao) getDao();
		    Collection col = dao.selectIncomingRequestHOD(filter, filStatus, department, sort, userId, desc, startIndex, maxRows);
		    
		    return col;
	    }
	 
	 public int getCountIncomingRequestHOD(String filter, String filStatus, String department, String userId) throws DaoException{
	    	
		 	int allcount = 0; 
		 	TransportDao dao = (TransportDao) getDao();
		 	allcount = dao.selectCountIncomingRequestHOD(filter, filStatus, department, userId);
		    
		    return allcount;
	    }
	 
	 public TransportRequest selectTransportRequest(String id)throws DaoException{
		 
			 TransportDao dao = (TransportDao) getDao();       
			 TransportRequest TR = dao.selectTransportRequest(id);
		     
		     return TR;
	 }
	 
	 public TransportRequest selectTransportRequestByEngId(String engRequestId) throws DaoException {
		 TransportDao dao = (TransportDao) getDao();
		 TransportRequest TR = dao.selectTransportRequestByEngId(engRequestId);
		 
		 return TR;
	 }
	 
	 public OutsourceObject selectTransportOutsource(String id)throws DaoException{
		 
		 TransportDao dao = (TransportDao) getDao();       
		 OutsourceObject oO = dao.selectTransportOutsource(id);
	     
	     return oO;
	 }
 
	 public void updateStatusFromEngineering(String status, String reason, String engRequestId, String userId, Date date){
		 
		 try{
			 TransportDao dao = (TransportDao) getDao();
			 dao.updateStatusFromEngineering(status, reason, engRequestId, userId, date);
		 }catch(DaoException e){
			Log.getLog(getClass()).error("Error updateStatusFromEngineering "+e);
			throw new RuntimeException("DAO Error");
		}
	 }
	 
	 public void updateStatus(String status, String reason, String id, String userId, Date date){
		 
		 try{
			 TransportDao dao = (TransportDao) getDao();
			 dao.updateStatus(status, reason, id, userId, date);
		 }catch(DaoException e){
				Log.getLog(getClass()).error("Error updateStatus"+e);
				throw new RuntimeException("DAO Error");
		}
	 }
	 
	 public void deleteTransportAssignmentByRequestId(String id)throws DaoException{
		 	try{
				TransportDao dao = (TransportDao) getDao();
				dao.deleteTransportAssignmentByRequestId(id);
			}catch(DaoException e){
				Log.getLog(getClass()).error("Error deleteTransportRequest"+e);
				throw new RuntimeException("DAO Error");
			}
		 
	 }
	 public void deleteTransportVehicleAndDriverByRequestId(String id)throws DaoException{
		 	try{
				TransportDao dao = (TransportDao) getDao();
				dao.deleteTransportRequestVehicleByRequestId(id);
				dao.deleteTransportRequestDriverByRequestId(id);
				
			}catch(DaoException e){
				Log.getLog(getClass()).error("Error deleteTransportRequest"+e);
				throw new RuntimeException("DAO Error");
			}
		 
	 }
	 
	 public void deleteVehicles(String requestId)throws DaoException{
		 
		 	try{
				TransportDao dao = (TransportDao) getDao();
				dao.deleteVehicles(requestId);
			}catch(DaoException e){
				Log.getLog(getClass()).error("Error deleteVehicles"+e);
				throw new RuntimeException("DAO Error");
			}
		 
	 }
	 
	 //approveStatus
	 public void approveStatus(String status, String reason, String userId, String statusRequest, String id){
		 
		 try{
			 TransportDao dao = (TransportDao) getDao();
			 dao.approveStatus(status, reason, userId, statusRequest, id);
		 }catch(DaoException e){
				Log.getLog(getClass()).error("Error updateStatus"+e);
				throw new RuntimeException("DAO Error");
		}
	 }
	 
	 public SetupObject getVehicleCategory(String setup_id){
		 SetupObject v = new SetupObject();
			try{
				TransportDao dao = (TransportDao) getDao();
				Collection rows = dao.selectVehicleCategory(setup_id);
				if (rows.size() > 0) {v = (SetupObject)rows.iterator().next();}
			}catch(DaoException e){
				Log.getLog(getClass()).error("Error getVehicle(1)", e);
			}
			return v;
		}
	 
	 public Collection getVehicleByAssignment(String vehicle_num, Date start, Date end) throws DaoException{ 
	    	
		 	TransportDao dao = (TransportDao) getDao();
		 	
		 	return dao.selectAssigmentByVehicle(vehicle_num, start, end);
		 	
	    }
	 
	 
	 public void insertVehicleAssigment(VehicleObject vo) throws DaoException{
		 
		 TransportDao dao = (TransportDao) getDao();
		 dao.insertVehicleAssigment(vo);
		    
	 }
	
	 public void insertDriverAssigment(ManpowerLeaveObject ml) throws DaoException{
		 
		 TransportDao dao = (TransportDao) getDao();
		 dao.insertDriverAssigment(ml);
		    
	 }
	 
	 public void deleteAssignedDriver(String assgId, String manpowerId){
		try{
			Application app = Application.getInstance();
			AssignmentLogModule mod = (AssignmentLogModule) app.getModule(AssignmentLogModule.class);
			AssignmentLog log = new AssignmentLog();
			TransportDao dao = (TransportDao) getDao();
			log.setLogId(UuidGenerator.getInstance().getUuid());
			log.setAssignmentId(assgId);
			log.setOldUserId(manpowerId);
			log.setAssignDate(new Date());
			mod.addLog(log);
			dao.deleteAssignedDriver(assgId, manpowerId);
			dao.deleteVehicleDriver(assgId, manpowerId);
			
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error deleteAssignedVehicle"+e);
			throw new RuntimeException("DAO Error");
		}
	}
	 
	 public Collection selectAssigmentByDriver(String userId,Date start, Date end) throws DaoException{ 
	    	
		 	TransportDao dao = (TransportDao) getDao();
		 	
		 	return dao.selectAssigmentByDriver(userId, start, end);
		 	
	    }
	 
	 
	 public Collection getDriversAssignmentByUserId(String userId,Date start, Date end) throws DaoException{ 
	    	
		 	TransportDao dao = (TransportDao) getDao();
		 	
		 	return dao.selectDriversAssignmentByUserId(userId, start, end);
		 	
	    }
	 
	 //
	 public Collection selectAssigmentByRequestId(String requestId, String assgId) throws DaoException {
	    	
		 	TransportDao dao = (TransportDao) getDao();
		 	
		 	return dao.selectAssigmentByRequestId(requestId, assgId);
		 	
	    }
	 //
	 
	 public Collection getVehicleByRequestId(String requestId){
			try{
				TransportDao dao = (TransportDao) getDao();
				return dao.selectVehicleByRequestId(requestId);
			}catch(DaoException e){
				Log.getLog(getClass()).error("Error selectVehicleByRequestId", e);
				throw new RuntimeException("DAO Error");
			}
		}
	 
	 public Collection getDriverByRequestId(String requestId){
		
		 //TODO change to selectDriverByAssgId??!!
		 SecurityService ss =(SecurityService) Application.getInstance().getService(SecurityService.class) ;
		 Collection drivers = new ArrayList();
		 String drivername = "";
		 String manpowerId = "";
		 try{
				TransportDao dao = (TransportDao) getDao();				
				Collection col = dao.selectDriverByRequestId(requestId);
				for(Iterator it = col.iterator(); it.hasNext(); ){
					ManpowerLeaveObject ob = (ManpowerLeaveObject) it.next();					
					try{
						drivername = ss.getUser(ob.getManpowerId()).getName();
						manpowerId = ob.getManpowerId();
					}catch(Exception er){}
					ob.setManpowerName(drivername);
					ob.setManpowerId(manpowerId);
					ob.setRequestId(requestId);
					drivers.add(ob);
					
				}
				
				
			}catch(DaoException e){
				Log.getLog(getClass()).error("Error selectDriverByRequestId", e);
				throw new RuntimeException("DAO Error");
			}
			
			return drivers;
		}
	 
	 public Collection getDriverByAssgId(String assgId){
			
		 SecurityService ss =(SecurityService) Application.getInstance().getService(SecurityService.class) ;
		 Collection drivers = new ArrayList();
		 String drivername = "";
		 String manpowerId = "";
		 String requestId = "";
		 try{
				TransportDao dao = (TransportDao) getDao();				
				Collection col = dao.selectDriverByAssgId(assgId);
				for(Iterator it = col.iterator(); it.hasNext(); ){
					ManpowerLeaveObject ob = (ManpowerLeaveObject) it.next();					
					try{
						drivername = ss.getUser(ob.getManpowerId()).getName();
						manpowerId = ob.getManpowerId();
						requestId = ob.getRequestId();
					}catch(Exception er){}
					ob.setManpowerName(drivername);
					ob.setManpowerId(manpowerId);
					ob.setRequestId(requestId);
					drivers.add(ob);
					
				}
				
				
			}catch(DaoException e){
				Log.getLog(getClass()).error("Error selectDriverByRequestId", e);
				throw new RuntimeException("DAO Error");
			}
			
			return drivers;
		}
	 
	 
	 public Collection getManPower(String userId,Date start, Date end) throws DaoException{ 
		 
		 ManpowerObject man = new ManpowerObject();
		 Collection manpower = new ArrayList();
		 String workProfile = null;
		 
		 Collection assignments = new ArrayList();
		 Collection leaves = new ArrayList();
		 TransportDao dao = (TransportDao) getDao();		
		 SecurityService ss =(SecurityService) Application.getInstance().getService(SecurityService.class) ;
		 		 
		 //assignments = dao.selectDriversByAssignment(userId, start, end);
		 assignments = dao.selectDriversAssignmentByUserId(userId, start, end);
		 leaves = dao.selectDriversByLeave(userId,start, end);
		 
		 
		 man.setManpowerId(userId);
		 man.setAssignments(assignments);
		 man.setLeaves(leaves);
		 
		 ////
		 workProfile = dao.userWorkingProfile(userId, start, end);
		 
		 /*if(workProfile == null || "".equals(workProfile)){
			 SetupDao sdao = (SetupDao) Application.getInstance().getModule(SetupModule.class).getDao();
	         Collection defCol = new ArrayList();
		 		try {
		 			defCol = sdao.selectWorkingProfile(null, null, false, 0, -1);
		 			for(Iterator it = defCol.iterator(); it.hasNext(); ){
		 				WorkingProfile wP = (WorkingProfile) it.next();
		 				if(wP.isDefaultProfile()){
		 					workProfile = wP.getName();
		 				}
		 			}
		 		} catch (DaoException e) {
		 			Log.getLog(getClass()).error(e.toString());			
		 		}
		 }*/
 		
 		
		 man.setWorkprofile(workProfile);		 
		 ////
		 
		 try{
			 man.setManpowerName(ss.getUser(userId).getName());
			 
		 }catch(Exception er){}
		 
		 manpower.add(man);
		 
		 return manpower;
	 }
	 
	 public String getWorkingProfileUser(String userId, Date startDate, Date endDate)throws DaoException{
		 
		 TransportDao dao = (TransportDao) getDao();		
		 return dao.userWorkingProfile(userId, startDate, endDate);
		 
	 }
	 
	 public Collection getManPowerAssignment(String userId, Date start, Date end) throws DaoException{ 
		 
		 return null;
	 }
	 
	 public Collection getVehicleAssignment(String assgId, String vehicle_num, Date start, Date end) throws DaoException{ 
		 
		 
		 VehicleAssignmentObject veh = new VehicleAssignmentObject();
		 Collection colV = new ArrayList();
		 Collection vehicles = new ArrayList();
		 Collection tmp = new ArrayList();
		 Collection coldetailveh = new ArrayList();
		 
		 
		 String category_name = "";
		 String categoryId = "";
		 
		 	TransportDao dao = (TransportDao) getDao();		 	
		 	tmp = dao.selectVehicleByAssignment(vehicle_num);		 	
		 	for(Iterator it = tmp.iterator(); it.hasNext(); ){
		 		VehicleObject vo = (VehicleObject) it.next();
		 		category_name = vo.getCategory_name();	
		 		categoryId = vo.getCategory_id();
		 	}
		 	
		 	//get category_id to match what user request
		 	try{
		 		coldetailveh = dao.selectDetailsVehicleByAssignmentId(assgId);		 		
		 	}catch(Exception er){}
		 	String[] category = new String[coldetailveh.size()];
		 	int x = 0;
		 	for(Iterator it = coldetailveh.iterator(); it.hasNext(); ){		 		
		 		VehicleRequest vr = (VehicleRequest) it.next();
		 		category[x] = vr.getCategory_id();
		 		x++;
		 	}
		 	
		 	for(int i = 0; i < category.length; i++){
		 		
			 	if(category[i].equals(categoryId)){
			 		//colV = dao.selectAssigmentByVehicle(vehicle_num, start, end);
			 		colV = dao.selectVehicleAssigmentByVehicleNum(vehicle_num, start, end);
				 	veh.setVehicle_num(vehicle_num);
				 	veh.setVehicleObject(colV);
				 	veh.setCategory_name(category_name);		 	
				 	vehicles.add(veh);
			 	}
		 	}
		 	
		 	return vehicles;
	 }
	 
	 public void insertOutsource(OutsourceObject oo)throws DaoException{ 
		 TransportDao dao = (TransportDao) getDao();
		 dao.addOutsource(oo);		 
	 }

	 /*public Collection getOutsourceItems(String outsourceId) throws DaoException{
	    	
		 	TransportDao dao = (TransportDao) getDao();
		    Collection col = dao.selectOutsources(outsourceId);
		    
		    return col;
	    }*/
	 
	 public Collection getAllComingRequest(Date from, Date to, String department, String filter, String filStatus, String sort, String userId, boolean desc, int startIndex,int maxRows) throws DaoException{
	    	
		 	TransportDao dao = (TransportDao) getDao();
		    Collection col = dao.selectAllComingRequest(from, to, department, filter, filStatus, sort, userId, desc, startIndex, maxRows);
		    
		    return col;
	    }
	 
	 public Collection getAssignment(int settingValue, String filter, String filDept, Date start, Date end, String sort, String userId, boolean desc, int startIndex,int maxRows) throws DaoException{
	    	
		 	TransportDao dao = (TransportDao) getDao();
		 	
		 	//Collection col = dao.selectTodayAssignment(filter, filDept, start, end, sort, userId, desc, startIndex, maxRows);
		 	Collection col = dao.selectAssignments(settingValue, filter, filDept, start, end, sort, userId, desc, startIndex, maxRows);
		    
		    return col;
	    }

	 public int getCountAssignment(int settingValue, String filter, String filDept, Date start, Date end, String userId) throws DaoException{
	    	
		 	int count = 0; 
		 	TransportDao dao = (TransportDao) getDao();		    
		 	count = dao.selectCountAssignments(settingValue, filter, filDept, start, end, userId);
		    
		    return count;
	    }
	 
	 public Collection getDriverAssignments(String filter, Date start, Date end, String sort, String userId, boolean desc, int startIndex,int maxRows) throws DaoException{
	    	
		 	TransportDao dao = (TransportDao) getDao();
		 	Collection col = dao.selectDriverAssignments(filter, start, end, sort, userId, desc, startIndex, maxRows);
		    
		    return col;
	    }
	 
	 public int getCountDriverAssignments(String filter, Date start, Date end, String userId) throws DaoException{
	    	
		 	int count = 0; 
		 	TransportDao dao = (TransportDao) getDao();		    
		 	count = dao.selectCountDriverAssignments(filter, start, end, userId);
		    
		    return count;
	    }
	 
	 
	 //selectCountDriverAssignments
	 public Collection getVehicleByNo(String vehicle_num, Date start, Date end) throws DaoException{
	    	
		 	TransportDao dao = (TransportDao) getDao();
		    Collection col = dao.getVehicleByNo(vehicle_num, start, end);
		    
		    return col;
	    }
	 
	 
	 public Collection getAssignmentDetailRequestId(String requestId) throws DaoException{
	    	
		 	TransportDao dao = (TransportDao) getDao();
		    Collection col = dao.selectAssignmentDetailRequestId(requestId);
		    
		    return col;
	    }
	 
	 public Collection getAssignmentDetailVehicleNo(String vehicleNo) throws DaoException{
	    	
		 	TransportDao dao = (TransportDao) getDao();
		    Collection col = dao.selectAssignmentDetailVehicleNo(vehicleNo);
		    
		    return col;
	    }
	 
	 public Collection getAssignmentDetailFlagId(String flagId) throws DaoException{
	    	
		 	TransportDao dao = (TransportDao) getDao();
		    Collection col = dao.selectAssignmentDetailFlagId(flagId);
		    
		    return col;
	    }
	 
	 
	 public Collection getAssignmentDetailFlagIdVehicleNo(String flagId, String vehicleNo) throws DaoException{
	    	
		 	TransportDao dao = (TransportDao) getDao();
		    Collection col = dao.selectAssignmentDetailFlagIdVehicleNo(flagId, vehicleNo);
		    
		    return col;
	    }
	 
	 
	 public void addUnfulfilledAssignment(String remarks, String status, String id)throws DaoException{
		 TransportDao dao = (TransportDao) getDao();
		 dao.addUnfulfilledAssignment(remarks, status, id);
	 }
	 
	 public boolean upsertUnfulfilledAssignment(AssignmentObject AO) {
		 try {
			 TransportDao dao = (TransportDao) getDao();
			 dao.upsertAssignmentToUnfulfilled(AO);
			 return true;
		 } catch (DaoException d) {
			 Log.getLog(getClass()).error(d.getMessage(), d);
			 return false;
		 }
	 }
	 
	 public TransportRequest selectTransportAssignment(String id)throws DaoException{
		 
		 TransportDao dao = (TransportDao) getDao();       
		 TransportRequest TR = dao.selectTransportAssignment(id);
	     
	     return TR;
	 }
	 
	 public TransportRequest selectTransportAssignmentByAssignmentId(String id)throws DaoException{
		 
		 TransportDao dao = (TransportDao) getDao();       
		 TransportRequest TR = dao.selectTransportAssignmentByAssignmentId(id);
	     
	     return TR;
 }
	 
	 public void insertAssignmentDetails(AssignmentObject AO)throws DaoException{ 
		 TransportDao dao = (TransportDao) getDao();
		 dao.addAssignmentDetails(AO);		 
	 }
	 
	 
	 public TransportRequest getTransportByAssignment(String id) throws DaoException{
	    	
		 	TransportDao dao = (TransportDao) getDao();
		    return dao.selectTransportByAssignment(id);
		    
		   
	    }
	 
	 public AssignmentObject getAssignmentObject(String id, String vehicleNo) throws DaoException{
	    	
		 	TransportDao dao = (TransportDao) getDao();
		    return dao.selectAssignmentObject(id, vehicleNo);
		    		   
	    }
	 
	 public void updateAssignmentDetails(AssignmentObject AO)throws DaoException{ 
		 TransportDao dao = (TransportDao) getDao();
		 dao.updateAssignmentDetails(AO);		 
	 }
	 
	 public Collection getVehicleRequestId(String requestId, String type, String assgId) throws DaoException{
	    	
		 	TransportDao dao = (TransportDao) getDao();
		    Collection col = dao.selectVehicleRequestId(requestId, type, assgId);
		    
		    return col;
	    }
	 
	 public int selectCountTotalAssigned(String requestId){
			int countAssg = 0;
			
			TransportDao dao = (TransportDao) getDao();
			try {
				return dao.selectCountTotalAssigned(requestId);
			} catch (DaoException e) {
				return countAssg;
			}
		}
	 //selectVehicleRequestId
	 
	 
	 
	 public TransportRequest getTransportRequest(String requestId)throws DaoException{
		 	TransportDao dao = (TransportDao) getDao();
		    return dao.getTransportRequest(requestId);
	 }
	 
	 public String selectStatus(String alpha){
		 
		 SetupModule sm = (SetupModule) Application.getInstance().getModule(SetupModule.class);
		 String status = null;
		 if (alpha.equals(sm.DRAFT_STATUS))
				status = Application.getInstance().getMessage("fms.tran.status.draft");
			
			else if (alpha.equals(sm.CANCELLED_STATUS))
				status = Application.getInstance().getMessage("fms.tran.status.cancelled");
			
			else if (alpha.equals(sm.PROCESS_STATUS))
				status = Application.getInstance().getMessage("fms.tran.status.process");

			else if (alpha.equals(sm.FULFILLED_STATUS))
				status = Application.getInstance().getMessage("fms.tran.status.fulfilled");

			else if (alpha.equals(sm.UNFULFILLED_STATUS))
				status = Application.getInstance().getMessage("fms.tran.status.unfulfilled");

			else if (alpha.equals(sm.OUTSOURCED_STATUS))
				status = Application.getInstance().getMessage("fms.tran.status.outsource");

			else if (alpha.equals(sm.REJECTED_STATUS))
				status = Application.getInstance().getMessage("fms.tran.status.rejected");

			else if (alpha.equals(sm.APPROVED_STATUS))
				status = Application.getInstance().getMessage("fms.tran.status.approved");

			else if (alpha.equals(sm.NORMAL_STATUS))
				status = Application.getInstance().getMessage("fms.tran.status.normal");
			
			else if (alpha.equals(sm.LATE_STATUS))
				status = Application.getInstance().getMessage("fms.tran.status.late");
			
			else if (alpha.equals(sm.PENDING_STATUS))
				status = Application.getInstance().getMessage("fms.tran.status.pending");		 
			
			else if (alpha.equals(sm.ADHOC_STATUS))
				status = Application.getInstance().getMessage("fms.tran.status.adHoc");
		 
			else if (alpha.equals(sm.COMPLETED_STATUS))
				status = Application.getInstance().getMessage("fms.tran.status.completed");
		 
			else if (alpha.equals(sm.NEW_STATUS))
				status =Application.getInstance().getMessage("fms.tran.status.new");
		 
			else if (alpha.equals(sm.CLOSED_STATUS))
				status = Application.getInstance().getMessage("fms.tran.status.closed");
		 
			else if (alpha.equals(sm.ASSIGNED_STATUS))
				status = Application.getInstance().getMessage("fms.tran.status.assigned");
		 
			else if (alpha.equals(sm.UPDATED_STATUS))
				status = Application.getInstance().getMessage("fms.tran.status.updated");
		 
			else if (alpha.equals(sm.CHECKOUT_STATUS))
				status = Application.getInstance().getMessage("fms.tran.status.checkout");

			
			
 		return status;				 
		 
	 }
	 
	 public void sendNotificationToApprovers(String deptId, String requestId, String wordings){
			
			FmsNotification notification = new FmsNotification();		
			FMSDepartmentManager deptManager = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);			
			String subject = replaceWithDetails(requestId, wordings);		
			String body = subject;
			try{			
				String emailTo[] = deptManager.getEmailApprovers(deptId);			
				notification.send(emailTo, subject, body);
				
			}catch(Exception er){
				Log.getLog(getClass()).error("TransportModule: ERROR sendNotification "+er);
			}					
		}

	private String replaceWithDetails(String requestId, String wordings) {
		String subject = wordings.replace("{REQUESTID}", requestId);
		return subject;
	}
	 
	 
	 public void sendNotificationToRequestor(String requestId, String wordings){
			
			FmsNotification notification = new FmsNotification();		
			FMSDepartmentManager deptManager = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);			
			String subject = replaceWithDetails(requestId, wordings);		
			String body = subject;
			try{			
				String emailTo[] = deptManager.getEmailRequestor(requestId);			
				notification.send(emailTo, subject, body);
				
			}catch(Exception er){
				Log.getLog(getClass()).error("TransportModule: ERROR sendNotification "+er);
			}					
		}
	 
	 ////
	 public void sendNotificationForApproval(String deptId, String requestId, String requestTitle, String requiredDate, String remarks, String requestor, String wordSubject, String wordings){
			
			FmsNotification notification = new FmsNotification();		
			FMSDepartmentManager deptManager = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);
						
			String subject = replaceWithDetails(requestId, wordSubject);		
			String body = replaceBodyWordings(requestId, requestTitle, requiredDate, remarks, requestor, wordings);
			try{			
				String emailTo[] = deptManager.getEmailApprovers(deptId);			
				notification.send(emailTo, subject, body);
				
			}catch(Exception er){
				Log.getLog(getClass()).error("TransportModule: ERROR sendNotification "+er);
			}					
		}
	 
	 public void sendNotificationToRequestors(String requestId, String requestTitle, String requiredDate, String wordSubject, String wordings, String remarks){
			
			FmsNotification notification = new FmsNotification();		
			FMSDepartmentManager deptManager = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);			
			
			String subject = replaceWithDetails(requestId, wordSubject);		
			String body = replaceBodyWordings(requestId, requestTitle, requiredDate, remarks, "", wordings);
			
			try{			
				String emailTo[] = deptManager.getEmailRequestor(requestId);			
				notification.send(emailTo, subject, body);
				
			}catch(Exception er){
				Log.getLog(getClass()).error("TransportModule: ERROR sendNotification "+er);
			}					
		}
	 
	 
	 
	 private String replaceBodyWordings(String requestId, String requestTitle, String requiredDate, String remarks, String requestor, String wordings) {
			
		 	String subject = wordings.replace("{REQUESTID}", requestId); 
			subject = subject.replace("{REQUESTTITLE}", requestTitle);
			subject = subject.replace("{REQUIREDDATE}", requiredDate);			
			subject = subject.replace("{REMARKS}", remarks);
			subject = subject.replace("{REQUESTOR}", requestor);
			
			return subject;
		}
		
	
	////
	 
	public Collection getMyAssignment(String search, String status, String userId, Date endDate, String sort, boolean desc, int start, int rows) throws DaoException{
		TransportDao dao=(TransportDao)getDao();
		try {
			return dao.selectMyAssignment(search, status, userId, endDate, sort, desc, start, rows);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}	
	
	public int countMyAssignment(String search, String status){
		int countRcm = 0;
		
		TransportDao dao = (TransportDao) getDao();
		try {
			return dao.selectCountMyAssignment(search, status, Application.getInstance().getCurrentUser().getId());
		} catch (DaoException e){
			return countRcm;
		}
	}
	
	public int countRequestedVehicle(String requestId){
		int countRV = 0;
		
		TransportDao dao = (TransportDao) getDao();
		try {
			return dao.selectCountRequestedVehicle(requestId);
		} catch (DaoException e) {
			return countRV;
		}
	}
	
	public Collection getManpowerAssignment(String userId,Date start, Date dateAssignment, String department) throws DaoException{ 
		 
		
		 Collection assignments = new ArrayList();
		 Collection wprofiles = new ArrayList();
		 		 
		 ManpowerAssignmentObject maObj = new ManpowerAssignmentObject();
		 String transportdept = Application.getInstance().getProperty("ManagementService");//get transport dept		 
	     String engineeringdept = Application.getInstance().getProperty("fms.facilities.engineering.engineeringDepartmentId");
	     Calendar calStart = Calendar.getInstance();
	     Calendar calEnd = Calendar.getInstance();
		 TransportDao dao = (TransportDao) getDao();		
		 Collection tmpassignments = new ArrayList();
		 ManpowerAssignmentObject manObj = new ManpowerAssignmentObject();
		
		 //Assignment
		 if (transportdept.equals(department)) {
			 assignments = dao.selectDriversDuty(userId, start, dateAssignment);
		 } else if(engineeringdept.equals(department)) {
			 assignments = null;			
			 assignments = dao.selectEngineeringDuty(userId, start, dateAssignment);			
			 
			 if(assignments.size() > 0){
				 
				 for(Iterator it = assignments.iterator(); it.hasNext(); ){
					 ManpowerAssignmentObject man = (ManpowerAssignmentObject) it.next();
					 
					 String startTime = man.getStartTime();
					 String endTime = man.getEndTime();
					
					
					 //for format start time
					 String hStartTime = startTime.substring(0,2);
					 String mStartTime = startTime.substring(3);
					 
					 calStart.setTime(man.getStartDate());
					 calStart.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hStartTime));
					 calStart.set(Calendar.MINUTE, Integer.parseInt(mStartTime));					 
					 Date startDate = calStart.getTime();
					 
					 //for format end time
					 String hEndTime = endTime.substring(0,2);
					 String mEndTime = endTime.substring(3);
					 
					 calEnd.setTime(man.getEndDate());
					 calEnd.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hEndTime));
					 calEnd.set(Calendar.MINUTE, Integer.parseInt(mEndTime));
					 Date endDate = calEnd.getTime();
					 
					 man.setStartDate(startDate);
					 man.setEndDate(endDate);
					 
					 manObj = man;
					 
					 tmpassignments.add(manObj);					 
					
				 }
				 
				 assignments = tmpassignments;
			 }
		 }
		 
		 //Working Profile
		 wprofiles = dao.selectWorkingProfile(userId, start, dateAssignment); //user has a working profile inside request time/date
		 
		 if(wprofiles.size() > 0){
			 for(Iterator it = wprofiles.iterator(); it.hasNext(); ){
				 maObj = (ManpowerAssignmentObject) it.next();
				 assignments.add(maObj);
			 }
		 }
		 
		//Leave
		Collection leaves = new ArrayList();
		leaves = dao.selectDriversLeave(userId);
		ManpowerLeaveObject mLObj = new ManpowerLeaveObject();
		mLObj = null;
		 if(leaves.size() > 0){
			 for(Iterator it = leaves.iterator(); it.hasNext(); ){
				 mLObj = (ManpowerLeaveObject) it.next();	
				 maObj = new ManpowerAssignmentObject();
				 maObj.setManpowerLeaveObject(mLObj);
				 assignments.add(maObj);				 
			 }						 
			 
		 }
		 
		 return assignments;
	}
	
		
	// for status trail
	public void insertRequestStatus(String requestId, String status,String reason){
		TransportDao dao=(TransportDao)getDao();
		try {
			Status statusObj=new Status();
			statusObj.setStatusId(UuidGenerator.getInstance().getUuid());
			statusObj.setId(requestId);
			statusObj.setStatus(status);
			statusObj.setReason(reason);
			statusObj.setCreatedBy(Application.getInstance().getCurrentUser().getName());
			Date now=new Date();
			statusObj.setCreatedDate(now);
			dao.insertStatus(statusObj);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void updateRequestStatus(String requestId, String status,String reason){
		TransportDao dao=(TransportDao)getDao();
		try {
			Status statusObj=new Status();
			statusObj.setStatusId(UuidGenerator.getInstance().getUuid());
			statusObj.setId(requestId);
			statusObj.setStatus(status);
			statusObj.setReason(reason);
			statusObj.setCreatedBy(Application.getInstance().getCurrentUser().getName());
			Date now=new Date();
			statusObj.setCreatedDate(now);
			dao.updateStatusTrail(statusObj);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public int countRequestStatus(String id, String status){
		int countRV = 0;
		
		TransportDao dao = (TransportDao) getDao();
		try {
			return dao.countStatusTrail(id, status);
		} catch (DaoException e) {
			return countRV;
		}
	}
	
	public Collection getStatusTrail(String requestId){
		TransportDao dao=(TransportDao)getDao();
		Collection col=new ArrayList();
		try {
			col=dao.selectStatusTrail(requestId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getStatusTrailByStatus(String requestId, String status){
		TransportDao dao=(TransportDao)getDao();
		Collection col=new ArrayList();
		try {
			col=dao.selectStatusTrailByStatus(requestId, status);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	

	public Collection getAssignmentDetailDriver(String requestId) throws DaoException{

		TransportDao dao = (TransportDao) getDao();
		Collection col = dao.selectAssignmentDetailDriver(requestId);

		return col;
	}
	 
	 public String getDriverAssignmentIdByRequestId(String requestId, String userId)throws DaoException {
	 	TransportDao dao = (TransportDao) getDao();  
	 	
	 	return dao.getDriverAssignmentIdByRequestId(requestId, userId);
	 }

	 public TransportRequest getDriverAssignment(String requestId, String userId)throws DaoException{
		 
		 TransportDao dao = (TransportDao) getDao();       
		 TransportRequest TR = dao.selectDriverAssignment(requestId, userId);
	     
	     return TR;
	 }
	 
	 public TransportRequest getDriverAssignmentByAssgIdUserId(String assgId, String userId)throws DaoException{
		 
		 TransportDao dao = (TransportDao) getDao();       
		 TransportRequest TR = dao.selectDriverAssignmentByAssgIdUserId(assgId, userId);
	     
	     return TR;
	 }
	 
	 public void updateCompleteAssignment(ManpowerAssignmentObject manPower)throws DaoException{
		 
		 TransportDao dao = (TransportDao) getDao();       
		 dao.updateCompleteAssignment(manPower);
		 //dao.addFileAttachment(manPower);
	 }
	 
	 public void addFileAssignment(ManpowerAssignmentObject manPower)throws DaoException{
		 
		 TransportDao dao = (TransportDao) getDao();
		 dao.addFileAttachment(manPower);
	 }
	 
	 public void updateDrivers(String newUserId, String id, String userId)throws DaoException{
		 
		 TransportDao dao = (TransportDao) getDao();       
		 dao.updateDrivers(newUserId, id, userId);		 
	 }
	 
	 public void updateUnfulfilledAssignment(ManpowerAssignmentObject manPower)throws DaoException{
		 
		 TransportDao dao = (TransportDao) getDao();       
		 dao.updateUnfulfilledAssignment(manPower);		
	 }
	 
	 public Collection getAllRequestForBatch(String search, String departmentId, String programId, Date fromDate, Date toDate) throws DaoException{
			try{
				TransportDao dao=(TransportDao)getDao();
				Collection col = dao.selectAllRequestForBulk(search, departmentId, programId, fromDate, toDate);
				
				/*for(Iterator<HashMap> itr=col.iterator();itr.hasNext();){
					HashMap map=(HashMap)itr.next();
					
					String requiredTime=map.get("fromTime")+" - "+map.get("toTime");
					map.put("requiredTime", requiredTime);
					map.put("statusLabel", EngineeringModule.ASSIGNMENT_MANPOWER_STATUS_MAP.get(map.get("status")+""));
				}*/
				return col;
			}catch(Exception e){
				Log.getLog(getClass()).error(e.getMessage(), e);
			}
			return new ArrayList();
		}
	
	 public Collection getUnitUsers(String search,String unitId,String unitHeadId, String groupId, String sort,boolean desc,int start,int rows){
			Collection col=new ArrayList();
			TransportDao dao=(TransportDao)getDao();
			try {
				col= dao.selectUnitUsers(search,unitId,unitHeadId, groupId,sort,desc,start,rows);
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.getMessage(),e);
			}
			return col;
		}
	 
	 public Collection selectHoliday(String year, String month) throws DaoException{
			try{
				TransportDao dao=(TransportDao)getDao();
				return dao.selectHolidays(year, month);
			}catch (DaoException e){
				Log.getLog(getClass()).error("Error selectHoliday(5)", e);
				throw new RuntimeException("DAO Error");
			}
		}
	 
	 public Collection getAssignmentListing(String filter, String sort, String userId, String requestId, boolean desc, int startIndex,int maxRows) throws DaoException{
	    	
		 	TransportDao dao = (TransportDao) getDao();		    
		 	Collection col = dao.selectAssignmentListing(filter, sort, userId, requestId, desc, startIndex, maxRows);
		    
		    return col;
	    }
	 
	 public void insertTranAssigment(TransportRequest tr) throws DaoException{
		 
		 TransportDao dao = (TransportDao) getDao();
		 dao.insertTranAssigment(tr);
		    
	 }
	
	 public Collection getAssignmentByRequestId(String requestId) throws DaoException{
	    	
		 	TransportDao dao = (TransportDao) getDao();		    
		 	Collection col = dao.selectAssignmentByRequestId(requestId);
		    
		    return col;
	 }
	 
	 public Collection getAssignmentByAssignmentId(String assgId) throws DaoException{
	    	
		 	TransportDao dao = (TransportDao) getDao();		    
		 	Collection col = dao.selectAssignmentByAssignmentId(assgId);
		    
		    return col;
	    }
	 
	 public Collection getAssignmentVehicles(String assgId) throws DaoException{
	    	
		 	TransportDao dao = (TransportDao) getDao();		    
		 	Collection col = dao.selectAssignmentVehicles(assgId);
		    
		    return col;
	 }
	 
	 public Collection getDriverByAssignmentId(String assgId) throws DaoException{
	    	
		 	TransportDao dao = (TransportDao) getDao();		    
		 	Collection col = dao.selectDriverByAssignmentId(assgId);
		    
		    return col;
	 }
	 
	 public Collection getDetailsVehicleByAssignmentId(String assgId) throws DaoException{
	    	
		 	TransportDao dao = (TransportDao) getDao();		    
		 	Collection col = dao.selectDetailsVehicleByAssignmentId(assgId);
		    
		    return col;
	 }
	 
	 public Collection getVehicleDriverQuantityByType(String reqId, String type, String assgId) throws DaoException
	    {    	
	    	
		 	TransportDao dao = (TransportDao) getDao();       
	        Collection col = dao.selectVehicleDriverQuantityByType(reqId, type, assgId);
	         
	        return col;
	    }
	 
	 public Collection getVehicleAssigmentByVehicleNum(String vehicle_num, Date start, Date end) throws DaoException
	    {    	
	    	
		 	TransportDao dao = (TransportDao) getDao();       
	        Collection col = dao.selectVehicleAssigmentByVehicleNum(vehicle_num, start, end);
	         
	        return col;
	    }
	 
	
	 
	 public int getVehicleQuantity(String requestId, String type){
			int countRV = 0;
			
			TransportDao dao = (TransportDao) getDao();
			try {
				return dao.selectVehicleQuantity(requestId, type);
			} catch (DaoException e) {
				return countRV;
			}
		}
	 
	 public Map getQuantityVehiclesDrivers(String requestId) throws DaoException
	    {
		 	Map map = new HashMap();
		 	TransportDao dao = (TransportDao) getDao();
		 	map = dao.getQuantityVehiclesDrivers(requestId);
	        return map;
	    }
	 
	 public int getVehicleCount(String requestId){
			int countRV = 0;
			
			TransportDao dao = (TransportDao) getDao();
			try {
				return dao.selectCountVehicleByRequest(requestId);
			} catch (DaoException e) {
				return countRV;
			}
		}
	 
	 public int getDriverCount(String requestId){
			int countRV = 0;
			
			TransportDao dao = (TransportDao) getDao();
			try {
				return dao.selectCountDriverByRequest(requestId);
			} catch (DaoException e) {
				return countRV;
			}
		}
	 

	 public TransportRequest getTransportByFlagId(String flagId)throws DaoException{
		 
		 TransportDao dao = (TransportDao) getDao();       
		 TransportRequest TR = dao.selectTransportByFlagId(flagId);
	     
	     return TR;
	 }
	 
	 public TransportRequest getTransportAssignmentByAssgId(String assgId)throws DaoException{
		 
		 TransportDao dao = (TransportDao) getDao();       
		 TransportRequest TR = dao.selectTransportAssignmentByAssgId(assgId);
	     
	     return TR;
	 }
	 //selectDriverAssignmentsForAdmin

	 public TransportRequest getDriverAssignmentsForAdmin(String flagId)throws DaoException{
		 
		 TransportDao dao = (TransportDao) getDao();       
		 TransportRequest TR = dao.selectDriverAssignmentsForAdmin(flagId);
	     
	     return TR;
	 }
	 
	 public Collection getAllTranRequest(Date from, Date to, String status, String filter, String filStatus, String department, String sort, String userId, boolean desc, int startIndex,int maxRows) throws DaoException{
	    	
		 	TransportDao dao = (TransportDao) getDao();
		    Collection col = dao.selectAllTransportRequest(from, to, status, filter, filStatus, department, sort, userId, desc, startIndex, maxRows);
		    
		    return col;
	    }
	 
	 public int getAllCountTranRequest(Date from, Date to, String status, String filter, String filStatus, String department, String userId) throws DaoException{
	    	
		 	int allcount = 0; 
		 	TransportDao dao = (TransportDao) getDao();
		 	allcount = dao.selectAllCountTransportRequest(from, to, status, filter, filStatus, department, userId);
		    
		    return allcount;
	    }
	 
	 public void updateCloseReqStatus(String id, String status){
		 
		 try{
			 TransportDao dao = (TransportDao) getDao();
			 dao.updateCloseReqStatus(id, status);
		 }catch(DaoException e){
			Log.getLog(getClass()).error("Error update Status for Transport Request "+e);
			throw new RuntimeException("DAO Error");
		}
	 }
	 
	 public VehicleRequest getVehicleRequestByRequestId(String requestId)throws DaoException{
		 
		 TransportDao dao = (TransportDao) getDao();
		 return dao.getVehicleRequestByRequestId(requestId);
	 }
	 
	 public void updateVehicleRate(String id, String rate){
		 
		 try{
			 TransportDao dao = (TransportDao) getDao();
			 dao.updateVehicleRate(id, rate);
		 }catch(DaoException e){
			Log.getLog(getClass()).error("Error update Rate for updateVehicleRate "+e);
			throw new RuntimeException("DAO Error");
		}
	 }
	 
	 public VehicleObject selectVehicleByNo(String vehicle_num){
		 
		 VehicleObject vehicleObject = null;
		 try{
			 TransportDao dao = (TransportDao) getDao();
			 vehicleObject = dao.selectVehicleByNo(vehicle_num);
		 }catch(DaoException e){
			Log.getLog(getClass()).error("Error on selectVehicleByNo "+e);
		 }
		 return vehicleObject;
	 }
	 
	 public void deleteOBVanRecords(String engRequestId)throws DaoException {
			
		try {
			TransportDao dao = (TransportDao) getDao();
			Collection transReqs = dao.listTransportRequestByEngId(engRequestId);
			 
			if (transReqs.size() > 0) {
				for (Iterator iterator = transReqs.iterator(); iterator.hasNext();) {
					TransportRequest transReq = (TransportRequest) iterator.next();
					
					// logging
					TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
					transLog.info(engRequestId, "DELETE_TRANS_REQUEST", "id=" + transReq.getId());
					
					dao.deleteOBRequest(transReq.getId());
					dao.deleteOBRequestStatus(transReq.getId());
					dao.deleteOBRequestVehicle(transReq.getId());
				}
			}
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error deleteOBVanRecords "+e);
			throw new RuntimeException("DAO Error");
		}
	}
	 
	 public Collection selectVehiclesByAssignmentId(String id) throws DaoException {

		 TransportDao dao = (TransportDao) getDao();
		 return dao.selectVehiclesByAssignmentId(id);

		}
	 
	 public void insertDriverVehicle(DriverVehicleObject dvObject) throws DaoException {
		 
		 TransportDao dao = (TransportDao) getDao();
		 dao.insertDriverVehicle(dvObject);
	 }
	 
	 public DriverVehicleObject selectDriverVehicle(String userId, String assignmentId)throws DaoException {
		 
		 TransportDao dao = (TransportDao) getDao();
		 return dao.selectDriverVehicle(userId, assignmentId);
	 }
	 
	 public Collection selectAssignments(String assgId, String status)	throws DaoException {
		 TransportDao dao = (TransportDao) getDao();
		 return dao.selectAssignments(assgId, status);
	 }
	 
	 /**
	  * Get all the information to create ABW transport transfer cost object(s). 
	 * @return
	 */
	public Collection<AbwTransferCostObject> getInfoToCreateAbwTransferCostObjects(){
		 try{
			 TransportDao dao = (TransportDao) getDao();
			 return dao.getInfoToCreateAbwTransferCostObjects();
		 }
		 catch(DaoException e){
			 Log.getLog(getClass()).error(e, e);
			 return new ArrayList<AbwTransferCostObject>();
		 }
	 }
	
	public Collection<AbwTransferCostObject> getInfoToCreateAbwTransferCostObjects(Date dateFrom, Date dateTo){
		 try{
			 TransportDao dao = (TransportDao) getDao();
			 return dao.getInfoToCreateAbwTransferCostObjects(dateFrom, dateTo);
		 }
		 catch(DaoException e){
			 Log.getLog(getClass()).error(e, e);
			 return new ArrayList<AbwTransferCostObject>();
		 }
	 }
	
	public String getAbwCost(String id, String categoryId, String type)throws DaoException {
				
		Date startDate = null;
		Date endDate = null;
		String fromTime = null;
		String toTime = null;
		String blockBooking = null;
		
		String abwCost = "0";
		TransportRequest transreq = new TransportRequest();
		SimpleDateFormat stf = new SimpleDateFormat("k:mm");
		
		try{
			transreq = getTransportRequest(id);			
		}catch(Exception er){
			
		}
		
		if(transreq.getId() != null){
			try{						
				startDate = DateUtil.getDateFromStartTime(transreq.getStartDate());
				endDate = DateUtil.getDateFromStartTime(transreq.getEndDate());
				fromTime = stf.format(transreq.getStartDate());
				toTime = stf.format(transreq.getEndDate());
				blockBooking = transreq.getBlockBooking();
			}catch(Exception er){
				
			}
		}
	
		abwCost = getTransportRate(id, startDate, endDate, fromTime, toTime, blockBooking, categoryId, type);	
		
		
		
		return abwCost;
		
	}
	
	private String getTransportRate(String id, Date startDate,Date endDate,String fromTime,String toTime, String blockBooking, String categoryId, String type)throws DaoException {
		
		Calendar c1 = Calendar.getInstance();
		RateCardObject rateObj = new RateCardObject();
		TransportModule transModule = (TransportModule) Application.getInstance().getModule(TransportModule.class);		
		double totalRate = 0;
		int totalHour = 0;
		VehicleRequest vr = new VehicleRequest();
		 TransportDao dao = (TransportDao) getDao();
		
		if(startDate != null && endDate != null){
			long diffdate = dateDiff4Assignment(startDate, endDate);      
			int hour = 0;
			for(int i = 0; i <= diffdate; i++){			
				c1.setTime(startDate);
				c1.add(Calendar.DATE, i);
				Date bookDate = c1.getTime();
				if("1".equals(blockBooking))
					hour = calculateBlockBookingHour(fromTime, toTime);
				else{
					hour = calculateHour(bookDate, startDate, endDate, fromTime, toTime);
				}
				
				totalHour += hour;
			}					
			
			if("D".equals(type)){				
				int driverQuantity = dao.selectQuantityDriver(id, categoryId);	
				rateObj = new RateCardObject();
				rateObj = transModule.selectRateCardByNameDate("Driver", startDate);
				if (!(null == rateObj)) {
					double amount = Double.parseDouble(rateObj.getAmount());						
					totalRate = amount * totalHour * driverQuantity;
				}
			}else{
				String categoryName = dao.getCategoryName(categoryId);
				int vehicleQuantity = dao.selectQuantityVehicle(id, categoryId);
				rateObj = new RateCardObject();
				rateObj = transModule.selectRateCardByNameDate(categoryName, startDate);
				if (!(null == rateObj)) {
					double amount = Double.valueOf(rateObj.getAmount());							
					totalRate = amount * totalHour * vehicleQuantity;
				}
			}
			
		}
		
		return String.valueOf(totalRate);
	}

		
	public long dateDiff4Assignment(Date start, Date end){		 
		long diff = Math.round((end.getTime() - start.getTime()) / (DateDiffUtil.MS_IN_A_DAY));			
		return diff;
	}	
	
	public int calculateBlockBookingHour(String fromTime,String toTime){
		
		int hour = 0;				
			DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
		    df.setTimeZone(TimeZone.getTimeZone("GMT"));			 		    
		    //Change 00:00 to 24:00
			if("00:00".equals(toTime)){
				toTime = "24:00";
			}
		    try{
		    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
		    	String strToday =  new SimpleDateFormat("yyyy-MM-dd ").format(new Date());		   	
		    	Date s = sdf.parse(strToday + fromTime);
	    		Date e = sdf.parse(strToday + toTime);
	    		long hours = (e.getTime() - s.getTime())/60000;
	    		hour = (int)(hours/60);		    				    	
		    }catch(Exception er){
		    	Log.getLog(getClass()).error(er);
		    }			    			   
		
		return hour;
	}
	
	public int calculateHour(Date currentDate, Date startDate, Date endDate, String fromTime,String toTime){
		int hour = 0;
		
		int startDiff = currentDate.compareTo(startDate);
		int endDiff = currentDate.compareTo(endDate);
		//Change 00:00 to 24:00
		if("00:00".equals(toTime)){
			toTime = "24:00";
		}
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
			String strToday =  new SimpleDateFormat("yyyy-MM-dd ").format(new Date());
			Date s = sdf.parse(strToday + fromTime);
    		Date e = sdf.parse(strToday + toTime);
    		Date endTime = sdf.parse(strToday + "24:00");    		
    	    	    	
			if(startDiff == 0 && endDiff == 0){
				long hours = (e.getTime() - s.getTime())/60000;
	    		hour = (int)(hours/60);	    		
				
			}else if(startDiff == 0){
				long hours = (endTime.getTime() - s.getTime())/60000;
	    		hour = (int)(hours/60);
				
			}else if(startDiff > 0 && endDiff < 0){
				hour = 24;
				
			}else if(endDiff == 0){				
				Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
	    		calendar.setTime(e);   // assigns calendar to given date 
	    		hour = calendar.get(Calendar.HOUR_OF_DAY);				
			}
		}catch(Exception er){
			Log.getLog(getClass()).error(er, er);
		}
		
		return hour;
	}
	
	public String getRateDriver(String id, Date startDate,Date endDate,String fromTime,String toTime, String blockBooking){
		
		Calendar c1 = Calendar.getInstance();
		RateCardObject rateObj = new RateCardObject();
		double totalDriver = 0;
		double total = 0;
		int totalHour = 0;
		VehicleRequest vr = new VehicleRequest();
		
		
		if(startDate != null && endDate != null){
			long diffdate = dateDiff4Assignment(startDate, endDate);      
			int hour = 0;
			for(int i = 0; i <= diffdate; i++){			
				c1.setTime(startDate);
				c1.add(Calendar.DATE, i);
				Date bookDate = c1.getTime();
				if("1".equals(blockBooking))
					hour = calculateBlockBookingHour(fromTime, toTime);
				else{
					hour = calculateHour(bookDate, startDate, endDate, fromTime, toTime);
				}
				
				totalHour += hour;
			}
			
			try {
				Collection vRequest = getVehicles(id);
				//request = transModule.getVehicles(id); // get how many vehicle & driver
				for (Iterator it = vRequest.iterator(); it.hasNext();) {
					vr = (VehicleRequest) it.next();					
					// rate for driver
					rateObj = new RateCardObject();
					totalDriver = 0;
					rateObj = selectRateCardByNameDate("Driver", startDate);
					if (!(null == rateObj)) {
						double amount = Double.parseDouble(rateObj.getAmount());
						totalDriver = amount * vr.getDriver();						
					}				
					total += totalDriver;
				}
				
				total = total * totalHour;
				
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.getMessage(), e);
			}
			
		}
		
		return String.valueOf(total);
	}
	
	public String getRateFacility(String id, Date startDate,Date endDate,String fromTime,String toTime, String blockBooking){
		
		Calendar c1 = Calendar.getInstance();
		RateCardObject rateObj = new RateCardObject();
		double totalFacility = 0;
		double total = 0;
		int totalHour = 0;
		VehicleRequest vr = new VehicleRequest();
		
		
		if(startDate != null && endDate != null){
			long diffdate = dateDiff4Assignment(startDate, endDate);      
			int hour = 0;
			for(int i = 0; i <= diffdate; i++){			
				c1.setTime(startDate);
				c1.add(Calendar.DATE, i);
				Date bookDate = c1.getTime();
				if("1".equals(blockBooking))
					hour = calculateBlockBookingHour(fromTime, toTime);
				else{
					hour = calculateHour(bookDate, startDate, endDate, fromTime, toTime);
				}
				
				totalHour += hour;
			}
			
			try {
				Collection vRequest = getVehicles(id);				
				for (Iterator it = vRequest.iterator(); it.hasNext();) {					
					vr = (VehicleRequest) it.next();		
					if("H".equals(vr.getType())){
						rateObj = new RateCardObject();
						totalFacility = 0;
						rateObj = selectRateCardByNameDate(vr.getName(), startDate);
						if (!(null == rateObj)) {
							double amount = Double.valueOf(rateObj.getAmount());
							totalFacility = amount * vr.getQuantity();						
						}
						total += totalFacility;
					}
				}							
				total = total * totalHour;
				
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.getMessage(), e);
			}
			
		}
		
		return String.valueOf(total);
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
	
	protected Date getDateToEndTime(Date xDate){
    	
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(xDate);
        endTime.set(Calendar.HOUR_OF_DAY, 24);
        endTime.set(Calendar.MINUTE, 00);
        endTime.set(Calendar.SECOND, 00); 
        xDate = endTime.getTime();
        
        return xDate;      
    }
	
	public long dateDiff(Date start, Date end){
		 
		long diff = Math.round((end.getTime() - start.getTime()) / (DateDiffUtil.MS_IN_A_DAY));			
		return diff;
	}
	
	public Collection updateTransportRequestCost(Date dateFrom, Date dateTo){
		Collection colRet = new ArrayList();
		 try{
			 Calendar dateTo1 = Calendar.getInstance();
			 dateTo1.setTime(dateTo);
			 dateTo1.add(Calendar.DAY_OF_MONTH, 1);
			 
			 TransportDao dao = (TransportDao) getDao();
			 Collection col = dao.getTransportRequestListing(dateFrom, dateTo1.getTime());
			 
			 for(Iterator iter=col.iterator();iter.hasNext();){
				 TransportRequest tr= (TransportRequest)iter.next();
				 
				 Date startDate = tr.getStartDate();
				 Date endDate = tr.getEndDate();
				 
				Calendar start = Calendar.getInstance();
				Calendar end = Calendar.getInstance();


				start.setTime(startDate);

				end.setTime(endDate);
				 
				Date dateS = getDateFromStartTime(start.getTime());
				Date dateE = getDateToEndTime(end.getTime());
				
				long days = dateDiff(dateS, dateE);
				Log.getLog(getClass()).info(days);
				
				DecimalFormat timeformat = new DecimalFormat("00");
				String timeStart = timeformat.format(startDate.getHours())+":"+timeformat.format(startDate.getMinutes()); 
				String timeEnd = timeformat.format(endDate.getHours())+":"+timeformat.format(endDate.getMinutes());
				String ratedriver = getRateDriver(tr.getId(), startDate, endDate, timeStart, timeEnd, tr.getBlockBooking());
				String rateVehicle = getRateFacility(tr.getId(), startDate, endDate, timeStart, timeEnd,tr.getBlockBooking());
				tr.setRateVehicle(rateVehicle);
				tr.setRate(ratedriver);
				
				tr.setUpdatedBy(Application.getInstance().getCurrentUser().getId());
				tr.setUpdatedDate(new Date());
				updateBackDatedTransportRequest(tr);
				colRet.add(tr);
			 }
			 return colRet;
		 }
		 catch(DaoException e){
			 Log.getLog(getClass()).error(e, e);
			 return colRet;
		 }
			
		}
	public void updateBackDatedTransportRequest(TransportRequest tr){
				try{
					TransportDao dao = (TransportDao) getDao();
					dao.updateBackDatedTransportRequest(tr);
				}catch(DaoException e){
					Log.getLog(getClass()).error("Error updateTransportRequest"+e);
					throw new RuntimeException("DAO Error");
				}
		}
		


}
