package com.tms.collab.formwizard.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import com.tms.collab.formwizard.model.*;
import com.tms.collab.formwizard.xmlwidget.FormElement;

import java.util.Collection;
import java.util.Iterator;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdom.Document;




public class EditFormTemplate extends AddFormTemplate {
    public void onRequest(Event event) {
        super.onRequest(event);
        fillupField();
    }

    public void init() {
        super.init();
        addButton.setText("Save");
    }

    public void verifyField() {
        if (templateNameTextField.getValue() != null){
            FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);

            try {
                boolean exist = module.isFormTemplateNameExist(StringEscapeUtils.escapeSql(templateNameTextField.getValue().toString()),
                                                               getTemplateId());
                if (exist) {
                    templateNameTextField.setInvalid(true);
                    templateNameTextField.setMessage("Template name already exist !");
                    setInvalid(true);
                }
            }
            catch (FormDaoException e) {
                templateNameTextField.setInvalid(true);
                templateNameTextField.setMessage("Error checking the duplication of this template name");
                setInvalid(true);
                Log.getLog(getClass()).error(e.getMessage(), e);
           }
        }

        if (!tfTableColumn.isInvalid() && !tfTableColumn.getValue().toString().equals("")
            &&(Integer.parseInt(tfTableColumn.getValue().toString())) < 2) {
            tfTableColumn.setInvalid(true);
            tfTableColumn.setMessage("Table column value must be greater than 1");
            setInvalid(true);
        }
    }

    public String getDefaultTemplate() {
        return "formwizard/editFormTemplate";
    }

    public void fillupField() {
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        Collection data = null;
        FormTemplate template = null;
        FormElement formTemplate;
        String columns;
        try {
            data = module.getFormTemplate(getTemplateId());
            for (Iterator iterator = data.iterator(); iterator.hasNext();) {
                template =  (FormTemplate) iterator.next();
                templateNameTextField.setValue(template.getTemplateName());
                formTemplate = Util.getTemplateElement(template.getFormTemplateId());
                columns = formTemplate.getAttributeValue("columns");
                if  (columns == null || columns.equals(""))
                    columns = "2";
                tfTableColumn.setValue(columns);
            }





        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }

    }

     public Forward manageTemplate() {
        Forward forward = null;
        FormTemplate template = null;
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        try {
            template = setFormTemplate();
            module.updateFormTemplateColumn(template);
            forward = new Forward("formTemplateEdited");
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        catch (FormDocumentException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }


        return forward;
     }

     public FormTemplate setFormTemplate() throws FormDaoException, FormDocumentException {
        FormTemplate template = null;
        template = new FormTemplate();
        template.setFormTemplateId(getTemplateId());
        template.setTemplateName(templateNameTextField.getValue().toString());
        template.setTableColumn(tfTableColumn.getValue().toString());


        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        Collection data = module.getFormTemplate(getTemplateId());
        for (Iterator iterator = data.iterator(); iterator.hasNext();) {
            FormTemplate formTemplate =  (FormTemplate) iterator.next();
            template.setPreviewXml(setTemplateNodeName(formTemplate.getPreviewXml()));
        }




        return template;


    }

    public String setTemplateNodeName(String xml) throws FormDocumentException {
        try {
            if (xml != null) {
                Document jDomDocument = Util.buildJDomDocument(new ByteArrayInputStream(xml.getBytes("UTF-8")));
                jDomDocument.getRootElement().setAttribute("name",Util.parseFormName(templateNameTextField.getValue().toString().trim()));
                return Util.JDomDocumentToString(jDomDocument);
            }
        }
        catch (UnsupportedEncodingException e) {
            Log.getLog(getClass()).error("Unsupported Encoding",e);
        }
        return xml;
    }
}
