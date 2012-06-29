package com.tms.crm.helpdesk.ui;

import com.tms.collab.messaging.model.*;
import com.tms.crm.helpdesk.HelpdeskException;
import com.tms.crm.helpdesk.Incident;
import com.tms.crm.helpdesk.Notification;
import com.tms.crm.helpdesk.NotificationModule;
import com.tms.ekms.setup.model.SetupException;
import com.tms.ekms.setup.model.SetupModule;
import com.tms.util.MailUtil;
import kacang.Application;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.text.SimpleDateFormat;
import java.util.*;


public class OnEscalaeSelectUsers{

	public static final String SITE_SMPT = "siteSmtpServer";
	//private String incidentId;

	public OnEscalaeSelectUsers() {
	}

	public void notifyUser(Incident incident, String[] selectedUsers, User currentUser, boolean toOwner) {
		NotificationModule userListHandler = (NotificationModule) Application.getInstance().getModule(NotificationModule.class);
		Notification alert = userListHandler.getAlertSettings();
		String email = (String) currentUser.getProperty("email1");
		String id = currentUser.getId();
		Notification currentIncident = getIncident(incident.getIncidentId());	// Getting incident details

		for (int i=0 ; i < selectedUsers.length ; i++)	{
			Notification user = new Notification();				// Getting user details
			String value = currentIncident.getIncidentCode();
			String subject = "Helpdesk Incident " + value + ":  " + incident.getSubject() + " " + "(Escalated)";
			String body = buildMessage(currentIncident);
			user = userListHandler.getContactDetails(selectedUsers[i]);
			if (user.getEmail() != null && !user.getEmail().equals("")) {
				if ("email".equals(alert.getMethod2())) {
					sendEmail(subject, email, user.getEmail(), body);
				}
				if ("memo".equals(alert.getMethod1())) {
					sendNotification(id, user.getUsername(), subject, body);
				}
			}
		}
		if (toOwner) {
			String value = currentIncident.getIncidentCode();
			String subject = "Helpdesk Incident " + value + ":  " + incident.getSubject() + " " + "(Escalated)";
			String body = buildMessage(currentIncident);
			Collection owner = new ArrayList();
			owner = userListHandler.getOwner(incident.getIncidentId());
			if (owner.size() > 0 ) {
				for (Iterator i = owner.iterator(); i.hasNext();) {
					Notification o =  (Notification) i.next();
					if ("email".equals(alert.getMethod2())) {
						sendEmail(subject, email, o.getEmail(), body);
					}
					if ("memo".equals(alert.getMethod1())) {
					sendNotification(id, o.getUsername(), subject, body);
					}
				}
			}
		}
	}

	public Notification getIncident(String incidentId) {
			NotificationModule handler = (NotificationModule) Application.getInstance().getModule(NotificationModule.class);
			Notification incident = new Notification();
			try {
				incident = handler.getIncident(incidentId);
			} catch (HelpdeskException e) {
				Log.getLog(getClass()).error(e.toString(), e);
			}
			return incident;
		}

	public void sendEmail(String subject, String sender, String receiver, String message) {
		SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
		try {
			String smtp = setup.get(SITE_SMPT);
			MailUtil.sendEmail(smtp, true, sender, receiver, subject, message);
			} catch (SetupException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}

	public void sendNotification(String fromId, String to, String subject, String body) {
			MessagingModule mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
			try {
				IntranetAccount intranetAccount = mm.getIntranetAccountByUserId(fromId);
				if (intranetAccount != null) {
					String toAddress = to + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
					String senderId = "";
					senderId = fromId;
					SmtpAccount smtpAccount = mm.getSmtpAccountByUserId(senderId);
					intranetAccount = mm.getIntranetAccountByUserId(senderId);
					String fromAddress = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
					Collection toList = new HashSet(1);
					toList.add(toAddress);
					Message message = new Message();
					message.setMessageId(UuidGenerator.getInstance().getUuid());
					message.setSubject(subject);
					message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
					message.setDate(new Date());
					message.setBody(body);
					message.setToIntranetList(new ArrayList(toList));
					message.setFrom(fromAddress);
					mm.sendMessage(smtpAccount, message, senderId, false);
				}
			} catch (MessagingException e) {
				Log.getLog(getClass()).error(e.toString(), e);
			}
		}

	public String buildMessage(Notification notify) {
		String incidentCode = notify.getIncidentCode();
		String companyName = notify.getCompanyName();
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
			incidentUrl = incidentUrl + "/ekms/helpdesk/incidentOpen.jsp?incidentId=" + id;
			contactUrl = contactUrl + "/ekms/helpdesk/contactDetails.jsp?contactID=" + contactId;
			companyUrl = companyUrl + "/ekms/helpdesk/companyView.jsp?cn=jsp_companyList_commain.table1&et=sel&companyID=" + companyId;
		} catch (SetupException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}

		Application app = Application.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, h:mm:ss a");
		String body = app.getMessage("helpdesk.escalate.message.header")+"<br><br>" +
				"<u>"+app.getMessage("helpdesk.escalate.label.incidentDetails")+"</u><br><br>" +
				"<b>"+app.getMessage("helpdesk.escalate.label.incidentNo")+" : </b>" + "<a href=\"" + incidentUrl + "\">" + incidentCode + "</a>" + "<br>" +
				"<b>"+app.getMessage("helpdesk.escalate.label.company")+" : </b>" + "<a href=\"" + companyUrl + "\">" + companyName + "</a>" + "<br>" +
				"<b>"+app.getMessage("helpdesk.escalate.label.contact")+" : </b>" + "<a href=\"" + contactUrl + "\">" + contact + "</a>" + "<br>" +
				"<b>"+app.getMessage("helpdesk.escalate.label.subject")+" : </b>" + subject + "<br>" +
				"<b>"+app.getMessage("helpdesk.escalate.label.description")+" : </b>" + desc + "<br>" +
				"<b>"+app.getMessage("helpdesk.escalate.label.severity")+" : </b>" + severity + "<br>" +
				"<b>"+app.getMessage("helpdesk.escalate.label.contactMethod")+" : </b>" + contactedBy + "<br>" +
				"<b>"+app.getMessage("helpdesk.escalate.label.incidentType")+" : </b>" + incidentType + "<br>" +
				"<b>"+app.getMessage("helpdesk.escalate.label.product")+" : </b> " + productName + "<br>" +
				"<b>"+app.getMessage("helpdesk.escalate.label.date")+" : </b>" + sdf.format(new Date()) + "<br>";
		return body;
	}
}
