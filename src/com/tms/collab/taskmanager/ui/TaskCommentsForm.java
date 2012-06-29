package com.tms.collab.taskmanager.ui;

import kacang.stdui.Form;
import kacang.stdui.TextField;
import kacang.stdui.Button;
import kacang.stdui.TextBox;
import kacang.Application;
import kacang.util.Log;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.Forward;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.Assignee;
import com.tms.collab.calendar.model.Attendee;
import com.tms.collab.calendar.ui.UserUtil;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.Util;
import com.tms.util.FormatUtil;

import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.TreeSet;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Feb 21, 2006
 * Time: 3:57:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaskCommentsForm extends Form {

    public static final String FORWARD_SUCCESS="success";
    public static final String FORWARD_FAIL="fail";
    public static final String FORWARD_CANCEL="cancel";

    protected TextBox tfComments;
    protected Button btnCancel;
    protected Button btnSubmit;

    protected String taskId;

    public void init() {
    	setMethod("POST");
        btnSubmit = new Button("btnSubmit", Application.getInstance().getMessage("general.label.submit","Submit"));
        btnCancel = new Button("btnCancel",Application.getInstance().getMessage("general.label.cancel","Cancel"));

        tfComments = new TextBox("tfComments");
        tfComments.setRows("5");
        tfComments.setCols("40");

        addChild(btnCancel);
        addChild(btnSubmit);
        addChild(tfComments);
    }

    public void onRequest(Event ev) {
        tfComments.setValue("");
        if (taskId!=null && !taskId.equals("")) {
            TaskManager manager = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            try {
                String comments = manager.getComments(taskId);
                comments = comments.replaceAll("<br>","\n");
                tfComments.setValue(comments);
            }
            catch(Exception e) {

            }
        }

    }

    public Forward onValidate(Event ev) {
        Forward forward = new Forward();

        if (btnSubmit.getAbsoluteName().equals(findButtonClicked(ev))) {
            String comments = (String)tfComments.getValue();
            comments = comments.replaceAll("\n","<br>");
            try {
                TaskManager manager = (TaskManager)Application.getInstance().getModule(TaskManager.class);
                manager.addComments(taskId,comments);
                sendNotification(taskId,ev);
                forward = new Forward(FORWARD_SUCCESS);
            }
            catch(Exception e) {
                forward = new Forward(FORWARD_FAIL);
            }
        }
        else if (btnCancel.getAbsoluteName().equals(findButtonClicked(ev))) {
            forward = new Forward(FORWARD_CANCEL);
        }

        return forward;
    }

    protected void sendNotification(String taskId, Event evt) {
        TaskManager manager = (TaskManager)Application.getInstance().getModule(TaskManager.class);
        MessagingModule mm = Util.getMessagingModule();
        Task task = new Task();
        try {
            task = manager.getTask(taskId);
            if (task!=null) {
                task.setComments(manager.getComments(taskId));
                Assignee assignee = manager.getAssignee(task.getId(),getWidgetManager().getUser().getId());

                // compose message
                SimpleDateFormat sdf = new SimpleDateFormat(FormatUtil.getInstance().getLongDateFormat());
                SimpleDateFormat sdf2 = new SimpleDateFormat(FormatUtil.getInstance().getLongDateTimeFormat());
                StringBuffer sb = new StringBuffer();
                sb.append("<U>" + Application.getInstance().getMessage("taskmanager.label.Task", "Task") + "</U>" );
                sb.append("<br><br>" + Application.getInstance().getMessage("taskmanager.label.title", "Title") + ": " + task.getTitle()+"\n" );
                sb.append("<br>" + Application.getInstance().getMessage("taskmanager.label.dueDate", "Due Date") + ": " + sdf.format(task.getDueDate())+"\n" );
                sb.append("<br>" + Application.getInstance().getMessage("taskmanager.label.dueTime", "Due Time") + ": " + new SimpleDateFormat("h:mm a").format(task.getDueDate())+"\n" );
                sb.append("<br>" + Application.getInstance().getMessage("taskmanager.label.description", "Description") + ": " + task.getDescription()+"\n" );
                sb.append("<br>" + Application.getInstance().getMessage("taskmanager.label.comments", "Comments") + ":<br> " + task.getComments()+"\n" );
                sb.append("<br>" + Application.getInstance().getMessage("taskmanager.label.commentsUpdatedBy", "Comments Updated By") + ": " + getWidgetManager().getUser().getProperty("firstName")+" "+getWidgetManager().getUser().getProperty("lastName")+"\n" );
                sb.append("<br>" + Application.getInstance().getMessage("taskmanager.label.OverallProgress", "Overall Progress") + ": " + new DecimalFormat("0.0").format(task.getOverallProgress()) +"%"+"\n" );
                sb.append("<br><a href=\"http://"+ evt.getRequest().getServerName()+":"+evt.getRequest().getServerPort()+evt.getRequest().getContextPath()+
                        "/ekms/calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId="+task.getEventId()+"&instanceId="+task.getInstanceId()+"\">" + Application.getInstance().getMessage("taskmanager.label.clickToView", "Click here to view") + "</a>"+"\n" );
                // add assignees
                Collection attendees = task.getAttendees();
                Collection ids = new TreeSet();
                for (Iterator iterator = attendees.iterator(); iterator.hasNext();)
                {
                    Attendee attendee = (Attendee) iterator.next();
                    if (!attendee.getUserId().equals(getWidgetManager().getUser().getId()))
                    {
                        ids.add(attendee.getUserId());
                    }
                }

                // add assigner
                if (!task.getAssignerId().equals(getWidgetManager().getUser().getId()))
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
                            String to = user.getUsername()+"@"+ MessagingModule.INTRANET_EMAIL_DOMAIN;
                            mm.sendStandardHtmlEmail(getWidgetManager().getUser().getId(),to,"","", Application.getInstance().getMessage("taskmanager.label.taskCommentsUpdated", "Task Comments Updated") + ": " + task.getTitle(), Application.getInstance().getMessage("taskmanager.label.taskCommentsUpdated", "Task Comments Updated"), sb.toString());
                        }
                        catch (Exception e)
                        {
                            Log.getLog(getClass()).error("Error sending task comments notification", e);
                        }
                    }
                }


            }
        }
        catch(Exception e) {

        }
    }

    public TextBox getTfComments() {
        return tfComments;
    }

    public void setTfComments(TextBox tfComments) {
        this.tfComments = tfComments;
    }

    public Button getBtnCancel() {
        return btnCancel;
    }

    public void setBtnCancel(Button btnCancel) {
        this.btnCancel = btnCancel;
    }

    public Button getBtnSubmit() {
        return btnSubmit;
    }

    public void setBtnSubmit(Button btnSubmit) {
        this.btnSubmit = btnSubmit;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDefaultTemplate() {
        return "/taskmanager/commentsForm";
    }
}
