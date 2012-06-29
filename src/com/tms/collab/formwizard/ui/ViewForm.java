package com.tms.collab.formwizard.ui;

import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;

import com.tms.collab.formwizard.model.*;
import com.tms.collab.formwizard.xmlwidget.LabelElement;
import com.tms.collab.formwizard.xmlwidget.TemplateElement;
import com.tms.collab.formwizard.xmlwidget.FormElement;


import org.jdom.JDOMException;
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.xpath.XPath;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Attr;


import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Iterator;


public class ViewForm extends Form {
    private String formId;
    protected Label formHeader;
    protected Label label;
    protected Label lbMessage;   

    public ViewForm() {
    }

    public ViewForm(String s) {
        super(s);


    }

    public void onRequest(Event event) {
        initForm();
    }

    public void initForm() {
        removeChildren();

        if (getFormId() != null && !getFormId().equals("")) {
            FormModule handler = (FormModule) Application.getInstance().getModule(FormModule.class);
            FormDao dao = (FormDao) handler.getDao();
            try {

                label = new Label("label");
                label.setText("");
                formHeader = new Label("formHeader");
                Collection form = dao.selectForm(getFormId());
                if (form.iterator().hasNext()) {
                    FormDataObject formDO = (FormDataObject) form.iterator().next();
                    formHeader.setText(formDO.getFormHeader() + "<br><br>");
                }
                addChild(label);
                addChild(formHeader);
                setMethod("post");
            }
            catch (FormDaoException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);
            }
        }
    }

    public void removeSubmitLbl(Document jDomDocument) throws FormDocumentException {
        try {
            Element element = (Element) XPath.selectSingleNode(jDomDocument,"/form/label[@name='submitLbl']");
            if (element != null)
                jDomDocument.getRootElement().removeContent(element);


        }
        catch (JDOMException e) {
            throw new FormDocumentException("Error selecting /form/label[@name='submitLbl'] nodes",e);
        }


    }

    public void appendHiddenField(Node node, org.w3c.dom.Document w3cDocument) {
        NamedNodeMap attributes = node.getAttributes();
        NodeList nodeList = node.getChildNodes();



        org.w3c.dom.Element hiddenElement = w3cDocument.createElement("hidden");
        hiddenElement.setAttribute("name","formid");
        hiddenElement.setAttribute("value",getFormId());
        node.insertBefore(hiddenElement,nodeList.item(0));



        hiddenElement = w3cDocument.createElement("hidden");
        hiddenElement.setAttribute("name","formName");
        hiddenElement.setAttribute("value",attributes.getNamedItem("name").getNodeValue());
        node.insertBefore(hiddenElement,nodeList.item(0));

    }

    public void parseElement(Node node, org.w3c.dom.Document w3cDocument) throws FormDaoException, FormDocumentException, FormException {
        NamedNodeMap attributes;
        NodeList nodeList = node.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            attributes = nodeList.item(i).getAttributes();



            if (nodeList.item(i).getNodeName().equals(TemplateElement.ELEMENT_NAME)) {
                setTemplateWidget(nodeList.item(i), attributes, w3cDocument);
                parseElement(nodeList.item(i), w3cDocument);
            }

            if (nodeList.item(i).getNodeName().equals(FormElement.ELEMENT_NAME)) {
                parseElement(nodeList.item(i), w3cDocument);
            }





            if (nodeList.item(i).getNodeName().equals("panel"))  {
                setPanelWidget(nodeList.item(i),w3cDocument,attributes,node);
            }

            if (nodeList.item(i).getNodeName().equals(LabelElement.ELEMENT_NAME)) {
                setLabelWidget(attributes);
            }
        }

    }

    //set the submit panel
    public void setPanelWidget(Node node, org.w3c.dom.Document w3cDocument,NamedNodeMap attributes, Node motherNode) {
        String column = "2";

       //set the submit and reset button
        NodeList nl = node.getChildNodes();
        Node submitNode = nl.item(0);
        submitNode.getAttributes().getNamedItem("text").setNodeValue(Application.getInstance().getMessage("formWizard.label.viewForm.submit","Submit"));

        Node resetNode = nl.item(1);
        resetNode.getAttributes().getNamedItem("text").setNodeValue(Application.getInstance().getMessage("formWizard.label.viewForm.reset","Reset"));



        //add the "Draft" button
        org.w3c.dom.Element w3cElement = w3cDocument.createElement("button");
        NamedNodeMap buttonAttribute = w3cElement.getAttributes();
        Attr attribute = w3cDocument.createAttribute("name");
        attribute.setValue("draft");
        buttonAttribute.setNamedItem(attribute);

        attribute = w3cDocument.createAttribute("text");
        attribute.setValue(Application.getInstance().getMessage("formWizard.label.","Draft"));
        buttonAttribute.setNamedItem(attribute);
        node.appendChild(w3cElement);

        if (motherNode.getAttributes().getNamedItem("columns") != null)
            column = motherNode.getAttributes().getNamedItem("columns").getNodeValue();
        attribute = w3cDocument.createAttribute("colspan");
        attribute.setValue(column);
        attributes.setNamedItem(attribute);

        attribute = w3cDocument.createAttribute("align");
        attribute.setValue("left");
        attributes.setNamedItem(attribute);

        attribute = w3cDocument.createAttribute("valign");
        attribute.setValue("bottom");
        attributes.setNamedItem(attribute);




    }

    public void setTemplateWidget(Node node, NamedNodeMap attributes, org.w3c.dom.Document doc)
            throws FormDaoException, FormException, FormDocumentException {
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        String templateId = attributes.getNamedItem("templateId").getNodeValue();
        InputStream stream = module.getTemplatePreviewXml(templateId);
        org.w3c.dom.Document w3cDocument;
        org.w3c.dom.Element element, childElement, formElement;
        NodeList nl, childNodeList;
        Node formNode, attributeNode;
        NamedNodeMap templateAttributes, childAttributes;
        try {
            w3cDocument = Util.buildDOMDocument(stream);
            formNode = w3cDocument.getDocumentElement();
            nl = formNode.getChildNodes();

            formElement = doc.createElement(formNode.getNodeName());
            templateAttributes = formNode.getAttributes();
            setFormNodeWidth(doc,formElement);
            for (int j=0; j < templateAttributes.getLength();j++) {
                attributeNode = templateAttributes.item(j);
                if (!attributeNode.getNodeName().equals("template")) {
                    formElement.setAttribute(attributeNode.getNodeName(), attributeNode.getNodeValue());
                }
            }
            node.appendChild(formElement);


            for (int i = 0; i < nl.getLength(); i++) {
                if (!nl.item(i).getNodeName().equals(FormElement.ELEMENT_NAME)) {
                    element = doc.createElement(nl.item(i).getNodeName());
                    templateAttributes = nl.item(i).getAttributes();

                    //add node attribute
                    for (int j = 0; j < templateAttributes.getLength(); j++) {
                        attributeNode = templateAttributes.item(j);
                        if (!attributeNode.getNodeName().equals("template")) {
                            element.setAttribute(attributeNode.getNodeName(), attributeNode.getNodeValue());
                        }
                    }

                    //add child node 1 level
                    childNodeList = nl.item(i).getChildNodes();
                    if (childNodeList != null && childNodeList.getLength() > 0) {
                        for (int j = 0; j < childNodeList.getLength(); j++) {
                            childElement = doc.createElement(childNodeList.item(j).getNodeName());
                            childAttributes = childNodeList.item(j).getAttributes();

                            for (int k = 0; k < childAttributes.getLength(); k++) {
                                attributeNode = childAttributes.item(k);
                                childElement.setAttribute(attributeNode.getNodeName(), attributeNode.getNodeValue());
                            }

                            //add create subnode to the parent
                            element.appendChild(childElement);
                        }
                    }


                    formElement.appendChild(element);
                }

            }


        }

        finally {
            try {
                if (stream != null)
                    stream.close();
            }
            catch (IOException e) {
            }
        }
    }

    public void setFormNodeWidth(org.w3c.dom.Document doc, Node node) {
         Attr attribute = doc.createAttribute("width");
         attribute.setValue("100%");
         node.getAttributes().setNamedItem(attribute);
    }

    protected void setLabelWidget(NamedNodeMap attributes) {
        //set the required field to bold and append with an asterisk
        if (attributes.getNamedItem("require") != null && "1".equals(attributes.getNamedItem("require").getNodeValue())) {
            attributes.getNamedItem("text").setNodeValue("<b>" + attributes.getNamedItem("text").getNodeValue() + "</b> *");
        }
        //set the hidden field to bold and italic
        else if (attributes.getNamedItem("hidden") != null && "1".equals(attributes.getNamedItem("hidden").getNodeValue())) {
            attributes.getNamedItem("text").setNodeValue("<b><i>" + attributes.getNamedItem("text").getNodeValue() + "</i></b>");
        }
        //set the normal field to bold
        else if (attributes.getNamedItem("type") == null && attributes.getNamedItem("text") != null) {
            attributes.getNamedItem("text").setNodeValue("<b>" + attributes.getNamedItem("text").getNodeValue() + "</b>");
        }





    }

    public void renameTemplateNode(Document doc) throws FormException {
           try {
               List templateList = XPath.selectNodes(doc.getRootElement(),"/form/"+ TemplateElement.ELEMENT_NAME);
               Element e;
               for (Iterator iterator = templateList.iterator(); iterator.hasNext();) {

                   e = (Element) iterator.next();
                   e.setName("panel");
                   e.setAttribute("width","100%");
                /*   e.setAttribute("columns","2");
                   e.setAttribute("template",FormConstants.FORM_PANEL);*/
               }
           }
           catch (JDOMException e) {
               throw new FormException("Error selecting nodes",e);
           }
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormHeader(Label formHeader) {
        this.formHeader = formHeader;
    }

    public Label getFormHeader() {
        return formHeader;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Label getLabel() {
        return label;
    }



    public Label getLbMessage() {
        return lbMessage;
    }

    public void setLbMessage(Label lbMessage) {
        this.lbMessage = lbMessage;
    }
}
