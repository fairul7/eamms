package com.tms.fms.engineering.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.util.Log;

import com.tms.fms.facility.model.RateCard;

public class CheckAvailabilityDao extends DataSourceDao{
	public void init() {
		
	}
	
	public FacilityObject selectFacilityBookingById(String id) throws DaoException {
		String sql = "SELECT " +
					"fb.id AS bookingId, fb.requestId AS requestId, fb.quantity AS quantityBooked, " +
					"fb.bookFrom AS bookFrom, fb.bookTo AS bookTo, " +
					"fb.timeFrom AS timeFromBooked, fb.timeTo AS timeToBooked, " +
					"req.title AS requestTitle, cat.name AS category_name " +
					"FROM fms_facility_booking fb " +
					"LEFT JOIN fms_eng_rate_card_category cat ON (fb.facilityId = cat.id) " +
					"INNER JOIN fms_eng_request req ON (fb.requestId = req.requestId) " +
					"WHERE fb.id = ?";
		
		Collection col = super.select(sql, FacilityObject.class, new Object[] {id}, 0, 1);
		
		try {
			return (FacilityObject) col.iterator().next();
		} catch (Exception e) {
		}
		
		return null;
	}
	
	public FacilityObject selectManpowerBookingById(String id) throws DaoException {
		String sql = "SELECT " +
					"fb.id AS bookingId, fb.requestId AS requestId, fb.quantity AS quantityBooked, " +
					"fb.bookFrom AS bookFrom, fb.bookTo AS bookTo, " +
					"fb.timeFrom AS timeFromBooked, fb.timeTo AS timeToBooked, " +
					"req.title AS requestTitle, comp.competencyName AS manpowerName " +
					"FROM fms_facility_booking fb " +
					"LEFT JOIN competency comp ON (fb.facilityId = comp.competencyId) " +
					"INNER JOIN fms_eng_request req ON (fb.requestId = req.requestId) " +
					"WHERE fb.id = ?";
		
		Collection col = super.select(sql, FacilityObject.class, new Object[] {id}, 0, 1);
		
		try {
			return (FacilityObject) col.iterator().next();
		} catch (Exception e) {
		}
		
		return null;
	}
	
	public Collection selectFacilityByDate(Date dateChecked, String timeFrom, String timeTo, String bookingType) throws DaoException {
		String sql = "SELECT " +
					"fb.id AS bookingId, fb.requestId AS requestId, fb.quantity AS quantityBooked, " +
					"fb.bookFrom AS bookFrom, fb.bookTo AS bookTo, " +
					"fb.timeFrom AS timeFromBooked, fb.timeTo AS timeToBooked, " +
					"cat.id AS id, req.title AS requestTitle " +
					"FROM fms_facility_booking fb INNER JOIN fms_eng_rate_card_category cat ON (fb.facilityId = cat.id) " +
					"INNER JOIN fms_eng_request req ON (fb.requestId = req.requestId) " +
					"WHERE (? BETWEEN fb.bookFrom AND fb.bookTo ) " +
					"AND (((? BETWEEN fb.timeFrom AND fb.timeTo) OR (? BETWEEN fb.timeFrom AND fb.timeTo)) " +
					"OR ('" + timeFrom + "' <= fb.timeFrom AND '" + timeTo + "' >= fb.timeTo))" +
					"AND (fb.bookingType = ?) ";
		return super.select(sql, FacilityObject.class, new Object[] {dateChecked, timeFrom, timeTo, bookingType}, 0, -1);
	}
	
