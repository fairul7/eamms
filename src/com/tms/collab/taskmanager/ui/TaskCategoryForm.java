package com.tms.collab.taskmanager.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.model.DaoException;
import kacang.util.UuidGenerator;
import kacang.util.Log;
import kacang.services.security.SecurityService;
import kacang.services.security.SecurityException;
import com.tms.collab.taskmanager.model.TaskCategory;
import com.tms.collab.taskmanager.model.TaskManager;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Oct 14, 2003
 * Time: 2:55:00 PM
 * To change this template use Options | File Templates.
 */
public class TaskCategoryForm extends Form
{
    private String categoryId;
   // private TaskCategory category = null;
    private TextField nameTF;
    private TextBox description;
    private Button submitButton,cancelButton;
    private CheckBox general;
    private ValidatorNotEmpty vad;
    private TaskCategory category = null;

    public TaskCategoryForm()
    {
    }

    public TaskCategoryForm(String name)
    {
        super(name);
    }

    public void init()
    {
        super.init();
        setMethod("POST");
        vad = new ValidatorNotEmpty("notemptyValidator");
        nameTF = new TextField("nameTF");
        nameTF.addChild(vad);
        description = new TextBox("catdescription");
        description.addChild(vad);
        description.setRows("5");
        submitButton = new Button("submitbutton");
        cancelButton = new Button("cancelbutton",Application.getInstance().getMessage("taskmanager.label.Cancel","Cancel"));
        SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
        try{
            if(ss.hasPermission(getWidgetManager().getUser().getId(),TaskManager.PERMISSION_MANAGETASK,
                null,null)){
                    general = new CheckBox("general",Application.getInstance().getMessage("taskmanager.label.PublicTobeusedbyall","Public (To be used by all)"));
                    addChild(general);
                }
        }catch(Exception e){
            Log.getLog(getClass()).error(e);
        }
        addChild(submitButton);
        addChild(cancelButton);
        addChild(nameTF);
        addChild(description);
    }

    public void onRequest(Event evt)
    {
        if(categoryId!=null&&categoryId.trim().length()>0){
            TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);

            try
            {
                category = tm.getCategory(categoryId);
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }
            /*submitButton.setText(Application.getInstance().getMessage("taskmanager.label.Save","Save"));
            nameTF.setValue(category.getName());
            description.setValue(category.getDescription());
            if(general!=null)
                general.setChecked(category.isGeneral());*/
            populate(category);
        }else{
            categoryId = null;
            category = null;
            init();
            submitButton.setText(Application.getInstance().getMessage("taskmanager.label.Add","Add"));
        }
    }

    private void populate(TaskCategory category){
        submitButton.setText(Application.getInstance().getMessage("taskmanager.label.Save","Save"));
        nameTF.setValue(category.getName());
        description.setValue(category.getDescription());
        if(general!=null)
            general.setChecked(category.isGeneral());
    }

    public Forward onValidate(Event evt)
    {
        TaskManager tm = (TaskManager)Application.getInstance().getModule(TaskManager.class);
        String buttonClicked = findButtonClicked(evt);
        if(submitButton.getAbsoluteName().equals(buttonClicked)){
            TaskCategory tc = assembleCategory();
            if(submitButton.getText().equals(Application.getInstance().getMessage("taskmanager.label.Save","Save"))){
                if(categoryId!=null&&categoryId.trim().length()>0){
                    tc.setId(categoryId);
                    try
                    {
                        tm.updateCategory(tc);
                    } catch (DaoException e)
                    {
                        Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                    }
                    //onRequest(evt);
                    populate(tc);
                    return new Forward("edit successful",null,false);
                }
            }else{
                tc.setId(UuidGenerator.getInstance().getUuid());
                try
                {
                    tm.addCategory(tc);
                } catch (DaoException e)
                {
                    Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
                }
                init();
                submitButton.setText(Application.getInstance().getMessage("taskmanager.label.Add","Add"));
                return new Forward("Added");
            }
        }
        return null;
    }

    private TaskCategory assembleCategory(){
        TaskCategory category = new TaskCategory();
        category.setName(nameTF.getValue().toString());
        category.setDescription(description.getValue().toString());
        category.setUserId(getWidgetManager().getUser().getId());
        if(general!=null)
            category.setGeneral(general.isChecked());
        else
            category.setGeneral(false);
        return category;
    }

    public String getDefaultTemplate()
    {
        return "taskmanager/categoryform";
    }

    public TextField getNameTF()
    {
        return nameTF;
    }

    public void setNameTF(TextField nameTF)
    {
        this.nameTF = nameTF;
    }

    public TextBox getDescription()
    {
        return description;
    }

    public void setDescription(TextBox description)
    {
        this.description = description;
    }

    public String getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(String categoryId)
    {
        this.categoryId = categoryId;
    }



    public Button getSubmitButton()
    {
        return submitButton;
    }

    public void setSubmitButton(Button submitButton)
    {
        this.submitButton = submitButton;
    }

    public Button getCancelButton()
    {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton)
    {
        this.cancelButton = cancelButton;
    }

    public CheckBox getGeneral()
    {
        return general;
    }

    public void setGeneral(CheckBox general)
    {
        this.general = general;
    }

    public Forward onSubmit(Event event)
    {
        String buttonClicked = findButtonClicked(event);
        if(cancelButton.getAbsoluteName().equals(buttonClicked))
        {
            return new Forward("cancel");
        }
        else
        {
            return super.onSubmit(event);
        }
    }

    public TaskCategory getCategory()
    {
        return category;
    }

    public void setCategory(TaskCategory category)
    {
        this.category = category;
    }

    public boolean isHasPermission() throws SecurityException
    {
        SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
        return ss.hasPermission(getWidgetManager().getUser().getId(),TaskManager.PERMISSION_MANAGETASK,null,null);
    }
}
