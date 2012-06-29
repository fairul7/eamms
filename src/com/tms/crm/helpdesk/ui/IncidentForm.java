package com.tms.crm.helpdesk.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.services.security.ui.UsersSelectBox;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import com.tms.crm.helpdesk.*;
import com.tms.crm.helpdesk.validator.ValidatorNotEquals;

import java.util.*;

import org.apache.commons.collections.SequencedHashMap;

public abstract class IncidentForm extends Form
{
	public static final String FORWARD_SUCCESS = "success";
	public static final String FORWARD_RESOLVED = "resolved";
	public static final String FORWARD_REOPEN = "reopen";
	public static final String FORWARD_ESCALATED = "escalated";
	public static final String FORWARD_CANCEL = "cancel";
	public static final String FORWARD_FAILED = "failed";

    protected String companyId;
    protected String contactId;
	protected SelectBox severity;
	protected SelectBoxTextCombo contactedBy;
	protected SelectBoxTextCombo incidentType;
	protected SelectBox product;
	protected ComboSelectBox features;
	protected TextField subject;
	protected TextBox description;
	protected SelectBoxTextCombo property1;
	protected SelectBoxTextCombo property2;
	protected SelectBoxTextCombo property3;
	protected SelectBoxTextCombo property4;
	protected SelectBoxTextCombo property5;
	protected SelectBoxTextCombo property6;
	protected SimpleFileUpload attachment;
	protected TextBox resolution;
	protected UsersSelectBox users;
	protected Label labelEscalation;
	protected CheckBox toOwner;
    protected SelectBox resolutionState;
    protected Label labelResolution;
    protected Label labelAudit;
    protected Label labelLogs;
    protected Label labelProperty1;
    protected Label labelProperty2;
    protected Label labelProperty3;
    protected Label labelProperty4;
    protected Label labelProperty5;
    protected Label labelProperty6;

	protected Button submit;
    protected Button escalate;
	protected Button cancel;
	protected Button resolve;
    protected Button reopen;

	protected String incidentId;
	protected Incident incident;

	protected ValidatorNotEquals validContactedBy;
	protected ValidatorNotEmpty validContactedByEmpty;
	protected ValidatorNotEquals validIncidentType;
	protected ValidatorNotEmpty validIncidentTypeEmpty;
	protected ValidatorNotEmpty validSubject;

	public IncidentForm()
	{
		super();
	}

	public IncidentForm(String s)
	{
		super(s);
	}

