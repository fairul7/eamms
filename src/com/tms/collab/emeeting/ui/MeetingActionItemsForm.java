package com.tms.collab.emeeting.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorIn;
import kacang.model.operator.DaoOperator;
import kacang.services.security.SecurityService;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import com.tms.collab.emeeting.Meeting;
import com.tms.collab.emeeting.MeetingHandler;
import com.tms.collab.emeeting.AgendaItem;
import com.tms.collab.emeeting.MeetingException;
import com.tms.collab.taskmanager.model.TaskManager;
import com.tms.collab.taskmanager.model.Task;

import java.util.Collection;
import java.util.Iterator;

public class MeetingActionItemsForm extends Form
{
    public static final String ADD_SUCCESSFUL = "add_success";
    public static final String ADD_FAILURE = "add_fail";
    public static final String EDIT_SUCCESSFUL = "edit_success";
    public static final String EDIT_FAILURE = "edit_fail";
    public static final String DELETE_SUCCESSFUL = "delete_success";
    public static final String DELETE_FAILURE = "delete_fail";
    public static final String INVALID_STATE = "invalid_state";
    public static final String ITEM_SELECTION = "item_select";
    public static final String FORWARD_ASSIGN_TASK = "assign_task";
    public static final String FORWARD_EDIT_TASK = "edit_task";
    public static final String FORWARD_TASK_DELETE = "task_delete";

    public static final String DEFAULT_TEMPLATE = "emeeting/meetingActionItems";

    private TextField title;
    private SelectBox itemParent;
    private TextBox notes;
    private TextBox itemAction;
    private ValidatorNotEmpty validTitle;

    private Button buttonAdd;
    private Button buttonEdit;
    private Button buttonDelete;
    private Button buttonCancel;
    private Button buttonAssignTask;
    private Button buttonEditTask;
    private Button buttonDeleteTask;

    private Meeting meeting;
    private AgendaItem item;
    private String eventId;
    private String itemId;
    private Collection assignees;

    public void init()
    {
        setMethod("POST");
        validTitle = new ValidatorNotEmpty("validTitle");
        validTitle.setMessage(Application.getInstance().getMessage("emeeting.error.pleaseEnterTheTitle", "Please Enter The Title"));
        title = new TextField("newTitle");
        title.addChild(validTitle);
        itemParent = new SelectBox("newParent");
        setParentValues("");
        notes = new RichTextBox("newNotes");
        itemAction = new TextBox("newAction");

        buttonAdd = new Button("buttonAdd");
        buttonAdd.setText(Application.getInstance().getMessage("emeeting.label.add", "Add"));
        buttonEdit = new Button("buttonEdit");
        buttonEdit.setText(Application.getInstance().getMessage("emeeting.label.update", "Update"));
        buttonDelete = new Button("Delete");
        buttonDelete.setText(Application.getInstance().getMessage("emeeting.label.delete", "Delete"));
        buttonCancel = new Button(Form.CANCEL_FORM_ACTION);
        buttonCancel.setText(Application.getInstance().getMessage("emeeting.label.cancel", "Cancel"));
        buttonAssignTask = new Button("buttonAssignTask");
        buttonAssignTask.setText(Application.getInstance().getMessage("emeeting.label.assignTask", "Assign Task"));
/*
        buttonAssignTask.setHidden(true);
*/
        buttonEditTask = new Button("buttonEditTask");
        buttonEditTask.setText(Application.getInstance().getMessage("emeeting.label.editTask", "Edit Task"));
        buttonEditTask.setHidden(true);
        buttonDeleteTask = new Button("buttonDeleteTask");
        buttonDeleteTask.setText(Application.getInstance().getMessage("emeeting.label.deleteTask", "Delete Task"));
        buttonDeleteTask.setHidden(true);

        addChild(title);
        addChild(itemParent);
        addChild(notes);
        addChild(itemAction);
        addChild(buttonAdd);
        addChild(buttonEdit);
        addChild(buttonDelete);
        addChild(buttonCancel);
        addChild(buttonAssignTask);
        addChild(buttonEditTask);
        addChild(buttonDeleteTask);

        assignees = null;
    }

