package com.tms.collab.formwizard.ui;

import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.io.IOException;
import java.io.InputStream;


import com.tms.collab.formwizard.xmlwidget.*;
import com.tms.collab.formwizard.model.*;
import com.tms.collab.formwizard.grid.G2Field;
import com.tms.collab.formwizard.grid.G2Column;


import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Document;
import org.jdom.xpath.XPath;
import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;


public class EditField extends AddFormField {
    protected String nodeName;

    protected Button save;
	protected Label fieldTypeLabel;
    protected Label fieldTypeValue;
    protected String formUid;
    protected Button btMoveUp;
    protected Button btMoveDown;
    protected Button btRemove;
    protected G2Field grid;
    protected String templateNodeName;


	public void onRequest(Event event) {
		super.onRequest(event);
        fieldForm = (AddG2FieldForm)event.getWidgetManager().getWidget(fieldFormAbsoluteName);
        String et =  event.getRequest().getParameter("et");
        if ("editColumn".equals(et) || "deleteColumn".equals(et)) {
        }
        else {
		    initFields();
        }
	}
	
	public void init() {
		super.init();
        removeChild(rtbLabel);
		fieldType.setHidden(true);
		save = new Button("save");
		save.setText(Application.getInstance().getMessage("formWizard.label.editField.save","Save"));

        btMoveUp = new Button("moveup");
        btMoveUp.setText(Application.getInstance().getMessage("formWizard.label.editField.moveUp","Move Up"));
        addChild(btMoveUp);

        btMoveDown = new Button("movedown");
        btMoveDown.setText(Application.getInstance().getMessage("formWizard.label.editField.moveDown","Move Down"));
        addChild(btMoveDown);

        btRemove = new Button("remove");
        btRemove.setText(Application.getInstance().getMessage("formWizard.label.editField.remove","Remove"));
        addChild(btRemove);


		fieldTypeLabel = new Label("fieldTypeLabel");
        fieldTypeValue = new Label("fieldTypeValue");

		addChild(fieldTypeLabel);
        addChild(fieldTypeValue);
		addChild(save);

		
			
	}
	
