package com.tms.collab.resourcemanager.model;

import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.ui.UserUtil;
import com.tms.collab.messaging.model.*;
import com.tms.collab.taskmanager.model.Task;
import com.tms.util.FormatUtil;
import kacang.services.security.User;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.util.Log;
import kacang.ui.Event;
import kacang.Application;

import javax.mail.internet.AddressException;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ResourceMailer
{
    public static void sendResourceApprovalMail(String senderId,CalendarEvent event,Event evt) throws SecurityException, AddressException
    {
        try
        {
            Collection resources = event.getResources();
            Collection receiverIds = new TreeSet();
            for (Iterator iterator = resources.iterator(); iterator.hasNext();)
            {
                Resource resource = (Resource) iterator.next();
                if(resource.isRequireApproval()){
                    receiverIds.addAll(resource.getAuthorities());
                }
            }
            if(receiverIds.size()>0){
            MessagingModule mm = Util.getMessagingModule();
            StringBuffer sb = new StringBuffer();

            SimpleDateFormat sdf = FormatUtil.getInstance().getDateFormat(FormatUtil.LONG_DATE_TIME);

            Application app = Application.getInstance();
            sb.append(app.getMessage("calendar.label.scheduler", "Scheduler") + ": " + event.getUserName() );
            sb.append("<BR><BR>" + app.getMessage("calendar.label.startDate", "Start Date") + ": " + sdf.format(event.getStartDate()==null?event.getCreationDate():event.getStartDate())+ "\n");
            sb.append("<BR>" + app.getMessage("calendar.label.endDate", "End Date") + ": " + sdf.format(event instanceof Task?((Task)event).getDueDate():event.getEndDate())+ "\n");
            sb.append("<BR>" + app.getMessage("calendar.label.description", "Description") + ": " + event.getDesc()+ "\n");
            if(event.getAgenda()!=null&&event.getAgenda().length()>0){
                sb.append("<BR>" + app.getMessage("calendar.label.agenda", "Agenda") + ": " + event.getAgen()+ "\n");
            }
            if(event.getLocation()!=null&&event.getLocation().length()>0){
                sb.append("<BR>" + app.getMessage("calendar.label.location", "Location") + ": " + event.getLocation()+ "\n<br>");
            }
            if(receiverIds.size()>0){
                for (Iterator iterator = receiverIds.iterator(); iterator.hasNext();)
                {
                    String id = (String) iterator.next();
                    StringBuffer ub = new StringBuffer(sb.toString());
                    ub.append("\n<br>" + app.getMessage("resourcemanager.label.resources", "Resource(s)") + ": \n<br>");
                    for (Iterator riterator = resources.iterator(); riterator.hasNext();)
                    {
                        Resource resource = (Resource) riterator.next();
                        if(resource.isRequireApproval()&&resource.getAuthorities().contains(id))
                            ub.append("<li> " + resource.getName()+ "\n</li><br>");
                    }
                    ub.append("\n<br>");
                    ub.append("<br><br><a href=\"\" onClick=\"javascript:window.open('http://"+ evt.getRequest().getServerName()+":"+evt.getRequest().getServerPort()+evt.getRequest().getContextPath()+"/ekms/calendar/mailresourceapprove.jsp?eventId="+event.getEventId()+"&userId="+id+"','approveRes','scrollbars=yes,resizable=yes,width=350,height=186');return false;\" onMouseOver=\"window.status='Approve';return (true);\">" + app.getMessage("resourcemanager.label.clickForApproval", "Click here for approval details") + "</a>");
                    ub.append("&nbsp;&nbsp;<a href=\"\" onClick=\"javascript:window.open('http://"+ evt.getRequest().getServerName()+":"+evt.getRequest().getServerPort()+evt.getRequest().getContextPath()+"/ekms/calendar/mailresourcereject.jsp?eventId="+event.getEventId()+"&userId="+id+"','approveRes','scrollbars=yes,resizable=yes,width=350,height=186');return false;\" onMouseOver=\"window.status='Reject';return (true);\">" + app.getMessage("resourcemanager.label.clickToReject", "Click here to reject") + "</a>");
                    User user = UserUtil.getUser(id);
                    String to = user.getUsername()+"@"+MessagingModule.INTRANET_EMAIL_DOMAIN;
                    mm.sendStandardHtmlEmail(senderId,to,"","",app.getMessage("resourcemanager.label.resourceApproval", "Resource Approval"),app.getMessage("resourcemanager.label.resourceApproval", "Resource Approval"),ub.toString());
                }
            }
            }
        } catch (MessagingException e)
        {
            Log.getLog(ResourceMailer.class).error(e);  //To change body of catch statement use Options | File Templates.
        }

    }

    public static void sendRejectNotification(CalendarEvent event, Resource resource, String reason,String userId) throws SecurityException, AddressException, MessagingException {
        SecurityService ss;
        User toUser;
        MessagingModule mm;
        Application app = Application.getInstance();
        if(reason==null||"".equals(reason))
            reason = app.getMessage("resourcemanager.label.noReason", "No reason given.");
        StringBuffer sb = new StringBuffer();
        String rejectMessage = app.getMessage("resourcemanager.label.rejectedMessage", "Your resource booking of {0} for the purpose of {1} has been REJECTED due to the following reason: <br><br><li>{2}</li><br><br>", new Object[] { resource.getName(), event.getTitle(), reason, new Date() });
        sb.append(rejectMessage);
        ss = (SecurityService) app.getService(SecurityService.class);
        toUser = ss.getUser(event.getUserId());

        String toEmail = toUser.getUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
        String ccEmail = "";
        String bccEmail = "";
        String subject = app.getMessage("resourcemanager.label.resourceRejected", "Notification of rejected resource booking");
        String title = subject;
        String content = sb.toString();

        mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
        mm.sendStandardHtmlEmail(userId, toEmail, ccEmail, bccEmail, subject, title, content);
    }

    public static void sendApproveNotification(CalendarEvent event, Collection resources,String userId,Event evt) throws SecurityException, AddressException, MessagingException {
        SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
        User toUser;
        MessagingModule mm;
        Application app = Application.getInstance();
        StringBuffer sb = new StringBuffer();

        User approver = ss.getUser(userId);
        sb.append(app.getMessage("resourcemanager.label.approvedBy", "Approved By") + ": "+approver.getName()+"\n");
        sb.append("<br><br>" + app.getMessage("resourcemanager.label.resourceBooking", "Resource Booking") + ":\n");
        for (Iterator iterator = resources.iterator(); iterator.hasNext();)
        {
            Resource resource = (Resource) iterator.next();
            sb.append("<br><li>" + resource.getName()+ "</li>\n");
        }

        sb.append("<br><br>" + app.getMessage("calendar.label.startDate", "Title") + ": "+event.getTitle());
        sb.append("<br>" + app.getMessage("calendar.label.startDate", "Start Date") + ": "+ event.getStartDate()==null?event.getCreationDate():event.getStartDate());
        sb.append("<br>" + app.getMessage("calendar.label.endDate", "End Date") + ": "+ (event instanceof Task?((Task)event).getDueDate():event.getEndDate()));
        sb.append("<br>" + app.getMessage("calendar.label.description", "Description") + ": "+event.getDesc());
        if(event.getAgenda()!=null&&!"".equals(event.getAgenda()))
            sb.append("<br>" + app.getMessage("calendar.label.agenda", "Agenda") + ": "+event.getAgen());
        if(event.getLocation()!=null&&!"".equals(event.getLocation()))
            sb.append("<br>" + app.getMessage("calendar.label.location", "Location") + ": "+event.getLocation());

        sb.append("<BR><BR><a href=\"http://"+ evt.getRequest().getServerName()+":"+evt.getRequest().getServerPort()+evt.getRequest().getContextPath()+
                "/ekms/calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId="+event.getEventId()+"&instanceId="+event.getInstanceId()+"\">" + app.getMessage("resourcemanager.label.clickToView", "Click here to view") + "</a>");

        toUser = ss.getUser(event.getUserId());

        String toEmail = toUser.getUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
        String ccEmail = "";
        String bccEmail = "";
        String subject = app.getMessage("resourcemanager.label.resourceApproved", "Resource Booking Approved");
        String title = subject;
        String content = sb.toString();

        mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
        mm.sendStandardHtmlEmail(userId, toEmail, ccEmail, bccEmail, subject, title, content);
    }

}
