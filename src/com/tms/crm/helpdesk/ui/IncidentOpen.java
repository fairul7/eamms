package com.tms.crm.helpdesk.ui;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;
import com.tms.crm.helpdesk.*;

import java.util.*;
import java.text.DateFormat;

public class IncidentOpen extends IncidentForm
{
	public IncidentOpen()
	{
		super();
	}
	
	public String getDefaultTemplate() {
		return "helpdesk/incidentAdd";
	}

	public IncidentOpen(String s)
	{
		super(s);
	}

	protected void refresh()
	{
		super.refresh();
        if(!isKeyEmpty())
		{

			HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
			/* Populating Form */
			try
			{
				incident = handler.getIncident(incidentId);
                incident.populateCompanyId(incident.getCompanyId());
                incident.populateContactId(incident.getContactId());
				Product incidentProduct = handler.getProduct(incident.getProductId());

				severity.setSelectedOption(incident.getSeverity());
				contactedBy.setSelectedOption(incident.getContactedBy());
				incidentType.setSelectedOption(incident.getIncidentType());
				product.setSelectedOption(incident.getProductId());

                try
                {
                    Map featuresLeft = new HashMap();
                    Map featuresRight = new HashMap();
                    Collection productFeatures = (incidentProduct != null) ? incidentProduct.getFeatures() : new ArrayList();
                    Collection incidentFeatures = incident.getFeatures();
                    if (incidentFeatures != null)
                    {
                        for (Iterator i = incidentFeatures.iterator(); i.hasNext();)
                        {
                            String feature = (String) i.next();
                            if (productFeatures.contains(feature))
                            {
                                featuresRight.put(feature,  feature);
                            }
                        }
                    }
                    if (productFeatures != null)
                    {
                        for (Iterator i = productFeatures.iterator(); i.hasNext();)
                        {
                            String feature = (String) i.next();
                            if(!(featuresRight.containsKey(feature)))
                                featuresLeft.put(feature,  feature);
                        }
                    }
                    features.setLeftValues(featuresLeft);
                    features.setRightValues(featuresRight);
                }
                catch (Exception e) {
                    Log.getLog(getClass()).error("Error retrieving product features", e);
                }

                subject.setValue(incident.getSubject());
				description.setValue(incident.getDescription());
				property1.setSelectedOption(incident.getProperty1());
				property2.setSelectedOption(incident.getProperty2());
				property3.setSelectedOption(incident.getProperty3());
				property4.setSelectedOption(incident.getProperty4());
				property5.setSelectedOption(incident.getProperty5());
				property6.setSelectedOption(incident.getProperty6());
				resolution.setValue(incident.getResolution());
				resolutionState.setSelectedOption(incident.getResolutionState());
                attachment.setPath(HelpdeskHandler.ATTACHMENT_STORAGE_PATH + incident.getIncidentId());
				/* Populating Audit Trails */
				String logs = "";
				DateFormat formatDate = DateFormat.getDateTimeInstance();
                Application app = Application.getInstance();
				for (Iterator i = incident.getLogs().iterator(); i.hasNext();)
				{
					IncidentLog log = (IncidentLog) i.next();
					logs += "[" + formatDate.format(log.getIncidentDate()) + "] "
                            + app.getMessage("helpdesk.label.action." + log.getAction(), log.getAction());
                    if (log.getResolutionState() != null && log.getResolutionState().trim().length() > 0)
                    {
                        logs += " (" + app.getMessage("helpdesk.label.resolution." + log.getResolutionState(), log.getResolutionState()) + ")";
                        
                    }
                    if("Escalated".equals(log.getAction())){
                    	logs += " to " + log.getReceipient() + " - "+log.getUser()+"<br>";
                    }
                    else{
                    logs += " - " + log.getUser() + "<br>";
                    }
				}
				labelLogs.setText(logs);
                /* Determine resolution status */
				if(incident.isResolved())
				{
                    escalate.setHidden(true);
					resolutionState.setHidden(true);
					resolve.setHidden(true);
					submit.setHidden(true);
					reopen.setHidden(false);
					users.setHidden(true);
					toOwner.setHidden(true);
					labelEscalation.setHidden(true);
				}
				else
				{
                    escalate.setHidden(false);
					resolutionState.setHidden(false);
					resolve.setHidden(false);
					submit.setHidden(false);
					reopen.setHidden(true);
					users.setHidden(false);
					toOwner.setHidden(false);
					labelEscalation.setHidden(false);
				}
			}
			catch (HelpdeskException e)
			{
				Log.getLog(getClass()).error("Error while retrieving incident", e);
			}
		}
	}

	/* Implementing Use Caseas */
	public Forward onValidate(Event evt)
	{
		Forward forward = super.onValidate(evt);
        String buttonClicked = findButtonClicked(evt);
        if(escalate.getAbsoluteName().equals(buttonClicked))
			forward = onEscalate(evt);
        else if(resolve.getAbsoluteName().equals(buttonClicked))
            forward = onResolve(evt);
        else if(reopen.getAbsoluteName().equals(buttonClicked))
            forward = onReopen(evt);
        else
            forward = onUpdate(evt);
        return forward;
	}