	public void initFields() {	
		Element valueElem = null;
		Element labelElem = null;
		FormElement form = null;
		try {
			form = getFormElement();
			labelElem = (Element) XPath.selectSingleNode(form,
				"/form/*[@name='" + formUid + "lb']");
			valueElem = (Element) XPath.selectSingleNode(form,
				"/form/*[@name='" + formUid+ "']");
		} 
		catch (JDOMException e) {
			Logger.getLogger(getClass()).error("Failed to parse XML form.", e);
		}



        if (valueElem != null) {
            nodeName = valueElem.getAttributeValue("name");
            tfColspan.setValue(valueElem.getAttributeValue("colspan"));
            tfRowspan.setValue(valueElem.getAttributeValue("rowspan"));

            sbAlign.setSelectedOption(valueElem.getAttributeValue("align"));
            sbValign.setSelectedOption(valueElem.getAttributeValue("valign"));

            if (labelElem != null)
                fieldName.setValue(labelElem.getAttributeValue("text"));

            if(valueElem.getName().equals(TextFieldElement.ELEMENT_NAME)){
                fieldName.setHidden(false);
                tfDataType.setHidden(false);
                fieldSize.setHidden(false);
                tfDefaultValue.setHidden(false);
                maxLength.setHidden(false);
                maxCols.setHidden(true);
                options.setHidden(true);
                maxRows.setHidden(true);
                String[] dataType= {valueElem.getAttributeValue("type").toString()};
                fieldSize.setValue(valueElem.getAttributeValue("size"));
                maxLength.setValue(valueElem.getAttributeValue("maxLength"));
                tfDefaultValue.setValue(valueElem.getAttributeValue("value"));
                tfDataType.setSelectedOptions(dataType);
                rtbLabel.setHidden(true);
                fieldTypeLabel.setText("Text Input");
                fieldTypeValue.setText(FormConstants.FIELD_TEXT_INPUT);
                tfTitle.setHidden(true);
            }
            else if(valueElem.getName().equals(TextBoxElement.ELEMENT_NAME)){
                fieldName.setHidden(false);
                tfDataType.setHidden(true);
                options.setHidden(true);
                maxLength.setHidden(true);
                maxRows.setHidden(false);
                maxCols.setHidden(false);
                fieldSize.setHidden(true);
                tfDefaultValue.setHidden(false);
                maxRows.setValue(valueElem.getAttributeValue("rows"));
                maxCols.setValue(valueElem.getAttributeValue("cols"));
                tfDefaultValue.setValue(valueElem.getAttributeValue("value"));
                rtbLabel.setHidden(true);
                fieldTypeLabel.setText("Text Box");
                fieldTypeValue.setText(FormConstants.FIELD_TEXT_BOX);
                tfTitle.setHidden(true);
            }
            else if(valueElem.getName().equals(ButtonGroupElement.ELEMENT_NAME) ){
                fieldName.setHidden(false);
                tfDataType.setHidden(true);
                tfDefaultValue.setHidden(true);
                options.setHidden(false);
                maxLength.setHidden(true);
                maxRows.setHidden(true);
                maxCols.setHidden(true);
                fieldSize.setHidden(true);
                tfDataType.setHidden(true);
                rtbLabel.setHidden(true);
                StringBuffer optionBuffer = new StringBuffer();
                List children = valueElem.getChildren();
                for (Iterator childIter = children.iterator(); childIter.hasNext();) {
                     Element elem = (Element) childIter.next();
                     if (  elem.getName().equals(ButtonGroupElement.CHECKBOX_ELEMENT_NAME)
                         ||elem.getName().equals(ButtonGroupElement.RADIO_ELEMENT_NAME))
                        optionBuffer.append(nullToEmpty(elem.getAttributeValue("text"))).append(';');

                     if (elem.getName().equals(ButtonGroupElement.CHECKBOX_ELEMENT_NAME)) {
                        fieldTypeLabel.setText("Check Box");
                        fieldTypeValue.setText(FormConstants.FIELD_CHECK_BOX);
                     }

                    if (elem.getName().equals(ButtonGroupElement.RADIO_ELEMENT_NAME)) {
                        fieldTypeLabel.setText("Radio Button");
                        fieldTypeValue.setText(FormConstants.FIELD_RADIO_BUTTON);
                    }
                 }
                 if (optionBuffer.length() > 0) {
                     options.setValue(optionBuffer.toString().substring(
                                     0, optionBuffer.length() - 1).replace(';', '\n'));
                 }
                 tfTitle.setHidden(true);

            }
            else if (valueElem.getName().equals(SelectElement.ELEMENT_NAME) ) {
                fieldName.setHidden(false);
                tfDataType.setHidden(true);
                tfDefaultValue.setHidden(true);
                options.setHidden(false);
                maxLength.setHidden(true);
                maxRows.setHidden(true);
                maxCols.setHidden(true);
                fieldSize.setHidden(true);
                tfDataType.setHidden(true);
                fieldTypeValue.setText(FormConstants.FIELD_PULL_DOWN_MENU);

                String sbOoptions = valueElem.getAttributeValue("options");
                StringBuffer optionBuffer = new StringBuffer();

                if (sbOoptions != null) {
                    StringTokenizer stk = new StringTokenizer(sbOoptions,";");
                    StringTokenizer stk2 = null;
                    while (stk.hasMoreTokens()) {
                        stk2 = new StringTokenizer(stk.nextToken(),"=");

                        optionBuffer.append(stk2.nextToken() + "\n");
                        stk2.nextToken();

                    }

                    if (optionBuffer.length() > 0) {
                        options.setValue(optionBuffer.toString().substring(
                                         0, optionBuffer.length() - 1).replace(';', '\n'));
                    }
                }
                else {
                    options.setValue("");
                }

                rtbLabel.setHidden(true);
                fieldTypeLabel.setText("Pull Down Menu");
                tfTitle.setHidden(true);

            }
            else if (valueElem.getName().equals(DateFieldElement.ELEMENT_NAME) ) {
                fieldName.setHidden(false);
                tfDataType.setHidden(true);
                options.setHidden(true);
                maxLength.setHidden(true);
                maxRows.setHidden(true);
                maxCols.setHidden(true);
                fieldSize.setHidden(true);
                tfDefaultValue.setHidden(true);
                rtbLabel.setHidden(true);
                fieldTypeLabel.setText("Date Select");
                fieldTypeValue.setText(FormConstants.FIELD_DATE_SELECT);
                tfTitle.setHidden(true);
            }
            else if (valueElem.getName().equals(FileUploadElement.ELEMENT_NAME) ) {
                fieldName.setHidden(false);
                tfDataType.setHidden(true);
                options.setHidden(true);
                maxLength.setHidden(true);
                maxRows.setHidden(true);
                maxCols.setHidden(true);
                fieldSize.setHidden(true);
                tfDefaultValue.setHidden(true);
                rtbLabel.setHidden(true);
                fieldTypeLabel.setText("File Upload");
                fieldTypeValue.setText(FormConstants.FIELD_FILE_UPLOAD);
                tfTitle.setHidden(true);
            }
            else if (valueElem.getName().equals(LabelElement.ELEMENT_NAME) ) {
                fieldName.setHidden(true);
                tfDataType.setHidden(true);
                options.setHidden(true);
                maxLength.setHidden(true);
                maxRows.setHidden(true);
                maxCols.setHidden(true);
                fieldSize.setHidden(true);
                tfDefaultValue.setHidden(true);
                addChild(rtbLabel);
                rtbLabel.setHidden(false);
                fieldTypeLabel.setText("Text Block");
                fieldTypeValue.setText(FormConstants.FIELD_TEXT_BLOCK);
                rtbLabel.setValue(valueElem.getAttributeValue("text"));
                tfTitle.setHidden(true);
            }
            else if (valueElem.getName().equals(TableGridElement.ELEMENT_NAME) ) {
                fieldName.setHidden(true);
                tfDataType.setHidden(true);
                options.setHidden(true);
                maxLength.setHidden(true);
                maxRows.setHidden(true);
                maxCols.setHidden(true);
                fieldSize.setHidden(true);
                tfDefaultValue.setHidden(true);
                rtbLabel.setHidden(true);
                fieldTypeLabel.setText("Table Grid");
                fieldTypeValue.setText(FormConstants.FIELD_TABLE_GRID);
                tfTitle.setHidden(false);
                tfTitle.setValue(valueElem.getAttributeValue("title"));

                grid = new G2Field(nodeName);
                grid.setColumnListXml(valueElem.getAttributeValue("columnListXml"));
                addChild(grid);



            }




            if 	(valueElem.getAttributeValue("hidden") != null &&
                 valueElem.getAttributeValue("hidden").toString().equals("1"))
                hiddenYes.setChecked(true);
            else
                hiddenNo.setChecked(true);

            if 	(valueElem.getAttributeValue("require") != null &&
                 valueElem.getAttributeValue("require").toString().equals("1"))
                requiredYes.setChecked(true);
            else
                requiredNo.setChecked(true);

        }
        //template
        else {
            try {
                valueElem = (Element) XPath.selectSingleNode(form,
				                       "/form/" + TemplateElement.ELEMENT_NAME +"[@name='" + getTemplateNodeName() + "']");

                if (valueElem != null) {
                    tfColspan.setValue(valueElem.getAttributeValue("colspan"));
                    tfRowspan.setValue(valueElem.getAttributeValue("rowspan"));

                    sbAlign.setSelectedOption(valueElem.getAttributeValue("align"));
                    sbValign.setSelectedOption(valueElem.getAttributeValue("valign"));
                }    
            }
            catch (JDOMException e) {
                Logger.getLogger(getClass()).error("Failed to parse XML form.", e);
            }
            fieldName.setHidden(true);
                tfDataType.setHidden(true);
                options.setHidden(true);
                maxLength.setHidden(true);
                maxRows.setHidden(true);
                maxCols.setHidden(true);
                fieldSize.setHidden(true);
                tfDefaultValue.setHidden(true);
                rtbLabel.setHidden(true);
                tfTitle.setHidden(true);
                fieldTypeLabel.setText("");
                fieldTypeValue.setText(valueElem.getAttributeValue("templateId"));

        }
	}
	
