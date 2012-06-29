package com.tms.collab.taskmanager.model;

import com.tms.collab.calendar.model.Attendee;
import com.tms.collab.calendar.ui.UserUtil;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.Util;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.util.Log;

import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class TaskMailer
{
    public static void sentTaskCompletedNotification(Task task,Event evt,String senderId) throws DaoException
    {

        Application app = Application.getInstance();
        MessagingModule mm = Util.getMessagingModule();
        TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
        Assignee assignee = tm.getAssignee(task.getId(),senderId);

        // compose message
        SimpleDateFormat sdf = new SimpleDateFormat(FormatUtil.getInstance().getLongDateFormat());
        SimpleDateFormat sdf2 = new SimpleDateFormat(FormatUtil.getInstance().getLongDateTimeFormat());
        StringBuffer sb = new StringBuffer();
        sb.append("<U>" + app.getMessage("taskmanager.label.Task", "Task") + "</U>" );
        sb.append("<BR><BR>" + app.getMessage("taskmanager.label.title", "Title") + ": " + task.getTitle() );
        sb.append("<BR>" + app.getMessage("taskmanager", "Category") + ": " + task.getCategory()+ "\n" );
        sb.append("<BR>" + app.getMessage("taskmanager.label.dueDate", "Due Date") + ": " + sdf.format(task.getDueDate())+ "\n");
        sb.append("<BR>" + app.getMessage("taskmanager.label.dueTime", "Due Time") + ": " + new SimpleDateFormat("h:mm a").format(task.getDueDate())+ "\n");
        sb.append("<BR>" + app.getMessage("taskmanager.label.description", "Description") + ": " + task.getDescription()+ "\n");
        sb.append("<BR>" + app.getMessage("taskmanager.label.completedBy", "Completed By") + ": " + assignee.getName()+"\n");
        sb.append("<BR>" + app.getMessage("taskmanager.label.completedAt", "Completed At") + ": " + sdf2.format(assignee.getCompleteDate())+"\n");
        sb.append("<BR>" + app.getMessage("taskmanager.label.OverallProgress", "Overall Progress") + ": " + new DecimalFormat("0.0").format(task.getOverallProgress()) +"%\n");
        sb.append("<BR><BR><a href=\"http://"+ evt.getRequest().getServerName()+":"+evt.getRequest().getServerPort()+evt.getRequest().getContextPath()+
                "/ekms/calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId="+task.getEventId()+"&instanceId="+task.getInstanceId()+"\">" + app.getMessage("taskmanager.label.clickToView", "Click here to view") + "</a>");

        // add assignees
        Collection attendees = task.getAttendees();
        Collection ids = new TreeSet();
        for (Iterator iterator = attendees.iterator(); iterator.hasNext();)
        {
            Attendee attendee = (Attendee) iterator.next();
            if (!attendee.getUserId().equals(senderId))
            {
                ids.add(attendee.getUserId());
            }
        }

        // add assigner
        if (!task.getAssignerId().equals(senderId))
        {
            ids.add(task.getAssignerId());
        }

        // send message
        if(ids.size()>0)
        {
            for (Iterator iterator = ids.iterator(); iterator.hasNext();)
            {
                String id = (String) iterator.next();
                User user = null;
                try
                {
                    user = UserUtil.getUser(id);
                    String to = user.getUsername()+"@"+MessagingModule.INTRANET_EMAIL_DOMAIN;
                    mm.sendStandardHtmlEmail(senderId,to,"","", app.getMessage("taskmanager.label.taskCompletion", "Task Completion") + ": " + task.getTitle(), app.getMessage("taskmanager.label.taskCompletion", "Task Completion"), sb.toString());
                }
                catch (Exception e)
                {
                    Log.getLog(TaskMailer.class).error("Error sending task completion notification", e);
                }
            }
        }

    }

    public static void sendTaskReassignNotification(Task task,Event evt,String userId){
        try
        {
            MessagingModule mm;
            mm = Util.getMessagingModule();
            Application app =Application.getInstance();

            // construct the message to send
            // setMessageProperties(message,event,newEvent,evt);

            String temp = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">"+
                    "</head><body><style>.contentBgColorMail {background-color: #FFFFFF; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt}.contentFont {font-family: Arial, Helvetica, sans-serif; font-size: 8.5pt}"+
                    ".contentTitleFont {font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 7.5pt; color: #FFFFFF; background-color: #003366}"+
                    ".contentStrapColor {background-color: #E6E6CA; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt}</style>"+
                    "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr valign=\"MIDDLE\">"+
                    "<td height=\"22\" bgcolor=\"#003366\" class=\"contentBgColorMail\"><b><font color=\"#000000\" class=\"contentBgColorMail\">"+
                    "<b><U>" + app.getMessage("taskmanager.label.taskDetails", "Task Details") + "</U></b></font></b></td><td align=\"right\" bgcolor=\"#003366\" class=\"contentBgColorMail\">&nbsp;</td>"+
                    "</tr><tr><td colspan=\"2\" valign=\"TOP\" bgcolor=\"#EFEFEF\" class=\"contentBgColorMail\">&nbsp;</td>"+
                    "</tr><tr><td colspan=\"2\" valign=\"TOP\"><table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"1\">"+
                    "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>"+
                    "<b>" + app.getMessage("taskmanager.label.title", "Title") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">"+ task.getTitle() +"</td></tr>"+
                    "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>"+
                    "<b>" + app.getMessage("taskmanager.label.category", "Category") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">"+ task.getCategory()+"</td></tr>"+
                    "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>"+
                    "<b>" + app.getMessage("taskmanager.label.AssignedBy", "Assigned By") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">"+ UserUtil.getUser(task.getAssignerId()).getName()+"</td></tr>"+
                    "<tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("taskmanager.label.dueDate", "Due Date") + "</b></td>"+
                    "<td class=\"contentBgColorMail\">" + new SimpleDateFormat(FormatUtil.getInstance().getLongDateFormat()).format(task.getDueDate())+"</td>"+
                    "</tr><tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("taskmanager.label.description", "Description") + "</b></td><td class=\"contentBgColorMail\">"+
                    task.getDescription() + "</td></tr>";

            String footer = "</table></td></tr><tr><td colspan=\"2\" valign=\"TOP\" bgcolor=\"#CCCCCC\" class=\"contentBgColorMail\">&nbsp;</td>"+
                    "</tr></table><p>&nbsp; </p></body></html>";



            String link = "<tr><td></td><td class=\"contentBgColorMail\" align=\"left\" valign=\"top\"  nowrap><a href=\"http://"+ evt.getRequest().getServerName()+":"+evt.getRequest().getServerPort()+evt.getRequest().getContextPath()+
                    "/ekms/calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId="+task.getId()+"\">" + app.getMessage("taskmanager.label.clickToView", "Click here to view") + "</a>"+
                    "</td></tr>";
            User user;
            StringBuffer sb = new StringBuffer(temp+link+footer);
            Collection col = task.getAttendees();
            for(Iterator i=col.iterator();i.hasNext();)
            {
                Attendee att = (Attendee)i.next();
                if(!userId.equals(att.getUserId()))
                {
                    user = UserUtil.getUser(att.getUserId());
                    String add = user.getUsername()+ "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
                    mm.sendStandardHtmlEmail(userId,add,"","",app.getMessage("taskmanager.label.taskReassignment", "Task Reassignment") + ": " + task.getTitle(),app.getMessage("taskmanager.label.taskReassignment", "Task Reassignment"),sb.toString());
                }
            }

        }
        catch (Exception e){
            Log.getLog(TaskMailer.class).error(e);
        }

    }

}
