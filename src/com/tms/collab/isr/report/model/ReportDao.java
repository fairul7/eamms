package com.tms.collab.isr.report.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import com.tms.collab.isr.model.StatusObject;

public class ReportDao extends DataSourceDao{
	
	public Collection selectTimeOfResolveReportDetailListing(Date fromDate, Date toDate, String condition, String assocId, String statusId) throws DaoException{
		Collection result = new ArrayList();
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT r.requestId, r.dateCreated, r.createdBy, r.requestPriority, " +
				"r.requestPriorityByAdmin, r.requestSubject, " +
				"s.statusName AS status, " +
				"concat(c2.shortDesc, '-', d2.shortDesc) AS reqDept, r.requestFromDept as reqDeptId " +
				"FROM isr_request r " +
				"LEFT OUTER JOIN isr_status s ON r.requestStatus = s.statusId " +
				"LEFT OUTER JOIN org_chart_dept_country dc1 ON dc1.associativityId = r.requestToDept " +
				"LEFT OUTER JOIN org_chart_department d1 ON dc1.deptCode = d1.code " +
				"LEFT OUTER JOIN org_chart_country c1 ON dc1.countryCode = c1.code " +
				"LEFT OUTER JOIN org_chart_dept_country dc2 ON dc2.associativityId = r.requestFromDept " +
				"LEFT OUTER JOIN org_chart_department d2 ON dc2.deptCode = d2.code " +
				"LEFT OUTER JOIN org_chart_country c2 ON dc2.countryCode = c2.code " +
				"WHERE 1=1 ");
		
		if(statusId == null || statusId.equals("")){
			sql.append(" AND (requestStatus = '" + StatusObject.STATUS_ID_CLOSE + "' OR requestStatus = '" + StatusObject.STATUS_ID_COMPLETED + "')");
		}else if(statusId != null && statusId.equals(StatusObject.STATUS_ID_CLOSE)){
			sql.append(" AND requestStatus = '" + StatusObject.STATUS_ID_CLOSE + "' ");
		}	
		else if(statusId != null && statusId.equals(StatusObject.STATUS_ID_COMPLETED)){
			sql.append(" AND requestStatus = '" + StatusObject.STATUS_ID_COMPLETED + "' ");
		}
		
		if(fromDate != null){
			sql.append(" AND r.dateCreated >= ? ");
			fromDate.setHours(0);
			fromDate.setMinutes(0);
			paramList.add(fromDate);
		}
		
		if(toDate != null){
			sql.append(" AND r.dateCreated <= ?");
			toDate.setHours(23);
			toDate.setMinutes(59);
			paramList.add(toDate);
		}
		
		if(condition != null && condition.trim().length() > 0){
			sql.append(" AND TO_DAYS(r.lastUpdatedDate) - TO_DAYS(r.dateCreated) " + condition);
		}
		
		if(assocId != null && assocId.trim().length() > 0){
			sql.append(" AND requestToDept = '" + assocId + "' ");
		}
		
		result = super.select(sql.toString(), ReportObject.class, paramList.toArray(), 0, -1);
		
