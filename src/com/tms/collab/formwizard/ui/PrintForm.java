package com.tms.collab.formwizard.ui;

import kacang.model.DefaultDataObject;
import kacang.Application;
import kacang.services.security.*;
import kacang.services.security.SecurityException;
import kacang.stdui.Label;
import kacang.util.Log;
import com.tms.collab.formwizard.model.*;
import com.tms.collab.formwizard.xmlwidget.*;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Attr;

import java.util.Collection;
import java.text.ParseException;
import java.sql.Timestamp;

public class PrintForm extends ViewHistoryData {
	public static final String PRINT_LABEL_TEMPLATE = "printLabel";

    private String showHidden ;
    private Label lblPostedByLabel;

    public void initViewField(DefaultDataObject ddo) throws FormException, FormDocumentException {
        SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
        User user = null;
        String date = "", name = "";
        try {
            user = service.getUser((String)ddo.getProperty("userId"));
        }
        catch (SecurityException e) {
            Log.getLog(getClass()).error("Error locating user - userId:" + ddo.getProperty("userId"),e);
        }
        try {
            date =  Util.parseFormDataDate( ((Timestamp) ddo.getProperty("datePosted")).toString());
        }

        catch (ParseException e) {
            Log.getLog(getClass()).error("Error parsing date - " + date ,e);
        }

        if (user != null)
            name = user.getName();


        super.initViewField(ddo);

        lblPostedByLabel = new Label("postedByLabel");
        lblPostedByLabel.setText("<p><span class=\"footer\">&nbsp;&nbsp;" + Application.getInstance().getMessage("general.label.submittedBy") +  ": " + name + " (" + date + ")</span></p>");
        addChild(lblPostedByLabel);

        cancelButton.setHidden(true);
        btPrint.setHidden(true);


    }

    public DefaultDataObject getSubmittedData() {
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        Collection data = null;
        DefaultDataObject ddo = null;

        try {
            data = module.getSubmittedFormData(getId(), getTableName(), "userId,datePosted");
            if (data.iterator().hasNext())
                ddo = (DefaultDataObject) data.iterator().next();

        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        return ddo;

    }


     public void parseViewElement(org.w3c.dom.Document w3cDocument, Node node, DefaultDataObject ddo, String templateNodeName)
            throws FormDaoException, FormDocumentException, FormException {
        String nodeName = null;
        NamedNodeMap attributes = null;
        NodeList nodeList = node.getChildNodes();


        for (int i = 0; i < nodeList.getLength(); i++) {
            attributes = nodeList.item(i).getAttributes();
            nodeName = nodeList.item(i).getNodeName();

            if ("0".equals(getShowHidden())
                && attributes.getNamedItem("hidden") != null && attributes.getNamedItem("hidden").getNodeValue().equals("1"))
                continue;

            if (nodeList.item(i).getNodeName().equals(TemplateElement.ELEMENT_NAME) ) {
                setTemplateWidget(nodeList.item(i), attributes, w3cDocument);
                parseViewElement(w3cDocument, nodeList.item(i), ddo, attributes.getNamedItem("name").getNodeValue());
            }

            if (nodeList.item(i).getNodeName().equals(FormElement.ELEMENT_NAME) ) {
                parseViewElement(w3cDocument, nodeList.item(i), ddo, templateNodeName);
            }

            if (nodeList.item(i).getNodeName().equals(LabelElement.ELEMENT_NAME)) {
                setLabelWidget(attributes);
            }
            else if (nodeName.equals(TextFieldElement.ELEMENT_NAME)
                     || nodeName.equals(TextBoxElement.ELEMENT_NAME)
                     || nodeName.equals(ButtonGroupElement.ELEMENT_NAME)
                     || nodeName.equals(SelectElement.ELEMENT_NAME)) {
                setValueLabel(w3cDocument, nodeList.item(i), node, attributes, ddo, templateNodeName);
            }
            else if (nodeName.equals(DateFieldElement.ELEMENT_NAME)) {
                setDateValueLabel(w3cDocument, nodeList.item(i), node, attributes, ddo, templateNodeName);
            }
            else if (attributes.getNamedItem("hidden") != null
                     && attributes.getNamedItem("hidden").getNodeValue().equals("0")
                     && nodeName.equals(FileUploadElement.ELEMENT_NAME)) {
                if (attributes.getNamedItem("created") == null) {
                    setFileObject(node, nodeList.item(i), attributes, w3cDocument, ddo, templateNodeName);
                }
            }
            else if (nodeName.equals(TableGridElement.ELEMENT_NAME)) {
                setTableGridValue(ddo,w3cDocument,attributes, templateNodeName);
            }

            if ("1".equals(getShowHidden()))
                setHidden(attributes);
        }

    }

	protected void setValueLabel(org.w3c.dom.Document w3cDocument, Node childNode, Node node, NamedNodeMap attributes, DefaultDataObject ddo, String name, Object value)
	{
        org.w3c.dom.Element element = w3cDocument.createElement(LabelElement.ELEMENT_NAME);
        NamedNodeMap elementAttributes = element.getAttributes();

        Attr attribute = w3cDocument.createAttribute("text");
        attribute.setValue(Util.nullToEmpty(value));
        elementAttributes.setNamedItem(attribute);

		Attr template = w3cDocument.createAttribute("template");
        template.setValue(PRINT_LABEL_TEMPLATE);
        elementAttributes.setNamedItem(template);

        attribute = w3cDocument.createAttribute("name");
        attribute.setValue(name);
        elementAttributes.setNamedItem(attribute);

        if (attributes.getNamedItem("colspan") != null) {
            attribute = w3cDocument.createAttribute("colspan");
            attribute.setValue(attributes.getNamedItem("colspan").getNodeValue());
            elementAttributes.setNamedItem(attribute);
        }

        if (attributes.getNamedItem("rowspan") != null) {
            attribute = w3cDocument.createAttribute("rowspan");
            attribute.setValue(attributes.getNamedItem("rowspan").getNodeValue());
            elementAttributes.setNamedItem(attribute);
        }

        if (attributes.getNamedItem("align") != null) {

            attribute = w3cDocument.createAttribute("align");
            attribute.setValue(attributes.getNamedItem("align").getNodeValue());
            elementAttributes.setNamedItem(attribute);
        }

        if (attributes.getNamedItem("valign") != null) {

            attribute = w3cDocument.createAttribute("valign");
            attribute.setValue(attributes.getNamedItem("valign").getNodeValue());
            elementAttributes.setNamedItem(attribute);
        }


        node.replaceChild(element, childNode);
    }

    public String getShowHidden() {
        return showHidden;
    }

    public void setShowHidden(String showHidden) {
        this.showHidden = showHidden;
    }

    public Label getLblPostedByLabel() {
        return lblPostedByLabel;
    }

    public void setLblPostedByLabel(Label lblPostedByLabel) {
        this.lblPostedByLabel = lblPostedByLabel;
    }
}
