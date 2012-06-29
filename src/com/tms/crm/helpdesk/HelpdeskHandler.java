package com.tms.crm.helpdesk;

import kacang.model.DaoQuery;
import kacang.model.DefaultModule;
import kacang.Application;
import kacang.util.UuidGenerator;
import kacang.util.Log;
import kacang.services.security.User;
import kacang.services.storage.StorageService;
import kacang.services.storage.StorageFile;

import java.util.*;
import java.text.DecimalFormat;
import java.text.DateFormat;

import com.tms.collab.messaging.model.*;
import org.apache.commons.collections.SequencedHashMap;
import org.apache.commons.beanutils.PropertyUtils;

public class HelpdeskHandler extends DefaultModule
{
	public static final String ACTION_CREATE = "Created";
	public static final String ACTION_UPDATE = "Updated";
	public static final String ACTION_ESCALATE = "Escalated";
	public static final String ACTION_RESOLVE = "Resolved";
	public static final String ACTION_REOPEN = "Reopened";

	public static final String PERMISSION_HELPDESK_USER = "com.tms.crm.helpdesk.User";
	public static final String PERMISSION_HELPDESK_ADMIN = "com.tms.crm.helpdesk.Admin";

    public static final String ATTACHMENT_STORAGE_PATH = "/helpdesk/incidents/";

	public void addIncident(Incident incident, User user) throws HelpdeskException
	{
		try
		{
            // set values
            incident.setCreated(new Date());
            incident.setCreatedBy(user.getId());
            incident.setLastModified(new Date());
            incident.setLastModifiedBy(user.getId());
            incident.setResolved(false);
            incident.setResolvedBy(null);

            // add log entry
            IncidentLog log = generateLog(incident.getIncidentId(), HelpdeskHandler.ACTION_CREATE, null, user);
            incident.getLogs().add(log);

            // save
			HelpdeskDao dao = (HelpdeskDao) getDao();
			dao.insertIncident(incident);
		}
		catch (Exception e)
		{
			throw new HelpdeskException("Error while adding incident " + incident.getIncidentId(), e);
		}
	}

    protected IncidentLog generateLog(String incidentId, String action, String resolution, User user)
    {
        Calendar calendar = Calendar.getInstance();
        IncidentLog log = new IncidentLog();
        log.setLogId(UuidGenerator.getInstance().getUuid());
        log.setIncidentId(incidentId);
        log.setIncidentDate(calendar.getTime());
        log.setAction(action);
        log.setResolutionState(resolution);
        log.setUser(user.getName());
        log.setUserId(user.getId());
        return log;
    }
    
    protected IncidentLog generateEscalationLog(String incidentId, String action, String resolution, User user, String receipients)
    {
        Calendar calendar = Calendar.getInstance();
        IncidentLog log = new IncidentLog();
        log.setLogId(UuidGenerator.getInstance().getUuid());
        log.setIncidentId(incidentId);
        log.setIncidentDate(calendar.getTime());
        log.setAction(action);
        log.setResolutionState(resolution);
        log.setUser(user.getName());
        log.setUserId(user.getId());
        log.setReceipient(receipients);
        return log;
    }

	public void addProduct(Product product) throws HelpdeskException
	{
		try
		{
			HelpdeskDao dao = (HelpdeskDao) getDao();
			dao.insertProduct(product);
		}
		catch (Exception e)
		{
			throw new HelpdeskException("Error while adding product " + product.getProductId(), e);
		}
	}

	public void addIncidentLog(IncidentLog log) throws HelpdeskException
	{
		try
		{
			HelpdeskDao dao = (HelpdeskDao) getDao();
			dao.insertIncidentLog(log);
		}
		catch (Exception e)
		{
			throw new HelpdeskException("Error while adding incident log " + log.getLogId(), e);
		}
	}

    public void updateIncident(Incident incident, User user) throws HelpdeskException
	{
		try
		{
            // set values
            incident.setLastModified(new Date());
            incident.setLastModifiedBy(user.getId());

            // add log entry
            IncidentLog log = generateLog(incident.getIncidentId(), HelpdeskHandler.ACTION_UPDATE, null, user);
            incident.getLogs().add(log);

            // save
			HelpdeskDao dao = (HelpdeskDao) getDao();
			dao.updateIncident(incident);
		}
		catch (Exception e)
		{
			throw new HelpdeskException("Error while updating incident " + incident.getIncidentId(), e);
		}
	}

