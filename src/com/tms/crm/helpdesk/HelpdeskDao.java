package com.tms.crm.helpdesk;

import kacang.model.DataSourceDao;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorIn;
import kacang.model.operator.DaoOperator;
import kacang.util.JdbcUtil;
import kacang.util.Log;
import kacang.services.security.*;
import kacang.services.security.SecurityException;
import kacang.Application;

import java.util.*;

import org.apache.commons.collections.SequencedHashMap;
import org.apache.commons.beanutils.PropertyUtils;

public class HelpdeskDao extends DataSourceDao
{
	public void init() throws DaoException
	{
		try {
		super.update("ALTER TABLE `hdk_incident` CHANGE `createdBy` `createdBy` VARCHAR(100)",null);
		 }
        catch(Exception e) {}
		try {
			super.update("ALTER TABLE hdk_incident_log ADD receipient TEXT DEFAULT ''",null);
           
        }
        catch(Exception e) {}
		
		super.update("CREATE TABLE hdk_incident(incidentId VARCHAR(35) NOT NULL, created DATETIME NOT NULL, " +
			"lastModified DATETIME, dateResolved DATETIME, " +
            "createdBy VARCHAR(100), lastModifiedBy VARCHAR(50), resolvedBy VARCHAR(50), " +
            "incidentCode INT NOT NULL AUTO_INCREMENT, severity VARCHAR(255) NOT NULL, " +
			"resolutionState VARCHAR(255) NOT NULL, companyId VARCHAR(35), companyName VARCHAR(255), " +
            "contactId VARCHAR(35), contactFirstName VARCHAR(255), contactLastName VARCHAR(255), contactEmail VARCHAR(255), " +
			"contactedBy VARCHAR (250) NOT NULL, incidentType VARCHAR(250) NOT NULL, productId VARCHAR(35), " +
			"productFeatures TEXT, subject VARCHAR(250) NOT NULL, description TEXT, property1 VARCHAR(250), " +
			"property2 VARCHAR(250), property3 VARCHAR(250), property4 VARCHAR(250), property5 VARCHAR(250), " +
			"property6 VARCHAR(250), attachmentPath VARCHAR(250), resolution TEXT, resolved CHAR(1) NOT NULL, " +
			"PRIMARY KEY(incidentId), KEY(incidentCode))", null);
		super.update("CREATE TABLE hdk_incident_log(logId VARCHAR(35) NOT NULL, incidentId VARCHAR(35) NOT NULL, " +
			"incidentDate DATETIME NOT NULL, action VARCHAR(250) NOT NULL, resolutionState VARCHAR(255), user VARCHAR(250) NOT NULL," +
            " userId VARCHAR(250), receipient TEXT NOT NULL DEFAULT '', PRIMARY KEY(logId))", null);
		super.update("CREATE TABLE hdk_product(productId VARCHAR(35) NOT NULL, productName VARCHAR(250) NOT NULL," +
			"description TEXT, productFeatures TEXT, PRIMARY KEY(productId))", null);
		super.update("CREATE TABLE hdk_product_owner(productID VARCHAR(35) NOT NULL, userId VARCHAR(250) NOT NULL, " +
			"PRIMARY KEY(productId, userId))", null);
        super.update("CREATE TABLE hdk_settings(property VARCHAR(255) NOT NULL, value TEXT)", null);
        super.update("INSERT INTO hdk_settings(property, value) VALUES (?,?)", new Object[] {
            "severityOptions",
                "Normal\n" +
                "Trivial\n" +
                "Minor\n" +
                "Major\n" +
                "Critical\n" +
                "Blocker"
        });
        super.update("INSERT INTO hdk_settings(property, value) VALUES (?,?)", new Object[] {
            "resolutionStateOptions",
                "Fixed\n" +
                "Won't Fix\n" +
                "Duplicate\n" +
                "Incomplete\n" +
                "Cannot Reproduce\n" +
                "Invalid\n" +
                "Resolved Locally"
        });
        super.update("INSERT INTO hdk_settings(property, value) VALUES (?,?)", new Object[] {
            "contactedByOptions",
                "Email\n" +
                "Phone\n" +
                "Fax\n" +
                "Visit"
        });
        super.update("INSERT INTO hdk_settings(property, value) VALUES (?,?)", new Object[] {
            "incidentTypeOptions",
                "Bug\n" +
                "New Feature\n" +
                "Improvement"
        });
	}