	public void init()
	{
		if(!isKeyEmpty())
		{
			super.init();
            setMethod("post");

            Application application = Application.getInstance();
            severity = new SelectBox("severity");
            HelpdeskHandler handler = (HelpdeskHandler)application.getModule(HelpdeskHandler.class);
            try
            {
                Map severityMap = handler.getSeverityOptions();
                severity.setOptionMap(severityMap);
                if (severityMap.size() > 0)
                {
                    severity.setSelectedOption((String)severityMap.keySet().iterator().next());
                }
            }
            catch (HelpdeskException e) {
                // ignore, module already logs message
            }
            contactedBy = new SelectBoxTextCombo("contactedBy");
			incidentType = new SelectBoxTextCombo("incidentType");
			product = new SelectBox("product");
			product.setOnChange("submit();");
			features = new ComboSelectBox("features");
			subject = new TextField("subject");
			subject.setSize("60");
			subject.setMaxlength("250");
			description = new TextBox("description");
			description.setRows("15");
			description.setCols("60");
			property1 = new SelectBoxTextCombo("property1");
			property2 = new SelectBoxTextCombo("property2");
			property3 = new SelectBoxTextCombo("property3");
			property4 = new SelectBoxTextCombo("database");
			property5 = new SelectBoxTextCombo("property5");
			property6 = new SelectBoxTextCombo("property6");
			attachment = new SimpleFileUpload("attachment");
			resolution = new TextBox("resolution");
            resolution.setRows("7");
			resolution.setCols("35");
			users = new UsersSelectBox("users");
			toOwner = new CheckBox("toOwner");
			toOwner.setText("Escalate to product owner");
			toOwner.setChecked(true);
            resolutionState = new SelectBox("resolutionState");
            try
            {
                Map resolutionMap = handler.getResolutionStateOptions();
                resolutionState.setOptionMap(resolutionMap);
                if (resolutionMap.size() > 0)
                {
                    resolutionState.setSelectedOption((String)resolutionMap.keySet().iterator().next());
                }
            }
            catch (HelpdeskException e) {
                // ignore, module already logs message
            }
            labelLogs = new Label("labelLogs");
			labelLogs.setText("-NA-");
            labelResolution = new Label("labelResolution", application.getMessage("helpdesk.label.resolution"));
            labelAudit = new Label("labelLogging", application.getMessage("helpdesk.label.auditTrail"));
			labelEscalation = new Label("labelEscalation",application.getMessage("helpdesk.label.escalation"));

            submit = new Button("submit", application.getMessage("helpdesk.label.save"));
            escalate = new Button("escalate", application.getMessage("helpdesk.label.escalate"));
            //escalate.setOnClick("return confirm(\"" + application.getMessage("helpdesk.message.confirmEscalation") + "\")");
			cancel = new Button("cancel", application.getMessage("helpdesk.label.cancel"));
			resolve = new Button("resolve", application.getMessage("helpdesk.label.resolve"));
			reopen = new Button("reopen", application.getMessage("helpdesk.label.reopen"));

			validContactedBy = new ValidatorNotEquals("validContactBy");
			validContactedBy.setCheck("-1");
			validContactedBy.setText(application.getMessage("helpdesk.message.contactMethodRequired"));
			validIncidentType = new ValidatorNotEquals("validIncidentType");
			validIncidentType.setCheck("-1");
			validIncidentType.setText(application.getMessage("helpdesk.message.incidentTypeRequired"));
			validSubject = new ValidatorNotEmpty("validSubject");
			validSubject.setText(application.getMessage("helpdesk.message.subjectRequired"));
            validContactedByEmpty = new ValidatorNotEmpty("validContactByEmpty");
			validContactedByEmpty.setText(application.getMessage("helpdesk.message.contactMethodRequired"));
			validIncidentTypeEmpty = new ValidatorNotEmpty("validIncidentTypeEmpty");
			validIncidentTypeEmpty.setText(application.getMessage("helpdesk.message.incidentTypeRequired"));

			contactedBy.addChild(validContactedBy);
			contactedBy.addChild(validContactedByEmpty);
			incidentType.addChild(validIncidentType);
			incidentType.addChild(validIncidentTypeEmpty);
            subject.addChild(validSubject);

            
			Panel panel = new Panel("buttonPanel");
			panel.addChild(submit);
            //panel.addChild(escalate);
			panel.addChild(cancel);

            Panel panel2 = new Panel("resolvePanel");
            panel2.addChild(reopen);
            panel2.addChild(resolutionState);
            panel2.addChild(resolve);

			Panel panel3 = new Panel("usersPanel");
			panel3.setColumns(1);
			panel3.addChild(users);
			panel3.addChild(escalate);
			panel3.addChild(toOwner);

			setColumns(2);
            addChild(new Label("labelSubject", application.getMessage("helpdesk.label.subject")+" *"));
            addChild(subject);
            addChild(new Label("labelDescription", application.getMessage("helpdesk.label.description")));
            addChild(description);
			addChild(new Label("labelSeverity", application.getMessage("helpdesk.label.severity")+" *"));
			addChild(severity);
			addChild(new Label("labelContactedBy", application.getMessage("helpdesk.label.contactedBy")+" *"));
			addChild(contactedBy);
			addChild(new Label("labelIncidentType", application.getMessage("helpdesk.label.incidentType")+" *"));
			addChild(incidentType);
			addChild(new Label("labelProduct", application.getMessage("helpdesk.label.product")));
			addChild(product);
			addChild(new Label("labelFeature", application.getMessage("helpdesk.label.feature")));
			addChild(features);

            // property labels
           try
            {
                IncidentSettings settings = handler.getIncidentSettings();
                labelProperty1 = new Label("labelproperty1", handler.getPropertyLabel(settings, "property1"));
                addChild(labelProperty1);
                addChild(property1);
                labelProperty2 = new Label("labelproperty2", handler.getPropertyLabel(settings, "property2"));
                addChild(labelProperty2);
                addChild(property2);
                labelProperty3 = new Label("labelproperty3", handler.getPropertyLabel(settings, "property3"));
                addChild(labelProperty3);
                addChild(property3);
                labelProperty4 = new Label("labelproperty4", handler.getPropertyLabel(settings, "property4"));
                addChild(labelProperty4);
                addChild(property4);
                labelProperty5 = new Label("labelproperty5", handler.getPropertyLabel(settings, "property5"));
                addChild(labelProperty5);
                addChild(property5);
                labelProperty6 = new Label("labelproperty6", handler.getPropertyLabel(settings, "property6"));
                addChild(labelProperty6);
                addChild(property6);
            }
            catch (HelpdeskException e) {
                // ignore, module logs message already
            }
            addChild(new Label("labelAttachment", application.getMessage("helpdesk.label.attachment")));
			addChild(attachment);
			addChild(labelResolution);
			addChild(resolution);
			addChild(new Label("labelButton", ""));
			addChild(panel);
            //addChild(new Label("labelButton3", ""));
			addChild(labelEscalation);
			addChild(panel3);
			addChild(new Label("labelButton2", ""));
			addChild(panel2);
			addChild(labelAudit);
            addChild(labelLogs);
			features.init();
			users.init();
		}
	}

