package com.tms.collab.calendar.ui;

import kacang.stdui.Form;
import kacang.stdui.Button;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import kacang.Application;

import java.util.*;
import java.text.SimpleDateFormat;

import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.Appointment;
import com.tms.collab.calendar.model.Attendee;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.emeeting.Meeting;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.resourcemanager.model.ResourceManager;
import com.tms.collab.resourcemanager.model.Resource;
import com.tms.collab.resourcemanager.model.ResourceMailer;
import com.tms.collab.messaging.model.*;
import com.tms.util.FormatUtil;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jan 8, 2004
 * Time: 5:50:34 PM
 * To change this template use Options | File Templates.
 */
public class ConflictForm extends Form
{
    protected boolean memo,email;
    protected boolean update=false;
    protected Collection conflicts;
    protected Collection resourceConflicts;
    protected Collection recurringEvents;
    protected Button addButton, cancelButton, saveButton;
    protected CalendarEvent event;
    protected Set conflictAttendees;
    protected String viewUserId,viewResourceId;
    public final static String EVENT_TYPE_VIEW_ALL = "view all";
    public final static String EVENT_TYPE_VIEW_USER = "view user";
    public final static String EVENT_TYPE_VIEW_RESOURCE = "view resource";
    public final static String FORWARD_VIEW_ALL = "view all";
    public final static String FORWARD_VIEW_USER  = "view user";
    public final static String FORWARD_VIEW_RESOURCE  = "view resource";


    public ConflictForm()
    {
        super();
    }

    public ConflictForm(String s)
    {
        super(s);
    }

    public void init()
    {
        super.init();
        setMethod("POST");
        addButton = new Button("addbutton",Application.getInstance().getMessage("calendar.label.save","Save"));
        cancelButton = new Button("cancelbutton",Application.getInstance().getMessage("calendar.label.cancel","Cancel"));
        //saveButton = new Button("savebutton",Application.getInstance().getMessage("calendar.label.save","Save"));
        addChild(addButton);
        addChild(cancelButton);
    }

    public Forward actionPerformed(Event event)
    {
        String type = event.getType();
        if(EVENT_TYPE_VIEW_USER.equals(type)){
            String userId = event.getParameter("userId");
            setViewUserId(userId);
            return new Forward(FORWARD_VIEW_USER);
        }else if(EVENT_TYPE_VIEW_ALL.equals(type)){
            return new Forward(FORWARD_VIEW_ALL);
        }else if(EVENT_TYPE_VIEW_RESOURCE.equals(type)){
            setViewResourceId(event.getParameter("resourceId"));
            return new Forward(FORWARD_VIEW_RESOURCE);
        }
        return super.actionPerformed(event);
    }



    public String getDefaultTemplate()
    {
        return "calendar/conflictsform";
    }

