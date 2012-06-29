package com.tms.collab.emeeting.ui;

import com.tms.collab.messaging.ui.ComposeMessageForm;
import com.tms.collab.emeeting.MeetingHandler;
import com.tms.collab.emeeting.Meeting;
import com.tms.collab.emeeting.MeetingException;
import com.tms.collab.emeeting.AgendaItem;
import com.tms.collab.calendar.model.Attendee;
import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.ui.UserUtil;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.resourcemanager.model.Resource;
import com.tms.util.FormatUtil;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Aug 12, 2004
 * Time: 5:24:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageForm extends ComposeMessageForm{
    private String meetingId;

    public void onRequest(Event event) {
        super.onRequest(event);    //To change body of overridden methods use File | Settings | File Templates.
        if(meetingId!=null&&meetingId.trim().length()>0){
            populateForm(event);
        }
    }

    private void populateForm(Event event){
        MeetingHandler mh = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
        try {
            Meeting meeting = mh.getMeeting(meetingId,true);
            getTfSubject().setValue(Application.getInstance().getMessage("emeeting.label.meetingnotification","Meeting Notification"));
            String message = getMessageBody(meeting, event);
            getTbBody().setValue(message);

        } catch (MeetingException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);  //To change body of catch statement use Options | File Templates.
        } catch (kacang.services.security.SecurityException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);  //To change body of catch statement use Options | File Templates.
        }
    }

    public static String getMessageBody(Meeting meeting, Event evt) throws kacang.services.security.SecurityException {

        StringBuffer sb = new StringBuffer();
        SimpleDateFormat format = new SimpleDateFormat(FormatUtil.getInstance().getLongDateTimeFormat());
        String title = " - ";
        String stitle = meeting.getTitle();
        if (stitle != null && stitle.trim().length() > 0){
            title = stitle;
        }
        Locale locale = Application.getInstance().getLocale();
        sb.append("<B><U>" + Application.getInstance().getMessage("emeeting.label.meetingdetails","Meeting Details") + "</B></U>\n<br>");
        sb.append( "<B>" + Application.getInstance().getMessage("emeeting.label.title","Title") + ":</B> "+title+"\n<br>");
        sb.append( "<B>" + Application.getInstance().getMessage("emeeting.label.starts","Starts") + ":</B> "+format.format(meeting.getEvent().getStartDate())+"\n<br>");
        sb.append( "<B>" + Application.getInstance().getMessage("emeeting.label.ends","Ends") + ":</B> "+format.format(meeting.getEvent().getEndDate())+"\n<br>");
        String secretaryName = " - ";
        String sname = meeting.getSecretaryName();
        if (sname != null && !"".equals(sname) && !" - ".equals(sname) && sname.trim().length() > 0){
            secretaryName = sname;
        }
        sb.append( "<B>" + Application.getInstance().getMessage("emeeting.label.secretary","Secretary") + ":</B> "+secretaryName+"\n<br>");
        sb.append( "<B>" + Application.getInstance().getMessage("emeeting.label.compulsory","Compulsory") + ":</B> ");
        Collection col = meeting.getEvent().getAttendees();
        int i = 0;
        for (Iterator iterator = col.iterator(); iterator.hasNext();) {
            Attendee attendee = (Attendee) iterator.next();
            if(attendee.isCompulsory()){
                if(i>0)
                    sb.append(", ");
                sb.append(attendee.getName());
                i++;
            }
        }
        sb.append("\n<br><B>" + Application.getInstance().getMessage("emeeting.label.attendees","Attendees") + ":</B> ");
        i = 0;
        for (Iterator iterator = col.iterator(); iterator.hasNext();) {
            Attendee attendee = (Attendee) iterator.next();
            if(!attendee.isCompulsory()){
                if(i>0)
                    sb.append(", ");
                sb.append(attendee.getName());
                i++;
            }
        }
        if(i==0)
            sb.append("-");

        String description = " - ";
        String sdesc = meeting.getDescription();

        if (sdesc != null && sdesc.trim().length() > 0){
            description = StringUtils.replace(sdesc,"\r","<br>");
        }
        sb.append("\n<br><B>" + Application.getInstance().getMessage("emeeting.label.description","Description") + ":</B><br>\n "+ description);

        String location = " - ";
        String slocation = meeting.getEvent().getLocation();
        if (slocation != null && slocation.trim().length() > 0){
            location = slocation;
        }
        //if(meeting.getEvent().getLocation()!=null&&meeting.getEvent().getLocation().trim().length()>0)
        sb.append("\n<br><B>" + Application.getInstance().getMessage("emeeting.label.location","Location") + ":</B> "+location);

/*
        sb.append( "-------------------------------------------------------------------\n<br>\n<br>");
*/
        Application app = Application.getInstance();
        StringBuffer resourceSb = new StringBuffer("");
        CalendarEvent event = meeting.getEvent();

        if(event != null){
            Collection resources = event.getResources();
            int size = resources.size();
            if(resources != null && size >0){
                for (Iterator iterator = resources.iterator(); iterator.hasNext();) {
                    Resource obj =  (Resource)iterator.next();
                    String name = obj.getName();
                    resourceSb.append(name);
                    resourceSb.append(",");
                }
            }else{
                resourceSb.append(" - ");
            }
            String str = resourceSb.toString();
            if (str.length() > 0) {
                str = str.substring(0, str.length() - 1);
            }
            if (str != null && str.trim().length() > 0) {
                sb.append("\n<br><b>" + app.getMessage("calendar.label.resources", "Resources") + ": </b>" + str +"\n");
            }
        }
        String notifyNote = " - ";
        if(meeting.getNotify()==null){
        	meeting.setNotify("");
        }
        String snotifyNote = meeting.getNotify().toString();
        if (snotifyNote != null && snotifyNote.trim().length() > 0){
            notifyNote = StringUtils.replace(snotifyNote,"\r","<br>");
        }
        //if(meeting.getEvent().getLocation()!=null&&meeting.getEvent().getLocation().trim().length()>0)
        sb.append("\n<br><B>" + Application.getInstance().getMessage("calendar.label.notes", "Notes") + ":</B><br>\n"+notifyNote+"\n");

        sb.append( "\n<br><B>" + Application.getInstance().getMessage("emeeting.label.lastmodified","Last Modified") + ":</B> "+format.format(meeting.getEvent().getLastModified())+
                " " + Application.getInstance().getMessage("emeeting.label.by","by") + " "+UserUtil.getUser(meeting.getEvent().getLastModifiedBy()).getName() +"\n");

        Collection agendas = meeting.getMeetingAgenda();
        if(agendas!=null&&agendas.size()>0){
            sb.append("<B><U>" + Application.getInstance().getMessage("emeeting.label.agenda","Agenda") + "</U></b>\n<br>");
            int parentNumber = 1;
            for (Iterator iterator1 = agendas.iterator(); iterator1.hasNext();) {
                AgendaItem agendaItem = (AgendaItem) iterator1.next();
                sb.append("\n<br><B>"+parentNumber+".0.</B> "+agendaItem.getTitle()+"\n<br>");
                if(agendaItem.getNotes()!=null&&agendaItem.getNotes().trim().length()>0){
                    String note = StringUtils.replace(agendaItem.getNotes(),"\n","<br>");
                    sb.append("<B>" + Application.getInstance().getMessage("emeeting.label.minutes","MINUTES") + ":</B>\n<br>"+note+"\n<br>");
                }
                if(agendaItem.getAction()!=null&&agendaItem.getAction().trim().length()>0){
                    String action = StringUtils.replace(agendaItem.getAction(),"\n","<br>");
                    sb.append("\n<br><B>" + Application.getInstance().getMessage("emeeting.label.actions","ACTIONS") + ":</B>\n<br>"+action+"\n<br>");
                }
                if(agendaItem.getTasks()!=null&&agendaItem.getTasks().size()>0){
                    sb.append("<br><B>" + Application.getInstance().getMessage("emeeting.label.task","TASK") + "" + (agendaItem.getTasks().size()>1?"S":"") + ":</B>");
                    for (Iterator taskIterator = agendaItem.getTasks().iterator(); taskIterator.hasNext();) {
                        Task task = (Task) taskIterator.next();
                        sb.append("<br>"+task.getTitle());
                        sb.append("<br>"+task.getDescription());
                        sb.append("<br>" + Application.getInstance().getMessage("emeeting.label.assignedto","Assigned to") + " ");
                        Collection attendees = task.getAttendees();
                        int attNum= 0;
                        for (Iterator attIterator = attendees.iterator(); attIterator.hasNext();) {
                            Attendee attendee = (Attendee) attIterator.next();
                            if(attNum>0)
                                sb.append(", ");
                            sb.append(attendee.getName());
                        }
                        if(task.isCompleted())
                        {
                            sb.append("<br>" + Application.getInstance().getMessage("emeeting.label.completedon","Completed on") + " "+format.format(task.getCompleteDate()));
                        }else{
                            sb.append("<br>" + Application.getInstance().getMessage("emeeting.label.dueon","Due on") + " "+format.format(task.getDueDate()));
                        }
                        sb.append("<br>");
                    }
                }
                if(agendaItem.getChildren()!=null&&agendaItem.getChildren().size()>0){
                    int childNumber = 1;
                    for (Iterator childIterator = agendaItem.getChildren().keySet().iterator(); childIterator.hasNext();) {
                        AgendaItem item = (AgendaItem) agendaItem.getChildren().get(childIterator.next());
                        if(item!=null){  /*table cellspacing="0" cellpadding="0" border="0"> <TR><td>&nbsp;&nbsp;&nbsp;</td><td*/
                            sb.append("\n<br>" +
                                    "<div style=\"margin-left:15px;\">" +
                                    "" +
                                    "\t<B>"+parentNumber+"."+childNumber+".</B> "+item.getTitle()+"\n<br>");
                            if(item.getNotes()!=null&&item.getNotes().trim().length()>0){
                                String notes = StringUtils.replace(item.getNotes(),"\n","<br>");
                                sb.append("\t<B>" + Application.getInstance().getMessage("emeeting.label.minutes","MINUTES") + ":</B>\n<br>\t"+notes+"\n<br>\n<br>");
                            }

                            if(item.getAction()!=null&&item.getAction().trim().length()>0){
                                String actions = StringUtils.replace(item.getAction(),"\n","<br>");
                                sb.append("\t<B>" + Application.getInstance().getMessage("emeeting.label.actions","ACTIONS") + ":</B>\n<br>\t"+actions+"\n<br>");
                            }
                            if(item.getTasks()!=null&&item.getTasks().size()>0){
                                sb.append("<br><B>" + Application.getInstance().getMessage("emeeting.label.task","TASK") + ""+ (item.getTasks().size()>1?"S":"") + ":</B>");
                                for (Iterator taskIterator = item.getTasks().iterator(); taskIterator.hasNext();) {
                                    Task task = (Task) taskIterator.next();
                                    sb.append("<br>"+task.getTitle());
                                    sb.append("<br>"+task.getDescription());
                                    sb.append("<br>" + Application.getInstance().getMessage("emeeting.label.assignedto","Assigned to") + " ");
                                    Collection attendees = task.getAttendees();
                                    int attNum= 0;
                                    for (Iterator attIterator = attendees.iterator(); attIterator.hasNext();) {
                                        Attendee attendee = (Attendee) attIterator.next();
                                        if(attNum>0)
                                            sb.append(", ");
                                        sb.append(attendee.getName());
                                    }
                                    if(task.isCompleted())
                                    {
                                        sb.append("<br>" + Application.getInstance().getMessage("emeeting.label.completedon","Completed on") + " "+format.format(task.getCompleteDate()));
                                    }else{
                                        sb.append("<br>" + Application.getInstance().getMessage("emeeting.label.dueon","Due on") + " "+format.format(task.getDueDate()));
                                    }
                                    sb.append("<br>");
                                }
                            }
                            sb.append("</div>");/*/td></tr></table*/
                        }
                        childNumber++;
                    }
                }
                parentNumber++;
            }
        }
        sb.append("<br><br><a href=\"http://" + evt.getRequest().getServerName() + ":" + evt.getRequest().getServerPort() + evt.getRequest().getContextPath() +
                "/ekms/calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId=" + event.getEventId() + "&instanceId=" + event.getInstanceId() + "\">" + app.getMessage("calendar.label.clickToView", "Click here to view") + "</a>");
        return sb.toString();
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }
}