    public void onRequest(Event evt)
    {
        resetFields();
    }

    public Forward actionPerformed(Event evt)
    {
        Forward forward = null;
        if(eventId == null || "".equals(eventId))
            forward = new Forward(INVALID_STATE);
        else
        {
            if("moveup".equals(evt.getType())){
                String itemId = evt.getParameter("itemId");
                if(itemId!=null&&itemId.trim().length()>0){
                    MeetingHandler mh = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
                    try {
                        mh.moveUp(itemId);
                        return new Forward("refresh");
                    } catch (MeetingException e) {
                        Log.getLog(getClass()).error(e.getMessage(), e);  //To change body of catch statement use Options | File Templates.
                    }
                }
            }else if("movedown".equals(evt.getType())){
                String itemId = evt.getParameter("itemId");
                if(itemId!=null&&itemId.trim().length()>0){
                    MeetingHandler mh = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
                    try {
                        mh.moveDown(itemId);
                        return new Forward("refresh");
                    } catch (MeetingException e) {
                        Log.getLog(getClass()).error(e.getMessage(), e);  //To change body of catch statement use Options | File Templates.
                    }
                }

            }else if(evt.getType() != null && evt.getType().equals(ITEM_SELECTION))
            {
                String itemId = evt.getParameter("itemId");
                if(!(itemId == null || "".equals(itemId)))
                    setItemId(itemId);
                forward = new Forward();
            }
            else if(buttonCancel.getAbsoluteName().equals(findButtonClicked(evt)))
            {
                resetFields();
                forward = super.actionPerformed(evt);
                setEventId(eventId);
            }
            else if(buttonDelete.getAbsoluteName().equals(findButtonClicked(evt)))
                forward = deleteItem();
            else
                forward = super.actionPerformed(evt);

            if(buttonAssignTask.getAbsoluteName().equals(findButtonClicked(evt)))
                forward = new Forward(FORWARD_ASSIGN_TASK);
            else if(buttonEditTask.getAbsoluteName().equals(findButtonClicked(evt)))
                forward = new Forward(FORWARD_EDIT_TASK);
            else if(buttonDeleteTask.getAbsoluteName().equals(findButtonClicked(evt)))
                forward = deleteTask();

        }
        return forward;
    }

    public Forward onValidate(Event evt)
    {
        String buttonClicked = findButtonClicked(evt);
        Forward forward = new Forward();
        if(buttonAdd.getAbsoluteName().equals(buttonClicked))
        {
            AgendaItem item = new AgendaItem();
            item.setItemId(UuidGenerator.getInstance().getUuid());
            item.setEventId(eventId);
            item.setTitle((String) title.getValue());
            item.setParentId((String) itemParent.getSelectedOptions().keySet().iterator().next());
            item.setNotes((String) notes.getValue());
            item.setAction((String) itemAction.getValue());
            try
            {
                MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
                Collection items = handler.getAgendaItems(eventId, item.getParentId());
                handler.addAgendaItem(item, item.getParentId(), new Integer(items.size() + 1));
                meeting = handler.getMeeting(eventId, true);
                setParentValues("");
                resetFields();

                return new Forward(ADD_SUCCESSFUL);
            }
            catch(Exception e)
            {
                Log.getLog(getClass()).error(e.toString(), e);
                return new Forward(ADD_FAILURE);
            }
        }
        else if(buttonEdit.getAbsoluteName().equals(buttonClicked))
        {
            saveAgendaItem();
            resetFields();
        }
        else if(buttonAssignTask.getAbsoluteName().equals(findButtonClicked(evt))
                || buttonEditTask.getAbsoluteName().equals(findButtonClicked(evt))
                || buttonDeleteTask.getAbsoluteName().equals(findButtonClicked(evt)))
        {
            saveAgendaItem();
        }
        return forward;
    }

