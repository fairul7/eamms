package com.tms.collab.formwizard.ui;

import java.io.IOException;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tms.collab.formwizard.model.*;
import com.tms.collab.formwizard.xmlwidget.*;
import com.tms.collab.formwizard.ui.validator.ValidatorFileUpload;
import com.tms.util.FormatUtil;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.runtime.config.WidgetParser;
import kacang.ui.Widget;
import kacang.ui.Event;
import kacang.util.Log;






public class EditFormData extends ViewForm {
	protected String id;
    protected String url;


    public void onRequest(Event event) {
         url = event.getRequest().getScheme() + "://" + event.getRequest().getServerName() + ":"
              + event.getRequest().getServerPort() + "/storage"      ;
        super.onRequest(event);

    }

	public void initForm(){
        super.initForm();
        label.setText("<i>" + Application.getInstance().getMessage("formWizard.form.label.italic","Italic - Hidden Field") + "</i><br><br>");

	}
	
	protected void initField(DefaultDataObject ddo)
            throws FormException, FormDocumentException  {

        InputStream xmlForm = null;
        org.w3c.dom.Document w3cDocument = null;
        Node node = null;
        Widget[] widgets = null;
        WidgetParser parser = null;
        Document jDomDocument = null;


        FormModule handler = (FormModule)Application.getInstance().getModule(FormModule.class);
	    parser = new WidgetParser();
        try {

            xmlForm = handler.getFormXML(getFormId());

            if (xmlForm.available() > 0) {
                w3cDocument = Util.buildDOMDocument(xmlForm);
                node = w3cDocument.getDocumentElement();
                appendHiddenField(node,w3cDocument);
                parseElement(w3cDocument,node, ddo,"");
                jDomDocument = Util.DOMtoJDOM(node.getOwnerDocument());
                renameTemplateNode(jDomDocument);
                removeSubmitLbl(jDomDocument);

                widgets = parser.parseConfig(Util.JDOMtoDOM(jDomDocument));

                addChild(widgets[0]);
            }
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(), e);
        }
        catch (IOException e) {
            Log.getLog(getClass()).error("Error reading the inputstream", e);
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
                parseElement(w3cDocument,nodeList.item(i), ddo, attributes.getNamedItem("name").getNodeValue());
            }

            if (nodeList.item(i).getNodeName().equals(FormElement.ELEMENT_NAME)) {
                parseElement(w3cDocument,nodeList.item(i), ddo, templateNodeName);
            }



            if (nodeList.item(i).getNodeName().equals(LabelElement.ELEMENT_NAME)) {
                setLabelWidget(attributes);
            }

            if (nodeName.equals(TextFieldElement.ELEMENT_NAME)) {
                setValue(attributes, w3cDocument, ddo,templateNodeName);
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
                setTableGridValue(w3cDocument,ddo,attributes,templateNodeName) ;
            }


            if (nodeName.equals("listener_form")) {
                setIdValue(attributes, w3cDocument);
            }

            if (nodeName.equals("panel")) {

                setPrintButton(w3cDocument, nodeList.item(i));
                setCancelButton(w3cDocument, nodeList.item(i));
                setPanelWidget(nodeList.item(i),w3cDocument,attributes);
            }