	public Collection selectFacility(String requestId, Date dateChecked, String timeFrom, String timeTo, String bookingType, String idCategory) throws DaoException {
		
		ArrayList args = new ArrayList();
		
		String sql = "SELECT " +
					"fb.id AS bookingId, fb.requestId AS requestId, fb.quantity AS quantityBooked, " +
					"fb.bookFrom AS bookFrom, fb.bookTo AS bookTo, " +
					"fb.timeFrom AS timeFromBooked, fb.timeTo AS timeToBooked, " +
					"cat.id AS id, req.title AS requestTitle, cat.name AS category_name " +
					"FROM fms_facility_booking fb INNER JOIN fms_eng_rate_card_category cat ON (fb.facilityId = cat.id) " +
					"INNER JOIN fms_eng_request req ON (fb.requestId = req.requestId) " +
					"WHERE (? BETWEEN fb.bookFrom AND fb.bookTo ) " +
					"AND (((? BETWEEN fb.timeFrom AND fb.timeTo) OR (? BETWEEN fb.timeFrom AND fb.timeTo)) " +
					"OR ('" + timeFrom + "' <= fb.timeFrom AND '" + timeTo + "' >= fb.timeTo)) " +
					"AND (fb.bookingType = ?) " +
					"AND cat.id = ? ";
		
		//new Object[] {dateChecked, timeFrom, timeTo, bookingType, idCategory, requestId}
		args.add(dateChecked);
		args.add(timeFrom);
		args.add(timeTo);
		args.add(bookingType);
		args.add(idCategory);
		
		
		if(!(requestId == null || "".equals(requestId))){
			sql += "AND fb.requestId <> ? ";
			args.add(requestId);
		}
		
		return super.select(sql, FacilityObject.class, args.toArray(), 0, -1);
	}
	
	public Collection selectFacilityRequest(Date dateChecked, String timeFrom, String timeTo, String bookingType, String idCategory) throws DaoException {
		String sql = "SELECT " +
					"fb.requestId AS requestId, sum(fb.quantity) AS quantityBooked, " +
					"fb.bookFrom AS bookFrom, fb.bookTo AS bookTo, " +
					"fb.timeFrom AS timeFromBooked, fb.timeTo AS timeToBooked, " +
					"cat.id AS id, req.title AS requestTitle, cat.name AS category_name " +
					"FROM fms_facility_booking fb INNER JOIN fms_eng_rate_card_category cat ON (fb.facilityId = cat.id) " +
					"INNER JOIN fms_eng_request req ON (fb.requestId = req.requestId) " +
					"WHERE (? BETWEEN fb.bookFrom AND fb.bookTo ) " +
					"AND (((? BETWEEN fb.timeFrom AND fb.timeTo) OR (? BETWEEN fb.timeFrom AND fb.timeTo)) " +
					"OR ('" + timeFrom + "' <= fb.timeFrom AND '" + timeTo + "' >= fb.timeTo)) " +
					"AND (fb.bookingType = ?) " +
					"AND cat.id = ? " +
					"GROUP BY fb.requestId, fb.bookFrom, fb.bookTo, fb.timeFrom, fb.timeTo, " +
					"cat.id, req.title, cat.name " +
					"ORDER BY fb.timeFrom ASC ";
		return super.select(sql, FacilityObject.class, new Object[] {dateChecked, timeFrom, timeTo, bookingType, idCategory}, 0, -1);
	}
	
	public Collection selectFacilityRequests(Date dateChecked, String timeFrom, String timeTo, String bookingType, String idCategory) throws DaoException {
		String sql = "SELECT " +
					"fb.requestId AS requestId, fb.quantity AS quantityBooked, " +
					"fb.bookFrom AS bookFrom, fb.bookTo AS bookTo, " +
					"fb.timeFrom AS timeFromBooked, fb.timeTo AS timeToBooked, " +
					"cat.id AS id, req.title AS requestTitle, cat.name AS category_name " +
					"FROM fms_facility_booking fb INNER JOIN fms_eng_rate_card_category cat ON (fb.facilityId = cat.id) " +
					"INNER JOIN fms_eng_request req ON (fb.requestId = req.requestId) " +
					"WHERE (? BETWEEN fb.bookFrom AND fb.bookTo ) " +
					"AND (((? BETWEEN fb.timeFrom AND fb.timeTo) OR (? BETWEEN fb.timeFrom AND fb.timeTo)) " +
					"OR ('" + timeFrom + "' <= fb.timeFrom AND '" + timeTo + "' >= fb.timeTo)) " +
					"AND (fb.bookingType = ?) " +
					"AND cat.id = ? " +
					"GROUP BY fb.requestId, fb.quantity, fb.bookFrom, fb.bookTo, fb.timeFrom, fb.timeTo, " +
					"cat.id, req.title, cat.name " +
					"ORDER BY fb.timeFrom ASC ";
		return super.select(sql, FacilityObject.class, new Object[] {dateChecked, timeFrom, timeTo, bookingType, idCategory}, 0, -1);
	}
	public Collection selectAvailabilityManpowerByDate(Date dateChecked, String timeFrom, String timeTo, String bookingType) throws DaoException {
		String sql = "SELECT " +
					"fb.id AS bookingId, fb.requestId AS requestId, fb.quantity AS quantityBooked, " +
					"fb.bookFrom AS bookFrom, fb.bookTo AS bookTo, " +
					"fb.timeFrom AS timeFromBooked, fb.timeTo AS timeToBooked, " +
					"comp.competencyId AS id, req.title AS requestTitle " +
					"FROM fms_facility_booking fb INNER JOIN competency comp ON (fb.facilityId = comp.competencyId) " +
					"INNER JOIN fms_eng_request req ON (fb.requestId = req.requestId) " +
					"WHERE (? BETWEEN fb.bookFrom AND fb.bookTo ) " +
					"AND (((? BETWEEN fb.timeFrom AND fb.timeTo) OR (? BETWEEN fb.timeFrom AND fb.timeTo)) " +
					"OR ('" + timeFrom + "' <= fb.timeFrom AND '" + timeTo + "' >= fb.timeTo))" +
					"AND (fb.bookingType = ?) ";
		return super.select(sql, FacilityObject.class, new Object[] {dateChecked, timeFrom, timeTo, bookingType}, 0, -1);
	}
	
