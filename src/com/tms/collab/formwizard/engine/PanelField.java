package com.tms.collab.formwizard.engine;

import java.util.List;

public class PanelField extends Field {
    private List buttonList;
    private FormLayout formLayout;


    public List getButtonList() {
        return buttonList;
    }

    public void setButtonList(List buttonList) {
        this.buttonList = buttonList;
    }

    public FormLayout getFormLayout() {
        return formLayout;
    }

    public void setFormLayout(FormLayout formLayout) {
        this.formLayout = formLayout;
    }

    
}
