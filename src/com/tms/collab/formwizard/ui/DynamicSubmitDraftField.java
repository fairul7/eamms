package com.tms.collab.formwizard.ui;

import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import com.tms.collab.formwizard.model.FormModule;
import com.tms.collab.formwizard.model.FormWorkFlowDataObject;
import com.tms.collab.formwizard.model.FormDaoException;
import com.tms.collab.formwizard.engine.*;

import java.util.List;
import java.util.Iterator;

public class DynamicSubmitDraftField extends DynamicEditFormDataField {
    public void onRequest (Event event) {
        getDraftFormId();
        super.onRequest(event);

    }

    public void  getDraftFormId() {
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        FormWorkFlowDataObject obj;
        try {

            obj =   module.getFormDraft(getId());
            if (obj != null)
                setFormId(obj.getFormId());

        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
    }

    public void resetValidator(List fieldList) {
        Object object;
        FileLinkCheckboxField fileLinkCheckboxField;
        ValidatorFileLinkCheckboxField validatorFileLinkCheckboxField;
        super.resetValidator(fieldList);

        for (Iterator iterator = fieldList.iterator(); iterator.hasNext();) {
            object =  iterator.next();

            if (object instanceof FileLinkCheckboxField) {
                fileLinkCheckboxField = (FileLinkCheckboxField) object;

                if (fileLinkCheckboxField.getValidatorFileLinkCheckboxField() != null) {
                    fileLinkCheckboxField.setInvalid(Field.FIELD_VALID);
                    validatorFileLinkCheckboxField = fileLinkCheckboxField.getValidatorFileLinkCheckboxField();
                    validatorFileLinkCheckboxField.setInvalid(Field.FIELD_VALID);
                }

            }

        }
    }

    public void setListener(ListenerForm listener) {
        listener.setClassName(FormDraftSubmissionEvent.class.getName());
    }

    public void setPanelField(PanelField panel) {
        FormLayout formLayout = panel.getFormLayout();
        ButtonField buttonField = new ButtonField();
        buttonField.setName("draft");
        buttonField.setText(Application.getInstance().getMessage("formWizard.label.viewForm.draft", "Save As Draft"));
        if (panel.getButtonList() != null)
            panel.getButtonList().add(buttonField);

        if (formLayout != null) {
            formLayout.setWidth("100%");
            parseLayout(formLayout.getFieldList());
        }
    }




}
