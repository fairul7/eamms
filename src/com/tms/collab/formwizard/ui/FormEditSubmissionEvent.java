package com.tms.collab.formwizard.ui;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.model.DaoException;
import kacang.util.Log;
import com.tms.collab.formwizard.model.*;
import com.tms.collab.formwizard.engine.HiddenField;
import com.tms.collab.formwizard.engine.Field;
import com.tms.collab.formwizard.engine.FileLinkCheckboxField;
import com.tms.collab.formwizard.widget.FileLinkCheckbox;


import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.io.IOException;


public class FormEditSubmissionEvent extends FormSubmissionEvent {

    public Forward onValidate(Event event) {
        Forward forward = null;
        String buttonName = findButtonClicked(event);
        try {
            if (buttonName.endsWith("submit")) {
                saveForm(event);
                forward = new Forward("dataSaved");
            }
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        catch (FormDocumentException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        catch (FormException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }

        return forward;
    }

    public void saveForm(Event event) throws FormDaoException, FormDocumentException, FormException  {
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        FormWorkFlowDataObject fwfdo = getFormWorkFlowDataObject(event);
        module.saveEditFormData(fwfdo, event, layout.getFieldList());


    }

    public void parseFieldList(List fieldList, Map values,
                               String templateId, FormWorkFlowDataObject fwfdo, Event event) {
        Field field;
        FileLinkCheckboxField fileLinkCheckboxField;
        FormData formData;

        for (Iterator iterator = fieldList.iterator(); iterator.hasNext();) {
            field = (Field) iterator.next();

            if (field instanceof HiddenField) {
                if ("formUid".equals(field.getName()))
                    fwfdo.setFormUid((String) field.getValue());
            }
        }

        super.parseFieldList(fieldList, values, templateId, fwfdo, event);

        for (Iterator iterator = fieldList.iterator(); iterator.hasNext();) {
            field = (Field) iterator.next();
            formData = new FormData();
            formData.setType(field);
            formData.setWidget(field.getWidget());

            if (field instanceof FileLinkCheckboxField) {
                fileLinkCheckboxField = (FileLinkCheckboxField) field;
                if (fileLinkCheckboxField.isChecked())
                    fwfdo.getFileList().add(fileLinkCheckboxField.getCheckBoxValue());

                if (fileLinkCheckboxField.getFileUploadValue() != null) {
                    fwfdo.getFileList().add(fileLinkCheckboxField.getCheckBoxValue());
                    try {
                        putFileUpload(((FileLinkCheckbox) fileLinkCheckboxField.getWidget()).getFile(),
                                      fileLinkCheckboxField.getName(), event, fwfdo, templateId, formData, values);
                    }
                    catch (IOException e) {
                        Log.getLog(getClass()).fatal(e.getMessage(), e);
                    }
                }

                if (!fileLinkCheckboxField.isChecked() && fileLinkCheckboxField.getFileUploadValue() == null) {
                    formData.setValue(fileLinkCheckboxField.getCheckBoxValue());
                    formData.setDisplayValue(formData.getValue());
                    values.put(templateId + fileLinkCheckboxField.getName(), formData);
                }


            }
        }
    }

}
