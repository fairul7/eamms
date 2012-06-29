package com.tms.crm.helpdesk.ui;

import java.util.Date;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import com.tms.crm.helpdesk.*;

public class IncidentAdd extends IncidentForm
{
	public IncidentAdd()
	{
		super();
	}

	public IncidentAdd(String s)
	{
		super(s);
	}

	public String getDefaultTemplate() {
		return "helpdesk/incidentAdd";
	}
	
	public void init()
	{
		super.init();
        escalate.setHidden(true);
		resolve.setHidden(true);
		reopen.setHidden(true);
		resolutionState.setHidden(true);
        resolution.setHidden(true);
        labelLogs.setHidden(true);
        labelResolution.setHidden(true);
        labelAudit.setHidden(true);
		users.setHidden(true);
		toOwner.setHidden(true);
		labelEscalation.setHidden(true);
	}

	public Forward onValidate(Event event)
	{
		HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
		NotificationModule notification = (NotificationModule) Application.getInstance().getModule(NotificationModule.class);
		Forward forward = super.onValidate(event);
		try
		{
			Incident incident = generateIncident();
			Notification alertNotification = notification.getAlertSettings();
    		int firstAlert = Integer.parseInt(alertNotification.getFirstAlert());

            User user = getWidgetManager().getUser();
			handler.addIncident(incident, user);

			Notification alert = new Notification();
			alert.setId(UuidGenerator.getInstance().getUuid());
			alert.setIncidentId(incident.getIncidentId());
			alert.setCounter("-1");
			Date alertTime=incident.getCreated();
			alertTime.setHours(alertTime.getHours()+firstAlert);
			alert.setAlertTime(alertTime);
			handler.addIncidentCounter(alert);

            handleFileUploads(incident, event);
			forward = new Forward(FORWARD_SUCCESS);
			init();
		}
		catch (HelpdeskException e)
		{
			Log.getLog(getClass()).error("Error while adding incident " + incident.getSubject(), e);
			forward = new Forward(FORWARD_FAILED);
		}
		return forward;
	}
}
