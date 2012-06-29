package com.tms.collab.calendar.ui;

import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendarException;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.messaging.model.MessagingException;
import com.tms.collab.messaging.model.MessagingModule;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.*;
import kacang.services.security.SecurityException;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.TextBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import javax.mail.internet.AddressException;

import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Dec 23, 2003
 * Time: 7:29:47 PM
 * To change this template use Options | File Templates.
 */
public class RejectForm extends Form {
    private TextBox reasonTB;
    private Button rejectButton, cancelButton;
    private String eventId, instanceId, attendeeId;

    public RejectForm() {
    }

    public RejectForm(String s) {
        super(s);
    }

    public void init() {
        super.init();
        setMethod("POST");
        reasonTB = new TextBox("reasontb");
        reasonTB.setRows("8");
        rejectButton = new Button("rejectButton", Application.getInstance().getMessage("calendar.label.reject", "Reject"));
        cancelButton = new Button("cancelButton", Application.getInstance().getMessage("calendar.label.cancel", "Cancel"));
        addChild(cancelButton);
        addChild(reasonTB);
        addChild(rejectButton);
    }

    public Forward onValidate(Event event) {
        super.onValidate(event);
        Forward fwd = null;
        String buttonClicked = findButtonClicked(event);
        if (rejectButton.getAbsoluteName().equals(buttonClicked)) {
            CalendarModule cm = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
            cm.rejectAppointment(eventId, instanceId, attendeeId, reasonTB.getValue().toString());

            try {
				CalendarEvent cEvent =(CalendarEvent)cm.getCalendarEvent(eventId);
				  try {
					  if(cEvent.isEkpEvent()){
                sendRejectNotification(cm.getCalendarEvent(eventId, instanceId), reasonTB.getValue().toString(), getWidgetManager().getUser().getId());
					  }else{
						  if(cEvent.getOrganizerEmail()!=null){
							  sendRejectNotification(cm.getCalendarEvent(eventId, instanceId), reasonTB.getValue().toString(), getWidgetManager().getUser().getId(),cm.createReplyCalendarFile(eventId,CalendarModule.ATTENDEE_STATUS_REJECT));
						  }
					  }
		            }catch (Exception e) {
		                Log.getLog(getClass()).error("Error rejecting calendar event", e);
		            }                
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error rejecting calendar event", e);
            }
            fwd = new Forward("rejected");
        }
        if (cancelButton.getAbsoluteName().equals(buttonClicked)) {
            fwd = new Forward("cancel");
        }
        // reset form
        removeChildren();
        init();
        return fwd;
    }

    public String getDefaultTemplate() {
        return "calendar/rejectform";
    }

    public TextBox getReasonTB() {
        return reasonTB;
    }

    public void setReasonTB(TextBox reasonTB) {
        this.reasonTB = reasonTB;
    }

    public Button getRejectButton() {
        return rejectButton;
    }

    public void setRejectButton(Button rejectButton) {
        this.rejectButton = rejectButton;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }

    public static void sendRejectNotification(CalendarEvent event, String reason, String userId) throws SecurityException, AddressException, MessagingException {
        SecurityService ss;
        User fromUser;
        MessagingModule mm;
        String prefix="";
        ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
        fromUser = ss.getUser(userId);
        if (reason == null || "".equals(reason))
            reason = "No reason given.";

        if (reason != null && reason.trim().length() > 0){
            reason = StringUtils.replace(reason,"\n","<br>");
        }


            if (event.getEventId() != null && event.getEventId().trim().length() > 0) {
                prefix = event.getEventId().substring(0, event.getEventId().indexOf("_"));
            }

        StringBuffer sb = new StringBuffer();
        sb.append(fromUser.getName() + " has rejected the following ");
        if("com.tms.collab.taskmanager.model.Task".equals(prefix))
        sb.append("task");
        else if("com.tms.collab.calendar.model.Appointment".equals(prefix))
        sb.append("appointment");
        else if("com.tms.collab.emeeting.Meeting".equals(prefix))
        sb.append("e-meeting");
        else
        sb.append("appointment");
        sb.append(" : \n\n<br><br>");
        sb.append("Title : " + event.getTitle() + "\n<br><br>");
        sb.append("Description : <br> " + event.getDesc() + "\n<br><br>");
        sb.append("Start time : " + event.getSDate() + "\n<br><br>");
        sb.append("End time : " + event.getEDate() + "\n\n<br><br>");
        sb.append("Reason : <br>");
        sb.append(reason + "\n\n<br><br>");

        String toEmail = ss.getUser(event.getUserId()).getUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
        String ccEmail = "";
        String bccEmail = "";
        String subject = "Notification of Rejected ";
        if("com.tms.collab.taskmanager.model.Task".equals(prefix))
        	subject+="Task";
        else if("com.tms.collab.calendar.model.Appointment".equals(prefix))
        	subject+="Appointment";
        else if("com.tms.collab.emeeting.Meeting".equals(prefix))
        	subject+="E-Meeting";
        else
        	subject+="Appointment";
      
        String title = subject;
        title+=" Details";
        title="<u>"+title+"</u>";
        subject+=": "+event.getTitle();
        String content = sb.toString();

        mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
        mm.sendStandardHtmlEmail(userId, toEmail, ccEmail, bccEmail, subject, title, content);
    }

//Added by Arun on 14th July,2006
// Overrided with icsMessage
public static void sendRejectNotification(CalendarEvent event, String reason, String userId,String icsMessage) throws SecurityException, AddressException, MessagingException {
    SecurityService ss;
    User fromUser;
    MessagingModule mm;
    ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
    fromUser = ss.getUser(userId);
    if (reason == null || "".equals(reason))
        reason = "No reason given.";
    StringBuffer sb = new StringBuffer();
    sb.append(fromUser.getName() + " has rejected the following appointment : \n\n<br><br>");
    sb.append("Title : " + event.getTitle() + "\n<br>");
    sb.append("Description : " + event.getDescription() + "\n<br>");
    sb.append("Start time : " + event.getStartDate() + "\n<br>");
    sb.append("End time : " + event.getEndDate() + "\n\n<br><br>");
    sb.append("Reason : \n<br>");
    sb.append(reason + "\n\n<br><br>");

    //String toEmail = ss.getUser(event.getUserId()).getUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
    String toEmail = event.getOrganizerEmail();
    String ccEmail = "";
    String bccEmail = "";
    String subject = "Notification of rejected appointment ";
    String title = subject;
    String content = sb.toString();

    mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
    mm.sendStandardHtmlEmail(userId, toEmail, ccEmail, bccEmail, subject, title, content,icsMessage);
}


}
