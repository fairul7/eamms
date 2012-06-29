package com.tms.collab.formwizard.engine;

import com.tms.collab.formwizard.model.Util;
import com.tms.collab.formwizard.model.FormDocumentException;
import com.tms.collab.formwizard.xmlwidget.*;
import kacang.util.Log;
import kacang.stdui.validator.ValidatorEmail;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.ButtonGroup;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

import java.util.*;
import java.io.*;


public class StructureEngine implements Serializable {
    private InputStream xml;
    private Map data;
    private FormLayout form;


    public StructureEngine(){
    }

    public FormLayout retriveStructure () {
        Document w3cDocument;
        Node rootNode;
        try {
            if (xml != null && xml.available() > 0) {
                w3cDocument = Util.buildDOMDocument(xml);
                rootNode = w3cDocument.getDocumentElement();

                form = new FormLayout();

                if (rootNode.getAttributes().getNamedItem("name") != null)
                    form.setFormName(rootNode.getAttributes().getNamedItem("name").getNodeValue());

                if (rootNode.getAttributes().getNamedItem("columns") != null)
                    form.setColumns(rootNode.getAttributes().getNamedItem("columns").getNodeValue());

                form.setFieldList(parseChildren(rootNode));

                if (data != null)
                    retriveData(data);

            }



        }
        catch (FormDocumentException e) {
            Log.getLog(getClass()).fatal(e.getMessage());
        }
        catch (IOException e) {
            Log.getLog(getClass()).fatal(e.getMessage());
        }

        return form;

    }

    public void retriveData(Map map) {
        Object object;
        Map checkboxMap = new HashMap();
        StringTokenizer stk;
        String token;

        for (Iterator iterator = form.getFieldList().iterator(); iterator.hasNext();) {
            object =  iterator.next();
            if (object instanceof TextFieldField ||
                object instanceof TextBoxField ||
                object instanceof ButtonGroupField ||
                object instanceof SelectBoxField ||
                object instanceof DateFieldField ||
                object instanceof FileField ||
                object instanceof TableGridField ) {
                ((Field) object).setValue(map.get( ((Field) object).getName()));
            }
            else if (object instanceof PanelField) {
                PanelField panel = (PanelField) object;
                if (panel.getFormLayout() != null) {
                    TemplateField template = new TemplateField();
                    template.setTemplateField(panel,map);    
                }

            }

            if (object instanceof ButtonGroupField) {
                if ( ((ButtonGroupField)object).getCheckBoxList() != null) {
					if(((Field) object).getValue() == null)
						stk = new StringTokenizer("", ",");
					else
                    	stk = new StringTokenizer((String) ((Field) object).getValue(),",");
                    while (stk.hasMoreTokens()) {
                        token = stk.nextToken();
                        checkboxMap.put(token, token);
                    }
                    setCheckBoxChecked(((ButtonGroupField)object).getCheckBoxList(), checkboxMap);
                }
                else if (((ButtonGroupField)object).getRadioList() != null) {
                    setRadioChecked(((ButtonGroupField)object).getRadioList(),(String) ((Field) object).getValue());
                }
            }
            else if (object instanceof SelectBoxField) {
                setSelectBoxValue((SelectBoxField)object, (String) ((Field) object).getValue());
            }


        }
    }



    protected List parseChildren(Node rootNode) {
        Node node;
        NodeList nodeList = rootNode.getChildNodes();
        List childList = new ArrayList();


        for (int i=0; i < nodeList.getLength(); i++) {
            node = nodeList.item(i);

            //<label name="_1098346739378lb" text="aaa" escapeXml="false" hidden="0" require="1" colspan="1" rowspan="1" align="left" valign="top" />
            if (LabelElement.ELEMENT_NAME.equals(node.getNodeName())) {
                childList.add(labelElement(node));
            }
            else if (TextFieldElement.ELEMENT_NAME.equals(node.getNodeName())) {
                childList.add(textFieldElement(node));
            }
            else if (TextBoxElement.ELEMENT_NAME.equals(node.getNodeName())) {
               childList.add(textBoxElement(node));
            }
            else if (ButtonGroupElement.ELEMENT_NAME.equals(node.getNodeName())) {
               childList.add(buttonGroupElement(node));
            }
            else if (SelectElement.ELEMENT_NAME.equals(node.getNodeName())) {
                childList.add(selectBoxElement(node));
            }
            else if (DateFieldElement.ELEMENT_NAME.equals(node.getNodeName())) {
                childList.add(dateElement(node));
            }
            else if (FileUploadElement.ELEMENT_NAME.equals(node.getNodeName())) {
                childList.add(fileElement(node));
            }
            else if (TableGridElement.ELEMENT_NAME.equals(node.getNodeName())) {
                childList.add(tableGridElement(node));
            }
            else if ("panel".equals(node.getNodeName())) {
                childList.add(panelElement(node));
            }
            else if ("listener_form".equals(node.getNodeName())) {
                childList.add(listenerFormElement(node));
            }
            else if (TemplateElement.ELEMENT_NAME.equals(node.getNodeName())) {
               childList.add(templateElement(node));
            }
            

        }

        return childList;

    }