	public void resetIncidentAlert(Incident incident) throws HelpdeskException
	{
		try
		{   // reset
			HelpdeskDao dao = (HelpdeskDao) getDao();
			dao.resetIncidentAlert(incident);
		} catch (Exception e) {
			throw new HelpdeskException("Error while resetting incident alert " + incident.getIncidentId(), e);
		}
	}

	public void closeIncidentCounter(Incident incident) throws HelpdeskException
	{
		try
		{   // closing
			HelpdeskDao dao = (HelpdeskDao) getDao();
			dao.closeIncidentCounter(incident);
		} catch (Exception e) {
			throw new HelpdeskException("Error while resetting incident alert " + incident.getIncidentId(), e);
		}
	}

    public void escalateIncident(Incident incident, User user, String receipients) throws HelpdeskException
	{
		try
		{
            // set values
            incident.setLastModified(new Date());
            incident.setLastModifiedBy(user.getId());

            // add log entry
            IncidentLog log = generateEscalationLog(incident.getIncidentId(), HelpdeskHandler.ACTION_ESCALATE, null, user, receipients);
            incident.getLogs().add(log);

            // save
			HelpdeskDao dao = (HelpdeskDao) getDao();
			dao.updateIncident(incident);

            // notify product owners
            //sendIncidentNotification(incident, log, user.getUsername());
		}
		catch (Exception e)
		{
			throw new HelpdeskException("Error while updating incident " + incident.getIncidentId(), e);
		}
	}

    public void resolveIncident(Incident incident, User user) throws HelpdeskException
	{
		try
		{
            // set values
            incident.setLastModified(new Date());
            incident.setLastModifiedBy(user.getId());
            incident.setDateResolved(new Date());
            incident.setResolved(true);
            incident.setResolvedBy(user.getId());

            // add log entry
            IncidentLog log = generateLog(incident.getIncidentId(), HelpdeskHandler.ACTION_RESOLVE, incident.getResolutionState(), user);
            incident.getLogs().add(log);

            // save
			HelpdeskDao dao = (HelpdeskDao) getDao();
			dao.updateIncident(incident);
		}
		catch (Exception e)
		{
			throw new HelpdeskException("Error while resolving incident " + incident.getIncidentId(), e);
		}
	}

    public void reopenIncident(Incident incident, User user) throws HelpdeskException
	{
		try
		{
            // set values
            incident.setLastModified(new Date());
            incident.setLastModifiedBy(user.getId());
            incident.setDateResolved(null);
            incident.setResolved(false);
            incident.setResolvedBy(null);
            incident.setResolutionState("1");

            // add log entry
            IncidentLog log = generateLog(incident.getIncidentId(), HelpdeskHandler.ACTION_REOPEN, null, user);
            incident.getLogs().add(log);

            // save
			HelpdeskDao dao = (HelpdeskDao) getDao();
			dao.updateIncident(incident);
		}
		catch (Exception e)
		{
			throw new HelpdeskException("Error while resolving incident " + incident.getIncidentId(), e);
		}
	}

	public void updateProduct(Product product) throws HelpdeskException
	{
		try
		{
			HelpdeskDao dao = (HelpdeskDao) getDao();
			dao.updateProduct(product);
		}
		catch (Exception e)
		{
			throw new HelpdeskException("Error while updating product " + product.getProductId(), e);
		}
	}

	public void deleteIncident(String incidentId) throws HelpdeskException
	{
		try
		{
            if (incidentId != null && incidentId.trim().length() > 0)
            {
                HelpdeskDao dao = (HelpdeskDao) getDao();
                dao.deleteIncident(incidentId);

                // delete attachments
                try
                {
                    StorageService ss = (StorageService)Application.getInstance().getService(StorageService.class);
                    StorageFile path = new StorageFile(HelpdeskHandler.ATTACHMENT_STORAGE_PATH + incidentId);
                    ss.delete(path);
                }
                catch (Exception e) {
                    Log.getLog(getClass()).error("Error deleting attachments for incident " + incidentId, e);
                }
            }

		}
		catch (Exception e)
		{
			throw new HelpdeskException("Error while deleting incident " + incidentId, e);
		}
	}

	public void deleteProduct(String productId) throws HelpdeskException
	{
		try
		{
			HelpdeskDao dao = (HelpdeskDao) getDao();
			dao.deleteProduct(productId);
		}
		catch (Exception e)
		{
			throw new HelpdeskException("Error while deleting product " + productId, e);
		}
	}

