package com.tms.collab.project.ui;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import com.tms.collab.project.Template;
import com.tms.collab.project.WormsHandler;
import com.tms.collab.project.WormsException;

public class TemplateOpen extends TemplateForm
{
    public void onRequest(Event event)
    {
        super.onRequest(event);
        if(!isKeyEmpty())
        {
            try
            {
                WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
                Template template = worms.getTemplate(templateId);
                if(template != null)
                {
                    templateName.setValue(template.getTemplateName());
                    templateDescription.setValue(template.getTemplateDescription());
                    templateCategory.setSelectedOptions(new String[] {template.getTemplateCategory()});
                }
            }
            catch (WormsException e)
            {
                Log.getLog(getClass()).error(e.getMessage(), e);
            }
        }
    }

    public Forward onValidate(Event event)
    {
        Forward forward = super.onValidate(event);
        try
        {
            Template template = generateTemplate();
            WormsHandler worms = (WormsHandler) Application.getInstance().getModule(WormsHandler.class);
            worms.updateTemplate(template);
            forward = new Forward(FORWARD_SUCESSFUL);
        }
        catch (WormsException e)
        {
            forward = new Forward(FORWARD_FAILED);
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        return forward;
    }

    protected boolean isKeyEmpty()
    {
        boolean empty = true;
        if(!(templateId == null || "".equals(templateId)))
            empty = false;
        return empty;
    }

    public void setTemplateId(String templateId)
    {
        super.setTemplateId(templateId);
        init();
    }

	public boolean isEditMode() {
		return true;
	}

}