    protected void setCheckBoxChecked(List checkboxList, Map map) {
        for (Iterator iterator = checkboxList.iterator(); iterator.hasNext();) {
            CheckBoxField checkBoxField = (CheckBoxField) iterator.next();
            checkBoxField.setChecked(false);
            if (map.containsKey(checkBoxField.getValue()))
                checkBoxField.setChecked(true);
        }

    }

    protected void setRadioChecked(List radioList, String value) {
        for (Iterator iterator = radioList.iterator(); iterator.hasNext();) {
            RadioField radioField = (RadioField) iterator.next();
            radioField.setChecked(false);
            if (radioField.getValue().equals(value))
                radioField.setChecked(true);
        }

    }

    protected void setSelectBoxValue(SelectBoxField selectBoxField, String value) {
        Map map = new HashMap();
        map.put(value,  Boolean.TRUE);
        selectBoxField.setValues(map);
    }

    protected PanelField panelElement(Node node) {
        NodeList nodeList;
        Node childNode;
        NamedNodeMap attributes = node.getAttributes();
        List buttonList = new ArrayList();

        PanelField field = new PanelField();


        if (attributes.getNamedItem("name") != null)
            field.setName(attributes.getNamedItem("name").getNodeValue());

        nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            childNode = nodeList.item(i);
            if ("button".equals(childNode.getNodeName())) {
                buttonList.add(buttonElement(childNode));
            }
        }

        field.setButtonList(buttonList);

