package com.tms.collab.formwizard.model;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.jdom.output.XMLOutputter;
import org.jdom.output.DOMOutputter;
import org.jdom.input.SAXBuilder;
import org.jdom.input.DOMBuilder;
import org.xml.sax.SAXException;


import kacang.Application;
import kacang.util.Log;

import com.tms.collab.formwizard.xmlwidget.FormElement;
import com.tms.collab.formwizard.xmlwidget.TemplateElement;
import com.tms.collab.formwizard.grid.G2Field;
import com.tms.collab.formwizard.grid.G2Column;
import com.tms.util.FormatUtil;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;


public class Util {

    public static String getDate(Date date) {
        SimpleDateFormat outDateFormat = new SimpleDateFormat(FormatUtil.getInstance().getLongDateFormat());
        return outDateFormat.format(date);
    }

	public static FormElement getFormElement(String formID){
			
		FormElement form = null;						
		form = getFormElementInstance(formID);								
		if (form == null) 
			form = getNativeFormElement(formID);	
		return form;
	}
		
	public static FormElement getNativeFormElement(String formId) {
		FormElement form = null;
		try {
			FormModule handler = (FormModule)Application.getInstance().getModule(FormModule.class);

			FormDataObject formsDO = handler.getForm(formId);
		    form = new FormElement(formsDO.getFormName(),formsDO.getFormHeader());

		}
		catch (FormDaoException e) {
			Log.getLog(Util.class).error(e.getMessage(), e);
		} 
		catch (FormException e) {
			Log.getLog(Util.class).error(e.getMessage(), e);
		} 	
		
		
		return form;
	}


    public static FormElement getFormElementInstanceByXml(String xml) {
        SAXBuilder saxBuilder = new SAXBuilder();
        FormElement form = null;
        ByteArrayInputStream baos = null;
        Document doc= null;
        try {

            baos = new  ByteArrayInputStream(xml.getBytes("UTF-8"));
            doc = saxBuilder.build(baos);
            Element e = doc.getRootElement();
		    form = (FormElement)FormElement.newInstance(e);
        }
        catch (JDOMException e) {
            Log.getLog(Util.class).error(e.getMessage(), e);
        }
        catch (IOException e) {
            Log.getLog(Util.class).error(e.getMessage(), e);
        }
        catch (FormException e) {
            Log.getLog(Util.class).error(e.getMessage(), e);
        }
        return form;
    }

	public static FormElement getFormElementInstance(String formID) {
			InputStream xmlForm = null;
			SAXBuilder saxBuilder = new SAXBuilder();
			FormElement form = null;
			
			FormModule handler = (FormModule)Application.getInstance().getModule(FormModule.class);
			FormDao dao =(FormDao)handler.getDao();
			
			try {
				xmlForm = dao.selectFormPreviewXML(formID);
				if (xmlForm != null) {					
					Document doc= saxBuilder.build(xmlForm);
					Element e = doc.getRootElement();							
					form = (FormElement)FormElement.newInstance(e);
				}
			}
			catch (JDOMException e) {
				Log.getLog(Util.class).error(e.getMessage(), e);
			} 
			catch (IOException e) {
				Log.getLog(Util.class).error(e.getMessage(), e);
			}
			catch (FormException e) {

			}
            catch (FormDaoException e) {
                Log.getLog(Util.class).error(e.getMessage(), e);
            }
            finally {
				try {
					if (xmlForm != null)
					xmlForm.close();
				}
				catch (IOException e) {
				}
			}
			
						
			return form;
		}

