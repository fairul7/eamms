package com.tms.collab.formwizard.ui;

import kacang.stdui.Button;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import com.tms.collab.formwizard.model.*;






public class FormDataDetail extends ResubmitFormData {
    protected Button approveButton;
    protected Button rejectButton;

    public void onRequest(Event event) {
         url = event.getRequest().getScheme() + "://" + event.getRequest().getServerName() + ":"
              + event.getRequest().getServerPort() + "/storage"      ;

         initForm();
    }

    public void initForm() {
        setData();
        initViewForm();
        removeChild(cancelButton);
        approveButton = new Button("approveButton");
        approveButton.setText(Application.getInstance().getMessage("formWizard.label.formDataDetail.approve","Approve"));
        rejectButton = new Button("rejectButton");
        rejectButton.setText(Application.getInstance().getMessage("formWizard.label.formDataDetail.reject","Reject"));
        addChild(approveButton);
        addChild(rejectButton);
        addChild(cancelButton);
    }

    public Forward onValidate(Event evt) {
        Forward forward = null;
        forward = new Forward("");
        forward = super.onValidate(evt);

        String buttonName = findButtonClicked(evt);



        if (buttonName != null) {
            if (buttonName.equals(approveButton.getAbsoluteName())) {
                forward = approveData(evt);
            }
            else if (buttonName.equals(rejectButton.getAbsoluteName())) {
                //forward = rejectData(evt);
                forward = new Forward("");
                forward.setName("rejectData");
            }

        }


        return forward;

    }

    protected Forward approveData(Event evt) {
        String userID;
        Forward forward = null;

        forward = new Forward("");
        FormModule handler = (FormModule) Application.getInstance().getModule(FormModule.class);


        userID = getWidgetManager().getUser().getId();

        try {
            handler.approveFormData(getId(), "approve", userID, evt);
            forward.setName("dataApproved");
        }
        catch (FormException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
            forward.setName("fail");
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        catch (FormDocumentException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }

        return forward;
    }

    protected Forward rejectData(Event evt) {
        String userID;
        Forward forward = null;

        forward = new Forward("");
        FormModule handler = (FormModule) Application.getInstance().getModule(FormModule.class);


        userID = getWidgetManager().getUser().getId();

        try {
            handler.approveFormData(getId(),"reject",userID, evt);
            forward.setName("dataRejected");
        }
        catch (FormException e) {
            Log.getLog(getClass()).error(e.toString());
            forward.setName("fail");
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        catch (FormDocumentException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }

        return forward;
    }
}
