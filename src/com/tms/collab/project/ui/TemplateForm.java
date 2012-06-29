package com.tms.collab.project.ui;

import com.tms.collab.project.Template;
import com.tms.collab.project.WormsException;
import com.tms.collab.project.WormsHandler;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import org.apache.commons.collections.SequencedHashMap;

import java.util.Collection;
import java.util.Iterator;

public abstract class TemplateForm extends Form
{
    public static final String FORWARD_SUCESSFUL = "forward.worms.template.Successful";
    public static final String FORWARD_FAILED = "forward.worms.template.Failed";
    public static final String FORWARD_CANCEL = "forward.worms.template.Cancel";
    public static final String DEFAULT_CATEGORY = "-1";

    protected TextField templateName;
    protected TextBox templateDescription;
    protected SelectBox templateCategory;
    protected TextField newCategory;
    protected Button submit;
    protected Button cancel;

    protected ValidatorNotEmpty validName;

    protected String templateId;

    public TemplateForm()
    {
    }

    public TemplateForm(String s)
    {
        super(s);
    }

    public void init()
    {
        super.init();
        setMethod("POST");
        if(!isKeyEmpty())
        {
            setColumns(2);

            validName = new ValidatorNotEmpty("validName");
            templateName = new TextField("templateName");
            templateName.setSize("35");
            templateName.addChild(validName);
            templateDescription = new TextBox("templateDescription");
            templateDescription.setRows("7");
            templateDescription.setCols("35");
            templateCategory = new SelectBox("templateCategory");
            newCategory = new TextField("newCategory");
            newCategory.setSize("35");
            Panel categoryPanel = new Panel("categoryPanel");
            categoryPanel.addChild(new Label("labelNewCategory", Application.getInstance().getResourceBundle().getString("resourcemanager.label.NewCategory")+" * " + ": "));
            categoryPanel.addChild(newCategory);

			if (isEditMode()) {
            	submit = new Button("submit", Application.getInstance().getResourceBundle().getString("general.label.update"));
			} else {
				submit = new Button("submit", Application.getInstance().getResourceBundle().getString("general.label.submit"));
			}
			cancel = new Button("cancel", Application.getInstance().getResourceBundle().getString("general.label.cancel"));
            Panel buttonPanel = new Panel("buttonPanel");
            buttonPanel.addChild(submit);
            buttonPanel.addChild(cancel);

            addChild(new Label("labelName", Application.getInstance().getResourceBundle().getString("project.label.name")+" *"));
            addChild(templateName);
            addChild(new Label("labelDescription", Application.getInstance().getResourceBundle().getString("project.label.description")));
            addChild(templateDescription);
            addChild(new Label("labelCategory", Application.getInstance().getResourceBundle().getString("project.label.category")+" *"));
            addChild(templateCategory);
            addChild(new Label("labelNew", ""));
            addChild(categoryPanel);
            addChild(new Label("labelButton", ""));
            addChild(buttonPanel);
        }
    }

    public void onRequest(Event event)
    {
        super.onRequest(event);
        if(!isKeyEmpty())
            refresh();
    }

    public Forward actionPerformed(Event event)
    {
        Forward forward = null;
        if(!isKeyEmpty())
        {
            if(cancel.getAbsoluteName().equals(findButtonClicked(event)))
                forward = new Forward(FORWARD_CANCEL);
            else
                forward = super.actionPerformed(event);
        }
        return forward;
    }

    public Forward onSubmit(Event event)
    {
        Forward forward =  super.onSubmit(event);
        if(!isKeyEmpty())
        {
            String selected = (String) templateCategory.getSelectedOptions().keySet().iterator().next();
            if(DEFAULT_CATEGORY.equals(selected) && (newCategory.getValue() == null || "".equals(newCategory.getValue())))
            {
                setInvalid(true);
                templateCategory.setInvalid(true);
                newCategory.setInvalid(true);
            }
            else if(isNameExists())
            {
                setInvalid(true);
                templateName.setInvalid(true);
                validName.setInvalid(true);
                validName.setText(Application.getInstance().getResourceBundle().getString("project.message.templateNameInUse"));
            }
        }
        return forward;
    }

    protected void refresh()
    {
        validName.setText(Application.getInstance().getResourceBundle().getString("project.message.enterTemplateName"));
        //Refreshing category options
        try
        {
            WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
            Collection list = worms.getTemplateCategories();
            SequencedHashMap map = new SequencedHashMap();
            map.put(DEFAULT_CATEGORY, Application.getInstance().getResourceBundle().getString("project.label.selectACategory"));
            for (Iterator i = list.iterator(); i.hasNext();)
            {
                String category = (String) i.next();
                map.put(category, category);
            }
            templateCategory.setOptionMap(map);
        }
        catch (WormsException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
    }

    protected boolean isNameExists()
    {
        boolean exists = true;
        WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
        DaoQuery query = new DaoQuery();
        query.addProperty(new OperatorEquals("templateName", templateName.getValue().toString(), DaoOperator.OPERATOR_AND));
        if(!(templateId == null || "".equals(templateId)))
            query.addProperty(new OperatorEquals("templateId", templateId, DaoOperator.OPERATOR_NAN));
        try
        {
            Collection list = worms.getTemplates(query, 0, 1, null, false);
            if(list.size() <= 0)
                exists = false;
        }
        catch(WormsException e)
        {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return exists;
    }

    protected Template generateTemplate()
    {
        Template template = new Template();
        if(templateId == null || "".equals(templateId))
            template.setTemplateId(UuidGenerator.getInstance().getUuid());
        else
            template.setTemplateId(templateId);
        template.setTemplateName(templateName.getValue().toString());
        template.setTemplateDescription(templateDescription.getValue().toString());
        if(!(newCategory.getValue() == null || "".equals(newCategory.getValue())))
            template.setTemplateCategory(newCategory.getValue().toString());
        else
            template.setTemplateCategory(templateCategory.getSelectedOptions().keySet().iterator().next().toString());
        return template;
    }

    protected boolean isKeyEmpty()
    {
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

    public TextField getNewCategory()
    {
        return newCategory;
    }

    public void setNewCategory(TextField newCategory)
    {
        this.newCategory = newCategory;
    }

    public Button getSubmit()
    {
        return submit;
    }

    public void setSubmit(Button submit)
    {
        this.submit = submit;
    }

    public SelectBox getTemplateCategory()
    {
        return templateCategory;
    }

    public void setTemplateCategory(SelectBox templateCategory)
    {
        this.templateCategory = templateCategory;
    }

    public TextBox getTemplateDescription()
    {
        return templateDescription;
    }

    public void setTemplateDescription(TextBox templateDescription)
    {
        this.templateDescription = templateDescription;
    }

    public TextField getTemplateName()
    {
        return templateName;
    }

    public void setTemplateName(TextField templateName)
    {
        this.templateName = templateName;
    }

    public String getTemplateId()
    {
        return templateId;
    }

    public void setTemplateId(String templateId)
    {
        this.templateId = templateId;
    }

	public boolean isEditMode() {
		return false;
	}
}
