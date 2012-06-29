package com.tms.collab.project.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorRange;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.DaoOperator;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.TaskDescriptor;
import com.tms.collab.project.Milestone;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;

public abstract class DescriptorForm extends Form
{
	public static final String FORWARD_SUCCESS = "forward_success";
	public static final String FORWARD_FAILED = "forward_failed";
	public static final String FORWARD_CANCEL = "forward_cancel";
    public static final String FORWARD_DELETE = "forward_delete";

	protected SelectBox milestone;
	protected TextField descName;
	protected TextBox descDescription;
	protected TextField descStart;
	protected TextField descDuration;
	protected SelectBox descriptors;
	protected Button submit;
	protected Button delete;
	protected Button cancel;

    protected ValidatorNotEmpty validName;
	protected ValidatorIsNumeric validStart;
    protected ValidatorIsNumeric validDuration;
    protected ValidatorNotEmpty validDescription;

	protected String projectId;
	protected String descId;

	public DescriptorForm()
	{
		super();
	}

	public DescriptorForm(String s)
	{
		super(s);
	}

	public void init()
	{
		super.init();
        if(!isKeyEmpty())
		{
			setColumns(2);
            Application app = Application.getInstance();

			milestone = new SelectBox("milestone");
			milestone.setSelectedOption("-1");
			descName = new TextField("descName");
			descName.setSize("35");
			descDescription = new TextBox("descDescription");
			descDescription.setRows("10");
			descDescription.setCols("40");
			descStart = new TextField("descStart");
			descStart.setSize("5");
			descDuration = new TextField("descDuration");
			descDuration.setSize("5");
			descriptors = new SelectBox("descriptors");
			descriptors.setOnChange("submit();");
			descriptors.setSelectedOption("-1");
            submit = new Button("submit", app.getResourceBundle().getString("general.label.submit"));
            cancel = new Button("cancel", app.getResourceBundle().getString("general.label.cancel"));
            delete = new Button("delete", app.getResourceBundle().getString("general.label.delete"));


			validName = new ValidatorNotEmpty("validName");
			descName.addChild(validName);
			validStart = new ValidatorIsNumeric("validStart");
			validStart.setText(app.getMessage("project.error.startdate","Start day has to be numeric"));
			descStart.addChild(validStart);
			validDuration = new ValidatorIsNumeric("validDuration");
			validDuration.setText(app.getMessage("project.error.duration","Duration has to be numeric"));
			descDuration.addChild(validDuration);
            descDuration.addChild(new ValidatorRange("validRange", "", new Double(1), null));
			validDescription = new ValidatorNotEmpty("validDescription");
			validDescription.setText(app.getMessage("project.error.description","Please enter a description"));
			descDescription.addChild(validDescription);
            Panel buttonPanel = new Panel("buttonPanel");
			buttonPanel.addChild(submit);
			buttonPanel.addChild(delete);
			buttonPanel.addChild(cancel);

			addChild(new Label("labelMilestone", app.getMessage("project.label.milestone","Milestone")+" *"));
			addChild(milestone);
			addChild(new Label("labelName", app.getMessage("project.label.title","Title")+" *"));
			addChild(descName);
			addChild(new Label("labelDescription", app.getMessage("project.label.description","Description")+" *"));
			addChild(descDescription);
			addChild(new Label("labelDescriptors", app.getMessage("project.label.startafter","Starts After")));
			addChild(descriptors);
			addChild(new Label("labelStart", app.getMessage("project.label.startaday","Starts At Day")+" *"));
			addChild(descStart);
			addChild(new Label("labelDuration", app.getMessage("project.label.duration","Duration")+" *"));
			addChild(descDuration);
			addChild(new Label("labelButton", ""));
			addChild(buttonPanel);
		}
	}

	public void onRequest(Event event)
	{
		refresh();
	}

	public Forward actionPerformed(Event event)
	{
		Forward forward = null;
		if(!isKeyEmpty())
		{
			if(cancel.getAbsoluteName().equals(findButtonClicked(event)))
				forward = new Forward(FORWARD_CANCEL);
			else if(delete.getAbsoluteName().equals(findButtonClicked(event)))
			{
				if(!(descId == null || "".equals(descId)))
				{
					try
					{
						WormsHandler handler = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
						handler.deleteDescriptor(descId);
						forward = new Forward(FORWARD_DELETE);
					}
					catch (WormsException e)
					{
						Log.getLog(getClass()).error("", e);
					}
				}
			}
			else
				forward = super.actionPerformed(event);
		}
		return forward;
	}

	public Forward onSubmit(Event event)
	{
		Forward forward = null;
        if(!isKeyEmpty())
        {
			forward = super.onSubmit(event);
			if(submit.getAbsoluteName().equals(findButtonClicked(event)))
			{
				if("-1".equals(milestone.getSelectedOptions().keySet().iterator().next()))
				{
					setInvalid(true);
					milestone.setInvalid(true);
					forward= new Forward(FORWARD_FAILED);
				}
				else if(!isUniqueDescriptor())
				{
					setInvalid(true);
					descName.setInvalid(true);
					validName.setInvalid(true);
					validName.setText("Descriptor Name Already In Use");
					forward= new Forward(FORWARD_FAILED);
				}
			}
			else
			{
				setInvalid(true);
				validName.setInvalid(false);
				descName.setInvalid(false);
				validStart.setInvalid(false);
				descStart.setInvalid(false);
				validDuration.setInvalid(false);
				descDuration.setInvalid(false);
				validDescription.setInvalid(false);
				validDescription.setInvalid(false);
				refresh();
			}
        }
        return forward;
	}