	public Collection selectAvailabilityManpower(Date dateChecked, String timeFrom, String timeTo, String bookingType, String competencyId) throws DaoException {
		String sql = "SELECT " +
					"distinct fb.requestId AS requestId, fb.quantity AS quantityBooked, " +
					"fb.bookFrom AS bookFrom, fb.bookTo AS bookTo, " +
					"fb.timeFrom AS timeFromBooked, fb.timeTo AS timeToBooked, " +
					"comp.competencyId AS id, req.title AS requestTitle " +
					"FROM fms_facility_booking fb INNER JOIN competency comp ON (fb.facilityId = comp.competencyId) " +
					"INNER JOIN fms_eng_request req ON (fb.requestId = req.requestId) " +
					"WHERE (? BETWEEN fb.bookFrom AND fb.bookTo ) " +
					"AND (" +
						"((? BETWEEN (CASE fb.timeFrom WHEN '0000' THEN '0001' ELSE fb.timeFrom END) " +
								"AND (CASE fb.timeTo WHEN '0000' THEN '2400' ELSE fb.timeTo END)) " +
							"OR (? BETWEEN (CASE fb.timeFrom WHEN '0000' THEN '0001' ELSE fb.timeFrom END)" +
								"AND (CASE fb.timeTo WHEN '0000' THEN '2400' ELSE fb.timeTo END))) " +
						"OR (? <= (CASE fb.timeFrom WHEN '0000' THEN '0001' ELSE fb.timeFrom END) " +
								"AND ? >= (CASE fb.timeTo WHEN '0000' THEN '2400' ELSE fb.timeTo END))" +
					") " +
					"AND (fb.bookingType = ?) " +
					"AND comp.competencyId = ? " +
					"ORDER BY fb.timeFrom ASC ";
		
		return super.select(sql, FacilityObject.class, new Object[] {dateChecked, timeFrom, timeTo, timeFrom, timeTo, bookingType, competencyId}, 0, -1);
	}

