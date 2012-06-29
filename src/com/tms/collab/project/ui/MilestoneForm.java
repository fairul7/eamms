package com.tms.collab.project.ui;

import com.tms.collab.project.Milestone;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.util.Collection;

public abstract class MilestoneForm extends Form
{
    public static final String FORWARD_SUCCESS = "forward.worms.milestone.Success";
    public static final String FORWARD_FAILED = "forward.worms.milestone.Failed";
    public static final String FORWARD_CANCEL = "forward.worms.milestone.Cancel";

    protected String projectId;
    protected String milestoneId;
    protected boolean currentlyTemplate = false;

    protected TextField milestoneName;
    protected TextBox milestoneDescription;
    protected TextField milestoneProgress;
    protected Button submit;
    protected Button cancel;

    protected ValidatorNotEmpty validName;
    protected ValidatorIsNumeric validProgress;

    public MilestoneForm()
    {
    }

    public MilestoneForm(String s)
    {
        super(s);
    }

    protected boolean isKeyEmpty()
    {
        return true;
    }

    public void init()
    {
        if(!isKeyEmpty())
        {
            super.init();
            setColumns(2);
            setMethod("POST");

            milestoneName = new TextField("milestoneName");
            milestoneName.setSize("35");
            milestoneDescription = new TextBox("milestoneDescription");
            milestoneDescription.setCols("45");
            milestoneDescription.setRows("10");
            milestoneProgress = new TextField("milestoneProgress");
            milestoneProgress.setSize("5");
            milestoneProgress.setMaxlength("3");
            submit = new Button("submit", Application.getInstance().getResourceBundle().getString("general.label.submit"));
            cancel = new Button("cancel", Application.getInstance().getResourceBundle().getString("general.label.cancel"));
            Panel buttonPanel = new Panel("buttonPanel");
            buttonPanel.addChild(submit);
            buttonPanel.addChild(cancel);

            validName = new ValidatorNotEmpty("validName");
            milestoneName.addChild(validName);
            validProgress = new ValidatorIsNumeric("validProgress");
            milestoneProgress.addChild(validProgress);

            addChild(new Label("labelName", Application.getInstance().getResourceBundle().getString("project.label.name")+" *"));
            addChild(milestoneName);
            addChild(new Label("labelDescription", Application.getInstance().getResourceBundle().getString("project.label.description")));
            addChild(milestoneDescription);
            addChild(new Label("labelContribution", Application.getInstance().getResourceBundle().getString("project.label.contribution")+" *"));
            addChild(milestoneProgress);
            addChild(new Label("labelButton", ""));
            addChild(buttonPanel);
        }
    }

    public void onRequest(Event event)
    {
        if(!isKeyEmpty())
        {
            super.onRequest(event);
            refresh();
        }
    }

    protected void refresh()
    {
        if(!isKeyEmpty())
        {
            validName.setInvalid(false);
            validName.setText(Application.getInstance().getResourceBundle().getString("project.message.enterMilestoneName"));
            validProgress.setInvalid(false);
            validProgress.setText(Application.getInstance().getResourceBundle().getString("project.message.valueNumeric"));
        }
    }

    public Forward actionPerformed(Event event)
    {
        Forward forward = null;
        if(!isKeyEmpty())
        {
            if(cancel.getAbsoluteName().equals(findButtonClicked(event)))
                forward = new Forward(FORWARD_CANCEL);
            else
                forward =  super.actionPerformed(event);
        }
        return forward;
    }

    public Forward onSubmit(Event event)
    {
        Forward forward = null;
        if(!isKeyEmpty())
        {
            forward = super.onSubmit(event);
            if(!validName.isInvalid())
            {
                if(!isUniqueMilestone())
                {
                    setInvalid(true);
                    milestoneName.setInvalid(true);
                    validName.setInvalid(true);
                    validName.setText(Application.getInstance().getResourceBundle().getString("project.message.milestoneNameInUse"));
                    forward= new Forward(FORWARD_FAILED);
                }
            }
            if(!validProgress.isInvalid())
            {
                if(!isValidProgress())
                {
                    setInvalid(true);
                    milestoneProgress.setInvalid(true);
                    validProgress.setInvalid(true);
                    validProgress.setText(Application.getInstance().getResourceBundle().getString("project.message.progressExceeds100"));
                    forward= new Forward(FORWARD_FAILED);
                }
            }
        }
        return forward;
    }