		return result;
	}
	
	public int selectTimeOfResolveReportDetailListingCount(Date fromDate, Date toDate, String condition, String assocId, String statusId) throws DaoException{
		
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT COUNT(r.requestId) AS total " +
				"FROM isr_assignment a " +
				"LEFT OUTER JOIN isr_request r ON a.requestId = r.requestId " +
				"LEFT OUTER JOIN security_user su ON su.id = a.assigneeUserId " +
				"LEFT OUTER JOIN isr_status s ON r.requestStatus = s.statusId " +
				"LEFT OUTER JOIN org_chart_dept_country dc1 ON dc1.associativityId = r.requestToDept " +
				"LEFT OUTER JOIN org_chart_department d1 ON dc1.deptCode = d1.code " +
				"LEFT OUTER JOIN org_chart_country c1 ON dc1.countryCode = c1.code " +
				"LEFT OUTER JOIN org_chart_dept_country dc2 ON dc2.associativityId = r.requestFromDept " +
				"LEFT OUTER JOIN org_chart_department d2 ON dc2.deptCode = d2.code " +
				"LEFT OUTER JOIN org_chart_country c2 ON dc2.countryCode = c2.code " +
				"WHERE 1=1 ");
		
		if(statusId == null || statusId.equals("")){
			sql.append(" AND (requestStatus = '" + StatusObject.STATUS_ID_CLOSE + "' OR requestStatus = '" + StatusObject.STATUS_ID_COMPLETED + "')");
		}else if(statusId != null && statusId.equals(StatusObject.STATUS_ID_CLOSE)){
			sql.append(" AND requestStatus = '" + StatusObject.STATUS_ID_CLOSE + "' ");
		}	
		else if(statusId != null && statusId.equals(StatusObject.STATUS_ID_COMPLETED)){
			sql.append(" AND requestStatus = '" + StatusObject.STATUS_ID_COMPLETED + "' ");
		}
		
		if(fromDate != null){
			sql.append(" AND r.dateCreated >= ? ");
			fromDate.setHours(0);
			fromDate.setMinutes(0);
			paramList.add(fromDate);
		}
		
		if(toDate != null){
			sql.append(" AND r.dateCreated <= ?");
			toDate.setHours(23);
			toDate.setMinutes(59);
			paramList.add(toDate);
		}
		
		if(condition != null && condition.trim().length() > 0){
			sql.append(" AND TO_DAYS(r.lastUpdatedDate) - TO_DAYS(r.dateCreated) " + condition);
		}
		
		if(assocId != null && assocId.trim().length() > 0){
			sql.append(" AND requestToDept = '" + assocId + "' ");
		}
		
		Map row = (Map) super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, -1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
	}

	public Collection selectStaffReportDetailListing(Date fromDate, Date toDate, String status, String assignee) throws DaoException{
		Collection result = new ArrayList();
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT r.requestId, r.dateCreated, r.createdBy, r.requestPriority, " +
				"r.requestPriorityByAdmin, r.requestSubject, " +
				"s.statusName AS status, " +
				"concat(c2.shortDesc, '-', d2.shortDesc) AS reqDept, r.requestFromDept as reqDeptId, " +
				"concat(su.firstName, ' ', su.lastName) As assignee " +
				"FROM isr_assignment a " +
				"LEFT OUTER JOIN isr_request r ON a.requestId = r.requestId " +
				"LEFT OUTER JOIN security_user su ON su.id = a.assigneeUserId " +
				"LEFT OUTER JOIN isr_status s ON r.requestStatus = s.statusId " +
				"LEFT OUTER JOIN org_chart_dept_country dc1 ON dc1.associativityId = r.requestToDept " +
				"LEFT OUTER JOIN org_chart_department d1 ON dc1.deptCode = d1.code " +
				"LEFT OUTER JOIN org_chart_country c1 ON dc1.countryCode = c1.code " +
				"LEFT OUTER JOIN org_chart_dept_country dc2 ON dc2.associativityId = r.requestFromDept " +
				"LEFT OUTER JOIN org_chart_department d2 ON dc2.deptCode = d2.code " +
				"LEFT OUTER JOIN org_chart_country c2 ON dc2.countryCode = c2.code " +
				"WHERE 1=1 ");
		
		if(fromDate != null){
			sql.append(" AND r.dateCreated >= ? ");
			fromDate.setHours(0);
			fromDate.setMinutes(0);
			paramList.add(fromDate);
		}
		
		if(toDate != null){
			sql.append(" AND r.dateCreated <= ?");
			toDate.setHours(23);
			toDate.setMinutes(59);
			paramList.add(toDate);
		}
		
		if(status != null && status.trim().length() > 0){
			sql.append(" AND r.requestStatus = '" + status + "' ");
		}else{
			sql.append(" AND ( ");
			sql.append("r.requestStatus = '" + StatusObject.STATUS_ID_NEW + "' ");
			sql.append("OR r.requestStatus = '" + StatusObject.STATUS_ID_IN_PROGRESS + "' ");
			sql.append("OR r.requestStatus = '" + StatusObject.STATUS_ID_COMPLETED + "' ");
			sql.append("OR r.requestStatus = '" + StatusObject.STATUS_ID_CLOSE + "' ");
			sql.append("OR r.requestStatus = '" + StatusObject.STATUS_ID_REOPEN + "' ");
			sql.append(") ");
		}
		
		if(assignee != null && assignee.trim().length() > 0){
			sql.append(" AND a.assigneeUserId = '" + assignee + "' ");
		}
		
		result = super.select(sql.toString(), ReportObject.class, paramList.toArray(), 0, -1);
		
		return result;
	}
	
	public int selectStaffReportDetailListingCount(Date fromDate, Date toDate, String status, String assignee) throws DaoException{
		
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT COUNT(r.requestId) AS total " +
				"FROM isr_assignment a " +
				"LEFT OUTER JOIN isr_request r ON a.requestId = r.requestId " +
				"LEFT OUTER JOIN security_user su ON su.id = a.assigneeUserId " +
				"LEFT OUTER JOIN isr_status s ON r.requestStatus = s.statusId " +
				"LEFT OUTER JOIN org_chart_dept_country dc1 ON dc1.associativityId = r.requestToDept " +
				"LEFT OUTER JOIN org_chart_department d1 ON dc1.deptCode = d1.code " +
				"LEFT OUTER JOIN org_chart_country c1 ON dc1.countryCode = c1.code " +
				"LEFT OUTER JOIN org_chart_dept_country dc2 ON dc2.associativityId = r.requestFromDept " +
				"LEFT OUTER JOIN org_chart_department d2 ON dc2.deptCode = d2.code " +
				"LEFT OUTER JOIN org_chart_country c2 ON dc2.countryCode = c2.code " +
				"WHERE 1=1 ");
		
		if(fromDate != null){
			sql.append(" AND r.dateCreated >= ? ");
			fromDate.setHours(0);
			fromDate.setMinutes(0);
			paramList.add(fromDate);
		}
		
		if(toDate != null){
			sql.append(" AND r.dateCreated <= ?");
			toDate.setHours(23);
			toDate.setMinutes(59);
			paramList.add(toDate);
		}
		
		if(status != null && status.trim().length() > 0){
			sql.append(" AND r.requestStatus = '" + status + "' ");
		}else{
			sql.append(" AND ( ");
			sql.append("r.requestStatus = '" + StatusObject.STATUS_ID_NEW + "' ");
			sql.append("OR r.requestStatus = '" + StatusObject.STATUS_ID_IN_PROGRESS + "' ");
			sql.append("OR r.requestStatus = '" + StatusObject.STATUS_ID_COMPLETED + "' ");
			sql.append("OR r.requestStatus = '" + StatusObject.STATUS_ID_CLOSE + "' ");
			sql.append("OR r.requestStatus = '" + StatusObject.STATUS_ID_REOPEN + "' ");
			sql.append(") ");
		}
		
		if(assignee != null && assignee.trim().length() > 0){
			sql.append(" AND a.assigneeUserId = '" + assignee + "' ");
		}
		
		Map row = (Map) super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, -1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
	}
	
	public Collection selectPriorityReportDetailListing(Date fromDate, Date toDate, String priority, String reqDept, String recDept) throws DaoException{
		Collection result = new ArrayList();
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT r.requestId, r.dateCreated, r.createdBy, r.requestPriority, " +
				"r.requestPriorityByAdmin, r.requestSubject, " +
				"s.statusName AS status, " +
				"concat(c2.shortDesc, '-', d2.shortDesc) AS reqDept, r.requestFromDept as reqDeptId, " +
				"concat(su.firstName, ' ', su.lastName) As assignee " +
				"FROM isr_request r " +
				"LEFT OUTER JOIN isr_assignment a ON a.requestId = r.requestId " +
				"LEFT OUTER JOIN security_user su ON su.id = a.assigneeUserId " +
				"LEFT OUTER JOIN isr_status s ON r.requestStatus = s.statusId " +
				"LEFT OUTER JOIN org_chart_dept_country dc1 ON dc1.associativityId = r.requestToDept " +
				"LEFT OUTER JOIN org_chart_department d1 ON dc1.deptCode = d1.code " +
				"LEFT OUTER JOIN org_chart_country c1 ON dc1.countryCode = c1.code " +
				"LEFT OUTER JOIN org_chart_dept_country dc2 ON dc2.associativityId = r.requestFromDept " +
				"LEFT OUTER JOIN org_chart_department d2 ON dc2.deptCode = d2.code " +
				"LEFT OUTER JOIN org_chart_country c2 ON dc2.countryCode = c2.code " +
				"WHERE 1=1 ");
		
		if(fromDate != null){
			sql.append(" AND r.dateCreated >= ? ");
			fromDate.setHours(0);
			fromDate.setMinutes(0);
			paramList.add(fromDate);
		}
		
		if(toDate != null){
			sql.append(" AND r.dateCreated <= ?");
			toDate.setHours(23);
			toDate.setMinutes(59);
			paramList.add(toDate);
		}
		
		if(priority != null && priority.trim().length() > 0){
			sql.append(" AND r.requestPriorityByAdmin = '" + priority + "' ");
		}
		
		if(reqDept != null && reqDept.trim().length() > 0){
			sql.append(" AND r.requestFromDept = '" + reqDept + "' ");
		}
		
		if(recDept != null && recDept.trim().length() > 0){
			sql.append(" AND r.requestToDept = '" + recDept + "' ");
		}
		
		result = super.select(sql.toString(), ReportObject.class, paramList.toArray(), 0, -1);
		
		return result;
	}
	
	public int selectPriorityReportDetailListingCount(Date fromDate, Date toDate, String priority, String reqDept, String recDept) throws DaoException{
		
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT COUNT(r.requestId) AS total " +
				"FROM isr_request r " +
				"LEFT OUTER JOIN isr_assignment a ON a.requestId = r.requestId " +
				"LEFT OUTER JOIN security_user su ON su.id = a.assigneeUserId " +
				"LEFT OUTER JOIN isr_status s ON r.requestStatus = s.statusId " +
				"LEFT OUTER JOIN org_chart_dept_country dc1 ON dc1.associativityId = r.requestToDept " +
				"LEFT OUTER JOIN org_chart_department d1 ON dc1.deptCode = d1.code " +
				"LEFT OUTER JOIN org_chart_country c1 ON dc1.countryCode = c1.code " +
				"LEFT OUTER JOIN org_chart_dept_country dc2 ON dc2.associativityId = r.requestFromDept " +
				"LEFT OUTER JOIN org_chart_department d2 ON dc2.deptCode = d2.code " +
				"LEFT OUTER JOIN org_chart_country c2 ON dc2.countryCode = c2.code " +
				"WHERE 1=1 ");
		
		if(fromDate != null){
			sql.append(" AND r.dateCreated >= ? ");
			fromDate.setHours(0);
			fromDate.setMinutes(0);
			paramList.add(fromDate);
		}
		
		if(toDate != null){
			sql.append(" AND r.dateCreated <= ?");
			toDate.setHours(23);
			toDate.setMinutes(59);
			paramList.add(toDate);
		}
		
		if(priority != null && priority.trim().length() > 0){
			sql.append(" AND r.requestPriorityByAdmin = '" + priority + "' ");
		}
		
		if(reqDept != null && reqDept.trim().length() > 0){
			sql.append(" AND r.requestFromDept = '" + reqDept + "' ");
		}
		
		if(recDept != null && recDept.trim().length() > 0){
			sql.append(" AND r.requestToDept = '" + recDept + "' ");
		}
		
		Map row = (Map) super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, -1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
	}
	
	public Collection selectStatusReportDetailListing(Date fromDate, Date toDate, String status, String reqDept, String recDept) throws DaoException{
		Collection result = new ArrayList();
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT r.requestId, r.dateCreated, " +
				"concat(c2.shortDesc, '-', d2.shortDesc, ' (',r.createdBy, ')') as createdBy, " +
				"r.requestPriority, " +
				"r.requestPriorityByAdmin, r.requestSubject, " +
				"s.statusName AS status, " +
				"concat(c2.shortDesc, '-', d2.shortDesc) AS reqDept, r.requestFromDept as reqDeptId, " +
				"concat(su.firstName, ' ', su.lastName) As assignee " +
				"FROM isr_request r " +
				"LEFT OUTER JOIN isr_assignment a ON a.requestId = r.requestId " +
				"LEFT OUTER JOIN security_user su ON su.id = a.assigneeUserId " +
				"LEFT OUTER JOIN isr_status s ON r.requestStatus = s.statusId " +
				"LEFT OUTER JOIN org_chart_dept_country dc1 ON dc1.associativityId = r.requestToDept " +
				"LEFT OUTER JOIN org_chart_department d1 ON dc1.deptCode = d1.code " +
				"LEFT OUTER JOIN org_chart_country c1 ON dc1.countryCode = c1.code " +
				"LEFT OUTER JOIN org_chart_dept_country dc2 ON dc2.associativityId = r.requestFromDept " +
				"LEFT OUTER JOIN org_chart_department d2 ON dc2.deptCode = d2.code " +
				"LEFT OUTER JOIN org_chart_country c2 ON dc2.countryCode = c2.code " +
				"WHERE 1=1 ");
		
		if(fromDate != null){
			sql.append(" AND r.dateCreated >= ? ");
			fromDate.setHours(0);
			fromDate.setMinutes(0);
			paramList.add(fromDate);
		}
		
		if(toDate != null){
			sql.append(" AND r.dateCreated <= ?");
			toDate.setHours(23);
			toDate.setMinutes(59);
			paramList.add(toDate);
		}
		
		if(status != null && status.trim().length() > 0){
			sql.append(" AND r.requestStatus = '" + status + "' ");
		}
		
		if(reqDept != null && reqDept.trim().length() > 0){
			sql.append(" AND r.requestFromDept = '" + reqDept + "' ");
		}
		
		if(recDept != null && recDept.trim().length() > 0){
			sql.append(" AND r.requestToDept = '" + recDept + "' ");
		}
		
		result = super.select(sql.toString(), ReportObject.class, paramList.toArray(), 0, -1);
		
		return result;
	}
	
	public int selectStatusReportDetailListingCount(Date fromDate, Date toDate, String status, String reqDept, String recDept) throws DaoException{
		Collection result = new ArrayList();
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT COUNT(r.requestId) AS total " +
				"FROM isr_request r " +
				"LEFT OUTER JOIN isr_assignment a ON a.requestId = r.requestId " +
				"LEFT OUTER JOIN security_user su ON su.id = a.assigneeUserId " +
				"LEFT OUTER JOIN isr_status s ON r.requestStatus = s.statusId " +
				"LEFT OUTER JOIN org_chart_dept_country dc1 ON dc1.associativityId = r.requestToDept " +
				"LEFT OUTER JOIN org_chart_department d1 ON dc1.deptCode = d1.code " +
				"LEFT OUTER JOIN org_chart_country c1 ON dc1.countryCode = c1.code " +
				"LEFT OUTER JOIN org_chart_dept_country dc2 ON dc2.associativityId = r.requestFromDept " +
				"LEFT OUTER JOIN org_chart_department d2 ON dc2.deptCode = d2.code " +
				"LEFT OUTER JOIN org_chart_country c2 ON dc2.countryCode = c2.code " +
				"WHERE 1=1 ");
		
		if(fromDate != null){
			sql.append(" AND r.dateCreated >= ? ");
			fromDate.setHours(0);
			fromDate.setMinutes(0);
			paramList.add(fromDate);
		}
		
		if(toDate != null){
			sql.append(" AND r.dateCreated <= ?");
			toDate.setHours(23);
			toDate.setMinutes(59);
			paramList.add(toDate);
		}
		
		if(status != null && status.trim().length() > 0){
			sql.append(" AND r.requestStatus = '" + status + "' ");
		}
		
		if(reqDept != null && reqDept.trim().length() > 0){
			sql.append(" AND r.requestFromDept = '" + reqDept + "' ");
		}
		
		if(recDept != null && recDept.trim().length() > 0){
			sql.append(" AND r.requestToDept = '" + recDept + "' ");
		}
		
		Map row = (Map) super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, -1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
	}
	
	public Collection selectDepartmentStaff(String deptId) throws DaoException{
		Collection result = new ArrayList();
		StringBuffer sql = new StringBuffer("select h.userId as userId, CONCAT(su.firstName, ' ', su.lastName) as staffName " +
				"FROM org_chart_dept_country dc, org_chart_hierachy h, security_user su WHERE " +
				"dc.associativityId = ? " +
				" AND dc.deptCode = h.deptCode AND dc.countryCode = h.countryCode " +
				" AND su.id = h.userId ");
		
		result = super.select(sql.toString(), ReportObject.class, new Object[]{deptId}, 0, -1);
		
		return result;
	}
	
	//new staff report dao func
	public Collection selectStaffReportListing(String fromDate, String toDate, String[] staffUserId, String sort, boolean desc, int start, int row) throws DaoException{
		
		Collection result = new ArrayList();
		Collection paramList = new ArrayList();
		
		StringBuffer sql = new StringBuffer("SELECT CONCAT(su.firstName, ' ', su.lastName) as staffName, su.id as userId, " +
				"CONCAT(a.assigneeUserId, '*" + StatusObject.STATUS_ID_NEW +"') as reportLinkNew, " +
				"COUNT(r1.requestId) as noOfReqNew, " +
				"CONCAT(a.assigneeUserId, '*" + StatusObject.STATUS_ID_REOPEN +"') as reportLinkReopen, " +
				"COUNT(r2.requestId) as noOfReqReopen, " +
				"CONCAT(a.assigneeUserId, '*" + StatusObject.STATUS_ID_IN_PROGRESS +"') as reportLinkInProgress, " +
				"COUNT(r3.requestId) as noOfReqInProgress, " +
				"CONCAT(a.assigneeUserId, '*" + StatusObject.STATUS_ID_COMPLETED +"') as reportLinkCompleted, " +
				"COUNT(r4.requestId) as noOfReqCompleted, " +
				"CONCAT(a.assigneeUserId, '*" + StatusObject.STATUS_ID_CLOSE +"') as reportLinkClose, " +
				"COUNT(r5.requestId) as noOfReqClose, " +
				"CONCAT(a.assigneeUserId, '*" + StatusObject.STATUS_ID_CANCEL +"') as reportLinkCancel, " +
				"COUNT(r6.requestId) as noOfReqCancel, " +
				"CONCAT(a.assigneeUserId, '*" + StatusObject.STATUS_ID_CLARIFICATION +"') as reportLinkClarification, " +
				"COUNT(r7.requestId) as noOfReqClarification, " +
				"a.assigneeUserId as reportLink, " +
				"COUNT(r8.requestId) as noOfReq " +
				"FROM isr_assignment a ");
		
		sql.append("LEFT JOIN isr_request r1 ON a.requestId = r1.requestId AND r1.requestStatus = '" + StatusObject.STATUS_ID_NEW +"' ");
		
		if(fromDate != null && fromDate.trim().length() > 0){
			fromDate += " 00:00:00";
		}
		if(toDate != null && toDate.trim().length() > 0){
			toDate += " 23:59:00";
		}
		
		if(fromDate != null && fromDate.trim().length() > 0){
			sql.append(" AND r1.dateCreated >= ? ");
			paramList.add(fromDate);
		}
		
		if(toDate != null && toDate.trim().length() > 0){
			sql.append(" AND r1.dateCreated <= ? ");
			paramList.add(toDate);
		}
		sql.append("LEFT JOIN isr_request r2 ON a.requestId = r2.requestId AND r2.requestStatus = '" + StatusObject.STATUS_ID_REOPEN +"' ");
		if(fromDate != null && fromDate.trim().length() > 0){
			sql.append(" AND r2.dateCreated >= ? ");
			paramList.add(fromDate);
		}
		
		if(toDate != null && toDate.trim().length() > 0){
			sql.append(" AND r2.dateCreated <= ? ");
			paramList.add(toDate);
		}
		sql.append("LEFT JOIN isr_request r3 ON a.requestId = r3.requestId AND r3.requestStatus = '" + StatusObject.STATUS_ID_IN_PROGRESS +"' ");
		if(fromDate != null && fromDate.trim().length() > 0){
			sql.append(" AND r3.dateCreated >= ? ");
			paramList.add(fromDate);
		}
		
		if(toDate != null && toDate.trim().length() > 0){
			sql.append(" AND r3.dateCreated <= ? ");
			paramList.add(toDate);
		}
		sql.append("LEFT JOIN isr_request r4 ON a.requestId = r4.requestId AND r4.requestStatus = '" + StatusObject.STATUS_ID_COMPLETED +"' ");
		if(fromDate != null && fromDate.trim().length() > 0){
			sql.append(" AND r4.dateCreated >= ? ");
			paramList.add(fromDate);
		}
		
		if(toDate != null && toDate.trim().length() > 0){
			sql.append(" AND r4.dateCreated <= ? ");
			paramList.add(toDate);
		}
		sql.append("LEFT JOIN isr_request r5 ON a.requestId = r5.requestId AND r5.requestStatus = '" + StatusObject.STATUS_ID_CLOSE +"' ");
		if(fromDate != null && fromDate.trim().length() > 0){
			sql.append(" AND r5.dateCreated >= ? ");
			paramList.add(fromDate);
		}
		
		if(toDate != null && toDate.trim().length() > 0){
			sql.append(" AND r5.dateCreated <= ? ");
			paramList.add(toDate);
		}
		sql.append("LEFT JOIN isr_request r6 ON a.requestId = r6.requestId AND r6.requestStatus = '" + StatusObject.STATUS_ID_CANCEL +"' " );
		if(fromDate != null && fromDate.trim().length() > 0){
			sql.append(" AND r6.dateCreated >= ? ");
			paramList.add(fromDate);
		}
		
		if(toDate != null && toDate.trim().length() > 0){
			sql.append(" AND r6.dateCreated <= ? ");
			paramList.add(toDate);
		}
		sql.append("LEFT JOIN isr_request r7 ON a.requestId = r7.requestId AND r7.requestStatus = '" + StatusObject.STATUS_ID_CLARIFICATION +"' ");
		if(fromDate != null && fromDate.trim().length() > 0){
			sql.append(" AND r7.dateCreated >= ? ");
			paramList.add(fromDate);
		}
		
		if(toDate != null && toDate.trim().length() > 0){
			sql.append(" AND r7.dateCreated <= ? ");
			paramList.add(toDate);
		}
		sql.append("LEFT JOIN isr_request r8 ON a.requestId = r8.requestId AND (");
		sql.append("r8.requestStatus = '"+StatusObject.STATUS_ID_NEW+"' " +
				"OR r8.requestStatus = '"+StatusObject.STATUS_ID_REOPEN+"' " +
						"OR r8.requestStatus = '"+StatusObject.STATUS_ID_IN_PROGRESS+"' " +
								"OR r8.requestStatus = '"+StatusObject.STATUS_ID_COMPLETED+"' " +
										"OR r8.requestStatus = '"+StatusObject.STATUS_ID_CLOSE+"'");
		sql.append(")");
		if(fromDate != null && fromDate.trim().length() > 0){
			sql.append(" AND r8.dateCreated >= ? ");
			paramList.add(fromDate);
		}
		
		if(toDate != null && toDate.trim().length() > 0){
			sql.append(" AND r8.dateCreated <= ? ");
			toDate += " 23:59:00";
			paramList.add(toDate);
		}
		sql.append("LEFT JOIN security_user su ON su.id = assigneeUserId " );
		sql.append("WHERE a.assigneeUserId IN (");
				
		for(int i=0; i<staffUserId.length ; i++){
			if(i>0)
				sql.append(",");
			sql.append("?");
			paramList.add(staffUserId[i]);
		}
		sql.append(") ");
		
		sql.append(" GROUP BY assigneeUserId ");
		
		if(sort != null && sort.length() > 0){
			sql.append(" ORDER BY " + sort);
		}else{
			sql.append(" ORDER BY staffName ");
		}
		
		if(desc)
			sql.append(" DESC");
		
		result = super.select(sql.toString(), ReportObject.class, paramList.toArray(), start, row);
		
		return result;
	}
	
	//original staff report dao func
	public Collection selectStaffReportingListing(String fromDate, String toDate, String status, String[] staffUserId, String sort, boolean desc, int start, int row) throws DaoException{
		
		Collection result = new ArrayList();
		Collection paramList = new ArrayList();
		
		StringBuffer sql = new StringBuffer("SELECT CONCAT(su.firstName, ' ', su.lastName) AS staffName, s.statusName AS status, " +
				"COUNT(a.requestId) AS noOfReq, " +
				"CONCAT(a.assigneeUserId, '_', r.requestStatus) AS reportLink " +
				"FROM isr_request r, isr_assignment a, security_user su, isr_status s " +
				"WHERE a.requestId = r.requestId " +
				"and a.assigneeUserId = su.id " +
				"AND r.requestStatus = s.statusId " +
				"AND a.assigneeUserId IN (");
		for(int i=0; i<staffUserId.length ; i++){
			if(i>0)
				sql.append(",");
			sql.append("?");
			paramList.add(staffUserId[i]);
		}
		sql.append(") ");
		
		if(fromDate != null && fromDate.trim().length() > 0){
			sql.append(" AND r.dateCreated >= ? ");
			fromDate += " 00:00:00";
			paramList.add(fromDate);
		}
		
		if(toDate != null && toDate.trim().length() > 0){
			sql.append(" AND r.dateCreated <= ? ");
			toDate += " 23:59:00";
			paramList.add(toDate);
		}
		
		if(status != null && status.trim().length() > 0){
			sql.append(" AND r.requestStatus = ? ");
			paramList.add(status);
		}
		
		sql.append(" GROUP BY a.assigneeUserId, r.requestStatus ");
		
		if(sort != null && sort.length() > 0){
			sql.append(" ORDER BY " + sort);
		}else{
			sql.append(" ORDER BY staffName ");
		}
		
		if(desc)
			sql.append(" DESC");
		
		result = super.select(sql.toString(), ReportObject.class, paramList.toArray(), start, row);
		
		return result;
	}
	
	public Collection selectTimeOfResolve(Date fromDate, Date toDate, String condition, String recDept, String statusId) throws DaoException{
		Collection result = new ArrayList();
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT COUNT(requestId) AS noOfReq FROM isr_request WHERE 1=1 ");
		
		sql.append(" AND TO_DAYS(lastUpdatedDate) - TO_DAYS(dateCreated) " + condition);
		sql.append(" AND requestToDept = '" + recDept + "' ");
		
		if(statusId == null || statusId.equals("")){
			sql.append(" AND (requestStatus = '" + StatusObject.STATUS_ID_CLOSE + "' OR requestStatus ='" + StatusObject.STATUS_ID_COMPLETED + "') ");
		}else if(statusId != null && statusId.equals(StatusObject.STATUS_ID_CLOSE)){
			sql.append(" AND requestStatus = '" + StatusObject.STATUS_ID_CLOSE + "' ");
		}else if(statusId != null && statusId.equals(StatusObject.STATUS_ID_COMPLETED)){
			sql.append(" AND requestStatus ='" + StatusObject.STATUS_ID_COMPLETED + "' ");
		}
		
		if(fromDate != null){
			sql.append(" AND dateCreated >= ? ");
			paramList.add(fromDate);
		}
		
		if(toDate != null){
			sql.append(" AND dateCreated <= ? ");
			paramList.add(toDate);
		}
		
		result = super.select(sql.toString(), ReportObject.class, paramList.toArray(), 0, -1);
		
		return result;
	}
	
	public Collection selectRecDeptNoOfReq(Date fromDate, Date toDate) throws DaoException{
		Collection result = new ArrayList();
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT concat(c.shortDesc, '-', d.shortDesc) as dept, " +
				"COUNT(r.requestId) as recDeptNoOfReq " +
				"FROM isr_request r " +
				"left join org_chart_dept_country dc ON dc.associativityId = r.requestToDept " +
				"left join org_chart_country c ON c.code = dc.countryCode " +
				"left join org_chart_department d ON d.code = dc.deptCode " +
				"WHERE 1=1 ");
		
		if(fromDate != null){
			sql.append(" AND dateCreated >= ? ");
			paramList.add(fromDate);
		}
		
		if(toDate != null){
			sql.append(" AND dateCreated <= ? ");
			paramList.add(toDate);
		}
		
		sql.append(" GROUP BY requestToDept ");
		
		result = super.select(sql.toString(), ReportObject.class, paramList.toArray(), 0, -1);
		
		return result;
	}
	
	public Collection selectReqDeptNoOfReq(Date fromDate, Date toDate) throws DaoException{
		Collection result = new ArrayList();
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT concat(c.shortDesc, '-', d.shortDesc) as dept, " +
				"COUNT(r.requestId) as reqDeptNoOfReq " +
				"FROM isr_request r " +
				"left join org_chart_dept_country dc ON dc.associativityId = r.requestFromDept " +
				"left join org_chart_country c ON c.code = dc.countryCode " +
				"left join org_chart_department d ON d.code = dc.deptCode " +
				"WHERE 1=1 ");
		
		if(fromDate != null){
			sql.append(" AND dateCreated >= ? ");
			paramList.add(fromDate);
		}
		
		if(toDate != null){
			sql.append(" AND dateCreated <= ? ");
			paramList.add(toDate);
		}
		
		sql.append(" GROUP BY requestFromDept ");
		
		result = super.select(sql.toString(), ReportObject.class, paramList.toArray(), 0, -1);
		
		return result;
	}
	
	public Collection selectStatusNoOfReq(Date fromDate, Date toDate) throws DaoException{
		
		Collection result = new ArrayList();
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT s.statusName as status, COUNT(requestId) as noOfReq FROM isr_request r LEFT JOIN isr_status s on s.statusId = r.requestStatus WHERE 1=1 ");
		
		if(fromDate != null){
			sql.append(" AND dateCreated >= ? ");
			paramList.add(fromDate);
		}
		
		if(toDate != null){
			sql.append(" AND dateCreated <= ? ");
			paramList.add(toDate);
		}
		
		sql.append(" GROUP BY requestStatus");
		
		result = super.select(sql.toString(), ReportObject.class, paramList.toArray(), 0, -1);
		
		return result;
	}
	
	public Collection selectRequestTypeNoOfReq(Date fromDate, Date toDate) throws DaoException{
		
		Collection result = new ArrayList();
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT requestType as requestType, COUNT(requestId) as noOfReq FROM isr_request " +
				"WHERE requestType != '' AND requestType is not null ");
		
		if(fromDate != null){
			sql.append(" AND dateCreated >= ? ");
			paramList.add(fromDate);
		}
		
		if(toDate != null){
			sql.append(" AND dateCreated <= ? ");
			paramList.add(toDate);
		}
		
		sql.append(" GROUP by requestType");
		
		result = super.select(sql.toString(), ReportObject.class, paramList.toArray(), 0, -1);
		
		return result;
	}
	
	public Collection selectPriorityNoOfReq(Date fromDate, Date toDate) throws DaoException{
		
		Collection result = new ArrayList();
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT requestPriority as priority, COUNT(requestId) as noOfReq FROM isr_request WHERE 1=1 ");
		
		if(fromDate != null){
			sql.append(" AND dateCreated >= ? ");
			paramList.add(fromDate);
		}
		
		if(toDate != null){
			sql.append(" AND dateCreated <= ? ");
			paramList.add(toDate);
		}
		
		sql.append(" GROUP by requestPriority");
		
		result = super.select(sql.toString(), ReportObject.class, paramList.toArray(), 0, -1);
		
		return result;
	}
	
	public int getCommonStatsPriority(Date fromDate, Date toDate, String status, String priority) throws DaoException{
		
		Collection result = new ArrayList();
		Collection paramList = new ArrayList();
		StringBuffer sql = new StringBuffer("SELECT COUNT(requestId) as noOfReq FROM isr_request WHERE 1=1 ");
		
		if(fromDate != null){
			sql.append(" AND dateCreated >= ? ");
			paramList.add(fromDate);
		}
		
		if(toDate != null){
			sql.append(" AND dateCreated <= ? ");
			paramList.add(toDate);
		}
		
		if(status != null){
			sql.append(" AND requestStatus = ? ");
			paramList.add(status);
		}
		if(status != null){
			sql.append(" AND requestPriority = ? ");
			paramList.add(priority);
		}
		
		result = super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, 1);
		if(result.size()==1){
		HashMap map = (HashMap)result.iterator().next();
        return Integer.parseInt(map.get("noOfReq").toString());}
		else{
			return 0;
		}
	}
	
	public Collection selectPriorityReportListing(String fromDate, String toDate, String priority, String reqDept, String recDept, 
			String sort, boolean desc, int start, int row) throws DaoException{
		Collection result = new ArrayList();
		
		StringBuffer sql = new StringBuffer("SELECT r.requestPriorityByAdmin as priorityByAdmin, " +
				"concat(c1.shortDesc, '-', d1.shortDesc) AS recDept, " +
				"concat(c2.shortDesc, '-', d2.shortDesc) AS reqDept, " +
				"count(requestId) AS noOfReq, " +
				"concat(r.requestFromDept, '_', r.requestToDept, '_', r.requestPriorityByAdmin) as reportLink " +
				"FROM isr_request r " +
				"LEFT OUTER JOIN org_chart_dept_country dc1 ON dc1.associativityId = r.requestToDept " +
				"LEFT OUTER JOIN org_chart_department d1 ON dc1.deptCode = d1.code " +
				"LEFT OUTER JOIN org_chart_country c1 ON dc1.countryCode = c1.code " +
				"LEFT OUTER JOIN org_chart_dept_country dc2 ON dc2.associativityId = r.requestFromDept " +
				"LEFT OUTER JOIN org_chart_department d2 ON dc2.deptCode = d2.code " +
				"LEFT OUTER JOIN org_chart_country c2 ON dc2.countryCode = c2.code " +
				"WHERE 1=1 ");
		
		if(fromDate != null && fromDate.trim().length() > 0){
			sql.append(" AND r.dateCreated >= '" + fromDate + " 00:00:00' ");
		}
		
		if(toDate != null && toDate.trim().length() > 0){
			sql.append(" AND r.dateCreated <= '" + toDate + " 23:59:00' ");
		}
		
		if(priority != null && priority.trim().length() > 0){
			sql.append(" AND r.requestPriorityByAdmin = '" + priority + "' ");
		}
		
		if(reqDept != null && reqDept.trim().length() > 0){
			sql.append(" AND r.requestFromDept = '" + reqDept + "' ");
		}
		
		if(recDept != null && recDept.trim().length() > 0){
			sql.append(" AND r.requestToDept = '" + recDept + "' ");
		}
		
		sql.append(" GROUP BY r.requestPriorityByAdmin, r.requestToDept, r.requestFromDept ");
		
		if(sort != null && sort.trim().length() > 0){
			sql.append(" ORDER BY " + sort);
		}else{
			sql.append(" ORDER BY c2.shortDesc ");
		}
		
		if(desc)
			sql.append(" DESC");
		
		result = super.select(sql.toString(), ReportObject.class, null, start, row);
		
		return result;
	}
	
	public Collection selectStatusReportListing(String fromDate, String toDate, String status, String reqDept, String recDept, 
			String sort, boolean desc, int start, int row) throws DaoException{
		Collection result = new ArrayList();
		
		StringBuffer sql = new StringBuffer("SELECT s.statusName AS status, s.statusId, " +
				"concat(c1.shortDesc, '-', d1.shortDesc) AS recDept, r.requestToDept as recDeptId, " +
				"concat(c2.shortDesc, '-', d2.shortDesc) AS reqDept, r.requestFromDept as reqDeptId, " +
				"count(requestId) AS noOfReq, " +
				"concat(r.requestFromDept, '_', r.requestToDept, '_', s.statusId) as reportLink " +
				"FROM isr_request r " +
				"LEFT OUTER JOIN isr_status s ON r.requestStatus = s.statusId " +
				"LEFT OUTER JOIN org_chart_dept_country dc1 ON dc1.associativityId = r.requestToDept " +
				"LEFT OUTER JOIN org_chart_department d1 ON dc1.deptCode = d1.code " +
				"LEFT OUTER JOIN org_chart_country c1 ON dc1.countryCode = c1.code " +
				"LEFT OUTER JOIN org_chart_dept_country dc2 ON dc2.associativityId = r.requestFromDept " +
				"LEFT OUTER JOIN org_chart_department d2 ON dc2.deptCode = d2.code " +
				"LEFT OUTER JOIN org_chart_country c2 ON dc2.countryCode = c2.code " +
				"WHERE 1=1 ");
		
		if(fromDate != null && fromDate.trim().length() > 0){
			sql.append(" AND r.dateCreated >= '" + fromDate + " 00:00:00' ");
		}
		
		if(toDate != null && toDate.trim().length() > 0){
			sql.append(" AND r.dateCreated <= '" + toDate + " 23:59:00' ");
		}
		
		if(status != null && status.trim().length() > 0){
			sql.append(" AND r.requestStatus = '" + status + "' ");
		}
		
		if(reqDept != null && reqDept.trim().length() > 0){
			sql.append(" AND r.requestFromDept = '" + reqDept + "' ");
		}
		
		if(recDept != null && recDept.trim().length() > 0){
			sql.append(" AND r.requestToDept = '" + recDept + "' ");
		}
		
		sql.append(" GROUP BY r.requestStatus, r.requestToDept, r.requestFromDept ");
		
		if(sort != null && sort.trim().length() > 0){
			sql.append(" ORDER BY " + sort);
		}else{
			sql.append(" ORDER BY recDept ");
		}
		
		if(desc)
			sql.append(" DESC");
		
		result = super.select(sql.toString(), ReportObject.class, null, start, row);
		
		return result;
	}
	
}