	public void onRequest(Event event)
    {
		removeChildren();	
		init();
        super.onRequest(event);
        if(!isKeyEmpty())
            refresh();
    }

    public Forward actionPerformed(Event event)
    {
        Forward forward = null;
        if(!isKeyEmpty())
        {
            String buttonClicked = findButtonClicked(event);
            if(cancel.getAbsoluteName().equals(buttonClicked))
                forward = new Forward(FORWARD_CANCEL);
            else
                forward = super.actionPerformed(event);
        }
        return forward;
    }

	protected void refresh()
	{
		//TODO: Utilize some sort of caching here. Entirely too many database accesses
        if(!isKeyEmpty())
		{
            Application application = Application.getInstance();
			HelpdeskHandler handler = (HelpdeskHandler) application.getModule(HelpdeskHandler.class);
			try
			{
				/* Distinct Elements */
                severity.setOptionMap(handler.getSeverityOptions());
				contactedBy.setOptions(handler.getContactedByOptions());
				incidentType.setOptions(handler.getIncidentTypeOptions());
                try
                {
                    IncidentSettings settings = handler.getIncidentSettings();
                    refreshProperty(handler, settings, "property1", property1, labelProperty1);
                    refreshProperty(handler, settings, "property2", property2, labelProperty2);
                    refreshProperty(handler, settings, "property3", property3, labelProperty3);
                    refreshProperty(handler, settings, "property4", property4, labelProperty4);
                    refreshProperty(handler, settings, "property5", property5, labelProperty5);
                    refreshProperty(handler, settings, "property6", property6, labelProperty6);
                }
                catch (Exception e) {
                    // ignore, module logs message already
                }

                /* Product Initialization */
				Map productMap = new SequencedHashMap();
				productMap.put("-1", application.getMessage("helpdesk.label.pleaseSelect"));
				Collection products = handler.getProducts();
				for (Iterator i = products.iterator(); i.hasNext();)
				{
					Product product = (Product) i.next();
					productMap.put(product.getProductId(), product.getProductName());
				}
				product.setOptionMap(productMap);
			}
			catch (HelpdeskException e)
			{
				Log.getLog(getClass()).error("Error occured while retrieving distinct incident elements", e);
			}
			validSubject.setText(application.getMessage("helpdesk.message.subjectRequired"));
		}
	}

    protected void refreshProperty(HelpdeskHandler handler, IncidentSettings settings, String property, SelectBoxTextCombo propertyWidget, Label labelWidget) throws HelpdeskException {
        propertyWidget.setOptions(handler.getDistinctProperties(property));
        String label = handler.getPropertyLabel(settings, property);
        if (label != null && label.trim().length() > 0)
        {
            propertyWidget.setHidden(false);
            labelWidget.setHidden(false);
            labelWidget.setText(label);
        }
        else
        {
            propertyWidget.setHidden(true);
            labelWidget.setHidden(true);
        }
    }

    public Forward onSubmit(Event event)
	{
		Forward forward = super.onSubmit(event);
		/* Refreshing lists if no button clicked */
		if(findButtonClicked(event) == null)
		{
			String selectedProduct = "";
			if(product.getSelectedOptions().keySet().size() > 0)
				selectedProduct = (String) product.getSelectedOptions().keySet().iterator().next();
			
			if(!("".equals(selectedProduct) || "-1".equals(selectedProduct)))
			{
				try
				{
					HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
					Product selected = handler.getProduct(selectedProduct);
					StringTokenizer tokenizer = new StringTokenizer(selected.getProductFeatures(), "\n");
					Map featureMap = new SequencedHashMap();
                    Map leftMap = new SequencedHashMap();
					while(tokenizer.hasMoreTokens())
					{
						String token = tokenizer.nextToken();
                        if (token.trim().length() > 0)
                        {
                            featureMap.put(token, token);
                            if(!(features.getRightValues().containsKey(token)))
                            {
                                leftMap.put(token, token);
                            }
                        }
					}
                    Map rightMap = features.getRightValues();
                    for(Iterator i=rightMap.keySet().iterator(); i.hasNext();)
                    {
                        String key = (String)i.next();
                        if (!featureMap.containsKey(key))
                        {
                            i.remove();
                        }
                    }
					features.setLeftValues(leftMap);
				}
				catch (HelpdeskException e)
				{
					Log.getLog(getClass()).error("Error while retrieving product", e);
				}
			}
            setInvalid(true);
			setAllValid();
		}
		return forward;
	}

