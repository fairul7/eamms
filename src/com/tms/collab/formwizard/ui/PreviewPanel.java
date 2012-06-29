package com.tms.collab.formwizard.ui;

import kacang.Application;
import kacang.stdui.Panel;
import kacang.stdui.Button;
import kacang.ui.Event;
import kacang.ui.Widget;
import kacang.ui.Forward;
import kacang.runtime.config.WidgetParser;
import kacang.util.Log;

import com.tms.collab.formwizard.model.*;
import com.tms.collab.formwizard.xmlwidget.*;
import org.jdom.output.DOMOutputter;
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
import java.util.List;
import java.util.Iterator;







public class PreviewPanel extends Panel{
    protected Button close;
	protected Button newField;
	protected FormElement form;
	protected static final String UP_EVENT="up";
	protected static final String DOWN_EVENT="down";
	protected static final String REMOVE_EVENT="remove";
	protected String formId;

    public PreviewPanel() {
    }

    public PreviewPanel(String s) {
        super(s);
    }

    public void onRequest(Event event) {				        
        removeChildren();
        initPanel();
    }

    public void initPanel(){
		DOMOutputter domOutput;
		org.w3c.dom.Document doc;
		Node node;
		WidgetParser parser;
		Widget[] widgets = null;
		
		try {
            

			form = Util.getFormElement(getFormId());
		
		
        	close = new Button("close");
        	close.setText(Application.getInstance().getMessage("formWizard.label.previewPanel.close","close"));
        	close.setOnClick("javascript:window.close()");
        	
        	
			newField = new Button("newField");
			newField.setText("New Field");
			newField.setOnClick("javascript:addfields('"+getFormId()+"')");
			newField.setHidden(true);		
			

			parser = new WidgetParser();
			
        	domOutput = new DOMOutputter();
        
		
			doc = domOutput.output(form.getDocument());


			node = doc.getDocumentElement();
            setFormTemplate(doc,node);


            parseElement(node,doc, "");

            Document jDomDocument = Util.DOMtoJDOM(node.getOwnerDocument());
            renameTemplateNode(jDomDocument);

            widgets = parser.parseConfig(Util.JDOMtoDOM(jDomDocument));

            


        	addEventListener(this);

        	addChild(widgets[0]);
			
		
			addChild(newField);
        	addChild(close);
		}
		catch (JDOMException e) {					
			Log.getLog(getClass()).error("Error parsing the config",e);
		}
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        catch (FormException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        catch (FormDocumentException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }

    }



    public void parseElement(Node node, org.w3c.dom.Document document, String templateName)
            throws FormDaoException, FormException, FormDocumentException {
        NamedNodeMap attributes = null;
        NodeList nodeList = node.getChildNodes();

        for (int i=0; i < nodeList.getLength(); i++) {
            attributes = nodeList.item(i).getAttributes();

            if (nodeList.item(i).getNodeName().equals(TemplateElement.ELEMENT_NAME)) {
                    setTemplateWidget(nodeList.item(i), attributes, document);
                    parseElement(nodeList.item(i).getFirstChild(),document,attributes.getNamedItem("name").getNodeValue());
            }

            if (nodeList.item(i).getNodeName().equals(LabelElement.ELEMENT_NAME)) {
                setLabelWidget(attributes, document,templateName);
            }

            if (nodeList.item(i).getNodeName().equals(TableGridElement.ELEMENT_NAME)) {
                setTableGridWidget(node,nodeList.item(i),document,templateName);
            }


			if(attributes.getNamedItem("hidden") != null)
			    attributes.getNamedItem("hidden").setNodeValue("0");


        }
    }

    public void setFormTemplate(org.w3c.dom.Document document,Node node){
        Node templateNode = node.getAttributes().getNamedItem("template");
        Attr attribute = null;
        if (templateNode != null) {
            templateNode.setNodeValue("formwizard/form");
        }
        else {
            attribute = document.createAttribute("template");
            attribute.setValue("formwizard/form");
            node.getAttributes().setNamedItem(attribute);
        }

    }




    public void setTableGridWidget(Node rootNode,Node node, org.w3c.dom.Document document, String templateName) {
        org.w3c.dom.Element element = document.createElement("label");
        //getParent().getName();
        element.setAttribute("name",node.getAttributes().getNamedItem("name").getNodeValue());
        /*element.setAttribute("text", "<a href=\"frwEditField.jsp?formId=" + getFormId() + "&formUid=" + element.getAttribute("name") + "\" target=\"_\">"
                                      + "Edit ' " +  node.getAttributes().getNamedItem("title").getNodeValue() + "'" + "</a>");*/
        element.setAttribute("text","<a href=\"\" onClick=\"openEditField('" + getFormId() +"','" + node.getAttributes().getNamedItem("name").getNodeValue() + "','" + templateName +"'); return false;\"> " + Application.getInstance().getMessage("formWizard.label.previewPanel.edit","Edit") + " '" + node.getAttributes().getNamedItem("title").getNodeValue() + "</a>'");



        element.setAttribute("colspan",node.getAttributes().getNamedItem("colspan").getNodeValue());
        element.setAttribute("rowspan",node.getAttributes().getNamedItem("rowspan").getNodeValue());
        element.setAttribute("align",node.getAttributes().getNamedItem("align").getNodeValue());
        element.setAttribute("valign",node.getAttributes().getNamedItem("valign").getNodeValue());

         //Label label = new Label();

        rootNode.replaceChild(element,node);

    }




    public void renameTemplateNode(Document doc) throws FormException {
        try {
            List templateList = XPath.selectNodes(doc.getRootElement(),"/form/"+ TemplateElement.ELEMENT_NAME);
            Element e;
            //String colspan = "2", rowspan = "1",align = "left",valign = "top";

            for (Iterator iterator = templateList.iterator(); iterator.hasNext();) {

                e = (Element) iterator.next();
                e.setName("panel");
                e.setAttribute("width","100%");
              //  e.getAttributeValue("colspan");
                //e.setAttribute("columns","2");
                //e.setAttribute("template",FormConstants.PREVIEW_PANEL);
            }
        }
        catch (JDOMException e) {
            throw new FormException("Error selecting nodes",e);
        }

    }





    public void setTemplateWidget(Node node, NamedNodeMap attributes,org.w3c.dom.Document doc)
            throws FormDaoException, FormException, FormDocumentException {
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        String templateId = attributes.getNamedItem("templateId").getNodeValue();
        InputStream stream = module.getTemplatePreviewXml(templateId);
        org.w3c.dom.Document w3cDocument = null;
        org.w3c.dom.Element element = null, childElement = null, formElement = null;
        NodeList nl = null, childNodeList = null;
        Node formNode = null, attributeNode = null;
        NamedNodeMap templateAttributes = null, childAttributes = null;
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
                    for (int j=0; j < templateAttributes.getLength();j++) {
                        attributeNode = templateAttributes.item(j);
                        if (!attributeNode.getNodeName().equals("template")) {
                            element.setAttribute(attributeNode.getNodeName(), attributeNode.getNodeValue());
                        }
                    }

                    //add child node 1 level
                    childNodeList = nl.item(i).getChildNodes();
                    if (childNodeList != null && childNodeList.getLength() > 0) {
                        for (int j = 0; j < childNodeList.getLength(); j++) {
                            childElement =  doc.createElement(childNodeList.item(j).getNodeName());
                            childAttributes =  childNodeList.item(j).getAttributes();

                             for (int k=0; k < childAttributes.getLength();k++) {
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



    public void setLabelWidget(NamedNodeMap attributes, org.w3c.dom.Document w3cDocument, String templateName) {
        String formUid = attributes.getNamedItem("name").getNodeValue();
        if (formUid.endsWith("lb")) {
            formUid = formUid.substring(0,formUid.length()-2);
        }


          //set the required field to bold and append with an asterisk
        if ("1".equals(attributes.getNamedItem("require").getNodeValue()))
	        attributes.getNamedItem("text").setNodeValue("<b><a href=\"\" onClick=\"openEditField('" + getFormId() +"','" + formUid +"','" + templateName + "');return false;\">"
                                                         + attributes.getNamedItem("text").getNodeValue() + "</a></b> *");
        //set the hidden field to bold and italic
        else if ("1".equals(attributes.getNamedItem("hidden").getNodeValue()))
		    attributes.getNamedItem("text").setNodeValue("<b><i><a href=\"\" onClick=\"openEditField('" + getFormId() +"','" + formUid +"','" + templateName + "');return false;\">"
                                                         + attributes.getNamedItem("text").getNodeValue() + "</a></i></b>");
        //set the normal field to bold
        else if (attributes.getNamedItem("type") == null)
            attributes.getNamedItem("text").setNodeValue("<b><a href=\"\" onClick=\"openEditField('" + getFormId() +"','" + formUid +"','" + templateName + "');return false;\">"
                                                         + attributes.getNamedItem("text").getNodeValue() + "</a></b>");
        else
            attributes.getNamedItem("text").setNodeValue("<a href=\"\" onClick=\"openEditField('" + getFormId() +"','" + formUid +"','" + templateName + "');return false;\">"
                                                         + attributes.getNamedItem("text").getNodeValue() + "</a>");


    }



    public Forward actionPerformed(Event event) {
        if (UP_EVENT.equals(event.getType())|| DOWN_EVENT.equals(event.getType())){
           return  move(event);
        } if(REMOVE_EVENT.equals(event.getType())){
            return remove(event);
        }
        return super.actionPerformed(event);
    }

    protected Forward move(Event event){
        String childName= event.getRequest().getParameter("childName");
        if(childName!=null){
            int index= childName.lastIndexOf(".");
            if(index<0)
                return null;
            
            FormElement form = Util.getFormElementInstance(getFormId());
            move(form,childName.substring(index+1),event.getType());            
            removeChildren();
            initPanel();
        }
        return null;
    }

    protected void move(FormElement rootElem, String child, String event) {
        Element headerElem = rootElem.getChild("description");
        rootElem.removeContent(headerElem);
        List children = rootElem.getChildren();
        int size = children.size();
        Object widget = null;
        Object currObj = null;
        Element elem = null;

        for (int i = 0; i < size; i++) {
			elem = (Element) children.get(i);
			if (elem.getAttributeValue("text") != null)
				elem.setAttribute("text",Util.escapeSingleQuote(elem.getAttributeValue("text")));

            if (elem.getAttributeValue("value") != null)
                elem.setAttribute("value",Util.escapeSingleQuote(elem.getAttributeValue("value")));
        }




        for (int i = 0; i < size; i++) {
            elem = (Element) children.get(i);
            String nameStr = elem.getAttributeValue("name");
			
            if (nameStr == null || !nameStr.equals(child)) {
                continue;
            }

            //move up
            if (UP_EVENT.equals(event)) {
                if (TemplateElement.ELEMENT_NAME.equals(elem.getName())
                    || LabelElement.ELEMENT_NAME.equals(elem.getName())
                    || TableGridElement.ELEMENT_NAME.equals(elem.getName())) {
                    if (i-1 > 0) {
                        widget = children.get(i-1);


                        if (widget instanceof TemplateElement
                            || widget instanceof TextBlockElement
                            || widget instanceof TableGridElement) {
                            currObj = children.remove(i);
                            children.add(i-1,currObj);
                        }
                        else {
                            currObj = children.remove(i);
                            children.add(i-2,currObj);
                        }
                    }

                }
                else {
                    if (i-2 >=0 ) {
                        //get the previous element
                        widget = children.get(i-2);
                        if (widget instanceof TemplateElement
                            || widget instanceof TextBlockElement
                            || widget instanceof TableGridElement) {
                             currObj = children.remove(i-2);
                             children.add(i,currObj);
                        }
                        else {
                            currObj = children.remove(i-3);
                            children.add(i, currObj);

                            currObj = children.remove(i-3);
                            children.add(i, currObj);
                        }

                    }

                }
            }
            //move down
            else {
                if (TemplateElement.ELEMENT_NAME.equals(elem.getName())
                    || LabelElement.ELEMENT_NAME.equals(elem.getName())
                    || TableGridElement.ELEMENT_NAME.equals(elem.getName())) {

                    if (i+1 < children.size()) {
                        //get the next element
                        widget = children.get(i+1);

                        if (widget instanceof TemplateElement
                            || widget instanceof TextBlockElement
                            || widget instanceof TableGridElement) {
                            currObj = children.remove(i);
                            children.add(i+1,currObj);
                        }
                        else {
                            currObj = children.remove(i);
                            children.add(i+2,currObj);
                        }
                    }
                }
                else {
                    if (i+1 < children.size()) {
                        widget = children.get(i+1);

                        if (widget instanceof TemplateElement
                         || widget instanceof TextBlockElement
                         || widget instanceof TableGridElement) {
                            currObj = children.remove(i + 1);
                            children.add(i-1,currObj);
                        }
                        else {
                            currObj = children.remove(i -1);
                            children.add(i+2,currObj);

                            currObj = children.remove(i -1 );
                            children.add(i+2,currObj);
                        }
                    }
                }
            }
		    updateConfig(rootElem);


            break;
        }
    }

    public void updateConfig(FormElement rootElem) {
        FormModule handler = (FormModule)Application.getInstance().getModule(FormModule.class);
        FormDao dao = (FormDao)handler.getDao();
        String previewXml = null;
        String formXml = null;
        try {
            previewXml = rootElem.display();
            formXml = handler.generateEditFormXML(rootElem);
            dao.updateFormConfig(getFormId(),previewXml,formXml);
        }

        catch (IOException e) {
            Log.getLog(getClass()).error("",e);
        }
        catch (FormException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
    }



    protected Forward remove(Event event){
        FormModule module = null;



        String childName= event.getRequest().getParameter("childName");
        if(childName!=null){
            int index= childName.lastIndexOf(".");
            if(index<0)
                return null;

            module = (FormModule) Application.getInstance().getModule(FormModule.class);
            try {

                removeWidget(event,childName);
                module.removeField(form,getFormId(),childName.substring(index + 1));
            }
            catch (FormDaoException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);
            }
            catch (FormException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);
            }
            catch (FormDocumentException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);
            }
          }
        return null;
    }

    public void removeWidget(Event event,String childName) {
        Widget widgetValue = event.getWidgetManager().getWidget(childName);
        widgetValue.getParent().removeChild(widgetValue);


        Widget widgetLabel = event.getWidgetManager().getWidget(childName + "lb");
        if (widgetLabel != null)
            widgetLabel.getParent().removeChild(widgetLabel);

    }

	public String getFormId() {
		return formId;
	}


	public void setFormId(String formId) {
		this.formId = formId;
	}

}
