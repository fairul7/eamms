package com.tms.crm.helpdesk;

import kacang.model.DaoException;

import java.util.Collection;

public class HelpdeskDaoOracle extends HelpdeskDao
{
	public void insertIncidentLog(IncidentLog log) throws DaoException
	{
        super.update("INSERT INTO hdk_incident_log(logId, incidentId, incidentDate, action, resolutionState, \"USER\", userId) VALUES(#logId#, #incidentId#, " +
			"#incidentDate#, #action#, #resolutionState#, #user#, #userId#)", log);
	}

	public Collection selectIncidentLogs(String incidentId) throws DaoException
	{
		return super.select("SELECT logId, incidentId, incidentDate, action, resolutionState, \"USER\", userId FROM hdk_incident_log WHERE " +
			"incidentId=? ORDER BY incidentDate",
			IncidentLog.class, new Object[] {incidentId}, 0, -1);
	}
}
