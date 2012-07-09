package com.tms.fms.eamms.model;

import java.util.Collection;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.model.DefaultDataObject;

public class EammsDao extends DataSourceDao {
	
	public void init() throws DaoException {
		super.init();
	}
	
	public void setOverdueStatus() throws DaoException {
		String sql = " update app_fd_eamms_asset_rental " +
				" SET c_status ='Overdue' " +
				" WHERE Convert(datetime,c_toDate) < getdate() " +
				" AND c_status='Rent Out' ";
		
		super.update(sql, null);
		
	}
	
	public DefaultDataObject getRentalReqestInfo(String id) throws DaoException {
		String sql = " select  r.c_rentalId as rentalId, r.c_toDate as toDate , " +
				" (s.firstName +' '+s.lastName) as requestorName, s.email1 as requestorEmail, " +
				" (se.firstName +' '+se.lastName) as engineerName, se.email1 as engineerEmail " +
				" FROM app_fd_eamms_asset_rental r " +
				" LEFT JOIN security_user s ON (s.username=r.c_createdBy) " +
				" LEFT JOIN security_user se ON (se.username=r.c_engineerAssign) " +
				" WHERE r.id = ? " ;    	
		
			Collection col = super.select(sql, DefaultDataObject.class, new String[]{id}, 0, 1);
			if (col.size() == 1){
				return (DefaultDataObject) col.iterator().next();
			}				
		return new DefaultDataObject();
	}
	
	public Collection getRentalReqestDueReminderListing() throws DaoException {
		String sql = " select id " +
				" from app_fd_eamms_asset_rental " +
				" WHERE  c_status='Rent Out' AND Convert(datetime,c_toDate,103) = Convert(datetime,DATEADD(day, 1, DATEDIFF(dd, 0, GETDATE())),103)" +
				"  " ;    	
		
			Collection col = super.select(sql, DefaultDataObject.class, null, 0, -1);
			return col;
	}


	

}
