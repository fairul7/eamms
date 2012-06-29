package com.tms.collab.formwizard.ui;


import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;


import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Label;

import kacang.model.DefaultDataObject;
import kacang.model.DaoException;
import kacang.runtime.config.WidgetParser;
import kacang.ui.Widget;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.util.Log;


import com.tms.collab.formwizard.model.*;
import com.tms.collab.formwizard.xmlwidget.*;
import com.tms.util.FormatUtil;

public class ResubmitFormData extends EditFormData {
   private String status;
    protected Button cancelButton;
    protected Button btPrint;


    public void initForm() {
        setData();
        if (FormModule.WORKFLOW_REJECT.equals(getStatus()))
            initViewForm();
//            initResubmitForm();
        else
            initViewForm();
        label.setText("");
    }

    public void initResubmitForm(){
		setMethod("POST");
		removeChildren();
        FormModule handler = (FormModule) Application.getInstance().getModule(FormModule.class);
        FormDao dao = (FormDao) handler.getDao();
		label = new Label("label");
		label.setText("<i>Italic - Hidden Field</i><br><br>");

        formHeader = new Label("formHeader");
        Collection form;
        try {
            form = dao.selectForm(getFormId());
            if (form.iterator().hasNext()) {
                FormDataObject formDO = (FormDataObject) form.iterator().next();
                formHeader.setText(formDO.getFormHeader() + "<br><br>");
            }
        }
        catch (FormDaoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

		addChild(label);
        addChild(formHeader);


        lbMessage = new Label("message");
        lbMessage.setText("");
        addChild(lbMessage);

		Collection data;
		DefaultDataObject ddo = null;

		try {

			data = handler.getSubmittedFormData(getId(),getTableName(),getColumnStr());
			if (data.iterator().hasNext())
				ddo = (DefaultDataObject) data.iterator().next();
			initField(ddo);

		}
		catch (FormException e) {
			Log.getLog(getClass()).error(e.toString());
		}
        catch (FormDocumentException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }



	}


    public void initViewForm() {
        setMethod("POST");
        removeChildren();
        label = new Label("label");
        label.setText("");
        addChild(label);
        FormModule handler = (FormModule) Application.getInstance().getModule(FormModule.class);
        Collection data;
        DefaultDataObject ddo = null;

        try {

            data = handler.getSubmittedFormData(getId(), getTableName(), getColumnStr());
            if (data.iterator().hasNext())
                ddo = (DefaultDataObject) data.iterator().next();
            initViewField(ddo);

        }
        catch (FormException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }        
        catch (FormDocumentException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }

    }

    public void initViewField(DefaultDataObject ddo) throws FormException, FormDocumentException {
        InputStream xmlForm = null;
        org.w3c.dom.Document w3cDocument = null;
        Node node = null;
        Widget[] widgets = null;
        WidgetParser parser = null;
        Document jDomDocument = null;


        FormModule handler = (FormModule) Application.getInstance().getModule(FormModule.class);
        parser = new WidgetParser();
        try {

            xmlForm = handler.getFormXML(getFormId());
            w3cDocument = Util.buildDOMDocument(xmlForm);
            node = w3cDocument.getDocumentElement();
            parseViewElement(w3cDocument, node, ddo, "");
            jDomDocument = Util.DOMtoJDOM(node.getOwnerDocument());
            removePanel(jDomDocument);
            removeListener(jDomDocument);
            renameTemplateNode(jDomDocument);

            widgets = parser.parseConfig(Util.JDOMtoDOM(jDomDocument));

            addChild(widgets[0]);

            btPrint = new Button("printButton");
            btPrint.setText("Print");
            btPrint.setHidden(true);
            addChild(btPrint);

            cancelButton = new Button("cancelButton");
            cancelButton.setText("Cancel");
            addChild(cancelButton);
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        finally {
            try {
                if (xmlForm != null)
                    xmlForm.close();
            }
            catch (IOException e) {
            }

        }

    }

    public void setData() {
        FormModule handler = (FormModule) Application.getInstance().getModule(FormModule.class);
        FormWorkFlowDataObject fwfdo;
        try {
            fwfdo = handler.getFormsWorkFlow(getId());
            if (fwfdo != null) {
                setFormId(fwfdo.getFormId());
                setStatus(fwfdo.getStatus());
            }
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }

    }

    public void parseViewElement(org.w3c.dom.Document w3cDocument, Node node, DefaultDataObject ddo, String templateNodeName)
            throws FormDaoException, FormDocumentException, FormException {
        String nodeName = null;
        NamedNodeMap attributes = null;
        NodeList nodeList = node.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            attributes = nodeList.item(i).getAttributes();
            nodeName = nodeList.item(i).getNodeName();

            if (attributes.getNamedItem("hidden") != null && attributes.getNamedItem("hidden").getNodeValue().equals("1"))
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
        }

    }

     public void setTableGridValue(DefaultDataObject ddo,org.w3c.dom.Document w3cDocument,NamedNodeMap attributes,String templateNodeName) {
        super.setTableGridValue(w3cDocument,ddo,attributes,templateNodeName);

        Attr attribute = w3cDocument.createAttribute("editButtonVisible");
        attribute.setValue("false");
        attributes.setNamedItem(attribute);
    }



    protected void setValueLabel(org.w3c.dom.Document w3cDocument, Node childNode, Node node, NamedNodeMap attributes,
                                 DefaultDataObject ddo, String templateNodeName) {
        String name = templateNodeName + attributes.getNamedItem("name").getNodeValue();
        Object value = ddo.getProperty(name);

        setValueLabel(w3cDocument, childNode, node, attributes, ddo, name, value);
    }

    protected void setValueLabel(org.w3c.dom.Document w3cDocument, Node childNode, Node node, NamedNodeMap attributes,
                                 DefaultDataObject ddo, String name, Object value) {
        org.w3c.dom.Element element = w3cDocument.createElement(LabelElement.ELEMENT_NAME);
        NamedNodeMap elementAttributes = element.getAttributes();

        Attr attribute = w3cDocument.createAttribute("text");
        attribute.setValue(Util.nullToEmpty(value));
        elementAttributes.setNamedItem(attribute);

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

    public void setDateValueLabel(org.w3c.dom.Document w3cDocument, Node childNode, Node node, NamedNodeMap attributes,
                                  DefaultDataObject ddo, String templateNodeName) throws FormException {

        String name = templateNodeName + attributes.getNamedItem("name").getNodeValue();
        String value = "";


        SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");        
        SimpleDateFormat outDateFormat = new SimpleDateFormat(FormatUtil.getInstance().getLongDateFormat());

        Date date = null;


        try {
            if (ddo.getProperty(name) != null) {
                date = inDateFormat.parse(String.valueOf(ddo.getProperty(name)));
                value = outDateFormat.format(date);
            }

        }
        catch (ParseException e) {

            try {
                inDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                date = inDateFormat.parse(String.valueOf(ddo.getProperty(name)));
                value = outDateFormat.format(date);
            }
            catch(Exception e1) {
                Log.getLog(getClass()).error("Error parsing the date", e);
            }
            //Log.getLog(getClass()).error("Error parsing the date", e);
        }

        setValueLabel(w3cDocument, childNode, node, attributes, ddo, name, value);


    }

    protected void setFileObject(Node node, Node removedNode, NamedNodeMap attributes,
                                 org.w3c.dom.Document doc, DefaultDataObject ddo, String templateNodeName) {
        String name = templateNodeName + attributes.getNamedItem("name").getNodeValue();
        if (ddo.getProperty(name) != null) {
            org.w3c.dom.Element linkElement = setLinkObject(attributes, doc, ddo, templateNodeName);
            node.replaceChild(linkElement, removedNode);
        }
        else {
            setValueLabel(doc, removedNode, node, attributes, ddo, name, "None");
        }


    }

    public void removePanel(Document jDomDocument) throws FormDocumentException {
        try {
            Element element = (Element) XPath.selectSingleNode(jDomDocument, "/form/panel");
            Element childrenElement = (Element) jDomDocument.getContent(0);
            childrenElement.removeContent(element);


        }
        catch (JDOMException e) {
            throw new FormDocumentException("Error selecting /form/panel nodes", e);
        }
    }

    public void removeListener(Document jDomDocument) throws FormDocumentException {

        try {
            Element element = (Element) XPath.selectSingleNode(jDomDocument, "/form/listener_form");
            Element childrenElement = (Element) jDomDocument.getContent(0);
            childrenElement.removeContent(element);
        }
        catch (JDOMException e) {
            throw new FormDocumentException("Error selecting /form/listener_form nodes", e);
        }

    }

    public Forward onValidate(Event evt) {
        Forward forward = null;
        String buttonName = findButtonClicked(evt);

        forward = new Forward("");
        if (buttonName != null) {
            if (buttonName.equals(cancelButton.getAbsoluteName())) {
                forward = new Forward("cancel");
            }
            else if (buttonName.equals(btPrint.getAbsoluteName())) {
                forward = new Forward("print");
            }

        }


        return forward;

    }


    public void parseElement(org.w3c.dom.Document w3cDocument, Node node, DefaultDataObject ddo, String templateNodeName)
            throws FormException, FormDaoException, FormDocumentException {
        String nodeName = null;
        NamedNodeMap attributes = null;
        NodeList nodeList = node.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            attributes = nodeList.item(i).getAttributes();
            nodeName = nodeList.item(i).getNodeName();

            if (nodeList.item(i).getNodeName().equals(TemplateElement.ELEMENT_NAME)) {
                setTemplateWidget(nodeList.item(i), attributes, w3cDocument);
                parseElement(w3cDocument, nodeList.item(i), ddo, attributes.getNamedItem("name").getNodeValue());
            }

            if (nodeList.item(i).getNodeName().equals(FormElement.ELEMENT_NAME)) {
                parseElement(w3cDocument,nodeList.item(i), ddo, templateNodeName);
            }

            if (nodeList.item(i).getNodeName().equals(LabelElement.ELEMENT_NAME)) {
                setLabelWidget(attributes);
            }

            if (nodeName.equals(TextFieldElement.ELEMENT_NAME)) {
                setValue(attributes, w3cDocument, ddo, templateNodeName);
            }


            if (nodeName.equals(TextBoxElement.ELEMENT_NAME)) {
                setValue(attributes, w3cDocument, ddo, templateNodeName);
            }

            if (nodeName.equals(ButtonGroupElement.ELEMENT_NAME)) {
                setOption(attributes, w3cDocument, ddo, nodeList.item(i), templateNodeName);
            }

            if (nodeName.equals(DateFieldElement.ELEMENT_NAME)) {
                setDateValue(attributes, w3cDocument, ddo, templateNodeName);
            }

            if (nodeName.equals(SelectElement.ELEMENT_NAME)) {
                setSelectedOption(attributes, w3cDocument, ddo, templateNodeName);
            }

            if (nodeName.equals(FileUploadElement.ELEMENT_NAME)) {
                if (attributes.getNamedItem("created") == null)
                    setFileObject(node, nodeList.item(i), nodeList.item(i + 1), attributes, w3cDocument, ddo, templateNodeName);
            }

            if (nodeName.equals(TableGridElement.ELEMENT_NAME)) {
                setTableGridValue(w3cDocument,ddo,attributes, templateNodeName);
            }


            if (nodeName.equals("listener_form")) {
                setIdValue(attributes, w3cDocument);
            }

            if (nodeName.equals("panel")) {
                setResubmitButton(w3cDocument, nodeList.item(i));
                setCancelButton(w3cDocument, nodeList.item(i));
                setPanelWidget(nodeList.item(i), w3cDocument, attributes);
            }
        }

    }

    protected void setResubmitButton(org.w3c.dom.Document doc, Node node) {
        NodeList nodeList = node.getChildNodes();
        NamedNodeMap attributes = null;
        for (int i = 0; i < nodeList.getLength(); i++) {
            attributes = nodeList.item(i).getAttributes();
            if (nodeList.item(i).getNodeName().equals("button")) {
                attributes.getNamedItem("text").setNodeValue("Resubmit");
                attributes.getNamedItem("name").setNodeValue("resubmit");
            }
        }
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}


