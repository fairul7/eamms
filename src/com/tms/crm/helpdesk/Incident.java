package com.tms.crm.helpdesk;

import com.tms.crm.sales.model.Company;
import com.tms.crm.sales.model.CompanyModule;
import com.tms.crm.sales.model.Contact;
import com.tms.crm.sales.model.ContactModule;
import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.util.Log;

import java.util.*;

public class Incident extends DefaultDataObject
{
	private String incidentId;
	private int incidentCode;
	private Date created;
    private String createdBy;
	private Date lastModified;
    private String lastModifiedBy;
	private Date dateResolved;
    private String resolvedBy;
	private String severity;
	private String companyId;
    private String companyName;
	private Company company;
	private String contactId;
    private String contactFirstName;
    private String contactLastName;
    private String contactEmail;
	private Contact contact;
	private String contactedBy;
	private String incidentType;
	private String productId;
	private Collection features;
	private String subject;
	private String description;
    private String property1;
	private String property2;
	private String property3;
	private String property4;
	private String property5;
	private String property6;
    private String attachmentPath;
    private String resolution;
	private boolean isResolved;
	private Collection logs;
    private String resolutionState;
    private Date alertTime;

	public Incident()
	{
		features = new ArrayList();
	}

	/* Getters and Setters */
	public String getIncidentId()
	{
		return incidentId;
	}

	public void setIncidentId(String incidentId)
	{
		this.incidentId = incidentId;
	}

	public Date getCreated()
	{
		return created;
	}

	public void setCreated(Date created)
	{
		this.created = created;
	}

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

	public Date getLastModified()
	{
		return lastModified;
	}

	public void setLastModified(Date lastModified)
	{
		this.lastModified = lastModified;
	}

