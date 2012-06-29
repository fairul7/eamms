package com.tms.collab.formwizard.ui;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import com.tms.collab.formwizard.model.*;
import com.tms.collab.formwizard.xmlwidget.FormElement;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;


public class AddFormTemplateField extends AddFormField {
    private String formTemplateId;

    public void init() {
        super.init();
        finish.setHidden(true);
    }
    
    public Forward onValidate(Event evt){
        fieldForm = (AddG2FieldForm)evt.getWidgetManager().getWidget(fieldFormAbsoluteName);
        Forward fwd = null;
        String buttonName = findButtonClicked(evt);

        if (buttonName!=null && finish.getAbsoluteName().equals(buttonName)){
        	fwd = new Forward("formConfigFinish");
        }
        else if (buttonName != null) {
            verifyField();
            if (isInvalid())
                return null;

            if (buttonName != null && add.getAbsoluteName().equals(buttonName)) {
                fwd = addTemplateField();
            }
        }
        else {
            if (fieldForm != null) {
                fieldForm.setColumnList(new ArrayList());
                fieldForm.setIdMap(new HashMap());
                fieldForm.setTableGridDataEmpty(true);
            }
        }


        return fwd;
    }

     public void initilizeFields() {

        fieldType.setSelectedOption(FormConstants.FIELD_SELECT);
        hiddenNo.setChecked(true);
        hiddenYes.setChecked(false);

        requiredNo.setChecked(true);
        requiredYes.setChecked(false);

        fieldName.setValue("");

        fieldSize.setValue("");
        tfDefaultValue.setValue("");
        maxCols.setValue("");
        maxRows.setValue("");
        tfDataType.setSelectedOption(String.valueOf(FormElement.FORM_VARCHAR_TYPE));
        options.setValue("");
        maxLength.setValue("");

        //reset the field error message
        fieldName.setMessage("");
        maxCols.setMessage("");
        fieldSize.setMessage("");
        maxRows.setMessage("");
        options.setMessage("");
        fieldType.setMessage("");
        maxLength.setMessage("");
        tfColspan.setMessage("");
        tfRowspan.setMessage("");

        fieldName.setInvalid(false);
        maxCols.setInvalid(false);
        fieldSize.setInvalid(false);
        maxRows.setInvalid(false);
        options.setInvalid(false);
        fieldType.setInvalid(false);
        maxLength.setInvalid(false);
        tfColspan.setInvalid(false);
        tfRowspan.setInvalid(false);
        setInvalid(false);

        tfColspan.setValue("1");
        tfRowspan.setValue("1");

        tfTitle.setValue("");
        lbColumnLabel.setText("");
    }

    public Forward addTemplateField() {
        Forward forward = null;
        FormTemplate template = null;
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);

        try {
            template = setFormTemplate();
            module.updateFormTemplate(template);
            forward = new Forward("formTemplateFieldAdded");
        }
        catch (FormException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        catch (IOException e) {
            Log.getLog(getClass()).error("Error converting from form element object to string object",e);
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        catch (FormDocumentException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }


        return forward;
    }

    public FormTemplate setFormTemplate() throws FormException, IOException, FormDaoException, FormDocumentException {
        FormTemplate template = null;
        FormElement form = null;


        template = new FormTemplate();
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        Collection data = module.getFormTemplate(getFormTemplateId());
        for (Iterator iterator = data.iterator(); iterator.hasNext();) {
            FormTemplate formTemplate = (FormTemplate) iterator.next();
            template.setTemplateName(formTemplate.getTemplateName());
        }
        
        template.setFormTemplateId(getFormTemplateId());
        form = getPreviewXml(template);
        template.setPreviewXml(form.display());
        /*getFormXml(form);
        template.setFormXml(form.display());*/


        return template;
    }

     public FormElement getPreviewXml(FormTemplate formTemplate) throws FormException, FormDaoException, FormDocumentException {

        FormElement form = Util.getTemplateElement(formTemplate);
        getFieldElement(form);
        return form;
    }

     public void getFormXml(FormElement form) {
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        module.removeFormMetaData(form);
    }





    public String getFormTemplateId() {
        return formTemplateId;
    }

    public void setFormTemplateId(String formTemplateId) {
        this.formTemplateId = formTemplateId;
    }
}
