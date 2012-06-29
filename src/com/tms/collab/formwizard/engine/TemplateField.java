package com.tms.collab.formwizard.engine;

import com.tms.collab.formwizard.model.FormModule;
import com.tms.collab.formwizard.model.FormDaoException;

import java.io.InputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Iterator;

import kacang.Application;
import kacang.util.Log;

public class TemplateField implements Serializable {
    private String name;
    private String templateId;
    private FormLayout layout;


    public FormLayout getTemplateField() {
        InputStream stream = null;
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        StructureEngine structureEngine = new StructureEngine();
        layout = null;
        try {
            stream = module.getTemplatePreviewXml(templateId);
            structureEngine.setXml(stream);
            layout =  structureEngine.retriveStructure();
            layout.setTemplateId(templateId);
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        finally {
            try {
                if (stream != null)
                    stream.close();
            }
            catch (IOException e) {
            }
        }
        return layout;
    }

    public FormLayout setTemplateField(PanelField panel, Map map) {

        Object object;
        layout = panel.getFormLayout();

        for (Iterator iterator = layout.getFieldList().iterator(); iterator.hasNext();) {
            object =  iterator.next();

            if (object instanceof TextFieldField ||
                object instanceof TextBoxField ||
                object instanceof ButtonGroupField ||
                object instanceof SelectBoxField ||
                object instanceof DateFieldField ||
                object instanceof FileField ||
                object instanceof TableGridField ) {
                ((Field) object).setValue(map.get( panel.getName() +  ((Field) object).getName()));
            }

        }
        return layout;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }




}
