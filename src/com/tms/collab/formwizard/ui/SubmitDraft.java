package com.tms.collab.formwizard.ui;

import kacang.util.Log;
import kacang.Application;
import com.tms.collab.formwizard.model.FormDaoException;
import com.tms.collab.formwizard.model.FormWorkFlowDataObject;
import com.tms.collab.formwizard.model.FormModule;


public class SubmitDraft extends EditFormData {

    public void initForm() {
        setData();
        super.initForm();
        label.setText("");
    }

    public void setData() {
        FormModule handler = (FormModule) Application.getInstance().getModule(FormModule.class);
        FormWorkFlowDataObject obj;
        try {

            obj =   handler.getFormDraft(getId());
            if (obj != null)
                setFormId(obj.getFormId());

        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
    }




}
