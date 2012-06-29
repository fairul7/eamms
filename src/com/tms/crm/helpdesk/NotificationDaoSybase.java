package com.tms.crm.helpdesk;

import kacang.model.*;
import kacang.util.*;
import java.util.*;

public class NotificationDaoSybase extends NotificationDaoMsSql {
	public void init() throws DaoException {
		try {
			String createSql = "CREATE TABLE hdk_notification_setup (" +
					"  id varchar(100) NOT NULL," +
					"  createdBy varchar(100) default '0' NULL," +
					"  createdOn datetime NULL," +
					"  firstAlert varchar(20) default '0' NULL," +
					"  subsequentAlert varchar(20) default '0' NULL," +
					"  alertOccurance varchar(20) default '0' NULL," +
					"  method1 varchar(20) NULL," +
					"  method2 varchar(20) NULL," +
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
					"  id varchar(255) NOT NULL, " +
					"  incidentId varchar(100) default '0' NULL, " +
					"  alertCounter varchar(5) default '0' NULL, " +
					"  alertTime datetime NULL, " +
					"  PRIMARY KEY  (id) " +
					")";
			super.update(createSql1, null);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString());
		}
		
		try {
			Collection col = super.select("select i.incidentId AS incidentId FROM hdk_incident i LEFT JOIN hdk_incident_alert_count ac ON i.incidentId=ac.incidentId WHERE ac.incidentId IS NULL", HashMap.class, null, 0, -1);
			for (Iterator mapIt = col.iterator(); mapIt.hasNext();) {
				HashMap map = (HashMap) mapIt.next();
				UuidGenerator uuid = UuidGenerator.getInstance();
				super.update("INSERT INTO hdk_incident_alert_count(id, incidentId, alertCounter, alertTime) VALUES('"+uuid.getUuid()+"', '"+map.get("incidentId").toString()+"', '0', getDate())", null);
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString());
		}
	}
}
