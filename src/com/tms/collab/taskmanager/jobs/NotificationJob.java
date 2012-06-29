package com.tms.collab.taskmanager.jobs;

import com.tms.collab.taskmanager.model.Assignee;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.messaging.model.*;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.operator.*;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import javax.mail.internet.AddressException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

public class NotificationJob extends BaseJob
{
    public void execute(JobTaskExecutionContext jobTaskExecutionContext) throws SchedulingException
    {
        sendAlmostDue();
        sendOverdue();
    }

    protected void sendAlmostDue()
    {
        TaskManager handler = (TaskManager) Application.getInstance().getModule(TaskManager.class);
        Calendar start = Calendar.getInstance();
        start.set(Calendar.HOUR, start.getMaximum(Calendar.HOUR));
        start.set(Calendar.MINUTE, start.getMaximum(Calendar.MINUTE));
        start.set(Calendar.MINUTE, start.getMaximum(Calendar.MINUTE));
        Calendar end = Calendar.getInstance();
        end.add(Calendar.DAY_OF_YEAR, 2);
        end.set(Calendar.HOUR, end.getMinimum(Calendar.HOUR));
        end.set(Calendar.MINUTE, end.getMinimum(Calendar.MINUTE));
        end.set(Calendar.SECOND, end.getMinimum(Calendar.SECOND));
        DaoQuery query = new DaoQuery();
        OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
        parenthesis.addOperator(new OperatorGreaterThan("dueDate", start.getTime(), null));
        parenthesis.addOperator(new OperatorLessThan("dueDate", end.getTime(), DaoOperator.OPERATOR_AND));
        query.addProperty(parenthesis);
        query.addProperty(new OperatorEquals("completed", "0", DaoOperator.OPERATOR_AND));
        try
        {
            Collection list = handler.getTasks(query, 0, -1, null, false);
            sendMessages(list, false);
        }
        catch (DaoException e)
        {
            Log.getLog(getClass()).error("Error while retrieving tasks almost overdue", e);
        }
    }

    protected void sendOverdue()
    {
        TaskManager handler = (TaskManager) Application.getInstance().getModule(TaskManager.class);
        Calendar calendar = Calendar.getInstance();
        DaoQuery query = new DaoQuery();
        query.addProperty(new OperatorLessThan("dueDate", calendar.getTime(), DaoOperator.OPERATOR_AND));
        query.addProperty(new OperatorEquals("completed", "0", DaoOperator.OPERATOR_AND));
        try
        {
            Collection list = handler.getTasks(query, 0, -1, null, false);
            sendMessages(list, true);
        }
        catch (DaoException e)
        {
            Log.getLog(getClass()).error("Error while retrieving tasks almost overdue", e);
        }
    }

