package com.tms.collab.formwizard.ui;

import kacang.stdui.*;
import kacang.stdui.Label;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DaoException;
import kacang.services.security.User;

import com.tms.collab.formwizard.model.*;
import com.tms.collab.formwizard.xmlwidget.FormElement;

import java.util.*;



import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.SequencedHashMap;


public class EditForm extends AddForm {
    protected Label formNameLabel;

    protected CheckBox activeCheckbox;


    public EditForm() {
    }

    public EditForm(String s) {
        super(s);
    }

    public void onRequest(Event event) {
        super.onRequest(event);
        fillFormFields();
    }




    public void init() {
        super.init();    
        removeChild(formName);
        formNameLabel = new Label("formNameLabel");

        activeCheckbox = new CheckBox("activeCheckbox");


        addChild(formNameLabel);
        addChild(activeCheckbox);

    }




    private void fillFormFields(){
        FormDao dao = (FormDao)Application.getInstance().getModule(FormModule.class).getDao();
        FormDataObject formDO;
        StringTokenizer stk ;
        String token;
        Map nonSelectedFormGroupsMap, selectedFormGroupsMap;
        Set keySet;
        String groupId;
        FormElement form;
        String columns;
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);

        try {
                formDO = module.getForm(getFormId());
                formNameLabel.setText(formDO.getFormDisplayName());
                formHeader.setValue(formDO.getFormHeader());
                if(formDO.getFormInternalFlag().equals("0")) {
                     publicFormAccess.setChecked(true);
                } else {
                    privateFormAccess.setChecked(true);
                }
                String flag= formDO.getFormPublicFlag();
                if(flag.equals("0"))
                    publicFormUserAccess.setChecked(true);
                else if(flag.equals("1")){
                    privateFormUserAccess.setChecked(true);
                    sbAccessUsers.setOptionMap(dao.selectFormUsers(getFormId(),"formAccess"));
                }
                else{
                    groupFormUserAccess.setChecked(true);

                    //set the left values
                    //no duplication of values for left and right select box
                    selectedFormGroupsMap = dao.selectFormGroup(getFormId());
                    nonSelectedFormGroupsMap =  cbGroups.getLeftValues();
                    keySet =  selectedFormGroupsMap.keySet();
                    for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
                        groupId = (String) iterator.next();
                        nonSelectedFormGroupsMap.remove(groupId);
                    }

                    cbGroups.setLeftValues(nonSelectedFormGroupsMap);

                    cbGroups.setRightValues(dao.selectFormGroup(getFormId()));
                }

                if (FormConstants.FORM_NO_SAVE_DB.equals(formDO.getSaveDb())) {
                    noDB.setChecked(true);
                    yesDB.setChecked(false);
                }
                else {
                    noDB.setChecked(false);
                    yesDB.setChecked(true);
                }


                submissionMessage.setValue(formDO.getFormRetMsg());
                emailTo.setValue(formDO.getFormEmails());
                Map map = dao.selectFormUsers(getFormId(),"memo");
                if (map.size() > 0)
                    sbMemoTo.setOptionMap(map);



                sbQueryBy.setOptionMap(dao.selectFormUsers(getFormId(),"query"));

                if(formDO.getFormDateUpdated().equals("1"))  {
                    workFlowYes.setChecked(true);
                    workFlowNo.setChecked(false);
                 //   sbApprover.set
                    sbApprover.setOptionMap(dao.getApproverUsers(getFormId()));
                } else {
                    workFlowNo.setChecked(true);
                    workFlowYes.setChecked(false);
                }

                emailApproveNotification.setChecked(false);
                memoApproveNotification.setChecked(false);
                stk = new StringTokenizer(formDO.getFormApprMethod(),",");
                while (stk.hasMoreTokens()) {
                    token = stk.nextToken();
                    if (FormModule.WORKFLOW_APPROVAL_EMAIL.equals(token))
                        emailApproveNotification.setChecked(true);
                    if (FormModule.WORKFLOW_APPROVAL_MEMO.equals(token))
                        memoApproveNotification.setChecked(true);
                }



                 String[] option = { (String)dao.getFormProperty(getFormId(),"formLink")};
                 sbForms.setSelectedOptions(option);

                 if (formDO.getIsActive().equals("1"))
                     activeCheckbox.setChecked(true);
                else
                     activeCheckbox.setChecked(false);

                FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
                Collection cForms = handler.getAllForms("formDisplayName",true);
                SequencedHashMap sequenceMap = new SequencedHashMap();
                sequenceMap.put("-1", Application.getInstance().getMessage("formWizard.label.addForm.select","Select..."));
                for(Iterator i = cForms.iterator();i.hasNext();) {
                    FormDataObject formsDO =(FormDataObject)i.next();
                    if (!getFormId().equals(formsDO.getFormId())) {
                        sequenceMap.put(formsDO.getFormId(),formsDO.getFormDisplayName());

                    }
                }
                sbForms.setOptionMap(sequenceMap);