	public Incident getIncident(String incidentId) throws HelpdeskException
	{
		try
		{
			HelpdeskDao dao = (HelpdeskDao) getDao();
			return dao.selectIncident(incidentId);
		}
		catch (Exception e)
		{
			throw new HelpdeskException("Error while retrieving incident " + incidentId, e);
		}
	}

	public Collection getIncidents() throws HelpdeskException
	{
		try
		{
			HelpdeskDao dao = (HelpdeskDao) getDao();
			return dao.selectIncidents();
		}
		catch (Exception e)
		{
			throw new HelpdeskException("Error while retrieving incidents", e);
		}
	}

	public Collection getIncidents(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws HelpdeskException
	{
		try
		{
            // check for product sort
            if ("productName".equals(sort))
            {
                sort = "productId";
            }
			HelpdeskDao dao = (HelpdeskDao) getDao();
			return dao.selectIncidents(query, start, maxResults, sort, descending);
		}
		catch (Exception e)
		{
			throw new HelpdeskException("Error while retrieving incidents", e);
		}
	}

	public int getIncidentsCount(DaoQuery query) throws HelpdeskException
	{
		try
		{
			HelpdeskDao dao = (HelpdeskDao) getDao();
			return dao.selectIncidentsCount(query);
		}
		catch (Exception e)
		{
			throw new HelpdeskException("Error while retrieving incidents count", e);
		}
	}

	public Product getProduct(String productId) throws HelpdeskException
	{
		try
		{
			HelpdeskDao dao = (HelpdeskDao) getDao();
			return dao.selectProduct(productId);
		}
		catch (Exception e)
		{
			throw new HelpdeskException("Error while retrieving product " + productId, e);
		}
	}

	public Collection getProducts() throws HelpdeskException
	{
		try
		{
			HelpdeskDao dao = (HelpdeskDao) getDao();
			return dao.selectProducts();
		}
		catch (Exception e)
		{
			throw new HelpdeskException("Error while retrieving products", e);
		}
	}

	public Collection getProducts(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws HelpdeskException
	{
		try
		{
			HelpdeskDao dao = (HelpdeskDao) getDao();
			return dao.selectProducts(query, start, maxResults, sort, descending);
		}
		catch (Exception e)
		{
			throw new HelpdeskException("Error while retrieving products", e);
		}
	}

	public int getProductsCount(DaoQuery query) throws HelpdeskException
	{
		try
		{
			HelpdeskDao dao = (HelpdeskDao) getDao();
			return dao.selectProductsCount(query);
		}
		catch (Exception e)
		{
			throw new HelpdeskException("Error while retrieving products count", e);
		}
	}

    public Map getSeverityOptions() throws HelpdeskException
    {
        // read options from settings
        Map optionMap = new SequencedHashMap();
        Map defaultMap = getIncidentSettingsOptionMap("severity");
        optionMap.putAll(defaultMap);
        return optionMap;
    }

    public Map getResolutionStateOptions() throws HelpdeskException
    {
        // read options from settings
        Map optionMap = new SequencedHashMap();
        Map defaultMap = getIncidentSettingsOptionMap("resolutionState");
        optionMap.putAll(defaultMap);
        return optionMap;
    }

    public Map getContactedByOptions() throws HelpdeskException
    {
        // read options from settings
        Map optionMap = new SequencedHashMap();
        Map defaultMap = getDistinctProperties("contactedBy");
        optionMap.putAll(defaultMap);
        return optionMap;
    }

    public Map getIncidentTypeOptions() throws HelpdeskException
    {
        // read options from settings
        Map optionMap = new SequencedHashMap();
        Map defaultMap = getDistinctProperties("incidentType");
        optionMap.putAll(defaultMap);
        return optionMap;
    }

    public String getPropertyLabel(IncidentSettings settings, String property)
    {
        String label = null;
        String propertyName = property + "Label";
        try {
            label = (String)PropertyUtils.getProperty(settings, propertyName);
        }
        catch (Exception e) {
            Log.getLog(getClass()).debug("Error retrieving default label for property " + property, e);
        }
        return label;
    }

	public Map getDistinctProperties(String property) throws HelpdeskException
	{
		try
		{
            Map optionMap = new SequencedHashMap();
            Application app = Application.getInstance();
            optionMap.put("-1", app.getMessage("helpdesk.label.pleaseSelect"));

            // read default options from settings
            Map defaultMap = getIncidentSettingsOptionMap(property);
            optionMap.putAll(defaultMap);
            int defaultMapSize = defaultMap.size();

            // get distinct properties
			HelpdeskDao dao = (HelpdeskDao) getDao();
			Map distinctMap = dao.selectDistinct(property);
            boolean isFirst = true;
            for(Iterator i=distinctMap.keySet().iterator(); i.hasNext();)
            {
                String key = (String)i.next();
                if (key.trim().length() > 0 && !optionMap.containsKey(key))
                {
                    if (isFirst && defaultMapSize > 0)
                    {
                        optionMap.put(" ", "---");
                        isFirst = false;
                    }
                    String value = (String)distinctMap.get(key);
                    optionMap.put(key, value);
                }
            }

            return optionMap;
		}
		catch (Exception e)
		{
			throw new HelpdeskException("Error while retrieving distinct property " + property, e);
		}
	}

    public Map getIncidentSettingsOptionMap(String property)
    {
        Map optionMap = new SequencedHashMap();
        String propertyName = property + "Options";
        try {
            IncidentSettings settings = getIncidentSettings();
            String options = (String)PropertyUtils.getProperty(settings, propertyName);
            if (options != null)
            {
                StringTokenizer tokenizer = new StringTokenizer(options, "\n");
                while(tokenizer.hasMoreTokens())
                {
                    String token = tokenizer.nextToken().trim();
                    if (token.length() > 0)
                    {
                        optionMap.put(token, token);
                    }
                }
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).debug("Error retrieving default options for property " + property, e);
        }
        return optionMap;
    }

    public IncidentSettings getIncidentSettings() throws HelpdeskException
    {
        try
        {
            HelpdeskDao dao = (HelpdeskDao) getDao();
            return dao.selectSettings();
        }
        catch (Exception e)
        {
            throw new HelpdeskException("Error while retrieving incident settings", e);
        }
    }

    public void updateIncidentSettings(IncidentSettings settings) throws HelpdeskException {
        try
        {
            HelpdeskDao dao = (HelpdeskDao) getDao();
            dao.updateSettings(settings);
        }
        catch (Exception e)
        {
            throw new HelpdeskException("Error updating incident settings", e);
        }
    }

	/* Sending messages */
	public void sendIncidentNotification(Incident incident, IncidentLog log, String sender) throws HelpdeskException
	{
		HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
        String to = "";
        String productId = incident.getProductId();
        if (productId != null && productId.trim().length() > 0)
        {
            Product product = handler.getProduct(productId);
            if (product != null)
            {
                Collection owners = product.getOwners();
                if (owners != null)
                {
                    for (Iterator i = product.getOwners().iterator(); i.hasNext();)
                    {
                        User user = (User) i.next();
                        String address = user.getUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
                        to = ("".equals(to)) ? address : to + "," + address;
                    }
                }
                /* Formulating Message */
                try
                {
                    Application app = Application.getInstance();
                    DecimalFormat df = new DecimalFormat("000");
                    DateFormat dtf = DateFormat.getDateTimeInstance();
                    String incidentNo = "#" + df.format(incident.getIncidentCode());
                    Message message = new Message();
                    message.setMessageId(UuidGenerator.getInstance().getUuid());
                    message.setMessageFormat(Message.MESSAGE_FORMAT_TEXT);
                    message.setDate(new Date());
                    message.setToIntranetList(Util.convertStringToIntranetRecipientsList(to));
                    message.setFrom(sender + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN);
                    message.setSubject(app.getMessage("helpdesk.message.escalateSubject", new Object[] { incidentNo, incident.getSubject() }));
                    message.setBody(app.getMessage("helpdesk.message.escalateBody", new Object[] { incidentNo, dtf.format(log.getIncidentDate()), incident.getSeverity(), incident.getSubject(), incident.getDescription() }));
                    MessagingModule module = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
                    SmtpAccount account = new SmtpAccount();
                    account.setServerName("");
                    account.setServerPort(0);
                    module.sendMessage(account, message);
                }
                catch (Exception e)
                {
                    Log.getLog(getClass()).error("Error while sending incident notification message", e);
                }
            }
        }
	}

	public void addIncidentCounter(Notification notify) throws HelpdeskException
	{
		try
		{
			HelpdeskDao dao = (HelpdeskDao) getDao();
			dao.insertIncidentCounter(notify);
		}
		catch (Exception e)
		{
			throw new HelpdeskException("Error while adding incident log " + notify.getIncidentId(), e);
		}
	}
}
