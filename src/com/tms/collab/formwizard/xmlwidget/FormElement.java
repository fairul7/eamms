package com.tms.collab.formwizard.xmlwidget;


import com.tms.collab.formwizard.model.FormException;
import org.apache.log4j.Logger;
import org.jdom.*;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;


public class FormElement extends Element{
    public static final  String SESSION_ID = "com.tms.collab.formwizard.xmlwidget.FormElement";
    public static final  String ELEMENT_NAME = "form";
    public static final int FORM_VARCHAR_TYPE=1;
    public static final int FORM_NUMERIC_TYPE=2;
    public static final int FORM_CLOB_TYPE=3;
    public static final int FORM_DATETIME_TYPE=4;
    public static final int FORM_PASSWD_TYPE=5;
    public static final int FORM_EMAIL_TYPE=6;
    public static final int FORM_DECIMAL_NUMBER = 7;

    public FormElement(String name, String description) throws FormException {
        this(null, name, description, 2);  // default to a column
    }

    public FormElement(String name, String description, int columns) throws FormException {
        this(null, name, description, columns);  // default to a column
    }

    public FormElement(Document doc, String name, String description, int columns) throws FormException {
        super(ELEMENT_NAME);
        if (isNullOrEmpty(name)) {
            throw new FormException("Name Can not be Null");
        }

        setAttribute("name", name);

        if (!isNullOrEmpty(description)) {
            Element descElem = new Element("description");
            descElem.addContent(new CDATA(description));
            addContent(descElem);
        }

        if (columns < 0) {
            columns = 2;
        }
        setAttribute("columns", Integer.toString(columns));


        if (doc == null) {
            new Document(this);
        } else {
            doc.setRootElement(this);
        }
    }

    public String display() throws IOException {
        XMLOutputter output = new XMLOutputter();
        
        StringWriter writer = new StringWriter();
        output.output(this, writer);
        return writer.toString();
    }

    

    static public Element newInstance(Element e) throws FormException {
        if (!ELEMENT_NAME.equals(e.getName())) {
            return null;
        }

        String elementName = e.getAttribute("name").getValue();
        String elementDesc = null;


        Element descChild = e.getChild("description");
        if (descChild != null) {
            elementDesc = descChild.getText();
        }
        else {
            elementDesc = e.getAttributeValue("description");
        }

        int elementColumn = -1;
        try {
            elementColumn = e.getAttribute("columns").getIntValue();
        }
        catch (DataConversionException e1) {
        }



        FormElement form = new FormElement(null, elementName, elementDesc, elementColumn);
        Iterator childIterator = e.getChildren().iterator();
        while (childIterator.hasNext()) {
            Element childElement = (Element) childIterator.next();
            String childElementName = childElement.getName();
            
            
            if (TextFieldElement.ELEMENT_NAME.equals(childElementName)) {
                form.addContent(TextFieldElement.newInstance(childElement));
            } 
            else if(TextBoxElement.ELEMENT_NAME.equals(childElementName)){
                form.addContent(TextBoxElement.newInstance(childElement));
            }  
            else if(ButtonGroupElement.ELEMENT_NAME.equals(childElementName))  {
                form.addContent(ButtonGroupElement.newInstance(childElement));
            }
            else if(SelectElement.ELEMENT_NAME.equals(childElementName)){
                form.addContent(SelectElement.newInstance(childElement));
            }
            else if(DateFieldElement.ELEMENT_NAME.equals(childElementName)){
                form.addContent(DateFieldElement.newInstance(childElement));
            }
            else if(LabelElement.ELEMENT_NAME.equals(childElementName)){
                form.addContent(LabelElement.newInstance(childElement));
            }
            else if(FileUploadElement.ELEMENT_NAME.equals(childElementName)){
                form.addContent(FileUploadElement.newInstance(childElement));
            }
            else if(TemplateElement.ELEMENT_NAME.equals(childElementName)){
                form.addContent(TemplateElement.newInstance(childElement));
            }
            else if (TableGridElement.ELEMENT_NAME.equals(childElementName)) {
                form.addContent(TableGridElement.newInstance(childElement));
            }

        }
        return form;
    }

    private boolean isNullOrEmpty(String s) {
        return (s == null || s.length() < 1);
    }

   // public Element addContent(Element element) {
     public Parent addContent(Element element) {
    	if (element != null) {
			String name = element.getAttributeValue("name");
					try {
						Element child = (Element) XPath.selectSingleNode(this, "/*/*[@name='"
							+ name + "']");
						if (child != null) {
							return null;
						}
					} catch (JDOMException e) {
						Logger.getLogger(FormElement.class).warn("XPath failed to find element " + name);
						return null;
					}
			return super.addContent(element);
    	}
        return element;
			
    }

}