    public Forward onValidate(Event event)
    {
        String buttonClicked = findButtonClicked(event);
        if(addButton.getAbsoluteName().equals(buttonClicked)){
        try{
            String[] attIds = event.getParameterValues("comAttendeesCB");
            Collection attendeeList = new TreeSet(); // Attendee objects
            if(attIds!=null){
                for (int i = 0; i < attIds.length; i++)
                {
                    String userId = attIds[i];
                    User tmpUser = UserUtil.getUser(userId.trim());
                    Attendee att = new Attendee();
                    att.setUserId(tmpUser.getId());
                    att.setProperty("username", tmpUser.getUsername());
                    att.setProperty("firstName", tmpUser.getProperty("firstName"));
                    att.setProperty("lastName", tmpUser.getProperty("lastName"));
                    att.setCompulsory(true);
                    att.setStatus(CalendarModule.ATTENDEE_STATUS_CONFIRMED);
                    attendeeList.add(att);

                }
            }
            attIds = event.getParameterValues("opAttendeesCB");
           try{
            if(attIds!=null){
                for (int i = 0; i < attIds.length; i++)
                {
                    String userId = attIds[i];
                    User tmpUser=null;
                    try{
                    tmpUser = UserUtil.getUser(userId.trim());
                    Attendee att = new Attendee();
                    att.setUserId(tmpUser.getId());
                    att.setProperty("username", tmpUser.getUsername());
                    att.setProperty("firstName", tmpUser.getProperty("firstName"));
                    att.setProperty("lastName", tmpUser.getProperty("lastName"));
                    att.setCompulsory(false);
                    att.setStatus(CalendarModule.ATTENDEE_STATUS_PENDING);
                    if(!(userId.trim().startsWith(User.class.getName())))
                    {
                        att.setProperty("username", att.getUserId());
                        att.setProperty("firstName", att.getUserId());
                        att.setEkpUser(false);
                        att.setNonEKPUser(true);
                    }
                    attendeeList.add(att);
                 	 	    }catch(Exception e){
                    	Attendee att = new Attendee();
                        att.setUserId(userId);
                        att.setProperty("username", userId);
                        att.setProperty("firstName", userId);
                        att.setProperty("lastName", "");
                        att.setCompulsory(false);
                        att.setStatus(CalendarModule.ATTENDEE_STATUS_PENDING);
                        attendeeList.add(att);
                    }
                }
            }
           }catch(Exception e){ 
        	
            }
            this.event.setAttendees(attendeeList);
            ResourceManager rm ;
            String [] resourceIds = event.getParameterValues("resourcesCB");
            rm = (ResourceManager)Application.getInstance().getModule(ResourceManager.class);
            Collection resourcesCol = new ArrayList();
            if(resourceIds!=null){
                for (int i = 0; i < resourceIds.length; i++)
                {
                    String resourceId = resourceIds[i];
					/**
					 * Trimming is since it was retrieved from request. Databases such as ORACLE
					 * is sensitive to white spaces
					 */
                    Resource resource = rm.getResource(resourceId.trim(),true);
                    resourcesCol.add(resource);
                }
            }
            this.event.setResources(resourcesCol);
            CalendarModule cm = (CalendarModule)Application.getInstance().getModule(CalendarModule.class);
            if(!update){
                cm.addCalendarEvent(null,this.event,getWidgetManager().getUser().getId(),true);
                if(this.event.getEventId().startsWith(Task.class.getName())){
                    TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
                    tm.addTask((Task)this.event);
                }
            }
            else{
                cm.updateCalendarEvent(this.event,getWidgetManager().getUser().getId(),true);
                if(this.event.getEventId().startsWith(Task.class.getName())){
                    TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
                    tm.updateTask((Task)this.event);
                }}
            if(recurringEvents!=null){
                for (Iterator iterator = recurringEvents.iterator(); iterator.hasNext();)
                {
                    CalendarEvent revent = (CalendarEvent) iterator.next();
                    cm.addRecurringEvent(revent,getWidgetManager().getUser().getId(),true);
                }
            }

            memo = ((Boolean)event.getRequest().getSession().getAttribute("memo")).booleanValue();
            email =((Boolean)event.getRequest().getSession().getAttribute("email")).booleanValue();
            if(memo||email){
                sendNotification(this.event,!update,event);
            }
            ResourceMailer.sendResourceApprovalMail(getWidgetManager().getUser().getId(),this.event,event);
            HttpSession  s= event.getRequest().getSession();
            s.removeAttribute("memo");
            s.removeAttribute("email");
            s.removeAttribute("event");
            s.removeAttribute("conflict");
            s.removeAttribute("recurringEvents");
            s.removeAttribute("notes");
            if(!update){

                return new Forward("added");
            }
            else{
                return new Forward("updated");
            }
        }catch(Exception e){
            Log.getLog(ConflictForm.class).error(e.toString(), e);
        }
        } else if(cancelButton.getAbsoluteName().equals(buttonClicked)){
            return new Forward("cancel");
        }
        return super.onValidate(event);
    }
    protected void sendNotification(CalendarEvent event,boolean newEvent,Event evt){
        try{
            MessagingModule mm;
            User user;
            SmtpAccount smtpAccount;
            Message message;

            mm = Util.getMessagingModule();
            user = getWidgetManager().getUser();
            String userId = user.getId();
            smtpAccount = mm.getSmtpAccountByUserId(user.getId());

            // construct the message to send
            message = new Message();
            message.setMessageId(UuidGenerator.getInstance().getUuid());
            // setMessageProperties(message,event,newEvent,evt);
            IntranetAccount intranetAccount;

            intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(getWidgetManager().getUser().getId());
            message.setFrom(intranetAccount.getFromAddress());

            Collection col = event.getAttendees();
            List memoList,emailList;
            memoList = new ArrayList(col.size());
            emailList = new ArrayList(col.size());
            for(Iterator i=col.iterator();i.hasNext();){
                Attendee att = (Attendee)i.next();
                if(!userId.equals(att.getUserId())){
                    if(memo){
                        intranetAccount = Util.getMessagingModule().getIntranetAccountByUserId(att.getUserId());
                        if(intranetAccount!=null){
                            String add = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
                            memoList.add(add);
                        }
                    }
                    if(email){
                        User tempuser = UserUtil.getUser(att.getUserId());
                        String email = (String)tempuser.getProperty("email1");
                        emailList.add(email);
                    }
                }
            }

            if(memoList.size()>0)
                message.setToIntranetList(memoList);

            Application app = Application.getInstance();
            if(newEvent)
                message.setSubject(app.getMessage("calendar.label.newAppointment", "New Appointment") + ": " + event.getTitle());
            else
                message.setSubject(app.getMessage("calendar.label.updatedAppointment", "Updated Appointment") + ": " + event.getTitle());

            String description =" - ";
            String sdesc = event.getDesc();
            if (sdesc != null && sdesc.trim().length() > 0){
                description = sdesc;
            }

            String temp = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">"+
                    "</head><body><style>.contentBgColorMail {background-color: #FFFFFF; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt}.contentFont {font-family: Arial, Helvetica, sans-serif; font-size: 8.5pt}"+
                    ".contentTitleFont {font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 7.5pt; color: #FFFFFF; background-color: #003366}"+
                    ".contentStrapColor {background-color: #E6E6CA; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt}</style>"+
                    "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr valign=\"MIDDLE\">"+
                    "<td height=\"22\" bgcolor=\"#003366\" class=\"contentBgColorMail\"><b><font color=\"#000000\" class=\"contentBgColorMail\">"+
                    "<b><U>" + app.getMessage("calendar.label.appointmentDetails", "Appointment Details") +
                    "</U></b></font></b></td><td align=\"right\" bgcolor=\"#003366\" class=\"contentBgColorMail\">&nbsp;</td>"+
                    "</tr><tr><td colspan=\"2\" valign=\"TOP\" bgcolor=\"#EFEFEF\" class=\"contentBgColorMail\">&nbsp;</td>"+
                    "</tr><tr><td colspan=\"2\" valign=\"TOP\"><table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"1\">"+
                    "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>"+
                    "<b>" + app.getMessage("calendar.label.scheduler", "Scheduler") +
                    "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">"+ event.getUserName()+"</td></tr>"+
                    "<tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("calendar.label.startDate", "Start Date") +
                    "</b></td>"+
                    "<td class=\"contentBgColorMail\">" + new SimpleDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()).format(event.getStartDate())+"</td>"+
                    "</tr><tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" +
                    app.getMessage("calendar.label.endDate", "End Date")+"</b></td>" +
                    "<td class=\"contentBgColorMail\">"+new SimpleDateFormat(FormatUtil.getInstance().getLongDateTimeFormat()).format(event.getEndDate())+"</td>"+
                    "</tr><tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" +app.getMessage("calendar.label.description", "Description") +
                    "</b></td><td class=\"contentBgColorMail\">"+
                    description + "</td></tr>";

            String agenda = " - ";
            String sagenda = event.getAgenda();
            if (sagenda != null && sagenda.trim().length() > 0){
                agenda = StringUtils.replace(sagenda,"\n","<br>");
            }

          //  if(event.getAgenda()!=null&& event.getAgenda().trim().length()>0){
                temp += "<tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" +app.getMessage("calendar.label.agenda", "Agenda") +
                        "</b></td>"+
                        "<td class=\"contentBgColorMail\">"+agenda + "</td></tr>";
          //  }

            String location = "- ";
            String slocation = event.getLocation();
            if (slocation != null && slocation.trim().length() > 0){
                location = slocation;
            }

         //   if(location!=null&&location.trim().length()>0){
                temp += "<tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>"
                        + app.getMessage("calendar.label.location", "Location") +"</b></td>"+
                        "<td class=\"contentBgColorMail\">"+location + "</td></tr>";
           // }

            StringBuffer resourceSb = new StringBuffer("");
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
            if (str.length() > 0 && !" - ".equals(str)) {
                str = str.substring(0, str.length() - 1);
            }
  //          if (str != null && str.trim().length() > 0) {
                temp += "<tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" + app.getMessage("calendar.label.resources", "Resources") + "</b></td>" +
                        "<td class=\"contentBgColorMail\">" + str + "</td></tr>";
//            }

            String notes = " - ";
            String snotes = (String) evt.getRequest().getSession().getAttribute("notes");
            if (snotes != null && snotes.trim().length() > 0){
                notes = StringUtils.replace(snotes,"\n","<br>");
            }

           // if(notes!=null&&notes.trim().length()>0){
                temp += "<tr><td class=\"contentBgColorMail\" align=\"right\" valign=\"top\" nowrap><b>" +
                        app.getMessage("calendar.label.notes", "Notes") +"</b></td>"+
                        "<td class=\"contentBgColorMail\">" + notes + "</td></tr>";

            //}
            String footer = "</table></td></tr><tr><td colspan=\"2\" valign=\"TOP\" bgcolor=\"#CCCCCC\" class=\"contentBgColorMail\">&nbsp;</td>"+
                    "</tr></table><p>&nbsp; </p></body></html>";

            String link = "<tr><td></td><td class=\"contentBgColorMail\" align=\"left\" valign=\"top\"  nowrap><a href=\"http://"+ evt.getRequest().getServerName()+":"+evt.getRequest().getServerPort()+evt.getRequest().getContextPath()+
                    "/ekms/calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId="+event.getEventId()+"&instanceId="+event.getInstanceId()+"\">" +app.getMessage("calendar.label.clickToView", "Click here to view") +
                    "</a>"+
                    "</td></tr>";
            message.setBody(temp+link+footer);

            message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
            message.setDate(new Date());

            if(memo)
                mm.sendMessage(smtpAccount, message, user.getId(), false);

            if(email){
                message.setToList(emailList);
                message.setToIntranetList(null);
                message.setBody(temp+footer);
                mm.sendMessage(smtpAccount, message, user.getId(), false);
            }
        }catch (Exception e){
            Log.getLog(getClass()).error(e.toString(), e);  //To change body of catch statement use Options | File Templates.
        }

    }


    public Collection getConflicts()
    {
        return conflicts;
    }

    public void setConflicts(Collection conflicts)
    {
        this.conflicts = conflicts;
        conflictAttendees = new TreeSet();
        Map allAttendees = new HashMap();
        for (Iterator iterator = conflicts.iterator(); iterator.hasNext();)
        {
            CalendarEvent event = (CalendarEvent) iterator.next();
            Collection atts = event.getAttendees();
            for (Iterator attit = atts.iterator(); attit.hasNext();)
            {
                Attendee attendee = (Attendee) attit.next();
                Collection conflictingAppts = (Collection)allAttendees.get(attendee.getUserId());
                if (conflictingAppts == null) {
                    conflictingAppts = new ArrayList();
                }
                conflictingAppts.add(event);
                allAttendees.put(attendee.getUserId(), conflictingAppts);
            }
        }
        Collection attendees = event.getAttendees();
        for (Iterator atts = attendees.iterator(); atts.hasNext();)
        {
            Attendee att = (Attendee) atts.next();
            if(allAttendees.containsKey(att.getUserId())) {
                Collection conflictingAppts = (Collection)allAttendees.get(att.getUserId());
                att.setProperty("conflicts", conflictingAppts); // store conflicting appointments in "conflicts" property
		if(att.getUserId()!=null){
                conflictAttendees.add(att.getUserId());
            }
        }
    }
    }

    public Button getAddButton()
    {
        return addButton;
    }

    public void setAddButton(Button addButton)
    {
        this.addButton = addButton;
    }

    public Button getCancelButton()
    {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton)
    {
        this.cancelButton = cancelButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(Button saveButton) {
        this.saveButton = saveButton;
    }

    public CalendarEvent getEvent()
    {
        return event;
    }

    public void setEvent(CalendarEvent event)
    {
        this.event = event;
    }

    public String getHeader(){
        String id = event.getEventId();
        String header = id.substring(0,id.indexOf("_"));
        if(Appointment.class.getName().equals(header))
            return Application.getInstance().getMessage("calendar.label.appointmentFilter","Appointment");
        else if(CalendarEvent.class.getName().equals(header))
            return Application.getInstance().getMessage("calendar.label.eventFilter","Event");
        else if(Meeting.class.getName().equals(header))
            return Application.getInstance().getMessage("calendar.label.e-MeetingFilter","E-Meeting");
        else if(Task.class.getName().equals(header))
            return Application.getInstance().getMessage("taskmanager.label.toDoTask","To Do Task");
        else
            return null;
    }

    public Set getConflictAttendees()
    {
        return conflictAttendees;
    }

    public void setConflictAttendees(Set conflictAttendees)
    {
        this.conflictAttendees = conflictAttendees;
    }

    public String getViewUserId()
    {
        return viewUserId;
    }

    public void setViewUserId(String viewUserId)
    {
        this.viewUserId = viewUserId;
    }

    public boolean isUpdate()
    {
        return update;
    }

    public void setUpdate(boolean update)
    {
        this.update = update;
    }

    public boolean isOwnerExcluded(){
        String userId = event.getUserId();
        Collection col = event.getAttendees();
        for (Iterator iterator = col.iterator(); iterator.hasNext();)
        {
            Attendee attendee = (Attendee) iterator.next();
            if(userId.equals(attendee.getUserId())&&attendee.isCompulsory())
                return false;
        }
        return true;
    }

    public Collection getResourceConflicts()
    {
        return resourceConflicts;
    }

    public void setResourceConflicts(Collection resourceConflicts)
    {
        this.resourceConflicts = resourceConflicts;
    }

    public String getViewResourceId()
    {
        return viewResourceId;
    }

    public void setViewResourceId(String viewResourceId)
    {
        this.viewResourceId = viewResourceId;
    }

    public Collection getRecurringEvents()
    {
        return recurringEvents;
    }

    public void setRecurringEvents(Collection recurringEvents)
    {
        this.recurringEvents = recurringEvents;
    }

}
