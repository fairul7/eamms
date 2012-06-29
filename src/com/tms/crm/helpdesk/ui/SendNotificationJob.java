package com.tms.crm.helpdesk.ui;

import com.tms.collab.messaging.model.*;
import com.tms.crm.helpdesk.HelpdeskException;
import com.tms.crm.helpdesk.Notification;
import com.tms.crm.helpdesk.NotificationModule;
import com.tms.crm.sales.misc.DateUtil;
import com.tms.ekms.setup.model.SetupException;
import com.tms.ekms.setup.model.SetupModule;
import com.tms.util.MailUtil;
import kacang.Application;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.util.*;

public class SendNotificationJob extends BaseJob {
	public static final String SITE_SMPT = "siteSmtpServer";
	public static final String SITE_ADMIN_EMAIL = "siteAdminEmail" ;

	public SendNotificationJob() {
	}

	public void execute(JobTaskExecutionContext jobTaskExecutionContext) throws SchedulingException {
		Application application = Application.getInstance();
		NotificationModule notification = (NotificationModule) application.getModule(NotificationModule.class);
		Date nowDate = new Date();

		// Getting unresolved incident : returns Collection
		Collection incidents = new ArrayList();
		try {
			incidents = notification.getIncidents();
		} catch (HelpdeskException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		// Getting default alert setting : returns DataObject
		Notification alert = notification.getAlertSettings();
		int firstAlert = Integer.parseInt(alert.getFirstAlert());
		int subsequentAlert = Integer.parseInt(alert.getSubsequentAlert());

		// process unresolved bugs
		for (Iterator i = incidents.iterator(); i.hasNext();) {
			Notification unresolved = (Notification) i.next();
			int alertCounter = Integer.parseInt(unresolved.getCounter());

			Date alertDate = null;
			if (alertCounter == -1) {
				//alertDate = DateUtil.dateAdd(unresolved.getAlertTime(), Calendar.HOUR, firstAlert);
				// check for first alert
				alertDate = unresolved.getAlertTime();
			} else if (alertCounter > 0) {
				// check for subsequent alert
				alertDate = DateUtil.dateAdd(unresolved.getAlertTime(), Calendar.HOUR, subsequentAlert);
			}

			if (nowDate.compareTo(alertDate) >= 0) {
				// get subject
				String subject = "";
				if (alertCounter == -1) {
					if (Integer.parseInt(alert.getAlertOccurance()) == 1 )	{
						subject = "First & Last Alert Notication: " + unresolved.getSubject();
					} else	{
						subject = "First Alert Notication: " + unresolved.getSubject();
					}
				} else if (alertCounter == 1) {
					subject = "Last Alert Notication: " + unresolved.getSubject();
				} else if (alertCounter > 1) {
					if (Integer.parseInt(alert.getAlertOccurance()) == 2 )	{
						subject = "Last Alert Notication: " + unresolved.getSubject();
					} else {
						subject = "Subsequent Alert Notication: " + unresolved.getSubject();
					}
				}

				// send memo and/or email
				String body = buildMessage(unresolved);
				if ("memo".equals(alert.getMethod1())) {
					sendNotification("admin", unresolved.getUsername(), subject, body);
				} if ("email".equals(alert.getMethod2())) {
					sendEmail(subject, unresolved.getEmail(), body);
				}
				updateIncidentCounter(String.valueOf(alertCounter), unresolved.getIncidentId());
			}
		}
	}

	public void sendEmail(String subject, String receiver, String message) {
		SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
		try {
			String smtp = setup.get(SITE_SMPT);
			String sender = setup.get(SITE_ADMIN_EMAIL);
			MailUtil.sendEmail(smtp, true, sender, receiver, subject, message);
		} catch (SetupException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}

	public void sendNotification(String fromUsername,String to, String subject,String body){
        MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        try {
			// get userId
			String toAddress = to+"@"+ MessagingModule.INTRANET_EMAIL_DOMAIN;
			String senderId = null;
			Collection col = service.getUsersByUsername(fromUsername);
			if (col.size() == 1) {
				senderId = ((User) col.iterator().next()).getId();
			}

			// get Intranet Account
			IntranetAccount intranetAccount = mm.getIntranetAccountByUserId(senderId);
			String fromAddress = intranetAccount.getIntranetUsername()+"@"+ MessagingModule.INTRANET_EMAIL_DOMAIN;
			Collection toList = new HashSet(1);
			toList.add(toAddress);
		
            if (intranetAccount!=null){
				SmtpAccount smtpAccount = mm.getSmtpAccountByUserId(senderId);
                Message message = new Message();
                message.setMessageId(UuidGenerator.getInstance().getUuid());
                message.setSubject(subject);
                message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
                message.setDate(new Date());
                message.setBody(body);
                message.setToIntranetList(new ArrayList(toList));
                message.setFrom(fromAddress);
                mm.sendMessage(smtpAccount,message,senderId,false);
            }
        } catch (MessagingException e) {
            Log.getLog(getClass()).error(e.toString(), e);
        } catch (kacang.services.security.SecurityException e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
    }

	public String getIncidentAlert(String incidentId, Notification incidents)	{
		Application application = Application.getInstance();
		NotificationModule notification = (NotificationModule) application.getModule(NotificationModule.class);
		Notification IncidentAlert = new Notification();
		IncidentAlert = notification.getIncidentAlert(incidents.getIncidentId());	// Getting alert counter "-1" : returns DataObject
		return IncidentAlert.getCounter();
	}

	public void updateIncidentCounter(String incidentCounter, String incidentId)	{
		Application application = Application.getInstance();
		NotificationModule notification = (NotificationModule) application.getModule(NotificationModule.class);

		if ("-1".equals(incidentCounter))	{
			Notification alert = new Notification();
			alert = notification.getAlertSettings();
			Date currentDate = new Date();
			try {
				int temp = Integer.parseInt(alert.getAlertOccurance());
				int diff = temp -1 ;
				notification.updateIndcidentCounter(String.valueOf(diff),currentDate, incidentId);
			} catch (Exception e) {
			Log.getLog(getClass()).error(e.toString(), e);
			}
		} else if (Integer.parseInt(incidentCounter) > 0 ) {
			Notification alert = new Notification();
			alert = notification.getIncidentAlert(incidentId);
			Date currentDate = new Date();
			int currentCounter = Integer.parseInt(alert.getCounter());
			try {
				notification.updateIndcidentCounter(String.valueOf(currentCounter-1), currentDate, incidentId);
				} catch (Exception e) {
				Log.getLog(getClass()).error(e.toString(), e);
			}
		}
	}

	public Date subsequentAlert(Date lastAlertTime, int subsequentAlertValue) {	//last alert time, subsequent alert value
		Date alertDate = DateUtil.dateAdd(lastAlertTime , Calendar.HOUR, subsequentAlertValue);
		return alertDate;
	}

	public String buildMessage(Notification notify)	{
		String incidentCode = notify.getIncidentCode();
		String companyName= notify.getCompanyName();
		String contact = notify.getContactFirstName() + " " + notify.getContactLastName();
		String subject = notify.getSubject();
		String desc = notify.getDescription();
		String id = notify.getIncidentId();
		String severity = notify.getSeverity();
		String contactedBy = notify.getContactedBy();
		String incidentType = notify.getIncidentType();
		String productName = notify.getProductName();
		String companyId = notify.getCompanyId();
		String contactId = notify.getContactId();
		if(desc==null||"".equals(desc))
			desc="-";
		if(productName==null||"".equals(productName))
			productName="-";
		SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
		String incidentUrl = "";
		String contactUrl = "";
		String companyUrl = "";
		try {
			incidentUrl = setup.get("siteUrl");
			contactUrl = setup.get("siteUrl");
			companyUrl = setup.get("siteUrl");

			incidentUrl = incidentUrl+"/ekms/helpdesk/incidentOpen.jsp?incidentId="+id;
			contactUrl = contactUrl+"/ekms/helpdesk/contactDetails.jsp?contactID="+contactId;
			companyUrl = companyUrl+"/ekms/helpdesk/companyView.jsp?cn=jsp_companyList_commain.table1&et=sel&companyID="+companyId;

		} catch (SetupException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		Application app = Application.getInstance();
		String body = app.getMessage("helpdesk.escalate.alert.header")+"<br><br>" +
					  "<u>"+app.getMessage("helpdesk.escalate.label.incidentDetails")+"</u><br><br>" +
					  "<b>"+app.getMessage("helpdesk.escalate.label.incidentNo")+" : </b>" + "<a href=\""+incidentUrl+"\">" +incidentCode + "</a>" + "<br>" +
					  "<b>"+app.getMessage("helpdesk.escalate.label.company")+" : </b>" + "<a href=\""+companyUrl+"\">" + companyName + "</a>"+"<br>" +
					  "<b>"+app.getMessage("helpdesk.escalate.label.contact")+" : </b>" + "<a href=\""+contactUrl+"\">" + contact + "</a>"+"<br>" +
					  "<b>"+app.getMessage("helpdesk.escalate.label.subject")+" : </b>" + subject + "<br>" +
					  "<b>"+app.getMessage("helpdesk.escalate.label.description")+" : </b>" + desc + "<br>" +
					  "<b>"+app.getMessage("helpdesk.escalate.label.severity")+" : </b>" + severity + "<br>" +
					  "<b>"+app.getMessage("helpdesk.escalate.label.contactMethod")+" : </b>" + contactedBy + "<br>" +
					  "<b>"+app.getMessage("helpdesk.escalate.label.incidentType")+" : </b>" + incidentType + "<br>" +
					  "<b>"+app.getMessage("helpdesk.escalate.label.product")+" : </b> " + productName;
	return body;
	}
 }


