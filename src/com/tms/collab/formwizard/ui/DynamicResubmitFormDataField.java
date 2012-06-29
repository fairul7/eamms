package com.tms.collab.formwizard.ui;

import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import com.tms.collab.formwizard.model.FormModule;
import com.tms.collab.formwizard.model.FormDaoException;
import com.tms.collab.formwizard.model.FormWorkFlowDataObject;
import com.tms.collab.formwizard.engine.PanelField;
import com.tms.collab.formwizard.engine.FormLayout;
import com.tms.collab.formwizard.engine.ButtonField;
import com.tms.collab.formwizard.engine.ListenerForm;

public class DynamicResubmitFormDataField extends DynamicEditFormDataField {
    private String status;

    public void onRequest(Event event) {

        getResubmitFormDataFormId();
        if (FormModule.WORKFLOW_REJECT.equals(getStatus())) {
            super.onRequest(event);
        }
    }

    public void getResubmitFormDataFormId() {
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        try {
            FormWorkFlowDataObject fwfdo = module.getFormsWorkFlow(getId());
            setFormId(fwfdo.getFormId());
            setStatus(fwfdo.getStatus());

        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }

    }

    public void setListener(ListenerForm listener) {
        listener.setClassName(FormResubmitFormDataSubmissionEvent.class.getName());
    }

    public void setPanelField(PanelField panel) {
        FormLayout formLayout = panel.getFormLayout();
        ButtonField buttonField = new ButtonField();
        buttonField.setName(CANCEL_FORM_ACTION);
        buttonField.setText(Application.getInstance().getMessage("formWizard.label.viewFormFields.cancel", "Cancel"));


        if (panel.getButtonList() != null) {
            panel.getButtonList().add(buttonField);
        }


        if (formLayout != null) {
            formLayout.setWidth("100%");
            parseLayout(formLayout.getFieldList());
        }

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