    protected Forward saveAgendaItem() {
        if(!(itemId == null || "".equals(itemId)))
        {
            AgendaItem newItem = new AgendaItem();
            newItem.setItemId(itemId);
            newItem.setEventId(eventId);
            newItem.setTitle((String) title.getValue());
            if(item.getChildren() == null || item.getChildren().size() <= 0)
                newItem.setParentId((String) itemParent.getSelectedOptions().keySet().iterator().next());
            else
                newItem.setParentId(item.getParentId());
            newItem.setNotes((String) notes.getValue());
            newItem.setAction((String) itemAction.getValue());
            try
            {
                MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
                if(newItem.getParentId().equals(item.getParentId()))
                    newItem.setItemOrder(item.getItemOrder());
                else
                {
                    Collection items = handler.getAgendaItems(eventId, newItem.getParentId());
                    newItem.setItemOrder(items.size() + 1);
                }
                handler.editAgendaItem(newItem, newItem.getParentId(), new Integer(newItem.getItemOrder()));
                meeting = handler.getMeeting(eventId, true);
            }
            catch(Exception e)
            {
                Log.getLog(getClass()).error(e.toString(), e);
                return new Forward(EDIT_FAILURE);
            }
        }
        return new Forward();
    }

    protected void resetFields()
    {
        itemId = null;
        item = null;
        title.setValue("");
        itemParent.setSelectedOptions(new String[] {AgendaItem.NO_PARENT});
        notes.setValue("");
        itemAction.setValue("");
    }

    private void setParentValues(String exclude)
    {
        if(meeting != null)
        {
            itemParent.removeAllOptions();
            itemParent.addOption(AgendaItem.NO_PARENT, Application.getInstance().getMessage("emeeting.label.noParent", "No Parent"));
            for(Iterator i = meeting.getMeetingAgenda().iterator(); i.hasNext();)
            {
                AgendaItem item = (AgendaItem) i.next();
                if(!item.getItemId().equals(exclude))
                    itemParent.addOption(item.getItemId(), item.getTitle());
            }
        }
    }

    public String getDefaultTemplate()
    {
        return DEFAULT_TEMPLATE;
    }

    private Forward deleteItem()
    {
        MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
        try
        {
            handler.deleteAgendaItem(itemId);

            resetFields();
            meeting = handler.getMeeting(eventId, true);
        }
        catch(Exception e)
        {
            Log.getLog(getClass()).error(e.toString(), e);
            return new Forward(DELETE_FAILURE);
        }
        return new Forward(DELETE_SUCCESSFUL);
    }

    private Forward deleteTask()
    {
        Forward forward = null;
        if(item != null || item.getTaskId() != null || "".equals(item.getTaskId()))
        {
            MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
            TaskManager manager = (TaskManager) Application.getInstance().getModule(TaskManager.class);
            try
            {
                manager.deleteTask(item.getTaskId());
                handler.assignTask(item.getItemId(), "");
                item.setTask(null);
                item.setTaskId("");
                buttonAssignTask.setHidden(false);
                buttonEditTask.setHidden(true);
                buttonDeleteTask.setHidden(true);
                forward = new Forward(FORWARD_TASK_DELETE);
            }
            catch(Exception e)
            {
                Log.getLog(MeetingActionItemsForm.class).error(e);
            }
        }
        return forward;
    }

    //Getters and Setter Methods
    public String getEventId()
    {
        return eventId;
    }

    public void setEventId(String eventId)
    {
        if(!(eventId == null || "".equals(eventId)))
        {
            this.eventId = eventId;
            MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
            try
            {
                meeting = handler.getMeeting(eventId, true);
                if(itemId == null || "".equals(itemId))
                    setParentValues("");
            }
            catch(Exception e)
            {
                Log.getLog(getClass()).error(e.toString(), e);
            }
        }
    }

    public String getItemId()
    {
        return itemId;
    }