	protected boolean isKeyEmpty()
	{
		return false;
	}

	/**
	 * Private method called to set all widgets to valid. Called only during filtering selection when
	 * validation needs to be overridden
	 */
	protected void setAllValid()
	{
		contactedBy.setInvalid(false);
		incidentType.setInvalid(false);
		subject.setInvalid(false);
		//Resetting validators
		validContactedBy.setInvalid(false);
		validContactedByEmpty.setInvalid(false);
		validIncidentType.setInvalid(false);
		validIncidentTypeEmpty.setInvalid(false);
		validSubject.setInvalid(false);
	}

	protected Incident generateIncident()
	{
		Incident incident = new Incident();
        Calendar calendar = Calendar.getInstance();
		if(this.incident == null)
		{
			incident.setIncidentId(UuidGenerator.getInstance().getUuid());
			incident.setIncidentCode(0);
			incident.setCreated(calendar.getTime());
			incident.setLastModified(null);
			incident.setDateResolved(null);
			incident.setResolved(false);
			incident.setResolutionState("1");
			incident.setLogs(new ArrayList());
            incident.populateCompanyId(getCompanyId());
            incident.populateContactId(getContactId());

		}
		else
		{
			incident.setIncidentId(this.incident.getIncidentId());
			incident.setIncidentCode(this.incident.getIncidentCode());
			incident.setCreated(this.incident.getCreated());
            incident.setCreatedBy(this.incident.getCreatedBy());
			incident.setLastModified(calendar.getTime());
            incident.setLastModifiedBy(this.incident.getLastModifiedBy());
			incident.setDateResolved(this.incident.getDateResolved());
			incident.setResolved(this.incident.isResolved());
            incident.setResolvedBy(this.incident.getResolvedBy());
            incident.setResolution(this.incident.getResolution());
			incident.setResolutionState(this.incident.getResolutionState());
			incident.setLogs(this.incident.getLogs());
            incident.populateCompanyId(this.incident.getCompanyId());
            incident.populateContactId(this.incident.getContactId());
		}
		incident.setSeverity((String) severity.getSelectedOptions().keySet().iterator().next());
		incident.setContactedBy((String) contactedBy.getValue());
		incident.setIncidentType((String) incidentType.getValue());
		incident.setProductId((String) product.getSelectedOptions().keySet().iterator().next());
		Map map = features.getRightValues();
		Collection list = new ArrayList();
		for (Iterator i = map.keySet().iterator(); i.hasNext();)
		{
			String feature = (String) i.next();
			list.add(feature);
		}
		incident.setFeatures(list);
		incident.setSubject((String) subject.getValue());
		incident.setDescription((String) description.getValue());
		incident.setProperty1((String) property1.getValue());
		incident.setProperty2((String) property2.getValue());
		incident.setProperty3((String) property3.getValue());
		incident.setProperty4((String) property4.getValue());
		incident.setProperty5((String) property5.getValue());
		incident.setProperty6((String) property6.getValue());
		incident.setResolution((String) resolution.getValue());

		return incident;
	}

    protected void handleFileUploads(Incident incident, Event evt)
    {
        if (incident != null)
        {
            try
            {
                StorageService ss = (StorageService)Application.getInstance().getService(StorageService.class);
                StorageFile file = attachment.getStorageFile(evt.getRequest());
                StorageFile path = new StorageFile(HelpdeskHandler.ATTACHMENT_STORAGE_PATH + incident.getIncidentId());
                if (attachment.isDelete())
                {
                    ss.delete(path);
                }
                else if (file != null)
                {
                    file.setParentDirectoryPath(path.getAbsolutePath());
                    ss.delete(path);
                    ss.store(file);
                }
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error uploading attachment", e);
            }
        }
    }

	/* Getters and Setters */
    public String getCompanyId()
    {
        return companyId;
    }

    public void setCompanyId(String companyId)
    {
        this.companyId = companyId;
    }

    public String getContactId()
    {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

	public Incident getIncident()
	{
		return incident;
	}

	public void setIncident(Incident incident)
	{
		this.incident = incident;
	}

	public String getIncidentId()
	{
		return incidentId;
	}

	public void setIncidentId(String incidentId)
	{
		this.incidentId = incidentId;
		try
		{
			HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
			incident = handler.getIncident(incidentId);
            incident.populateCompanyId(incident.getCompanyId());
            incident.populateContactId(incident.getContactId());
		}
		catch (HelpdeskException e)
		{
			Log.getLog(getClass()).error("Error while retrieving incident " + incidentId, e);
		}
	}

}
