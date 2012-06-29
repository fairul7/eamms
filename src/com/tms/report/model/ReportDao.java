package com.tms.report.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.services.security.Group;

// dao class to get related information from DB for generating report for Khazanah
public class ReportDao extends DataSourceDao {
	
	public Collection getContentUsageReportDetails(String parentId, String groupId, Date startDate, Date endDate) throws DaoException{
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT distinct cms.userId, concat(u.firstName, ' ', u.lastName) as userName " +
				"FROM cms_content_report cms, security_user_group ug, security_user u " +
				"WHERE cms.event = 'View' " +
				"AND ug.userId = cms.userId " +
				"AND cms.userId = u.id ");
		
		if(groupId != null && !groupId.equals("")){
			sql.append(" AND ug.groupId = '" + groupId + "' ");
		}
		
		if(parentId != null && parentId.length() > 0){
			sql.append(" AND cms.id IN ( SELECT DISTINCT id FROM cms_content_status WHERE parentId = '" + parentId + "')");
		}
		
		if(startDate != null){
			
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);
			startCal.set(Calendar.HOUR_OF_DAY, 0);
			startCal.set(Calendar.MINUTE, 0);
			sql.append("AND cms.date >= ? ");
			paramList.add(startCal.getTime());
		}
		
		if(endDate != null){
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);
			endCal.set(Calendar.HOUR_OF_DAY, 23);
			endCal.set(Calendar.MINUTE, 59);
			
			
			sql.append("AND cms.date <= ? ");
			paramList.add(endCal.getTime());
			
		}
		
		return super.select(sql.toString(), ReportContentObject.class, paramList.toArray(), 0, -1);
	}
	
	public Collection getContentSubmittedReportDetails(String parentId, String groupId, Date startDate, Date endDate) throws DaoException{
		
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT distinct cms.userId, concat(u.firstName, ' ', u.lastName) as userName " +
				"FROM cms_content_audit cms, security_user_group ug, security_user u " +
				"WHERE cms.event = 'Submit' " +
				"AND ug.userId = cms.userId " +
				"AND cms.userId = u.id ");
		
		if(groupId != null && !groupId.equals("")){
			sql.append(" AND ug.groupId = '" + groupId + "' ");
		}
		
		if(parentId != null && parentId.length() > 0){
			sql.append(" AND cms.id IN ( SELECT DISTINCT id FROM cms_content_status WHERE parentId = '" + parentId + "')");
		}
		
		if(startDate != null){
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);
			startCal.set(Calendar.HOUR_OF_DAY, 0);
			startCal.set(Calendar.MINUTE, 0);
			
			sql.append("AND cms.date >= ? ");
			paramList.add(startCal.getTime());
		}
		
		if(endDate != null){
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);
			endCal.set(Calendar.HOUR_OF_DAY, 23);
			endCal.set(Calendar.MINUTE, 59);
			
			
			sql.append("AND cms.date <= ? ");
			paramList.add(endCal.getTime());
		}
		
		return super.select(sql.toString(), ReportContentObject.class, paramList.toArray(), 0, -1);
		
	}
	
	public Collection getExternalResourcesData(Date startDate, Date endDate) throws DaoException{
		
		Collection paramList = new ArrayList();
		StringBuffer sql= new StringBuffer("SELECT external_source_report.sourceId, external_source.sourceName as source, count(userId) as totalCount, count(distinct userId) as uniqueCount, " +
	        "SUM((to_number(to_char(endTime, 'SSSSS')) - to_number(to_char(startTime, 'SSSSS')))/3600) as totalHour " +
	        "from external_source_report, external_source " +
	        "WHERE external_source.sourceId = external_source_report.sourceId ");
		
		if(startDate != null && endDate != null){
			
			sql.append("AND external_source_report.startTime >= ? " +
					"AND external_source_report.endTime <= ? ");
			
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);
			startCal.set(Calendar.HOUR_OF_DAY, 0);
			startCal.set(Calendar.MINUTE, 0);
			
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);
			endCal.set(Calendar.HOUR_OF_DAY, 23);
			endCal.set(Calendar.MINUTE, 59);
			
			paramList.add(startCal.getTime());
			paramList.add(endCal.getTime());
		}
		
		sql.append("group by external_source_report.sourceId, external_source.sourceName ");
		
		return super.select(sql.toString(),ReportObject.class,paramList.toArray(),0,-1);
		
	}
	
	public Collection getResourceUsageData(Date startDate, Date endDate, String category) throws DaoException{
		Collection paramList = new ArrayList();
		StringBuffer sql= new StringBuffer("SELECT eventId,instanceId,resourceId,startDate,endDate,re.name AS resourceName " +
	        " FROM rm_booking b, rm_resource re " +
	        " WHERE re.id = b.resourceId " +
	        " AND b.status = 'C' ");
		
		if(category != null && category.trim().length() > 0){
			sql.append(" AND re.categoryId = '" + category + "' ");
		}
		
		if(startDate != null && endDate != null){
			
			sql.append("AND (");
			
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);
			startCal.set(Calendar.HOUR_OF_DAY, 0);
			startCal.set(Calendar.MINUTE, 0);
			
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);
			endCal.set(Calendar.HOUR_OF_DAY, 23);
			endCal.set(Calendar.MINUTE, 59);
			
			sql.append("( startDate >= ? ");
			sql.append("AND endDate <= ?) ");
			
			sql.append("OR ( startDate >= ? ");
			sql.append("AND startDate <= ?) ");
			
			sql.append("OR ( endDate >= ? ");
			sql.append("AND endDate <= ?) ");
			
			sql.append("OR ( startDate <= ? ");
			sql.append("AND endDate >= ?) ");
			
			sql.append(") ");
			
			paramList.add(startCal.getTime());
			paramList.add(endCal.getTime());
			
			paramList.add(startCal.getTime());
			paramList.add(endCal.getTime());
			
			paramList.add(startCal.getTime());
			paramList.add(endCal.getTime());
			
			paramList.add(startCal.getTime());
			paramList.add(endCal.getTime());
		}
		
		sql.append(" ORDER BY resourceName, startDate  ");
		
		return super.select(sql.toString(),ReportObject.class,paramList.toArray(),0,-1);
	}
	
	public Collection getContentSummaryDate(String[][] contentArr, Date startDate, Date endDate, String groupId) throws DaoException{
		Collection paramList = new ArrayList();
		Collection temp = new ArrayList();
		
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		startCal.set(Calendar.HOUR_OF_DAY, 0);
		startCal.set(Calendar.MINUTE, 0);
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		endCal.set(Calendar.HOUR_OF_DAY, 23);
		endCal.set(Calendar.MINUTE, 59);
		
		
		StringBuffer submittedSql = new StringBuffer("SELECT cms_content_audit.id as id, event, message " +
				"FROM cms_content_audit, security_user_group " +
				"WHERE (event = 'Submit' OR event = 'Approve' OR event = 'Publish') " +
				"AND cms_content_audit.userId = security_user_group.userId ");
		
		submittedSql.append("AND cms_content_audit.date >= ? ");
		paramList.add(startCal.getTime());
		submittedSql.append("AND cms_content_audit.date <= ? ");
		paramList.add(endCal.getTime());
		
		if(groupId != null && !"".equals(groupId)){
			submittedSql.append("AND security_user_group.groupId = ? ");
			paramList.add(groupId);
		}
		
		temp = super.select(submittedSql.toString(), HashMap.class, paramList.toArray(), 0, -1);
		
		return temp;
	}
	
	public ReportContentObject getContentSummaryData(String contentName, String contentPrefix, Date startDate, Date endDate) throws DaoException{
		Collection paramList = new ArrayList();
		Collection temp = new ArrayList();
		int submittedCount = 0;
		int approvedBySupervisorCount = 0;
		int approvedByEditorCount = 0;
		int approvedByComplianceCount = 0;
		int publishCount = 0;
		
		ReportContentObject obj = new ReportContentObject();
		obj.setContentName(contentName);
		
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		startCal.set(Calendar.HOUR_OF_DAY, 0);
		startCal.set(Calendar.MINUTE, 0);
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		endCal.set(Calendar.HOUR_OF_DAY, 23);
		endCal.set(Calendar.MINUTE, 59);
		
		StringBuffer submittedSql = new StringBuffer("SELECT cms_content_audit.id " +
				"FROM cms_content_audit, " + contentPrefix + 
				" WHERE event = 'Submit' " +
				"AND cms_content_audit.id = " + contentPrefix + ".id ");
		
		submittedSql.append("AND cms_content_audit.date >= ? ");
		paramList.add(startCal.getTime());
		submittedSql.append("AND cms_content_audit.date <= ? ");
		paramList.add(endCal.getTime());
		
		temp = super.select(submittedSql.toString(), HashMap.class, paramList.toArray(), 0, -1);
		
		if (temp!=null && temp.size()>0) {
            submittedCount = temp.size();
        }
		
		paramList = new ArrayList();
		
		StringBuffer approvedSql = new StringBuffer("SELECT cms_content_audit.id, message " +
				"FROM cms_content_audit, " + contentPrefix + 
				" WHERE event = 'Approve' " +
				"AND message <> ' ' " +
				"AND cms_content_audit.id = " + contentPrefix + ".id ");
		
		approvedSql.append("AND cms_content_audit.date >= ? ");
		paramList.add(startCal.getTime());
		approvedSql.append("AND cms_content_audit.date <= ? ");
		paramList.add(endCal.getTime());
		
		temp = super.select(approvedSql.toString(), HashMap.class, paramList.toArray(), 0, -1);
		
		String message = new String();
		
		if (temp!=null && temp.size()>0) {
			for(Iterator i=temp.iterator(); i.hasNext();  ){
				HashMap row = (HashMap)i.next();
				message = row.get("message").toString();
				if(!message.equals("")){
					if(message.startsWith("ASA-")){
						approvedBySupervisorCount++;
					}else if(message.startsWith("AEA-")){
						approvedByEditorCount++;
					}else if(message.startsWith("ACA-")){
						approvedByComplianceCount++;
					}
				}
			}
		}
		
		paramList = new ArrayList();
		
		StringBuffer publishedSql = new StringBuffer("SELECT cms_content_audit.id " +
				"FROM cms_content_audit, " + contentPrefix +
				" WHERE event = 'Publish' " +
				"AND cms_content_audit.id = " + contentPrefix + ".id ");
		
		publishedSql.append("AND cms_content_audit.date >= ? ");
		paramList.add(startCal.getTime());
		publishedSql.append("AND cms_content_audit.date <= ? ");
		paramList.add(endCal.getTime());
		
		temp = super.select(publishedSql.toString(), HashMap.class, paramList.toArray(), 0, -1);
		
		if (temp!=null && temp.size()>0) {
			publishCount = temp.size();
        }
		
		obj.setSubmittedCount(submittedCount);
		obj.setApprovedByComplianceCount(approvedByComplianceCount);
		obj.setApprovedByEditorCount(approvedByEditorCount);
		obj.setApprovedBySupervisorCount(approvedBySupervisorCount);
		obj.setPublishCount(publishCount);
		
		return obj;
	}
	
	public Collection getContentUsageData(String parentName, String group, String parentId, Date startDate, Date endDate) throws DaoException{
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT '" + parentName + "' AS sectionName, '" + parentId +"' AS contentSectionId, " + 
				"g.groupName as groupName, g.id as groupId, COUNT(cms.userId) as totalCount, COUNT(distinct cms.userId) AS uniqueCount " +
				"FROM cms_content_report cms, security_user_group ug, security_group g  " +
				"WHERE cms.event = 'View' " +
				"AND ug.userId = cms.userId " +
				"AND g.id = ug.groupId ");
		
		if(group != null && !group.equals("")){
			sql.append(" AND g.id = '" + group + "' ");
		}
		
		if(parentId != null && parentId.length() > 0){
			sql.append(" AND cms.id IN ( SELECT DISTINCT id FROM cms_content_status WHERE parentId = '" + parentId + "')");
		}
		
		if(startDate != null){
			
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);
			startCal.set(Calendar.HOUR_OF_DAY, 0);
			startCal.set(Calendar.MINUTE, 0);
			sql.append("AND cms.date >= ? ");
			paramList.add(startCal.getTime());
		}
		
		if(endDate != null){
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);
			endCal.set(Calendar.HOUR_OF_DAY, 23);
			endCal.set(Calendar.MINUTE, 59);
			
			
			sql.append("AND cms.date <= ? ");
			paramList.add(endCal.getTime());
			
		}
		
		sql.append("GROUP BY g.groupName, g.id ");
		
		return super.select(sql.toString(), ReportContentObject.class, paramList.toArray(), 0, -1);
	}
	
	public Collection getContentSubmittedData(String parentName, String group, String parentId, Date startDate, Date endDate) throws DaoException{
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT '" + parentName + "' AS sectionName, '" + parentId +"' AS contentSectionId, " + 
				"g.groupName as groupName, g.id as groupId, COUNT(cms.userId) as totalCount, COUNT(distinct cms.userId) AS uniqueCount " +
				"FROM cms_content_audit cms, security_user_group ug, security_group g " +
				"WHERE cms.event = 'Submit' " +
				"AND ug.userId = cms.userId " +
				"AND g.id = ug.groupId ");
		
		if(group != null && !group.equals("")){
			sql.append(" AND g.id = '" + group + "' ");
		}
		
		if(parentId != null && parentId.length() > 0){
			sql.append(" AND cms.id IN ( SELECT DISTINCT id FROM cms_content_status WHERE parentId = '" + parentId + "')");
		}
		
		if(startDate != null){
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);
			startCal.set(Calendar.HOUR_OF_DAY, 0);
			startCal.set(Calendar.MINUTE, 0);
			
			sql.append("AND cms.date >= ? ");
			paramList.add(startCal.getTime());
		}
		
		if(endDate != null){
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);
			endCal.set(Calendar.HOUR_OF_DAY, 23);
			endCal.set(Calendar.MINUTE, 59);
			
			
			sql.append("AND cms.date <= ? ");
			paramList.add(endCal.getTime());
		}
		
		sql.append("GROUP BY g.groupName, g.id ");
		
		return super.select(sql.toString(), ReportContentObject.class, paramList.toArray(), 0, -1);
	}
	
	public Collection getUserDivision() throws DaoException{
		StringBuffer sql = new StringBuffer("SELECT DISTINCT division FROM security_user_others_info " +
				"where division <> '-1' " +
				"order by division ");
		return super.select(sql.toString(), HashMap.class, null, 0, -1);
	}
	
    public Collection getReportUserList(Date startDate, Date endDate) throws DaoException {
        String sql = "SELECT DISTINCT userId, firstName, lastName FROM log_entry LEFT JOIN " +
                "security_user ON log_entry.userId=security_user.id WHERE " +
                "(entryDate > ? AND entryDate < ? AND entryLabel='kacang.services.log.security.Login') " +
                "ORDER BY firstName";

        return super.select(sql, HashMap.class,new Object[]{startDate,endDate},0,-1);
    }

    public int getTotalLoginPerUser(Date startDate, Date endDate, String userId) throws DaoException {
        String sql = "SELECT COUNT(userId) AS total FROM log_entry WHERE " +
                "entryDate > ? AND entryDate < ? AND userId=? AND entryLabel='kacang.services.log.security.Login'";
        Collection col = super.select(sql, HashMap.class,new Object[]{startDate,endDate,userId},0,-1);
        int iRet = 0;
        if (col!=null && col.size()>0) {
            HashMap map = (HashMap)col.iterator().next();
            iRet = ((Number)map.get("total")).intValue();
        }
        return iRet;
    }

    public Collection getReportModuleList(Date startDate, Date endDate) throws DaoException {
        String sql = "SELECT DISTINCT module FROM log_entry WHERE " +
                "entryDate>? AND entryDate<? ORDER BY module";
        return super.select(sql,HashMap.class, new Object[]{startDate,endDate},0,-1);
    }

    public int getTotalUniqueCountPerModule(Date startDate, Date endDate, String module) throws DaoException {
        String sql = "SELECT COUNT(distinct userId ) AS total FROM log_entry WHERE " +
                "entryDate>? AND entryDate<? AND module=? ";
        int iTotal=0;
        Collection col = super.select(sql,HashMap.class,new Object[]{startDate,endDate,module},0,-1);
        if (col!=null && col.size()>0) {
            HashMap map = (HashMap)col.iterator().next();
            iTotal = ((Number)map.get("total")).intValue();
        }
        return iTotal;
    }

    public int getTotalCountPerModule(Date startDate, Date endDate, String module) throws DaoException {
        String sql = "SELECT COUNT(userId) AS total FROM log_entry WHERE " +
                "entryDate>? AND entryDate<? AND module=?";
        int iTotal=0;
        Collection col = super.select(sql,HashMap.class,new Object[]{startDate,endDate,module},0,-1);
        if (col!=null && col.size()>0) {
            HashMap map = (HashMap)col.iterator().next();
            iTotal = ((Number)map.get("total")).intValue();
        }
        return iTotal;
    }

    // select groups by user
    public Collection selectGroupsByUser(String userId) throws DaoException {
        String sql = "SELECT id,groupName,description,active FROM " +
                "security_group LEFT JOIN security_user_group ON " +
                "security_group.id=security_user_group.groupid WHERE " +
                "security_user_group.userId=?";
        return super.select(sql, Group.class,new Object[]{userId},0,-1);
    }

    // select distinct modules
    public Collection selectDistinctModules(String groupId) throws DaoException {
        String sql = "SELECT DISTINCT(moduleId) AS moduleId FROM security_principal_permission " +
                "WHERE principalId=?";
        return super.select(sql,HashMap.class,new Object[]{groupId},0,-1);
    }

    //select permission list based on group and module
    public Collection selectPermissionForGroupAndModule(String groupId, String moduleId) throws DaoException {
        String sql = "SELECT DISTINCT permissionId FROM security_principal_permission " +
                "WHERE principalId=? AND moduleId=?";
        return super.select(sql,HashMap.class,new Object[]{groupId,moduleId},0,-1);
    }
}
