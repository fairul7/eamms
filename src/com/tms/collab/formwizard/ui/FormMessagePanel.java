package com.tms.collab.formwizard.ui;

import kacang.stdui.Panel;
import kacang.stdui.Label;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DaoException;
import com.tms.collab.formwizard.model.FormModule;
import com.tms.collab.formwizard.model.FormDao;
import com.tms.collab.formwizard.model.FormDataObject;
import com.tms.collab.formwizard.model.FormDaoException;

import java.util.Collection;
import java.util.Iterator;


public class FormMessagePanel extends Panel{
    private Label submissionMsg;
    private Label submissionEmail;
    private String formId;
    public FormMessagePanel() {
    }

    public FormMessagePanel(String s) {
        super(s);
    }

    public void onRequest(Event event) {
        initPanel();
    }

    public void initPanel(){
        submissionMsg = new Label("submissionMsg");
        submissionEmail = new Label("submissionEmail");
        Application application = Application.getInstance();
        FormModule handler = (FormModule)application.getModule(FormModule.class);        
         try {


            FormDataObject formsDO = handler.getForm(getFormId());
            if(formsDO.getFormRetMsg()!=null && !formsDO.getFormRetMsg().equals(""))
                submissionMsg.setText(formsDO.getFormRetMsg());
            else
                submissionMsg.setHidden(true);
            if(formsDO.getFormEmails()!=null && !formsDO.getFormEmails().equals(""))
                submissionEmail.setText(formsDO.getFormEmails());
            else
                submissionEmail.setHidden(true);


        }
         catch (FormDaoException e) {
             Log.getLog(getClass()).error(e.getMessage(),e);
         }
        addChild(submissionMsg);
        addChild(submissionEmail);
    }

    public Label getSubmissionMsg() {
        return submissionMsg;
    }


    public Label getSubmissionEmail() {
        return submissionEmail;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getDefaultTemplate() {
        return "formwizard/formMessagePanel";
    }

}
