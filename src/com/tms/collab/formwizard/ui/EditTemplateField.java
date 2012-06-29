package com.tms.collab.formwizard.ui;

import com.tms.collab.formwizard.xmlwidget.FormElement;
import com.tms.collab.formwizard.model.*;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.Application;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

import org.jdom.Document;


public class EditTemplateField extends EditField {
    private String formTemplateId;


    public FormElement getFormElement()  {
        FormTemplate formTemplate = new FormTemplate();
        formTemplate.setFormTemplateId(getFormTemplateId());
        return Util.getTemplateElement(formTemplate);
    }

    public Forward editFormConfig(FormElement form, HttpServletRequest request)  {
        FormTemplate formTemplate = null;
        FormModule handler = (FormModule)Application.getInstance().getModule(FormModule.class);


        formTemplate = new FormTemplate();
        formTemplate.setTemplateName(form.getAttributeValue("name"));
        try {
            formTemplate.setPreviewXml(form.display());
            formTemplate.setFormXml("");
            formTemplate.setFormTemplateId(getFormTemplateId());

            //update XML
            handler.updateFormTemplateTable(formTemplate);

            //alter the regarding tables
            handler.updateFormTablesColumn(getFormTemplateId(),getFormUid());

            return new Forward("fieldEdited");
        }
        catch (IOException e) {
            Log.getLog(getClass()).error("form.display() error",e);
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        

        return null;
    }

    public InputStream getInputStream() throws FormDaoException {
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        InputStream stream = module.getTemplatePreviewXml(getFormTemplateId());
        return stream;
    }

    public void remove(Event evt) {
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        FormElement form = Util.getTemplateElement(getFormTemplateId());

        try {
            module.removeTemplateField(form,getFormUid(),getFormTemplateId());
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        catch (FormException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        catch (FormDocumentException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
    }

     public void updateConfig(org.w3c.dom.Document w3cDocument) {

        FormModule handler = (FormModule)Application.getInstance().getModule(FormModule.class);
        FormDao dao = (FormDao)handler.getDao();
        FormElement form = null;
        Document jDomDocument = null;

        try {
            String previewXml = Util.domDocumentToString(w3cDocument);
            jDomDocument = Util.DOMtoJDOM(w3cDocument);
            form = (FormElement) FormElement.newInstance(jDomDocument.getRootElement());

            FormTemplate template = new FormTemplate();
            template.setFormTemplateId(getFormTemplateId());
            template.setTemplateName(String.valueOf(form.getAttributeValue("name")));
            template.setPreviewXml(previewXml);





            dao.updateFormTemplate(template);

        }
        /*catch (IOException e) {
            Log.getLog(getClass()).error("",e);
        }                    */
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        catch (FormDocumentException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        catch (FormException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
    }

    public String getDefaultTemplate() {
        return "formwizard/editTemplateFormField";
    }


    public String getFormTemplateId() {
        return formTemplateId;
    }

    public void setFormTemplateId(String formTemplateId) {
        this.formTemplateId = formTemplateId;
    }


}
