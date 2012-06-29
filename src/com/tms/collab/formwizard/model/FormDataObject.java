package com.tms.collab.formwizard.model;

import kacang.model.DefaultDataObject;

import java.util.Date;
import java.util.Map;




public class FormDataObject extends DefaultDataObject {

    private String formId = "";
    private String formName = "";
    private Date formDateCreated =new Date();
    private String formHeader = "";
    private String formRetMsg = "";
    private String formEmails = "";
    private String formInternalFlag = "";
    private String formPublicFlag = "";
    private String isActive = "1";
    private String isPending = "1";
    private String formDateUpdated = "";
    private String formUpdatedBy = "";
    private String formApprMethod = "";
    private String formDisplayName = "";
    private String formUpdatedByName ="";
    private String saveDb = "";
    private String tableColumn = "2";


    // Access
    private Map accessGroupIDs;
    private Map accessUserIDs;
    private Map queryAccessUserIDs;

    //Notification
    private Map memoUserIDs;

    //Form properties
    private String submitContinue="";
    private String formLink="";

    //Form WorkFlow
    private Map approverIDs;


    public Date getFormDateCreated() {
        return formDateCreated;
    }

    public void setFormDateCreated(Date formDateCreated) {
        this.formDateCreated = formDateCreated;
    }

    public String getFormHeader() {
        return formHeader;
    }

    public void setFormHeader(String formHeader) {
        this.formHeader = formHeader;
    }

    public String getFormRetMsg() {
        return formRetMsg;
    }

    public void setFormRetMsg(String formRetMsg) {
        this.formRetMsg = formRetMsg;
    }

    public String getFormEmails() {
        return formEmails;
    }

    public void setFormEmails(String formEmails) {
        this.formEmails = formEmails;
    }

    public String getFormInternalFlag() {
        return formInternalFlag;
    }

    public void setFormInternalFlag(String formInternalFlag) {
        this.formInternalFlag = formInternalFlag;
    }


    public String getFormPublicFlag() {
        return formPublicFlag;
    }

    public void setFormPublicFlag(String formPublicFlag) {
        this.formPublicFlag = formPublicFlag;
    }

    public String getFormDateUpdated() {
        return formDateUpdated;
    }

    public void setFormDateUpdated(String formDateUpdated) {
        this.formDateUpdated = formDateUpdated;
    }



    public String getFormUpdatedBy() {
        return formUpdatedBy;
    }

    public void setFormUpdatedBy(String formUpdatedBy) {
        this.formUpdatedBy = formUpdatedBy;
    }

    public String getFormApprMethod() {
        return formApprMethod;
    }

    public void setFormApprMethod(String formApprMethod) {
        this.formApprMethod = formApprMethod;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }


    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsPending() {
        return isPending;
    }

    public void setIsPending(String isPending) {
        this.isPending = isPending;
    }

    public Map getAccessGroupIDs() {
        return accessGroupIDs;
    }

    public void setAccessGroupIDs(Map accessGroupIDs) {
        this.accessGroupIDs = accessGroupIDs;
    }

    public Map getAccessUserIDs() {
        return accessUserIDs;
    }

    public void setAccessUserIDs(Map accessUserIDs) {
        this.accessUserIDs = accessUserIDs;
    }

    public Map getQueryAccessUserIDs() {
        return queryAccessUserIDs;
    }

    public void setQueryAccessUserIDs(Map queryAccessUserIDs) {
        this.queryAccessUserIDs = queryAccessUserIDs;
    }

    public Map getMemoUserIDs() {
        return memoUserIDs;
    }

    public void setMemoUserIDs(Map memoUserIDs) {
        this.memoUserIDs = memoUserIDs;
    }

    public String getSubmitContinue() {
        return submitContinue;
    }

    public void setSubmitContinue(String submitContinue) {
        this.submitContinue = submitContinue;
    }

    public String getFormLink() {
        return formLink;
    }

    public void setFormLink(String formLink) {
        this.formLink = formLink;
    }

    public Map getApproverIDs() {
        return approverIDs;
    }

    public void setApproverIDs(Map approverIDs) {
        this.approverIDs = approverIDs;
    }

	public void setFormDisplayName(String formTableName) {
		this.formDisplayName = formTableName;
	}

	public String getFormDisplayName() {
		return formDisplayName;
	}

    public String getFormUpdatedByName() {
        return formUpdatedByName;
    }

    public void setFormUpdatedByName(String formUpdatedByName) {
        this.formUpdatedByName = formUpdatedByName;
    }

    public String getSaveDb() {
        return saveDb;
    }

    public void setSaveDb(String saveDb) {
        this.saveDb = saveDb;
    }

    public String getTableColumn() {
        return tableColumn;
    }

    public void setTableColumn(String tableColumn) {
        this.tableColumn = tableColumn;
    }
}
