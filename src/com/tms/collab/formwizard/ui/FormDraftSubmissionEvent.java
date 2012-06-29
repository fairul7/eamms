package com.tms.collab.formwizard.ui;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.Application;
import com.tms.collab.formwizard.model.*;


public class FormDraftSubmissionEvent extends FormEditSubmissionEvent {
    public Forward onValidate(Event event) {
        Forward forward = null;
        String buttonName = findButtonClicked(event);
        try {
            if (buttonName.endsWith("draft")) {
                draftForm(event);
                forward = new Forward("dataDrafted");
            }
            else if (buttonName.endsWith("submit")) {
                saveForm(event);
                forward = getSubmissionForward(event);
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

    public void saveForm(Event event) throws FormDaoException, FormDocumentException, FormException {
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        FormWorkFlowDataObject fwfdo = getFormWorkFlowDataObject(event);
        getLabelMap(fwfdo);
        module.saveEditFormData(fwfdo, event, layout.getFieldList());


    }

}