	public Collection selectAvailabilityManpowerWithBookingId(String requestId, Date dateChecked, String timeFrom, String timeTo, String bookingType, String competencyId) throws DaoException {
		
		ArrayList args = new ArrayList();
		String sql = "SELECT " +
					"fb.id AS bookingId, fb.requestId AS requestId, fb.quantity AS quantityBooked, " +
					"fb.bookFrom AS bookFrom, fb.bookTo AS bookTo, " +
					"fb.timeFrom AS timeFromBooked, fb.timeTo AS timeToBooked, " +
					"comp.competencyId AS id, req.title AS requestTitle " +
					"FROM fms_facility_booking fb INNER JOIN competency comp ON (fb.facilityId = comp.competencyId) " +
					"INNER JOIN fms_eng_request req ON (fb.requestId = req.requestId) " +
					"WHERE (? BETWEEN fb.bookFrom AND fb.bookTo ) " +
					"AND (" +
						"((? BETWEEN (CASE fb.timeFrom WHEN '0000' THEN '0001' ELSE fb.timeFrom END) " +
								"AND (CASE fb.timeTo WHEN '0000' THEN '2400' ELSE fb.timeTo END)) " +
							"OR (? BETWEEN (CASE fb.timeFrom WHEN '0000' THEN '0001' ELSE fb.timeFrom END)" +
								"AND (CASE fb.timeTo WHEN '0000' THEN '2400' ELSE fb.timeTo END))) " +
						"OR (? <= (CASE fb.timeFrom WHEN '0000' THEN '0001' ELSE fb.timeFrom END) " +
								"AND ? >= (CASE fb.timeTo WHEN '0000' THEN '2400' ELSE fb.timeTo END))" +
					") " +
					"AND (fb.bookingType = ?) " +
					"AND comp.competencyId = ? ";
					
					args.add(dateChecked);
					args.add(timeFrom);
					args.add(timeTo);
					args.add(timeFrom);
					args.add(timeTo);
					args.add(bookingType);
					args.add(competencyId);
					
					if(!(requestId == null || "".equals(requestId))){
						sql += "AND fb.requestId <> ? ";
						args.add(requestId);
					}
					
					sql += " ORDER BY fb.timeFrom ASC ";
		
		return super.select(sql, FacilityObject.class, args.toArray(), 0, -1);
	}
	