	/* Use case methods */

    public Forward onUpdate(Event evt)
    {
        HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
        NotificationModule notification = (NotificationModule) Application.getInstance().getModule(NotificationModule.class);
        Forward forward = null;
        try
        {        	
            Incident incident = generateIncident();
            User user = getWidgetManager().getUser();
            Notification alert = notification.getAlertSettings();
    		int firstAlert = Integer.parseInt(alert.getFirstAlert());
    		Date alertTime=new Date();
    		alertTime.setHours(alertTime.getHours()+firstAlert);
    		incident.setAlertTime(alertTime);
    		notification.getLastAlert(incident.getIncidentId());
            handler.updateIncident(incident, user);
			// Resetting the incident counter value once the incident is updated
			handler.resetIncidentAlert(incident);

            forward = new Forward(FORWARD_SUCCESS);
            handleFileUploads(incident, evt);
            init();
        }
        catch (HelpdeskException e)
        {
            Log.getLog(getClass()).error("Error while updating incident " + incident.getIncidentId(), e);
            forward = new Forward(FORWARD_FAILED);
        }
        return forward;
    }

    public Forward onEscalate(Event evt)
    {
        HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
        SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
        NotificationModule notificationModule = (NotificationModule) Application.getInstance().getModule(NotificationModule.class);
        Forward forward = null;
        try
        {
            Incident incident = generateIncident();
            User user = getWidgetManager().getUser();
            List userList= new ArrayList();
            Collection ownerCollection=notificationModule.getOwner(incident.getIncidentId());
            String receipients="";
            String []userid=users.getIds();
            if (userid != null && userid.length > 0)
            {
            	for (int i=0; i<userid.length; i++)
                {
                try {
                	userList.add(userid[i]);
					User receipientUser=ss.getUser(userid[i]);
					receipients+=receipientUser.getProperty("firstName")+" "+receipientUser.getProperty("lastName")+",";
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                }
            }
            if(toOwner.isChecked()){
            if (ownerCollection.size() > 0 ) {
				for (Iterator i = ownerCollection.iterator(); i.hasNext();) {
					Notification o =  (Notification) i.next();
					if(!userList.contains(o.getUserId())){
						receipients+=o.getFirstName()+" "+o.getLastName()+",";	
					}
					}
				}
			
            }
            if(receipients.length()>1){
            	receipients=receipients.substring(0,receipients.length()-1);
            }
            handler.escalateIncident(incident, user, receipients);
            
			// Sending notification
			OnEscalaeSelectUsers escalate = new OnEscalaeSelectUsers();
			escalate.notifyUser(incident,users.getIds(),user,toOwner.isChecked());
            forward = new Forward(FORWARD_ESCALATED);
            handleFileUploads(incident, evt);
            init();
        }
        catch (HelpdeskException e)
        {
            Log.getLog(getClass()).error("Error while escalating incident " + incident.getIncidentId(), e);
            forward = new Forward(FORWARD_FAILED);
        }
        return forward;
    }


	protected Forward onResolve(Event evt)
	{
		HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
		Forward forward = null;
		resolutionState.onSubmit(evt);
		try
		{
            User user = getWidgetManager().getUser();
			Incident incident = generateIncident();
			String value = (String) resolutionState.getSelectedOptions().keySet().iterator().next();
            incident.setResolutionState(value);
			handler.resolveIncident(incident, user);
			// closing the incident counter value once the incident is resolved
			handler.closeIncidentCounter(incident);
            handleFileUploads(incident, evt);
			forward = new Forward(FORWARD_RESOLVED);
			init();
		}
		catch (HelpdeskException e)
		{
			Log.getLog(getClass()).error("Error while resolving incident " + incident.getIncidentId(), e);
			forward = new Forward(FORWARD_FAILED);
		}
        return forward;
	}

	protected Forward onReopen(Event event)
	{
		HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
		NotificationModule notification = (NotificationModule) Application.getInstance().getModule(NotificationModule.class);
		Forward forward = null;
		try
		{
			Incident incident = generateIncident();
            User user = getWidgetManager().getUser();
			handler.reopenIncident(incident, user);
			// Re-opening the the incident counter value once the incident is re-opened
			Notification alert = notification.getAlertSettings();
    		int firstAlert = Integer.parseInt(alert.getFirstAlert());
    		Date alertTime=new Date();
    		alertTime.setHours(alertTime.getHours()+firstAlert);
    		incident.setAlertTime(alertTime);
			handler.resetIncidentAlert(incident);
			forward = new Forward(FORWARD_REOPEN);
			init();
		}
		catch (HelpdeskException e)
		{
			Log.getLog(getClass()).error("Error while reopening incident " + incident.getIncidentId(), e);
			forward = new Forward(FORWARD_FAILED);
		}
        return forward;
	}
}