        return field;

    }

    protected LabelField labelElement(Node node) {
        NamedNodeMap attributes = node.getAttributes();

        LabelField field = new LabelField();


        if (attributes.getNamedItem("name") != null)
            field.setName(attributes.getNamedItem("name").getNodeValue());

        if (attributes.getNamedItem("text") != null)
            field.setText(attributes.getNamedItem("text").getNodeValue());

        if (attributes.getNamedItem("hidden") != null)
            field.setHidden(attributes.getNamedItem("hidden").getNodeValue());

        if (attributes.getNamedItem("require") != null)
            field.setRequire(attributes.getNamedItem("require").getNodeValue());

        if (attributes.getNamedItem("colspan") != null)
            field.setColspan(attributes.getNamedItem("colspan").getNodeValue());

        if (attributes.getNamedItem("rowspan") != null)
            field.setRowspan(attributes.getNamedItem("rowspan").getNodeValue());

        if (attributes.getNamedItem("align") != null)
            field.setAlign(attributes.getNamedItem("align").getNodeValue());

        if (attributes.getNamedItem("valign") != null)
            field.setValign(attributes.getNamedItem("valign").getNodeValue());

        if (attributes.getNamedItem("escapeXml") != null)
            field.setEscapeXml(attributes.getNamedItem("escapeXml").getNodeValue());

        if (attributes.getNamedItem("type") != null)
            field.setType(attributes.getNamedItem("type").getNodeValue());

        return field;

    }

    protected TextFieldField textFieldElement(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        NodeList nodeList;
        Node childNode;

        TextFieldField field = new TextFieldField();



        if (attributes.getNamedItem("name") != null)
            field.setName(attributes.getNamedItem("name").getNodeValue());

        if (attributes.getNamedItem("hidden") != null)
            field.setHidden(attributes.getNamedItem("hidden").getNodeValue());

        if (attributes.getNamedItem("require") != null)
            field.setRequire(attributes.getNamedItem("require").getNodeValue());

        if (attributes.getNamedItem("colspan") != null)
            field.setColspan(attributes.getNamedItem("colspan").getNodeValue());

        if (attributes.getNamedItem("rowspan") != null)
            field.setRowspan(attributes.getNamedItem("rowspan").getNodeValue());

        if (attributes.getNamedItem("align") != null)
            field.setAlign(attributes.getNamedItem("align").getNodeValue());

        if (attributes.getNamedItem("valign") != null)
            field.setValign(attributes.getNamedItem("valign").getNodeValue());

        if (attributes.getNamedItem("size") != null)
            field.setSize(attributes.getNamedItem("size").getNodeValue());

        if (attributes.getNamedItem("maxLength") != null)
            field.setMaxLength(attributes.getNamedItem("maxLength").getNodeValue());

        if (attributes.getNamedItem("type") != null)
            field.setType(attributes.getNamedItem("type").getNodeValue());

        if (attributes.getNamedItem("value") != null)
            field.setValue(attributes.getNamedItem("value").getNodeValue());

        field.setInvalid(ValidatorField.VALIDATOR_VALID);


        nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            childNode = nodeList.item(i);
            if (ValidatorNotEmptyElement.ELEMENT_NAME.equals(childNode.getNodeName()) ||
                ValidatorNotEmpty.class.getName().equals(childNode.getNodeName())) {
                field.setValidatorNotEmpty(validatorNotEmptyElement(childNode));
            }
            else if (ValidatorIsIntegerElement.ELEMENT_NAME.equals(childNode.getNodeName())) {
                field.setValidatorIsInteger(validatorIsIntegerElement(childNode));
            }
            else if (ValidatorIsNumericElement.ELEMENT_NAME.equals(childNode.getNodeName())) {
                field.setValidatorIsNumeric(validatorIsNumericElement(childNode));
            }
            else if (ValidatorEmailElement.ELEMENT_NAME.equals(childNode.getNodeName()) ||
                     ValidatorEmail.class.getName().equals(childNode.getNodeName())) {
                field.setValidatorEmail(validatorEmailElement(childNode));
            }
        }



        return field;

    }

    protected TextBoxField textBoxElement(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        NodeList nodeList;
        Node childNode;

        TextBoxField field = new TextBoxField();



        if (attributes.getNamedItem("name") != null)
            field.setName(attributes.getNamedItem("name").getNodeValue());

        if (attributes.getNamedItem("hidden") != null)
            field.setHidden(attributes.getNamedItem("hidden").getNodeValue());

        if (attributes.getNamedItem("require") != null)
            field.setRequire(attributes.getNamedItem("require").getNodeValue());

        if (attributes.getNamedItem("colspan") != null)
            field.setColspan(attributes.getNamedItem("colspan").getNodeValue());

        if (attributes.getNamedItem("rowspan") != null)
            field.setRowspan(attributes.getNamedItem("rowspan").getNodeValue());

        if (attributes.getNamedItem("align") != null)
            field.setAlign(attributes.getNamedItem("align").getNodeValue());

        if (attributes.getNamedItem("valign") != null)
            field.setValign(attributes.getNamedItem("valign").getNodeValue());

        if (attributes.getNamedItem("rows") != null)
            field.setRows(attributes.getNamedItem("rows").getNodeValue());

        if (attributes.getNamedItem("cols") != null)
            field.setCols(attributes.getNamedItem("cols").getNodeValue());

        if (attributes.getNamedItem("value") != null)
            field.setValue(attributes.getNamedItem("value").getNodeValue());


        nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            childNode = nodeList.item(i);
            if (ValidatorNotEmptyElement.ELEMENT_NAME.equals(childNode.getNodeName()) ||
                ValidatorNotEmpty.class.getName().equals(childNode.getNodeName())) {
                field.setValidatorNotEmpty(validatorNotEmptyElement(childNode));
            }
        }



        return field;

    }

    protected ButtonGroupField buttonGroupElement(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        NodeList nodeList;
        Node childNode;
        List buttonList = new ArrayList();
        RadioField radioField;
        CheckBoxField checkBoxField;

        ButtonGroupField field = new ButtonGroupField();



        if (attributes.getNamedItem("name") != null)
            field.setName(attributes.getNamedItem("name").getNodeValue());

        if (attributes.getNamedItem("hidden") != null)
            field.setHidden(attributes.getNamedItem("hidden").getNodeValue());

        if (attributes.getNamedItem("require") != null)
            field.setRequire(attributes.getNamedItem("require").getNodeValue());

        if (attributes.getNamedItem("colspan") != null)
            field.setColspan(attributes.getNamedItem("colspan").getNodeValue());

        if (attributes.getNamedItem("rowspan") != null)
            field.setRowspan(attributes.getNamedItem("rowspan").getNodeValue());

        if (attributes.getNamedItem("align") != null)
            field.setAlign(attributes.getNamedItem("align").getNodeValue());

        if (attributes.getNamedItem("valign") != null)
            field.setValign(attributes.getNamedItem("valign").getNodeValue());

        if (attributes.getNamedItem("type") != null)
            field.setType(attributes.getNamedItem("type").getNodeValue());


        nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            childNode = nodeList.item(i);
            if (ValidatorNotEmptyElement.ELEMENT_NAME.equals(childNode.getNodeName()) ||
                ValidatorNotEmpty.class.getName().equals(childNode.getNodeName())) {
                field.setValidatorNotEmpty(validatorNotEmptyElement(childNode));
            }
            else if (ButtonGroupElement.CHECKBOX_ELEMENT_NAME.equals(childNode.getNodeName())) {
                checkBoxField = new CheckBoxField();

                if (childNode.getAttributes().getNamedItem("name") != null)
                    checkBoxField.setName(childNode.getAttributes().getNamedItem("name").getNodeValue());

                if (childNode.getAttributes().getNamedItem("value") != null)
                    checkBoxField.setValue(childNode.getAttributes().getNamedItem("value").getNodeValue());

                if (childNode.getAttributes().getNamedItem("text") != null)
                    checkBoxField.setText(childNode.getAttributes().getNamedItem("text").getNodeValue());

                buttonList.add(checkBoxField);
            }
            else if  (ButtonGroupElement.RADIO_ELEMENT_NAME.equals(childNode.getNodeName())) {
                radioField = new RadioField();

                if (childNode.getAttributes().getNamedItem("name") != null)
                    radioField.setName(childNode.getAttributes().getNamedItem("name").getNodeValue());

                if (childNode.getAttributes().getNamedItem("value") != null)
                    radioField.setValue(childNode.getAttributes().getNamedItem("value").getNodeValue());

                if (childNode.getAttributes().getNamedItem("text") != null)
                    radioField.setText(childNode.getAttributes().getNamedItem("text").getNodeValue());

                if (childNode.getAttributes().getNamedItem("groupName") != null)
                    radioField.setGroupName(childNode.getAttributes().getNamedItem("groupName").getNodeValue());

                buttonList.add(radioField);
            }
        }

        if (field.getType() != null && ButtonGroup.CHECKBOX_TYPE.equals(field.getType()))
            field.setCheckBoxList(buttonList);
        else
            field.setRadioList(buttonList);


        return field;

    }

    protected SelectBoxField selectBoxElement(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        NodeList nodeList;
        Node childNode;

        SelectBoxField field = new SelectBoxField();



        if (attributes.getNamedItem("name") != null)
            field.setName(attributes.getNamedItem("name").getNodeValue());

        if (attributes.getNamedItem("hidden") != null)
            field.setHidden(attributes.getNamedItem("hidden").getNodeValue());

        if (attributes.getNamedItem("require") != null)
            field.setRequire(attributes.getNamedItem("require").getNodeValue());

        if (attributes.getNamedItem("colspan") != null)
            field.setColspan(attributes.getNamedItem("colspan").getNodeValue());

        if (attributes.getNamedItem("rowspan") != null)
            field.setRowspan(attributes.getNamedItem("rowspan").getNodeValue());

        if (attributes.getNamedItem("align") != null)
            field.setAlign(attributes.getNamedItem("align").getNodeValue());

        if (attributes.getNamedItem("valign") != null)
            field.setValign(attributes.getNamedItem("valign").getNodeValue());

        if (attributes.getNamedItem("options") != null)
            field.setOptions(attributes.getNamedItem("options").getNodeValue());


        nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            childNode = nodeList.item(i);
            if (ValidatorNotEmptyElement.ELEMENT_NAME.equals(childNode.getNodeName()) ||
                ValidatorNotEmpty.class.getName().equals(childNode.getNodeName())) {
                field.setValidatorNotEmpty(validatorNotEmptyElement(childNode));
            }
        }



        return field;

    }

    protected DateFieldField dateElement(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        NodeList nodeList;
        Node childNode;

        DateFieldField field = new DateFieldField();



        if (attributes.getNamedItem("name") != null)
            field.setName(attributes.getNamedItem("name").getNodeValue());

        if (attributes.getNamedItem("hidden") != null)
            field.setHidden(attributes.getNamedItem("hidden").getNodeValue());

        if (attributes.getNamedItem("require") != null)
            field.setRequire(attributes.getNamedItem("require").getNodeValue());

        if (attributes.getNamedItem("colspan") != null)
            field.setColspan(attributes.getNamedItem("colspan").getNodeValue());

        if (attributes.getNamedItem("rowspan") != null)
            field.setRowspan(attributes.getNamedItem("rowspan").getNodeValue());

        if (attributes.getNamedItem("align") != null)
            field.setAlign(attributes.getNamedItem("align").getNodeValue());

        if (attributes.getNamedItem("valign") != null)
            field.setValign(attributes.getNamedItem("valign").getNodeValue());



        nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            childNode = nodeList.item(i);
            if (ValidatorNotEmptyElement.ELEMENT_NAME.equals(childNode.getNodeName()) ||
                ValidatorNotEmpty.class.getName().equals(childNode.getNodeName())) {
                field.setValidatorNotEmpty(validatorNotEmptyElement(childNode));
            }
        }



        return field;

    }

    protected FileField fileElement(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        NodeList nodeList;
        Node childNode;

        FileField field = new FileField();



        if (attributes.getNamedItem("name") != null)
            field.setName(attributes.getNamedItem("name").getNodeValue());

        if (attributes.getNamedItem("hidden") != null)
            field.setHidden(attributes.getNamedItem("hidden").getNodeValue());

        if (attributes.getNamedItem("require") != null)
            field.setRequire(attributes.getNamedItem("require").getNodeValue());

        if (attributes.getNamedItem("colspan") != null)
            field.setColspan(attributes.getNamedItem("colspan").getNodeValue());

        if (attributes.getNamedItem("rowspan") != null)
            field.setRowspan(attributes.getNamedItem("rowspan").getNodeValue());

        if (attributes.getNamedItem("align") != null)
            field.setAlign(attributes.getNamedItem("align").getNodeValue());

        if (attributes.getNamedItem("valign") != null)
            field.setValign(attributes.getNamedItem("valign").getNodeValue());



        nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            childNode = nodeList.item(i);
            if (ValidatorNotEmptyElement.ELEMENT_NAME.equals(childNode.getNodeName()) ||
                ValidatorNotEmpty.class.getName().equals(childNode.getNodeName())) {
                field.setValidatorNotEmpty(validatorNotEmptyElement(childNode));
            }
        }



        return field;

    }

    protected TableGridField tableGridElement(Node node) {
        NamedNodeMap attributes = node.getAttributes();

        TableGridField field = new TableGridField();



        if (attributes.getNamedItem("name") != null)
            field.setName(attributes.getNamedItem("name").getNodeValue());

        if (attributes.getNamedItem("hidden") != null)
            field.setHidden(attributes.getNamedItem("hidden").getNodeValue());

        if (attributes.getNamedItem("require") != null)
            field.setRequire(attributes.getNamedItem("require").getNodeValue());

        if (attributes.getNamedItem("colspan") != null)
            field.setColspan(attributes.getNamedItem("colspan").getNodeValue());

        if (attributes.getNamedItem("rowspan") != null)
            field.setRowspan(attributes.getNamedItem("rowspan").getNodeValue());

        if (attributes.getNamedItem("align") != null)
            field.setAlign(attributes.getNamedItem("align").getNodeValue());

        if (attributes.getNamedItem("valign") != null)
            field.setValign(attributes.getNamedItem("valign").getNodeValue());

        if (attributes.getNamedItem("title") != null)
            field.setTitle(attributes.getNamedItem("title").getNodeValue());

        if (attributes.getNamedItem("columnListXml") != null)
            field.setColumnListXml(attributes.getNamedItem("columnListXml").getNodeValue());



        return field;

    }

    protected ButtonField buttonElement(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        ButtonField field = new ButtonField();

        if (attributes.getNamedItem("name") != null)
            field.setName(attributes.getNamedItem("name").getNodeValue());

        if (attributes.getNamedItem("text") != null)
            field.setText(attributes.getNamedItem("text").getNodeValue());


        return field;

    }

    protected ListenerForm listenerFormElement(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        ListenerForm listerner = new ListenerForm();

        if (attributes.getNamedItem("class") != null)
            listerner.setClassName(attributes.getNamedItem("class").getNodeValue());

        return listerner;

    }

    protected ValidatorNotEmptyField validatorNotEmptyElement(Node node) {
        ValidatorNotEmptyField validatorNotEmpty;
        NamedNodeMap attributes = node.getAttributes();


        validatorNotEmpty = new ValidatorNotEmptyField();

        if (attributes.getNamedItem("name") != null)
            validatorNotEmpty.setName(attributes.getNamedItem("name").getNodeValue());

        if (attributes.getNamedItem("text") != null)
            validatorNotEmpty.setText(attributes.getNamedItem("text").getNodeValue());

        return validatorNotEmpty;
    }

    protected ValidatorIsIntegerField validatorIsIntegerElement(Node node) {
        ValidatorIsIntegerField validatorIsInteger ;
        NamedNodeMap attributes = node.getAttributes();


        validatorIsInteger = new ValidatorIsIntegerField();

        if (attributes.getNamedItem("name") != null)
            validatorIsInteger.setName(attributes.getNamedItem("name").getNodeValue());

        if (attributes.getNamedItem("text") != null)
            validatorIsInteger.setText(attributes.getNamedItem("text").getNodeValue());

        if (attributes.getNamedItem("optional") != null) {
            validatorIsInteger.setOptional(attributes.getNamedItem("optional").getNodeValue());
        }

        return validatorIsInteger;
    }

    protected ValidatorIsNumericField validatorIsNumericElement(Node node) {
        ValidatorIsNumericField validatorIsNumeric;
        NamedNodeMap attributes = node.getAttributes();


        validatorIsNumeric = new ValidatorIsNumericField();

        if (attributes.getNamedItem("name") != null)
            validatorIsNumeric.setName(attributes.getNamedItem("name").getNodeValue());

        if (attributes.getNamedItem("text") != null)
            validatorIsNumeric.setText(attributes.getNamedItem("text").getNodeValue());

        if (attributes.getNamedItem("optional") != null)
            validatorIsNumeric.setOptional(attributes.getNamedItem("optional").getNodeValue());

        return validatorIsNumeric;
    }

    protected ValidatorEmailField validatorEmailElement(Node node) {
        ValidatorEmailField validatorEmail;
        NamedNodeMap attributes = node.getAttributes();


        validatorEmail = new ValidatorEmailField();

        if (attributes.getNamedItem("name") != null)
            validatorEmail.setName(attributes.getNamedItem("name").getNodeValue());

        if (attributes.getNamedItem("text") != null)
            validatorEmail.setText(attributes.getNamedItem("text").getNodeValue());

        return validatorEmail;
    }

    protected PanelField templateElement(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        TemplateField template = new TemplateField();
        template.setTemplateId(attributes.getNamedItem("templateId").getNodeValue());

        PanelField panelField = new PanelField();
        panelField.setName(node.getAttributes().getNamedItem("name").getNodeValue());

        if (attributes.getNamedItem("colspan") != null)
            panelField.setColspan(attributes.getNamedItem("colspan").getNodeValue());

        if (attributes.getNamedItem("rowspan") != null)
            panelField.setRowspan(attributes.getNamedItem("rowspan").getNodeValue());

        if (attributes.getNamedItem("align") != null)
            panelField.setAlign(attributes.getNamedItem("align").getNodeValue());

        if (attributes.getNamedItem("valign") != null)
            panelField.setValign(attributes.getNamedItem("valign").getNodeValue());



        panelField.setFormLayout(template.getTemplateField());
        return panelField;
    }

    public void setXml(InputStream xml) {
        this.xml = xml;
    }

    public void setXml(String xml) {

        try {
            setXml(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e) {
            Log.getLog(getClass()).info("Unsupported Encoding");
        }
    }


    public void setData(Map data) {
        this.data = data;
    }
}