	public int selectRateCardSumQty(String idCategory) throws DaoException {
		int quantity = 0;
		//String sql = "SELECT SUM(f.quantity) AS equipmentQty " +
		//			"FROM fms_facility f INNER JOIN fms_eng_rate_card_cat_item i " +
		//			"ON (f.id = i.facilityId) WHERE i.categoryId = ? GROUP BY i.categoryId";
		
		String sql = "SELECT COUNT(*) AS equipmentQty " +
				"FROM fms_facility f " +
				"INNER JOIN fms_eng_rate_card_cat_item i ON (f.id = i.facilityId) " +
				"INNER JOIN fms_facility_item fi ON (f.id = fi.facility_id  AND (fi.status = '1' OR fi.status='C')) " + //-- commented due to issue #7626  
				"WHERE i.categoryId =? ";
		
		try {
			Collection col =  super.select(sql, HashMap.class, new String[] {idCategory}, 0, -1);
			if (col != null) {
				if (col.size() >0 ) {
					HashMap map = (HashMap) col.iterator().next();
					quantity = (Integer) map.get("equipmentQty");
				}
			}
		} catch (Exception e){
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		
		return quantity;
	}
	
	public Collection selectManpowerByCompetencyId(String competencyId, String departmentId) throws DaoException{
		String sql = "SELECT cu.userId AS manpowerId, u.firstName + ' ' + u.lastName AS manpowerName " +
					"FROM competency_user cu INNER JOIN security_user u ON (cu.userId = u.id)" +
					"WHERE cu.competencyId = ? " +
					"AND u.department = ? " ;
		
		return super.select(sql, FacilityObject.class, new Object[] {competencyId, departmentId}, 0, -1);
	}
	
	public Collection selectManpowerAvailable(Date dateChecked, String timeFrom, String timeTo) throws DaoException {
		String sql = "SELECT u.firstName +' '+ u.lastName AS manpowerName " +
				"FROM fms_working_profile wp INNER JOIN fms_working_profile_duration wpd ON (wpd.workingProfileId = wp.workingProfileId) " +
				"INNER JOIN fms_working_profile_duration_manpower wpdm ON (wpd.workingProfileDurationId = wpdm.workingProfileDurationId) " +
				"INNER JOIN security_user u ON (wpdm.userId = u.id) " +
				"WHERE 1=1 " +
				"AND (? BETWEEN wpd.startDate AND wpd.endDate) " +
				"AND (((? BETWEEN wp.startTime AND wp.endTime) OR (? BETWEEN wp.startTime AND wp.endTime)) " +
				"OR (? <= wp.startTime AND ? >= wp.endTime))" ;
		return super.select(sql, FacilityObject.class, new Object[] {dateChecked, timeFrom, timeTo, timeFrom, timeTo}, 0, -1);
	}
	
	public boolean isInNormalWorkingProfile(String timeFrom, String timeTo) throws DaoException {
		String sql = "SELECT * FROM fms_working_profile " +
					 "WHERE (? BETWEEN startTime AND endTime) AND (? BETWEEN startTime AND endTime) " +
					 "AND defaultProfile = '1'";
		
		boolean isInNormalWp = false; 
		try {
			Collection col = super.select(sql, HashMap.class, new Object[] {timeFrom, timeTo}, 0, -1);
			if (col != null && col.size()>0) {
				isInNormalWp = true;
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return isInNormalWp;
	}
	
	public int countManpowerAvailable(Date dateChecked, String timeFrom, String timeTo, String competencyId) throws DaoException {
		String sql = "SELECT count(*) AS COUNT " +
				"FROM fms_working_profile wp INNER JOIN fms_working_profile_duration wpd ON (wpd.workingProfileId = wp.workingProfileId) " +
				"INNER JOIN fms_working_profile_duration_manpower wpdm ON (wpd.workingProfileDurationId = wpdm.workingProfileDurationId) " +
				"INNER JOIN security_user u ON (wpdm.userId = u.id) " +
				"INNER JOIN competency_user comp ON (comp.userId = u.id) " +
				"WHERE 1=1 " +
				"AND (? BETWEEN wpd.startDate AND wpd.endDate) " +
				"AND (((? BETWEEN wp.startTime AND wp.endTime) OR (? BETWEEN wp.startTime AND wp.endTime)) " +
				//"OR (? <= wp.startTime AND ? >= wp.endTime)" +
				") " +
				"AND  comp.competencyId =? " +
				"AND wp.name <> 'R' " ;
		//return super.select(sql, FacilityObject.class, new Object[] {dateChecked, timeFrom, timeTo}, 0, -1);
		int count = 0;
		
		try {
			Collection col = super.select(sql, HashMap.class, new Object[] {dateChecked, timeFrom, timeTo/*, timeFrom, timeTo*/, competencyId}, 0, 1);
			if (col != null){
				HashMap map = (HashMap)col.iterator().next();
				count = (Integer)map.get("COUNT");
			}
		} catch (Exception e){
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return count;
	}
	
	public int countManpowerRest(Date dateChecked, String timeFrom, String timeTo, String competencyId) throws DaoException {
		String sql = "SELECT count(*) AS COUNT " +
			"FROM fms_working_profile wp INNER JOIN fms_working_profile_duration wpd ON (wpd.workingProfileId = wp.workingProfileId) " +
			"INNER JOIN fms_working_profile_duration_manpower wpdm ON (wpd.workingProfileDurationId = wpdm.workingProfileDurationId) " +
			"INNER JOIN security_user u ON (wpdm.userId = u.id) " +
			"INNER JOIN competency_user comp ON (comp.userId = u.id) " +
			"WHERE 1=1 " +
			"AND (? BETWEEN wpd.startDate AND wpd.endDate) " +
			"AND (((? BETWEEN wp.startTime AND wp.endTime) OR (? BETWEEN wp.startTime AND wp.endTime)) " +
			//"OR (? <= wp.startTime AND ? >= wp.endTime) " +
			") " +
			"AND  comp.competencyId =? " +
			"AND wp.name = 'R' " ;
			//return super.select(sql, FacilityObject.class, new Object[] {dateChecked, timeFrom, timeTo}, 0, -1);
		int count = 0;
		
		try {
			Collection col = super.select(sql, HashMap.class, new Object[] {dateChecked, timeFrom, timeTo/*, timeFrom, timeTo*/, competencyId}, 0, 1);
			if (col != null){
				HashMap map = (HashMap)col.iterator().next();
				count = (Integer)map.get("COUNT");
			}
		} catch (Exception e){
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return count;
	}
	
// check availability for manpower
	
	public Map selectManpowerByCompetencyId(String competencyId) throws DaoException {
		Map manpowerMap = new HashMap();
		String sql = "SELECT c.competencyId AS competencyId, " +
				"(SELECT cx.competencyName FROM competency cx WHERE cx.competencyId = c.competencyId) AS competencyName, " +
				"COUNT(cu.userId) AS user_competent " +
				"FROM competency c LEFT JOIN competency_user cu ON (c.competencyId = cu.competencyId) " +
				"WHERE c.competencyId = ? " +
				"GROUP BY c.competencyId";
		Collection col = super.select(sql, HashMap.class, new Object[] { competencyId }, 0, -1);
		for (Iterator itr = col.iterator(); itr.hasNext();) {
			HashMap map = (HashMap) itr.next();
			manpowerMap.put("competencyId", (String) map.get("competencyId"));
			manpowerMap.put("competencyName", (String) map.get("competencyName"));
			manpowerMap.put("totalUser", (Integer) map.get("user_competent"));
		}
		return manpowerMap;
	}
	
	public int countManpowerTotalInPool(String competencyId) throws DaoException {
		int count = 0;
		String sql = "SELECT c.competencyId AS competencyId, " +
				"COUNT(cu.userId) AS user_competent " +
				"FROM competency c LEFT JOIN competency_user cu ON (c.competencyId = cu.competencyId) " +
				"WHERE c.competencyId = ? " +
				"GROUP BY c.competencyId";
		try {
			Collection col =  super.select(sql, HashMap.class, new String[] {competencyId}, 0, -1);
			if (col != null && col.size()>0) {
				HashMap map = (HashMap) col.iterator().next();
				count = (Integer) map.get("user_competent");
			}
		} catch (Exception e){
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		
		return count;
	}
	
	public Collection selectScpService(String requestId) throws DaoException{
		String sql="SELECT p.id,p.requestId,p.serviceId,p.facilityId," +
				"p.requiredFrom,p.requiredTo,p.departureTime,p.location,p.segment," +
				"p.settingFrom,p.settingTo,p.rehearsalFrom,p.rehearsalTo,p.recordingFrom,p.recordingTo," +
				"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as facility " +
				"FROM fms_eng_service_scp p " +
				"INNER JOIN fms_rate_card f ON (f.id=p.facilityId) " +
				"WHERE p.requestId=? ORDER BY createdDate DESC";
		return super.select(sql, ScpService.class, new String[]{requestId}, 0, -1);
	}
	
	public Collection selectPostProductionService(String requestId) throws DaoException{
		String sql="Select p.id,p.requestId,p.serviceId,p.facilityId,p.requiredDate,p.requiredDateTo, p.fromTime," +
				"p.toTime,p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as facility from fms_eng_service_postproduction p " +
				" INNER JOIN fms_rate_card f on f.id=p.facilityId " +
				"where p.requestId=? order By createdDate desc";
		return super.select(sql, PostProductionService.class, new String[]{requestId}, 0, -1);
	}
	
	public Collection selectManpowerService(String requestId) throws DaoException{
		String sql="Select p.id,p.requestId,p.serviceId,p.competencyId,p.quantity," +
				"p.requiredFrom,p.requiredTo,p.remarks,p.fromTime,p.toTime," +
				"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as competencyName " +
				"from fms_eng_service_manpower p " +
				" INNER JOIN fms_rate_card f on f.id=p.competencyId " +
				"where p.requestId=? order By p.createdDate desc";
		return super.select(sql, ManpowerService.class, new String[]{requestId}, 0, -1);
	}
	
	public Collection selectStudioService(String requestId) throws DaoException{
		String sql="Select p.id,p.requestId,p.serviceId,p.facilityId,p.bookingDate, p.bookingDateTo, " +
				"p.requiredFrom,p.requiredTo,p.segment," +
				"p.settingFrom,p.settingTo,p.rehearsalFrom,p.rehearsalTo,p.vtrFrom,p.vtrTo," +
				"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as facility " +
				"from fms_eng_service_studio p " +
				" INNER JOIN fms_rate_card f on f.id=p.facilityId " +
				"where p.requestId=? order By p.createdDate desc";
		return super.select(sql, StudioService.class, new String[]{requestId}, 0, -1);
	}
	
	public Collection selectOtherService(String requestId) throws DaoException{
		String sql="Select p.id,p.requestId,p.serviceId,p.facilityId,p.quantity," +
				"p.requiredFrom,p.requiredTo,p.remarks,p.fromTime,p.toTime," +
				"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as facility " +
				"from fms_eng_service_other p " +
				" INNER JOIN fms_rate_card f on f.id=p.facilityId " +
				"where p.requestId=? order By p.createdDate desc";
		return super.select(sql, OtherService.class, new String[]{requestId}, 0, -1);
	}
	
	public Collection selectTvroService(String requestId) throws DaoException{
		String sql="Select p.id,p.requestId,p.serviceId,p.feedTitle,p.location," +
				"p.requiredDate,p.requiredDateTo, p.timezone,p.totalTimeReq,p.timeMeasure,p.remarks,p.fromTime,p.toTime," +
				"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,p.feedType "+
				"from fms_eng_service_tvro p " +
				"where p.requestId=? order By p.createdDate desc";
		return super.select(sql, TvroService.class, new String[]{requestId}, 0, -1);
	}
	
	public Collection selectVtrService(String requestId) throws DaoException{
		String sql="Select p.id,p.requestId,p.serviceId,p.service,p.facilityId, " +
				"p.requiredDate,p.requiredDateTo, p.requiredFrom,p.requiredTo," +
				"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate "+
				"from fms_eng_service_vtr p " +
				"where p.requestId=? order By p.createdDate desc";
		return super.select(sql, VtrService.class, new String[]{requestId}, 0, -1);
	}
	
	public Collection selectVtrServiceMod(String requestId) throws DaoException {
		String sql = "Select p.id,p.requestId,p.serviceId,p.service," +
				"p.requiredDate,p.requiredDateTo,p.requiredFrom,p.requiredTo,p.formatFrom,p.formatTo," +
				"p.conversionFrom,p.conversionTo,p.duration,p.noOfCopies,p.remarks," +
				"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate," +
				"p.facilityId, f.name as facility," +
				"p.internalRate, p.externalRate, p.blockBooking " +
				"from fms_eng_service_vtr p " +
				"LEFT JOIN fms_rate_card f on f.id=p.facilityId " +
				"where p.requestId=? order By createdDate desc";
		return super.select(sql, VtrService.class, new String[] {requestId} ,0, -1);
	}
	
	public Collection selectRateCardCategoryByRCId(String rateCardId) throws DaoException {
		String sql = "SELECT rc.rateCardId AS id, rc.equipment AS idCategory " +
				"FROM fms_rate_card_equipment rc " +
				"WHERE 1=1 " +
				"AND rc.rateCardId = ? ";
		
		return super.select(sql, RateCard.class, new String[] {rateCardId} , 0, -1);		
	}
	
	public Collection selectRateCardByRCId(String rateCardId) throws DaoException {
		String sql = "SELECT rc.equipment AS idCategory, sum(rc.quantity) AS equipmentQty " +
				"FROM fms_rate_card_equipment rc " +
				"WHERE 1=1 " +
				"AND rc.rateCardId = ? " +
				"GROUP BY rc.equipment ";
		
		return super.select(sql, RateCard.class, new String[] {rateCardId} , 0, -1);		
	}
	
	public Collection selectRateCardManpowerByRCId(String rateCardId) throws DaoException {
		String sql = "SELECT rc.manpower AS idCategory " +
				"FROM fms_rate_card_manpower rc " +
				"WHERE 1=1 " +
				"AND rc.rateCardId = ? ";
		
		return super.select(sql, RateCard.class, new String[] {rateCardId} , 0, -1);		
	}
	
	public Collection selectRateCardMPByRCId(String rateCardId) throws DaoException {
		String sql = "SELECT rc.manpower AS idCategory, sum(rc.quantity) AS manpowerQty " +
				"FROM fms_rate_card_manpower rc " +
				"WHERE 1=1 " +
				"AND rc.rateCardId = ? " +
				"GROUP BY rc.manpower ";
		
		return super.select(sql, RateCard.class, new String[] {rateCardId} , 0, -1);		
	}
	
	public RateCard selectRateCardByService(String serviceTypeId) throws DaoException {
		String sql = "SELECT TOP 1 " +
				"rc.id AS id, " +
				"rc.name AS name " +
				"FROM fms_rate_card rc " +
				"WHERE rc.serviceTypeId=? " + 
				"ORDER BY rc.modifiedOn DESC ";
				//+ " AND (rcd.effectiveDate <= GETDATE() OR rcd.effectiveDate > GETDATE()) "
				
		Collection col = super.select(sql, RateCard.class, new Object[] { serviceTypeId },
				0, 1);
		try {
			return (RateCard) col.iterator().next();
		} catch (Exception e) {
		}

		return null;
	}
}
