package com.tms.fms.facility.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultDataObject;
import kacang.model.DefaultModule;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import org.apache.axis.collections.SequencedHashMap;

import com.tms.fms.abw.model.AbwCodeObject;
import com.tms.fms.abw.model.AbwModule;
import com.tms.fms.department.model.FMSUnit;

/**
 * @author arunkumar
 *
 */
	public class SetupModule extends DefaultModule {
	
	public static String DATE_FORMAT="dd MMM yyyy"; 
	public static String DATE_TIME_FORMAT="dd MMM yyyy hh:mm aa"; 
	public static final String INDICATOR_FACILITIES="F";
	public static final String INDICATOR_MANPOWER="M";
	public static final String FMS_SYSTEM_ADMIN="FMS System Administrator";
	
	public void insertWorkingProfile(WorkingProfile wp){
		SetupDao dao=(SetupDao)getDao();
		try {
			wp.setWorkingProfileId(UuidGenerator.getInstance().getUuid());
			wp.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			wp.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			wp.setCreatedOn(now);
			wp.setModifiedOn(now);
			dao.insertWorkingProfile(wp);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void updateWorkingProfile(WorkingProfile wp){
		SetupDao dao=(SetupDao)getDao();
		try {
			wp.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			wp.setModifiedOn(now);
			dao.updateWorkingProfile(wp);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public Collection getWorkingProfile(){
		SetupDao dao=(SetupDao)getDao();
		Collection col= new ArrayList();
		try {
			col= dao.selectWorkingProfile(null, null, false, 0, -1);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}

	public WorkingProfile getWorkingProfile(String workingProfileId){
		SetupDao dao=(SetupDao)getDao();
		try {
			return dao.selectWorkingProfile(workingProfileId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}
	
	public void deleteWorkingProfile(String workingProfileId){
		SetupDao dao=(SetupDao)getDao();
		try {
			dao.deleteRecord("fms_working_profile","workingProfileId",workingProfileId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public boolean isDuplicate(String tableName , String columnName, String value, String excludeColumn,String excludeId){
		SetupDao dao=(SetupDao)getDao();
		try {
			return dao.isDuplicate(tableName,columnName,value,excludeColumn,excludeId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return false;
	}
	
	public Collection getUnitUsers(String search,String unitId,String unitHeadId, String groupId, String sort,boolean desc,int start,int rows){
		Collection col=new ArrayList();
		SetupDao dao=(SetupDao)getDao();
		try {
			col= dao.selectUnitUsers(search,unitId,unitHeadId, groupId,sort,desc,start,rows);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getUnits(String unitHeadId) {
		Collection col = new ArrayList();
		SetupDao dao = (SetupDao) getDao();
		try {
			col = dao.selectUnits(unitHeadId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		
		return col;
	}
	
	public String getCurrentUnits(String userId) {
		SetupDao dao = (SetupDao) getDao();
		try {
			return dao.selectCurrentUnits(userId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return null;
	}
	
	public Collection getUnitsForUnitHead(String unitHeadId) {
		Collection col = new ArrayList();
		SetupDao dao = (SetupDao) getDao();
		try {
			col = dao.selectUnitsForUnitHead(unitHeadId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		
		return col;
	}
	
	public void deleteWorkingProfileDurationManpower(String workingProfileDurationId, String manpowerId){
		SetupDao dao=(SetupDao)getDao();
		try {
			dao.deleteWorkingProfileDurationManpower(workingProfileDurationId, manpowerId);
			
			if (dao.countWorkingProfileDurationManpower(workingProfileDurationId)<=0){
				dao.deleteRecord("fms_working_profile_duration", "workingProfileDurationId", workingProfileDurationId);
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void deleteWorkingProfileDuration(String workingProfileDurationId){
		SetupDao dao=(SetupDao)getDao();
		try {
			dao.deleteRecord("fms_working_profile_duration","workingProfileDurationId",workingProfileDurationId);
			deleteDurationManpower(workingProfileDurationId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void deleteDurationManpower(String workingProfileDurationId){
		SetupDao dao=(SetupDao)getDao();
		try {
			dao.deleteRecord("fms_working_profile_duration_manpower","workingProfileDurationId",workingProfileDurationId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void insertWorkingProfileDuration(WorkingProfile wp){
		SetupDao dao=(SetupDao)getDao();
		try {
			wp.setWorkingProfileDurationId(UuidGenerator.getInstance().getUuid());
			
			wp.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			wp.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			wp.setCreatedOn(now);
			wp.setModifiedOn(now);
			dao.insertWorkingProfileDuration(wp);
			
			deleteDurationManpower(wp.getWorkingProfileDurationId());
			Map map=(Map)wp.getManpowerMap();
			for(Iterator itr=map.keySet().iterator();itr.hasNext();){
				String userId=(String)itr.next();
				dao.insertDurationManpower(wp.getWorkingProfileDurationId(), userId,
						wp.getStudio1(),
						wp.getStudio2(),
						wp.getStudio3(),
						wp.getStudio4(),
						wp.getStudio5(),
						wp.getStudio6(),
						wp.getStudio7(),
						wp.getStudio8());
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void updateWorkingProfileDuration(WorkingProfile wp){
		SetupDao dao=(SetupDao)getDao();
		try {
			wp.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			wp.setModifiedOn(now);
			dao.updateWorkingProfileDuration(wp);
			
			deleteDurationManpower(wp.getWorkingProfileDurationId());
			Map map=(Map)wp.getManpowerMap();
			for(Iterator itr=map.keySet().iterator();itr.hasNext();){
				String userId=(String)itr.next();
				dao.insertDurationManpower(wp.getWorkingProfileDurationId(), userId,
						wp.getStudio1(),
						wp.getStudio2(),
						wp.getStudio3(),
						wp.getStudio4(),
						wp.getStudio5(),
						wp.getStudio6(),
						wp.getStudio7(),
						wp.getStudio8());
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public WorkingProfile getWorkingProfileDuration(String workingProfileDurationId){
		SetupDao dao=(SetupDao)getDao();
		try {
			return dao.selectWorkingProfileDuration(workingProfileDurationId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}
	
	public Map getWorkingProfileDurationManpower(String workingProfileDurationId){
		SetupDao dao=(SetupDao)getDao();
		try {
			return dao.selectWorkingProfileDurationManpower(workingProfileDurationId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}
	
	
	
	public Collection selectManpowerSetup(String search, String unitApprover, String unitFilter, String sort,boolean desc,int start,int rows) throws DaoException{
		SetupDao dao=(SetupDao)getDao();
		Collection finalCol=new ArrayList();
		try {
			Collection col= new ArrayList(); //dao.selectManpowerSetup(search,sort,desc,start,rows);
			
			SetupModule module=(SetupModule)Application.getInstance().getModule(SetupModule.class);			
	    	Collection lstUnit = module.getUnits(unitApprover);
	    	 if (lstUnit.size() > 0) {
		    	for (Iterator i=lstUnit.iterator(); i.hasNext();) {
		        	FMSUnit o = (FMSUnit)i.next();
		        	
		        	if (unitFilter!=null){
		        		if(o.getId().equals(unitFilter)){
		        			col.addAll(dao.selectManpowerSetup(search,o.getId(),sort,desc,start,rows));
		        		}		        		
		        	} else {
		        		col.addAll(dao.selectManpowerSetup(search,o.getId(),sort,desc,start,rows));
		        	}		        	
		        }
		    }
	    	 
			
			String tempUserId="";
			String tempCom="";
			String tempComLevel="";
			HashMap newMap=new HashMap();
			int i=1;
			for(Iterator itr=col.iterator();itr.hasNext();i++){
				HashMap map=(HashMap)itr.next();
				if(!tempUserId.equals("") && !tempUserId.equals((String)map.get("userId"))){
					newMap.put("competencyName", tempCom);
					newMap.put("competencyLevel", tempComLevel);
					finalCol.add(newMap);
					tempUserId="";
					tempCom="";
					tempComLevel="";
					newMap=new HashMap();
				}
				if(tempUserId.equals("") || tempUserId.equals((String)map.get("userId"))){
					tempUserId=(String)map.get("userId");
					newMap.put("userId", tempUserId);
					newMap.put("firstName", (String)map.get("firstName"));
					newMap.put("manpowerName", (String)map.get("manpowerName"));
					if(tempCom.equals("")){
						tempCom+=(String)map.get("competencyName");
					}else{
						tempCom+=",<br>"+(String)map.get("competencyName");
					}
					if(tempComLevel.equals("")){
						tempComLevel+=(String)map.get("competencyLevel");
					}else{
						tempComLevel+=",<br>"+(String)map.get("competencyLevel");
					}
				}
				if(i==col.size()){
					newMap.put("competencyName", tempCom);
					newMap.put("competencyLevel", tempComLevel);
					finalCol.add(newMap);
				}
			}
			return finalCol;
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		
		return finalCol;
	}
	
	public int selectManpowerSetupCount(String search, String unitApprover, String unitFilter) throws DaoException{
		int count=0;
		try {
			count=selectManpowerSetup(search, unitApprover, unitFilter, null, false, 0, -1).size();
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		
		return count;
	}
	
	// rateCard
	public void insertRateCard(RateCard rc){
		SetupDao dao=(SetupDao)getDao();
		try {
			
			rc.setIdDetail(UuidGenerator.getInstance().getUuid());
			rc.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			rc.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			rc.setCreatedOn(now);
			rc.setModifiedOn(now);
			
			dao.insertRateCard(rc);
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void insertRateCardEquipment(String idEquipment, String id, int equipmentQty){
		SetupDao dao = (SetupDao)getDao();
		try{
			RateCard rc = new RateCard();
			rc.setIdEquipment(idEquipment);
			rc.setId(id);
			rc.setIdDetail(UuidGenerator.getInstance().getUuid());
			rc.setEquipmentQty(equipmentQty);
			rc.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			rc.setCreatedOn(now);
			rc.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			rc.setModifiedOn(now);
						
			// count existing equipment on the fms_rate_card_equipment table
			int countRce = dao.countRateCardEquipment(id, idEquipment); 
			
			// if any equipment selected more than once, so do update, not insert			
			if (countRce <=0){
				dao.insertRateCardEquipment(rc);
			} else {
				dao.updateRateCardEquipment(rc);
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
	}
	
	public void insertRateCardManpower(String idManpower, String id, int manpowerQty){
		SetupDao dao = (SetupDao)getDao();
		try{
			RateCard rc = new RateCard();
			rc.setIdManpower(idManpower);
			rc.setId(id);
			rc.setIdDetail(UuidGenerator.getInstance().getUuid());
			rc.setManpowerQty(manpowerQty);
			rc.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			rc.setCreatedOn(now);
			rc.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			rc.setModifiedOn(now);
			
			// count existing equipment on the fms_rate_card_equipment table
			int countRce = dao.countRateCardManpower(id, idManpower); 
			
			// if any equipment selected more than once, so do update, not insert			
			if (countRce <=0){
				dao.insertRateCardManpower(rc);
			} else {
				dao.updateRateCardManpower(rc);
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
	}
	
	public void insertRateCardDetail(RateCard rc){
		SetupDao dao=(SetupDao)getDao();
		try {
			rc.setIdDetail(UuidGenerator.getInstance().getUuid());
			rc.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			rc.setCreatedOn(now);
			dao.insertRateCardDetail(rc);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void updateRateCard(RateCard rc){
		try{
			SetupDao dao = (SetupDao) getDao();
			rc.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now = new Date();
			rc.setModifiedOn(now);
			
			dao.updateRateCard(rc);
			
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error updateRateCard(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void updateRateCard(String id, String qty){
		try{
			RateCard rc = new RateCard();
			SetupDao dao = (SetupDao) getDao();
			rc.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now = new Date();
			rc.setModifiedOn(now);
			rc.setIdEquipment(id);
			rc.setEquipmentQty(Integer.parseInt(qty));
			
			dao.updateRateCardEquipmentQty(rc);
			
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error updateRateCard(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void updateRateCardManpower(String id, String qty){
		try{
			RateCard rc = new RateCard();
			SetupDao dao = (SetupDao) getDao();
			rc.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now = new Date();
			rc.setModifiedOn(now);
			rc.setIdManpower(id);
			rc.setManpowerQty(Integer.parseInt(qty));
			
			dao.updateRateCardManpowerQty(rc);
			
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error updateRateCard(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void deleteRateCard(String id){
		try{
			SetupDao dao = (SetupDao) getDao();
			dao.deleteRateCard(id);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error deleteRateCard(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public Collection getRateCard(String name, String status, String serviceTypeId, String sort, boolean desc, int start, int rows){
		SetupDao dao=(SetupDao)getDao();		
		Collection col= new ArrayList();
		try {
			col= dao.selectRateCard(name, status, serviceTypeId, sort, desc, start, rows);
			for(Iterator itr=col.iterator();itr.hasNext();){
				RateCard rateCard=(RateCard)itr.next();
				String id=rateCard.getId();
				
				RateCard temp = new RateCard();
				temp = dao.selectRateCardDetail(id);
				
				rateCard.setInternalRate(temp.getInternalRate());
				rateCard.setExternalRate(temp.getExternalRate());
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public RateCard getRateCard(String id ){
		RateCard rc = new RateCard();
		try{
			SetupDao dao = (SetupDao) getDao();
			Collection rows = dao.selectRateCard(id);
			if (rows.size() > 0) {
				rc = (RateCard)rows.iterator().next();
			}
		} catch(DaoException e){
			Log.getLog(getClass()).error("Error getProgram(1)", e);
		}
		return rc;
	}
	
	public RateCard getRateCardDetail(String id ){
		RateCard rc = new RateCard();
		try{
			SetupDao dao = (SetupDao) getDao();
			rc = dao.selectRateCardDetail(id);
		} catch(DaoException e){
			Log.getLog(getClass()).error("Error getProgram(1)", e);
		}
		return rc;
	}
	
	public RateCard getRateCardByService(String serviceTypeId){
		RateCard rc = new RateCard();
		try{
			SetupDao dao = (SetupDao) getDao();
			rc = dao.selectRateCardByService(serviceTypeId);
		} catch(DaoException e){
			Log.getLog(getClass()).error("Error getProgram(1)", e);
		}
		return rc;
	}
	
	public Collection getRateCardAllEquipment(String search, String sort, boolean desc , int start, int rows){
		SetupDao dao=(SetupDao)getDao();
		Collection col= new ArrayList();
		try {
			col= dao.selectRateCardAllEquipment(search, sort, desc, start, rows);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	
	public Collection getRateCardEquipment(String id, String idEquipment){
		SetupDao dao=(SetupDao)getDao();
		Collection col= new ArrayList();
		try {
			col= dao.selectRateCardEquipment(id, idEquipment);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getRateCardEquipmentForChecking(String rateCardId) {
		SetupDao dao = (SetupDao) getDao();
		Collection col = new ArrayList();
		try {
			col = dao.selectRateCardEquipmentForChecking(rateCardId);
			for (Iterator iterator = col.iterator(); iterator.hasNext();) {
				RateCard rc = (RateCard) iterator.next();
				String categoryId = rc.getCategoryId();
				if (categoryId != null) {
					if (!isValidRateCardCategory(categoryId)) {
						rc.setStatus("(invalid)");
					}
				} else {
					rc.setStatus("(deleted)");
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return col;
	}
	
	public Collection getRateCardAllManpower(String search){
		SetupDao dao=(SetupDao)getDao();
		Collection col= new ArrayList();
		try {
			col= dao.selectRateCardAllManpower(search);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
		
	public Collection getRateCardManpower(String id, String idManpower){
		SetupDao dao=(SetupDao)getDao();
		Collection col= new ArrayList();
		try {
			col= dao.selectRateCardManpower(id, idManpower);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getRateCardManpowerForChecking(String rateCardId) {
		SetupDao dao = (SetupDao) getDao();
		Collection col = new ArrayList();
		try {
			col = dao.selectRateCardManpowerForChecking(rateCardId);
			for (Iterator iterator = col.iterator(); iterator.hasNext();) {
				RateCard rc = (RateCard) iterator.next();
				String competencyId = rc.getCompetencyId();
				if (competencyId == null) {
					rc.setStatus("(deleted)");
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return col;
	}
	
	public Collection getRateCardHistory(String id){
		SetupDao dao=(SetupDao)getDao();
		Collection col= new ArrayList();
		try {
			col= dao.selectRateCardHistory(id);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public int countRateCard(String name, String status, String serviceTypeId){
		int countRateCard = 0;
		
		SetupDao dao = (SetupDao) getDao();
		try {
			return dao.selectRateCardCount(name, status, serviceTypeId);
		} catch (DaoException e){
			return countRateCard;
		}
	}
	
	public int countRateCardHistory(String id){
		int countRch = 0;
		
		SetupDao dao = (SetupDao) getDao();
		try {
			return dao.countRateCardHistory(id);
		} catch (DaoException e){
			return countRch;
		}
	}
	
	public int countRateCardAllEquipment(String search){
		int countRceAll = 0;
		
		SetupDao dao = (SetupDao) getDao();
		try {
			return dao.countRateCardAllEquipment(search);
		} catch (DaoException e){
			return countRceAll;
		}
	}
	
	public int countRateCardEquipment(String id, String idEquipment){
		int countRce = 0;
		
		SetupDao dao = (SetupDao) getDao();
		try {
			return dao.countRateCardEquipment(id, idEquipment);
		} catch (DaoException e){
			return countRce;
		}
	}
	
	public int countRateCardAllManpower(String search){
		int countRcmAll = 0;
		
		SetupDao dao = (SetupDao) getDao();
		try {
			return dao.countRateCardAllManpower(search);
		} catch (DaoException e){
			return countRcmAll;
		}
	}
	
	public int countRateCardManpower(String id, String idManpower){
		int countRcm = 0;
		
		SetupDao dao = (SetupDao) getDao();
		try {
			return dao.countRateCardManpower(id, idManpower);
		} catch (DaoException e){
			return countRcm;
		}
	}
	
	public void deleteRateCardEquipment(String id, String idEquipment){
		try{
			SetupDao dao = (SetupDao) getDao();
			dao.deleteRateCardEquipment(id, idEquipment);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error deleteRateCardEquipment(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void deleteRateCardManpower(String id, String idManpower){
		try{
			SetupDao dao = (SetupDao) getDao();
			dao.deleteRateCardManpower(id, idManpower);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error deleteRateCardManpower(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	// Rate Card - Item Category
	public RateCard getRateCardCategory(String id){
		RateCard rc = new RateCard();
		try{
			SetupDao dao = (SetupDao) getDao();
			rc = dao.selectRateCardCategoryById(id);
		} catch(DaoException e){
			Log.getLog(getClass()).error("Error getRateCardCategory(1)", e);
		}
		return rc;
	}
	
	public RateCard getRateCardCheckAvailability(String id){
		RateCard rc = new RateCard();
		try{
			SetupDao dao = (SetupDao) getDao();
			rc = dao.selectRateCardCategoryByIdCheck(id);
		} catch(DaoException e){
			Log.getLog(getClass()).error("Error getRateCardCategory2(1)", e);
		}
		return rc;
	}
	
	public Collection getRateCardCategory(String name, String sort, boolean desc, int start, int rows){
		SetupDao dao=(SetupDao)getDao();		
		Collection col= new ArrayList();
		try {
			col= dao.selectRateCardCategory(name, sort, desc, start, rows);
			for(Iterator itr=col.iterator();itr.hasNext();){
				RateCard rateCard=(RateCard)itr.next();
				String idCategory = rateCard.getIdCategory();
				
				Collection items = dao.selectRateCardCategoryDetail(idCategory, "");
				rateCard.setCategoryItems(items);			
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getRateCardAllCategory(String name, String sort, boolean desc, int start, int rows){
		SetupDao dao=(SetupDao)getDao();		
		Collection col= new ArrayList();
		try {
			col= dao.selectRateCardCategory(name, sort, desc, start, rows);
			for(Iterator itr=col.iterator();itr.hasNext();){
				RateCard rateCard=(RateCard)itr.next();
				String idCategory = rateCard.getIdCategory();
				
				int qty = dao.selectRateCardSumQty(idCategory);
				rateCard.setEquipmentQty(qty);
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getRateCardCategoryItems(String categoryId, String operator){
		SetupDao dao=(SetupDao)getDao();
		Collection col= new ArrayList();
		try {
			col= dao.selectRateCardCategoryDetail(categoryId, operator);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public Collection getFacility(String facilityId, String operator){
		SetupDao dao=(SetupDao)getDao();
		Collection col= new ArrayList();
		try {
			col= dao.selectFacility(facilityId, operator);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}
	
	public int countRateCardCategory(String name){
		int countRateCard = 0;
		
		SetupDao dao = (SetupDao) getDao();
		try {
			return dao.selectRateCardCategoryCount(name);
		} catch (DaoException e){
			return countRateCard;
		}
	}
	
	public int countRateCardAllCategory(String name){
		int countRateCard = 0;
		
		SetupDao dao = (SetupDao) getDao();
		try {
			return dao.selectRateCardCategoryCount(name);
		} catch (DaoException e){
			return countRateCard;
		}
	}
	
	public void deleteRateCardCategory(String id){
		try{
			SetupDao dao = (SetupDao) getDao();
			dao.deleteRateCardCategory(id);
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error deleteRateCard(1)", e);
			throw new RuntimeException("DAO Error");
		}
	}
	
	public void insertRateCardCategory(RateCard rc){
		SetupDao dao=(SetupDao)getDao();
		try {
			rc.setCreatedBy(Application.getInstance().getCurrentUser().getUsername());
			rc.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			rc.setCreatedOn(now);
			rc.setModifiedOn(now);
			
			dao.insertRateCardCategory(rc);
			
			//Collection c = rc.getCategoryItems();
			//String[] facilityIds = (String[])c.toArray();
			String[] facilityIds = rc.getEquipments();
			for (int i=0; i<facilityIds.length; i++){
				rc.setIdEquipment(facilityIds[i]);
				dao.insertCategoryItems(rc);
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public void updateRateCardCategory(RateCard rc){
		SetupDao dao=(SetupDao)getDao();
		try {
			rc.setModifiedBy(Application.getInstance().getCurrentUser().getUsername());
			Date now=new Date();
			rc.setModifiedOn(now);
			
			dao.updateRateCardCategory(rc);
			dao.deleteRateCardCategoryItems(rc.getIdCategory());
			
//			Collection c = rc.getCategoryItems();
//			String[] facilityIds = (String[])c.toArray();
//			for (int i=0; i<facilityIds.length; i++){
//				Log.getLog(getClass()).info(" ooo " + facilityIds[i]);
//				rc.setIdEquipment(facilityIds[i]);
//				dao.insertCategoryItems(rc);
//			}
			String[] facilityIds = rc.getEquipments();
			for (int i=0; i<facilityIds.length; i++){
				rc.setIdEquipment(facilityIds[i]);
				dao.insertCategoryItems(rc);
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}
	
	public String getWorkingProfileCode(String wpName) throws DaoException {
		SetupDao dao=(SetupDao)getDao();
		return dao.getCodeByWorkingProfileName(wpName);
	}
	
	public Collection selectWorkingProfile(String userId, String wpName, Date startDate, Date endDate) throws DaoException {
		SetupDao dao=(SetupDao)getDao();
		return dao.selectWorkingProfile(userId, wpName, startDate, endDate);
	}
	
	public Collection selectWorkingProfileForUpdate(String userId, Date startDate, Date endDate) throws DaoException {
		SetupDao dao=(SetupDao)getDao();
		return dao.selectWorkingProfileForUpdate(userId, startDate, endDate);
	}
	
	public Collection selectWorkingProfileForUpdate(String userId, String wpdId, Date startDate, Date endDate) throws DaoException {
		SetupDao dao=(SetupDao)getDao();
		return dao.selectWorkingProfileForUpdate(userId, wpdId, startDate, endDate);
	}
	
	public String getStudio(String userId, String studioName){
		SetupDao dao=(SetupDao)getDao();
		
		return dao.isStudioExist(userId, studioName);
	}
	
	
	/***** rate card checking *****/
	public boolean requestHasValidRateCards(String requestId) {
		SetupDao dao = (SetupDao) getDao();
		
		Collection col = dao.listRequestRateCard(requestId);
		if (col != null) {
			for (Iterator iterator = col.iterator(); iterator.hasNext();) {
				HashMap map = (HashMap) iterator.next();
				String rateCardId = (String) map.get("rateCardId");
				
				// check for invalid & inactive rate cards
				if (!isValidRateCard(rateCardId, true)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean isValidRateCard(String rateCardId) {
		return isValidRateCard(rateCardId, false);
	}
	
	public boolean isValidRateCard(String rateCardId, boolean checkInactive) {
		SetupDao dao = (SetupDao) getDao();
		boolean atLeastOne = false;
			
		// check rate card record
		HashMap rateCard = dao.checkRateCard(rateCardId);
		if (rateCard == null) {
			Log.getLog(getClass()).warn("Rate Card missing rateCardId=" + rateCardId);
			return false;
		}
		
		// check rate card not inactive
		if (checkInactive) {
			String status = (String) rateCard.get("status");
			if (status.equals("0")) {
				Log.getLog(getClass()).warn("Rate Card inactive rateCardId=" + rateCardId);
				return false;
			}
		}
		
		// check equipment
		Collection colEquipment = dao.checkRateCardEquipment(rateCardId);
		if (colEquipment.size() > 0) {
			atLeastOne = true;
			
			// check validity of each equipment
			if (!isValidRateCardEquipment(colEquipment)) {
				return false;
			}
		}
		
		// check manpower
		Collection colManpower = dao.checkRateCardManpower(rateCardId);
		if (colManpower.size() > 0) {
			atLeastOne = true;
			
			// check validity of each manpower
			if (!isValidRateCardManpower(colManpower)) {
				return false;
			}
		}
		
		// NOTE: no need to check transport request
		
		if (atLeastOne) {
			return true;
		}
		return false;
	}
	
	private boolean isValidRateCardEquipment(Collection colEquipment) {
		if (colEquipment.size() > 0) {
			for (Iterator iterator = colEquipment.iterator(); iterator.hasNext();) {
				HashMap map = (HashMap) iterator.next();
				String rateCardId = (String) map.get("rateCardId");
				String categoryId = (String) map.get("categoryId");
				String itemCategory = (String) map.get("itemCategory");
				String facilityStatus = (String) map.get("facilityStatus");
				
				// check rate card category table
				if (categoryId == null) {
					Log.getLog(getClass()).warn("Record missing from fms_eng_rate_card_category for rateCardId=" + rateCardId);
					return false;
				}
				
				// check rate card item table
				if (itemCategory == null) {
					Log.getLog(getClass()).warn("Record missing from fms_eng_rate_card_cat_item for rateCardId=" + rateCardId);
					return false;
				}
				
				// check facilityStatus
				if (facilityStatus == null) {
					Log.getLog(getClass()).warn("Record missing from fms_facility for rateCardId=" + rateCardId);
					return false;
				} else if (!facilityStatus.equals("1")) {
					Log.getLog(getClass()).warn("Facility is inactive for rateCardId=" + rateCardId);
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isValidRateCardManpower(Collection colManpower) {
		if (colManpower.size() > 0) {
			for (Iterator iterator = colManpower.iterator(); iterator.hasNext();) {
				HashMap map = (HashMap) iterator.next();
				String rateCardId = (String) map.get("rateCardId");
				String competencyId = (String) map.get("competencyId");
				
				// check competency
				if (competencyId == null) {
					Log.getLog(getClass()).warn("Record missing from competency for rateCardId=" + rateCardId);
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isValidRateCardCategory(String rateCardCategoryId) {
		SetupDao dao = (SetupDao) getDao();
		
		Collection col = dao.checkRateCardCategory(rateCardCategoryId);
		if (col != null) {
			if (col.size() > 0) {
				for (Iterator iterator = col.iterator(); iterator.hasNext();) {
					HashMap map = (HashMap) iterator.next();
					String facilityStatus = (String) map.get("facilityStatus");
					
					// check facilityStatus
					if (facilityStatus == null) {
						return false;
					} else if (!facilityStatus.equals("1")) {
						return false;
					}
				}
				
				return true;
			}
		}
		return false;
	}
	
	public Collection getRateCardCategoryFacility(String rateCardCategoryId) {
		SetupDao dao = (SetupDao) getDao();
		
		return dao.checkRateCardCategory(rateCardCategoryId);
	}
	
	public boolean canInactiveRateCard(String rateCardId, String serviceId) {
		SetupDao dao = (SetupDao) getDao();
		
		if (!dao.isRateCardUsedInFuture(rateCardId, serviceId)) {
			return true;
		}
		return false;
	}
	
	public int getTotalFacility(String requestId, String times, String facilityId, boolean today)throws DaoException {
		int total = 0;
		
		SetupDao dao = (SetupDao) getDao();
		total = dao.getTotalFacility(requestId, times, facilityId, today);
		
		return total;
	}
	
	public SequencedHashMap getAbwCode()
	{
		//ABW Code Setup
		SetupDao dao= (SetupDao)getDao();
		try {
			Collection col=dao.selectABWCode();
			SequencedHashMap abwCodeMap = new SequencedHashMap();
			for(Iterator itr=col.iterator();itr.hasNext();){
				AbwCodeObject object=(AbwCodeObject)itr.next();
				abwCodeMap.put(object.getAbw_code(), object.getDescription());
			}
			return abwCodeMap;
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
			return null;
		}
	}
	
	public void insertAbwCode(DefaultDataObject abwObj)
	{
		SetupDao dao = (SetupDao) getDao();
		try
		{
			dao.insertAbwCode(abwObj);
		}
		catch(Exception e)
		{
			Log.getLog(getClass()).error(" error @ AbwModule.insertAbwCode(1) : " + e, e);
		}
	}
	
	public Collection getAbwCodes(String abwCode)
	{
		SetupDao dao = (SetupDao) getDao();
		try
		{
			return dao.getAbwCodes(abwCode, null, false, null, 0, -1);
		}
		catch(Exception e)
		{
			Log.getLog(getClass()).error(" error @ AbwModule.getAbwCodes(1) : " + e, e);
			return new ArrayList();
		}
	}
	
	public void updateAbwCode(DefaultDataObject abwObj)
	{
		SetupDao dao = (SetupDao) getDao();
		try
		{
			dao.updateAbwCode(abwObj);
		}
		catch(Exception e)
		{
			Log.getLog(getClass()).error(" error @ AbwModule.updateAbwCode(1) : " + e, e);
		}
	}
	
	public void deleteAbwCode(String abwCode)
	{
		SetupDao dao = (SetupDao) getDao();
		try
		{
			dao.deleteAbwCode(abwCode);
		}
		catch(Exception e)
		{
			Log.getLog(getClass()).error(" error @ AbwModule.deleteAbwCode(1) : " + e, e);
		}
	}

	public Collection getAbwCodes(String keyword, boolean desc, String sort,
			int start, int rows)
	{
		SetupDao dao = (SetupDao) getDao();
		try
		{
			return dao.getAbwCodes(null, keyword, desc, sort, start, rows);
		}
		catch(Exception e)
		{
			Log.getLog(getClass()).error(" error @ AbwModule.getAbwCodes(6) : " + e, e);
			return new ArrayList();
		}
	}

	public int getAbwCodesCount(String keyword)
	{
		SetupDao dao = (SetupDao) getDao();
		try
		{
			return dao.getAbwCodesCount(null, keyword);
		}
		catch(Exception e)
		{
			Log.getLog(getClass()).error(" error @ AbwModule.getAbwCodes(1) : " + e, e);
			return 0;
		}
	}

	public void pushToAbwServer(RateCard rc)
	{
		AbwModule am = (AbwModule) Application.getInstance().getModule(AbwModule.class);
		
		RateCard rcDetails = getRateCardDetail(rc.getId());
		if(rcDetails != null)
		{
			rc.setInternalRate(rcDetails.getInternalRate());
		}
		
		rc.setProperty("indicator", SetupModule.INDICATOR_FACILITIES);
		if(rc.getServiceTypeId() != null && rc.getServiceTypeId().equals("4"))
		{
			rc.setProperty("indicator", SetupModule.INDICATOR_MANPOWER);
		}
		
		rc.setProperty("uniqueId", UuidGenerator.getInstance().getUuid());
		rc.setCreatedBy(SetupModule.FMS_SYSTEM_ADMIN);
		rc.setCreatedOn(new Date());
		am.insertRateCard(rc);
	}

	public boolean isAbwCodeInUse(String abwCode)
	{
		SetupDao dao = (SetupDao) getDao();
		try
		{
			return dao.isAbwCodeInUse(abwCode);
		}
		catch(Exception e)
		{
			Log.getLog(getClass()).error(" error @ AbwModule.isAbwCodeInUse(1) : " + e, e);
			throw new RuntimeException(e);
		}
	}
	
	public void insertRateCardEmailNotification(DefaultDataObject obj)
	{
		SetupDao dao = (SetupDao) getDao();
		try
		{
			RateCard rcDetails = getRateCardDetail(obj.getId());
			if(rcDetails != null)
			{
				obj.setProperty("internalRate", rcDetails.getInternalRate());
			}
			obj.setProperty("createdDate", new Date());
			
			dao.insertRateCardEmailNotification(obj);
		}
		catch(Exception e)
		{
			Log.getLog(getClass()).error(" error @ AbwModule.insertRateCardEmailNotification(1) : " + e, e);
		}
	}

	public Collection getRateCardEmailNotification(Date currDateTime)
	{
		SetupDao dao = (SetupDao) getDao();
		try
		{
			String dateCreatedStart = "";
			String dateCreatedEnd = "";
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try
			{
				Calendar calS = Calendar.getInstance();
				calS.setTime(currDateTime);
				calS.add(Calendar.DAY_OF_MONTH, -1);
				dateCreatedStart = sdf.format(calS.getTime());
				
				dateCreatedEnd = sdf.format(currDateTime);
			}
			catch(Exception e){}
			
			return dao.getRateCardEmailNotification(dateCreatedStart, dateCreatedEnd);
		}
		catch(Exception e)
		{
			Log.getLog(getClass()).error(" error @ AbwModule.getRateCardEmailNotification(1) : " + e, e);
			return null;
		}
	}

	public void clearRateCardEmailNotification(Date currDateTime)
	{
		SetupDao dao = (SetupDao) getDao();
		try
		{
			String dateCreatedStart = "";
			String dateCreatedEnd = "";
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try
			{
				Calendar calS = Calendar.getInstance();
				calS.setTime(currDateTime);
				calS.add(Calendar.DAY_OF_MONTH, -1);
				dateCreatedStart = sdf.format(calS.getTime());
				
				dateCreatedEnd = sdf.format(currDateTime);
			}
			catch(Exception e){}
			dao.clearRateCardEmailNotification(dateCreatedStart, dateCreatedEnd);
		}
		catch(Exception e)
		{
			Log.getLog(getClass()).error(" error @ AbwModule.clearRateCardEmailNotification(1) : " + e, e);
		}
	}
	
}
