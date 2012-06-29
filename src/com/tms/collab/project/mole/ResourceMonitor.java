package com.tms.collab.project.mole;

import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskManager;
import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.security.ui.UsersSelectBox;
import kacang.stdui.Button;
import kacang.stdui.DateField;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.*;

public class ResourceMonitor extends Form
{
    public static final String FORWARD_REPORT_GENERATED = "com.tms.worms.mole.ReportGenerated";
    public static final String FORWARD_CANCEL = "com.tms.worms.mole.Cancel";
    public static final String DEFAULT_TEMPLATE = "project/mole/resourceMonitor";

    protected UsersSelectBox users;
    protected DateField startDate;
    protected DateField endDate;

    protected Button submit;
    protected Button reset;
    protected Button cancel;

    protected Map report;
    protected Map freeTime;

    public void init()
    {
        super.init();
        setMethod("POST");
        users = new UsersSelectBox("users");
        startDate = new DateField("startDate");
        endDate = new DateField("endDate");
        submit = new Button("submit", Application.getInstance().getMessage("project.label.generateReport","Generate Report"));
        reset = new Button("reset", Application.getInstance().getMessage("general.label.reset","Reset"));
        cancel = new Button("cancel",Application.getInstance().getMessage("general.label.cancel","Cancel"));

        addChild(users);
        addChild(startDate);
        addChild(endDate);
        addChild(submit);
        addChild(reset);
        addChild(cancel);

        users.init();
        report = new SequencedHashMap();
    }

    public Forward onSubmit(Event event)
    {
        Forward forward = null;
        String button = findButtonClicked(event);
        if(cancel.getAbsoluteName().equals(button))
            forward = new Forward(FORWARD_CANCEL);
        else if(reset.getAbsoluteName().equals(button))
            init();
        else
        {
            forward = super.onSubmit(event);
            if(users.getIds().length <= 0)
            {
                setInvalid(true);
                users.setInvalid(true);
            }
            if(startDate.getDate().after(endDate.getDate()))
            {
                setInvalid(true);
                startDate.setInvalid(true);
                endDate.setInvalid(true);
            }
        }
        return forward;
    }

    public Forward onValidate(Event event)
    {
        return generateReport();
    }

    public void onRequest(Event event)
    {
        if (!isInvalid()) {
            generateReport();
        }
    }

    protected Forward generateReport()
    {
        report = new SequencedHashMap();
        freeTime = new SequencedHashMap();
        if(users.getIds().length > 0)
        {
            String[] ids = users.getIds();
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
			TaskManager handler = (TaskManager) Application.getInstance().getModule(TaskManager.class);
            for(int i = 0; i < ids.length; i++)
            {
                try
                {
                    String userId = ids[i];
                    User user = service.getUser(userId);
					Collection involved = handler.getCalendarTasks(startDate.getDate(), endDate.getDate(), new String[] {userId}, false);
					//TODO: Fix TaskManager so it returns only tasks within the date range
                    Collection tasks = new ArrayList();
                    for (Iterator it = involved.iterator(); it.hasNext();)
                    {
						Task task = (Task) it.next();
					  if(task.getStartDate()!=null && task.getEndDate()!=null){
						if((task.getEndDate().after(startDate.getDate()) || task.getEndDate().equals(startDate.getDate())) &&
							(task.getStartDate().before(endDate.getDate()) || task.getStartDate().equals(endDate.getDate()))){

                            //do not add to task list if priority is 7-Ongoing (task created in project doesn't have priority)
                            if(task.getTaskPriority() == null || !task.getTaskPriority().equals("7")){
                                tasks.add(task);
                            }
                        }
					  }
                        
                    }
                    /* Generating free time matrix */
                    if(tasks.size() > 0)
                    {
                        Map matrix = new SequencedHashMap();
                        for (Iterator it = tasks.iterator(); it.hasNext();)
                        {
							Task task = (Task) it.next();
							if(!(task.getStartDate() == null || task.getEndDate() == null))
							{
								Date currentStart = task.getStartDate();
								if(currentStart.before(startDate.getDate()))
									currentStart = startDate.getDate();
								Date currentEnd = task.getEndDate();
								if(currentEnd.after(endDate.getDate()))
									currentEnd = endDate.getDate();
								Collection removeList = new ArrayList();
								for (Iterator im = matrix.keySet().iterator(); im.hasNext();)
								{
									Date taskStart = (Date) im.next();
									Date taskEnd = (Date) matrix.get(taskStart);
									/* Attempting to assimilate matrix blocks */
									if(withinRange(currentStart, taskStart, taskEnd))
										if(!currentStart.before(taskStart))
											currentStart = taskStart;
									if(withinRange(currentEnd, taskStart, taskEnd))
										if(!currentEnd.after(taskEnd))
											currentEnd = taskEnd;
									if(remove(currentStart, currentEnd, taskStart, taskEnd))
										removeList.add(taskStart);
								}
								for (Iterator rem = removeList.iterator(); rem.hasNext();)
								{
									Date date = (Date) rem.next();
									matrix.remove(date);
								}
								matrix.put(currentStart, currentEnd);
								removeList = new ArrayList();
							}
                        }
                        freeTime.put(user, matrix);
                    }
                    report.put(user, tasks);
                }
                catch (SecurityException e)
                {
                    Log.getLog(getClass()).error(e.getMessage(), e);
                }
				catch (DaoException e)
				{
					Log.getLog(getClass()).error(e.getMessage(), e);
				}
            }
        }
        return new Forward(FORWARD_REPORT_GENERATED);
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    protected boolean withinRange(Date date, Date rangeStart, Date rangeEnd)
    {
        if((date.after(rangeStart) || date.equals(rangeStart)) && (date.before(rangeEnd) || date.equals(rangeEnd)))
            return true;
        return false;
    }

	protected boolean remove(Date startDate, Date endDate, Date compareStart, Date compareEnd)
	{
        if((startDate.before(compareStart) || startDate.equals(compareStart)))
			return true;
		return false;
	}

    /* Getters and Setters */
    public Button getCancel()
    {
        return cancel;
    }

    public void setCancel(Button cancel)
    {
        this.cancel = cancel;
    }

    public DateField getEndDate()
    {
        return endDate;
    }

    public void setEndDate(DateField endDate)
    {
        this.endDate = endDate;
    }

    public Button getReset()
    {
        return reset;
    }

    public void setReset(Button reset)
    {
        this.reset = reset;
    }

    public DateField getStartDate()
    {
        return startDate;
    }

    public void setStartDate(DateField startDate)
    {
        this.startDate = startDate;
    }

    public Button getSubmit()
    {
        return submit;
    }

    public void setSubmit(Button submit)
    {
        this.submit = submit;
    }

    public UsersSelectBox getUsers()
    {
        return users;
    }

    public void setUsers(UsersSelectBox users)
    {
        this.users = users;
    }

    public Map getReport()
    {
        return report;
    }

    public void setReport(Map report)
    {
        this.report = report;
    }

    public Map getFreeTime()
    {
        return freeTime;
    }

    public void setFreeTime(Map freeTime)
    {
        this.freeTime = freeTime;
    }
}