	/* Insertions */
    public void insertIncident(Incident incident) throws DaoException
	{
        // NOTE: incidentCode must be auto_increment
		super.update("INSERT INTO hdk_incident(incidentId, created, lastModified, dateResolved, createdBy, lastModifiedBy, resolvedBy, incidentCode, severity, " +
			"resolutionState, companyId, companyName, contactId, contactFirstName, contactLastName, contactEmail, contactedBy, incidentType, productId, productFeatures, subject, " +
			"description, property1, property2, property3, property4, property5, property6, attachmentPath, " +
			"resolution, resolved) VALUES(#incidentId#, #created#, #lastModified#, #dateResolved#, #createdBy#, #lastModifiedBy#, #resolvedBy#, #incidentCode#, #severity#, " +
			"#resolutionState#, #companyId#, #companyName#, #contactId#, #contactFirstName#, #contactLastName#, #contactEmail#, #contactedBy#, #incidentType#, #productId#, #productFeatures#, " +
			"#subject#, #description#, #property1#, #property2#, #property3#, #property4#, #property5#, " +
			"#property6#, #attachmentPath#, #resolution#, #resolved#)", incident);
		for (Iterator i = incident.getLogs().iterator(); i.hasNext();)
            insertIncidentLog((IncidentLog) i.next());
	}

	public void insertIncidentLog(IncidentLog log) throws DaoException
	{
        super.update("INSERT INTO hdk_incident_log(logId, incidentId, incidentDate, action, resolutionState, user, userId, receipient) VALUES(#logId#, #incidentId#, " +
			"#incidentDate#, #action#, #resolutionState#, #user#, #userId#, #receipient#)", log);
	}

    public void insertProduct(Product product) throws DaoException
	{
		super.update("INSERT INTO hdk_product(productId, productName, description, productFeatures) VALUES(#productId#, " +
			"#productName#, #description#, #productFeatures#)", product);
		insertProductOwner(product);
	}

	public void insertProductOwner(Product product)throws DaoException
	{
		for (Iterator i = product.getOwners().iterator(); i.hasNext();)
		{
			User user = (User) i.next();
			super.update("INSERT INTO hdk_product_owner(productId, userId) VALUES(?, ?)",
				new Object[] {product.getProductId(), user.getId()});
		}
	}