	protected boolean isUniqueDescriptor()
	{
		try
		{
			WormsHandler handler = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
			DaoQuery query = new DaoQuery();
			query.addProperty(new OperatorEquals("descName", descName.getValue(), DaoOperator.OPERATOR_AND));
			query.addProperty(new OperatorEquals("milestoneId", milestone.getSelectedOptions().keySet().iterator().next(), DaoOperator.OPERATOR_AND));
			if(!(descId == null || "".equals(descId)))
				query.addProperty(new OperatorEquals("descId", descId, DaoOperator.OPERATOR_NAN));
			Collection list = handler.getDescriptors(query, 0, 1, null, false);
			if(list.size() == 0)
				return true;
		}
		catch (WormsException e)
		{
			Log.getLog(getClass()).error("Error while trying to determine uniqueness for task descriptor " + descName.getValue(), e);
		}
		return false;
	}

	protected TaskDescriptor generateDescriptor()
	{
		TaskDescriptor descriptor = new TaskDescriptor();
		if(descId == null || "".equals(descId))
			descriptor.setDescId(UuidGenerator.getInstance().getUuid());
		else descriptor.setDescId(descId);
		descriptor.setMilestoneId(milestone.getSelectedOptions().keySet().iterator().next().toString());
		descriptor.setDescName(descName.getValue().toString());
		descriptor.setDescDescription(descDescription.getValue().toString());
		descriptor.setDescStart(Integer.parseInt(descStart.getValue().toString()));
		descriptor.setDescDuration(Integer.parseInt(descDuration.getValue().toString()));
		return descriptor;
	}

	protected void refresh()
	{
		String selected = "-1";
        if(!isKeyEmpty())
		{
			validName.setInvalid(false);
			validName.setText("Please Enter The Descriptor Name");
            validStart.setInvalid(false);
            validDuration.setInvalid(false);
            validDescription.setInvalid(false);

            if(descId == null || "".equals(descId))
				delete.setHidden(true);
			else
				delete.setHidden(false);

			WormsHandler handler = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
			try
			{
				//Initializing milestones
				milestone.addOption("-1", Application.getInstance().getMessage("project.label.selectAMilestone","Select A Milestone"));
				Collection milestones = handler.getMilestonesByProject(projectId, false);
				for (Iterator i = milestones.iterator(); i.hasNext();)
				{
					Milestone object = (Milestone) i.next();
					milestone.addOption(object.getMilestoneId(), object.getMilestoneName());
				}
				//Initializing descriptors
				Map options = new SequencedHashMap();
				options.put("-1", Application.getInstance().getMessage("project.label.selectADescriptor","Select A Descriptor"));
				Collection list = handler.getDescriptorsByProject(projectId);
				for (Iterator i = list.iterator(); i.hasNext();)
				{
					TaskDescriptor task =  (TaskDescriptor) i.next();
					if(!(task.getDescId().equals(descId)))
						options.put(task.getDescId(), task.getDescName());
				}
				descriptors.setOptionMap(options);
				//Setting start days
				selected = descriptors.getSelectedOptions().keySet().iterator().next().toString();
				if(!("-1".equals(selected)))
				{
                    TaskDescriptor descriptor = handler.getDescriptor(selected);
					if(descriptor != null)
						descStart.setValue(String.valueOf((descriptor.getDescStart()-1) + descriptor.getDescDuration() + 1));
				}
			}
			catch (WormsException e)
			{
				Log.getLog(getClass()).error("Error while retrieving milestones", e);
			}
		}
	}

	protected boolean isKeyEmpty()
	{
		if(!(projectId == null || "".equals(projectId)))
			return false;
		return true;
	}

	/* Getters and Setters */
	public TextField getDescName()
	{
		return descName;
	}

	public void setDescName(TextField descName)
	{
		this.descName = descName;
	}

	public TextBox getDescDescription()
	{
		return descDescription;
	}

	public void setDescDescription(TextBox descDescription)
	{
		this.descDescription = descDescription;
	}

	public TextField getDescStart()
	{
		return descStart;
	}

	public void setDescStart(TextField descStart)
	{
		this.descStart = descStart;
	}

	public SelectBox getDescriptors()
	{
		return descriptors;
	}

	public void setDescriptors(SelectBox descriptors)
	{
		this.descriptors = descriptors;
	}

	public Button getSubmit()
	{
		return submit;
	}

	public void setSubmit(Button submit)
	{
		this.submit = submit;
	}

	public Button getDelete()
	{
		return delete;
	}

	public void setDelete(Button delete)
	{
		this.delete = delete;
	}

	public TextField getDescDuration()
	{
		return descDuration;
	}

	public void setDescDuration(TextField descDuration)
	{
		this.descDuration = descDuration;
	}

	public Button getCancel()
	{
		return cancel;
	}

	public void setCancel(Button cancel)
	{
		this.cancel = cancel;
	}

	public String getProjectId()
	{
		return projectId;
	}

	public void setProjectId(String projectId)
	{
		this.projectId = projectId;
	}

	public SelectBox getMilestone()
	{
		return milestone;
	}

	public void setMilestone(SelectBox milestone)
	{
		this.milestone = milestone;
	}

	public String getDescId()
	{
		return descId;
	}

	public void setDescId(String descId)
	{
		this.descId = descId;
	}
}
