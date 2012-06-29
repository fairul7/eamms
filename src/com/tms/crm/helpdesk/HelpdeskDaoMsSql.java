package com.tms.crm.helpdesk;

import java.util.Collection;
import java.util.Iterator;

import kacang.model.DaoException;


public class HelpdeskDaoMsSql extends HelpdeskDao{
	
	public void insertIncident(Incident incident) throws DaoException	{
        // NOTE: incidentCode must be auto_increment
		super.update("INSERT INTO hdk_incident(incidentId, created, lastModified, dateResolved, createdBy, lastModifiedBy, resolvedBy, severity, " +
			"resolutionState, companyId, companyName, contactId, contactFirstName, contactLastName, contactEmail, contactedBy, incidentType, productId, productFeatures, subject, " +
			"description, property1, property2, property3, property4, property5, property6, attachmentPath, " +
			"resolution, resolved) VALUES(#incidentId#, #created#, #lastModified#, #dateResolved#, #createdBy#, #lastModifiedBy#, #resolvedBy#, #severity#, " +
			"#resolutionState#, #companyId#, #companyName#, #contactId#, #contactFirstName#, #contactLastName#, #contactEmail#, #contactedBy#, #incidentType#, #productId#, #productFeatures#, " +
			"#subject#, #description#, #property1#, #property2#, #property3#, #property4#, #property5#, " +
			"#property6#, #attachmentPath#, #resolution#, #resolved#)", incident);
		for (Iterator i = incident.getLogs().iterator(); i.hasNext();)
            insertIncidentLog((IncidentLog) i.next());
	}
	
	public void updateIncident(Incident incident) throws DaoException
	{
		super.update("UPDATE hdk_incident SET created=#created#, lastModified=#lastModified#, dateResolved=#dateResolved#, " +
            "createdBy=#createdBy#, lastModifiedBy=#lastModifiedBy#, resolvedBy=#resolvedBy#, " +
			"severity=#severity#, resolutionState=#resolutionState#, companyId=#companyId#, companyName=#companyName#, " +
			"contactId=#contactId#, contactFirstName=#contactFirstName#, contactLastName=#contactLastName#, contactEmail=#contactEmail#, " +
            "contactedBy=#contactedBy#, incidentType=#incidentType#, productId=#productId#, " +
			"productFeatures=#productFeatures#, subject=#subject#, description=#description#, property1=#property1#, " +
			"property2=#property2#, property3=#property3#, property4=#property4#, property5=#property5#, " +
			"property6=#property6#, attachmentPath=#attachmentPath#, resolution=#resolution#, resolved=#resolved# " +
			"WHERE incidentId=#incidentId#", incident);
		deleteIncidentLogs(incident.getIncidentId());
		for (Iterator i = incident.getLogs().iterator(); i.hasNext();)
            insertIncidentLog((IncidentLog) i.next());
	}
	
	public void insertIncidentLog(IncidentLog log) throws DaoException
	{
        super.update("INSERT INTO hdk_incident_log(logId, incidentId, incidentDate, action, resolutionState, \"user\", userId) VALUES(#logId#, #incidentId#, " +
			"#incidentDate#, #action#, #resolutionState#, #user#, #userId#)", log);
	}
	
	public Collection selectIncidentLogs(String incidentId) throws DaoException
	{
		return super.select("SELECT logId, incidentId, incidentDate, action, resolutionState, \"user\", userId FROM hdk_incident_log WHERE " +
			"incidentId=? ORDER BY incidentDate",
			IncidentLog.class, new Object[] {incidentId}, 0, -1);
	}
	
}
