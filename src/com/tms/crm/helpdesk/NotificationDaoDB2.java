package com.tms.crm.helpdesk;

import java.util.Collection;
import java.util.Date;

import kacang.model.DaoException;
import kacang.util.Log;


public class NotificationDaoDB2 extends NotificationDao{
	
	public void init() throws DaoException {
		try {
			String createSql = "CREATE TABLE hdk_notification_setup (" +
					"  id varchar(100) NOT NULL default '0'," +
					"  createdBy varchar(100) default '0'," +
					"  createdOn timestamp ," +
					"  firstAlert varchar(20) default '0'," +
					"  subsequentAlert varchar(20) default '0'," +
					"  alertOccurance varchar(20) default '0'," +
					"  method1 varchar(20) default NULL," +
					"  method2 varchar(20) default NULL," +
					"  PRIMARY KEY  (id)" +
					")";
			super.update(createSql, null);

			super.update("INSERT INTO hdk_notification_setup (id, createdBy, createdOn, firstAlert, " +
					"subsequentAlert, alertOccurance, method1, method2) " +
					"VALUES('6cea120d-c0a8c894-13c1b690-8d350be1'," +
					"'kacang.services.security.User_e53cbea0-c0a8c860-1abf8700-d57cad4c', " +
					"?, '0', '0', '0', 'memo', 'email')", new Object[]{new Date()});
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString());
		}

		try {

			String createSql1 = "CREATE TABLE hdk_incident_alert_count (" +
					"  id varchar(255) NOT NULL default '0', " +
					"  incidentId varchar(100) default '0', " +
					"  alertCounter varchar(5) default '0', " +
					"  alertTime timestamp default NULL, " +
					"  PRIMARY KEY  (id) " +
					")";
			super.update(createSql1, null);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString());
		}
	}
	
	public Collection selectIncidents() throws DaoException {
		String sql = " select i.incidentId AS incidentId, i.created AS createdOn, i.subject AS subject, i.description AS description, " +
				" i.incidentCode AS incidentCode, i.companyName AS companyName, i.contactFirstName AS firstName, " +
				" i.contactLastName AS lastName, i.contactedBy AS contactedBy, i.incidentType AS incidentType, i.severity AS severity,  " +
				" i.companyId, i.contactId, p.productName AS productName, ac.alertTime AS alertTime, su.username AS username, " +
				" su.firstName AS firstName, su.lastName AS lastName, su.email1 AS email, " +
				" ac.alertCounter AS counter FROM hdk_incident i INNER JOIN security_user su ON i.createdBy=su.id " +
				" INNER JOIN hdk_incident_alert_count ac ON i.incidentId=ac.incidentId " +
				" INNER JOIN hdk_product p ON i.productId=p.productId " +
				" WHERE i.resolved='0' AND ac.alertCounter <> '0' ";
		return super.select(sql, Notification.class, null, 0, -1);
	}
	
}