    protected void sendMessages(Collection tasks, boolean overdue)
    {
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        User owner = null;
        User user = null;
        for (Iterator i = tasks.iterator(); i.hasNext();)
        {
            String to = "";
            String body="";
            String subject="";
            SimpleDateFormat sdf = new SimpleDateFormat(FormatUtil.getInstance().getLongDateFormat());
            Task task = (Task) i.next();
            String archived=(String)task.getProperty("projectArchived");
            if(archived==null||"0".equals(archived)){
            try
            {
                owner = service.getUser(task.getAssignerId());
                if(owner.isActive())
                {
                    to = owner.getUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
                    for (Iterator it = task.getAttendees().iterator(); it.hasNext();)
                    {
                        Assignee assignee = (Assignee) it.next();
                        user = service.getUser(assignee.getUserId());
                        if(!user.getUsername().equals(owner.getUsername())&&assignee.getTaskStatus()!=Assignee.TASK_STATUS_COMPLETED)
                            to += "," + user.getUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
                    }
                    /* Sending messages */
                    //Message message = new Message();
                    //message.setMessageId(UuidGenerator.getInstance().getUuid());
                    //message.setMessageFormat(Message.MESSAGE_FORMAT_TEXT);
                    //message.setDate(new Date());
                    //message.setToIntranetList(Util.convertStringToIntranetRecipientsList(to));
                    //message.setFrom(owner.getUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN);
                    if(overdue)
                    {
                        //message.setSubject("Overdue Task: " + task.getTitle());
                        subject = Application.getInstance().getMessage("taskmanager.label.mailOverdueTask") + ": " + task.getTitle();
                        body = Application.getInstance().getMessage("taskmanager.label.mailOverdueMsg1") + " [" + task.getTitle() + "] " + Application.getInstance().getMessage("taskmanager.label.mailOverdueMsg2") + " " + task.getDueDate()+ " " + Application.getInstance().getMessage("taskmanager.label.mailOverdueMsgIsDue")+ "\n\n<br><br>";
                        //message.setBody("The task [" + task.getTitle() + "] scheduled for completion on " + task.getDueDate() + " is now overdue");
                    }
                    else
                    {
                        //message.setSubject("Task: " + task.getTitle() + " almost due");
                        subject = Application.getInstance().getMessage("taskmanager.label.mailAlmostDue")+" "+Application.getInstance().getMessage("calendar.label.task") + ": " + task.getTitle() ;
                        body =Application.getInstance().getMessage("taskmanager.label.mailOverdueMsg1") + " [" + task.getTitle() + "] " + Application.getInstance().getMessage("taskmanager.label.mailOverdueMsg2") + " " + task.getDueDate() + " " + Application.getInstance().getMessage("taskmanager.label.mailOverdueMsgAlmostDue") + "\n\n<br><br>";
                        //message.setBody("The task [" + task.getTitle() + "] scheduled for completion on " + task.getDueDate() + " is almost due");
                    }

                    body += "<U>" + Application.getInstance().getMessage("taskmanager.label.Task", "Task") + "</U>";
                    body += "<BR><BR>" + Application.getInstance().getMessage("taskmanager.label.title", "Title") + ": " + task.getTitle();
                    body += "<BR>" + Application.getInstance().getMessage("taskmanager.label.Category", "Cateogry") + ": " + task.getCategory();
                    body += "<BR>" + Application.getInstance().getMessage("taskmanager.label.dueDate", "Due Date") + ": " + sdf.format(task.getDueDate())+ "\n";
                    body += "<BR>" + Application.getInstance().getMessage("taskmanager.label.dueTime", "Due Time") + ": " + new SimpleDateFormat("h:mm a").format(task.getDueDate())+ "\n";
                    body += "<BR>" + Application.getInstance().getMessage("taskmanager.label.description", "Description") + ": " + task.getDescription()+ "\n";
                    body += "<BR>" + Application.getInstance().getMessage("taskmanager.label.OverallProgress", "Overall Progress") + ": " + new DecimalFormat("0.0").format(task.getOverallProgress()) +"%\n";
                    body += "<BR><BR><a href=\"/ekms/calendar/calendar.jsp?cn=calendarPage.calendarView&et=select&eventId="+task.getEventId()+"&instanceId="+task.getInstanceId()+"\">" + Application.getInstance().getMessage("taskmanager.label.clickToView", "Click here to view") + "</a>";

                    //message.setBody(body);
                    MessagingModule handler = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
                    //SmtpAccount account = new SmtpAccount();
                    //account.setServerName("");
                    //account.setServerPort(0);
                    //handler.sendMessage(account, message);
                    handler.sendStandardHtmlEmail(owner.getId(),to,"","",subject,"",body);
                }
            }
            catch (SecurityException e)
            {
                Log.getLog(getClass()).error("Error while retrieving user", e);
            }
            catch (AddressException e)
            {
                Log.getLog(getClass()).info("Invalid address while sending notification", e);
            }
            catch (MessagingException e)
            {
                Log.getLog(getClass()).info("Messaging error while sending notification", e);
            }
            }
        }
    }
}