	public Forward onValidate(Event evt){
		Forward forward = null;
		List fieldTypeList = new ArrayList();
		
		
		String buttonName = findButtonClicked(evt);


        if (buttonName != null) {
            if (save.getAbsoluteName().equals(buttonName)) {
			    fieldTypeList.add(fieldTypeValue.getText());
			    fieldType.setValue(fieldTypeList);
			    verifyField();
			    if (isInvalid())
				    return null;
			    forward = editField(evt.getRequest());
		    }
            else if (btMoveUp.getAbsoluteName().equals(buttonName)) {
                moveUp(evt);
                forward = new Forward("movedUp");
            }
            else if (btMoveDown.getAbsoluteName().equals(buttonName)) {
                moveDown(evt);
                forward = new Forward("movedDown");
            }
            else if (btRemove.getAbsoluteName().equals(buttonName)) {
                remove(evt);
                forward = new Forward("removed");
            }
        }


		return forward;
	}

    public void remove(Event evt) {
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        FormElement form = Util.getFormElement(getFormId());
        String id =  getFormUid();
        try {
            if (getTemplateNodeName() != null && getTemplateNodeName().trim().length() > 0)
                id = getTemplateNodeName();
            module.removeField(form,getFormId(),id);
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

    public InputStream getInputStream() throws FormDaoException {
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
        InputStream stream = module.getFormPreviewXML(getFormId());
        return stream;
    }

    public void moveDown(Event evt) {

        InputStream stream = null;

        try {
            stream = getInputStream();
            org.w3c.dom.Document w3cDocument = Util.buildDOMDocument(stream);
            org.w3c.dom.Element rootElem = w3cDocument.getDocumentElement();
            NodeList nodeList = rootElem.getChildNodes();
            Node node = null, removedNode = null;
            NamedNodeMap attributes = null;
            String name = "", labelName = "", fieldName = "";
            Map map1, map2 = null;
            String colspanValue = "1";
            String id = getFormUid();
            if (getTemplateNodeName() != null)
                id = getTemplateNodeName();

            for (int i = 0 ;i < nodeList.getLength(); i++) {
                node = nodeList.item(i);
                attributes = node.getAttributes();

                if (attributes.getNamedItem("name") != null)
                    name = attributes.getNamedItem("name").getNodeValue();

                if (name != null && name.equals(id)) {
                    if (node.getPreviousSibling() != null
                        && node.getPreviousSibling().getAttributes().getNamedItem("name") != null) {
                        labelName = node.getPreviousSibling().getAttributes().getNamedItem("name").getNodeValue();
                    }
                        //selected item is label + field
                         if (labelName != null && labelName.equals(id + "lb")) {
                             map2 = getNodeAttribute(node);

                             if (node.getNextSibling() != null && node.getNextSibling().getNextSibling() != null) {
                                labelName = node.getNextSibling().getAttributes().getNamedItem("name").getNodeValue();
                                fieldName =  node.getNextSibling().getNextSibling().getAttributes().getNamedItem("name").getNodeValue();
                             }


                             //(label + field) + label + field
                             if (labelName.equals(fieldName+"lb")) {
                                 map1 = getNodeAttribute(node.getNextSibling().getNextSibling());

                                 //swap the field attribute
                                 swapAttribute(map1,map2,node.getNextSibling().getNextSibling(),node,w3cDocument);

                                 map2 = getNodeAttribute(node.getPreviousSibling());
                                 map1 = getNodeAttribute(node.getNextSibling());
                                 //swap the label attribute
                                 swapAttribute(map1,map2,node.getNextSibling(),node.getPreviousSibling(),w3cDocument);

                                 removedNode = rootElem.removeChild(node.getPreviousSibling());
                                 rootElem.insertBefore(removedNode,node.getNextSibling().getNextSibling().getNextSibling());


                                 rootElem.insertBefore(node,node.getNextSibling().getNextSibling().getNextSibling().getNextSibling());
                             }
                             // (label + field) + field
                             else {
                                if (node.getNextSibling() != null) {
                                    map1 =  getNodeAttribute(node.getNextSibling());
                                    //swap the field attribute
                                    swapAttribute(map1,map2,node.getNextSibling(),node,w3cDocument);
                                    if (node.getPreviousSibling().getAttributes().getNamedItem("colspan") != null)
                                        colspanValue = node.getPreviousSibling().getAttributes().getNamedItem("colspan").getNodeValue();
                                    addColspan(colspanValue,node.getNextSibling());
                                    reduceColspan(node.getPreviousSibling(),node);

                                    removedNode = rootElem.removeChild(node.getPreviousSibling());

                                    rootElem.insertBefore(removedNode,node.getNextSibling().getNextSibling());
                                    rootElem.insertBefore(node,node.getNextSibling().getNextSibling().getNextSibling());
                                }


                             }

                         }
                         //selected item is field
                         else {
                             map2 = getNodeAttribute(node);

                             if (node.getNextSibling() != null) {
                                labelName = node.getNextSibling().getAttributes().getNamedItem("name").getNodeValue();
                                if (node.getNextSibling().getNextSibling() != null)
                                    fieldName = node.getNextSibling().getNextSibling().getAttributes().
                                                getNamedItem("name").getNodeValue();
                             }

                             // (field) + label + field
                             if (labelName.equals(fieldName+"lb")) {
                                map1 = getNodeAttribute(node.getNextSibling().getNextSibling());
                                //swap the field attribute
                                swapAttribute(map1,map2,node.getNextSibling().getNextSibling(),node,w3cDocument);

                                if (node.getNextSibling().getAttributes().getNamedItem("colspan") != null)
                                    colspanValue = node.getNextSibling().getAttributes().getNamedItem("colspan").getNodeValue();

                                addColspan(colspanValue,node);
                                reduceColspan(node.getNextSibling(),node.getNextSibling().getNextSibling());

                                rootElem.insertBefore(node,node.getNextSibling().getNextSibling().getNextSibling());

                             }
                             //(field) + field
                             else if (node.getNextSibling() != null) {
                                map1 = getNodeAttribute(node.getNextSibling());
                                //swap the field attribute
                                swapAttribute(map1,map2,node.getNextSibling(),node,w3cDocument);

                                rootElem.insertBefore(node,node.getNextSibling().getNextSibling());
                             }

                         }

                         break;

                }
            }

            updateConfig(w3cDocument);
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        catch (FormDocumentException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
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

    public void moveUp(Event evt)  {
        InputStream stream = null;
        try {
            stream = getInputStream();
            org.w3c.dom.Document w3cDocument = Util.buildDOMDocument(stream);
            org.w3c.dom.Element rootElem = w3cDocument.getDocumentElement();
            NodeList nodeList = rootElem.getChildNodes();
            Node node = null, previousSibling = null, removedNode = null;
            NamedNodeMap attributes = null;
            String name = null;
            String labelName = "";
            Map map1 , map2 = null;
            String fieldName;
            String colspanValue = "1";
            String id = getFormUid();
            if (getTemplateNodeName() != null)
                id = getTemplateNodeName();


            for (int i = 0 ;i < nodeList.getLength(); i++) {
                node = nodeList.item(i);
                attributes = node.getAttributes();
                if (attributes != null) {
                    if (attributes.getNamedItem("name") != null)
                        name = attributes.getNamedItem("name").getNodeValue();
                    if (name != null && name.equals(id)) {
                        previousSibling = node.getPreviousSibling();
                        if (previousSibling != null && previousSibling.getAttributes().getNamedItem("name") != null) {
                            labelName = previousSibling.getAttributes().getNamedItem("name").getNodeValue();

                            //selected item is label + field
                            if (labelName != null && labelName.equals(id + "lb") ) {
                                if (node.getPreviousSibling().getPreviousSibling() != null) {
                                    map2 = getNodeAttribute(node);
                                    map1 = getNodeAttribute(node.getPreviousSibling().getPreviousSibling());
                                    //swap the field attribute
                                    swapAttribute(map1,map2,node.getPreviousSibling().getPreviousSibling(),node,w3cDocument);


                                    map2 = getNodeAttribute(node.getPreviousSibling());
                                    if (node.getPreviousSibling().getPreviousSibling().getAttributes().getNamedItem("name") != null) {
                                        fieldName =  node.getPreviousSibling().getPreviousSibling().getAttributes().
                                                    getNamedItem("name").getNodeValue();

                                        if (node.getPreviousSibling().getPreviousSibling().getPreviousSibling() != null &&
                                            node.getPreviousSibling().getPreviousSibling().getPreviousSibling().
                                                    getAttributes().getNamedItem("name") != null)
                                            labelName = node.getPreviousSibling().getPreviousSibling().getPreviousSibling().
                                                        getAttributes().getNamedItem("name").getNodeValue();

                                        //label + field + (label + field)
                                        if (labelName.equals(fieldName+"lb")) {
                                            map1 = getNodeAttribute(node.getPreviousSibling().getPreviousSibling().getPreviousSibling());
                                            //swap the label attribute
                                            swapAttribute(map1,map2,node.getPreviousSibling().getPreviousSibling().getPreviousSibling(),
                                                          node.getPreviousSibling(),w3cDocument);


                                            removedNode = rootElem.removeChild(node.getPreviousSibling());
                                            rootElem.insertBefore(removedNode,node.getPreviousSibling().getPreviousSibling());


                                            rootElem.insertBefore(node,node.getPreviousSibling().getPreviousSibling());

                                        }
                                        else {  //field + (label + field)
                                            //add the label colspan
                                            addColspan((String)map2.get("colspan"),node.getPreviousSibling().getPreviousSibling());
                                            reduceColspan(node.getPreviousSibling(),node);



                                            removedNode = rootElem.removeChild(node.getPreviousSibling());
                                            rootElem.insertBefore(removedNode,node.getPreviousSibling());

                                            rootElem.insertBefore(node,node.getPreviousSibling());


                                        }
                                    }
                                }
                            }
                            //selected item is field
                            else {
                                map2 = getNodeAttribute(node);
                                map1 = getNodeAttribute(node.getPreviousSibling());
                                //swap the field attribute
                                swapAttribute(map1,map2,node.getPreviousSibling(),node,w3cDocument);


                                fieldName =  node.getPreviousSibling().getAttributes().
                                             getNamedItem("name").getNodeValue();

                                if (node.getPreviousSibling().getPreviousSibling().
                                           getAttributes().getNamedItem("name") != null)

                                labelName = node.getPreviousSibling().getPreviousSibling().
                                           getAttributes().getNamedItem("name").getNodeValue();

                                //label + field + (field)
                                if (labelName.equals(fieldName+"lb")) {
                                    if (node.getPreviousSibling().getPreviousSibling().getAttributes().
                                                    getNamedItem("colspan") != null)
                                        colspanValue = node.getPreviousSibling().getPreviousSibling().getAttributes().
                                                        getNamedItem("colspan").getNodeValue();
                                    addColspan(colspanValue,node);

                                    reduceColspan(node.getPreviousSibling().getPreviousSibling(),node.getPreviousSibling());
                                    rootElem.insertBefore(node,node.getPreviousSibling().getPreviousSibling());
                                }
                                //field + (field)
                                else {
                                   rootElem.insertBefore(node,node.getPreviousSibling());

                                }
                            }

                            break;
                        }


                    }
                }
            }

            updateConfig(w3cDocument);
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        catch (FormDocumentException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
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

    public void addColspan(String colspanValue, Node node) {
        int colspan = 0;
        int nodeColspan = 0;
        if (colspanValue != null)
            colspan = Integer.parseInt(colspanValue);
        else
            colspan = 0;

        nodeColspan = Integer.parseInt(node.getAttributes().getNamedItem("colspan").getNodeValue()) + colspan;

        node.getAttributes().getNamedItem("colspan").setNodeValue(String.valueOf(nodeColspan));

    }

    public void reduceColspan(Node labelNode, Node fieldNode) {
        int labelColspan = 1;
        int fieldColspan = 3;
        if (labelNode.getAttributes().getNamedItem("colspan") != null)
            labelColspan =  Integer.parseInt(labelNode.getAttributes().getNamedItem("colspan").getNodeValue()) ;

        if (fieldNode.getAttributes().getNamedItem("colspan") != null)   {
            fieldColspan =  Integer.parseInt(fieldNode.getAttributes().getNamedItem("colspan").getNodeValue()) ;
            fieldNode.getAttributes().getNamedItem("colspan").setNodeValue(String.valueOf(fieldColspan - labelColspan));
        }




    }

    public String getFormXml(FormElement form) throws IOException {
        FormModule handler = (FormModule)Application.getInstance().getModule(FormModule.class);
        return handler.generateEditFormXML(form);
    }

   public void updateConfig(org.w3c.dom.Document w3cDocument) {
        FormModule handler = (FormModule)Application.getInstance().getModule(FormModule.class);
        FormDao dao = (FormDao)handler.getDao();
        String previewXml = null;
        String formXml = null;
        FormElement form = null;
        Document jDomDocument = null;
        try {
            previewXml = Util.domDocumentToString(w3cDocument);
            jDomDocument = Util.DOMtoJDOM(w3cDocument);
            form = (FormElement) FormElement.newInstance(jDomDocument.getRootElement());
            formXml = getFormXml(form);
            dao.updateFormConfig(getFormId(),previewXml,formXml);
        }
        catch (FormException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        catch (FormDocumentException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
        catch (IOException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
    }

    public Map getNodeAttribute(Node node) {
        Map map = new HashMap();

        if (node.getAttributes().getNamedItem("colspan") != null)
            map.put("colspan",node.getAttributes().getNamedItem("colspan").getNodeValue());

        if (node.getAttributes().getNamedItem("rowspan") != null)
            map.put("rowspan",node.getAttributes().getNamedItem("rowspan").getNodeValue());

        if (node.getAttributes().getNamedItem("align") != null)
            map.put("align",node.getAttributes().getNamedItem("align").getNodeValue());

        if (node.getAttributes().getNamedItem("valign") != null)
            map.put("valign",node.getAttributes().getNamedItem("valign").getNodeValue());

        return map;
    }

    public void swapAttribute(Map map1,Map map2,Node node1, Node node2, org.w3c.dom.Document w3cDocument) {
        setNodeAttribute(node2,map1,w3cDocument);
        setNodeAttribute(node1,map2,w3cDocument);

    }



    public void setNodeAttribute(Node node, Map map, org.w3c.dom.Document w3cDocument) {

        Attr attribute = null;
        if (node.getAttributes().getNamedItem("colspan") != null && map.get("colspan") != null) {
            node.getAttributes().getNamedItem("colspan").setNodeValue((String)map.get("colspan"));
        }
        else if (node.getAttributes().getNamedItem("colspan") != null && map == null ) {
            node.getAttributes().getNamedItem("colspan").setNodeValue("1");
        }
        else if  (node.getAttributes().getNamedItem("colspan") == null && map.get("colspan") != null) {
            attribute = w3cDocument.createAttribute("colspan");
            attribute.setValue((String)map.get("colspan"));
            node.getAttributes().setNamedItem(attribute);
        }

        if (node.getAttributes().getNamedItem("rowspan") != null && map.get("rowspan") != null) {
            node.getAttributes().getNamedItem("rowspan").setNodeValue((String)map.get("rowspan"));
        }
        else if (node.getAttributes().getNamedItem("rowspan") != null && map == null ) {
            node.getAttributes().getNamedItem("rowspan").setNodeValue("1");
        }
        else if  (node.getAttributes().getNamedItem("rowspan") == null && map.get("rowspan") != null) {
            attribute = w3cDocument.createAttribute("rowspan");
            attribute.setValue((String)map.get("rowspan"));
            node.getAttributes().setNamedItem(attribute);
        }

        if (node.getAttributes().getNamedItem("align") != null && map.get("align") != null) {
            node.getAttributes().getNamedItem("align").setNodeValue((String)map.get("align"));
        }
        else if (node.getAttributes().getNamedItem("align") != null && map == null ) {
            node.getAttributes().getNamedItem("align").setNodeValue("left");
        }
        else if  (node.getAttributes().getNamedItem("align") == null && map.get("align") != null) {
            attribute = w3cDocument.createAttribute("align");
            attribute.setValue((String)map.get("align"));
            node.getAttributes().setNamedItem(attribute);
        }

        if (node.getAttributes().getNamedItem("valign") != null && map.get("valign") != null) {
            node.getAttributes().getNamedItem("valign").setNodeValue((String)map.get("valign"));
        }
        else if (node.getAttributes().getNamedItem("valign") != null && map == null ) {
            node.getAttributes().getNamedItem("valign").setNodeValue("top");
        }
        else if  (node.getAttributes().getNamedItem("valign") == null && map.get("valign") != null) {
            attribute = w3cDocument.createAttribute("valign");
            attribute.setValue((String)map.get("valign"));
            node.getAttributes().setNamedItem(attribute);
        }


    }



    public FormElement getFormElement() {
    	return Util.getFormElement(getFormId());
    }


    public Forward editField(HttpServletRequest request){
    	
    	

        String selected ="";
        FormElement form = getFormElement();
        Element valueElem = null;
        Element labelElem = null;
        try {
            labelElem = (Element) XPath.selectSingleNode(form,
                "/form/*[@name='" + formUid + "lb']");
            valueElem = (Element) XPath.selectSingleNode(form,
                "/form/*[@name='" + formUid + "']");
        } catch (JDOMException e) {
            Logger.getLogger(getClass()).error("Failed to parse XML form.", e);
        }
        FormFieldDataObject ffDO = new FormFieldDataObject() ;
        ffDO.setFieldType(selected);
        ffDO.setName(String.valueOf(fieldName.getValue()));
        ffDO.setColspan(tfColspan.getValue().toString());
        ffDO.setRowspan(tfRowspan.getValue().toString());
        ffDO.setAlign((String) sbAlign.getSelectedOptions().keySet().iterator().next());
        ffDO.setValign((String) sbValign.getSelectedOptions().keySet().iterator().next());

        if (valueElem != null) {
            if(valueElem.getName().equals(TextFieldElement.ELEMENT_NAME)){
                ffDO.setFieldSize(fieldSize.getValue().toString());
                Collection cSelectedList = (Collection)tfDataType.getValue();
                String dataTypeSelected="";
                if(cSelectedList.iterator().hasNext())
                    dataTypeSelected=(String)cSelectedList.iterator().next();
                ffDO.setDataType(dataTypeSelected);
                ffDO.setDefaultValue(tfDefaultValue.getValue().toString());
            } else  if(valueElem.getName().equals(TextBoxElement.ELEMENT_NAME)){
                ffDO.setDataType("1");
            } else {
                ffDO.setDataType("1");
            }

            if(hiddenYes.isChecked())
                ffDO.setHiddenFlag("1");
            else if(hiddenNo.isChecked())
                ffDO.setHiddenFlag("0");

            if(requiredYes.isChecked())
                ffDO.setRequiredFlag("1");
            else if(requiredNo.isChecked())
                ffDO.setRequiredFlag("0");





            if (labelElem != null) {
                labelElem.setAttribute("text", ffDO.getName());
                labelElem.setAttribute("hidden",ffDO.getHiddenFlag());
                labelElem.setAttribute("require",ffDO.getRequiredFlag());
                labelElem.setAttribute("rowspan",ffDO.getRowspan());
                labelElem.setAttribute("align","left");
            }



             if(valueElem.getName().equals(TextFieldElement.ELEMENT_NAME)){
                valueElem.getChildren().clear();
                valueElem.setAttribute("value", ffDO.getDefaultValue());
                valueElem.setAttribute("type", ffDO.getDataType());
                valueElem.setAttribute("size", ffDO.getFieldSize());
                valueElem.setAttribute("maxLength", maxLength.getValue().toString());
                valueElem.setAttribute("hidden",ffDO.getHiddenFlag());
                valueElem.setAttribute("require",ffDO.getRequiredFlag());
                if (ffDO.getRequiredFlag().equals("1"))
                    addValidatorNotEmpty(valueElem);
                if (ffDO.getDataType().equals(String.valueOf(FormElement.FORM_NUMERIC_TYPE)))
                    addValidatorIsInteger(valueElem);
                else if (ffDO.getDataType().equals(String.valueOf(FormElement.FORM_EMAIL_TYPE)))
                    addValidatorEmail(valueElem);
                 else if (ffDO.getDataType().equals(String.valueOf(FormElement.FORM_DECIMAL_NUMBER)))
                    addValidatorIsNumeric(valueElem);
                 valueElem.setAttribute("colspan",ffDO.getColspan());
                 valueElem.setAttribute("rowspan",ffDO.getRowspan());
                 valueElem.setAttribute("align",ffDO.getAlign());
                 valueElem.setAttribute("valign",ffDO.getValign());



             }
             else if(valueElem.getName().equals(TextBoxElement.ELEMENT_NAME)){
                valueElem.getChildren().clear();
                valueElem.setAttribute("rows",maxRows.getValue().toString());
                valueElem.setAttribute("cols",maxCols.getValue().toString());
                valueElem.setAttribute("value",tfDefaultValue.getValue().toString());
                valueElem.setAttribute("hidden",ffDO.getHiddenFlag());
                valueElem.setAttribute("require",ffDO.getRequiredFlag());

                if (ffDO.getRequiredFlag().equals("1"))
                    addValidatorNotEmpty(valueElem);

                valueElem.setAttribute("colspan",ffDO.getColspan());
                valueElem.setAttribute("rowspan",ffDO.getRowspan());
                valueElem.setAttribute("align",ffDO.getAlign());
                valueElem.setAttribute("valign",ffDO.getValign());
             }
             else if(valueElem.getName().equals(ButtonGroupElement.ELEMENT_NAME)){
                 valueElem.setAttribute("hidden",ffDO.getHiddenFlag());
                 valueElem.setAttribute("require",ffDO.getRequiredFlag());
                 String elementType = ButtonGroupElement.elementType(valueElem);
                 valueElem.getChildren().clear();

                 if (ffDO.getRequiredFlag().equals("1"))
                     addValidatorNotEmpty(valueElem);

                 if (elementType.equals(ButtonGroupElement.CHECKBOX_ELEMENT_NAME))
                    addOptions(options.getValue().toString(),valueElem);
                 else if (elementType.equals(ButtonGroupElement.RADIO_ELEMENT_NAME))
                    addRadioOptions(options.getValue().toString(),valueElem,ffDO.getName());

                 valueElem.setAttribute("colspan",ffDO.getColspan());
                 valueElem.setAttribute("rowspan",ffDO.getRowspan());
                 valueElem.setAttribute("align",ffDO.getAlign());
                 valueElem.setAttribute("valign",ffDO.getValign());

             }
             else if(valueElem.getName().equals(SelectElement.ELEMENT_NAME)){
                valueElem.getChildren().clear();
                String optionValue = options.getValue().toString();
                if (optionValue != null && optionValue.trim().length() > 0) {
                    StringTokenizer tokenizer = new StringTokenizer(optionValue, "\r\n");
                    StringBuffer optionBuffer = new StringBuffer();
                    while (tokenizer.hasMoreTokens()) {
                        String option = tokenizer.nextToken();
                        int index = option.indexOf('=');
                        if (index < 0) {
                            optionBuffer.append(option).append('=').append(option);
                        }
                        else {
                            optionBuffer.append(option);
                        }
                        optionBuffer.append(';');
                    }
                    valueElem.setAttribute("options",optionBuffer.toString());
                }
                else {
                    valueElem.setAttribute("options","");
                }

                valueElem.setAttribute("hidden",ffDO.getHiddenFlag());
                valueElem.setAttribute("require",ffDO.getRequiredFlag());
                valueElem.setAttribute("colspan",ffDO.getColspan());
                valueElem.setAttribute("rowspan",ffDO.getRowspan());
                valueElem.setAttribute("align",ffDO.getAlign());
                valueElem.setAttribute("valign",ffDO.getValign());

                if (ffDO.getRequiredFlag().equals("1"))
                    addValidatorNotEmpty(valueElem);
             }
             else if (valueElem.getName().equals(DateFieldElement.ELEMENT_NAME) || valueElem.getName().equals(FileUploadElement.ELEMENT_NAME) ) {
                valueElem.getChildren().clear();
                valueElem.setAttribute("hidden",ffDO.getHiddenFlag());
                valueElem.setAttribute("require",ffDO.getRequiredFlag());

                 if (ffDO.getRequiredFlag().equals("1"))
                    addValidatorNotEmpty(valueElem);

                 valueElem.setAttribute("colspan",ffDO.getColspan());
                 valueElem.setAttribute("rowspan",ffDO.getRowspan());
                 valueElem.setAttribute("align",ffDO.getAlign());
                 valueElem.setAttribute("valign",ffDO.getValign());
            }
            else if (valueElem.getName().equals(LabelElement.ELEMENT_NAME)) {
                getLabelElemValue(valueElem,ffDO);

            }
            else if (valueElem.getName().equals(TableGridElement.ELEMENT_NAME)) {
                valueElem.getChildren().clear();
                valueElem.setAttribute("colspan",ffDO.getColspan());
                valueElem.setAttribute("rowspan",ffDO.getRowspan());
                valueElem.setAttribute("align",ffDO.getAlign());
                valueElem.setAttribute("valign",ffDO.getValign());
                valueElem.setAttribute("title",tfTitle.getValue().toString());


                G2Field gridField = new G2Field(tfTitle.getValue().toString());

                if (fieldForm != null) {
                    List columnList = fieldForm.getColumnList();
                    deletedField(fieldForm.getIdSequenceMap(),columnList);
                    gridField.setColumnList(columnList);
                    String columnListXml = gridField.getColumnListXml();
                    valueElem.setAttribute("columnListXml",columnListXml);
                }


            }
        }
        else {
            try {

                valueElem = (Element) XPath.selectSingleNode(form,
                            "/form/" + TemplateElement.ELEMENT_NAME + "[@name='" + getTemplateNodeName() + "']");
                valueElem.setAttribute("colspan",ffDO.getColspan());
                valueElem.setAttribute("rowspan",ffDO.getRowspan());
                valueElem.setAttribute("align",ffDO.getAlign());
                valueElem.setAttribute("valign",ffDO.getValign());


            }
            catch (JDOMException e) {
                Logger.getLogger(getClass()).error("Failed to parse XML form.", e);
            }
        }
		return editFormConfig(form, request);
    }

    public void deletedField(Map map, List columnList) {
        G2Column column = null;
        for (Iterator iterator = columnList.iterator(); iterator.hasNext();) {
            column = (G2Column) iterator.next();
            if (map.containsKey(column.getName()))
                map.remove(column.getName());
        }
    }


    public void getLabelElemValue(Element valueElem, FormFieldDataObject ffDO) {
        valueElem.setAttribute("hidden",ffDO.getHiddenFlag());
        valueElem.setAttribute("require",ffDO.getRequiredFlag());
        valueElem.setAttribute("text",rtbLabel.getValue().toString());
        valueElem.setAttribute("colspan",ffDO.getColspan());
        valueElem.setAttribute("rowspan",ffDO.getRowspan());
        valueElem.setAttribute("align",ffDO.getAlign());
        valueElem.setAttribute("valign",ffDO.getValign());
        valueElem.setAttribute("type",FormConstants.FIELD_TEXT_BLOCK_TYPE);

    }

    protected void verifyField() {
        super.verifyField();
        if (fieldForm == null || fieldForm.getColumnListSize() < 1) {
            if (grid != null && grid.getColumnList().size() > 0 ) {
                lbColumnLabel.setText("");
                setInvalid(false);
            }
        }
    }

	public Forward editFormConfig(FormElement form, HttpServletRequest request)  {
			FormConfigDataObject  fcDO = new FormConfigDataObject();
            Map deletedField = null;
            if (fieldForm != null)
                 fieldForm.getIdSequenceMap();
			fcDO.setFormConfigId(UuidGenerator.getInstance().getUuid());
			
			fcDO.setFormId(getFormId());

			try{


				fcDO.setPreviewXml(form.display());
				FormModule handler = (FormModule)Application.getInstance().getModule(FormModule.class);
				handler.editFormConfig(fcDO,form, formUid,deletedField);

				return new Forward("fieldEdited");
			} catch (IOException e) {
				Log.getLog(getClass()).error(e.toString());
				return new Forward("formConfigFailed");
			} catch (FormException e) {
			   Log.getLog(getClass()).error(e.getMessage(),e);
				return new Forward("formConfigFailed");
			}
		}
		

	
    protected void addValidatorNotEmpty(Element btn) {
        btn.addContent(new ValidatorNotEmptyElement());        
    }

    protected void addValidatorIsNumeric(Element btn) {
        btn.addContent(new ValidatorIsNumericElement());
    }

    protected void addValidatorIsInteger(Element btn) {
        btn.addContent(new ValidatorIsIntegerElement());
    }

    protected void addValidatorEmail(Element btn) {
        btn.addContent(new ValidatorEmailElement());
    }

    public String nullToEmpty(String str) {
    	if (str == null)
    		return "";
    	return str;
    }
    
    public Button getSave() {
        return save;
    }



    public String getDefaultTemplate() {
        return "formwizard/editFormField";
    }
    

	/**
	 * @return
	 */
	public Label getFieldTypeLabel() {
		return fieldTypeLabel;
	}


	public void setFieldTypeLabel(Label fieldTypeLabel) {
		this.fieldTypeLabel = fieldTypeLabel;
	}

    public Label getFieldTypeValue() {
        return fieldTypeValue;
    }

    public void setFieldTypeValue(Label fieldTypeValue) {
        this.fieldTypeValue = fieldTypeValue;
    }

    public String getFormUid() {
        return formUid;
    }

    public void setFormUid(String formUid) {
        this.formUid = formUid;
    }

    public Button getBtMoveUp() {
        return btMoveUp;
    }

    public void setBtMoveUp(Button btMoveUp) {
        this.btMoveUp = btMoveUp;
    }

    public Button getBtMoveDown() {
        return btMoveDown;
    }

    public void setBtMoveDown(Button btMoveDown) {
        this.btMoveDown = btMoveDown;
    }

    public Button getBtRemove() {
        return btRemove;
    }

    public void setBtRemove(Button btRemove) {
        this.btRemove = btRemove;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getTemplateNodeName() {
        return templateNodeName;
    }

    public void setTemplateNodeName(String templateNodeName) {
        this.templateNodeName = templateNodeName;
    }
}


