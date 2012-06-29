package com.tms.collab.formwizard.model;

import kacang.model.DefaultDataObject;


public class FormFieldDataObject extends DefaultDataObject {
    private String formId="";
    private String name = "";
    private String formFieldId="";
    private String dataType = "";
    private String fieldType = "";
    private String fieldSize = "";
    private String requiredFlag = "0";
    private String hiddenFlag = "0";
    private int order;
    private String defaultValue="0";
    private String maxRows = "";
    private String maxCols = "";
    private String options = "";
    private String maxLength = "";
    private String colspan = "1";
    private String rowspan = "1";
    private String labelColspan = "1";
    private String align = "left";
    private String valign = "top";

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormFieldId() {
        return formFieldId;
    }

    public void setFormFieldId(String formFieldId) {
        this.formFieldId = formFieldId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldSize() {
        return fieldSize;
    }

    public void setFieldSize(String fieldSize) {
        this.fieldSize = fieldSize;
    }

    public String getRequiredFlag() {
        return requiredFlag;
    }

    public void setRequiredFlag(String requiredFlag) {
        this.requiredFlag = requiredFlag;
    }

    public String getHiddenFlag() {
        return hiddenFlag;
    }

    public void setHiddenFlag(String hiddenFlag) {
        this.hiddenFlag = hiddenFlag;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
	
	public String getMaxCols() {
		return maxCols;
	}

	
	public void setMaxCols(String maxCols) {
		this.maxCols = maxCols;
	}

	public String getMaxRows() {
		return maxRows;
	}


	public void setMaxRows(String maxRows) {
		this.maxRows = maxRows;
	}


	public String getOptions() {
		return options;
	}


	public void setOptions(String options) {
		this.options = options;
	}

    public String getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    public String getColspan() {
        return colspan;
    }

    public void setColspan(String colspan) {
        this.colspan = colspan;
    }

    public String getRowspan() {
        return rowspan;
    }

    public void setRowspan(String rowspan) {
        this.rowspan = rowspan;
    }

    public String getLabelColspan() {
        return labelColspan;
    }

    public void setLabelColspan(String labelColspan) {
        this.labelColspan = labelColspan;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getValign() {
        return valign;
    }

    public void setValign(String valign) {
        this.valign = valign;
    }

}