    public static String getNodeElement(FormElement form, String name) {
		String nodeElement = "";
        FormElement formTemplateElement = null, value = null;
        List templateNodeList = null;
        Element element = null;
        FormTemplate formTemplate = null;
        String templateFieldName = null;
        Map templateListMap = null;
        Set keySet = null;
        String key = null;

		try {

            //get the formwizardtemplate node
            templateListMap = new HashMap();
            templateNodeList = XPath.selectNodes(form,"/form/"+ TemplateElement.ELEMENT_NAME);
            for (Iterator iterator = templateNodeList.iterator(); iterator.hasNext();) {
                element = (Element) iterator.next();
                formTemplate = new FormTemplate();
                formTemplate.setFormTemplateId(element.getAttributeValue("templateId"));
                formTemplateElement = Util.getTemplateElement(formTemplate);
                templateListMap.put(element.getAttributeValue("name"),formTemplateElement);

            }



            element = (Element) XPath.selectSingleNode(form,"/form/*[@name='" + name + "']");

            if (element != null) {
                nodeElement = element.getName();
            }
            else {
                keySet = templateListMap.keySet();
                for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
                    key = (String) iterator.next();
                    if (name.startsWith(key)) {
                        value = (FormElement) templateListMap.get(key);
                        templateFieldName = name.substring(key.length());
                        element = (Element) XPath.selectSingleNode(value,"/form/*[@name='" + templateFieldName + "']");
                        nodeElement = element.getName();
                    }

                }

            }
		}
		catch (JDOMException e) {
			Log.getLog(Util.class).error(e.getMessage(), e);
		}


