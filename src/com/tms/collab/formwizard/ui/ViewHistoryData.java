package com.tms.collab.formwizard.ui;

import kacang.model.DefaultDataObject;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Attr;
import com.tms.collab.formwizard.model.FormException;
import com.tms.collab.formwizard.model.FormDocumentException;

public class ViewHistoryData extends ResubmitFormData {
     public void setTableGridValue(org.w3c.dom.Document w3cDocument,DefaultDataObject ddo,NamedNodeMap attributes) {
        Attr attribute = w3cDocument.createAttribute("value");
        attribute.setValue(nullToEmpty(String.valueOf(ddo.getProperty(attributes.getNamedItem("name").getNodeValue()))));
        attributes.setNamedItem(attribute);

        attribute = w3cDocument.createAttribute("editButtonVisible");
        attribute.setValue("false");
        attributes.setNamedItem(attribute);
    }

    public void initViewField(DefaultDataObject ddo) throws FormException, FormDocumentException {
        super.initViewField(ddo);
        btPrint.setHidden(false);
    }


}