    public String getLastModifiedBy()
    {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy)
    {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getDateResolved()
	{
		return dateResolved;
	}

	public void setDateResolved(Date dateResolved)
	{
		this.dateResolved = dateResolved;
	}

    public String getResolvedBy()
    {
        return resolvedBy;
    }

    public void setResolvedBy(String resolvedBy)
    {
        this.resolvedBy = resolvedBy;
    }

    public String getCompanyId()
	{
		return companyId;
	}

    public void setCompanyId(String companyId)
    {
        this.companyId = companyId;
    }

	public void populateCompanyId(String companyId)
	{
		this.companyId = companyId;
		if(!("-1".equals(companyId) || companyId == null))
		{
			try
			{
				CompanyModule module = (CompanyModule) Application.getInstance().getModule(CompanyModule.class);
                Company company = module.getCompany(companyId);
				setCompany(company);
                if (company != null)
                {
                    setCompanyName(company.getCompanyName());
                }
			}
            catch (Exception e)
            {
                Log.getLog(getClass()).error("Error while retrieving company " + companyId, e);
            }
		}
	}

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

	public Company getCompany()
	{
		return company;
	}

	public void setCompany(Company company)
	{
		this.company = company;
	}

	public String getContactId()
	{
		return contactId;
	}

    public void setContactId(String contactId)
    {
        this.contactId = contactId;
    }

	public void populateContactId(String contactId)
	{
		this.contactId = contactId;
		if(!("-1".equals(contactId) || contactId == null))
		{
			try
			{
				ContactModule module = (ContactModule) Application.getInstance().getModule(ContactModule.class);
                Contact contact = module.getContact(contactId);
				setContact(contact);
                if (contact != null)
                {
                    setContactFirstName(contact.getContactFirstName());
                    setContactLastName(contact.getContactLastName());
                    setContactEmail(contact.getContactEmail());
                }
			}
            catch (Exception e)
            {
                Log.getLog(getClass()).error("Error while retrieving contact " + contactId, e);
            }
		}
	}

    public String getContactFirstName()
    {
        return contactFirstName;
    }

    public void setContactFirstName(String contactFirstName)
    {
        this.contactFirstName = contactFirstName;
    }

    public String getContactLastName()
    {
        return contactLastName;
    }

    public void setContactLastName(String contactLastName)
    {
        this.contactLastName = contactLastName;
    }

    public String getContactEmail()
    {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail)
    {
        this.contactEmail = contactEmail;
    }

	public Contact getContact()
	{
		return contact;
	}

	public void setContact(Contact contact)
	{
		this.contact = contact;
	}

	public String getContactedBy()
	{
		return contactedBy;
	}

	public void setContactedBy(String contactedBy)
	{
		this.contactedBy = contactedBy;
	}

	public String getIncidentType()
	{
		return incidentType;
	}

	public void setIncidentType(String incidentType)
	{
		this.incidentType = incidentType;
	}

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	public Collection getFeatures()
	{
		return features;
	}

	public void setFeatures(Collection features)
	{
		this.features = features;
	}

	/**
	 * Facade method for HelpdeskDao
	 * @return  A \n delimited string of the selected features
	 */
	public String getProductFeatures()
	{
		String value = "";
		for (Iterator i = features.iterator(); i.hasNext();)
		{
			String feature = (String) i.next();
			if(!("".equals(value)))
				value += Product.DEFAULT_DELIMITER;
            value += feature;
		}
		return value;
	}

	/**
	 * Facade method for HelpdeskDao
	 * @param productFeatures
	 */
    public void setProductFeatures(String productFeatures)
	{
		StringTokenizer tokenizer = new StringTokenizer(productFeatures, Product.DEFAULT_DELIMITER);
		features = new ArrayList();
		while(tokenizer.hasMoreTokens())
			features.add(tokenizer.nextToken());
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getProperty1()
	{
		return property1;
	}

	public void setProperty1(String property1)
	{
		this.property1 = property1;
	}

	public String getProperty2()
	{
		return property2;
	}

	public void setProperty2(String property2)
	{
		this.property2 = property2;
	}

	public String getProperty3()
	{
		return property3;
	}

	public void setProperty3(String property3)
	{
		this.property3 = property3;
	}

	public String getProperty4()
	{
		return property4;
	}

	public void setProperty4(String property4)
	{
		this.property4 = property4;
	}

	public String getProperty5()
	{
		return property5;
	}

	public void setProperty5(String property5)
	{
		this.property5 = property5;
	}

	public String getProperty6()
	{
		return property6;
	}

	public void setProperty6(String property6)
	{
		this.property6 = property6;
	}

	public String getAttachmentPath()
	{
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath)
	{
		this.attachmentPath = attachmentPath;
	}

	public String getResolution()
	{
		return resolution;
	}

	public void setResolution(String resolution)
	{
		this.resolution = resolution;
	}

	public boolean isResolved()
	{
		return isResolved;
	}

	public void setResolved(boolean resolved)
	{
		isResolved = resolved;
	}

	public Collection getLogs()
	{
		return logs;
	}

	public void setLogs(Collection logs)
	{
		this.logs = logs;
	}

	public void addLog(IncidentLog log)
	{
		logs.add(log);
	}

	public String getProductName()
	{
		String name = "";
		if( productId != null)
        {
            HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
            try
            {
                Product product = handler.getProduct(productId);
                if (product != null)
                    name = product.getProductName();
            }
            catch (HelpdeskException e)
            {
                Log.getLog(getClass()).error("Error while retrieving product " + productId, e);
            }
        }
		return name;
	}

	public int getIncidentCode()
	{
		return incidentCode;
	}

	public void setIncidentCode(int incidentCode)
	{
		this.incidentCode = incidentCode;
	}

	/**
	 * 1. Enhancement
	 * 2. Trivial
	 * 3. Minor
	 * 4. Normal
	 * 5. Major
	 * 6. Critical
	 * 7. Blocker
	 * @return incident severity
	 */
	public String getSeverity()
	{
		return severity;
	}

	public void setSeverity(String severity)
	{
		this.severity = severity;
	}

	/**
	 * 1. Unable to resolve
	 * 2. Fixed
	 * 3. Invalid
	 * 4. Won't Fix
	 * 5. Works for me
	 * @return resolution state
	 */
	public String getResolutionState()
	{
		return resolutionState;
	}

	public void setResolutionState(String resolutionState)
	{
		this.resolutionState = resolutionState;
	}

	public Date getAlertTime() {
		return alertTime;
	}

	public void setAlertTime(Date alertTime) {
		this.alertTime = alertTime;
	}
	
	
}