    protected boolean isUniqueMilestone()
    {
        boolean unique = false;
        try
        {
            WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
            DaoQuery query = new DaoQuery();
            query.addProperty(new OperatorEquals("milestoneName", milestoneName.getValue().toString(), DaoOperator.OPERATOR_AND));
            query.addProperty(new OperatorEquals("projectId", projectId, DaoOperator.OPERATOR_AND));
            if(!(milestoneId == null || "".equals(milestoneId)))
                query.addProperty(new OperatorEquals("milestoneId", milestoneId, DaoOperator.OPERATOR_NAN));
            Collection list = worms.getMilestones(query, 0, 1, null, false, false);
            if(list.size() <= 0)
                unique = true;
        }
        catch (WormsException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return unique;
    }

    protected boolean isValidProgress()
    {
        boolean valid = false;
        if(!(projectId == null || "".equals(projectId)))
        {
            try
            {
                WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                String milestoneId = "-1";
                if(!(this.milestoneId == null || "".equals(this.milestoneId)))
                    milestoneId = this.milestoneId;
                int total = worms.getMilestoneTotalProgress(projectId, milestoneId);
                if((total + Integer.parseInt(milestoneProgress.getValue().toString())) <= 100)
                    valid = true;
            }
            catch (WormsException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }
        return valid;
    }

    protected Milestone generateMilestone()
    {
        Milestone milestone = new Milestone();
        if(milestoneId == null || "".equals(milestoneId))
            milestone.setMilestoneId(UuidGenerator.getInstance().getUuid());
        else
            milestone.setMilestoneId(milestoneId);
        milestone.setMilestoneName(milestoneName.getValue().toString());
        milestone.setMilestoneDescription(milestoneDescription.getValue().toString());
        milestone.setMilestoneProgress(Integer.parseInt(milestoneProgress.getValue().toString()));
        milestone.setProjectId(projectId);
        return milestone;
    }

    /* Getters and Setters */
    public String getProjectId()
    {
        return projectId;
    }

    public void setProjectId(String projectId)
    {
        this.projectId = projectId;
    }

    public String getMilestoneId()
    {
        return milestoneId;
    }

    public void setMilestoneId(String milestoneId)
    {
        this.milestoneId = milestoneId;
    }

    public TextField getMilestoneName()
    {
        return milestoneName;
    }

    public void setMilestoneName(TextField milestoneName)
    {
        this.milestoneName = milestoneName;
    }

    public TextBox getMilestoneDescription()
    {
        return milestoneDescription;
    }

    public void setMilestoneDescription(TextBox milestoneDescription)
    {
        this.milestoneDescription = milestoneDescription;
    }

    public TextField getMilestoneProgress()
    {
        return milestoneProgress;
    }

    public void setMilestoneProgress(TextField milestoneProgress)
    {
        this.milestoneProgress = milestoneProgress;
    }

    public Button getSubmit()
    {
        return submit;
    }

    public void setSubmit(Button submit)
    {
        this.submit = submit;
    }

    public Button getCancel()
    {
        return cancel;
    }

    public void setCancel(Button cancel)
    {
        this.cancel = cancel;
    }

    public ValidatorNotEmpty getValidName()
    {
        return validName;
    }

    public void setValidName(ValidatorNotEmpty validName)
    {
        this.validName = validName;
    }

    public ValidatorIsNumeric getValidProgress()
    {
        return validProgress;
    }

    public void setValidProgress(ValidatorIsNumeric validProgress)
    {
        this.validProgress = validProgress;
    }

    public boolean isCurrentlyTemplate()
    {
        return currentlyTemplate;
    }

    public void setCurrentlyTemplate(boolean currentlyTemplate)
    {
        this.currentlyTemplate = currentlyTemplate;
    }

    public void setCurrentTemplate(String string)
    {
        if("true".equals(string))
            setCurrentlyTemplate(true);
        else
            setCurrentlyTemplate(false);
    }
}
