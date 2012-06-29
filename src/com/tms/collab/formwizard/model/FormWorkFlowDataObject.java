package com.tms.collab.formwizard.model;

import kacang.model.DefaultDataObject;

import java.util.Date;
import java.util.List;
import java.util.Map;



public class FormWorkFlowDataObject extends DefaultDataObject {
    private String formId;
    private String formName;
    private String formUid;
    private String userId ;
    private String name;
    private int priority = 0;
    private int workflowFlag=0;
    private String approverId;
    private String formApprovalId;
    private Date formApprovalDate;
    private String approverName;
    private String status;
    
	private Map labelMap;
    private Map values;
    private String persistenceFormName;    
    private String formDisplayName;
    private List fieldNameList;
    private Date formSubmissionDate;
    private List fileList;
    
  

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormUid() {
        return formUid;
    }

    public void setFormUid(String formUid) {
        this.formUid = formUid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApproverId() {
        return approverId;
    }

    public void setApproverId(String approverId) {
        this.approverId = approverId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getWorkflowFlag() {
        return workflowFlag;
    }

    public void setWorkflowFlag(int workflowFlag) {
        this.workflowFlag = workflowFlag;
    }

    public String getFormApprovalId() {
        return formApprovalId;
    }

    public void setFormApprovalId(String formApprovalId) {
        this.formApprovalId = formApprovalId;
    }

    public Date getFormApprovalDate() {
        return formApprovalDate;
    }

    public void setFormApprovalDate(Date formApprovalDate) {
        this.formApprovalDate = formApprovalDate;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
    public Map getValues() {
        return values;
    }

    public void setValues(Map values) {
        this.values = values;
    }

    public String getPersistenceFormName() {
        return persistenceFormName;
    }

    public void setPersistenceFormName(String persistenceFormName) {
        this.persistenceFormName = persistenceFormName;
    }

	public void setFormDisplayName(String formDisplayName) {
		this.formDisplayName = formDisplayName;
	}

	public String getFormDisplayName() {
		return formDisplayName;
	}

	public void setLabelMap(Map labelMap) {
		this.labelMap = labelMap;
	}

	public Map getLabelMap() {
		return labelMap;
	}

	public void setFieldNameList(List fieldNameList) {
		this.fieldNameList = fieldNameList;
	}

	public List getFieldNameList() {
		return fieldNameList;
	}

    public Date getFormSubmissionDate() {
        return formSubmissionDate;
    }

    public void setFormSubmissionDate(Date formSubmissionDate) {
        this.formSubmissionDate = formSubmissionDate;
    }

    public List getFileList() {
        return fileList;
    }

    public void setFileList(List fileList) {
        this.fileList = fileList;
    }


}
