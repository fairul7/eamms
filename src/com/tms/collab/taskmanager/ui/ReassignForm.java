package com.tms.collab.taskmanager.ui;

import kacang.stdui.*;
import kacang.services.security.ui.UsersSelectBox;
import kacang.Application;
import kacang.util.Log;
import kacang.ui.Forward;
import kacang.ui.Event;

import java.util.Map;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

import org.apache.commons.collections.SequencedHashMap;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.model.Task;
import com.tms.collab.taskmanager.model.TaskMailer;
import com.tms.collab.calendar.model.Attendee;
import com.tms.collab.calendar.ui.CalendarUsersSelectBox;

public class ReassignForm extends Form
{
    private ButtonGroup reassignBG;
    private Radio reassignYes;
    private Radio reassignNo;
    private Button reassignButton,cancelButton;
    private UsersSelectBox assignees;
    private String taskId;
    public static final String FORWARD_CANCEL = "cancel";
    public static final String FORWARD_REASSIGNED = "reassigned";
    public ReassignForm()
    {
    }

    public ReassignForm(String name)
    {
        super(name);
    }

    public void init()
    {
        try
        {
            super.init();
            setMethod("POST");
            reassignNo = new Radio("reassignNo",Application.getInstance().getMessage("taskmanager.label.No","No"));
            reassignNo.setChecked(true);
            reassignYes = new Radio("reassignYes",Application.getInstance().getMessage("taskmanager.label.Yes","Yes"));
            reassignBG = new ButtonGroup("reassignBG",new Radio[]{reassignYes,reassignNo});
            reassignButton = new Button("reassignButton",Application.getInstance().getMessage("taskmanager.label.Reassign","Reassign"));
            assignees = new CalendarUsersSelectBox("assigneescsb");
            addChild(assignees);
            assignees.init();
           // assignees.getRightSelect().addChild(new ValidatorNotEmpty("not empty"));
            cancelButton  = new Button("cancelbutton",Application.getInstance().getMessage("taskmanager.label.Cancel","Cancel"));
            addChild(cancelButton);
            addChild(reassignButton);
            addChild(reassignYes);
            addChild(reassignNo);
    } catch (/*Security*/Exception e)
    {
        e.printStackTrace();
        Log.getLog(getClass()).error(e);
    }
    }

    public void onRequest(Event event)
    {
       /* try
        {
            TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
            Task task = tm.getTask(taskId);
            Collection attendees = task.getAttendees();
            Collection attendeeIds = new ArrayList();
            for (Iterator iterator = attendees.iterator(); iterator.hasNext();)
            {
                Attendee attendee = (Attendee) iterator.next();
                attendeeIds.add(attendee.getUserId());
            }
            assignees.setIds((String[])attendeeIds.toArray(new String[0]));
        }
        catch(Exception e)
        {
            Log.getLog(getClass()).error(e);
        }*/
        super.onRequest(event);
    }

    public Forward onValidate(Event evt)
    {
        try{
        String buttonClicked = findButtonClicked(evt);
        if(reassignButton.getAbsoluteName().equals(buttonClicked)&&taskId!=null&&taskId.trim().length()>0){
            String[] selectedUserIds = assignees.getIds();
            Map selectedUsers = new SequencedHashMap();
            for (int i=0; i<selectedUserIds.length; i++) {
                selectedUsers.put(selectedUserIds[i], selectedUserIds[i]);
            }
            TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
            tm.reassignTask(taskId,getWidgetManager().getUser().getId(),selectedUsers);
            tm.setReassign(taskId,(reassignYes.isChecked()?true:false));
            TaskMailer.sendTaskReassignNotification(tm.getTask(taskId),evt,getWidgetManager().getUser().getId());
            init();
            return new Forward(FORWARD_REASSIGNED);
        } else if(cancelButton.getAbsoluteName().equals(buttonClicked))
        {
            return new Forward(FORWARD_CANCEL);
        }
        }catch(Exception e){
            Log.getLog(getClass()).error(e);
        }
        return null;
    }

    public String getDefaultTemplate()
    {
        return "taskmanager/reassignform";
    }

    public ButtonGroup getReassignBG()
    {
        return reassignBG;
    }

    public void setReassignBG(ButtonGroup reassignBG)
    {
        this.reassignBG = reassignBG;
    }

    public Radio getReassignYes()
    {
        return reassignYes;
    }

    public void setReassignYes(Radio reassignYes)
    {
        this.reassignYes = reassignYes;
    }

    public Radio getReassignNo()
    {
        return reassignNo;
    }

    public void setReassignNo(Radio reassignNo)
    {
        this.reassignNo = reassignNo;
    }

    public Button getReassignButton()
    {
        return reassignButton;
    }

    public void setReassignButton(Button reassignButton)
    {
        this.reassignButton = reassignButton;
    }

    public UsersSelectBox getAssignees()
    {
        return assignees;
    }

    public void setAssignees(UsersSelectBox assignees)
    {
        this.assignees = assignees;
    }

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    public Button getCancelButton()
    {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton)
    {
        this.cancelButton = cancelButton;
    }
}
