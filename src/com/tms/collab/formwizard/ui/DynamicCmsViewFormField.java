package com.tms.collab.formwizard.ui;

import com.tms.collab.formwizard.engine.PanelField;
import com.tms.collab.formwizard.engine.FormLayout;

public class DynamicCmsViewFormField extends DynamicViewFormField {
    public void setPanelField(PanelField panel) {
           FormLayout formLayout = panel.getFormLayout();

           if (formLayout != null) {
               formLayout.setWidth("100%");
               parseLayout(formLayout.getFieldList());
           }

       }

}