                form = Util.getFormElement(getFormId());
                columns = form.getAttributeValue("columns");
                if  (columns == null || columns.equals(""))
                    columns = "2";
                tfTableColumn.setValue(columns);



        } catch (DaoException e) {
            Log.getLog(getClass()).error(e.toString());
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }

    }
    

    public Forward onValidate(Event evt){

        String buttonName = findButtonClicked(evt);
        Forward forward = null;

        if (buttonName != null && submit.getAbsoluteName().equals(buttonName)) {
            forward = addForm(evt.getRequest());
        }

        return forward;
    }

    public Forward addForm(HttpServletRequest request){
        Forward forward=new Forward("");
        User user = getWidgetManager().getUser();
        String userID = user.getId();
        String memo;

        memo = Application.getInstance().getProperty(FormModule.PROPERTY_MEMO);
        FormDataObject formsDO = new FormDataObject();
        formsDO.setFormId(getFormId());
        formsDO.setFormDisplayName(formNameLabel.getText());
        formsDO.setFormHeader(request.getParameter(formHeader.getAbsoluteName()));
        formsDO.setFormDateCreated(new Date());
        formsDO.setFormUpdatedBy(userID);
        formsDO.setFormRetMsg(request.getParameter(submissionMessage.getAbsoluteName()));
        formsDO.setFormEmails(request.getParameter(emailTo.getAbsoluteName()));
        if(publicFormAccess.isChecked()){
            formsDO.setFormInternalFlag("0");
            formsDO.setFormPublicFlag("0");
        } else {
            formsDO.setFormInternalFlag("1");
            if(privateFormUserAccess.isChecked())
                formsDO.setFormPublicFlag("1");
            if(publicFormUserAccess.isChecked())
                formsDO.setFormPublicFlag("0");
            if(groupFormUserAccess.isChecked())
                formsDO.setFormPublicFlag("2");
        }
        formsDO.setIsActive("1");
        if (yesDB.isChecked())
            formsDO.setSaveDb(FormConstants.FORM_SAVE_DB);
        else
            formsDO.setSaveDb(FormConstants.FORM_NO_SAVE_DB);

        if(noDB.isChecked() && !workFlowYes.isChecked() && !workFlowNo.isChecked())
            formsDO.setFormDateUpdated("0");
        else {
            if(workFlowYes.isChecked())
                formsDO.setFormDateUpdated("1");
            else if(workFlowNo.isChecked())
                formsDO.setFormDateUpdated("0");
        }

        if ("false".equals(memo)) {
            emailApproveNotification.setChecked(true);
            memoApproveNotification.setChecked(false);
        }

        if(memoApproveNotification.isChecked())
            formsDO.setFormApprMethod(FormModule.WORKFLOW_APPROVAL_MEMO);
        if(emailApproveNotification.isChecked())
            formsDO.setFormApprMethod(formsDO.getFormApprMethod() + "," + FormModule.WORKFLOW_APPROVAL_EMAIL);

        if(privateFormUserAccess.isChecked() && sbAccessUsers.getOptionMap().size()>0){
            formsDO.setAccessUserIDs(sbAccessUsers.getOptionMap());
        } else if(groupFormUserAccess.isChecked() && cbGroups.getRightValues().size()>0){
            formsDO.setAccessGroupIDs(cbGroups.getRightValues());
        }
        if(sbQueryBy.getOptionMap().size()>0)
            formsDO.setQueryAccessUserIDs(sbQueryBy.getOptionMap());
        if(sbMemoTo.getOptionMap().size()>0)
            formsDO.setMemoUserIDs(sbMemoTo.getOptionMap());

        Collection cSelectecd = (Collection)sbForms.getValue();
        String selected = (String)cSelectecd.iterator().next();
        if(!selected.equals("-1"))
            formsDO.setFormLink(selected);
        if(workFlowYes.isChecked() && yesDB.isChecked() && sbApprover.getSelectedOptions().size()>0)
            formsDO.setApproverIDs(sbApprover.getSelectedOptions());

        if (activeCheckbox.isChecked())
            formsDO.setIsActive(FormModule.FORM_ACTIVE);
        else
            formsDO.setIsActive(FormModule.FORM_INACTIVE);

        formsDO.setTableColumn(tfTableColumn.getValue().toString());

        FormModule handler = (FormModule)Application.getInstance().getModule(FormModule.class);
        try{
            handler.editForm(userID,formsDO);            
            forward.setName("formEdited");
        }
        catch(FormException e){
            Log.getLog(getClass()).error(e.getMessage(),e);
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



    public Label getFormNameLabel() {
        return formNameLabel;
    }

    
    public String getDefaultTemplate() {
        return "formwizard/editForm";
    }

    public CheckBox getActiveCheckbox() {
        return activeCheckbox;
    }
}