            setHidden(attributes);

        }

    }

    public void setTableGridValue(org.w3c.dom.Document w3cDocument,DefaultDataObject ddo,NamedNodeMap attributes, String templateNodeName) {
        String propertyName = templateNodeName + attributes.getNamedItem("name").getNodeValue();
        Attr attribute = w3cDocument.createAttribute("value");
        attribute.setValue(nullToEmpty(String.valueOf(ddo.getProperty(propertyName))));
        attributes.setNamedItem(attribute);
    }

    //set the submit panel
    public void setPanelWidget(Node node, org.w3c.dom.Document w3cDocument,NamedNodeMap attributes) {
        Attr attribute = w3cDocument.createAttribute("columns");
        attribute.setValue("4");
        attributes.setNamedItem(attribute);
    }


	protected void setHidden(NamedNodeMap attributes) {
		if (attributes.getNamedItem("hidden") != null)
			attributes.getNamedItem("hidden").setNodeValue("0");							
	}
	
	protected void setValue(NamedNodeMap attributes, org.w3c.dom.Document doc, DefaultDataObject ddo,String templateNodeName) {
        String propertyName = templateNodeName + attributes.getNamedItem("name").getNodeValue();
		Attr attribute = doc.createAttribute("value");
		attribute.setValue(nullToEmpty(String.valueOf(ddo.getProperty(propertyName))));
		attributes.setNamedItem(attribute);
	}
	
	protected void setOption(NamedNodeMap attributes, org.w3c.dom.Document doc, DefaultDataObject ddo, Node node,
                             String templateNodeName) {
        String name =  templateNodeName + attributes.getNamedItem("name").getNodeValue();
		String value = nullToEmpty(String.valueOf(ddo.getProperty(name)));
		StringTokenizer stk = new StringTokenizer(value,",");
		String token,optionNodeValue ;
		NodeList optionNode;
		NamedNodeMap optionAttributes;
		Attr attribute;
		while (stk.hasMoreTokens()) {
			token = stk.nextToken().trim();
			
			optionNode = node.getChildNodes();
												     		
			for (int i = 0; i < optionNode.getLength(); i++) {				     			
				optionAttributes = optionNode.item(i).getAttributes();
				optionNodeValue = optionAttributes.getNamedItem("text").getNodeValue().trim();
				if (token.equals(optionNodeValue)) {
					attribute = doc.createAttribute("checked");
					attribute.setValue("true");
					optionAttributes.setNamedItem(attribute);
				}
			}
		}
	}
	
	protected void setDateValue(NamedNodeMap attributes, org.w3c.dom.Document doc, DefaultDataObject ddo, String templateNodeName)
            throws FormException {
		Attr attribute = doc.createAttribute("value");
        String name = templateNodeName +  attributes.getNamedItem("name").getNodeValue();
		String value = nullToEmpty(String.valueOf(ddo.getProperty(name)));
	    SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(FormatUtil.getInstance().getLongDateFormat());

        Date date = null;
        try {
            if  (value != null && !value.equals("")) {
                date = inDateFormat.parse(value);
                attribute.setValue(outDateFormat.format(date));
            }
        }
        catch (ParseException e) {
            Log.getLog(getClass()).error("Error parsing the date", e);
        }

		attributes.setNamedItem(attribute);
		
		attribute = doc.createAttribute("format");
		attribute.setValue(FormatUtil.getInstance().getLongDateFormat());
		attributes.setNamedItem(attribute);
	}
	
	protected void setSelectedOption(NamedNodeMap attributes, org.w3c.dom.Document doc, DefaultDataObject ddo, String templateNodeName) {
		Attr attribute = doc.createAttribute("selectedOption");
        String name = templateNodeName + attributes.getNamedItem("name").getNodeValue();
		attribute.setValue(nullToEmpty(String.valueOf(ddo.getProperty(name))));
		attributes.setNamedItem(attribute);		
	}

    protected void setFileObject(Node node, Node removedNode, Node nextNode,NamedNodeMap attributes,
                                 org.w3c.dom.Document doc,  DefaultDataObject ddo, String templateNodeName) {
        String name = templateNodeName + attributes.getNamedItem("name").getNodeValue();

        if (ddo.getProperty(name) != null   ) {
            org.w3c.dom.Element linkElement =  setLinkObject(attributes,doc, ddo, templateNodeName);

            node.replaceChild(linkElement, removedNode);


            org.w3c.dom.Element labelElement = setEmptyLabelObject(attributes, "emptyLbl",doc);
            node.insertBefore(labelElement,nextNode);

            org.w3c.dom.Element checkboxElement = setRemoveCheckBoxObject(attributes,doc);
            node.insertBefore(checkboxElement,nextNode);


            labelElement = setEmptyLabelObject(attributes, "emptyLbl2",doc);
            node.insertBefore(labelElement,nextNode);

             org.w3c.dom.Element fileuploadElement = setFileUploadObject(attributes,doc);
             node.insertBefore(fileuploadElement,nextNode);


        }


    }


    protected org.w3c.dom.Element setLinkObject(NamedNodeMap attributes,org.w3c.dom.Document doc,
                                                DefaultDataObject ddo, String templateNodeName) {
        String name = templateNodeName + attributes.getNamedItem("name").getNodeValue();
        org.w3c.dom.Element linkElement = doc.createElement("link");
        linkElement.setAttribute("name",attributes.getNamedItem("name").getNodeValue() + FormConstants.FIELD_LINK_SUFFIX + ddo.getProperty("formUid"));
        linkElement.setAttribute("url",nullToEmpty( url + ddo.getProperty(name)));
        linkElement.setAttribute("text",processFile(nullToEmpty(String.valueOf(ddo.getProperty(name)))));

        return linkElement;
    }

    protected org.w3c.dom.Element setEmptyLabelObject(NamedNodeMap attributes,String name,org.w3c.dom.Document doc) {
        org.w3c.dom.Element labelElement = doc.createElement("label");
        labelElement.setAttribute("name",attributes.getNamedItem("name").getNodeValue() + name);
        return labelElement;
    }

    protected org.w3c.dom.Element setRemoveCheckBoxObject(NamedNodeMap attributes,org.w3c.dom.Document doc) {
        org.w3c.dom.Element checkboxElement = doc.createElement("checkbox");
        checkboxElement.setAttribute("name", attributes.getNamedItem("name").getNodeValue() + FormConstants.FIELD_CHECK_BOX_SUFFIX );
        checkboxElement.setAttribute("text","Remove");
        return checkboxElement;
    }

    protected org.w3c.dom.Element setFileUploadObject(NamedNodeMap attributes,org.w3c.dom.Document doc) {
        org.w3c.dom.Element fileuploadElement = doc.createElement("fileupload");
        fileuploadElement.setAttribute("name",attributes.getNamedItem("name").getNodeValue());
        fileuploadElement.setAttribute("hidden",attributes.getNamedItem("hidden").getNodeValue());
        fileuploadElement.setAttribute("require",attributes.getNamedItem("require").getNodeValue());
        fileuploadElement.setAttribute("created","true");

        //append validator not empty
        if (attributes.getNamedItem("require").getNodeValue().equals("1")) {
            org.w3c.dom.Element fileuploadRequiredElement = doc.createElement(ValidatorFileUpload.class.getName());
            fileuploadRequiredElement.setAttribute("name","required");
            fileuploadRequiredElement.setAttribute("text","Required Field!");
            fileuploadElement.appendChild(fileuploadRequiredElement);
        }

        return fileuploadElement;
    }



	protected void setIdValue(NamedNodeMap attributes, org.w3c.dom.Document doc) {
		Attr attribute = doc.createAttribute("id");
		attribute.setValue(getId());
		attributes.setNamedItem(attribute);
		
	}
	
	protected void setCancelButton(org.w3c.dom.Document doc, Node node) {
		org.w3c.dom.Element e = doc.createElement("button");
		NamedNodeMap attributes = e.getAttributes();
		
		Attr attribute = doc.createAttribute("name");
		attribute.setValue("cancel");
		attributes.setNamedItem(attribute);
		
		attribute = doc.createAttribute("text");
		attribute.setValue("Cancel");
		attributes.setNamedItem(attribute);
		
		node.appendChild(e);
		
		
	}

    protected void setPrintButton(org.w3c.dom.Document doc, Node node) {
		org.w3c.dom.Element e = doc.createElement("button");
		NamedNodeMap attributes = e.getAttributes();

		Attr attribute = doc.createAttribute("name");
		attribute.setValue("print");
		attributes.setNamedItem(attribute);

		attribute = doc.createAttribute("text");
		attribute.setValue("Print");
		attributes.setNamedItem(attribute);

		node.appendChild(e);


	}



	protected String getTableName() throws FormDaoException {
		FormModule module = (FormModule)Application.getInstance().getModule(FormModule.class);		
		String tableName = null;

		FormDataObject fdo = module.getForm(getFormId());
		tableName = fdo.getFormName();

			

		
		return tableName;
	}
	
	protected String getColumnStr() throws FormDocumentException {
        List templateList = null;

        StringBuffer columnsBuffer = new StringBuffer();
        Element templateElement = null;
        FormElement form = null, formTemplate = null;

        form = Util.getFormElement(getFormId());

            columnsBuffer = getColumnStr(form,"");


            try {
                templateList = XPath.selectNodes(form,"/form/" + TemplateElement.ELEMENT_NAME);
                for (Iterator iterator = templateList.iterator(); iterator.hasNext();) {
                    templateElement =  (Element) iterator.next();
                    formTemplate = Util.getTemplateElement(templateElement.getAttributeValue("templateId"));
                    columnsBuffer.append(getColumnStr(formTemplate,templateElement.getAttributeValue("name")));

                }




                if (columnsBuffer.lastIndexOf(",") == columnsBuffer.length() - 1)
                    columnsBuffer.append("formUid,userId,datePosted");
            }
            catch (JDOMException e) {
                throw new FormDocumentException("Error selecting /form/" + TemplateElement.ELEMENT_NAME + " nodes",e);
            }



        return columnsBuffer.toString();
	}

    private StringBuffer getColumnStr(FormElement form, String templateNodeName) throws FormDocumentException {
        Element labelElement = null, tableGridElement = null;
        String labelNodeName = null;
        StringBuffer columnsBuffer = null;
        try {

            columnsBuffer = new StringBuffer();
            List labelNode = XPath.selectNodes(form,"/form/label");
            for (Iterator iterator = labelNode.iterator(); iterator.hasNext();) {
                labelElement = (Element) iterator.next();
                labelNodeName = labelElement.getAttributeValue("name");

                //remove "lb" to get the field name
                if (labelNodeName.endsWith("lb")) {
                    labelNodeName = labelNodeName.substring(0,labelNodeName.length() - 2);
                    columnsBuffer.append(templateNodeName).append(labelNodeName).append(",");
                }
            }

            List tableGridNode =  XPath.selectNodes(form,"/form/"+ TableGridElement.ELEMENT_NAME);
            for (Iterator iterator = tableGridNode.iterator(); iterator.hasNext();) {
               tableGridElement =  (Element) iterator.next();
               columnsBuffer.append(templateNodeName).append(tableGridElement.getAttributeValue("name")).append(",");


            }
        }
        catch (JDOMException e) {
            throw new FormDocumentException("Error selecting /form/label nodes",e);
        }
        return  columnsBuffer;

    }

	protected String nullToEmpty(String str) {
		if (str == null)
			str = "";
		if (str.equals("null"))
			str = "";
		return str;
	}

     protected String processFile(String file) {
        StringTokenizer stk = null;
        String fileName = "";

        if (file != null && file.trim().length() > 0) {
            stk = new StringTokenizer(file,"/");

            while(stk.hasMoreTokens())
                fileName = stk.nextToken();
        }
        return fileName;

    }



	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

}

