package com.tms.crm.helpdesk;

import kacang.model.DataSourceDao;
import kacang.model.DaoException;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Date;

public class NotificationDao extends DataSourceDao {

	public void init() throws DaoException {
		try {
			String createSql = "CREATE TABLE hdk_notification_setup (" +
					"  id varchar(100) NOT NULL default '0'," +
					"  createdBy varchar(100) default '0'," +
					"  createdOn datetime default '0000-00-00 00:00:00'," +
					"  firstAlert varchar(20) default '0'," +
					"  subsequentAlert varchar(20) default '0'," +
					"  alertOccurance varchar(20) default '0'," +
					"  method1 varchar(20) default NULL," +
					"  method2 varchar(20) default NULL," +
					"  PRIMARY KEY  (id)" +
					") TYPE=MyISAM;";
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
					"  alertTime datetime default NULL, " +
					"  PRIMARY KEY  (id) " +
					") TYPE=MyISAM;";
			super.update(createSql1, null);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString());
		}
		
		try {
		Collection col = super.select("select i.incidentId AS incidentId FROM hdk_incident i LEFT JOIN hdk_incident_alert_count ac ON i.incidentId=ac.incidentId WHERE ac.incidentId IS NULL", HashMap.class, null, 0, -1);
		for (Iterator mapIt = col.iterator(); mapIt.hasNext();) {
			HashMap map = (HashMap) mapIt.next();
			UuidGenerator uuid = UuidGenerator.getInstance();
			super.update("INSERT INTO hdk_incident_alert_count(id, incidentId, alertCounter, alertTime) VALUES('"+uuid.getUuid()+"', '"+map.get("incidentId").toString()+"', '0', now())", null);

		}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString());
		}
	}


	public void insertNotification(Notification alert) throws Exception {
		String sql = "INSERT INTO hdk_notification_setup" +
				"(id, createdBy, createdOn, firstAlert, subsequentAlert, alertOccurance, method1, method2) " +
				"VALUES(#id#,#userId#,#createdOn#,#firstAlert#,#subsequentAlert#,#alertOccurance#,#method1#,#method2#)";
		super.update(sql, alert);
	}

	public int dispalyAlertSettingCount() throws DaoException {
		String sql = "SELECT  COUNT(*) as total FROM hdk_notification_setup";
		HashMap row = (HashMap) super.select(sql, HashMap.class, null, 0, -1).iterator().next();
		Number number = (Number) row.get("total");
		return number.intValue();
	}

	public Collection selectIncidents() throws DaoException {
		String sql = " select i.incidentId AS incidentId, i.created AS createdOn, i.subject AS subject, i.description AS description, " +
				" i.incidentCode AS incidentCode, i.companyName AS companyName, i.contactFirstName AS contactFirstName, " +
				" i.contactLastName AS contactLastName, i.contactedBy AS contactedBy, i.incidentType AS incidentType, i.severity AS severity,  " +
				" i.companyId, i.contactId, p.productName AS productName, ac.alertTime AS alertTime, su.username AS username, " +
				" su.firstName AS firstName, su.lastName AS lastName, su.email1 AS email, " +
				" ac.alertCounter AS counter FROM hdk_incident i INNER JOIN security_user su ON i.createdBy=su.id " +
				" INNER JOIN hdk_incident_alert_count ac ON i.incidentId=ac.incidentId " +
				" LEFT JOIN hdk_product p ON i.productId=p.productId " +
				" WHERE i.resolved = '0' AND ac.alertCounter <> '0' ";
		return super.select(sql, Notification.class, null, 0, -1);
	}

	public Notification getAlertSettings() throws DaoException {
		Collection col = super.select("SELECT id AS id, createdBy AS userId, createdOn AS createdOn, firstAlert AS firstAlert, " +
				" subsequentAlert AS subsequentAlert, alertOccurance AS alertOccurance, method1 AS method1 , method2 AS method2" +
				" FROM hdk_notification_setup", Notification.class, null, 0, 1);

		Notification cd = null;
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			cd = (Notification) iterator.next();
		}
		return (cd);
	}

	public void updateIndcidentCounter(String occurance, Date currentTime, String indcidentId) throws Exception {
		super.update("UPDATE hdk_incident_alert_count SET alertCounter = '" + occurance + "' , alertTime = ? WHERE incidentId='" + indcidentId + "'", new Object[]{currentTime});
	}

	public Notification getIncidentAlert(String incidentId) throws DaoException {
		Collection col = super.select("SELECT alertCounter AS counter FROM hdk_incident_alert_count WHERE incidentId = '" + incidentId + "'", Notification.class, null, 0, 1);

		Notification cd = null;
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			cd = (Notification) iterator.next();
		}
		return (cd);
	}

	public Notification getLastAlert(String incidentId) throws DaoException {
		Collection col = super.select("SELECT alertTime FROM hdk_incident_alert_count WHERE incidentId = '" + incidentId + "'", Notification.class, null, 0, 1);
		Notification cd = null;
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			cd = (Notification) iterator.next();
		}
		return (cd);
	}

	public void updateAlertSetting(Notification alert) throws Exception {
		super.update("UPDATE hdk_notification_setup SET createdBy=#userId#, " +
				"createdOn=#createdOn#, " +
				"firstAlert=#firstAlert#, " +
				"subsequentAlert=#subsequentAlert#, " +
				"alertOccurance=#alertOccurance#, " +
				"method1=#method1#, " +
				"method2=#method2# WHERE id=#id#", alert);
	}

	public Collection userList(String sort, boolean desc, int start, int rows) throws DaoException {
		String sql = " SELECT id AS id, username AS username, firstName AS firstName, lastName AS lastName, email1 AS email " +
				" FROM security_user";
		if (sort != null && !sort.equals("")) {
			sql = sql + " ORDER BY '" + sort + "'";
			if (desc) {
				sql = sql + " DESC";
			}
		}
		return super.select(sql, Notification.class, null, start, rows);
	}


	public Notification getContactDetails(String id) throws DaoException {
		Collection col = super.select("SELECT id AS id, username AS username, email1 AS email FROM security_user WHERE id='" + id + "'", Notification.class, null, 0, 1);
		Notification cd = null;
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			cd = (Notification) iterator.next();
		}
		return (cd);
	}

	public Notification getIncident(String incidentId) throws DaoException {
		Collection col = super.select(" select i.incidentId AS incidentId, i.created AS createdOn, i.subject AS subject, i.description AS description, " +
				" i.incidentCode AS incidentCode, i.companyName AS companyName, i.contactFirstName AS contactFirstName, " +
				" i.contactLastName AS contactLastName, i.contactedBy AS contactedBy, i.incidentType AS incidentType, i.severity AS severity,  " +
				" i.companyId, i.contactId, p.productName AS productName, ac.alertTime AS alertTime, su.username AS username, " +
				" su.firstName AS firstName, su.lastName AS lastName, su.email1 AS email, " +
				" ac.alertCounter AS counter FROM hdk_incident i LEFT JOIN security_user su ON i.createdBy=su.id " +
				" INNER JOIN hdk_incident_alert_count ac ON i.incidentId=ac.incidentId " +
				" LEFT JOIN hdk_product p ON i.productId=p.productId " +
				" WHERE i.incidentId=?", Notification.class, new Object[]{incidentId}, 0, -1);
		Notification cd = null;
		if (col.size() == 1) {
			Iterator iterator = col.iterator();
			cd = (Notification) iterator.next();
		}
		return (cd);
	}

	public Collection getOwner(String incidentId) throws DaoException {
		String sql = "select su.id AS userId, su.firstName AS firstName,su.lastName AS lastName," +
				"su.username AS username, su.email1 AS email FROM security_user su " +
				"INNER JOIN hdk_product_owner po ON su.id=po.userId " +
				"INNER JOIN hdk_product p ON po.productID=p.productId " +
				"INNER JOIN hdk_incident i ON p.productID=i.productId " +
				"WHERE i.incidentId='" + incidentId + "'";
		return super.select(sql, Notification.class, null, 0, -1);
	}
}