		return nodeElement;

	}

	public static String getNodeElement(String formId, String name) {
		String nodeElement = "";
        FormElement form = null, formTemplateElement = null, value = null;
        List templateNodeList = null;
        Element element = null;
        FormTemplate formTemplate = null;
        String templateFieldName = null;
        Map templateListMap = null;
        Set keySet = null;
        String key = null;

		try {
            form = Util.getFormElement(formId);

            //get the formwizardtemplate node
            templateListMap = new HashMap();
            templateNodeList = XPath.selectNodes(form,"/form/"+ TemplateElement.ELEMENT_NAME);
            for (Iterator iterator = templateNodeList.iterator(); iterator.hasNext();) {
                element = (Element) iterator.next();
                formTemplate = new FormTemplate();
                formTemplate.setFormTemplateId(element.getAttributeValue("templateId"));
                formTemplateElement = Util.getTemplateElement(formTemplate);
                templateListMap.put(element.getAttributeValue("name"),formTemplateElement);

            }



            element = (Element) XPath.selectSingleNode(form,"/form/*[@name='" + name + "']");

            if (element != null) {
                nodeElement = element.getName();
            }
            else {
                keySet = templateListMap.keySet();
                for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
                    key = (String) iterator.next();
                    if (name.startsWith(key)) {
                        value = (FormElement) templateListMap.get(key);
                        templateFieldName = name.substring(key.length());
                        element = (Element) XPath.selectSingleNode(value,"/form/*[@name='" + templateFieldName + "']");
                        nodeElement = element.getName();
                    }

                }

            }
		}
		catch (JDOMException e) {
			Log.getLog(Util.class).error(e.getMessage(), e);
		} 

			
		return nodeElement;						
		
	}

    public static FormElement getTemplateElement(String formTemplateId) {
        FormTemplate formTemplate = new FormTemplate();
        formTemplate.setFormTemplateId(formTemplateId);
        return getTemplateElement(formTemplate);
    }
    
    public static FormElement getTemplateElement(FormTemplate formTemplate) {
        FormElement form = null;

        form = getTemplateElementInstance(formTemplate.getFormTemplateId());
        if (form == null)
            form = getNativeTemplateElement(formTemplate.getFormTemplateId(),formTemplate.getTableColumn());
        return form;
    }

    public static FormElement getNativeTemplateElement(String templateId, String tableColumn) {
        FormElement form = null;
        String templateName = null;
        try {
            FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);
            Collection formTemplate = module.getFormTemplate(templateId) ;
            for (Iterator iterator = formTemplate.iterator(); iterator.hasNext();) {
                FormTemplate template = (FormTemplate) iterator.next();
                templateName = parseFormName(template.getTemplateName());

            }
            form = new FormElement(templateName,null, Integer.parseInt(tableColumn));

        }
        catch (FormException e) {
            Log.getLog(Util.class).error(e.getMessage(), e);
        }
        catch (FormDaoException e) {
            Log.getLog(Util.class).error(e.getMessage(), e);
        }

        return form;


    }

    public static FormElement getTemplateElementInstance(String templateId) {
	    InputStream xmlForm = null;
		SAXBuilder saxBuilder = new SAXBuilder();
		FormElement form = null;

		FormModule handler = (FormModule)Application.getInstance().getModule(FormModule.class);
		FormDao dao =(FormDao)handler.getDao();

		try {
		    xmlForm = dao.getTemplatePreviewXml(templateId);
			if (xmlForm != null) {
			    Document doc= saxBuilder.build(xmlForm);
				Element e = doc.getRootElement();
				form = (FormElement)FormElement.newInstance(e);
			}
		}
		catch (JDOMException e) {
			Log.getLog(Util.class).error(e.getMessage(), e);
		}
		catch (IOException e) {
			Log.getLog(Util.class).error(e.getMessage(), e);
		}
		catch (FormException e) {
			Log.getLog(Util.class).error(e.getMessage(), e);
		}
        catch (FormDaoException e) {
            	Log.getLog(Util.class).error(e.getMessage(), e);
        }
        finally {
		    try {
			    if (xmlForm != null)
				    xmlForm.close();
				}
				catch (IOException e) {
				}
		}

		return form;
	}

     public static String processFile(String file) {
        StringTokenizer stk ;
        String fileName = "";

        if (file != null && file.trim().length() > 0) {
            stk = new StringTokenizer(file,"/");

            while(stk.hasMoreTokens())
                fileName = stk.nextToken();
        }
        return fileName;

    }

    public static Document buildJDomDocument(InputStream stream) throws FormDocumentException {
        SAXBuilder builder = new SAXBuilder();
        Document jDomDocument = null;
        try {
            if (stream != null)
                jDomDocument = builder.build(stream);
        }
        catch (JDOMException e) {
            throw new FormDocumentException("Error building a JDom Document",e);
        }
        catch (IOException e) {
            throw new FormDocumentException("Error building a JDom Document",e);
        }
        return jDomDocument;
    }

    public static Document buildJDomDocument(String str) throws FormDocumentException {                
        if (str != null)
        try {
            return buildJDomDocument(new ByteArrayInputStream(str.getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e) {
            Log.getLog(Util.class).error("Unsupported Encoding",e);
        }

        return null;
    }



    public static String JDomDocumentToString(Document jDomDocument) throws FormDocumentException {
        XMLOutputter output = new XMLOutputter();
        StringWriter writer = new StringWriter();
        try {
            output.output(jDomDocument, writer);
        }
        catch (IOException e) {
            throw new FormDocumentException("Error printing a JDom Document",e);
        }
        return writer.toString();

    }

    public static String domDocumentToString(org.w3c.dom.Document w3cDocument) throws FormDocumentException {
        DOMSource source = new DOMSource(w3cDocument);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Transformer transformer = tf.newTransformer();
            transformer.transform(source,result);
            return writer.toString();
        }
        catch (TransformerConfigurationException e) {
            throw new FormDocumentException("Error in creating a new Transformer object",e);
        }
        catch (TransformerException e) {
            throw new FormDocumentException("Error in processing the source tree to the output result.",e);
        }
    }

     public static String escapeSingleQuote(String str) {
    	StringBuffer stringBuffer = new StringBuffer();

		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\'')
				stringBuffer.append("\\");

			stringBuffer.append(str.charAt(i));
		}
    	return stringBuffer.toString();
    }

    public static org.w3c.dom.Document buildDOMDocument(String str) throws FormDocumentException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        org.w3c.dom.Document w3cDocument = null;
        try {
            w3cDocument = factory.newDocumentBuilder().parse(str);
        }
        catch (SAXException e) {
            throw new FormDocumentException("Error building a Dom Document",e);
        }
        catch (IOException e) {
            throw new FormDocumentException("Error building a Dom Document",e);
        }
        catch (ParserConfigurationException e) {
            throw new FormDocumentException("Error building a Dom Document",e);
        }
        return w3cDocument;
    }

    public static org.w3c.dom.Document buildDOMDocument(InputStream stream) throws FormDocumentException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        org.w3c.dom.Document w3cDocument = null;
        try {
            w3cDocument = factory.newDocumentBuilder().parse(stream);
        }
        catch (SAXException e) {
            throw new FormDocumentException("Error building a Dom Document",e);
        }
        catch (IOException e) {
            throw new FormDocumentException("Error building a Dom Document",e);
        }
        catch (ParserConfigurationException e) {
            throw new FormDocumentException("Error building a Dom Document",e);
        }
        return w3cDocument;
    }

    //convert w3cDOM to JDOM
    public static Document DOMtoJDOM(org.w3c.dom.Document w3cDocument) {
        DOMBuilder builder = new DOMBuilder();
        Document jDomDocument;

        jDomDocument = builder.build(w3cDocument);

        return jDomDocument;
    }

     //convert JDOM to w3c DOM
    public static org.w3c.dom.Document JDOMtoDOM(Document jDomDocument) throws FormDocumentException {
        DOMOutputter outputter = new DOMOutputter();
        org.w3c.dom.Document w3cDocument = null;
         try {
             w3cDocument = outputter.output(jDomDocument);
         }
         catch (JDOMException e) {
             throw new FormDocumentException("Error converting JDom Document to w3c Document",e);
         }
         return w3cDocument;
    }

    public static String nullToEmpty(String str) {
        if (str == null)
            return "";
        else
            return str;
    }

    public static String nullToEmpty(Object obj) {
        if (obj == null)
            return "";
        else
            return String.valueOf(obj);
    }

    public static String parseFormDataDate(String inDate) throws ParseException {        
        if (inDate != null) {
            SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		    SimpleDateFormat outDateFormat = new SimpleDateFormat("dd MMM yyyy");

            Date date = inDateFormat.parse(inDate);
            return outDateFormat.format(date);

        }

        return "";
    }


    public static String formatG2FieldData(String title, String columnListXml, String value) {
        G2Field g2Field = new G2Field(title);
        g2Field.setColumnListXml(columnListXml);
        g2Field.setValue(value);
        return formatG2FieldData(g2Field);
    }

    public static String formatG2FieldData(G2Field field) {
           List columnList = field.getColumnList();
           List dataList = field.getDataList();
           List list;
           G2Column column;
           StringBuffer buffer = new StringBuffer();
           String value;
           int cnt = 0, no = 1;
           double total = 0;

           buffer.append("<table border=\"1\" bgcolor=\"white\" class=\"contentFont\"><tr>");
           buffer.append("<td>").append(Application.getInstance().getMessage("formWizard.tablegrid.numbering","No")).append("</td>");
           //header
           for (Iterator iterator = columnList.iterator(); iterator.hasNext();) {
               column = (G2Column) iterator.next();
               buffer.append("<td>");
               buffer.append(column.getHeader());
               buffer.append("</td>");
           }
           buffer.append("</tr>");

           //data
           for (Iterator iterator = dataList.iterator(); iterator.hasNext();no++) {
               list = (List)  iterator.next();


               buffer.append("<tr>");
               buffer.append("<td>").append(no).append("</td>");
               for (Iterator iterator1 = list.iterator(); iterator1.hasNext();) {
                   value =  String.valueOf(iterator1.next());
                   buffer.append("<td>");
                   buffer.append(value);
                   buffer.append("</td>");
               }
               buffer.append("</tr>");
           }

           //total
            if (dataList.size() > 0) {
                buffer.append("<tr>");
                buffer.append("<td>&nbsp;</td>");                
                for (Iterator iterator = columnList.iterator(); iterator.hasNext(); cnt++) {
                    //add by micol - reset total to 0 for other column total calculation
                    total=0;

                    column = (G2Column) iterator.next();
                    buffer.append("<td>");
                    if (column.isColumnTotal()) {
                        for (int i = 0; i < dataList.size(); i++) {
                            value = (String) ((List) dataList.get(i)).get(cnt);
                            total += Double.parseDouble(value);
                        }
                        buffer.append(G2Field.df.format(total));
                    }
                    else {
                        buffer.append("&nbsp;");
                    }
                    buffer.append("</td>");
                }
                buffer.append("</tr>");
            }


           buffer.append("</table>");


           return buffer.toString();

       }


     //Convert the form name white space to underscore
	public static String parseFormName(String formName) {
        char[] formNameChar = formName.toCharArray();
        String parsedFormName = "";

        for (int i = 0; i < formNameChar.length; i++) {
            if (formNameChar[i] == ' ')
                parsedFormName += '_';
            else
                parsedFormName += formNameChar[i];
        }

        return parsedFormName;
	}


}