	/* Updates */
	public void updateIncident(Incident incident) throws DaoException
	{
		super.update("UPDATE hdk_incident SET created=#created#, lastModified=#lastModified#, dateResolved=#dateResolved#, " +
            "createdBy=#createdBy#, lastModifiedBy=#lastModifiedBy#, resolvedBy=#resolvedBy#, " +
			"incidentCode=#incidentCode#, severity=#severity#, resolutionState=#resolutionState#, companyId=#companyId#, companyName=#companyName#, " +
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

	public void resetIncidentAlert(Incident incident) throws DaoException	
	{
		super.update("UPDATE hdk_incident_alert_count SET alertCounter='-1', alertTime=#alertTime# WHERE incidentId=#incidentId#", incident);
	}

	public void closeIncidentCounter(Incident incident) throws DaoException
	{
		super.update("UPDATE hdk_incident_alert_count SET alertCounter='0' WHERE incidentId=#incidentId#", incident);
	}

	public void updateProduct(Product product) throws DaoException
	{
		super.update("UPDATE hdk_product SET productName=#productName#, description=#description#, " +
			"productFeatures=#productFeatures# WHERE productId=#productId#", product);
		deleteProductOwner(product.getProductId());
		insertProductOwner(product);
	}

	/* Deletion */
	public void deleteIncident(String incidentId) throws DaoException
	{
        super.update("DELETE FROM hdk_incident WHERE incidentId=?", new Object[] {incidentId});
	}

	public void deleteIncidentLog(String logId) throws DaoException
	{
		super.update("DELETE FROM hdk_incident_log WHERE logId=?", new Object[] {logId});
	}

	public void deleteIncidentLogs(String incidentId) throws DaoException
	{
		super.update("DELETE FROM hdk_incident_log WHERE incidentId=?", new Object[] {incidentId});
	}

	public void deleteProduct(String productId) throws DaoException
	{
		super.update("DELETE FROM hdk_product WHERE productId=?", new Object[] {productId});
	}

	public void deleteProductOwner(String productId) throws DaoException
	{
		super.update("DELETE FROM hdk_product_owner WHERE productId=?", new Object[] {productId});
	}

	/* Selection */
	public Incident selectIncident(String incidentId) throws DaoException
	{
		Collection list = super.select("SELECT incidentId, created, lastModified, dateResolved, createdBy, lastModifiedBy, resolvedBy, incidentCode, severity, " +
			"resolutionState, companyId, companyName, contactId, contactFirstName, contactLastName, contactEmail, contactedBy, incidentType, productId, productFeatures, subject, " +
			"description, property1, property2, property3, property4, property5, property6, attachmentPath, " +
			"resolution, resolved FROM hdk_incident WHERE incidentId=?", Incident.class, new Object[] {incidentId}, 0, 1);
		if(list.size() > 0)
		{
			Incident incident = (Incident) list.iterator().next();
			incident.setLogs(selectIncidentLogs(incidentId));
			return incident;
		}
		return null;
	}

	public Collection selectIncidents() throws DaoException
	{
		Collection list = super.select("SELECT incidentId, created, lastModified, dateResolved, createdBy, lastModifiedBy, resolvedBy, incidentCode, severity, " +
			"resolutionState, companyId, companyName, contactId, contactFirstName, contactLastName, contactEmail, contactedBy, incidentType, productId, productFeatures, subject, " +
			"description, property1, property2, property3, property4, property5, property6, attachmentPath, " +
			"resolution, resolved FROM hdk_incident ORDER BY subject", Incident.class, null, 0, -1);
/*
		for (Iterator i = list.iterator(); i.hasNext();)
		{
			Incident incident = (Incident) i.next();
			incident.setLogs(selectIncidentLogs(incident.getIncidentId()));
		}
*/
		return list;
	}

	public Collection selectIncidents(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws DaoException
	{
		Collection list = super.select("SELECT incidentId, created, lastModified, dateResolved, createdBy, lastModifiedBy, resolvedBy, incidentCode, severity, " +
			"resolutionState, companyId, companyName, contactId, contactFirstName, contactLastName, contactEmail, contactedBy, incidentType, productId, productFeatures, subject, " +
			"description, property1, property2, property3, property4, property5, property6, attachmentPath, " +
			"resolution, resolved FROM hdk_incident WHERE 1=1 " + query.getStatement() + JdbcUtil.getSort(sort, descending),
			Incident.class, query.getArray(), start, maxResults);
/*
		for (Iterator i = list.iterator(); i.hasNext();)
		{
			Incident incident = (Incident) i.next();
			incident.setLogs(selectIncidentLogs(incident.getIncidentId()));
		}
*/
		return list;
	}

	public int selectIncidentsCount(DaoQuery query) throws DaoException
	{
		Collection list = super.select("SELECT COUNT(incidentId) as intCount FROM hdk_incident WHERE 1=1 " + query.getStatement(),
			HashMap.class, query.getArray(), 0, 1);
		for (Iterator i = list.iterator(); i.hasNext();)
		{
			HashMap map = (HashMap) i.next();
			return ((Number) map.get("intCount")).intValue();
		}
		return 0;
	}

	public Collection selectIncidentLogs(String incidentId) throws DaoException
	{
		return super.select("SELECT logId, incidentId, incidentDate, action, resolutionState, user, userId, receipient FROM hdk_incident_log WHERE " +
			"incidentId=? ORDER BY incidentDate",
			IncidentLog.class, new Object[] {incidentId}, 0, -1);
	}

	public Product selectProduct(String productId) throws DaoException
	{
		Collection list = super.select("SELECT productId, productName, description, productFeatures FROM hdk_product " +
			"WHERE productId=?", Product.class, new Object[] {productId}, 0, 1);
		if(list.size() > 0)
		{
			Product product = (Product) list.iterator().next();
			product.setOwners(selectProductOwners(product.getProductId()));
			return product;
		}
		return null;
	}

	public Collection selectProducts() throws DaoException
	{
		Collection list = super.select("SELECT productId, productName, description, productFeatures FROM hdk_product ORDER BY productName", Product.class,
			null, 0, -1);
		for (Iterator i = list.iterator(); i.hasNext();)
		{
			Product product = (Product) i.next();
			product.setOwners(selectProductOwners(product.getProductId()));
		}
		return list;
	}

	public Collection selectProducts(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws DaoException
	{
		Collection list = super.select("SELECT productId, productName, description, productFeatures FROM hdk_product WHERE 1=1 "
			+ query.getStatement() + JdbcUtil.getSort(sort, descending), Product.class, query.getArray(), start, maxResults);
		for (Iterator i = list.iterator(); i.hasNext();)
		{
			Product product = (Product) i.next();
			product.setOwners(selectProductOwners(product.getProductId()));
		}
		return list;
	}

	public Collection selectProductOwners(String productId) throws DaoException
	{
		Collection users = new ArrayList();
		Collection list = super.select("SELECT userId FROM hdk_product_owner WHERE productId=?", HashMap.class,
			new Object[] {productId}, 0, -1);
		Collection ids = new ArrayList();
		for (Iterator i = list.iterator(); i.hasNext();)
		{
			HashMap map = (HashMap) i.next();
            ids.add(map.get("userId"));
		}
		try
		{
            if (ids.size() > 0)
            {
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorIn("id", ids.toArray(), DaoOperator.OPERATOR_AND));
                users = service.getUsers(query, 0, -1, "firstName", false);
            }
		}
		catch (SecurityException e)
		{
			Log.getLog(getClass()).error("Error while retrieving product owners", e);
		}
		return users;
	}

	public int selectProductsCount(DaoQuery query) throws DaoException
	{
		Collection list = super.select("SELECT COUNT(productId) as intCount FROM hdk_product WHERE 1=1 " + query.getStatement(),
			HashMap.class, query.getArray(), 0, 1);
		for (Iterator i = list.iterator(); i.hasNext();)
		{
			HashMap map = (HashMap) i.next();
			return ((Number) map.get("intCount")).intValue();
		}
		return 0;
	}

	public Map selectDistinct(String property) throws DaoException
	{
		Map map = new SequencedHashMap();
		Collection list = super.select("SELECT DISTINCT " + property + " as property FROM hdk_incident WHERE " +
			" NOT (" + property + "='-1') ORDER BY property", HashMap.class, null, 0, -1);
		for (Iterator i = list.iterator(); i.hasNext();)
		{
			HashMap object = (HashMap) i.next();
			map.put(object.get("property"), object.get("property"));
		}
		return map;
	}

    public IncidentSettings selectSettings() throws DaoException
    {
        IncidentSettings settings = new IncidentSettings();
        Collection results = super.select("SELECT property, value FROM hdk_settings", HashMap.class, null, 0, -1);
        for (Iterator i=results.iterator(); i.hasNext();)
        {
            HashMap map = (HashMap)i.next();
            String property = (String)map.get("property");
            String value = (String)map.get("value");
            try
            {
                PropertyUtils.setProperty(settings, property, value);
            }
            catch (Exception e) {
                Log.getLog(getClass()).debug("Error setting property " + property, e);
            }
        }
        return settings;
    }

    public int updateSettings(IncidentSettings settings) throws DaoException
    {
        int i;
        String[] properties = settings.getProperties();
        super.update("DELETE FROM hdk_settings", null);
        for (i=0; i<properties.length; i++)
        {
            String prop = properties[i];
            String value = null;
            try {
                value = (String)PropertyUtils.getProperty(settings, prop);
            }
            catch (Exception e) {
                Log.getLog(getClass()).debug("Error getting property " + prop, e);
            }
            super.update("INSERT INTO hdk_settings(property, value) VALUES (?,?)", new Object[] { prop, value });
        }
        return i;
    }

	public void insertIncidentCounter(Notification notify) throws DaoException
	{
        super.update("INSERT INTO hdk_incident_alert_count(id, incidentId, alertCounter, alertTime) VALUES(#id#, #incidentId#, #counter#, #alertTime#)", notify);
	}

}
