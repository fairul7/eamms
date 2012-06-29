package com.tms.collab.taskmanager.ui;

import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.stdui.Button;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.model.DaoException;
import com.tms.collab.taskmanager.model.Assignee;
import com.tms.collab.taskmanager.model.TaskManager;

import java.util.Map;
import java.util.Date;

import org.apache.commons.collections.SequencedHashMap;

public class ProgressForm extends Form
{
    public static final String FORWARD_SET_SUCCESSFULLY = "set successfully";
    public static final String FORWARD_CANCEL ="cancel";
    private String taskId;
    private String userId;
    private Assignee assignee;
    private SelectBox progressSelectBox;
    private Button setButton,cancelButton;

    public ProgressForm()
    {
    }

    public ProgressForm(String s)
    {
        super(s);
    }

    public Forward onSubmit(Event event)
    {
        if(cancelButton.getAbsoluteName().equals(findButtonClicked(event))){
            return new Forward(FORWARD_CANCEL);
        }
        return super.onSubmit(event);
    }

    public void onRequest(Event event)
    {
        if(taskId!=null&&userId!=null&&taskId.trim().length()>0&&userId.trim().length()>0&&userId.equals(getWidgetManager().getUser().getId())){
            TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
            try
            {
                assignee = tm.getAssignee(taskId,userId);
                if(assignee.getTaskStatus()==Assignee.TASK_STATUS_IN_PROGRESS){
                    if(progressSelectBox!=null)
                        progressSelectBox.setSelectedOptions(new String[]{""+assignee.getProgress()});
                }

            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }

        super.onRequest(event);
    }

    public void init()
    {
        progressSelectBox = new SelectBox("progressSelectBox");
        Map map = new SequencedHashMap();
        for (int i = 0; i < 100; i+=5)
        {
            Integer percentage = new Integer(i);
              map.put(""+i,percentage);
        }
        progressSelectBox.setOptionMap(map);
        setButton = new Button("set");
        setButton.setText(Application.getInstance().getMessage("taskmanager.label.Set","Set"));
        cancelButton = new Button("cancelButton");
        cancelButton.setText(Application.getInstance().getMessage("taskmanager.label.Cancel","Cancel"));
        addChild(cancelButton);
        addChild(setButton);
        addChild(progressSelectBox);
        super.init();
    }

    public Forward onValidate(Event event)
    {
        if(setButton.getAbsoluteName().equals(findButtonClicked(event))){
            if(assignee!=null&&assignee.getUserId().equals(getWidgetManager().getUser().getId())&&assignee.getTaskStatus()==Assignee.TASK_STATUS_IN_PROGRESS){
                progressSelectBox.getSelectedOptions();
                Map map = progressSelectBox.getSelectedOptions();
               // Integer i = (Integer)map.keySet().iterator().next();
                assignee.setProgress(Integer.parseInt((String)map.keySet().iterator().next()));
                TaskManager tm = (TaskManager) Application.getInstance().getModule(TaskManager.class);
                try
                {
                    tm.updateAssignee(assignee);
					User user = getWidgetManager().getUser();
					Log.getLog(getClass()).write(new Date(), taskId, user.getId(), "kacang.services.log.task.UpdateProgress", "User "+ user.getName() + " has updated his progress for task " + taskId + " to " + assignee.getProgress() + "% completed", event.getRequest().getRemoteAddr(), event.getRequest().getSession().getId());
                    return new Forward(FORWARD_SET_SUCCESSFULLY);
                } catch (DaoException e)
                {
                    Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                }
            }
        }
        return super.onValidate(event);
    }

    public String getDefaultTemplate()
    {
        return "taskmanager/progressform";
    }

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    public String getUserId()
    {
        return userId;
    }

    public Button getCancelButton()
    {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton)
    {
        this.cancelButton = cancelButton;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public Assignee getAssignee()
    {
        return assignee;
    }

    public void setAssignee(Assignee assignee)
    {
        this.assignee = assignee;
    }

    public SelectBox getProgressSelectBox()
    {
        return progressSelectBox;
    }

    public void setProgressSelectBox(SelectBox progressSelectBox)
    {
        this.progressSelectBox = progressSelectBox;
    }

    public Button getSetButton()
    {
        return setButton;
    }

    public void setSetButton(Button setButton)
    {
        this.setButton = setButton;
    }
}
