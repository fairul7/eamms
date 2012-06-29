package com.tms.collab.formwizard.xmlwidget;

import com.tms.collab.formwizard.model.FormFieldDataObject;
import com.tms.collab.formwizard.model.FormException;
import com.tms.collab.formwizard.model.FormConstants;

public class TextBlockElement extends LabelElement {
    public TextBlockElement(FormFieldDataObject ffdo) throws FormException {
        this(ffdo.getFormFieldId(), ffdo.getName(), ffdo.getHiddenFlag(), ffdo.getRequiredFlag(), ffdo.getLabelColspan(),
                 ffdo.getRowspan(),ffdo.getAlign(),ffdo.getValign());
    }

    public TextBlockElement(String name, String text, String hiddenField, String requiredField,
                            String colspan, String rowspan, String align , String valign) throws FormException {
        super(name,text,hiddenField,requiredField,colspan,rowspan, align, valign,FormConstants.FIELD_TEXT_BLOCK_TYPE);
    }

     public void setAttribute(String name, String text, String hiddenField, String requiredField,
                              String colspan, String rowspan, String align, String valign) {
        if (colspan == null)
            colspan = "2";
        super.setAttribute(name,text,hiddenField,requiredField,colspan,rowspan, align, valign,FormConstants.FIELD_TEXT_BLOCK_TYPE);        
    }


     public void removeMetaData() {
        removeAttribute("template");
    }

    public void removeMetaData(String attrName) {
           removeAttribute(attrName);
    }

    

}
