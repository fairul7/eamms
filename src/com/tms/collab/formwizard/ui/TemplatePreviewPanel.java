package com.tms.collab.formwizard.ui;

import org.jdom.output.DOMOutputter;
import org.jdom.JDOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import kacang.runtime.config.WidgetParser;
import kacang.ui.Widget;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.stdui.Button;
import kacang.util.Log;
import kacang.Application;
import com.tms.collab.formwizard.model.*;
import com.tms.collab.formwizard.xmlwidget.FormElement;
import com.tms.collab.formwizard.xmlwidget.TableGridElement;

import java.io.IOException;



public class TemplatePreviewPanel extends PreviewPanel {
    protected String formTemplateId;

    public void initPanel(){
		DOMOutputter domOutput;
		org.w3c.dom.Document doc;
		Node node;
		NodeList nl;
		NamedNodeMap attributes;
		WidgetParser parser;
		Widget[] widgets = null;

		try {
            FormTemplate template = new FormTemplate();
            template.setFormTemplateId(getFormTemplateId());

            form = Util.getTemplateElement(template);



        	close = new Button("close");
        	close.setText(Application.getInstance().getMessage("formWizard.label.previewPanel.close","close"));
        	close.setOnClick("javascript:window.close()");





			parser = new WidgetParser();

        	domOutput = new DOMOutputter();


			doc = domOutput.output(form.getDocument());

			node = doc.getDocumentElement();
            setFormTemplate(doc,node);

            
			nl = node.getChildNodes();

			for (int i = 0; i < nl.getLength(); i++) {
				attributes = nl.item(i).getAttributes();

				if (nl.item(i).getNodeName().equals("label"))
					setLabelWidget(attributes,doc);

                if (nl.item(i).getNodeName().equals(TableGridElement.ELEMENT_NAME))
                    setTableGridWidget(node,nl.item(i),doc);

				if(attributes.getNamedItem("hidden") != null)
					attributes.getNamedItem("hidden").setNodeValue("0");


			}

			widgets = parser.parseConfig(node.getOwnerDocument());

        	addEventListener(this);

        	addChild(widgets[0]);


			//addChild(newField);
        	addChild(close);
		}
		catch (JDOMException e) {
			Log.getLog(getClass()).error("Error parsing template",e);
		}
    }

    public void setTableGridWidget(Node rootNode,Node node, org.w3c.dom.Document document) {
        org.w3c.dom.Element element = document.createElement("label");
        //getParent().getName();
        element.setAttribute("name",node.getAttributes().getNamedItem("name").getNodeValue());
        /*element.setAttribute("text", "<a href=\"frwEditField.jsp?formId=" + getFormId() + "&formUid=" + element.getAttribute("name") + "\" target=\"_\">"
                                      + "Edit ' " +  node.getAttributes().getNamedItem("title").getNodeValue() + "'" + "</a>");*/
        element.setAttribute("text","<a href=\"\" onClick=\"openEditField('" + getFormTemplateId() +"','" + node.getAttributes().getNamedItem("name").getNodeValue() + "'); return false;\"> " + Application.getInstance().getMessage("formWizard.label.previewPanel.edit","Edit") + " '" + node.getAttributes().getNamedItem("title").getNodeValue() + "</a>'");



        element.setAttribute("colspan",node.getAttributes().getNamedItem("colspan").getNodeValue());
        element.setAttribute("rowspan",node.getAttributes().getNamedItem("rowspan").getNodeValue());
        element.setAttribute("align",node.getAttributes().getNamedItem("align").getNodeValue());
        element.setAttribute("valign",node.getAttributes().getNamedItem("valign").getNodeValue());

         //Label label = new Label();

        rootNode.replaceChild(element,node);

    }

    public void setLabelWidget(NamedNodeMap attributes, org.w3c.dom.Document w3cDocument) {
            String formUid = attributes.getNamedItem("name").getNodeValue();
            if (formUid.endsWith("lb")) {
                formUid = formUid.substring(0,formUid.length()-2);
            }


              //set the required field to bold and append with an asterisk
            if ("1".equals(attributes.getNamedItem("require").getNodeValue()))
                attributes.getNamedItem("text").setNodeValue("<b><a href=\"\" onClick=\"openEditField('" + getFormTemplateId() +"','" + formUid +"');return false;\">"
                                                             + attributes.getNamedItem("text").getNodeValue() + "</a></b> *");
            //set the hidden field to bold and italic
            else if ("1".equals(attributes.getNamedItem("hidden").getNodeValue()))
                attributes.getNamedItem("text").setNodeValue("<b><i><a href=\"\" onClick=\"openEditField('" + getFormTemplateId() +"','" + formUid +"');return false;\">"
                                                             + attributes.getNamedItem("text").getNodeValue() + "</a></i></b>");
            //set the normal field to bold
            else if (attributes.getNamedItem("type") == null)
                attributes.getNamedItem("text").setNodeValue("<b><a href=\"\" onClick=\"openEditField('" + getFormTemplateId() +"','" + formUid +"');return false;\">"
                                                             + attributes.getNamedItem("text").getNodeValue() + "</a></b>");
            else
                attributes.getNamedItem("text").setNodeValue("<a href=\"\" onClick=\"openEditField('" + getFormTemplateId() +"','" + formUid +"');return false;\">"
                                                             + attributes.getNamedItem("text").getNodeValue() + "</a>");


    }




     protected Forward move(Event event){
        String childName= event.getRequest().getParameter("childName");
        if(childName!=null){
            int index= childName.lastIndexOf(".");
            if(index<0)
                return null;

            FormElement form = Util.getTemplateElementInstance(getFormTemplateId());
            move(form,childName.substring(index+1),event.getType());
            removeChildren();
            initPanel();
        }
        return null;
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
                module.removeTemplateField(form,childName.substring(index + 1),getFormTemplateId());
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

    public void updateConfig(FormElement rootElem) {
        FormModule handler = (FormModule)Application.getInstance().getModule(FormModule.class);
        FormDao dao = (FormDao)handler.getDao();


        try {
            FormTemplate template = new FormTemplate();
            template.setFormTemplateId(getFormTemplateId());
            template.setTemplateName(String.valueOf(rootElem.getAttributeValue("name")));
            template.setPreviewXml(rootElem.display());

            handler.removeFormMetaData(rootElem);

            template.setFormXml(rootElem.display());

            dao.updateFormTemplate(template);

        }
        catch (IOException e) {
            Log.getLog(getClass()).error("",e);
        }
        catch (FormDaoException e) {
            Log.getLog(getClass()).error(e.getMessage(),e);
        }
    }



    public String getFormTemplateId() {
        return formTemplateId;
    }

    public void setFormTemplateId(String formTemplateId) {
        this.formTemplateId = formTemplateId;
    }
}