    public void setItemId(String itemId)
    {
        if(!(itemId == null || "".equals(itemId)))
        {
            this.itemId = itemId;
            MeetingHandler handler = (MeetingHandler) Application.getInstance().getModule(MeetingHandler.class);
            try
            {
                item = handler.getAgendaItem(itemId);
                title.setValue(item.getTitle());
                setParentValues(item.getItemId());
                itemParent.setSelectedOptions(new String[] {item.getParentId()});
                notes.setValue(item.getNotes());
                itemAction.setValue(item.getAction());
                if(item.getTask() == null)
                {
                    buttonAssignTask.setHidden(false);
                    buttonEditTask.setHidden(true);
                    buttonDeleteTask.setHidden(true);
                    assignees = null;
                }
                else
                {

/*
                    buttonAssignTask.setHidden(true);
*/
                    buttonEditTask.setHidden(false);
                    buttonDeleteTask.setHidden(false);
                    if(item.getTask().getAttendeeMap().size() > 0)
                    {
                        //Populating assignees
                        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                        DaoQuery query = new DaoQuery();
                        query.addProperty(new OperatorIn("id", item.getTask().getAttendeeMap().keySet().toArray(), DaoOperator.OPERATOR_AND));
                        assignees = service.getUsers(query, 0, -1, "username", false);
                    }
                }
            }
            catch(Exception e)
            {
                Log.getLog(getClass()).error(e.toString(), e);
            }
        }
    }

    public Meeting getMeeting()
    {
        return meeting;
    }

    public void setMeeting(Meeting meeting)
    {
        this.meeting = meeting;
    }

    public TextField getTitle()
    {
        return title;
    }

    public void setTitle(TextField title)
    {
        this.title = title;
    }

    public SelectBox getItemParent()
    {
        return itemParent;
    }

    public void setItemParent(SelectBox itemParent)
    {
        this.itemParent = itemParent;
    }

    public TextBox getNotes()
    {
        return notes;
    }

    public void setNotes(TextBox notes)
    {
        this.notes = notes;
    }

    public TextBox getItemAction()
    {
        return itemAction;
    }

    public void setItemAction(TextBox itemAction)
    {
        this.itemAction = itemAction;
    }

    public Button getButtonAdd()
    {
        return buttonAdd;
    }

    public void setButtonAdd(Button buttonAdd)
    {
        this.buttonAdd = buttonAdd;
    }

    public Button getButtonEdit()
    {
        return buttonEdit;
    }

    public void setButtonEdit(Button buttonEdit)
    {
        this.buttonEdit = buttonEdit;
    }

    public Button getButtonDelete()
    {
        return buttonDelete;
    }

    public void setButtonDelete(Button buttonDelete)
    {
        this.buttonDelete = buttonDelete;
    }

    public Button getButtonCancel()
    {
        return buttonCancel;
    }

    public void setButtonCancel(Button buttonCancel)
    {
        this.buttonCancel = buttonCancel;
    }

    public ValidatorNotEmpty getValidTitle()
    {
        return validTitle;
    }

    public void setValidTitle(ValidatorNotEmpty validTitle)
    {
        this.validTitle = validTitle;
    }

    public AgendaItem getItem()
    {
        return item;
    }

    public void setItem(AgendaItem item)
    {
        this.item = item;
    }

    public Button getButtonAssignTask()
    {
        return buttonAssignTask;
    }

    public void setButtonAssignTask(Button buttonAssignTask)
    {
        this.buttonAssignTask = buttonAssignTask;
    }

    public Button getButtonEditTask()
    {
        return buttonEditTask;
    }

    public void setButtonEditTask(Button buttonEditTask)
    {
        this.buttonEditTask = buttonEditTask;
    }

    public Button getButtonDeleteTask()
    {
        return buttonDeleteTask;
    }

    public void setButtonDeleteTask(Button buttonDeleteTask)
    {
        this.buttonDeleteTask = buttonDeleteTask;
    }

    public Collection getAssignees()
    {
        return assignees;
    }

    public void setAssignees(Collection assignees)
    {
        this.assignees = assignees;
    }
}
